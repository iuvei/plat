<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.marketingchannel"); // 自定义一个命名空间
marketingchannel = Ext.market.marketingchannel; // 定义命名空间的别名
marketingchannel = {
	all : '/manage/marketingchannel/queryMarketingChannel.do',// 加载所有
	save : "/manage/marketingchannel/saveMarketingChannel.do",//保存
	del : "/manage/proxy/deldomain/",//删除
	bind : "/manage/proxy/bindomain/",
	pageSize : 30, // 每页显示的记录数
	cStatus : eval('(${fields.enabled})'),
	memGrade:eval('(${fields.memGrade})')
};


/** 改变页的combo */
marketingchannel.pageSizeCombo = new Share.pageSizeCombo({
			value : '30',
			listeners : {
				select : function(comboBox) {
					marketingchannel.pageSize = parseInt(comboBox.getValue());
					marketingchannel.bbar.pageSize = parseInt(comboBox.getValue());
					marketingchannel.store.baseParams.limit = marketingchannel.pageSize;
					marketingchannel.store.baseParams.start = 0;
					marketingchannel.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
marketingchannel.pageSize = parseInt(marketingchannel.pageSizeCombo.getValue());
/** 基本信息-数据源 */
marketingchannel.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : marketingchannel.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : marketingchannel.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['channelid', 'channelkey', 'channelname', 'channelvalue',"channelurl",'channelremark','channelstatus','channeltime']),
			listeners : {
				'load' : function(store, records, options) {
				//	memberGrade.alwaysFun();
				}
			}
		});
		
/** 基本信息-选择模式 */
marketingchannel.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				marketingchannel.editAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				marketingchannel.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
marketingchannel.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [marketingchannel.selModel, {
						hidden : true,
						header : 'ID',
						dataIndex : 'channelid'
					},{
						header : '渠道Key',
						dataIndex : 'channelkey',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '渠道名称',
						dataIndex : 'channelname',
						width : 100,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '渠道值',
						dataIndex : 'channelvalue',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '渠道域名',
						dataIndex : 'channelurl',
						width : 300,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '渠道备注',
						dataIndex : 'channelremark',
						width : 200,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '发布时间',
						dataIndex : 'channeltime',
						width : 140,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '绑定状态',
						dataIndex : 'channelstatus',
						width : 80,
						renderer : function(v) {
							if(v == 1){
								return '<span style="color:green;">已启用</span>';
							}else{
								return '<span style="color:red;">未启用</span>';
							}
      					}

					}]
		});
		
/** 新建 */
marketingchannel.addAction = new Ext.Action({
			text : '新建',
			iconCls : 'Add',
			handler : function() {
				marketingchannel.addWindow.setIconClass('Applicationadd'); // 设置窗口的样式
				marketingchannel.addWindow.setTitle('新建推广渠道'); // 设置窗口的名称
				marketingchannel.addWindow.show().center(); // 显示窗口
				marketingchannel.formPanel.getForm().reset(); // 清空表单里面的元素的值.

			}
		});
/** 编辑 */
marketingchannel.editAction = new Ext.Action({
			text : '编辑',
			iconCls : 'Applicationedit',
			disabled : true,
			handler : function() {
				var record = marketingchannel.grid.getSelectionModel().getSelected();
				marketingchannel.addWindow.setIconClass('Applicationedit'); // 设置窗口的样式
				marketingchannel.addWindow.setTitle('编辑域名'); // 设置窗口的名称
				marketingchannel.addWindow.show().center();
				marketingchannel.formPanel.getForm().reset();
				marketingchannel.formPanel.getForm().loadRecord(record);
				
				
			}
		});
	/** 查询按钮 */
	marketingchannel.searchAction = new Ext.Action({
			text : '查询',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				marketingchannel.searchFun();
			}
		
	});

	marketingchannel.searchParams = function(){
		var obj = {};
		obj.channelname =  $("#channelname").val();
	    return obj;
	}
	
	marketingchannel.searchFun = function(){
		marketingchannel.store.load({params: marketingchannel.searchParams()});
	}
	
	marketingchannel.store.on('beforeload',function(store, options){
	    marketingchannel.store.baseParams = marketingchannel.searchParams();
	});



	
/** 顶部工具栏 */
marketingchannel.tbar = [marketingchannel.addAction, '-', marketingchannel.editAction, '-',
		'-','&nbsp;','渠道名称:',{id:'channelname',xtype:'textfield',width:80},
		'-',marketingchannel.searchAction
		
		];
/** 底部工具条 */
marketingchannel.bbar = new Ext.PagingToolbar({
			pageSize : marketingchannel.pageSize,
			store : marketingchannel.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', marketingchannel.pageSizeCombo]
		});
/** 基本信息-表格 */
marketingchannel.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : marketingchannel.store,
			colModel : marketingchannel.colModel,
			selModel : marketingchannel.selModel,
			tbar : marketingchannel.tbar,
			bbar : marketingchannel.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'memberGradeDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});
			
marketingchannel.mcStatusComboBox = new Ext.form.ComboBox({
		fieldLabel : '渠道状态',
		hiddenName : 'channelstatus',
		name : 'channelstatus',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(marketingchannel.cStatus)
				}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		value : "1",
		anchor : '99%'
	});
	
/** 基本信息-详细信息的form */
marketingchannel.formPanel = new Ext.form.FormPanel({
			autoScroll : false,
			border: false,
        	style: 'border-bottom:0px;',
        	bodyStyle: 'padding:10px;background-color:transparent;',
			labelwidth : 50,
			defaultType : 'textfield',
			items : [{
						xtype : 'hidden',
						fieldLabel : 'ID',
						name : 'channelid',
						anchor : '99%'
					},{
						fieldLabel : '渠道Key',
						maxLength : 32,
						allowBlank : false,
						name : 'channelkey',
						anchor : '99%'
					},{
						fieldLabel : '渠道名称',
						maxLength : 32,
						allowBlank : false,
						name : 'channelname',
						anchor : '99%'
					},{
						fieldLabel : '渠道值',
						maxLength : 32,
						allowBlank : false,
						name : 'channelvalue',
						anchor : '99%'
					},marketingchannel.mcStatusComboBox,{
						fieldLabel : '备注',
						xtype : 'textarea',
						allowBlank : false,
						name : 'channelremark',
						anchor : '99%'
					}]
		});

		

			
/** 编辑新建窗口 */
marketingchannel.addWindow = new Ext.Window({
			layout : 'fit',
			width : 399,
			height : 252,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [marketingchannel.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							marketingchannel.saveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = marketingchannel.formPanel.getForm();
							var id = form.findField("channelid").getValue();
							form.reset();
							if (id != '')
								form.findField("channelid").setValue(id);
						}
					}]
		});
	
marketingchannel.alwaysFun = function() {
	Share.resetGrid(marketingchannel.grid);
	marketingchannel.editAction.disable();
};
marketingchannel.saveFun = function() {
	var form = marketingchannel.formPanel.getForm();

	
	// 发送请求
	Share.AjaxRequest({
				url : marketingchannel.save,
				params : form.getValues(),
				callback : function(json) {
				if (json.success==false){
				     Ext.MessageBox.alert('Status', json.msg, showResult);
					return;
				}else{
				    marketingchannel.addWindow.hide();
					marketingchannel.alwaysFun();
					marketingchannel.store.reload();
				}
					
				}
			});
};

marketingchannel.delFun = function() {
	var record = marketingchannel.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '你真的要删除选中的域名吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : marketingchannel.del + record.data.urlid +".do",
								callback : function(json) {
									marketingchannel.alwaysFun();
									marketingchannel.store.reload();
								}
							});
				}
			});
};

marketingchannel.bindFun = function(s){
	var record = marketingchannel.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '确定要修改域名绑定状态吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : marketingchannel.bind + record.data.urlid+"/"+s+".do",
								callback : function(json) {
									marketingchannel.alwaysFun();
									marketingchannel.store.reload();
								}
							});
				}
			});
}
  
marketingchannel.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [marketingchannel.grid]
		});

</script>