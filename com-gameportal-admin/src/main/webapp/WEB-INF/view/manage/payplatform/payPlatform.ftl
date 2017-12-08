<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.payPlatforms"); // 自定义一个命名空间
payPlatform = Ext.market.payPlatforms; // 定义命名空间的别名
payPlatform = {
	all : '/manage/payplatform/queryPayPlatform.do',// 加载所有
	save : "/manage/payplatform/savePayPlatform.do",//保存
	del : "/manage/payplatform/delPayPlatform/",//删除
	pageSize : 20, // 每页显示的记录数
	ENABLED : eval('(${fields.enabled})')
};


/** 改变页的combo */
payPlatform.pageSizeCombo = new Share.pageSizeCombo({
			value : '20',
			listeners : {
				select : function(comboBox) {
					payPlatform.pageSize = parseInt(comboBox.getValue());
					payPlatform.bbar.pageSize = parseInt(comboBox.getValue());
					payPlatform.store.baseParams.limit = payPlatform.pageSize;
					payPlatform.store.baseParams.start = 0;
					payPlatform.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
payPlatform.pageSize = parseInt(payPlatform.pageSizeCombo.getValue());
/** 基本信息-数据源 */
payPlatform.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : payPlatform.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : payPlatform.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['ppid', 'pname', 'domainname', 'platformkey', 'ciphercode',
				'returnUrl', 'noticeUrl', 'status', 'createDate', 'updateDate','sequence','channelType','fee']),
			listeners : {
				'load' : function(store, records, options) {
					payPlatform.alwaysFun();
				}
			}
		});
/** 基本信息-选择模式 */
payPlatform.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				payPlatform.deleteAction.enable();
				payPlatform.editAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				payPlatform.alwaysFun();
			}
		}
	});
/** 基本信息-数据列 */
payPlatform.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [payPlatform.selModel, {
						hidden : true,
						header : '平台编号',
						dataIndex : 'ppid'
					}, {
						header : '平台名称',
						dataIndex : 'pname',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '平台域名',
						dataIndex : 'domainname',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '平台KEY',
						dataIndex : 'platformkey',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '平台密码',
						dataIndex : 'ciphercode',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '商户回调地址',
						dataIndex : 'returnUrl',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '商户通知地址',
						dataIndex : 'noticeUrl',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '终端类型',
						dataIndex : 'channelType',
						width : 60,
						renderer : function(v) {
							if(v == 1){
								return '<span style="color:green;">PC端</span>';
							}else{
								return '<span style="color:red;">WAP端</span>';
							}
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '手续费',
						dataIndex : 'fee',
						width : 60,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '排序',
						width : 60,
						dataIndex : 'sequence',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '状态',
						dataIndex : 'status',
						renderer : function(v) {
							if(v == 1){
								return '<span style="color:green;">启用</span>';
							}else{
								return '<span style="color:red;">禁用</span>';
							}
							//return Share.map(v, payPlatform.ENABLED, '');
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}]
		});
/** 新建 */
payPlatform.addAction = new Ext.Action({
			text : '新建',
			iconCls : 'payPlatform_add',
			handler : function() {
				payPlatform.addWindow.setIconClass('payPlatform_add'); // 设置窗口的样式
				payPlatform.addWindow.setTitle('新建平台'); // 设置窗口的名称
				payPlatform.addWindow.show().center(); // 显示窗口
				payPlatform.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			}
		});
/** 编辑 */
payPlatform.editAction = new Ext.Action({
			text : '编辑',
			iconCls : 'payPlatform_edit',
			disabled : true,
			handler : function() {
				var record = payPlatform.grid.getSelectionModel().getSelected();
				payPlatform.addWindow.setIconClass('payPlatform_edit'); // 设置窗口的样式
				payPlatform.addWindow.setTitle('编辑平台'); // 设置窗口的名称
				payPlatform.addWindow.show().center();
				payPlatform.formPanel.getForm().reset();
				payPlatform.formPanel.getForm().loadRecord(record);
			}
		});
/** 删除 */
payPlatform.deleteAction = new Ext.Action({
			text : '删除',
			iconCls : 'payPlatform_delete',
			disabled : true,
			handler : function() {
				payPlatform.delFun();
			}
		});
/** 查询 */
payPlatform.searchField = new Ext.Action({
		text : '查询',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				payPlatform.store.load({params: payPlatform.searchParams()});
			}
	});
	
	payPlatform.store.on('beforeload',function(store, options){
	    payPlatform.store.baseParams = payPlatform.searchParams();
	});

payPlatform.searchParams = function(){
	var obj = {};
	var payPlatformName=$("#payPlatformName").val();
	var channelType = $("#channelType").prev().val(); 
	var status = $("#status").prev().val(); 
	obj.pname = payPlatformName;
	obj.channelType=channelType;
	obj.status=status;
    return obj;
}
	
/** 顶部工具栏 */
payPlatform.tbar = [payPlatform.addAction, '-', payPlatform.editAction, '-',
		payPlatform.deleteAction, '-','渠道名称:',{id:'payPlatformName',xtype:'textfield',width:200},'-',
'终端类型:',
                       		new Ext.form.ComboBox({
								hiddenName :'channelType',
								id : 'channelType',
								triggerAction : 'all',
								mode : 'local',
								store : new Ext.data.ArrayStore({
											fields : ['v', 't'],
											data : [['', '全部'], ['1', 'PC端'],['2', 'WAP端']]
										}),
								valueField : 'v',
								displayField : 't',
								allowBlank : true,
								editable : true,
								value:'',
								width:80
		
							}),'-','状态:',
                       		new Ext.form.ComboBox({
								hiddenName :'status',
								id : 'status',
								triggerAction : 'all',
								mode : 'local',
								store : new Ext.data.ArrayStore({
											fields : ['v', 't'],
											data : [['', '全部'], ['1', '启用'],['0', '禁用']]
										}),
								valueField : 'v',
								displayField : 't',
								allowBlank : true,
								editable : true,
								value:'',
								width:80
								
			
							}),
payPlatform.searchField];
		
		
/** 底部工具条 */
payPlatform.bbar = new Ext.PagingToolbar({
			pageSize : payPlatform.pageSize,
			store : payPlatform.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', payPlatform.pageSizeCombo]
		});
/** 基本信息-表格 */
payPlatform.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : payPlatform.store,
			colModel : payPlatform.colModel,
			selModel : payPlatform.selModel,
			tbar : payPlatform.tbar,
			bbar : payPlatform.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'payPlatformDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});

payPlatform.statusCombo = new Ext.form.ComboBox({
			fieldLabel : '状态',
			hiddenName : 'status',
			name : 'status',
			triggerAction : 'all',
			mode : 'local',
			store : new Ext.data.ArrayStore({
						fields : ['v', 't'],
						data : Share.map2Ary(payPlatform.ENABLED)
					}),
			valueField : 'v',
			displayField : 't',
			allowBlank : false,
			editable : false,
			value : "1",
			anchor : '99%'
		});
		
/** 基本信息-详细信息的form */
payPlatform.formPanel = new Ext.form.FormPanel({
			frame : true,
			//title : '平台信息',
			bodyStyle : 'padding:10px;border:0px',
			labelwidth : 50,
			defaultType : 'textfield',
			items : [{
						xtype : 'hidden',
						fieldLabel : 'ID',
						name : 'ppid',
						anchor : '99%'
					}, {
						fieldLabel : '平台名称',
						name : 'pname',
						anchor : '99%'
					},{
						fieldLabel : '平台域名',
						name : 'domainname',
						anchor : '99%'
					},{
						fieldLabel : '平台KEY',
						name : 'platformkey',
						anchor : '99%'
					},{
						fieldLabel : '平台密码',
						name : 'ciphercode',
						anchor : '99%'
					},{
						fieldLabel : '商户回调地址',
						name : 'returnUrl',
						anchor : '99%'
					},{
						fieldLabel : '商户通知地址',
						name : 'noticeUrl',
						anchor : '99%'
					},{
						fieldLabel : '手续费',
						name : 'fee',
						anchor : '99%'
					},{
						fieldLabel : '排序',
						name : 'sequence',
						anchor : '99%'
					},payPlatform.statusCombo]
		});
/** 编辑新建窗口 */
payPlatform.addWindow = new Ext.Window({
			layout : 'fit',
			width : 500,
			height : 400,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [payPlatform.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							payPlatform.saveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = payPlatform.formPanel.getForm();
							var id = form.findField("ppid").getValue();
							form.reset();
							if (id != '')
								form.findField("ppid").setValue(id);
						}
					}]
		});
payPlatform.alwaysFun = function() {
	Share.resetGrid(payPlatform.grid);
	payPlatform.deleteAction.disable();
	payPlatform.editAction.disable();
};
payPlatform.saveFun = function() {
	var form = payPlatform.formPanel.getForm();
	if (!form.isValid()) {
		return;
	}
	// 发送请求
	Share.AjaxRequest({
				url : payPlatform.save,
				params : form.getValues(),
				callback : function(json) {
					payPlatform.addWindow.hide();
					payPlatform.alwaysFun();
					payPlatform.store.reload();
				}
			});
};
payPlatform.delFun = function() {
	var record = payPlatform.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '你真的要删除选中菜单及其包含的所有子菜单吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : payPlatform.del + record.data.ppid +".do",
								callback : function(json) {
									payPlatform.alwaysFun();
									payPlatform.store.reload();
								}
							});
				}
			});
};
payPlatform.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [payPlatform.grid]
		});
</script>