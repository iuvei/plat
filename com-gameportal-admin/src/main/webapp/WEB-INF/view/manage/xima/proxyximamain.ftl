<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	// ----------------------明细记录--------------------
	Ext.ns("Ext.market.proxyximadetail"); // 自定义一个命名空间
	proxyximadetail = Ext.market.proxyximadetail; // 定义命名空间的别名
	proxyximadetail = {
		all : '/manage/proxyximadetail/queryProxyximadetail.do', // 所有代理洗码明细
		save : "/manage/proxyximadetail/save.do",// 保存代理洗码明细
		del : "/manage/proxyximadetail/del/",// 删除代理洗码明细
		pageSize : 20,// 每页显示的记录数
		OPTTYPEMAP : eval('(${opttypeMap})')//注意括号
	};
	/** 改变页的combo*/
	proxyximadetail.pageSizeCombo = new Share.pageSizeCombo({
		value : '20',
		listeners : {
			select : function(comboBox) {
				proxyximadetail.pageSize  = parseInt(comboBox.getValue());
				proxyximadetail.bbar.pageSize  = parseInt(comboBox.getValue());
				proxyximadetail.store.baseParams.limit = proxyximadetail.pageSize;
				proxyximadetail.store.baseParams.start = 0;
				proxyximadetail.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	proxyximadetail.pageSize = parseInt(proxyximadetail.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	proxyximadetail.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:true,
		baseParams : {
			uiid : -1,
			start : 0,
			limit : proxyximadetail.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : proxyximadetail.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'pxdid','uiid','amount','ymdstart','ymdend','freetime','optuiid','optusername','opttype','opttime','paramlog' ]),
		listeners : {
			'load' : function(store, records, options) {
				proxyximadetail.alwaysFun();
			}
		}
	});
	/** 基本信息-选择模式 */
	proxyximadetail.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				proxyximadetail.deleteAction.enable();
				proxyximadetail.showAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				proxyximadetail.deleteAction.disable();
				proxyximadetail.showAction.disable();
			}
		}
	});
	/** 基本信息-数据列 */
	proxyximadetail.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 90
		},
		// 'pxdid','uiid','amount','ymdstart','ymdend','freetime','optuiid','optusername','opttype','opttime','paramlog'
		columns : [ proxyximadetail.selModel, {
			hidden : true,
			header : '主键ID',
			dataIndex : 'pxdid'
		}, {
			hidden : true,
			header : '代理ID',
			dataIndex : 'uiid'
		}, {
			hidden : true,
			header : '操作人ID',
			dataIndex : 'optuiid'
		}, {
			header : '操作人名称',
			dataIndex : 'optusername',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '操作类型',
			dataIndex : 'opttype',
			renderer : function(v) {
				return Share.map(v, proxyximadetail.OPTTYPEMAP, '');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '操作时间',
			dataIndex : 'opttime',
			width : 150,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '返水金额（元）',
			dataIndex : 'amount',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '洗码开始日期',
			dataIndex : 'ymdstart',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '洗码结束日期',
			dataIndex : 'ymdend',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '自动解锁时间',
			dataIndex : 'freetime',
			width : 150,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '参数日志',
			dataIndex : 'paramlog',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		} ]
	});
	
	/** 查看 */
	proxyximadetail.showAction = new Ext.Action({
		text : '查看',
		//text : '<fmt:message key="common.cancel"/>',
		iconCls : 'proxyximadetail_add',
		handler : function() {
			proxyximadetail.addWindow.setIconClass('proxyximadetail_add'); // 设置窗口的样式
			proxyximadetail.addWindow.setTitle('查看'); // 设置窗口的名称
			proxyximadetail.addWindow.show().center(); // 显示窗口
			proxyximadetail.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			var record = proxyximadetail.grid.getSelectionModel().getSelected();
			proxyximadetail.formPanel.getForm().loadRecord(record);
		}
	});
	/** 删除 */
	proxyximadetail.deleteAction = new Ext.Action({
		text : '删除',
		iconCls : 'proxyximadetail_delete',
		disabled : true,
		handler : function() {
			proxyximadetail.delFun();
		}
	});
	/** 查询 */
	proxyximadetail.searchField = new Ext.ux.form.SearchField({
		store : proxyximadetail.store,
		paramName : 'optusername',
		emptyText : '请输入操作人名称',
		style : 'margin-left: 5px;'
	});
	/** 顶部工具栏 */
	proxyximadetail.tbar = [ proxyximadetail.showAction
	                          //, '-', proxyximadetail.deleteAction
	                           , '-',proxyximadetail.searchField
	                ];
	/** 底部工具条 */
	proxyximadetail.bbar = new Ext.PagingToolbar({
		pageSize : proxyximadetail.pageSize,
		store : proxyximadetail.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', proxyximadetail.pageSizeCombo ]
	});
	/** 基本信息-表格 */
	proxyximadetail.grid = new Ext.grid.EditorGridPanel({
		title : "洗码明细记录",
		store : proxyximadetail.store,
		colModel : proxyximadetail.colModel,
		selModel : proxyximadetail.selModel,
		tbar : proxyximadetail.tbar,
		bbar : proxyximadetail.bbar,
		autoScroll : 'auto',
		region : 'east',
		width : '50%',
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig:{},
		stripeRows : true
	});
	
	proxyximadetail.opttypeDisabledCombo = new Ext.form.ComboBox({
		fieldLabel : '操作',
		hiddenName : 'opttype',
		name : 'opttype',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(proxyximadetail.OPTTYPEMAP)
				}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		disabled : true,
		anchor : '99%'
	});

	/** 基本信息-详细信息的form */
	proxyximadetail.formPanel = new Ext.form.FormPanel({
		region : 'center',
		autoScroll : true,
		frame : false,
		title : '代理洗码明细记录信息',
		bodyStyle : 'padding:10px;border:0px',
		labelwidth : 70,
		defaultType : 'textfield',
		// 'pxdid','uiid','amount','ymdstart','ymdend','freetime','optuiid','optusername','opttype','opttime','paramlog'
		items : [ {
			xtype : 'hidden',
			fieldLabel : 'ID',
			name : 'pxdid'
		}, {
			xtype : 'hidden',
			fieldLabel : '代理ID',
			name : 'uiid'
		}, {
			xtype : 'hidden',
			fieldLabel : '操作人ID',
			name : 'optuiid'
		}, {
			disabled : true,
			fieldLabel : '返水金额（元）',
			name : 'amount'
		}, {
			disabled : true,
			fieldLabel : '洗码开始日期',
			name : 'ymdstart'
		}, {
			disabled : true,
			fieldLabel : '洗码结束日期',
			name : 'ymdend'
		}, {
			disabled : true,
			fieldLabel : '操作时间',
			name : 'opttime'
		}, {
			disabled : true,
			fieldLabel : '自动解锁时间',
			name : 'freetime'
		}, proxyximadetail.opttypeDisabledCombo, {
			disabled : true,
			fieldLabel : '参数日志',
			maxLength : 200,
			xtype : 'textarea',
			anchor : '99%',
			name : 'paramlog'
		}
		]
	});
	

	/** 编辑新建窗口 */
	proxyximadetail.addWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 360,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ proxyximadetail.formPanel ],
		buttons : [ {
			text : '关闭',
			handler : function() {
				proxyximadetail.addWindow.hide();
			}
		} ]
	});
	
	
	proxyximadetail.alwaysFun = function() {
		Share.resetGrid(proxyximadetail.grid);
		proxyximadetail.deleteAction.disable();
		proxyximadetail.showAction.disable();
	};
	
	proxyximadetail.delFun = function() {
		var record = proxyximadetail.grid.getSelectionModel().getSelected();
		Ext.Msg.confirm('提示', '确定要删除这条记录吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : proxyximadetail.del + record.data.pxdid + ".do",
					callback : function(json) {
						proxyximadetail.alwaysFun();
						proxyximadetail.store.reload();
					}
				});
			}
		});
	};
	// ----------------------总记录--------------------
	Ext.ns("Ext.market.proxyximamain"); // 自定义一个命名空间
	proxyximamain = Ext.market.proxyximamain; // 定义命名空间的别名
	proxyximamain = {
		all : '/manage/proxyximamain/queryProxyximamain.do', // 所有代理洗码总记录
		save : "/manage/proxyximamain/save.do",// 保存代理洗码总记录
		del : "/manage/proxyximamain/del/",// 删除代理洗码总记录
		clear : "/manage/proxyximamain/clear/",// 洗码清零
		force : "/manage/proxyximamain/force/",// 强制洗码
		enable : "/manage/proxyximamain/enable/",// 锁定代理洗码总记录
		disable : "/manage/proxyximamain/disable/",// 解锁代理洗码总记录
		pageSize : 20,// 每页显示的记录数
		LOCKEDMAP : eval('(${lockedMap})')//注意括号
	};
	/** 改变页的combo*/
	proxyximamain.pageSizeCombo = new Share.pageSizeCombo({
		value : '20',
		listeners : {
			select : function(comboBox) {
				proxyximamain.pageSize  = parseInt(comboBox.getValue());
				proxyximamain.bbar.pageSize  = parseInt(comboBox.getValue());
				proxyximamain.store.baseParams.limit = proxyximamain.pageSize;
				proxyximamain.store.baseParams.start = 0;
				proxyximamain.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	proxyximamain.pageSize = parseInt(proxyximamain.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	proxyximamain.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:true,
		baseParams : {
			start : 0,
			limit : proxyximamain.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : proxyximamain.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'pxmid', 'uiid','account','name','total','ymdstart','ymdend','updatetime','locked' ]),
		listeners : {
			'load' : function(store, records, options) {
				proxyximamain.alwaysFun();
			}
		}
	});
	/** 基本信息-选择模式 */
	proxyximamain.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				proxyximamain.deleteAction.enable();
				proxyximamain.forceAction.enable();
				proxyximamain.clearAction.enable();
				if(record.data.locked==1){
					proxyximamain.enableAction.enable();
				}
				if(record.data.locked==0){
					proxyximamain.disableAction.enable();
				}
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				proxyximamain.deleteAction.disable();
				proxyximamain.forceAction.disable();
				proxyximamain.clearAction.disable();
				proxyximamain.enableAction.disable();
				proxyximamain.disableAction.disable();
			}
		}
	});
	/** 基本信息-数据列 */
	proxyximamain.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 100
		},
		// 'pxmid', 'uiid','account','name','total','ymdstart','ymdend','updatetime','locked'
		columns : [ proxyximamain.selModel, {
			hidden : true,
			header : '主键ID',
			dataIndex : 'pxmid'
		}, {
			hidden : true,
			header : '代理ID',
			dataIndex : 'uiid'
		}, {
			header : '状态',
			dataIndex : 'locked',
			renderer : function(v) {
				return Share.map(v, proxyximamain.LOCKEDMAP, '');
			}	
		}, {
			header : '代理账号',
			dataIndex : 'account'
		}, {
			hidden : true,
			header : '代理姓名',
			dataIndex : 'name'
		}, {
			header : '返水总金额（元）',
			dataIndex : 'total'
		}, {
			header : '洗码开始日期',
			dataIndex : 'ymdstart'
		}, {
			header : '洗码结束日期',
			dataIndex : 'ymdend'
		}, {
			header : '更新时间',
			dataIndex : 'updatetime',
			width : 150,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			}
		} ]
	});
	
	/** 新建 */
	proxyximamain.addAction = new Ext.Action({
		text : '新建',
		//text : '<fmt:message key="common.cancel"/>',
		iconCls : 'proxyximamain_add',
		handler : function() {
			proxyximamain.addWindow.setIconClass('proxyximamain_add'); // 设置窗口的样式
			proxyximamain.addWindow.setTitle('新建'); // 设置窗口的名称
			proxyximamain.addWindow.show().center(); // 显示窗口
			proxyximamain.formPanel.getForm().reset(); // 清空表单里面的元素的值.
		}
	});
	/** 删除 */
	proxyximamain.deleteAction = new Ext.Action({
		text : '删除',
		iconCls : 'proxyximamain_delete',
		disabled : true,
		handler : function() {
			proxyximamain.delFun();
		}
	});
	/** 强制洗码 */
	proxyximamain.forceAction = new Ext.Action({
		text : '强制洗码',
		iconCls : 'proxyximamain_force',
		disabled : true,
		handler : function() {
			proxyximamain.forceFun();
		}
	});
	/** 洗码清零 */
	proxyximamain.clearAction = new Ext.Action({
		text : '洗码清零',
		iconCls : 'proxyximamain_clear',
		disabled : true,
		handler : function() {
			proxyximamain.clearFun();
		}
	});
	/** 解锁 */
	proxyximamain.enableAction = new Ext.Action({
		text : '解锁',
		iconCls : 'enable',
		disabled : true,
		handler : function() {
			proxyximamain.enableFun();
		}
	});
	/** 锁定 */
	proxyximamain.disableAction = new Ext.Action({
		text : '锁定',
		iconCls : 'disable',
		disabled : true,
		handler : function() {
			proxyximamain.disableFun();
		}
	});
	/** 查询 */
	proxyximamain.searchField = new Ext.ux.form.SearchField({
		store : proxyximamain.store,
		width : 110,
		paramName : 'account',
		emptyText : '请输入代理账号',
		style : 'margin-left: 5px;'
	});
	/** 顶部工具栏 */
	proxyximamain.tbar = [ proxyximamain.addAction, '-', proxyximamain.forceAction, '-', proxyximamain.clearAction, 
	                        //'-', proxyximamain.deleteAction, 
	                '-', proxyximamain.enableAction, '-', proxyximamain.disableAction,
	                '-', proxyximamain.searchField
	                ];
	/** 底部工具条 */
	proxyximamain.bbar = new Ext.PagingToolbar({
		pageSize : proxyximamain.pageSize,
		store : proxyximamain.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', proxyximamain.pageSizeCombo ]
	});
	/** 基本信息-表格 */
	proxyximamain.grid = new Ext.grid.GridPanel({
		title : "代理洗码总记录",
		store : proxyximamain.store,
		colModel : proxyximamain.colModel,
		selModel : proxyximamain.selModel,
		tbar : proxyximamain.tbar,
		bbar : proxyximamain.bbar,
		autoScroll : 'auto',
		region : 'center',
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig:{},
		stripeRows : true,
		listeners : {
			'cellclick' : function(obj, rowIndex, columnIndex, e) {
				var record = proxyximamain.grid.getSelectionModel().getSelected();
				if (record) {
					// 更新明细记录
					proxyximadetail.store.baseParams.uiid = record.data.uiid;
					proxyximadetail.store.reload();
				}
			}
		}
	});
	
	proxyximamain.lockedCombo = new Ext.form.ComboBox({
		fieldLabel : '状态',
		hiddenName : 'locked',
		name : 'locked',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(proxyximamain.LOCKEDMAP)
				}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		value : "0",
		anchor : '99%'
	});

	/** 基本信息-详细信息的form */
	proxyximamain.formPanel = new Ext.form.FormPanel({
		region : 'center',
		autoScroll : true,
		frame : false,
		title : '代理洗码总记录信息',
		bodyStyle : 'padding:10px;border:0px',
		labelwidth : 70,
		defaultType : 'textfield',
		// 'pxmid', 'uiid','account','name','total','ymdstart','ymdend','updatetime','locked'
		items : [ {
			xtype : 'hidden',
			fieldLabel : 'ID',
			name : 'pxmid'
//		}, {
//			xtype : 'hidden',
//			fieldLabel : '代理ID',
//			name : 'uiid'
//		}, {
//			xtype : 'hidden',
//			fieldLabel : '代理姓名',
//			name : 'name'
//		}, {
//			xtype : 'hidden',
//			fieldLabel : '返水总额',
//			name : 'total'
//		}, {
//			xtype : 'hidden',
//			fieldLabel : '洗码开始日期',
//			name : 'ymdstart'
//		}, {
//			xtype : 'hidden',
//			fieldLabel : '洗码结束日期',
//			name : 'ymdend'
//		}, {
//			xtype : 'hidden',
//			fieldLabel : '记录更新时间',
//			name : 'updatetime'
//		}, {
//			xtype : 'hidden',
//			fieldLabel : '状态',
//			name : 'locked'
		}, {
			fieldLabel : '代理账号',
			name : 'account'
		}
		]
	});
	

	/** 编辑新建窗口 */
	proxyximamain.addWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 200,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ proxyximamain.formPanel ],
		buttons : [ {
			text : '保存',
			handler : function() {
				proxyximamain.saveFun();
			}
		}, {
			text : '取消',
			handler : function() {
				proxyximamain.addWindow.hide();
			}
		} ]
	});
	
	
	proxyximamain.alwaysFun = function() {
		Share.resetGrid(proxyximamain.grid);
		proxyximamain.deleteAction.disable();
		proxyximamain.forceAction.disable();
		proxyximamain.clearAction.disable();
		proxyximamain.disableAction.disable();
		proxyximamain.enableAction.disable();
	};
	proxyximamain.saveFun = function() {
		var form = proxyximamain.formPanel.getForm();
		if (!form.isValid()) {
			return;
		}
//		var puiid = form.findField("puiid").getValue();
//		var accounttype = form.findField("accounttype").getValue();
//		if(1==accounttype){ // 代理代理，必须选择上线代理,总代理只能直接在数据库添加
//			if(!puiid || ''==puiid){
//				Ext.Msg.alert("提示", "请为此代理选择一个上线代理!");
//				return;
//			}
//		}
		// 发送请求
		Share.AjaxRequest({
			url : proxyximamain.save,
			params : form.getValues(),
			callback : function(json) {
				proxyximamain.addWindow.hide();
				proxyximamain.alwaysFun();
				proxyximamain.store.reload();
			}
		});
	};
	
	proxyximamain.delFun = function() {
		var record = proxyximamain.grid.getSelectionModel().getSelected();
		Ext.Msg.confirm('提示', '确定要删除这条记录吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : proxyximamain.del + record.data.pxmid + ".do",
					callback : function(json) {
						proxyximamain.alwaysFun();
						proxyximamain.store.reload();
					}
				});
			}
		});
	};

	proxyximamain.forceFun = function(){
		var record = proxyximamain.grid.getSelectionModel().getSelected();
		if (record.data.locked==1) { // 锁定状态 0未锁定 1锁定
			Ext.Msg.alert('提示', '该记录已被锁定，请先解锁后再操作！');
			return;
		}
		Ext.Msg.confirm('提示', '确定要对此代理['+record.data.account+']进行"强制洗码"操作吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : proxyximamain.force + record.data.pxmid + ".do",
					callback : function(json) {
						proxyximamain.alwaysFun();
						proxyximamain.store.reload();
						proxyximadetail.store.reload();
					}
				});
			}
		});
	};
	proxyximamain.clearFun = function(){
		var record = proxyximamain.grid.getSelectionModel().getSelected();
		if (record.data.locked==1) { // 锁定状态 0未锁定 1锁定
			Ext.Msg.alert('提示', '该记录已被锁定，请先解锁后再操作！');
			return;
		}
		Ext.Msg.confirm('提示', '确定要对此代理['+record.data.account+']进行"洗码清零"操作吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : proxyximamain.clear + record.data.pxmid + ".do",
					callback : function(json) {
						proxyximamain.alwaysFun();
						proxyximamain.store.reload();
						proxyximadetail.store.reload();
					}
				});
			}
		});
	};
	proxyximamain.enableFun = function(){
		var record = proxyximamain.grid.getSelectionModel().getSelected();
		if (record.data.locked==0) { // 锁定状态 0未锁定 1锁定
			Ext.Msg.alert('提示', '此记录已经是[未锁定]状态');
			return;
		}
		Ext.Msg.confirm('提示', '确定要[解锁]此记录吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : proxyximamain.enable + record.data.pxmid + ".do",
					callback : function(json) {
						proxyximamain.alwaysFun();
						proxyximamain.store.reload();
					}
				});
			}
		});
	};
	proxyximamain.disableFun = function(){
		var record = proxyximamain.grid.getSelectionModel().getSelected();
		if (record.data.locked==1) {
			Ext.Msg.alert('提示', '此记录已经是[锁定]状态');
			return;
		}
		Ext.Msg.confirm('提示', '确定要[锁定]此记录吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : proxyximamain.disable + record.data.pxmid + ".do",
					callback : function(json) {
						proxyximamain.alwaysFun();
						proxyximamain.store.reload();
					}
				});
			}
		});
	};
	
	// -------------------------接合------------------------
	
	proxyximamain.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'border',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
		items : [ proxyximamain.grid, proxyximadetail.grid ]
	});
</script>
