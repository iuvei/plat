<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.proxyPayOrder"); // 自定义一个命名空间
proxyPayOrder = Ext.market.proxyPayOrder; // 定义命名空间的别名
proxyPayOrder = {
	all : '/proxymanage/m/queryPayOrderlog.do',// 加载所有
	pageSize : 20, // 每页显示的记录数
	memGrade:eval('(${fields.memGrade})')
};


/** 改变页的combo */
proxyPayOrder.pageSizeCombo = new Share.pageSizeCombo({
			value : '20',
			listeners : {
				select : function(comboBox) {
					proxyPayOrder.pageSize = parseInt(comboBox.getValue());
					proxyPayOrder.bbar.pageSize = parseInt(comboBox.getValue());
					proxyPayOrder.store.baseParams.limit = proxyPayOrder.pageSize;
					proxyPayOrder.store.baseParams.start = 0;
					proxyPayOrder.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
proxyPayOrder.pageSize = parseInt(proxyPayOrder.pageSizeCombo.getValue());
/** 基本信息-数据源 */
proxyPayOrder.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				startDate: "",
				endDate : "",
				uaccount : "",
				start : 0,
				limit : proxyPayOrder.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : proxyPayOrder.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['uiid', 'uaccount', 'urealname', 'amount',"paytyple",'paymethods','deposittime','status','cwremarks']),
			listeners : {
				'load' : function(store, records, options) {
					proxyPayOrder.amountsum();
				}
			}
		});
		
/** 基本信息-选择模式 */
proxyPayOrder.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				proxyPayOrder.deleteAction.enable();
				proxyPayOrder.editAction.enable();
				if(record.data.status == '0'){
					proxyPayOrder.removeAction.enable();
					proxyPayOrder.bindAction.disable();
				}else{
					proxyPayOrder.bindAction.enable();
					proxyPayOrder.removeAction.disable();
				}
				
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				proxyPayOrder.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
proxyPayOrder.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [proxyPayOrder.selModel, {
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
proxyPayOrder.addAction = new Ext.Action({
			text : '查找',
			iconCls : 'Zoom',
			handler : function() {
				proxyPayOrder.store.baseParams.uaccount = $("#uaccount").val();
				proxyPayOrder.store.baseParams.startDate = $("#payorderStartDate").val();
				proxyPayOrder.store.baseParams.endDate = $("#payorderEndDate").val();
				proxyPayOrder.store.reload();
			}
		});

proxyPayOrder.searchField = new Ext.ux.form.SearchField({
			store : proxyPayOrder.store,
			paramName : 'account',
			emptyText : '请输入代理账号',
			style : 'margin-left: 5px;'
		});

/** 顶部工具栏 */
proxyPayOrder.tbar = ['会员账号：',{id:'uaccount',xtype:'textfield',width:120},'&nbsp;','-',
		'开始日期：',{ id:'payorderStartDate',xtype:'datetimefield',format:'Y-m-d H:i:s'},'&nbsp;','-',
		'结束日期：',{ id:'payorderEndDate',xtype:'datetimefield',format:'Y-m-d H:i:s'},'&nbsp;','-',proxyPayOrder.addAction];
/** 底部工具条 */
proxyPayOrder.bbar = new Ext.PagingToolbar({
			pageSize : proxyPayOrder.pageSize,
			store : proxyPayOrder.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', proxyPayOrder.pageSizeCombo]
		});
/** 基本信息-表格 */
proxyPayOrder.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : proxyPayOrder.store,
			colModel : proxyPayOrder.colModel,
			selModel : proxyPayOrder.selModel,
			tbar : proxyPayOrder.tbar,
			bbar : proxyPayOrder.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'memberGradeDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});
			
proxyPayOrder.returnscaleNumberField = new Ext.form.NumberField({
		maxLength : 8,
		allowBlank : false,
		decimalPrecision : 4,
		fieldLabel : '占成比例',
		name : 'returnscale',
		anchor : '99%'
	});

proxyPayOrder.ximascaleNumberField = new Ext.form.NumberField({
		maxLength : 8,
		allowBlank : false,
		decimalPrecision : 4,
		fieldLabel : '洗码比例',
		name : 'ximascale',
		anchor : '99%'
	});
	
/** 基本信息-详细信息的form */
proxyPayOrder.formPanel = new Ext.form.FormPanel({
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
					},proxyPayOrder.returnscaleNumberField,proxyPayOrder.ximascaleNumberField]
		});

proxyPayOrder.amountsum = function(){
	var p = new Ext.data.Record({fields:['uiid', 'uaccount', 'urealname', 'amount',"paytyple",'paymethods','deposittime','status','cwremarks']});
	var amountTotal=0;
	proxyPayOrder.store.each(function(record){
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
	proxyPayOrder.store.add(p);
}

			
/** 编辑新建窗口 */
proxyPayOrder.addWindow = new Ext.Window({
			layout : 'fit',
			width : 399,
			height : 160,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [proxyPayOrder.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							proxyPayOrder.saveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = proxyPayOrder.formPanel.getForm();
							var id = form.findField("pyid").getValue();
							form.reset();
							if (id != '')
								form.findField("pyid").setValue(id);
						}
					}]
		});
	
proxyPayOrder.alwaysFun = function() {
	Share.resetGrid(proxyPayOrder.grid);
	proxyPayOrder.deleteAction.disable();
	proxyPayOrder.editAction.disable();
	proxyPayOrder.bindAction.disable();
	proxyPayOrder.removeAction.disable();
};
proxyPayOrder.saveFun = function() {
	var form = proxyPayOrder.formPanel.getForm();

	if(!form.isValid()){
 		return;
 	}
	// 发送请求
	Share.AjaxRequest({
				url : proxyPayOrder.save,
				params : form.getValues(),
				callback : function(json) {
				if (json.success==false){
				     Ext.MessageBox.alert('Status', json.msg, showResult);
					return;
				}else{
				    proxyPayOrder.addWindow.hide();
					proxyPayOrder.alwaysFun();
					proxyPayOrder.store.reload();
				}
					
				}
			});
};

proxyPayOrder.delFun = function() {
	var record = proxyPayOrder.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '您确定要删除选中代理占成数据吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : proxyPayOrder.del + record.data.pyid +".do",
								callback : function(json) {
									proxyPayOrder.alwaysFun();
									proxyPayOrder.store.reload();
								}
							});
				}
			});
};

proxyPayOrder.bindFun = function(s){
	var record = proxyPayOrder.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '确定要修改修改占成数据状态吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : proxyPayOrder.bind + record.data.pyid+"/"+s+".do",
								callback : function(json) {
									proxyPayOrder.alwaysFun();
									proxyPayOrder.store.reload();
								}
							});
				}
			});
}
  
proxyPayOrder.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [proxyPayOrder.grid]
		});

</script>