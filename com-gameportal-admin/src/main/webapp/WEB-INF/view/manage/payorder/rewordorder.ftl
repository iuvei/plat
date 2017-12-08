<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	Ext.ns("Ext.market.rewordorder"); // 自定义一个命名空间
	rewordorder = Ext.market.rewordorder; // 定义命名空间的别名
	rewordorder = {
		all : '/manage/rewordorder/queryRewordorder.do', // 所有赠送订单
		save : "/manage/rewordorder/save.do",// 保存赠送订单
		del : "/manage/rewordorder/del/",// 删除赠送订单
		auditKf : "/manage/rewordorder/auditKf/",// 核实赠送订单
		auditCw : "/manage/rewordorder/auditCw/",// 审核赠送订单
		down:'/manage/rewordorder/toDownloadReport.do',//导出加款订单报表
		pageSize : 30,// 每页显示的记录数
		isKF : eval('${isKF}'),//是否为客服：0否，1是
		isCW : eval('${isCW}'),//是否为账务：0否，1是
		isFK : eval('${isFK}'),//是否为账务：0否，1是
		ordertype : eval('(${ordertypeMap})'),
		STATUSMAP : eval('(${statusMap})')//注意括号
	};
	/** 改变页的combo*/
	rewordorder.pageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				rewordorder.pageSize  = parseInt(comboBox.getValue());
				rewordorder.bbar.pageSize  = parseInt(comboBox.getValue());
				rewordorder.store.baseParams.limit = rewordorder.pageSize;
				rewordorder.store.baseParams.start = 0;
				rewordorder.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	rewordorder.pageSize = parseInt(rewordorder.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	rewordorder.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:true,
		baseParams : {
			start : 0,
			limit : rewordorder.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : rewordorder.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'poid','platformorders','uiid','paytyple','ordertype','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','ordercontent','createDate','updateDate','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','cwid','cwname','cwopttime','beforebalance','laterbalance' ]),
		listeners : {
			'load' : function(store, records, options) {
				rewordorder.amountsum();
				rewordorder.alwaysFun();
			}
		}
	});
	//小计
	rewordorder.amountsum = function(){
		var p = new Ext.data.Record({fields:[ 'poid','platformorders','uiid','paytyple','ordertype','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','ordercontent','createDate','updateDate','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','cwid','cwname','cwopttime','beforebalance','laterbalance' ]});
		var amounttotal= 0;
		rewordorder.store.each(function(record){
			var amounts = record.data.amount;
			if(amounts != null){//存款金额
				amounttotal += Number(amounts);
			}
		});
		p.set('poid','小计：');
		p.set('amount',amounttotal);
		rewordorder.store.add(p);
	}
	//rewordorder.store.load(); 
	/** 基本信息-选择模式 */
	rewordorder.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				rewordorder.editAction.disable();
				rewordorder.deleteAction.disable();
				rewordorder.auditCwAction.disable();
				rewordorder.auditKfAction.disable();
				
				if(1==rewordorder.isKF){
					rewordorder.addAction.enable();
					rewordorder.deleteAction.enable();
					rewordorder.editAction.enable();
					rewordorder.auditKfAction.enable();
				}else{
					rewordorder.addAction.disable();
				}
				if(1==rewordorder.isCW || 1== rewordorder.isFK){
					rewordorder.deleteAction.enable();
					rewordorder.auditCwAction.enable();
				}
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				if(1==rewordorder.isKF || 1== rewordorder.isFK){
					rewordorder.addAction.enable();
				}else{
					rewordorder.addAction.disable();
				}
				rewordorder.deleteAction.disable();
				rewordorder.editAction.disable();
				rewordorder.auditCwAction.disable();
				rewordorder.auditKfAction.disable();
			}
		}
	});
	/** 基本信息-数据列 */
	rewordorder.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 110
		},
		// 'poid','platformorders','uiid','paytyple','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','ordercontent','createDate','updateDate','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','cwid','cwname','cwopttime'  
		columns : [ rewordorder.selModel, {
			header : '赠送订单ID',
			width : 180,
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
		}, {
			hidden : true,
			header : '银行名称',
			dataIndex : 'bankname'
		}, {
			hidden : true,
			header : '存入银行卡',
			dataIndex : 'bankcard'
		}, {
			hidden : true,
			header : '开户行名称',
			dataIndex : 'deposit'
		}, {
			hidden : true,
			header : '开户人',
			dataIndex : 'openname'
		}, {
			header : '订单类型',
			dataIndex : 'ordertype',
			renderer : function(v) {
				return Share.map(v, rewordorder.ordertype, '');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '赠送金额',
			dataIndex : 'amount',
			width : 70,
//		}, {
//			header : '赠送方式',
//			dataIndex : 'paymethods',
//			renderer : function(v) {
//				return Share.map(v, rewordorder.PAYMETHODSMAP, '');
//			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '当前状态',
			dataIndex : 'status',
			width : 60,
			renderer : function(v) {
				if(v==4){
					return '<span style="color:#FF0000;">失败</span>';
				}else if(v==1){
					return '<span style="color:#FF9900;font-size:14px;font-weight:bold;">发起</span>';
				}else{
					return Share.map(v, rewordorder.STATUSMAP, '');
				}
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '会员账号',
			dataIndex : 'uaccount',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			}),
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '会员姓名',
			dataIndex : 'urealname',
			width : 70,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			}),
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '赠送时间',
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
			width : 250,
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
			width : 250,
			dataIndex : 'cwremarks',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '操作财务',
			dataIndex : 'cwname',
			width : 80,
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
			header : '赠送前余额',
			dataIndex : 'beforebalance',
			width : 100,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '赠送后余额',
			dataIndex : 'laterbalance',
			width : 100,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		} ]
	});
	/** 新建 */
	rewordorder.addAction = new Ext.Action({
		text : '新建',
		//text : '<fmt:message key="common.cancel"/>',
		iconCls : 'Add',
		handler : function() {
			rewordorder.addWindow.setIconClass('Applicationadd'); // 设置窗口的样式
			rewordorder.addWindow.setTitle('新增赠送订单'); // 设置窗口的名称
			rewordorder.addWindow.show().center(); // 显示窗口
			rewordorder.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			$("#poid").parent().parent().hide();
		}
	});
	/** 编辑 */
	rewordorder.editAction = new Ext.Action({
		text : '编辑',
		iconCls : 'Applicationedit',
		disabled : true,
		handler : function() {
			var record = rewordorder.grid.getSelectionModel().getSelected();
			if(4!=record.data.status){
				Ext.Msg.alert('提示','只能编辑状态为“赠送失败”的赠送订单！');
				return;
			}
			rewordorder.addWindow.setIconClass('Applicationedit'); // 设置窗口的样式
			rewordorder.addWindow.setTitle('编辑'); // 设置窗口的名称
			rewordorder.addWindow.show().center();
			rewordorder.formPanel.getForm().reset();
			rewordorder.formPanel.getForm().loadRecord(record);
			$("#poid").parent().parent().show();
		}
	});
	/** 客服核实 */
	rewordorder.auditKfAction = new Ext.Action({
		text : '客服核实',
		iconCls : 'Applicationedit',
		disabled : true,
		handler : function() {
			var record = rewordorder.grid.getSelectionModel().getSelected();
			if(2!=record.data.status){
				Ext.Msg.alert('提示','只能审核状态为“待客服核实”的赠送订单！');
				return;
			}
			rewordorder.auditKfWindow.setIconClass('Applicationedit'); // 设置窗口的样式
			rewordorder.auditKfWindow.setTitle('客服核实'); // 设置窗口的名称
			rewordorder.auditKfWindow.show().center();
			rewordorder.auditKfFormPanel.getForm().reset();
			rewordorder.auditKfFormPanel.getForm().loadRecord(record);
		}
	});
	/** 财务审核 */
	rewordorder.auditCwAction = new Ext.Action({
		text : '财务审核',
		iconCls : 'Applicationedit',
		disabled : true,
		handler : function() {
			var record = rewordorder.grid.getSelectionModel().getSelected();
			if(1!=record.data.status){
				Ext.Msg.alert('提示','只能审核状态为“待财务审核”的赠送订单！');
				return;
			}
			rewordorder.auditCwWindow.setIconClass('Applicationedit'); // 设置窗口的样式
			rewordorder.auditCwWindow.setTitle('财务审核'); // 设置窗口的名称
			rewordorder.auditCwWindow.show().center();
			rewordorder.auditCwFormPanel.getForm().reset();
			rewordorder.auditCwFormPanel.getForm().loadRecord(record);
		}
	});
	/** 删除 */
	rewordorder.deleteAction = new Ext.Action({
		text : '删除',
		iconCls : 'Cross',
		disabled : true,
		handler : function() {
			rewordorder.delFun();
		}
	});
	/** 查询按钮 */
	rewordorder.searchAction = new Ext.Action({
		text : '查询',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				rewordorder.searchFun();
			}
	});
	
	rewordorder.searchParams = function(){
		var obj = {};
		obj.uaccount=$("#rewordorderuaccount").val(); //会员账号
		obj.startDate=$("#rewordorderstartDate").val(); //开始时间 
		obj.endDate=$("#rewordorderendDate").val(); //结束时间
		obj.status=$("#rewordorderorderstatus").prev().val();//订单状态
		obj.orderType=$("#rewordorderordertype").prev().val();//订单类型
	    return obj;
	}
	
	rewordorder.searchFun = function(){
		rewordorder.store.load({params: rewordorder.searchParams()});
	}
	
	rewordorder.store.on('beforeload',function(store, options){
	    rewordorder.store.baseParams = rewordorder.searchParams();
	});
	/** 导出报表按钮 */
	rewordorder.downAction = new Ext.Action({
			text : '导出报表',
			iconCls : 'Diskdownload',
			disabled : false,
			handler : function() {
				rewordorder.downReport();
			}
	});
	/** 导出报表函数 */
	rewordorder.downReport = function() {
		 //发送请求
		 window.open(rewordorder.down + "?uaccount="+$("#rewordorderuaccount").val()+"&startDate="+$("#rewordorderstartDate").val()+"&endDate="+$("#rewordorderendDate").val()+"&status="+$("#rewordorderorderstatus").prev().val()+"&orderType="+$("#rewordorderordertype").prev().val());
	};
	/**日期条件  -- 开始时间*/
 	rewordorder.startDateField = new Ext.form.DateField({
  			id:'rewordorderstartDate',
        	showToday:true,
        	format:'Y-m-d',
        	invalidText:'日期输入非法',
        	allowBlank : false,
        	width:110 
 	});
 	/** 日期条件  -- 结束时间*/
 	rewordorder.endDateField = new Ext.form.DateField({
  			id:'rewordorderendDate',
        	showToday:true,
        	format:'Y-m-d',
        	invalidText:'日期输入非法',
        	allowBlank : false,
        	width:110 
 	});
 	/** 处理状态*/
	rewordorder.orderstatusAction = new Ext.form.ComboBox({
		hiddenName : 'rewordorderorderstatus',
		id : 'rewordorderorderstatus',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(rewordorder.STATUSMAP)
				}),
		valueField : 'v',
		displayField : 't',
		value:'',
		allowBlank : false,
		editable : false,
		width:100
	});
	/** 订单类型*/
	rewordorder.ordertypeAction = new Ext.form.ComboBox({
		hiddenName : 'rewordorderordertype',
		id : 'rewordorderordertype',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(rewordorder.ordertype)
				}),
		valueField : 'v',
		displayField : 't',
		value:'',
		allowBlank : false,
		editable : false,
		width:100
	});
	/** 顶部工具栏 */
	rewordorder.tbar = [ /**rewordorder.addAction, 
	                      '-', rewordorder.deleteAction,
	                      '-',*/ rewordorder.auditCwAction,
	                     /** '-',rewordorder.downAction,*/
	                      '-','&nbsp;','会员账号:',{id:'rewordorderuaccount',xtype:'textfield',width:100},
	                      '-','&nbsp;','加款时间:',rewordorder.startDateField,'~',rewordorder.endDateField,
	                      '-','&nbsp;','处理状态:',rewordorder.orderstatusAction,'-','&nbsp;','订单类型:',rewordorder.ordertypeAction,
	                      '-','&nbsp;',rewordorder.searchAction
	                ];
	/** 底部工具条 */
	rewordorder.bbar = new Ext.PagingToolbar({
		pageSize : rewordorder.pageSize,
		store : rewordorder.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', rewordorder.pageSizeCombo ]
	});
	/** 基本信息-表格 */
	rewordorder.grid = new Ext.grid.EditorGridPanel({
		store : rewordorder.store,
		colModel : rewordorder.colModel,
		selModel : rewordorder.selModel,
		tbar : rewordorder.tbar,
		bbar : rewordorder.bbar,
		autoScroll : 'auto',
		region : 'center',
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig: {},  
		stripeRows : true
	});
	rewordorder.statusCombo = new Ext.form.ComboBox({
		fieldLabel : '状态',
		hiddenName : 'status',
		name : 'status',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
			fields : ['v', 't'],
			data : Share.map2Ary(rewordorder.STATUSMAP)
		}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		value : "0",
		anchor : '99%'
	});
	
	/** 基本信息-详细信息的form */
	rewordorder.formPanel = new Ext.form.FormPanel({
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
			fieldLabel : '赠送订单ID',
			id : 'poid',
			name : 'poid'
		}, {
			fieldLabel : '赠送会员账号',
			allowBlank : false,
			anchor : '99%',
			name : 'uaccount'
//		}, {
//			fieldLabel : '赠送会员名称',
//			allowBlank : false,
//			name : 'urealname'
		}, 
		{
			xtype : 'hidden',
			readOnly : true,
			fieldLabel : '银行卡卡号',
			allowBlank : false,
			id : 'bankcard',
			name : 'bankcard'
		}, {
			xtype : 'hidden',
			fieldLabel : '银行名称',
			allowBlank : false,
			id : 'bankname',
			name : 'bankname'
		}, {
			xtype : 'hidden',
			fieldLabel : '开户行名称',
			allowBlank : false,
			id : 'deposit',
			name : 'deposit'
		}, {
			xtype : 'hidden',
			fieldLabel : '开户人',
			allowBlank : false,
			id : 'openname',
			name : 'openname'
		}, {
			fieldLabel : '赠送金额',
			allowBlank : false,
			anchor : '99%',
			name : 'amount'
//		}, {
//			fieldLabel : '赠送时间',
//			allowBlank : false,
//			id : 'deposittimeStr',
//			name : 'deposittimeStr',
//			xtype:'datetimefield'
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
	rewordorder.addWindow = new Ext.Window({
		layout : 'fit',
		width : 400,
		height : 250,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ rewordorder.formPanel ],
		buttons : [ {
			text : '保存',
			handler : function() {
				rewordorder.saveFun();
			}
		}, {
			text : '重置',
			handler : function() {
				var form = rewordorder.formPanel.getForm();
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
	rewordorder.auditKfFormPanel = new Ext.form.FormPanel({
		autoScroll : true,
		frame : false,
		title : '赠送订单信息',
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
			fieldLabel : '赠送订单ID',
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
		}, {
			readOnly : true,
			allowBlank : false,
			fieldLabel : '银行卡卡号',
			name : 'bankcard'
		}, {
			readOnly : true,
			fieldLabel : '银行名称',
			name : 'bankname'
		}, {
			readOnly : true,
			fieldLabel : '开户行名称',
			name : 'deposit'
		}, {
			readOnly : true,
			fieldLabel : '开户人',
			name : 'openname'
		}, {
			readOnly : true,
			allowBlank : false,
			fieldLabel : '赠送金额',
			name : 'amount'
//		}, {
//			readOnly : true,
//			allowBlank : false,
//			fieldLabel : '赠送时间',
//			id : 'deposittimeStrKf',
//			name : 'deposittimeStr',
//			xtype:'datetimefield'
		}, {
			allowBlank : false,
			fieldLabel : '客服备注',
			maxLength : 200,
			xtype : 'textarea',
			name : 'kfremarks',
			anchor : '99%'
		}, {
			readOnly : true,
			fieldLabel : '财务备注',
			maxLength : 200,
			xtype : 'textarea',
			name : 'cwremarks',
			anchor : '99%'
		}
		]
	});
	/** 客服核实窗口 */
	rewordorder.auditKfWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 460,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ rewordorder.auditKfFormPanel ],
		buttons : [ {
			text : '核实通过',
			handler : function() {
				rewordorder.auditKfFun(2);
			}
		}, {
			text : '取消',
			handler : function() {
				rewordorder.auditKfWindow.hide();
			}
		} ]
	});
	/** 财务审核表单 */
	rewordorder.auditCwFormPanel = new Ext.form.FormPanel({
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
			fieldLabel : '赠送订单ID',
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
		}, {
			xtype : 'hidden',
			readOnly : true,
			allowBlank : false,
			fieldLabel : '银行卡卡号',
			name : 'bankcard'
		}, {
			xtype : 'hidden',
			readOnly : true,
			fieldLabel : '银行名称',
			name : 'bankname'
		}, {
			xtype : 'hidden',
			readOnly : true,
			fieldLabel : '开户行名称',
			name : 'deposit'
		}, {
			xtype : 'hidden',
			readOnly : true,
			fieldLabel : '开户人',
			name : 'openname'
		}, {
			readOnly : true,
			allowBlank : false,
			fieldLabel : '赠送金额',
			anchor : '99%',
			name : 'amount'
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
	rewordorder.auditCwWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 310,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ rewordorder.auditCwFormPanel ],
		buttons : [ {
			text : '审核通过',
			handler : function() {
				rewordorder.auditCwFun(3);
			}
		}, {
			text : '审核不通过',
			handler : function() {
				rewordorder.auditCwFun(4);
			}
		} ]
	});

	rewordorder.alwaysFun = function() {
		Share.resetGrid(rewordorder.grid);
		if(1==rewordorder.isKF){
			rewordorder.addAction.enable();
		}else{
			rewordorder.addAction.disable();
		}
		rewordorder.deleteAction.disable();
		rewordorder.editAction.disable();
		rewordorder.auditKfAction.disable();
		rewordorder.auditCwAction.disable();
	};
	rewordorder.saveFun = function() {
		var form = rewordorder.formPanel.getForm();
		if (!form.isValid()) {
			return;
		}
		Ext.Msg.confirm('提示', '确定已经电话核实过了吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : rewordorder.save,
					params : form.getValues(),
					callback : function(json) {
						rewordorder.addWindow.hide();
						rewordorder.alwaysFun();
						rewordorder.store.reload();
					}
				});
			}
		});
	};
	
	rewordorder.delFun = function() {
		var record = rewordorder.grid.getSelectionModel().getSelected();
		Ext.Msg.confirm('提示', '确定要删除这条记录吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : rewordorder.del + record.data.poid + ".do",
					callback : function(json) {
						rewordorder.alwaysFun();
						rewordorder.store.reload();
					}
				});
			}
		});
	};

	rewordorder.auditKfFun = function(result){
		var form = rewordorder.auditKfFormPanel.getForm();
		if (!form.isValid()) {
			return;
		}
		Ext.Msg.confirm('提示', '确定已经电话核实过了吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : rewordorder.auditKf+result+".do",
					params : form.getValues(),
					callback : function(json) {
						rewordorder.auditKfWindow.hide();
						rewordorder.alwaysFun();
						rewordorder.store.reload();
					}
				});
			}
		});
	};
	rewordorder.auditCwFun = function(result){
		var form = rewordorder.auditCwFormPanel.getForm();
		if (!form.isValid()) {
			return;
		}
		// 发送请求
		Share.AjaxRequest({
			url : rewordorder.auditCw+result+".do",
			params : form.getValues(),
			callback : function(json) {
				rewordorder.auditCwWindow.hide();
				rewordorder.alwaysFun();
				rewordorder.store.reload();
			}
		});
	};
	rewordorder.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'border',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
		items : [ rewordorder.grid ]
	});
</script>
