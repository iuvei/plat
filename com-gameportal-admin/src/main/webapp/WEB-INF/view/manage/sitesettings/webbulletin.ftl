<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.webbulletins"); // 自定义一个命名空间
webbulletins = Ext.market.webbulletins; // 定义命名空间的别名
webbulletins = {
	all : '/manage/wbulletin/queryWebBulletin.do',// 加载所有
	save : "/manage/wbulletin/save.do",//保存
	del : "/manage/wbulletin/del/",//删除
	pageSize : 20 // 每页显示的记录数
	//ENABLED : eval('(${fields.enabled})')
};


/** 改变页的combo */
webbulletins.pageSizeCombo = new Share.pageSizeCombo({
			value : '20',
			listeners : {
				select : function(comboBox) {
					webbulletins.pageSize = parseInt(comboBox.getValue());
					webbulletins.bbar.pageSize = parseInt(comboBox.getValue());
					webbulletins.store.baseParams.limit = webbulletins.pageSize;
					webbulletins.store.baseParams.start = 0;
					webbulletins.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
webbulletins.pageSize = parseInt(webbulletins.pageSizeCombo.getValue());
/** 基本信息-数据源 */
webbulletins.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : webbulletins.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : webbulletins.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['bid', 'btitle', 'bcontext', 'status', 'blocation','edittime']),
			listeners : {
				'load' : function(store, records, options) {
					webbulletins.alwaysFun();
				}
			}
		});
/** 基本信息-选择模式 */
webbulletins.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				webbulletins.deleteAction.enable();
				webbulletins.editAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				webbulletins.alwaysFun();
			}
		}
	});
/** 基本信息-数据列 */
webbulletins.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [webbulletins.selModel, {
						hidden : true,
						header : '公告ID',
						dataIndex : 'bid'
					}, {
						header : '公告标题',
						dataIndex : 'btitle',
						width : 200,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '公告内容',
						dataIndex : 'bcontext',
						width : 650,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '状态',
						dataIndex : 'status',
						width : 50,
						renderer : function(v) {
							if(v == 1){
								return '<span style="color:green;">正常</span>';
							}else{
								return '<span style="color:red;">无效</span>';
							}
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '公告位置',
						width : 70,
						dataIndex : 'blocation',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '发布日期',
						dataIndex : 'edittime',
						width : 130,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						}),
						renderer : function(v) {
							return v==null?'':new Date(v).format('Y-m-d H:i:s');
						}
					}]
		});
/** 新建 */
webbulletins.addAction = new Ext.Action({
			text : '新建',
			iconCls : 'Add',
			handler : function() {
				webbulletins.addWindow.setIconClass('Applicationadd'); // 设置窗口的样式
				webbulletins.addWindow.setTitle('新建站点'); // 设置窗口的名称
				webbulletins.addWindow.show().center(); // 显示窗口
				webbulletins.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			}
		});
/** 编辑 */
webbulletins.editAction = new Ext.Action({
			text : '编辑',
			iconCls : 'Applicationedit',
			disabled : true,
			handler : function() {
				var record = webbulletins.grid.getSelectionModel().getSelected();
				webbulletins.addWindow.setIconClass('Applicationedit'); // 设置窗口的样式
				webbulletins.addWindow.setTitle('编辑站点'); // 设置窗口的名称
				webbulletins.addWindow.show().center();
				webbulletins.formPanel.getForm().reset();
				webbulletins.formPanel.getForm().loadRecord(record);
			}
		});
/** 删除 */
webbulletins.deleteAction = new Ext.Action({
			text : '删除',
			iconCls : 'Delete',
			disabled : true,
			handler : function() {
				webbulletins.delFun();
			}
		});
/** 查询 */
webbulletins.searchField = new Ext.ux.form.SearchField({
			store : webbulletins.store,
			paramName : 'btitle',
			emptyText : '请输公告信息标题',
			style : 'margin-left: 5px;'
		});
/** 顶部工具栏 */
webbulletins.tbar = [webbulletins.addAction, '-', webbulletins.editAction, '-',
		webbulletins.deleteAction, '-', webbulletins.searchField];
/** 底部工具条 */
webbulletins.bbar = new Ext.PagingToolbar({
			pageSize : webbulletins.pageSize,
			store : webbulletins.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', webbulletins.pageSizeCombo]
		});
/** 基本信息-表格 */
webbulletins.grid = new Ext.grid.EditorGridPanel({
			// title : '站点列表',
			store : webbulletins.store,
			colModel : webbulletins.colModel,
			selModel : webbulletins.selModel,
			tbar : webbulletins.tbar,
			bbar : webbulletins.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'webbulletinsDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});
//设置基本的是否选项
webbulletins.baseSelectStore = new Ext.data.SimpleStore({  
    fields : ['key', 'value'],  
    data : [['正常', '1'], ['无效', '0']]  
});
webbulletins.isclosedCombo = new Ext.form.ComboBox({  
    fieldLabel : '公告状态',  
    id : 'status',  
    store : webbulletins.baseSelectStore,  
    displayField : 'key',  
    valueField : 'value',  
    mode : 'local',  
    typeAhead : true,  
    forceSelection : true,  
    triggerAction : 'all',  
    anchor : '99%',  
    selectOnFocus : true,
    allowBlank : false,
    hiddenName : 'status'
});

/** 基本信息-详细信息的form */
webbulletins.formPanel = new Ext.form.FormPanel({
			autoScroll : false,
			border: false,
        	style: 'border-bottom:0px;',
        	bodyStyle: 'padding:10px;background-color:transparent;',
			labelwidth : 50,
			defaultType : 'textfield',
			items : [{
						xtype : 'hidden',
						fieldLabel : 'ID',
						name : 'bid',
						anchor : '99%'
					}, {
						fieldLabel : '公告标题',
						allowBlank : false,
						name : 'btitle',
						anchor : '99%'
					},{
						fieldLabel : '公告位置',
						maxLength : 64,
						// allowBlank : false,
						name : 'blocation',
						anchor : '99%'
					},webbulletins.isclosedCombo
					,{
						fieldLabel : '公告内容',
						xtype : 'textarea',
						allowBlank : false,
						name : 'bcontext',
						anchor : '99%'
					}]
		});
/** 编辑新建窗口 */
webbulletins.addWindow = new Ext.Window({
			layout : 'fit',
			width : 500,
			height : 270,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [webbulletins.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							webbulletins.saveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = webbulletins.formPanel.getForm();
							var id = form.findField("bid").getValue();
							form.reset();
							if (id != '')
								form.findField("bid").setValue(id);
						}
					}]
		});
webbulletins.alwaysFun = function() {
	Share.resetGrid(webbulletins.grid);
	webbulletins.deleteAction.disable();
	webbulletins.editAction.disable();
};
webbulletins.saveFun = function() {
	var form = webbulletins.formPanel.getForm();
	if (!form.isValid()) {
		return;
	}
	// 发送请求
	Share.AjaxRequest({
				url : webbulletins.save,
				params : form.getValues(),
				callback : function(json) {
					webbulletins.addWindow.hide();
					webbulletins.alwaysFun();
					webbulletins.store.reload();
				}
			});
};
webbulletins.delFun = function() {
	var record = webbulletins.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '你真的要删除选中的公告信息吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : webbulletins.del + record.data.bid +".do",
								callback : function(json) {
									webbulletins.alwaysFun();
									webbulletins.store.reload();
								}
							});
				}
			});
};
webbulletins.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [webbulletins.grid]
		});
</script>