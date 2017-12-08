<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.smsPlatAccounts"); // 自定义一个命名空间
smsPlatAccount = Ext.market.smsPlatAccounts; // 定义命名空间的别名
smsPlatAccount = {
	all : '/manage/smsplatinfo/account/querySmsPlatAccount.do',// 加载所有
	save : "/manage/smsplatinfo/account/saveSmsPlatAccount.do",//保存
	del : "/manage/smsplatinfo/account/delSmsPlatAccount/",//删除
	enable : "/manage/smsplatinfo/account/enableSmsPlatAccount/",//启用
	disable : "/manage/smsplatinfo/account/disableSmsPlatAccount/",//禁用
	pageSize : 20, // 每页显示的记录数
	ENABLED : eval('(${fields.enabled})'),
	SMSPLATMAP : eval('(${smsPlatMap})')
};

/** 改变页的combo */
smsPlatAccount.pageSizeCombo = new Share.pageSizeCombo({
			value : '20',
			listeners : {
				select : function(comboBox) {
					smsPlatAccount.pageSize = parseInt(comboBox.getValue());
					smsPlatAccount.bbar.pageSize = parseInt(comboBox.getValue());
					smsPlatAccount.store.baseParams.limit = smsPlatAccount.pageSize;
					smsPlatAccount.store.baseParams.start = 0;
					smsPlatAccount.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
smsPlatAccount.pageSize = parseInt(smsPlatAccount.pageSizeCombo.getValue());
/** 基本信息-数据源 */
smsPlatAccount.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : smsPlatAccount.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : smsPlatAccount.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['spaid','servername','serverport','account','pwd','status','createtime',
			    'createname','updatetime','updatename']
			),
			listeners : {
				'load' : function(store, records, options) {
					smsPlatAccount.alwaysFun();
				}
			}
		});
/** 基本信息-选择模式 */
smsPlatAccount.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				smsPlatAccount.deleteAction.enable();
				smsPlatAccount.editAction.enable();
				smsPlatAccount.enableAction.enable();
				smsPlatAccount.disableAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				smsPlatAccount.alwaysFun();
			}
		}
	});
/** 基本信息-数据列 */
smsPlatAccount.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [smsPlatAccount.selModel, {
						hidden : true,
						header : '短信平台账号编号',
						dataIndex : 'spaid'
					}, {
						header : '短信服务器地址',
						dataIndex : 'servername',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '服务端口',
						dataIndex : 'serverport',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '短信账号',
						dataIndex : 'account',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '短信密码',
						dataIndex : 'pwd',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '状态',
						dataIndex : 'status',
						renderer : function(v) {
							if(v == 0){
								return '<span style="color:red;">停用</span>';
							}else if(v == 2){
								return '<span style="color:green;">启用</span>';
							}else{
								return '<span style="color:green;">启用</span>';
							}
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '创建时间',
						dataIndex : 'createtime',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '创建人',
						dataIndex : 'createname',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '修改时间',
						dataIndex : 'updatetime',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '修改人',
						dataIndex : 'updatename',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}]
		});
/** 新建 */
smsPlatAccount.addAction = new Ext.Action({
			text : '新建',
			iconCls : 'Add',
			handler : function() {
				smsPlatAccount.addWindow.setIconClass('Applicationadd'); // 设置窗口的样式
				smsPlatAccount.addWindow.setTitle('新建短信平台'); // 设置窗口的名称
				smsPlatAccount.addWindow.show().center(); // 显示窗口
				smsPlatAccount.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			}
		});
/** 编辑 */
smsPlatAccount.editAction = new Ext.Action({
			text : '编辑',
			iconCls : 'Applicationedit',
			disabled : true,
			handler : function() {
				var record = smsPlatAccount.grid.getSelectionModel().getSelected();
				smsPlatAccount.addWindow.setIconClass('Applicationedit'); // 设置窗口的样式
				smsPlatAccount.addWindow.setTitle('编辑短信平台'); // 设置窗口的名称
				smsPlatAccount.addWindow.show().center();
				smsPlatAccount.formPanel.getForm().reset();
				smsPlatAccount.formPanel.getForm().loadRecord(record);
			}
		});
/** 删除 */
smsPlatAccount.deleteAction = new Ext.Action({
			text : '删除',
			iconCls : 'Delete',
			disabled : true,
			handler : function() {
				smsPlatAccount.delFun();
			}
		});
/** 启用 */
smsPlatAccount.enableAction = new Ext.Action({
	text : '启用',
	iconCls : 'Lockopen',
	disabled : true,
	handler : function() {
		smsPlatAccount.enableFun();
	}
});
/** 禁用 */
smsPlatAccount.disableAction = new Ext.Action({
	text : '禁用',
	iconCls : 'Lockkey',
	disabled : true,
	handler : function() {
		smsPlatAccount.disableFun();
	}
});

/** 查询 */
smsPlatAccount.searchField = new Ext.ux.form.SearchField({
			store : smsPlatAccount.store,
			paramName : 'account',
			emptyText : '请输入账号',
			style : 'margin-left: 5px;'
		});
/** 顶部工具栏 */
smsPlatAccount.tbar = [smsPlatAccount.addAction, '-', smsPlatAccount.editAction, '-',
		smsPlatAccount.deleteAction, '-',smsPlatAccount.enableAction, '-',
		smsPlatAccount.disableAction, '-', smsPlatAccount.searchField];
/** 底部工具条 */
smsPlatAccount.bbar = new Ext.PagingToolbar({
			pageSize : smsPlatAccount.pageSize,
			store : smsPlatAccount.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', smsPlatAccount.pageSizeCombo]
		});
/** 基本信息-表格 */
smsPlatAccount.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : smsPlatAccount.store,
			colModel : smsPlatAccount.colModel,
			selModel : smsPlatAccount.selModel,
			tbar : smsPlatAccount.tbar,
			bbar : smsPlatAccount.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'smsPlatAccountDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});

/** 账号密码 */
smsPlatAccount.password = new Ext.form.TextField({
	fieldLabel : '短信密码',
	inputType : 'password',
	maxLength : 32,
	allowBlank : false,
	name : 'pwd',
	anchor : '99%'
});
smsPlatAccount.statusCombo = new Ext.form.ComboBox({
			fieldLabel : '状态',
			hiddenName : 'status',
			name : 'status',
			triggerAction : 'all',
			mode : 'local',
			store : new Ext.data.ArrayStore({
						fields : ['v', 't'],
						data : Share.map2Ary(smsPlatAccount.ENABLED)
					}),
			valueField : 'v',
			displayField : 't',
			allowBlank : false,
			editable : false,
			value : "1",
			anchor : '99%'
		});
		
/** 基本信息-详细信息的form */
smsPlatAccount.formPanel = new Ext.form.FormPanel({
			autoScroll : false,
			border: false,
        	style: 'border-bottom:0px;',
        	bodyStyle: 'padding:10px;background-color:transparent;',
			labelwidth : 50,
			defaultType : 'textfield',
			items : [{
						xtype : 'hidden',
						fieldLabel : 'ID',
						name : 'spaid',
						anchor : '99%'
					}, {
						fieldLabel : '服务器地址',
						allowBlank : false,
						name : 'servername',
						anchor : '99%'
					}, {
						fieldLabel : '服务器端口',
						allowBlank : false,
						name : 'serverport',
						anchor : '99%'
					}, {
						fieldLabel : '短信账号',
						maxLength : 50,
						allowBlank : false,
						name : 'account',
						anchor : '99%'
					},smsPlatAccount.password,
					smsPlatAccount.statusCombo]
		});
/** 编辑新建窗口 */
smsPlatAccount.addWindow = new Ext.Window({
			layout : 'fit',
			width : 500,
			height : 220,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [smsPlatAccount.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							smsPlatAccount.saveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = smsPlatAccount.formPanel.getForm();
							var id = form.findField("spaid").getValue();
							form.reset();
							if (id != '')
								form.findField("spaid").setValue(id);
						}
					}]
		});
smsPlatAccount.alwaysFun = function() {
	Share.resetGrid(smsPlatAccount.grid);
	smsPlatAccount.deleteAction.disable();
	smsPlatAccount.editAction.disable();
	smsPlatAccount.enableAction.disable();
	smsPlatAccount.disableAction.disable();
};
smsPlatAccount.saveFun = function() {
	var form = smsPlatAccount.formPanel.getForm();
	if (!form.isValid()) {
		return;
	}
	// 发送请求
	Share.AjaxRequest({
				url : smsPlatAccount.save,
				params : form.getValues(),
				callback : function(json) {
					smsPlatAccount.addWindow.hide();
					smsPlatAccount.alwaysFun();
					smsPlatAccount.store.reload();
				}
			});
};
smsPlatAccount.delFun = function() {
	var record = smsPlatAccount.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '你真的要删除选中的短信平台账号吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : smsPlatAccount.del + record.data.spaid +".do",
								callback : function(json) {
									smsPlatAccount.alwaysFun();
									smsPlatAccount.store.reload();
								}
							});
				}
			});
};

smsPlatAccount.enableFun = function(){
	var record = smsPlatAccount.grid.getSelectionModel().getSelected();
	if (record.data.status==1) {
		Ext.Msg.alert('提示', '此短信账号已经是启用状态');
		return;
	}
	Ext.Msg.confirm('提示', '确定要启用此短信账号吗?', function(btn, text) {
		if (btn == 'yes') {
			// 发送请求
			Share.AjaxRequest({
				url : smsPlatAccount.enable + record.data.spaid + ".do",
				callback : function(json) {
					smsPlatAccount.alwaysFun();
					smsPlatAccount.store.reload();
				}
			});
		}
	});
};

smsPlatAccount.disableFun = function(){
	var record = smsPlatAccount.grid.getSelectionModel().getSelected();
	if (record.data.status==0) {
		Ext.Msg.alert('提示', '此短信账号已经是禁用状态');
		return;
	}
	Ext.Msg.confirm('提示', '确定要禁用此短信账号吗?', function(btn, text) {
		if (btn == 'yes') {
			// 发送请求
			Share.AjaxRequest({
				url : smsPlatAccount.disable + record.data.spaid + ".do",
				callback : function(json) {
					smsPlatAccount.alwaysFun();
					smsPlatAccount.store.reload();
				}
			});
		}
	});
};

smsPlatAccount.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [smsPlatAccount.grid]
		});
</script>