<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.memberUpgradeLogs"); // 自定义一个命名空间
gameTransferLog = Ext.market.transferLogs; // 定义命名空间的别名
gameTransferLog = {
	all : '/manage/gameTransferLog/queryGameTransferLog.do',// 加载所有
	pageSize : 30, // 每页显示的记录数
	memGrade:eval('(${fields.memGrade})')
};


/** 改变页的combo */
gameTransferLog.pageSizeCombo = new Share.pageSizeCombo({
			value : '30',
			listeners : {
				select : function(comboBox) {
					gameTransferLog.pageSize = parseInt(comboBox.getValue());
					gameTransferLog.bbar.pageSize = parseInt(comboBox.getValue());
					gameTransferLog.store.baseParams.limit = gameTransferLog.pageSize;
					gameTransferLog.store.baseParams.start = 0;
					gameTransferLog.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
gameTransferLog.pageSize = parseInt(gameTransferLog.pageSizeCombo.getValue());
/** 基本信息-数据源 */
gameTransferLog.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : gameTransferLog.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : gameTransferLog.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['lid', 'uid', 'account', 'formplat','toplat','amount','createDate','createusername','createuserid'
			    	]),
			listeners : {
				'load' : function(store, records, options) {
				//	gameTransferLog.alwaysFun();
				}
			}
		});
		
/** 基本信息-选择模式 */
gameTransferLog.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				
				
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				gameTransferLog.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
gameTransferLog.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [gameTransferLog.selModel, {
						hidden : true,
						header : 'ID',
						dataIndex : 'lid'
					}, {
						hidden : true,
						header : 'uid',
						dataIndex : 'uid'
					}, {
						header : '账号',
						dataIndex : 'account',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '转出平台',
						dataIndex : 'formplat',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '转入平台',
						dataIndex : 'toplat',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '金额',
						dataIndex : 'amount',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					} ,{
						header : '备注',
						dataIndex : 'createuserid',
						width : 130,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '操作员',
						dataIndex : 'createusername',
						width : 100,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					},{
						header : '操作日期',
						dataIndex : 'createDate',
						width : 130,
						renderer : function(v) {
							return v==null?'':new Date(v).format('Y-m-d H:i:s');
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}]
		});
		


/** 查询 */
gameTransferLog.searchField = new Ext.ux.form.SearchField({
			store : gameTransferLog.store,
			paramName : 'account',
			emptyText : '请输入会员帐号',
			style : 'margin-left: 5px;'
		});

	
/** 顶部工具栏 */
gameTransferLog.tbar = [gameTransferLog.searchField];
/** 底部工具条 */
gameTransferLog.bbar = new Ext.PagingToolbar({
			pageSize : gameTransferLog.pageSize,
			store : gameTransferLog.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', gameTransferLog.pageSizeCombo]
		});
/** 基本信息-表格 */
gameTransferLog.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : gameTransferLog.store,
			colModel : gameTransferLog.colModel,
			selModel : gameTransferLog.selModel,
			tbar : gameTransferLog.tbar,
			bbar : gameTransferLog.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'gameTransferLogDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});


	

	
gameTransferLog.alwaysFun = function() {
	Share.resetGrid(gameTransferLog.grid);

	
};



  
gameTransferLog.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [gameTransferLog.grid]
		});

</script>