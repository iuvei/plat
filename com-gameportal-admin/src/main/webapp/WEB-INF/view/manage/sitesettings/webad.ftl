<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.webads"); // 自定义一个命名空间
webads = Ext.market.webads; // 定义命名空间的别名
webads = {
	all : '/manage/webad/queryWebAd.do',// 加载所有
	save : "/manage/webad/save.do",//保存
	del : "/manage/webad/del/",//删除
	pageSize : 20 // 每页显示的记录数
	//ENABLED : eval('(${fields.enabled})')
};


/** 改变页的combo */
webads.pageSizeCombo = new Share.pageSizeCombo({
			value : '20',
			listeners : {
				select : function(comboBox) {
					webads.pageSize = parseInt(comboBox.getValue());
					webads.bbar.pageSize = parseInt(comboBox.getValue());
					webads.store.baseParams.limit = webads.pageSize;
					webads.store.baseParams.start = 0;
					webads.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
webads.pageSize = parseInt(webads.pageSizeCombo.getValue());
/** 基本信息-数据源 */
webads.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : webads.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : webads.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['adid', 'adtitle', 'adhref', 'adimages', 'adheight','adwidth','adcolor','status','adsequence','adlocation','edittime']),
			listeners : {
				'load' : function(store, records, options) {
					webads.alwaysFun();
				}
			}
		});
/** 基本信息-选择模式 */
webads.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				webads.deleteAction.enable();
				webads.editAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				webads.alwaysFun();
			}
		}
	});
/** 基本信息-数据列 */
webads.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [webads.selModel, {
						hidden : true,
						header : '广告ID',
						dataIndex : 'adid'
					}, {
						header : '广告标题',
						dataIndex : 'adtitle',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '广告连接',
						width : 80,
						dataIndex : 'adhref',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '广告图片',
						dataIndex : 'adimages',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '广告高度',
						dataIndex : 'adheight',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '广告宽度',
						dataIndex : 'adwidth',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '广告颜色',
						dataIndex : 'adcolor',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '排序',
						dataIndex : 'adsequence',
						width : 50,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '广告位置',
						dataIndex : 'adlocation',
						width : 80,
						renderer : function(v) {
							if(v =='index'){
								return '<span style="color:blue;">线下首页</span>';
							}else if(v == 'index_xs'){
								return '<span style="color:blue;">线上首页</span>';
							}else{
								return v;
							}
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
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
webads.addAction = new Ext.Action({
			text : '新建',
			iconCls : 'Add',
			handler : function() {
				webads.addWindow.setIconClass('Applicationadd'); // 设置窗口的样式
				webads.addWindow.setTitle('发布广告信息'); // 设置窗口的名称
				webads.addWindow.show().center(); // 显示窗口
				webads.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			}
		});
/** 编辑 */
webads.editAction = new Ext.Action({
			text : '编辑',
			iconCls : 'Applicationedit',
			disabled : true,
			handler : function() {
				var record = webads.grid.getSelectionModel().getSelected();
				webads.addWindow.setIconClass('Applicationedit'); // 设置窗口的样式
				webads.addWindow.setTitle('编辑广告信息'); // 设置窗口的名称
				webads.addWindow.show().center();
				webads.formPanel.getForm().reset();
				webads.formPanel.getForm().loadRecord(record);
			}
		});
/** 删除 */
webads.deleteAction = new Ext.Action({
			text : '删除',
			iconCls : 'Delete',
			disabled : true,
			handler : function() {
				webads.delFun();
			}
		});
/** 查询 */
webads.searchField = new Ext.ux.form.SearchField({
			store : webads.store,
			paramName : 'adtitle',
			emptyText : '请输广告信息标题',
			style : 'margin-left: 5px;'
		});
/** 顶部工具栏 */
webads.tbar = [webads.addAction, '-', webads.editAction, '-',
		webads.deleteAction, '-', webads.searchField];
/** 底部工具条 */
webads.bbar = new Ext.PagingToolbar({
			pageSize : webads.pageSize,
			store : webads.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', webads.pageSizeCombo]
		});
/** 基本信息-表格 */
webads.grid = new Ext.grid.EditorGridPanel({
			// title : '站点列表',
			store : webads.store,
			colModel : webads.colModel,
			selModel : webads.selModel,
			tbar : webads.tbar,
			bbar : webads.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'webadsDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});
//设置基本的是否选项
webads.baseSelectStore = new Ext.data.SimpleStore({  
    fields : ['key', 'value'],  
    data : [['线下首页', 'index'], ['线上首页', 'index_xs']]  
});

webads.baseSelectStoreStatus = new Ext.data.SimpleStore({  
    fields : ['key', 'value'],  
    data : [['无效', '0'], ['正常', '1']]  
});

webads.isclosedCombo = new Ext.form.ComboBox({  
    fieldLabel : '广告状态',  
    id : 'status',  
    store : webads.baseSelectStoreStatus,  
    displayField : 'key',  
    valueField : 'value',  
    mode : 'local',  
    typeAhead : true,  
    forceSelection : true,  
    triggerAction : 'all',  
    allowBlank: false,
    selectOnFocus : true,
    hiddenName : 'status'
});

webads.isclosedComboLocation = new Ext.form.ComboBox({  
    fieldLabel : '广告位置',  
    id : 'adlocation',  
    store : webads.baseSelectStore,  
    displayField : 'key',  
    valueField : 'value',  
    mode : 'local',  
    typeAhead : true,  
    forceSelection : true,  
    triggerAction : 'all',
    selectOnFocus : true,
    allowBlank: false,
    hiddenName : 'adlocation'
});

/** 基本信息-详细信息的form */
webads.formPanel = new Ext.form.FormPanel({
			autoScroll : false,
			border: false,
			fileUpload: true,
        	style: 'border-bottom:0px;',
        	bodyStyle: 'padding:10px;background-color:transparent;',
			labelwidth : 50,
			defaultType : 'textfield',
			defaults: {
	            anchor: '95%',
	            allowBlank: false,
	            msgTarget: 'side'
	        },
			items : [{
						xtype : 'hidden',
						fieldLabel : 'ID',
						name : 'adid'
					}, {
						fieldLabel : '广告标题',
						maxLength : 64,
						name : 'adtitle'
					},{
						fieldLabel : '广告连接',
						maxLength : 64,
						name : 'adhref'
					},{
			            //xtype: 'fileuploadfield',
			            //id: 'form-file',
			            //emptyText: 'Select an image',
			            fieldLabel: '广告图片',
			            name: 'adimages',
			            buttonText: '',
			            buttonCfg: {
			                iconCls: 'Imageadd'
			            }
			        },{
						fieldLabel : '广告高度',
						maxLength : 64,
						name : 'adheight'
					},{
						fieldLabel : '广告宽度',
						maxLength : 64,
						name : 'adwidth'
					},{
						fieldLabel : '广告颜色',
						maxLength : 64,
						name : 'adcolor'
					},{
						fieldLabel : '广告排序',
						maxLength : 64,
						name : 'adsequence'
					},webads.isclosedComboLocation,webads.isclosedCombo]
		});
/** 编辑新建窗口 */
webads.addWindow = new Ext.Window({
			layout : 'fit',
			width : 500,
			height : 330,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [webads.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							webads.saveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = webads.formPanel.getForm();
							var id = form.findField("adid").getValue();
							form.reset();
							if (id != '')
								form.findField("adid").setValue(id);
						}
					}]
		});
webads.alwaysFun = function() {
	Share.resetGrid(webads.grid);
	webads.deleteAction.disable();
	webads.editAction.disable();
};
webads.saveFun = function() {
	var form = webads.formPanel.getForm();
	if (!form.isValid()) {
		return;
	}
	// 发送请求
	Share.AjaxRequest({
				url: webads.save,
				params : form.getValues(),
				
				callback : function(json) {
					if(json.success){
						webads.addWindow.hide();
						webads.alwaysFun();
						webads.store.reload();
					}else{
						Ext.MessageBox.alert('Status', json.msg, showResult);
					}
				}
			});
			/**
	form.submit({
        url: webads.save,
        method: 'POST',
        waitMsg: '正在上传图片...',
        success: function(form, o){
        	if(o.result.success == true){
        		if(o.result.msg != '0'){
        			Ext.Msg.alert('错误',o.result.msg);
        		}else{
        			Ext.Msg.alert('提示','操作成功。',function () {
		        		webads.addWindow.hide();
						webads.alwaysFun();
						webads.store.reload();
		    		});
        		}
        	}
        },
        failure: function (response, options) {
            Share.ExceptionWindow('错误：' + response.status + ' ' + response.statusText, response.responseText);
        }
    });
    */
};
webads.delFun = function() {
	var record = webads.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '你真的要删除选中的广告信息吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : webads.del + record.data.adid +".do",
								callback : function(json) {
									webads.alwaysFun();
									webads.store.reload();
								}
							});
				}
			});
};
webads.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [webads.grid]
		});
</script>