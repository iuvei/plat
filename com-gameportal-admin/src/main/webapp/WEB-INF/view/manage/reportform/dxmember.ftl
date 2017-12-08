<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	Ext.ns("Ext.market.dxMemberReport"); // 自定义一个命名空间
	dxMemberReport = Ext.market.dxMemberReport; // 定义命名空间的别名
	dxMemberReport = {
		queryDxMember:'/manage/dxMemberReport/queryDxMemberReportResult.do', //查询all
		pageSize : 30 // 每页显示的记录数
	};
	/** 订单管理数据源 */
	dxMemberReport.store = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : true,
		baseParams : {
			account:'',
			uname:'',
			truename:'',
			phone:'',
			qq:'',
			email:'',
			startDate:'',
			endDate:'',
			start : 0,
			limit : dxMemberReport.pageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : dxMemberReport.queryDxMember
		}),
		fields : [ 'account','uname','truename','depositorderMoney','pickUpMoney','finalMoney','validBetMoney'],
		listeners : {
			'load' : function(store, records, options) {
				dxMemberReport.amountsum();
			 }
		}
	});
	
	/**日期条件  -- 开始时间*/
 	dxMemberReport.startDateField = new Ext.form.DateField({
  			id:'dxMemberReportstartDate',
        	showToday:true,
        	format:'Y-m-d H:i:s',
        	invalidText:'日期输入非法',
        	allowBlank : false,
        	width:130
 	});
 	/** 日期条件  -- 结束时间*/
 	dxMemberReport.endDateField = new Ext.form.DateField({
  			id:'dxMemberReportendDate',
        	showToday:true,
        	format:'Y-m-d H:i:s',
        	invalidText:'日期输入非法',
        	allowBlank : false,
        	width:130
 	});
	/** 查询按钮 */
	dxMemberReport.searchAction = new Ext.Action({
			text : '查询',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				   dxMemberReport.searchFun();
			}
	});
	dxMemberReport.searchParams = function(){
		var obj = {};
		obj.account =  $("#dxMemberReportaccount").val();
		obj.uname =  $("#uname").val();
		obj.truename =  $("#truename").val();
		obj.startDate = $("#dxMemberReportstartDate").val();
		obj.endDate = $("#dxMemberReportendDate").val();
	    return obj;
	}
	
	dxMemberReport.searchFun = function(){
		dxMemberReport.store.load({params: dxMemberReport.searchParams()});
	}
	
	dxMemberReport.store.on('beforeload',function(store, options){
	    dxMemberReport.store.baseParams = dxMemberReport.searchParams();
	});
	/** 顶部工具栏 */
	dxMemberReport.tbar = ['会员账号:',{id:'dxMemberReportaccount',xtype:'textfield',width:100},
						'-','&nbsp;','会员名称:',{id:'uname',xtype:'textfield',width:100},
						'-','&nbsp;','电销名称:',{id:'truename',xtype:'textfield',width:100},
						'-','&nbsp;','时间区间:',dxMemberReport.startDateField,'~',dxMemberReport.endDateField,
						'-','&nbsp;',dxMemberReport.searchAction];
	
	//小计
	dxMemberReport.amountsum = function(){
		var p = new Ext.data.Record({fields:['account','uname','truename','depositorderMoney','pickUpMoney','finalMoney','validBetMoney']});
		var depositorderMoneyTotal= 0,pickUpMoneyTotal = 0,finalMoneyTotal=0,validBetMoneyTotal=0;
		dxMemberReport.store.each(function(record){
			var depositorderMoneys = record.data.depositorderMoney;
			if(depositorderMoneys != null){//存款金额
				depositorderMoneyTotal += Number(depositorderMoneys);
			}
			var pickUpMoneys = record.data.pickUpMoney;
			if(pickUpMoneys != null){//提款金额
				pickUpMoneyTotal += Number(pickUpMoneys);
			}
			var finalMoneys = record.data.finalMoney;
			if(finalMoneys != null){//输赢值
				finalMoneyTotal += Number(finalMoneys);
			}
			var validBetMoneys = record.data.validBetMoney;
			if(validBetMoneys != null){//有效投注额
				validBetMoneyTotal += Number(validBetMoneys);
			}
		});
		if(Number(finalMoneyTotal) > 0){
			finalMoneyTotal= '<span style="color:blue;">'+finalMoneyTotal+'</span>';
		}else{
			finalMoneyTotal= '<span style="color:red;">'+finalMoneyTotal+'</span>';
		}
		p.set('account','小计：');
		p.set('uname','');
		p.set('truename','');
		p.set('depositorderMoney',depositorderMoneyTotal);
		p.set('pickUpMoney',pickUpMoneyTotal);
		p.set('finalMoney',finalMoneyTotal);
		p.set('validBetMoney',validBetMoneyTotal);
		dxMemberReport.store.add(p);
	}
	/** 基本信息-选择模式 */
	dxMemberReport.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
			}
		}
	});
	/** 改变页的combo*/
	dxMemberReport.PageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				dxMemberReport.pageSize  = parseInt(comboBox.getValue());
				dxMemberReport.bbar.pageSize  = parseInt(comboBox.getValue());
				dxMemberReport.store.baseParams.limit = dxMemberReport.pageSize;
				dxMemberReport.store.baseParams.start = 0;
				dxMemberReport.store.load();
			}
		}
	});
	/** 分页 */
	dxMemberReport.bbar = new Ext.PagingToolbar({
		pageSize : dxMemberReport.pageSize,
		store : dxMemberReport.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', dxMemberReport.PageSizeCombo ]
	});
	/** 电销客户分析表 */
	dxMemberReport.grid = new Ext.grid.EditorGridPanel({
		title : "电销客户分析表",
		store : dxMemberReport.store,
		selModel : dxMemberReport.selModel,
		autoScroll : 'auto',
		region : 'center',
	    tbar : dxMemberReport.tbar,
		bbar:dxMemberReport.bbar,
		loadMask : true,
		stripeRows : true,
		border: false,
		columns : [dxMemberReport.selModel,
			{
				header : "会员账号",
				dataIndex : 'account',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "会员名称",
				dataIndex : 'uname',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "电销名称",
				dataIndex : 'truename',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "存款金额",
				dataIndex : 'depositorderMoney',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "提款金额",
				dataIndex : 'pickUpMoney',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : "输赢值",
				dataIndex : 'finalMoney',
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
				header : "有效投注额",
				dataIndex : 'validBetMoney',
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			}
		]
	});
	
	dxMemberReport.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'border',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
    	items: [dxMemberReport.grid]
	});
</script>
