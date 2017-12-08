<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.memberUpgradeLogs"); // 自定义一个命名空间
memberRpt = Ext.market.memberRpts; // 定义命名空间的别名
memberRpt = {
	all : '/manage/memberinfo/queryMemberInfoRpt.do',// 加载所有
	gameMoney:"/manage/memberinfo/queryGameMoney.do",//加载游戏余额
	pageSize : 30, // 每页显示的记录数
	memGrade:eval('(${fields.memGrade})'),
	WINLOSSMAP:eval('(${winlossMap})')
};


/** 改变页的combo */
memberRpt.pageSizeCombo = new Share.pageSizeCombo({
			value : '30',
			listeners : {
				select : function(comboBox) {
					memberRpt.pageSize = parseInt(comboBox.getValue());
					memberRpt.bbar.pageSize = parseInt(comboBox.getValue());
					memberRpt.store.baseParams.limit = memberRpt.pageSize;
					memberRpt.store.baseParams.start = 0;
					memberRpt.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
memberRpt.pageSize = parseInt(memberRpt.pageSizeCombo.getValue());
/** 基本信息-数据源 */
memberRpt.store = new Ext.data.Store({
			autoLoad : false,
			remoteSort : true,
			baseParams : {
				account : '',
				starttime : '',
				endtime : '',
				quick : '',//默认查询本月
				start : 0,
				winloss: '1',
				limit : memberRpt.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : memberRpt.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, [ 'uiid','apipassword', 'account', 'uname','money','depositTotal','withdrawalTotal','preferentialTotal','ximaTotal','winLossTotal'
			    	]),
			listeners : {
				'load' : function(store, records, options) {
					memberRpt.amountsum();
				}
			}
		});
		
/** 基本信息-选择模式 */
memberRpt.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				memberRpt.selectGameAmonut.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				memberRpt.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
memberRpt.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [memberRpt.selModel, {
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
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '总存款',
						dataIndex : 'depositTotal',
						width : 80,
						renderer : function(v) {
							return '<span style="color:blue;">'+v+'</span>';
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '总提款',
						dataIndex : 'withdrawalTotal',
						width : 80,
						renderer : function(v) {
							return '<span style="color:blue;">'+v+'</span>';
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '总优惠',
						dataIndex : 'preferentialTotal',
						width : 80,
						renderer : function(v) {
							return '<span style="color:blue;">'+v+'</span>';
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '总洗码',
						dataIndex : 'ximaTotal',
						width : 80,
						renderer : function(v) {
							return '<span style="color:blue;">'+v+'</span>';
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '游戏输赢',
						dataIndex : 'winLossTotal',
						width : 120,
						renderer : function(v) {
							if(Number(v) == 0){
								return '<span style="color:blue;">'+v+'</span>';
							}
							if(Number(v) > 0){
								return '<span style="color:blue;">'+v+'</span>';
							}else{
								return '<span style="color:red;">'+v+'</span>';
							}
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '实际盈亏',
						dataIndex : 'profitandloss',
						width : 120,
						renderer : function(v, row, column, rowIndex, record, table) {
							// var m = Share.decimalTwo(Number(column.data.withdrawalTotal) - Number(column.data.depositTotal) - Number(column.data.preferentialTotal) - Number(column.data.ximaTotal) + Number(column.data.money));
							var m = Share.decimalTwo((Number(column.data.withdrawalTotal) + Number(column.data.money)) - Number(column.data.depositTotal));
							if(Number(m) == 0){
								return '<span style="color:blue;">'+m+'</span>';
							}
							if(Number(m) > 0){
								return '<span style="color:blue;">'+m+'</span>';
							}else{
								return '<span style="color:red;">'+m+'</span>';
							}
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}]
		});
		
		
		

memberRpt.selectGameAmonut = new Ext.Action({
			text : '查询游戏余额',
			iconCls : 'Zoom',
			disabled : true,
			handler : function() {
				var record = memberRpt.grid.getSelectionModel().getSelected();
				memberRpt.gameMoneyAddWindow.setIconClass('Zoom'); // 设置窗口的样式
				memberRpt.gameMoneyAddWindow.setTitle('查询游戏余额'); // 设置窗口的名称
				memberRpt.gameMoneyAddWindow.show().center(); // 显示窗口
				if(record){
					Share.resetGrid(memberRpt.gameMoneyGrid);
					memberRpt.gameMoneyStore.baseParams.account = record.data.account;
					memberRpt.gameMoneyStore.baseParams.apipassword = record.data.apipassword;
					memberRpt.gameMoneyStore.reload();
				}
			}
		});

/** 查询 */
memberRpt.searchField = new Ext.form.TextField({
			id : 'rptaccount',
			width:80,
			style : 'margin-left: 5px;'
		});

memberRpt.searchButton = new Ext.Action({
	text : '会员查询',
	iconCls : 'Zoom',
	handler : function() {
		memberRpt.alwaysFun();
		memberRpt.store.baseParams.account = $("#rptaccount").val();
		memberRpt.store.baseParams.starttime = $("#searchStartDate").val();
		memberRpt.store.baseParams.endtime = $("#endStartDate").val();
		memberRpt.store.baseParams.stype = 1;
		memberRpt.store.baseParams.quick = '';
		memberRpt.store.load();
	}
});

memberRpt.lowerSearchButton = new Ext.Action({
	text : '下线查询',
	iconCls : 'Zoom',
	handler : function() {
		if($("#rptaccount").val() == ''){
			Ext.MessageBox.alert('提示', '请输入会员账号!');
			return;
		}
		memberRpt.alwaysFun();
		memberRpt.store.baseParams.account = $("#rptaccount").val();
		memberRpt.store.baseParams.starttime = $("#searchStartDate").val();
		memberRpt.store.baseParams.endtime = $("#endStartDate").val();
		memberRpt.store.baseParams.stype = 2;
		memberRpt.store.baseParams.quick = '';
		memberRpt.store.load();
	}
});

/**
 * 查询今天数据
 */
memberRpt.searchTodayButton = new Ext.Action({
	text : '今天',
	iconCls : 'Date',
	handler : function() {
		memberRpt.alwaysFun();
		memberRpt.store.baseParams.account = $("#rptaccount").val();
		memberRpt.store.baseParams.starttime = '';
		memberRpt.store.baseParams.endtime = '';
		memberRpt.store.baseParams.quick = 'today';
		memberRpt.store.load();
	}
});

/**
 * 昨天
 */
memberRpt.searchYesterdayButton = new Ext.Action({
	text : '昨天',
	iconCls : 'Date',
	handler : function() {
		memberRpt.alwaysFun();
		memberRpt.store.baseParams.account = $("#rptaccount").val();
		memberRpt.store.baseParams.starttime = '';
		memberRpt.store.baseParams.endtime = '';
		memberRpt.store.baseParams.quick = 'yesterday';
		memberRpt.store.load();
	}
});

/**
 * 昨天
 */
memberRpt.searchSZButton = new Ext.Action({
	text : '上周',
	iconCls : 'Date',
	handler : function() {
		memberRpt.alwaysFun();
		memberRpt.store.baseParams.account = $("#rptaccount").val();
		memberRpt.store.baseParams.starttime = '';
		memberRpt.store.baseParams.endtime = '';
		memberRpt.store.baseParams.quick = 'toSZ';
		memberRpt.store.load();
	}
});

memberRpt.searchBZButton = new Ext.Button({
	text : '本周',
	iconCls : 'Date',
	handler : function() {
		memberRpt.alwaysFun();
		memberRpt.store.baseParams.account = $("#rptaccount").val();
		memberRpt.store.baseParams.starttime = '';
		memberRpt.store.baseParams.endtime = '';
		memberRpt.store.baseParams.quick = 'toBZ';
		memberRpt.store.load();
	}
});

memberRpt.searchBYButton = new Ext.Action({
	text : '本月',
	iconCls : 'Date',
	handler : function() {
		memberRpt.alwaysFun();
		memberRpt.store.baseParams.account = $("#rptaccount").val();
		memberRpt.store.baseParams.starttime = '';
		memberRpt.store.baseParams.endtime = '';
		memberRpt.store.baseParams.quick = 'toM';
		memberRpt.store.load();
	}
});

memberRpt.winlossCombo = new Ext.form.ComboBox({
		hiddenName : 'winloss',
		id : 'winloss',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(memberRpt.WINLOSSMAP)
				}),
		valueField : 'v',
		displayField : 't',
		value: '1',
		editable : false,
		width:50,
		listeners : {
			select : function(comboBox) {
				memberRpt.store.baseParams.winloss = comboBox.getValue();
			}
		}
});
	
/** 顶部工具栏 */
memberRpt.tbar = [memberRpt.selectGameAmonut,'-','&nbsp;','会员账号：',memberRpt.searchField,'&nbsp;',
'查询日期：',{id:'searchStartDate',xtype:'datetimefield',format:'Y-m-d H:i:s',width:130},'&nbsp;',
'至',{id:'endStartDate',xtype:'datetimefield',format:'Y-m-d H:i:s',width:130},'&nbsp;','排序类型:',memberRpt.winlossCombo,'&nbsp;',
memberRpt.searchButton,'-',memberRpt.lowerSearchButton,'-',
memberRpt.searchTodayButton,'-',memberRpt.searchYesterdayButton,'-',
memberRpt.searchSZButton,'-',memberRpt.searchBZButton,'-',memberRpt.searchBYButton];

/** 底部工具条 */
memberRpt.bbar = new Ext.PagingToolbar({
			pageSize : memberRpt.pageSize,
			store : memberRpt.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', memberRpt.pageSizeCombo]
		});
/** 基本信息-表格 */
memberRpt.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : memberRpt.store,
			colModel : memberRpt.colModel,
			selModel : memberRpt.selModel,
			tbar : memberRpt.tbar,
			bbar : memberRpt.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'memberRptDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
});
		
memberRpt.alwaysFun = function() {
	Share.resetGrid(memberRpt.grid);
	memberRpt.selectGameAmonut.disable();
	
};

memberRpt.gameMoneyselModel = new Ext.grid.CheckboxSelectionModel();
memberRpt.gameMoneyStore = new Ext.data.JsonStore({
	root : 'data',
	totalProperty : 'totalProperty',
	autoLoad : false,
	baseParams : {
		account : "",
		apipassword : ""
	},  
	proxy : new Ext.data.HttpProxy({
		method : 'POST',
		url : memberRpt.gameMoney
	}),
	fields : [ 'gpid', 'gpname', 'money'],
	listeners : {
		'load' : function(store, records, options) {
			// user.roleSelModel.clearSelections();
		}
	}
});

memberRpt.gameMoneyGrid = new Ext.grid.GridPanel({
	store : memberRpt.gameMoneyStore,
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
memberRpt.gameMoneyAddWindow = new Ext.Window({
	layout : 'fit',
	width : 300,
	height : 200,
	closeAction : 'hide',
	plain : true,
	modal : true,
	resizable : true,
	items : [memberRpt.gameMoneyGrid]
});

//小计
memberRpt.amountsum = function(){
	var p = new Ext.data.Record({fields:['uiid','apipassword', 'account', 'uname','money','depositTotal','withdrawalTotal','preferentialTotal','ximaTotal','winLossTotal','profitandloss']});
	var depositTotal= 0,withdrawalTotal = 0,preferentialTotal=0,ximaTotal=0,winLossTotal=0,moneyTotal=0;
	memberRpt.store.each(function(record){
		var deposit = record.data.depositTotal;
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
		var winLoss = record.data.winLossTotal;
		if(winLoss != null){
			winLossTotal += Number(winLoss);
		}
		var money = record.data.money;
		if(money != null){
			moneyTotal += Number(money);
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
	p.set('winLossTotal',Share.decimalTwo(winLossTotal));
	p.set('profitandloss','');
	memberRpt.store.add(p);
}

memberRpt.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [memberRpt.grid]
		});

</script>