<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	Ext.ns("Ext.market.emails"); // 自定义一个命名空间
	emails = Ext.market.emails; // 定义命名空间的别名
	emails = {
		all : '/manage/email/querySendlist.do', // 
		del : "/manage/email/deleteWaitEmail.do",//删除待发邮件数据
		pageSize : 1000// 每页显示的记录数
		//STATUSMAP : eval('(${statusMap})')//注意括号
	};
	/** 改变页的combo*/
	emails.pageSizeCombo = new Share.pageSizeCombo({
		value : '1000',
		listeners : {
			select : function(comboBox) {
				emails.pageSize  = parseInt(comboBox.getValue());
				emails.bbar.pageSize  = parseInt(comboBox.getValue());
				emails.store.baseParams.limit = emails.pageSize;
				emails.store.baseParams.start = 0;
				emails.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	emails.pageSize = parseInt(emails.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	emails.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:true,
		baseParams : {
			start : 0,
			limit : emails.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : emails.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'rediskey','mailServerHost','mailServerPort','fromAddress','toAddress','subject','content','createDate']),
		listeners : {
			'load' : function(store, records, options) {
				emails.alwaysFun();
			}
		}
	});
	//emails.store.load(); 
	/** 基本信息-选择模式 */
	emails.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				emails.deleteAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				emails.deleteAction.disable();
			}
		}
	});
	/** 基本信息-数据列 */
	emails.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 110
		},
		columns : [ emails.selModel,{
			header : 'SessionKey',
			dataIndex : 'rediskey',
			width : 150
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
			editor : new Ext.form.TextField({
				style: 'border:0px;'
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
		}, {
			header : '邮件内容',
			dataIndex : 'content',
			width : 260,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}]
	});

	/** 编辑 */
	emails.deleteAction = new Ext.Action({
		text : '删除',
		iconCls : 'Cross',
		disabled : true,
		handler : function() {
			emails.tiFun();
		}
	});
	
	/** 查询 */
	emails.searchField = new Ext.ux.form.SearchField({
		store : emails.store,
		paramName : 'ccno',
		emptyText : '请输入会员账号',
		style : 'margin-left: 5px;'
	});
	/** 顶部工具栏 */
	emails.tbar = [ emails.deleteAction, '-',emails.searchField];
	/** 底部工具条 */
	emails.bbar = new Ext.PagingToolbar({
		pageSize : emails.pageSize,
		store : emails.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', emails.pageSizeCombo ]
	});
	/** 基本信息-表格 */
	emails.grid = new Ext.grid.EditorGridPanel({
		store : emails.store,
		colModel : emails.colModel,
		selModel : emails.selModel,
		tbar : emails.tbar,
		bbar : emails.bbar,
		autoScroll : 'auto',
		region : 'center',
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig:{},
		stripeRows : true
	});
	
	emails.alwaysFun = function() {
		Share.resetGrid(emails.grid);
		emails.deleteAction.disable();
	};
	
	//删除待发邮件数据
  emails.tiFun=function(){
     var record = emails.grid.getSelectionModel().getSelected();
     if(record){
     	Ext.Msg.confirm('提示', '确定要删除此邮件数据吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : emails.del,
								params : {
									ukey:record.data.rediskey
								},
								callback : function(json) {
									if (json.success==false){
				       						Ext.MessageBox.alert('Status', json.msg, showResult);
									}else{
				                        emails.store.remove(record);//删除数据
					  					emails.alwaysFun();
									}
								}
							});
				}
			});
     }
  
  }

	emails.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'border',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
		items : [ emails.grid ]
	});
</script>
