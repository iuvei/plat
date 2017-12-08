<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	Ext.ns("Ext.market.punishorder"); // 自定义一个命名空间
	punishorder = Ext.market.punishorder; // 定义命名空间的别名
	punishorder = {
		all : '/manage/punishorder/queryPunishorder.do', // 所有扣款订单
		save : "/manage/punishorder/save.do",// 保存扣款订单
		del : "/manage/punishorder/del/",// 删除扣款订单
		auditKf : "/manage/punishorder/auditKf/",// 核实扣款订单
		auditCw : "/manage/punishorder/auditCw/",// 审核扣款订单
		pageSize : 30,// 每页显示的记录数
		isKF : eval('${isKF}'),//是否为客服：0否，1是
		isCW : eval('${isCW}'),//是否为账务：0否，1是
		isFK : eval('${isFK}'),//是否为账务：0否，1是
		STATUSMAP : eval('(${statusMap})')//注意括号
	};
	/** 改变页的combo*/
	punishorder.pageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				punishorder.pageSize  = parseInt(comboBox.getValue());
				punishorder.bbar.pageSize  = parseInt(comboBox.getValue());
				punishorder.store.baseParams.limit = punishorder.pageSize;
				punishorder.store.baseParams.start = 0;
				punishorder.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	punishorder.pageSize = parseInt(punishorder.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	punishorder.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:true,
		baseParams : {
			start : 0,
			limit : punishorder.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : punishorder.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'poid','platformorders','uiid','paytyple','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','ordercontent','createDate','updateDate','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','cwid','cwname','cwopttime','beforebalance','laterbalance' ]),
		listeners : {
			'load' : function(store, records, options) {
				punishorder.amountsum();
				punishorder.alwaysFun();
			}
		}
	});
	//小计
	punishorder.amountsum = function(){
		var p = new Ext.data.Record({fields:[ 'poid','platformorders','uiid','paytyple','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','ordercontent','createDate','updateDate','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','cwid','cwname','cwopttime','beforebalance','laterbalance' ]});
		var amounttotal= 0;
		punishorder.store.each(function(record){
			var amounts = record.data.amount;
			if(amounts != null){//存款金额
				amounttotal += Number(amounts);
			}
		});
		p.set('poid','小计：');
		p.set('amount',amounttotal);
		punishorder.store.add(p);
	}
	//punishorder.store.load(); 
	/** 基本信息-选择模式 */
	punishorder.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				punishorder.editAction.disable();
				punishorder.deleteAction.disable();
				punishorder.auditCwAction.disable();
				punishorder.auditKfAction.disable();
				
				if(1==punishorder.isKF){
					punishorder.addAction.enable();
					punishorder.deleteAction.enable();
					punishorder.editAction.enable();
					punishorder.auditKfAction.enable();
				}else{
					punishorder.addAction.disable();
				}
				if(1==punishorder.isCW || 1==punishorder.isFK){
					punishorder.deleteAction.enable();
					punishorder.auditCwAction.enable();
				}
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				if(1==punishorder.isKF || 1==punishorder.isFK){
					punishorder.addAction.enable();
				}else{
					punishorder.addAction.disable();
				}
				punishorder.deleteAction.disable();
				punishorder.editAction.disable();
				punishorder.auditCwAction.disable();
				punishorder.auditKfAction.disable();
			}
		}
	});
	/** 基本信息-数据列 */
	punishorder.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 110
		},
		// 'poid','platformorders','uiid','paytyple','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','ordercontent','createDate','updateDate','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','cwid','cwname','cwopttime'  
		columns : [ punishorder.selModel, {
			header : '扣款订单ID',
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
			width : 70,
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
			header : '扣款金额',
			dataIndex : 'amount',
//		}, {
//			header : '扣款方式',
//			dataIndex : 'paymethods',
//			renderer : function(v) {
//				return Share.map(v, punishorder.PAYMETHODSMAP, '');
//			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '当前状态',
			dataIndex : 'status',
			width : 70,
			renderer : function(v) {
				if(v==4){
					return '<span style="color:#FF0000;">失败</span>';
				}else if(v==1){
					return '<span style="color:#FF9900;font-size:14px;font-weight:bold;">发起</span>';
				}else{
					return Share.map(v, punishorder.STATUSMAP, '');
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
		},  {
			hidden : false,
			header : '会员姓名',
			width : 70,
			dataIndex : 'urealname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '扣款时间',
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
		} ,{
			header : '扣款前余额',
			dataIndex : 'beforebalance',
			width : 150,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '扣款后余额',
			dataIndex : 'laterbalance',
			width : 150,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}]
	});
	/** 新建 */
	punishorder.addAction = new Ext.Action({
		text : '新建',
		//text : '<fmt:message key="common.cancel"/>',
		disabled : true,
		iconCls : 'Add',
		handler : function() {
			punishorder.addWindow.setIconClass('Applicationadd'); // 设置窗口的样式
			punishorder.addWindow.setTitle('新建扣款订单'); // 设置窗口的名称
			punishorder.addWindow.show().center(); // 显示窗口
			punishorder.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			$("#poid").parent().parent().hide();
		}
	});
	/** 编辑 */
	punishorder.editAction = new Ext.Action({
		text : '编辑',
		iconCls : 'punishorder_edit',
		disabled : true,
		handler : function() {
			var record = punishorder.grid.getSelectionModel().getSelected();
			if(4!=record.data.status){
				Ext.Msg.alert('提示','只能编辑状态为“扣款失败”的扣款订单！');
				return;
			}
			punishorder.addWindow.setIconClass('punishorder_edit'); // 设置窗口的样式
			punishorder.addWindow.setTitle('编辑'); // 设置窗口的名称
			punishorder.addWindow.show().center();
			punishorder.formPanel.getForm().reset();
			punishorder.formPanel.getForm().loadRecord(record);
			var d = new Date();
			d.setTime(record.data.deposittime);
			$("#deposittimeStr").val(Ext.util.Format.date(d, "Y-m-d H:i:s"));
			$("#poid").parent().parent().show();
		}
	});
	/** 客服核实 */
	punishorder.auditKfAction = new Ext.Action({
		text : '客服核实',
		iconCls : 'Applicationedit',
		disabled : true,
		handler : function() {
			var record = punishorder.grid.getSelectionModel().getSelected();
			if(2!=record.data.status){
				Ext.Msg.alert('提示','只能审核状态为“待客服核实”的扣款订单！');
				return;
			}
			punishorder.auditKfWindow.setIconClass('Applicationedit'); // 设置窗口的样式
			punishorder.auditKfWindow.setTitle('客服核实'); // 设置窗口的名称
			punishorder.auditKfWindow.show().center();
			punishorder.auditKfFormPanel.getForm().reset();
			punishorder.auditKfFormPanel.getForm().loadRecord(record);
			
		}
	});
	/** 财务审核 */
	punishorder.auditCwAction = new Ext.Action({
		text : '财务审核',
		iconCls : 'Applicationedit',
		disabled : true,
		handler : function() {
			var record = punishorder.grid.getSelectionModel().getSelected();
			if(1!=record.data.status){
				Ext.Msg.alert('提示','只能审核状态为“待财务审核”的扣款订单！');
				return;
			}
			punishorder.auditCwWindow.setIconClass('Applicationedit'); // 设置窗口的样式
			punishorder.auditCwWindow.setTitle('财务审核'); // 设置窗口的名称
			punishorder.auditCwWindow.show().center();
			punishorder.auditCwFormPanel.getForm().reset();
			punishorder.auditCwFormPanel.getForm().loadRecord(record);
		}
	});
	/** 删除 */
	punishorder.deleteAction = new Ext.Action({
		text : '删除',
		iconCls : 'Cross',
		disabled : true,
		handler : function() {
			punishorder.delFun();
		}
	});
	/** 查询 */
	punishorder.searchField = new Ext.ux.form.SearchField({
		store : punishorder.store,
		paramName : 'uaccount',
		emptyText : '请输入会员账号',
		style : 'margin-left: 5px;'
	});
	/** 顶部工具栏 */
	punishorder.tbar = [ 
	                    punishorder.auditCwAction,'-',punishorder.searchField
	                     // '-',punishorder.addAction, 
	                     // '-', punishorder.deleteAction, 
	                      //'-', punishorder.editAction, 
	                      //'-', punishorder.auditKfAction, 
	                ];
	/** 底部工具条 */
	punishorder.bbar = new Ext.PagingToolbar({
		pageSize : punishorder.pageSize,
		store : punishorder.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', punishorder.pageSizeCombo ]
	});
	/** 基本信息-表格 */
	punishorder.grid = new Ext.grid.EditorGridPanel({
		store : punishorder.store,
		colModel : punishorder.colModel,
		selModel : punishorder.selModel,
		tbar : punishorder.tbar,
		bbar : punishorder.bbar,
		autoScroll : 'auto',
		region : 'center',
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig:{},
		stripeRows : true
	});
	punishorder.statusCombo = new Ext.form.ComboBox({
		fieldLabel : '状态',
		hiddenName : 'status',
		name : 'status',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
			fields : ['v', 't'],
			data : Share.map2Ary(punishorder.STATUSMAP)
		}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		value : "0",
		anchor : '99%'
	});
	
	/** 基本信息-详细信息的form */
	punishorder.formPanel = new Ext.form.FormPanel({
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
			fieldLabel : '扣款订单ID',
			id : 'poid',
			name : 'poid'
		}, {
			fieldLabel : '扣款会员账号',
			allowBlank : false,
			anchor : '99%',
			name : 'uaccount'
//		}, {
//			fieldLabel : '扣款会员名称',
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
			fieldLabel : '扣款金额',
			allowBlank : false,
			anchor : '99%',
			name : 'amount'
//		}, {
//			fieldLabel : '扣款时间',
//			allowBlank : false,
//			id : 'deposittimeStr',
//			name : 'deposittimeStr',
//			xtype:'datetimefield'
		}, {
			fieldLabel : '客服备注',
			allowBlank : false,
			anchor : '99%',
			maxLength : 200,
			xtype : 'textarea',
			name : 'kfremarks',
			anchor : '99%'
		}
		]
	});
	/** 编辑新建窗口 */
	punishorder.addWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 460,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ punishorder.formPanel ],
		buttons : [ {
			text : '保存',
			handler : function() {
				punishorder.saveFun();
			}
		}, {
			text : '重置',
			handler : function() {
				var form = punishorder.formPanel.getForm();
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
	punishorder.auditKfFormPanel = new Ext.form.FormPanel({
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
			fieldLabel : '扣款订单ID',
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
			fieldLabel : '扣款金额',
			name : 'amount'
//		}, {
//			readOnly : true,
//			allowBlank : false,
//			fieldLabel : '扣款时间',
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
	punishorder.auditKfWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 460,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ punishorder.auditKfFormPanel ],
		buttons : [ {
			text : '核实通过',
			handler : function() {
				punishorder.auditKfFun(2);
			}
		}, {
			text : '取消',
			handler : function() {
				punishorder.auditKfWindow.hide();
			}
		} ]
	});
	/** 财务审核表单 */
	punishorder.auditCwFormPanel = new Ext.form.FormPanel({
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
			fieldLabel : '扣款订单ID',
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
			fieldLabel : '扣款金额',
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
	punishorder.auditCwWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 310,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ punishorder.auditCwFormPanel ],
		buttons : [ {
			text : '审核通过',
			handler : function() {
				punishorder.auditCwFun(3);
			}
		}, {
			text : '审核不通过',
			handler : function() {
				punishorder.auditCwFun(4);
			}
		} ]
	});

	punishorder.alwaysFun = function() {
		Share.resetGrid(punishorder.grid);
		if(1==punishorder.isKF){
			punishorder.addAction.enable();
		}else{
			punishorder.addAction.disable();
		}
		punishorder.deleteAction.disable();
		punishorder.editAction.disable();
		punishorder.auditKfAction.disable();
		punishorder.auditCwAction.disable();
	};
	punishorder.saveFun = function() {
		var form = punishorder.formPanel.getForm();
		if (!form.isValid()) {
			return;
		}
		Ext.Msg.confirm('提示', '确定已经电话核实过了吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : punishorder.save,
					params : form.getValues(),
					callback : function(json) {
						punishorder.addWindow.hide();
						punishorder.alwaysFun();
						punishorder.store.reload();
					}
				});
			}
		});
	};
	
	punishorder.delFun = function() {
		var record = punishorder.grid.getSelectionModel().getSelected();
		Ext.Msg.confirm('提示', '确定要删除这条记录吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : punishorder.del + record.data.poid + ".do",
					callback : function(json) {
						punishorder.alwaysFun();
						punishorder.store.reload();
					}
				});
			}
		});
	};

	punishorder.auditKfFun = function(result){
		var form = punishorder.auditKfFormPanel.getForm();
		if (!form.isValid()) {
			return;
		}
		Ext.Msg.confirm('提示', '确定已经电话核实过了吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : punishorder.auditKf+result+".do",
					params : form.getValues(),
					callback : function(json) {
						punishorder.auditKfWindow.hide();
						punishorder.alwaysFun();
						punishorder.store.reload();
					}
				});
			}
		});
	};
	punishorder.auditCwFun = function(result){
		var form = punishorder.auditCwFormPanel.getForm();
		if (!form.isValid()) {
			return;
		}
		// 发送请求
		Share.AjaxRequest({
			url : punishorder.auditCw+result+".do",
			params : form.getValues(),
			callback : function(json) {
				punishorder.auditCwWindow.hide();
				punishorder.alwaysFun();
				punishorder.store.reload();
			}
		});
	};
	punishorder.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'border',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
		items : [ punishorder.grid ]
	});
</script>
