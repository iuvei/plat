<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	Ext.ns("Ext.market.memberDetailReport"); // 自定义一个命名空间
	memberDetailReport = Ext.market.memberDetailReport; // 定义命名空间的别名
	memberDetailReport = {
		accountMoneyUrl:'/manage/memberDetailReport/queryAccountMoney.do', //钱包余额
		transferDetailUrl:'/manage/memberDetailReport/queryTransferResult.do',//转账记录
		payMoneyDetailUrl:'/manage/memberDetailReport/queryPayMoneyResult.do',//存款记录
		pickMoneyDetailUrl:'/manage/memberDetailReport/queryPickMoneyResult.do',//取款记录
		memberCouponDetailUrl:'/manage/memberDetailReport/queryCouponMoneyResult.do',//优惠纪录
		ximaDetailUrl:'/manage/memberDetailReport/queryMemberXimaResult.do',//洗码记录
		pageSize : 30,// 每页显示的记录数
		ordertype : eval('(${fields.ordertype})'),//订单类型
		paystatus : eval('(${paystatus})')//订单状态
	};
	/**日期条件  -- 开始时间*/
 	memberDetailReport.startDateField = new Ext.form.DateField({
  			id:'memberDetailReportstartDate',
        	showToday:true,
        	format:'Y-m-d',
        	invalidText:'日期输入非法',
        	allowBlank : false,
        	width:150 
 	});
 	/** 日期条件  -- 结束时间*/
 	memberDetailReport.endDateField = new Ext.form.DateField({
  			id:'memberDetailReportendDate',
        	showToday:true,
        	format:'Y-m-d',
        	invalidText:'日期输入非法',
        	allowBlank : false,
        	width:150 
 	});
 	/** 订单状态*/
	memberDetailReport.orderstateAction = new Ext.form.ComboBox({
		hiddenName : 'orderstate',
		id : 'orderstate',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(memberDetailReport.paystatus)
				}),
		valueField : 'v',
		displayField : 't',
		value:'',
		allowBlank : false,
		editable : false,
		width:100
	});
	/** 订单类型*/
	memberDetailReport.orderTypeAction = new Ext.form.ComboBox({
		hiddenName : 'ordertype',
		id : 'ordertype',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(memberDetailReport.ordertype)
				}),
		valueField : 'v',
		displayField : 't',
		value:'',
		allowBlank : false,
		editable : false,
		width:100
	});
	/** 查询按钮 */
	memberDetailReport.searchAction = new Ext.Action({
			text : '查询',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				var account=$("#memberDetailReportaccount").val(); //会员账号
				var startDate=$("#memberDetailReportstartDate").val(); //开始时间 
				var endDate=$("#memberDetailReportendDate").val(); //结束时间
				var orderstate=$("#orderstate").prev().val();//订单状态
				var poid=$("#poid").val();//订单号
				var ordertype=$("#ordertype").prev().val();//订单类型
				/**转账*/
				memberDetailReport.transferDetail.baseParams.account = account;
				memberDetailReport.transferDetail.baseParams.startDate = startDate;
				memberDetailReport.transferDetail.baseParams.endDate = endDate;
				/** 存款*/
				memberDetailReport.payMoneyDetail.baseParams.account = account;
				memberDetailReport.payMoneyDetail.baseParams.startDate = startDate;
				memberDetailReport.payMoneyDetail.baseParams.endDate = endDate;
				memberDetailReport.payMoneyDetail.baseParams.orderstate = orderstate;
				memberDetailReport.payMoneyDetail.baseParams.poid = poid;
				memberDetailReport.payMoneyDetail.baseParams.ordertype = ordertype;
				/** 取款*/
				memberDetailReport.pickMoneyDetail.baseParams.account = account;  
				memberDetailReport.pickMoneyDetail.baseParams.startDate = startDate;
				memberDetailReport.pickMoneyDetail.baseParams.endDate = endDate;
				memberDetailReport.pickMoneyDetail.baseParams.orderstate = orderstate;
				memberDetailReport.pickMoneyDetail.baseParams.poid = poid;
				memberDetailReport.pickMoneyDetail.baseParams.ordertype = ordertype;
				/** 优惠*/
				memberDetailReport.memberCouponDetail.baseParams.account = account; 
				memberDetailReport.memberCouponDetail.baseParams.startDate = startDate;
				memberDetailReport.memberCouponDetail.baseParams.endDate = endDate;
				memberDetailReport.memberCouponDetail.baseParams.orderstate = orderstate;
				memberDetailReport.memberCouponDetail.baseParams.poid = poid;
				memberDetailReport.memberCouponDetail.baseParams.ordertype = ordertype;
				/** 洗码*/
				memberDetailReport.ximaDetail.baseParams.account = account;
				memberDetailReport.ximaDetail.baseParams.startDate = startDate;
				memberDetailReport.ximaDetail.baseParams.endDate = endDate;
				
				
				memberDetailReport.transferDetail.reload();
				memberDetailReport.payMoneyDetail.reload();
				memberDetailReport.pickMoneyDetail.reload();
				memberDetailReport.memberCouponDetail.reload();
				memberDetailReport.ximaDetail.reload();
				// 查询钱包余额
				Share.AjaxRequest({
					url : memberDetailReport.accountMoneyUrl,
					params : {account:account},
					callback : function(json) {
						$("#memberDetailaccountMoneys").text(json.accountMoney.totalamount);
					}
				});
			}
	});
	// ----------------------转账记录统计 begin--------------------
	/** 转账记录详情数据源 */
	memberDetailReport.transferDetail = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : false,
		baseParams : {
			start : 0,
			limit : memberDetailReport.pageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : memberDetailReport.transferDetailUrl
		}),
		fields : [ 'account','truename','gamename','togamename','amount','updateDate'],
		listeners : {
			'load' : function(store, records, options) {
			}
		}
	});
	/** 改变页的combo*/
	memberDetailReport.transferPageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				memberDetailReport.pageSize  = parseInt(comboBox.getValue());
				memberDetailReport.transferbbar.pageSize  = parseInt(comboBox.getValue());
				memberDetailReport.transferNumberDetail.baseParams.limit = memberDetailReport.pageSize;
				memberDetailReport.transferNumberDetail.baseParams.start = 0;
				memberDetailReport.transferNumberDetail.load();
			}
		}
	});
	/** 分页 */
	memberDetailReport.transferbbar = new Ext.PagingToolbar({
		pageSize : memberDetailReport.pageSize,
		store : memberDetailReport.transferDetail,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', memberDetailReport.transferPageSizeCombo ]
	});
		/** 顶部工具栏 */
	memberDetailReport.tbar = ['会员账号:',{id:'memberDetailReportaccount',xtype:'textfield',width:100},'-','&nbsp;','时间区间:',memberDetailReport.startDateField,'~',memberDetailReport.endDateField,'-','&nbsp;','订单状态:',memberDetailReport.orderstateAction,'-','&nbsp;','订单号:',{id:'poid',xtype:'textfield',width:100},'-','&nbsp;','订单类型:',memberDetailReport.orderTypeAction,'-','&nbsp;',memberDetailReport.searchAction,'-','&nbsp;钱包余额:<span id="memberDetailaccountMoneys" style="color:blue;"></span>'];
	memberDetailReport.transferColModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 90
		},
		columns : [
			{
				header : "会员账号",
				dataIndex : 'account',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "会员姓名",
				dataIndex : 'truename',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "转出平台",
				dataIndex : 'gamename',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "转入平台",
				dataIndex : 'togamename',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "转账金额",
				dataIndex : 'amount',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "转账时间",
				dataIndex : 'updateDate',
				renderer : function(v) {
					return v==null?'':new Date(v).format('Y-m-d H:i:s');
				},
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			}
		]
	});
	/** 转账记录列表 */
	memberDetailReport.transfergrid = new Ext.grid.EditorGridPanel({
		title : "转账记录",
		store : memberDetailReport.transferDetail,
		colModel : memberDetailReport.transferColModel,
		autoScroll : 'auto',
		anchor: '100% 20%',
	    tbar : memberDetailReport.tbar,
		bbar:memberDetailReport.transferbbar,
		loadMask : true,
		stripeRows : true,
		viewConfig:{forceFit : true}
	});
	
	// ----------------------转账记录统计 end--------------------
	
	// ----------------------存款列表统计 begin--------------------
	/** 存款列表数据源 */
	memberDetailReport.payMoneyDetail = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : false,
		baseParams : {
			start : 0,
			limit : memberDetailReport.pageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : memberDetailReport.payMoneyDetailUrl
		}),
		fields : [ 'poid','uaccount','urealname','amount','deposittime','kfremarks','cwremarks','beforebalance','laterbalance'],
		listeners : {
			'load' : function(store, records, options) {
			}
		}
	});
	/** 改变页的combo*/
	memberDetailReport.payMoneyPageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				memberDetailReport.pageSize  = parseInt(comboBox.getValue());
				memberDetailReport.payMoneybbar.pageSize  = parseInt(comboBox.getValue());
				memberDetailReport.payMoneyDetail.baseParams.limit = memberDetailReport.pageSize;
				memberDetailReport.payMoneyDetail.baseParams.start = 0;
				memberDetailReport.payMoneyDetail.load();
			}
		}
	});
	/** 分页 */
	memberDetailReport.payMoneybbar = new Ext.PagingToolbar({
		pageSize : memberDetailReport.pageSize,
		store : memberDetailReport.payMoneyDetail,
		displayInfo : true,
		items : [ '-', '&nbsp;', memberDetailReport.payMoneyPageSizeCombo ]
	});
	/**存款列表列表 */
	memberDetailReport.paymoneygrid = new Ext.grid.EditorGridPanel({
		store : memberDetailReport.payMoneyDetail,
		autoScroll : 'auto',
		anchor: '100% 20%',
	    title: '存款列表',
		bbar:memberDetailReport.payMoneybbar,
		loadMask : true,
		stripeRows : true,
		border: false,
		viewConfig:{forceFit : true},
		columns : [
			{
				header : "订单ID",
				dataIndex : 'poid'
			},{
				header : "会员账号",
				dataIndex : 'uaccount'
			},{
				header : "会员姓名",
				dataIndex : 'urealname'
			},{
				header : "存款金额",
				dataIndex : 'amount'
			},{
				header : "存款时间",
				dataIndex : 'deposittime',
				renderer : function(v) {
					return v==null?'':new Date(v).format('Y-m-d H:i:s');
				}
			},{
				header : "客服备注",
				dataIndex : 'kfremarks'
			},{
				header : "财务备注",
				dataIndex : 'cwremarks'
			},{
				header : '存款前余额',
				dataIndex : 'beforebalance',
				width : 150
			},{
				header : '存款后余额',
				dataIndex : 'laterbalance',
				width : 150
			}
		]
	});
	
	// ----------------------存款列表 end--------------------
	
	// ----------------------取款列表 begin--------------------
	/** 取款列表数据源 */
	memberDetailReport.pickMoneyDetail = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : false,
		baseParams : {
			start : 0,
			limit : memberDetailReport.pageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : memberDetailReport.pickMoneyDetailUrl
		}),
		fields : ['poid','uaccount','urealname','amount','deposittime','kfremarks','cwremarks','beforebalance','laterbalance'],
		listeners : {
			'load' : function(store, records, options) {
			}
		}
	});
	/** 改变页的combo*/
	memberDetailReport.pickMoneyPageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				memberDetailReport.pageSize  = parseInt(comboBox.getValue());
				memberDetailReport.pickMoneybbar.pageSize  = parseInt(comboBox.getValue());
				memberDetailReport.pickMoneyDetail.baseParams.limit = memberDetailReport.pageSize;
				memberDetailReport.pickMoneyDetail.baseParams.start = 0;
				memberDetailReport.pickMoneyDetail.load();
			}
		}
	});
	/** 分页 */
	memberDetailReport.pickMoneybbar = new Ext.PagingToolbar({
		pageSize : memberDetailReport.pageSize,
		store : memberDetailReport.pickMoneyDetail,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', memberDetailReport.pickMoneyPageSizeCombo ]
	});
	/** 取款列表列表 */
	memberDetailReport.pickmoneygrid = new Ext.grid.EditorGridPanel({
		store : memberDetailReport.pickMoneyDetail,
		autoScroll : 'auto',
		bbar : memberDetailReport.pickMoneybbar,
		anchor: '100% 20%',
	    title: '取款列表',
		loadMask : true,
		viewConfig:{forceFit : true},
		stripeRows : true,
		border: false,
		columns : [
			{
				header : "订单ID",
				dataIndex : 'poid'
			},{
				header : "会员账号",
				dataIndex : 'uaccount'
			},{
				header : "会员姓名",
				dataIndex : 'urealname'
			},{
				header : "取款金额",
				dataIndex : 'amount'
			},{
				header : "取款时间",
				dataIndex : 'deposittime',
				renderer : function(v) {
					return v==null?'':new Date(v).format('Y-m-d H:i:s');
				}
			},{
				header : "客服备注",
				dataIndex : 'kfremarks'
			},{
				header : "财务备注",
				dataIndex : 'cwremarks'
			},{
				header : '取款前余额',
				dataIndex : 'beforebalance',
				width : 150
			},{
				header : '取款后余额',
				dataIndex : 'laterbalance',
				width : 150
			}
		]
	});
	// ----------------------取款列表 end--------------------
	
	
	// ----------------------优惠列表 begin--------------------
	/** 优惠列表数据源 */
	memberDetailReport.memberCouponDetail = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : false,
		baseParams : {
			start : 0,
			limit : memberDetailReport.pageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : memberDetailReport.memberCouponDetailUrl
		}),
		fields : [ 'poid','uaccount','urealname','amount','deposittime','kfremarks','cwremarks','ordertype','beforebalance','laterbalance'],
		listeners : {
			'load' : function(store, records, options) {
			}
		}
	});
	/** 改变页的combo*/
	memberDetailReport.memberCouponPageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				memberDetailReport.pageSize  = parseInt(comboBox.getValue());
				memberDetailReport.memberCouponbbar.pageSize  = parseInt(comboBox.getValue());
				memberDetailReport.memberCouponDetail.baseParams.limit = memberDetailReport.pageSize;
				memberDetailReport.memberCouponDetail.baseParams.start = 0;
				memberDetailReport.memberCouponDetail.load();
			}
		}
	});
	/** 分页 */
	memberDetailReport.memberCouponbbar = new Ext.PagingToolbar({
		pageSize : memberDetailReport.pageSize,
		store : memberDetailReport.memberCouponDetail,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', memberDetailReport.memberCouponPageSizeCombo ]
	});
	/** 会员优惠列表 */
	memberDetailReport.membercoupongrid = new Ext.grid.EditorGridPanel({
		store : memberDetailReport.memberCouponDetail,
		autoScroll : 'auto',
		bbar : memberDetailReport.memberCouponbbar,
		anchor: '100% 20%',
		title: '优惠列表',
		loadMask : true,
		stripeRows : true,
		viewConfig:{forceFit : true},
		border: false,
		columns : [
			{
				header : "订单ID",
				dataIndex : 'poid'
			},{
				header : "会员账号",
				dataIndex : 'uaccount'
			},{
				header : "会员姓名",
				dataIndex : 'urealname'
			},{
				header : "优惠金额",
				dataIndex : 'amount'
			},{
				header : "订单类型",
				dataIndex : 'ordertype',
				renderer : function(v) {
					return Share.map(v, memberDetailReport.ordertype, '');
       						
      			}
			},{
				header : "客服备注",
				dataIndex : 'kfremarks'
			},{
				header : "财务备注",
				dataIndex : 'cwremarks'
			},{
				header : "优惠时间",
				dataIndex : 'deposittime',
				renderer : function(v) {
					return v==null?'':new Date(v).format('Y-m-d H:i:s');
				}
			},{
				header : '优惠前余额',
				dataIndex : 'beforebalance',
				width : 150
			},{
				header : '优惠后余额',
				dataIndex : 'laterbalance',
				width : 150
			}
		]
	});
	
	// ----------------------优惠列表 end--------------------
	
	// ----------------------洗码列表 begin--------------------
	/** 洗码列表数据源 */
	memberDetailReport.ximaDetail = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : false,
		baseParams : {
			start : 0,
			limit : memberDetailReport.pageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : memberDetailReport.ximaDetailUrl
		}),
		fields : ['account','name','gpid','total','ymdstart','ymdend','updatetime'],
		listeners : {
			'load' : function(store, records, options) {
			}
		}
	});
	/** 改变页的combo*/
	memberDetailReport.ximaPageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				memberDetailReport.pageSize  = parseInt(comboBox.getValue());
				memberDetailReport.ximabbar.pageSize  = parseInt(comboBox.getValue());
				memberDetailReport.ximaDetail.baseParams.limit = memberDetailReport.pageSize;
				memberDetailReport.ximaDetail.baseParams.start = 0;
				memberDetailReport.ximaDetail.load();
			}
		}
	});
	/** 分页 */
	memberDetailReport.ximabbar = new Ext.PagingToolbar({
		pageSize : memberDetailReport.pageSize,
		store : memberDetailReport.ximaDetail,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', memberDetailReport.ximaPageSizeCombo ]
	});
	/** 洗码列表列表 */
	memberDetailReport.ximagrid = new Ext.grid.EditorGridPanel({
		store : memberDetailReport.ximaDetail,
		autoScroll : 'auto',
		bbar : memberDetailReport.ximabbar,
		anchor: '100% 20%',
		loadMask : true,
	    title: '洗码列表',
		stripeRows : true,
		border: false,
		viewConfig:{forceFit : true},
		columns : [
			{
				header : "游戏平台",
				dataIndex : 'gpid'
			},{
				header : "会员账号",
				dataIndex : 'account'
			},{
				header : "会员姓名",
				dataIndex : 'name'
			},{
				header : "返回总金额(元)",
				dataIndex : 'total'
			},{
				header : "洗码开始日期",
				dataIndex : 'ymdstart',
				renderer : function(v) {
					return v==null?'':new Date(v).format('Y-m-d H:i:s');
				}
			},{
				header : "洗码结束日期",
				dataIndex : 'ymdend',
				renderer : function(v) {
					return v==null?'':new Date(v).format('Y-m-d H:i:s');
				}
			},{
				header : "更新时间",
				dataIndex : 'updatetime',
				renderer : function(v) {
					return v==null?'':new Date(v).format('Y-m-d H:i:s');
				}
			}
		]
	});
	
	// ----------------------洗码列表计 end--------------------
	
	memberDetailReport.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'anchor',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
		defaults: {
       	 	split: true,                 //是否有分割线
        	collapsible: true           //是否可以折叠
    	},
    	items: [memberDetailReport.transfergrid,memberDetailReport.paymoneygrid,memberDetailReport.pickmoneygrid , memberDetailReport.membercoupongrid, memberDetailReport.ximagrid]
	});
</script>
