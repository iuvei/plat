<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.smsPlatBlacklists"); // 自定义一个命名空间
smsPlatBlacklist = Ext.market.smsPlatBlacklists; // 定义命名空间的别名
smsPlatBlacklist = {
	all : '/manage/smsplatinfo/blacklist/querySmsPlatBlacklist.do',// 加载所有
	save : "/manage/smsplatinfo/blacklist/saveSmsPlatBlacklist.do",//保存
	del : "/manage/smsplatinfo/blacklist/delSmsPlatBlacklist/",//删除
	pageSize : 20, // 每页显示的记录数
	ENABLED : eval('(${fields.enabled})'),
	SMSPLATMAP : eval('(${smsPlatMap})')
};

/** 改变页的combo */
smsPlatBlacklist.pageSizeCombo = new Share.pageSizeCombo({
			value : '20',
			listeners : {
				select : function(comboBox) {
					smsPlatBlacklist.pageSize = parseInt(comboBox.getValue());
					smsPlatBlacklist.bbar.pageSize = parseInt(comboBox.getValue());
					smsPlatBlacklist.store.baseParams.limit = smsPlatBlacklist.pageSize;
					smsPlatBlacklist.store.baseParams.start = 0;
					smsPlatBlacklist.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
smsPlatBlacklist.pageSize = parseInt(smsPlatBlacklist.pageSizeCombo.getValue());
/** 基本信息-数据源 */
smsPlatBlacklist.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : smsPlatBlacklist.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : smsPlatBlacklist.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['spbid', 'spiid', 'mobile', 'createtime']),
			listeners : {
				'load' : function(store, records, options) {
					smsPlatBlacklist.alwaysFun();
				}
			}
		});
/** 基本信息-选择模式 */
smsPlatBlacklist.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				smsPlatBlacklist.deleteAction.enable();
				smsPlatBlacklist.editAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				smsPlatBlacklist.alwaysFun();
			}
		}
	});
/** 基本信息-数据列 */
smsPlatBlacklist.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [smsPlatBlacklist.selModel, {
						hidden : true,
						header : '短信平台黑名单编号',
						dataIndex : 'spbid'
					}, {
						header : '短信平台',
						dataIndex : 'spiid',
						renderer : function(v) {
							return Share.map(v, smsPlatBlacklist.SMSPLATMAP, '');
						}
					}, {
						header : '号码',
						dataIndex : 'mobile'
					}]
		});
/** 新建 */
smsPlatBlacklist.addAction = new Ext.Action({
			text : '新建',
			iconCls : 'smsPlatBlacklist_add',
			handler : function() {
				smsPlatBlacklist.addWindow.setIconClass('smsPlatBlacklist_add'); // 设置窗口的样式
				smsPlatBlacklist.addWindow.setTitle('新建黑名单'); // 设置窗口的名称
				smsPlatBlacklist.addWindow.show().center(); // 显示窗口
				smsPlatBlacklist.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			}
		});
/** 编辑 */
smsPlatBlacklist.editAction = new Ext.Action({
			text : '编辑',
			iconCls : 'smsPlatBlacklist_edit',
			disabled : true,
			handler : function() {
				var record = smsPlatBlacklist.grid.getSelectionModel().getSelected();
				smsPlatBlacklist.addWindow.setIconClass('smsPlatBlacklist_edit'); // 设置窗口的样式
				smsPlatBlacklist.addWindow.setTitle('编辑黑名单'); // 设置窗口的名称
				smsPlatBlacklist.addWindow.show().center();
				smsPlatBlacklist.formPanel.getForm().reset();
				smsPlatBlacklist.formPanel.getForm().loadRecord(record);
			}
		});
/** 删除 */
smsPlatBlacklist.deleteAction = new Ext.Action({
			text : '删除',
			iconCls : 'smsPlatBlacklist_delete',
			disabled : true,
			handler : function() {
				smsPlatBlacklist.delFun();
			}
		});
/** 查询 */
smsPlatBlacklist.searchField = new Ext.ux.form.SearchField({
			store : smsPlatBlacklist.store,
			paramName : 'mobile',
			emptyText : '请输入黑名单号码',
			style : 'margin-left: 5px;'
		});
/** 顶部工具栏 */
smsPlatBlacklist.tbar = [smsPlatBlacklist.addAction, '-', smsPlatBlacklist.editAction, '-',
		smsPlatBlacklist.deleteAction, '-', smsPlatBlacklist.searchField];
/** 底部工具条 */
smsPlatBlacklist.bbar = new Ext.PagingToolbar({
			pageSize : smsPlatBlacklist.pageSize,
			store : smsPlatBlacklist.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', smsPlatBlacklist.pageSizeCombo]
		});
/** 基本信息-表格 */
smsPlatBlacklist.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : smsPlatBlacklist.store,
			colModel : smsPlatBlacklist.colModel,
			selModel : smsPlatBlacklist.selModel,
			tbar : smsPlatBlacklist.tbar,
			bbar : smsPlatBlacklist.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'smsPlatBlacklistDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});

smsPlatBlacklist.spiidCombo = new Ext.form.ComboBox({
	fieldLabel : '短信平台',
	hiddenName : 'spiid',
	name : 'spiid',
	triggerAction : 'all',
	mode : 'local',
	store : new Ext.data.ArrayStore({
		fields : ['v', 't'],
		data : Share.map2Ary(smsPlatBlacklist.SMSPLATMAP)
	}),
	valueField : 'v',
	displayField : 't',
	allowBlank : false,
	editable : false,
	value : "1",
	anchor : '99%'
});
		
/** 基本信息-详细信息的form */
smsPlatBlacklist.formPanel = new Ext.form.FormPanel({
			frame : true,
			//title : '平台信息',
			bodyStyle : 'padding:10px;border:0px',
			labelwidth : 50,
			defaultType : 'textfield',
			items : [{
						xtype : 'hidden',
						fieldLabel : 'ID',
						name : 'spbid',
						anchor : '99%'
					}, smsPlatBlacklist.spiidCombo,{
						fieldLabel : '黑名单号码',
						maxLength : 50,
						allowBlank : false,
						name : 'mobile',
						anchor : '99%'
					}]
		});
/** 编辑新建窗口 */
smsPlatBlacklist.addWindow = new Ext.Window({
			layout : 'fit',
			width : 500,
			height : 420,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [smsPlatBlacklist.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							smsPlatBlacklist.saveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = smsPlatBlacklist.formPanel.getForm();
							var id = form.findField("spbid").getValue();
							form.reset();
							if (id != '')
								form.findField("spbid").setValue(id);
						}
					}]
		});
smsPlatBlacklist.alwaysFun = function() {
	Share.resetGrid(smsPlatBlacklist.grid);
	smsPlatBlacklist.deleteAction.disable();
	smsPlatBlacklist.editAction.disable();
};
smsPlatBlacklist.saveFun = function() {
	var form = smsPlatBlacklist.formPanel.getForm();
	if (!form.isValid()) {
		return;
	}
	// 发送请求
	Share.AjaxRequest({
				url : smsPlatBlacklist.save,
				params : form.getValues(),
				callback : function(json) {
					smsPlatBlacklist.addWindow.hide();
					smsPlatBlacklist.alwaysFun();
					smsPlatBlacklist.store.reload();
				}
			});
};
smsPlatBlacklist.delFun = function() {
	var record = smsPlatBlacklist.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '你真的要删除选中的短信平台黑名单吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : smsPlatBlacklist.del + record.data.spbid +".do",
								callback : function(json) {
									smsPlatBlacklist.alwaysFun();
									smsPlatBlacklist.store.reload();
								}
							});
				}
			});
};
smsPlatBlacklist.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [smsPlatBlacklist.grid]
		});
</script>