<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.memberUpgradeLogs"); // 自定义一个命名空间
gameTransfer = Ext.market.transferLogs; // 定义命名空间的别名
gameTransfer = {
	all : '/manage/gameTransferLog/queryUserTransfer.do',// 加载所有
	pageSize : 30, // 每页显示的记录数
	tranfersDetailUrl:'/manage/gameTransferLog/queryTransferForReport.do',//转账统计记录
	mark:'/manage/gameTransferLog/mark/',
	memGrade:eval('(${fields.memGrade})'),
	gameplat : eval('(${gameplat})'),//游戏平台
	logStatus : eval('(${logStatus})')//记录状态
};


/** 改变页的combo */
gameTransfer.pageSizeCombo = new Share.pageSizeCombo({
			value : '30',
			listeners : {
				select : function(comboBox) {
					gameTransfer.pageSize = parseInt(comboBox.getValue());
					gameTransfer.bbar.pageSize = parseInt(comboBox.getValue());
					gameTransfer.store.baseParams.limit = gameTransfer.pageSize;
					gameTransfer.store.baseParams.start = 0;
					gameTransfer.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
gameTransfer.pageSize = parseInt(gameTransfer.pageSizeCombo.getValue());
/** 基本信息-数据源 */
gameTransfer.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : gameTransfer.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : gameTransfer.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['gtid', 'uuid', 'account','truename','origamount','otherbefore','gamename','togamename','amount','status','balance','otherafter','createDateStr','remark'
			    	]),
			listeners : {
				'load' : function(store, records, options) {
				//	gameTransfer.alwaysFun();
				}
			}
		});
		
/** 基本信息-选择模式 */
gameTransfer.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				
				
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				gameTransfer.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
gameTransfer.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [gameTransfer.selModel, {
						hidden : true,
						header : 'ID',
						dataIndex : 'gtid'
					}, {
						hidden : true,
						header : 'uuid',
						dataIndex : 'uuid'
					}, {
						header : '账号',
						dataIndex : 'account',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '姓名',
						dataIndex : 'truename',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '转账前钱包余额',
						dataIndex : 'origamount',
						width : 120,
						renderer : function(v) {
							return '<span style="color:blue;">'+v+'</span>';
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '转账前第三方余额',
						dataIndex : 'otherbefore',
						width : 120,
						renderer : function(v) {
							return '<span style="color:blue;">'+v+'</span>';
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '转出平台',
						dataIndex : 'gamename',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '转入平台',
						dataIndex : 'togamename',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '转账金额',
						dataIndex : 'amount',
						width : 80,
						renderer : function(v) {
							return '<span style="color:blue;">'+v+'</span>';
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '状态',
						dataIndex : 'status',
						width : 60,
						renderer : function(v) {
							if(v =='0'){
								return '<span style="color:blue;">处理中</span>';
							}else if(v =='1' || v == '9'){
								return '<span style="color:green;">成功</span>';
							}else{
								return '<span style="color:red;">失败</span>';
							}
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '转账后钱包余额',
						dataIndex : 'balance',
						width : 120,
						renderer : function(v) {
							return '<span style="color:blue;">'+v+'</span>';
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '转账后第三方余额',
						dataIndex : 'otherafter',
						width : 120,
						renderer : function(v) {
							return '<span style="color:blue;">'+v+'</span>';
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '操作日期',
						dataIndex : 'createDateStr',
						width : 130,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '描述',
						dataIndex : 'remark',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}]
		});
		
	/**日期条件  -- 开始时间*/
	gameTransfer.startDateField = new Ext.form.DateField({
	  			id:'gameTransferstartDate',
	        	showToday:true,
	        	format:'Y-m-d H:i:s',
	        	invalidText:'日期输入非法',
	        	allowBlank : true,
	        	width:140 
	});
	/** 日期条件  -- 结束时间*/
	gameTransfer.endDateField = new Ext.form.DateField({
	  			id:'gameTransferendDate',
	        	showToday:true,
	        	format:'Y-m-d H:i:s',
	        	invalidText:'日期输入非法',
	        	allowBlank : true,
	        	width:140 
	});
	/** 转入平台*/
	gameTransfer.gameplatAction = new Ext.form.ComboBox({
			hiddenName : 'gameplat',
			id : 'gameplat',
			triggerAction : 'all',
			mode : 'local',
			store : new Ext.data.ArrayStore({
						fields : ['v', 't'],
						data : Share.map2Ary(gameTransfer.gameplat)
					}),
			valueField : 'v',
			displayField : 't',
			value:'',
			allowBlank : false,
			editable : false,
			width:100
	});
	/** 状态*/
	gameTransfer.logStatusAction = new Ext.form.ComboBox({
			hiddenName : 'logStatus',
			id : 'logStatus',
			triggerAction : 'all',
			mode : 'local',
			store : new Ext.data.ArrayStore({
						fields : ['v', 't'],
						data : Share.map2Ary(gameTransfer.logStatus)
					}),
			valueField : 'v',
			displayField : 't',
			value:'',
			allowBlank : false,
			editable : false,
			width:100
	});
	/** 查询按钮 */
	gameTransfer.searchAction = new Ext.Action({
		text : '查询',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				gameTransfer.searchFun();
			}
	});
	
	gameTransfer.markSuccessAction = new Ext.Action({
		text : '标记成功',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				gameTransfer.markSuccessFun();
			}
	});
	
	gameTransfer.markFailAction = new Ext.Action({
		text : '标记失败',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				gameTransfer.markFailFun();
			}
	});
	
	gameTransfer.searchParams = function(){
		var obj = {};
		var account=$("#account").val(); //会员账号
		var startDate=$("#gameTransferstartDate").val(); //开始时间 
		var endDate=$("#gameTransferendDate").val(); //结束时间
		var gameplat=$("#gameplat").prev().val();//订单状态
		var status=$("#logStatus").prev().val();//订单状态
		obj.account = account;
		obj.startDate = startDate;
		obj.endDate = endDate;
		obj.gameplat = gameplat;
		obj.status = status;
	    return obj;
	}
	
	gameTransfer.searchFun = function(){
		gameTransfer.store.load({params: gameTransfer.searchParams()});
	}
	
	gameTransfer.markSuccessFun =function(){
		var record = gameTransfer.grid.getSelectionModel().getSelected();
		if (record.data.status =='1') {
			Ext.Msg.alert('提示', '该记录状态已经操作成功');
			return;
		}
		Ext.Msg.confirm('提示', '确定要标记成功该记录吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : gameTransfer.mark +record.data.gtid + "/1.do",
					callback : function(json) {
						gameTransfer.store.reload();
					}
				});
			}
		});
	}
	
	gameTransfer.markFailFun = function(){
		var record = gameTransfer.grid.getSelectionModel().getSelected();
		if (record.data.status =='2') {
			Ext.Msg.alert('提示', '该记录状态已经操作失败');
			return;
		}
		Ext.Msg.confirm('提示', '确定要标记失败该记录吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : gameTransfer.mark +record.data.gtid +"/2.do",
					callback : function(json) {
						gameTransfer.store.reload();
					}
				});
			}
		});
	}
	
	gameTransfer.store.on('beforeload',function(store, options){
	    gameTransfer.store.baseParams = gameTransfer.searchParams();
	});
	
	// ----------------------统计转账记录 begin--------------------
	/** 统计转账记录按钮 */
	gameTransfer.tranfersAction = new Ext.Action({
		text : '统计平台转账记录 ',
		disabled : false,
		iconCls : 'Coinsadd',
		handler : function() {
			gameTransfer.addtranfersWindow.setIconClass('Coinsadd'); // 设置窗口的样式
			gameTransfer.addtranfersWindow.setTitle('统计平台转账记录'); // 设置窗口的名称
			gameTransfer.addtranfersWindow.show().center(); // 显示窗口
			gameTransfer.tranfersDetail.reload();
		}
	});
	
	/** 统计转账记录数据源 */
	gameTransfer.tranfersDetail = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : false,
		baseParams : {
			start : 0,
			limit : gameTransfer.pageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : gameTransfer.tranfersDetailUrl
		}),
		fields : [ 'togamename','acounts','amounts'],
		listeners : {
			'load' : function(store, records, options) {
			}
		}
	});
	/** 改变页的combo*/
	gameTransfer.tranfersPageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				gameTransfer.pageSize  = parseInt(comboBox.getValue());
				gameTransfer.tranfersbbar.pageSize  = parseInt(comboBox.getValue());
				gameTransfer.tranfersDetail.baseParams.limit = gameTransfer.pageSize;
				gameTransfer.tranfersDetail.baseParams.start = 0;
				gameTransfer.tranfersDetail.load();
			}
		}
	});
	/** 分页 */
	gameTransfer.tranfersbbar = new Ext.PagingToolbar({
		pageSize : gameTransfer.pageSize,
		store : gameTransfer.tranfersDetail,
		displayInfo : true,
		items : [ '-', '&nbsp;', gameTransfer.tranfersPageSizeCombo ]
	});
	/**日期条件  -- 开始时间*/
	gameTransfer.transferReportStartDateField = new Ext.form.DateField({
  			id:'transferreportstartDate',
        	showToday:true,
        	format:'Y-m-d H:i:s',
        	invalidText:'日期输入非法',
        	allowBlank : true,
        	width:150 
	});
	/** 日期条件  -- 结束时间*/
	gameTransfer.transferReportEndDateField = new Ext.form.DateField({
  			id:'transferreportendDate',
        	showToday:true,
        	format:'Y-m-d H:i:s',
        	invalidText:'日期输入非法',
        	allowBlank : true,
        	width:150 
	});
	/** 查询按钮 */
	gameTransfer.transferReportSearchAction = new Ext.Action({
		text : '查询',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				var startDate=$("#transferreportstartDate").val(); //开始时间 
				var endDate=$("#transferreportendDate").val(); //结束时间
				gameTransfer.tranfersDetail.baseParams.startDate = startDate;
				gameTransfer.tranfersDetail.baseParams.endDate = endDate;
				gameTransfer.tranfersDetail.reload();
			}
	});
	/** 顶部工具栏 */
	gameTransfer.reporttbar = [
	                 '操作时间:',gameTransfer.transferReportStartDateField,'~',gameTransfer.transferReportEndDateField,
	                 '-','&nbsp;',gameTransfer.transferReportSearchAction];
	/** 统计转账记录列表 */
	gameTransfer.tranfersgrid = new Ext.grid.EditorGridPanel({
		store : gameTransfer.tranfersDetail,
		autoScroll : 'auto',
		region : 'center',
		tbar:gameTransfer.reporttbar,
		bbar:gameTransfer.tranfersbbar,
		loadMask : true,
		stripeRows : true,
		border: false,
		columns : [
			{
				header : "转入平台",
				dataIndex : 'togamename'
			},{
				header : "转入人数",
				dataIndex : 'acounts'
			},{
				header : "转入金额",
				dataIndex : 'amounts'
			}
		]
	});
	
	/** 统计转账记录窗口 */
	gameTransfer.addtranfersWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 450,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [gameTransfer.tranfersgrid]
	});
	
// ----------------------统计转账记录 end--------------------
	
/** 顶部工具栏 */
gameTransfer.tbar = ['会员账号:',{id:'account',xtype:'textfield',width:80},
	                 '-','&nbsp;','操作时间:',gameTransfer.startDateField,'~',gameTransfer.endDateField,
	                 '-','&nbsp;','转入平台:',gameTransfer.gameplatAction,
	                 '-','&nbsp;','状态:',gameTransfer.logStatusAction,
	                 '-','&nbsp;',gameTransfer.searchAction,
	                 '-','&nbsp;',gameTransfer.markSuccessAction,
	                 '-','&nbsp;',gameTransfer.markFailAction,
	                 '-','&nbsp;',gameTransfer.tranfersAction];
/** 底部工具条 */
gameTransfer.bbar = new Ext.PagingToolbar({
			pageSize : gameTransfer.pageSize,
			store : gameTransfer.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', gameTransfer.pageSizeCombo]
		});
/** 基本信息-表格 */
gameTransfer.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : gameTransfer.store,
			colModel : gameTransfer.colModel,
			selModel : gameTransfer.selModel,
			tbar : gameTransfer.tbar,
			bbar : gameTransfer.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'gameTransferDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});


	
gameTransfer.alwaysFun = function() {
	Share.resetGrid(gameTransfer.grid);
};

gameTransfer.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [gameTransfer.grid]
		});

</script>