<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	Ext.ns("Ext.market.emailall"); // 自定义一个命名空间
	emailall = Ext.market.emailall; // 定义命名空间的别名
	emailall = {
		all : '/manage/email/queryAll.do', // 
		del : "/manage/email/delete.do",//删除待发邮件数据
		pageSize : 30, // 每页显示的记录数
		mailstatus : eval('(${emailstatus})')//注意括号
	};
	/** 改变页的combo*/
	emailall.pageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				emailall.pageSize  = parseInt(comboBox.getValue());
				emailall.bbar.pageSize  = parseInt(comboBox.getValue());
				emailall.store.baseParams.limit = emailall.pageSize;
				emailall.store.baseParams.start = 0;
				emailall.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	emailall.pageSize = parseInt(emailall.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	emailall.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:true,
		baseParams : {
			start : 0,
			limit : emailall.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : emailall.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'mailid','uuid','mailServerHost','mailServerPort','fromAddress','toAddress','subject','content','createDate','sendDate','status','validTime']),
		listeners : {
			'load' : function(store, records, options) {
				emailall.alwaysFun();
			}
		}
	});
	//emailall.store.load(); 
	/** 基本信息-选择模式 */
	emailall.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				emailall.deleteAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				emailall.deleteAction.disable();
			}
		}
	});
	/** 基本信息-数据列 */
	emailall.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 110
		},
		columns : [ emailall.selModel, {
			hidden : true,
			header : '邮件数据ID',
			dataIndex : 'mailid'
		},{
			hidden : true,
			header : '所属用户ID',
			dataIndex : 'uuid'
		},{
			header : '服务器地址',
			dataIndex : 'mailServerHost',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '服务器端口',
			dataIndex : 'mailServerPort',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '发件人地址',
			dataIndex : 'fromAddress',
			width : 130,
			editor: new Ext.form.TextField({
				stle: 'border:0px;'
			})
		}, {
			header : '收件人地址',
			dataIndex : 'toAddress',
			width : 130,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '邮件主题',
			dataIndex : 'subject',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '创建时间',
			dataIndex : 'createDate',
			width : 150,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '发送时间',
			dataIndex : 'sendDate',
			width : 150,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '验证时间',
			dataIndex : 'validTime',
			width : 150,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '邮件内容',
			dataIndex : 'content',
			width : 260,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '发送状态',
			dataIndex : 'status',
			width : 100,
			renderer : function(v) {
				return '<span style="color:blue;">'+Share.map(v, emailall.mailstatus, '')+'</span>';
			}
		}]
	});

	/** 编辑 */
	emailall.deleteAction = new Ext.Action({
		text : '删除',
		iconCls : 'Cross',
		disabled : true,
		handler : function() {
			emailall.delFun();
		}
	});
	
	/**搜索按钮*/
	emailall.searchAction = new Ext.Action({
			text : '搜索',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				emailall.searchFun();
			}
	});
	
	emailall.statusCombo = new Ext.form.ComboBox({
		hiddenName : 'status',
		id : 'emailallstatus',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(emailall.mailstatus)
				}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		value : '',
		width:100
	});

	/** 顶部工具栏 */
	emailall.tbar = [ emailall.deleteAction,'-','&nbsp;', 
	'服务器地址：',{id:'mailServerHost',xtype:'textfield',width:100},'&nbsp;',
	'发件地址：',{id:'fromAddress',xtype:'textfield',width:100},'&nbsp;',
	'收件地址：',{id:'toAddress',xtype:'textfield',width:100},'&nbsp;',
	'创建时间：',{id:'emailStartTime',xtype:'datetimefield',format:'Y-m-d H:i:s',width:140},'&nbsp;',
	'至','&nbsp;',{id:'emailEndTime',xtype:'datetimefield',format:'Y-m-d H:i:s',width:140},'&nbsp;',
	'发送状态：',emailall.statusCombo,'&nbsp;',
	'&nbsp;',emailall.searchAction
	];
	
	/** 底部工具条 */
	emailall.bbar = new Ext.PagingToolbar({
		pageSize : emailall.pageSize,
		store : emailall.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', emailall.pageSizeCombo ]
	});
	/** 基本信息-表格 */
	emailall.grid = new Ext.grid.EditorGridPanel({
		store : emailall.store,
		colModel : emailall.colModel,
		selModel : emailall.selModel,
		tbar : emailall.tbar,
		bbar : emailall.bbar,
		autoScroll : 'auto',
		region : 'center',
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig:{},
		stripeRows : true
	});
	
	emailall.alwaysFun = function() {
		Share.resetGrid(emailall.grid);
		emailall.deleteAction.disable();
	};
	
	/**
	 * 搜索参数
	 * @return {}
	 */
	emailall.searchParams = function(){
		var obj = {};
		if($("#mailServerHost").val()!=""){
			obj.host = $("#mailServerHost").val();
		}
		if($("#fromAddress").val()!=""){
			obj.fromAddress = $("#fromAddress").val();
		}
		if($("#toAddress").val()!=""){
			obj.toAddress = $("#toAddress").val();
		}
		if($("#emailStartTime").val()!=""){
			obj.starttime = $("#emailStartTime").val();
		}
		if($("#emailEndTime").val()!=""){
			obj.endtime = $("#emailEndTime").val();
		}
		
		var st=$("#emailallstatus").prev();
	    if($(st).val()!=""){
	     	obj.status=$(st).val();
	    }
		return obj;
	}
	/**
	 * 搜索函数
	 */
	emailall.searchFun = function(){
		emailall.store.load({params: emailall.searchParams()});
	}
	emailall.store.on('beforeload',function(store, options){
	    emailall.store.baseParams = emailall.searchParams();
	});
	//删除邮件方法
  emailall.delFun=function(){
     var record = emailall.grid.getSelectionModel().getSelected();
     if(record){
     	Ext.Msg.confirm('提示', '确定要删除此邮件数据吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : emailall.del,
								params : {
									mailid:record.data.mailid
								},
								callback : function(json) {
									if (json.success==false){
				       						Ext.MessageBox.alert('Status', json.msg, showResult);
									}else{
					  					emailall.alwaysFun();
									}
								}
							});
				}
			});
     }
  
  }

	emailall.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'border',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
		items : [ emailall.grid ]
	});
</script>
