<div id="${id}"></div>
<script type="text/javascript">
Ext.ns("Ext.market.fkmemberInfos"); // 自定义一个命名空间
fkmemberInfo = Ext.market.fkmemberInfos; // 定义命名空间的别名
fkmemberInfo = {
	all : '/manage/memberinfo/queryFKMemberInfo.do',// 加载所有
	edit : '/manage/memberinfo/fk/edit.do',
	fkinfo : '/manage/memberinfo/queryFkMemberDetail/',
	memStatus : eval('(${fields.memStatus})'),
	memGrade:eval('(${fields.memGrade})'),
	pageSize : 30 // 每页显示的记录数
};


/** 改变页的combo */
fkmemberInfo.pageSizeCombo = new Share.pageSizeCombo({
			value : '30',
			listeners : {
				select : function(comboBox) {
					fkmemberInfo.pageSize = parseInt(comboBox.getValue());
					fkmemberInfo.bbar.pageSize = parseInt(comboBox.getValue());
					fkmemberInfo.store.baseParams.limit = fkmemberInfo.pageSize;
					fkmemberInfo.store.baseParams.start = 0;
					fkmemberInfo.store.load();
				}
			}
		});
// 覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
fkmemberInfo.pageSize = parseInt(fkmemberInfo.pageSizeCombo.getValue());
/** 基本信息-数据源 */
fkmemberInfo.store = new Ext.data.Store({
			autoLoad : false,
			remoteSort : true,
			baseParams : {
				start : 0,
				limit : fkmemberInfo.pageSize
			},
			proxy : new Ext.data.HttpProxy({// 获取数据的方式
				method : 'POST',
				url : fkmemberInfo.all
			}),
			reader : new Ext.data.JsonReader({// 数据读取器
				totalProperty : 'totalProperty', // 记录总数
				root : 'data' // Json中的列表数据根节点
			}, ['uiid', 'account', 'accounttype', 'uname', 'identitycard', 'phone', 
			    	'email','qq', 'birthday','grade','status','createDate','url','regip','puname','create_date','typeflag','phonevalid','emailvalid','paymoney','remark','withdrawalMoney','couponMoney','winMoney','relaflag']),
			listeners : {
				'load' : function(store, records, options) {
				}
			}
		});
		
/** 基本信息-选择模式 */
fkmemberInfo.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				fkmemberInfo.addAction.enable();
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				fkmemberInfo.addAction.disable();
			}
		}
	});
	
/** 基本信息-数据列 */
fkmemberInfo.colModel = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				width : 140
			},
			columns : [fkmemberInfo.selModel, {
						hidden : true,
						header : '用户ID',
						dataIndex : 'uiid'
					}, {
						header : '帐号',
						dataIndex : 'account',
						width : 95,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '姓名',
						dataIndex : 'uname',
						width : 84,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '会员类型',
						dataIndex : 'accounttype',
						width : 60,
						renderer : function(v) {
							if(v == 0){
								return '<span style="color:blue;">普通会员</span>';
							}else if(v ==1){
								return '<span style="color:#992211;">代理会员</span>';
							}else if(v ==2){
								return '<span style="color:#992211;">信用代理</span>';
							}else if(v ==3){
								return '<span style="color:#992211;">信用会员</span>';
							}
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '手机认证',
						dataIndex : 'phonevalid',
						width : 60,
						renderer : function(v) {
							if(v == 0){
								return '<span style="color:red;">未认证</span>';
							}else{
								return '<span style="color:green;">已认证</span>';
							}
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '邮箱认证',
						dataIndex : 'emailvalid',
						width : 60,
						renderer : function(v) {
							if(v == 0){
								return '<span style="color:red;">未认证</span>';
							}else if(v==1){
								return '<span style="color:green;">已认证</span>';
							}else{
								return '<span style="color:orange;">待认证</span>';
							}
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '充值金额',
						dataIndex : 'paymoney',
						width : 60,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '提款金额',
						dataIndex : 'withdrawalMoney',
						width : 60,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '优惠金额',
						dataIndex : 'couponMoney',
						width : 60,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '输赢值',
						dataIndex : 'winMoney',
						width : 60,
						renderer : function(v) {
							if(v >= 0){
								return '<span style="color:blue;">'+v+'</span>';
							}else{
								return '<span style="color:#992211;">'+v+'</span>';
							}
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '关联账号',
						dataIndex : 'relaflag',
						width : 60,
						renderer : function(v) {
							if(v == 1){
								return '<span style="color:red;">已关联</span>';
							}else{
								return '<span style="color:green;">未关联</span>';
							}
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '会员类型',
						dataIndex : 'paymoney',
						width : 60,
						renderer : function(v) {
							if(v >= 100){
								return '<span style="color:blue;">有效会员</span>';
							}else{
								return '<span style="color:#992211;">注册会员</span>';
							}
						},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						hidden : true,
						header : '身份证',
						dataIndex : 'identitycard',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						hidden : true,
						header : '电话',
						dataIndex : 'phone',
						width : 96,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						hidden : true,
						header : '邮箱',
						dataIndex : 'email',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						hidden : true,
						header : 'QQ',
						dataIndex : 'qq',
						width : 81,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '会员等级',
						dataIndex : 'grade',
						width : 60,
						renderer : function(v) {
							return Share.map(v, fkmemberInfo.memGrade, '');
       						
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})

					}, {
						header : '状态',
						dataIndex : 'status',
						width : 60,
						renderer : function(v) {
							return Share.map(v, fkmemberInfo.memStatus, '');
      					},
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '注册IP',
						dataIndex : 'regip',
						width : 104,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : 'URL',
						dataIndex : 'url',
						width : 200,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}, {
						header : '所属代理',
						dataIndex : 'puname',
						width : 104,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					},{
						header : '创建日期',
						dataIndex : 'create_date',
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						}),
						renderer : function(v) {
       						return new Date(v).format('Y-m-d H:i:s');
      					}
					},{
						header : '备注',
						dataIndex : 'remark',
						width : 200,
						editor : new Ext.form.TextField({
							style: 'border:0px;'
						})
					}]
		});
		
/** 备注 */
fkmemberInfo.formPanelRemark = new Ext.form.FormPanel({
	region : 'center',
	autoScroll : false,
	frame : false,
	border: false,
	style: 'border-bottom:0px;',
	bodyStyle: 'padding:10px;background-color:transparent;',
	labelwidth : 70,
	defaultType : 'textfield',
	items : [ {
		xtype : 'hidden',
		fieldLabel : 'ID',
		name : 'uiid'
	},{
			fieldLabel : '备注',
			maxLength : 1000,
			xtype : 'textarea',
			name : 'remark',
			allowBlank : false,
			anchor : '99%'
		}]
});

/** 编辑新建窗口 */
fkmemberInfo.addWindowRemark = new Ext.Window({
	layout : 'fit',
	width : 500,
	height : 200,
	closeAction : 'hide',
	plain : true,
	modal : true,
	resizable : true,
	items : [ fkmemberInfo.formPanelRemark ],
	buttons : [ {
		text : '保存',
		handler : function() {
			fkmemberInfo.saveFun();
		}
	}, {
		text : '取消',
		handler : function() {
			fkmemberInfo.addWindowRemark.hide();
		}
	} ]
});

fkmemberInfo.saveFun = function(){
	var form = fkmemberInfo.formPanelRemark.getForm();

	if(!form.isValid()){
 		return;
 	}
	// 发送请求
	Share.AjaxRequest({
				url : fkmemberInfo.edit,
				params : form.getValues(),
				callback : function(json) {
					if (json.success==false){
					    Ext.MessageBox.alert('Status', json.msg, showResult);
						return;
					}else{
					    fkmemberInfo.addWindowRemark.hide();
						fkmemberInfo.store.reload();
					}
				}
			});
}
		
/** 添加备注 */
fkmemberInfo.addAction = new Ext.Action({
			text : '添加备注',
			iconCls : 'Layoutedit',
			disabled : true,
			handler : function() {
				fkmemberInfo.addWindowRemark.setIconClass('Layoutedit'); // 设置窗口的样式
				fkmemberInfo.addWindowRemark.setTitle('添加备注'); // 设置窗口的名称
				fkmemberInfo.addWindowRemark.show().center(); // 显示窗口
				fkmemberInfo.formPanelRemark.getForm().reset(); // 清空表单里面的元素的值.
				var record = fkmemberInfo.grid.getSelectionModel().getSelected();
				var form=fkmemberInfo.formPanelRemark.getForm();
				form.findField("uiid").setValue(record.data.uiid);
			}
});

/** 顶部工具栏 */
fkmemberInfo.tbar = [   {
                        	text: '会员详情',
                        	iconCls: 'Zoom',
                        	handler: function(){
                        		var record = fkmemberInfo.grid.getSelectionModel().getSelected();
                        		window.open(fkmemberInfo.fkinfo+record.data.uiid+".do");
                        	}
                        },'&nbsp;',fkmemberInfo.addAction,'&nbsp;','会员账号:',
                        new Ext.form.TextField({ id:'fkmemberInfo_account',width:120,emptyText : '请输会员账号'}),'&nbsp',
                         {
                        	text: '查询',
                        	iconCls: 'Zoom',
                        	handler: function(){ fkmemberInfo.selectFun(); }
                        }];
/** 底部工具条 */
fkmemberInfo.bbar = new Ext.PagingToolbar({
			pageSize : fkmemberInfo.pageSize,
			store : fkmemberInfo.store,
			displayInfo : true,
			// plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			items : ['-', '&nbsp;', fkmemberInfo.pageSizeCombo]
		});
/** 基本信息-表格 */
fkmemberInfo.grid = new Ext.grid.EditorGridPanel({
			// title : '平台列表',
			store : fkmemberInfo.store,
			colModel : fkmemberInfo.colModel,
			selModel : fkmemberInfo.selModel,
			tbar : fkmemberInfo.tbar,
			bbar : fkmemberInfo.bbar,
			autoScroll : 'auto',
			region : 'center',
			loadMask : true,
			stripeRows : true,
			listeners : {},
			viewConfig : {}
		});

  /**
   * 搜索参数
   */
   fkmemberInfo.selectParams = function(){
   		var obj={};
	  	if($("#fkmemberInfo_account").val()!=""){
	  		obj.account=$("#fkmemberInfo_account").val();
	  	}
	  	return obj;
   }
  /**
   * 查询事件
   */
  fkmemberInfo.selectFun=function(){
  	if($("#fkmemberInfo_account").val()!=""){
  		fkmemberInfo.store.load({params: fkmemberInfo.selectParams()});
  		Share.resetGrid(fkmemberInfo.grid);
  	}else{
		Ext.MessageBox.alert("提示","请输入会员账号!")
  	}
  }
  
  fkmemberInfo.store.on('beforeload',function(){
  	fkmemberInfo.store.baseParams = fkmemberInfo.selectParams();
  });
  
  
fkmemberInfo.myPanel = new Ext.Panel({
			id : '${id}' + '_panel',
			renderTo : '${id}',
			layout : 'border',
			boder : false,
			height : index.tabPanel.getInnerHeight() - 1,
			items : [fkmemberInfo.grid]
		});
</script>