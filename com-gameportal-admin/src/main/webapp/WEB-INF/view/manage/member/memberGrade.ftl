<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.memberGrades"); // 自定义一个命名空间
memberGrade = Ext.market.memberGrades; // 定义命名空间的别名
memberGrade = {
	all : '/manage/memberGrade/queryMemberGrade.do',// 加载所有
	save : "/manage/memberGrade/saveMemberGrade.do",//保存
	del : "/manage/memberGrade/delMemberGrade/",//删除
	pageSize : 30, // 每页显示的记录数
	memGrade:eval('(${fields.memGrade})')
};


/** 改变页的combo */
memberGrade.pageSizeCombo = new Share.pageSizeCombo({
			value : '30',
			listeners : {
				select : function(comboBox) {
					memberGrade.pageSize = parseInt(comboBox.getValue());
					memberGrade.bbar.pageSize = parseInt(comboBox.getValue());
					memberGrade.store.baseParams.limit = memberGrade.pageSize;
					memberGrade.store.baseParams.start = 0;
					memberGrade.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
memberGrade.pageSize = parseInt(memberGrade.pageSizeCombo.getValue());
/** 基本信息-数据源 */
memberGrade.store = new Ext.data.Store({
			autoLoad : true,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : memberGrade.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : memberGrade.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['gid', 'grade', 'betamont','withdrawalcount','withdrawalquota', 'remark'
			    	]),
			listeners : {
				'load' : function(store, records, options) {
				//	memberGrade.alwaysFun();
				}
			}
		});
		
/** 基本信息-选择模式 */
memberGrade.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				memberGrade.deleteAction.enable();
				memberGrade.editAction.enable();
				
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				memberGrade.alwaysFun();
			}
		}
	});
	
/** 基本信息-数据列 */
memberGrade.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [memberGrade.selModel, {
						hidden : true,
						header : 'ID',
						dataIndex : 'gid'
					}, {
						header : '会员等级',
						dataIndex : 'grade',
						width : 80,
						renderer : function(v) {
							return Share.map(v, memberGrade.memGrade, '');
       						
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '每日提款次数',
						dataIndex : 'withdrawalcount',
						width : 104,
						renderer : function(v) {
							return v + '次';
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '单笔提款额度',
						dataIndex : 'withdrawalquota',
						width : 104,
						renderer : function(v) {
							return '<span style="color:blue;">'+v+'</span>';
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '最少押注金额',
						dataIndex : 'betamont',
						width : 104,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '备注',
						dataIndex : 'remark',
						width : 500,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}]
		});
		
/** 新建 */
memberGrade.addAction = new Ext.Action({
			text : '新建',
			iconCls : 'memberGrade_add',
			handler : function() {
				memberGrade.addWindow.setIconClass('memberGrade_add'); // 设置窗口的样式
				memberGrade.addWindow.setTitle('新建平台'); // 设置窗口的名称
				memberGrade.addWindow.show().center(); // 显示窗口
				memberGrade.formPanel.getForm().reset(); // 清空表单里面的元素的值.
				var form=memberGrade.formPanel.getForm();
				form.findField("grade").setVisible(true);
			}
		});
/** 编辑 */
memberGrade.editAction = new Ext.Action({
			text : '编辑',
			iconCls : 'memberGrade_edit',
			disabled : true,
			handler : function() {
				var record = memberGrade.grid.getSelectionModel().getSelected();
				memberGrade.addWindow.setIconClass('memberGrade_edit'); // 设置窗口的样式
				memberGrade.addWindow.setTitle('编辑平台'); // 设置窗口的名称
				memberGrade.addWindow.show().center();
				memberGrade.formPanel.getForm().reset();
				memberGrade.formPanel.getForm().loadRecord(record);
				var form=memberGrade.formPanel.getForm();
				form.findField("grade").setVisible(false);
			}
		});
		
/** 删除 */
memberGrade.deleteAction = new Ext.Action({
			text : '删除',
			iconCls : 'memberGrade_delete',
			disabled : true,
			handler : function() {
				memberGrade.delFun();
			}
		});



	
/** 顶部工具栏 */
memberGrade.tbar = [memberGrade.addAction, '-', memberGrade.editAction, '-',
		memberGrade.deleteAction];
/** 底部工具条 */
memberGrade.bbar = new Ext.PagingToolbar({
			pageSize : memberGrade.pageSize,
			store : memberGrade.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', memberGrade.pageSizeCombo]
		});
/** 基本信息-表格 */
memberGrade.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : memberGrade.store,
			colModel : memberGrade.colModel,
			selModel : memberGrade.selModel,
			tbar : memberGrade.tbar,
			bbar : memberGrade.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			// autoExpandColumn :'memberGradeDesc',
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});


	
/**会员等级 */		
memberGrade.gradeCombo = new Ext.form.ComboBox({
			fieldLabel : '会员等级',
			hiddenName : 'grade',
			name : 'grade',
			triggerAction : 'all',
			mode : 'local',
			store : new Ext.data.ArrayStore({
						fields : ['v', 't'],
						data : Share.map2Ary(memberGrade.memGrade)
					}),
			valueField : 'v',
			displayField : 't',
			allowBlank : false,
			editable : false,
			value : "1",
			anchor : '50%'
			
		});	
			
	
/** 基本信息-详细信息的form */
memberGrade.formPanel = new Ext.form.FormPanel({
			autoScroll : false,
			border: false,
        	style: 'border-bottom:0px;',
        	bodyStyle: 'padding:10px;background-color:transparent;',
			labelwidth : 50,
			defaultType : 'textfield',
			items : [{
						xtype : 'hidden',
						fieldLabel : 'ID',
						name : 'gid',
						anchor : '99%'
					} 
					,memberGrade.gradeCombo, {
						fieldLabel : '最少押注金额',
						maxLength : 32,
						allowBlank : false,
						name : 'betamont',
						anchor : '99%'
					}, {
						fieldLabel : '每日提款次数',
						maxLength : 32,
						allowBlank : false,
						name : 'withdrawalcount',
						anchor : '99%'
					}, {
						fieldLabel : '单笔提款额度',
						maxLength : 32,
						allowBlank : false,
						name : 'withdrawalquota',
						anchor : '99%'
					},{
						fieldLabel : '备注',
						allowBlank : true,
						name : 'remark',
						anchor : '99%'
					}]
		});

		

			
/** 编辑新建窗口 */
memberGrade.addWindow = new Ext.Window({
			layout : 'fit',
			width : 400,
			height : 220,
			closeAction : 'hide',
			plain : true,
			modal : true,
			resizable : true,
			items : [memberGrade.formPanel],
			buttons : [{
						text : '保存',
						handler : function() {
							memberGrade.saveFun();
						}
					}, {
						text : '重置',
						handler : function() {
							var form = memberGrade.formPanel.getForm();
							var id = form.findField("gid").getValue();
							form.reset();
							if (id != '')
								form.findField("gid").setValue(id);
						}
					}]
		});
	
memberGrade.alwaysFun = function() {
	Share.resetGrid(memberGrade.grid);
	memberGrade.deleteAction.disable();
	memberGrade.editAction.disable();
	
};
memberGrade.saveFun = function() {
	var form = memberGrade.formPanel.getForm();

	
	// 发送请求
	Share.AjaxRequest({
				url : memberGrade.save,
				params : form.getValues(),
				callback : function(json) {
				if (json.success==false){
				     Ext.MessageBox.alert('Status', json.msg, showResult);
					return;
				}else{
				    memberGrade.addWindow.hide();
					memberGrade.alwaysFun();
					memberGrade.store.reload();
				}
					
				}
			});
};

memberGrade.delFun = function() {
	var record = memberGrade.grid.getSelectionModel().getSelected();
	Ext.Msg.confirm('提示', '你真的要删除选中的资料吗?', function(btn, text) {
				if (btn == 'yes') {
					// 发送请求
					Share.AjaxRequest({
								url : memberGrade.del + record.data.gid +".do",
								callback : function(json) {
									memberGrade.alwaysFun();
									memberGrade.store.reload();
								}
							});
				}
			});
};


  
memberGrade.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [memberGrade.grid]
		});

</script>