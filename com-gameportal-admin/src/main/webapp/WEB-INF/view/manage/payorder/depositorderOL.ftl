<div id="${id}"></div>
<script type="text/javascript">
	Ext.QuickTips.init();
	Ext.ns("Ext.market.depositorderOL"); // 自定义一个命名空间
	depositorderOL = Ext.market.depositorderOL; // 定义命名空间的别名
	depositorderOL = {
		all : '/manage/depositorderOL/queryDepositorderOL.do', // 所有存款订单
		save : "/manage/depositorderOL/save.do",// 保存存款订单
		del : "/manage/depositorderOL/del/",// 删除存款订单
		auditKf : "/manage/depositorderOL/auditKf/",// 核实存款订单
		auditCw : "/manage/depositorderOL/auditCw/",// 审核存款订单
		down:'/manage/depositorderOL/toDownloadReport.do',//导出在线支付报表
		pageSize : 30,// 每页显示的记录数
		makeOrder:"/manage/depositorderOL/makeOrder/", //补单
		isKF : eval('${isKF}'),//是否为客服：0否，1是
		isCW : eval('${isCW}'),//是否为账务：0否，1是
		PAYMETHODSMAP : eval('(${paymethodsMap})'),//注意括号
		CCNOMAP : eval('(${ccnoMap})'),//注意括号
		STATUSMAP : eval('(${statusMap})'),//注意括号
		paystatus : eval('(${paystatus})'),//注意括号
		PLATNAME : eval('(${platMap})')
	};
	/** 改变页的combo*/
	depositorderOL.pageSizeCombo = new Share.pageSizeCombo({
		value : '30',
		listeners : {
			select : function(comboBox) {
				depositorderOL.pageSize  = parseInt(comboBox.getValue());
				depositorderOL.bbar.pageSize  = parseInt(comboBox.getValue());
				depositorderOL.store.baseParams.limit = depositorderOL.pageSize;
				depositorderOL.store.baseParams.start = 0;
				depositorderOL.store.load();
			}
		}
	});
	//覆盖已经设置的。具体设置以当前页面的pageSizeCombo为准
	depositorderOL.pageSize = parseInt(depositorderOL.pageSizeCombo.getValue());
	/** 基本信息-数据源 */
	depositorderOL.store = new Ext.data.Store({
		remoteSort : true,
		autoLoad:true,
		baseParams : {
			start : 0,
			limit : depositorderOL.pageSize
		},  
		proxy : new Ext.data.HttpProxy({// 获取数据的方式
			method : 'POST',
			url : depositorderOL.all
		}),
		reader : new Ext.data.JsonReader({// 数据读取器
			totalProperty : 'totalProperty', // 记录总数
			root : 'data' // Json中的列表数据根节点
		}, [ 'poid','platformorders','uiid','paytyple','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','hdnumber','ordercontent','createDate','updateDate','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','cwid','cwname','cwopttime' ,'proxyname']),
		listeners : {
			'load' : function(store, records, options) {
				depositorderOL.amountsum();
				depositorderOL.alwaysFun();
			}
		}
	});
	//小计
	depositorderOL.amountsum = function(){
		var p = new Ext.data.Record({fields:[ 'poid','platformorders','uiid','paytyple','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','ordercontent','createDate','updateDate','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','cwid','cwname','cwopttime' ,'proxyname']});
		var amounttotal= 0;
		depositorderOL.store.each(function(record){
			var amounts = record.data.amount;
			if(amounts != null){//存款金额
				amounttotal += Number(amounts);
			}
		});
		p.set('poid','小计：');
		p.set('hdnumber','--');
		p.set('amount',amounttotal);
		depositorderOL.store.add(p);
	}
	//depositorderOL.store.load(); 
	/** 基本信息-选择模式 */
	depositorderOL.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			'rowselect' : function(selectionModel, rowIndex, record) {
				depositorderOL.editAction.disable();
				depositorderOL.deleteAction.disable();
				depositorderOL.auditCwAction.disable();
				depositorderOL.auditKfAction.disable();
				
				if(1==depositorderOL.isKF){
					depositorderOL.addAction.enable();
					depositorderOL.deleteAction.enable();
					depositorderOL.editAction.enable();
					depositorderOL.auditKfAction.enable();
				}else{
					depositorderOL.addAction.disable();
				}
				if(1==depositorderOL.isCW){
					depositorderOL.deleteAction.enable();
					depositorderOL.auditCwAction.enable();
					depositorderOL.makeOrderAction.enable();
				}
			},
			'rowdeselect' : function(selectionModel, rowIndex, record) {
				if(1==depositorderOL.isKF){
					depositorderOL.addAction.enable();
				}else{
					depositorderOL.addAction.disable();
				}
				depositorderOL.deleteAction.disable();
				depositorderOL.editAction.disable();
				depositorderOL.auditCwAction.disable();
				depositorderOL.auditKfAction.disable();
				depositorderOL.makeOrderAction.disable();
			}
		}
	});
	/** 基本信息-数据列 */
	depositorderOL.colModel = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true,
			width : 90
		},
		// 'poid','platformorders','uiid','paytyple','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','ordercontent','create_date','update_date','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','cwid','cwname','cwopttime'  
		columns : [ depositorderOL.selModel, {
			header : '存款订单ID',
			width : 150,
			dataIndex : 'poid',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			hidden : true,
			header : '操作人ID',
			dataIndex : 'createuserid'
		}, {
			hidden : true,
			header : '用户ID',
			dataIndex : 'uiid'
		}, {
			hidden : true,
			header : '收支类型',
			dataIndex : 'paytyple'
		},{
			hidden : false,
			header : '交易流水号',
			dataIndex : 'platformorders',
			width : 120,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			hidden : true,
			header : '第三方平台订单号',
			width : 150,
			dataIndex : 'remarks',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			hidden : true,
			header : '银行名称',
			dataIndex : 'bankname'
		}, {
			header : '第三方状态',
			dataIndex : 'paystatus',
			width : 70,
			renderer : function(v) {
				if(v == '0'){
					return '<span style="color:#FF9900;">已接受</span>';
				}
				if(v == '1'){
					return '<span style="color:#FF3366;">处理中</span>';
				}
				if(v == '2'){
					return '<span style="color:#009900;">处理成功</span>';
				}
				if(v == '3'){
					return '<span style="color:#FF0000;">处理失败</span>';
				}
				return v;
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '当前状态',
			dataIndex : 'status',
			width : 70,
			renderer : function(v) {
				if(v == 0){
					return '<span style="color:blue;">作废</span>';
				}
				if(v == 1){
					return '<span style="color:#FF9900;">提交充值</span>';
				}
				if(v == 2){
					return '<span style="color:orange;">处理中</span>';
				}
				if(v == 3){
					return '<span style="color:#009900;">充值成功</span>';
				}
				if(v == 4){
					return '<span style="color:#FF0000;">充值失败</span>';
				}
				return v;
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '优惠',
			dataIndex : 'hdnumber',
			width:100,
			renderer : function(v,metadata,record) {
				var couponid = record.data['couponid'];
				if(couponid != null && couponid != ''){
					return '<span style="color:#111eb9;font-weight:bold;">优惠券</span>';
				}
				if(v == '--'){
					return '';
				}
				if(v != null && v !=''){
					if(v =='3'){
						return '<span style="color:#438eb9;font-weight:bold;">首存优惠100%</span>';
					}else if(v =='1'){
						return '<span style="color:#438eb9;font-weight:bold;">存100送88元</span>';
					}else if(v =='2'){
						return '<span style="color:#438eb9;font-weight:bold;">存50送58元</span>';
					}else if(v =='5'){
						return '<span style="color:#438eb9;font-weight:bold;">次次存送20%</span>';
					}else if(v =='6'){
						return '<span style="color:#438eb9;font-weight:bold;">次次存送10%</span>';
					}else if(v =='7'){
						return '<span style="color:#438eb9;font-weight:bold;">每日首存送60%</span>';
					}else if(v =='8'){
						return '<span style="color:#438eb9;font-weight:bold;">存200送50元</span>';
					}else if(v =='9'){
						return '<span style="color:#438eb9;font-weight:bold;">送500送100元</span>';
					}else if(v =='11'){
						return '<span style="color:#438eb9;font-weight:bold;">百家乐存送10%</span>';
					}else if(v =='12'){
						return '<span style="color:#438eb9;font-weight:bold;">百家乐存送20%</span>';
					}else if(v =='13'){
						return '<span style="color:#438eb9;font-weight:bold;">百家乐存送33%</span>';
					}else if(v =='14'){
						return '<span style="color:#438eb9;font-weight:bold;">BBIN电游存送100%</span>';
					}else if(v =='17'){
						return '<span style="color:#438eb9;font-weight:bold;">每日次存50%</span>';
					}else if(v =='18'){
						return '<span style="color:#438eb9;font-weight:bold;">存10元送18</span>';
					}
				}else{
					return '<span style="color:#393939;">未参加</span>';
				}
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '会员账号',
			dataIndex : 'uaccount',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			hidden : false,
			header : '会员姓名',
			dataIndex : 'urealname',
			width : 70,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '所属代理',
			dataIndex : 'proxyname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		},{
			header : '存款金额',
			dataIndex : 'amount',
			width : 70,
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '存款方式',
			dataIndex : 'paymethods',
			width : 70,
			renderer : function(v) {
				return Share.map(v, depositorderOL.PAYMETHODSMAP, '');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '存款时间',
			hidden : true,
			dataIndex : 'deposittime',
			width : 130,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			
			header : '创建时间',
			dataIndex : 'createDate',
			width : 130,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			header : '更新时间',
			dataIndex : 'updateDate',
			width : 130,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			hidden : true,
			header : '存入银行卡',
			width : 150,
			dataIndex : 'bankcard',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			hidden : true,
			header : '开户行名称',
			dataIndex : 'deposit',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			hidden : true,
			header : '开户人',
			dataIndex : 'openname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			hidden : true,
			header : '客服备注',
			dataIndex : 'kfremarks',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			hidden : true,
			header : '操作客服',
			dataIndex : 'kfname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			hidden : true,
			header : '客户操作时间',
			dataIndex : 'kfopttime',
			width : 130,
			renderer : function(v) {
				return v==null?'':new Date(v).format('Y-m-d H:i:s');
			},
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			hidden : true,
			header : '财务备注',
			dataIndex : 'cwremarks',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			hidden : true,
			header : '操作财务',
			dataIndex : 'cwname',
			editor : new Ext.form.TextField({
				style: 'border:0px;'
			})
		}, {
			hidden : true,
			header : '财务操作时间',
			dataIndex : 'cwopttime',
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
	depositorderOL.addAction = new Ext.Action({
		text : '新建',
		//text : '<fmt:message key="common.cancel"/>',
		iconCls : 'depositorderOL_add',
		handler : function() {
			depositorderOL.addWindow.setIconClass('depositorderOL_add'); // 设置窗口的样式
			depositorderOL.addWindow.setTitle('新增存款订单'); // 设置窗口的名称
			depositorderOL.addWindow.show().center(); // 显示窗口
			depositorderOL.formPanel.getForm().reset(); // 清空表单里面的元素的值.
			$("#poid").parent().parent().hide();
			depositorderOL.ccnoCombo.enable();
		}
	});
	/** 编辑 */
	depositorderOL.editAction = new Ext.Action({
		text : '编辑',
		iconCls : 'depositorderOL_edit',
		disabled : true,
		handler : function() {
			var record = depositorderOL.grid.getSelectionModel().getSelected();
			if(4!=record.data.status){
				Ext.Msg.alert('提示','只能编辑状态为“存款失败”的存款订单！');
				return;
			}
			depositorderOL.addWindow.setIconClass('depositorderOL_edit'); // 设置窗口的样式
			depositorderOL.addWindow.setTitle('编辑'); // 设置窗口的名称
			depositorderOL.addWindow.show().center();
			depositorderOL.formPanel.getForm().reset();
			depositorderOL.formPanel.getForm().loadRecord(record);
			var d = new Date();
			d.setTime(record.data.deposittime);
			$("#deposittimeStr").val(Ext.util.Format.date(d, "Y-m-d H:i:s"));
			$("#poid").parent().parent().show();
			depositorderOL.ccnoCombo.disable();
		}
	});
	/** 客服核实 */
	depositorderOL.auditKfAction = new Ext.Action({
		text : '客服核实',
		iconCls : 'depositorderOL_edit',
		disabled : true,
		handler : function() {
			var record = depositorderOL.grid.getSelectionModel().getSelected();
			if(2!=record.data.status){
				Ext.Msg.alert('提示','只能审核状态为“待客服核实”的存款订单！');
				return;
			}
			depositorderOL.auditKfWindow.setIconClass('depositorderOL_audit'); // 设置窗口的样式
			depositorderOL.auditKfWindow.setTitle('客服核实'); // 设置窗口的名称
			depositorderOL.auditKfWindow.show().center();
			depositorderOL.auditKfFormPanel.getForm().reset();
			depositorderOL.auditKfFormPanel.getForm().loadRecord(record);
			var d = new Date();
			d.setTime(record.data.deposittime);
			$("#deposittimeStrKf").val(Ext.util.Format.date(d, "Y-m-d H:i:s"));
		}
	});
	/** 财务审核 */
	depositorderOL.auditCwAction = new Ext.Action({
		text : '财务审核',
		iconCls : 'depositorderOL_edit',
		disabled : true,
		handler : function() {
			var record = depositorderOL.grid.getSelectionModel().getSelected();
			if(1!=record.data.status){
				Ext.Msg.alert('提示','只能审核状态为“待财务审核”的存款订单！');
				return;
			}
			depositorderOL.auditCwWindow.setIconClass('depositorderOL_audit'); // 设置窗口的样式
			depositorderOL.auditCwWindow.setTitle('财务审核'); // 设置窗口的名称
			depositorderOL.auditCwWindow.show().center();
			depositorderOL.auditCwFormPanel.getForm().reset();
			depositorderOL.auditCwFormPanel.getForm().loadRecord(record);
			var d = new Date();
			d.setTime(record.data.deposittime);
			$("#deposittimeStrCw").val(Ext.util.Format.date(d, "Y-m-d H:i:s"));
		}
	});
	/** 删除 */
	depositorderOL.deleteAction = new Ext.Action({
		text : '删除',
		iconCls : 'depositorderOL_delete',
		disabled : true,
		handler : function() {
			depositorderOL.delFun();
		}
	});
	
	/** 导出报表按钮 */
	depositorderOL.downAction = new Ext.Action({
			text : '导出报表',
			iconCls : 'Diskdownload',
			disabled : false,
			handler : function() {
				depositorderOL.downReport();
			}
	});
	/** 导出报表函数 */
	depositorderOL.downReport = function() {
		 //发送请求
		 window.open(depositorderOL.down + "?uaccount="+$("#olaccount").val()+"&startDate="+$("#depositorderOLstartDate").val()+"&endDate="+$("#depositorderOLendDate").val()+"&status="+$("#paystatus").prev().val()+"&platname="+$("#platname").prev().val()+"&platformorders="+$("#platformorders").val());
	};
	/** 补单按钮 */
	depositorderOL.makeOrderAction = new Ext.Action({
			text : '补单',
			iconCls : 'Diskdownload',
			disabled : true,
			handler : function() {
				depositorderOL.makeOrderFun();
			}
	});
	/** 补单函数 */
	depositorderOL.makeOrderFun = function() {
		var record = depositorderOL.grid.getSelectionModel().getSelected();
		Ext.Msg.confirm('提示', '确定要补单吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : depositorderOL.makeOrder + record.data.poid + ".do",
					callback : function(json) {
						depositorderOL.alwaysFun();
						depositorderOL.store.reload();
					}
				});
			}
		});
	};
	
	depositorderOL.platCombo = new Ext.form.ComboBox({
		hiddenName : 'platname',
		id : 'platname',
		name : 'platname',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
			fields : ['v', 't'],
			data : Share.map2Ary(depositorderOL.PLATNAME)
		}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		value : "",
		anchor : '99%',
		width:80
	});
	
	depositorderOL.paystatusCombo = new Ext.form.ComboBox({
		hiddenName : 'paystatus',
		id : 'paystatus',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['v', 't'],
					data : Share.map2Ary(depositorderOL.STATUSMAP)
				}),
		valueField : 'v',
		displayField : 't',
		value:'',
		allowBlank : false,
		editable : false,
		width:80
	});
	/**日期条件  -- 开始时间*/
 	depositorderOL.startDateField = new Ext.form.DateField({
  		id:'depositorderOLstartDate',
        showToday:true,
        format:'Y-m-d',
        invalidText:'日期输入非法',
        allowBlank : false,
        width:100 
 	});
 	/** 日期条件  -- 结束时间*/
 	depositorderOL.endDateField = new Ext.form.DateField({
  		id:'depositorderOLendDate',
        showToday:true,
        format:'Y-m-d',
        invalidText:'日期输入非法',
        allowBlank : false,
        width:100 
 	});
	/**搜索按钮*/
	depositorderOL.searchAction = new Ext.Action({
			text : '搜索',
			iconCls : 'Zoom',
			disabled : false,
			handler : function() {
				depositorderOL.searchFun();
			}
	});
	
	depositorderOL.searchParams = function(){
		var obj = {};
		if($("#olaccount").val()!=""){
			obj.uaccount = $("#olaccount").val();
		}
		var st=$("#paystatus").prev();
	    if($(st).val()!=""){
	     	obj.status=$(st).val();
	    }
	    if($("#depositorderOLstartDate").val()!=""){
			obj.startDate = $("#depositorderOLstartDate").val();
		}
		if($("#depositorderOLendDate").val()!=""){
			obj.endDate = $("#depositorderOLendDate").val();
		}
		if($("#platname").prev().val()!=""){
			obj.platname = $("#platname").prev().val();
		}
		if($("#platformorders").val()!=""){
			obj.platformorders = $("#platformorders").val();
		}
	    return obj;
	}
	
	depositorderOL.searchFun = function(){
		depositorderOL.store.load({params: depositorderOL.searchParams()});
	}
	
	depositorderOL.store.on('beforeload',function(store, options){
	    depositorderOL.store.baseParams = depositorderOL.searchParams();
	});
	
	/** 顶部工具栏 */
	depositorderOL.tbar = [
	/** depositorderOL.downAction,'-','&nbsp;',*/
	depositorderOL.makeOrderAction,'-','&nbsp;',
	'会员账号：',{id:'olaccount',xtype:'textfield',width:90},'-','&nbsp;',
	'交易号：',{id:'platformorders',xtype:'textfield',width:120},'-','&nbsp;',
	'平台名称：',depositorderOL.platCombo,'-','&nbsp;',
	'处理状态：',depositorderOL.paystatusCombo,'-','&nbsp;',
	'存款时间：',depositorderOL.startDateField,'~',depositorderOL.endDateField,'-',
	'&nbsp;',depositorderOL.searchAction
	];
	/** 底部工具条 */
	depositorderOL.bbar = new Ext.PagingToolbar({
		pageSize : depositorderOL.pageSize,
		store : depositorderOL.store,
		displayInfo : true,
		//plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		items : [ '-', '&nbsp;', depositorderOL.pageSizeCombo ]
	});
	/** 基本信息-表格 */
	depositorderOL.grid = new Ext.grid.EditorGridPanel({
		store : depositorderOL.store,
		colModel : depositorderOL.colModel,
		selModel : depositorderOL.selModel,
		tbar : depositorderOL.tbar,
		bbar : depositorderOL.bbar,
		autoScroll : 'auto',
		region : 'center',
		//autoExpandColumn :'remark',
		loadMask : true,
		viewConfig:{},
		stripeRows : true
	});
	depositorderOL.statusCombo = new Ext.form.ComboBox({
		fieldLabel : '状态',
		hiddenName : 'status',
		name : 'status',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
			fields : ['v', 't'],
			data : Share.map2Ary(depositorderOL.STATUSMAP)
		}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		value : "0",
		anchor : '99%'
	});
	
	depositorderOL.paymethodsCombo = new Ext.form.ComboBox({
		fieldLabel : '存款方式',
		hiddenName : 'paymethods',
		name : 'paymethods',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
			fields : ['v', 't'],
			data : Share.map2Ary(depositorderOL.PAYMETHODSMAP)
		}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		anchor : '99%'
	});
	depositorderOL.ccnoCombo = new Ext.form.ComboBox({
		fieldLabel : '公司收款银行卡',
		hiddenName : 'ccno',
		name : 'ccno',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
			fields : ['v', 't'],
			data : Share.map2Ary(depositorderOL.CCNOMAP)
		}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		anchor : '99%',
		listeners : {
			 'select': function(){
                 if(""!=depositorderOL.ccnoCombo.getValue()){
                	 var arr = depositorderOL.ccnoCombo.getValue().split("::");
                	 $("#bankname").val(arr[1]);
                	 $("#bankcard").val(arr[0]);
                	 $("#deposit").val(arr[2]);
                	 $("#openname").val(arr[3]);
                 }
             }
		}
	});
	
	depositorderOL.paymethodsReadOnlyCombo = new Ext.form.ComboBox({
		fieldLabel : '存款方式',
		hiddenName : 'paymethods',
		name : 'paymethods',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
			fields : ['v', 't'],
			data : Share.map2Ary(depositorderOL.PAYMETHODSMAP)
		}),
		valueField : 'v',
		displayField : 't',
		allowBlank : false,
		editable : false,
		readOnly : true,
		anchor : '99%'
	});
	/** 基本信息-详细信息的form */
	depositorderOL.formPanel = new Ext.form.FormPanel({
		autoScroll : true,
		frame : false,
		title : '存款订单信息',
		bodyStyle : 'padding:10px;border:0px',
		labelwidth : 70,
		defaultType : 'textfield',
		// 'poid','platformorders','uiid','paytyple','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','ordercontent','create_date','update_date','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','cwid','cwname','cwopttime'
		items : [ {
			xtype : 'hidden',
			fieldLabel : '流水号',
			name : 'platformorders'
		}, {
			xtype : 'hidden',
			fieldLabel : '收支类型',
			name : 'paytyple'
		}, {
			readOnly : true,
			fieldLabel : '存款订单ID',
			id : 'poid',
			name : 'poid'
		}, {
			fieldLabel : '存款会员账号',
			allowBlank : false,
			name : 'uaccount'
//		}, {
//			fieldLabel : '存款会员名称',
//			allowBlank : false,
//			name : 'urealname'
		}, depositorderOL.paymethodsCombo, depositorderOL.ccnoCombo,
		{
			readOnly : true,
			fieldLabel : '银行卡卡号',
			allowBlank : false,
			id : 'bankcard',
			name : 'bankcard'
		}, {
			readOnly : true,
			fieldLabel : '银行名称',
			allowBlank : false,
			id : 'bankname',
			name : 'bankname'
		}, {
			readOnly : true,
			fieldLabel : '开户行名称',
			allowBlank : false,
			id : 'deposit',
			name : 'deposit'
		}, {
			readOnly : true,
			fieldLabel : '开户人',
			allowBlank : false,
			id : 'openname',
			name : 'openname'
		}, {
			fieldLabel : '存款金额',
			allowBlank : false,
			name : 'amount'
		}, {
			fieldLabel : '存款时间',
			allowBlank : false,
			id : 'deposittimeStr',
			name : 'deposittimeStr',
			xtype:'datetimefield'
		}, {
			fieldLabel : '客服备注',
			allowBlank : false,
			maxLength : 200,
			xtype : 'textarea',
			name : 'kfremarks',
			anchor : '99%'
		}
		]
	});
	/** 编辑新建窗口 */
	depositorderOL.addWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 460,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ depositorderOL.formPanel ],
		buttons : [ {
			text : '保存',
			handler : function() {
				depositorderOL.saveFun();
			}
		}, {
			text : '重置',
			handler : function() {
				var form = depositorderOL.formPanel.getForm();
				var poid = form.findField("poid").getValue();
				var uaccount = form.findField("uaccount").getValue();
				form.reset();
				if (poid != '')
					form.findField("poid").setValue(poid);
				if (uaccount != '')
					form.findField("uaccount").setValue(uaccount);
			}
		} ]
	});
	/** 客服核实表单 */
	depositorderOL.auditKfFormPanel = new Ext.form.FormPanel({
		autoScroll : true,
		frame : false,
		title : '存款订单信息',
		bodyStyle : 'padding:10px;border:0px',
		labelwidth : 70,
		defaultType : 'textfield',
		// 'poid','platformorders','uiid','paytyple','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','ordercontent','create_date','update_date','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','cwid','cwname','cwopttime'
		items : [ {
			xtype : 'hidden',
			fieldLabel : '收支类型',
			name : 'paytyple'
		}, {
			xtype : 'hidden',
			fieldLabel : '流水号',
			name : 'platformorders'
		}, {
			readOnly : true,
			allowBlank : false,
			fieldLabel : '存款订单ID',
			id : 'poidKf',
			name : 'poid'
		}, {
			readOnly : true,
			allowBlank : false,
			fieldLabel : '会员账号',
			name : 'uaccount'
//		}, {
//			readOnly : true,
//			allowBlank : false,
//			fieldLabel : '会员名称',
//			name : 'urealname'
		}, depositorderOL.paymethodsReadOnlyCombo, 
		{
			readOnly : true,
			allowBlank : false,
			fieldLabel : '银行卡卡号',
			name : 'bankcard'
		}, {
			readOnly : true,
			fieldLabel : '银行名称',
			allowBlank : false,
			name : 'bankname'
		}, {
			readOnly : true,
			fieldLabel : '开户行名称',
			allowBlank : false,
			name : 'deposit'
		}, {
			readOnly : true,
			fieldLabel : '开户人',
			allowBlank : false,
			name : 'openname'
		}, {
			readOnly : true,
			allowBlank : false,
			fieldLabel : '存款金额',
			name : 'amount'
		}, {
			readOnly : true,
			allowBlank : false,
			fieldLabel : '存款时间',
			id : 'deposittimeStrKf',
			name : 'deposittimeStr',
			xtype:'datetimefield'
		}, {
			allowBlank : false,
			fieldLabel : '客服备注',
			maxLength : 200,
			xtype : 'textarea',
			name : 'kfremarks',
			anchor : '99%'
		}, {
			readOnly : true,
			allowBlank : false,
			fieldLabel : '财务备注',
			maxLength : 200,
			xtype : 'textarea',
			name : 'cwremarks',
			anchor : '99%'
		}
		]
	});
	/** 客服核实窗口 */
	depositorderOL.auditKfWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 460,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ depositorderOL.auditKfFormPanel ],
		buttons : [ {
			text : '核实通过',
			handler : function() {
				depositorderOL.auditKfFun(2);
			}
		}, {
			text : '取消',
			handler : function() {
				depositorderOL.auditKfWindow.hide();
			}
		} ]
	});
	/** 财务审核表单 */
	depositorderOL.auditCwFormPanel = new Ext.form.FormPanel({
		autoScroll : true,
		frame : false,
		title : '存款订单信息',
		bodyStyle : 'padding:10px;border:0px',
		labelwidth : 70,
		defaultType : 'textfield',
		// 'poid','platformorders','uiid','paytyple','ppid','paymethods','bankname','bankcard','openname','deposit','deposittime','amount','paystatus','status','remarks','ordercontent','create_date','update_date','uaccount','urealname','kfremarks','kfid','kfname','kfopttime','cwremarks','cwid','cwname','cwopttime'
		items : [ {
			xtype : 'hidden',
			fieldLabel : '收支类型',
			name : 'paytyple'
		}, {
			xtype : 'hidden',
			fieldLabel : '流水号',
			name : 'platformorders'
		}, {
			readOnly : true,
			allowBlank : false,
			fieldLabel : '存款订单ID',
			id : 'poidCw',
			name : 'poid'
		}, {
			readOnly : true,
			allowBlank : false,
			fieldLabel : '会员账号',
			name : 'uaccount'
//		}, {
//			readOnly : true,
//			allowBlank : false,
//			fieldLabel : '会员名称',
//			name : 'urealname'
		}, depositorderOL.paymethodsReadOnlyCombo, 
		{
			readOnly : true,
			allowBlank : false,
			fieldLabel : '银行卡卡号',
			name : 'bankcard'
		}, {
			readOnly : true,
			fieldLabel : '银行名称',
			allowBlank : false,
			name : 'bankname'
		}, {
			readOnly : true,
			fieldLabel : '开户行名称',
			allowBlank : false,
			name : 'deposit'
		}, {
			readOnly : true,
			fieldLabel : '开户人',
			allowBlank : false,
			name : 'openname'
		}, {
			readOnly : true,
			allowBlank : false,
			fieldLabel : '存款金额',
			name : 'amount'
		}, {
			readOnly : true,
			allowBlank : false,
			fieldLabel : '存款时间',
			id : 'deposittimeStrCw',
			name : 'deposittimeStr',
			xtype:'datetimefield'
		}, {
			readOnly : true,
			allowBlank : false,
			fieldLabel : '客服备注',
			maxLength : 200,
			xtype : 'textarea',
			name : 'kfremarks',
			anchor : '99%'
		}, {
			allowBlank : false,
			fieldLabel : '财务备注',
			maxLength : 200,
			xtype : 'textarea',
			name : 'cwremarks',
			anchor : '99%'
		}
		]
	});
	/** 财务审核窗口 */
	depositorderOL.auditCwWindow = new Ext.Window({
		layout : 'fit',
		width : 500,
		height : 480,
		closeAction : 'hide',
		plain : true,
		modal : true,
		resizable : true,
		items : [ depositorderOL.auditCwFormPanel ],
		buttons : [ {
			text : '审核通过',
			handler : function() {
				depositorderOL.auditCwFun(3);
			}
		}, {
			text : '审核不通过',
			handler : function() {
				depositorderOL.auditCwFun(4);
			}
		} ]
	});

	depositorderOL.alwaysFun = function() {
		Share.resetGrid(depositorderOL.grid);
		depositorderOL.deleteAction.disable();
		depositorderOL.editAction.disable();
		depositorderOL.auditKfAction.disable();
		depositorderOL.auditCwAction.disable();
	};
	depositorderOL.saveFun = function() {
		var form = depositorderOL.formPanel.getForm();
		if (!form.isValid()) {
			return;
		}
		Ext.Msg.confirm('提示', '确定已经电话核实过了吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : depositorderOL.save,
					params : form.getValues(),
					callback : function(json) {
						depositorderOL.addWindow.hide();
						depositorderOL.alwaysFun();
						depositorderOL.store.reload();
					}
				});
			}
		});
	};
	
	depositorderOL.delFun = function() {
		var record = depositorderOL.grid.getSelectionModel().getSelected();
		Ext.Msg.confirm('提示', '确定要删除这条记录吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : depositorderOL.del + record.data.poid + ".do",
					callback : function(json) {
						depositorderOL.alwaysFun();
						depositorderOL.store.reload();
					}
				});
			}
		});
	};

	depositorderOL.auditKfFun = function(result){
		var form = depositorderOL.auditKfFormPanel.getForm();
		if (!form.isValid()) {
			return;
		}
		Ext.Msg.confirm('提示', '确定已经电话核实过了吗?', function(btn, text) {
			if (btn == 'yes') {
				// 发送请求
				Share.AjaxRequest({
					url : depositorderOL.auditKf+result+".do",
					params : form.getValues(),
					callback : function(json) {
						depositorderOL.auditKfWindow.hide();
						depositorderOL.alwaysFun();
						depositorderOL.store.reload();
					}
				});
			}
		});
	};
	depositorderOL.auditCwFun = function(result){
		var form = depositorderOL.auditCwFormPanel.getForm();
		if (!form.isValid()) {
			return;
		}
		// 发送请求
		Share.AjaxRequest({
			url : depositorderOL.auditCw+result+".do",
			params : form.getValues(),
			callback : function(json) {
				depositorderOL.auditCwWindow.hide();
				depositorderOL.alwaysFun();
				depositorderOL.store.reload();
			}
		});
	};
	depositorderOL.myPanel = new Ext.Panel({
		id : '${id}' + '_panel',
		renderTo : '${id}',
		layout : 'border',
		boder : false,
		height : index.tabPanel.getInnerHeight() - 1,
		items : [ depositorderOL.grid ]
	});
</script>
