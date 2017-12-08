<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	// ----------------------明细记录--------------------
	Ext.ns("Ext.market.memberximadetail"); // 自定义一个命名空间
	memberximadetail = Ext.market.memberximadetail; // 定义命名空间的别名
	memberximadetail = {
		all : '/manage/memberximadetail/queryMemberximadetail.do', // 所有会员洗码明细
		save : "/manage/memberximadetail/save.do",// 保存会员洗码明细
		del : "/manage/memberximadetail/del/",// 删除会员洗码明细
		pageSize : 30,// 每页显示的记录数
		OPTTYPEMAP : eval('(${opttypeMap})'),//注意括号
		GAMEPLATMAP : eval('(${gameplatMap})')//注意括号
	};
	/** 改变页的combo*/
	memberximadetail.pageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				memberximadetail.pageSize  = parseInt(comboBox.getValue());
				memberximadetail.bbar.pageSize  = parseInt(comboBox.getValue());
				memberximadetail.store.baseParams.limit = memberximadetail.pageSize;
				memberximadetail.store.baseParams.start = 0;
				memberximadetail.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	memberximadetail.pageSize = parseInt(memberximadetail.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	memberximadetail.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:false,
		baseParams : {
			uiid : -1,
			start : 0,
			limit : memberximadetail.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : memberximadetail.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'mxdid','gpid','uiid','amount','ymdstart','ymdend','optuiid','optusername','opttype','opttime','paramlog' ]),
		listeners : {
			'load' : function(store, records, options) {
				memberximadetail.alwaysFun();
			}
		}
	});
	/** 基本信息-选择模式 */
	memberximadetail.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				memberximadetail.deleteAction.enable();
				memberximadetail.showAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				memberximadetail.deleteAction.disable();
				memberximadetail.showAction.disable();
			}
		}
	});
	/** 基本信息-数据列 */
	memberximadetail.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 90
		},
		// 'mxdid','gpid','uiid','amount','ymdstart','ymdend','optuiid','optusername','opttype','opttime','paramlog'
		columns : [ memberximadetail.selModel, {
			hidden : true,
			header : '主键ID',
			dataIndex : 'mxdid'
		}, {
			hidden : true,
			header : '会员ID',
			dataIndex : 'uiid'
		}, {
			hidden : true,
			header : '操作人ID',
			dataIndex : 'optuiid'
		}, {
			header : '操作人名称',
			dataIndex : 'optusername',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '操作类型',
			dataIndex : 'opttype',
			renderer : function(v) {
				return Share.map(v, memberximadetail.OPTTYPEMAP, '');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '操作时间',
			dataIndex : 'opttime',
			width : 150,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '游戏平台',
			dataIndex : 'gpid',
			renderer : function(v) {
				return Share.map(v, memberximadetail.GAMEPLATMAP, '');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})	
		}, {
			header : '返水金额（元）',
			dataIndex : 'amount',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '洗码开始日期',
			dataIndex : 'ymdstart',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '洗码结束日期',
			dataIndex : 'ymdend',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '参数日志',
			dataIndex : 'paramlog',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		} ]
	});
	
	/** 查看 */
	memberximadetail.showAction = new Ext.Action({
		text : '查看',
		//text : '<fmt:message key="common.cancel"/>',
		iconCls : 'memberximadetail_add',
		handler : function() {
			memberximadetail.addWindow.setIconClass('memberximadetail_add'); // 设置窗口的样式
			memberximadetail.addWindow.setTitle('查看'); // 设置窗口的名称
			memberximadetail.addWindow.show().center(); // 显示窗口
			memberximadetail.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			var record = memberximadetail.grid.getSelectionModel().getSelected();
			memberximadetail.formPanel.getForm().loadRecord(record);
		}
	});
	/** 删除 */
	memberximadetail.deleteAction = new Ext.Action({
		text : '删除',
		iconCls : 'memberximadetail_delete',
		disabled : true,
		handler : function() {
			memberximadetail.delFun();
		}
	});
	/** 查询 */
	memberximadetail.searchField = new Ext.ux.form.SearchField({
		store : memberximadetail.store,
		paramName : 'optusername',
		emptyText : '请输入操作人名称',
		style : 'margin-left: 5px;'
	});
	/** 顶部工具栏 */
	memberximadetail.tbar = [ memberximadetail.showAction
	                          //, '-', memberximadetail.deleteAction
	                           , '-',memberximadetail.searchField
	                ];
	/** 底部工具条 */
	memberximadetail.bbar = new Ext.PagingToolbar({
		pageSize : memberximadetail.pageSize,
		store : memberximadetail.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', memberximadetail.pageSizeCombo ]
	});
	/** 基本信息-表格 */
	memberximadetail.grid = new Ext.grid.EditorGridPanel({
		title : "洗码明细记录",
		store : memberximadetail.store,
		colModel : memberximadetail.colModel,
		selModel : memberximadetail.selModel,
		tbar : memberximadetail.tbar,
		bbar : memberximadetail.bbar,
		autoScroll : 'auto',
		region : 'east',
		width : '50%',
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig:{},
		stripeRows : true
	});
	
	memberximadetail.gpidDisabledCombo = new Ext.form.ComboBox({
		fieldLabel : '游戏平台',
		hiddenName : 'gpid',
		name : 'gpid',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
			fields : ['v', 't'],
			data : Share.map2Ary(memberximadetail.GAMEPLATMAP)
		}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		disabled : true,
		anchor : '99%'
	});
	memberximadetail.opttypeDisabledCombo = new Ext.form.ComboBox({
		fieldLabel : '操作',
		hiddenName : 'opttype',
		name : 'opttype',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(memberximadetail.OPTTYPEMAP)
				}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		disabled : true,
		anchor : '99%'
	});

	/** 基本信息-详细信息的form */
	memberximadetail.formPanel = new Ext.form.FormPanel({
		region : 'center',
		autoScroll : true,
		frame : false,
		border: false,
    	style: 'border-bottom:0px;',
    	bodyStyle: 'padding:10px;background-color:transparent;',
		labelwidth : 70,
		defaultType : 'textfield',
		// 'mxdid','gpid','uiid','amount','ymdstart','ymdend','optuiid','optusername','opttype','opttime','paramlog'
		items : [ {
			xtype : 'hidden',
			fieldLabel : 'ID',
			name : 'mxmid'
		}, {
			xtype : 'hidden',
			fieldLabel : '会员ID',
			name : 'uiid'
		}, {
			xtype : 'hidden',
			fieldLabel : '操作人ID',
			name : 'optuiid'
		}, memberximadetail.gpidDisabledCombo, {
			disabled : true,
			fieldLabel : '返水金额（元）',
			name : 'amount'
		}, {
			disabled : true,
			fieldLabel : '洗码开始日期',
			name : 'ymdstart'
		}, {
			disabled : true,
			fieldLabel : '洗码结束日期',
			name : 'ymdend'
		}, {
			disabled : true,
			fieldLabel : '操作时间',
			name : 'opttime'
		}, memberximadetail.opttypeDisabledCombo, {
			disabled : true,
			fieldLabel : '参数日志',
			maxLength : 200,
			xtype : 'textarea',
			anchor : '99%',
			name : 'paramlog'
		}
		]
	});

	/** 编辑新建窗口 */
	memberximadetail.addWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 360,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ memberximadetail.formPanel ],
		buttons : [ {
			text : '关闭',
			handler : function() {
				memberximadetail.addWindow.hide();
			}
		} ]
	});
	
	
	memberximadetail.alwaysFun = function() {
		Share.resetGrid(memberximadetail.grid);
		memberximadetail.deleteAction.disable();
		memberximadetail.showAction.disable();
	};
	
	memberximadetail.delFun = function() {
		var record = memberximadetail.grid.getSelectionModel().getSelected();
		Ext.Msg.confirm('提示', '确定要删除这条记录吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : memberximadetail.del + record.data.mxdid + ".do",
					callback : function(json) {
						memberximadetail.alwaysFun();
						memberximadetail.store.reload();
					}
				});
			}
		});
	};
	// ----------------------总记录--------------------
	Ext.ns("Ext.market.memberximamain"); // 自定义一个命名空间
	memberximamain = Ext.market.memberximamain; // 定义命名空间的别名
	memberximamain = {
		all : '/manage/memberximamain/queryMemberximamain.do', // 所有会员洗码总记录
		save : "/manage/memberximamain/save.do",// 保存会员洗码总记录
		del : "/manage/memberximamain/del/",// 删除会员洗码总记录
		clear : "/manage/memberximamain/clear/",// 洗码清零
		force : "/manage/memberximamain/force/",// 强制洗码
		enable : "/manage/memberximamain/enable/",//审核通过给会员钱包加钱
		batchenable : "/manage/memberximamain/batchEnable.do",//批量审核通过给会员钱包加钱
		disable : "/manage/memberximamain/disable/",// 解锁会员洗码总记录
		clearingAll : "/manage/memberximamain/queryBetCountLog.do",//结算
		ptAll : "/manage/memberximamain/queryBetCountLogPT.do",//结算PT
		down:'/manage/memberximamain/toDownloadReport.do',//导出会员洗码记录管理
		batchDetailUrl:'/manage/memberximamain/queryNoAccount.do',//查询所有未入账
		pageSize : 30,// 每页显示的记录数
		LOCKEDMAP : eval('(${lockedMap})'),//注意括号
		GAMEPLATMAP : eval('(${gameplatMap})')//注意括号
	};
	/** 改变页的combo*/
	memberximamain.pageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				memberximamain.pageSize  = parseInt(comboBox.getValue());
				memberximamain.bbar.pageSize  = parseInt(comboBox.getValue());
				memberximamain.store.baseParams.limit = memberximamain.pageSize;
				memberximamain.store.baseParams.start = 0;
				memberximamain.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	memberximamain.pageSize = parseInt(memberximamain.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	memberximamain.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:true,
		baseParams : {
			start : 0,
			limit : memberximamain.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : memberximamain.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'mxmid','gpid','uiid','account','name','total','ymdstart','ymdend','updatetime','locked' ]),
		listeners : {
			'load' : function(store, records, options) {
				memberximamain.alwaysFun();
			}
		}
	});
	/** 基本信息-选择模式 */
	memberximamain.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				//memberximamain.deleteAction.enable();
				memberximamain.forceAction.enable();
				//memberximamain.clearAction.enable();
				if(record.data.locked==1){
					memberximamain.enableAction.disable();
					
				}
				if(record.data.locked==0){
					memberximamain.enableAction.enable();
					memberximamain.disableAction.enable();
				}
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				//memberximamain.deleteAction.disable();
				memberximamain.forceAction.disable();
				//memberximamain.clearAction.disable();
				memberximamain.enableAction.disable();
				memberximamain.disableAction.disable();
			}
		}
	});
	/** 基本信息-数据列 */
	memberximamain.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 100
		},
		// 'mxmid','gpid','uiid','account','name','total','ymdstart','ymdend','updatetime','locked'
		columns : [ memberximamain.selModel, {
			hidden : true,
			header : '主键ID',
			dataIndex : 'mxmid'
		}, {
			hidden : true,
			header : '会员ID',
			dataIndex : 'uiid'
		}, {
			header : '状态',
			dataIndex : 'locked',
			renderer : function(v) {
				//return Share.map(v, memberximamain.LOCKEDMAP, '');
				if(v == 1){
					return '<span style="color:green;">已入账</span>';
				}else if(v == 2){
					return '<span style="color:#FF6600;">审核失败</span>';
				}else{
					return '<span style="color:red;">未入账</span>';
				}
			}	
		}, {
			header : '游戏平台',
			dataIndex : 'gpid'
		}, {
			header : '会员账号',
			dataIndex : 'account',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			hidden : false,
			header : '会员姓名',
			dataIndex : 'name',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '返水总金额（元）',
			dataIndex : 'total'
		}, {
			header : '洗码开始日期',
			dataIndex : 'ymdstart'
		}, {
			header : '洗码结束日期',
			dataIndex : 'ymdend'
		}, {
			header : '更新时间',
			dataIndex : 'updatetime',
			width : 150,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			}
		} ]
	});
	
	/** 新建 */
	memberximamain.addAction = new Ext.Action({
		text : '结算洗码',
		disabled : false,
		iconCls : 'Coinsadd',
		handler : function() {
			memberximamain.addWindow.setIconClass('Coinsadd'); // 设置窗口的样式
			memberximamain.addWindow.setTitle('结算洗码'); // 设置窗口的名称
			memberximamain.addWindow.show().center(); // 显示窗口
			//memberximamain.clearingstore.reload();
			$("#liveAccount").val('');
			memberximamain.delclearinggrid();
		}
	});
	/** PT洗码结算 */
	memberximamain.addPTAction = new Ext.Action({
		text : 'PT洗码',
		disabled : false,
		iconCls : 'Coinsadd',
		handler : function() {
			memberximamain.addPTWindow.setIconClass('Coinsadd'); // 设置窗口的样式
			memberximamain.addPTWindow.setTitle('PT结算洗码'); // 设置窗口的名称
			memberximamain.addPTWindow.show().center(); // 显示窗口
			//memberximamain.clearingstorePT.reload();
			$("#ptAccount").val('');
			memberximamain.delclearinggridPT();
		}
	});
	/** 删除 */
	memberximamain.deleteAction = new Ext.Action({
		text : '删除',
		iconCls : 'memberximamain_delete',
		disabled : true,
		handler : function() {
			memberximamain.delFun();
		}
	});
	/** 强制洗码 */
	memberximamain.forceAction = new Ext.Action({
		text : '强制洗码',
		iconCls : 'memberximamain_force',
		disabled : true,
		handler : function() {
			memberximamain.forceFun();
		}
	});
	/** 洗码清零 */
	memberximamain.clearAction = new Ext.Action({
		text : '洗码清零',
		iconCls : 'memberximamain_clear',
		disabled : true,
		handler : function() {
			memberximamain.clearFun();
		}
	});
	/** 通过审核 */
	memberximamain.enableAction = new Ext.Action({
		text : '通过',
		iconCls : 'Coinsadd',
		disabled : true,
		handler : function() {
			memberximamain.enableFun();
		}
	});
	/** 审核失败 */
	memberximamain.disableAction = new Ext.Action({
		text : '失败',
		iconCls : 'Coinsadd',
		disabled : true,
		handler : function() {
			memberximamain.disableFun();
		}
	});

	// ----------------------批量入账 begin--------------------
	/** 批量入账 */
	memberximamain.batchAction = new Ext.Action({
		text : '批量入账',
		iconCls : 'Coinsadd',
		disabled : false,
		handler : function() {
			var record = memberximamain.grid.getSelectionModel().getSelected();
			memberximamain.addBatchWindow.setIconClass('Coinsadd'); // 设置窗口的样式
			memberximamain.addBatchWindow.setTitle('批量入账'); // 设置窗口的名称
			memberximamain.addBatchWindow.show().center(); // 显示窗口
			memberximamain.batchDetail.reload();
		}
	});
	
		/** 基本信息-选择模式 */
	memberximamain.batchSelModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
			 },
			'rowdeselect' : function(selectionModel, rowIndex, record) {
			 }
		}
	});
	
	
	/** 批量入账详情数据源 */
	memberximamain.batchDetail = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : true,
		baseParams : {
			reportdate:'',
			start : 0,
			limit : 100
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : memberximamain.batchDetailUrl
		}),
		fields : [ 'mxmid','gpid','uiid','account','name','total','ymdstart','ymdend','updatetime','locked' ],
		listeners : {
			'load' : function(store, records, options) {
			}
		}
	});
	/** 改变页的combo*/
	memberximamain.batchPageSizeCombo = new Share.pageSizeCombo({
		value : '100',
		listeners : {
			select : function(comboBox) {
				memberximamain.pageSize  = parseInt(comboBox.getValue());
				memberximamain.batchbbar.pageSize  = parseInt(comboBox.getValue());
				memberximamain.batchDetail.baseParams.limit = memberximamain.pageSize;
				memberximamain.batchDetail.baseParams.start = 0;
				memberximamain.batchDetail.load();
			}
		}
	});
	/** 分页 */
	memberximamain.batchbbar = new Ext.PagingToolbar({
		pageSize : 100,
		store : memberximamain.batchDetail,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', memberximamain.batchPageSizeCombo ]
	});

	
	/** 查询按钮 */
	memberximamain.batchsearchAction = new Ext.Action({
			text : '查询',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				memberximamain.batchsearchFun();
			}
		
	});
	
	memberximamain.batchSearchParams = function(){
		var obj = {};
		 obj.account =  $("#batchuaccount").val();
		 obj.startDate = $("#batchStartDate").val();
		 obj.endDate = $("#batchEndDate").val();
	     return obj;
	}
	
	memberximamain.batchsearchFun = function(){
		memberximamain.batchDetail.load({params: memberximamain.batchSearchParams()});
	}
	
	memberximamain.batchDetail.on('beforeload',function(store, options){
	    memberximamain.batchDetail.baseParams = memberximamain.batchSearchParams();
	});
	
	
		/** 顶部工具栏 */
	memberximamain.batchtbar = [
					'会员账号:',{id:'batchuaccount',xtype:'textfield',width:100},
	                ' 开始日期:',{ id:'batchStartDate',xtype:'datetimefield',format:'Y-m-d H:i:s'},'-',
            		' 至',{ id:'batchEndDate',xtype:'datetimefield',format:'Y-m-d H:i:s'},
	                '-','&nbsp;',memberximamain.batchsearchAction
	          ];
	
	
	/** 批量入账列表 */
	memberximamain.batchgrid = new Ext.grid.EditorGridPanel({
		store : memberximamain.batchDetail,
		autoScroll : 'auto',
		region : 'center',
		tbar:memberximamain.batchtbar,
		bbar:memberximamain.batchbbar,
		selModel : memberximamain.batchSelModel,
		loadMask : true,
		stripeRows : true,
		border: false,
		columns : [memberximamain.batchSelModel,
		{
			hidden : true,
			header : '主键ID',
			dataIndex : 'mxmid'
		}, {
			hidden : true,
			header : '会员ID',
			dataIndex : 'uiid'
		}, {
			header : '状态',
			dataIndex : 'locked',
			renderer : function(v) {
				if(v == 1){
					return '<span style="color:green;">已入账</span>';
				}else if(v == 2){
					return '<span style="color:#FF6600;">审核失败</span>';
				}else{
					return '<span style="color:red;">未入账</span>';
				}
			}	
		}, {
			header : '游戏平台',
			dataIndex : 'gpid'
		}, {
			header : '会员账号',
			dataIndex : 'account',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			hidden : false,
			header : '会员姓名',
			dataIndex : 'name',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '返水总金额（元）',
			dataIndex : 'total'
		}, {
			header : '洗码开始日期',
			dataIndex : 'ymdstart'
		}, {
			header : '洗码结束日期',
			dataIndex : 'ymdend'
		}, {
			header : '更新时间',
			dataIndex : 'updatetime',
			width : 150,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			}
		}]
	});
	
	/** 批量入账窗口 */
	memberximamain.addBatchWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 450,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [memberximamain.batchgrid],
		buttons : [ {
			text : '保存',
			handler : function() {
				memberximamain.batchAccountFun();
			}
		}, {
			text : '取消',
			handler : function() {
				memberximamain.addBatchWindow.hide();
			}
		} ]
	});
	
	// ----------------------批量入账 end--------------------
	
	
	
	/** 查询按钮 */
	memberximamain.searchAction = new Ext.Action({
		text : '查询',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				var account=$("#uaccount").val(); //会员账号
				var startDate=$("#memberximamainstartDate").val(); //开始时间 
				var endDate=$("#memberximamainendDate").val(); //结束时间
				memberximamain.store.baseParams.account = account;
				memberximamain.store.baseParams.startDate = startDate;
				memberximamain.store.baseParams.endDate = endDate;
				memberximamain.store.reload();
			}
	});
	/** 导出报表按钮 */
	memberximamain.downAction = new Ext.Action({
			text : '导出报表',
			iconCls : 'Diskdownload',
			disabled : false,
			handler : function() {
				memberximamain.downReport();
			}
	});
	/** 导出报表函数 */
	memberximamain.downReport = function() {
		 //发送请求
		 window.open(memberximamain.down + "?account="+$("#uaccount").val()+"&startDate="+$("#memberximamainstartDate").val()+"&endDate="+$("#memberximamainendDate").val());
	};
	/**日期条件  -- 开始时间*/
 	memberximamain.startDateField = new Ext.form.DateField({
  			id:'memberximamainstartDate',
        	showToday:true,
        	format:'Y-m-d',
        	invalidText:'日期输入非法',
        	allowBlank : false,
        	width:100
 	});
 	/** 日期条件  -- 结束时间*/
 	memberximamain.endDateField = new Ext.form.DateField({
  			id:'memberximamainendDate',
        	showToday:true,
        	format:'Y-m-d',
        	invalidText:'日期输入非法',
        	allowBlank : false,
        	width:100 
 	});
	/** 顶部工具栏 */
	memberximamain.tbar = [ memberximamain.addAction,'-',memberximamain.addPTAction,
					'-',memberximamain.batchAction,
	                '-', memberximamain.enableAction, '-',memberximamain.disableAction,
	                '-',memberximamain.downAction,
	                '-','会员账号:',{id:'uaccount',xtype:'textfield',width:100},
	                '-','更新时间:',memberximamain.startDateField,'~',memberximamain.endDateField,
	                '-','&nbsp;',memberximamain.searchAction
	          ];
	/** 底部工具条 */
	memberximamain.bbar = new Ext.PagingToolbar({
		pageSize : memberximamain.pageSize,
		store : memberximamain.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', memberximamain.pageSizeCombo ]
	});
	/** 基本信息-表格 */
	memberximamain.grid = new Ext.grid.EditorGridPanel({
		title : "会员洗码总记录",
		store : memberximamain.store,
		colModel : memberximamain.colModel,
		selModel : memberximamain.selModel,
		tbar : memberximamain.tbar,
		bbar : memberximamain.bbar,
		autoScroll : 'auto',
		region : 'center',
		width:800,
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig:{},
		stripeRows : true,
		listeners : {
			'cellclick' : function(obj, rowIndex, columnIndex, e) {
				var record = memberximamain.grid.getSelectionModel().getSelected();
				if (record) {
					// 更新明细记录
					memberximadetail.store.baseParams.uiid = record.data.uiid;
					memberximadetail.store.reload();
				}
			}
		}
	});
	
	memberximamain.gpidCombo = new Ext.form.ComboBox({
		fieldLabel : '游戏平台',
		hiddenName : 'gpid',
		name : 'gpid',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(memberximamain.GAMEPLATMAP)
				}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		anchor : '99%'
	});
	
	var myCheckboxItems = [];
	var boxMap = memberximamain.GAMEPLATMAP;
	for (var key in boxMap) {
        myCheckboxItems.push({
        	boxLabel : boxMap[key],
        	name : "platboxs",
        	value : key+'#'+boxMap[key],
        	checked : true
        });
    }
	
	/**
	 * 平台复选框
	 */
	memberximamain.gpidCheckbox = new Ext.form.CheckboxGroup({
		xtype : 'checkboxgroup', 
		columns : 3, 
		name : 'powerName', 
		allowBlank : false, 
		fieldLabel : '游戏平台',
		defaultType: 'checkbox', // each item will be a checkbox
		items: myCheckboxItems
	});
	
	memberximamain.lockedCombo = new Ext.form.ComboBox({
		fieldLabel : '状态',
		hiddenName : 'locked',
		name : 'locked',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(memberximamain.LOCKEDMAP)
				}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		value : "0",
		anchor : '99%'
	});
	
	//日期
	memberximamain.startDateField = new Ext.form.DateField({
		fieldLabel : '洗码日期',
		id:'startTime',
        showToday:true,
        format:'Y-m-d',
        invalidText:'日期输入非法',
        allowBlank : false,
        width:150 
	});
	
	/** 基本信息-详细信息的form */
	memberximamain.formPanel = new Ext.form.FormPanel({
		region : 'center',
		autoScroll : true,
		frame : false,
		border: false,
    	style: 'border-bottom:0px;',
    	bodyStyle: 'padding:10px;background-color:transparent;',
		labelwidth : 70,
		defaultType : 'textfield',
		// 'mxmid','gpid','uiid','account','name','total','ymdstart','ymdend','updatetime','locked'
		items : [ {
			xtype : 'hidden',
			fieldLabel : 'ID',
			name : 'mxmid'
		},{
			fieldLabel : '会员账号',
			name : 'account',
			allowBlank : false
			
		}, memberximamain.startDateField, memberximamain.gpidCheckbox
		]
	});
	
	/** 改变页的combo*/
	memberximamain.clearingpageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				memberximamain.pageSize  = parseInt(comboBox.getValue());
				memberximamain.clearingbbar.pageSize  = parseInt(comboBox.getValue());
				memberximamain.clearingstore.baseParams.limit = memberximamain.pageSize;
				memberximamain.clearingstore.baseParams.start = 0;
				memberximamain.clearingstore.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	memberximamain.clearingpageSize = parseInt(memberximamain.clearingpageSizeCombo.getValue());
	
	memberximamain.clearingselModel = new Ext.grid.CheckboxSelectionModel();
	memberximamain.clearingselPTModel = new Ext.grid.CheckboxSelectionModel();
	memberximamain.clearingstore = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : false,
		baseParams : {
			startDate : "",
			endDate : "",
			platformcode : ""
			//start : 0,
			//limit : memberximamain.clearingpageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : memberximamain.clearingAll
		}),
		fields : [ 'account','uname','betTotel','platformcode','gamename','betAmountTotal','profitamountTotal','validBetAmountTotal','betdate','ximaAmount','clearingstatus'],
		listeners : {
			'load' : function(store, records, options) {
				// user.roleSelModel.clearSelections();
			}
		}
	});
	
	/*PT结算数据查询*/
	memberximamain.clearingstorePT = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : false,
		baseParams : {
			startDate : "",
			endDate : "",
			start : 0,
			limit : memberximamain.clearingpageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : memberximamain.ptAll
		}),
		fields : [ 'account','uname','betTotel','platformcode','gamename','betAmountTotal','profitamountTotal','validBetAmountTotal','betdate','ximaAmount','clearingstatus'],
		listeners : {
			'load' : function(store, records, options) {
				// user.roleSelModel.clearSelections();
			}
		}
	});
	
	/**结算日期**/
memberximamain.clearingDatetimeField = new  Ext.form.DateField({ 
		id:'memberClearingDate',
		showToday:true,
		format:'Y-m-d',
		invalidText:'日期输入非法',
		value:new Date().format("Y-m-d"),
		width:120,
		listeners:{
			'select':function(v){
				//memberximamain.delclearinggrid();
				//Share.resetGrid(memberximamain.clearinggrid);
				//memberximamain.clearingstore.baseParams.startDate = Ext.util.Format.date(v.getValue(),"Y-m-d");
				//memberximamain.clearingstore.baseParams.endDate = Ext.util.Format.date(v.getValue(),"Y-m-d");
				//var platformcode = [];
				//$("input[name='memberClearingType'][checked]").each(function(){
					//var gc = $(this).val();
					//var gcsub = gc.substring(gc.indexOf('#')+1,gc.length);
      				//platformcode.push("'"+gcsub+"'");                     
    			//}); 
    			//memberximamain.clearingstore.baseParams.platformcode=platformcode;
				//memberximamain.clearingstore.load();
			}
		}
});

/*PT结算日期*/
memberximamain.clearingDatetimeFieldPT = new  Ext.form.DateField({ 
		id:'memberPTClearingDate',
		showToday:true,
		format:'Y-m-d',
		invalidText:'日期输入非法',
		value:new Date().format("Y-m-d"),
		width:120,
		listeners:{
			'select':function(v){
				//memberximamain.delclearinggridPT();
				//Share.resetGrid(memberximamain.clearinggridPT);
				//memberximamain.clearingstorePT.baseParams.startDate = Ext.util.Format.date(v.getValue(),"Y-m-d");
				//memberximamain.clearingstorePT.baseParams.endDate = Ext.util.Format.date(v.getValue(),"Y-m-d");
				//memberximamain.clearingstorePT.load();
			}
		}
});

/**真人洗码查询*/                
memberximamain.liveSearchAction = new Ext.Action({
			text : '搜索',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				memberximamain.delclearinggrid();
				Share.resetGrid(memberximamain.clearinggrid);
				memberximamain.clearingstore.baseParams.startDate = $("#memberClearingDate").val();
				memberximamain.clearingstore.baseParams.endDate = $("#memberClearingDate").val()
				memberximamain.clearingstore.baseParams.account = $("#liveAccount").val();
				var platformcode = [];
				$("input[name='memberClearingType'][checked]").each(function(){
					var gc = $(this).val();
					var gcsub = gc.substring(gc.indexOf('#')+1,gc.length);
      				platformcode.push("'"+gcsub+"'");                     
    			}); 
    			memberximamain.clearingstore.baseParams.platformcode=platformcode;
				memberximamain.clearingstore.load();
			}
	});

/**PT洗码查询*/                
memberximamain.ptSearchAction = new Ext.Action({
			text : '搜索',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				memberximamain.delclearinggridPT();
				Share.resetGrid(memberximamain.clearinggridPT);
				memberximamain.clearingstorePT.baseParams.startDate =  $("#memberPTClearingDate").val();
				memberximamain.clearingstorePT.baseParams.endDate =  $("#memberPTClearingDate").val();
				memberximamain.clearingstorePT.baseParams.account = $("#ptAccount").val();
				memberximamain.clearingstorePT.load();
			}
	});

memberximamain.clearingtbar = ['结算日期：',memberximamain.clearingDatetimeField,'-','&nbsp;','结算平台：',${pt},'-','&nbsp;','会员账号:',
                        new Ext.form.TextField({ id:'liveAccount',width:100 }),'-','&nbsp;',memberximamain.liveSearchAction];
memberximamain.clearingtbarPT = ['结算日期：',memberximamain.clearingDatetimeFieldPT,'-','&nbsp;', '会员账号:',
                        new Ext.form.TextField({ id:'ptAccount',width:100 }),'-','&nbsp;',memberximamain.ptSearchAction];

/** 底部工具条 */
memberximamain.clearingbbar = new Ext.PagingToolbar({
		pageSize : memberximamain.clearingpageSize,
		store : memberximamain.clearingstorePT,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', memberximamain.clearingpageSizeCombo ]
	});
	
memberximamain.clearinggrid = new Ext.grid.EditorGridPanel({
	store : memberximamain.clearingstore,
	sm : memberximamain.clearingselModel,
	tbar:memberximamain.clearingtbar,
	//bbar:memberximamain.clearingbbar,
	autoScroll : 'auto',
	region : 'center',
	loadMask : true,
	stripeRows : true,
	border: false,
	viewConfig : {},
	columns : [ memberximamain.clearingselModel,{
			header : "游戏平台",
			dataIndex : 'platformcode',
			width : 80
		},{
			header : "游戏名称",
			dataIndex : 'gamename',
			width : 80
		},{
			header : "会员账号",
			dataIndex : 'account',
			width : 80,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : "会员姓名",
			dataIndex : 'uname',
			width : 80
		},{
			header : "注单数量",
			dataIndex : 'betTotel',
			width : 80
		},{
			header : "投注金额",
			dataIndex : 'betAmountTotal',
			width : 80
		},{
			header : "有效投注额",
			dataIndex : 'validBetAmountTotal',
			width : 100,
			renderer : function(v) {
				return '<span style="color:blue;">'+v+'</span>';
			}
		},{
			header : "洗码值#比例",
			dataIndex : 'ximaAmount',
			width : 120
		}, {
			header : '派彩金额',
			dataIndex : 'profitamountTotal',
			renderer : function(v) {
				return Share.amount(v);
			}
		},{
			header : "结算状态",
			dataIndex : 'clearingstatus',
			width : 80,
			renderer : function(v) {
				if(v == 0){
					return '<span style="color:red;">未结算</span>';
				}else{
					return '<span style="color:green;">已结算</span>';
				}
			}
		}],
	listeners : {}
});

memberximamain.clearinggridPT = new Ext.grid.EditorGridPanel({
	store : memberximamain.clearingstorePT,
	sm : memberximamain.clearingselPTModel,
	tbar:memberximamain.clearingtbarPT,
	bbar:memberximamain.clearingbbar,
	autoScroll : 'auto',
	region : 'center',
	loadMask : true,
	stripeRows : true,
	border: false,
	viewConfig : {},
	columns : [ memberximamain.clearingselPTModel,{
			header : "游戏平台",
			dataIndex : 'platformcode',
			width : 80
		},{
			header : "游戏名称",
			dataIndex : 'gamename',
			width : 80
		},{
			header : "会员账号",
			dataIndex : 'account',
			width : 80,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : "会员姓名",
			dataIndex : 'uname',
			width : 80
		},{
			header : "注单数量",
			dataIndex : 'betTotel',
			width : 80
		},{
			header : "投注金额",
			dataIndex : 'betAmountTotal',
			width : 80
		},{
			header : "有效投注额",
			dataIndex : 'validBetAmountTotal',
			width : 100,
			renderer : function(v) {
				return '<span style="color:blue;">'+v+'</span>';
			}
		},{
			header : "洗码值#比例",
			dataIndex : 'ximaAmount',
			width : 120
		}, {
			header : '派彩金额',
			dataIndex : 'profitamountTotal',
			renderer : function(v) {
				return Share.amount(v);
			}
		},{
			header : "结算状态",
			dataIndex : 'clearingstatus',
			width : 80,
			renderer : function(v) {
				if(v == 0){
					return '<span style="color:red;">未结算</span>';
				}else{
					return '<span style="color:green;">已结算</span>';
				}
			}
		}],
	listeners : {}
});

memberximamain.delclearinggrid = function(){
	memberximamain.clearingstore.each(function(record){
		 memberximamain.clearingstore.remove(record);//删除数据
	});
}

memberximamain.delclearinggridPT = function(){
	memberximamain.clearingstorePT.each(function(record){
		 memberximamain.clearingstorePT.remove(record);//删除数据
	});
}

	/** 编辑新建窗口 */
	memberximamain.addWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 450,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ memberximamain.clearinggrid ],
		buttons : [ {
			text : '保存',
			handler : function() {
				memberximamain.saveFun();
			}
		}, {
			text : '取消',
			handler : function() {
				memberximamain.addWindow.hide();
			}
		} ]
	});
	
	/** 编辑新建窗口 */
	memberximamain.addPTWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 450,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ memberximamain.clearinggridPT ],
		buttons : [ {
			text : '保存',
			handler : function() {
				memberximamain.saveFunPT();
			}
		}, {
			text : '取消',
			handler : function() {
				memberximamain.addPTWindow.hide();
			}
		} ]
	});
	
	
	memberximamain.alwaysFun = function() {
		Share.resetGrid(memberximamain.grid);
		memberximamain.deleteAction.disable();
		memberximamain.forceAction.disable();
		memberximamain.clearAction.disable();
		memberximamain.disableAction.disable();
		memberximamain.enableAction.disable();
	};
	

	memberximamain.saveFun = function() {
		var selections = memberximamain.clearinggrid.getSelectionModel().getSelections();//获取选择的数据
		if(selections.length <= 0){
			Ext.Msg.alert("提示", "请至少选择一个用户进行结算洗码!");
			return;
		}
		var clearingDate = $("#memberClearingDate").val();
		
		var itcIds = [];
		$("input[name='memberClearingType'][checked]").each(function(){
			itcIds.push($(this).val());
		});

		var ids = [];
		for ( var i = 0; i < selections.length; i++) {
			if(selections[i].data.clearingstatus == 0){
				ids.push(selections[i].data.account);
			}
		}
		
		var params = {
			gameplat : itcIds,
			startTime : clearingDate,
			accounts : ids
		};
		// 发送请求
		Share.AjaxRequest({
			url : memberximamain.save,
			params : params,
			callback : function(json) {
				memberximamain.addWindow.hide();
				memberximamain.alwaysFun();
				memberximamain.store.reload();
			}
		});
	};
	
	memberximamain.saveFunPT = function() {
		var selections = memberximamain.clearinggridPT.getSelectionModel().getSelections();//获取选择的数据
		if(selections.length <= 0){
			Ext.Msg.alert("提示", "请至少选择一个用户进行结算洗码!");
			return;
		}
		var clearingDate = $("#memberPTClearingDate").val();
		
		var itcIds = ['8#PT'];
		var ids = [];
		for ( var i = 0; i < selections.length; i++) {
			if(selections[i].data.clearingstatus == 0){
				ids.push(selections[i].data.account);
			}
		}
		
		var params = {
			gameplat : itcIds,
			startTime : clearingDate,
			accounts : ids
		};
		// 发送请求
		Share.AjaxRequest({
			url : memberximamain.save,
			params : params,
			callback : function(json) {
				memberximamain.addPTWindow.hide();
				memberximamain.alwaysFun();
				memberximamain.store.reload();
			}
		});
	};
	
	memberximamain.delFun = function() {
		var record = memberximamain.grid.getSelectionModel().getSelected();
		Ext.Msg.confirm('提示', '确定要删除这条记录吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : memberximamain.del + record.data.mxmid + ".do",
					callback : function(json) {
						memberximamain.alwaysFun();
						memberximamain.store.reload();
					}
				});
			}
		});
	};

	memberximamain.forceFun = function(){
		var record = memberximamain.grid.getSelectionModel().getSelected();
		if (record.data.locked==1) { // 锁定状态 0未锁定 1锁定
			Ext.Msg.alert('提示', '该记录已被锁定，请先解锁后再操作！');
			return;
		}
		Ext.Msg.confirm('提示', '确定要对此会员['+record.data.account+']进行"强制洗码"操作吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : memberximamain.force + record.data.mxmid + ".do",
					callback : function(json) {
						memberximamain.alwaysFun();
						memberximamain.store.reload();
						memberximadetail.store.reload();
					}
				});
			}
		});
	};
	memberximamain.clearFun = function(){
		var record = memberximamain.grid.getSelectionModel().getSelected();
		if (record.data.locked==1) { // 锁定状态 0未锁定 1锁定
			Ext.Msg.alert('提示', '该记录已被锁定，请先解锁后再操作！');
			return;
		}
		Ext.Msg.confirm('提示', '确定要对此会员['+record.data.account+']进行"洗码清零"操作吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : memberximamain.clear + record.data.mxmid + ".do",
					callback : function(json) {
						memberximamain.alwaysFun();
						memberximamain.store.reload();
						memberximadetail.store.reload();
					}
				});
			}
		});
	};
	/** 批量入账*/
	memberximamain.batchAccountFun = function(){
		var arrayId=new Array();
		var record = memberximamain.batchgrid.selModel.selections.items;
		for(var i=0;i<record.length;i++){
			arrayId.push(record[i].data.mxmid);
		}
		Ext.Msg.confirm('提示', '确定要通过审核吗?通过审核后系统会将洗码金额添加到玩家钱包。', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : memberximamain.batchenable+"?mxmid="+arrayId,
					callback : function(json) {
						Share.resetGrid(memberximamain.batchgrid);
						memberximamain.batchDetail.reload();
						memberximamain.alwaysFun();
						memberximamain.store.reload();
					}
				});
			}
		});
	};
	
	memberximamain.enableFun = function(){
		var record = memberximamain.grid.getSelectionModel().getSelected();
		if (record.data.locked==1) { // 锁定状态 0未锁定 1锁定
			Ext.Msg.alert('提示', '此记录已经通过审核。');
			return;
		}
		Ext.Msg.confirm('提示', '确定要通过审核吗?通过审核后系统会将洗码金额添加到玩家钱包。', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : memberximamain.enable + record.data.mxmid + ".do",
					callback : function(json) {
						memberximamain.alwaysFun();
						memberximamain.store.reload();
					}
				});
			}
		});
	};
	memberximamain.disableFun = function(){
		var record = memberximamain.grid.getSelectionModel().getSelected();
		if (record.data.locked==2) {
			Ext.Msg.alert('提示', '此记录已经是[审核失败]状态');
			return;
		}
		Ext.Msg.confirm('提示', '确定次记录[审核失败]吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : memberximamain.disable + record.data.mxmid + ".do",
					callback : function(json) {
						memberximamain.alwaysFun();
						memberximamain.store.reload();
					}
				});
			}
		});
	};
	
	// -------------------------接合------------------------
	
	memberximamain.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'border',
		boder : false,
		defaults: {
       	 	split: true,                 //是否有分割线
        	collapsible: true           //是否可以折叠
    	},
		height : index.tabPanel.getInnerHeight() - 1,
		items : [ memberximamain.grid, memberximadetail.grid ]
	});
</script>
