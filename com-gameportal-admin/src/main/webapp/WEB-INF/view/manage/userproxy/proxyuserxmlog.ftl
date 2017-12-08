<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.proxyInfos"); // 自定义一个命名空间
proxyuserxmlog = Ext.market.proxyInfos; // 定义命名空间的别名
proxyuserxmlog = {
	all : '/proxymanage/m/queryProxyuserxm.do',// 加载所有
	pageSize : 30 // 每页显示的记录数
};


/** 改变页的combo */
proxyuserxmlog.pageSizeCombo = new Share.pageSizeCombo({
			value : '30',
			listeners : {
				select : function(comboBox) {
					proxyuserxmlog.pageSize = parseInt(comboBox.getValue());
					proxyuserxmlog.bbar.pageSize = parseInt(comboBox.getValue());
					proxyuserxmlog.store.baseParams.limit = proxyuserxmlog.pageSize;
					proxyuserxmlog.store.baseParams.start = 0;
					proxyuserxmlog.store.load();
				}
			}
		});
		
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
proxyuserxmlog.pageSize = parseInt(proxyuserxmlog.pageSizeCombo.getValue());
/** 基本信息-数据源 */
proxyuserxmlog.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : proxyuserxmlog.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : proxyuserxmlog.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['logid', 'uiid', 'account','uname', 'proxyaccount',"proxyuname",'ximascale','ximamoney','yhmoney','validmoney','jsstarttime','jsendtime','ximatime','status']),
			listeners : {
				'load' : function(store, records, options) {
				//	memberGrade.alwaysFun();
				}
			}
		});
		
/** 基本信息-选择模式 */
proxyuserxmlog.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				var status = record.data.status;
				if(status == '0'){
					proxyuserxmlog.addAction.enable();
					proxyuserxmlog.verifyAction.enable();
				}
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				proxyuserxmlog.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
proxyuserxmlog.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [proxyuserxmlog.selModel, {
						hidden : true,
						header : 'ID',
						dataIndex : 'logid'
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
						header : '代理账号',
						dataIndex : 'proxyaccount',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '代理姓名',
						dataIndex : 'proxyuname',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '洗码比例',
						dataIndex : 'ximascale',
						width : 80
					},{
						header : '洗码金额',
						dataIndex : 'ximamoney',
						width : 80,
						renderer : function(v) {
							return '<span style="color:blue;">'+v+'</span>';
						}
					},{
						header : '优惠金额',
						dataIndex : 'yhmoney',
						width : 80,
						renderer : function(v) {
							return '<span style="color:blue;">'+v+'</span>';
						}
					},{
						header : '有效投注额',
						dataIndex : 'validmoney',
						width : 80,
						renderer : function(v) {
							return '<span style="color:blue;">'+v+'</span>';
						}
					},{
						header : '结算时间',
						dataIndex : 'ximatime',
						width : 130
					},{
						header : '开始日期',
						dataIndex : 'jsstarttime',
						width : 130,
						renderer : function(v) {
       						return new Date(v).format('Y-m-d H:i:s');
      					}
					},{
						header : '结束日期',
						dataIndex : 'jsendtime',
						width : 130,
						renderer : function(v) {
       						return new Date(v).format('Y-m-d H:i:s');
      					}
					}, {
						header : '状态',
						dataIndex : 'status',
						width : 80,
						renderer : function(v) {
							if(v == 1){
								return '<span style="color:green;">已入账</span>';
							}else if(v == 0){
								return '<span style="color:red;">未入账</span>';
							}else if(v == 2){
								return '<span style="color:#CC3333;">不通过</span>';
							}else{
								return v;
							}
      					}

					}]
		});
		
/** 新建 */
proxyuserxmlog.addAction = new Ext.Action({
			text : '入账',
			iconCls : 'Add',
			disabled : true,
			handler : function() {
				
				proxyuserxmlog.saveFun();
			}
		});
		
proxyuserxmlog.verifyAction = new Ext.Action({
			text : '审核不通过',
			iconCls : 'Cross',
			disabled : true,
			handler : function() {
				proxyuserxmlog.verifyFun();
			}
		});

proxyuserxmlog.searchField = new Ext.ux.form.SearchField({
			store : proxyuserxmlog.store,
			paramName : 'account',
			emptyText : '请输入会员账号',
			style : 'margin-left: 5px;'
		});
/** 顶部工具栏 */
proxyuserxmlog.tbar = [proxyuserxmlog.searchField];
/** 底部工具条 */
proxyuserxmlog.bbar = new Ext.PagingToolbar({
			pageSize : proxyuserxmlog.pageSize,
			store : proxyuserxmlog.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', proxyuserxmlog.pageSizeCombo]
		});
/** 基本信息-表格 */
proxyuserxmlog.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : proxyuserxmlog.store,
			colModel : proxyuserxmlog.colModel,
			selModel : proxyuserxmlog.selModel,
			tbar : proxyuserxmlog.tbar,
			bbar : proxyuserxmlog.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'memberGradeDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});
			
proxyuserxmlog.returnscaleNumberField = new Ext.form.NumberField({
		maxLength : 10,
		allowBlank : false,
		decimalPrecision : 10,
		readOnly:true,
		fieldLabel : '结算金额',
		name : 'clearingAmount',
		anchor : '99%'
	});

proxyuserxmlog.ximascaleNumberField = new Ext.form.NumberField({
		maxLength : 10,
		allowBlank : false,
		decimalPrecision : 10,
		fieldLabel : '入账金额',
		name : 'rAmount',
		anchor : '99%'
	});
	

	
proxyuserxmlog.alwaysFun = function() {
	Share.resetGrid(proxyuserxmlog.grid);
	proxyuserxmlog.addAction.disable();
	proxyuserxmlog.verifyAction.disable();
};
proxyuserxmlog.saveFun = function() {
	var record = proxyuserxmlog.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '入账后会将相应的金额添加到用户钱包，确认入账吗?', function(btn, text) {
		if (btn == 'yes') {
			// 发送请求
			Share.AjaxRequest({
						url : proxyuserxmlog.save+record.data.logid+".do",
						callback : function(json) {
						if (json.success==false){
						     Ext.MessageBox.alert('Status', json.msg, showResult);
							return;
						}else{
							proxyuserxmlog.alwaysFun();
							proxyuserxmlog.store.reload();
						}
						}
					});
		}
	});
	
};

proxyuserxmlog.verifyFun = function() {
	var record = proxyuserxmlog.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '确定要将此洗码审核不通过吗?', function(btn, text) {
		if (btn == 'yes') {
			// 发送请求
			Share.AjaxRequest({
						url : proxyuserxmlog.verify+record.data.logid+".do",
						callback : function(json) {
						if (json.success==false){
						     Ext.MessageBox.alert('Status', json.msg, showResult);
							return;
						}else{
							proxyuserxmlog.alwaysFun();
							proxyuserxmlog.store.reload();
						}
						}
					});
		}
	});
	
};

proxyuserxmlog.delFun = function() {
	var record = proxyuserxmlog.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '您确定要删除选中代理占成数据吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : proxyuserxmlog.del + record.data.pyid +".do",
								callback : function(json) {
									proxyuserxmlog.alwaysFun();
									proxyuserxmlog.store.reload();
								}
							});
				}
			});
};

proxyuserxmlog.bindFun = function(s){
	var record = proxyuserxmlog.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '确定要修改修改占成数据状态吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : proxyuserxmlog.bind + record.data.pyid+"/"+s+".do",
								callback : function(json) {
									proxyuserxmlog.alwaysFun();
									proxyuserxmlog.store.reload();
								}
							});
				}
			});
}
  
proxyuserxmlog.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [proxyuserxmlog.grid]
		});

</script>