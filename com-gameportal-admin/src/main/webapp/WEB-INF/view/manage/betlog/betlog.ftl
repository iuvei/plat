<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.memberInfos"); // 自定义一个命名空间
betlog = Ext.market.betlogs; // 定义命名空间的别名
betlog = {
	all : '/manage/betlog/queryBetLog.do',// 加载所有
	pageSize : 35, // 每页显示的记录数
	today:'${today}',
	winFields : eval('(${fields.winFields})')
};


/** 改变页的combo */
betlog.pageSizeCombo = new Share.pageSizeCombo({
			value : '35',
			listeners : {
				select : function(comboBox) {
					betlog.pageSize = parseInt(comboBox.getValue());
					betlog.bbar.pageSize = parseInt(comboBox.getValue());
					betlog.store.baseParams.limit = betlog.pageSize;
					betlog.store.baseParams.start = 0;
					betlog.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
betlog.pageSize = parseInt(betlog.pageSizeCombo.getValue());
/** 基本信息-数据源 */
betlog.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : betlog.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : betlog.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['pdid', 'uiid', 'platformcode', 'gamecode', 'betdate', 'betno', 
			    	'betamount','profitamount', 'finalamount','origin','result',
			    	'gamename','account','validBetAmount','playType','tableCode',
			    	'inningsCode','beforeCerdit','loginIP','flag']),
			listeners : {
				'load' : function(store, records, options) {
				//	betlog.alwaysFun();
				}
			}
		});
		
/** 基本信息-选择模式 */
betlog.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				//betlog.deleteAction.enable();
				//betlog.editAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				betlog.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
betlog.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 100
			},
			columns : [betlog.selModel, {
						hidden : true,
						header : 'ID',
						dataIndex : 'pdid',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '游戏平台',
						dataIndex : 'platformcode',
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
						header : '游戏',
						dataIndex : 'gamename',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '押注单号',
						dataIndex : 'betno',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '押注金额',
						dataIndex : 'betamount',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '有效投注额',
						dataIndex : 'validBetAmount',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '派彩金额',
						dataIndex : 'profitamount',
						renderer : function(v) {
							return Share.amount(v);
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '输赢金额',
						dataIndex : 'finalamount',
						renderer : function(v) {
							return Share.amount(v);
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '押注时间',
						dataIndex : 'betdate',
						width : 130,
						renderer : function(v) {
							return new Date(v).format('Y-m-d H:i:s');
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '下注前余额',
						dataIndex : 'beforeCerdit',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '游戏玩法',
						dataIndex : 'playType',
						renderer : function(v,cellMeta, record) {
							return Share.playType(record.data['platformcode'],record.data['gamecode'],v);
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '桌子编号',
						dataIndex : 'tableCode',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '局号',
						dataIndex : 'inningsCode',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '登录IP',
						dataIndex : 'loginIP',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '洗码状态',
						dataIndex : 'flag',
						renderer : function(v) {
							return Share.flag(v);
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '结果',
						dataIndex : 'result',
						renderer : function(v) {
							return Share.winorloss(Share.map(v, betlog.winFields, ''));
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}]
		});
		



/** 查询 */
betlog.searchField = new Ext.ux.form.SearchField({
			store : betlog.store,
			paramName : 'account',
			emptyText : '请输入帐号',
			style : 'margin-left: 5px;'
		});
/** 顶部工具栏 */
betlog.tbar = [];
/** 底部工具条 */
betlog.bbar = new Ext.PagingToolbar({
			pageSize : betlog.pageSize,
			store : betlog.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', betlog.pageSizeCombo]
		});
		
/*搜索参数*/
betlog.searchParams = function(){
	var obj={};
	var gametype = [];
	obj.account = $("#memberBetLogAcconut").val();
	obj.startDate = $("#betLogStartDate").val();
	obj.endDate = $("#betLogEndDate").val();
	obj.betno = $("#memberBetno").val();
    $("input[name='platboxs'][checked]").each(function(){
      gametype.push("'"+$(this).val()+"'");                     
    }); 
    obj.gamename = gametype;
    obj.limit = betlog.pageSize;
	obj.start = 0;
    return obj;
}

//搜索函数
betlog.searchFun = function(){
	betlog.store.load({params: betlog.searchParams()});
	betlog.alwaysFun();
}

betlog.store.on('beforeload',function(){
  	betlog.store.baseParams = betlog.searchParams();
});


betlog.searchBtn = new Ext.Button({
		text : '查询',
		handler : function() {
			betlog.searchFun();
		}
	})

/** 基本信息-表格 */
betlog.grid = new Ext.grid.EditorGridPanel({
			// title : '押注记录列表',
			store : betlog.store,
			colModel : betlog.colModel,
			selModel : betlog.selModel,
			tbar:betlog.tbar,
			bbar : betlog.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'betlogDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {},
			listeners : {
               render: function() {
               this.tbar2 = new Ext.Toolbar({  
               renderTo: betlog.grid.tbar,
               items:[
               			'会员账号:',{id:'memberBetLogAcconut',xtype:'textfield',width:100},'-',
						'投注单号:',{id:'memberBetno',xtype:'textfield',width:100},'-',
						' 开始日期:',{id:'betLogStartDate',xtype:'datetimefield',format:'Y-m-d H:i:s',value:new Date().format("Y-m-d"),width:145},'-',
           				 ' 到',{id:'betLogEndDate',xtype:'datetimefield',format:'Y-m-d H:i:s',value:betlog.today,width:145}
                     ]
            	}),
               this.tbar3 = new Ext.Toolbar({  
               renderTo: betlog.grid.tbar,
               items:[
                       ' 平台类型:',{xtype:'checkbox',boxLabel : 'AG极速厅',name : "platboxs",inputValue : 'AG',checked : true},'-',
			            {xtype:'checkbox',boxLabel : 'AGIN国际厅',name : "platboxs",inputValue : 'AGIN',checked : true},'-',
			            {xtype:'checkbox',boxLabel : 'PT电子游戏',name : "platboxs",inputValue : 'PT',checked : true},'-',
			            {xtype:'checkbox',boxLabel : 'BBIN游戏厅',name : "platboxs",inputValue : 'BBIN',checked : true},'-',
			            {xtype:'checkbox',boxLabel : 'MG游戏厅',name : "platboxs",inputValue : 'MG',checked : true},'-', 
			            {xtype:'checkbox',boxLabel : 'SA游戏厅',name : "platboxs",inputValue : 'SA',checked : true},'-','&nbsp;',
			            betlog.searchBtn
                	]
            	})
          	 }
			}
		});


betlog.alwaysFun = function() {
	Share.resetGrid(betlog.grid);
    
};

betlog.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [betlog.grid]
		});

</script>