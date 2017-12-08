<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	Ext.ns("Ext.market.pickuporder"); // 自定义一个命名空间
	pickuporder = Ext.market.pickuporder; // 定义命名空间的别名
	pickuporder = {
		all : '/manage/pickuporder/queryPickuporder.do', // 所有提款订单
		save : "/manage/pickuporder/save.do",// 保存提款订单
		del : "/manage/pickuporder/del/",// 删除提款订单
		auditKf : "/manage/pickuporder/auditKf/",// 核实提款订单
		auditFK : "/manage/pickuporder/auditFK/",// 风控审核提款订单
		auditCw : "/manage/pickuporder/auditCw/",// 审核提款订单
		auditCK : "/manage/pickuporder/auditCk/",// 客户出款
		down:'/manage/pickuporder/toDownloadReport.do',//导出提款订单报表
		pageSize : 30,// 每页显示的记录数
		isKF : eval('${isKF}'),//是否为客服：0否，1是
		isCW : eval('${isCW}'),//是否为账务：0否，1是
		isFK : eval('${isFK}'),//是否为账务：0否，1是
		STATUSMAP : eval('(${statusMap})')//注意括号
	};
	/** 改变页的combo*/
	pickuporder.pageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				pickuporder.pageSize  = parseInt(comboBox.getValue());
				pickuporder.bbar.pageSize  = parseInt(comboBox.getValue());
				pickuporder.store.baseParams.limit = pickuporder.pageSize;
				pickuporder.store.baseParams.start = 0;
				pickuporder.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	pickuporder.pageSize = parseInt(pickuporder.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	pickuporder.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:true,
		baseParams : {
			start : 0,
			limit : pickuporder.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : pickuporder.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'poid','platformorders','uiid','paytyple','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','ordercontent','createDate','updateDate','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','cwid','cwname','cwopttime','beforebalance','laterbalance','proxyname']),
		listeners : {
			'load' : function(store, records, options) {
				pickuporder.amountsum();
				pickuporder.alwaysFun();
			}
		}
	});
	//小计
	pickuporder.amountsum = function(){
		var p = new Ext.data.Record({fields:[ 'poid','platformorders','uiid','paytyple','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','ordercontent','createDate','updateDate','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','cwid','cwname','cwopttime','beforebalance','laterbalance','proxyname' ]});
		var amounttotal= 0;
		pickuporder.store.each(function(record){
			var amounts = record.data.amount;
			if(amounts != null){//存款金额
				amounttotal += Number(amounts);
			}
		});
		p.set('poid','小计：');
		p.set('amount',amounttotal);
		pickuporder.store.add(p);
	}
	//pickuporder.store.load(); 
	/** 基本信息-选择模式 */
	pickuporder.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				pickuporder.editAction.disable();
				pickuporder.deleteAction.disable();
				pickuporder.auditCwAction.disable();
				pickuporder.auditCkAction.disable();
				pickuporder.auditFKAction.disable();
				pickuporder.auditKfAction.disable();
				
				if(1==pickuporder.isFK || 1==pickuporder.isCW){
					pickuporder.addAction.enable();
					pickuporder.deleteAction.enable();
					pickuporder.editAction.enable();
					pickuporder.auditKfAction.enable();
					pickuporder.auditCkAction.enable();
				}else{
					pickuporder.addAction.disable();
				}
				if(1==pickuporder.isFK || 1==pickuporder.isCW){
					pickuporder.auditCwAction.enable();
				}
				if(1==pickuporder.isFK || 1==pickuporder.isCW){
					pickuporder.auditFKAction.enable();
				}
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				if(1==pickuporder.isFK || 1==pickuporder.isCW){
					pickuporder.addAction.enable();
				}else{
					pickuporder.addAction.disable();
				}
				pickuporder.addAction.enable();
				
				pickuporder.deleteAction.disable();
				pickuporder.editAction.disable();
				pickuporder.auditCwAction.disable();
				pickuporder.auditKfAction.disable();
				pickuporder.auditFKAction.disable();
			}
		}
	});
	/** 基本信息-数据列 */
	pickuporder.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 110
		},
		// 'poid','platformorders','uiid','paytyple','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','ordercontent','createDate','updateDate','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','cwid','cwname','cwopttime'  
		columns : [ pickuporder.selModel, {
			header : '提款订单ID',
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
		}, {
			header : '当前状态',
			dataIndex : 'status',
			width : 80,
			renderer : function(v) {
				if(v==4){
					return '<span style="color:#FF0000;">失败</span>';
				}else if(v==1){
					return '<span style="color:#FF9900;font-size:14px;font-weight:bold;">发起</span>';
				}else if(v==2){
					return '<span style="color:#FF9900;font-size:14px;font-weight:bold;">待财务审核</span>';
				}else{
					return Share.map(v, pickuporder.STATUSMAP, '');
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
			})
		},{
			header : '会员姓名',
			width : 70,
			dataIndex : 'urealname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '所属代理',
			dataIndex : 'proxyname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
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
			width : 70,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '提款金额',
			dataIndex : 'amount',
			width : 70,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '提款时间',
			dataIndex : 'deposittime',
			width : 130,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '风控备注',
			dataIndex : 'kfremarks',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '操作风控',
			dataIndex : 'kfname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '风控操作时间',
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
		} ,{
			header : '提款前余额',
			dataIndex : 'beforebalance',
			width : 150,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '提款后余额',
			dataIndex : 'laterbalance',
			width : 150,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}]
	});
	/** 新建 */
	pickuporder.addAction = new Ext.Action({
		text : '新建',
		//text : '<fmt:message key="common.cancel"/>',
		disabled : true,
		iconCls : 'Add',
		handler : function() {
			pickuporder.addWindow.setIconClass('Applicationadd'); // 设置窗口的样式
			pickuporder.addWindow.setTitle('新建提款订单'); // 设置窗口的名称
			pickuporder.addWindow.show().center(); // 显示窗口
			pickuporder.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			$("#poid").parent().parent().hide();
		}
	});
	/** 编辑 */
	pickuporder.editAction = new Ext.Action({
		text : '编辑',
		iconCls : 'pickuporder_edit',
		disabled : true,
		handler : function() {
			var record = pickuporder.grid.getSelectionModel().getSelected();
			if(4!=record.data.status){
				Ext.Msg.alert('提示','只能编辑状态为“提款失败”的提款订单！');
				return;
			}
			pickuporder.addWindow.setIconClass('pickuporder_edit'); // 设置窗口的样式
			pickuporder.addWindow.setTitle('编辑'); // 设置窗口的名称
			pickuporder.addWindow.show().center();
			pickuporder.formPanel.getForm().reset();
			pickuporder.formPanel.getForm().loadRecord(record);
			$("#poid").parent().parent().show();
		}
	});
	/** 客服核实 */
	pickuporder.auditKfAction = new Ext.Action({
		text : '客服核实',
		iconCls : 'Applicationedit',
		disabled : true,
		handler : function() {
			var record = pickuporder.grid.getSelectionModel().getSelected();
			if(2!=record.data.status){
				Ext.Msg.alert('提示','只能审核状态为“待客服核实”的提款订单！');
				return;
			}
			pickuporder.auditKfWindow.setIconClass('Applicationedit'); // 设置窗口的样式
			pickuporder.auditKfWindow.setTitle('客服核实'); // 设置窗口的名称
			pickuporder.auditKfWindow.show().center();
			pickuporder.auditKfFormPanel.getForm().reset();
			pickuporder.auditKfFormPanel.getForm().loadRecord(record);
		}
	});
	/** 财务审核 */
	pickuporder.auditCwAction = new Ext.Action({
		text : '财务审核',
		iconCls : 'Applicationedit',
		disabled : true,
		handler : function() {
			var record = pickuporder.grid.getSelectionModel().getSelected();
			if(2!=record.data.status){
				Ext.Msg.alert('提示','只能审核状态为“待财务审核”的提款订单！');
				return;
			}
			pickuporder.auditCwWindow.setIconClass('Applicationedit'); // 设置窗口的样式
			pickuporder.auditCwWindow.setTitle('财务审核'); // 设置窗口的名称
			pickuporder.auditCwWindow.show().center();
			pickuporder.auditCwFormPanel.getForm().reset();
			pickuporder.auditCwFormPanel.getForm().loadRecord(record);
		}
	});
	
	/** 财务审核 */
	pickuporder.auditCkAction = new Ext.Action({
		text : '会员出款',
		iconCls : 'Applicationedit',
		disabled : true,
		handler : function() {
			var record = pickuporder.grid.getSelectionModel().getSelected();
			if(2!=record.data.status){
				Ext.Msg.alert('提示','只能出款状态为“待财务审核”的提款订单！');
				return;
			}
			window.open(pickuporder.auditCK+record.data.poid+".do");
		}
	});
	/** 风控审核 */
	pickuporder.auditFKAction = new Ext.Action({
		text : '风控审核',
		iconCls : 'Applicationedit',
		disabled : true,
		handler : function() {
			var record = pickuporder.grid.getSelectionModel().getSelected();
			if(1!=record.data.status){
				Ext.Msg.alert('提示','只能审核状态为“发起”的提款订单！');
				return;
			}
			pickuporder.auditFKWindow.setIconClass('Applicationedit'); // 设置窗口的样式
			pickuporder.auditFKWindow.setTitle('风控审核'); // 设置窗口的名称
			pickuporder.auditFKWindow.show().center();
			pickuporder.auditFKFormPanel.getForm().reset();
			pickuporder.auditFKFormPanel.getForm().loadRecord(record);
		}
	});
	/** 删除 */
	pickuporder.deleteAction = new Ext.Action({
		text : '删除',
		iconCls : 'Cross',
		disabled : true,
		handler : function() {
			pickuporder.delFun();
		}
	});
	/** 查询按钮 */
	pickuporder.searchAction = new Ext.Action({
		text : '查询',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				pickuporder.searchFun();
			}
	});
	pickuporder.searchParams = function(){
		var obj = {};
		obj.uaccount=$("#pickuporderuaccount").val(); //会员账号
		obj.startDate=$("#pickuporderstartDate").val(); //开始时间 
		obj.endDate=$("#pickuporderendDate").val(); //结束时间
		obj.status=$("#pickuporderorderstatus").prev().val();//订单状态
	    return obj;
	}
	
	pickuporder.searchFun = function(){
		pickuporder.store.load({params: pickuporder.searchParams()});
	}
	
	pickuporder.store.on('beforeload',function(store, options){
	    pickuporder.store.baseParams = pickuporder.searchParams();
	});
	
	/** 导出报表按钮 */
	pickuporder.downAction = new Ext.Action({
			text : '导出',
			iconCls : 'Diskdownload',
			disabled : false,
			handler : function() {
				pickuporder.downReport();
			}
	});
	/** 导出报表函数 */
	pickuporder.downReport = function() {
		 //发送请求
		 window.open(pickuporder.down + "?uaccount="+$("#pickuporderuaccount").val()+"&startDate="+$("#pickuporderstartDate").val()+"&endDate="+$("#pickuporderendDate").val()+"&status="+$("#pickuporderorderstatus").prev().val());
	};
	/**日期条件  -- 开始时间*/
 	pickuporder.startDateField = new Ext.form.DateField({
  			id:'pickuporderstartDate',
        	showToday:true,
        	format:'Y-m-d',
        	invalidText:'日期输入非法',
        	allowBlank : false,
        	width:130 
 	});
 	/** 日期条件  -- 结束时间*/
 	pickuporder.endDateField = new Ext.form.DateField({
  			id:'pickuporderendDate',
        	showToday:true,
        	format:'Y-m-d',
        	invalidText:'日期输入非法',
        	allowBlank : false,
        	width:130 
 	});
 	/** 处理状态*/
	pickuporder.orderstatusAction = new Ext.form.ComboBox({
		hiddenName : 'pickuporderorderstatus',
		id : 'pickuporderorderstatus',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(pickuporder.STATUSMAP)
				}),
		valueField : 'v',
		displayField : 't',
		value:'',
		allowBlank : false,
		editable : false,
		width:100
	});
	/** 顶部工具栏 */
	pickuporder.tbar = [ pickuporder.addAction, 
	                      '-', pickuporder.deleteAction, 
	                      '-', pickuporder.auditFKAction,
	                      '-', pickuporder.auditCwAction,
	                      '-', pickuporder.auditCkAction,
	                      '-', pickuporder.downAction,
	                      '-','&nbsp;','会员账号:',{id:'pickuporderuaccount',xtype:'textfield',width:100},
	                      '-','&nbsp;','提款时间:',pickuporder.startDateField,'~',pickuporder.endDateField,
	                      '-','&nbsp;','处理状态:',pickuporder.orderstatusAction,
	                      '-','&nbsp;',pickuporder.searchAction
	                ];
	/** 底部工具条 */
	pickuporder.bbar = new Ext.PagingToolbar({
		pageSize : pickuporder.pageSize,
		store : pickuporder.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', pickuporder.pageSizeCombo ]
	});
	/** 基本信息-表格 */
	pickuporder.grid = new Ext.grid.EditorGridPanel({
		store : pickuporder.store,
		colModel : pickuporder.colModel,
		selModel : pickuporder.selModel,
		tbar : pickuporder.tbar,
		bbar : pickuporder.bbar,
		autoScroll : 'auto',
		region : 'center',
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig:{},
		stripeRows : true
	});
	pickuporder.statusCombo = new Ext.form.ComboBox({
		fieldLabel : '状态',
		hiddenName : 'status',
		name : 'status',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
			fields : ['v', 't'],
			data : Share.map2Ary(pickuporder.STATUSMAP)
		}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		value : "0",
		anchor : '99%'
	});
	
	/** 基本信息-详细信息的form */
	pickuporder.formPanel = new Ext.form.FormPanel({
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
			fieldLabel : '提款订单ID',
			id : 'poid',
			name : 'poid'
		}, {
			fieldLabel : '会员账号',
			allowBlank : false,
			anchor : '99%',
			name : 'uaccount'
//		}, {
//			fieldLabel : '提款会员名称',
//			allowBlank : false,
//			name : 'urealname'
		}, 
		{
			xtype : 'hidden',
			readOnly : true,
			fieldLabel : '银行卡卡号',
			anchor : '99%',
			allowBlank : false,
			id : 'bankcard',
			name : 'bankcard'
		}, {
			xtype : 'hidden',
			fieldLabel : '银行名称',
			anchor : '99%',
			allowBlank : false,
			id : 'bankname',
			name : 'bankname'
		}, {
			xtype : 'hidden',
			fieldLabel : '开户行名称',
			anchor : '99%',
			allowBlank : false,
			id : 'deposit',
			name : 'deposit'
		}, {
			xtype : 'hidden',
			fieldLabel : '开户人',
			anchor : '99%',
			allowBlank : false,
			id : 'openname',
			name : 'openname'
		}, {
			fieldLabel : '提款金额',
			anchor : '99%',
			allowBlank : false,
			name : 'amount'
//		}, {
//			fieldLabel : '提款时间',
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
	pickuporder.addWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 203,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ pickuporder.formPanel ],
		buttons : [ {
			text : '保存',
			handler : function() {
				pickuporder.saveFun();
			}
		}, {
			text : '重置',
			handler : function() {
				var form = pickuporder.formPanel.getForm();
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
	pickuporder.auditKfFormPanel = new Ext.form.FormPanel({
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
			fieldLabel : '提款订单ID',
			anchor : '99%',
			id : 'poidKf',
			name : 'poid'
		}, {
			readOnly : true,
			allowBlank : false,
			fieldLabel : '会员账号',
			anchor : '99%',
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
			name : 'deposit'
		}, {
			readOnly : true,
			fieldLabel : '开户人',
			anchor : '99%',
			name : 'openname'
		}, {
			readOnly : true,
			allowBlank : false,
			fieldLabel : '提款金额',
			anchor : '99%',
			name : 'amount'
//		}, {
//			readOnly : true,
//			allowBlank : false,
//			fieldLabel : '提款时间',
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
	pickuporder.auditKfWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 460,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ pickuporder.auditKfFormPanel ],
		buttons : [ {
			text : '核实通过',
			handler : function() {
				pickuporder.auditKfFun(2);
			}
		}, {
			text : '取消',
			handler : function() {
				pickuporder.auditKfWindow.hide();
			}
		} ]
	});
	/** 财务审核表单 */
	pickuporder.auditCwFormPanel = new Ext.form.FormPanel({
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
			fieldLabel : '提款订单ID',
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
			fieldLabel : '提款金额',
			name : 'amount'
//		}, {
//			readOnly : true,
//			allowBlank : false,
//			fieldLabel : '提款时间',
//			id : 'deposittimeStrCw',
//			name : 'deposittimeStr',
//			xtype:'datetimefield'
		}, {
			readOnly : true,
			fieldLabel : '风控备注',
			anchor : '99%',
			maxLength : 200,
			xtype : 'textarea',
			name : 'kfremarks',
			anchor : '99%'
		}, {
			//allowBlank : false,
			fieldLabel : '财务备注',
			anchor : '99%',
			maxLength : 200,
			xtype : 'textarea',
			name : 'cwremarks',
			anchor : '99%'
		}
		]
	});
		/** 风控审核表单 */
	pickuporder.auditFKFormPanel = new Ext.form.FormPanel({
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
			fieldLabel : '提款订单ID',
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
			fieldLabel : '提款金额',
			name : 'amount'
//		}, {
//			readOnly : true,
//			allowBlank : false,
//			fieldLabel : '提款时间',
//			id : 'deposittimeStrCw',
//			name : 'deposittimeStr',
//			xtype:'datetimefield'
		}, {
			fieldLabel : '风控备注',
			anchor : '99%',
			maxLength : 200,
			xtype : 'textarea',
			name : 'kfremarks',
			anchor : '99%'
		}
		]
	});
	/** 风控审核窗口 */
	pickuporder.auditFKWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 410,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ pickuporder.auditFKFormPanel ],
		buttons : [ {
			text : '审核通过',
			handler : function() {
				pickuporder.auditFKFun(2);
			}
		}, {
			text : '审核不通过',
			handler : function() {
				pickuporder.auditFKFun(4);
			}
		} ]
	});
	/** 财务审核窗口 */
	pickuporder.auditCwWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 410,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ pickuporder.auditCwFormPanel ],
		buttons : [ {
			text : '审核通过',
			handler : function() {
				pickuporder.auditCwFun(3);
			}
		}, {
			text : '审核不通过',
			handler : function() {
				pickuporder.auditCwFun(4);
			}
		} ]
	});

	pickuporder.alwaysFun = function() {
		Share.resetGrid(pickuporder.grid);
		if(1==pickuporder.isFK || 1==pickuporder.isCW){
			pickuporder.addAction.enable();
		}else{
			pickuporder.addAction.disable();
		}
		
		pickuporder.deleteAction.disable();
		pickuporder.editAction.disable();
		pickuporder.auditKfAction.disable();
		pickuporder.auditCwAction.disable();
		pickuporder.auditCkAction.disable();
		pickuporder.auditFKAction.disable();
	};
	pickuporder.saveFun = function() {
		var form = pickuporder.formPanel.getForm();
		if (!form.isValid()) {
			return;
		}
		Ext.Msg.confirm('提示', '确定已经电话核实过了吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : pickuporder.save,
					params : form.getValues(),
					callback : function(json) {
						pickuporder.addWindow.hide();
						pickuporder.alwaysFun();
						pickuporder.store.reload();
					}
				});
			}
		});
	};
	
	pickuporder.delFun = function() {
		var record = pickuporder.grid.getSelectionModel().getSelected();
		Ext.Msg.confirm('提示', '确定要删除这条记录吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : pickuporder.del + record.data.poid + ".do",
					callback : function(json) {
						pickuporder.alwaysFun();
						pickuporder.store.reload();
					}
				});
			}
		});
	};

	pickuporder.auditKfFun = function(result){
		var form = pickuporder.auditKfFormPanel.getForm();
		if (!form.isValid()) {
			return;
		}
		Ext.Msg.confirm('提示', '确定已经电话核实过了吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : pickuporder.auditKf+result+".do",
					params : form.getValues(),
					callback : function(json) {
						pickuporder.auditKfWindow.hide();
						pickuporder.alwaysFun();
						pickuporder.store.reload();
					}
				});
			}
		});
	};
	pickuporder.auditCwFun = function(result){
		var form = pickuporder.auditCwFormPanel.getForm();
		if (!form.isValid()) {
			return;
		}
		if(result ==4 && form.findField("cwremarks").getValue()==''){
			Ext.Msg.alert('提示','请填写财务备注！');
			return;
		}
		// 发送请求
		Share.AjaxRequest({
			url : pickuporder.auditCw+result+".do",
			params : form.getValues(),
			callback : function(json) {
				pickuporder.auditCwWindow.hide();
				pickuporder.alwaysFun();
				pickuporder.store.reload();
			}
		});
	};
	pickuporder.auditFKFun = function(result){
		var form = pickuporder.auditFKFormPanel.getForm();
		if (!form.isValid()) {
			return;
		}
		if(result ==4 && form.findField("kfremarks").getValue()==''){
			Ext.Msg.alert('提示','请填写风控备注！');
			return;
		}
		// 发送请求
		Share.AjaxRequest({
			url : pickuporder.auditFK+result+".do",
			params : form.getValues(),
			callback : function(json) {
				pickuporder.auditFKWindow.hide();
				pickuporder.alwaysFun();
				pickuporder.store.reload();
			}
		});
	};
	pickuporder.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'border',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
		items : [ pickuporder.grid ]
	});
</script>
