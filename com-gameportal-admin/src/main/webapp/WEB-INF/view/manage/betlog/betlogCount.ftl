<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.memberInfos"); // 自定义一个命名空间
betlogCountCount = Ext.market.betlogCountCount; // 定义命名空间的别名
betlogCount = {
	all : '/manage/betlog/queryBetCountLog.do',// 加载所有
	pageSize : 30, // 每页显示的记录数
	down:'/manage/betlog/toDownloadReport.do',//导出游戏注单统计报表
	today:'${today}',
	winFields : eval('(${fields.winFields})'),
	ximaFlag : eval('(${ximaFlag})')
};


/** 改变页的combo */
betlogCount.pageSizeCombo = new Share.pageSizeCombo({
			value : '30',
			listeners : {
				select : function(comboBox) {
					betlogCount.pageSize = parseInt(comboBox.getValue());
					betlogCount.bbar.pageSize = parseInt(comboBox.getValue());
					betlogCount.store.baseParams.limit = betlogCount.pageSize;
					betlogCount.store.baseParams.start = 0;
					betlogCount.store.load();
				}
			}
		});
betlogCount.tbar = [];
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
betlogCount.pageSize = parseInt(betlogCount.pageSizeCombo.getValue());
/** 基本信息-数据源 */
betlogCount.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				account:'',
				startDate:'',
				endDate:'',
				platformcode:'',
				start : 0,
				limit : betlogCount.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : betlogCount.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['account','uname','betTotel','platformcode','gamename','betAmountTotal','profitamountTotal','validBetAmountTotal','betdate']),
			listeners : {
				'load' : function(store, records, options) {
					betlogCount.total();
				}
			}
		});
		
/** 基本信息-选择模式 */
betlogCount.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				//betlogCount.deleteAction.enable();
				//betlogCount.editAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				betlogCount.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
betlogCount.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 100
			},
			columns : [betlogCount.selModel, {
						hidden : true,
						header : 'ID',
						dataIndex : 'account'
					}, {
						header : '游戏平台',
						dataIndex : 'platformcode',
						width : 70,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '游戏名称',
						dataIndex : 'gamename',
						width : 70,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '帐号',
						dataIndex : 'account',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '姓名',
						dataIndex : 'uname',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '注单数量',
						dataIndex : 'betTotel',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '投注金额',
						dataIndex : 'betAmountTotal',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '有效投注额',
						dataIndex : 'validBetAmountTotal',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '派彩金额',
						dataIndex : 'profitamountTotal',
						renderer : function(v) {
							return Share.amount(v);
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '日期',
						dataIndex : 'betdate',
						width : 150,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}]
		});
		
//小计
betlogCount.total = function(){
	var p = new Ext.data.Record({fields:['account','uname','betTotel','platformcode','gamename','betAmountTotal','profitamountTotal','validBetAmountTotal','betdate']});
	var zbetTotel= 0,zbetAmountTotal = 0,zprofitamountTotal=0,zvalidBetAmountTotal=0;
	betlogCount.store.each(function(record){
		var betlog = record.data.betTotel;
		if(betlog != null){
			zbetTotel += Number(betlog);
		}
		var betAmountTotal = record.data.betAmountTotal;
		if(betAmountTotal != null){
			zbetAmountTotal += Number(betAmountTotal);
		}
		var profitamountTotal = record.data.profitamountTotal;
		if(profitamountTotal != null){
			zprofitamountTotal += Number(profitamountTotal);
		}
		var validBetAmountTotal = record.data.validBetAmountTotal;
		if(validBetAmountTotal != null){
			zvalidBetAmountTotal += Number(validBetAmountTotal);
		}
	});
	p.set('account','');
	p.set('platformcode','小计：');
	p.set('gamename','');
	p.set('account','');
	p.set('uname','');
	p.set('betTotel',zbetTotel);
	p.set('betAmountTotal',Share.decimalTwo(zbetAmountTotal));
	p.set('profitamountTotal',Share.decimalTwo(zprofitamountTotal));
	p.set('validBetAmountTotal',Share.decimalTwo(zvalidBetAmountTotal));
	p.set('betdate','');
	betlogCount.store.add(p);
}

/** 查询 */
betlogCount.searchField = new Ext.ux.form.SearchField({
			store : betlogCount.store,
			paramName : 'account',
			emptyText : '请输入帐号',
			style : 'margin-left: 5px;'
		});

	/** 导出报表按钮 */
	betlogCount.downAction = new Ext.Action({
			text : '导出报表',
			iconCls : 'Diskdownload',
			disabled : false,
			handler : function() {
				betlogCount.downReport();
			}
	});
	/** 导出报表函数 */
	betlogCount.downReport = function() {
		var gametype = [];
	    var account =  $("#memberAcconut").val();
	    var startDate = $("#betlogCountStartDate").val();
	    var endDate = $("#betlogCountEndDate").val();
	    var flag = $("#xiama_flag").prev().val();
	    $("input[name='countplatboxs'][checked]").each(function(){
	      gametype.push("'"+$(this).val()+"'");                     
	    }); 
		//发送请求
		window.open(betlogCount.down + "?account="+account+"&startDate="+startDate+"&endDate="+endDate+"&platformcode="+gametype+"&flag="+flag);
	};
/** 顶部工具栏 */
// betlogCount.tbar = [betlogCount.downAction,'-','&nbsp;',betlogCount.searchField];
/** 底部工具条 */
betlogCount.bbar = new Ext.PagingToolbar({
			pageSize : betlogCount.pageSize,
			store : betlogCount.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', betlogCount.pageSizeCombo]
		});
//搜索函数
betlogCount.searchFun = function(){
	var gametype = [];
    betlogCount.store.baseParams.account =  $("#memberAcconut").val();
    betlogCount.store.baseParams.startDate = $("#betlogCountStartDate").val();
    betlogCount.store.baseParams.endDate = $("#betlogCountEndDate").val();
    $("input[name='countplatboxs'][checked]").each(function(){
      gametype.push("'"+$(this).val()+"'");                     
    }); 
    betlogCount.store.baseParams.flag= $("#xiama_flag").prev().val();
    betlogCount.store.baseParams.platformcode = gametype;
	betlogCount.store.reload();
}
betlogCount.searchBtn = new Ext.Button({
		text : '查询',
		handler : function() {
			betlogCount.searchFun();
		}
	})

/** 基本信息-表格 */
betlogCount.grid = new Ext.grid.EditorGridPanel({
			// title : '押注记录列表',
			store : betlogCount.store,
			colModel : betlogCount.colModel,
			selModel : betlogCount.selModel,
			tbar : betlogCount.tbar,
			bbar : betlogCount.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'betlogCountDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {},
			listeners : {
               render: function() {
               this.tbar2 = new Ext.Toolbar({  
               renderTo: betlogCount.grid.tbar,
               items:[
					betlogCount.downAction,'-','&nbsp;',
					'会员账号:',{id:'memberAcconut',xtype:'textfield',width:100},'-',
					' 开始日期:',{ id:'betlogCountStartDate',xtype:'datetimefield',format:'Y-m-d H:i:s',value:new Date().format("Y-m-d"),width:145},'-',
            		' 至',{ id:'betlogCountEndDate',xtype:'datetimefield',format:'Y-m-d H:i:s',value:betlogCount.today,width:145},'-',
          		  	'洗码状态:',
                       		new Ext.form.ComboBox({
								hiddenName :'xiama_flag',
								id : 'xiama_flag',
								triggerAction : 'all',
								mode : 'local',
								store : new Ext.data.ArrayStore({
											fields : ['v', 't'],
											data : Share.map2Ary(betlogCount.ximaFlag)
										}),
								valueField : 'v',
								displayField : 't',
								allowBlank : true,
								value:'',
								editable : true,
								width:80
							})
                     ]
            	}),
               this.tbar3 = new Ext.Toolbar({  
               renderTo: betlogCount.grid.tbar,
               items:[
                       ' 平台类型:',{xtype:'checkbox',boxLabel : 'AG极速厅',name : "countplatboxs",inputValue : 'AG',checked : true},'-',
          			  	{xtype:'checkbox',boxLabel : 'AGIN国际厅',name : "countplatboxs",inputValue : 'AGIN',checked : true},'-',
           			 	{xtype:'checkbox',boxLabel : 'PT电子游戏',name : "countplatboxs",inputValue : 'PT',checked : true},'-',
           				{xtype:'checkbox',boxLabel : 'BBIN游戏厅',name : "countplatboxs",inputValue : 'BBIN',checked : true},'-',
           				{xtype:'checkbox',boxLabel : 'MG游戏厅',name : "countplatboxs",inputValue : 'MG',checked : true},'-',
           				{xtype:'checkbox',boxLabel : 'SA游戏厅',name : "countplatboxs",inputValue : 'SA',checked : true},'-', '&nbsp;',
            			betlogCount.searchBtn
                	]
            	})
          	 }
			}
		});


betlogCount.alwaysFun = function() {
	Share.resetGrid(betlogCount.grid);
    
};

betlogCount.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [betlogCount.grid]
		});

</script>