<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.proxyInfos"); // 自定义一个命名空间
reportuser = Ext.market.proxyInfos; // 定义命名空间的别名
reportuser = {
	all : '/proxymanage/m/queryReport.do',// 加载所有
	gameMoney:"/manage/memberinfo/queryGameMoney.do",//加载游戏余额
	pageSize : 30 // 每页显示的记录数
};


/** 改变页的combo */
reportuser.pageSizeCombo = new Share.pageSizeCombo({
			value : '30',
			listeners : {
				select : function(comboBox) {
					reportuser.pageSize = parseInt(comboBox.getValue());
					reportuser.bbar.pageSize = parseInt(comboBox.getValue());
					reportuser.store.baseParams.limit = reportuser.pageSize;
					reportuser.store.baseParams.start = 0;
					reportuser.store.load();
				}
			}
		});
		
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
reportuser.pageSize = parseInt(reportuser.pageSizeCombo.getValue());
/** 基本信息-数据源 */
reportuser.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				account : '',
				startDate : '',
				endDate : '',
				start : 0,
				limit : reportuser.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : reportuser.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['uiid','apipassword', 'account', 'uname','money','depositTotal','withdrawalTotal','preferentialTotal','ximaTotal','winLossTotal']),
			listeners : {
				'load' : function(store, records, options) {
					reportuser.amountsum();
				}
			}
		});
		
/** 基本信息-选择模式 */
reportuser.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				reportuser.selectGameMoneyAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				reportuser.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
reportuser.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [reportuser.selModel, {
						hidden : true,
						header : 'ID',
						dataIndex : 'uiid'
					},{
						hidden : true,
						header : 'ApiPassWord',
						dataIndex : 'apipassword'
					}, {
						header : '账号',
						dataIndex : 'account',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '姓名',
						dataIndex : 'uname',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '钱包余额',
						dataIndex : 'money',
						width : 80,
						renderer : function(v) {
							return '<span style="color:blue;">'+v+'</span>';
						}

					}, {
						header : '总存款',
						dataIndex : 'depositTotal',
						width : 80,
						renderer : function(v) {
							return '<span style="color:blue;">'+v+'</span>';
						}

					}, {
						header : '总提款',
						dataIndex : 'withdrawalTotal',
						width : 80,
						renderer : function(v) {
							return '<span style="color:blue;">'+v+'</span>';
						}
					}, {
						header : '总优惠',
						dataIndex : 'preferentialTotal',
						width : 80,
						renderer : function(v) {
							return '<span style="color:blue;">'+v+'</span>';
						}
					}, {
						header : '总洗码',
						dataIndex : 'ximaTotal',
						width : 80,
						renderer : function(v) {
							return '<span style="color:blue;">'+v+'</span>';
						}
					}, {
						header : '游戏输赢',
						dataIndex : 'winLossTotal',
						width : 120,
						/*renderer : function(v, row, column, rowIndex, record, table) {
							var m = Share.decimalTwo(Number(column.data.withdrawalTotal) - Number(column.data.depositTotal));
							if(Number(m) == 0){
								return '<span style="color:blue;">'+m+'</span>';
							}
							if(Number(m) > 0){
								return '<span style="color:blue;">'+m+'</span>';
							}else{
								return '<span style="color:red;">'+m+'</span>';
							}
      					}*/
						renderer : function(v) {
							if(Number(v) >= 0){
								return '<span style="color:blue;">'+v+'</span>';
							}else{
								return '<span style="color:red;">'+v+'</span>';
							}
						}

					}]
		});
		
/** 新建 */
reportuser.selectGameMoneyAction = new Ext.Action({
			text : '查询游戏余额',
			iconCls : 'Zoom',
			disabled : true,
			handler : function() {
				var record = reportuser.grid.getSelectionModel().getSelected();
				reportuser.gameMoneyAddWindow.setIconClass('Zoom'); // 设置窗口的样式
				reportuser.gameMoneyAddWindow.setTitle('查询游戏余额'); // 设置窗口的名称
				reportuser.gameMoneyAddWindow.show().center(); // 显示窗口
				if(record){
					Share.resetGrid(reportuser.gameMoneyGrid);
					reportuser.gameMoneyStore.baseParams.account = record.data.account;
					reportuser.gameMoneyStore.baseParams.apipassword = record.data.apipassword;
					reportuser.gameMoneyStore.reload();
				}
			}
		});
		
reportuser.searchAction = new Ext.Action({
			text : '查询',
			iconCls : 'Zoom',
			handler : function() {
				reportuser.store.baseParams.account =  $("#reportAcconut").val();
			    reportuser.store.baseParams.startDate = $("#reportStartDate").val();
			    reportuser.store.baseParams.endDate = $("#reportEndDate").val();
				reportuser.store.reload();
			}
		});
		
/** 顶部工具栏 */
reportuser.tbar = [reportuser.selectGameMoneyAction,'-','&nbsp;','会员账号：',{id:'reportAcconut',xtype:'textfield',width:120},'-','&nbsp;',
			'开始日期：',{ id:'reportStartDate',xtype:'datetimefield',format:'Y-m-d H:i:s',value:new Date().format("Y-m-d H:i:s")},'&nbsp;','至','&nbsp;',
			{ id:'reportEndDate',xtype:'datetimefield',format:'Y-m-d H:i:s',value:new Date().format("Y-m-d H:i:s")},'-','&nbsp;',
			reportuser.searchAction
			];
			
/** 底部工具条 */
reportuser.bbar = new Ext.PagingToolbar({
			pageSize : reportuser.pageSize,
			store : reportuser.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', reportuser.pageSizeCombo]
		});
/** 基本信息-表格 */
reportuser.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : reportuser.store,
			colModel : reportuser.colModel,
			selModel : reportuser.selModel,
			tbar : reportuser.tbar,
			bbar : reportuser.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'memberGradeDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});

	
reportuser.alwaysFun = function() {
	Share.resetGrid(reportuser.grid);
	reportuser.addAction.disable();
	reportuser.verifyAction.disable();
};

reportuser.gameMoneyselModel = new Ext.grid.CheckboxSelectionModel();
reportuser.gameMoneyStore = new Ext.data.JsonStore({
	root : 'data',
	totalProperty : 'totalProperty',
	autoLoad : false,
	baseParams : {
		account : "",
		apipassword : ""
	},  
	proxy : new Ext.data.HttpProxy({
		method : 'POST',
		url : reportuser.gameMoney
	}),
	fields : [ 'gpid', 'gpname', 'money'],
	listeners : {
		'load' : function(store, records, options) {
			// user.roleSelModel.clearSelections();
		}
	}
});

reportuser.gameMoneyGrid = new Ext.grid.GridPanel({
	store : reportuser.gameMoneyStore,
	//tbar:proxyInfo.clearingtbar,
	autoScroll : 'auto',
	region : 'center',
	loadMask : true,
	border: false,
	columns : [new Ext.grid.RowNumberer(),{
			hidden : true,
			header : '平台ID',
			dataIndex : 'gpid'
		},{
			header : "游戏平台",
			dataIndex : 'gpname',
			width : 80
		},{
			header : "余额",
			dataIndex : 'money',
			width : 150,
			renderer : function(v) {
				return '<span style="color:blue;">'+v+'</span>';
			}
		}],
	listeners : {}
});

/**游戏余额查询窗口*/
reportuser.gameMoneyAddWindow = new Ext.Window({
	layout : 'fit',
	width : 300,
	height : 200,
	closeAction : 'hide',
	plain : true,
	modal : true,
	resizable : true,
	items : [reportuser.gameMoneyGrid]
});

//小计
reportuser.amountsum = function(){
	var p = new Ext.data.Record({fields:['uiid','apipassword', 'account', 'uname','money','depositTotal','withdrawalTotal','preferentialTotal','ximaTotal','winLossTotal']});
	var depositTotal= 0,withdrawalTotal = 0,preferentialTotal=0,ximaTotal=0,profitandlossTotal=0,moneyTotal=0;
	reportuser.store.each(function(record){
		var deposit = record.data.depositTotal;
		var account = record.data.account;
		if(account!="总计："){
			if(deposit != null){
				depositTotal += Number(deposit);
			}
			var withdrawal = record.data.withdrawalTotal;
			if(withdrawal != null){
				withdrawalTotal += Number(withdrawal);
			}
			var preferential = record.data.preferentialTotal;
			if(preferential != null){
				preferentialTotal += Number(preferential);
			}
			var xima = record.data.ximaTotal;
			if(xima != null){
				ximaTotal += Number(xima);
			}
			var profitand = record.data.winLossTotal;
			if(profitand != null){
				profitandlossTotal += Number(profitand);
			}
			var money = record.data.money;
			if(money != null){
				moneyTotal += Number(money);
			}
		}
		
	});
	p.set('uiid','');
	p.set('apipassword','');
	p.set('account','小计：');
	p.set('uname','');
	p.set('money',moneyTotal);
	p.set('depositTotal',depositTotal);
	p.set('withdrawalTotal',withdrawalTotal);
	p.set('preferentialTotal',Share.decimalTwo(preferentialTotal));
	p.set('ximaTotal',Share.decimalTwo(ximaTotal));
	p.set('winLossTotal',Share.decimalTwo(profitandlossTotal));
	reportuser.store.add(p);
}
  
reportuser.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [reportuser.grid]
		});

</script>