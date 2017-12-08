<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	Ext.ns("Ext.market.depositorder"); // 自定义一个命名空间
	depositorder = Ext.market.depositorder; // 定义命名空间的别名
	depositorder = {
		all : '/manage/depositorder/queryDepositorder.do', // 所有存款订单
		save : "/manage/depositorder/save.do",// 保存存款订单
		del : "/manage/depositorder/del/",// 删除存款订单
		auditKf : "/manage/depositorder/auditKf/",// 核实存款订单
		auditCw : "/manage/depositorder/auditCw/",// 审核存款订单
		down:'/manage/depositorder/toDownloadReport.do',//导出存款订单报表
		pageSize : 30,// 每页显示的记录数
		isKF : eval('${isKF}'),//是否为客服：0否，1是
		isCW : eval('${isCW}'),//是否为账务：0否，1是
		PAYMETHODSMAP : eval('(${paymethodsMap})'),//注意括号
		CCNOMAP : eval('(${ccnoMap})'),//注意括号
		yhcode : eval('(${yhcode})'),//注意括号
		STATUSMAP : eval('(${statusMap})')//注意括号
	};
	/** 改变页的combo*/
	depositorder.pageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				depositorder.pageSize  = parseInt(comboBox.getValue());
				depositorder.bbar.pageSize  = parseInt(comboBox.getValue());
				depositorder.store.baseParams.limit = depositorder.pageSize;
				depositorder.store.baseParams.start = 0;
				depositorder.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	depositorder.pageSize = parseInt(depositorder.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	depositorder.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:true,
		baseParams : {
			start : 0,
			limit : depositorder.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : depositorder.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'poid','platformorders','uiid','paytyple','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','ordercontent','createDate','updateDate','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','cwid','cwname','cwopttime','beforebalance','laterbalance','hdnumber','ordernumber','proxyname']),
		listeners : {
			'load' : function(store, records, options) {
				depositorder.amountsum();
				depositorder.alwaysFun();
			}
		}
	});
	//小计
	depositorder.amountsum = function(){
		var p = new Ext.data.Record({fields:[ 'poid','platformorders','uiid','paytyple','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','ordercontent','createDate','updateDate','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','cwid','cwname','cwopttime','beforebalance','laterbalance','hdnumber','ordernumber','proxyname']});
		var amounttotal= 0;
		depositorder.store.each(function(record){
			var amounts = record.data.amount;
			if(amounts != null){//存款金额
				amounttotal += Number(amounts);
			}
		});
		p.set('poid','小计：');
		p.set('hdnumber','--');
		p.set('amount',amounttotal);
		depositorder.store.add(p);
	}
	//depositorder.store.load(); 
	/** 基本信息-选择模式 */
	depositorder.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				depositorder.editAction.disable();
				depositorder.deleteAction.disable();
				depositorder.auditCwAction.disable();
				depositorder.auditKfAction.disable();
				
				if(1==depositorder.isKF || 1==depositorder.isCW){
					depositorder.addAction.enable();
					depositorder.deleteAction.enable();
					depositorder.editAction.enable();
					depositorder.auditKfAction.enable();
				}else{
					depositorder.addAction.disable();
				}
				if(1==depositorder.isKF || 1==depositorder.isCW){
					depositorder.auditCwAction.enable();
				}
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				if(1==depositorder.isKF || 1==depositorder.isCW){
					depositorder.addAction.enable();
				}else{
					depositorder.addAction.disable();
				}
				depositorder.deleteAction.disable();
				depositorder.editAction.disable();
				depositorder.auditCwAction.disable();
				depositorder.auditKfAction.disable();
			}
		}
	});
	/** 基本信息-数据列 */
	depositorder.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 110
		},
		// 'poid','platformorders','uiid','paytyple','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','ordercontent','createDate','updateDate','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','cwid','cwname','cwopttime'  
		columns : [ depositorder.selModel, {
			header : '存款订单ID',
			width : 150,
			dataIndex : 'poid',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			hidden : true,
			header : '操作人ID',
			dataIndex : 'createuserid'
		}, {
			hidden : true,
			header : '用户ID',
			dataIndex : 'uiid'
		}, {
			hidden : true,
			header : '收支类型',
			dataIndex : 'paytyple'
		}, {
			hidden : true,
			header : '第三方支付平台ID',
			dataIndex : 'ppid'
		}, {
			hidden : true,
			header : '流水号',
			dataIndex : 'platformorders'
		},  {
			header : '当前状态',
			dataIndex : 'status',
			width : 60,
			renderer : function(v) {
				if(v==4){
					return '<span style="color:#FF0000;">失败</span>';
				}else if(v==1){
					return '<span style="color:#FF9900;font-size:14px;font-weight:bold;">发起</span>';
				}else{
					return Share.map(v, depositorder.STATUSMAP, '');
				}
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '会员账号',
			dataIndex : 'uaccount',
			width : 80,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '所属代理',
			dataIndex : 'proxyname',
			width : 80,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},  {
			header : '订单号',
			dataIndex : 'ordernumber',
			width : 80,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '优惠',
			dataIndex : 'hdnumber',
			width:100,
			renderer : function(v,metadata,record) {
				if(v != null && v !=''){
					if(v =='3'){
						return '<span style="color:#438eb9;font-weight:bold;">首存优惠100%</span>';
					}else if(v =='1'){
						return '<span style="color:#438eb9;font-weight:bold;">存100送88元</span>';
					}else if(v =='2'){
						return '<span style="color:#438eb9;font-weight:bold;">存50送58元</span>';
					}else if(v =='5'){
						return '<span style="color:#438eb9;font-weight:bold;">次次存送20%</span>';
					}else if(v =='6'){
						return '<span style="color:#438eb9;font-weight:bold;">次次存送10%</span>';
					}else if(v =='7'){
						return '<span style="color:#438eb9;font-weight:bold;">每日首存送60%</span>';
					}else if(v =='8'){
						return '<span style="color:#438eb9;font-weight:bold;">存200送50元</span>';
					}else if(v =='9'){
						return '<span style="color:#438eb9;font-weight:bold;">送500送100元</span>';
					}else if(v =='11'){
						return '<span style="color:#438eb9;font-weight:bold;">百家乐存送10%</span>';
					}else if(v =='12'){
						return '<span style="color:#438eb9;font-weight:bold;">百家乐存送20%</span>';
					}else if(v =='13'){
						return '<span style="color:#438eb9;font-weight:bold;">百家乐存送33%</span>';
					}else if(v =='14'){
						return '<span style="color:#438eb9;font-weight:bold;">BBIN电游存送100%</span>';
					}else if(v =='17'){
						return '<span style="color:#438eb9;font-weight:bold;">每日次存50%</span>';
					}
				}else{
					return '<span style="color:#393939;">未参加</span>';
				}
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			hidden : false,
			header : '会员姓名',
			width : 60,
			dataIndex : 'urealname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '银行名称',
			dataIndex : 'bankname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '存入银行卡',
			width : 150,
			dataIndex : 'bankcard',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '开户行名称',
			dataIndex : 'deposit',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '开户人',
			dataIndex : 'openname',
			width : 60,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '存款金额',
			width : 60,
			dataIndex : 'amount',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '存款方式',
			dataIndex : 'paymethods',
			width : 60,
			renderer : function(v) {
				return Share.map(v, depositorder.PAYMETHODSMAP, '');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '存款时间',
			dataIndex : 'deposittime',
			width : 130,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '客服备注',
			dataIndex : 'kfremarks',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '操作客服',
			dataIndex : 'kfname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '客服操作时间',
			dataIndex : 'kfopttime',
			width : 130,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '财务备注',
			dataIndex : 'cwremarks',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '操作财务',
			dataIndex : 'cwname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '财务操作时间',
			dataIndex : 'cwopttime',
			width : 130,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '存款前余额',
			dataIndex : 'beforebalance',
			width : 150,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '存款后余额',
			dataIndex : 'laterbalance',
			width : 150,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		} ]
	});
	/** 新建 */
	depositorder.addAction = new Ext.Action({
		text : '新建',
		//text : '<fmt:message key="common.cancel"/>',
		disabled : true,
		iconCls : 'Add',
		handler : function() {
			depositorder.addWindow.setIconClass('Applicationadd'); // 设置窗口的样式
			depositorder.addWindow.setTitle('新建存款订单'); // 设置窗口的名称
			depositorder.addWindow.show().center(); // 显示窗口
			depositorder.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			$("#poid").parent().parent().hide();
			depositorder.ccnoCombo.enable();
		}
	});
	/** 编辑 */
	depositorder.editAction = new Ext.Action({
		text : '编辑',
		iconCls : 'depositorder_edit',
		disabled : true,
		handler : function() {
			var record = depositorder.grid.getSelectionModel().getSelected();
			if(4!=record.data.status){
				Ext.Msg.alert('提示','只能编辑状态为“存款失败”的存款订单！');
				return;
			}
			depositorder.addWindow.setIconClass('depositorder_edit'); // 设置窗口的样式
			depositorder.addWindow.setTitle('编辑'); // 设置窗口的名称
			depositorder.addWindow.show().center();
			depositorder.formPanel.getForm().reset();
			depositorder.formPanel.getForm().loadRecord(record);
			var d = new Date();
			d.setTime(record.data.deposittime);
			$("#deposittimeStr").val(Ext.util.Format.date(d, "Y-m-d H:i:s"));
			$("#poid").parent().parent().show();
			depositorder.ccnoCombo.disable();
		}
	});
	/** 客服核实 */
	depositorder.auditKfAction = new Ext.Action({
		text : '客服核实',
		iconCls : 'Applicationedit',
		disabled : true,
		handler : function() {
			var record = depositorder.grid.getSelectionModel().getSelected();
			if(2!=record.data.status){
				Ext.Msg.alert('提示','只能审核状态为“待客服核实”的存款订单！');
				return;
			}
			depositorder.auditKfWindow.setIconClass('Applicationedit'); // 设置窗口的样式
			depositorder.auditKfWindow.setTitle('客服核实'); // 设置窗口的名称
			depositorder.auditKfWindow.show().center();
			depositorder.auditKfFormPanel.getForm().reset();
			depositorder.auditKfFormPanel.getForm().loadRecord(record);
		}
	});
	/** 财务审核 */
	depositorder.auditCwAction = new Ext.Action({
		text : '财务审核',
		iconCls : 'Applicationedit',
		disabled : true,
		handler : function() {
			var record = depositorder.grid.getSelectionModel().getSelected();
			if(1!=record.data.status){
				Ext.Msg.alert('提示','只能审核状态为“待财务审核”的存款订单！');
				return;
			}
			depositorder.auditCwWindow.setIconClass('Applicationedit'); // 设置窗口的样式
			depositorder.auditCwWindow.setTitle('财务审核'); // 设置窗口的名称
			depositorder.auditCwWindow.show().center();
			depositorder.auditCwFormPanel.getForm().reset();
			depositorder.auditCwFormPanel.getForm().loadRecord(record);
		}
	});
	/** 删除 */
	depositorder.deleteAction = new Ext.Action({
		text : '删除',
		iconCls : 'Cross',
		disabled : true,
		handler : function() {
			depositorder.delFun();
		}
	});
	/** 查询按钮 */
	depositorder.searchAction = new Ext.Action({
		text : '查询',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				depositorder.searchFun();
			}
	});
	depositorder.searchParams = function(){
		var obj = {};
		obj.uaccount=$("#depositorderuaccount").val(); //会员账号
		obj.startDate=$("#depositorderstartDate").val(); //开始时间 
		obj.endDate=$("#depositorderendDate").val(); //结束时间
		obj.status=$("#depositorderorderstatus").prev().val();//订单状态
	    return obj;
	}
	
	depositorder.searchFun = function(){
		depositorder.store.load({params: depositorder.searchParams()});
	}
	
	depositorder.store.on('beforeload',function(store, options){
	    depositorder.store.baseParams = depositorder.searchParams();
	});
	
	
		/** 导出报表按钮 */
	depositorder.downAction = new Ext.Action({
			text : '导出报表',
			iconCls : 'Diskdownload',
			disabled : false,
			handler : function() {
				depositorder.downReport();
			}
	});
	/** 导出报表函数 */
	depositorder.downReport = function() {
		 //发送请求
		 window.open(depositorder.down + "?uaccount="+$("#depositorderuaccount").val()+"&startDate="+$("#depositorderstartDate").val()+"&endDate="+$("#depositorderendDate").val()+"&status="+$("#depositorderorderstatus").prev().val());
	};
	/**日期条件  -- 开始时间*/
 	depositorder.startDateField = new Ext.form.DateField({
  			id:'depositorderstartDate',
        	showToday:true,
        	format:'Y-m-d',
        	invalidText:'日期输入非法',
        	allowBlank : true,
        	width:150 
 	});
 	/** 日期条件  -- 结束时间*/
 	depositorder.endDateField = new Ext.form.DateField({
  			id:'depositorderendDate',
        	showToday:true,
        	format:'Y-m-d',
        	invalidText:'日期输入非法',
        	allowBlank : true,
        	width:150 
 	});
 	/** 处理状态*/
	depositorder.orderstatusAction = new Ext.form.ComboBox({
		hiddenName : 'depositorderorderstatus',
		id : 'depositorderorderstatus',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(depositorder.STATUSMAP)
				}),
		valueField : 'v',
		displayField : 't',
		value:'',
		allowBlank : false,
		editable : false,
		width:100
	});
	/** 顶部工具栏 */
	depositorder.tbar = [ depositorder.addAction, 
	                      '-', depositorder.deleteAction, 
	                      '-', depositorder.auditCwAction,
	                     /** '-',depositorder.downAction,*/
	                      '-','&nbsp;','会员账号:',{id:'depositorderuaccount',xtype:'textfield',width:100},
	                      '-','&nbsp;','存款时间:',depositorder.startDateField,'~',depositorder.endDateField,
	                      '-','&nbsp;','处理状态:',depositorder.orderstatusAction,
	                      '-','&nbsp;',depositorder.searchAction
	                ];
	/** 底部工具条 */
	depositorder.bbar = new Ext.PagingToolbar({
		pageSize : depositorder.pageSize,
		store : depositorder.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', depositorder.pageSizeCombo ]
	});
	/** 基本信息-表格 */
	depositorder.grid = new Ext.grid.EditorGridPanel({
		store : depositorder.store,
		colModel : depositorder.colModel,
		selModel : depositorder.selModel,
		tbar : depositorder.tbar,
		bbar : depositorder.bbar,
		autoScroll : 'auto',
		region : 'center',
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig:{},
		stripeRows : true
	});
	depositorder.statusCombo = new Ext.form.ComboBox({
		fieldLabel : '状态',
		hiddenName : 'status',
		name : 'status',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
			fields : ['v', 't'],
			data : Share.map2Ary(depositorder.STATUSMAP)
		}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		value : "0",
		anchor : '99%'
	});
	
	depositorder.paymethodsCombo = new Ext.form.ComboBox({
		fieldLabel : '存款方式',
		hiddenName : 'paymethods',
		name : 'paymethods',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
			fields : ['v', 't'],
			data : Share.map2Ary(depositorder.PAYMETHODSMAP)
		}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		anchor : '99%'
	});
	depositorder.ccnoCombo = new Ext.form.ComboBox({
		fieldLabel : '公司收款银行卡',
		hiddenName : 'ccno',
		name : 'ccno',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
			fields : ['v', 't'],
			data : Share.map2Ary(depositorder.CCNOMAP)
		}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		anchor : '99%',
		listeners : {
			 'select': function(){
                 if(""!=depositorder.ccnoCombo.getValue()){
                	 var arr = depositorder.ccnoCombo.getValue().split("::");
                	 $("#bankname").val(arr[1]);
                	 $("#bankcard").val(arr[0]);
                	 $("#deposit").val(arr[2]);
                	 $("#openname").val(arr[3]);
                 }
             }
		}
	});
	
	depositorder.paymethodsReadOnlyCombo = new Ext.form.ComboBox({
		fieldLabel : '存款方式',
		hiddenName : 'paymethods',
		name : 'paymethods',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
			fields : ['v', 't'],
			data : Share.map2Ary(depositorder.PAYMETHODSMAP)
		}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		readOnly : true,
		anchor : '99%'
	});
	
	depositorder.hdnumberCombo = new Ext.form.ComboBox({
		fieldLabel : '优惠代码',
		hiddenName : 'hdnumber',
		name : 'hdnumber',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
			fields : ['v', 't'],
			data : Share.map2Ary(depositorder.yhcode)
		}),
		valueField : 'v',
		displayField : 't',
		//allowBlank : false,
		editable : false,
		anchor : '99%'
	});
	
	/** 基本信息-详细信息的form */
	depositorder.formPanel = new Ext.form.FormPanel({
		autoScroll : false,
		border: false,
        style: 'border-bottom:0px;',
        bodyStyle: 'padding:10px;background-color:transparent;',
		labelwidth : 70,
		defaultType : 'textfield',
		// 'poid','platformorders','uiid','paytyple','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','ordercontent','createDate','updateDate','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','cwid','cwname','cwopttime'
		items : [ {
			xtype : 'hidden',
			fieldLabel : '流水号',
			name : 'platformorders'
		}, {
			xtype : 'hidden',
			fieldLabel : '收支类型',
			name : 'paytyple'
		}, {
			readOnly : true,
			fieldLabel : '存款订单ID',
			id : 'poid',
			name : 'poid'
		}, {
			fieldLabel : '存款会员账号',
			allowBlank : false,
			anchor : '99%',
			name : 'uaccount'
//		}, {
//			fieldLabel : '存款会员名称',
//			allowBlank : false,
//			name : 'urealname'
		}, depositorder.paymethodsCombo, depositorder.ccnoCombo,
		{
			readOnly : true,
			fieldLabel : '银行卡卡号',
			allowBlank : false,
			id : 'bankcard',
			anchor : '99%',
			name : 'bankcard'
		}, {
			readOnly : true,
			fieldLabel : '银行名称',
			id : 'bankname',
			anchor : '99%',
			name : 'bankname'
		}, {
			readOnly : true,
			fieldLabel : '开户行名称',
			id : 'deposit',
			anchor : '99%',
			name : 'deposit'
		}, {
			readOnly : true,
			fieldLabel : '开户人',
			anchor : '99%',
			id : 'openname',
			name : 'openname'
		}, depositorder.hdnumberCombo, {
			fieldLabel : '存款金额',
			allowBlank : false,
			anchor : '99%',
			name : 'amount'
		}, {
			fieldLabel : '存款时间',
			allowBlank : false,
			anchor : '99%',
			id : 'deposittimenew',
			name : 'deposittimenew',
			xtype:'datetimefield'
		}, {
			fieldLabel : '客服备注',
			allowBlank : false,
			maxLength : 200,
			xtype : 'textarea',
			name : 'kfremarks',
			anchor : '99%'
		}
		]
	});
	/** 编辑新建窗口 */
	depositorder.addWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 420,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ depositorder.formPanel ],
		buttons : [ {
			text : '保存',
			handler : function() {
				depositorder.saveFun();
			}
		}, {
			text : '重置',
			handler : function() {
				var form = depositorder.formPanel.getForm();
				var poid = form.findField("poid").getValue();
				var uaccount = form.findField("uaccount").getValue();
				form.reset();
				if (poid != '')
					form.findField("poid").setValue(poid);
				if (uaccount != '')
					form.findField("uaccount").setValue(uaccount);
			}
		} ]
	});
	/** 客服核实表单 */
	depositorder.auditKfFormPanel = new Ext.form.FormPanel({
		autoScroll : true,
		frame : false,
		title : '存款订单信息',
		bodyStyle : 'padding:10px;border:0px',
		labelwidth : 70,
		defaultType : 'textfield',
		// 'poid','platformorders','uiid','paytyple','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','ordercontent','createDate','updateDate','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','cwid','cwname','cwopttime'
		items : [ {
			xtype : 'hidden',
			fieldLabel : '收支类型',
			name : 'paytyple'
		}, {
			xtype : 'hidden',
			fieldLabel : '流水号',
			name : 'platformorders'
		}, {
			readOnly : true,
			allowBlank : false,
			fieldLabel : '存款订单ID',
			id : 'poidKf',
			name : 'poid'
		}, {
			readOnly : true,
			allowBlank : false,
			fieldLabel : '会员账号',
			name : 'uaccount'
//		}, {
//			readOnly : true,
//			allowBlank : false,
//			fieldLabel : '会员名称',
//			name : 'urealname'
		}, depositorder.paymethodsReadOnlyCombo, 
		{
			readOnly : true,
			allowBlank : false,
			fieldLabel : '银行卡卡号',
			name : 'bankcard'
		}, {
			readOnly : true,
			fieldLabel : '银行名称',
			allowBlank : false,
			name : 'bankname'
		}, {
			readOnly : true,
			fieldLabel : '开户行名称',
			allowBlank : false,
			name : 'deposit'
		}, {
			readOnly : true,
			fieldLabel : '开户人',
			allowBlank : false,
			name : 'openname'
		}, {
			readOnly : true,
			allowBlank : false,
			fieldLabel : '存款金额',
			name : 'amount'
		}, {
			readOnly : true,
			allowBlank : false,
			fieldLabel : '存款时间',
			id : 'deposittimeStrKf',
			name : 'deposittimeStr',
			xtype:'datetimefield'
		}, {
			allowBlank : false,
			fieldLabel : '客服备注',
			maxLength : 200,
			xtype : 'textarea',
			name : 'kfremarks',
			anchor : '99%'
		}, {
			readOnly : true,
			allowBlank : false,
			fieldLabel : '财务备注',
			maxLength : 200,
			xtype : 'textarea',
			name : 'cwremarks',
			anchor : '99%'
		}
		]
	});
	/** 客服核实窗口 */
	depositorder.auditKfWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 460,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ depositorder.auditKfFormPanel ],
		buttons : [ {
			text : '核实通过',
			handler : function() {
				depositorder.auditKfFun(2);
			}
		}, {
			text : '取消',
			handler : function() {
				depositorder.auditKfWindow.hide();
			}
		} ]
	});
	/** 财务审核表单 */
	depositorder.auditCwFormPanel = new Ext.form.FormPanel({
		autoScroll : false,
		border: false,
        style: 'border-bottom:0px;',
        bodyStyle: 'padding:10px;background-color:transparent;',
		labelwidth : 70,
		defaultType : 'textfield',
		// 'poid','platformorders','uiid','paytyple','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','ordercontent','createDate','updateDate','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','cwid','cwname','cwopttime'
		items : [ {
			xtype : 'hidden',
			fieldLabel : '收支类型',
			name : 'paytyple'
		}, {
			xtype : 'hidden',
			fieldLabel : '流水号',
			name : 'platformorders'
		}, {
			readOnly : true,
			allowBlank : false,
			fieldLabel : '存款订单ID',
			anchor : '99%',
			name : 'poid'
		}, {
			readOnly : true,
			fieldLabel : '会员账号',
			anchor : '99%',
			name : 'uaccount'
//		}, {
//			readOnly : true,
//			allowBlank : false,
//			fieldLabel : '会员名称',
//			name : 'urealname'
		}, depositorder.paymethodsReadOnlyCombo, 
		{
			readOnly : true,
			allowBlank : false,
			fieldLabel : '银行卡卡号',
			anchor : '99%',
			name : 'bankcard'
		}, {
			readOnly : true,
			fieldLabel : '银行名称',
			anchor : '99%',
			name : 'bankname'
		}, {
			readOnly : true,
			fieldLabel : '开户行名称',
			anchor : '99%',
			name : 'deposit'
		}, {
			readOnly : true,
			fieldLabel : '开户人',
			anchor : '99%',
			name : 'openname'
		}, {
			readOnly : true,
			allowBlank : false,
			anchor : '99%',
			fieldLabel : '存款金额',
			name : 'amount'
		}, {
			readOnly : true,
			allowBlank : false,
			fieldLabel : '存款时间',
			anchor : '99%',
			id : 'deposittime',
			name : 'deposittime'
		}, {
			readOnly : true,
			fieldLabel : '客服备注',
			maxLength : 200,
			xtype : 'textarea',
			name : 'kfremarks',
			anchor : '99%'
		}, {
			allowBlank : false,
			fieldLabel : '财务备注',
			maxLength : 200,
			xtype : 'textarea',
			name : 'cwremarks',
			anchor : '99%'
		}
		]
	});
	/** 财务审核窗口 */
	depositorder.auditCwWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 480,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ depositorder.auditCwFormPanel ],
		buttons : [ {
			text : '审核通过',
			handler : function() {
				depositorder.auditCwFun(3);
			}
		}, {
			text : '审核不通过',
			handler : function() {
				depositorder.auditCwFun(4);
			}
		} ]
	});

	depositorder.alwaysFun = function() {
		Share.resetGrid(depositorder.grid);
		if(1==depositorder.isKF || 1==depositorder.isCW){
			depositorder.addAction.enable();
			depositorder.auditKfAction.enable();
			depositorder.auditCwAction.enable();
		}else{
			depositorder.addAction.disable();
		}
		depositorder.deleteAction.disable();
		depositorder.editAction.disable();
		//depositorder.auditKfAction.disable();
		//depositorder.auditCwAction.disable();
	};
	depositorder.saveFun = function() {
		var form = depositorder.formPanel.getForm();
		if (!form.isValid()) {
			return;
		}
		Ext.Msg.confirm('提示', '确定已经电话核实过了吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : depositorder.save,
					params : form.getValues(),
					callback : function(json) {
						depositorder.addWindow.hide();
						depositorder.alwaysFun();
						depositorder.store.reload();
					}
				});
			}
		});
	};
	
	depositorder.delFun = function() {
		var record = depositorder.grid.getSelectionModel().getSelected();
		Ext.Msg.confirm('提示', '确定要删除这条记录吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : depositorder.del + record.data.poid + ".do",
					callback : function(json) {
						depositorder.alwaysFun();
						depositorder.store.reload();
					}
				});
			}
		});
	};

	depositorder.auditKfFun = function(result){
		var form = depositorder.auditKfFormPanel.getForm();
		if (!form.isValid()) {
			return;
		}
		Ext.Msg.confirm('提示', '确定已经电话核实过了吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : depositorder.auditKf+result+".do",
					params : form.getValues(),
					callback : function(json) {
						depositorder.auditKfWindow.hide();
						depositorder.alwaysFun();
						depositorder.store.reload();
					}
				});
			}
		});
	};
	depositorder.auditCwFun = function(result){
		var form = depositorder.auditCwFormPanel.getForm();
		if (!form.isValid()) {
			return;
		}
		// 发送请求
		Share.AjaxRequest({
			url : depositorder.auditCw+result+".do",
			params : form.getValues(),
			callback : function(json) {
				depositorder.auditCwWindow.hide();
				depositorder.alwaysFun();
				depositorder.store.reload();
			}
		});
	};
	depositorder.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'border',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
		items : [ depositorder.grid ]
	});
</script>
