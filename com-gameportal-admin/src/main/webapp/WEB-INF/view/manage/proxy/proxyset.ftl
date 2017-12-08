<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.proxyInfos"); // 自定义一个命名空间
proxyset = Ext.market.proxyInfos; // 定义命名空间的别名
proxyset = {
	all : '/manage/proxyset/queryProxySet.do',// 加载所有
	save : "/manage/proxyset/save.do",//保存
	del : "/manage/proxyset/delproxyset/",//删除
	bind : "/manage/proxyset/bindproxyset/",
	pageSize : 20, // 每页显示的记录数
	memGrade:eval('(${fields.memGrade})'),
	isXimaFlag : eval('(${fields.ixXimaFlag})'),
	clearingtype:eval('(${fields.proxyclearingtype})')
};


/** 改变页的combo */
proxyset.pageSizeCombo = new Share.pageSizeCombo({
			value : '20',
			listeners : {
				select : function(comboBox) {
					proxyset.pageSize = parseInt(comboBox.getValue());
					proxyset.bbar.pageSize = parseInt(comboBox.getValue());
					proxyset.store.baseParams.limit = proxyset.pageSize;
					proxyset.store.baseParams.start = 0;
					proxyset.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
proxyset.pageSize = parseInt(proxyset.pageSizeCombo.getValue());
/** 基本信息-数据源 */
proxyset.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : proxyset.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : proxyset.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['pyid', 'uiid', 'account', 'uname',"returnscale",'ximascale','createtime','uptime','upuser','upclientip','status','isximaflag','clearingtype']),
			listeners : {
				'load' : function(store, records, options) {
				//	memberGrade.alwaysFun();
				}
			}
		});
		
/** 基本信息-选择模式 */
proxyset.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				proxyset.deleteAction.enable();
				proxyset.editAction.enable();
				if(record.data.status == '0'){
					proxyset.removeAction.enable();
					proxyset.bindAction.disable();
				}else{
					proxyset.bindAction.enable();
					proxyset.removeAction.disable();
				}
				
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				proxyset.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
proxyset.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [proxyset.selModel, {
						hidden : true,
						header : 'ID',
						dataIndex : 'pyid'
					},{
						header : '代理ID',
						dataIndex : 'uiid',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '代理账号',
						dataIndex : 'account',
						width : 100,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '代理姓名',
						dataIndex : 'uname',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '占成比例',
						dataIndex : 'returnscale',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '洗码比例',
						dataIndex : 'ximascale',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '创建时间',
						dataIndex : 'createtime',
						width : 130,
						renderer : function(v) {
       						return new Date(v).format('Y-m-d H:i:s');
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '修改时间',
						dataIndex : 'uptime',
						width : 130,
						renderer : function(v) {
       						return new Date(v).format('Y-m-d H:i:s');
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '修改人',
						dataIndex : 'upuser',
						width : 120,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '修改客户端IP',
						dataIndex : 'upclientip',
						width : 120,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '状态',
						dataIndex : 'status',
						width : 80,
						renderer : function(v) {
							if(v == 1){
								return '<span style="color:green;">已启用</span>';
							}else{
								return '<span style="color:red;">未启用</span>';
							}
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '洗码状态',
						dataIndex : 'isximaflag',
						width : 80,
						renderer : function(v) {
							if(v == 1){
								return '<span style="color:green;">已开启</span>';
							}else{
								return '<span style="color:red;">未开启</span>';
							}
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '结算类型',
						dataIndex : 'clearingtype',
						width : 80,
						renderer : function(v) {
							if(v == 0){
								return '<span style="color:blue;">输值结算</span>';
							}else if(v == 1){
								return '<span style="color:#FF3333;">按月洗码</span>';
							}else if(v == 2){
								return '<span style="color:#FF3333;">按天洗码</span>';
							}else{
								return v;
							}
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}]
		});
		
/** 新建 */
proxyset.addAction = new Ext.Action({
			text : '新建',
			iconCls : 'Add',
			handler : function() {
				proxyset.addWindow.setIconClass('Applicationadd'); // 设置窗口的样式
				proxyset.addWindow.setTitle('新建代理占成'); // 设置窗口的名称
				proxyset.addWindow.show().center(); // 显示窗口
				proxyset.formPanel.getForm().reset(); // 清空表单里面的元素的值.

			}
		});
/** 编辑 */
proxyset.editAction = new Ext.Action({
			text : '编辑',
			iconCls : 'Applicationedit',
			disabled : true,
			handler : function() {
				var record = proxyset.grid.getSelectionModel().getSelected();
				proxyset.addWindow.setIconClass('Applicationedit'); // 设置窗口的样式
				proxyset.addWindow.setTitle('编辑代理占成'); // 设置窗口的名称
				proxyset.addWindow.show().center();
				proxyset.formPanel.getForm().reset();
				proxyset.formPanel.getForm().loadRecord(record);
				
				
			}
		});
		
/** 删除 */
proxyset.deleteAction = new Ext.Action({
			text : '删除',
			iconCls : 'Delete',
			disabled : true,
			handler : function() {
				proxyset.delFun();
			}
		});
/***/
proxyset.bindAction = new Ext.Action({
			text : '禁用',
			iconCls : 'Lockkey',
			disabled : true,
			handler : function() {
				proxyset.bindFun(1);
			}
		});
		
proxyset.removeAction = new Ext.Action({
			text : '启用',
			iconCls : 'Lockopen',
			disabled : true,
			handler : function() {
				proxyset.bindFun(0);
			}
		});

proxyset.searchField = new Ext.ux.form.SearchField({
			store : proxyset.store,
			paramName : 'account',
			emptyText : '请输入代理账号',
			style : 'margin-left: 5px;'
		});
/** 顶部工具栏 */
proxyset.tbar = [proxyset.addAction, '-', proxyset.editAction, '-',
		proxyset.deleteAction,'-',proxyset.bindAction,'-',proxyset.removeAction,'-', proxyset.searchField];
/** 底部工具条 */
proxyset.bbar = new Ext.PagingToolbar({
			pageSize : proxyset.pageSize,
			store : proxyset.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', proxyset.pageSizeCombo]
		});
/** 基本信息-表格 */
proxyset.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : proxyset.store,
			colModel : proxyset.colModel,
			selModel : proxyset.selModel,
			tbar : proxyset.tbar,
			bbar : proxyset.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'memberGradeDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});
			
proxyset.returnscaleNumberField = new Ext.form.NumberField({
		maxLength : 8,
		allowBlank : false,
		decimalPrecision : 4,
		fieldLabel : '占成比例',
		name : 'returnscale',
		anchor : '99%'
	});

proxyset.ximascaleNumberField = new Ext.form.NumberField({
		maxLength : 8,
		allowBlank : false,
		decimalPrecision : 4,
		fieldLabel : '洗码比例',
		name : 'ximascale',
		anchor : '99%'
	});
	
proxyset.isximaflag = new Ext.form.ComboBox({
		fieldLabel : '洗码状态',
		hiddenName : 'isximaflag',
		name : 'isximaflag',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(proxyset.isXimaFlag)
				}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		value : "1",
		anchor : '99%'
	});
	
	proxyset.clearingtype = new Ext.form.ComboBox({
		fieldLabel : '结算类型',
		hiddenName : 'clearingtype',
		name : 'clearingtype',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(proxyset.clearingtype)
				}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		value : "1",
		anchor : '99%'
	});
	
/** 基本信息-详细信息的form */
proxyset.formPanel = new Ext.form.FormPanel({
			autoScroll : false,
			border: false,
        	style: 'border-bottom:0px;',
        	bodyStyle: 'padding:10px;background-color:transparent;',
			labelwidth : 50,
			defaultType : 'textfield',
			items : [{
						xtype : 'hidden',
						fieldLabel : 'ID',
						name : 'pyid',
						anchor : '99%'
					},{
						fieldLabel : '代理账号',
						maxLength : 32,
						allowBlank : false,
						name : 'account',
						anchor : '99%'
					},proxyset.returnscaleNumberField,proxyset.ximascaleNumberField,proxyset.isximaflag,proxyset.clearingtype]
		});

		

			
/** 编辑新建窗口 */
proxyset.addWindow = new Ext.Window({
			layout : 'fit',
			width : 399,
			height : 220,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [proxyset.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							proxyset.saveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = proxyset.formPanel.getForm();
							var id = form.findField("pyid").getValue();
							form.reset();
							if (id != '')
								form.findField("pyid").setValue(id);
						}
					}]
		});
	
proxyset.alwaysFun = function() {
	Share.resetGrid(proxyset.grid);
	proxyset.deleteAction.disable();
	proxyset.editAction.disable();
	proxyset.bindAction.disable();
	proxyset.removeAction.disable();
};
proxyset.saveFun = function() {
	var form = proxyset.formPanel.getForm();

	if(!form.isValid()){
 		return;
 	}
	// 发送请求
	Share.AjaxRequest({
				url : proxyset.save,
				params : form.getValues(),
				callback : function(json) {
				if (json.success==false){
				     Ext.MessageBox.alert('Status', json.msg, showResult);
					return;
				}else{
				    proxyset.addWindow.hide();
					proxyset.alwaysFun();
					proxyset.store.reload();
				}
					
				}
			});
};

proxyset.delFun = function() {
	var record = proxyset.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '您确定要删除选中代理占成数据吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : proxyset.del + record.data.pyid +".do",
								callback : function(json) {
									proxyset.alwaysFun();
									proxyset.store.reload();
								}
							});
				}
			});
};

proxyset.bindFun = function(s){
	var record = proxyset.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '确定要修改修改占成数据状态吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : proxyset.bind + record.data.pyid+"/"+s+".do",
								callback : function(json) {
									proxyset.alwaysFun();
									proxyset.store.reload();
								}
							});
				}
			});
}
  
proxyset.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [proxyset.grid]
		});

</script>