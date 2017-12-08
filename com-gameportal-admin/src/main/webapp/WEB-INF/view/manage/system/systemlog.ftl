<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.systemlog"); // 自定义一个命名空间
systemlog = Ext.market.systemlog; // 定义命名空间的别名
systemlog = {
	all : '/manage/system/systemlog/querySystemLog.do',// 加载所有
	pageSize : 20
};


/** 改变页的combo */
systemlog.pageSizeCombo = new Share.pageSizeCombo({
			value : '20',
			listeners : {
				select : function(comboBox) {
					systemlog.pageSize = parseInt(comboBox.getValue());
					systemlog.bbar.pageSize = parseInt(comboBox.getValue());
					systemlog.store.baseParams.limit = systemlog.pageSize;
					systemlog.store.baseParams.start = 0;
					systemlog.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
systemlog.pageSize = parseInt(systemlog.pageSizeCombo.getValue());
/** 基本信息-数据源 */
systemlog.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				account:'',
				startDate:'',
				endDate:'',
				start : 0,
				limit : systemlog.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : systemlog.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['logid', 'loginaccount', 'loginname', 'loginrole',"loginip",'loginmac','logintime','loginsource']),
			listeners : {
				'load' : function(store, records, options) {
				//	memberGrade.alwaysFun();
				}
			}
		});
		
/** 基本信息-选择模式 */
systemlog.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				systemlog.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
systemlog.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [systemlog.selModel, {
						hidden : true,
						header : 'ID',
						dataIndex : 'logid'
					},{
						header : '登录账号',
						dataIndex : 'loginaccount',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '真实姓名',
						dataIndex : 'loginname',
						width : 100,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '登录角色',
						dataIndex : 'loginrole',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '客户端IP',
						dataIndex : 'loginip',
						width : 120,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '客户端MAC地址',
						dataIndex : 'loginmac',
						width : 120,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '登录时间',
						dataIndex : 'logintime',
						width : 140,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '登录设备',
						dataIndex : 'loginsource',
						width : 830,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}]
		});
		

systemlog.searchAction = new Ext.Action({
			text : '查询',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				systemlog.store.baseParams.account = $("#systemloginAcconut").val();
				systemlog.store.baseParams.startDate = $("#systemLoginStartDate").val();
				systemlog.store.baseParams.endDate = $("#systemLoginEndDate").val();
				systemlog.store.reload();
			}
		});

/** 顶部工具栏 */
systemlog.tbar = ['登录账号：',{id:'systemloginAcconut',xtype:'textfield',width:120}, '&nbsp;','-',
		'日期：',{ id:'systemLoginStartDate',xtype:'datetimefield',format:'Y-m-d H:i:s',value:new Date().format("Y-m-d H:i:s")},'&nbsp;','-',
		"到",'&nbsp;',{ id:'systemLoginEndDate',xtype:'datetimefield',format:'Y-m-d H:i:s',value:new Date().format("Y-m-d H:i:s")},'&nbsp;','-',
		systemlog.searchAction];
/** 底部工具条 */
systemlog.bbar = new Ext.PagingToolbar({
			pageSize : systemlog.pageSize,
			store : systemlog.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', systemlog.pageSizeCombo]
		});
/** 基本信息-表格 */
systemlog.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : systemlog.store,
			colModel : systemlog.colModel,
			selModel : systemlog.selModel,
			tbar : systemlog.tbar,
			bbar : systemlog.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'memberGradeDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});
			
systemlog.returnscaleNumberField = new Ext.form.NumberField({
		maxLength : 8,
		allowBlank : false,
		decimalPrecision : 4,
		fieldLabel : '占成比例',
		name : 'returnscale',
		anchor : '99%'
	});

systemlog.ximascaleNumberField = new Ext.form.NumberField({
		maxLength : 8,
		allowBlank : false,
		decimalPrecision : 4,
		fieldLabel : '洗码比例',
		name : 'ximascale',
		anchor : '99%'
	});
	
/** 基本信息-详细信息的form */
systemlog.formPanel = new Ext.form.FormPanel({
			autoScroll : false,
			border: false,
        	style: 'border-bottom:0px;',
        	bodyStyle: 'padding:10px;background-color:transparent;',
			labelwidth : 50,
			defaultType : 'textfield',
			items : [{
						xtype : 'hidden',
						fieldLabel : 'ID',
						name : 'pyid',
						anchor : '99%'
					},{
						fieldLabel : '代理账号',
						maxLength : 32,
						allowBlank : false,
						name : 'account',
						anchor : '99%'
					},systemlog.returnscaleNumberField,systemlog.ximascaleNumberField]
		});
	
systemlog.alwaysFun = function() {
	Share.resetGrid(systemlog.grid);
};
  
systemlog.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [systemlog.grid]
		});

</script>