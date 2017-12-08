<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.proxyPunishOrder"); // 自定义一个命名空间
proxyPunishOrder = Ext.market.proxyPunishOrder; // 定义命名空间的别名
proxyPunishOrder = {
	all : '/proxymanage/m/queryPunishOrderlog.do',// 加载所有
	pageSize : 20 // 每页显示的记录数
};


/** 改变页的combo */
proxyPunishOrder.pageSizeCombo = new Share.pageSizeCombo({
			value : '20',
			listeners : {
				select : function(comboBox) {
					proxyPunishOrder.pageSize = parseInt(comboBox.getValue());
					proxyPunishOrder.bbar.pageSize = parseInt(comboBox.getValue());
					proxyPunishOrder.store.baseParams.limit = proxyPunishOrder.pageSize;
					proxyPunishOrder.store.baseParams.start = 0;
					proxyPunishOrder.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
proxyPunishOrder.pageSize = parseInt(proxyPunishOrder.pageSizeCombo.getValue());
/** 基本信息-数据源 */
proxyPunishOrder.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				startDate: "",
				endDate : "",
				uaccount : "",
				start : 0,
				limit : proxyPunishOrder.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : proxyPunishOrder.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['uiid', 'uaccount', 'urealname', 'amount','paytyple','deposittime','status','cwremarks']),
			listeners : {
				'load' : function(store, records, options) {
					proxyPunishOrder.amountsum();
				}
			}
		});
		
/** 基本信息-选择模式 */
proxyPunishOrder.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				proxyPunishOrder.deleteAction.enable();
				proxyPunishOrder.editAction.enable();
				if(record.data.status == '0'){
					proxyPunishOrder.removeAction.enable();
					proxyPunishOrder.bindAction.disable();
				}else{
					proxyPunishOrder.bindAction.enable();
					proxyPunishOrder.removeAction.disable();
				}
				
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				proxyPunishOrder.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
proxyPunishOrder.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [proxyPunishOrder.selModel, {
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
						header : '操作时间',
						dataIndex : 'deposittime',
						width : 130
					}, {
						header : '状态',
						dataIndex : 'status',
						width : 80,
						renderer : function(v) {
							if(v == ''){
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
proxyPunishOrder.addAction = new Ext.Action({
			text : '查找',
			iconCls : 'Zoom',
			handler : function() {
				proxyPunishOrder.store.baseParams.uaccount = $("#uaccount").val();
				proxyPunishOrder.store.baseParams.startDate = $("#payorderStartDate").val();
				proxyPunishOrder.store.baseParams.endDate = $("#payorderEndDate").val();
				proxyPunishOrder.store.reload();
			}
		});

proxyPunishOrder.searchField = new Ext.ux.form.SearchField({
			store : proxyPunishOrder.store,
			paramName : 'account',
			emptyText : '请输入代理账号',
			style : 'margin-left: 5px;'
		});

/** 顶部工具栏 */
proxyPunishOrder.tbar = ['会员账号：',{id:'uaccount',xtype:'textfield',width:120},'&nbsp;','-',
		'开始日期：',{ id:'payorderStartDate',xtype:'datetimefield',format:'Y-m-d H:i:s'},'&nbsp;','-',
		'结束日期：',{ id:'payorderEndDate',xtype:'datetimefield',format:'Y-m-d H:i:s'},'&nbsp;','-',proxyPunishOrder.addAction];
/** 底部工具条 */
proxyPunishOrder.bbar = new Ext.PagingToolbar({
			pageSize : proxyPunishOrder.pageSize,
			store : proxyPunishOrder.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', proxyPunishOrder.pageSizeCombo]
		});
/** 基本信息-表格 */
proxyPunishOrder.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : proxyPunishOrder.store,
			colModel : proxyPunishOrder.colModel,
			selModel : proxyPunishOrder.selModel,
			tbar : proxyPunishOrder.tbar,
			bbar : proxyPunishOrder.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'memberGradeDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});
			
proxyPunishOrder.returnscaleNumberField = new Ext.form.NumberField({
		maxLength : 8,
		allowBlank : false,
		decimalPrecision : 4,
		fieldLabel : '占成比例',
		name : 'returnscale',
		anchor : '99%'
	});

proxyPunishOrder.ximascaleNumberField = new Ext.form.NumberField({
		maxLength : 8,
		allowBlank : false,
		decimalPrecision : 4,
		fieldLabel : '洗码比例',
		name : 'ximascale',
		anchor : '99%'
	});
	
/** 基本信息-详细信息的form */
proxyPunishOrder.formPanel = new Ext.form.FormPanel({
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
					},proxyPunishOrder.returnscaleNumberField,proxyPunishOrder.ximascaleNumberField]
		});

proxyPunishOrder.amountsum = function(){
	var p = new Ext.data.Record({fields:['uiid', 'uaccount', 'urealname', 'amount',"paytyple",'paymethods','deposittime','status','cwremarks']});
	var amountTotal=0;
	proxyPunishOrder.store.each(function(record){
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
	proxyPunishOrder.store.add(p);
}

			
/** 编辑新建窗口 */
proxyPunishOrder.addWindow = new Ext.Window({
			layout : 'fit',
			width : 399,
			height : 160,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [proxyPunishOrder.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							proxyPunishOrder.saveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = proxyPunishOrder.formPanel.getForm();
							var id = form.findField("pyid").getValue();
							form.reset();
							if (id != '')
								form.findField("pyid").setValue(id);
						}
					}]
		});
	
proxyPunishOrder.alwaysFun = function() {
	Share.resetGrid(proxyPunishOrder.grid);
	proxyPunishOrder.deleteAction.disable();
	proxyPunishOrder.editAction.disable();
	proxyPunishOrder.bindAction.disable();
	proxyPunishOrder.removeAction.disable();
};
proxyPunishOrder.saveFun = function() {
	var form = proxyPunishOrder.formPanel.getForm();

	if(!form.isValid()){
 		return;
 	}
	// 发送请求
	Share.AjaxRequest({
				url : proxyPunishOrder.save,
				params : form.getValues(),
				callback : function(json) {
				if (json.success==false){
				     Ext.MessageBox.alert('Status', json.msg, showResult);
					return;
				}else{
				    proxyPunishOrder.addWindow.hide();
					proxyPunishOrder.alwaysFun();
					proxyPunishOrder.store.reload();
				}
					
				}
			});
};

proxyPunishOrder.delFun = function() {
	var record = proxyPunishOrder.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '您确定要删除选中代理占成数据吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : proxyPunishOrder.del + record.data.pyid +".do",
								callback : function(json) {
									proxyPunishOrder.alwaysFun();
									proxyPunishOrder.store.reload();
								}
							});
				}
			});
};

proxyPunishOrder.bindFun = function(s){
	var record = proxyPunishOrder.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '确定要修改修改占成数据状态吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : proxyPunishOrder.bind + record.data.pyid+"/"+s+".do",
								callback : function(json) {
									proxyPunishOrder.alwaysFun();
									proxyPunishOrder.store.reload();
								}
							});
				}
			});
}
  
proxyPunishOrder.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [proxyPunishOrder.grid]
		});

</script>