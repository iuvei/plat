<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.gamePlatforms"); // 自定义一个命名空间
gamePlatform = Ext.market.gamePlatforms; // 定义命名空间的别名
gamePlatform = {
	all : '/manage/gameplatform/queryGamePlatform.do',// 加载所有
	save : "/manage/gameplatform/saveGamePlatform.do",//保存
	del : "/manage/gameplatform/delGamePlatform/",//删除
	pageSize : 20, // 每页显示的记录数
	ENABLED : eval('(${fields.enabled})')
};


/** 改变页的combo */
gamePlatform.pageSizeCombo = new Share.pageSizeCombo({
			value : '20',
			listeners : {
				select : function(comboBox) {
					gamePlatform.pageSize = parseInt(comboBox.getValue());
					gamePlatform.bbar.pageSize = parseInt(comboBox.getValue());
					gamePlatform.store.baseParams.limit = gamePlatform.pageSize;
					gamePlatform.store.baseParams.start = 0;
					gamePlatform.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
gamePlatform.pageSize = parseInt(gamePlatform.pageSizeCombo.getValue());
/** 基本信息-数据源 */
gamePlatform.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : gamePlatform.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : gamePlatform.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['gpid', 'gpname', 'fullname','domainip', 'domainname', /**'deskey',
					'ciphercode', */'status', 'createDate','updateDate']),
			listeners : {
				'load' : function(store, records, options) {
					gamePlatform.alwaysFun();
				}
			}
		});
/** 基本信息-选择模式 */
gamePlatform.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				gamePlatform.deleteAction.enable();
				gamePlatform.editAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				gamePlatform.alwaysFun();
			}
		}
	});
/** 基本信息-数据列 */
gamePlatform.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [gamePlatform.selModel, {
						hidden : true,
						header : '平台编号',
						dataIndex : 'gpid'
					}, {
						header : '平台名称',
						dataIndex : 'gpname',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '平台全名',
						dataIndex : 'fullname'
					}, {
						header : '登录游戏域名',
						dataIndex : 'domainip',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '平台域名',
						dataIndex : 'domainname',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, 
					/**{
						header : '游戏平台KEY',
						dataIndex : 'deskey',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '游戏平台密码',
						dataIndex : 'ciphercode',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, */{
						header : '状态',
						dataIndex : 'status',
						renderer : function(v) {
							return Share.map(v, gamePlatform.ENABLED, '');
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}]
		});
/** 新建 */
gamePlatform.addAction = new Ext.Action({
			text : '新建',
			iconCls : 'gamePlatform_add',
			handler : function() {
				gamePlatform.addWindow.setIconClass('gamePlatform_add'); // 设置窗口的样式
				gamePlatform.addWindow.setTitle('新建平台'); // 设置窗口的名称
				gamePlatform.addWindow.show().center(); // 显示窗口
				gamePlatform.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			}
		});
/** 编辑 */
gamePlatform.editAction = new Ext.Action({
			text : '编辑',
			iconCls : 'gamePlatform_edit',
			disabled : true,
			handler : function() {
				var record = gamePlatform.grid.getSelectionModel().getSelected();
				gamePlatform.addWindow.setIconClass('gamePlatform_edit'); // 设置窗口的样式
				gamePlatform.addWindow.setTitle('编辑平台'); // 设置窗口的名称
				gamePlatform.addWindow.show().center();
				gamePlatform.formPanel.getForm().reset();
				gamePlatform.formPanel.getForm().loadRecord(record);
			}
		});
/** 删除 */
gamePlatform.deleteAction = new Ext.Action({
			text : '删除',
			iconCls : 'gamePlatform_delete',
			disabled : true,
			handler : function() {
				gamePlatform.delFun();
			}
		});
/** 查询 */
gamePlatform.searchField = new Ext.ux.form.SearchField({
			store : gamePlatform.store,
			paramName : 'gpname',
			emptyText : '请输入游戏平台名称',
			style : 'margin-left: 5px;'
		});
/** 顶部工具栏 */
gamePlatform.tbar = [gamePlatform.addAction, '-', gamePlatform.editAction, '-',
		gamePlatform.deleteAction, '-', gamePlatform.searchField];
/** 底部工具条 */
gamePlatform.bbar = new Ext.PagingToolbar({
			pageSize : gamePlatform.pageSize,
			store : gamePlatform.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', gamePlatform.pageSizeCombo]
		});
/** 基本信息-表格 */
gamePlatform.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : gamePlatform.store,
			colModel : gamePlatform.colModel,
			selModel : gamePlatform.selModel,
			tbar : gamePlatform.tbar,
			bbar : gamePlatform.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'gamePlatformDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});

gamePlatform.statusCombo = new Ext.form.ComboBox({
			fieldLabel : '状态',
			hiddenName : 'status',
			name : 'status',
			triggerAction : 'all',
			mode : 'local',
			store : new Ext.data.ArrayStore({
						fields : ['v', 't'],
						data : Share.map2Ary(gamePlatform.ENABLED)
					}),
			valueField : 'v',
			displayField : 't',
			allowBlank : false,
			editable : false,
			value : "1",
			anchor : '99%'
		});
		
/** 基本信息-详细信息的form */
gamePlatform.formPanel = new Ext.form.FormPanel({
			frame : true,
			//title : '平台信息',
			bodyStyle : 'padding:10px;border:0px',
			labelwidth : 50,
			defaultType : 'textfield',
			items : [{
						xtype : 'hidden',
						fieldLabel : 'ID',
						name : 'gpid',
						anchor : '99%'
					}, {
						fieldLabel : '平台名称',
						maxLength : 64,
						allowBlank : false,
						name : 'gpname',
						anchor : '99%'
					},{
						fieldLabel : '平台全名',
						maxLength : 64,
						allowBlank : false,
						name : 'fullname',
						anchor : '99%'
					},{
						fieldLabel : '平台IP',
						maxLength : 64,
						// allowBlank : false,
						name : 'domainip',
						anchor : '99%'
					},{
						fieldLabel : '平台域名',
						maxLength : 64,
						// allowBlank : false,
						name : 'domainname',
						anchor : '99%'
					},
					/**{
						fieldLabel : '平台KEY',
						maxLength : 256,
						// allowBlank : false,
						name : 'deskey',
						anchor : '99%'
					},{
						fieldLabel : '平台密码',
						maxLength : 64,
						// allowBlank : false,
						name : 'ciphercode',
						anchor : '99%'
					},*/gamePlatform.statusCombo]
		});
/** 编辑新建窗口 */
gamePlatform.addWindow = new Ext.Window({
			layout : 'fit',
			width : 500,
			height : 250,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [gamePlatform.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							gamePlatform.saveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = gamePlatform.formPanel.getForm();
							var id = form.findField("gpid").getValue();
							form.reset();
							if (id != '')
								form.findField("gpid").setValue(id);
						}
					}]
		});
gamePlatform.alwaysFun = function() {
	Share.resetGrid(gamePlatform.grid);
	gamePlatform.deleteAction.disable();
	gamePlatform.editAction.disable();
};
gamePlatform.saveFun = function() {
	var form = gamePlatform.formPanel.getForm();
	if (!form.isValid()) {
		return;
	}
	// 发送请求
	Share.AjaxRequest({
				url : gamePlatform.save,
				params : form.getValues(),
				callback : function(json) {
					gamePlatform.addWindow.hide();
					gamePlatform.alwaysFun();
					gamePlatform.store.reload();
				}
			});
};
gamePlatform.delFun = function() {
	var record = gamePlatform.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '你真的要删除选中游戏平台吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : gamePlatform.del + record.data.gpid +".do",
								callback : function(json) {
									gamePlatform.alwaysFun();
									gamePlatform.store.reload();
								}
							});
				}
			});
};
gamePlatform.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [gamePlatform.grid]
		});
</script>