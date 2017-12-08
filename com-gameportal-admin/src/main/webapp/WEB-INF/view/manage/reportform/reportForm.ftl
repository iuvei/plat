<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	Ext.ns("Ext.market.platReport"); // 自定义一个命名空间
	platReport = Ext.market.platReport; // 定义命名空间的别名
	platReport = {
		all : '/manage/reportForm/queryReportResult.do', // 加载数据
		down:'/manage/reportForm/toDownloadReport.do',//导出报表
		registerDetailUrl:'/manage/reportForm/queryRegisterResult.do',//注册人数详情
		firstPayDetailUrl:'/manage/reportForm/queryFirstPayResult.do',//首冲人数详情
		realBetMoneyDetailUrl:'/manage/reportForm/queryBetMoneyResult.do',//实际投注额详情
		platformBunkoDetailUrl:'',//平台输赢详情
		memberCouponDetailUrl:'/manage/reportForm/queryMemberCouponResult.do',//会员优惠详情
		proxyCouponDetailUrl:'/manage/reportForm/queryProxyCouponResult.do',//代理优惠详情
		payMoneyDetailUrl:'/manage/reportForm/queryPayMoneyResult.do',//充值金额详情
		pickUpMoneyrDetailUrl:'/manage/reportForm/queryPickMoneyResult.do',//提款金额详情  
		platformMoneyDetailUrl:'',//平台余额详情
		pageSize : 30,// 每页显示的记录数
		ordertype : eval('(${fields.ordertype})')//订单类型
	};
	/** 改变页的combo*/
	platReport.pageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				platReport.pageSize  = parseInt(comboBox.getValue());
				platReport.bbar.pageSize  = parseInt(comboBox.getValue());
				platReport.store.baseParams.limit = platReport.pageSize;
				platReport.store.baseParams.start = 0;
				platReport.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	platReport.pageSize = parseInt(platReport.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	platReport.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:true,
		baseParams : {
			startDate:'',
			endDate:'',
			start : 0,
			limit : platReport.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : platReport.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'reporttime','registerNumber','firstPayNumber','firstPayTotalMoney','realBetMoney','platformBunko','memberCoupon','proxyCoupon','payMoney','pickUpMoney','platformMoney','payMoneyPerson','payMoneyCount','pickUpMoneyPerson','pickUpMoneyCount',<#--'memberXimaMoney'-->,'proxyXimaMoney','proxyClearMoney','payOrderXimaMoney']),
		listeners : {
			'load' : function(store, records, options) {
				platReport.amountsum();
				platReport.alwaysFun();
			}
		}
	});
	//online.store.load(); 
	/** 基本信息-选择模式 */
	platReport.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				platReport.downAction.enable();
				platReport.registerAction.enable();
				platReport.firstPayNumberAction.enable();
				platReport.betMoneyAction.enable();
				platReport.memberCouponAction.enable();
				platReport.proxyCouponAction.enable();
				platReport.payMoneyAction.enable();
				platReport.pickMoneyAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				platReport.alwaysFun();
			}
		}
	});
	/** 基本信息-数据列 */
	platReport.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 110
		},
		columns : [ platReport.selModel, {
						header : '日期',
						dataIndex : 'reporttime',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '注册人数',
						dataIndex : 'registerNumber',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '首充人数',
						dataIndex : 'firstPayNumber',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '首充总金额',
						dataIndex : 'firstPayTotalMoney',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '投注总额',
						dataIndex : 'realBetMoney',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '会员优惠',
						dataIndex : 'memberCoupon',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},
					{
						header : '会员洗码金额',
						dataIndex : 'payOrderXimaMoney',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},
					{
						header : '代理优惠',
						dataIndex : 'proxyCoupon',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},
					<#--
					{
						header : '会员洗码金额',
						dataIndex : 'memberXimaMoney',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},-->
					{
						header : '代理洗码金额',
						dataIndex : 'proxyXimaMoney',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},
					{
						header : '代理结算金额',
						dataIndex : 'proxyClearMoney',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},
					{
						header : '充值金额',
						dataIndex : 'payMoney',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},
					{
						header : '充值人数',
						dataIndex : 'payMoneyPerson',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},
					{
						header : '充值笔数',
						dataIndex : 'payMoneyCount',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},
					{
						header : '提款金额',
						dataIndex : 'pickUpMoney',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},
					{
						header : '提款人数',
						dataIndex : 'pickUpMoneyPerson',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},
					{
						header : '提款笔数',
						dataIndex : 'pickUpMoneyCount',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}
				]
	});
	//小计
	platReport.amountsum = function(){
		var p = new Ext.data.Record({fields:['reporttime','registerNumber', 'firstPayNumber', 'firstPayTotalMoney','realBetMoney','platformBunko','memberCoupon','proxyCoupon','payMoney','pickUpMoney','platformMoney','payMoneyPerson','payMoneyCount','pickUpMoneyPerson','pickUpMoneyCount',<#--'memberXimaMoney',-->'proxyXimaMoney','proxyClearMoney','payOrderXimaMoney']});
		var registerNumberTotal= 0,firstPayNumberTotal = 0,firstPayTotalMoneyTotal=0,realBetMoneyTotal=0,platformBunkoTotal=0,memberCouponTotal=0,proxyCouponTotal=0,payMoneyTotal=0,pickUpMoneyTotal=0,platformMoneyTotal=0,payMoneyPersonTotal=0,payMoneyCountTotal=0,pickUpMoneyPersonTotal=0,pickUpMoneyCountTotal=0,<#--memberXimaMoneyTotal=0,-->proxyXimaMoneyTotal=0,proxyClearMoneyTotal=0,payOrderXimaMoneyTotal=0;
		platReport.store.each(function(record){
			var registerNumbers = record.data.registerNumber;
			if(registerNumbers != null){//注册人数
				registerNumberTotal += Number(registerNumbers);
			}
			var firstPayNumbers = record.data.firstPayNumber;
			if(firstPayNumbers != null){//首冲人数
				firstPayNumberTotal += Number(firstPayNumbers);
			}
			var firstPayTotalMoneys = record.data.firstPayTotalMoney;
			if(firstPayTotalMoneys != null){//首冲金额
				firstPayTotalMoneyTotal += Number(firstPayTotalMoneys);
			}
			var realBetMoneys = record.data.realBetMoney;
			if(realBetMoneys != null){ //实际投注额
				realBetMoneyTotal += Number(realBetMoneys);
			}
			var platformBunkos = record.data.platformBunko;
			if(platformBunkos != null){//平台输赢
				platformBunkoTotal += Number(platformBunkos);
			}
			var memberCoupons = record.data.memberCoupon;
			if(memberCoupons != null){//会员优惠
				memberCouponTotal += Number(memberCoupons);
			}
			var proxyCoupons = record.data.proxyCoupon;
			if(proxyCoupons != null){//代理优惠
				proxyCouponTotal += Number(proxyCoupons);
			}
			<#--
			var memberXimaMoneys = record.data.memberXimaMoney;
			if(memberXimaMoneys != null){//会员洗码金额
				memberXimaMoneyTotal += Number(memberXimaMoneys);
			}-->
			var proxyXimaMoneys = record.data.proxyXimaMoney;
			if(proxyXimaMoneys != null){//代理洗码金额
				proxyXimaMoneyTotal += Number(proxyXimaMoneys);
			}
			var proxyClearMoneys = record.data.proxyClearMoney;
			if(proxyClearMoneys != null){//代理佣金结算金额
				proxyClearMoneyTotal += Number(proxyClearMoneys);
			}
			var payOrderXimaMoneys = record.data.payOrderXimaMoney;
			if(payOrderXimaMoneys != null){//手动洗码金额
				payOrderXimaMoneyTotal += Number(payOrderXimaMoneys);
			}
			var payMoneys = record.data.payMoney;
			if(payMoneys != null){//充值金额
				payMoneyTotal += Number(payMoneys);
			}
			var payMoneyPersons = record.data.payMoneyPerson;
			if(payMoneyPersons != null){//充值人数
				payMoneyPersonTotal += Number(payMoneyPersons);
			}
			var payMoneyCounts = record.data.payMoneyCount;
			if(payMoneyCounts != null){//充值笔数
				payMoneyCountTotal += Number(payMoneyCounts);
			}
			var pickUpMoneys = record.data.pickUpMoney;
			if(pickUpMoneys != null){//提款金额
				pickUpMoneyTotal += Number(pickUpMoneys);
			}
			var pickUpMoneyPersons = record.data.pickUpMoneyPerson;
			if(pickUpMoneyPersons != null){//提款人数
				pickUpMoneyPersonTotal += Number(pickUpMoneyPersons);
			}
			var pickUpMoneyCounts = record.data.pickUpMoneyCount;
			if(pickUpMoneyCounts != null){//提款笔数
				pickUpMoneyCountTotal += Number(pickUpMoneyCounts);
			}
			var platformMoneys = record.data.platformMoney;
			if(platformMoneys != null){//平台余额
				platformMoneyTotal += Number(platformMoneys);
			}
			
			
		});
		
		p.set('reporttime','小计：');
		p.set('registerNumber',registerNumberTotal);
		p.set('firstPayNumber',firstPayNumberTotal);
		p.set('firstPayTotalMoney',firstPayTotalMoneyTotal.toFixed(2));
		p.set('realBetMoney',realBetMoneyTotal.toFixed(2));
		p.set('memberCoupon',memberCouponTotal.toFixed(2));
		p.set('proxyCoupon',proxyCouponTotal.toFixed(2));
		p.set('payOrderXimaMoney',payOrderXimaMoneyTotal.toFixed(2));
		<#--p.set('memberXimaMoney',memberXimaMoneyTotal);-->
		p.set('proxyXimaMoney',proxyXimaMoneyTotal.toFixed(2));
		p.set('proxyClearMoney',proxyClearMoneyTotal.toFixed(2));
		p.set('payMoney',payMoneyTotal.toFixed(2));
		p.set('payMoneyPerson',payMoneyPersonTotal);
		p.set('payMoneyCount',payMoneyCountTotal);
		p.set('pickUpMoney',pickUpMoneyTotal);
		p.set('pickUpMoneyPerson',pickUpMoneyPersonTotal);
		p.set('pickUpMoneyCount',pickUpMoneyCountTotal);
		platReport.store.add(p);
	}
	
 	// ----------------------单独模块统计数据 begin--------------------
	/** 导出报表按钮 */
	platReport.downAction = new Ext.Action({
			text : '导出报表',
			iconCls : 'Diskdownload',
			disabled : false,
			handler : function() {
				platReport.downReport();
			}
	});
	/** 导出报表函数 */
	platReport.downReport = function() {
		 //发送请求
		 window.open(platReport.down + "?reportStartDate="+$("#reportStartDate").val()+"&reportEndDate="+$("#reportEndDate").val());
	};
	

	// ----------------------注册人数统计 begin--------------------
	/** 注册人数详情按钮 */
	platReport.registerAction = new Ext.Action({
		text : '注册人数',
		disabled : true,
		iconCls : 'Coinsadd',
		handler : function() {
			var record = platReport.grid.getSelectionModel().getSelected();
			platReport.addRegisterWindow.setIconClass('Coinsadd'); // 设置窗口的样式
			platReport.addRegisterWindow.setTitle('注册人数'); // 设置窗口的名称
			platReport.addRegisterWindow.show().center(); // 显示窗口
			platReport.registerNumberDetail.baseParams.reportdate = record.data.reporttime;
			platReport.registerNumberDetail.reload();
		}
	});
	
	/** 注册人数详情数据源 */
	platReport.registerNumberDetail = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : false,
		baseParams : {
			reportdate:'',
			start : 0,
			limit : platReport.pageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : platReport.registerDetailUrl
		}),
		fields : [ 'create_date','uname','account'],
		listeners : {
			'load' : function(store, records, options) {
			}
		}
	});
	/** 改变页的combo*/
	platReport.registerPageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				platReport.pageSize  = parseInt(comboBox.getValue());
				platReport.registerbbar.pageSize  = parseInt(comboBox.getValue());
				platReport.registerNumberDetail.baseParams.limit = platReport.pageSize;
				platReport.registerNumberDetail.baseParams.start = 0;
				platReport.registerNumberDetail.load();
			}
		}
	});
	/** 分页 */
	platReport.registerbbar = new Ext.PagingToolbar({
		pageSize : platReport.pageSize,
		store : platReport.registerNumberDetail,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', platReport.registerPageSizeCombo ]
	});
	/** 注册人数列表 */
	platReport.registergrid = new Ext.grid.EditorGridPanel({
		store : platReport.registerNumberDetail,
		autoScroll : 'auto',
		region : 'center',
		bbar:platReport.registerbbar,
		loadMask : true,
		stripeRows : true,
		border: false,
		columns : [
			{
				header : "会员账号",
				dataIndex : 'account',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "会员姓名",
				dataIndex : 'uname',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "注册时间",
				dataIndex : 'create_date',
				renderer : function(v) {
					return v==null?'':new Date(v).format('Y-m-d H:i:s');
				},
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			}
		]
	});
	
	/** 新建注册人数窗口 */
	platReport.addRegisterWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 450,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [platReport.registergrid]
	});
	
	// ----------------------注册人数统计 end--------------------
	
	// ----------------------首冲人数统计 begin--------------------
	/** 首冲人数详情按钮 */
	platReport.firstPayNumberAction = new Ext.Action({
		text : '首充人数',
		disabled : true,
		iconCls : 'Coinsadd',
		handler : function() {
			var record = platReport.grid.getSelectionModel().getSelected();
			platReport.addFirstPayNumberWindow.setIconClass('Coinsadd'); // 设置窗口的样式
			platReport.addFirstPayNumberWindow.setTitle('首冲人数'); // 设置窗口的名称
			platReport.addFirstPayNumberWindow.show().center(); // 显示窗口
			platReport.firstPayNumberDetail.baseParams.reportdate = record.data.reporttime;
			platReport.firstPayNumberDetail.reload();
		}
	});
	
	/** 首冲人数详情数据源 */
	platReport.firstPayNumberDetail = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : false,
		baseParams : {
			reportdate:'',
			start : 0,
			limit : platReport.pageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : platReport.firstPayDetailUrl
		}),
		fields : [ 'poid','acounts','username','amounts','depotime','kfremarks','cwremarks'],
		listeners : {
			'load' : function(store, records, options) {
			}
		}
	});
	/** 改变页的combo*/
	platReport.firstPayPageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				platReport.pageSize  = parseInt(comboBox.getValue());
				platReport.firstPaybbar.pageSize  = parseInt(comboBox.getValue());
				platReport.firstPayNumberDetail.baseParams.limit = platReport.pageSize;
				platReport.firstPayNumberDetail.baseParams.start = 0;
				platReport.firstPayNumberDetail.load();
			}
		}
	});
	/** 分页 */
	platReport.firstPaybbar = new Ext.PagingToolbar({
		pageSize : platReport.pageSize,
		store : platReport.firstPayNumberDetail,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', platReport.firstPayPageSizeCombo ]
	});
	/** 首冲人数列表 */
	platReport.firstpaygrid = new Ext.grid.EditorGridPanel({
		store : platReport.firstPayNumberDetail,
		autoScroll : 'auto',
		region : 'center',
		bbar:platReport.firstPaybbar,
		loadMask : true,
		stripeRows : true,
		border: false,
		columns : [
			{
				header : "订单ID",
				dataIndex : 'poid',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "会员账号",
				dataIndex : 'acounts',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "会员姓名",
				dataIndex : 'username',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "充值金额",
				dataIndex : 'amounts',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "充值时间",
				dataIndex : 'depotime',
				renderer : function(v) {
					return v==null?'':new Date(v).format('Y-m-d H:i:s');
				},
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "客服备注",
				dataIndex : 'kfremarks',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "财务备注",
				dataIndex : 'cwremarks',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			}
		]
	});
	
	/** 新建首冲人数窗口 */
	platReport.addFirstPayNumberWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 450,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [platReport.firstpaygrid]
	});
	
	// ----------------------首冲人数统计 end--------------------
	
	// ----------------------实际投注额统计 begin--------------------
	/** 实际投注额按钮 */
	platReport.betMoneyAction = new Ext.Action({
		text : '实际投注额',
		disabled : true,
		iconCls : 'Coinsadd',
		handler : function() {
			var record = platReport.grid.getSelectionModel().getSelected();
			platReport.addBetMoneyWindow.setIconClass('Coinsadd'); // 设置窗口的样式
			platReport.addBetMoneyWindow.setTitle('实际投注额'); // 设置窗口的名称
			platReport.addBetMoneyWindow.show().center(); // 显示窗口
			platReport.betMoneyDetail.baseParams.reportdate = record.data.reporttime;
			platReport.betMoneyDetail.reload();
		}
	});
	
	/** 实际投注额数据源 */
	platReport.betMoneyDetail = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : false,
		baseParams : {
			reportdate:'',
			start : 0,
			limit : platReport.pageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : platReport.realBetMoneyDetailUrl
		}),
		fields : [ 'platformcode','validBetAmount','betamounts','profitamounts','finalamounts','betcount'],
		listeners : {
			'load' : function(store, records, options) {
			}
		}
	});
	/** 改变页的combo*/
	platReport.betMoneyPageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				platReport.pageSize  = parseInt(comboBox.getValue());
				platReport.betMoneybbar.pageSize  = parseInt(comboBox.getValue());
				platReport.betMoneyDetail.baseParams.limit = platReport.pageSize;
				platReport.betMoneyDetail.baseParams.start = 0;
				platReport.betMoneyDetail.load();
			}
		}
	});
	/** 分页 */
	platReport.betMoneybbar = new Ext.PagingToolbar({
		pageSize : platReport.pageSize,
		store : platReport.betMoneyDetail,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', platReport.betMoneyPageSizeCombo ]
	});
	/** 实际投注额列表 */
	platReport.betmoneygrid = new Ext.grid.EditorGridPanel({
		store : platReport.betMoneyDetail,
		autoScroll : 'auto',
		bbar : platReport.betMoneybbar,
		region : 'center',
		loadMask : true,
		stripeRows : true,
		border: false,
		columns : [
			{
				header : "游戏平台",
				dataIndex : 'platformcode',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "投注总额",
				dataIndex : 'betamounts',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "注单数量",
				dataIndex : 'betcount',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "有效投注额",
				dataIndex : 'validBetAmount',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "派彩金额",
				dataIndex : 'profitamounts',
				renderer : function(v) {
					if(Number(v) > 0){
						return '<span style="color:blue;">'+v+'</span>';
					}else{
						return '<span style="color:red;">'+v+'</span>';
					}
				},
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "输赢金额",
				dataIndex : 'finalamounts',
				renderer : function(v) {
					if(Number(v) > 0){
						return '<span style="color:blue;">'+v+'</span>';
					}else{
						return '<span style="color:red;">'+v+'</span>';
					}
				},
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			}
		]
	});
	
	/** 实体投注额窗口 */
	platReport.addBetMoneyWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 450,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [platReport.betmoneygrid]
	});
	
	// ----------------------实际投注额统计 end--------------------
	
	
	// ----------------------会员优惠统计 begin--------------------
	/** 会员优惠按钮 */
	platReport.memberCouponAction = new Ext.Action({
		text : '会员优惠',
		disabled : true,
		iconCls : 'Coinsadd',
		handler : function() {
			var record = platReport.grid.getSelectionModel().getSelected();
			platReport.addMemberCouponWindow.setIconClass('Coinsadd'); // 设置窗口的样式
			platReport.addMemberCouponWindow.setTitle('会员优惠'); // 设置窗口的名称
			platReport.addMemberCouponWindow.show().center(); // 显示窗口
			platReport.memberCouponDetail.baseParams.reportdate = record.data.reporttime;
			platReport.memberCouponDetail.reload();
		}
	});
	
	/** 会员优惠数据源 */
	platReport.memberCouponDetail = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : false,
		baseParams : {
			reportdate:'',
			start : 0,
			limit : platReport.pageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : platReport.memberCouponDetailUrl
		}),
		fields : [ 'poid','acounts','username','amounts','depotime','kfremark','cwremark','ordertype'],
		listeners : {
			'load' : function(store, records, options) {
			}
		}
	});
	/** 改变页的combo*/
	platReport.memberCouponPageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				platReport.pageSize  = parseInt(comboBox.getValue());
				platReport.memberCouponbbar.pageSize  = parseInt(comboBox.getValue());
				platReport.memberCouponDetail.baseParams.limit = platReport.pageSize;
				platReport.memberCouponDetail.baseParams.start = 0;
				platReport.memberCouponDetail.load();
			}
		}
	});
	/** 分页 */
	platReport.memberCouponbbar = new Ext.PagingToolbar({
		pageSize : platReport.pageSize,
		store : platReport.memberCouponDetail,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', platReport.memberCouponPageSizeCombo ]
	});
	/** 会员优惠列表 */
	platReport.membercoupongrid = new Ext.grid.EditorGridPanel({
		store : platReport.memberCouponDetail,
		autoScroll : 'auto',
		bbar : platReport.memberCouponbbar,
		region : 'center',
		loadMask : true,
		stripeRows : true,
		border: false,
		columns : [
			{
				header : "订单ID",
				dataIndex : 'poid',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "会员账号",
				dataIndex : 'acounts',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "会员姓名",
				dataIndex : 'username',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "优惠金额",
				dataIndex : 'amounts',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "订单类型",
				dataIndex : 'ordertype',
				renderer : function(v) {
					return Share.map(v, platReport.ordertype, '');
       						
      			},
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "客服备注",
				dataIndex : 'kfremark',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "财务备注",
				dataIndex : 'cwremark',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "优惠时间",
				dataIndex : 'depotime',
				renderer : function(v) {
					return v==null?'':new Date(v).format('Y-m-d H:i:s');
				},
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			}
		]
	});
	
	/** 会员优惠窗口 */
	platReport.addMemberCouponWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 450,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [platReport.membercoupongrid]
	});
	
	// ----------------------会员优惠统计 end--------------------
	
	// ----------------------代理优惠统计 begin--------------------
	/** 代理优惠按钮 */
	platReport.proxyCouponAction = new Ext.Action({
		text : '代理优惠',
		disabled : true,
		iconCls : 'Coinsadd',
		handler : function() {
			var record = platReport.grid.getSelectionModel().getSelected();
			platReport.addProxyCouponWindow.setIconClass('Coinsadd'); // 设置窗口的样式
			platReport.addProxyCouponWindow.setTitle('会员优惠'); // 设置窗口的名称
			platReport.addProxyCouponWindow.show().center(); // 显示窗口
			platReport.proxyCouponDetail.baseParams.reportdate = record.data.reporttime;
			platReport.proxyCouponDetail.reload();
		}
	});
	
	/** 代理优惠数据源 */
	platReport.proxyCouponDetail = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : false,
		baseParams : {
			reportdate:'',
			start : 0,
			limit : platReport.pageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : platReport.proxyCouponDetailUrl
		}),
		fields : [ 'poid','acounts','username','amounts','depotime','kfremark','cwremark','ordertype'],
		listeners : {
			'load' : function(store, records, options) {
			}
		}
	});
	/** 改变页的combo*/
	platReport.proxyCouponPageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				platReport.pageSize  = parseInt(comboBox.getValue());
				platReport.proxyCouponbbar.pageSize  = parseInt(comboBox.getValue());
				platReport.proxyCouponDetail.baseParams.limit = platReport.pageSize;
				platReport.proxyCouponDetail.baseParams.start = 0;
				platReport.proxyCouponDetail.load();
			}
		}
	});
	/** 分页 */
	platReport.proxyCouponbbar = new Ext.PagingToolbar({
		pageSize : platReport.pageSize,
		store : platReport.proxyCouponDetail,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', platReport.proxyCouponPageSizeCombo ]
	});
	/** 代理优惠列表 */
	platReport.proxycoupongrid = new Ext.grid.EditorGridPanel({
		store : platReport.proxyCouponDetail,
		autoScroll : 'auto',
		bbar : platReport.proxyCouponbbar,
		region : 'center',
		loadMask : true,
		stripeRows : true,
		border: false,
		columns : [
			{
				header : "订单ID",
				dataIndex : 'poid',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "代理账号",
				dataIndex : 'acounts',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "代理姓名",
				dataIndex : 'username',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "优惠金额",
				dataIndex : 'amounts',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "订单类型",
				dataIndex : 'ordertype',
				renderer : function(v) {
					return Share.map(v, platReport.ordertype, '');
      			},
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "客服备注",
				dataIndex : 'kfremark',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "财务备注",
				dataIndex : 'cwremark',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "优惠时间",
				dataIndex : 'depotime',
				renderer : function(v) {
					return v==null?'':new Date(v).format('Y-m-d H:i:s');
				},
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			}
		]
	});
	
	/** 代理优惠窗口 */
	platReport.addProxyCouponWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 450,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [platReport.proxycoupongrid]
	});
	
	// ----------------------代理优惠统计 end--------------------
	
	// ----------------------充值金额统计 begin--------------------
	/** 充值金额按钮 */
	platReport.payMoneyAction = new Ext.Action({
		text : '充值金额',
		disabled : true,
		iconCls : 'Coinsadd',
		handler : function() {
			var record = platReport.grid.getSelectionModel().getSelected();
			platReport.addPayMoneyWindow.setIconClass('Coinsadd'); // 设置窗口的样式
			platReport.addPayMoneyWindow.setTitle('充值金额'); // 设置窗口的名称
			platReport.addPayMoneyWindow.show().center(); // 显示窗口
			platReport.payMoneyDetail.baseParams.reportdate = record.data.reporttime;
			platReport.payMoneyDetail.reload();
		}
	});
	
	/** 充值金额数据源 */
	platReport.payMoneyDetail = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : false,
		baseParams : {
			reportdate:'',
			start : 0,
			limit : platReport.pageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : platReport.payMoneyDetailUrl
		}),
		fields : [ 'poid','acounts','username','amounts','depotime','kfremarks','cwremarks'],
		listeners : {
			'load' : function(store, records, options) {
			}
		}
	});
	/** 改变页的combo*/
	platReport.payMoneyPageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				platReport.pageSize  = parseInt(comboBox.getValue());
				platReport.payMoneybbar.pageSize  = parseInt(comboBox.getValue());
				platReport.payMoneyDetail.baseParams.limit = platReport.pageSize;
				platReport.payMoneyDetail.baseParams.start = 0;
				platReport.payMoneyDetail.load();
			}
		}
	});
	/** 分页 */
	platReport.payMoneybbar = new Ext.PagingToolbar({
		pageSize : platReport.pageSize,
		store : platReport.payMoneyDetail,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', platReport.payMoneyPageSizeCombo ]
	});
	/** 充值金额列表 */
	platReport.paymoneygrid = new Ext.grid.EditorGridPanel({
		store : platReport.payMoneyDetail,
		autoScroll : 'auto',
		bbar : platReport.payMoneybbar,
		region : 'center',
		loadMask : true,
		stripeRows : true,
		border: false,
		columns : [
			{
				header : "订单ID",
				dataIndex : 'poid',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "会员账号",
				dataIndex : 'acounts',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "会员姓名",
				dataIndex : 'username',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "充值金额",
				dataIndex : 'amounts',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "充值时间",
				dataIndex : 'depotime',
				renderer : function(v) {
					return v==null?'':new Date(v).format('Y-m-d H:i:s');
				},
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "客服备注",
				dataIndex : 'kfremarks',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "财务备注",
				dataIndex : 'cwremarks',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			}
		]
	});
	
	/** 充值金额窗口 */
	platReport.addPayMoneyWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 450,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [platReport.paymoneygrid]
	});
	
	// ----------------------充值金额统计 end--------------------
	
	// ----------------------提款金额统计 begin--------------------
	/** 提款金额按钮 */
	platReport.pickMoneyAction = new Ext.Action({
		text : '提款金额',
		disabled : true,
		iconCls : 'Coinsadd',
		handler : function() {
			var record = platReport.grid.getSelectionModel().getSelected();
			platReport.addPickMoneyWindow.setIconClass('Coinsadd'); // 设置窗口的样式
			platReport.addPickMoneyWindow.setTitle('提款金额'); // 设置窗口的名称
			platReport.addPickMoneyWindow.show().center(); // 显示窗口
			platReport.pickMoneyDetail.baseParams.reportdate = record.data.reporttime;
			platReport.pickMoneyDetail.reload();
		}
	});
	
	/** 提款金额数据源 */
	platReport.pickMoneyDetail = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : false,
		baseParams : {
			reportdate:'',
			start : 0,
			limit : platReport.pageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : platReport.pickUpMoneyrDetailUrl
		}),
		fields : [ 'poid','acounts','username','amounts','depotime','kfremarks','cwremarks'],
		listeners : {
			'load' : function(store, records, options) {
			}
		}
	});
	/** 改变页的combo*/
	platReport.pickMoneyPageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				platReport.pageSize  = parseInt(comboBox.getValue());
				platReport.pickMoneybbar.pageSize  = parseInt(comboBox.getValue());
				platReport.pickMoneyDetail.baseParams.limit = platReport.pageSize;
				platReport.pickMoneyDetail.baseParams.start = 0;
				platReport.pickMoneyDetail.load();
			}
		}
	});
	/** 分页 */
	platReport.pickMoneybbar = new Ext.PagingToolbar({
		pageSize : platReport.pageSize,
		store : platReport.pickMoneyDetail,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', platReport.pickMoneyPageSizeCombo ]
	});
	/** 提款金额列表 */
	platReport.pickmoneygrid = new Ext.grid.EditorGridPanel({
		store : platReport.pickMoneyDetail,
		autoScroll : 'auto',
		bbar : platReport.pickMoneybbar,
		region : 'center',
		loadMask : true,
		stripeRows : true,
		border: false,
		columns : [
			{
				header : "订单ID",
				dataIndex : 'poid',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "会员账号",
				dataIndex : 'acounts',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "会员姓名",
				dataIndex : 'username',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "提款金额",
				dataIndex : 'amounts',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "提款时间",
				dataIndex : 'depotime',
				renderer : function(v) {
					return v==null?'':new Date(v).format('Y-m-d H:i:s');
				},
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "客服备注",
				dataIndex : 'kfremarks',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "财务备注",
				dataIndex : 'cwremarks',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			}
		]
	});
	
	/** 提款金额窗口 */
	platReport.addPickMoneyWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 450,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [platReport.pickmoneygrid]
	});
	
	// ----------------------提款金额统计 end--------------------
	
	
	// ----------------------单独模块统计数据 end--------------------
	/** 查询 */
	platReport.searchAction = new Ext.Action({
			text : '查询',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				platReport.searchFun();
			}
	});
	platReport.searchParams = function(){
		var obj = {};
		obj.startDate = $("#reportStartDate").val();
		obj.endDate = $("#reportEndDate").val();
	    return obj;
	}
	
	platReport.searchFun = function(){
		platReport.store.load({params: platReport.searchParams()});
	}
	
	platReport.store.on('beforeload',function(store, options){
	    platReport.store.baseParams = platReport.searchParams();
	});
 	/**日期条件  -- 开始时间*/
 	platReport.startDateField = new Ext.form.DateField({
  			id:'reportStartDate',
        	showToday:true,
        	format:'Y-m-d',
        	invalidText:'日期输入非法',
        	allowBlank : false,
        	width:150 
 	});
 	/** 日期条件  -- 结束时间*/
 	platReport.endDateField = new Ext.form.DateField({
  			id:'reportEndDate',
        	showToday:true,
        	format:'Y-m-d',
        	invalidText:'日期输入非法',
        	allowBlank : false,
        	width:150 
 	});
	
	/** 顶部工具栏 */
	platReport.tbar = [platReport.downAction,platReport.registerAction,platReport.firstPayNumberAction,platReport.betMoneyAction,platReport.memberCouponAction,platReport.proxyCouponAction,platReport.payMoneyAction,platReport.pickMoneyAction];
	/** 底部工具条 */
	platReport.bbar = new Ext.PagingToolbar({
		pageSize : platReport.pageSize,
		store : platReport.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', platReport.pageSizeCombo ]
	});
	
	/** 基本信息-表格 */
	platReport.grid = new Ext.grid.EditorGridPanel({
		store : platReport.store,
		colModel : platReport.colModel,
		selModel : platReport.selModel,
		tbar : platReport.tbar,
		bbar : platReport.bbar,
		autoScroll : 'auto',
		region : 'center',
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig:{},
		stripeRows : true,
		listeners : {
            render: function() {
               this.tbar2 = new Ext.Toolbar({  
               renderTo: platReport.grid.tbar,
               items:[
	               		'开始时间:',platReport.startDateField,
	               		'&nbsp;','-','&nbsp;',
	               		'截止时间:',platReport.endDateField,
	               		'&nbsp;','-','&nbsp;',
						platReport.searchAction
                    ]
            	})
          	 }
		}
	});
	
	platReport.alwaysFun = function() {
		Share.resetGrid(platReport.grid);
		platReport.registerAction.disable();
		platReport.firstPayNumberAction.disable();
		platReport.betMoneyAction.disable();
		platReport.memberCouponAction.disable();
		platReport.proxyCouponAction.disable();
		platReport.payMoneyAction.disable();
		platReport.pickMoneyAction.disable();
	};
	
	platReport.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'border',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
		items : [ platReport.grid ]
	});
</script>
