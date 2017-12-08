<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	Ext.ns("Ext.market.ccgroup"); // 自定义一个命名空间
	ccgroup = Ext.market.ccgroup; // 定义命名空间的别名
	ccgroup = {
		all : '/manage/ccgroup/queryCcgroup.do', // 所有公司银行卡分组
		save : "/manage/ccgroup/save.do",// 保存公司银行卡分组
		del : "/manage/ccgroup/del/",// 删除公司银行卡分组
		allCC : "/manage/ccgroup/cc/queryAll.do",//所有公司银行卡
		myCC : "/manage/ccgroup/cc/queryByCcgid/", // 当前分组的公司银行卡
		saveCCAndGroup : "/manage/ccgroup/cc/saveCCAndGroup.do",//保存银行卡与分组的关联
		childNodes : '',
		pageSize : 20,// 每页显示的记录数
		memGrade:eval('(${fields.memGrade})'),
		TYPEMAP : eval('(${typeMap})')//注意括号
	};
	/** 改变页的combo*/
	ccgroup.pageSizeCombo = new Share.pageSizeCombo({
		value : '20',
		listeners : {
			select : function(comboBox) {
				ccgroup.pageSize  = parseInt(comboBox.getValue());
				ccgroup.bbar.pageSize  = parseInt(comboBox.getValue());
				ccgroup.store.baseParams.limit = ccgroup.pageSize;
				ccgroup.store.baseParams.start = 0;
				ccgroup.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	ccgroup.pageSize = parseInt(ccgroup.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	ccgroup.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:true,
		baseParams : {
			start : 0,
			limit : ccgroup.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : ccgroup.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'ccgid','type','grade','name','descript','createuserid','createusername','createtime','updateuserid','updateusername','updatetime' ]),
		listeners : {
			'load' : function(store, records, options) {
				ccgroup.alwaysFun();
			}
		}
	});
	//ccgroup.store.load(); 
	/** 基本信息-选择模式 */
	ccgroup.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				ccgroup.deleteAction.enable();
				ccgroup.editAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				ccgroup.deleteAction.disable();
				ccgroup.editAction.disable();
			}
		}
	});
	/** 基本信息-数据列 */
	ccgroup.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 110
		},
		/*
		 * 'ccgid','type','grade','name','descript',
		 * 'createuserid','createusername','createtime','updateuserid','updateusername','updatetime'
		 */
		columns : [ ccgroup.selModel, {
			hidden : true,
			header : '公司银行卡分组ID',
			dataIndex : 'ccgid'
		}, {
			header : '名称',
			dataIndex : 'name',
			hidden:true,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '类型',
			dataIndex : 'type',
			hidden:true,
			width:60,
			renderer : function(v) {
				return Share.map(v, ccgroup.TYPEMAP, '');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '星级',
			dataIndex : 'grade',
			width:60,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			}),
			renderer : function(v) {
				return Share.map(v, ccgroup.memGrade, '');
				
			}
		}, {
			header : '描述',
			dataIndex : 'descript',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '创建者',
			width:80,
			dataIndex : 'createusername',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '创建时间',
			dataIndex : 'createtime',
			width : 130,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '更新者',
			dataIndex : 'updateusername',
			width:80,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '更新时间',
			dataIndex : 'updatetime',
			width : 130,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		} ]
	});
	/** 新建 */
	ccgroup.addAction = new Ext.Action({
		text : '新建',
		//text : '<fmt:message key="common.cancel"/>',
		iconCls : 'Add',
		handler : function() {
			ccgroup.addWindow.setIconClass('Add'); // 设置窗口的样式
			ccgroup.addWindow.setTitle('新建'); // 设置窗口的名称
			ccgroup.addWindow.show().center(); // 显示窗口
			ccgroup.formPanel.getForm().reset(); // 清空表单里面的元素的值.
		}
	});
	/** 编辑 */
	ccgroup.editAction = new Ext.Action({
		text : '编辑',
		iconCls : 'Applicationedit',
		disabled : true,
		handler : function() {
			var record = ccgroup.grid.getSelectionModel().getSelected();
			ccgroup.addWindow.setIconClass('Applicationedit'); // 设置窗口的样式
			ccgroup.addWindow.setTitle('编辑'); // 设置窗口的名称
			ccgroup.addWindow.show().center();
			ccgroup.formPanel.getForm().reset();
			ccgroup.formPanel.getForm().loadRecord(record);
		}
	});
	/** 删除 */
	ccgroup.deleteAction = new Ext.Action({
		text : '删除',
		iconCls : 'Cross',
		disabled : true,
		handler : function() {
			ccgroup.delFun();
		}
	});
	/** 查询 */
	ccgroup.searchField = new Ext.ux.form.SearchField({
		store : ccgroup.store,
		paramName : 'name',
		emptyText : '请输入分组名称',
		style : 'margin-left: 5px;'
	});
	/** 顶部工具栏 */
	ccgroup.tbar = [ ccgroup.addAction, '-', ccgroup.editAction, '-', ccgroup.deleteAction, '-',ccgroup.searchField ];
	
	/** 底部工具条 */
	ccgroup.bbar = new Ext.PagingToolbar({
		pageSize : ccgroup.pageSize,
		store : ccgroup.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', ccgroup.pageSizeCombo ]
	});
	/** 基本信息-表格 */
	ccgroup.grid = new Ext.grid.EditorGridPanel({
		store : ccgroup.store,
		colModel : ccgroup.colModel,
		selModel : ccgroup.selModel,
		tbar : ccgroup.tbar,
		bbar : ccgroup.bbar,
		autoScroll : 'auto',
		region : 'center',
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig:{},
		stripeRows : true,
		listeners : {
			'cellclick' : function(obj, rowIndex, columnIndex, e) {
				var record = ccgroup.grid.getSelectionModel().getSelected();
				if (record) {
					ccgroup.treePanel.enable();
					// 先清空已选择的状态
					ccgroup.clearTreeNodeCheckFun(ccgroup.treePanel.root, false);
					// 当前选择用户的资源信息
					Share.AjaxRequest({
								url : ccgroup.myCC + record.data.ccgid+".do",
								callback : function(json) {
									ccgroup.treePanel.getRootNode().expand(true,true);//默认全部展开
									// 勾选资源树的checkbox
									if (json.length > 0) {
										for (var i = 0; i < json.length; i++) {
											ccid = json[i].ccid;
											var treeNode = ccgroup.treePanel.getNodeById("_generate_" + ccid);
											treeNode.expand(true);  
											if(treeNode){
												treeNode.getUI().checkbox.checked = true;
											}
										}
									}
								}
							});
				}
			}
		}
	});
	ccgroup.typeCombo = new Ext.form.ComboBox({
		fieldLabel : '类型',
		hiddenName : 'type',
		name : 'type',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
			fields : ['v', 't'],
			data : Share.map2Ary(ccgroup.TYPEMAP)
		}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		value : "0",
		anchor : '99%'
	});
	
	/** 基本信息-详细信息的form */
	ccgroup.formPanel = new Ext.form.FormPanel({
		autoScroll : false,
		border: false,
        style: 'border-bottom:0px;',
        bodyStyle: 'padding:10px;background-color:transparent;',
		labelwidth : 70,
		defaultType : 'textfield',
		/*
		 * 'ccgid','type','grade','name','descript',
		 * 'createuserid','createusername','createtime','updateuserid','updateusername','updatetime'
		 */
		items : [ {
			xtype : 'hidden',
			fieldLabel : 'ID',
			name : 'ccgid'
		}, {
			allowBlank : false,
			fieldLabel : '名称',
			anchor : '99%',
			name : 'name'
		}, ccgroup.typeCombo, {
			fieldLabel : '星级',
			name : 'grade',
			anchor : '99%'
		}, {
			fieldLabel : '描述',
			name : 'descript',
			maxLength : 200,
			xtype : 'textarea',
			anchor : '99%'
		} 
		]
	});
	/** 编辑新建窗口 */
	ccgroup.addWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 260,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ ccgroup.formPanel ],
		buttons : [ {
			text : '保存',
			handler : function() {
				ccgroup.saveFun();
			}
		}, {
			text : '重置',
			handler : function() {
				var form = ccgroup.formPanel.getForm();
				var ccgid = form.findField("ccgid").getValue();
				form.reset();
				if (ccgid != '')
					form.findField("ccgid").setValue(ccgid);
			}
		} ]
	});
	ccgroup.alwaysFun = function() {
		Share.resetGrid(ccgroup.grid);
		ccgroup.deleteAction.disable();
		ccgroup.editAction.disable();
		
		ccgroup.saveCCAndGroupAction.disable();
		ccgroup.clearTreeNodeCheckFun(ccgroup.treePanel.root, false);// 取消选择
		ccgroup.treePanel.setDisabled(true);// 设置树为无效
	};
	ccgroup.saveFun = function() {
		var form = ccgroup.formPanel.getForm();
		if (!form.isValid()) {
			return;
		}
		// 发送请求
		Share.AjaxRequest({
			url : ccgroup.save,
			params : form.getValues(),
			callback : function(json) {
				ccgroup.addWindow.hide();
				ccgroup.alwaysFun();
				ccgroup.store.reload();
			}
		});
	};
	
	ccgroup.delFun = function() {
		var record = ccgroup.grid.getSelectionModel().getSelected();
		Ext.Msg.confirm('提示', '确定要删除这条记录吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : ccgroup.del + record.data.ccgid + ".do",
					callback : function(json) {
						ccgroup.alwaysFun();
						ccgroup.store.reload();
					}
				});
			}
		});
	};


	/** 先清空已选择的状态 */
	ccgroup.clearTreeNodeCheckFun = function(treeNode, checked) {
		var i;
		if (treeNode.hasChildNodes()) {
			for (i = 0; i < treeNode.childNodes.length; i++) {
				if (treeNode.childNodes[i].getUI().checkbox) {
					treeNode.childNodes[i].getUI().checkbox.checked = checked;
				}
			}
			for (i = 0; i < treeNode.childNodes.length; i++) {
				ccgroup.clearTreeNodeCheckFun(treeNode.childNodes[i], checked);
			}
		}
	};
	ccgroup.visitAllTreeNodeFun = function(treeNode) {
		var i;
		if (treeNode.hasChildNodes()) {
			for (i = 0; i < treeNode.childNodes.length; i++) {
				if (treeNode.childNodes[i].getUI().checkbox) {
					if (treeNode.childNodes[i].getUI().checkbox.checked) {
						// 去除前缀
						ccgroup.childNodes += treeNode.childNodes[i].id.replace("_generate_", "") + ',';
					}
				}
			}
			for (i = 0; i < treeNode.childNodes.length; i++) {
				ccgroup.visitAllTreeNodeFun(treeNode.childNodes[i]);
			}
		}
	};
	/** 保存分组与公司银行卡的关联 */
	ccgroup.saveCCAndGroupAction = new Ext.Action({
				text : '保存',
				iconCls : 'Disk',
				disabled : true,
				handler : function() {
					ccgroup.childNodes = "";
					ccgroup.visitAllTreeNodeFun(ccgroup.treePanel.root);// 得到选择的菜单
					if (ccgroup.childNodes == "") {
						Ext.Msg.alert("提示", "请至少为此分组分配一个银行卡!");
						return;
					}
					var record = ccgroup.grid.getSelectionModel().getSelected();
					var params = {
						'ccids' : ccgroup.childNodes,
						'ccgid' : record.data.ccgid
					};
					// 发送请求
					Share.AjaxRequest({
								url : ccgroup.saveCCAndGroup,
								params : params,
								callback : function(json) {
									ccgroup.alwaysFun();
									ccgroup.store.reload();
								}
							});

				}
			});
	ccgroup.treePanel = new Ext.tree.TreePanel({
				title : '当前分组下公司银行卡',
				region : 'east',
				split : true,
				minSize : 200,
				maxSize : 900,
				useArrows : true,
				autoScroll : true,
				width : '30%',
				tbar : [ccgroup.saveCCAndGroupAction, '-'],
				animate : true,
				enableDD : true,
				containerScroll : true,
				rootVisible : false,
				buttonAlign : 'left',
				frame : false,
				expanded:true,
				root : {
					nodeType : 'async'
				},
				dataUrl : ccgroup.allCC,
				listeners : {
					'checkchange' : function(node, checked) {
						// 保存按钮生效
						ccgroup.saveCCAndGroupAction.enable();
						if (node.childNodes.length > 0) {
							for (var i = 0; i < node.childNodes.length; i++) {
								if (node.childNodes[i].getUI().checkbox) {
									node.childNodes[i].getUI().checkbox.checked = checked;
								}
							}
						}
						if (node.parentNode && node.parentNode.getUI().checkbox != null) {
							ccgroup.checkParentFun(node.parentNode);
						}
					}
				}
			});
	
	ccgroup.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'border',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
		items : [ ccgroup.grid, ccgroup.treePanel ]
	});
</script>
