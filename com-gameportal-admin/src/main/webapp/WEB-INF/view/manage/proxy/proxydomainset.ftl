<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.proxyInfos"); // 自定义一个命名空间
proxydomainset = Ext.market.proxyInfos; // 定义命名空间的别名
proxydomainset = {
	all : '/manage/proxy/queryProxyDomain.do',// 加载所有
	save : "/manage/proxy/saveProxyDomainInfo.do",//保存
	del : "/manage/proxy/deldomain/",//删除
	bind : "/manage/proxy/bindomain/",
	pageSize : 30, // 每页显示的记录数
	memGrade:eval('(${fields.memGrade})')
};


/** 改变页的combo */
proxydomainset.pageSizeCombo = new Share.pageSizeCombo({
			value : '30',
			listeners : {
				select : function(comboBox) {
					proxydomainset.pageSize = parseInt(comboBox.getValue());
					proxydomainset.bbar.pageSize = parseInt(comboBox.getValue());
					proxydomainset.store.baseParams.limit = proxydomainset.pageSize;
					proxydomainset.store.baseParams.start = 0;
					proxydomainset.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
proxydomainset.pageSize = parseInt(proxydomainset.pageSizeCombo.getValue());
/** 基本信息-数据源 */
proxydomainset.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : proxydomainset.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : proxydomainset.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['urlid', 'proxyurl', 'proxyuid', 'proxyaccount',"proxyname",'status']),
			listeners : {
				'load' : function(store, records, options) {
				//	memberGrade.alwaysFun();
				}
			}
		});
		
/** 基本信息-选择模式 */
proxydomainset.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				proxydomainset.deleteAction.enable();
				proxydomainset.editAction.enable();
				if(record.data.status == '1'){
					proxydomainset.removeAction.enable();
					proxydomainset.bindAction.disable();
				}else{
					proxydomainset.bindAction.enable();
					proxydomainset.removeAction.disable();
				}
				
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				proxydomainset.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
proxydomainset.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [proxydomainset.selModel, {
						hidden : true,
						header : 'ID',
						dataIndex : 'urlid'
					},{
						header : '代理ID',
						dataIndex : 'proxyuid',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '代理账号',
						dataIndex : 'proxyaccount',
						width : 100,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '代理姓名',
						dataIndex : 'proxyname',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '代理域名',
						dataIndex : 'proxyurl',
						width : 300,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '绑定状态',
						dataIndex : 'status',
						width : 80,
						renderer : function(v) {
							if(v == 1){
								return '<span style="color:green;">已绑定</span>';
							}else{
								return '<span style="color:red;">未绑定</span>';
							}
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}]
		});
		
/** 新建 */
proxydomainset.addAction = new Ext.Action({
			text : '新建',
			iconCls : 'Add',
			handler : function() {
				proxydomainset.addWindow.setIconClass('Applicationadd'); // 设置窗口的样式
				proxydomainset.addWindow.setTitle('新建域名绑定'); // 设置窗口的名称
				proxydomainset.addWindow.show().center(); // 显示窗口
				proxydomainset.formPanel.getForm().reset(); // 清空表单里面的元素的值.

			}
		});
/** 编辑 */
proxydomainset.editAction = new Ext.Action({
			text : '编辑',
			iconCls : 'Applicationedit',
			disabled : true,
			handler : function() {
				var record = proxydomainset.grid.getSelectionModel().getSelected();
				proxydomainset.addWindow.setIconClass('Applicationedit'); // 设置窗口的样式
				proxydomainset.addWindow.setTitle('编辑域名'); // 设置窗口的名称
				proxydomainset.addWindow.show().center();
				proxydomainset.formPanel.getForm().reset();
				proxydomainset.formPanel.getForm().loadRecord(record);
				
				
			}
		});
		
/** 删除 */
proxydomainset.deleteAction = new Ext.Action({
			text : '删除',
			iconCls : 'Delete',
			disabled : true,
			handler : function() {
				proxydomainset.delFun();
			}
		});
/***/
proxydomainset.bindAction = new Ext.Action({
			text : '绑定',
			iconCls : 'Lockkey',
			disabled : true,
			handler : function() {
				proxydomainset.bindFun(0);
			}
		});
		
proxydomainset.removeAction = new Ext.Action({
			text : '解除绑定',
			iconCls : 'Lockopen',
			disabled : true,
			handler : function() {
				proxydomainset.bindFun(1);
			}
		});

	/** 查询按钮 */
	proxydomainset.searchAction = new Ext.Action({
			text : '查询',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				proxydomainset.searchFun();
			}
		
	});

	proxydomainset.searchParams = function(){
		var obj = {};
		obj.proxyaccount =  $("#proxyaccount").val();
		obj.proxydomain = $("#proxydomain").val();
	    return obj;
	}
	
	proxydomainset.searchFun = function(){
		proxydomainset.store.load({params: proxydomainset.searchParams()});
	}
	
	proxydomainset.store.on('beforeload',function(store, options){
	    proxydomainset.store.baseParams = proxydomainset.searchParams();
	});



	
/** 顶部工具栏 */
proxydomainset.tbar = [proxydomainset.addAction, '-', proxydomainset.editAction, '-',
		proxydomainset.deleteAction,'-',proxydomainset.bindAction,'-',proxydomainset.removeAction,
		'-','&nbsp;','代理账号:',{id:'proxyaccount',xtype:'textfield',width:80},
		'-','&nbsp;','代理域名:',{id:'proxydomain',xtype:'textfield',width:80},
		'-',proxydomainset.searchAction
		
		];
/** 底部工具条 */
proxydomainset.bbar = new Ext.PagingToolbar({
			pageSize : proxydomainset.pageSize,
			store : proxydomainset.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', proxydomainset.pageSizeCombo]
		});
/** 基本信息-表格 */
proxydomainset.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : proxydomainset.store,
			colModel : proxydomainset.colModel,
			selModel : proxydomainset.selModel,
			tbar : proxydomainset.tbar,
			bbar : proxydomainset.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'memberGradeDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});
			
	
/** 基本信息-详细信息的form */
proxydomainset.formPanel = new Ext.form.FormPanel({
			autoScroll : false,
			border: false,
        	style: 'border-bottom:0px;',
        	bodyStyle: 'padding:10px;background-color:transparent;',
			labelwidth : 50,
			defaultType : 'textfield',
			items : [{
						xtype : 'hidden',
						fieldLabel : 'ID',
						name : 'urlid',
						anchor : '99%'
					},{
						fieldLabel : '代理账号',
						maxLength : 32,
						allowBlank : false,
						name : 'proxyaccount',
						anchor : '99%'
					},{
						fieldLabel : '绑定域名',
						maxLength : 32,
						allowBlank : false,
						name : 'proxyurl',
						anchor : '99%'
					}]
		});

		

			
/** 编辑新建窗口 */
proxydomainset.addWindow = new Ext.Window({
			layout : 'fit',
			width : 399,
			height : 142,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [proxydomainset.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							proxydomainset.saveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = proxydomainset.formPanel.getForm();
							var id = form.findField("gid").getValue();
							form.reset();
							if (id != '')
								form.findField("gid").setValue(id);
						}
					}]
		});
	
proxydomainset.alwaysFun = function() {
	Share.resetGrid(proxydomainset.grid);
	proxydomainset.deleteAction.disable();
	proxydomainset.editAction.disable();
	proxydomainset.bindAction.disable();
	proxydomainset.removeAction.disable();
};
proxydomainset.saveFun = function() {
	var form = proxydomainset.formPanel.getForm();

	
	// 发送请求
	Share.AjaxRequest({
				url : proxydomainset.save,
				params : form.getValues(),
				callback : function(json) {
				if (json.success==false){
				     Ext.MessageBox.alert('Status', json.msg, showResult);
					return;
				}else{
				    proxydomainset.addWindow.hide();
					proxydomainset.alwaysFun();
					proxydomainset.store.reload();
				}
					
				}
			});
};

proxydomainset.delFun = function() {
	var record = proxydomainset.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '你真的要删除选中的域名吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : proxydomainset.del + record.data.urlid +".do",
								callback : function(json) {
									proxydomainset.alwaysFun();
									proxydomainset.store.reload();
								}
							});
				}
			});
};

proxydomainset.bindFun = function(s){
	var record = proxydomainset.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '确定要修改域名绑定状态吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : proxydomainset.bind + record.data.urlid+"/"+s+".do",
								callback : function(json) {
									proxydomainset.alwaysFun();
									proxydomainset.store.reload();
								}
							});
				}
			});
}
  
proxydomainset.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [proxydomainset.grid]
		});

</script>