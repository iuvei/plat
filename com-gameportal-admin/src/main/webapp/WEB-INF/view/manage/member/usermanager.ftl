<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.usermanager"); // 自定义一个命名空间
usermanager = Ext.market.usermanager; // 定义命名空间的别名
usermanager = {
	all : '/manage/memberinfo/dx/queryUser.do',// 加载所有
	save : '/manage/memberinfo/dx/save.do',
	del : '/manage/memberinfo/dx/delete/',
	callUrl:'/manage/memberinfo/dx/call/',
	sendSmsUrl:"/manage/member/sendSms/",//发送短信
	sendAnoSmsUrl:"/manage/member/sendAnoSms.do",//发送匿名短信
	dispositorDetailUrl:'/manage/memberinfo/dx/queryDxDepositorder.do',//存款列表
	pickupDetailUrl:'/manage/memberinfo/dx/queryDxPickUporder.do',//取款列表
	couponDetailUrl:'/manage/memberinfo/dx/queryDxCouponorder.do',//优惠列表
	ximaDetailUrl:'/manage/memberinfo/dx/queryDxMemberXimaMain.do',//洗码列表
	isAdmin : '${isAdmin}',
	pageSize : 30, // 每页显示的记录数
	alldx:eval('(${alldx})'),
	payertype:eval('(${fields.payertype})'),
	PAYMETHODSMAP : eval('(${paymethodsMap})'),//注意括号
	ordertype : eval('(${fields.ordertype})'),
	STATUSMAP : eval('(${statusMap})')//注意括号
};


/** 改变页的combo */
usermanager.pageSizeCombo = new Share.pageSizeCombo({
			value : '30',
			listeners : {
				select : function(comboBox) {
					usermanager.pageSize = parseInt(comboBox.getValue());
					usermanager.bbar.pageSize = parseInt(comboBox.getValue());
					usermanager.store.baseParams.limit = usermanager.pageSize;
					usermanager.store.baseParams.start = 0;
					usermanager.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
usermanager.pageSize = parseInt(usermanager.pageSizeCombo.getValue());
/** 基本信息-数据源 */
usermanager.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : usermanager.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : usermanager.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['uiid', 'belongid', 'extno','truename','payertype', 'account','uname','accounttype','identitycard','phone','email','qq','phonevalid','emailvalid','typeflag','createDate','url','remark','logincount','updateDate','paytotal','fastpay']),
			listeners : {
				'load' : function(store, records, options) {
					usermanager.amoneysum();
				//	usermanager.alwaysFun();
				}
			}
		});
usermanager.amoneysum = function(){
	var p = new Ext.data.Record({fields:['uiid', 'belongid','extno', 'truename','payertype', 'account','uname','accounttype','identitycard','phone','email','qq','phonevalid','emailvalid','typeflag','createDate','url','remark','logincount','updateDate','paytotal','fastpay']});
	var scamount= 0,ckamount = 0;
	usermanager.store.each(function(record){
		var scamounts = record.data.fastpay;
		if(scamounts!=null){
			scamount += Number(scamounts);
		}
		var ckamounts = record.data.paytotal;
		if(ckamounts != null){
			ckamount += Number(ckamounts);
		}
		
	});
	p.set('payertype','--')
	p.set('account','小计：')
	p.set('uname','--')
	p.set('phonevalid','--')
	p.set('emailvalid','--')
	p.set('typeflag','--')
	p.set('accounttype','--')
	p.set('fastpay',Share.decimalTwo(scamount));
	p.set('paytotal',Share.decimalTwo(ckamount));
	usermanager.store.add(p);
}
/** 基本信息-选择模式 */
usermanager.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				if(usermanager.isAdmin == 1){
					usermanager.deleteAction.enable();
				}
				usermanager.remarkAction.enable();
				usermanager.dispositorAction.enable();
				usermanager.pickupAction.enable();
				usermanager.couponAction.enable();
				usermanager.ximaAction.enable();
				usermanager.callAction.enable();
				usermanager.sendSmsAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				usermanager.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
usermanager.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [usermanager.selModel, {
						hidden : true,
						header : '用户ID',
						dataIndex : 'uiid'
					}, {
						header : '电销姓名',
						dataIndex : 'belongid',
						width : 80,
						renderer : function(v) {
							return Share.map(v, usermanager.alldx, '');
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						hidden : true,
						header :'分机号码',
						dataIndex: 'extno',
						width:60
					}, {
						header : '玩家类型',
						dataIndex : 'payertype',
						width : 80,
						renderer : function(v) {
							return Share.map(v, usermanager.payertype, '');
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '会员账号',
						dataIndex : 'account',
						width : 80,
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
						header : '会员类型',
						dataIndex : 'accounttype',
						width : 60,
						renderer : function(v) {
							if(v == '--'){
								return '';
							}
							if(v == 0){
								return '<span style="color:blue;">普通会员</span>';
							}else{
								return '<span style="color:#992211;">代理会员</span>';
							}
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '备注',
						dataIndex : 'remark',
						width : 150,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '身份证号码',
						dataIndex : 'identitycard',
						width : 120,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						hidden: true,
						header : '电话号码',
						dataIndex : 'phone',
						width : 100,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '手机认证',
						dataIndex : 'phonevalid',
						width : 60,
						renderer : function(v) {
							if(v == '--'){
								return '';
							}
							if(v == 0){
								return '<span style="color:red;">未认证</span>';
							}else{
								return '<span style="color:green;">已认证</span>';
							}
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '邮箱认证',
						dataIndex : 'emailvalid',
						width : 60,
						renderer : function(v) {
							if(v == '--'){
								return '';
							}
							if(v == 0){
								return '<span style="color:red;">未认证</span>';
							}else{
								return '<span style="color:green;">已认证</span>';
							}
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '玩家来源',
						dataIndex : 'typeflag',
						width : 60,
						renderer : function(v) {
							if(v == '--'){
								return '';
							}
							if(v == 0){
								return '<span style="color:blue;">线下会员</span>';
							}else{
								return '<span style="color:#992211;">线上会员</span>';
							}
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '首存优惠',
						dataIndex : 'fastpay',
						width : 80,
						renderer : function(v) {
							return '<span style="color:blue;">'+v+'</span>';
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '存款金额',
						dataIndex : 'paytotal',
						width : 80,
						renderer : function(v) {
							return '<span style="color:blue;">'+v+'</span>';
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '注册时间',
						dataIndex : 'createDate',
						width : 130,
						renderer : function(v) {
							return v==null?'':new Date(v).format('Y-m-d H:i:s');
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '最后登录时间',
						dataIndex : 'updateDate',
						width : 130,
						renderer : function(v) {
							return v==null?'':new Date(v).format('Y-m-d H:i:s');
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '登录次数',
						dataIndex : 'logincount',
						width : 60,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '注册网址',
						dataIndex : 'url',
						width : 220,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}]
		});
		

/** 分配 */
usermanager.addAction = new Ext.Action({
			text : '分配',
			iconCls : 'Arrowswitch',
			disabled : usermanager.isAdmin==1?false:true,
			handler : function() {
				usermanager.addWindowRemark.setIconClass('Arrowswitch'); // 设置窗口的样式
				usermanager.addWindowRemark.setTitle('分配用户'); // 设置窗口的名称
				usermanager.addWindowRemark.show().center(); // 显示窗口
				usermanager.formPanelRemark.getForm().reset(); // 清空表单里面的元素的值.
				var form=usermanager.formPanelRemark.getForm();
				form.findField("account").setReadOnly(false);
				form.findField("belongid").setReadOnly(false);
			}
});

/** 拨打电话 */
usermanager.callAction = new Ext.Action({
	text : '拨打电话',
	iconCls : 'Anchor',
	disabled : true,
	handler : function() {
		usermanager.callFun();
	}
});

/** 发送短信form */
usermanager.sendAnoSmsPanel = new Ext.form.FormPanel({
	autoScroll : false,
	autoHeight: false,
	border: false,
	style: 'border-bottom:0px;',
	bodyStyle: 'padding:10px;background-color:transparent;',
	labelwidth : 50,
	items : [{
				xtype : 'textfield',
				fieldLabel : '手机号码',
				allowBlank : false,
				regex: /^1\d{10}$/,
				name : 'phone',
				width:200
			}, {
				xtype : 'textarea',
				fieldLabel : '短信内容',
				maxLength : 200,
				width:400,
				height:130,
				allowBlank : false,
				name : 'smsInfo',
				anchor : '99%'
			}]
});

/**修改密码窗口 */
usermanager.addAnoSmsWin = new Ext.Window({
	layout : 'fit',
	width : 500,
	height : 250,
	closeAction : 'hide',
	plain : true,
	modal : true,
	resizable : true,
	items : [usermanager.sendAnoSmsPanel],
	buttons : [{
				text : '确定',
				handler : function() {
					var form = usermanager.sendAnoSmsPanel.getForm();
					if(form.isValid()){
						usermanager.sendAnoSmsFun();
					}
				}
			}, {
				text : '重置',
				handler : function() {
					usermanager.sendAnoSmsPanel.getForm().reset();
				}
			}]
});

usermanager.sendAnoSmsAction = new Ext.Action({
	text : '发送匿名短信',
	iconCls : 'Usergo',
	disabled : usermanager.isAdmin==1?false:true,
	handler : function() {
		usermanager.addAnoSmsWin.setIconClass('Usergo'); // 设置窗口的样式
		usermanager.addAnoSmsWin.setTitle('发送匿名短信'); // 设置窗口的名称
		usermanager.addAnoSmsWin.show().center(); // 显示窗口
		usermanager.sendAnoSmsPanel.getForm().reset(); // 清空表单里面的元素的值.
	}
});

/** 发送短信form */
usermanager.sendSmsFormPanel = new Ext.form.FormPanel({
	autoScroll : false,
	autoHeight: false,
	border: false,
	style: 'border-bottom:0px;',
	bodyStyle: 'padding:10px;background-color:transparent;',
	labelwidth : 50,
	items : [{
				xtype : 'hidden',
				fieldLabel : 'ID',
				name : 'uiid',
				anchor : '99%'
			}, {
				xtype : 'textarea',
				fieldLabel : '短信内容',
				maxLength : 200,
				width:400,
				height:150,
				allowBlank : false,
				name : 'smsInfo',
				anchor : '99%'
			}]
});

/** 发送短信 */
usermanager.sendAnoSmsFun = function(){
	var form = usermanager.sendAnoSmsPanel.getForm();
	// 发送请求
	Share.AjaxRequest({
		url : usermanager.sendAnoSmsUrl,
		params : form.getValues(),
		callback : function(json) {
			if (json.success){
			    usermanager.addAnoSmsWin.hide();
			}else{
				Ext.MessageBox.alert(json.msg);
			  	return;
			}
		}
	});
};

/**修改密码窗口 */
usermanager.sendWindow = new Ext.Window({
	layout : 'fit',
	width : 500,
	height : 250,
	closeAction : 'hide',
	plain : true,
	modal : true,
	resizable : true,
	items : [usermanager.sendSmsFormPanel],
	buttons : [{
				text : '确定',
				handler : function() {
					var form = usermanager.sendSmsFormPanel.getForm();
					if(form.isValid()){
						usermanager.sendSmsFun();
					}
				}
			}, {
				text : '重置',
				handler : function() {
					var form = usermanager.sendSmsFormPanel.getForm();
					var id = form.findField("uiid").getValue();
					form.reset();
					if (id != '')
						form.findField("uiid").setValue(id);
				}
			}]
});

/**发送短信*/
usermanager.sendSmsAction = new Ext.Action({
	text : '发送短信',
	iconCls : 'Usergo',
	disabled : true,
	handler : function() {
		var record = usermanager.grid.getSelectionModel().getSelected();
		usermanager.sendWindow.setIconClass('Usergo'); // 设置窗口的样式
		usermanager.sendWindow.setTitle('发送短信'); // 设置窗口的名称
		usermanager.sendWindow.show().center();
		usermanager.sendSmsFormPanel.getForm().reset();
		usermanager.sendSmsFormPanel.getForm().findField("uiid").setValue(record.data.uiid);
	}
});

/** 发送短信 */
usermanager.sendSmsFun = function(){
	var form = usermanager.sendSmsFormPanel.getForm();
	// 发送请求
	Share.AjaxRequest({
		url : usermanager.sendSmsUrl+form.findField("uiid").getValue()+".do",
		params : form.getValues(),
		callback : function(json) {
			if (json.success){
			    usermanager.sendWindow.hide();
				//usermanager.alwaysFun();
			}else{
				Ext.MessageBox.alert(json.msg);
			  	return;
			}
		}
	});
};

usermanager.remarkAction = new Ext.Action({
			text : '备注',
			iconCls : 'Layoutedit',
			disabled : true,
			handler : function() {
				var record = usermanager.grid.getSelectionModel().getSelected();
				usermanager.addWindowRemark.setIconClass('Layoutedit'); // 设置窗口的样式
				usermanager.addWindowRemark.setTitle('备注回访信息'); // 设置窗口的名称
				usermanager.addWindowRemark.show().center();
				usermanager.formPanelRemark.getForm().reset();
				usermanager.formPanelRemark.getForm().loadRecord(record);
				var form=usermanager.formPanelRemark.getForm();
				form.findField("account").setReadOnly(true);
				form.findField("belongid").setReadOnly(true);
			}
});
	// ----------------------存款列表 start--------------------
	usermanager.dispositorAction = new Ext.Action({
				text : '存款列表',
				iconCls : 'Layoutedit',
				disabled : true,
				handler : function() {
						var record = usermanager.grid.getSelectionModel().getSelected();
						usermanager.addDispositorWindow.setIconClass('Coinsadd'); // 设置窗口的样式
						usermanager.addDispositorWindow.setTitle('存款列表'); // 设置窗口的名称
						usermanager.addDispositorWindow.show().center(); // 显示窗口
						usermanager.dispositorDetail.baseParams.account = record.data.account;
						usermanager.dispositorDetail.reload();
				}
	});
	/** 存款数据源 */
	usermanager.dispositorDetail = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : false,
		baseParams : {
			account:'',
			start : 0,
			limit : usermanager.pageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : usermanager.dispositorDetailUrl
		}),
		fields : [ 'poid','platformorders','uiid','paytyple','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','ordercontent','createDate','updateDate','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','cwid','cwname','cwopttime','beforebalance','laterbalance' ],
		listeners : {
			'load' : function(store, records, options) {
			}
		}
	});
	/** 改变页的combo*/
	usermanager.dispositorPageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				usermanager.pageSize  = parseInt(comboBox.getValue());
				usermanager.dispositorbbar.pageSize  = parseInt(comboBox.getValue());
				usermanager.dispositorDetail.baseParams.limit = usermanager.pageSize;
				usermanager.dispositorDetail.baseParams.start = 0;
				usermanager.dispositorDetail.load();
			}
		}
	});
	/** 分页 */
	usermanager.dispositorbbar = new Ext.PagingToolbar({
		pageSize : usermanager.pageSize,
		store : usermanager.dispositorDetail,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', usermanager.dispositorPageSizeCombo ]
	});
	/** 存款列表 */
	usermanager.dispositorgrid = new Ext.grid.EditorGridPanel({
		store : usermanager.dispositorDetail,
		autoScroll : 'auto',
		region : 'center',
		bbar:usermanager.dispositorbbar,
		loadMask : true,
		stripeRows : true,
		border: false,
		columns : [{
			header : '存款订单ID',
			width : 150,
			dataIndex : 'poid',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			hidden : true,
			header : '操作人ID',
			dataIndex : 'createuserid'
		}, {
			hidden : true,
			header : '用户ID',
			dataIndex : 'uiid'
		}, {
			hidden : true,
			header : '收支类型',
			dataIndex : 'paytyple'
		}, {
			hidden : true,
			header : '第三方支付平台ID',
			dataIndex : 'ppid'
		}, {
			hidden : true,
			header : '流水号',
			dataIndex : 'platformorders'
		}, {
			header : '当前状态',
			dataIndex : 'status',
			renderer : function(v) {
				return Share.map(v, usermanager.STATUSMAP, '');
			}
		}, {
			header : '会员账号',
			dataIndex : 'uaccount',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			hidden : false,
			header : '会员姓名',
			dataIndex : 'urealname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '银行名称',
			dataIndex : 'bankname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '存入银行卡',
			width : 150,
			dataIndex : 'bankcard',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '开户行名称',
			dataIndex : 'deposit',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '开户人',
			dataIndex : 'openname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '存款金额',
			dataIndex : 'amount'
		}, {
			header : '存款方式',
			dataIndex : 'paymethods',
			renderer : function(v) {
				return Share.map(v, usermanager.PAYMETHODSMAP, '');
			}
		}, {
			header : '存款时间',
			dataIndex : 'deposittime',
			width : 130,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			}
		}, {
			header : '客服备注',
			dataIndex : 'kfremarks'
		}, {
			header : '操作客服',
			dataIndex : 'kfname'
		}, {
			header : '客服操作时间',
			dataIndex : 'kfopttime',
			width : 130,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			}
		}, {
			header : '财务备注',
			dataIndex : 'cwremarks'
		}, {
			header : '操作财务',
			dataIndex : 'cwname'
		}, {
			header : '财务操作时间',
			dataIndex : 'cwopttime',
			width : 130,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			}
		},{
			header : '存款前余额',
			dataIndex : 'beforebalance',
			width : 150
		},{
			header : '存款后余额',
			dataIndex : 'laterbalance',
			width : 150
		} ]
	});
	
	/** 存款新窗口 */
	usermanager.addDispositorWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 450,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [usermanager.dispositorgrid]
	});
	
// ----------------------存款列表 end--------------------
// ----------------------优惠列表 start--------------------
/** 优惠列表*/
usermanager.couponAction = new Ext.Action({
			text : '优惠列表',
			iconCls : 'Layoutedit',
			disabled : true,
			handler : function() {
						var record = usermanager.grid.getSelectionModel().getSelected();
						usermanager.addCouponWindow.setIconClass('Coinsadd'); // 设置窗口的样式
						usermanager.addCouponWindow.setTitle('存款列表'); // 设置窗口的名称
						usermanager.addCouponWindow.show().center(); // 显示窗口
						usermanager.couponDetail.baseParams.account = record.data.account;
						usermanager.couponDetail.reload();
			}
});
	/** 优惠数据源 */
	usermanager.couponDetail = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : false,
		baseParams : {
			account:'',
			start : 0,
			limit : usermanager.pageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : usermanager.couponDetailUrl
		}),
		fields : [ 'poid','platformorders','uiid','paytyple','ordertype','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','ordercontent','createDate','updateDate','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','cwid','cwname','cwopttime','beforebalance','laterbalance' ],
		listeners : {
			'load' : function(store, records, options) {
			}
		}
	});
	/** 改变页的combo*/
	usermanager.couponPageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				usermanager.pageSize  = parseInt(comboBox.getValue());
				usermanager.couponbbar.pageSize  = parseInt(comboBox.getValue());
				usermanager.couponDetail.baseParams.limit = usermanager.pageSize;
				usermanager.couponDetail.baseParams.start = 0;
				usermanager.couponpDetail.load();
			}
		}
	});
	/** 分页 */
	usermanager.couponbbar = new Ext.PagingToolbar({
		pageSize : usermanager.pageSize,
		store : usermanager.couponDetail,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', usermanager.couponPageSizeCombo ]
	});
	/** 优惠列表 */
	usermanager.coupongrid = new Ext.grid.EditorGridPanel({
		store : usermanager.couponDetail,
		autoScroll : 'auto',
		region : 'center',
		bbar:usermanager.couponbbar,
		loadMask : true,
		stripeRows : true,
		border: false,
		columns : [{
			header : '赠送订单ID',
			width : 150,
			dataIndex : 'poid',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			hidden : true,
			header : '操作人ID',
			dataIndex : 'createuserid'
		}, {
			hidden : true,
			header : '用户ID',
			dataIndex : 'uiid'
		}, {
			hidden : true,
			header : '收支类型',
			dataIndex : 'paytyple'
		}, {
			hidden : true,
			header : '第三方支付平台ID',
			dataIndex : 'ppid'
		}, {
			hidden : true,
			header : '流水号',
			dataIndex : 'platformorders'
		}, {
			hidden : true,
			header : '银行名称',
			dataIndex : 'bankname'
		}, {
			hidden : true,
			header : '存入银行卡',
			dataIndex : 'bankcard'
		}, {
			hidden : true,
			header : '开户行名称',
			dataIndex : 'deposit'
		}, {
			hidden : true,
			header : '开户人',
			dataIndex : 'openname'
		}, {
			header : '订单类型',
			dataIndex : 'ordertype',
			renderer : function(v) {
				return Share.map(v, usermanager.ordertype, '');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '赠送金额',
			dataIndex : 'amount',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '当前状态',
			dataIndex : 'status',
			renderer : function(v) {
				return Share.map(v, usermanager.STATUSMAP, '');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '会员账号',
			dataIndex : 'uaccount',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			}),
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '会员姓名',
			dataIndex : 'urealname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			}),
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '赠送时间',
			dataIndex : 'deposittime',
			width : 130,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			}),
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			}
		}, {
			header : '客服备注',
			dataIndex : 'kfremarks',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '操作客服',
			dataIndex : 'kfname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '客服操作时间',
			dataIndex : 'kfopttime',
			width : 130,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			}),
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			}
			
		}, {
			header : '财务备注',
			dataIndex : 'cwremarks',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '操作财务',
			dataIndex : 'cwname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '财务操作时间',
			dataIndex : 'cwopttime',
			width : 130,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			}),
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			}
		},{
			header : '赠送前余额',
			dataIndex : 'beforebalance',
			width : 150
		},{
			header : '赠送后余额',
			dataIndex : 'laterbalance',
			width : 150
		} ]
	});
	
	/** 优惠新窗口 */
	usermanager.addCouponWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 450,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [usermanager.coupongrid]
	});
	
// ----------------------优惠列表 end--------------------	
// ----------------------取款列表 start--------------------
/** 取款列表*/
usermanager.pickupAction = new Ext.Action({
			text : '取款列表',
			iconCls : 'Layoutedit',
			disabled : true,
			handler : function() {
						var record = usermanager.grid.getSelectionModel().getSelected();
						usermanager.addPickupWindow.setIconClass('Coinsadd'); // 设置窗口的样式
						usermanager.addPickupWindow.setTitle('存款列表'); // 设置窗口的名称
						usermanager.addPickupWindow.show().center(); // 显示窗口
						usermanager.pickupDetail.baseParams.account = record.data.account;
						usermanager.pickupDetail.reload();
			}
});
	/** 取款数据源 */
	usermanager.pickupDetail = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : false,
		baseParams : {
			account:'',
			start : 0,
			limit : usermanager.pageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : usermanager.pickupDetailUrl
		}),
		fields : [ 'poid','platformorders','uiid','paytyple','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','ordercontent','createDate','updateDate','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','cwid','cwname','cwopttime','beforebalance','laterbalance' ],
		listeners : {
			'load' : function(store, records, options) {
			}
		}
	});
	/** 改变页的combo*/
	usermanager.pickupPageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				usermanager.pageSize  = parseInt(comboBox.getValue());
				usermanager.pickupbbar.pageSize  = parseInt(comboBox.getValue());
				usermanager.pickupDetail.baseParams.limit = usermanager.pageSize;
				usermanager.pickupDetail.baseParams.start = 0;
				usermanager.pickupDetail.load();
			}
		}
	});
	/** 分页 */
	usermanager.pickupbbar = new Ext.PagingToolbar({
		pageSize : usermanager.pageSize,
		store : usermanager.pickupDetail,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', usermanager.pickupPageSizeCombo ]
	});
	/** 取款列表 */
	usermanager.pickupgrid = new Ext.grid.EditorGridPanel({
		store : usermanager.pickupDetail,
		autoScroll : 'auto',
		region : 'center',
		bbar:usermanager.pickupbbar,
		loadMask : true,
		stripeRows : true,
		border: false,
		columns : [{
			header : '提款订单ID',
			width : 150,
			dataIndex : 'poid',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			hidden : true,
			header : '操作人ID',
			dataIndex : 'createuserid'
		}, {
			hidden : true,
			header : '用户ID',
			dataIndex : 'uiid'
		}, {
			hidden : true,
			header : '收支类型',
			dataIndex : 'paytyple'
		}, {
			hidden : true,
			header : '第三方支付平台ID',
			dataIndex : 'ppid'
		}, {
			hidden : true,
			header : '流水号',
			dataIndex : 'platformorders'
		}, {
			header : '当前状态',
			dataIndex : 'status',
			renderer : function(v) {
				return Share.map(v, usermanager.STATUSMAP, '');
			}
		}, {
			header : '会员账号',
			dataIndex : 'uaccount',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '会员姓名',
			dataIndex : 'urealname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '银行名称',
			dataIndex : 'bankname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '存入银行卡',
			width : 150,
			dataIndex : 'bankcard',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '开户行名称',
			dataIndex : 'deposit',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '开户人',
			dataIndex : 'openname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '提款金额',
			dataIndex : 'amount',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '提款时间',
			dataIndex : 'deposittime',
			width : 150
		}, {
			header : '客服备注',
			dataIndex : 'kfremarks'
		}, {
			header : '操作客服',
			dataIndex : 'kfname'
		}, {
			header : '客服操作时间',
			dataIndex : 'kfopttime',
			width : 150
		}, {
			header : '财务备注',
			dataIndex : 'cwremarks'
		}, {
			header : '操作财务',
			dataIndex : 'cwname'
		}, {
			header : '财务操作时间',
			dataIndex : 'cwopttime',
			width : 150
		} ,{
			header : '提款前余额',
			dataIndex : 'beforebalance',
			width : 150
		},{
			header : '提款后余额',
			dataIndex : 'laterbalance',
			width : 150
		}]
	});
	
	/** 取款新窗口 */
	usermanager.addPickupWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 450,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [usermanager.pickupgrid]
	});
	
// ----------------------取款列表 end--------------------	
// ----------------------洗码列表 start--------------------
/** 洗码列表*/
usermanager.ximaAction = new Ext.Action({
			text : '洗码列表',
			iconCls : 'Layoutedit',
			disabled : true,
			handler : function() {
						var record = usermanager.grid.getSelectionModel().getSelected();
						usermanager.addXimaWindow.setIconClass('Coinsadd'); // 设置窗口的样式
						usermanager.addXimaWindow.setTitle('存款列表'); // 设置窗口的名称
						usermanager.addXimaWindow.show().center(); // 显示窗口
						usermanager.ximaDetail.baseParams.account = record.data.account;
						usermanager.ximaDetail.reload();
			}
});
	/** 洗码数据源 */
	usermanager.ximaDetail = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : false,
		baseParams : {
			account:'',
			start : 0,
			limit : usermanager.pageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : usermanager.ximaDetailUrl
		}),
		fields : [ 'mxmid','gpid','uiid','account','name','total','ymdstart','ymdend','updatetime','locked' ],
		listeners : {
			'load' : function(store, records, options) {
			}
		}
	});
	/** 改变页的combo*/
	usermanager.ximaPageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				usermanager.pageSize  = parseInt(comboBox.getValue());
				usermanager.ximabbar.pageSize  = parseInt(comboBox.getValue());
				usermanager.ximaDetail.baseParams.limit = usermanager.pageSize;
				usermanager.ximaDetail.baseParams.start = 0;
				usermanager.ximaDetail.load();
			}
		}
	});
	/** 分页 */
	usermanager.ximabbar = new Ext.PagingToolbar({
		pageSize : usermanager.pageSize,
		store : usermanager.ximaDetail,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', usermanager.ximaPageSizeCombo ]
	});
	/** 洗码列表 */
	usermanager.ximagrid = new Ext.grid.EditorGridPanel({
		store : usermanager.ximaDetail,
		autoScroll : 'auto',
		region : 'center',
		bbar:usermanager.ximabbar,
		loadMask : true,
		stripeRows : true,
		border: false,
		columns : [{
			hidden : true,
			header : '主键ID',
			dataIndex : 'mxmid'
		}, {
			hidden : true,
			header : '会员ID',
			dataIndex : 'uiid'
		},{
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
			header : '返水总金额',
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
	
	/** 洗码新窗口 */
	usermanager.addXimaWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 450,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [usermanager.ximagrid]
	});
	
// ----------------------洗码列表 end--------------------	
/**删除*/
usermanager.deleteAction = new Ext.Action({
			text : '删除',
			iconCls : 'Cross',
			disabled : true,
			handler : function() {
				usermanager.deleteFun();
			}
});
/**搜索按钮*/
usermanager.searchAction = new Ext.Action({
			text : '搜索',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				usermanager.searchFun();
			}
});
/** 顶部工具栏 */
usermanager.tbar = [usermanager.addAction,'-',/**usermanager.callAction,'-',*/usermanager.remarkAction,'-',/**usermanager.sendSmsAction,'-',usermanager.sendAnoSmsAction,*/'-',usermanager.deleteAction,'-','&nbsp;',usermanager.dispositorAction,'-','&nbsp;',usermanager.pickupAction,'-','&nbsp;',usermanager.couponAction,'-','&nbsp;',usermanager.ximaAction,'-','&nbsp;',
'会员账号:',{id:'dxAcconut',xtype:'textfield',width:100},'&nbsp;',
'会员姓名:',{id:'dxUname',xtype:'textfield',width:100},'&nbsp;'
];
/** 底部工具条 */
usermanager.bbar = new Ext.PagingToolbar({
			pageSize : usermanager.pageSize,
			store : usermanager.store,
			displayInfo : true,
			items : ['-', '&nbsp;', usermanager.pageSizeCombo]
		});

usermanager.sequenceComboBoxKV = new Ext.data.SimpleStore({  
	fields : ['key', 'value'],  
	data : [['注册时间', 'regtime'], ['登录时间', 'logintime']]  
});
usermanager.sequenceComboBox = new Ext.form.ComboBox({
	hiddenName :'dxsequencec',
	id : 'dxsequencec',
	triggerAction : 'all',
	mode : 'local',
	store : usermanager.sequenceComboBoxKV,
	displayField : 'key',  
    valueField : 'value', 
	allowBlank : true,
	value : "logintime",
	editable : true,
	width:100
});
/** 基本信息-表格 */
usermanager.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : usermanager.store,
			colModel : usermanager.colModel,
			selModel : usermanager.selModel,
			tbar : usermanager.tbar,
			bbar : usermanager.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'usermanagerDesc',
			stripeRows : true,
			listeners : {
				render: function(){
					this.tbar2 = new Ext.Toolbar({
						 renderTo: usermanager.grid.tbar,
						 items:[
						 '电销姓名:',{id:'dxTruename',xtype:'textfield',width:100},'&nbsp;',
						 'QQ号码:',{id:'dxQQ',xtype:'textfield',width:100},'&nbsp;',
						 'E-mail:',{id:'dxEmail',xtype:'textfield',width:100},'&nbsp;',
						 '存款金额:',{id:'dxPayMoney',xtype:'textfield',width:100},'&nbsp;',
						 '排序方式:',usermanager.sequenceComboBox,'&nbsp;'
						 ]
					});
					this.tbar3 = new Ext.Toolbar({
						 renderTo: usermanager.grid.tbar,
						 items:[
						 '开始日期:',{ id:'dxstarttime',xtype:'datetimefield',format:'Y-m-d H:i:s',width:140},'-',
            			 '至',{ id:'dxendtime',xtype:'datetimefield',format:'Y-m-d H:i:s',width:140},'-',
						 '&nbsp;',usermanager.searchAction
						 ]
					});
				}
			},
			viewConfig : {}
		});
		
usermanager.alldxCombo = new Ext.form.ComboBox({
		fieldLabel : '电销人员',
		hiddenName : 'belongid',
		name : 'belongid',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(usermanager.alldx)
				}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		anchor : '99%'
	});
		
usermanager.payertypeCombo = new Ext.form.ComboBox({
		fieldLabel : '玩家类型',
		hiddenName : 'payertype',
		name : 'payertype',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(usermanager.payertype)
				}),
		valueField : 'v',
		displayField : 't',
		value : "slot",
		allowBlank : false,
		editable : false,
		anchor : '99%'
	});


/** 备注 */
usermanager.formPanelRemark = new Ext.form.FormPanel({
	region : 'center',
	autoScroll : false,
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
		name : 'uiid'
	},{
		fieldLabel : '会员账号',
		name : 'account',
		anchor : '99%',
		allowBlank : false
		
	}, usermanager.payertypeCombo, usermanager.alldxCombo,{
			fieldLabel : '电销备注',
			maxLength : 1000,
			xtype : 'textarea',
			name : 'remark',
			anchor : '99%'
		}]
});

/** 编辑新建窗口 */
usermanager.addWindowRemark = new Ext.Window({
	layout : 'fit',
	width : 500,
	height : 260,
	closeAction : 'hide',
	plain : true,
	modal : true,
	resizable : true,
	items : [ usermanager.formPanelRemark ],
	buttons : [ {
		text : '保存',
		handler : function() {
			usermanager.saveFun();
		}
	}, {
		text : '取消',
		handler : function() {
			usermanager.addWindowRemark.hide();
		}
	} ]
});

/**
 * 搜索参数
 * @return {}
 */
usermanager.searchParams = function(){
	var obj={};
	if($("#dxAcconut").val()!=""){
		obj.account = $("#dxAcconut").val();
	}
	if($("#dxUname").val()!=""){
		obj.uname = $("#dxUname").val();
	}
	if($("#dxTruename").val()!=""){
		obj.truename = $("#dxTruename").val();
	}
	if($("#dxPhone").val()!=""){
		obj.phone = $("#dxPhone").val();
	}
	if($("#dxQQ").val()!=""){
		obj.qq = $("#dxQQ").val();
	}
	if($("#dxEmail").val()!=""){
		obj.email = $("#dxEmail").val();
	}
	if($("#dxPayMoney").val() != ""){
		obj.paymoney = $("#dxPayMoney").val();
	}
	if($("#dxstarttime").val() != ""){
		obj.starttime = $("#dxstarttime").val();
	}
	if($("#dxendtime").val() != ""){
		obj.endtime = $("#dxendtime").val();
	}
	var sort = $("#dxsequencec").prev();
	if($(sort).val() != ""){
		obj.sequence = $(sort).val();
	}
	return obj;
}
usermanager.searchFun = function(){
	usermanager.store.load({params: usermanager.searchParams()});
}
usermanager.store.on('beforeload',function(store, options){
    usermanager.store.baseParams = usermanager.searchParams();
});
	
usermanager.alwaysFun = function() {
	Share.resetGrid(usermanager.grid);
	usermanager.deleteAction.disable();
	usermanager.remarkAction.disable();
	usermanager.dispositorAction.disable();
	usermanager.pickupAction.disable();
	usermanager.couponAction.disable();
	usermanager.ximaAction.disable();
	usermanager.callAction.disable();
	usermanager.sendSmsAction.disable();
};

/**
 * 删除
 */
usermanager.deleteFun = function(){
	var record = usermanager.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '确定要删除这条记录吗?', function(btn, text) {
		if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : usermanager.del + record.data.uiid + ".do",
					callback : function(json) {
						usermanager.alwaysFun();
						usermanager.store.reload();
					}
				});
			}
	});
}

usermanager.saveFun = function(){
	var form = usermanager.formPanelRemark.getForm();

	if(!form.isValid()){
 		return;
 	}
	// 发送请求
	Share.AjaxRequest({
				url : usermanager.save,
				params : form.getValues(),
				callback : function(json) {
					if (json.success==false){
					    Ext.MessageBox.alert('Status', json.msg, showResult);
						return;
					}else{
					    usermanager.addWindowRemark.hide();
						usermanager.alwaysFun();
						usermanager.store.reload();
					}
				}
			});
}
// 拨打电话
usermanager.callFun =function(){
	var record = usermanager.grid.getSelectionModel().getSelected();
	Share.AjaxRequest({
		url : usermanager.callUrl +record.data.extno+"/"+ record.data.phone +".do",
		callback : function(json) {
			if(!json.success){
				 Ext.MessageBox.alert(json.msg);
			}
		}
	});
};
 
usermanager.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [usermanager.grid]
		});

</script>