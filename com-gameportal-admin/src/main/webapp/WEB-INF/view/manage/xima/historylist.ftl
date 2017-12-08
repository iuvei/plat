<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.historylist"); // 自定义一个命名空间
historylist = Ext.market.historylist; // 定义命名空间的别名
historylist = {
	all : '/manage/memberximaset/historylist.do',// 加载所有
	pageSize : 20 // 每页显示的记录数
};


/** 改变页的combo */
historylist.pageSizeCombo = new Share.pageSizeCombo({
			value : '20',
			listeners : {
				select : function(comboBox) {
					historylist.pageSize = parseInt(comboBox.getValue());
					historylist.bbar.pageSize = parseInt(comboBox.getValue());
					historylist.store.baseParams.limit = historylist.pageSize;
					historylist.store.baseParams.start = 0;
					historylist.store.baseParams.startDate = $("#startDate").val();
					historylist.store.baseParams.endDate = $("#endDate").val();
					historylist.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
historylist.pageSize = parseInt(historylist.pageSizeCombo.getValue());
/** 基本信息-数据源 */
historylist.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : historylist.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : historylist.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['flagid', 'flagaccount', 'isxima',"updatetime",'remark']),
			listeners : {
				'load' : function(store, records, options) {
				//	memberGrade.alwaysFun();
				}
			}
		});
		
/** 基本信息-选择模式 */
historylist.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {}
	});
	
/** 基本信息-数据列 */
historylist.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [historylist.selModel, {
						hidden : true,
						header : 'ID',
						dataIndex : 'flagid'
					},{
						header : '会员账号',
						dataIndex : 'flagaccount',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '洗码状态',
						dataIndex : 'isxima',
						width : 60,
						renderer : function(v) {
							if(v =='0'){
								return "<span style='color:red;'>不洗码</span>";
							}else{
								return "<span style='color:green;'>可洗码</span>";
							}
						},
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
					},{
						header : '备注',
						dataIndex : 'remark',
						width : 400,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}]
		});
	
/** 查询按钮 */
historylist.searchAction = new Ext.Action({
		text : '查询',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				historylist.searchFun();
			}
	});
historylist.searchFun = function(){
		historylist.store.load({params: historylist.searchParams()});
	}
historylist.searchParams = function(){
		var obj = {};
		obj.flagaccount=$("#flagaccount").val(); //会员账号
		obj.startDate=$("#startDate").val(); //开始时间 
		obj.endDate=$("#endDate").val(); //结束时间
	    return obj;
	}
/**日期条件  -- 开始时间*/
 	historylist.startDateField = new Ext.form.DateField({
  			id:'startDate',
        	showToday:true,
        	format:'Y-m-d',
        	invalidText:'日期输入非法',
        	allowBlank : true,
        	value:'${startDate}',
        	width:150 
 	});
 	/** 日期条件  -- 结束时间*/
 	historylist.endDateField = new Ext.form.DateField({
  			id:'endDate',
        	showToday:true,
        	format:'Y-m-d',
        	invalidText:'日期输入非法',
        	allowBlank : true,
        	value:'${endDate}',
        	width:150 
 	});
/** 顶部工具栏 */
historylist.tbar = [
'会员账号:',{id:'flagaccount',xtype:'textfield',width:100},
'-','&nbsp;','存款时间:',historylist.startDateField,'~',historylist.endDateField,
'-','&nbsp;',historylist.searchAction];
/** 底部工具条 */
historylist.bbar = new Ext.PagingToolbar({
			pageSize : historylist.pageSize,
			store : historylist.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', historylist.pageSizeCombo]
		});
/** 基本信息-表格 */
historylist.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : historylist.store,
			colModel : historylist.colModel,
			selModel : historylist.selModel,
			tbar : historylist.tbar,
			bbar : historylist.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'memberGradeDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});


historylist.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [historylist.grid]
		});

</script>