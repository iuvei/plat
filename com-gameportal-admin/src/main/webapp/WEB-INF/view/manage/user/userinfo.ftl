<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	Ext.ns("Ext.market.userinfo"); // 自定义一个命名空间
	userinfo = Ext.market.userinfo; // 定义命名空间的别名
	userinfo = {
		all : '/manage/userinfo/queryUserinfo.do', // 所有会员
		save : "/manage/userinfo/save.do",// 保存会员
		reset : "/manage/userinfo/resetPwd/",// 重置会员密码
		del : "/manage/userinfo/del/",// 删除会员
		enable : "/manage/userinfo/enable/",// 禁用会员
		disable : "/manage/userinfo/disable/",// 启用会员
		//allProxy : "/manage/userinfo/queryProxy.do",// 查询全部代理
		loadAbove : "/manage/userinfo/queryAbove.do",// 查询全部代理
		myAbove : "/manage/userinfo/queryAboveById.do",// 加载指定会员的上线代理
		pageSize : 20,// 每页显示的记录数
		isEdit : false, // 编辑模式
		STATUSMAP : eval('(${statusMap})'),//注意括号
		ACCOUNTTYPEMAP : eval('(${accounttypeMap})')//注意括号
	};
	/** 改变页的combo*/
	userinfo.pageSizeCombo = new Share.pageSizeCombo({
		value : '20',
		listeners : {
			select : function(comboBox) {
				userinfo.pageSize  = parseInt(comboBox.getValue());
				userinfo.bbar.pageSize  = parseInt(comboBox.getValue());
				userinfo.store.baseParams.limit = userinfo.pageSize;
				userinfo.store.baseParams.start = 0;
				userinfo.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	userinfo.pageSize = parseInt(userinfo.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	userinfo.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:true,
		baseParams : {
			start : 0,
			limit : userinfo.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : userinfo.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'uiid', 'account', 'passwd', 'accounttype', 'uname', 'atmpasswd', 'identitycard', 'phone', 'email', 'qq', 'birthday', 'grade', 'puiid', 'status', 'create_date', 'update_date' ]),
		listeners : {
			'load' : function(store, records, options) {
				userinfo.alwaysFun();
			}
		}
	});
	//userinfo.store.load(); 
	/** 基本信息-选择模式 */
	userinfo.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				userinfo.deleteAction.enable();
				userinfo.editAction.enable();
				userinfo.resetPwdAction.enable();
				userinfo.resetAtmPwdAction.enable();
				userinfo.enableAction.enable();
				userinfo.disableAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				userinfo.deleteAction.disable();
				userinfo.editAction.disable();
				userinfo.resetPwdAction.disable();
				userinfo.resetAtmPwdAction.disable();
				userinfo.enableAction.disable();
				userinfo.disableAction.disable();
			}
		}
	});
	/** 基本信息-数据列 */
	userinfo.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 110
		},
		//'uiid', 'account', 'passwd', 'accounttype', 'uname', 'atmpasswd', 'identitycard', 'phone', 'email', 'QQ', 'birthday', 'grade', 'puiid', 'status', 'create_date', 'update_date'
		columns : [ userinfo.selModel, {
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
		}, {
			header : '账号类型', //0 普通用户 1代理用户
			dataIndex : 'accounttype',
			renderer : function(v) {
				return Share.map(v, userinfo.ACCOUNTTYPEMAP, '');
			},
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
//			dataIndex : 'atmpasswd'
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
				return Share.map(v, userinfo.STATUSMAP, '');
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
	userinfo.password = new Ext.form.TextField({
		fieldLabel : '登录密码',
		inputType : 'password',
		minLength : 6,
		maxLength : 12,
		allowBlank : false,
		name : 'passwd',
		//confirmTo : 'passwdConfirm',
		anchor : '99%'
	});
	/** 会员登录密码确认 */
	userinfo.passwordConfirm = new Ext.form.TextField({
		fieldLabel : '登录密码确认',
		inputType : 'password',
		minLength : 6,
		maxLength : 12,
		allowBlank : false,
		name : 'passwdConfirm',
		//confirmTo : 'passwd',
		anchor : '99%'
	});
	/** 会员提款密码 */
	userinfo.atmpassword = new Ext.form.TextField({
		fieldLabel : '提款密码',
		inputType : 'password',
		maxLength : 32,
		allowBlank : false,
		name : 'atmpasswd',
		//confirmTo : 'atmpasswdConfirm',
		anchor : '99%'
	});
	/** 会员提款密码确认 */
	userinfo.atmpasswordConfirm = new Ext.form.TextField({
		fieldLabel : '提款密码确认',
		inputType : 'password',
		maxLength : 32,
		allowBlank : false,
		name : 'atmpasswdConfirm',
		//confirmTo : 'atmpasswd',
		anchor : '99%'
	});
	/** 新建 */
	userinfo.addAction = new Ext.Action({
		text : '新建',
		//text : '<fmt:message key="common.cancel"/>',
		iconCls : 'userinfo_add',
		handler : function() {
			userinfo.addWindow.setIconClass('userinfo_add'); // 设置窗口的样式
			userinfo.addWindow.setTitle('新建会员'); // 设置窗口的名称
			userinfo.addWindow.show().center(); // 显示窗口
			userinfo.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			
			userinfo.password.setVisible(true);
			userinfo.password.setDisabled(false);
			userinfo.passwordConfirm.setVisible(true);
			userinfo.passwordConfirm.setDisabled(false);
			userinfo.atmpassword.setVisible(true);
			userinfo.atmpassword.setDisabled(false);
			userinfo.atmpasswordConfirm.setVisible(true );
			userinfo.atmpasswordConfirm.setDisabled(false);
			
			userinfo.isEdit = false;
			// 显示代理层级
			userinfo.treeEditPanel.loader.url = userinfo.loadAbove;
			userinfo.treeEditPanel.root.reload();
		}
	});
	/** 编辑 */
	userinfo.editAction = new Ext.Action({
		text : '编辑',
		iconCls : 'userinfo_edit',
		disabled : true,
		handler : function() {
			var record = userinfo.grid.getSelectionModel().getSelected();
			
			userinfo.addWindow.setIconClass('userinfo_edit'); // 设置窗口的样式
			userinfo.addWindow.setTitle('编辑会员'); // 设置窗口的名称
			userinfo.addWindow.show().center();
			
			userinfo.password.setVisible(false);
			userinfo.password.setDisabled(true);
			userinfo.passwordConfirm.setVisible(false);
			userinfo.passwordConfirm.setDisabled(true);
			userinfo.atmpassword.setVisible(false);
			userinfo.atmpassword.setDisabled(true);
			userinfo.atmpasswordConfirm.setVisible(false);
			userinfo.atmpasswordConfirm.setDisabled(true);
			
			userinfo.formPanel.getForm().reset();
			userinfo.formPanel.getForm().loadRecord(record);
//			console.log(userinfo.password.value);
//			console.log(userinfo.passwordConfirm.value);
//			console.log(userinfo.atmpassword.value);
//			console.log(userinfo.atmpasswordConfirm.value);
			
			userinfo.isEdit = true;
			// 显示当前会员的上线代理层级
			userinfo.treeEditPanel.loader.url = userinfo.myAbove +"?uiid="+record.data.uiid;
			userinfo.treeEditPanel.root.reload();
		}
	});
	/** 删除 */
	userinfo.deleteAction = new Ext.Action({
		text : '删除',
		iconCls : 'userinfo_delete',
		disabled : true,
		handler : function() {
			userinfo.delFun();
		}
	});
	/** 重置登录密码 */
	userinfo.resetPwdAction = new Ext.Action({
		text : '重置登录密码',
		iconCls : 'reset_pwd',
		disabled : true,
		handler : function() {
			userinfo.resetPwdFun(1);
		}
	});
	/** 重置提款密码 */
	userinfo.resetAtmPwdAction = new Ext.Action({
		text : '重置提款密码',
		iconCls : 'reset_pwd',
		disabled : true,
		handler : function() {
			userinfo.resetPwdFun(2);
		}
	});
	/** 启用 */
	userinfo.enableAction = new Ext.Action({
		text : '启用',
		iconCls : 'enable',
		disabled : true,
		handler : function() {
			userinfo.enableFun();
		}
	});
	/** 禁用 */
	userinfo.disableAction = new Ext.Action({
		text : '禁用',
		iconCls : 'disable',
		disabled : true,
		handler : function() {
			userinfo.disableFun();
		}
	});
	/** 查询 */
	userinfo.searchField = new Ext.ux.form.SearchField({
		store : userinfo.store,
		paramName : 'account',
		emptyText : '请输入会员账号',
		style : 'margin-left: 5px;'
	});
	/** 顶部工具栏 */
	userinfo.tbar = [ userinfo.addAction, '-', userinfo.editAction, '-', userinfo.deleteAction, '-',userinfo.searchField,
	                '-', userinfo.resetPwdAction, '-', userinfo.resetAtmPwdAction, 
	                '-', userinfo.enableAction, '-', userinfo.disableAction
	                ];
	/** 底部工具条 */
	userinfo.bbar = new Ext.PagingToolbar({
		pageSize : userinfo.pageSize,
		store : userinfo.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', userinfo.pageSizeCombo ]
	});
	userinfo.treePanel = new Ext.tree.TreePanel({
		title : '会员代理层级',
		region : 'east',
		split : true,
		minSize : 200,
		maxSize : 900,
		useArrows : true,
		autoScroll : true,
		width : '30%',
		//tbar : [role.saveMudulesAction, '-'],
		animate : true,
		enableDD : true,
		containerScroll : true,
		rootVisible : true,
		buttonAlign : 'left',
		frame : false,
		expanded:true,
		root : 
			{
			id: 'rootnode_123',
			text: '平台',
	        draggable: false,
			nodeType : 'async'
			},
		loader : new Ext.tree.TreeLoader({
            url : userinfo.myAbove
        }),
		listeners : {
		}
	});
	/** 基本信息-表格 */
	userinfo.grid = new Ext.grid.EditorGridPanel({
		store : userinfo.store,
		colModel : userinfo.colModel,
		selModel : userinfo.selModel,
		tbar : userinfo.tbar,
		bbar : userinfo.bbar,
		autoScroll : 'auto',
		region : 'center',
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig:{},
		stripeRows : true,
		listeners : {
			'cellclick' : function(obj, rowIndex, columnIndex, e) {
				var record = userinfo.grid.getSelectionModel().getSelected();
				if (record) {
					// 更新树节点
					userinfo.treePanel.enable();
					userinfo.treePanel.loader.baseParams.uiid = record.data.uiid;
					userinfo.treePanel.root.reload();
				}
			}
		}
	});
	
//	myTreeLoader.on("beforeload", function(treeLoader, node) {
//        this.baseParams.category = node.attributes.category;
//    }, this);

	
	userinfo.statusCombo = new Ext.form.ComboBox({
				fieldLabel : '状态',
				hiddenName : 'status',
				name : 'status',
				triggerAction : 'all',
				mode : 'local',
				store : new Ext.data.ArrayStore({
							fields : ['v', 't'],
							data : Share.map2Ary(userinfo.STATUSMAP)
						}),
				valueField : 'v',
				displayField : 't',
				allowBlank : false,
				editable : false,
				value : "1",
				anchor : '99%'
			});
			
	userinfo.accounttypeCombo = new Ext.form.ComboBox({
				fieldLabel : '账号类型',
				hiddenName : 'accounttype',
				name : 'accounttype',
				triggerAction : 'all',
				mode : 'local',
				store : new Ext.data.ArrayStore({
							fields : ['v', 't'],
							data : Share.map2Ary(userinfo.ACCOUNTTYPEMAP)
						}),
				valueField : 'v',
				displayField : 't',
				allowBlank : false,
				editable : false,
				value : "0",
				anchor : '99%'
			});
	/** 基本信息-详细信息的form */
	userinfo.formPanel = new Ext.form.FormPanel({
		region : 'center',
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
//		}, {
//			xtype : 'hidden',
//			fieldLabel : '账号类型',
//			name : 'accounttype'
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
		userinfo.accounttypeCombo,
		userinfo.password, userinfo.passwordConfirm, 
		userinfo.atmpassword, userinfo.atmpasswordConfirm, 
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
		}, userinfo.statusCombo
		]
	});
	
	/** 代理层级树 */
	userinfo.treeEditPanel = new Ext.tree.TreePanel({
		title : '会员代理层级',
		region : 'west',
		split : true,
		minSize : 200,
		maxSize : 900,
		useArrows : true,
		autoScroll : true,
		width : '30%',
		//tbar : [userinfo.saveMudulesAction, '-'],
		animate : true,
		enableDD : true,
		containerScroll : true,
		rootVisible : false,
		buttonAlign : 'left',
		frame : false,
		expanded:true,
		root : {
			id: 'rootnode_456',
			text: '平台',
	        draggable: false,
			nodeType : 'async'
		},
		loader : new Ext.tree.TreeLoader({
	        url : userinfo.loadAbove
	    }),
		listeners : {
			'checkchange' : function(node, checked) {
//				console.log(node);
//				console.log(userinfo.isEdit);
//				console.log(checked);
				if(userinfo.isEdit===true){
					// 在编辑模式下
					// 无法更改上线代理,(还原为原始值)
					if(checked==true){
						node.getUI().checkbox.checked = false;
					}else{
						node.getUI().checkbox.checked = true;	
					}
					return;
				}else{
					// 在新建模式下
					// 只能选择一个上线上代理，(即为单选)
					if(checked==true){
						// 取消所有选择
						userinfo.clearTreeNodeCheckFun(userinfo.treeEditPanel.root, false);
					}
					// 当前节点设置成选中
					node.getUI().checkbox.checked = true;
					// 将上线代理值更新到表单
					userinfo.formPanel.getForm().findField("puiid").setValue(node.id.replace("_generate_",""))
					console.log(userinfo.formPanel.getForm().findField("puiid").getValue());
				}
			}
		}
	});
	
	userinfo.editPanel = new Ext.Panel({
		layout : 'border',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
		//autoHeight : true,
		items : [ userinfo.treeEditPanel, userinfo.formPanel ]
	});

	/** 编辑新建窗口 */
	userinfo.addWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 460,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ userinfo.editPanel ],
		buttons : [ {
			text : '保存',
			handler : function() {
				userinfo.saveFun();
			}
		}, {
			text : '重置',
			handler : function() {
				Share.resetGrid(userinfo.proxyGrid);
				var form = userinfo.formPanel.getForm();
				var uiid = form.findField("uiid").getValue();
				var account = form.findField("account").getValue();
				var puiid = form.findField("puiid").getValue();
				form.reset();
				if (uiid != '')
					form.findField("uiid").setValue(uiid);
				if (account != '')
					form.findField("account").setValue(account);
				if (puiid != '')
					form.findField("puiid").setValue(puiid);
			}
		} ]
	});
	
	/** 先清空已选择的代理 */
	userinfo.clearTreeNodeCheckFun = function(treeNode, checked) {
		var i;
		if (treeNode.hasChildNodes()) {
			for (i = 0; i < treeNode.childNodes.length; i++) {
				if (treeNode.childNodes[i].getUI().checkbox) {
					treeNode.childNodes[i].getUI().checkbox.checked = checked;
				}
			}
			for (i = 0; i < treeNode.childNodes.length; i++) {
				userinfo.clearTreeNodeCheckFun(treeNode.childNodes[i], checked);
			}
		}
	};
	
	userinfo.alwaysFun = function() {
		Share.resetGrid(userinfo.grid);
		userinfo.deleteAction.disable();
		userinfo.resetPwdAction.disable();
		userinfo.resetAtmPwdAction.disable();
		userinfo.editAction.disable();
		userinfo.clearTreeNodeCheckFun(userinfo.treeEditPanel.root, false);// 取消选择
		userinfo.treePanel.setDisabled(true);// 设置树为无效
	};
	userinfo.saveFun = function() {
		var form = userinfo.formPanel.getForm();
		if (!form.isValid()) {
			return;
		}
		var puiid = form.findField("puiid").getValue();
		var accounttype = form.findField("accounttype").getValue();
		if(1==accounttype){ // 代理会员，必须选择上线代理,总代理只能直接在数据库添加
			if(!puiid || ''==puiid){
				Ext.Msg.alert("提示", "请为此会员选择一个上线代理!");
				return;
			}
		}
		// 发送请求
		Share.AjaxRequest({
			url : userinfo.save,
			params : form.getValues(),
			callback : function(json) {
				userinfo.addWindow.hide();
				userinfo.alwaysFun();
				userinfo.store.reload();
			}
		});
	};
	userinfo.resetPwdFun = function(type) {
		var record = userinfo.grid.getSelectionModel().getSelected();
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
					url : userinfo.reset + "/" + type + "/" + record.data.uiid + ".do",
					callback : function(json) {
						userinfo.alwaysFun();
						userinfo.store.reload();
					}
				});
			}
		});
	};
	userinfo.delFun = function() {
		var record = userinfo.grid.getSelectionModel().getSelected();
		Ext.Msg.confirm('提示', '确定要删除这条记录吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : userinfo.del + record.data.uiid + ".do",
					callback : function(json) {
						userinfo.alwaysFun();
						userinfo.store.reload();
					}
				});
			}
		});
	};

	userinfo.enableFun = function(){
		var record = userinfo.grid.getSelectionModel().getSelected();
		if (record.data.status==1) {
			Ext.Msg.alert('提示', '此会员已经是启用状态');
			return;
		}
		Ext.Msg.confirm('提示', '确定要启用此会员吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : userinfo.enable + record.data.uiid + ".do",
					callback : function(json) {
						userinfo.alwaysFun();
						userinfo.store.reload();
					}
				});
			}
		});
	};
	userinfo.disableFun = function(){
		var record = userinfo.grid.getSelectionModel().getSelected();
		if (record.data.status==0) {
			Ext.Msg.alert('提示', '此会员已经是禁用状态');
			return;
		}
		Ext.Msg.confirm('提示', '确定要禁用此会员吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : userinfo.disable + record.data.uiid + ".do",
					callback : function(json) {
						userinfo.alwaysFun();
						userinfo.store.reload();
					}
				});
			}
		});
	};

	userinfo.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'border',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
		items : [ userinfo.grid, userinfo.treePanel ]
	});
</script>
