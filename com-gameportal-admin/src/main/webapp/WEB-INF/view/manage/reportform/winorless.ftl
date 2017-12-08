<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	Ext.ns("Ext.market.winorless"); // 自定义一个命名空间
	winorless = Ext.market.winorless; // 定义命名空间的别名
	winorless = {
		all : '/manage/reportForm/getwinorlessReport.do',
		down:'/manage/reportForm/toDownloadWinOrLessReport.do',//导出报表
		pageSize : 2000, // 每页显示的记录数
		today:'${today}',
	};
	/** 改变页的combo*/
	winorless.pageSizeCombo = new Share.pageSizeCombo({
		value : '1000',
		listeners : {
			select : function(comboBox) {
				winorless.pageSize  = parseInt(comboBox.getValue());
				winorless.bbar.pageSize  = parseInt(comboBox.getValue());
				winorless.store.baseParams.limit = winorless.pageSize;
				winorless.store.baseParams.start = 0;
				winorless.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	winorless.pageSize = parseInt(winorless.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	winorless.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:true,
		baseParams : {
			startDate:'',
			endDate:'',
			start : 0,
			limit : winorless.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : winorless.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'uaccount','deposit','withdrawal','preferential']),
		listeners : {
			'load' : function(store, records, options) {
			}
		}
	});
	//online.store.load(); 
	/** 基本信息-选择模式 */
	winorless.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
			}
		}
	});
	/** 基本信息-数据列 */
	winorless.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 110
		},
		columns : [ winorless.selModel, {
						header : '会员账号',
						dataIndex : 'uaccount',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '充值总额',
						dataIndex : 'deposit',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '提款总额',
						dataIndex : 'withdrawal',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '优惠总额',
						dataIndex : 'preferential',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}
				]
	});
	
	/** 查询 */
	winorless.searchAction = new Ext.Action({
			text : '数据查询',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				winorless.searchFun();
			}
	});
	/** 导出报表按钮 */
	winorless.downAction = new Ext.Action({
			text : '导出报表',
			iconCls : 'Diskdownload',
			disabled : false,
			handler : function() {
				winorless.downReport();
			}
	});
	
	/** 导出报表函数 */
	winorless.downReport = function() {
		 //发送请求
		 window.open(winorless.down + "?startDate="+$("#sreportStartDate").val()+"&endDate="+$("#sreportEndDate").val()+"&account="+$("#saccount").val()+"&paccount="+$("#spaccount").val());
	};
	
	winorless.searchParams = function(){
		var obj = {};
		obj.startDate = $("#sreportStartDate").val();
		obj.endDate = $("#sreportEndDate").val();
		obj.account = $("#saccount").val();
		obj.paccount = $("#spaccount").val();
		obj.sortField = $("#sortField").prev().val();
	    return obj;
	}
	
	winorless.searchFun = function(){
		winorless.store.load({params: winorless.searchParams()});
	}
	
	winorless.store.on('beforeload',function(store, options){
	    winorless.store.baseParams = winorless.searchParams();
	});
 	/**日期条件  -- 开始时间*/
 	winorless.startDateField = new Ext.form.DateField({
  			id:'sreportStartDate',
        	showToday:true,
        	format:'Y-m-d',
        	invalidText:'日期输入非法',
        	allowBlank : false,
        	value:winorless.today,
        	width:100 
 	});
 	/** 日期条件  -- 结束时间*/
 	winorless.endDateField = new Ext.form.DateField({
  			id:'sreportEndDate',
        	showToday:true,
        	format:'Y-m-d',
        	invalidText:'日期输入非法',
        	allowBlank : false,
        	value:winorless.today,
        	width:100 
 	});
 	
 	winorless.sortFiledStore = new Ext.data.SimpleStore({
					fields : ['v', 't'],  
					data : [['1', '充值降序'], ['2', '充值升序'], ['3', '提款降序'], ['4', '提款升序'],['5', '优惠降序'],['6','优惠升序']]
		});
		
	winorless.sortFiled = new Ext.form.ComboBox({
		hiddenName : 'sortField',
		id:'sortField',
		name : 'sortField',
		triggerAction : 'all',
		mode : 'local',
	    store : winorless.sortFiledStore , 
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		value : "1",
		width:100,
		listeners : {}
	});
	
	/** 底部工具条 */
	winorless.bbar = new Ext.PagingToolbar({
		pageSize : winorless.pageSize,
		store : winorless.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', winorless.pageSizeCombo ]
	});
	winorless.tbar =['会员账号:',
                    new Ext.form.TextField({ id:'saccount',width:120 }),'&nbsp;','-','&nbsp;',
                    '代理账号:',
                    new Ext.form.TextField({ id:'spaccount',width:120 }),
           			'&nbsp;','-','&nbsp;','排序方式:',winorless.sortFiled,'&nbsp;','-','&nbsp;',
               		'开始时间:',winorless.startDateField,
               		'&nbsp;','-','&nbsp;',
               		'截止时间:',winorless.endDateField,
               		'&nbsp;','-','&nbsp;',
					winorless.searchAction,'&nbsp;','-','&nbsp;',winorless.downAction ];
	/** 基本信息-表格 */
	winorless.grid = new Ext.grid.EditorGridPanel({
		store : winorless.store,
		colModel : winorless.colModel,
		selModel : winorless.selModel,
		tbar : winorless.tbar,
		bbar : winorless.bbar,
		autoScroll : 'auto',
		region : 'center',
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig:{},
		stripeRows : true,
		listeners : {}
	});
	
	
	winorless.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'border',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
		items : [ winorless.grid ]
	});
</script>
