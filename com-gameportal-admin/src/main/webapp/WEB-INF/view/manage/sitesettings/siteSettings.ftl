<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.siteSettingss"); // 自定义一个命名空间
siteSettings = Ext.market.siteSettingss; // 定义命名空间的别名
siteSettings = {
	all : '/manage/sitesettings/querySiteSettings.do',// 加载所有
	save : "/manage/sitesettings/saveSiteSettings.do",//保存
	del : "/manage/sitesettings/delSiteSettings/",//删除
	pageSize : 20 // 每页显示的记录数
	//ENABLED : eval('(${fields.enabled})')
};


/** 改变页的combo */
siteSettings.pageSizeCombo = new Share.pageSizeCombo({
			value : '20',
			listeners : {
				select : function(comboBox) {
					siteSettings.pageSize = parseInt(comboBox.getValue());
					siteSettings.bbar.pageSize = parseInt(comboBox.getValue());
					siteSettings.store.baseParams.limit = siteSettings.pageSize;
					siteSettings.store.baseParams.start = 0;
					siteSettings.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
siteSettings.pageSize = parseInt(siteSettings.pageSizeCombo.getValue());
/** 基本信息-数据源 */
siteSettings.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : siteSettings.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : siteSettings.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['ssid', 'sitename', 'siteurl', 'spreadkey', 'lockedcount',
				'isclosed', 'isregister', 'islogin', 'isrecharge', 'isdraw',
				'lowestdraw','highestdraw','createDate', 'updateDate']),
			listeners : {
				'load' : function(store, records, options) {
					siteSettings.alwaysFun();
				}
			}
		});
/** 基本信息-选择模式 */
siteSettings.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				siteSettings.deleteAction.enable();
				siteSettings.editAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				siteSettings.alwaysFun();
			}
		}
	});
/** 基本信息-数据列 */
siteSettings.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [siteSettings.selModel, {
						hidden : true,
						header : '站点编号',
						dataIndex : 'ssid'
					}, {
						header : '站点名称',
						dataIndex : 'sitename',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '站点URL',
						dataIndex : 'siteurl',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '推广关键词',
						dataIndex : 'spreadkey',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '输错锁定次数',
						dataIndex : 'lockedcount',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}]
		});
/** 新建 */
siteSettings.addAction = new Ext.Action({
			text : '新建',
			iconCls : 'siteSettings_add',
			handler : function() {
				siteSettings.addWindow.setIconClass('siteSettings_add'); // 设置窗口的样式
				siteSettings.addWindow.setTitle('新建站点'); // 设置窗口的名称
				siteSettings.addWindow.show().center(); // 显示窗口
				siteSettings.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			}
		});
/** 编辑 */
siteSettings.editAction = new Ext.Action({
			text : '编辑',
			iconCls : 'siteSettings_edit',
			disabled : true,
			handler : function() {
				var record = siteSettings.grid.getSelectionModel().getSelected();
				siteSettings.addWindow.setIconClass('siteSettings_edit'); // 设置窗口的样式
				siteSettings.addWindow.setTitle('编辑站点'); // 设置窗口的名称
				siteSettings.addWindow.show().center();
				siteSettings.formPanel.getForm().reset();
				siteSettings.formPanel.getForm().loadRecord(record);
			}
		});
/** 删除 */
siteSettings.deleteAction = new Ext.Action({
			text : '删除',
			iconCls : 'siteSettings_delete',
			disabled : true,
			handler : function() {
				siteSettings.delFun();
			}
		});
/** 查询 */
siteSettings.searchField = new Ext.ux.form.SearchField({
			store : siteSettings.store,
			paramName : 'pname',
			emptyText : '请输入站点名称',
			style : 'margin-left: 5px;'
		});
/** 顶部工具栏 */
siteSettings.tbar = [siteSettings.addAction, '-', siteSettings.editAction, '-',
		siteSettings.deleteAction, '-', siteSettings.searchField];
/** 底部工具条 */
siteSettings.bbar = new Ext.PagingToolbar({
			pageSize : siteSettings.pageSize,
			store : siteSettings.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', siteSettings.pageSizeCombo]
		});
/** 基本信息-表格 */
siteSettings.grid = new Ext.grid.EditorGridPanel({
			// title : '站点列表',
			store : siteSettings.store,
			colModel : siteSettings.colModel,
			selModel : siteSettings.selModel,
			tbar : siteSettings.tbar,
			bbar : siteSettings.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'siteSettingsDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});
//设置基本的是否选项
siteSettings.baseSelectStore = new Ext.data.SimpleStore({  
    fields : ['key', 'value'],  
    data : [['开启', '1'], ['关闭', '0']]  
});
siteSettings.isclosedCombo = new Ext.form.ComboBox({  
    fieldLabel : '是否关闭站点',  
    id : 'isclosed',  
    store : siteSettings.baseSelectStore,  
    displayField : 'key',  
    valueField : 'value',  
    mode : 'local',  
    typeAhead : true,  
    forceSelection : true,  
    triggerAction : 'all',  
    anchor : '99%',  
    selectOnFocus : true,
    hiddenName : 'isclosed'
});
siteSettings.isregisterCombo = new Ext.form.ComboBox({  
    fieldLabel : '是否关闭注册',  
    id : 'isregister',  
    store : siteSettings.baseSelectStore,  
    displayField : 'key',  
    valueField : 'value',  
    mode : 'local',  
    typeAhead : true,  
    forceSelection : true,  
    triggerAction : 'all',  
    anchor : '99%',  
    selectOnFocus : true,
    hiddenName : 'isregister'
});
siteSettings.isloginCombo = new Ext.form.ComboBox({  
    fieldLabel : '是否关闭登陆',  
    id : 'islogin',  
    store : siteSettings.baseSelectStore,  
    displayField : 'key',  
    valueField : 'value',  
    mode : 'local',  
    typeAhead : true,  
    forceSelection : true,  
    triggerAction : 'all',  
    anchor : '99%',  
    selectOnFocus : true,
    hiddenName : 'islogin'
});
siteSettings.isrechargeCombo = new Ext.form.ComboBox({  
    fieldLabel : '是否关闭会员充值',  
    id : 'isrecharge',  
    store : siteSettings.baseSelectStore,  
    displayField : 'key',  
    valueField : 'value',  
    mode : 'local',  
    typeAhead : true,  
    forceSelection : true,  
    triggerAction : 'all',  
    anchor : '99%',  
    selectOnFocus : true,
    hiddenName : 'isrecharge'
});
siteSettings.isdrawCombo = new Ext.form.ComboBox({  
    fieldLabel : '是否关闭会员提款',  
    id : 'isdraw',  
    store : siteSettings.baseSelectStore,  
    displayField : 'key',  
    valueField : 'value',  
    mode : 'local',  
    typeAhead : true,  
    forceSelection : true,  
    triggerAction : 'all',  
    anchor : '99%',  
    selectOnFocus : true,
    hiddenName : 'isdraw'
});
/** 基本信息-详细信息的form */
siteSettings.formPanel = new Ext.form.FormPanel({
			frame : true,
			//title : '站点信息',
			bodyStyle : 'padding:10px;border:0px',
			labelwidth : 50,
			defaultType : 'textfield',
			items : [{
						xtype : 'hidden',
						fieldLabel : 'ID',
						name : 'ssid',
						anchor : '99%'
					}, {
						fieldLabel : '站点名称',
						maxLength : 64,
						allowBlank : false,
						name : 'sitename',
						anchor : '99%'
					},{
						fieldLabel : '站点URL',
						maxLength : 64,
						// allowBlank : false,
						name : 'siteurl',
						anchor : '99%'
					},{
						fieldLabel : '推广关键字',
						maxLength : 64,
						// allowBlank : false,
						name : 'spreadkey',
						anchor : '99%'
					},{
						fieldLabel : '输错锁定次数',
						maxLength : 64,
						// allowBlank : false,
						name : 'lockedcount',
						anchor : '99%'
					},siteSettings.isclosedCombo,
					siteSettings.isregisterCombo,
					siteSettings.isloginCombo,
					siteSettings.isrechargeCombo,
					siteSettings.isdrawCombo,{
						fieldLabel : '最低提款次数',
						maxLength : 64,
						// allowBlank : false,
						name : 'lowestdraw',
						anchor : '99%'
					},{
						fieldLabel : '最高提款次数',
						maxLength : 64,
						// allowBlank : false,
						name : 'highestdraw',
						anchor : '99%'
					}]
		});
/** 编辑新建窗口 */
siteSettings.addWindow = new Ext.Window({
			layout : 'fit',
			width : 500,
			height : 420,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [siteSettings.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							siteSettings.saveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = siteSettings.formPanel.getForm();
							var id = form.findField("ssid").getValue();
							form.reset();
							if (id != '')
								form.findField("ssid").setValue(id);
						}
					}]
		});
siteSettings.alwaysFun = function() {
	Share.resetGrid(siteSettings.grid);
	siteSettings.deleteAction.disable();
	siteSettings.editAction.disable();
};
siteSettings.saveFun = function() {
	var form = siteSettings.formPanel.getForm();
	if (!form.isValid()) {
		return;
	}
	// 发送请求
	Share.AjaxRequest({
				url : siteSettings.save,
				params : form.getValues(),
				callback : function(json) {
					siteSettings.addWindow.hide();
					siteSettings.alwaysFun();
					siteSettings.store.reload();
				}
			});
};
siteSettings.delFun = function() {
	var record = siteSettings.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '你真的要删除选中菜单及其包含的所有子菜单吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : siteSettings.del + record.data.ssid +".do",
								callback : function(json) {
									siteSettings.alwaysFun();
									siteSettings.store.reload();
								}
							});
				}
			});
};
siteSettings.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [siteSettings.grid]
		});
</script>