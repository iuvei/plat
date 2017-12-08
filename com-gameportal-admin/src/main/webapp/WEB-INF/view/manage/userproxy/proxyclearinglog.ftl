<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.proxyclearinglog"); // 自定义一个命名空间
proxyclearinglog = Ext.market.proxyclearinglog; // 定义命名空间的别名
proxyclearinglog = {
	all : '/proxymanage/m/queryClearinglog.do',// 加载所有
	ximajs : '/proxymanage/m/ximajs.do',
	//save : "/manage/proxyclearinglog/save.do",//保存
	pageSize : 30 // 每页显示的记录数
};


/** 改变页的combo */
proxyclearinglog.pageSizeCombo = new Share.pageSizeCombo({
			value : '30',
			listeners : {
				select : function(comboBox) {
					proxyclearinglog.pageSize = parseInt(comboBox.getValue());
					proxyclearinglog.bbar.pageSize = parseInt(comboBox.getValue());
					proxyclearinglog.store.baseParams.limit = proxyclearinglog.pageSize;
					proxyclearinglog.store.baseParams.start = 0;
					proxyclearinglog.store.load();
				}
			}
		});
		
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
proxyclearinglog.pageSize = parseInt(proxyclearinglog.pageSizeCombo.getValue());
/** 基本信息-数据源 */
proxyclearinglog.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				startDate: "",
				endDate : "",
				start : 0,
				limit : proxyclearinglog.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : proxyclearinglog.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['clearingid', 'uiid', 'account','uname', 'clearingAmount',"clearingScale",'finalamountTotal','validBetAmountTotal','ximaAmount','preferentialTotal','realPL','clearingType','clearingStatus','clearingTime','clearingStartTime','clearingEndTime','clearingRemark','upuser','uptime','upclient']),
			listeners : {
				'load' : function(store, records, options) {
				//	memberGrade.alwaysFun();
				}
			}
		});
		
/** 基本信息-选择模式 */
proxyclearinglog.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				if(isximaFlag == 1){//检查用户是否有洗码权限
					if(record.data.clearingStatus == '4'){
						proxyclearinglog.addAction.enable();
					}else{
						proxyclearinglog.addAction.disable();
					}
				}
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				proxyclearinglog.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
proxyclearinglog.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [proxyclearinglog.selModel, {
						hidden : true,
						header : 'ID',
						dataIndex : 'clearingid'
					},{
						hidden : true,
						header : 'UserID',
						dataIndex : 'uiid'
					}, {
						header : '会员账号',
						dataIndex : 'account',
						width : 100,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '会员姓名',
						dataIndex : 'uname',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '结算金额',
						dataIndex : 'clearingAmount',
						width : 80,
						renderer : function(v) {
							return Share.amount(v);
						}
					},{
						header : '结算比例',
						dataIndex : 'clearingScale',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '盈亏',
						dataIndex : 'finalamountTotal',
						width : 80,
						renderer : function(v) {
							if(Number(v) < 0 ){
								return '<span style="color:red;">'+v+'</span>';
							}else{
								return '<span style="color:blue;">'+v+'</span>';
							}
						}
					},{
						header : '投注总额',
						dataIndex : 'validBetAmountTotal',
						width : 80,
						renderer : function(v) {
							return '<span style="color:blue;">'+v+'</span>';
						}
					},{
						header : '洗码总额',
						dataIndex : 'ximaAmount',
						width : 80,
						renderer : function(v) {
							return '<span style="color:blue;">'+v+'</span>';
						}
					},{
						header : '总优惠',
						dataIndex : 'preferentialTotal',
						width : 80,
						renderer : function(v) {
							return '<span style="color:blue;">'+v+'</span>';
						}
					},{
						header : '实际盈亏',
						dataIndex : 'realPL',
						width : 80,
						renderer : function(v) {
							if(Number(v) < 0 ){
								return '<span style="color:red;">'+v+'</span>';
							}else{
								return '<span style="color:blue;">'+v+'</span>';
							}
						}
					},{
						header : '结算类型',
						dataIndex : 'clearingType',
						width : 80,
						renderer : function(v) {
							if(v == 1){
								return '<span style="color:#992211;">输值结算</span>';
							}else if(v == 2){
								return '<span style="color:#660033;">洗码结算</span>';
							}else if(v == -1){
								return "";
							}else{
								return v;
							}
						}
					},{
						header : '结算日期',
						dataIndex : 'clearingTime',
						width : 130
					},{
						header : '结算开始日期',
						dataIndex : 'clearingStartTime',
						width : 130,
						renderer : function(v) {
							if(v==null || v==""){
								return "";
							}
       						return new Date(v).format('Y-m-d H:i:s');
      					}
					},{
						header : '结算结束日期',
						dataIndex : 'clearingEndTime',
						width : 130,
						renderer : function(v) {
							if(v==null || v==""){
								return "";
							}
       						return new Date(v).format('Y-m-d H:i:s');
      					}
					},{
						header : '备注',
						dataIndex : 'clearingRemark',
						width : 120
					}, {
						header : '状态',
						dataIndex : 'clearingStatus',
						width : 80,
						renderer : function(v) {
							if(v == 1){
								return '<span style="color:green;">已入账</span>';
							}else if(v == 0){
								return '<span style="color:red;">未入账</span>';
							}else if(v == 2){
								return '<span style="color:#CC3333;">记录</span>';
							}else if(v == 3){
								return '<span style="color:#CC3333;">撤除记录</span>';
							}else if(v == 4){
								return '<span style="color:#FF9900;">未洗码</span>';
							}else if(v == 5){
								return '<span style="color:#FF9900;">已洗码</span>';
							}else if(v == 6){
								return '<span style="color:green;">洗码记录</span>';
							}else if(v == -1){
								return "";
							}else{
								return v;
							}
      					}

					},{
						header : '入账操作人',
						dataIndex : 'upuser',
						width : 80
					},{
						header : '入账客户端',
						dataIndex : 'upclient',
						width : 80
					},{
						header : '入账日期',
						dataIndex : 'uptime',
						width : 130,
						renderer : function(v) {
							if(v==null || v==""){
								return "";
							}
       						return new Date(v).format('Y-m-d H:i:s');
      					}
					}]
		});
		
/** 新建 */
proxyclearinglog.addAction = new Ext.Action({
			text : '自助洗码',
			iconCls : 'Coinsadd',
			disabled : true,
			handler : function() {
				proxyclearinglog.selfximaFun();
			}
		});

proxyclearinglog.searchField = new Ext.ux.form.SearchField({
			store : proxyclearinglog.store,
			paramName : 'account',
			emptyText : '请输入代理账号',
			style : 'margin-left: 5px;'
		});
/**开始日期*/
proxyclearinglog.startDatetimeField = new  Ext.form.DateField({
		id:'clearinglogStartDate',
		showToday:true,
		format:'Y-m-d',
		invalidText:'日期输入非法',
		value:new Date().format("Y-m-d"),
		width:120
});

/**结束日期*/
proxyclearinglog.endDatetimeField = new  Ext.form.DateField({
		id:'clearinglogEndDate',
		showToday:true,
		format:'Y-m-d',
		invalidText:'日期输入非法',
		value:new Date().format("Y-m-d"),
		width:120
});

proxyclearinglog.searchActionBtn = new Ext.Action({
	text : '查找',
	iconCls : 'Zoom',
	disabled : false,
	handler : function() {
		var record = proxyclearinglog.grid.getSelectionModel().getSelected();
		proxyclearinglog.store.baseParams.startDate = $("#clearinglogStartDate").val();
		proxyclearinglog.store.baseParams.endDate = $("#clearinglogEndDate").val();
		proxyclearinglog.store.reload();
	}
});
/** 顶部工具栏 */
proxyclearinglog.tbar = [proxyclearinglog.addAction,'&nbsp;','-','查询日期：',proxyclearinglog.startDatetimeField,'&nbsp;','-',proxyclearinglog.endDatetimeField,'&nbsp;','-',proxyclearinglog.searchActionBtn];
/** 底部工具条 */
proxyclearinglog.bbar = new Ext.PagingToolbar({
			pageSize : proxyclearinglog.pageSize,
			store : proxyclearinglog.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', proxyclearinglog.pageSizeCombo]
		});
/** 基本信息-表格 */
proxyclearinglog.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : proxyclearinglog.store,
			colModel : proxyclearinglog.colModel,
			selModel : proxyclearinglog.selModel,
			tbar : proxyclearinglog.tbar,
			bbar : proxyclearinglog.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'memberGradeDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});
			
proxyclearinglog.returnscaleNumberField = new Ext.form.NumberField({
		maxLength : 10,
		allowBlank : false,
		decimalPrecision : 10,
		readOnly:true,
		fieldLabel : '结算金额',
		name : 'clearingAmount',
		anchor : '99%'
	});

proxyclearinglog.ximascaleNumberField = new Ext.form.NumberField({
		maxLength : 10,
		allowBlank : false,
		decimalPrecision : 10,
		fieldLabel : '入账金额',
		name : 'rAmount',
		anchor : '99%'
	});
	
/** 基本信息-详细信息的form */
proxyclearinglog.formPanel = new Ext.form.FormPanel({
			autoScroll : false,
			border: false,
        	style: 'border-bottom:0px;',
        	bodyStyle: 'padding:10px;background-color:transparent;',
			labelwidth : 50,
			defaultType : 'textfield',
			items : [{
						xtype : 'hidden',
						fieldLabel : 'ID',
						name : 'clearingid',
						anchor : '99%'
					},{
						xtype : 'hidden',
						fieldLabel : 'UserID',
						name : 'uiid',
						anchor : '99%'
					},proxyclearinglog.returnscaleNumberField,proxyclearinglog.ximascaleNumberField,{
						xtype : 'textarea',
						fieldLabel : '入账备注',
						maxLength : 100,
						allowBlank : false,
						name : 'rRemark',
						anchor : '99%'
					}]
		});

		

			
/** 编辑新建窗口 */
proxyclearinglog.addWindow = new Ext.Window({
			layout : 'fit',
			width : 399,
			height : 205,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [proxyclearinglog.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							proxyclearinglog.saveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = proxyclearinglog.formPanel.getForm();
							var id = form.findField("pyid").getValue();
							form.reset();
							if (id != '')
								form.findField("pyid").setValue(id);
						}
					}]
		});
	
proxyclearinglog.alwaysFun = function() {
	Share.resetGrid(proxyclearinglog.grid);
	proxyclearinglog.addAction.disable();
};
proxyclearinglog.saveFun = function() {
	var form = proxyclearinglog.formPanel.getForm();
	
	if(Number(form.findField("rAmount").getValue()) > Number(form.findField("clearingAmount").getValue())){
		Ext.Msg.alert("提示", "入账金额不能大于可入账的金额!");
		return;
	}
	if(!form.isValid()){
 		return;
 	}
	// 发送请求
	Share.AjaxRequest({
				url : proxyclearinglog.save,
				params : form.getValues(),
				callback : function(json) {
				if (json.success==false){
				     Ext.MessageBox.alert('Status', json.msg, showResult);
					return;
				}else{
				    proxyclearinglog.addWindow.hide();
					proxyclearinglog.alwaysFun();
					proxyclearinglog.store.reload();
				}
					
				}
			});
};

proxyclearinglog.delFun = function() {
	var record = proxyclearinglog.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '您确定要删除选中代理占成数据吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : proxyclearinglog.del + record.data.pyid +".do",
								callback : function(json) {
									proxyclearinglog.alwaysFun();
									proxyclearinglog.store.reload();
								}
							});
				}
			});
};

proxyclearinglog.selfximaFun = function() {
	var record = proxyclearinglog.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '自助洗码后会将您的洗码金额分配到下线会员，确定要自助洗码吗?', function(btn, text) {
		if(btn == 'yes'){
			var record = proxyclearinglog.grid.getSelectionModel().getSelected();
			var param={clearingid:record.data.clearingid,jsdate:record.data.clearingStartTime};
			// 发送请求
			Share.AjaxRequest({
						url : proxyclearinglog.ximajs,
						params : param,
						callback : function(json) {
							proxyclearinglog.alwaysFun();
							proxyclearinglog.store.reload();
						}
					});
		}
		
	});
}

proxyclearinglog.bindFun = function(s){
	var record = proxyclearinglog.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '确定要修改修改占成数据状态吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : proxyclearinglog.bind + record.data.pyid+"/"+s+".do",
								callback : function(json) {
									proxyclearinglog.alwaysFun();
									proxyclearinglog.store.reload();
								}
							});
				}
			});
}
  
proxyclearinglog.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [proxyclearinglog.grid]
		});

</script>