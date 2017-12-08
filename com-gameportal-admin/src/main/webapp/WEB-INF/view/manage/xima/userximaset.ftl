<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.userximaset"); // 自定义一个命名空间
userximaset = Ext.market.userximaset; // 定义命名空间的别名
userximaset = {
	all : '/manage/uxmset/queryUserXimaSet.do',// 加载所有
	save : "/manage/uxmset/save.do",//保存
	del : "/manage/uxmset/deluserximaset/",//删除
	bind : "/manage/uxmset/binduserximaset/",
	pageSize : 20, // 每页显示的记录数
	memGrade:eval('(${fields.memGrade})'),
	isXimaFlag : eval('(${fields.ixXimaFlag})'),
	clearingtype:eval('(${fields.proxyclearingtype})')
};


/** 改变页的combo */
userximaset.pageSizeCombo = new Share.pageSizeCombo({
			value : '20',
			listeners : {
				select : function(comboBox) {
					userximaset.pageSize = parseInt(comboBox.getValue());
					userximaset.bbar.pageSize = parseInt(comboBox.getValue());
					userximaset.store.baseParams.limit = userximaset.pageSize;
					userximaset.store.baseParams.start = 0;
					userximaset.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
userximaset.pageSize = parseInt(userximaset.pageSizeCombo.getValue());
/** 基本信息-数据源 */
userximaset.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : userximaset.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : userximaset.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['xmid', 'uiid', 'account', 'uname',"proxyid",'ximascale','status','settime']),
			listeners : {
				'load' : function(store, records, options) {
				//	memberGrade.alwaysFun();
				}
			}
		});
		
/** 基本信息-选择模式 */
userximaset.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				
				if(record.data.proxyid == '0'){
					userximaset.deleteAction.enable();
					userximaset.editAction.enable();
				}
				if(record.data.status == '0'){
					userximaset.removeAction.enable();
					userximaset.bindAction.disable();
				}else{
					userximaset.bindAction.enable();
					userximaset.removeAction.disable();
				}
				
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				userximaset.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
userximaset.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [userximaset.selModel, {
						hidden : true,
						header : 'ID',
						dataIndex : 'xmid'
					},{
						header : '代理ID',
						dataIndex : 'uiid',
						hidden : true
					}, {
						header : '会员账号',
						dataIndex : 'account',
						width : 100,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '会员姓名',
						dataIndex : 'uname',
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
						header : '所属',
						dataIndex : 'proxyid',
						width : 120,
						renderer : function(v) {
							if(v == 0){
								return '<span style="color:blue;">公司直属</span>';
							}else{
								return '代理下线';
							}
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '创建时间',
						dataIndex : 'settime',
						width : 140,
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

					}]
		});
		
/** 新建 */
userximaset.addAction = new Ext.Action({
			text : '新建',
			iconCls : 'Add',
			handler : function() {
				userximaset.addWindow.setIconClass('Applicationadd'); // 设置窗口的样式
				userximaset.addWindow.setTitle('新建会员洗码比例'); // 设置窗口的名称
				userximaset.addWindow.show().center(); // 显示窗口
				userximaset.formPanel.getForm().reset(); // 清空表单里面的元素的值.

			}
		});
/** 编辑 */
userximaset.editAction = new Ext.Action({
			text : '编辑',
			iconCls : 'Applicationedit',
			disabled : true,
			handler : function() {
				var record = userximaset.grid.getSelectionModel().getSelected();
				userximaset.addWindow.setIconClass('Applicationedit'); // 设置窗口的样式
				userximaset.addWindow.setTitle('编辑会员洗码比例'); // 设置窗口的名称
				userximaset.addWindow.show().center();
				userximaset.formPanel.getForm().reset();
				userximaset.formPanel.getForm().loadRecord(record);
				
				
			}
		});
		
/** 删除 */
userximaset.deleteAction = new Ext.Action({
			text : '删除',
			iconCls : 'Delete',
			disabled : true,
			handler : function() {
				userximaset.delFun();
			}
		});
/***/
userximaset.bindAction = new Ext.Action({
			text : '禁用',
			iconCls : 'Lockkey',
			disabled : true,
			handler : function() {
				userximaset.bindFun(1);
			}
		});
		
userximaset.removeAction = new Ext.Action({
			text : '启用',
			iconCls : 'Lockopen',
			disabled : true,
			handler : function() {
				userximaset.bindFun(0);
			}
		});

userximaset.searchField = new Ext.ux.form.SearchField({
			store : userximaset.store,
			paramName : 'account',
			emptyText : '请输入会员账号',
			style : 'margin-left: 5px;'
		});
/** 顶部工具栏 */
userximaset.tbar = [userximaset.addAction, '-', userximaset.editAction, '-',
		userximaset.deleteAction,'-',userximaset.bindAction,'-',userximaset.removeAction,'-', userximaset.searchField];
/** 底部工具条 */
userximaset.bbar = new Ext.PagingToolbar({
			pageSize : userximaset.pageSize,
			store : userximaset.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', userximaset.pageSizeCombo]
		});
/** 基本信息-表格 */
userximaset.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : userximaset.store,
			colModel : userximaset.colModel,
			selModel : userximaset.selModel,
			tbar : userximaset.tbar,
			bbar : userximaset.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'memberGradeDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});

userximaset.ximascaleNumberField = new Ext.form.NumberField({
		maxLength : 8,
		allowBlank : false,
		decimalPrecision : 4,
		fieldLabel : '洗码比例',
		name : 'ximascale',
		anchor : '99%'
	});
	
/** 基本信息-详细信息的form */
userximaset.formPanel = new Ext.form.FormPanel({
			autoScroll : false,
			border: false,
        	style: 'border-bottom:0px;',
        	bodyStyle: 'padding:10px;background-color:transparent;',
			labelwidth : 50,
			defaultType : 'textfield',
			items : [{
						xtype : 'hidden',
						fieldLabel : 'ID',
						name : 'xmid',
						anchor : '99%'
					},{
						fieldLabel : '会员账号',
						maxLength : 32,
						allowBlank : false,
						name : 'account',
						anchor : '99%'
					},userximaset.ximascaleNumberField]
		});

		

			
/** 编辑新建窗口 */
userximaset.addWindow = new Ext.Window({
			layout : 'fit',
			width : 399,
			height : 140,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [userximaset.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							userximaset.saveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = userximaset.formPanel.getForm();
							var id = form.findField("pyid").getValue();
							form.reset();
							if (id != '')
								form.findField("pyid").setValue(id);
						}
					}]
		});
	
userximaset.alwaysFun = function() {
	Share.resetGrid(userximaset.grid);
	userximaset.deleteAction.disable();
	userximaset.editAction.disable();
	userximaset.bindAction.disable();
	userximaset.removeAction.disable();
};
userximaset.saveFun = function() {
	var form = userximaset.formPanel.getForm();

	if(!form.isValid()){
 		return;
 	}
	// 发送请求
	Share.AjaxRequest({
				url : userximaset.save,
				params : form.getValues(),
				callback : function(json) {
				if (json.success==false){
				     Ext.MessageBox.alert('Status', json.msg, showResult);
					return;
				}else{
				    userximaset.addWindow.hide();
					userximaset.alwaysFun();
					userximaset.store.reload();
				}
					
				}
			});
};

userximaset.delFun = function() {
	var record = userximaset.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '您确定要删除选中会员洗码数据吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : userximaset.del + record.data.xmid +".do",
								callback : function(json) {
									userximaset.alwaysFun();
									userximaset.store.reload();
								}
							});
				}
			});
};

userximaset.bindFun = function(s){
	var record = userximaset.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '确定要修改会员洗码数据状态吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : userximaset.bind + record.data.xmid+"/"+s+".do",
								callback : function(json) {
									userximaset.alwaysFun();
									userximaset.store.reload();
								}
							});
				}
			});
}
  
userximaset.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [userximaset.grid]
		});

</script>