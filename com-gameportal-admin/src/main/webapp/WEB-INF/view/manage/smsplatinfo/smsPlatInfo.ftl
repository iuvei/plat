<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.smsPlatInfos"); // 自定义一个命名空间
smsPlatInfo = Ext.market.smsPlatInfos; // 定义命名空间的别名
smsPlatInfo = {
	all : '/manage/smsplatinfo/smslog/querySmsLoglacklist.do',// 加载所有
	pageSize : 30, // 每页显示的记录数
	ENABLED : eval('(${fields.enabled})')
};


/** 改变页的combo */
smsPlatInfo.pageSizeCombo = new Share.pageSizeCombo({
			value : '30',
			listeners : {
				select : function(comboBox) {
					smsPlatInfo.pageSize = parseInt(comboBox.getValue());
					smsPlatInfo.bbar.pageSize = parseInt(comboBox.getValue());
					smsPlatInfo.store.baseParams.limit = smsPlatInfo.pageSize;
					smsPlatInfo.store.baseParams.start = 0;
					smsPlatInfo.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
smsPlatInfo.pageSize = parseInt(smsPlatInfo.pageSizeCombo.getValue());
/** 基本信息-数据源 */
smsPlatInfo.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : smsPlatInfo.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : smsPlatInfo.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['spsid', 'spaid', 'username', 'mobile', 'content', 'type', 
			    	'sendtime','servername']),
			listeners : {
				'load' : function(store, records, options) {
					smsPlatInfo.alwaysFun();
				}
			}
		});
/** 基本信息-选择模式 */
smsPlatInfo.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				smsPlatInfo.deleteAction.enable();
				smsPlatInfo.editAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				smsPlatInfo.alwaysFun();
			}
		}
	});
/** 基本信息-数据列 */
smsPlatInfo.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [smsPlatInfo.selModel, {
						hidden : true,
						header : '短信编号',
						dataIndex : 'spsid'
					}, {
						header : '平台名称',
						dataIndex : 'servername',
						width:130,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '会员账号',
						dataIndex : 'username',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '短信内容',
						dataIndex : 'content',
						width:650,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '短信用途',
						dataIndex : 'type',
						width:60,
						renderer : function(v) {
							if(v == 1){
								return '<span style="color:blue;">首次提款</span>';
							}else if(v == 2){
								return '<span style="color:green;">找回密码</span>';
							}else if(v ==4){
								return '<span style="color:#FF3399;">客户要求</span>';
							}else{
								return '<span style="color:#9c6927;">手机认证</span>';
							}
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '发送时间',
						dataIndex : 'sendtime',
						width:130,
						renderer : function(v) {
							return new Date(v).format('Y-m-d H:i:s');
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}]
		});
		
/** 查询 */
smsPlatInfo.searchField = new Ext.ux.form.SearchField({
			store : smsPlatInfo.store,
			paramName : 'username',
			emptyText : '请输入会员账号',
			style : 'margin-left: 5px;'
		});
/** 顶部工具栏 */
smsPlatInfo.tbar = [smsPlatInfo.searchField];
/** 底部工具条 */
smsPlatInfo.bbar = new Ext.PagingToolbar({
			pageSize : smsPlatInfo.pageSize,
			store : smsPlatInfo.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', smsPlatInfo.pageSizeCombo]
		});
/** 基本信息-表格 */
smsPlatInfo.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : smsPlatInfo.store,
			colModel : smsPlatInfo.colModel,
			selModel : smsPlatInfo.selModel,
			tbar : smsPlatInfo.tbar,
			bbar : smsPlatInfo.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'smsPlatInfoDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});

smsPlatInfo.statusCombo = new Ext.form.ComboBox({
			fieldLabel : '状态',
			hiddenName : 'status',
			name : 'status',
			triggerAction : 'all',
			mode : 'local',
			store : new Ext.data.ArrayStore({
						fields : ['v', 't'],
						data : Share.map2Ary(smsPlatInfo.ENABLED)
					}),
			valueField : 'v',
			displayField : 't',
			allowBlank : false,
			editable : false,
			value : "1",
			anchor : '99%'
		});
		
/** 基本信息-详细信息的form */
smsPlatInfo.formPanel = new Ext.form.FormPanel({
			frame : true,
			//title : '平台信息',
			bodyStyle : 'padding:10px;border:0px',
			labelwidth : 50,
			defaultType : 'textfield',
			items : [{
						xtype : 'hidden',
						fieldLabel : 'ID',
						name : 'spiid',
						anchor : '99%'
					}, {
						fieldLabel : '平台名称',
						maxLength : 64,
						allowBlank : false,
						name : 'name',
						anchor : '99%'
					},smsPlatInfo.statusCombo]
		});
/** 编辑新建窗口 */
smsPlatInfo.addWindow = new Ext.Window({
			layout : 'fit',
			width : 500,
			height : 420,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [smsPlatInfo.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							smsPlatInfo.saveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = smsPlatInfo.formPanel.getForm();
							var id = form.findField("spiid").getValue();
							form.reset();
							if (id != '')
								form.findField("spiid").setValue(id);
						}
					}]
		});
smsPlatInfo.alwaysFun = function() {
	Share.resetGrid(smsPlatInfo.grid);
};
smsPlatInfo.saveFun = function() {
	var form = smsPlatInfo.formPanel.getForm();
	if (!form.isValid()) {
		return;
	}
	// 发送请求
	Share.AjaxRequest({
				url : smsPlatInfo.save,
				params : form.getValues(),
				callback : function(json) {
					smsPlatInfo.addWindow.hide();
					smsPlatInfo.alwaysFun();
					smsPlatInfo.store.reload();
				}
			});
};
smsPlatInfo.delFun = function() {
	var record = smsPlatInfo.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '你真的要删除选中的短信平台吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : smsPlatInfo.del + record.data.spiid +".do",
								callback : function(json) {
									smsPlatInfo.alwaysFun();
									smsPlatInfo.store.reload();
								}
							});
				}
			});
};
smsPlatInfo.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [smsPlatInfo.grid]
		});
</script>