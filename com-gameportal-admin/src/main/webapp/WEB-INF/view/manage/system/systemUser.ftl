<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	Ext.ns("Ext.market.user"); // 自定义一个命名空间
	user = Ext.market.user; // 定义命名空间的别名
	user = {
		all : '/manage/system/user/querySystemUser.do', // 所有用户
		save : "/manage/system/user/save.do",// 保存用户
		reset : "/manage/system/user/resetPwd.do",// 重置用户密码
		del : "/manage/system/user/del/",// 删除用户
		myRole : "/manage/system/role/",// 加载某个用户的所有角色
		loadRole : '/manage/system/role/select.do',// 加载所有角色
		pageSize : 20,// 每页显示的记录数
		/** 当前正在编辑用户的角色ID数组 */
		userRoleIds : new Ext.util.MixedCollection(),
		hasActive : true,
		sysuserStatus : eval('(${fields.sysuserStatus})'),
		sysuserBindStatus : eval('(${fields.sysuserBindStatus})'),
		SEX: eval('(${fields.sex})')//注意括号
	};
	/** 改变页的combo*/
	user.pageSizeCombo = new Share.pageSizeCombo({
		value : '20',
		listeners : {
			select : function(comboBox) {
				user.pageSize  = parseInt(comboBox.getValue());
				user.bbar.pageSize  = parseInt(comboBox.getValue());
				user.store.baseParams.limit = user.pageSize;
				user.store.baseParams.start = 0;
				user.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	user.pageSize = parseInt(user.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	user.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:true,
		baseParams : {
			start : 0,
			limit : user.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : user.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'userId', 'account', 'truename','realName', 'sex', 'email', 'mobile', 'officePhone', 'lastLoginTime', 'lastLoginIp', 'remark','status','clientmac','clientip','bindstatus','errorCount']),
		listeners : {
			'load' : function(store, records, options) {
				user.alwaysFun();
			}
		}
	});
	//user.store.load(); 
	/** 基本信息-选择模式 */
	user.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				user.deleteAction.enable();
				user.editAction.enable();
				user.resetPwdAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				user.deleteAction.disable();
				user.editAction.disable();
				user.resetPwdAction.disable();
			}
		}
	});
	/** 基本信息-数据列 */
	user.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 110
		},
		columns : [ user.selModel, {
			hidden : true,
			header : '用户ID',
			dataIndex : 'userId'
		}, {
			header : '账号',
			dataIndex : 'account',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '真实姓名',
			dataIndex : 'truename',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '用户角色',
			dataIndex : 'realName',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '性别',
			dataIndex : 'sex',
			renderer : function(v) {
				return Share.map(v, user.SEX, '其他');
			},
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
			header : '手机',
			dataIndex : 'mobile',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '办公电话',
			dataIndex : 'officePhone',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '状态',
			dataIndex : 'status',
			renderer : function(v) {
				if(v == 1){
					return '<span style="color:green;">正常</span>';
				}else{
					return '<span style="color:red;">禁用</span>';
				}
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : 'MAC地址',
			dataIndex : 'clientmac',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : 'IP地址',
			dataIndex : 'clientip',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '绑定状态',
			dataIndex : 'bindstatus',
			renderer : function(v) {
				if(v == 1){
					return '<span style="color:green;">已绑定</span>';
				}else{
					return '<span style="color:red;">未绑定</span>';
				}
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '上次登录时间',
			dataIndex : 'lastLoginTime',
			width : 150,
			renderer : function(v) {
				return new Date(v).format('Y-m-d H:i:s');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '上次登录IP地址',
			dataIndex : 'lastLoginIp',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '备注',
			dataIndex : 'remark',
			renderer : function(value, metadata, record) {
				if(value){
				metadata.attr = 'ext:qwidth="200" ext:qtitle="备注" ext:qtip="' + value + '"';
				}
				return value;
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		} ]
	});
	/** 用户密码 */
	user.password = new Ext.form.TextField({
		fieldLabel : '密码',
		inputType : 'password',
		maxLength : 32,
		allowBlank : false,
		name : 'password',
		anchor : '99%'
	});
	/** 新建 */
	user.addAction = new Ext.Action({
		text : '新建',
		//text : '<fmt:message key="common.cancel"/>',
		iconCls : 'user_add',
		handler : function() {
			user.addWindow.setIconClass('user_add'); // 设置窗口的样式
			user.addWindow.setTitle('新建用户'); // 设置窗口的名称
			user.addWindow.show().center(); // 显示窗口
			user.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			user.userRoleIds.clear();//清空原来用户的角色
			user.password.setVisible(true);
			user.password.setDisabled(false);
			user.hasActive = true;
			user.tabPanel.activate(user.formPanel);
		}
	});
	/** 编辑 */
	user.editAction = new Ext.Action({
		text : '编辑',
		iconCls : 'user_edit',
		disabled : true,
		handler : function() {
			var record = user.grid.getSelectionModel().getSelected();
			user.userRoleIds.clear();
			// 发送请求
			// 查找当前用户的角色
			Share.AjaxRequest({
				url : user.myRole + record.data.userId +".do",
				callback : function(json) {
					//console.log(json);
					Ext.each(json, function(r,index) {
						// 当前用户的角色
						//console.log(r.roleId+","+r.roleName)
						user.userRoleIds.add(r.roleId, r.roleName);
					});
				}
			});
			user.addWindow.setIconClass('user_edit'); // 设置窗口的样式
			user.addWindow.setTitle('编辑用户'); // 设置窗口的名称
			user.addWindow.show().center();
			user.formPanel.getForm().reset();
			user.formPanel.getForm().loadRecord(record);
			user.password.setVisible(false);
			user.password.setDisabled(true);
			user.hasActive = true;
			user.tabPanel.activate(user.formPanel);
		}
	});
	/** 删除 */
	user.deleteAction = new Ext.Action({
		text : '删除',
		iconCls : 'user_delete',
		disabled : true,
		handler : function() {
			user.delFun();
		}
	});
	/** 重置密码 */
	user.resetPwdAction = new Ext.Action({
		text : '修改密码',
		iconCls : 'reset_pwd',
		disabled : true,
		handler : function() {
			var record = user.grid.getSelectionModel().getSelected();
			user.addWindowUppass.setIconClass('user_edit'); // 设置窗口的样式
			user.addWindowUppass.setTitle('修改密码'); // 设置窗口的名称
			user.addWindowUppass.show().center();
			user.formUppwdPanel.getForm().reset(); // 清空表单里面的元素的值.
			user.formUppwdPanel.getForm().findField("userId").setValue(record.data.userId);
			//user.resetPwdFun();
		}
	});
	/** 查询 */
	user.searchField = new Ext.ux.form.SearchField({
		store : user.store,
		paramName : 'realName',
		emptyText : '请输入用户角色',
		style : 'margin-left: 5px;'
	});
	/** 顶部工具栏 */
	user.tbar = [ user.addAction, '-', user.editAction, '-', user.deleteAction, '-', user.resetPwdAction, '-', user.searchField ];
	/** 底部工具条 */
	user.bbar = new Ext.PagingToolbar({
		pageSize : user.pageSize,
		store : user.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', user.pageSizeCombo ]
	});
	/** 基本信息-表格 */
	user.grid = new Ext.grid.EditorGridPanel({
		store : user.store,
		colModel : user.colModel,
		selModel : user.selModel,
		tbar : user.tbar,
		bbar : user.bbar,
		autoScroll : 'auto',
		region : 'center',
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig:{},
		stripeRows : true
	});
	user.sexCombo = new Ext.form.ComboBox({
		fieldLabel : '性别',
		hiddenName : 'sex',
		name : 'sex',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(user.SEX)
				}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		anchor : '99%'
	});
	user.status = new Ext.form.ComboBox({
		fieldLabel : '状态',
		hiddenName : 'status',
		name : 'status',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(user.sysuserStatus)
				}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		anchor : '99%'
	});
	
	user.bindStatus = new Ext.form.ComboBox({
		fieldLabel : '绑定状态',
		hiddenName : 'bindstatus',
		name : 'bindstatus',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(user.sysuserBindStatus)
				}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		anchor : '99%'
	});
	/*修改密码*/
	user.formUppwdPanel = new Ext.form.FormPanel({
		autoScroll : false,
		border: false,
		labelwidth : 70,
        style: 'border-bottom:0px;',
        bodyStyle: 'padding:10px;background-color:transparent;',
		defaultType : 'textfield',
		items :[{
			xtype : 'hidden',
			fieldLabel : 'ID',
			name : 'userId'
		},{
			fieldLabel : '新密码',
			maxLength : 64,
			name : 'newpassword',
			inputType: 'password',
			allowBlank : false,
			anchor : '99%'
		},{
			fieldLabel : '确认新密码',
			maxLength : 64,
			name : 'okpassword',
			allowBlank : false,
			inputType: 'password',
			anchor : '99%'
		}]
	});
	/** 基本信息-详细信息的form */
	user.formPanel = new Ext.form.FormPanel({
		autoScroll : true,
		frame : false,
		title : '用户信息',
		bodyStyle : 'padding:10px;border:0px',
		labelwidth : 70,
		defaultType : 'textfield',
		items : [ {
			xtype : 'hidden',
			fieldLabel : 'ID',
			name : 'userId'
		}, {
			fieldLabel : '账号',
			maxLength : 64,
			allowBlank : false,
			name : 'account',
			anchor : '99%'
		}, user.password, {
			fieldLabel : '用户姓名',
			maxLength : 64,
			allowBlank : false,
			name : 'truename',
			anchor : '99%'
		}, {
			fieldLabel : '角色名称',
			maxLength : 64,
			allowBlank : false,
			name : 'realName',
			anchor : '99%'
		}, user.sexCombo, {
			fieldLabel : '电子邮件',
			maxLength : 64,
			allowBlank : false,
			regex : /^[a-zA-Z0-9_\.\-]+\@([a-zA-Z0-9\-]+\.)+[a-zA-Z0-9]{2,4}$/,
			regexText : '请输入有效的邮箱地址',
			name : 'email',
			anchor : '99%'
		}, {
			fieldLabel : '手机',
			maxLength : 32,
			allowBlank : false,
			name : 'mobile',
			anchor : '99%'
		}, {
			fieldLabel : '办公电话',
			maxLength : 32,
			allowBlank : false,
			name : 'officePhone',
			anchor : '99%'
		},user.status, {
			fieldLabel : 'MAC地址',
			maxLength : 32,
			name : 'clientmac',
			anchor : '99%'
		}, {
			fieldLabel : 'IP地址',
			maxLength : 128,
			name : 'clientip',
			anchor : '99%'
		},user.bindStatus, {
			xtype : 'textarea',
			fieldLabel : '备注',
			maxLength : 128,
			height : 90,
			name : 'remark',
			anchor : '99%'
		} ]
	});
	/** 角色 */
	user.roleSelModel = new Ext.grid.CheckboxSelectionModel();
	user.roleStore = new Ext.data.JsonStore({
		root : 'data',
		totalProperty : 'totalProperty',
		autoLoad : true,
		proxy : new Ext.data.HttpProxy({
			method : 'POST',
			url : user.loadRole
		}),
		fields : [ 'roleId', 'roleName', 'roleDesc' ],
		listeners : {
			'load' : function(store, records, options) {
				// user.roleSelModel.clearSelections();
			}
		}
	});
	user.roleGrid = new Ext.grid.GridPanel({
		title : '用户角色',
		store : user.roleStore,
		sm : user.roleSelModel,
		autoScroll : 'auto',
		loadMask : true,
		deferRowRender : false,
		stripeRows : true,
		//autoExpandColumn :'roleDesc',
		bodyStyle : 'padding:0px;border:0px',
		columns : [ user.roleSelModel, {
			hidden : true,
			header : '角色ID',
			dataIndex : 'roleId'
		}, {
			header : "角色名称",
			width : 200,
			dataIndex : 'roleName'
		}, {
			header : "角色描述",
			width : 300,
			//id : 'roleDesc',
			dataIndex : 'roleDesc'
		} ],
		listeners : {
			activate : function() {
				if (user.hasActive) {
					var grid = user.roleGrid;
					var store = user.roleStore;// store
					Share.resetGrid(grid);
					grid.getSelectionModel();// 选择所有行
					var total = store.getCount();// 数据行数
					for ( var i = 0; i < total; i++) {
						var row = store.data.items[i];
						if (user.userRoleIds.containsKey(row.data.roleId)) {
							user.roleSelModel.selectRow(i, true);
						}
					}
					user.hasActive = false;
				}
			}
		}
	});
	
	user.tabPanel = new Ext.TabPanel({
		activeTab : 0,
		defaults : {
			autoHeight : true
		},
		items : [ user.formPanel, user.roleGrid ]
	});
	
	user.addWindowUppass = new Ext.Window({
		layout : 'fit',
		width : 300,
		height : 150,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ user.formUppwdPanel ],
		buttons : [ {text : '保存',
			handler : function() {
				var form = user.formUppwdPanel.getForm();
				if(form.isValid()){
					var newpassword = form.findField("newpassword").getValue();
					var okpassword = form.findField("okpassword").getValue();
					if(okpassword != newpassword){
						Ext.Msg.alert("提示", "新密码与确认密码不一致。");
						return;
					}
					user.resetPwdFun();
				}
			}
		},{
			text : '重置',
			handler : function() {
				user.formUppwdPanel.getForm().findField("newpassword").setValue('');
				user.formUppwdPanel.getForm().findField("okpassword").setValue('');
			}
		} ]
	});
	
	/** 编辑新建窗口 */
	user.addWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 550,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ user.tabPanel ],
		buttons : [ {
			text : '保存',
			handler : function() {
				user.saveFun();
			}
		}, {
			text : '重置',
			handler : function() {
				Share.resetGrid(user.roleGrid);
				var form = user.formPanel.getForm();
				var id = form.findField("userId").getValue();
				var account = form.findField("account").getValue();
				form.reset();
				if (id != '')
					form.findField("userId").setValue(id);
				if (account != '')
					form.findField("account").setValue(account);
			}
		} ]
	});
	user.alwaysFun = function() {
		Share.resetGrid(user.grid);
		user.deleteAction.disable();
		user.resetPwdAction.disable();
		user.editAction.disable();
	};
	user.saveFun = function() {
		// 保存角色
		var selections = user.roleGrid.getSelectionModel().getSelections();
		var ids = [];
		if (user.hasActive) {//如果没有点击角色tab
			ids = user.userRoleIds.keys;
		} else {//点击了角色tab
			for ( var i = 0; i < selections.length; i++) {
				ids.push(selections[i].data.roleId);
			}
		}
		if (ids.length == 0) {
			user.tabPanel.activate(user.roleGrid);
			Ext.Msg.alert("提示", "请至少为此用户分配一个角色!");
			return;
		}
		var form = user.formPanel.getForm();
		if (!form.isValid()) {
			user.tabPanel.activate(user.formPanel);
			return;
		}
		var params = {
			roleIds : ids
		};
		// 合并 params 和 form.getValues()，修改并返回 params。
		$.extend(params, form.getValues());
		
		//console.log("roleIds="+ids+"\n"+user.save+"\n"+form.getValues());
		if (params.password) {
			params.password = Ext.MD5(params.password);
		}
		Share.AjaxRequest({
			url : user.save,
			params : params,
			callback : function(json) {
				user.addWindow.hide();
				user.alwaysFun();
				user.store.reload();
				
				//fix bug 打开页面，编辑，不点击角色的tab。直接点击保存，再点击新建，在保存，会直接提交。
				//	user.tabPanel.activate(user.roleGrid);
				//Share.resetGrid(user.roleGrid);
			}
		});
	};
	user.resetPwdFun = function() {
		var form = user.formUppwdPanel.getForm();
		// 发送请求
		Share.AjaxRequest({
			url : user.reset,
			params : form.getValues(),
			callback : function(json) {
				user.addWindowUppass.hide();
				user.alwaysFun();
				user.store.reload();
			}
		});
		
	};
	user.delFun = function() {
		var record = user.grid.getSelectionModel().getSelected();
		Ext.Msg.confirm('提示', '确定要删除这条记录吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : user.del + record.data.userId + ".do",
					callback : function(json) {
						user.alwaysFun();
						user.store.reload();
					}
				});
			}
		});
	};
	user.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'border',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
		items : [ user.grid ]
	});
</script>
