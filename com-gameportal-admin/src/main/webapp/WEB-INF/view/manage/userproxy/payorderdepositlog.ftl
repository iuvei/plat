<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.proxyDepositPayOrder"); // 自定义一个命名空间
proxyDepositPayOrder = Ext.market.proxyDepositPayOrder; // 定义命名空间的别名
proxyDepositPayOrder = {
	all : '/proxymanage/m/queryDepositPayOrderlog.do',// 加载所有
	pageSize : 20, // 每页显示的记录数
	paymethods:eval('(${paymethodsMap})'),
	memGrade:eval('(${fields.memGrade})')
};


/** 改变页的combo */
proxyDepositPayOrder.pageSizeCombo = new Share.pageSizeCombo({
			value : '20',
			listeners : {
				select : function(comboBox) {
					proxyDepositPayOrder.pageSize = parseInt(comboBox.getValue());
					proxyDepositPayOrder.bbar.pageSize = parseInt(comboBox.getValue());
					proxyDepositPayOrder.store.baseParams.limit = proxyDepositPayOrder.pageSize;
					proxyDepositPayOrder.store.baseParams.start = 0;
					proxyDepositPayOrder.store.load();
				} 
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
proxyDepositPayOrder.pageSize = parseInt(proxyDepositPayOrder.pageSizeCombo.getValue());
/** 基本信息-数据源 */
proxyDepositPayOrder.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				startDate: "",
				endDate : "",
				uaccount : "",
				start : 0,
				limit : proxyDepositPayOrder.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : proxyDepositPayOrder.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['uiid', 'uaccount', 'urealname', 'amount',"paytyple",'paymethods','deposittime','status','cwremarks']),
			listeners : {
				'load' : function(store, records, options) {
					proxyDepositPayOrder.amountsum();
				}
			}
		});
		
/** 基本信息-选择模式 */
proxyDepositPayOrder.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				proxyDepositPayOrder.deleteAction.enable();
				proxyDepositPayOrder.editAction.enable();
				if(record.data.status == '0'){
					proxyDepositPayOrder.removeAction.enable();
					proxyDepositPayOrder.bindAction.disable();
				}else{
					proxyDepositPayOrder.bindAction.enable();
					proxyDepositPayOrder.removeAction.disable();
				}
				
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				proxyDepositPayOrder.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
proxyDepositPayOrder.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [proxyDepositPayOrder.selModel, {
						hidden : true,
						header : 'ID',
						dataIndex : 'uiid'
					},{
						header : '用户账号',
						dataIndex : 'uaccount',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '用户姓名',
						dataIndex : 'urealname',
						width : 100,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '交易金额',
						dataIndex : 'amount',
						width : 80,
						renderer : function(v) {
							return '<span style="color:blue;">'+v+'</span>';
      					}
					}, {
						header : '交易类型',
						dataIndex : 'paytyple',
						width : 80,
						renderer : function(v) {
							if(v == ''){
								return v;
							}
							if(v =='-1'){
								return "";
							}
							if(v == 0){
								return '<span style="color:#006600;">存款</span>';
							}else if(v == 1){
								return '<span style="color:#009900;">提款</span>';
							}else if(v == 2){
								return '<span style="color:#006600;">赠送</span>';
							}else if(v == 3){
								return '<span style="color:red;">扣款</span>';
							}
      					}
					},{
						header : '入款类型',
						dataIndex : 'paymethods',
						width : 80,
						renderer : function(v) {
							if(v == ''){
								return v;
							}
							if(v =='-1'){
								return "";
							}
							if(v == 0){
								return '<span style="color:#FF3300;">公司入款</span>';
							}else if(v == 1){
								return '<span style="color:#FF0099;">在线支付</span>';
							}else{
								return '<span style="color:#FF3300;">公司入款</span>';
							}
      					}
					},{
						header : '操作时间',
						dataIndex : 'deposittime',
						width : 130
					}, {
						header : '状态',
						dataIndex : 'status',
						width : 80,
						renderer : function(v) {
							if(v==''){
								return v;
							}
							if(v =='-1'){
								return "";
							}
							if(v == 3){
								return '<span style="color:green;">成功</span>';
							}else{
								return '<span style="color:red;">失败</span>';
							}
      					}
					},{
						header : '备注',
						dataIndex : 'cwremarks',
						width : 160
					}]
		});
		
/** 新建 */
proxyDepositPayOrder.addAction = new Ext.Action({
			text : '查找',
			iconCls : 'Zoom',
			handler : function() {
				proxyDepositPayOrder.store.baseParams.uaccount = $("#uaccountDeposit").val();
				proxyDepositPayOrder.store.baseParams.startDate = $("#payorderDepositStartDate").val();
				proxyDepositPayOrder.store.baseParams.endDate = $("#payorderDepositEndDate").val();
				proxyDepositPayOrder.store.reload();
			}
		});

proxyDepositPayOrder.searchField = new Ext.ux.form.SearchField({
			store : proxyDepositPayOrder.store,
			paramName : 'account',
			emptyText : '请输入代理账号',
			style : 'margin-left: 5px;'
		});
// 入款方式
proxyDepositPayOrder.paymethods = new Ext.form.ComboBox({
		hiddenName : 'paymethods',
		id : 'paymethods',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(proxyDepositPayOrder.paymethods)
				}),
		valueField : 'v',
		displayField : 't',
		value: '',
		editable : false,
		width:100,
		listeners : {
			select : function(comboBox) {
				proxyDepositPayOrder.store.baseParams.paymethods = comboBox.getValue();
			}
		}
});

/** 顶部工具栏 */
proxyDepositPayOrder.tbar = ['会员账号：',{id:'uaccountDeposit',xtype:'textfield',width:120},'&nbsp;','-',
		'开始日期：',{ id:'payorderDepositStartDate',xtype:'datetimefield',format:'Y-m-d H:i:s'},'&nbsp;','-',
		'结束日期：',{ id:'payorderDepositEndDate',xtype:'datetimefield',format:'Y-m-d H:i:s'},'&nbsp;','-','入款类型：',proxyDepositPayOrder.paymethods,'&nbsp;','-',proxyDepositPayOrder.addAction];
/** 底部工具条 */
proxyDepositPayOrder.bbar = new Ext.PagingToolbar({
			pageSize : proxyDepositPayOrder.pageSize,
			store : proxyDepositPayOrder.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', proxyDepositPayOrder.pageSizeCombo]
		});
/** 基本信息-表格 */
proxyDepositPayOrder.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : proxyDepositPayOrder.store,
			colModel : proxyDepositPayOrder.colModel,
			selModel : proxyDepositPayOrder.selModel,
			tbar : proxyDepositPayOrder.tbar,
			bbar : proxyDepositPayOrder.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'memberGradeDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});
			
proxyDepositPayOrder.returnscaleNumberField = new Ext.form.NumberField({
		maxLength : 8,
		allowBlank : false,
		decimalPrecision : 4,
		fieldLabel : '占成比例',
		name : 'returnscale',
		anchor : '99%'
	});

proxyDepositPayOrder.ximascaleNumberField = new Ext.form.NumberField({
		maxLength : 8,
		allowBlank : false,
		decimalPrecision : 4,
		fieldLabel : '洗码比例',
		name : 'ximascale',
		anchor : '99%'
	});
	
/** 基本信息-详细信息的form */
proxyDepositPayOrder.formPanel = new Ext.form.FormPanel({
			autoScroll : false,
			border: false,
        	style: 'border-bottom:0px;',
        	bodyStyle: 'padding:10px;background-color:transparent;',
			labelwidth : 50,
			defaultType : 'textfield',
			items : [{
						xtype : 'hidden',
						fieldLabel : 'ID',
						name : 'pyid',
						anchor : '99%'
					},{
						fieldLabel : '代理账号',
						maxLength : 32,
						allowBlank : false,
						name : 'account',
						anchor : '99%'
					},proxyDepositPayOrder.returnscaleNumberField,proxyDepositPayOrder.ximascaleNumberField]
		});

		

			
/** 编辑新建窗口 */
proxyDepositPayOrder.addWindow = new Ext.Window({
			layout : 'fit',
			width : 399,
			height : 160,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [proxyDepositPayOrder.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							proxyDepositPayOrder.saveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = proxyDepositPayOrder.formPanel.getForm();
							var id = form.findField("pyid").getValue();
							form.reset();
							if (id != '')
								form.findField("pyid").setValue(id);
						}
					}]
		});
	
proxyDepositPayOrder.alwaysFun = function() {
	Share.resetGrid(proxyDepositPayOrder.grid);
	proxyDepositPayOrder.deleteAction.disable();
	proxyDepositPayOrder.editAction.disable();
	proxyDepositPayOrder.bindAction.disable();
	proxyDepositPayOrder.removeAction.disable();
};
proxyDepositPayOrder.saveFun = function() {
	var form = proxyDepositPayOrder.formPanel.getForm();

	if(!form.isValid()){
 		return;
 	}
	// 发送请求
	Share.AjaxRequest({
				url : proxyDepositPayOrder.save,
				params : form.getValues(),
				callback : function(json) {
				if (json.success==false){
				     Ext.MessageBox.alert('Status', json.msg, showResult);
					return;
				}else{
				    proxyDepositPayOrder.addWindow.hide();
					proxyDepositPayOrder.alwaysFun();
					proxyDepositPayOrder.store.reload();
				}
					
				}
			});
};

proxyDepositPayOrder.amountsum = function(){
	var p = new Ext.data.Record({fields:['uiid', 'uaccount', 'urealname', 'amount',"paytyple",'paymethods','deposittime','status','cwremarks']});
	var amountTotal=0;
	proxyDepositPayOrder.store.each(function(record){
		var amount = record.data.amount;
		var uaccount=record.data.uaccount;
		if(amount != null&&uaccount!="总计:"){
			amountTotal += Number(amount);
		}
	});
	p.set('uiid','');
	p.set('uaccount','小计：');
	p.set('urealname','');
	p.set('amount',amountTotal);
	p.set('paytyple','');
	p.set('paymethods','');
	p.set('deposittime','');
	p.set('status','');
	p.set('cwremarks','');
	proxyDepositPayOrder.store.add(p);
}

proxyDepositPayOrder.delFun = function() {
	var record = proxyDepositPayOrder.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '您确定要删除选中代理占成数据吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : proxyDepositPayOrder.del + record.data.pyid +".do",
								callback : function(json) {
									proxyDepositPayOrder.alwaysFun();
									proxyDepositPayOrder.store.reload();
								}
							});
				}
			});
};

proxyDepositPayOrder.bindFun = function(s){
	var record = proxyDepositPayOrder.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '确定要修改修改占成数据状态吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : proxyDepositPayOrder.bind + record.data.pyid+"/"+s+".do",
								callback : function(json) {
									proxyDepositPayOrder.alwaysFun();
									proxyDepositPayOrder.store.reload();
								}
							});
				}
			});
}
  
proxyDepositPayOrder.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [proxyDepositPayOrder.grid]
		});

</script>