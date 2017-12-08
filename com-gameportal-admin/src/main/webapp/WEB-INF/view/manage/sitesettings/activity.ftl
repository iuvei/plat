<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.siteactivity"); // 自定义一个命名空间
siteactivity = Ext.market.siteactivity; // 定义命名空间的别名
siteactivity = {
	all : '/manage/sitesettings/querysiteactivity.do',// 加载所有
	save : "/manage/sitesettings/save.do",//保存
	del : "/manage/sitesettings/delsiteactivity/",//删除
	bind : "/manage/sitesettings/bindsiteactivity/",
	pageSize : 20, // 每页显示的记录数
	hdtype:eval('(${fields.hdtype})')
};


/** 改变页的combo */
siteactivity.pageSizeCombo = new Share.pageSizeCombo({
			value : '20',
			listeners : {
				select : function(comboBox) {
					siteactivity.pageSize = parseInt(comboBox.getValue());
					siteactivity.bbar.pageSize = parseInt(comboBox.getValue());
					siteactivity.store.baseParams.limit = siteactivity.pageSize;
					siteactivity.store.baseParams.start = 0;
					siteactivity.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
siteactivity.pageSize = parseInt(siteactivity.pageSizeCombo.getValue());
/** 基本信息-数据源 */
siteactivity.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : siteactivity.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : siteactivity.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['aid', 'hdtype', 'hdnumber', 'hdtext',"hdscale",'multiple','isxima','notecontext','status','uptime','maxmoney','minmoney','hdrule','acgroup','rewmoney']),
			listeners : {
				'load' : function(store, records, options) {
				//	memberGrade.alwaysFun();
				}
			}
		});
		
/** 基本信息-选择模式 */
siteactivity.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				siteactivity.deleteAction.enable();
				siteactivity.editAction.enable();
				
				if(record.data.status == '0'){
					siteactivity.removeAction.enable();
					siteactivity.bindAction.disable();
				}else{
					siteactivity.bindAction.enable();
					siteactivity.removeAction.disable();
				}
				
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				siteactivity.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
siteactivity.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [siteactivity.selModel, {
						hidden : true,
						header : 'ID',
						dataIndex : 'aid'
					},{
						hidden : true,
						header : '优惠类型',
						dataIndex : 'hdtype',
						width : 80,
						/**
						renderer : function(v) {
							return Share.map(v, siteactivity.hdtype, '');
						},
						*/
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '优惠代码',
						dataIndex : 'hdnumber',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '优惠标题',
						dataIndex : 'hdtext',
						width : 160,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '优惠比例',
						dataIndex : 'hdscale',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '流水倍数',
						dataIndex : 'multiple',
						width : 80,
						renderer : function(v) {
							return v+'倍';
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '是否洗码',
						dataIndex : 'isxima',
						width : 60,
						renderer : function(v) {
							if(v == 1){
								return '<span style="color:green;">可洗码</span>';
							}else{
								return '<span style="color:red;">不洗码</span>';
							}
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '最大金额',
						dataIndex : 'maxmoney',
						width : 80,
						renderer : function(v) {
							return '<span style="color:green;">'+v+'</span>';
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '最小金额',
						dataIndex : 'minmoney',
						width : 80,
						renderer : function(v) {
							return '<span style="color:green;">'+v+'</span>';
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '优惠备注',
						dataIndex : 'notecontext',
						width : 140,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '活动规则',
						dataIndex : 'hdrule',
						width : 140,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '活动分组',
						dataIndex : 'acgroup',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '赠送金额',
						dataIndex : 'rewmoney',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '开始时间',
						dataIndex : 'uptime',
						width : 140,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						}),
						renderer : function(v) {
							return v==null?'':new Date(v).format('Y-m-d H:i:s');
						}
					}, {
						header : '状态',
						dataIndex : 'status',
						width : 80,
						renderer : function(v) {
							if(v == 1){
								return '<span style="color:green;">已启用</span>';
							}else{
								return '<span style="color:red;">未启用</span>';
							}
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}]
		});
		
/** 新建 */
siteactivity.addAction = new Ext.Action({
			text : '新建',
			iconCls : 'Add',
			handler : function() {
				siteactivity.addWindow.setIconClass('Applicationadd'); // 设置窗口的样式
				siteactivity.addWindow.setTitle('新建优惠活动'); // 设置窗口的名称
				siteactivity.addWindow.show().center(); // 显示窗口
				var form = siteactivity.formPanel.getForm();
				form.reset();
				form.findField("hdnumber").setReadOnly(false);
			}
		});
/** 编辑 */
siteactivity.editAction = new Ext.Action({
			text : '编辑',
			iconCls : 'Applicationedit',
			disabled : true,
			handler : function() {
				var record = siteactivity.grid.getSelectionModel().getSelected();
				siteactivity.addWindow.setIconClass('Applicationedit'); // 设置窗口的样式
				siteactivity.addWindow.setTitle('编辑优惠活动'); // 设置窗口的名称
				siteactivity.addWindow.show().center();
				var form = siteactivity.formPanel.getForm();
				form.reset();
				form.loadRecord(record);
				$("#uptime").val(Ext.util.Format.date(record.data.uptime, "Y-m-d H:i:s"));
				form.findField("hdnumber").setReadOnly(true);
				
			}
		});
		
/** 删除 */
siteactivity.deleteAction = new Ext.Action({
			text : '删除',
			iconCls : 'Delete',
			disabled : true,
			handler : function() {
				siteactivity.delFun();
			}
		});
/***/
siteactivity.bindAction = new Ext.Action({
			text : '禁用',
			iconCls : 'Lockkey',
			disabled : true,
			handler : function() {
				siteactivity.bindFun(1);
			}
		});
		
siteactivity.removeAction = new Ext.Action({
			text : '启用',
			iconCls : 'Lockopen',
			disabled : true,
			handler : function() {
				siteactivity.bindFun(0);
			}
		});

siteactivity.searchField = new Ext.ux.form.SearchField({
			store : siteactivity.store,
			paramName : 'hdnumber',
			emptyText : '请输优惠活动代码',
			style : 'margin-left: 5px;'
		});
/** 顶部工具栏 */
siteactivity.tbar = [siteactivity.addAction, '-', siteactivity.editAction,'-', siteactivity.searchField];
/** 底部工具条 */
siteactivity.bbar = new Ext.PagingToolbar({
			pageSize : siteactivity.pageSize,
			store : siteactivity.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', siteactivity.pageSizeCombo]
		});
/** 基本信息-表格 */
siteactivity.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : siteactivity.store,
			colModel : siteactivity.colModel,
			selModel : siteactivity.selModel,
			tbar : siteactivity.tbar,
			bbar : siteactivity.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'memberGradeDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});
	
	//设置基本的是否选项
siteactivity.baseSelectStore = new Ext.data.SimpleStore({  
    fields : ['key', 'value'],  
    data : [['可洗码', '1'], ['不洗码', '0']]  
});
siteactivity.isximaCombo = new Ext.form.ComboBox({  
    fieldLabel : '是否洗码',  
    id : 'isxima',  
    store : siteactivity.baseSelectStore,  
    displayField : 'key',  
    valueField : 'value',  
    mode : 'local',  
    typeAhead : true,  
    forceSelection : true,  
    triggerAction : 'all',  
    anchor : '99%',  
    selectOnFocus : true,
    allowBlank : false,
    hiddenName : 'isxima'
});



siteactivity.astatuSelectStore = new Ext.data.SimpleStore({  
    fields : ['key', 'value'],  
    data : [['正常', '1'], ['未启用', '0']]  
});
siteactivity.astatusCombo = new Ext.form.ComboBox({  
    fieldLabel : '状态',  
    id : 'status',  
    store : siteactivity.astatuSelectStore,  
    displayField : 'key',  
    valueField : 'value',  
    mode : 'local',  
    typeAhead : true,  
    forceSelection : true,  
    triggerAction : 'all',  
    anchor : '99%',  
    selectOnFocus : true,
    allowBlank : false,
    hiddenName : 'status'
});


siteactivity.yhtypeCombo = new Ext.form.ComboBox({  
    fieldLabel : '优惠类型',  
    id : 'hdtype',  
    hiddenName : 'hdtype',
   	triggerAction : 'all',
	mode : 'local',
	store : new Ext.data.ArrayStore({
		fields : ['v', 't'],
		data : Share.map2Ary(siteactivity.hdtype)
	}),
	valueField : 'v',
	displayField : 't',
	allowBlank : false,
	editable : false,
	anchor : '99%'
    
});
	
/** 基本信息-详细信息的form */
siteactivity.formPanel = new Ext.form.FormPanel({
			autoScroll : false,
			border: false,
        	style: 'border-bottom:0px;',
        	bodyStyle: 'padding:10px;background-color:transparent;',
			labelwidth : 50,
			defaultType : 'textfield',
			items : [{
						xtype : 'hidden',
						fieldLabel : 'ID',
						name : 'aid',
						anchor : '99%'
					},{
						fieldLabel : '优惠代码',
						maxLength : 32,
						allowBlank : false,
						name : 'hdnumber',
						anchor : '99%'
					},siteactivity.yhtypeCombo ,{
						fieldLabel : '优惠标题',
						maxLength : 32,
						allowBlank : false,
						name : 'hdtext',
						anchor : '99%'
					},{
						fieldLabel : '优惠比例',
						maxLength : 32,
						allowBlank : false,
						name : 'hdscale',
						anchor : '99%'
					},
					{
						fieldLabel : '流水倍数',
						maxLength : 32,
						allowBlank : false,
						name : 'multiple',
						anchor : '99%'
					},siteactivity.isximaCombo,siteactivity.astatusCombo,
					{
						fieldLabel : '最大金额',
						maxLength : 32,
						allowBlank : false,
						name : 'maxmoney',
						anchor : '99%'
					},{
						fieldLabel : '最小金额',
						maxLength : 32,
						allowBlank : false,
						name : 'minmoney',
						anchor : '99%'
					},{
						fieldLabel : '优惠规则',
						allowBlank : false,
						maxLength : 100,
						xtype : 'textarea',
						name : 'hdrule',
						anchor : '99%'
					},{
						fieldLabel : '优惠备注',
						allowBlank : false,
						maxLength : 100,
						xtype : 'textarea',
						name : 'notecontext',
						anchor : '99%'
					},{
						fieldLabel : '活动分组',
						maxLength : 32,
						allowBlank : false,
						name : 'acgroup',
						anchor : '99%'
					},{
						fieldLabel : '赠送金额',
						allowBlank : false,
						maxLength : 200,
						name : 'rewmoney',
						anchor : '99%'
					},{
						fieldLabel : '开始时间',
						allowBlank : false,
						anchor : '99%',
						name : 'uptime',
						id:'uptime',
						xtype:'datetimefield'
					}]
		});

		

			
/** 编辑新建窗口 */
siteactivity.addWindow = new Ext.Window({
			layout : 'fit',
			width : 500,
			height : 530,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [siteactivity.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							siteactivity.saveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = siteactivity.formPanel.getForm();
							var id = form.findField("pyid").getValue();
							form.reset();
							if (id != '')
								form.findField("pyid").setValue(id);
						}
					}]
		});
	
siteactivity.alwaysFun = function() {
	Share.resetGrid(siteactivity.grid);
	siteactivity.deleteAction.disable();
	siteactivity.editAction.disable();
	siteactivity.bindAction.disable();
	siteactivity.removeAction.disable();
};
siteactivity.saveFun = function() {
	var form = siteactivity.formPanel.getForm();

	if(!form.isValid()){
 		return;
 	}
	// 发送请求
	Share.AjaxRequest({
				url : siteactivity.save,
				params : form.getValues(),
				callback : function(json) {
					if (json.success==false){
					     Ext.MessageBox.alert('Status', json.msg, showResult);
						return;
					}else{
					    siteactivity.addWindow.hide();
						siteactivity.alwaysFun();
						siteactivity.store.reload();
					}
				}
			});
};

siteactivity.delFun = function() {
	var record = siteactivity.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '您确定要删除选中会员洗码数据吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : siteactivity.del + record.data.xmid +".do",
								callback : function(json) {
									siteactivity.alwaysFun();
									siteactivity.store.reload();
								}
							});
				}
			});
};

siteactivity.bindFun = function(s){
	var record = siteactivity.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '确定要修改会员洗码数据状态吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : siteactivity.bind + record.data.xmid+"/"+s+".do",
								callback : function(json) {
									siteactivity.alwaysFun();
									siteactivity.store.reload();
								}
							});
				}
			});
}
  
siteactivity.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [siteactivity.grid]
		});

</script>