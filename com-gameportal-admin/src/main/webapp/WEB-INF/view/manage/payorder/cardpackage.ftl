<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	Ext.ns("Ext.market.cardpackag"); // 自定义一个命名空间
	Ext.namespace('Ext.form.dict');
	cardpackag = Ext.market.cardpackag; // 定义命名空间的别名
	cardpackag = {
		all : '/manage/cardpackag/queryCardPackage.do', // 所有公司银行卡
		save : "/manage/cardpackag/save.do",// 保存公司银行卡
		del : "/manage/cardpackag/del/",// 删除公司银行卡
		enable : "/manage/cardpackag/enable/",// 禁用公司银行卡
		disable : "/manage/cardpackag/disable/",// 启用公司银行卡
		pageSize : 30,// 每页显示的记录数
		BANKNAMEMAP : eval('(${bankNameMap})'),//注意括号
		STATUSMAP : eval('(${statusMap})'),//注意括号
		isAdmin : '${isAdmin}',
		isFK : '${isFK}'
	};
	/** 改变页的combo*/
	cardpackag.pageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				cardpackag.pageSize  = parseInt(comboBox.getValue());
				cardpackag.bbar.pageSize  = parseInt(comboBox.getValue());
				cardpackag.store.baseParams.limit = cardpackag.pageSize;
				cardpackag.store.baseParams.start = 0;
				cardpackag.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	cardpackag.pageSize = parseInt(cardpackag.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	cardpackag.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:true,
		baseParams : {
			start : 0,
			limit : cardpackag.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : cardpackag.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'cpid','uiid','account','trueName','bankname','openingbank','accountname','cardnumber','status','cardnumber','openingbank','createDate','province','city','updateDate','alipayname','alipay' ]),
		listeners : {
			'load' : function(store, records, options) {
				cardpackag.alwaysFun();
			}
		}
	});
	//cardpackag.store.load(); 
	/** 基本信息-选择模式 */
	cardpackag.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				if(cardpackag.isAdmin == '1'){
					cardpackag.deleteAction.enable();
					cardpackag.editAction.enable();
				}
				if(cardpackag.isFK == '1'){
					cardpackag.editAction.enable();
				}
				cardpackag.enableAction.enable();
				cardpackag.disableAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				cardpackag.deleteAction.disable();
				cardpackag.editAction.disable();
				cardpackag.enableAction.disable();
				cardpackag.disableAction.disable();
			}
		}
	});
	/** 基本信息-数据列 */
	cardpackag.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 110
		},
		//'cpid','uiid','bankname'trueName,'openingbank','accountname','cardnumber','status','cardnumber','openingbank','createDate','province','city','updateDate'
		columns : [ cardpackag.selModel, {
			hidden : true,
			header : '公司银行卡ID',
			dataIndex : 'cpid'
		},{
			header : '会员账号',
			dataIndex : 'account',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '开户姓名',
			dataIndex : 'trueName',
			width:60,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '银行名称',
			dataIndex : 'bankname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '卡号',
			dataIndex : 'cardnumber',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '开户省份',
			width : 60,
			dataIndex : 'province',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '开户市',
			width : 60,
			dataIndex : 'city',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '开户地址',
			width : 200,
			dataIndex : 'openingbank',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '是否解锁',
			dataIndex : 'status',
			width:60,
			renderer : function(v) {
				return Share.map(v, cardpackag.STATUSMAP, '');
			}
		}, {
			header : '创建者',
			dataIndex : 'accountname',
			width:60,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '创建时间',
			dataIndex : 'createDate',
			width : 130,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '更新时间',
			dataIndex : 'updateDate',
			width : 130,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
		header : '支付宝姓名',
			dataIndex : 'alipayname',
			width : 80,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '支付宝账号',
			dataIndex : 'alipay',
			width : 130,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		} ]
	});
	/** 新建 */
	cardpackag.addAction = new Ext.Action({
		text : '新建',
		//text : '<fmt:message key="common.cancel"/>',
		iconCls : 'Add',
		handler : function() {
			cardpackag.addWindow.setIconClass('Add'); // 设置窗口的样式
			cardpackag.addWindow.setTitle('新增会员银行卡'); // 设置窗口的名称
			cardpackag.addWindow.show().center(); // 显示窗口
			cardpackag.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			//cardpackag.password.setVisible(true);
			//cardpackag.password.setDisabled(false);
			//cardpackag.tabPanel.activate(cardpackag.formPanel);
		}
	});
	/** 编辑 */
	cardpackag.editAction = new Ext.Action({
		text : '编辑',
		iconCls : 'Applicationedit',
		disabled : true,
		handler : function() {
			var record = cardpackag.grid.getSelectionModel().getSelected();
			cardpackag.addWindow.setIconClass('Applicationedit'); // 设置窗口的样式
			cardpackag.addWindow.setTitle('编辑会员银行卡'); // 设置窗口的名称
			cardpackag.addWindow.show().center();
			cardpackag.formPanel.getForm().reset();
			cardpackag.formPanel.getForm().loadRecord(record);
			//cardpackag.tabPanel.activate(cardpackag.formPanel);
			var form =cardpackag.formPanel.getForm();
			if(cardpackag.isFK == '1'){
				form.findField("account").setReadOnly(true);
				form.findField("accountname").setReadOnly(true);
			}
		}
	});
	/** 删除 */
	cardpackag.deleteAction = new Ext.Action({
		text : '删除',
		iconCls : 'Delete',
		disabled : true,
		handler : function() {
			cardpackag.delFun();
		}
	});
	/** 锁定 */
	cardpackag.enableAction = new Ext.Action({
		text : '解锁',
		iconCls : 'Lockkey',
		disabled : true,
		handler : function() {
			cardpackag.enableFun();
		}
	});
	/** 解锁 */
	cardpackag.disableAction = new Ext.Action({
		text : '锁定',
		iconCls : 'Lockopen',
		disabled : true,
		handler : function() {
			cardpackag.disableFun();
		}
	});
	
		/** 查询按钮 */
	cardpackag.searchAction = new Ext.Action({
		text : '查询',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				cardpackag.searchFun();
			}
	});
	cardpackag.searchParams = function(){
		var obj = {};
		obj.openingbank=$("#depositorderuaccount").val(); //会员账号
		obj.uname=$("#accountname").val(); //会员姓名
		obj.bankcard=$("#depositorderbankcard").val(); //银行卡号
	    return obj;
	}
	
	cardpackag.searchFun = function(){
		cardpackag.store.load({params: cardpackag.searchParams()});
	}
	
	cardpackag.store.on('beforeload',function(store, options){
	    cardpackag.store.baseParams = cardpackag.searchParams();
	});
	
	
	/** 顶部工具栏 */
	cardpackag.tbar = [ cardpackag.addAction, '-', cardpackag.editAction, '-', cardpackag.deleteAction,
	                '-', cardpackag.enableAction, '-', cardpackag.disableAction,
	                '-','&nbsp;','会员账号:',{id:'depositorderuaccount',xtype:'textfield',width:100},
	                 '-','&nbsp;','开户姓名:',{id:'accountname',xtype:'textfield',width:100},
	                '-','&nbsp;','银行卡号:',{id:'depositorderbankcard',xtype:'textfield',width:130},
	                '-','&nbsp;',cardpackag.searchAction
	                ];
	/** 底部工具条 */
	cardpackag.bbar = new Ext.PagingToolbar({
		pageSize : cardpackag.pageSize,
		store : cardpackag.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', cardpackag.pageSizeCombo ]
	});
	/** 基本信息-表格 */
	cardpackag.grid = new Ext.grid.EditorGridPanel({
		store : cardpackag.store,
		colModel : cardpackag.colModel,
		selModel : cardpackag.selModel,
		tbar : cardpackag.tbar,
		bbar : cardpackag.bbar,
		autoScroll : 'auto',
		region : 'center',
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig:{},
		stripeRows : true
	});
	cardpackag.statusCombo = new Ext.form.ComboBox({
		fieldLabel : '是否解锁',
		hiddenName : 'status',
		name : 'status',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
			fields : ['v', 't'],
			data : Share.map2Ary(cardpackag.STATUSMAP)
		}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		value : "1",
		anchor : '99%'
	});
	
	cardpackag.bankprovinceCombo = new Ext.form.ComboBox({
				fieldLabel : '开户省份',
				hiddenName : 'province',
				name : 'province',
				triggerAction : 'all',
				mode : 'local',
				store : new Ext.data.ArrayStore({
							fields : ['v', 't'],
							data : Share.map2Ary(cardpackag.BANKNAMEMAP)
						}),
				valueField : 'v',
				displayField : 't',
				allowBlank : false,
				editable : false,
				anchor : '99%'
			});
		cardpackag.bankcityCombo = new Ext.form.ComboBox({
			fieldLabel : '开户市区',
			hiddenName : 'city',
			name : 'city',
			triggerAction : 'all',
			mode : 'local',
			store : new Ext.data.ArrayStore({
						fields : ['v', 't'],
						data : Share.map2Ary(cardpackag.BANKNAMEMAP)
					}),
			valueField : 'v',
			displayField : 't',
			allowBlank : false,
			editable : false,
			anchor : '99%'
		});
	
	cardpackag.banknameCombo = new Ext.form.ComboBox({
				fieldLabel : '银行名称',
				hiddenName : 'bankname',
				name : 'bankname',
				triggerAction : 'all',
				mode : 'local',
				store : new Ext.data.ArrayStore({
							fields : ['v', 't'],
							data : Share.map2Ary(cardpackag.BANKNAMEMAP)
						}),
				valueField : 'v',
				displayField : 't',
				editable : false,
				anchor : '99%'
			});
			
	/** 基本信息-详细信息的form */
	cardpackag.formPanel = new Ext.form.FormPanel({
		autoScroll : false,
		border: false,
        style: 'border-bottom:0px;',
        bodyStyle: 'padding:10px;background-color:transparent;',
		labelwidth : 70,
		defaultType : 'textfield',
		//'cpid','uiid','bankname','openingbank','accountname','cardnumber','status','cardnumber','openingbank','createDate','province','city','updateDate','alipayname','alipay'
		items : [ {
			xtype : 'hidden',
			fieldLabel : 'ID',
			name : 'cpid'
		},{
			fieldLabel : '会员账号',
			name : 'account',
			allowBlank : false,
			anchor : '99%'
		},{
			fieldLabel : '开户人姓名',
			name : 'accountname',
			anchor : '99%'
		}, cardpackag.banknameCombo,
		{
			fieldLabel : '卡号',
			name : 'cardnumber',
			anchor : '99%'
		}, cardpackag.statusCombo,
		{
			fieldLabel : '开户省份',
			name : 'province',
			anchor : '99%'
		},{
			fieldLabel : '开户市区',
			name : 'city',
			anchor : '99%'
		},{
			fieldLabel : '开户行地址',
			maxLength : 200,
			xtype : 'textarea',
			name : 'openingbank',
			anchor : '99%'
		},{
			fieldLabel : '支付宝真实姓名',
			maxLength : 40,
			name : 'alipayname',
			anchor : '99%'
		},{
			fieldLabel : '支付宝账号',
			maxLength : 60,
			name : 'alipay',
			anchor : '99%'
		}]
	});
	/** 编辑新建窗口 */
	cardpackag.addWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 420,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ cardpackag.formPanel ],
		buttons : [ {
			text : '保存',
			handler : function() {
				cardpackag.saveFun();
			}
		}, {
			text : '重置',
			handler : function() {
				var form = cardpackag.formPanel.getForm();
				var cpid = form.findField("cpid").getValue();
				var account = form.findField("account").getValue();
				form.reset();
				if (cpid != '')
					form.findField("cpid").setValue(cpid);
				if (account != '')
					form.findField("account").setValue(account);
			}
		} ]
	});
	cardpackag.alwaysFun = function() {
		Share.resetGrid(cardpackag.grid);
		cardpackag.deleteAction.disable();
		cardpackag.editAction.disable();
	};
	cardpackag.saveFun = function() {
		var form = cardpackag.formPanel.getForm();
		if (!form.isValid()) {
			return;
		}
		// 发送请求
		Share.AjaxRequest({
			url : cardpackag.save,
			params : form.getValues(),
			callback : function(json) {
				cardpackag.addWindow.hide();
				cardpackag.alwaysFun();
				cardpackag.store.reload();
				
				//fix bug 打开页面，编辑，不点击角色的tab。直接点击保存，再点击新建，在保存，会直接提交。
				//	cardpackag.tabPanel.activate(cardpackag.roleGrid);
				//Share.resetGrid(cardpackag.roleGrid);
			}
		});
	};
	
	cardpackag.delFun = function() {
		var record = cardpackag.grid.getSelectionModel().getSelected();
		Ext.Msg.confirm('提示', '确定要删除这条记录吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : cardpackag.del + record.data.cpid + ".do",
					callback : function(json) {
						cardpackag.alwaysFun();
						cardpackag.store.reload();
					}
				});
			}
		});
	};

	cardpackag.enableFun = function(){
		var record = cardpackag.grid.getSelectionModel().getSelected();
		if (record.data.status==0) {
			Ext.Msg.alert('提示', '此银行卡已经是未锁定状态');
			return;
		}
		Ext.Msg.confirm('提示', '确定要解锁此银行卡吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : cardpackag.enable + record.data.cpid + ".do",
					callback : function(json) {
						cardpackag.alwaysFun();
						cardpackag.store.reload();
					}
				});
			}
		});
	};
	cardpackag.disableFun = function(){
		var record = cardpackag.grid.getSelectionModel().getSelected();
		if (record.data.status==1) {
			Ext.Msg.alert('提示', '此银行卡已经是锁定状态');
			return;
		}
		Ext.Msg.confirm('提示', '确定要锁定此银行卡吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : cardpackag.disable + record.data.cpid + ".do",
					callback : function(json) {
						cardpackag.alwaysFun();
						cardpackag.store.reload();
					}
				});
			}
		});
	};
	cardpackag.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'border',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
		items : [ cardpackag.grid ]
	});
</script>
