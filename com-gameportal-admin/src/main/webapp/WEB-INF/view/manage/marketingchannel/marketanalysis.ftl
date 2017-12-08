<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.marketanalysis"); // 自定义一个命名空间
marketanalysis = Ext.market.marketanalysis; // 定义命名空间的别名
marketanalysis = {
	all : '/manage/marketingchannel/queryMarketAnalysis.do',// 加载所有
	save : "/manage/proxy/saveProxyDomainInfo.do",//保存
	del : "/manage/proxy/deldomain/",//删除
	bind : "/manage/proxy/bindomain/",
	pageSize : 30, // 每页显示的记录数
	memGrade:eval('(${fields.memGrade})')
};


/** 改变页的combo */
marketanalysis.pageSizeCombo = new Share.pageSizeCombo({
			value : '30',
			listeners : {
				select : function(comboBox) {
					marketanalysis.pageSize = parseInt(comboBox.getValue());
					marketanalysis.bbar.pageSize = parseInt(comboBox.getValue());
					marketanalysis.store.baseParams.limit = marketanalysis.pageSize;
					marketanalysis.store.baseParams.start = 0;
					marketanalysis.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
marketanalysis.pageSize = parseInt(marketanalysis.pageSizeCombo.getValue());
/** 基本信息-数据源 */
marketanalysis.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : marketanalysis.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : marketanalysis.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['channel', 'regcount', 'paycount', 'payamont']),
			listeners : {
				'load' : function(store, records, options) {
				//	memberGrade.alwaysFun();
				}
			}
		});
		
/** 基本信息-选择模式 */
marketanalysis.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				marketanalysis.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
marketanalysis.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [marketanalysis.selModel,{
						header : '推广渠道',
						dataIndex : 'channel',
						width : 100,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '注册人数',
						dataIndex : 'regcount',
						width : 100,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '充值人数',
						dataIndex : 'paycount',
						width : 100,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '充值金额',
						dataIndex : 'payamont',
						width : 100,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}]
		});
		
/** 新建 */
marketanalysis.addAction = new Ext.Action({
			text : '新建',
			iconCls : 'Add',
			handler : function() {
				//marketanalysis.addWindow.setIconClass('Applicationadd'); // 设置窗口的样式
				//marketanalysis.addWindow.setTitle('新建域名绑定'); // 设置窗口的名称
				//marketanalysis.addWindow.show().center(); // 显示窗口
				//marketanalysis.formPanel.getForm().reset(); // 清空表单里面的元素的值.

			}
		});

	/** 查询按钮 */
	marketanalysis.searchAction = new Ext.Action({
			text : '查询',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				marketanalysis.searchFun();
			}
		
	});

	marketanalysis.searchParams = function(){
		var obj = {};
		obj.channel = $("#channel").val();
		obj.startdate = $("#maStartdate").val();
		obj.enddate = $("#maEnddate").val();
	    return obj;
	}
	
	marketanalysis.searchFun = function(){
		marketanalysis.store.load({params: marketanalysis.searchParams()});
	}
	
	marketanalysis.store.on('beforeload',function(store, options){
	    marketanalysis.store.baseParams = marketanalysis.searchParams();
	});


marketanalysis.maStartdate = new  Ext.form.DateField({ 
		id:'maStartdate',
		showToday:true,
		format:'Y-m-d',
		invalidText:'日期输入非法',
		width:120
});
marketanalysis.maEnddate = new  Ext.form.DateField({ 
		id:'maEnddate',
		showToday:true,
		format:'Y-m-d',
		invalidText:'日期输入非法',
		width:120
});
	
/** 顶部工具栏 */
marketanalysis.tbar = [marketanalysis.addAction,
		'-','&nbsp;','渠道名称:',{id:'channel',xtype:'textfield',width:120},
		'-','&nbsp;','开始日期:',marketanalysis.maStartdate,
		'至',marketanalysis.maEnddate,
		'-',marketanalysis.searchAction
		
		];
/** 底部工具条 */
marketanalysis.bbar = new Ext.PagingToolbar({
			pageSize : marketanalysis.pageSize,
			store : marketanalysis.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', marketanalysis.pageSizeCombo]
		});
/** 基本信息-表格 */
marketanalysis.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : marketanalysis.store,
			colModel : marketanalysis.colModel,
			selModel : marketanalysis.selModel,
			tbar : marketanalysis.tbar,
			bbar : marketanalysis.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'memberGradeDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});
			
	
/** 基本信息-详细信息的form */
marketanalysis.formPanel = new Ext.form.FormPanel({
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
marketanalysis.addWindow = new Ext.Window({
			layout : 'fit',
			width : 399,
			height : 142,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [marketanalysis.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							marketanalysis.saveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = marketanalysis.formPanel.getForm();
							var id = form.findField("gid").getValue();
							form.reset();
							if (id != '')
								form.findField("gid").setValue(id);
						}
					}]
		});
	
marketanalysis.alwaysFun = function() {
	Share.resetGrid(marketanalysis.grid);

};
marketanalysis.saveFun = function() {
	var form = marketanalysis.formPanel.getForm();

	
	// 发送请求
	Share.AjaxRequest({
				url : marketanalysis.save,
				params : form.getValues(),
				callback : function(json) {
				if (json.success==false){
				     Ext.MessageBox.alert('Status', json.msg, showResult);
					return;
				}else{
				    marketanalysis.addWindow.hide();
					marketanalysis.alwaysFun();
					marketanalysis.store.reload();
				}
					
				}
			});
};

marketanalysis.delFun = function() {
	var record = marketanalysis.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '你真的要删除选中的域名吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : marketanalysis.del + record.data.urlid +".do",
								callback : function(json) {
									marketanalysis.alwaysFun();
									marketanalysis.store.reload();
								}
							});
				}
			});
};

marketanalysis.bindFun = function(s){
	var record = marketanalysis.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '确定要修改域名绑定状态吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : marketanalysis.bind + record.data.urlid+"/"+s+".do",
								callback : function(json) {
									marketanalysis.alwaysFun();
									marketanalysis.store.reload();
								}
							});
				}
			});
}
  
marketanalysis.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [marketanalysis.grid]
		});

</script>