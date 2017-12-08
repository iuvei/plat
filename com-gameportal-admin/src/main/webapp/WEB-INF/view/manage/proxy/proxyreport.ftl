<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.proxyfroms"); // 自定义一个命名空间
proxyfrom = Ext.market.proxyfroms; // 定义命名空间的别名
proxyfrom = {
	all : '/manage/proxy/queryProxyInfo.do',// 加载所有
	info : "/manage/proxy/proxyfrominfo.do",//查询详情
	pageSize : 20, // 每页显示的记录数
	memGrade:eval('(${fields.memGrade})'),
	isXimaFlag : eval('(${fields.ixXimaFlag})'),
	clearingtype:eval('(${fields.proxyclearingtype})')
};


/** 改变页的combo */
proxyfrom.pageSizeCombo = new Share.pageSizeCombo({
			value : '20',
			listeners : {
				select : function(comboBox) {
					proxyfrom.pageSize = parseInt(comboBox.getValue());
					proxyfrom.bbar.pageSize = parseInt(comboBox.getValue());
					proxyfrom.store.baseParams.limit = proxyfrom.pageSize;
					proxyfrom.store.baseParams.start = 0;
					proxyfrom.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
proxyfrom.pageSize = parseInt(proxyfrom.pageSizeCombo.getValue());
/** 基本信息-数据源 */
proxyfrom.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : proxyfrom.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : proxyfrom.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['uiid', 'account', 'uname', 'identitycard', 'phone','returnscale','ximascale','pyid',
				'email', 'qq', 'birthday', 'grade', 'url',
				'status','createDate','updateDate','lowerUser','loginip','logincount','isximaflag','clearingtype']),
			listeners : {
				'load' : function(store, records, options) {
				//	memberGrade.alwaysFun();
				}
			}
		});
		
/** 基本信息-选择模式 */
proxyfrom.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				proxyfrom.messageAction.enable();
				
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				proxyfrom.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
proxyfrom.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [proxyfrom.selModel, {
						hidden : true,
						header : '用户编号',
						dataIndex : 'uiid'
					},{
						hidden : true,
						header : '代理占成数据编号',
						dataIndex : 'pyid'
					}, {
						header : '代理账号',
						dataIndex : 'account',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '代理姓名',
						dataIndex : 'uname',
						width : 100,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '下级人数',
						dataIndex : 'lowerUser',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '代理域名',
						dataIndex : 'url',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '占成比例',
						dataIndex : 'returnscale',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '洗码比例',
						dataIndex : 'ximascale',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '洗码状态',
						dataIndex : 'isximaflag',
						width : 100,
						renderer : function(v) {
							if(v == 0){
								return '<span style="color:red;">未开启</span>';
							}else{
								return '<span style="color:green;">已开启</span>';
							}
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '结算类型',
						dataIndex : 'clearingtype',
						width : 80,
						renderer : function(v) {
							if(v == 0){
								return '<span style="color:blue;">输值结算</span>';
							}else if(v == 1){
								return '<span style="color:#FF3333;">按月洗码</span>';
							}else if(v == 2){
								return '<span style="color:#FF3333;">按天洗码</span>';
							}else{
								return v;
							}
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '代理级别',
						dataIndex : 'grade',
						width : 100,
						renderer : function(v) {
							return Share.map(v, proxyfrom.memGrade, '');
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '代理状态',
						dataIndex : 'status',
						width : 100,
						renderer : function(v) {
							if(v == 0){
								return '<span style="color:red;">禁用</span>';
							}else{
								return '<span style="color:green;">正常</span>';
							}
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : 'QQ',
						dataIndex : 'qq',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '创建日期',
						dataIndex : 'createDate',
						renderer : function(v) {
       						return new Date(v).format('Y-m-d H:i:s');
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
						
					},{
						header : '登录次数',
						dataIndex : 'logincount',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}]
		});

		/** 查询按钮 */
proxyfrom.searchAction = new Ext.Action({
		text : '搜索',
		iconCls : 'Zoom',
		disabled : false,
		handler : function() {
			proxyfrom.searchFun();
		}
	
});

proxyfrom.messageAction = new Ext.Action({
		text : '查看详情',
		iconCls : 'Applicationedit',
		disabled : true,
		handler : function() {
			var record = proxyfrom.grid.getSelectionModel().getSelected();
			var startdate = $("#proxyFromStartdate").val();
			var enddate = $("#proxyFromEnddate").val();
			window.open(proxyfrom.info+'?id='+record.data.uiid+'&startdate='+startdate+'&enddate='+enddate);
		}
	});
		
proxyfrom.searchParams = function(){
	var obj = {};
	obj.account = $("#proxyaccount").val();
	obj.startdate = $("#proxyFromStartdate").val();
	obj.enddate = $("#proxyFromEnddate").val();
	obj.domain = $("#proxydomain").val();
    return obj;
}
	
proxyfrom.searchFun = function(){
	proxyfrom.store.load({params: proxyfrom.searchParams()});
}

proxyfrom.store.on('beforeload',function(store, options){
    proxyfrom.store.baseParams = proxyfrom.searchParams();
});

proxyfrom.proxyFromStartdate = new  Ext.form.DateField({ 
		id:'proxyFromStartdate',
		showToday:true,
		format:'Y-m-d',
		invalidText:'日期输入非法',
		width:120
});
proxyfrom.proxyFromEnddate = new  Ext.form.DateField({ 
		id:'proxyFromEnddate',
		showToday:true,
		format:'Y-m-d',
		invalidText:'日期输入非法',
		width:120
});
/** 顶部工具栏 */
proxyfrom.tbar = [proxyfrom.messageAction,'&nbsp;','代理帐号:',{id:'proxyaccount',xtype:'textfield',width:120},
		'-','&nbsp;','代理域名:',{id:'proxydomain',xtype:'textfield',width:120},
		'-','&nbsp;','开始日期:',proxyfrom.proxyFromStartdate,
		'至',proxyfrom.proxyFromEnddate,
		'-', proxyfrom.searchAction];
/** 底部工具条 */
proxyfrom.bbar = new Ext.PagingToolbar({
			pageSize : proxyfrom.pageSize,
			store : proxyfrom.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', proxyfrom.pageSizeCombo]
		});
/** 基本信息-表格 */
proxyfrom.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : proxyfrom.store,
			colModel : proxyfrom.colModel,
			selModel : proxyfrom.selModel,
			tbar : proxyfrom.tbar,
			bbar : proxyfrom.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'memberGradeDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});
			

	
proxyfrom.alwaysFun = function() {
	Share.resetGrid(proxyfrom.grid);
	proxyfrom.messageAction.disable();
};


proxyfrom.delFun = function() {
	var record = proxyfrom.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '您确定要删除选中代理占成数据吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : proxyfrom.del + record.data.pyid +".do",
								callback : function(json) {
									proxyfrom.alwaysFun();
									proxyfrom.store.reload();
								}
							});
				}
			});
};

  
proxyfrom.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [proxyfrom.grid]
		});

</script>