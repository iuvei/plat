<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	Ext.ns("Ext.market.proxyximaset"); // 自定义一个命名空间
	proxyximaset = Ext.market.proxyximaset; // 定义命名空间的别名
	proxyximaset = {
		all : '/manage/proxyximaset/queryProxyximaset.do', // 所有代理洗码设置
		save : "/manage/proxyximaset/save.do",// 保存代理洗码设置
		del : "/manage/proxyximaset/del/",// 删除代理洗码设置
		pageSize : 20// 每页显示的记录数
	};
	/** 改变页的combo*/
	proxyximaset.pageSizeCombo = new Share.pageSizeCombo({
		value : '20',
		listeners : {
			select : function(comboBox) {
				proxyximaset.pageSize  = parseInt(comboBox.getValue());
				proxyximaset.bbar.pageSize  = parseInt(comboBox.getValue());
				proxyximaset.store.baseParams.limit = proxyximaset.pageSize;
				proxyximaset.store.baseParams.start = 0;
				proxyximaset.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	proxyximaset.pageSize = parseInt(proxyximaset.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	proxyximaset.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:true,
		baseParams : {
			start : 0,
			limit : proxyximaset.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : proxyximaset.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'pxsid','uiid','account','name','scale','updateuserid','updatetime','updateusername' ]),
		listeners : {
			'load' : function(store, records, options) {
				proxyximaset.alwaysFun();
			}
		}
	});
	//proxyximaset.store.load(); 
	/** 基本信息-选择模式 */
	proxyximaset.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				proxyximaset.deleteAction.enable();
				proxyximaset.editAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				proxyximaset.deleteAction.disable();
				proxyximaset.editAction.disable();
			}
		}
	});
	/** 基本信息-数据列 */
	proxyximaset.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 110
		},
		// 'pxsid','uiid','account','name','scale','updateuserid','updatetime','updateusername'
		columns : [ proxyximaset.selModel, {
			hidden : true,
			header : '代理洗码设置ID',
			dataIndex : 'pxsid'
		}, {
			hidden : true,
			header : '代理ID',
			dataIndex : 'uiid'
		}, {
			hidden : true,
			header : '更新者ID',
			dataIndex : 'updateuserid'
		}, {
			header : '代理账号',
			dataIndex : 'account',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '代理名字',
			dataIndex : 'name',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '返水比例',
			dataIndex : 'scale',
			renderer : function(v) {
				if(v){
					if(isNaN(v)){
						return v;
					}else{
						return parseFloat(v)*10000/100+'%';
					}
				}else{
					return '';
				}
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '更新者',
			dataIndex : 'updateusername',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '更新时间',
			dataIndex : 'updatetime',
			width : 150,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		} ]
	});
	/** 新建 */
	proxyximaset.addAction = new Ext.Action({
		text : '新建',
		//text : '<fmt:message key="common.cancel"/>',
		iconCls : 'proxyximaset_add',
		handler : function() {
			proxyximaset.addWindow.setIconClass('proxyximaset_add'); // 设置窗口的样式
			proxyximaset.addWindow.setTitle('新增'); // 设置窗口的名称
			proxyximaset.addWindow.show().center(); // 显示窗口
			proxyximaset.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			proxyximaset.formPanel.find("name","account")[0].enable();
		}
	});
	/** 编辑 */
	proxyximaset.editAction = new Ext.Action({
		text : '编辑',
		iconCls : 'proxyximaset_edit',
		disabled : true,
		handler : function() {
			var record = proxyximaset.grid.getSelectionModel().getSelected();
			proxyximaset.addWindow.setIconClass('proxyximaset_edit'); // 设置窗口的样式
			proxyximaset.addWindow.setTitle('编辑'); // 设置窗口的名称
			proxyximaset.addWindow.show().center();
			proxyximaset.formPanel.getForm().reset();
			proxyximaset.formPanel.getForm().loadRecord(record);
			// 禁用代理账号
			proxyximaset.formPanel.find("name","account")[0].disable();
			// 返水比例值显示转化
			proxyximaset.scaleNumberField.setValue(proxyximaset.scaleNumberField.getValue()*100);
		}
	});
	/** 删除 */
	proxyximaset.deleteAction = new Ext.Action({
		text : '删除',
		iconCls : 'proxyximaset_delete',
		disabled : true,
		handler : function() {
			proxyximaset.delFun();
		}
	});
	/** 查询 */
	proxyximaset.searchField = new Ext.ux.form.SearchField({
		store : proxyximaset.store,
		paramName : 'account',
		emptyText : '请输入代理账号',
		style : 'margin-left: 5px;'
	});
	
	/** 顶部工具栏 */
	proxyximaset.tbar = [ proxyximaset.addAction, '-', proxyximaset.editAction, '-', proxyximaset.deleteAction
	                      , '-',proxyximaset.searchField,
	                ];
	/** 底部工具条 */
	proxyximaset.bbar = new Ext.PagingToolbar({
		pageSize : proxyximaset.pageSize,
		store : proxyximaset.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', proxyximaset.pageSizeCombo ]
	});
	/** 基本信息-表格 */
	proxyximaset.grid = new Ext.grid.EditorGridPanel({
		store : proxyximaset.store,
		colModel : proxyximaset.colModel,
		selModel : proxyximaset.selModel,
		tbar : proxyximaset.tbar,
		bbar : proxyximaset.bbar,
		autoScroll : 'auto',
		region : 'center',
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig:{},
		stripeRows : true
	});
	
	proxyximaset.scaleNumberField = new Ext.form.NumberField({
		maxLength : 8,
		allowBlank : false,
		decimalPrecision : 4,
		regex : /^\d+(\.\d+)?$/,
		regexText : '请输入有效的返水比例',
		fieldLabel : '返水比例(%)',
		name : 'scale'
	});
	/** 基本信息-详细信息的form */
	proxyximaset.formPanel = new Ext.form.FormPanel({
		autoScroll : true,
		frame : false,
		title : '代理洗码设置信息',
		bodyStyle : 'padding:10px;border:0px',
		labelwidth : 70,
		defaultType : 'textfield',
		// 'pxsid','uiid','account','name','scale','updateuserid','updatetime','updateusername'
		items : [ {
			xtype : 'hidden',
			fieldLabel : 'ID',
			name : 'pxsid'
		}, {
			xtype : 'hidden',
			fieldLabel : '代理ID',
			name : 'uiid'
		}, {
			xtype : 'hidden',
			fieldLabel : '代理名字',
			name : 'name'
		}, {
			maxLength : 64,
			allowBlank : false,
			fieldLabel : '代理账号',
			name : 'account'
		}, proxyximaset.scaleNumberField
		]
	});
	/** 编辑新建窗口 */
	proxyximaset.addWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 170,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ proxyximaset.formPanel ],
		buttons : [ {
			text : '保存',
			handler : function() {
				proxyximaset.saveFun();
			}
		}, {
			text : '重置',
			handler : function() {
				var form = proxyximaset.formPanel.getForm();
				var pxsid = form.findField("pxsid").getValue();
				form.reset();
				if (pxsid != '')
					form.findField("pxsid").setValue(pxsid);
			}
		} ]
	});
	proxyximaset.alwaysFun = function() {
		Share.resetGrid(proxyximaset.grid);
		proxyximaset.deleteAction.disable();
		proxyximaset.editAction.disable();
	};
	proxyximaset.saveFun = function() {
		var form = proxyximaset.formPanel.getForm();
		if (!form.isValid()) {
			return;
		}
		var scale = form.findField("scale").getValue();
		if(isNaN(scale) || scale<0 || scale>100){
			Ext.Msg.alert('提示', '返水比例值非法，请重新输入[合法:0~100]！');
			return;	
		}
		// 发送请求
		Share.AjaxRequest({
			url : proxyximaset.save,
			params : form.getValues(),
			callback : function(json) {
				proxyximaset.addWindow.hide();
				proxyximaset.alwaysFun();
				proxyximaset.store.reload();
			}
		});
	};
	
	proxyximaset.delFun = function() {
		var record = proxyximaset.grid.getSelectionModel().getSelected();
		Ext.Msg.confirm('提示', '确定要删除这条记录吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : proxyximaset.del + record.data.pxsid + ".do",
					callback : function(json) {
						proxyximaset.alwaysFun();
						proxyximaset.store.reload();
					}
				});
			}
		});
	};

	proxyximaset.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'border',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
		items : [ proxyximaset.grid ]
	});
</script>
