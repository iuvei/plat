<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	Ext.ns("Ext.market.memberximaset"); // 自定义一个命名空间
	memberximaset = Ext.market.memberximaset; // 定义命名空间的别名
	memberximaset = {
		all : '/manage/memberximaset/queryMemberximaset.do', // 所有会员洗码设置
		save : "/manage/memberximaset/save.do",// 保存会员洗码设置
		del : "/manage/memberximaset/del/",// 删除会员洗码设置
		pageSize : 20,// 每页显示的记录数
		GAMEPLATMAP : eval('(${gameplatMap})'),//注意括号
		memGrade:eval('(${memGradeMap})')
	};
	/** 改变页的combo*/
	memberximaset.pageSizeCombo = new Share.pageSizeCombo({
		value : '20',
		listeners : {
			select : function(comboBox) {
				memberximaset.pageSize  = parseInt(comboBox.getValue());
				memberximaset.bbar.pageSize  = parseInt(comboBox.getValue());
				memberximaset.store.baseParams.limit = memberximaset.pageSize;
				memberximaset.store.baseParams.start = 0;
				memberximaset.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	memberximaset.pageSize = parseInt(memberximaset.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	memberximaset.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:true,
		baseParams : {
			start : 0,
			limit : memberximaset.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : memberximaset.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'mxsid','gpid','grade','scale','updateuserid','updatetime','updateusername' ]),
		listeners : {
			'load' : function(store, records, options) {
				memberximaset.alwaysFun();
			}
		}
	});
	//memberximaset.store.load(); 
	/** 基本信息-选择模式 */
	memberximaset.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				memberximaset.deleteAction.enable();
				memberximaset.editAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				memberximaset.deleteAction.disable();
				memberximaset.editAction.disable();
			}
		}
	});
	/** 基本信息-数据列 */
	memberximaset.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 110
		},
		// 'mxsid','gpid','grade','scale','updateuserid','updatetime','updateusername'
		columns : [ memberximaset.selModel, {
			hidden : true,
			header : '会员洗码设置ID',
			dataIndex : 'mxsid'
		}, {
			header : '游戏平台',
			dataIndex : 'gpid',
			renderer : function(v) {
				return Share.map(v, memberximaset.GAMEPLATMAP, '');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '会员星级',
			dataIndex : 'grade',
			renderer : function(v) {
				return Share.map(v, memberximaset.memGrade, '');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '返水比例',
			dataIndex : 'scale',
			width:700,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '更新者',
			dataIndex : 'updateusername',
			width:60,
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
		} ]
	});
	/** 新建 */
	memberximaset.addAction = new Ext.Action({
		text : '新建',
		//text : '<fmt:message key="common.cancel"/>',
		iconCls : 'memberximaset_add',
		handler : function() {
			memberximaset.addWindow.setIconClass('memberximaset_add'); // 设置窗口的样式
			memberximaset.addWindow.setTitle('新增'); // 设置窗口的名称
			memberximaset.addWindow.show().center(); // 显示窗口
			memberximaset.formPanel.getForm().reset(); // 清空表单里面的元素的值.
		}
	});
	/** 编辑 */
	memberximaset.editAction = new Ext.Action({
		text : '编辑',
		iconCls : 'memberximaset_edit',
		disabled : true,
		handler : function() {
			var record = memberximaset.grid.getSelectionModel().getSelected();
			memberximaset.addWindow.setIconClass('memberximaset_edit'); // 设置窗口的样式
			memberximaset.addWindow.setTitle('编辑'); // 设置窗口的名称
			memberximaset.addWindow.show().center();
			memberximaset.formPanel.getForm().reset();
			memberximaset.formPanel.getForm().loadRecord(record);
			
			// 返水比例值显示转化
			//memberximaset.scaleNumberField.setValue(memberximaset.scaleNumberField.getValue()*100);
		}
	});
	/** 删除 */
	memberximaset.deleteAction = new Ext.Action({
		text : '删除',
		iconCls : 'memberximaset_delete',
		disabled : true,
		handler : function() {
			memberximaset.delFun();
		}
	});
	/** 查询 */
	memberximaset.platCombos = new Ext.form.ComboBox({
		hiddenName : 'gpid',
		id : 'gpid',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(memberximaset.GAMEPLATMAP)
				}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		value : '',
		width:80
	});
	
	memberximaset.gradeCombos = new Ext.form.ComboBox({
			hiddenName : 'grade',
			id : 'grade',
			triggerAction : 'all',
			mode : 'local',
			store : new Ext.data.ArrayStore({
						fields : ['v', 't'],
						data : Share.map2Ary(memberximaset.memGrade)
					}),
			valueField : 'v',
			displayField : 't',
			allowBlank : false,
			editable : false,
			value : '',
			width:80
		});	
	
	memberximaset.searchAction = new Ext.Action({
			text : '搜索',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				memberximaset.searchFun();
			}
	});
	
	memberximaset.searchFun = function(){
		memberximaset.store.load({params: memberximaset.searchParams()});
	}
	
	memberximaset.store.on('beforeload',function(store, options){
	    memberximaset.store.baseParams = memberximaset.searchParams();
	});
	
	memberximaset.searchParams = function(){
		var obj = {};
		var st=$("#gpid").prev();
	    if($(st).val()!=""){
	     	obj.gpid=$(st).val();
	    }
	    var grade=$("#grade").prev();
	    if($(grade).val()!=""){
	     	obj.grade=$(grade).val();
	    }
	    return obj;
	}

	/** 顶部工具栏 */
	memberximaset.tbar = [ memberximaset.addAction, '-', memberximaset.editAction, '-', memberximaset.deleteAction, '-'
	                       ,'平台名称：',memberximaset.platCombos,'-','会员等级：',memberximaset.gradeCombos,'-','&nbsp;',	memberximaset.searchAction
	                ];
	/** 底部工具条 */
	memberximaset.bbar = new Ext.PagingToolbar({
		pageSize : memberximaset.pageSize,
		store : memberximaset.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', memberximaset.pageSizeCombo ]
	});
	/** 基本信息-表格 */
	memberximaset.grid = new Ext.grid.EditorGridPanel({
		store : memberximaset.store,
		colModel : memberximaset.colModel,
		selModel : memberximaset.selModel,
		tbar : memberximaset.tbar,
		bbar : memberximaset.bbar,
		autoScroll : 'auto',
		region : 'center',
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig:{},
		stripeRows : true
	});
	
	memberximaset.gpidCombo = new Ext.form.ComboBox({
		fieldLabel : '游戏平台',
		hiddenName : 'gpid',
		name : 'gpid',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(memberximaset.GAMEPLATMAP)
				}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		anchor : '99%'
	});
	
	memberximaset.gradeCombo = new Ext.form.ComboBox({
			fieldLabel : '会员等级',
			hiddenName : 'grade',
			name : 'grade',
			triggerAction : 'all',
			mode : 'local',
			store : new Ext.data.ArrayStore({
						fields : ['v', 't'],
						data : Share.map2Ary(memberximaset.memGrade)
					}),
			valueField : 'v',
			displayField : 't',
			allowBlank : false,
			editable : false,
			value : "1",
			anchor : '99%'
			
		});	
	
	/** 基本信息-详细信息的form */
	memberximaset.formPanel = new Ext.form.FormPanel({
		autoScroll : true,
		border: false,
        style: 'border-bottom:0px;',
        bodyStyle: 'padding:10px;background-color:transparent;',
		labelwidth : 70,
		defaultType : 'textfield',
		//'mxsid','gpid','grade','scale','updateuserid','updatetime','updateusername'
		items : [ {
			xtype : 'hidden',
			fieldLabel : 'ID',
			name : 'mxsid'
		}, memberximaset.gpidCombo,memberximaset.gradeCombo,{
			fieldLabel : '返水比例',
			allowBlank : false,
			maxLength : 2048,
			allowBlank : false,
			name : 'scale',
			xtype : 'textarea',
			anchor : '99%'
		}]
	});
	/** 编辑新建窗口 */
	memberximaset.addWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 250,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ memberximaset.formPanel ],
		buttons : [ {
			text : '保存',
			handler : function() {
				memberximaset.saveFun();
			}
		}, {
			text : '重置',
			handler : function() {
				var form = memberximaset.formPanel.getForm();
				var mxsid = form.findField("mxsid").getValue();
				form.reset();
				if (mxsid != '')
					form.findField("mxsid").setValue(mxsid);
			}
		} ]
	});
	memberximaset.alwaysFun = function() {
		Share.resetGrid(memberximaset.grid);
		memberximaset.deleteAction.disable();
		memberximaset.editAction.disable();
	};
	memberximaset.saveFun = function() {
		var form = memberximaset.formPanel.getForm();
		if (!form.isValid()) {
			return;
		}
		//var grade = form.findField("grade").getValue();
		//var scale = form.findField("scale").getValue();
//		if(isNaN(grade) || grade<1 || grade>100 || false==/^[0-9]*[1-9][0-9]*$/.test(grade)){
//			Ext.Msg.alert('提示', '会员星级值非法，请重新输入[合法:1~100]！');
//			return;	
//		}
		//if(isNaN(scale) || scale<0 || scale>100){
		//	Ext.Msg.alert('提示', '返水比例值非法，请重新输入[合法:0~100]！');
		//	return;	
		//}
		// 发送请求
		Share.AjaxRequest({
			url : memberximaset.save,
			params : form.getValues(),
			callback : function(json) {
				memberximaset.addWindow.hide();
				memberximaset.alwaysFun();
				memberximaset.store.reload();
			}
		});
	};
	
	memberximaset.delFun = function() {
		var record = memberximaset.grid.getSelectionModel().getSelected();
		Ext.Msg.confirm('提示', '确定要删除这条记录吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : memberximaset.del + record.data.mxsid + ".do",
					callback : function(json) {
						memberximaset.alwaysFun();
						memberximaset.store.reload();
					}
				});
			}
		});
	};

	memberximaset.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'border',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
		items : [ memberximaset.grid ]
	});
</script>
