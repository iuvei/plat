<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.loginLog"); // 自定义一个命名空间
loginLog = Ext.market.loginLog; // 定义命名空间的别名
loginLog = {
	all : '/manage/memberinfo/queryUserloginLog.do',// 加载所有
	pageSize : 30, // 每页显示的记录数
	memGrade:eval('(${fields.memGrade})')
};


/** 改变页的combo */
loginLog.pageSizeCombo = new Share.pageSizeCombo({
			value : '30',
			listeners : {
				select : function(comboBox) {
					loginLog.pageSize = parseInt(comboBox.getValue());
					loginLog.bbar.pageSize = parseInt(comboBox.getValue());
					loginLog.store.baseParams.limit = loginLog.pageSize;
					loginLog.store.baseParams.start = 0;
					loginLog.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
loginLog.pageSize = parseInt(loginLog.pageSizeCombo.getValue());
/** 基本信息-数据源 */
loginLog.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : loginLog.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : loginLog.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['logid', 'uiid', 'account','truename', 'logintime','loginip','iparea','loginsource','logindevice']),
			listeners : {
				'load' : function(store, records, options) {
				//	loginLog.alwaysFun();
				}
			}
		});
		
/** 基本信息-选择模式 */
loginLog.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				
				
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				loginLog.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
loginLog.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [loginLog.selModel, {
						hidden : true,
						header : 'ID',
						dataIndex : 'logid'
					}, {
						hidden : true,
						header : 'uiid',
						dataIndex : 'uiid'
					}, {
						header : '账号',
						dataIndex : 'account',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '姓名',
						dataIndex : 'truename',
						width : 80,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '登录时间',
						dataIndex : 'logintime',
						width : 130,
						renderer : function(v) {
							return v==null?'':new Date(v).format('Y-m-d H:i:s');
						}
					}, {
						header : '客户端IP',
						dataIndex : 'loginip',
						width : 120,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '所属区域',
						dataIndex : 'iparea',
						width : 120,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '登录来源',
						dataIndex : 'loginsource',
						width : 300,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '登录设备',
						dataIndex : 'logindevice',
						width : 600,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}]
		});
		


/** 查询 */
loginLog.searchField = new Ext.Action({
		text : '查询',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				loginLog.store.load({params: loginLog.searchParams()});
			}
	});
	
	loginLog.store.on('beforeload',function(store, options){
	    loginLog.store.baseParams = loginLog.searchParams();
	});

loginLog.searchParams = function(){
	var obj = {};
	var account=$("#account").val(); //会员账号
	var loginip=$("#loginip").val(); //登录IP 
	var iparea = $("#iparea").prev().val(); //所属地区
	obj.account = account;
	obj.loginip = loginip;
	obj.iparea=iparea;
    return obj;
}
	
/** 顶部工具栏 */
loginLog.tbar = ['会员账号:',{id:'account',xtype:'textfield',width:100},'-','&nbsp;',
'客户端IP:',{id:'loginip',xtype:'textfield',width:130},'-','&nbsp;',
'所属地区:',
                       		new Ext.form.ComboBox({
								
								hiddenName :'iparea',
								id : 'iparea',
								triggerAction : 'all',
								mode : 'local',
								store : new Ext.data.ArrayStore({
											fields : ['v', 't'],
											data : [['', '全部'], ['1', '菲律宾']]
										}),
								valueField : 'v',
								displayField : 't',
								allowBlank : true,
								editable : true,
								value:'',
								width:80
								
			
							}),
loginLog.searchField];
/** 底部工具条 */
loginLog.bbar = new Ext.PagingToolbar({
			pageSize : loginLog.pageSize,
			store : loginLog.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', loginLog.pageSizeCombo]
		});
		
		
/** 基本信息-表格 */
loginLog.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : loginLog.store,
			colModel : loginLog.colModel,
			selModel : loginLog.selModel,
			tbar : loginLog.tbar,
			bbar : loginLog.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'loginLogDesc',
			stripeRows : true,
			listeners : {
			
			},
			viewConfig : {}
		});


	

	
loginLog.alwaysFun = function() {
	Share.resetGrid(loginLog.grid);

	
};



  
loginLog.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [loginLog.grid]
		});

</script>