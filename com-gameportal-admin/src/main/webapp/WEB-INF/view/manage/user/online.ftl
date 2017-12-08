<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	Ext.ns("Ext.market.online"); // 自定义一个命名空间
	online = Ext.market.online; // 定义命名空间的别名
	online = {
		all : '/manage/memberinfo/queryOnline.do', // 
		ti : "/manage/memberinfo/tiMemberInfo.do",//踢出会员
		pageSize : 1000,// 每页显示的记录数
		STATUSMAP : eval('(${statusMap})')//注意括号
	};
	/** 改变页的combo*/
	online.pageSizeCombo = new Share.pageSizeCombo({
		value : '1000',
		listeners : {
			select : function(comboBox) {
				online.pageSize  = parseInt(comboBox.getValue());
				online.bbar.pageSize  = parseInt(comboBox.getValue());
				online.store.baseParams.limit = online.pageSize;
				online.store.baseParams.start = 0;
				online.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	online.pageSize = parseInt(online.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	online.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:true,
		baseParams : {
			start : 0,
			limit : online.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : online.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'key','account','uname','loginip','loginurl','loginClient','updateDate']),
		listeners : {
			'load' : function(store, records, options) {
				online.alwaysFun();
			}
		}
	});
	//online.store.load(); 
	/** 基本信息-选择模式 */
	online.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				online.editAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				online.editAction.disable();
			}
		}
	});
	/** 基本信息-数据列 */
	online.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 110
		},
		//'ccid','ccholder','bankname','ccno','bankaddr','remarks','status','createuserid','createusername','createtime','updateuserid','updateusername','updatetime'
		columns : [ online.selModel, {
			hidden : true,
			header : '用户ID',
			dataIndex : 'uiid'
		},{
			header : 'SessionKey',
			dataIndex : 'key',
			width : 150,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '用户账号',
			dataIndex : 'account',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '用户姓名',
			dataIndex : 'uname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '登录URL地址',
			width : 300,
			dataIndex : 'loginurl',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '登录设备',
			width : 400,
			dataIndex : 'loginClient',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '登录IP',
			dataIndex : 'loginip',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '登录时间',
			dataIndex : 'updateDate',
			width : 130,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}]
	});

	/** 编辑 */
	online.editAction = new Ext.Action({
		text : '踢出',
		iconCls : 'online_edit',
		disabled : true,
		handler : function() {
			online.tiFun();
		}
	});
	
	/** 查询 */
	online.searchField = new Ext.ux.form.SearchField({
		store : online.store,
		paramName : 'account',
		emptyText : '请输入会员账号',
		style : 'margin-left: 5px;'
	});
	/** 顶部工具栏 */
	online.tbar = [ online.editAction, '-',online.searchField];
	/** 底部工具条 */
	online.bbar = new Ext.PagingToolbar({
		pageSize : online.pageSize,
		store : online.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', online.pageSizeCombo ]
	});
	/** 基本信息-表格 */
	online.grid = new Ext.grid.EditorGridPanel({
		store : online.store,
		colModel : online.colModel,
		selModel : online.selModel,
		tbar : online.tbar,
		bbar : online.bbar,
		autoScroll : 'auto',
		region : 'center',
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig:{},
		stripeRows : true
	});
	
	online.alwaysFun = function() {
		Share.resetGrid(online.grid);
		online.editAction.disable();
	};
	   //踢出在线会员的方法
  online.tiFun=function(){
     var record = online.grid.getSelectionModel().getSelected();
     if(record){
     	Ext.Msg.confirm('提示', '你真的要踢出选中的会员吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : online.ti,
								params : {
									ukey:record.data.key
								},
								callback : function(json) {
									if (json.success==false){
				       						Ext.MessageBox.alert('Status', json.msg, showResult);
									}else{
				                        online.store.remove(record);//删除数据
					  					online.alwaysFun();
									}
								}
							});
				}
			});
     }
  
  }

	online.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'border',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
		items : [ online.grid ]
	});
</script>
