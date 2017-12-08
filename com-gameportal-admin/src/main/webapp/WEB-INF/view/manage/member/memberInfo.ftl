<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.memberInfos"); // 自定义一个命名空间
memberInfo = Ext.market.memberInfos; // 定义命名空间的别名
memberInfo = {
	all : '/manage/memberinfo/queryMemberInfo.do',// 加载所有
	save : "/manage/memberinfo/saveMemberInfo.do",//保存
	del : "/manage/memberinfo/delMemberInfo/",//删除
	ti : "/manage/memberinfo/logoutPT/",//踢出会员
	modifyPwd:"/manage/memberinfo/modifyPwd.do",//修改密码
	upgrade:"/manage/memberinfo/upgrade.do",
	setstatus: "/manage/memberinfo/setstatus.do",//设置状态
	getProxy:"/manage/proxy/queryProxyInfo.do",//获取代理商
	trans:"/manage/memberinfo/gameTransfer.do",//转账
	queryWalletMoney:"/manage/memberinfo/queryWalletMoney/",//查询游戏余额
	sendSmsUrl:"/manage/member/sendSms/",//发送短信
	callUrl:'/manage/memberinfo/kf/call/',// 拨打电话
	birthdayCount:'/manage/memberinfo/queryBirthDayCount.do',//查询生日人数
	birthdayResult:'/manage/memberinfo/queryBirthDayResult.do',//查询会员生日列表
	authInfo:'/manage/member/authInfo/',//验证手机、邮箱
	openWeekRake:"/manage/member/openWeekRake/",// 打开提款
	closeWeekRake:"/manage/member/closeWeekRake/",// 关闭提款
	pageSize : 30, // 每页显示的记录数
	memStatus : eval('(${fields.memStatus})'),
	memGrade:eval('(${fields.memGrade})'),
	accounttype:eval('(${userType})'),
	typeflag : eval('(${typeflag})'),
	isPayMoney : eval('(${isPayMoney})'),//是否充值
	isRelAccount : eval('(${isRelAccount})'),//是否关联账号
	weekRake :eval('(${weekRake})'), // 周返水
	isAdmin : '${isAdmin}',	//是否是管理员
	isKF : '${isKF}', //是否是客服
	isDX : '${isDX}', //是否是电销
	isSC : '${isSC}', //是否是市场专员
	isFK : '${isFK}', //是否是风控
	isHZ : '${isHZ}', //是否是合作伙伴
	isEdit : false
};


/** 改变页的combo */
memberInfo.pageSizeCombo = new Share.pageSizeCombo({
			value : '30',
			listeners : {
				select : function(comboBox) {
					memberInfo.pageSize = parseInt(comboBox.getValue());
					memberInfo.bbar.pageSize = parseInt(comboBox.getValue());
					memberInfo.store.baseParams.limit = memberInfo.pageSize;
					memberInfo.store.baseParams.start = 0;
					memberInfo.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
memberInfo.pageSize = parseInt(memberInfo.pageSizeCombo.getValue());
/** 基本信息-数据源 */
memberInfo.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : memberInfo.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : memberInfo.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['uiid', 'account', 'accounttype','withdrawlFlag', 'uname', 'identitycard', 'phone', 
			    	'email','qq', 'birthday','grade','status','createDate','url','regip','puname','create_date','typeflag','phonevalid','emailvalid','paymoney','remark','relaflag']),
			listeners : {
				'load' : function(store, records, options) {
				}
			}
		});
		
/** 基本信息-选择模式 */
memberInfo.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				if(memberInfo.isAdmin == '1'){
					memberInfo.deleteAction.enable();
					memberInfo.editAction.enable();
				}
				if(memberInfo.isFK == '1'){
					memberInfo.editAction.enable();
				}
				memberInfo.modifyPasAction.enable();
				memberInfo.tiAction.enable();
				memberInfo.enabledAction.enable();
				memberInfo.disabledAction.enable();
				memberInfo.upgradeAction.enable();
				memberInfo.transAction.enable();
				memberInfo.openWeekRakeAction.enable();
				memberInfo.closeWeekRakeAction.enable();
				if(memberInfo.isDX =='1' || memberInfo.isKF =='1' || memberInfo.isAdmin == '1'){
					memberInfo.sendSmsAction.enable();
					memberInfo.callAction.enable();
					memberInfo.authPhoneAction.enable();
					memberInfo.authEmailAction.enable();
				}else if(memberInfo.isSC =='1'){//市场专员可以拨打电话、发送短信
					memberInfo.callAction.enable();
					memberInfo.sendSmsAction.enable();
				}
				if(memberInfo.isFK == '1'){
					memberInfo.authPhoneAction.enable();
					memberInfo.authEmailAction.enable();
					memberInfo.sendSmsAction.enable();
				}
				if(memberInfo.isHZ == '1'){
					memberInfo.authPhoneAction.enable();
					memberInfo.authEmailAction.enable();
					memberInfo.sendSmsAction.enable();
					memberInfo.upgradeAction.disable();
					memberInfo.sendSmsAction.disable();
					memberInfo.callAction.disable();
					memberInfo.authPhoneAction.disable();
					memberInfo.authEmailAction.disable();
					memberInfo.tiAction.disable();
					memberInfo.addAction.disable();
					memberInfo.birthdayAction.disable();
				}
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				memberInfo.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
memberInfo.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [memberInfo.selModel, {
						hidden : true,
						header : '用户ID',
						dataIndex : 'uiid'
					}, {
						header : '帐号',
						dataIndex : 'account',
						width : 95,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '姓名',
						dataIndex : 'uname',
						width : 84,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '关联账号',
						dataIndex : 'relaflag',
						width : 84,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						}),
						renderer : function(v) {
							if(v == 1){
								return '<span style="color:blue;">已关联</span>';
							}else{
								return '<span style="color:#992211;">未关联</span>';
							}
						}
					}, {
						header : '提款状态',
						dataIndex : 'withdrawlFlag',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						}),
						renderer : function(v) {
							if(v == '1'){
								return '<span style="color:green;">启用</span>';
							}else{
								return '<span style="color:red;">禁用</span>';
							}
						}
					}, {
						hidden : true,
						header : '会员类型',
						dataIndex : 'accounttype',
						width : 60,
						renderer : function(v) {
							if(v == 0){
								return '<span style="color:blue;">普通会员</span>';
							}else{
								return '<span style="color:#992211;">代理会员</span>';
							}
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '手机认证',
						dataIndex : 'phonevalid',
						width : 60,
						renderer : function(v) {
							if(v == 0){
								return '<span style="color:red;">未认证</span>';
							}else{
								return '<span style="color:green;">已认证</span>';
							}
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '邮箱认证',
						dataIndex : 'emailvalid',
						width : 60,
						renderer : function(v) {
							if(v == 0){
								return '<span style="color:red;">未认证</span>';
							}else if(v==1){
								return '<span style="color:green;">已认证</span>';
							}else{
								return '<span style="color:orange;">待认证</span>';
							}
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '充值金额',
						dataIndex : 'paymoney',
						width : 60,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '会员类型',
						dataIndex : 'paymoney',
						width : 60,
						renderer : function(v) {
							if(v >= 100){
								return '<span style="color:blue;">有效会员</span>';
							}else{
								return '<span style="color:#992211;">注册会员</span>';
							}
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						hidden : true,
						header : '身份证',
						dataIndex : 'identitycard',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						//hidden : true,
						header : '电话',
						dataIndex : 'phone',
						width : 96,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						//hidden : true,
						header : '邮箱',
						dataIndex : 'email',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						//hidden : true,
						header : 'QQ',
						dataIndex : 'qq',
						width : 81,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '会员等级',
						dataIndex : 'grade',
						width : 60,
						renderer : function(v) {
							return Share.map(v, memberInfo.memGrade, '');
       						
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '状态',
						dataIndex : 'status',
						width : 60,
						renderer : function(v) {
							return Share.map(v, memberInfo.memStatus, '');
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '注册IP',
						dataIndex : 'regip',
						width : 104,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : 'URL',
						dataIndex : 'url',
						width : 200,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '所属代理',
						dataIndex : 'puname',
						width : 104,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '创建日期',
						dataIndex : 'create_date',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						}),
						renderer : function(v) {
       						return new Date(v).format('Y-m-d H:i:s');
      					}
					},{
						header : '备注',
						hidden : true,
						dataIndex : 'remark',
						width : 104,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}]
		});
		
/** 新建 */
memberInfo.addAction = new Ext.Action({
			text : '新建',
			iconCls : 'Add',
			disabled : memberInfo.isHZ == '1'?true:false,
			handler : function() {
				memberInfo.isEdit = false;
				memberInfo.addWindow.setIconClass('Applicationadd'); // 设置窗口的样式
				memberInfo.addWindow.setTitle('新建平台'); // 设置窗口的名称
				memberInfo.addWindow.show().center(); // 显示窗口
				memberInfo.formPanel.getForm().reset(); // 清空表单里面的元素的值.
				
				var form=memberInfo.formPanel.getForm();
				form.findField("account").setReadOnly(false);
				form.findField("uname").setReadOnly(false);
				form.findField("identitycard").setReadOnly(false);
				//form.findField("phone").setReadOnly(false);
				//form.findField("email").setReadOnly(false);
				//form.findField("qq").setReadOnly(false);
				form.findField("passwd").setVisible(true);
				form.findField("alignPasswd").setVisible(true);
				form.findField("atmpasswd").setVisible(true);
				form.findField("alignAtmpasswd").setVisible(true);
			}
		});
/** 编辑 */
memberInfo.editAction = new Ext.Action({
			text : '编辑',
			iconCls : 'Applicationedit',
			disabled : true,
			handler : function() {
				memberInfo.isEdit = true;
				var record = memberInfo.grid.getSelectionModel().getSelected();
				memberInfo.addWindow.setIconClass('Applicationedit'); // 设置窗口的样式
				memberInfo.addWindow.setTitle('编辑用户信息'); // 设置窗口的名称
				memberInfo.addWindow.show().center();
				memberInfo.formPanel.getForm().reset();
				memberInfo.formPanel.getForm().loadRecord(record);
				var form=memberInfo.formPanel.getForm();
				form.findField("account").setReadOnly(true);
				form.findField("proxyaccount").setValue(record.data.puname);
				if(memberInfo.isAdmin != '1' && memberInfo.isFK != '1'){
					form.findField("uname").setDisabled(true);
					form.findField("identitycard").setDisabled(true);
					form.findField("phone").setDisabled(true);
					form.findField("email").setDisabled(true);
					form.findField("qq").setDisabled(true);
					form.findField("puiid").setVisible(true);
				}
				if(memberInfo.isFK == '1'){
					form.findField("uname").setReadOnly(true);
					form.findField("identitycard").setDisabled(true);
				}
				
				form.findField("passwd").setVisible(false);
				form.findField("alignPasswd").setVisible(false);
				form.findField("atmpasswd").setVisible(false);
				form.findField("alignAtmpasswd").setVisible(false);
			}
		});
		
/** 删除 */
memberInfo.deleteAction = new Ext.Action({
			text : '删除',
			iconCls : 'Cross',
			disabled : true,
			handler : function() {
				memberInfo.delFun();
			}
		});

/**修改密码*/
memberInfo.modifyPasAction = new Ext.Action({
			text : '修改密码',
			iconCls : 'Lockedit',
			disabled : true,
			handler : function() {
				var record = memberInfo.grid.getSelectionModel().getSelected();
				memberInfo.modifyPasWindow.setIconClass('Lockedit'); // 设置窗口的样式
				memberInfo.modifyPasWindow.setTitle('修改密码'); // 设置窗口的名称
				memberInfo.modifyPasWindow.show().center();
				memberInfo.modifyPasFormPanel.getForm().reset();
				memberInfo.modifyPasFormPanel.getForm().findField("uiid").setValue(record.data.uiid);
			}
		});
/** 踢出在线会员 */
memberInfo.tiAction = new Ext.Action({
			text : '踢出',
			iconCls : 'Userdelete',
			disabled : true,
			handler : function() {
				memberInfo.tiFun();
			}
		});

/** 启用 */
 memberInfo.enabledAction = new Ext.Action({
			text : '启用',
			iconCls : 'Lockopen',
			disabled : true,
			handler : function() {
					var record = memberInfo.grid.getSelectionModel().getSelected();
					var param={uiid:record.data.uiid,status:'2'};
					Share.AjaxRequest({
					url : memberInfo.setstatus,
					params : param,
					callback : function(json) {
								if (json.success==false){
							       Ext.MessageBox.alert('Status', json.msg, showResult);
								   return;
								}else{
								  //TODO
									memberInfo.store.reload();
								}
							}
					});
			}
 });
 /** 禁用form */
memberInfo.disableFormPanel = new Ext.form.FormPanel({
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
				xtype : 'textfield',
			   	fieldLabel : '会员账号',
				maxLength : 64,
				allowBlank : true,
				name : 'account',
				anchor : '99%'
			},{
				xtype : 'textarea',
			   	fieldLabel : '禁用原因',
				maxLength : 64,
				allowBlank : false,
				name : 'remark',
				anchor : '99%'
			}]
});
		
 /**修改密码窗口 */
memberInfo.disableWindow = new Ext.Window({
	layout : 'fit',
	width : 300,
	height : 170,
	closeAction : 'hide',
	plain : true,
	modal : true,
	resizable : true,
	items : [memberInfo.disableFormPanel],
	buttons : [{
				text : '确定',
				handler : function() {
					var form = memberInfo.disableFormPanel.getForm();
					var uiid = form.findField("uiid").getValue();
					var remark = form.findField("remark").getValue();
					if(form.isValid()){
						var param={uiid:uiid,status:'0',remark:remark};
						Share.AjaxRequest({
						url : memberInfo.setstatus,
						params : param,
						callback : function(json) {
								if (json.success==false){
							       Ext.MessageBox.alert('Status', json.msg, showResult);
								   return;
								}else{
								  	memberInfo.disableWindow.hide();
									memberInfo.store.reload();
								}
							}
						});
					}
				}
			}, {
				text : '重置',
				handler : function() {
					var form = memberInfo.disableFormPanel.getForm();
					var id = form.findField("uiid").getValue();
					var account = form.findField("account").getValue();
					form.reset();
					if (id != '')
						form.findField("uiid").setValue(id);
						form.findField("account").setValue(account).setReadOnly(true);
				}
			}]
});
		
 /** 禁用 */
 memberInfo.disabledAction = new Ext.Action({
	text : '禁用',
	iconCls : 'Lockkey',
	disabled : true,
	handler : function() {
		var record = memberInfo.grid.getSelectionModel().getSelected();
		memberInfo.disableWindow.setIconClass('Lockedit'); // 设置窗口的样式
		memberInfo.disableWindow.setTitle('禁用'); // 设置窗口的名称
		memberInfo.disableWindow.show().center();
		memberInfo.disableFormPanel.getForm().reset();
		memberInfo.disableFormPanel.getForm().findField("uiid").setValue(record.data.uiid);
		memberInfo.disableFormPanel.getForm().findField("account").setValue(record.data.account).setReadOnly(true);
		memberInfo.disableFormPanel.getForm().findField("remark").setValue(record.data.remark);;
	}
 });
 
 
 /** 会员升级 */
memberInfo.upgradeAction = new Ext.Action({
			text : '会员升级',
			iconCls : 'Userstar',
			disabled : true,
			handler : function() {
				var record = memberInfo.grid.getSelectionModel().getSelected();
				memberInfo.upgradeWindow.setIconClass('Userstar'); // 设置窗口的样式
				memberInfo.upgradeWindow.setTitle('会员升级'); // 设置窗口的名称
				memberInfo.upgradeWindow.show().center();
				memberInfo.upgradeFormPanel.getForm().reset();
				memberInfo.upgradeFormPanel.getForm().findField("uiid").setValue(record.data.uiid);
				memberInfo.upgradeFormPanel.getForm().findField("oldgrade").setValue(record.data.grade);
			}
		});
 
/**会员等级 */		
memberInfo.gradeCombo = new Ext.form.ComboBox({
			fieldLabel : '会员等级',
			hiddenName : 'grade',
			name : 'grade',
			triggerAction : 'all',
			mode : 'local',
			store : new Ext.data.ArrayStore({
						fields : ['v', 't'],
						data : Share.map2Ary(memberInfo.memGrade)
					}),
			valueField : 'v',
			displayField : 't',
			allowBlank : false,
			editable : false,
			value : "1",
			anchor : '99%'
			
		});	
/**会员转账*/
memberInfo.transAction = new Ext.Action({
			text : '会员转账',
			iconCls : 'Usergo',
			disabled : true,
			handler : function() {
				var record = memberInfo.grid.getSelectionModel().getSelected();
				memberInfo.transWindow.setIconClass('Usergo'); // 设置窗口的样式
				memberInfo.transWindow.setTitle('会员转账'); // 设置窗口的名称
				memberInfo.transWindow.show().center();
				memberInfo.transFormPanel.getForm().reset();
				memberInfo.transFormPanel.getForm().findField("uiid").setValue(record.data.uiid);
				memberInfo.queryWalletMoneyFun(record.data.uiid,'AA');
			}
		});

/**发送短信*/
memberInfo.sendSmsAction = new Ext.Action({
	text : '发送短信',
	iconCls : 'Usergo',
	disabled : true,
	handler : function() {
		var record = memberInfo.grid.getSelectionModel().getSelected();
		memberInfo.sendWindow.setIconClass('Usergo'); // 设置窗口的样式
		memberInfo.sendWindow.setTitle('发送短信'); // 设置窗口的名称
		memberInfo.sendWindow.show().center();
		memberInfo.sendSmsFormPanel.getForm().reset();
		memberInfo.sendSmsFormPanel.getForm().findField("uiid").setValue(record.data.uiid);
	}
});

/** 拨打电话 */
memberInfo.callAction = new Ext.Action({
	text : '拨打电话',
	iconCls : 'Anchor',
	disabled : true,
	handler : function() {
		memberInfo.callFun();
	}
});

/** 启用提款 */
memberInfo.openWeekRakeAction = new Ext.Action({
	text : '启用提款',
	iconCls : 'Lockopen',
	disabled : true,
	handler : function() {
		memberInfo.openFun();
	}
});
/** 禁用提款 */
memberInfo.closeWeekRakeAction = new Ext.Action({
	text : '禁用提款',
	iconCls : 'Lockkey',
	disabled : true,
	handler : function() {
		memberInfo.closeFun();
	}
});

/** 生日会员 */
memberInfo.birthdayAction = new Ext.Action({
		text : '会员生日',
		iconCls : 'Userstar',
		handler : function() {
			memberInfo.addBirthdayWindow.setIconClass('Userstar'); // 设置窗口的样式
			memberInfo.addBirthdayWindow.setTitle('会员生日列表'); // 设置窗口的名称
			memberInfo.addBirthdayWindow.show().center(); // 显示窗口
			memberInfo.birthdayDetail.reload();
		}
	});

/** 认证手机 */
memberInfo.authPhoneAction = new Ext.Action({
		text : '认证手机',
		iconCls : 'Userstar',
		disabled : true,
		handler : function() {
			memberInfo.authPhoneFun();
		}
	});
/** 认证邮箱 */
memberInfo.authEmailAction = new Ext.Action({
		text : '认证邮箱',
		iconCls : 'Userstar',
		disabled : true,
		handler : function() {
			memberInfo.authEmailFun();
		}
	});
	
/** 会员生日详情数据源 */
memberInfo.birthdayDetail = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : false,
		baseParams : {
			start : 0,
			limit : memberInfo.pageSize
		},  
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : memberInfo.birthdayResult
		}),
		fields : [ 'account','uname','phone','email','qq','birthday','grade','identitycard','accounttype','phonevalid','emailvalid','status','regip','url','createDate'],
		listeners : {
			'load' : function(store, records, options) {
			}
		}
	});
	/** 改变页的combo*/
	memberInfo.birthdayPageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				memberInfo.pageSize  = parseInt(comboBox.getValue());
				memberInfo.birthdaybbar.pageSize  = parseInt(comboBox.getValue());
				memberInfo.birthdayDetail.baseParams.limit = memberInfo.pageSize;
				memberInfo.birthdayDetail.baseParams.start = 0;
				memberInfo.birthdayDetail.load();
			}
		}
	});
	/** 分页 */
	memberInfo.birthdaybbar = new Ext.PagingToolbar({
		pageSize : memberInfo.pageSize,
		store : memberInfo.birthdayDetail,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', memberInfo.birthdayPageSizeCombo ]
	});
	/** 会员生日列表 */
	memberInfo.birthdaygrid = new Ext.grid.EditorGridPanel({
		store : memberInfo.birthdayDetail,
		autoScroll : 'auto',
		region : 'center',
		bbar:memberInfo.birthdaybbar,
		loadMask : true,
		stripeRows : true,
		border: false,
		columns : [
					{
						header : '帐号',
						dataIndex : 'account',
						width : 95,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '姓名',
						dataIndex : 'uname',
						width : 84,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '生日',
						dataIndex : 'birthday',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						}),
						renderer : function(v) {
       						return new Date(v).format('Y-m-d H:i:s');
      					}
					},{
						header : '会员类型',
						dataIndex : 'accounttype',
						width : 60,
						renderer : function(v) {
							if(v == 0){
								return '<span style="color:blue;">普通会员</span>';
							}else{
								return '<span style="color:#992211;">代理会员</span>';
							}
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '手机认证',
						dataIndex : 'phonevalid',
						width : 60,
						renderer : function(v) {
							if(v == 0){
								return '<span style="color:red;">未认证</span>';
							}else{
								return '<span style="color:green;">已认证</span>';
							}
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '邮箱认证',
						dataIndex : 'emailvalid',
						width : 60,
						renderer : function(v) {
							if(v == 0){
								return '<span style="color:red;">未认证</span>';
							}else if(v==1){
								return '<span style="color:green;">已认证</span>';
							}else{
								return '<span style="color:orange;">待认证</span>';
							}
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '身份证',
						dataIndex : 'identitycard',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '电话',
						dataIndex : 'phone',
						width : 96,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '邮箱',
						dataIndex : 'email',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : 'QQ',
						dataIndex : 'qq',
						width : 81,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '会员等级',
						dataIndex : 'grade',
						width : 60,
						renderer : function(v) {
							return Share.map(v, memberInfo.memGrade, '');
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '状态',
						dataIndex : 'status',
						width : 60,
						renderer : function(v) {
							return Share.map(v, memberInfo.memStatus, '');
       						
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '注册IP',
						dataIndex : 'regip',
						width : 104,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : 'URL',
						dataIndex : 'url',
						width : 200,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '创建日期',
						dataIndex : 'createDate',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						}),
						renderer : function(v) {
       						return new Date(v).format('Y-m-d H:i:s');
      					}
					}
		]
	});
	
	/** 新建会员生日窗口 */
	memberInfo.addBirthdayWindow = new Ext.Window({
		layout : 'fit',
		width : 1000,
		height : 450,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [memberInfo.birthdaygrid]
	});
	
// ----------------------生日会员列表结束-------------------
	

/** 顶部工具栏 */
memberInfo.tbar = [memberInfo.addAction, '-', memberInfo.editAction, '-',
		memberInfo.deleteAction, '-',memberInfo.modifyPasAction,'-',memberInfo.tiAction,'-', memberInfo.enabledAction,'-', 
		memberInfo.disabledAction,'-',memberInfo.openWeekRakeAction,'-',memberInfo.closeWeekRakeAction,'-',memberInfo.upgradeAction,'-',memberInfo.transAction,'-',memberInfo.birthdayAction,'-',memberInfo.sendSmsAction,memberInfo.authPhoneAction,'-',memberInfo.authEmailAction];
/** 底部工具条 */
memberInfo.bbar = new Ext.PagingToolbar({
			pageSize : memberInfo.pageSize,
			store : memberInfo.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', memberInfo.pageSizeCombo]
		});
/** 基本信息-表格 */
memberInfo.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : memberInfo.store,
			colModel : memberInfo.colModel,
			selModel : memberInfo.selModel,
			tbar : memberInfo.tbar,
			bbar : memberInfo.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'memberInfoDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {},
			listeners : {
               render: function() {
               this.tbar2 = new Ext.Toolbar({  
               renderTo: memberInfo.grid.tbar,
               items:[
               			'账号:',
                        new Ext.form.TextField({ id:'memberInfo_account',width:80 }),
                        '姓名:',
                        new Ext.form.TextField({ id:'memberInfo_uname',width:80 }),
                        '星级:',
                       		new Ext.form.ComboBox({
								
								hiddenName :'memberInfo_grade',
								id : 'memberInfo_grade',
								triggerAction : 'all',
								mode : 'local',
								store : new Ext.data.ArrayStore({
											fields : ['v', 't'],
											data : Share.map2Ary(memberInfo.memGrade)
										}),
								valueField : 'v',
								displayField : 't',
								allowBlank : true,
								editable : true,
								width:80
								
			
							}),
                         '电话:',
                        new Ext.form.TextField({ id:'memberInfo_phone',width:100 }),
                        '邮箱:',
                        new Ext.form.TextField({ id:'memberInfo_email',width:120 }),
                         'QQ:',
                        new Ext.form.TextField({ id:'memberInfo_qq',width:80 }),
                        'ip:',
                        new Ext.form.TextField({ id:'memberInfo_regip',width:120 }),
                        '代理url:',
                        new Ext.form.TextField({ id:'memberInfo_url',width:250 })
                       ]
            	}),
               this.tbar3 = new Ext.Toolbar({  
               renderTo: memberInfo.grid.tbar,
               items:[
                        '代理商:',
						new Ext.form.TextField({ id:'memberInfo_puname',width:120 }),
                        '注册日期:',
                        new Ext.form.DateField({ id:'memberInfo_beginDate',
                        						showToday:true,
                        						format:'Y-m-d',
                        						invalidText:'日期输入非法',
                        						width:120 
                        }),
                        '~',
                        new Ext.form.DateField({ id:'memberInfo_endDate',
                        						showToday:true,
                        						format:'Y-m-d',
                        						invalidText:'日期输入非法',
                        						width:120 
                        }),
                        '用户类型:',
                       		new Ext.form.ComboBox({
								hiddenName :'account_type',
								id : 'account_type',
								triggerAction : 'all',
								mode : 'local',
								store : new Ext.data.ArrayStore({
											fields : ['v', 't'],
											data : Share.map2Ary(memberInfo.accounttype)
										}),
								valueField : 'v',
								displayField : 't',
								allowBlank : true,
								value:'',
								editable : true,
								width:80
						}),'&nbsp;',
                        '是否充值:',
                       		new Ext.form.ComboBox({
								
								hiddenName :'memberInfo_paymoney',
								id : 'memberInfo_paymoney',
								triggerAction : 'all',
								mode : 'local',
								store : new Ext.data.ArrayStore({
											fields : ['v', 't'],
											data : Share.map2Ary(memberInfo.isPayMoney)
										}),
								valueField : 'v',
								displayField : 't',
								allowBlank : true,
								value:'',
								editable : true,
								width:80
							}),'&nbsp;',
							'关联账号:',
                       		new Ext.form.ComboBox({
								hiddenName :'memberInfo_relaccount',
								id : 'memberInfo_relaccount',
								triggerAction : 'all',
								mode : 'local',
								store : new Ext.data.ArrayStore({
											fields : ['v', 't'],
											data : Share.map2Ary(memberInfo.isRelAccount)
										}),
								valueField : 'v',
								displayField : 't',
								allowBlank : true,
								value:'',
								editable : true,
								width:80
							}),'提款状态:',
                       		new Ext.form.ComboBox({
								hiddenName :'memberInfo_weekrake',
								id : 'memberInfo_weekrake',
								triggerAction : 'all',
								mode : 'local',
								store : new Ext.data.ArrayStore({
											fields : ['v', 't'],
											data : Share.map2Ary(memberInfo.weekRake)
										}),
								valueField : 'v',
								displayField : 't',
								allowBlank : true,
								value:'',
								editable : true,
								width:80
							}),'&nbsp;',
                        {
                        	text: '查询',
                        	iconCls: 'Zoom',
                        	handler: function(){ memberInfo.selectFun(); 
                        }
                }]
            	})
          	 }
			}
		});

memberInfo.statusCombo = new Ext.form.ComboBox({
			fieldLabel : '状态',
			hiddenName : 'status',
			name : 'status',
			triggerAction : 'all',
			mode : 'local',
			store : new Ext.data.ArrayStore({
						fields : ['v', 't'],
						data : Share.map2Ary(memberInfo.ENABLED)
					}),
			valueField : 'v',
			displayField : 't',
			allowBlank : false,
			editable : false,
			value : "1",
			anchor : '99%'
		});
/**帐号类型 */		
memberInfo.accounttypeCombo = new Ext.form.ComboBox({
			fieldLabel : '帐号类型',
			hiddenName : 'accounttype',
			name : 'accounttype',
			triggerAction : 'all',
			mode : 'local',
			store : new Ext.data.ArrayStore({
						fields : ['v', 't'],
						data : Share.map2Ary(memberInfo.accounttype)
					}),
			valueField : 'v',
			displayField : 't',
			allowBlank : false,
			editable : false,
			value : "0",
			anchor : '99%'
		});	
		
		memberInfo.typeflagCombo = new Ext.form.ComboBox({
			fieldLabel : '会员来源',
			hiddenName : 'typeflag',
			name : 'typeflag',
			triggerAction : 'all',
			mode : 'local',
			store : new Ext.data.ArrayStore({
						fields : ['v', 't'],
						data : Share.map2Ary(memberInfo.typeflag)
					}),
			valueField : 'v',
			displayField : 't',
			allowBlank : false,
			editable : false,
			value : "0",
			anchor : '99%'
		});		

/**会员状态 */		
memberInfo.memStatusCombo = new Ext.form.ComboBox({
			fieldLabel : '会员状态',
			hiddenName : 'status',
			name : 'status',
			triggerAction : 'all',
			mode : 'local',
			store : new Ext.data.ArrayStore({
						fields : ['v', 't'],
						data : Share.map2Ary(memberInfo.memStatus)
					}),
			valueField : 'v',
			displayField : 't',
			allowBlank : false,
			editable : false,
			value : "1",
			anchor : '99%'
		});
		
memberInfo.startDateField = new Ext.form.DateField({
		fieldLabel : '生日',
		name:'birthday',
        showToday:true,
        format:'Y-m-d',
        invalidText:'日期输入非法',
        allowBlank : true,
        anchor : '99%'
});
	
/** 基本信息-详细信息的form */
memberInfo.formPanel = new Ext.form.FormPanel({
			autoScroll : true,
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
					}, {
						fieldLabel : '帐号',
						maxLength : 64,
						allowBlank : false,
						name : 'account',
						anchor : '99%'
					}, {
						
						fieldLabel : '登陆密码',
						maxLength : 64,
						allowBlank : false,
						name : 'passwd',
						inputType: 'password',
						anchor : '99%'
					}, {
					   
						fieldLabel : '再次密码',
						maxLength : 64,
						allowBlank : false,
						name : 'alignPasswd',
						inputType: 'password',
						anchor : '99%'
					},
					memberInfo.accounttypeCombo,memberInfo.typeflagCombo,{
					   	fieldLabel : '真实姓名',
						maxLength : 64,
						allowBlank : true,
						name : 'uname',
						anchor : '99%'
					},{
					   	fieldLabel : '所属代理',
						maxLength : 64,
						allowBlank : true,
						name : 'proxyaccount',
						anchor : '99%'
					},{
					   	fieldLabel : '身份证',
						maxLength : 18,
						allowBlank : true,
						name : 'identitycard',
						anchor : '99%'
					},{
					   	fieldLabel : '电话',
						maxLength : 14,
						allowBlank : true,
						name : 'phone',
						anchor : '99%'
					},{
					   	fieldLabel : '邮箱',
						maxLength : 25,
						allowBlank : true,
						//vtype: "email",    //email格式验证 
   						//vtypeText: "不是有效的邮箱地址",     //错误提示信息
						name : 'email',
						anchor : '99%'
					},{
					   	fieldLabel : 'QQ',
						maxLength : 25,
						allowBlank : true,
						name : 'qq',
						anchor : '99%'
					},{
					   	fieldLabel : '注册IP',
						maxLength : 14,
						allowBlank : true,
						name : 'regip',
						anchor : '99%'
					},memberInfo.startDateField ,memberInfo.gradeCombo,{
					    
					    fieldLabel : '提取密码',
						maxLength : 12,
						allowBlank : false,
						name : 'atmpasswd',
						 inputType: 'password',
						anchor : '99%'
					
					},{
					  
					    fieldLabel : '再次密码',
						maxLength : 64,
						allowBlank : false,
						name : 'alignAtmpasswd',
					    inputType: 'password',
						anchor : '99%'
					},
					memberInfo.memStatusCombo]
		});
/** 修改密码form */
memberInfo.modifyPasFormPanel = new Ext.form.FormPanel({
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
						xtype : 'textfield',
						fieldLabel : '新密码',
						maxLength : 64,
						allowBlank : false,
						msgTarget: 'side',
						name : 'passwd',
						 inputType: 'password',
						anchor : '99%'
					}
					,{
						xtype : 'textfield',
					   	fieldLabel : '确认新密码',
						maxLength : 64,
						allowBlank : false,
						msgTarget: 'side',
						name : 'okpasswd',
						inputType: 'password',
						anchor : '99%'
					},{
						 layout : "column",
						 border: false,
						 style: 'border-bottom:0px;',
        				 bodyStyle: 'padding:10px;background-color:transparent;',
						 columns: 2,
						 items:[
						    {
							　　xtype : "checkbox",
							　　columnWidth : .5,
							　　boxLabel : "修改登陆密码",
							　　name : "isCheckPas",
							   checked : true
						　　}, {
							　　xtype : "checkbox",
							　　columnWidth : .5,
							　　boxLabel : "修改提款密码",
							　　name : "isCheckAtmPas"
							
						　　}
						 ]
					
					}]
		});

/**修改密码窗口 */
memberInfo.modifyPasWindow = new Ext.Window({
			layout : 'fit',
			width : 300,
			height : 170,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [memberInfo.modifyPasFormPanel],
			buttons : [{
						text : '确定',
						handler : function() {
							var form = memberInfo.modifyPasFormPanel.getForm();
							if(form.isValid()){
								memberInfo.modifyPasFun();
							}
						}
					}, {
						text : '重置',
						handler : function() {
							var form = memberInfo.modifyPasFormPanel.getForm();
							var id = form.findField("uiid").getValue();
							form.reset();
							if (id != '')
								form.findField("uiid").setValue(id);
						}
					}]
		});
		
		
/** 发送短信form */
memberInfo.sendSmsFormPanel = new Ext.form.FormPanel({
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
				maxLength : 65,
				width:400,
				height:150,
				allowBlank : false,
				name : 'smsInfo',
				anchor : '99%'
			}]
});

/**修改密码窗口 */
memberInfo.sendWindow = new Ext.Window({
	layout : 'fit',
	width : 500,
	height : 250,
	closeAction : 'hide',
	plain : true,
	modal : true,
	resizable : true,
	items : [memberInfo.sendSmsFormPanel],
	buttons : [{
				text : '确定',
				handler : function() {
					var form = memberInfo.sendSmsFormPanel.getForm();
					if(form.isValid()){
						memberInfo.sendSmsFun();
					}
				}
			}, {
				text : '重置',
				handler : function() {
					var form = memberInfo.sendSmsFormPanel.getForm();
					var id = form.findField("uiid").getValue();
					form.reset();
					if (id != '')
						form.findField("uiid").setValue(id);
				}
			}]
});
		
			
/** 编辑新建窗口 */
memberInfo.addWindow = new Ext.Window({
			layout : 'fit',
			width : 550,
			height : 500,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [memberInfo.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							memberInfo.saveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = memberInfo.formPanel.getForm();
							var id = form.findField("spiid").getValue();
							form.reset();
							if (id != '')
								form.findField("spiid").setValue(id);
						}
					}]
		});
		
		
		memberInfo.transferOutStore = new Ext.data.SimpleStore({
					fields : ['v', 't'],  
					data : [['AA', '我的钱包'], ['BBIN', 'BBIN平台'], ['MG', 'MG平台'], ['AG', 'AG极速厅'],['AGIN', 'AGIN国际厅'],['PT','PT电子游戏'],['SA','SA电子游戏']]
		});
		
		memberInfo.transferOutCombo = new Ext.form.ComboBox({
			fieldLabel : '转出平台',
			hiddenName : 'fromPlaform',
			name : 'fromPlaform',
			triggerAction : 'all',
			mode : 'local',
		    store : memberInfo.transferOutStore, 
			valueField : 'v',
			displayField : 't',
			allowBlank : false,
			editable : false,
			value : "AA",
			anchor : '99%',
			listeners : {
				select : function(comboBox) {
					var record = memberInfo.grid.getSelectionModel().getSelected();
					memberInfo.queryWalletMoneyFun(record.data.uiid,comboBox.getValue());
				}
			}
		});
		
		/**
		 * 转入
		 */
		memberInfo.transferInCombo = new Ext.form.ComboBox({
				fieldLabel : '转入平台',
				hiddenName : 'toPlaform',
				name : 'toPlaform',
				triggerAction : 'all',
				mode : 'local',
				store : memberInfo.transferOutStore, 
				valueField : 'v',
				displayField : 't',
				allowBlank : false,
				editable : false,
				value : "BBIN",
				anchor : '99%'
		
		});
		memberInfo.transFormNumberField = new Ext.form.NumberField({
			name : 'amount',
			allowDecimals: false,     // 是否与许小数 
			allowNegative: false,    // 是否允许负数 
			allowBlank : false,
			fieldLabel : '转出金额',
			regex: /^\d+$/,
            regexText: '请输入正确的数据类型',
            blankText: '请填写转账金额',
			anchor : '99%'
		});
/**会员转账 */
memberInfo.transFormPanel = new Ext.form.FormPanel({
			autoScroll : false,
			border: false,
        	style: 'border-bottom:0px;',
        	bodyStyle: 'padding:10px;background-color:transparent;',
			labelwidth : 50,
			items : [{
						xtype : 'hidden',
						fieldLabel : 'ID',
						name : 'uiid',
						anchor : '99%'
					},memberInfo.transferOutCombo,memberInfo.transferInCombo,
					memberInfo.transFormNumberField,{
						xtype : 'textfield',
					   	fieldLabel : '备注',
						maxLength : 64,
						allowBlank : false,
						name : 'remark',
						anchor : '99%'
					}
					
				]
		});	
/** 会员升级窗口 */
memberInfo.transWindow = new Ext.Window({
			layout : 'fit',
			width : 400,
			height : 200,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [memberInfo.transFormPanel],
			buttons : [{
						text : '确定',
						handler : function() {
							memberInfo.transFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = memberInfo.transFormPanel.getForm();
							var id = form.findField("uiid").getValue();
							form.reset();
							if (id != '')
								form.findField("uiid").setValue(id);
						}
					}]
		});		
		
/** 会员升级form */
memberInfo.upgradeFormPanel = new Ext.form.FormPanel({
			labelwidth : 50,
			autoScroll : false,
			border: false,
        	style: 'border-bottom:0px;',
        	bodyStyle: 'padding:10px;background-color:transparent;',
			items : [{
						xtype : 'hidden',
						fieldLabel : 'ID',
						name : 'uiid',
						anchor : '99%'
					},
					new Ext.form.ComboBox({
								fieldLabel : '会员原来等级',
								hiddenName : 'oldgrade',
								name : 'oldgrade',
								triggerAction : 'all',
								mode : 'local',
								store : new Ext.data.ArrayStore({
											fields : ['v', 't'],
											data : Share.map2Ary(memberInfo.memGrade)
										}),
								valueField : 'v',
								displayField : 't',
								allowBlank : false,
								editable : false,
								anchor : '99%'
								
			
					}),
					new Ext.form.ComboBox({
								fieldLabel : '会员现在等级',
								hiddenName : 'newgrade',
								name : 'newgrade',
								triggerAction : 'all',
								mode : 'local',
								store : new Ext.data.ArrayStore({
											fields : ['v', 't'],
											data : Share.map2Ary(memberInfo.memGrade)
										}),
								valueField : 'v',
								displayField : 't',
								allowBlank : false,
								editable : false,
								anchor : '99%'
								
			
					}),{
						xtype : 'textfield',
					   	fieldLabel : '升级理由',
						maxLength : 64,
						allowBlank : true,
						name : 'reason',
						anchor : '99%'
					}
					
				]
		});
/** 会员升级窗口 */
memberInfo.upgradeWindow = new Ext.Window({
			layout : 'fit',
			width : 400,
			height : 200,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [memberInfo.upgradeFormPanel],
			buttons : [{
						text : '确定',
						handler : function() {
							memberInfo.upgradeFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = memberInfo.upgradeFormPanel.getForm();
							var id = form.findField("uiid").getValue();
							form.reset();
							if (id != '')
								form.findField("uiid").setValue(id);
						}
					}]
		});		
memberInfo.alwaysFun = function() {
	Share.resetGrid(memberInfo.grid);
	memberInfo.deleteAction.disable();
	memberInfo.editAction.disable();
	memberInfo.modifyPasAction.disable();
	memberInfo.tiAction.disable();
	memberInfo.enabledAction.disable();
	memberInfo.disabledAction.disable();
	memberInfo.upgradeAction.disable();
	memberInfo.transAction.disable();
	memberInfo.sendSmsAction.disable();
	memberInfo.callAction.disable();
	memberInfo.authEmailAction.disable();
	memberInfo.authPhoneAction.disable();
	memberInfo.openWeekRakeAction.disable();
	memberInfo.closeWeekRakeAction.disable();
};
memberInfo.saveFun = function() {
	var form = memberInfo.formPanel.getForm();
	if(memberInfo.isEdit == false){
		//判断两次登陆密码
		if(form.findField("passwd").getValue()!=form.findField("alignPasswd").getValue()){
		    Ext.MessageBox.alert('提示', '两次登陆密码输入不一致!');
			return;
		}
		//判断两次提取密码
		if(form.findField("atmpasswd").getValue()!=form.findField("alignAtmpasswd").getValue()){
		    Ext.MessageBox.alert('提示', '两次提取密码输入不一致!');
			return;
		}
		if (!form.isValid()) {
			return;
		}
	}else{
		if(!form.findField("email").isValid()){
			return;
		}
		if(!form.findField("identitycard").isValid()){
			return;
		}
	}
	// 发送请求
	Share.AjaxRequest({
				url : memberInfo.save,
				params : form.getValues(),
				callback : function(json) {
				if (json.success==false){
				     Ext.MessageBox.alert('Status', json.msg, showResult);
					return;
				}else{
				    memberInfo.addWindow.hide();
					memberInfo.alwaysFun();
					memberInfo.store.reload();
				}
					
				}
			});
};

/** 发送短信 */
memberInfo.sendSmsFun = function(){
	var form = memberInfo.sendSmsFormPanel.getForm();
	// 发送请求
	Share.AjaxRequest({
		url : memberInfo.sendSmsUrl+form.findField("uiid").getValue()+".do",
		params : form.getValues(),
		callback : function(json) {
			if (json.success){
			    memberInfo.sendWindow.hide();
				return;
			}else{
				Ext.MessageBox.alert('Status', json.msg);
			  	return;
			}
		}
	});
}

// 拨打电话
memberInfo.callFun =function(){
	var record = memberInfo.grid.getSelectionModel().getSelected();
	Share.AjaxRequest({
		url : memberInfo.callUrl +"8003/"+ record.data.uiid +".do",
		callback : function(json) {
			if(!json.success){
				 Ext.MessageBox.alert(json.msg);
			}
		}
	});
};

memberInfo.delFun = function() {
	var record = memberInfo.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '你确定要删除这个会员账号吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : memberInfo.del + record.data.uiid +".do",
								callback : function(json) {
									memberInfo.alwaysFun();
									memberInfo.store.reload();
								}
							});
				}
			});
};
memberInfo.modifyPasFun=function(){
   var form=memberInfo.modifyPasFormPanel.getForm();
   
     if(form.findField("okpasswd").getValue()!=form.findField("passwd").getValue()){
       Ext.MessageBox.alert('提示', '两次登陆密码输入不一致!');
       return;
     }
     if(form.findField("isCheckPas").getValue() == false && form.findField("isCheckAtmPas").getValue() == false){
     	Ext.MessageBox.alert('提示', '请选择要修改的密码类型，登录密码或者提款密码至少一个。');
       return;
     }
   
   Share.AjaxRequest({
		url : memberInfo.modifyPwd,
		params : form.getValues(),
		callback : function(json) {
					if (json.success==false){
				       Ext.MessageBox.alert('Status', json.msg, showResult);
					   return;
					}else{
					    memberInfo.modifyPasWindow.hide();
						memberInfo.alwaysFun();
						
					}
				}
		});
   
   
};

// 查询第三方游戏余额
memberInfo.queryWalletMoneyFun = function(uiid,gamePlat){
 	Share.AjaxRequest({
		url : memberInfo.queryWalletMoney+uiid+"/"+gamePlat+".do",
		showMsg: false,
		callback : function(json) {
				if (json.success == false){
				   Ext.MessageBox.alert('提示', json.msg);
				   return;
				}else{
					var form =memberInfo.transFormPanel.getForm();
					form.findField("amount").setValue(json.msg);
				}
			}
		});
};
   //踢出在线会员的方法
  memberInfo.tiFun=function(){
     var record = memberInfo.grid.getSelectionModel().getSelected();
     if(record){
     	Ext.Msg.confirm('提示', '你确定要踢出这个会员吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : memberInfo.ti + "PT/"+record.data.uiid +".do",
								callback : function(json) {
									if (json.success==false){
				       						Ext.MessageBox.alert('Status', json.msg, showResult);
					  					 //	return;
									}else{
										    Ext.MessageBox.alert('Status', json.msg, showResult);
					  					 //	return;	
					  					 	
									}
								}
							});
				}
			});
     }
  
  }
  memberInfo.authPhoneFun =function(){
  	 var record = memberInfo.grid.getSelectionModel().getSelected();
     if(record){
     	Ext.Msg.confirm('提示', '你确定要认证通过选中会员的手机吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
							url : memberInfo.authInfo +"1/"+ record.data.uiid +".do",
							callback : function(json) {
								if (json.success==false){
			       					Ext.MessageBox.alert('Status', json.msg, showResult);
								}else{
			       					memberInfo.store.reload();
									Ext.MessageBox.alert('Status', json.msg, showResult);
								}
							}
						});
				}
			});
     }
  }
  
  memberInfo.authEmailFun =function(){
  	 var record = memberInfo.grid.getSelectionModel().getSelected();
     if(record){
     	Ext.Msg.confirm('提示', '你确定要认证通过选中会员的邮箱吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
							url : memberInfo.authInfo +"2/"+ record.data.uiid +".do",
							callback : function(json) {
								if (json.success==false){
			       					Ext.MessageBox.alert('Status', json.msg, showResult);
								}else{
									memberInfo.store.reload();
									Ext.MessageBox.alert('Status', json.msg, showResult);
									//memberInfo.store.reload();
								}
							}
						});
				}
			});
     }
  }
  
  /**
   * 搜索参数
   */
   memberInfo.selectParams = function(){
   		var obj={};
   		/**
	  	if($("#memberInfo_beginDate").val()!="" && $("#memberInfo_endDate").val()==""){
	  		 Ext.MessageBox.alert('提示', '注册日期开始日期与结束日期必须填完整!');
	  		return;
	  	}
	  	if($("#memberInfo_endDate").val()!="" && $("#memberInfo_beginDate").val()==""){
	  		 Ext.MessageBox.alert('提示', '注册日期开始日期与结束日期必须填完整!');
	  		return;
	  	}
	  	*/
	  	if($("#memberInfo_account").val()!=""){
	  		obj.account=$("#memberInfo_account").val();
	  	}
	  	if($("#memberInfo_uname").val()!=""){
	  		obj.uname=$("#memberInfo_uname").val();
	  	}
	  	var grade=$("#memberInfo_grade").prev();
	  	if($(grade).val()!=""){
	  		obj.grade=$(grade).val();
	  	}
	  	if($("#memberInfo_phone").val()!=""){
	  		obj.phone=$("#memberInfo_phone").val();
	  	}
	  	if($("#memberInfo_email").val()!=""){
	  		obj.email=$("#memberInfo_email").val();
	  	}
	  	if($("#memberInfo_qq").val()!=""){
	  		obj.qq=$("#memberInfo_qq").val();
	  	}
	  	if($("#memberInfo_regip").val()!=""){
	  		obj.regip=$("#memberInfo_regip").val();
	  	}
	  	if($("#memberInfo_url").val()!=""){
	  		obj.url=$("#memberInfo_url").val();
	  	}
	  	if($("#memberInfo_puname").val()!=""){
	  		obj.puname=$("#memberInfo_puname").val();
	  	}
	  	if($("#memberInfo_beginDate").val()!=""){
	  		obj.beginDate=$("#memberInfo_beginDate").val();
	  	}
	  	if($("#memberInfo_endDate").val()!=""){
	  		obj.endDate=$("#memberInfo_endDate").val();
	  	}	 
	  	var paymoney=$("#memberInfo_paymoney").prev();
	  	if($(paymoney).val()!=""){
	  		obj.paymoney=$(paymoney).val();
	  	}
	  	var relaccount=$("#memberInfo_relaccount").prev();
	  	if($(relaccount).val()!=""){
	  		obj.relaflag=$(relaccount).val();
	  	}
	  	
	  	var weekrake=$("#memberInfo_weekrake").prev();
	  	if($(weekrake).val()!=""){
	  		obj.weekrake=$(weekrake).val();
	  	}
	  	
	  	var accountType=$("#account_type").prev();
	  	if($(accountType).val()!=""){
	  		obj.accounttype=$(accountType).val();
	  	}
	  	return obj;
   }
  /**
   * 查询事件
   */
  memberInfo.selectFun=function(){
  	
  	
  	memberInfo.store.load({params: memberInfo.selectParams()});
  	memberInfo.alwaysFun();
  }
  
  memberInfo.store.on('beforeload',function(){
  		memberInfo.store.baseParams = memberInfo.selectParams();
  	});
  
 memberInfo.upgradeFun=function(){
 	var record = memberInfo.grid.getSelectionModel().getSelected();
 	memberInfo.upgradeFormPanel.getForm().findField("oldgrade").setValue(record.data.grade);
 	var form=memberInfo.upgradeFormPanel.getForm();
 	var value=form.findField("newgrade").getValue();
 	if(value=="0" || value==""){
 	    Ext.MessageBox.alert('提示', '选择新的会员等级!');
 		return;
 	}
 	if (form.findField("newgrade").getValue()==form.findField("oldgrade").getValue()){
 	    Ext.MessageBox.alert('提示', '旧的会员等级不能与新的会员等级一样!');
 		return;
 	}
    Share.AjaxRequest({
		url : memberInfo.upgrade,
		params : form.getValues(),
		callback : function(json) {
					if (json.success==false){
				       Ext.MessageBox.alert('Status', json.msg, showResult);
					   return;
					}else{
					    memberInfo.upgradeWindow.hide();
						memberInfo.alwaysFun();
						memberInfo.store.reload();
						
					}
				}
		});
 
 } 
 
 memberInfo.openFun = function(){
		var record = memberInfo.grid.getSelectionModel().getSelected();
		if (record.data.withdrawlFlag == '1') {
			Ext.Msg.alert('提示', '此会员提款已经是打开状态');
			return;
		}
		Ext.Msg.confirm('提示', '确定要打开此会员的提款吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : memberInfo.openWeekRake + record.data.uiid + ".do",
					callback : function(json) {
						memberInfo.alwaysFun();
						memberInfo.store.reload();
					}
				});
			}
		});
	};
	
	memberInfo.closeFun = function(){
		var record = memberInfo.grid.getSelectionModel().getSelected();
		if (record.data.withdrawlFlag =='0') {
			Ext.Msg.alert('提示', '此会员提款已经是关闭状态');
			return;
		}
		Ext.Msg.confirm('提示', '确定要关闭此会员提款吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : memberInfo.closeWeekRake + record.data.uiid + ".do",
					callback : function(json) {
						memberInfo.alwaysFun();
						memberInfo.store.reload();
					}
				});
			}
		});
	};
 
 memberInfo.transFun=function(){
    var record = memberInfo.grid.getSelectionModel().getSelected();
 	var form=memberInfo.transFormPanel.getForm();
 	var  transOut = form.findField("fromPlaform").getValue();
 	var  transIn = form.findField("toPlaform").getValue();
 	var amount = form.findField("amount").getValue();
 	if(transOut=='' || transOut.length <=0){
 	    Ext.MessageBox.alert('提示', '转出平台还没有选择!');
 		return;
 	}
 	if(transIn=='' || transIn.length <= 0){
 	    Ext.MessageBox.alert('提示', '转入平台还没有选择!');
 		return;
 	}
 	if (transIn==transOut){
 	    Ext.MessageBox.alert('提示', '转出平台不能转入平台一样!');
 		return;
 	}
 	if(transOut == 'BBIN' || transOut == 'MG' || transOut=='AG' || transOut=='AGIN' || transOut=='PT'){
 		if(transIn != 'AA'){
 			Ext.MessageBox.alert('提示', '您选择的转出平台为第三方游戏，转入平台只能选择【我的钱包】。');
 			return;
 		}
 	}
 	if(parseInt(amount) <=0){
 		Ext.MessageBox.alert('提示', '转出金额必须大于0。');
 		return;
 	}
 	if(!form.isValid()){
 		return;
 	}
    Share.AjaxRequest({
		url : memberInfo.trans,
		params : form.getValues(),
		callback : function(json) {
					if (json.success==false){
				       Ext.MessageBox.alert('Status', json.msg, showResult);
					   return;
					}else{
					    memberInfo.transWindow.hide();
						memberInfo.alwaysFun();
						//memberInfo.store.reload();
						
					}
				}
		});
 }
Ext.onReady(queryBirthDayFun);
function queryBirthDayFun(){
	Share.AjaxRequest({
		url : memberInfo.birthdayCount,
		showMsg: false,
		callback : function(json) {
				if (json.success == true){
				   if(json.msg>0){
				   		Ext.MessageBox.alert('温馨提示', "您好,今天有 <span style='color:red;'>"+json.msg+"</span> 位会员过生日!");	
				   }
				}
			}
		});
} 
 
memberInfo.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [memberInfo.grid]
		});
</script>