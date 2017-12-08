<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.memberUpgradeLogs"); // 自定义一个命名空间
memberUpgradeLog = Ext.market.memberUpgradeLogs; // 定义命名空间的别名
memberUpgradeLog = {
	all : '/manage/memberUpgradeLog/queryMemberUpgradeLog.do',// 加载所有
	pageSize : 20, // 每页显示的记录数
	memGrade:eval('(${fields.memGrade})')
};


/** 改变页的combo */
memberUpgradeLog.pageSizeCombo = new Share.pageSizeCombo({
			value : '20',
			listeners : {
				select : function(comboBox) {
					memberUpgradeLog.pageSize = parseInt(comboBox.getValue());
					memberUpgradeLog.bbar.pageSize = parseInt(comboBox.getValue());
					memberUpgradeLog.store.baseParams.limit = memberUpgradeLog.pageSize;
					memberUpgradeLog.store.baseParams.start = 0;
					memberUpgradeLog.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
memberUpgradeLog.pageSize = parseInt(memberUpgradeLog.pageSizeCombo.getValue());
/** 基本信息-数据源 */
memberUpgradeLog.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : memberUpgradeLog.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : memberUpgradeLog.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['lid', 'uid', 'account', 'oldgrade','newgrade','reason','createDate','createusername','createuserid'
			    	]),
			listeners : {
				'load' : function(store, records, options) {
				//	memberUpgradeLog.alwaysFun();
				}
			}
		});
		
/** 基本信息-选择模式 */
memberUpgradeLog.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				
				
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				memberUpgradeLog.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
memberUpgradeLog.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [memberUpgradeLog.selModel, {
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
						header : '原等级',
						dataIndex : 'oldgrade',
						width : 80,
						renderer : function(v) {
							return Share.map(v, memberUpgradeLog.memGrade, '');
       						
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '现等级',
						dataIndex : 'newgrade',
						width : 80,
						renderer : function(v) {
							return Share.map(v, memberUpgradeLog.memGrade, '');
       						
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '变更理由',
						dataIndex : 'reason',
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
       						return new Date(v).format('Y-m-d H:i:s');
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}]
		});
		


/** 查询 */
memberUpgradeLog.searchField = new Ext.ux.form.SearchField({
			store : memberUpgradeLog.store,
			paramName : 'account',
			emptyText : '请输入会员帐号',
			style : 'margin-left: 5px;'
		});

	
/** 顶部工具栏 */
memberUpgradeLog.tbar = [memberUpgradeLog.searchField];
/** 底部工具条 */
memberUpgradeLog.bbar = new Ext.PagingToolbar({
			pageSize : memberUpgradeLog.pageSize,
			store : memberUpgradeLog.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', memberUpgradeLog.pageSizeCombo]
		});
/** 基本信息-表格 */
memberUpgradeLog.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : memberUpgradeLog.store,
			colModel : memberUpgradeLog.colModel,
			selModel : memberUpgradeLog.selModel,
			tbar : memberUpgradeLog.tbar,
			bbar : memberUpgradeLog.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'memberUpgradeLogDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});


	

	
memberUpgradeLog.alwaysFun = function() {
	Share.resetGrid(memberUpgradeLog.grid);

	
};



  
memberUpgradeLog.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [memberUpgradeLog.grid]
		});

</script>