<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	Ext.ns("Ext.market.orderManageReport"); // 自定义一个命名空间
	orderManageReport = Ext.market.orderManageReport; // 定义命名空间的别名
	orderManageReport = {
		accountMoneyUrl:'/manage/ordermanageReport/queryAccountMoney.do', //钱包余额
		orderManageUrl:'/manage/ordermanageReport/queryOrderManageResult.do',//订单管理记录
		down:'/manage/ordermanageReport/toDownloadReport.do',//导出订单管理报表
		pageSize : 30,// 每页显示的记录数
		ordertype : eval('(${fields.ordertype})'),//订单类型(fields)
		statusMap : eval('(${statusMap})'),//订单状态(作废,发起,处理中,处理成功,处理失败)
		paymethods : eval('(${paymethods})'),//支付方法(公司入款,第三方支付)
		paytype : eval('(${paytype})')//订单类型(存款,提款,加款,扣款)
	};
	
	/** 订单管理数据源 */
	orderManageReport.store = new Ext.data.JsonStore({
		remoteSort : true,
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : false,
		baseParams : {
			account:'',
			startDate:'',
			endDate:'',
			paystatus:'',
			poid:'',
			paytype:'',
			ordertype:'',
			paymethods:'',
			start : 0,
			limit : orderManageReport.pageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : orderManageReport.orderManageUrl
		}),
		fields : [ 'poid','uaccount','urealname','paytyple','ordertype','paymethods','deposittime','amount','status','kfremarks','kfname','kfopttime','cwremarks','cwname','cwopttime','beforebalance','laterbalance','memberximaMoney','proxyclearMoney','proxyuserximaMoney'],
		listeners : {
			'load' : function(store, records, options) {
			}
		}
	});
	/**日期条件  -- 开始时间*/
 	orderManageReport.startDateField = new Ext.form.DateField({
  			id:'orderManageReportstartDate',
        	showToday:true,
        	format:'Y-m-d H:i:s',
        	invalidText:'日期输入非法',
        	allowBlank : false,
        	width:130
 	});
 	/** 日期条件  -- 结束时间*/
 	orderManageReport.endDateField = new Ext.form.DateField({
  			id:'orderManageReportendDate',
        	showToday:true,
        	format:'Y-m-d H:i:s',
        	invalidText:'日期输入非法',
        	allowBlank : false,
        	width:130
 	});
 	/** 导出报表按钮 */
	orderManageReport.downAction = new Ext.Action({
			text : '导出报表',
			iconCls : 'Diskdownload',
			disabled : false,
			handler : function() {
				orderManageReport.downReport();
			}
	});
	/** 导出报表函数 */
	orderManageReport.downReport = function() {
			var paystatus = [];
			var paytype = [];
			var paymethods = [];
			var ordertype = [];
		    var account =  $("#orderManageReportaccount").val();
		    var startDate = $("#orderManageReportstartDate").val();
		    var endDate = $("#orderManageReportendDate").val();
		    var poid = $("#orderpoid").val();
		   $("input[name='paystatusboxs'][checked]").each(function(){
		      var val=$(this).val();  
		      if(val=='one'){
		   	    val='0';
		      }
		   paystatus.push("'"+val+"'");                     
		   }); 
		   $("input[name='paytypeboxs'][checked]").each(function(){
		      var val=$(this).val();  
		      if(val=='one'){
		   	    val='0';
		      }
		   paytype.push("'"+val+"'");                     
		   }); 
		   $("input[name='paymethodsboxs'][checked]").each(function(){
		      var val=$(this).val();  
		      if(val=='one'){
		   	    val='0';
		      }
		   paymethods.push("'"+val+"'");                     
		   }); 
		   $("input[name='ordertypeboxs'][checked]").each(function(){
		      var val=$(this).val();  
		      if(val=='one'){
		   	    val='0';
		      }
		   ordertype.push("'"+val+"'");                     
		   }); 
		 //发送请求
		 window.open(orderManageReport.down + "?account="+account+"&startDate="+startDate+"&endDate="+endDate+"&poid="+poid+"&paystatus="+paystatus+"&paytype="+paytype+"&paymethods="+paymethods+"&ordertype="+ordertype);
	};
	/** 查询按钮 */
	orderManageReport.searchAction = new Ext.Action({
			text : '查询',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				// 查询钱包余额
				Share.AjaxRequest({
						url : orderManageReport.accountMoneyUrl,
						params : {account:$("#orderManageReportaccount").val()},
						callback : function(json) {
							$("#orderaccountMoneys").text(json.accountMoney.totalamount);
						}
				});
				orderManageReport.searchFun();
			}
		
	});
	
	orderManageReport.searchParams = function(){
		var obj = {};
		var paystatus = [];
		var paytype = [];
		var paymethods = [];
		var ordertype = [];
		 $("input[name='paystatusboxs'][checked]").each(function(){
		   var val=$(this).val();  
		   if(val=='one'){
		   	  val='0';
		   }
		   paystatus.push("'"+val+"'");                     
		 }); 
		 $("input[name='paytypeboxs'][checked]").each(function(){
		   var val=$(this).val();  
		   if(val=='one'){
		   	  val='0';
		   }
		   paytype.push("'"+val+"'");                     
		 }); 
		 $("input[name='paymethodsboxs'][checked]").each(function(){
		   var val=$(this).val();  
		   if(val=='one'){
		   	  val='0';
		   }
		   paymethods.push("'"+val+"'");                     
		 }); 
		 $("input[name='ordertypeboxs'][checked]").each(function(){
		   var val=$(this).val();  
		   if(val=='one'){
		   	  val='0';
		   }
		   ordertype.push("'"+val+"'");                     
		 }); 
		 obj.account =  $("#orderManageReportaccount").val();
		 obj.startDate = $("#orderManageReportstartDate").val();
		 obj.endDate = $("#orderManageReportendDate").val();
		 obj.poid = $("#orderpoid").val();
		 obj.paystatus = paystatus;
		 obj.paytype = paytype;
		 obj.paymethods = paymethods;
		 obj.ordertype = ordertype;
	     return obj;
	}
	
	orderManageReport.searchFun = function(){
		orderManageReport.store.load({params: orderManageReport.searchParams()});
	}
	
	orderManageReport.store.on('beforeload',function(store, options){
	    orderManageReport.store.baseParams = orderManageReport.searchParams();
	});
	
	/** 顶部工具栏 */
	orderManageReport.tbar = [orderManageReport.downAction,
							   '-','&nbsp;','会员账号:',{id:'orderManageReportaccount',xtype:'textfield',width:80},
							   '-','&nbsp;','时间区间:',orderManageReport.startDateField,'~',orderManageReport.endDateField,
							   '-','&nbsp;','订单号:',{id:'orderpoid',xtype:'textfield',width:80},
							   '-','&nbsp;','处理状态:',
							   {xtype:'checkbox',boxLabel :'作废',name : "paystatusboxs",inputValue : 'one',checked : true},'-',
            				   {xtype:'checkbox',boxLabel :'发起',name : "paystatusboxs",inputValue : '1',checked : true},'-',
            				   {xtype:'checkbox',boxLabel :'处理中',name : "paystatusboxs",inputValue : '2',checked : true},'-',
            				   {xtype:'checkbox',boxLabel :'处理成功',name : "paystatusboxs",inputValue : '3',checked : true},'-',
            				   {xtype:'checkbox',boxLabel :'处理失败',name : "paystatusboxs",inputValue : '4',checked : true}
							   ];
	/** 基本信息-选择模式 */
	orderManageReport.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
			}
		}
	});
	/** 改变页的combo*/
	orderManageReport.PageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				orderManageReport.pageSize  = parseInt(comboBox.getValue());
				orderManageReport.bbar.pageSize  = parseInt(comboBox.getValue());
				orderManageReport.store.baseParams.limit = orderManageReport.pageSize;
				orderManageReport.store.baseParams.start = 0;
				orderManageReport.store.load();
			}
		}
	});
	/** 分页 */
	orderManageReport.bbar = new Ext.PagingToolbar({
		pageSize : orderManageReport.pageSize,
		store : orderManageReport.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', orderManageReport.PageSizeCombo ]
	});
	/** 订单管理 */
	orderManageReport.grid = new Ext.grid.EditorGridPanel({
		title : "订单管理",
		store : orderManageReport.store,
		selModel : orderManageReport.selModel,
		autoScroll : 'auto',
		region : 'center',
	    tbar : orderManageReport.tbar,
		bbar:orderManageReport.bbar,
		loadMask : true,
		stripeRows : true,
		border: false,
		columns : [orderManageReport.selModel,
			{
				header : "订单号",
				dataIndex : 'poid',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "会员账号",
				dataIndex : 'uaccount',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "会员名称",
				dataIndex : 'urealname',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "交易类型",
				dataIndex : 'paytyple',
				renderer : function(v) {
					return Share.map(v, orderManageReport.paytype, '');
				},
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "订单类型",
				dataIndex : 'ordertype',
				renderer : function(v) {
					return Share.map(v, orderManageReport.ordertype, '');
				},
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "支付方式",
				dataIndex : 'paymethods',
				renderer : function(v) {
					return Share.map(v, orderManageReport.paymethods, '');
				},
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "处理时间",
				dataIndex : 'deposittime',
				renderer : function(v) {
					return v==null?'':new Date(v).format('Y-m-d H:i:s');
				},
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "金额",
				dataIndex : 'amount',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "官方洗码金额",
				dataIndex : 'memberximaMoney',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "代理洗码金额",
				dataIndex : 'proxyclearMoney',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "代理下线洗码金额",
				dataIndex : 'proxyuserximaMoney',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "处理状态",
				dataIndex : 'status',
				renderer : function(v) {
					return Share.map(v, orderManageReport.statusMap, '');
				},
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "客服名称",
				dataIndex : 'kfname',
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
				header : "客服操作时间",
				dataIndex : 'kfopttime',
				renderer : function(v) {
					return v==null?'':new Date(v).format('Y-m-d H:i:s');
				},
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "财务名称",
				dataIndex : 'cwname',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "财务备注",
				dataIndex : 'cwremarks',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "财务操作时间",
				dataIndex : 'cwopttime',
				renderer : function(v) {
					return v==null?'':new Date(v).format('Y-m-d H:i:s');
				},
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "操作前钱包余额",
				dataIndex : 'beforebalance',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "操作后钱包余额",
				dataIndex : 'laterbalance',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			}
		],
		listeners : {
           render: function() {
               this.tbar2 = new Ext.Toolbar({  
               renderTo: orderManageReport.grid.tbar,
               items:[
					'支付方式:',
					{xtype:'checkbox',boxLabel :'公司入款',name : "paymethodsboxs",inputValue : 'one',checked : true},'-',
            		{xtype:'checkbox',boxLabel :'第三方支付',name : "paymethodsboxs",inputValue : '1',checked : true},
					'-','&nbsp;','交易类型:',
					{xtype:'checkbox',boxLabel :'存款',name : "paytypeboxs",inputValue : 'one',checked : true},'-',
            		{xtype:'checkbox',boxLabel :'提款',name : "paytypeboxs",inputValue : '1',checked : true},'-',
					{xtype:'checkbox',boxLabel :'加款',name : "paytypeboxs",inputValue : '2',checked : true},'-',
            		{xtype:'checkbox',boxLabel :'扣款',name : "paytypeboxs",inputValue : '3',checked : true},'-',
            		{xtype:'checkbox',boxLabel :'洗码',name : "ordertypeboxs",inputValue : '3',checked : true},
            		'-','&nbsp;',orderManageReport.searchAction,
	               	'钱包余额:<span id="orderaccountMoneys" style="color:blue;"></span>'
                    ]
            	})
          	 }
		}
	});
	
	orderManageReport.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'border',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
    	items: [orderManageReport.grid]
	});
</script>
