<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	// ----------------------明细记录--------------------
	Ext.ns("Ext.market.rporder"); // 自定义一个命名空间
	rporder = Ext.market.rporder; // 定义命名空间的别名
	rporder = {
		all : '/manage/rporder/queryRporder.do', // 所有赠送和捐款订单
		pageSize : 30,// 每页显示的记录数
		ordertype : eval('(${fields.ordertype})'),
		PAYTYPLEMAP : eval('(${paytypleMap})')//注意括号
	};
	/** 改变页的combo*/
	rporder.pageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				rporder.pageSize  = parseInt(comboBox.getValue());
				rporder.bbar.pageSize  = parseInt(comboBox.getValue());
				rporder.store.baseParams.limit = rporder.pageSize;
				rporder.store.baseParams.start = 0;
				rporder.store.baseParams.ordertype=$("#otype").prev().val();
				rporder.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	rporder.pageSize = parseInt(rporder.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	rporder.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:true,
		baseParams : {
			uiid : -1,
			start : 0,
			limit : rporder.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : rporder.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'poid','platformorders','uiid','paytyple','ordertype','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','ordercontent','createDate','updateDate','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','beforebalance','laterbalance','cwid','cwname','cwopttime' ]),
		listeners : {
			'load' : function(store, records, options) {
				rporder.alwaysFun();
			}
		}
	});
	/** 基本信息-选择模式 */
	rporder.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				rporder.showAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				rporder.showAction.disable();
			}
		}
	});
	/** 基本信息-数据列 */
	rporder.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 90
		},
		columns : [ rporder.selModel, {
			header : '收支类型',
			dataIndex : 'paytyple',
			renderer : function(v) {
				return Share.map(v, rporder.PAYTYPLEMAP, '');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})	
		},{
			header : '订单类型',
			dataIndex : 'ordertype',
			renderer : function(v) {
				return Share.map(v, rporder.ordertype, '');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})	
		}, {
			header : '存款金额（元）',
			dataIndex : 'amount',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '存款订单ID',
			width : 150,
			hidden : true,
			dataIndex : 'poid',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			hidden : true,
			header : '客服操作时间',
			dataIndex : 'kfopttime',
			width : 150,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			},
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
			header : '第三方支付平台ID',
			dataIndex : 'ppid'
		}, {
			hidden : true,
			header : '流水号',
			dataIndex : 'platformorders'
		}, {
			hidden : true,
			header : '会员名称',
			dataIndex : 'urealname'
		}, {
			hidden : true,
			header : '银行名称',
			dataIndex : 'bankname'
		}, {
			hidden : true,
			header : '当前状态',
			dataIndex : 'status'
		}, {
			hidden : true,
			header : '会员账号',
			dataIndex : 'uaccount'
		}, {
			hidden : true,
			header : '存入银行卡',
			width : 150,
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
			hidden : true,
			header : '存款方式',
			dataIndex : 'paymethods'
		}, {
			header : '存款时间',
			dataIndex : 'deposittime',
			width : 150,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},  {
			header : '操作前余额',
			dataIndex : 'beforebalance',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '操作后余额',
			dataIndex : 'laterbalance',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
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
			hidden : true,
			header : '财务备注',
			dataIndex : 'cwremarks',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			hidden : true,
			header : '操作财务',
			dataIndex : 'cwname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			hidden : true,
			header : '财务操作时间',
			dataIndex : 'cwopttime',
			width : 150,
			renderer : function(v) {
					return v==null?'':new Date(v).format('Y-m-d H:i:s');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		} ]
	});
	
	/** 查看 */
	rporder.showAction = new Ext.Action({
		text : '查看',
		//text : '<fmt:message key="common.cancel"/>',
		iconCls : 'rporder_show',
		handler : function() {
			rporder.addWindow.setIconClass('rporder_add'); // 设置窗口的样式
			rporder.addWindow.setTitle('额度增减记录信息'); // 设置窗口的名称
			rporder.addWindow.show().center(); // 显示窗口
			rporder.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			var record = rporder.grid.getSelectionModel().getSelected();
			rporder.formPanel.getForm().loadRecord(record);
			rporder.paytypleDisabledCombo.setValue(record.data['paytyple']);
			$("#kfopttimeStr").val(record.data.cwopttime);
		}
	});
	/** 订单类型 */
	rporder.orderTypeAction = new Ext.form.ComboBox({
			hiddenName : 'type',
			id : 'otype',
			triggerAction : 'all',
			mode : 'local',
			store : new Ext.data.ArrayStore({
						fields : ['v', 't'],
						data : Share.map2Ary(rporder.ordertype)
					}),
			valueField : 'v',
			displayField : 't',
			value:'',
			editable : false,
			width:100
	});
	
	rporder.searchParams = function(){
		rporder.store.baseParams.ordertype=$("#otype").prev().val(); //订单类型
	}
	
	rporder.searchFun = function(){
		rporder.store.load({params: rporder.searchParams()});
	}
		
		/** 查询按钮 */
	rporder.searchAction = new Ext.Action({
		text : '查询',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				rporder.searchFun();
			}
	});
	
	/** 顶部工具栏 */
	rporder.tbar = [ rporder.showAction, '-',
		'&nbsp;','订单类型:',rporder.orderTypeAction,'-','&nbsp;',rporder.searchAction
	];
	/** 底部工具条 */
	rporder.bbar = new Ext.PagingToolbar({
		pageSize : rporder.pageSize,
		store : rporder.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', rporder.pageSizeCombo ]
	});
	/** 基本信息-表格 */
	rporder.grid = new Ext.grid.EditorGridPanel({
		title : "余额变更记录",
		store : rporder.store,
		colModel : rporder.colModel,
		selModel : rporder.selModel,
		tbar : rporder.tbar,
		bbar : rporder.bbar,
		autoScroll : 'auto',
		region : 'east',
		width : '65%',
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig:{},
		stripeRows : true
	});
	
	rporder.paytypleDisabledCombo = new Ext.form.ComboBox({
		fieldLabel : '收支类型',
		hiddenName : 'paytyple',
		name : 'paytyple',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(rporder.PAYTYPLEMAP)
		}),
		valueField : 'v',
		displayField : 't',
		allowBlank : true,
		editable : false,
		disabled : false,
		anchor : '99%'
	});

	/** 基本信息-详细信息的form */
	rporder.formPanel = new Ext.form.FormPanel({
		region : 'center',
		autoScroll : true,
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
			fieldLabel : '订单ID',
			id : 'poid',
			name : 'poid',
			anchor : '99%'
		}, {
			readOnly : true,
			fieldLabel : '会员账号',
			allowBlank : true,
			name : 'uaccount',
			anchor : '99%'
		}, {
			fieldLabel : '会员名称',
			allowBlank : true,
			name : 'urealname',
			anchor : '99%'
		},rporder.paytypleDisabledCombo,{
			readOnly : true,
			fieldLabel : '金额',
			allowBlank : true,
			name : 'amount',
			anchor : '99%'
		}, {
			fieldLabel : '客服操作时间',
			allowBlank : true,
			id : 'kfopttimeStr',
			name : 'kfopttimeStr',
			anchor : '99%'
		}, {
			fieldLabel : '客服备注',
			allowBlank : true,
			maxLength : 200,
			xtype : 'textarea',
			name : 'kfremarks',
			anchor : '99%'
		}
		]
	});
	

	/** 编辑新建窗口 */
	rporder.addWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 360,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ rporder.formPanel ],
		buttons : [ {
			text : '关闭',
			handler : function() {
				rporder.addWindow.hide();
			}
		} ]
	});
	
	rporder.alwaysFun = function() {
		Share.resetGrid(rporder.grid);
		rporder.showAction.disable();
	};
	
	// ----------------------会员钱包记录--------------------
	Ext.ns("Ext.market.accountmoney"); // 自定义一个命名空间
	accountmoney = Ext.market.accountmoney; // 定义命名空间的别名
	accountmoney = {
		all : '/manage/accountmoney/queryAccountmoney.do', // 所有会员钱包
		save : "/manage/accountmoney/save.do",// 保存会员钱包
		changeamount : "/manage/accountmoney/changeamount/",// 变更会员钱包的余额
		enable : "/manage/accountmoney/lock/1/",// 解锁会员钱包
		disable : "/manage/accountmoney/lock/0/",// 锁定会员钱包
		pageSize : 30,// 每页显示的记录数
		ordertype : eval('(${fields.ordertype})'),
		STATUSMAP : eval('(${statusMap})')//注意括号
	};
	/** 改变页的combo*/
	accountmoney.pageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				accountmoney.pageSize  = parseInt(comboBox.getValue());
				accountmoney.bbar.pageSize  = parseInt(comboBox.getValue());
				accountmoney.store.baseParams.limit = accountmoney.pageSize;
				accountmoney.store.baseParams.start = 0;
				accountmoney.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	accountmoney.pageSize = parseInt(accountmoney.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	accountmoney.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:true,
		baseParams : {
			start : 0,
			limit : accountmoney.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : accountmoney.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'amid','uiid','totalamount','integral','status','createDate','updateDate'
		     ,'account','accounttype','uname','identitycard','phone','email','qq','birthday','grade' ]),
		listeners : {
			'load' : function(store, records, options) {
				accountmoney.alwaysFun();
			}
		}
	});
	/** 基本信息-选择模式 */
	accountmoney.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				accountmoney.rewordAction.enable();
				accountmoney.punishAction.enable();
				if(record.data.status==0){
					accountmoney.enableAction.enable();
				}
				if(record.data.status==1){
					accountmoney.disableAction.enable();
				}
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				accountmoney.rewordAction.enable();
				accountmoney.punishAction.enable();
				accountmoney.enableAction.disable();
				accountmoney.disableAction.disable();
			}
		}
	});
	/** 基本信息-数据列 */
	accountmoney.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 90
		},
		/*
		 'amid','uiid','totalamount','status','createDate','updateDate'
		     ,'account','accounttype','uname','identitycard','phone','email','qq','birthday','grade'
		 */
		columns : [ accountmoney.selModel, {
			hidden : true,
			header : '主键ID',
			dataIndex : 'amid'
		}, {
			hidden : true,
			header : '会员ID',
			dataIndex : 'uiid'
		}, {
			header : '余额',
			dataIndex : 'totalamount'
		},{
			header : '积分',
			dataIndex : 'integral'
		},  {
			header : '状态',
			dataIndex : 'status',
			renderer : function(v) {
				return Share.map(v, accountmoney.STATUSMAP, '');
			}	
		}, {
			header : '会员账号',
			dataIndex : 'account'
		}, {
			header : '会员姓名',
			dataIndex : 'uname'
		}, {
			hidden : true,
			header : '号码',
			dataIndex : 'phone'
		}, {
			hidden : true,
			header : 'e-mail',
			dataIndex : 'email'
		}, {
			hidden : true,
			header : 'qq号码',
			dataIndex : 'qq'
		}, {
			header : '更新时间',
			dataIndex : 'updateDate',
			width : 150,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			}
		}, {
			header : '创建时间',
			dataIndex : 'createDate',
			width : 150,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			}
		} ]
	});
	
	/** 赠送 */
	accountmoney.rewordAction = new Ext.Action({
		text : '加款',
		//text : '<fmt:message key="common.cancel"/>',
		iconCls : 'Coinsadd',
		handler : function() {
			var record = accountmoney.grid.getSelectionModel().getSelected();
			accountmoney.addWindow.setIconClass('Coinsadd'); // 设置窗口的样式
			accountmoney.addWindow.setTitle('加款'); // 设置窗口的名称
			accountmoney.addWindow.show().center(); // 显示窗口
			accountmoney.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			accountmoney.formPanel.getForm().loadRecord(record);
			accountmoney.formPanel.getForm().findField("paytyple").setValue(2);
		}
	});
	/** 扣款 */
	accountmoney.punishAction = new Ext.Action({
		text : '扣款',
		//text : '<fmt:message key="common.cancel"/>',
		iconCls : 'Coinsdelete',
		handler : function() {
			var record = accountmoney.grid.getSelectionModel().getSelected();
			accountmoney.addWindow.setIconClass('Coinsdelete'); // 设置窗口的样式
			accountmoney.addWindow.setTitle('扣款'); // 设置窗口的名称
			accountmoney.addWindow.show().center(); // 显示窗口
			accountmoney.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			accountmoney.formPanel.getForm().loadRecord(record);
			accountmoney.formPanel.getForm().findField("paytyple").setValue(3);
		}
	});
	
	/** 解锁 */
	accountmoney.enableAction = new Ext.Action({
		text : '解锁',
		iconCls : 'Lockopen',
		disabled : true,
		handler : function() {
			accountmoney.enableFun();
		}
	});
	/** 锁定 */
	accountmoney.disableAction = new Ext.Action({
		text : '锁定',
		iconCls : 'Lock',
		disabled : true,
		handler : function() {
			accountmoney.disableFun();
		}
	});
	/** 查询 */
	accountmoney.searchField = new Ext.ux.form.SearchField({
		store : accountmoney.store,
		width : 110,
		paramName : 'account',
		emptyText : '请输入会员账号',
		style : 'margin-left: 5px;'
	});
	/** 顶部工具栏 */
	accountmoney.tbar = [ accountmoney.rewordAction, '-', accountmoney.punishAction, 
	                        //'-', accountmoney.deleteAction, 
	                '-', accountmoney.enableAction, '-', accountmoney.disableAction,
	                '-', accountmoney.searchField
	                ];
	/** 底部工具条 */
	accountmoney.bbar = new Ext.PagingToolbar({
		pageSize : accountmoney.pageSize,
		store : accountmoney.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', accountmoney.pageSizeCombo ]
	});
	/** 基本信息-表格 */
	accountmoney.grid = new Ext.grid.GridPanel({
		title : "会员钱包",
		store : accountmoney.store,
		colModel : accountmoney.colModel,
		selModel : accountmoney.selModel,
		tbar : accountmoney.tbar,
		bbar : accountmoney.bbar,
		autoScroll : 'auto',
		region : 'center',
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig:{},
		stripeRows : true,
		listeners : {
			'cellclick' : function(obj, rowIndex, columnIndex, e) {
				var record = accountmoney.grid.getSelectionModel().getSelected();
				if (record) {
					// 更新明细记录
					rporder.store.baseParams.uiid = record.data.uiid;
					rporder.store.reload();
				}
			}
		}
	});
	
	accountmoney.statusCombo = new Ext.form.ComboBox({
		fieldLabel : '状态',
		hiddenName : 'status',
		name : 'status',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(accountmoney.STATUSMAP)
				}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		value : "0",
		anchor : '99%'
	});
	
	accountmoney.orderTypeCombo = new Ext.form.ComboBox({
		fieldLabel : '订单类型',
		hiddenName : 'ordertype',
		name : 'ordertype',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(accountmoney.ordertype)
				}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		value : "0",
		anchor : '99%'
	});

	/** 基本信息-详细信息的form */
	accountmoney.formPanel = new Ext.form.FormPanel({
		region : 'center',
		autoScroll : true,
		border: false,
        style: 'border-bottom:0px;',
        bodyStyle: 'padding:10px;background-color:transparent;',
		labelwidth : 70,
		defaultType : 'textfield',
		/*
		 'amid','uiid','totalamount','status','createDate','updateDate'
		     ,'account','accounttype','uname','identitycard','phone','email','qq','birthday','grade'
		 */
		items : [ {
			xtype : 'hidden',
			fieldLabel : '主键ID',
			name : 'amid'
		}, {
			xtype : 'hidden',
			header : '收支类型',
			name : 'paytyple'
		}, {
			xtype : 'hidden',
			header : '会员ID',
			name : 'uiid'
		}, {
			xtype : 'hidden',
			header : '会员账号',
			name : 'account'
		}, {
			fieldLabel : '金额',
			xtype:'numberfield',
			allowBlank : false,
			name : 'amount',
			anchor : '99%'
		}, accountmoney.orderTypeCombo,{
			allowBlank : false,
			fieldLabel : '备注',
			maxLength : 200,
			xtype : 'textarea',
			name : 'kfremarks',
			anchor : '99%'
		}]
	});
	

	/** 编辑新建窗口 */
	accountmoney.addWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 220,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ accountmoney.formPanel ],
		buttons : [ {
			text : '提交',
			handler : function() {
				accountmoney.changeamountFun();
			}
		}, {
			text : '取消',
			handler : function() {
				accountmoney.addWindow.hide();
			}
		} ]
	});
	
	
	accountmoney.alwaysFun = function() {
		Share.resetGrid(accountmoney.grid);
		accountmoney.rewordAction.disable();
		accountmoney.punishAction.disable();
		accountmoney.disableAction.disable();
		accountmoney.enableAction.disable();
	};
	accountmoney.changeamountFun = function() {
		var form = accountmoney.formPanel.getForm();
		if (!form.isValid()) {
			return;
		}
		var paytyple = form.findField("paytyple").getValue();
		// 发送请求
		Share.AjaxRequest({
			url : accountmoney.changeamount+paytyple+".do",
			params : form.getValues(),
			callback : function(json) {
				accountmoney.addWindow.hide();
				accountmoney.alwaysFun();
				accountmoney.store.reload();
			}
		});
	};
	
	accountmoney.enableFun = function(){
		var record = accountmoney.grid.getSelectionModel().getSelected();
		if (record.data.status==1) { // 锁定状态 0未锁定 1锁定
			Ext.Msg.alert('提示', '此记录已经是[未锁定]状态');
			return;
		}
		Ext.Msg.confirm('提示', '确定要[解锁]此记录吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : accountmoney.enable + record.data.amid + ".do",
					callback : function(json) {
						accountmoney.alwaysFun();
						accountmoney.store.reload();
					}
				});
			}
		});
	};
	accountmoney.disableFun = function(){
		var record = accountmoney.grid.getSelectionModel().getSelected();
		if (record.data.status==0) {
			Ext.Msg.alert('提示', '此记录已经是[锁定]状态');
			return;
		}
		Ext.Msg.confirm('提示', '确定要[锁定]此记录吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : accountmoney.disable + record.data.amid + ".do",
					callback : function(json) {
						accountmoney.alwaysFun();
						accountmoney.store.reload();
					}
				});
			}
		});
	};
	
	// -------------------------接合------------------------
	
	accountmoney.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'border',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
		items : [ accountmoney.grid, rporder.grid ]
	});
</script>
