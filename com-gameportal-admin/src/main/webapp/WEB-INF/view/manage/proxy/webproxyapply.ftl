<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.webproxyapplys"); // 自定义一个命名空间
webproxyapplys = Ext.market.webproxyapplys; // 定义命名空间的别名
webproxyapplys = {
	all : '/manage/proxyapply/queryWebProxyApply.do',// 加载所有
	save : "/manage/proxyapply/save.do",//保存
	del : "/manage/proxyapply/del/",//删除
	pageSize : 20 // 每页显示的记录数
	//ENABLED : eval('(${fields.enabled})')
};


/** 改变页的combo */
webproxyapplys.pageSizeCombo = new Share.pageSizeCombo({
			value : '20',
			listeners : {
				select : function(comboBox) {
					webproxyapplys.pageSize = parseInt(comboBox.getValue());
					webproxyapplys.bbar.pageSize = parseInt(comboBox.getValue());
					webproxyapplys.store.baseParams.limit = webproxyapplys.pageSize;
					webproxyapplys.store.baseParams.start = 0;
					webproxyapplys.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
webproxyapplys.pageSize = parseInt(webproxyapplys.pageSizeCombo.getValue());
/** 基本信息-数据源 */
webproxyapplys.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : webproxyapplys.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : webproxyapplys.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['aid', 'truename', 'qqskype', 'aemail', 'spreadfs','applysource','applytime','status','description']),
			listeners : {
				'load' : function(store, records, options) {
					webproxyapplys.alwaysFun();
				}
			}
		});
/** 基本信息-选择模式 */
webproxyapplys.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				webproxyapplys.deleteAction.enable();
				webproxyapplys.editAction.enable();
				/*if(record.data.status == 0){
					
				}*/
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				webproxyapplys.alwaysFun();
			}
		}
	});
/** 基本信息-数据列 */
webproxyapplys.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [webproxyapplys.selModel, {
						hidden : true,
						header : '申请ID',
						dataIndex : 'aid'
					}, {
						header : '姓名',
						dataIndex : 'truename',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : 'QQ/Skype',
						dataIndex : 'qqskype',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : 'E-Mail',
						dataIndex : 'aemail',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '推广方式',
						dataIndex : 'spreadfs',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '申请来源',
						dataIndex : 'applysource',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '申请时间',
						dataIndex : 'applytime',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '状态',
						dataIndex : 'status',
						renderer : function(v) {
							if(v == 0){
								return '<span style="color:#FF6600;">待审核</span>';
							}else if(v == 1){
								return '<span style="color:green;">审核通过</span>';
								
							}else if(v == 2){
								return '<span style="color:red;">审核失败</span>';
							}else{
								return v;
							}
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '备注',
						dataIndex : 'description',
						width : 200,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}]
		});
		
/** 编辑 */
webproxyapplys.editAction = new Ext.Action({
			text : '审核',
			iconCls : 'Applicationedit',
			disabled : true,
			handler : function() {
				var record = webproxyapplys.grid.getSelectionModel().getSelected();
				webproxyapplys.addWindow.setIconClass('Applicationedit'); // 设置窗口的样式
				webproxyapplys.addWindow.setTitle('审核代理申请'); // 设置窗口的名称
				webproxyapplys.addWindow.show().center();
				webproxyapplys.formPanel.getForm().reset();
				webproxyapplys.formPanel.getForm().loadRecord(record);
			}
		});
/** 删除 */
webproxyapplys.deleteAction = new Ext.Action({
			text : '删除',
			iconCls : 'Delete',
			disabled : true,
			handler : function() {
				webproxyapplys.delFun();
			}
		});
/** 查询 */
webproxyapplys.searchField = new Ext.ux.form.SearchField({
			store : webproxyapplys.store,
			paramName : 'truename',
			emptyText : '请输入申请人姓名',
			style : 'margin-left: 5px;'
		});
/** 顶部工具栏 */
webproxyapplys.tbar = [webproxyapplys.editAction, '-',
		webproxyapplys.deleteAction, '-', webproxyapplys.searchField];
/** 底部工具条 */
webproxyapplys.bbar = new Ext.PagingToolbar({
			pageSize : webproxyapplys.pageSize,
			store : webproxyapplys.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', webproxyapplys.pageSizeCombo]
		});
/** 基本信息-表格 */
webproxyapplys.grid = new Ext.grid.EditorGridPanel({
			// title : '站点列表',
			store : webproxyapplys.store,
			colModel : webproxyapplys.colModel,
			selModel : webproxyapplys.selModel,
			tbar : webproxyapplys.tbar,
			bbar : webproxyapplys.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'webproxyapplysDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});
//设置基本的是否选项
webproxyapplys.baseSelectStore = new Ext.data.SimpleStore({  
    fields : ['key', 'value'],  
    data : [['审核通过', '1'], ['审核失败', '2'],['待审核', '0']]  
});
webproxyapplys.isclosedCombo = new Ext.form.ComboBox({  
    fieldLabel : '审核状态',  
    id : 'status',  
    store : webproxyapplys.baseSelectStore,  
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
webproxyapplys.formPanel = new Ext.form.FormPanel({
			autoScroll : false,
			border: false,
        	style: 'border-bottom:0px;',
        	bodyStyle: 'padding:10px;background-color:transparent;',
			labelwidth : 50,
			defaultType : 'textfield',
			items : [{
						xtype : 'hidden',
						fieldLabel : 'ID',
						name : 'aid',
						anchor : '99%'
					},webproxyapplys.isclosedCombo,{
						fieldLabel : '备注',
						xtype : 'textarea',
						allowBlank : false,
						name : 'description',
						anchor : '99%'
					}]
		});
/** 编辑新建窗口 */
webproxyapplys.addWindow = new Ext.Window({
			layout : 'fit',
			width : 500,
			height : 192,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [webproxyapplys.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							webproxyapplys.saveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = webproxyapplys.formPanel.getForm();
							var id = form.findField("bid").getValue();
							form.reset();
							if (id != '')
								form.findField("bid").setValue(id);
						}
					}]
		});
webproxyapplys.alwaysFun = function() {
	Share.resetGrid(webproxyapplys.grid);
	webproxyapplys.deleteAction.disable();
	webproxyapplys.editAction.disable();
};
webproxyapplys.saveFun = function() {
	var form = webproxyapplys.formPanel.getForm();
	if (!form.isValid()) {
		return;
	}
	// 发送请求
	Share.AjaxRequest({
				url : webproxyapplys.save,
				params : form.getValues(),
				callback : function(json) {
					webproxyapplys.addWindow.hide();
					webproxyapplys.alwaysFun();
					webproxyapplys.store.reload();
				}
			});
};
webproxyapplys.delFun = function() {
	var record = webproxyapplys.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '你真的要删除选中的代理申请记录吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : webproxyapplys.del + record.data.aid +".do",
								callback : function(json) {
									webproxyapplys.alwaysFun();
									webproxyapplys.store.reload();
								}
							});
				}
			});
};
webproxyapplys.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [webproxyapplys.grid]
		});
</script>