<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.fcRecord"); // 自定义一个命名空间
fcRecord = Ext.market.fcRecord; // 定义命名空间的别名
fcRecord ={
	all:'/manage/memberinfo/dx/queryDailyFcIndex.do',
	save:'/manage/memberinfo/dx/updateDailyFcRecord.do',
	callUrl:'/manage/memberinfo/dx/call/', // 拨打电话
	pageSize : 30
};

/** 改变页的combo */
fcRecord.pageSizeCombo = new Share.pageSizeCombo({
	value : '30',
	listeners : {
		select : function(comboBox) {
			fcRecord.pageSize = parseInt(comboBox.getValue());
			fcRecord.bbar.pageSize = parseInt(comboBox.getValue());
			fcRecord.store.baseParams.limit = fcRecord.pageSize;
			fcRecord.store.baseParams.start = 0;
			fcRecord.store.load();
		}
	}
});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
fcRecord.pageSize = parseInt(fcRecord.pageSizeCombo.getValue());

/** 基本信息-数据源 */
fcRecord.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : fcRecord.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : fcRecord.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['rid','account','username','phone','pname','money','paytime','visitorId','visitor','vistortime','remark']),
			listeners : {
				'load' : function(store, records, options) {}
			}
		});
		
/** 基本信息-选择模式 */
fcRecord.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				fcRecord.callAction.enable();
				fcRecord.remarkAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				fcRecord.callAction.disable();
				fcRecord.remarkAction.disable();
			}
		}
	});
	
/** 基本信息-数据列 */
fcRecord.colModel = new Ext.grid.ColumnModel({
	defaults : {
		sortable : true,
		width : 140
	},
	columns : [fcRecord.selModel, {
				hidden : true,
				header : 'ID',
				dataIndex : 'rid'
			}, {
				header : '帐号',
				dataIndex : 'account',
				width : 95,
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			}, {
				header : '姓名',
				dataIndex : 'username',
				width : 84,
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			}, {
				header : '电话',
				dataIndex : 'phone',
				width : 100,
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : '所属代理',
				dataIndex : 'pname',
				width : 80,
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : '充值金额',
				dataIndex : 'money',
				width : 60
			}, {
				header : '充值时间',
				dataIndex : 'paytime',
				width : 130,
				renderer : function(v) {
					return v == null ? "" :new Date(v).format('Y-m-d H:i:s');
				},
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : '回访人ID',
				hidden:true,
				dataIndex : 'visitorId',
				width : 80,
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : '回访人',
				dataIndex : 'visitor',
				width : 80,
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			},{
				header : '回访时间',
				dataIndex : 'vistortime',
				width : 130,
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				}),
				renderer : function(v) {
					return v==null ? "" :new Date(v).format('Y-m-d H:i:s');
				}
			},{
				header : '备注',
				dataIndex : 'remark',
				width:300,
				editor : new Ext.form.TextField({
					style: 'border:0px;'
				})
			}]
});

// 拨打电话
fcRecord.callFun =function(){
	var record = fcRecord.grid.getSelectionModel().getSelected();
	Share.AjaxRequest({
		url : fcRecord.callUrl +"0/"+ record.data.phone +".do",
		callback : function(json) {
			if(!json.success){
				 Ext.MessageBox.alert(json.msg);
			}
		}
	});
};

fcRecord.startDateField = new Ext.form.DateField({
		id:'startDate',
    	showToday:true,
    	format:'Y-m-d',
    	invalidText:'日期输入非法',
    	allowBlank : true,
    	value:"${defaultDate}",
    	width:100 
});
fcRecord.endDateField = new Ext.form.DateField({
		id:'endDate',
    	showToday:true,
    	format:'Y-m-d',
    	invalidText:'日期输入非法',
    	allowBlank : true,
    	value:"${defaultDate}",
    	width:100 
});
/** 查询按钮 */
fcRecord.rcSearchAction = new Ext.Action({
	text : '查询',
		iconCls : 'Zoom',
		disabled : false,
		handler : function() {
			fcRecord.store.baseParams.startDate = $("#startDate").val();
			fcRecord.store.baseParams.endDate = $("#endDate").val();
			fcRecord.store.baseParams.account=$("#account").val();
			fcRecord.store.baseParams.pname = $("#pname").val();
			fcRecord.store.reload();
		}
});

/** 拨打电话 */
fcRecord.callAction = new Ext.Action({
	text : '拨打电话',
	iconCls : 'Anchor',
	disabled : true,
	handler : function() {
		fcRecord.callFun();
	}
});

/** 备注 */
fcRecord.formPanelRemark = new Ext.form.FormPanel({
	region : 'center',
	autoScroll : false,
	frame : false,
	border: false,
	style: 'border-bottom:0px;',
	bodyStyle: 'padding:10px;background-color:transparent;',
	labelwidth : 70,
	defaultType : 'textfield',
	items : [ {
		xtype : 'hidden',
		fieldLabel : 'ID',
		name : 'rid'
	},{
		fieldLabel : '会员账号',
		name : 'account',
		anchor : '99%',
		allowBlank : false
		
	},{
		fieldLabel : '电销备注',
		maxLength : 1000,
		xtype : 'textarea',
		name : 'remark',
		anchor : '99%'
	}]
});

/** 编辑新建窗口 */
fcRecord.addWindowRemark = new Ext.Window({
	layout : 'fit',
	width : 500,
	height : 200,
	closeAction : 'hide',
	plain : true,
	modal : true,
	resizable : true,
	items : [ fcRecord.formPanelRemark ],
	buttons : [ {
		text : '保存',
		handler : function() {
			fcRecord.saveFun();
		}
	}, {
		text : '取消',
		handler : function() {
			fcRecord.addWindowRemark.hide();
		}
	} ]
});

fcRecord.remarkAction = new Ext.Action({
			text : '备注',
			iconCls : 'Layoutedit',
			disabled : true,
			handler : function() {
				var record = fcRecord.grid.getSelectionModel().getSelected();
				fcRecord.addWindowRemark.setIconClass('Layoutedit'); // 设置窗口的样式
				fcRecord.addWindowRemark.setTitle('备注回访信息'); // 设置窗口的名称
				fcRecord.addWindowRemark.show().center();
				fcRecord.formPanelRemark.getForm().reset();
				fcRecord.formPanelRemark.getForm().loadRecord(record);
				var form=fcRecord.formPanelRemark.getForm();
				form.findField("account").setReadOnly(true);
			}
});

fcRecord.saveFun = function(){
	var form = fcRecord.formPanelRemark.getForm();
	if(!form.isValid()){
 		return;
 	}
	// 发送请求
	Share.AjaxRequest({
		url : fcRecord.save,
		params : form.getValues(),
		callback : function(json) {
			if (!json.success){
			    Ext.MessageBox.alert(json.msg);
				return;
			}else{
			    fcRecord.addWindowRemark.hide();
				fcRecord.refreshFun();
			}
		}
	});
}

/** 顶部工具栏 */
fcRecord.tbar = [/**fcRecord.callAction,'-',*/fcRecord.remarkAction,'-','会员账号:',{id:'account',xtype:'textfield',width:100},'代理账号:',{id:'pname',xtype:'textfield',width:100},
	                 '-','&nbsp;','存款日期:',fcRecord.startDateField,'~',fcRecord.endDateField,
	                 '-','&nbsp;',fcRecord.rcSearchAction];
/** 底部工具条 */
fcRecord.bbar = new Ext.PagingToolbar({
	pageSize : fcRecord.pageSize,
	store : fcRecord.store,
	displayInfo : true,
	items : ['-', '&nbsp;', fcRecord.pageSizeCombo]
});

fcRecord.grid = new Ext.grid.EditorGridPanel({
			store : fcRecord.store,
			colModel : fcRecord.colModel,
			selModel : fcRecord.selModel,
			tbar : fcRecord.tbar,
			bbar : fcRecord.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});
		
fcRecord.refreshFun = function(){
	Share.resetGrid(fcRecord.grid);
	fcRecord.callAction.disable();
	fcRecord.remarkAction.disable();
	fcRecord.store.reload();
}
fcRecord.myPanel = new Ext.Panel({
	id : '${id}' + '_panel',
	renderTo : '${id}',
	layout : 'border',
	boder : false,
	height : index.tabPanel.getInnerHeight() - 1,
	items : [fcRecord.grid]
});
</script>