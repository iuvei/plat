<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	Ext.ns("Ext.market.member"); // 自定义一个命名空间
	member = Ext.market.member; // 定义命名空间的别名
	member = {
		all : '/manage/member/queryMember.do', // 所有会员
		save : "/manage/member/save.do",// 保存会员
		reset : "/manage/member/resetPwd/",// 重置会员密码
		del : "/manage/member/del/",// 删除会员
		enable : "/manage/member/enable/",// 禁用会员
		disable : "/manage/member/disable/",// 启用会员
		pageSize : 20,// 每页显示的记录数
		STATUSMAP : eval('(${statusMap})')//注意括号
	};
	/** 改变页的combo*/
	member.pageSizeCombo = new Share.pageSizeCombo({
		value : '20',
		listeners : {
			select : function(comboBox) {
				member.pageSize  = parseInt(comboBox.getValue());
				member.bbar.pageSize  = parseInt(comboBox.getValue());
				member.store.baseParams.limit = member.pageSize;
				member.store.baseParams.start = 0;
				member.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	member.pageSize = parseInt(member.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	member.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:true,
		baseParams : {
			start : 0,
			limit : member.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : member.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'uiid', 'account', 'passwd', 'accounttype', 'uname', 'atmpasswd', 'identitycard', 'phone', 'email', 'qq', 'birthday', 'grade', 'puiid', 'status', 'create_date', 'update_date' ]),
		listeners : {
			'load' : function(store, records, options) {
				member.alwaysFun();
			}
		}
	});
	//member.store.load(); 
	/** 基本信息-选择模式 */
	member.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				member.deleteAction.enable();
				member.editAction.enable();
				member.resetPwdAction.enable();
				member.resetAtmPwdAction.enable();
				member.enableAction.enable();
				member.disableAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				member.deleteAction.disable();
				member.editAction.disable();
				member.resetPwdAction.disable();
				member.resetAtmPwdAction.disable();
				member.enableAction.disable();
				member.disableAction.disable();
			}
		}
	});
	/** 基本信息-数据列 */
	member.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 110
		},
		//'uiid', 'account', 'passwd', 'accounttype', 'uname', 'atmpasswd', 'identitycard', 'phone', 'email', 'QQ', 'birthday', 'grade', 'puiid', 'status', 'create_date', 'update_date'
		columns : [ member.selModel, {
			hidden : true,
			header : '会员ID',
			dataIndex : 'uiid'
		}, {
			header : '账号',
			dataIndex : 'account',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
//		}, {
//			header : '密码',
//			dataIndex : 'passwd',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
//		}, {
//			header : '账号类型', //0 普通用户 1代理用户
//			dataIndex : 'accounttype',
//			renderer : function(v) {
//				return Share.map(v, member.ACCOUNTTYPE, '');
//			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '真实姓名',
			dataIndex : 'uname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
//		}, {
//			header : '提款密码',
//			dataIndex : 'atmpasswd',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '身份证',
			dataIndex : 'identitycard',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '电话',
			dataIndex : 'phone',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '电子邮件',
			dataIndex : 'email',
			renderer : function(value) {
				return '<span title="点击给 ' + value + ' 发邮件"><a href="mailto:' + value + '">' + value + '</a></span>';
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
			header : '生日',
			dataIndex : 'birthday',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '会员等级',
			dataIndex : 'grade',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
//		}, {
//			header : '父用户ID',
//			dataIndex : 'puiid',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '状态',
			dataIndex : 'status',
			renderer : function(v) {
				return Share.map(v, member.STATUSMAP, '');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
//		}, {
//			header : '创建时间',
//			dataIndex : 'create_date',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
//		}, {
//			header : '更新时间',
//			dataIndex : 'update_date',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		} ]
	});
	/** 会员登录密码 */
	member.password = new Ext.form.TextField({
		fieldLabel : '登录密码',
		inputType : 'password',
		minLength : 6,
		maxLength : 12,
		allowBlank : false,
		name : 'passwd',
		confirmTo : 'passwdConfirm',
		anchor : '99%'
	});
	/** 会员登录密码确认 */
	member.passwordConfirm = new Ext.form.TextField({
		fieldLabel : '登录密码确认',
		inputType : 'password',
		minLength : 6,
		maxLength : 12,
		allowBlank : false,
		name : 'passwdConfirm',
		confirmTo : 'passwd',
		anchor : '99%'
	});
	/** 会员提款密码 */
	member.atmpassword = new Ext.form.TextField({
		fieldLabel : '提款密码',
		inputType : 'password',
		maxLength : 32,
		allowBlank : false,
		name : 'atmpasswd',
		confirmTo : 'atmpasswdConfirm',
		anchor : '99%'
	});
	/** 会员提款密码确认 */
	member.atmpasswordConfirm = new Ext.form.TextField({
		fieldLabel : '提款密码确认',
		inputType : 'password',
		maxLength : 32,
		allowBlank : false,
		name : 'atmpasswdConfirm',
		confirmTo : 'atmpasswd',
		anchor : '99%'
	});
	/** 新建 */
	member.addAction = new Ext.Action({
		text : '新建',
		//text : '<fmt:message key="common.cancel"/>',
		iconCls : 'member_add',
		handler : function() {
			member.addWindow.setIconClass('member_add'); // 设置窗口的样式
			member.addWindow.setTitle('新建会员'); // 设置窗口的名称
			member.addWindow.show().center(); // 显示窗口
			member.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			member.password.setVisible(true);
			member.password.setDisabled(false);
			member.tabPanel.activate(member.formPanel);
		}
	});
	/** 编辑 */
	member.editAction = new Ext.Action({
		text : '编辑',
		iconCls : 'member_edit',
		disabled : true,
		handler : function() {
			var record = member.grid.getSelectionModel().getSelected();
//			member.memberAgents.clear();
//			// 发送请求
//			// 查找当前会员的代理
//			Share.AjaxRequest({
//				url : member.myRole + record.data.uiid +".do",
//				callback : function(json) {
//					//console.log(json);
//					Ext.each(json, function(r,index) {
//						// 当前会员的代理
//						//console.log(r.roleId+","+r.roleName)
//						member.memberAgents.add(r.roleId, r.roleName);
//					});
//				}
//			});
			member.addWindow.setIconClass('member_edit'); // 设置窗口的样式
			member.addWindow.setTitle('编辑会员'); // 设置窗口的名称
			member.addWindow.show().center();
			member.formPanel.getForm().reset();
			member.formPanel.getForm().loadRecord(record);
			member.password.setVisible(false);
			//member.password.setDisabled(true);
			member.passwordConfirm.setVisible(false);
			member.passwordConfirm.setDisabled(true);
			member.atmpassword.setVisible(false);
			//member.atmpassword.setDisabled(true);
			member.atmpasswordConfirm.setVisible(false);
			member.atmpasswordConfirm.setDisabled(true);
			member.tabPanel.activate(member.formPanel);
		}
	});
	/** 删除 */
	member.deleteAction = new Ext.Action({
		text : '删除',
		iconCls : 'member_delete',
		disabled : true,
		handler : function() {
			member.delFun();
		}
	});
	/** 重置登录密码 */
	member.resetPwdAction = new Ext.Action({
		text : '重置登录密码',
		iconCls : 'reset_pwd',
		disabled : true,
		handler : function() {
			member.resetPwdFun(1);
		}
	});
	/** 重置提款密码 */
	member.resetAtmPwdAction = new Ext.Action({
		text : '重置提款密码',
		iconCls : 'reset_pwd',
		disabled : true,
		handler : function() {
			member.resetPwdFun(2);
		}
	});
	/** 启用 */
	member.enableAction = new Ext.Action({
		text : '启用',
		iconCls : 'enable',
		disabled : true,
		handler : function() {
			member.enableFun();
		}
	});
	/** 禁用 */
	member.disableAction = new Ext.Action({
		text : '禁用',
		iconCls : 'disable',
		disabled : true,
		handler : function() {
			member.disableFun();
		}
	});
	/** 查询 */
	member.searchField = new Ext.ux.form.SearchField({
		store : member.store,
		paramName : 'account',
		emptyText : '请输入会员账号',
		style : 'margin-left: 5px;'
	});
	/** 顶部工具栏 */
	member.tbar = [ member.addAction, '-', member.editAction, '-', member.deleteAction, '-',member.searchField,
	                '-', member.resetPwdAction, '-', member.resetAtmPwdAction, 
	                '-', member.enableAction, '-', member.disableAction
	                ];
	/** 底部工具条 */
	member.bbar = new Ext.PagingToolbar({
		pageSize : member.pageSize,
		store : member.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', member.pageSizeCombo ]
	});
	/** 基本信息-表格 */
	member.grid = new Ext.grid.EditorGridPanel({
		store : member.store,
		colModel : member.colModel,
		selModel : member.selModel,
		tbar : member.tbar,
		bbar : member.bbar,
		autoScroll : 'auto',
		region : 'center',
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig:{},
		stripeRows : true
	});
	member.statusCombo = new Ext.form.ComboBox({
				fieldLabel : '状态',
				hiddenName : 'status',
				name : 'status',
				triggerAction : 'all',
				mode : 'local',
				store : new Ext.data.ArrayStore({
							fields : ['v', 't'],
							data : Share.map2Ary(member.STATUSMAP)
						}),
				valueField : 'v',
				displayField : 't',
				allowBlank : false,
				editable : false,
				value : "1",
				anchor : '99%'
			});
			
//	member.sexCombo = new Ext.form.ComboBox({
//		fieldLabel : '性别',
//		hiddenName : 'sex',
//		name : 'sex',
//		triggerAction : 'all',
//		mode : 'local',
//		store : new Ext.data.ArrayStore({
//					fields : ['v', 't'],
//					data : Share.map2Ary(member.SEX)
//				}),
//		valueField : 'v',
//		displayField : 't',
//		allowBlank : false,
//		editable : false,
//		anchor : '99%'
//	});
	/** 基本信息-详细信息的form */
	member.formPanel = new Ext.form.FormPanel({
		autoScroll : true,
		frame : false,
		title : '会员信息',
		bodyStyle : 'padding:10px;border:0px',
		labelwidth : 70,
		defaultType : 'textfield',
		items : [ {
			xtype : 'hidden',
			fieldLabel : 'ID',
			name : 'uiid'
		}, {
			xtype : 'hidden',
			fieldLabel : '账号类型',
			name : 'accounttype'
		}, {
			xtype : 'hidden',
			fieldLabel : '会员等级',
			name : 'grade'
		}, {
			xtype : 'hidden',
			fieldLabel : '父用户ID',
			name : 'puiid'
//		}, {
//			xtype : 'hidden',
//			fieldLabel : '创建时间',
//			name : 'create_date'
		}, {
			fieldLabel : '账号',
			maxLength : 64,
			allowBlank : false,
			name : 'account',
			anchor : '99%'
		}, 
		member.password,// member.passwordConfirm, 
		member.atmpassword,// member.atmpasswordConfirm, 
		{
			fieldLabel : '真实姓名',
			maxLength : 64,
			allowBlank : false,
			name : 'uname',
			anchor : '99%'
		}, {
			fieldLabel : '身份证',
			maxLength : 32,
			allowBlank : false,
			name : 'identitycard',
			anchor : '99%'
		}, {
			fieldLabel : '电话',
			maxLength : 32,
			allowBlank : false,
			name : 'phone',
			anchor : '99%'
		}, {
			fieldLabel : '电子邮件',
			maxLength : 64,
			allowBlank : false,
			regex : /^[a-zA-Z0-9_\.\-]+\@([a-zA-Z0-9\-]+\.)+[a-zA-Z0-9]{2,4}$/,
			regexText : '请输入有效的邮箱地址',
			name : 'email',
			anchor : '99%'
		}, {
			fieldLabel : 'QQ',
			maxLength : 32,
			allowBlank : false,
			name : 'qq',
			anchor : '99%'
		}, {
			fieldLabel : '生日',
			maxLength : 32,
			allowBlank : false,
			name : 'birthday',
			anchor : '99%'
		}, member.statusCombo
		]
	});
	/** 编辑新建窗口 */
	member.addWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 460,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ member.formPanel ],
		buttons : [ {
			text : '保存',
			handler : function() {
				member.saveFun();
			}
		}, {
			text : '重置',
			handler : function() {
				var form = member.formPanel.getForm();
				var uiid = form.findField("uiid").getValue();
				var account = form.findField("account").getValue();
				form.reset();
				if (uiid != '')
					form.findField("uiid").setValue(uiid);
				if (account != '')
					form.findField("account").setValue(account);
			}
		} ]
	});
	member.alwaysFun = function() {
		Share.resetGrid(member.grid);
		member.deleteAction.disable();
		member.resetPwdAction.disable();
		member.resetAtmPwdAction.disable();
		member.editAction.disable();
	};
	member.saveFun = function() {
		var form = member.formPanel.getForm();
		if (!form.isValid()) {
			return;
		}
		// 发送请求
		Share.AjaxRequest({
			url : member.save,
			params : form.getValues(),
			callback : function(json) {
				member.addWindow.hide();
				member.alwaysFun();
				member.store.reload();
				
				//fix bug 打开页面，编辑，不点击角色的tab。直接点击保存，再点击新建，在保存，会直接提交。
				//	member.tabPanel.activate(member.roleGrid);
				//Share.resetGrid(member.roleGrid);
			}
		});
	};
	member.resetPwdFun = function(type) {
		var record = member.grid.getSelectionModel().getSelected();
		var typeStr = "登录密码";
		if(1==type){
			typeStr = "登录密码";
		}else if(2==type){
			typeStr = "提款密码";
		}else{
			Ext.Msg.alert('非法操作');
			return;
		}
		Ext.Msg.confirm('提示', '确定要重置此会员的'+typeStr+'吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : member.reset + "/" + type + "/" + record.data.uiid + ".do",
					callback : function(json) {
						member.alwaysFun();
						member.store.reload();
					}
				});
			}
		});
	};
	member.delFun = function() {
		var record = member.grid.getSelectionModel().getSelected();
		Ext.Msg.confirm('提示', '确定要删除这条记录吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : member.del + record.data.uiid + ".do",
					callback : function(json) {
						member.alwaysFun();
						member.store.reload();
					}
				});
			}
		});
	};

	member.enableFun = function(){
		var record = member.grid.getSelectionModel().getSelected();
		if (record.data.status==1) {
			Ext.Msg.alert('提示', '此会员已经是启用状态');
			return;
		}
		Ext.Msg.confirm('提示', '确定要启用此会员吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : member.enable + record.data.uiid + ".do",
					callback : function(json) {
						member.alwaysFun();
						member.store.reload();
					}
				});
			}
		});
	};
	member.disableFun = function(){
		var record = member.grid.getSelectionModel().getSelected();
		if (record.data.status==0) {
			Ext.Msg.alert('提示', '此会员已经是禁用状态');
			return;
		}
		Ext.Msg.confirm('提示', '确定要禁用此会员吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : member.disable + record.data.uiid + ".do",
					callback : function(json) {
						member.alwaysFun();
						member.store.reload();
					}
				});
			}
		});
	};
	member.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'border',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
		items : [ member.grid ]
	});
</script>
