<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.accountMoney"); // 自定义一个命名空间
accountMoney = Ext.market.accountMoney; // 定义命名空间的别名
accountMoney = {
	all : '/manage/memberinfo/queryMoney.do',// 加载所有
	pageSize : 30
};

/** 改变页的combo */
accountMoney.pageSizeCombo = new Share.pageSizeCombo({
	value : '30',
	listeners : {
		select : function(comboBox) {
			accountMoney.pageSize = parseInt(comboBox.getValue());
			accountMoney.bbar.pageSize = parseInt(comboBox.getValue());
			accountMoney.store.baseParams.limit = accountMoney.pageSize;
			accountMoney.store.baseParams.start = 0;
			accountMoney.store.load();
		}
	}
});

// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
accountMoney.pageSize = parseInt(accountMoney.pageSizeCombo.getValue());

/** 基本信息-数据源 */
accountMoney.store = new Ext.data.Store({
		autoLoad : true,
		remoteSort : true,
		baseParams : {
			start : 0,
			limit : accountMoney.pageSize
		},
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : accountMoney.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'uiid','account', 'uname','totalamount']),
		listeners : {
			'load' : function(store, records, options) {
				Share.resetGrid(accountMoney.grid);
			}
		}
});

/** 基本信息-选择模式 */
accountMoney.selModel = new Ext.grid.CheckboxSelectionModel({
	singleSelect : true
});

/** 基本信息-数据列 */
accountMoney.colModel = new Ext.grid.ColumnModel({
	defaults : {
		sortable : true,
		width : 140
	},
	columns : [accountMoney.selModel, {
				hidden : true,
				header : 'ID',
				dataIndex : 'uiid'
			}, {
				header : '会员账号',
				dataIndex : 'account',
				width : 120,
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			}, {
				header : '用户姓名',
				dataIndex : 'uname',
				width : 80,
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			}, {
				header : '钱包余额',
				dataIndex : 'totalamount',
				width :80,
				renderer : function(v) {
					return '<span style="color:blue;">'+v+'</span>';
				}
		  }]
});

/** 查询 */
accountMoney.searchField = new Ext.ux.form.SearchField({
	store : accountMoney.store,
	paramName : 'account',
	emptyText : '请输入会员账号',
	style : 'margin-left: 5px;'
});

accountMoney.tbar = [accountMoney.searchField];

/** 底部工具条 */
accountMoney.bbar = new Ext.PagingToolbar({
	pageSize : accountMoney.pageSize,
	store : accountMoney.store,
	displayInfo : true,
	// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
	items : ['-', '&nbsp;', accountMoney.pageSizeCombo]
});

/** 基本信息-表格 */
accountMoney.grid = new Ext.grid.EditorGridPanel({
	// title : '模块列表',
	store : accountMoney.store,
	colModel : accountMoney.colModel,
	selModel : accountMoney.selModel,
	tbar : accountMoney.tbar,
	bbar : accountMoney.bbar,
	autoScroll : 'auto',
	region : 'center',
	loadMask : true,
	stripeRows : true,
	listeners : {},
	viewConfig : {}
});

accountMoney.myPanel = new Ext.Panel({
	id : '${id}' + '_panel',
	renderTo : '${id}',
	layout : 'border',
	boder : false,
	height : index.tabPanel.getInnerHeight() - 1,
	items : [accountMoney.grid]
});
</script>