<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	Ext.ns("Ext.market.companycard"); // 自定义一个命名空间
	companycard = Ext.market.companycard; // 定义命名空间的别名
	companycard = {
		all : '/manage/companycard/queryCompanycard.do', // 所有公司银行卡
		save : "/manage/companycard/save.do",// 保存公司银行卡
		del : "/manage/companycard/del/",// 删除公司银行卡
		enable : "/manage/companycard/enable/",// 禁用公司银行卡
		disable : "/manage/companycard/disable/",// 启用公司银行卡
		pageSize : 20,// 每页显示的记录数
		STATUSMAP : eval('(${statusMap})')//注意括号
	};
	/** 改变页的combo*/
	companycard.pageSizeCombo = new Share.pageSizeCombo({
		value : '20',
		listeners : {
			select : function(comboBox) {
				companycard.pageSize  = parseInt(comboBox.getValue());
				companycard.bbar.pageSize  = parseInt(comboBox.getValue());
				companycard.store.baseParams.limit = companycard.pageSize;
				companycard.store.baseParams.start = 0;
				companycard.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	companycard.pageSize = parseInt(companycard.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	companycard.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:true,
		baseParams : {
			start : 0,
			limit : companycard.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : companycard.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'ccid','ccholder','bankname','ccno','bankaddr','remarks','status','createuserid','createusername','createtime','updateuserid','updateusername','updatetime' ]),
		listeners : {
			'load' : function(store, records, options) {
				companycard.alwaysFun();
			}
		}
	});
	//companycard.store.load(); 
	/** 基本信息-选择模式 */
	companycard.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				companycard.deleteAction.enable();
				companycard.editAction.enable();
				companycard.enableAction.enable();
				companycard.disableAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				companycard.deleteAction.disable();
				companycard.editAction.disable();
				companycard.enableAction.disable();
				companycard.disableAction.disable();
			}
		}
	});
	/** 基本信息-数据列 */
	companycard.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 110
		},
		//'ccid','ccholder','bankname','ccno','bankaddr','remarks','status','createuserid','createusername','createtime','updateuserid','updateusername','updatetime'
		columns : [ companycard.selModel, {
			hidden : true,
			header : '公司银行卡ID',
			dataIndex : 'ccid'
		}, {
			header : '银行名称',
			dataIndex : 'bankname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '卡号',
			dataIndex : 'ccno',
			width : 200,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '开户行地址',
			width : 200,
			dataIndex : 'bankaddr',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '是否锁定',
			width : 60,
			dataIndex : 'status',
			renderer : function(v) {
				return Share.map(v, companycard.STATUSMAP, '');
			}
		}, {
			header : '创建者',
			width : 80,
			dataIndex : 'createusername',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '创建时间',
			dataIndex : 'createtime',
			width : 130,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '更新时间',
			dataIndex : 'updatetime',
			width : 130,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		} ]
	});
	/** 新建 */
	companycard.addAction = new Ext.Action({
		text : '新建',
		//text : '<fmt:message key="common.cancel"/>',
		iconCls : 'Add',
		handler : function() {
			companycard.addWindow.setIconClass('Add'); // 设置窗口的样式
			companycard.addWindow.setTitle('新增银行卡'); // 设置窗口的名称
			companycard.addWindow.show().center(); // 显示窗口
			companycard.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			companycard.tabPanel.activate(companycard.formPanel);
		}
	});
	/** 编辑 */
	companycard.editAction = new Ext.Action({
		text : '编辑',
		iconCls : 'Applicationedit',
		disabled : true,
		handler : function() {
			var record = companycard.grid.getSelectionModel().getSelected();
			companycard.addWindow.setIconClass('Applicationedit'); // 设置窗口的样式
			companycard.addWindow.setTitle('编辑'); // 设置窗口的名称
			companycard.addWindow.show().center();
			companycard.formPanel.getForm().reset();
			companycard.formPanel.getForm().loadRecord(record);
		}
	});
	/** 删除 */
	companycard.deleteAction = new Ext.Action({
		text : '删除',
		iconCls : 'Cross',
		disabled : true,
		handler : function() {
			companycard.delFun();
		}
	});
	/** 解锁 */
	companycard.enableAction = new Ext.Action({
		text : '解锁',
		iconCls : 'Lockopen',
		disabled : true,
		handler : function() {
			companycard.enableFun();
		}
	});
	/** 锁定 */
	companycard.disableAction = new Ext.Action({
		text : '锁定',
		iconCls : 'Lock',
		disabled : true,
		handler : function() {
			companycard.disableFun();
		}
	});
	/** 查询 */
	companycard.searchField = new Ext.ux.form.SearchField({
		store : companycard.store,
		paramName : 'ccno',
		emptyText : '请输入银行卡卡号',
		style : 'margin-left: 5px;'
	});
	/** 顶部工具栏 */
	companycard.tbar = [ companycard.addAction, '-', companycard.editAction, '-', companycard.deleteAction, '-',companycard.searchField,
	                '-', companycard.enableAction, '-', companycard.disableAction
	                ];
	/** 底部工具条 */
	companycard.bbar = new Ext.PagingToolbar({
		pageSize : companycard.pageSize,
		store : companycard.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', companycard.pageSizeCombo ]
	});
	/** 基本信息-表格 */
	companycard.grid = new Ext.grid.EditorGridPanel({
		store : companycard.store,
		colModel : companycard.colModel,
		selModel : companycard.selModel,
		tbar : companycard.tbar,
		bbar : companycard.bbar,
		autoScroll : 'auto',
		region : 'center',
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig:{},
		stripeRows : true
	});
	companycard.statusCombo = new Ext.form.ComboBox({
		fieldLabel : '是否锁定',
		hiddenName : 'status',
		name : 'status',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
			fields : ['v', 't'],
			data : Share.map2Ary(companycard.STATUSMAP)
		}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		value : "0",
		anchor : '99%'
	});
	
	/** 基本信息-详细信息的form */
	companycard.formPanel = new Ext.form.FormPanel({
		autoScroll : false,
		border: false,
        style: 'border-bottom:0px;',
        bodyStyle: 'padding:10px;background-color:transparent;',
		labelwidth : 70,
		defaultType : 'textfield',
		//'ccid','ccholder','bankname','ccno','bankaddr','remarks','status','createuserid','createusername','createtime','updateuserid','updateusername','updatetime'
		items : [ {
			xtype : 'hidden',
			fieldLabel : 'ID',
			name : 'ccid'
		}, {
			fieldLabel : '开户人姓名',
			name : 'ccholder',
			anchor : '99%'
		}, {
			fieldLabel : '开户银行',
			name : 'bankname',
			anchor : '99%'
		}, {
			fieldLabel : '银行卡号',
			name : 'ccno',
			anchor : '99%'
		}, companycard.statusCombo,
		{
			fieldLabel : '开户行地址',
			maxLength : 200,
			xtype : 'textarea',
			name : 'bankaddr',
			anchor : '99%'
		}, {
			fieldLabel : '备注',
			maxLength : 200,
			xtype : 'textarea',
			name : 'remarks',
			anchor : '99%'
		} 
		]
	});
	/** 编辑新建窗口 */
	companycard.addWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 340,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ companycard.formPanel ],
		buttons : [ {
			text : '保存',
			handler : function() {
				companycard.saveFun();
			}
		}, {
			text : '重置',
			handler : function() {
				var form = companycard.formPanel.getForm();
				var ccid = form.findField("ccid").getValue();
				var account = form.findField("account").getValue();
				form.reset();
				if (ccid != '')
					form.findField("ccid").setValue(ccid);
				if (account != '')
					form.findField("account").setValue(account);
			}
		} ]
	});
	companycard.alwaysFun = function() {
		Share.resetGrid(companycard.grid);
		companycard.deleteAction.disable();
		companycard.editAction.disable();
	};
	companycard.saveFun = function() {
		var form = companycard.formPanel.getForm();
		if (!form.isValid()) {
			return;
		}
		// 发送请求
		Share.AjaxRequest({
			url : companycard.save,
			params : form.getValues(),
			callback : function(json) {
				companycard.addWindow.hide();
				companycard.alwaysFun();
				companycard.store.reload();
				
				//fix bug 打开页面，编辑，不点击角色的tab。直接点击保存，再点击新建，在保存，会直接提交。
				//	companycard.tabPanel.activate(companycard.roleGrid);
				//Share.resetGrid(companycard.roleGrid);
			}
		});
	};
	
	companycard.delFun = function() {
		var record = companycard.grid.getSelectionModel().getSelected();
		Ext.Msg.confirm('提示', '确定要删除这条记录吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : companycard.del + record.data.ccid + ".do",
					callback : function(json) {
						companycard.alwaysFun();
						companycard.store.reload();
					}
				});
			}
		});
	};

	companycard.enableFun = function(){
		var record = companycard.grid.getSelectionModel().getSelected();
		if (record.data.status==0) {
			Ext.Msg.alert('提示', '此银行卡已经是未锁定状态');
			return;
		}
		Ext.Msg.confirm('提示', '确定要解锁此银行卡吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : companycard.enable + record.data.ccid + ".do",
					callback : function(json) {
						companycard.alwaysFun();
						companycard.store.reload();
					}
				});
			}
		});
	};
	companycard.disableFun = function(){
		var record = companycard.grid.getSelectionModel().getSelected();
		if (record.data.status==1) {
			Ext.Msg.alert('提示', '此银行卡已经是锁定状态');
			return;
		}
		Ext.Msg.confirm('提示', '确定要锁定此银行卡吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : companycard.disable + record.data.ccid + ".do",
					callback : function(json) {
						companycard.alwaysFun();
						companycard.store.reload();
					}
				});
			}
		});
	};
	companycard.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'border',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
		items : [ companycard.grid ]
	});
</script>
