<div id="${id}"></div>
<script type="text/javascript">
Ext.QuickTips.init();
Ext.ns("Ext.market.proxyInfos"); // 自定义一个命名空间
proxyInfo = Ext.market.proxyInfos; // 定义命名空间的别名
proxyInfo = {
	all : '/manage/proxy/queryProxyInfo.do',// 加载所有
	save : "/manage/proxy/saveProxyInfo.do",//保存
	proxyset : "/manage/proxyset/save.do",//代理占成设置
	memberSetProxy:"/manage/proxy/memberSetProxy.do",//会员转代理
	clearingAll:"/manage/proxy/queryClearing.do",//加载结算全部用户
	clearing : "/manage/proxy/clearing.do",	//结算
	ximaAll : "/manage/proxy/queryXima.do",//加载结算全部用户
	xima : "/manage/proxy/xima.do",	//结算
	isXimaFlag : eval('(${fields.ixXimaFlag})'),
	paymethods:eval('(${paymethodsMap})'),
	deposittype:eval('(${deposittypeMap})'),
	pageSize : 30, // 每页显示的记录数
	memStatus : eval('(${fields.memStatus})'),
	memGrade:eval('(${fields.memGrade})'),
	clearingtype:eval('(${fields.proxyclearingtype})'),
	memberDepositUrl:"/manage/proxy/queryMemberDepositPayOrderlog.do", //会员存款记录
	memberPayUrl:"/manage/proxy/queryMemberPayOrderlog.do", //会员提款记录
	memberPunishUrl:"/manage/proxy/queryMemberPunishOrderlog.do",  //会员扣款记录
	isManage : eval('${isManage}'),//是否为账务：0否，1是
	//ENABLED : eval('(${fields.enabled})')
};

//设置基本的是否选项
proxyInfo.baseSelectStore = new Ext.data.SimpleStore({  
    fields : ['key', 'value'],  
   data : [['开启', '1'], ['关闭', '0']]  
});

/** 改变页的combo */
proxyInfo.pageSizeCombo = new Share.pageSizeCombo({
			value : '30',
			listeners : {
				select : function(comboBox) {
					proxyInfo.pageSize = parseInt(comboBox.getValue());
					proxyInfo.bbar.pageSize = parseInt(comboBox.getValue());
					proxyInfo.store.baseParams.limit = proxyInfo.pageSize;
					proxyInfo.store.baseParams.start = 0;
					proxyInfo.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
proxyInfo.pageSize = parseInt(proxyInfo.pageSizeCombo.getValue());
/** 基本信息-数据源 */
proxyInfo.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : proxyInfo.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				
				method : 'POST',
				url : proxyInfo.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['uiid', 'account', 'uname', 'identitycard', 'phone','returnscale','ximascale','pyid',
				'email', 'qq', 'birthday', 'grade', 'url',
				'status','createDate','updateDate','lowerUser','loginip','logincount','isximaflag','clearingtype']),
			listeners : {
				'load' : function(store, records, options) {
					proxyInfo.alwaysFun();
				}
			}
		});
/** 基本信息-选择模式 */
proxyInfo.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
			    if(proxyInfo.isManage =='1'){
			   	 	proxyInfo.proxySetEditAction.disable();
					proxyInfo.memberDepositAction.disable();
					proxyInfo.memberPayAction.disable();
					proxyInfo.memberPunishAction.disable();
					proxyInfo.clearingAction.disable();
					proxyInfo.proxySetAction.disable();
					proxyInfo.memberChangeProxyAction.disable();
					proxyInfo.ximaAction.disable();
			    }else{
					proxyInfo.proxySetEditAction.enable();
					proxyInfo.memberDepositAction.enable();
					proxyInfo.memberPayAction.enable();
					proxyInfo.memberPunishAction.enable();
				}
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				proxyInfo.alwaysFun();
			}
		}
	});
/** 基本信息-数据列 */
proxyInfo.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [proxyInfo.selModel, {
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
							return Share.map(v, proxyInfo.memGrade, '');
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
		
proxyInfo.returnscaleNumberField = new Ext.form.NumberField({
		maxLength : 8,
		allowBlank : false,
		decimalPrecision : 4,
		fieldLabel : '占成比例',
		name : 'returnscale',
		anchor : '99%'
	});

proxyInfo.ximascaleNumberField = new Ext.form.NumberField({
		maxLength : 8,
		allowBlank : false,
		decimalPrecision : 4,
		fieldLabel : '洗码比例',
		name : 'ximascale',
		anchor : '99%'
	});
	
proxyInfo.isximaflag = new Ext.form.ComboBox({
		fieldLabel : '洗码状态',
		hiddenName : 'isximaflag',
		name : 'isximaflag',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(proxyInfo.isXimaFlag)
				}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		value : "1",
		anchor : '99%'
	});
	
	
	proxyInfo.clearingtype = new Ext.form.ComboBox({
		fieldLabel : '洗码状态',
		hiddenName : 'clearingtype',
		name : 'clearingtype',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(proxyInfo.clearingtype)
				}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		value : "1",
		anchor : '99%'
	});
	
/** 基本信息-详细信息的form */
proxyInfo.proxySetformPanel = new Ext.form.FormPanel({
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
					},proxyInfo.returnscaleNumberField,proxyInfo.ximascaleNumberField,proxyInfo.isximaflag,proxyInfo.clearingtype]
		});
/** 基本信息-详细信息的form */
proxyInfo.memberChangeProxyformPanel = new Ext.form.FormPanel({
			autoScroll : false,
			border: false,
        	style: 'border-bottom:0px;',
        	bodyStyle: 'padding:10px;background-color:transparent;',
			labelwidth : 50,
			defaultType : 'textfield',
			items : [{
						fieldLabel : '会员账号',
						maxLength : 32,
						allowBlank : false,
						invalidText:'请输入会员账号',
						name : 'memberAccount',
						anchor : '99%'
					}]
		});
		
/** 结算佣金 */
proxyInfo.clearingAction = new Ext.Action({
			text : '结算佣金',
			iconCls : 'Coins',
			disabled : false,
			handler : function() {
				proxyInfo.addWindow.setIconClass('Coinsadd'); // 设置窗口的样式
				proxyInfo.addWindow.setTitle('结算代理佣金'); // 设置窗口的名称
				proxyInfo.addWindow.show().center(); // 显示窗口
				Share.resetGrid(proxyInfo.clearinggrid);
				proxyInfo.clearingstore.reload();
				//proxyInfo.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			}
		});

/**代理洗码*/
proxyInfo.ximaAction = new Ext.Action({
		text : '洗码',
		iconCls : 'Coins',
		disabled : false,
		handler : function() {
			proxyInfo.addXimaWindow.setIconClass('Coinsadd'); // 设置窗口的样式
			proxyInfo.addXimaWindow.setTitle('结算代理洗码'); // 设置窗口的名称
			proxyInfo.addXimaWindow.show().center(); // 显示窗口
			Share.resetGrid(proxyInfo.ximagrid);
			proxyInfo.ximastore.reload();
			//proxyInfo.formPanel.getForm().reset(); // 清空表单里面的元素的值.
		}
});

/**会员存款*/
proxyInfo.memberDepositAction = new Ext.Action({
		text : '下线存款记录',
		disabled : true,
		handler : function() {
			var record = proxyInfo.grid.getSelectionModel().getSelected();
			proxyInfo.addMemberDepositWindow.setIconClass('Coinsadd'); // 设置窗口的样式
			proxyInfo.addMemberDepositWindow.setTitle('下线存款记录'); // 设置窗口的名称
			proxyInfo.addMemberDepositWindow.show().center(); // 显示窗口
			Share.resetGrid(proxyInfo.memberDepositgrid);
			proxyInfo.memberDepositDetail.baseParams.puiid = record.data.uiid;
			proxyInfo.memberDepositDetail.reload();
		}
});

	/** 会员存款详情数据源 */
	proxyInfo.memberDepositDetail = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : false,
		baseParams : {
			startDate: "",
			endDate : "",
			uaccount : "",
			puiid:0,
			start : 0,
			limit : proxyInfo.pageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : proxyInfo.memberDepositUrl
		}),
		fields : ['uiid', 'uaccount', 'urealname', 'amount',"paytyple",'paymethods','deposittime','status','cwremarks'],
		listeners : {
			'load' : function(store, records, options) {
			}
		}
	});
	/** 改变页的combo*/
	proxyInfo.memberDepositPageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				proxyInfo.pageSize  = parseInt(comboBox.getValue());
				proxyInfo.memberDepositbbar.pageSize  = parseInt(comboBox.getValue());
				proxyInfo.memberDepositDetail.baseParams.limit = proxyInfo.pageSize;
				proxyInfo.memberDepositDetail.baseParams.start = 0;
				proxyInfo.memberDepositDetail.load();
			}
		}
	});
	/** 分页 */
	proxyInfo.memberDepositbbar = new Ext.PagingToolbar({
		pageSize : proxyInfo.pageSize,
		store : proxyInfo.memberDepositDetail,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', proxyInfo.memberDepositPageSizeCombo ]
	});
	/** 新建 */
proxyInfo.memberDepositAddAction = new Ext.Action({
			text : '查找',
			iconCls : 'Zoom',
			handler : function() {
				proxyInfo.memberDepositDetail.baseParams.uaccount = $("#memberDeposituaccountDeposit").val();
				proxyInfo.memberDepositDetail.baseParams.startDate = $("#memberDepositpayorderDepositStartDate").val();
				proxyInfo.memberDepositDetail.baseParams.endDate = $("#memberDepositpayorderDepositEndDate").val();
				proxyInfo.memberDepositDetail.reload();
			}
		});

// 入款方式
proxyInfo.memberDepositPaymethods = new Ext.form.ComboBox({
		hiddenName : 'memberpaymethods',
		id : 'memberpaymethods',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(proxyInfo.paymethods)
				}),
		valueField : 'v',
		displayField : 't',
		value: '',
		editable : false,
		width:80,
		listeners : {
			select : function(comboBox) {
				proxyInfo.memberDepositDetail.baseParams.paymethods = comboBox.getValue();
			}
		}
});
// 交易类型
proxyInfo.memberDepositTypes = new Ext.form.ComboBox({
		hiddenName : 'memberDepositTypes',
		id : 'memberDepositTypes',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(proxyInfo.deposittype)
				}),
		valueField : 'v',
		displayField : 't',
		value: '',
		editable : false,
		width:80,
		listeners : {
			select : function(comboBox) {
				proxyInfo.memberDepositDetail.baseParams.depositype = comboBox.getValue();
			}
		}
});
	/** 顶部工具栏 */
    proxyInfo.memberDeposittbar = ['会员账号：',{id:'memberDeposituaccountDeposit',xtype:'textfield',width:100},'&nbsp;','-',
		'开始日期：',{ id:'memberDepositpayorderDepositStartDate',xtype:'datetimefield',format:'Y-m-d H:i:s',width:120},'&nbsp;','-',
		'结束日期：',{ id:'memberDepositpayorderDepositEndDate',xtype:'datetimefield',format:'Y-m-d H:i:s',width:120},'&nbsp;','-','入款类型：',proxyInfo.memberDepositPaymethods,'&nbsp;','-','交易类型：',proxyInfo.memberDepositTypes,'&nbsp;','-',proxyInfo.memberDepositAddAction];
	/** 会员存款记录列表 */
	proxyInfo.memberDepositgrid = new Ext.grid.EditorGridPanel({
		store : proxyInfo.memberDepositDetail,
		autoScroll : 'auto',
		region : 'center',
		tbar : proxyInfo.memberDeposittbar,
		bbar:proxyInfo.memberDepositbbar,
		loadMask : true,
		stripeRows : true,
		border: false,
		columns : [ {
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
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
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
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
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
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '操作时间',
						dataIndex : 'deposittime',
						width : 130,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
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
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '备注',
						dataIndex : 'cwremarks',
						width : 160,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}]
	});
	
	/** 新建会员存款记录窗口 */
	proxyInfo.addMemberDepositWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 450,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [proxyInfo.memberDepositgrid]
	});
	

/**会员提款*/
proxyInfo.memberPayAction = new Ext.Action({
		text : '下线提款记录',
		disabled : true,
		handler : function() {
			var record = proxyInfo.grid.getSelectionModel().getSelected();
			proxyInfo.addMemberPayWindow.setIconClass('Coinsadd'); // 设置窗口的样式
			proxyInfo.addMemberPayWindow.setTitle('下线提款记录'); // 设置窗口的名称
			proxyInfo.addMemberPayWindow.show().center(); // 显示窗口
			Share.resetGrid(proxyInfo.memberPaygrid);
			proxyInfo.memberPayDetail.baseParams.puiid = record.data.uiid;
			proxyInfo.memberPayDetail.reload();
		}
});


	/** 会员提款详情数据源 */
	proxyInfo.memberPayDetail = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : false,
		baseParams : {
			startDate: "",
			endDate : "",
			uaccount : "",
			puiid:0,
			start : 0,
			limit : proxyInfo.pageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : proxyInfo.memberPayUrl
		}),
		fields : ['uiid', 'uaccount', 'urealname', 'amount',"paytyple",'paymethods','deposittime','status','cwremarks'],
		listeners : {
			'load' : function(store, records, options) {
			}
		}
	});
	/** 改变页的combo*/
	proxyInfo.memberPayPageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				proxyInfo.pageSize  = parseInt(comboBox.getValue());
				proxyInfo.memberPaybbar.pageSize  = parseInt(comboBox.getValue());
				proxyInfo.memberPayDetail.baseParams.limit = proxyInfo.pageSize;
				proxyInfo.memberPayDetail.baseParams.start = 0;
				proxyInfo.memberPayDetail.load();
			}
		}
	});
	/** 分页 */
	proxyInfo.memberPaybbar = new Ext.PagingToolbar({
		pageSize : proxyInfo.pageSize,
		store : proxyInfo.memberPayDetail,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', proxyInfo.memberPayPageSizeCombo ]
	});
	/** 新建 */
proxyInfo.memberPayAddAction = new Ext.Action({
			text : '查找',
			iconCls : 'Zoom',
			handler : function() {
				proxyInfo.memberPayDetail.baseParams.uaccount = $("#memberpayuaccountDeposit").val();
				proxyInfo.memberPayDetail.baseParams.startDate = $("#memberpaypayorderDepositStartDate").val();
				proxyInfo.memberPayDetail.baseParams.endDate = $("#memberpaypayorderDepositEndDate").val();
				proxyInfo.memberPayDetail.reload();
			}
		});

	/** 顶部工具栏 */
    proxyInfo.memberPaytbar = ['会员账号：',{id:'memberpayuaccountDeposit',xtype:'textfield',width:120},'&nbsp;','-',
		'开始日期：',{ id:'memberpaypayorderDepositStartDate',xtype:'datetimefield',format:'Y-m-d H:i:s'},'&nbsp;','-',
		'结束日期：',{ id:'memberpaypayorderDepositEndDate',xtype:'datetimefield',format:'Y-m-d H:i:s'},'&nbsp;','-',proxyInfo.memberPayAddAction];
	/** 会员提款记录列表 */
	proxyInfo.memberPaygrid = new Ext.grid.EditorGridPanel({
		store : proxyInfo.memberPayDetail,
		autoScroll : 'auto',
		region : 'center',
		tbar : proxyInfo.memberPaytbar,
		bbar:proxyInfo.memberPaybbar,
		loadMask : true,
		stripeRows : true,
		border: false,
		columns : [ {
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
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
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
						width : 130,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
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
						width : 160,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}]
	});
	
	/** 新建会员存款记录窗口 */
	proxyInfo.addMemberPayWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 450,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [proxyInfo.memberPaygrid]
	});

/**会员扣款记录*/
proxyInfo.memberPunishAction = new Ext.Action({
		text : '下线扣款记录',
		disabled : true,
		handler : function() {
			var record = proxyInfo.grid.getSelectionModel().getSelected();
			proxyInfo.addMemberPunishWindow.setIconClass('Coinsadd'); // 设置窗口的样式
			proxyInfo.addMemberPunishWindow.setTitle('下线扣款记录'); // 设置窗口的名称
			proxyInfo.addMemberPunishWindow.show().center(); // 显示窗口
			Share.resetGrid(proxyInfo.memberPunishgrid);
			proxyInfo.memberPunishDetail.baseParams.puiid = record.data.uiid;
			proxyInfo.memberPunishDetail.reload();
		}
});
		

	/** 会员扣款详情数据源 */
	proxyInfo.memberPunishDetail = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : false,
		baseParams : {
			startDate: "",
			endDate : "",
			uaccount : "",
			puiid:0,
			start : 0,
			limit : proxyInfo.pageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : proxyInfo.memberPunishUrl
		}),
		fields : ['uiid', 'uaccount', 'urealname', 'amount','paytyple','deposittime','status','cwremarks'],
		listeners : {
			'load' : function(store, records, options) {
			}
		}
	});
	/** 改变页的combo*/
	proxyInfo.memberPunishPageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				proxyInfo.pageSize  = parseInt(comboBox.getValue());
				proxyInfo.memberPunishbbar.pageSize  = parseInt(comboBox.getValue());
				proxyInfo.memberPunishDetail.baseParams.limit = proxyInfo.pageSize;
				proxyInfo.memberPunishDetail.baseParams.start = 0;
				proxyInfo.memberPunishDetail.load();
			}
		}
	});
	/** 分页 */
	proxyInfo.memberPunishbbar = new Ext.PagingToolbar({
		pageSize : proxyInfo.pageSize,
		store : proxyInfo.memberPunishDetail,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', proxyInfo.memberPunishPageSizeCombo ]
	});
	/** 新建 */
proxyInfo.memberPunishAddAction = new Ext.Action({
			text : '查找',
			iconCls : 'Zoom',
			handler : function() {
				proxyInfo.memberPunishDetail.baseParams.uaccount = $("#memberpunishuaccountDeposit").val();
				proxyInfo.memberPunishDetail.baseParams.startDate = $("#memberpunishpayorderDepositStartDate").val();
				proxyInfo.memberPunishDetail.baseParams.endDate = $("#memberpunishpayorderDepositEndDate").val();
				proxyInfo.memberPunishDetail.reload();
			}
		});

	/** 顶部工具栏 */
    proxyInfo.memberPunishtbar = ['会员账号：',{id:'memberpunishuaccountDeposit',xtype:'textfield',width:120},'&nbsp;','-',
		'开始日期：',{ id:'memberpunishpayorderDepositStartDate',xtype:'datetimefield',format:'Y-m-d H:i:s'},'&nbsp;','-',
		'结束日期：',{ id:'memberpunishpayorderDepositEndDate',xtype:'datetimefield',format:'Y-m-d H:i:s'},'&nbsp;','-',proxyInfo.memberPunishAddAction];
	/** 会员扣款记录列表 */
	proxyInfo.memberPunishgrid = new Ext.grid.EditorGridPanel({
		store : proxyInfo.memberPunishDetail,
		autoScroll : 'auto',
		region : 'center',
		tbar : proxyInfo.memberPunishtbar,
		bbar:proxyInfo.memberPunishbbar,
		loadMask : true,
		stripeRows : true,
		border: false,
		columns :  [{
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
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
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
						width : 130,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
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
						width : 160,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}]
	});
	
	/** 新建会员扣款记录窗口 */
	proxyInfo.addMemberPunishWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 450,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [proxyInfo.memberPunishgrid]
	});

/**代理占成设置*/
proxyInfo.proxySetAction = new Ext.Action({
			text : '占成比例设置',
			iconCls : 'Add',
			disabled : false,
			handler : function() {
				proxyInfo.proxySetaddWindow.setIconClass('Applicationadd'); // 设置窗口的样式
				proxyInfo.proxySetaddWindow.setTitle('新建代理占成数据'); // 设置窗口的名称
				proxyInfo.proxySetaddWindow.show().center(); // 显示窗口
				proxyInfo.proxySetformPanel.getForm().reset(); // 清空表单里面的元素的值.
			}
		});
		/**编辑代理占成比例*/
proxyInfo.proxySetEditAction = new Ext.Action({
			text : '编辑占成比例',
			iconCls : 'Applicationedit',
			disabled : true,
			handler : function() {
				var record = proxyInfo.grid.getSelectionModel().getSelected();
				proxyInfo.proxySetaddWindow.setIconClass('Applicationedit'); // 设置窗口的样式
				proxyInfo.proxySetaddWindow.setTitle('编辑代理占成数据'); // 设置窗口的名称
				proxyInfo.proxySetaddWindow.show().center(); // 显示窗口
				proxyInfo.proxySetformPanel.getForm().reset(); // 清空表单里面的元素的值.
				proxyInfo.proxySetformPanel.getForm().loadRecord(record);
			}
		});
/**设置会员为代理*/
proxyInfo.memberChangeProxyAction = new Ext.Action({
			text : '设置',
			iconCls : 'Applicationedit',
			disabled : false,
			handler : function() {
				proxyInfo.memberChangeProxyaddWindow.setIconClass('Applicationedit'); // 设置窗口的样式
				proxyInfo.memberChangeProxyaddWindow.setTitle('会员转代理'); // 设置窗口的名称
				proxyInfo.memberChangeProxyaddWindow.show().center(); // 显示窗口
				proxyInfo.memberChangeProxyformPanel.getForm().reset(); // 清空表单里面的元素的值.
			}
		});
/** 查询 */
proxyInfo.searchField = new Ext.ux.form.SearchField({
			store : proxyInfo.store,
			paramName : 'account',
			emptyText : '请输入代理账号',
			width:130,
			style : 'margin-left: 5px;'
		});
		
proxyInfo.datetimeField = new  Ext.form.DateField({ 
		id:'queryDate',
		showToday:true,
		format:'Y-m-d',
		invalidText:'日期输入非法',
		value:new Date().format("Y-m-d"),
		width:120
});

proxyInfo.enddatetimeField = new  Ext.form.DateField({ 
		id:'queryDateEnd',
		showToday:true,
		format:'Y-m-d',
		invalidText:'日期输入非法',
		value:new Date().format("Y-m-d"),
		width:120
});
/** 顶部工具栏 */
proxyInfo.tbar = [proxyInfo.proxySetAction, '-',proxyInfo.proxySetEditAction,'-',proxyInfo.memberChangeProxyAction,'-',proxyInfo.clearingAction,'-',proxyInfo.ximaAction,'-',proxyInfo.datetimeField,'至',proxyInfo.enddatetimeField,'-',proxyInfo.searchField];
/** 底部工具条 */
proxyInfo.bbar = new Ext.PagingToolbar({
			pageSize : proxyInfo.pageSize,
			store : proxyInfo.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', proxyInfo.pageSizeCombo]
		});
/** 基本信息-表格 */
proxyInfo.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : proxyInfo.store,
			colModel : proxyInfo.colModel,
			selModel : proxyInfo.selModel,
			tbar : proxyInfo.tbar,
			bbar : proxyInfo.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'proxyInfoDesc',
			stripeRows : true,
			viewConfig : {},
			listeners : {
				'cellclick' : function(obj, rowIndex, columnIndex, e) {
					
					var record = proxyInfo.grid.getSelectionModel().getSelected();
					if (record) {
						// 更新明细记录
						proxyLowerUserInfo.store.baseParams.uiid = record.data.uiid;
						proxyLowerUserInfo.store.baseParams.startDate = $("#queryDate").val();
						proxyLowerUserInfo.store.baseParams.endDate = $("#queryDateEnd").val();
	//					if(proxyDownuser.isSearch){
	//						proxyDownuserBetInfo.store.baseParams.startDate=$("#startDate").val();
	//						proxyDownuserBetInfo.store.baseParams.endDate=$("#endDate").val();
	//					}
						//proxyLowerUserInfo.store.load();
					}
				},
			  render: function() {
               this.tbar2 = new Ext.Toolbar({  
               renderTo: proxyInfo.grid.tbar,
               items:[
					proxyInfo.memberDepositAction,
					'-','&nbsp;',proxyInfo.memberPayAction,
					'-','&nbsp;',proxyInfo.memberPunishAction
                    ]
            	})
          	 }
			}
		});
		
/** 编辑新建窗口 */
proxyInfo.proxySetaddWindow = new Ext.Window({
			layout : 'fit',
			width : 399,
			height : 220,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [proxyInfo.proxySetformPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							proxyInfo.proxySetsaveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = proxyInfo.proxySetformPanel.getForm();
							var id = form.findField("pyid").getValue();
							form.reset();
							if (id != '')
								form.findField("pyid").setValue(id);
						}
					}]
		});
		
/** 编辑新建窗口 */
proxyInfo.memberChangeProxyaddWindow = new Ext.Window({
			layout : 'fit',
			width : 399,
			height : 220,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [proxyInfo.memberChangeProxyformPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							proxyInfo.memberChangeProxysaveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = proxyInfo.memberChangeProxyformPanel.getForm();
							form.reset();
						}
					}]
		});


proxyInfo.clearingselModel = new Ext.grid.CheckboxSelectionModel();
proxyInfo.clearingstore = new Ext.data.JsonStore({
	root : 'data',
	totalProperty : 'totalProperty',
	autoLoad : false,
	baseParams : {
		startDate : "",
		endDate : "",
		account : ""
	},  
	proxy : new Ext.data.HttpProxy({
		method : 'POST',
		timeout : 180000,
		url : proxyInfo.clearingAll
	}),
	fields : [ 'uiid', 'account', 'uname','pyid','returnscale','ximascale','betTotel','lowerUser','validBetAmountTotal','finalamountTotal','ximaAmount','preferentialTotal','clearingstatus','clearingtype'],
	listeners : {
		'load' : function(store, records, options) {
			// user.roleSelModel.clearSelections();
		}
	}
});

//结算查询按钮
proxyInfo.clearingSearchAction = new Ext.Action({
		text : '查询',
		iconCls : 'Zoom',
		disabled : false,
		handler : function() {
			proxyInfo.jssearchFun();
		}
});

		
/**结算日期**/
proxyInfo.clearingDatetimeField = new  Ext.form.DateField({ 
		id:'clearingDate',
		showToday:true,
		format:'Y-m-d',
		invalidText:'日期输入非法',
		value:new Date().format("Y-m-d"),
		width:120
});

proxyInfo.jssearchParams = function(){
	var obj = {};
	obj.account =  $("#proxyclearinaccount").val();
	obj.startDate = $("#clearingDate").val();
	obj.endDate = $("#clearingDate").val();
    return obj;
}

proxyInfo.jssearchFun = function(){
	proxyInfo.clearingstore.load({params: proxyInfo.jssearchParams()});
}

proxyInfo.clearingstore.on('beforeload',function(store, options){
    proxyInfo.clearingstore.baseParams = proxyInfo.jssearchParams();
});

proxyInfo.clearingtbar = ['结算日期：',proxyInfo.clearingDatetimeField,'-','&nbsp;',
'代理帐号：',{id:'proxyclearinaccount',xtype:'textfield',width:100},'-',proxyInfo.clearingSearchAction];

proxyInfo.clearinggrid = new Ext.grid.EditorGridPanel({
	store : proxyInfo.clearingstore,
	sm : proxyInfo.clearingselModel,
	tbar:proxyInfo.clearingtbar,
	autoScroll : 'auto',
	region : 'center',
	loadMask : true,
	stripeRows : true,
	border: false,
	viewConfig : {},
	columns : [ proxyInfo.clearingselModel, {
			hidden : true,
			header : '代理ID',
			dataIndex : 'uiid'
		},{
			header : "代理账号",
			dataIndex : 'account',
			width : 80,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : "代理姓名",
			dataIndex : 'uname',
			width : 80
		},{
			header : "下线人数",
			dataIndex : 'lowerUser',
			width : 80
		},{
			header : "注单数量",
			dataIndex : 'betTotel',
			width : 80
		},{
			header : "有效投注额",
			dataIndex : 'validBetAmountTotal',
			width : 100,
			renderer : function(v) {
				return '<span style="color:blue;">'+v+'</span>';
			}
		},{
			header : "下线本月总洗码",
			dataIndex : 'ximaAmount',
			width : 100,
			renderer : function(v) {
				return '<span style="color:blue;">'+v+'</span>';
			}
		},{
			header : "下线本月总优惠",
			dataIndex : 'preferentialTotal',
			width : 100,
			renderer : function(v) {
				return '<span style="color:blue;">'+v+'</span>';
			}
		},{
			header : "本月盈亏",
			dataIndex : 'finalamountTotal',
			width : 80,
			renderer : function(v) {
				return Share.amount(v);
			}
		},{
			header : "占成比例",
			dataIndex : 'returnscale',
			width : 80
		},{
			header : "洗码比例",
			dataIndex : 'ximascale',
			width : 80
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
		
/** 编辑新建窗口 */
proxyInfo.addWindow = new Ext.Window({
			layout : 'fit',
			width : 1100,
			height : 450,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [proxyInfo.clearinggrid],
			buttons : [{
						text : '保存',
						handler : function() {
							proxyInfo.saveClearingFun();
						}
					}, {
						text : '重置',
						handler : function() {
							
						}
					}]
		});
		
		
proxyInfo.ximaselModel = new Ext.grid.CheckboxSelectionModel();
proxyInfo.ximastore = new Ext.data.JsonStore({
	root : 'data',
	totalProperty : 'totalProperty',
	autoLoad : false,
	baseParams : {
		startDate : ""
	},  
	proxy : new Ext.data.HttpProxy({
		method : 'POST',
		timeout : 180000,
		url : proxyInfo.ximaAll
	}),
	fields : [ 'uiid', 'account', 'uname','pyid','returnscale','ximascale','betTotel','lowerUser','validBetAmountTotal','finalamountTotal','ximaAmount','preferentialTotal','clearingstatus','isximaflag'],
	listeners : {
		'load' : function(store, records, options) {
			// user.roleSelModel.clearSelections();
		}
	}
});

/**结算日期**/
proxyInfo.ximaDatetimeField = new  Ext.form.DateField({ 
		id:'ximaDate',
		showToday:true,
		format:'Y-m-d',
		invalidText:'日期输入非法',
		value:new Date().format("Y-m-d"),
		width:120,
		listeners:{
			'select':function(v){
				proxyInfo.ximastore.baseParams.startDate = Ext.util.Format.date(v.getValue(),"Y-m-d");
				proxyInfo.ximastore.reload();
				Share.resetGrid(proxyInfo.ximagrid);
			}
		}
});

proxyInfo.ximatbar = ['结算日期：',proxyInfo.ximaDatetimeField];

proxyInfo.ximagrid = new Ext.grid.EditorGridPanel({
	store : proxyInfo.ximastore,
	sm : proxyInfo.ximaselModel,
	tbar:proxyInfo.ximatbar,
	autoScroll : 'auto',
	region : 'center',
	loadMask : true,
	stripeRows : true,
	border: false,
	viewConfig : {},
	columns : [ proxyInfo.ximaselModel, {
			hidden : true,
			header : '代理ID',
			dataIndex : 'uiid'
		},{
			header : "代理账号",
			dataIndex : 'account',
			width : 80,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : "代理姓名",
			dataIndex : 'uname',
			width : 80
		},{
			header : "下线人数",
			dataIndex : 'lowerUser',
			width : 80
		},{
			header : "注单数量",
			dataIndex : 'betTotel',
			width : 80
		},{
			header : "有效投注额",
			dataIndex : 'validBetAmountTotal',
			width : 100,
			renderer : function(v) {
				return '<span style="color:blue;">'+v+'</span>';
			}
		},{
			header : "下线本月总洗码",
			dataIndex : 'ximaAmount',
			width : 100,
			renderer : function(v) {
				return '<span style="color:blue;">'+v+'</span>';
			}
		},{
			header : "下线本月总优惠",
			dataIndex : 'preferentialTotal',
			width : 100,
			renderer : function(v) {
				return '<span style="color:blue;">'+v+'</span>';
			}
		},{
			header : "本月盈亏",
			dataIndex : 'finalamountTotal',
			width : 80,
			renderer : function(v) {
				return Share.amount(v);
			}
		},{
			header : "占成比例",
			dataIndex : 'returnscale',
			width : 80
		},{
			header : "洗码比例",
			dataIndex : 'ximascale',
			width : 80
		},{
			header : "自助洗码",
			dataIndex : 'isximaflag',
			width : 80,
			renderer : function(v) {
				if(v == 0){
					return '<span style="color:red;">未开启</span>';
				}else{
					return '<span style="color:green;">已开启</span>';
				}
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

/** 编辑代理洗码窗口 */
proxyInfo.addXimaWindow = new Ext.Window({
			layout : 'fit',
			width : 1100,
			height : 450,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [proxyInfo.ximagrid],
			buttons : [{
						text : '保存',
						handler : function() {
							proxyInfo.ximaClearingFun();
						}
					}, {
						text : '重置',
						handler : function() {
							
						}
					}]
		});

proxyInfo.alwaysFun = function() {
	Share.resetGrid(proxyInfo.grid);
	if(proxyInfo.isManage =='1'){
   	 	proxyInfo.proxySetEditAction.disable();
		proxyInfo.memberDepositAction.disable();
		proxyInfo.memberPayAction.disable();
		proxyInfo.memberPunishAction.disable();
		proxyInfo.clearingAction.disable();
		proxyInfo.proxySetAction.disable();
		proxyInfo.memberChangeProxyAction.disable();
		proxyInfo.ximaAction.disable();
    }else{
		proxyInfo.proxySetEditAction.disable();
		proxyInfo.memberDepositAction.disable();
		proxyInfo.memberPayAction.disable();
		proxyInfo.memberPunishAction.disable();
    }
};

proxyInfo.saveClearingFun = function() {
	var selections = proxyInfo.clearinggrid.getSelectionModel().getSelections();//获取选择的数据
	if(selections.length <= 0){
		Ext.Msg.alert("提示", "请至少选择一个代理进行结算佣金!");
		return;
	}
	var ids = [];
	for ( var i = 0; i < selections.length; i++) {
		if(selections[i].data.clearingstatus == 0 && Number(selections[i].data.finalamountTotal) !=0){//结算未结算的
			var jsdata = selections[i].data.uiid+"#"+selections[i].data.returnscale+"#"+selections[i].data.ximascale+"#"+selections[i].data.validBetAmountTotal+"#"+selections[i].data.finalamountTotal+"#"+selections[i].data.ximaAmount+"#"+selections[i].data.preferentialTotal+"#"+selections[i].data.clearingtype;
			ids.push(jsdata);
		}
	}
	var clearingDate = $("#clearingDate").val();
	var params = {
		proxys : ids,
		startDate : clearingDate,
		endDate:clearingDate
	};
	// 发送请求
	Share.AjaxRequest({
				url : proxyInfo.clearing,
				params : params,
				callback : function(json) {
					//proxyInfo.addWindow.hide();
					Share.resetGrid(proxyInfo.clearinggrid);
					proxyInfo.clearingstore.reload();
				}
			});
};

/**洗码*/
proxyInfo.ximaClearingFun = function() {
	var selections = proxyInfo.ximagrid.getSelectionModel().getSelections();//获取选择的数据
	if(selections.length <= 0){
		Ext.Msg.alert("提示", "请至少选择一个代理进行结算佣金!");
		return;
	}
	var ids = [];
	for ( var i = 0; i < selections.length; i++) {
		if(selections[i].data.clearingstatus == 0 && Number(selections[i].data.validBetAmountTotal) !=0){//结算未结算的
			var jsdata = selections[i].data.uiid+"#"+selections[i].data.returnscale+"#"+selections[i].data.ximascale+"#"+selections[i].data.validBetAmountTotal+"#"+selections[i].data.finalamountTotal+"#"+selections[i].data.ximaAmount+"#"+selections[i].data.preferentialTotal+"#"+selections[i].data.isximaflag;
			ids.push(jsdata);
		}
	}
	var clearingDate = $("#ximaDate").val();
	var clearingType = 2;
	var params = {
		proxys : ids,
		clearingType:clearingType,
		startDate : clearingDate,
		endDate:clearingDate
	};
	// 发送请求
	Share.AjaxRequest({
				url : proxyInfo.xima,
				params : params,
				callback : function(json) {
					//proxyInfo.addWindow.hide();
					Share.resetGrid(proxyInfo.ximagrid);
					proxyInfo.ximastore.reload();
				}
			});
};


proxyInfo.proxySetsaveFun = function() {
	var form = proxyInfo.proxySetformPanel.getForm();

	if(!form.isValid()){
 		return;
 	}
	// 发送请求
	Share.AjaxRequest({
				url : proxyInfo.proxyset,
				params : form.getValues(),
				callback : function(json) {
				if (json.success==false){
				     Ext.MessageBox.alert('Status', json.msg, showResult);
					return;
				}else{
				    proxyInfo.proxySetaddWindow.hide();
					proxyInfo.alwaysFun();
					proxyInfo.store.reload();
				}
					
				}
			});
};

/** 会员转代理*/
proxyInfo.memberChangeProxysaveFun = function() {
	var form = proxyInfo.memberChangeProxyformPanel.getForm();

	if(!form.isValid()){
 		return;
 	}
	// 发送请求
	Share.AjaxRequest({
		url : proxyInfo.memberSetProxy,
		params : form.getValues(),
		callback : function(json) {
		if (json.success==false){
		     Ext.MessageBox.alert('Status', json.msg, showResult);
			return;
		}else{
		    proxyInfo.memberChangeProxyaddWindow.hide();
			proxyInfo.alwaysFun();
			proxyInfo.store.reload();
		}
			
		}
	});
};

proxyInfo.delFun = function() {
	var record = proxyInfo.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '你真的要删除选中菜单及其包含的所有子菜单吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : proxyInfo.del + record.data.pyid +".do",
								callback : function(json) {
									proxyInfo.alwaysFun();
									proxyInfo.store.reload();
								}
							});
				}
			});



};











//-----------------------------代理下线用户---------------------------------------------
Ext.ns("Ext.market.proxyLowerUserInfo"); // 自定义一个命名空间
proxyLowerUserInfo = Ext.market.proxyLowerUserInfo; // 定义命名空间的别名
proxyLowerUserInfo = {
	all : '/manage/proxy/queryDownuser.do', // 
	pageSize : 30// 每页显示的记录数
}

/** 改变页的combo*/
proxyLowerUserInfo.pageSizeCombo = new Share.pageSizeCombo({
	value : '30',
	listeners : {
		select : function(comboBox) {
			proxyLowerUserInfo.pageSize  = parseInt(comboBox.getValue());
			proxyLowerUserInfo.bbar.pageSize  = parseInt(comboBox.getValue());
			proxyLowerUserInfo.store.baseParams.limit = proxyLowerUserInfo.pageSize;
			proxyLowerUserInfo.store.baseParams.start = 0;
			proxyLowerUserInfo.store.load();
		}
	}
});

//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
proxyLowerUserInfo.pageSize = parseInt(proxyLowerUserInfo.pageSizeCombo.getValue());

/** 基本信息-数据源 */
proxyLowerUserInfo.store = new Ext.data.Store({
	remoteSort : true,
	autoLoad:false,
	baseParams : {
		uiid : -1,
		startDate : "",
		endDate : "",
		start : 0,
		limit : proxyLowerUserInfo.pageSize
	},  
	proxy : new Ext.data.HttpProxy({// 获取数据的方式
		method : 'POST',
		url : proxyLowerUserInfo.all
	}),
	reader : new Ext.data.JsonReader({// 数据读取器
		totalProperty : 'totalProperty', // 记录总数
		root : 'data' // Json中的列表数据根节点
	}, ['account','uname','betTotel','finalamountTotal','betAmountTotal','profitamountTotal','validBetAmountTotal']),
	listeners : {
		'load' : function(store, records, options) {
			proxyLowerUserInfo.amountsum();
			proxyLowerUserInfo.alwaysFun();
		}
	}
});

/** 基本信息-选择模式 */
proxyLowerUserInfo.selModel = new Ext.grid.CheckboxSelectionModel({
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
proxyLowerUserInfo.colModel = new Ext.grid.ColumnModel({
	defaults : {
		sortable : true,
		width : 120
	},
	columns : [proxyLowerUserInfo.selModel, {
						hidden : true,
						header : 'ID',
						dataIndex : 'account'
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
						header : '注单数量',
						dataIndex : 'betTotel',
						width : 80

					}, {
						header : '投注金额',
						dataIndex : 'betAmountTotal'
					}, {
						header : '有效投注额',
						dataIndex : 'validBetAmountTotal'

					}, {
						header : '派彩金额',
						dataIndex : 'profitamountTotal',
						renderer : function(v) {
							return Share.amount(v);
      					}
					}]
});

/** 查询 */
proxyLowerUserInfo.searchField = new Ext.ux.form.SearchField({
	store : proxyLowerUserInfo.store,
	paramName : 'optusername',
	width:120,
	emptyText : '请输入代理用户名',
	style : 'margin-left: 5px;'
});
/** 顶部工具栏 */
proxyLowerUserInfo.tbar = [proxyLowerUserInfo.searchField];

/** 底部工具条 */
proxyLowerUserInfo.bbar = new Ext.PagingToolbar({
	pageSize : proxyLowerUserInfo.pageSize,
	store : proxyLowerUserInfo.store,
	displayInfo : true,
	//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
	items : [ '-', '&nbsp;', proxyLowerUserInfo.pageSizeCombo ]
});

proxyLowerUserInfo.alwaysFun = function() {
		Share.resetGrid(proxyLowerUserInfo.grid);
};

/** 基本信息-表格 */
proxyLowerUserInfo.grid = new Ext.grid.GridPanel({
	title : "代理下级会员明细",
	store : proxyLowerUserInfo.store,
	colModel : proxyLowerUserInfo.colModel,
	selModel : proxyLowerUserInfo.selModel,
	tbar : proxyLowerUserInfo.tbar,
	bbar : proxyLowerUserInfo.bbar,
	autoScroll : 'auto',
	region : 'east',
	width : '50%',
	//autoExpandColumn :'remark',
	loadMask : true,
	viewConfig:{},
	stripeRows : true
});

proxyLowerUserInfo.amountsum = function(){
	var p = new Ext.data.Record({fields:['account','uname','betTotel','betAmountTotal','profitamountTotal','validBetAmountTotal']});
	var finalamount= 0,bettotel = 0,betAmount=0,profitamount=0,validBetAmount=0;
	proxyLowerUserInfo.store.each(function(record){
		var betTotels = record.data.betTotel;
		if(betTotels!=null){
			bettotel += Number(betTotels);
		}
		var betAmountTotal = record.data.betAmountTotal;
		if(betAmountTotal != null){
			betAmount += Number(betAmountTotal);
		}
		var profitamountTotal = record.data.profitamountTotal;
		if(profitamountTotal != null){
			profitamount += Number(profitamountTotal);
		}
		var validBetAmountTotal = record.data.validBetAmountTotal;
		if(validBetAmountTotal != null){
			validBetAmount += Number(validBetAmountTotal);
		}
		
	});
	p.set('account','小计：');
	p.set('uname','');
	p.set('betTotel',bettotel);
	p.set('betAmountTotal',Share.decimalTwo(betAmount));
	p.set('profitamountTotal',Share.decimalTwo(profitamount));
	p.set('validBetAmountTotal',Share.decimalTwo(validBetAmount));
	proxyLowerUserInfo.store.add(p);
}


proxyInfo.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,		
			defaults: {
       	 		split: true,                 //是否有分割线
        		collapsible: true           //是否可以折叠
    		},
			height : index.tabPanel.getInnerHeight() - 1,
			items : [proxyInfo.grid/**,proxyLowerUserInfo.grid*/]
		});
</script>