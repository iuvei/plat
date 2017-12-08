<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.proxyDownuser"); // 自定义一个命名空间
proxyDownuser = Ext.market.proxyDownuser; // 定义命名空间的别名
proxyDownuser = {
	all : '/proxymanage/m/queryDownuser.do',// 加载所有
	setxima:'/proxymanage/m/setUserXima.do',//设置用户洗码
	pageSize : 35, // 每页显示的记录数
	today:'${today}',
	isSearch : false,
	ximaStatus : '${ximaStatus}',//洗码状态
	winFields : eval('(${fields.winFields})')
};


/** 改变页的combo */
proxyDownuser.pageSizeCombo = new Share.pageSizeCombo({
			value : '35',
			listeners : {
				select : function(comboBox) {
					proxyDownuser.pageSize = parseInt(comboBox.getValue());
					proxyDownuser.bbar.pageSize = parseInt(comboBox.getValue());
					proxyDownuser.store.baseParams.limit = proxyDownuser.pageSize;
					proxyDownuser.store.baseParams.start = 0;
					proxyDownuser.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
proxyDownuser.pageSize = parseInt(proxyDownuser.pageSizeCombo.getValue());
/** 基本信息-数据源 */
proxyDownuser.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : proxyDownuser.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : proxyDownuser.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['uiid','account','uname','ximascale','betTotel','finalamountTotal','betAmountTotal','profitamountTotal','validBetAmountTotal']),
			listeners : {
				'load' : function(store, records, options) {
				//	proxyDownuser.alwaysFun();
				}
			}
		});
		
/** 基本信息-选择模式 */
proxyDownuser.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				if(isximaFlag == 1){
					proxyDownuser.ximaSetAction.enable();
				}
				//proxyDownuser.editAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				proxyDownuser.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
proxyDownuser.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 100
			},
			columns : [proxyDownuser.selModel, {
						hidden : true,
						header : 'ID',
						dataIndex : 'uiid'
					},{
						header : '帐号',
						dataIndex : 'account',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '姓名',
						dataIndex : 'uname',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '洗码比例',
						dataIndex : 'ximascale',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '注单数量',
						dataIndex : 'betTotel',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '投注金额',
						dataIndex : 'betAmountTotal',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '有效投注额',
						dataIndex : 'validBetAmountTotal',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '派彩金额',
						dataIndex : 'profitamountTotal',
						renderer : function(v) {
							return Share.amount(v);
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '当前盈亏',
						dataIndex : 'finalamountTotal',
						renderer : function(v) {
							return Share.amount(v);
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}]
		});
		


proxyDownuser.ximaSetAction = new Ext.Action({
			text : '设置洗码',
			iconCls : 'Applicationedit',
			disabled : true,
			handler : function() {
				var record = proxyDownuser.grid.getSelectionModel().getSelected();
				proxyDownuser.addWindow.setIconClass('Applicationedit'); // 设置窗口的样式
				proxyDownuser.addWindow.setTitle('编辑会员洗码比例'); // 设置窗口的名称
				proxyDownuser.addWindow.show().center();
				proxyDownuser.formPanel.getForm().reset();
				proxyDownuser.formPanel.getForm().loadRecord(record);
			}
		});

proxyDownuser.ximascaleNumberField = new Ext.form.NumberField({
		maxLength : 8,
		allowBlank : false,
		decimalPrecision : 4,
		fieldLabel : '洗码比例',
		name : 'ximascale',
		anchor : '99%'
	});
	
/** 基本信息-详细信息的form */
proxyDownuser.formPanel = new Ext.form.FormPanel({
			autoScroll : false,
			border: false,
        	style: 'border-bottom:0px;',
        	bodyStyle: 'padding:10px;background-color:transparent;',
			labelwidth : 50,
			defaultType : 'textfield',
			items : [{
						xtype : 'hidden',
						fieldLabel : 'ID',
						name : 'uiid',
						anchor : '99%'
					},proxyDownuser.ximascaleNumberField]
		});

/** 查询 */
proxyDownuser.searchField = new Ext.ux.form.SearchField({
			store : proxyDownuser.store,
			paramName : 'account',
			emptyText : '请输入帐号',
			style : 'margin-left: 5px;'
		});
/** 顶部工具栏 */
proxyDownuser.tbar = [ proxyDownuser.searchField];
/** 底部工具条 */
proxyDownuser.bbar = new Ext.PagingToolbar({
			pageSize : proxyDownuser.pageSize,
			store : proxyDownuser.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', proxyDownuser.pageSizeCombo]
		});
//搜索函数
proxyDownuser.searchFun = function(){
	var obj={};
	obj.account = $("#memberAcconut").val();
	obj.startDate = $("#proxyDownuserstartDate").val();
	obj.endDate = $("#proxyDownuserendDate").val();
	proxyDownuser.store.load({params: obj});
}

proxyDownuser.searchBtn = new Ext.Action({
		text : '查询',
		iconCls : 'Zoom',
		handler : function() {
			proxyDownuser.isSearch = true;
			proxyDownuser.searchFun();
		}
	})

/** 基本信息-表格 */
proxyDownuser.grid = new Ext.grid.EditorGridPanel({
			// title : '押注记录列表',
			store : proxyDownuser.store,
			colModel : proxyDownuser.colModel,
			selModel : proxyDownuser.selModel,
			tbar : [proxyDownuser.ximaSetAction,'&nbsp;','-',
			'会员账号:',{id:'memberAcconut',xtype:'textfield',width:120},'-',
			' 开始日期:',{ id:'proxyDownuserstartDate',xtype:'datetimefield',format:'Y-m-d H:i:s',value:new Date().format("Y-m-d")},'-',
            ' 到',{ id:'proxyDownuserendDate',xtype:'datetimefield',format:'Y-m-d H:i:s',value:proxyDownuser.today},'-','&nbsp;',
            proxyDownuser.searchBtn
			],
			bbar : proxyDownuser.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'proxyDownuserDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {},
			listeners : {
			'cellclick' : function(obj, rowIndex, columnIndex, e) {
				var record = proxyDownuser.grid.getSelectionModel().getSelected();
				if (record) {
					// 更新明细记录
					proxyDownuserBetInfo.store.baseParams.account = record.data.account;
					if(proxyDownuser.isSearch){
						proxyDownuserBetInfo.store.baseParams.startDate=$("#proxyDownuserstartDate").val();
						proxyDownuserBetInfo.store.baseParams.endDate=$("#proxyDownuserendDate").val();
					}
					proxyDownuserBetInfo.store.reload();
				}
			}
		}
		});

/** 编辑新建窗口 */
proxyDownuser.addWindow = new Ext.Window({
			layout : 'fit',
			width : 350,
			height : 120,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [proxyDownuser.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							proxyDownuser.setximaFun();
						}
					}, {
						text : '重置',
						handler : function() {
							
						}
					}]
		});
		

proxyDownuser.setximaFun = function(){
	var form = proxyDownuser.formPanel.getForm();

	if(!form.isValid()){
 		return;
 	}
	// 发送请求
	Share.AjaxRequest({
				url : proxyDownuser.setxima,
				params : form.getValues(),
				callback : function(json) {
				if (json.success==false){
				     Ext.MessageBox.alert('Status', json.msg, showResult);
					return;
				}else{
				    proxyDownuser.addWindow.hide();
					proxyDownuser.alwaysFun();
					proxyDownuser.store.reload();
				}
				}
			});
}
proxyDownuser.alwaysFun = function() {
	Share.resetGrid(proxyDownuser.grid);
	proxyDownuser.ximaSetAction.disable();
    
};









//--------------------------------下线会员投注详情-------------------------------------------
Ext.ns("Ext.market.proxyDownuser"); // 自定义一个命名空间
proxyDownuserBetInfo = Ext.market.proxyDownuserBetInfo; // 定义命名空间的别名
proxyDownuserBetInfo = {
	all : '/proxymanage/m/queryUserBet.do', // 
	pageSize : 30// 每页显示的记录数
}

/** 改变页的combo*/
proxyDownuserBetInfo.pageSizeCombo = new Share.pageSizeCombo({
	value : '30',
	listeners : {
		select : function(comboBox) {
			proxyDownuserBetInfo.pageSize  = parseInt(comboBox.getValue());
			proxyDownuserBetInfo.bbar.pageSize  = parseInt(comboBox.getValue());
			proxyDownuserBetInfo.store.baseParams.limit = proxyLowerUserInfo.pageSize;
			proxyDownuserBetInfo.store.baseParams.start = 0;
			proxyDownuserBetInfo.store.load();
		}
	}
});

//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
proxyDownuserBetInfo.pageSize = parseInt(proxyDownuserBetInfo.pageSizeCombo.getValue());

/** 基本信息-数据源 */
proxyDownuserBetInfo.store = new Ext.data.Store({
	remoteSort : true,
	autoLoad:false,
	baseParams : {
		account : "",
		startDate : "",
		endDate : "",
		start : 0,
		limit : proxyDownuserBetInfo.pageSize
	},  
	proxy : new Ext.data.HttpProxy({// 获取数据的方式
		method : 'POST',
		url : proxyDownuserBetInfo.all
	}),
	reader : new Ext.data.JsonReader({// 数据读取器
		totalProperty : 'totalProperty', // 记录总数
		root : 'data' // Json中的列表数据根节点
	}, ['pdid','platformcode', 'gamecode', 'betdate', 'betno', 
			    	'betamount','profitamount', 'finalamount','origin','result',
			    	'gamename','account','validBetAmount','playType','tableCode',
			    	'inningsCode','beforeCerdit','loginIP','flag']),
	listeners : {
		'load' : function(store, records, options) {
			proxyDownuserBetInfo.alwaysFun();
		}
	}
});

/** 基本信息-选择模式 */
proxyDownuserBetInfo.selModel = new Ext.grid.CheckboxSelectionModel({
	singleSelect : true,
	listeners : {
		'rowselect' : function(selectionModel, rowIndex, record) {
			//memberximadetail.deleteAction.enable();
			//memberximadetail.showAction.enable();
		},
		'rowdeselect' : function(selectionModel, rowIndex, record) {
			//memberximadetail.deleteAction.disable();
			//memberximadetail.showAction.disable();
		}
	}
});

/** 基本信息-数据列 */
proxyDownuserBetInfo.colModel = new Ext.grid.ColumnModel({
	defaults : {
		sortable : true,
		width : 90
	},
	columns : [proxyDownuserBetInfo.selModel, {
		hidden : true,
		header : 'ID',
		dataIndex : 'pdid'
	}, {
		header : '游戏平台',
		dataIndex : 'platformcode',
		width : 70
	}, {
		header : '帐号',
		dataIndex : 'account',
		editor : new Ext.form.TextField({
			style: 'border:0px;'
		})
	}, {
		header : '游戏',
		dataIndex : 'gamename'

	}, {
		header : '押注单号',
		dataIndex : 'betno'

	}, {
		header : '押注金额',
		dataIndex : 'betamount'
	}, {
		header : '有效投注额',
		dataIndex : 'validBetAmount'

	}, {
		header : '派彩金额',
		dataIndex : 'profitamount',
		renderer : function(v) {
			return Share.amount(v);
		}
	}, {
		header : '输赢金额',
		dataIndex : 'finalamount',
		renderer : function(v) {
			return Share.amount(v);
			
		}
	}, {
		header : '投注时间',
		dataIndex : 'betdate',
		width : 130,
		renderer : function(v) {
			return new Date(v).format('Y-m-d H:i:s');
		}
	}, {
		header : '下注前余额',
		dataIndex : 'beforeCerdit'
	}, {
		header : '游戏玩法',
		dataIndex : 'playType',
		renderer : function(v,cellMeta, record) {
			return Share.playType(record.data['platformcode'],record.data['gamecode'],v);
			
		}
	},{
		header : '桌子编号',
		dataIndex : 'tableCode'
	},{
		header : '局号',
		dataIndex : 'inningsCode'
	},{
		header : '登录IP',
		dataIndex : 'loginIP'
	},{
		header : '结算状态',
		dataIndex : 'flag',
		renderer : function(v) {
			return Share.flag(v);
		}
	}]
});

/** 查询 */
proxyDownuserBetInfo.searchField = new Ext.ux.form.SearchField({
	store : proxyDownuserBetInfo.store,
	paramName : 'optusername',
	emptyText : '请输入操作人名称',
	style : 'margin-left: 5px;'
});
/** 顶部工具栏 */
proxyDownuserBetInfo.tbar = [];

/** 底部工具条 */
proxyDownuserBetInfo.bbar = new Ext.PagingToolbar({
	pageSize : proxyDownuserBetInfo.pageSize,
	store : proxyDownuserBetInfo.store,
	displayInfo : true,
	//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
	items : [ '-', '&nbsp;', proxyDownuserBetInfo.pageSizeCombo ]
});

proxyDownuserBetInfo.alwaysFun = function() {
		Share.resetGrid(proxyDownuserBetInfo.grid);
};

/** 基本信息-表格 */
proxyDownuserBetInfo.grid = new Ext.grid.GridPanel({
	title : "会员投注明细",
	store : proxyDownuserBetInfo.store,
	colModel : proxyDownuserBetInfo.colModel,
	selModel : proxyDownuserBetInfo.selModel,
	tbar : proxyDownuserBetInfo.tbar,
	bbar : proxyDownuserBetInfo.bbar,
	autoScroll : 'auto',
	region : 'east',
	width : '50%',
	//autoExpandColumn :'remark',
	loadMask : true,
	viewConfig:{},
	stripeRows : true
});

//------------------结合-----------------
proxyDownuser.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			defaults: {
       	 		split: true,                 //是否有分割线
        		collapsible: true           //是否可以折叠
    		},
			height : index.tabPanel.getInnerHeight() - 1,
			items : [proxyDownuser.grid,proxyDownuserBetInfo.grid]
		});

</script>