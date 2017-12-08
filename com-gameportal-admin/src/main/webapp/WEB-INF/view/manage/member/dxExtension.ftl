<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.dxList"); // 自定义一个命名空间
dxList = Ext.market.dxList; // 定义命名空间的别名
dxList = {
	all : '/manage/memberinfo/dxList.do',// 加载所有
	save: '/manage/memberinfo/saveExtension.do',
	pageSize : 30
};

/** 改变页的combo */
dxList.pageSizeCombo = new Share.pageSizeCombo({
	value : '30',
	listeners : {
		select : function(comboBox) {
			dxList.pageSize = parseInt(comboBox.getValue());
			dxList.bbar.pageSize = parseInt(comboBox.getValue());
			dxList.store.baseParams.limit = dxList.pageSize;
			dxList.store.baseParams.start = 0;
			dxList.store.load();
		}
	}
});

// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
dxList.pageSize = parseInt(dxList.pageSizeCombo.getValue());

/** 基本信息-数据源 */
dxList.store = new Ext.data.Store({
		autoLoad : true,
		remoteSort : true,
		baseParams : {
			start : 0,
			limit : dxList.pageSize
		},
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : dxList.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'userId','account','truename','remark']),
		listeners : {
			'load' : function(store, records, options) {
				Share.resetGrid(dxList.grid);
			}
		}
});

/** 基本信息-选择模式 */
dxList.selModel = new Ext.grid.CheckboxSelectionModel({
	singleSelect : true,
	listeners : {
		'rowselect' : function(selectionModel, rowIndex, record) {
			dxList.setExtensionAction.enable();
		},
		'rowdeselect' : function(selectionModel, rowIndex, record) {
			dxList.setExtensionAction.disable();
		}
	}
});

/** 基本信息-数据列 */
dxList.colModel = new Ext.grid.ColumnModel({
	defaults : {
		sortable : true,
		width : 140
	},
	columns : [dxList.selModel, {
				hidden : true,
				header : 'ID',
				dataIndex : 'userId'
			}, {
				header : '会员账号',
				dataIndex : 'account',
				width : 120,
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			}, {
				header : '用户姓名',
				dataIndex : 'truename',
				width : 120,
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			}, {
				header : '分机号码',
				dataIndex : 'remark',
				width : 80,
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
		  }]
});

/** 查询 */
dxList.searchField = new Ext.ux.form.SearchField({
	store : dxList.store,
	paramName : 'account',
	emptyText : '请输入会员账号',
	style : 'margin-left: 5px;'
});

dxList.setExtensionAction = new Ext.Action({
		text : '设置分机号码',
		iconCls : 'Applicationformedit',
		disabled : true,
		handler : function() {
			var record = dxList.grid.getSelectionModel().getSelected();
			dxList.addWindow.setIconClass('Applicationedit'); // 设置窗口的样式
			dxList.addWindow.setTitle('设置分机号码'); // 设置窗口的名称
			dxList.addWindow.show().center();
			dxList.formPanel.getForm().reset();
			dxList.formPanel.getForm().loadRecord(record);
		}
});

/** 基本信息-详细信息的form */
dxList.formPanel = new Ext.form.FormPanel({
	frame : false,
	bodyStyle : 'padding:10px;border:0px',
	labelwidth : 70,
	defaultType : 'textfield',
	items : [{
				xtype : 'hidden',
				fieldLabel : 'ID',
				name : 'userId',
				anchor : '99%'
			},{
				fieldLabel : '会员账号',
				maxLength : 64,
				allowBlank : false,
				name : 'account',
				anchor : '99%'
			}, {
				fieldLabel : '真实姓名',
				maxLength : 64,
				allowBlank : false,
				name : 'truename',
				anchor : '99%'
			}, {
				fieldLabel : '分机号码',
				maxLength : 64,
				allowBlank : false,
				name : 'remark',
				anchor : '99%'
			}]
});

/** 编辑窗口 */
dxList.addWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 250,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [dxList.formPanel],
		buttons : [{
					text : '保存',
					handler : function() {
						dxList.saveExstensionFun();
					}
				}, {
					text : '重置',
					handler : function() {
						var form = dxList.formPanel.getForm();
						var id = dxList.findField("userId").getValue();
						form.reset();
						if (id != '')
							form.findField("userId").setValue(id);
					}
				}]
});

dxList.saveExstensionFun = function() {
	var form = dxList.formPanel.getForm();
	if (!form.isValid()) {
		return;
	}
	// 发送请求,保存数据。
	Share.AjaxRequest({
		url : dxList.save,
		params : form.getValues(),
		callback : function(json) {
			dxList.addWindow.hide();
			dxList.store.reload();
		}
	});
};

dxList.tbar = [dxList.setExtensionAction,'-',dxList.searchField];

/** 底部工具条 */
dxList.bbar = new Ext.PagingToolbar({
	pageSize : dxList.pageSize,
	store : dxList.store,
	displayInfo : true,
	items : ['-', '&nbsp;', dxList.pageSizeCombo]
});

/** 基本信息-表格 */
dxList.grid = new Ext.grid.EditorGridPanel({
	store : dxList.store,
	colModel : dxList.colModel,
	selModel : dxList.selModel,
	tbar : dxList.tbar,
	bbar : dxList.bbar,
	autoScroll : 'auto',
	region : 'center',
	loadMask : true,
	stripeRows : true,
	listeners : {},
	viewConfig : {}
});

dxList.myPanel = new Ext.Panel({
	id : '${id}' + '_panel',
	renderTo : '${id}',
	layout : 'border',
	boder : false,
	height : index.tabPanel.getInnerHeight() - 1,
	items : [dxList.grid]
});
</script>