<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	Ext.ns("Ext.market.totalreportform"); // 自定义一个命名空间
	totalreportform = Ext.market.totalreportform; // 定义命名空间的别名
	totalreportform = {
		all : '/manage/reportFormTotal/queryPlatformReport.do',
		regreport : '/manage/reportFormTotal/queryRegReport.do', // 注册报表
		betreport : '/manage/reportFormTotal/queryBetReport.do',//投注报表
		payreport : '/manage/reportFormTotal/queryPayReport.do',//存款报表
		tikuanreport : '/manage/reportFormTotal/queryWithdrawalReport.do',//存款报表
		favorablereport : '/manage/reportFormTotal/queryFavorableReport.do',//优惠报表
		ximareport : '/manage/reportFormTotal/queryXimaReport.do',//洗码报表
		hymember : '/manage/reportFormTotal/queryHymemberReport.do',//活跃会员
		ti : "/",//踢出会员
		pageSize : 30// 每页显示的记录数
	};
	/** 改变页的combo*/
	totalreportform.pageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				totalreportform.pageSize  = parseInt(comboBox.getValue());
				totalreportform.bbar.pageSize  = parseInt(comboBox.getValue());
				totalreportform.store.baseParams.limit = totalreportform.pageSize;
				totalreportform.store.baseParams.start = 0;
				totalreportform.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	totalreportform.pageSize = parseInt(totalreportform.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	totalreportform.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:true,
		baseParams : {
			start : 0,
			limit : totalreportform.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : totalreportform.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'totalmoney','create_date']),
		listeners : {
			'load' : function(store, records, options) {
				totalreportform.alwaysFun();
			}
		}
	});
	//totalreportform.store.load(); 
	/** 基本信息-选择模式 */
	totalreportform.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
		
			}
		}
	});
	/** 基本信息-数据列 */
	totalreportform.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 110
		},
		//'ccid','ccholder','bankname','ccno','bankaddr','remarks','status','createuserid','createusername','createtime','updateuserid','updateusername','updatetime'
		columns : [ totalreportform.selModel, {
			header : '平台时间',
			dataIndex : 'create_date',
			width:150,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			}
		},{
			header : '平台总余额',
			dataIndex : 'totalmoney',
			width : 150
		}]
	});

	/** 注册人数 */
	totalreportform.regReportFormAction = new Ext.Action({
		text : '注册人数',
		iconCls : 'Chartbar',
		disabled : false,
		handler : function() {
			totalreportform.regReportFormWindow.setIconClass('Chartbar'); // 设置窗口的样式
			totalreportform.regReportFormWindow.setTitle('注册人数统计'); // 设置窗口的名称
			totalreportform.regReportFormWindow.show(this).center(); // 显示窗口
		}
	});
	
	/**注单统计*/
	totalreportform.betReportFormAction = new Ext.Action({
		text : '投注统计',
		iconCls : 'Chartbar',
		disabled : false,
		handler : function() {
			totalreportform.betFormWindow.setIconClass('Chartbar'); // 设置窗口的样式
			totalreportform.betFormWindow.setTitle('投注金额峰值信息'); // 设置窗口的名称
			totalreportform.betFormWindow.show(this).center(); // 显示窗口
		}
	});
	
	/**充值统计*/
	totalreportform.payReportFormAction = new Ext.Action({
		text : '存款统计',
		iconCls : 'Chartbar',
		disabled : false,
		handler : function() {
			totalreportform.payFormWindow.setIconClass('Chartbar'); // 设置窗口的样式
			totalreportform.payFormWindow.setTitle('存款金额峰值信息'); // 设置窗口的名称
			totalreportform.payFormWindow.show(this).center(); // 显示窗口
		}
	});
	
	
	
	/**提款统计*/
	totalreportform.tikuanReportFormAction = new Ext.Action({
		text : '提款统计',
		iconCls : 'Chartbar',
		disabled : false,
		handler : function() {
			totalreportform.tikuanFormWindow.setIconClass('Chartbar'); // 设置窗口的样式
			totalreportform.tikuanFormWindow.setTitle('提款金额峰值信息'); // 设置窗口的名称
			totalreportform.tikuanFormWindow.show(this).center(); // 显示窗口
		}
	});
	
	/**优惠金额统计*/
	totalreportform.favorableReportFormAction = new Ext.Action({
		text : '优惠金额统计',
		iconCls : 'Chartbar',
		disabled : false,
		handler : function() {
			totalreportform.favorabFormWindow.setIconClass('Chartbar'); // 设置窗口的样式
			totalreportform.favorabFormWindow.setTitle('优惠金额峰值信息'); // 设置窗口的名称
			totalreportform.favorabFormWindow.show(this).center(); // 显示窗口
		}
	});
	
	/**洗码金额统计*/
	totalreportform.ximaReportFormAction = new Ext.Action({
		text : '洗码金额统计',
		iconCls : 'Chartbar',
		disabled : false,
		handler : function() {
			totalreportform.ximaFormWindow.setIconClass('Chartbar'); // 设置窗口的样式
			totalreportform.ximaFormWindow.setTitle('洗码金额峰值信息'); // 设置窗口的名称
			totalreportform.ximaFormWindow.show(this).center(); // 显示窗口
		}
	});
	
	/**活跃会员*/
	totalreportform.hymembrtReportFormAction = new Ext.Action({
		text : '活跃会员',
		iconCls : 'Chartbar',
		disabled : false,
		handler : function() {
			totalreportform.hymemberFormWindow.setIconClass('Chartbar'); // 设置窗口的样式
			totalreportform.hymemberFormWindow.setTitle('活跃会员峰值信息'); // 设置窗口的名称
			totalreportform.hymemberFormWindow.show(this).center(); // 显示窗口
		}
	});
	
	/**
	 * 注册统计搜索按钮
	 */
	totalreportform.searchRegBtn = new Ext.Button({
		text : '查询',
		iconCls : 'Zoom',
		handler : function() {
			totalreportform.loadReg();
		}
	});
	
	/**
	 * 投注统计搜索按钮
	 */
	totalreportform.searchBetBtn = new Ext.Button({
		text : '查询',
		iconCls : 'Zoom',
		handler : function() {
			totalreportform.loadBet();
		}
	});
	
	/**
	 * 存款统计搜索按钮
	 */
	totalreportform.searchPayBtn = new Ext.Button({
		text : '查询',
		iconCls : 'Zoom',
		handler : function() {
			totalreportform.loadPay();
		}
	});
	
	/**
	 * 存款统计搜索按钮
	 */
	totalreportform.searchTikuanBtn = new Ext.Button({
		text : '查询',
		iconCls : 'Zoom',
		handler : function() {
			totalreportform.loadTikuan();
		}
	});
	
	/**
	 * 优惠统计搜索按钮
	 */
	totalreportform.searchFavorabBtn = new Ext.Button({
		text : '查询',
		iconCls : 'Zoom',
		handler : function() {
			totalreportform.loadFavorab();
		}
	});
	
	/**
	 * 洗码统计搜索按钮
	 */
	totalreportform.searchXimaBtn = new Ext.Button({
		text : '查询',
		iconCls : 'Zoom',
		handler : function() {
			totalreportform.loadXima();
		}
	});
	
	/**
	 * 活跃会员搜索按钮
	 */
	totalreportform.searchHymemberBtn = new Ext.Button({
		text : '查询',
		iconCls : 'Zoom',
		handler : function() {
			totalreportform.loadHymember();
		}
	});
	//注册统计搜索
	totalreportform.reportRegStartDateField = new Ext.form.DateField({
		id:'reportRegStartDate',
        showToday:true,
        format:'Y-m-d',
        invalidText:'日期输入非法',
        allowBlank : false,
        width:120
	});
	
	totalreportform.reportRegEndDateField = new Ext.form.DateField({
		id:'reportRegEndDate',
        showToday:true,
        format:'Y-m-d',
        invalidText:'日期输入非法',
        allowBlank : false,
        width:120 
	});
	
	//投注统计搜索
	totalreportform.reportBetStartDateField = new Ext.form.DateField({
		id:'reportBetStartDate',
        showToday:true,
        format:'Y-m-d',
        invalidText:'日期输入非法',
        allowBlank : false,
        width:120
	});
	
	totalreportform.reportBetEndDateField = new Ext.form.DateField({
		id:'reportBetEndDate',
        showToday:true,
        format:'Y-m-d',
        invalidText:'日期输入非法',
        allowBlank : false,
        width:120 
	});
	
	//存款统计搜索
	totalreportform.reportPayStartDateField = new Ext.form.DateField({
		id:'reportPayStartDate',
        showToday:true,
        format:'Y-m-d',
        invalidText:'日期输入非法',
        allowBlank : false,
        width:120
	});
	
	totalreportform.reportPayEndDateField = new Ext.form.DateField({
		id:'reportPayEndDate',
        showToday:true,
        format:'Y-m-d',
        invalidText:'日期输入非法',
        allowBlank : false,
        width:120 
	});
	
	//提款统计搜索
	totalreportform.reportTikuanStartDateField = new Ext.form.DateField({
		id:'reportTikuanStartDate',
        showToday:true,
        format:'Y-m-d',
        invalidText:'日期输入非法',
        allowBlank : false,
        width:120
	});
	
	totalreportform.reportTikuanEndDateField = new Ext.form.DateField({
		id:'reportTikuanEndDate',
        showToday:true,
        format:'Y-m-d',
        invalidText:'日期输入非法',
        allowBlank : false,
        width:120 
	});
	
	//优惠统计搜索
	totalreportform.reportFavorabStartDateField = new Ext.form.DateField({
		id:'reportFavorabStartDate',
        showToday:true,
        format:'Y-m-d',
        invalidText:'日期输入非法',
        allowBlank : false,
        width:120
	});
	
	totalreportform.reportFavorabEndDateField = new Ext.form.DateField({
		id:'reportFavorabEndDate',
        showToday:true,
        format:'Y-m-d',
        invalidText:'日期输入非法',
        allowBlank : false,
        width:120 
	});
	
	//洗码统计搜索
	totalreportform.reportXimaStartDateField = new Ext.form.DateField({
		id:'reportXimaStartDate',
        showToday:true,
        format:'Y-m-d',
        invalidText:'日期输入非法',
        allowBlank : false,
        width:120
	});
	
	totalreportform.reportXimaEndDateField = new Ext.form.DateField({
		id:'reportXimaEndDate',
        showToday:true,
        format:'Y-m-d',
        invalidText:'日期输入非法',
        allowBlank : false,
        width:120 
	});
	
	//活跃会员
	totalreportform.reportHymemberStartDateField = new Ext.form.DateField({
		id:'reportHymemberStartDate',
        showToday:true,
        format:'Y-m-d',
        invalidText:'日期输入非法',
        allowBlank : false,
        width:120
	});
	
	totalreportform.reportHymemberEndDateField = new Ext.form.DateField({
		id:'reportHymemberEndDate',
        showToday:true,
        format:'Y-m-d',
        invalidText:'日期输入非法',
        allowBlank : false,
        width:120 
	});
	
	/**注册搜索工具条*/
	totalreportform.searchRegTbar = [
	'查询日期：',totalreportform.reportRegStartDateField,'&nbsp;',
	'至','&nbsp;',totalreportform.reportRegEndDateField,'&nbsp;','-',
	totalreportform.searchRegBtn
	];
	
	/**投注统计搜索工具条*/
	totalreportform.searchBetTbar = [
	'查询日期：',totalreportform.reportBetStartDateField,'&nbsp;',
	'至','&nbsp;',totalreportform.reportBetEndDateField,'&nbsp;','-',
	totalreportform.searchBetBtn
	];
	
	/**存款统计搜索工具条*/
	totalreportform.searchPayTbar = [
	'查询日期：',totalreportform.reportPayStartDateField,'&nbsp;',
	'至','&nbsp;',totalreportform.reportPayEndDateField,'&nbsp;','-',
	totalreportform.searchPayBtn
	];
	
	/**提款统计搜索工具条*/
	totalreportform.searchTikuanTbar = [
	'查询日期：',totalreportform.reportTikuanStartDateField,'&nbsp;',
	'至','&nbsp;',totalreportform.reportTikuanEndDateField,'&nbsp;','-',
	totalreportform.searchTikuanBtn
	];
	
	/**优惠统计搜索工具条*/
	totalreportform.searchFavorabTbar = [
	'查询日期：',totalreportform.reportFavorabStartDateField,'&nbsp;',
	'至','&nbsp;',totalreportform.reportFavorabEndDateField,'&nbsp;','-',
	totalreportform.searchFavorabBtn
	];
	
	/**洗码统计搜索工具条*/
	totalreportform.searchXimaTbar = [
	'查询日期：',totalreportform.reportXimaStartDateField,'&nbsp;',
	'至','&nbsp;',totalreportform.reportXimaEndDateField,'&nbsp;','-',
	totalreportform.searchXimaBtn
	];
	
	/**活跃会员统计搜索工具条*/
	totalreportform.searchHymemberTbar = [
	'查询日期：',totalreportform.reportHymemberStartDateField,'&nbsp;',
	'至','&nbsp;',totalreportform.reportHymemberEndDateField,'&nbsp;','-',
	totalreportform.searchHymemberBtn
	];
	
	/** 顶部工具栏 */
	totalreportform.tbar = [ totalreportform.regReportFormAction,'&nbsp;',totalreportform.betReportFormAction,'&nbsp;',
	totalreportform.payReportFormAction,'&nbsp;',
	totalreportform.tikuanReportFormAction,'&nbsp;',
	totalreportform.favorableReportFormAction,'&nbsp;',
	totalreportform.ximaReportFormAction,'&nbsp;',
	totalreportform.hymembrtReportFormAction
	];
	/** 底部工具条 */
	totalreportform.bbar = new Ext.PagingToolbar({
		pageSize : totalreportform.pageSize,
		store : totalreportform.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', totalreportform.pageSizeCombo ]
	});
	/** 基本信息-表格 */
	totalreportform.grid = new Ext.grid.EditorGridPanel({
		store : totalreportform.store,
		colModel : totalreportform.colModel,
		selModel : totalreportform.selModel,
		tbar : totalreportform.tbar,
		bbar : totalreportform.bbar,
		autoScroll : 'auto',
		region : 'center',
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig:{},
		stripeRows : true
	});
	
	totalreportform.alwaysFun = function() {
		Share.resetGrid(totalreportform.grid);
	};
	
	/** 注册人数报表窗口 */
	totalreportform.regReportFormWindow = new Ext.Window({
		layout : 'fit',
		width : 900,
		height : 520,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		tbar:totalreportform.searchRegTbar,
		html:'<div id="flashContentsReg"></div>',
		buttons : [ {
			text : '关闭',
			handler : function() {
				totalreportform.regReportFormWindow.hide();
			}
		} ]
	});
	
	/**
	 * 注册统计默认加载
	 */
	totalreportform.regReportFormWindow.on('show',function(){
		totalreportform.loadReg();
	});
	
	/**
	 * 加载注册统计数据
	 */
	totalreportform.loadReg = function(){
		var obj = {};
		var starttime = $("#reportRegStartDate").val();
		if(starttime != null && starttime !=''){
			obj.starttime = starttime;
		}
		var endtime = $("#reportRegEndDate").val();
		if(endtime != null && endtime !=''){
			obj.endtime = endtime;
		}
		// 发送请求
		Share.AjaxRequest({
			url : totalreportform.regreport,
			params : obj,
			callback : function(json) {
				window.console.log("json>>"+json);
				window.console.log("json.data>>"+json.data);
				window.console.log("json.totalCount>>"+json.totalCount);
				var str = new StringBuffer();
				str.append('<chart>');
				str.append('<series>');
				$.each(json.data,function(n,value){
					str.append('<value xid="'+n+'">'+value.time+'</value>');
				});
				str.append('</series>');
				str.append('<graphs>');
				str.append('<graph title="'+json.starttime+'到'+json.endtime+'注册人数信息-注册总人数：'+json.totalCount+'人" fill_alpha="60" line_width="2" bullet="round" color="#FF80C0">');
				$.each(json.data,function(n,value){
					str.append('<value xid="'+n+'">'+value.count+'</value>');
				});
				str.append('</graph>');
				str.append('</graphs>');
				str.append('</chart>');
				var so = new SWFObject("/js/reportform/amline/amline.swf", "amline", "100%", "430", "8", "#FFFFFF");
				so.addVariable("path", "/js/reportform/amline/");
		        so.addVariable("settings_file", encodeURIComponent("/js/reportform/xml/register_settings.xml"));
				so.addVariable("chart_data", encodeURIComponent(str));
				so.write("flashContentsReg");
			}
		});
	}
	
	
	/** 投注统计报表窗口 */
	totalreportform.betFormWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 590,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		tbar:totalreportform.searchBetTbar,
		html:'<div id="flashContentsBet"></div><br /><div id="bettotalContents"></div>',
		buttons : [ {
			text : '关闭',
			handler : function() {
				totalreportform.betFormWindow.hide();
			}
		} ]
	});
	
	totalreportform.betFormWindow.on('show',function(){
		totalreportform.loadBet();
	});
	
	/**
	 * 加载投注统计数据
	 */
	totalreportform.loadBet = function(){
		var obj = {};
		var starttime = $("#reportBetStartDate").val();
		if(starttime != null && starttime !=''){
			obj.starttime = starttime;
		}
		var endtime = $("#reportBetEndDate").val();
		if(endtime != null && endtime !=''){
			obj.endtime = endtime;
		}
		// 发送请求
		Share.AjaxRequest({
			url : totalreportform.betreport,
			params : obj,
			callback : function(json) {
				var bet = new StringBuffer();
				bet.append('<chart>');
				bet.append('<series>');
				$.each(json.data,function(n,value){
					bet.append('<value xid="'+n+'">'+value.time+'</value>');
				});
				bet.append('</series>');
				bet.append('<graphs>');
				bet.append('<graph title="投注峰值信息" fill_alpha="60" line_width="2" bullet="round" color="#FF6600">');
				$.each(json.data,function(n,value){
					bet.append('<value xid="'+n+'" url="javascript:;" bullet="1" color="'+value.color+'">'+value.betcount+'</value>');
				});
				bet.append('</graph>');
				bet.append('</graphs>');
				bet.append('</chart>');
				var betso = new SWFObject("/js/reportform/amcolumn/amcolumn.swf", "amcolumn", "100%", "450", "8", "#EEF2FB");
				betso.addVariable("path", "/js/reportform/amcolumn/");
		        betso.addVariable("settings_file", encodeURIComponent("/js/reportform/xml/bet_amcolumn_settings2.xml"));
				betso.addVariable("chart_data", encodeURIComponent(bet));
				betso.write("flashContentsBet");
				$("#bettotalContents").html(json.starttime+'到'+json.endtime+'总投注金额：'+json.totalBet+'元');
			}
		});	
	}
	
	/** 存款统计报表窗口 */
	totalreportform.payFormWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 590,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		tbar:totalreportform.searchPayTbar,
		html:'<div id="flashContentsPay"></div><br /><div id="paytotalContents"></div>',
		buttons : [ {
			text : '关闭',
			handler : function() {
				totalreportform.payFormWindow.hide();
			}
		} ]
	});
	
	totalreportform.payFormWindow.on('show',function(){
		totalreportform.loadPay();
	});
	
	/**
	 * 加载存款数据
	 */
	totalreportform.loadPay = function(){
		var obj = {};
		var starttime = $("#reportPayStartDate").val();
		if(starttime != null && starttime !=''){
			obj.starttime = starttime;
		}
		var endtime = $("#reportPayEndDate").val();
		if(endtime != null && endtime !=''){
			obj.endtime = endtime;
		}
		
		// 发送请求
		Share.AjaxRequest({
			url : totalreportform.payreport,
			params : obj,
			callback : function(json) {
				var pay = new StringBuffer();
				pay.append('<chart>');
				pay.append('<series>');
				$.each(json.data,function(n,value){
					pay.append('<value xid="'+n+'">'+value.time+'</value>');
				});
				pay.append('</series>');
				pay.append('<graphs>');
				pay.append('<graph title="存款峰值信息" fill_alpha="60" line_width="2" bullet="round" color="#FF6600">');
				$.each(json.data,function(n,value){
					pay.append('<value xid="'+n+'" url="javascript:;" bullet="1" color="'+value.color+'">'+value.paysum+'</value>');
				});
				pay.append('</graph>');
				pay.append('</graphs>');
				pay.append('</chart>');
				var payso = new SWFObject("/js/reportform/amcolumn/amcolumn.swf", "amcolumn", "100%", "450", "8", "#EEF2FB");
				payso.addVariable("path", "/js/reportform/amcolumn/");
		        payso.addVariable("settings_file", encodeURIComponent("/js/reportform/xml/bet_amcolumn_settings2.xml"));
				payso.addVariable("chart_data", encodeURIComponent(pay));
				payso.write("flashContentsPay");
				$("#paytotalContents").html(json.starttime+'到'+json.endtime+'总存款金额：'+json.totalPay+'元');
			}
		});	
	}
	
	/** 提款统计报表窗口 */
	totalreportform.tikuanFormWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 590,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		tbar:totalreportform.searchTikuanTbar,
		html:'<div id="flashContentsTikuan"></div><br /><div id="TikuantotalContents"></div>',
		buttons : [ {
			text : '关闭',
			handler : function() {
				totalreportform.tikuanFormWindow.hide();
			}
		} ]
	});
	
	totalreportform.tikuanFormWindow.on('show',function(){
		totalreportform.loadTikuan();
	});
	
	/**
	 * 加载提款数据
	 */
	totalreportform.loadTikuan = function(){
		var obj = {};
		var starttime = $("#reportTikuanStartDate").val();
		if(starttime != null && starttime !=''){
			obj.starttime = starttime;
		}
		var endtime = $("#reportTikuanEndDate").val();
		if(endtime != null && endtime !=''){
			obj.endtime = endtime;
		}
		
		// 发送请求
		Share.AjaxRequest({
			url : totalreportform.tikuanreport,
			params : obj,
			callback : function(json) {
				var tikuan = new StringBuffer();
				tikuan.append('<chart>');
				tikuan.append('<series>');
				$.each(json.data,function(n,value){
					tikuan.append('<value xid="'+n+'">'+value.time+'</value>');
				});
				tikuan.append('</series>');
				tikuan.append('<graphs>');
				tikuan.append('<graph title="提款峰值信息" fill_alpha="60" line_width="2" bullet="round" color="#FF6600">');
				$.each(json.data,function(n,value){
					tikuan.append('<value xid="'+n+'" url="javascript:;" bullet="1" color="'+value.color+'">'+value.withdrawalsum+'</value>');
				});
				tikuan.append('</graph>');
				tikuan.append('</graphs>');
				tikuan.append('</chart>');
				var tikuanso = new SWFObject("/js/reportform/amcolumn/amcolumn.swf", "amcolumn", "100%", "450", "8", "#EEF2FB");
				tikuanso.addVariable("path", "/js/reportform/amcolumn/");
		        tikuanso.addVariable("settings_file", encodeURIComponent("/js/reportform/xml/bet_amcolumn_settings2.xml"));
				tikuanso.addVariable("chart_data", encodeURIComponent(tikuan));
				tikuanso.write("flashContentsTikuan");
				$("#TikuantotalContents").html(json.starttime+'到'+json.endtime+'总提款金额：'+json.totalTikuan+'元');
			}
		});	
	}
	
	/** 优惠统计报表窗口 */
	totalreportform.favorabFormWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 590,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		tbar:totalreportform.searchFavorabTbar,
		html:'<div id="flashContentsFavorab"></div><br /><div id="FavorabtotalContents"></div>',
		buttons : [ {
			text : '关闭',
			handler : function() {
				totalreportform.favorabFormWindow.hide();
			}
		} ]
	});
	
	totalreportform.favorabFormWindow.on('show',function(){
		totalreportform.loadFavorab();
	});
	
	/**
	 * 加载优惠数据
	 */
	totalreportform.loadFavorab = function(){
		var obj = {};
		var starttime = $("#reportFavorabStartDate").val();
		if(starttime != null && starttime !=''){
			obj.starttime = starttime;
		}
		var endtime = $("#reportFavorabEndDate").val();
		if(endtime != null && endtime !=''){
			obj.endtime = endtime;
		}
		
		// 发送请求
		Share.AjaxRequest({
			url : totalreportform.favorablereport,
			params : obj,
			callback : function(json) {
				var favorab = new StringBuffer();
				favorab.append('<chart>');
				favorab.append('<series>');
				$.each(json.data,function(n,value){
					favorab.append('<value xid="'+n+'">'+value.time+'</value>');
				});
				favorab.append('</series>');
				favorab.append('<graphs>');
				favorab.append('<graph title="优惠金额峰值信息" fill_alpha="60" line_width="2" bullet="round" color="#FF6600">');
				$.each(json.data,function(n,value){
					favorab.append('<value xid="'+n+'" url="javascript:;" bullet="1" color="'+value.color+'">'+value.favorablesum+'</value>');
				});
				favorab.append('</graph>');
				favorab.append('</graphs>');
				favorab.append('</chart>');
				var favorabso = new SWFObject("/js/reportform/amcolumn/amcolumn.swf", "amcolumn", "100%", "450", "8", "#EEF2FB");
				favorabso.addVariable("path", "/js/reportform/amcolumn/");
		        favorabso.addVariable("settings_file", encodeURIComponent("/js/reportform/xml/bet_amcolumn_settings2.xml"));
				favorabso.addVariable("chart_data", encodeURIComponent(favorab));
				favorabso.write("flashContentsFavorab");
				$("#FavorabtotalContents").html(json.starttime+'到'+json.endtime+'总优惠金额：'+json.totalFavorab+'元');
			}
		});	
	}
	
	/** 优惠统计报表窗口 */
	totalreportform.ximaFormWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 590,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		tbar:totalreportform.searchXimaTbar,
		html:'<div id="flashContentsXima"></div><br /><div id="XimatotalContents"></div>',
		buttons : [ {
			text : '关闭',
			handler : function() {
				totalreportform.ximaFormWindow.hide();
			}
		} ]
	});
	
	totalreportform.ximaFormWindow.on('show',function(){
		totalreportform.loadXima();
	});
	
	/**
	 * 加载洗码数据
	 */
	totalreportform.loadXima = function(){
		var obj = {};
		var starttime = $("#reportXimaStartDate").val();
		if(starttime != null && starttime !=''){
			obj.starttime = starttime;
		}
		var endtime = $("#reportXimaEndDate").val();
		if(endtime != null && endtime !=''){
			obj.endtime = endtime;
		}
		
		// 发送请求
		Share.AjaxRequest({
			url : totalreportform.ximareport,
			params : obj,
			callback : function(json) {
				var xima = new StringBuffer();
				xima.append('<chart>');
				xima.append('<series>');
				$.each(json.data,function(n,value){
					xima.append('<value xid="'+n+'">'+value.time+'</value>');
				});
				xima.append('</series>');
				xima.append('<graphs>');
				xima.append('<graph title="优惠金额峰值信息" fill_alpha="60" line_width="2" bullet="round" color="#FF6600">');
				$.each(json.data,function(n,value){
					xima.append('<value xid="'+n+'" url="javascript:;" bullet="1" color="'+value.color+'">'+value.ximasum+'</value>');
				});
				xima.append('</graph>');
				xima.append('</graphs>');
				xima.append('</chart>');
				var ximaso = new SWFObject("/js/reportform/amcolumn/amcolumn.swf", "amcolumn", "100%", "450", "8", "#EEF2FB");
				ximaso.addVariable("path", "/js/reportform/amcolumn/");
		        ximaso.addVariable("settings_file", encodeURIComponent("/js/reportform/xml/bet_amcolumn_settings2.xml"));
				ximaso.addVariable("chart_data", encodeURIComponent(xima));
				ximaso.write("flashContentsXima");
				$("#XimatotalContents").html(json.starttime+'到'+json.endtime+'总洗码金额：'+json.totalXima+'元');
			}
		});	
	}
	
	/** 活跃会员统计窗口 */
	totalreportform.hymemberFormWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 550,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		tbar:totalreportform.searchHymemberTbar,
		html:'<div id="flashContentsHyMember"></div>',
		buttons : [ {
			text : '关闭',
			handler : function() {
				totalreportform.hymemberFormWindow.hide();
			}
		} ]
	});
	
	totalreportform.hymemberFormWindow.on('show',function(){
		totalreportform.loadHymember();
	});
	
	totalreportform.loadHymember = function(){
		var obj = {};
		var starttime = $("#reportHymemberStartDate").val();
		if(starttime != null && starttime !=''){
			obj.starttime = starttime;
		}
		var endtime = $("#reportHymemberEndDate").val();
		if(endtime != null && endtime !=''){
			obj.endtime = endtime;
		}
		Share.AjaxRequest({
			url : totalreportform.hymember,
			params : obj,
			callback : function(text) {
				var hymemberso = new SWFObject("/js/reportform/amline/amline.swf", "amline", "100%", "450", "8", "#EEF2FB");
				hymemberso.addVariable("path", "/js/reportform/amline/");
		        hymemberso.addVariable("settings_file", encodeURIComponent("/js/reportform/xml/online_settings.xml"));
				hymemberso.addVariable("chart_data", encodeURIComponent(text));
				hymemberso.write("flashContentsHyMember");
			}
		});
	}

	totalreportform.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'border',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
		items : [ totalreportform.grid ]
	});
</script>
