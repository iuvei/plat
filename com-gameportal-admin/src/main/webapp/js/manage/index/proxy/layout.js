Ext.ns("Ext.market.proxyindex"); // 自定义一个命名空间
index = Ext.market.proxyindex; // 定义命名空间的别名
index = {
	treeMenu : "/proxymanage/treeMenu.do"
}

// 设置主题
//Share.swapStyle();
var loginText = "<b>尊敬的 <samp style='color:#00F'>" + Ext.util.Cookies.get("PROXY_USERNAME") + "</samp> 代理用户 , 欢迎登陆,钱宝娱乐代理-管理平台</b>";
// 头部
index.headerPanel = new Ext.Panel({
	region : 'north',
	height : 60,
	border : false,
	margins : '0 0 0 0',
	collapseMode : 'mini',
	//collapsible : true,
	bodyStyle : 'background-color:transparent;',
	items:[{
		html:"<div style='background:url(/img/manage/headerBg02.png) repeat-x;height:35px;'><div style='line-height:35px;font-size:14px; color:#FFFFFF;padding-left: 10px;font-weight: bold;width:20%;float:left;'>钱宝娱乐代理管理平台</div><div style='color:#FFFFFF;line-height:35px;'>当前下线会员（<span id='meUserCount'>"+proxyUserCount+"</span>）人 &nbsp;&nbsp;&nbsp;当前钱包余额：<span id='proxyMoney'>"+proxyWallet+"</span>&nbsp;&nbsp;&nbsp;总盈亏：<span id='profitandloss'>"+profitandloss+"</span>&nbsp;&nbsp;&nbsp;占成：<span>"+proxyreturnscale+"</span>&nbsp;&nbsp;&nbsp;洗码占成：<span>"+proxyximascale+"</span>&nbsp;&nbsp;&nbsp;您的代理推广地址：<span id='proxy_url'>http://"+proxydomain+"</span></div></div>"
	},
	  new Ext.Toolbar({
	    height : 25,
	  	items:[new Ext.Toolbar.Fill(),
	  	 {
	  		xtype:'label',
			//iconCls: 'grid-add',
			id:'head-lb-1',
			cls:'welcome',
			html:loginText,
			margin:'0 20 0 20'
	  	},'-',
	  	{
	  		xtype:'button',
	  		html:'<b>退出</b>',
	  		handler: function(){
	  			window.location.href = "/proxy/logout.do?r="+(new Date()).getTime();
	        }
	  	},'-']
	  })]
});


index.menuTree = new Ext.tree.TreePanel({
	useArrows : true,
	// 设置为true将在树中使用Vista-style的箭头
	autoScroll : true,
	animate : true,
	// 设置为true以启用展开/折叠时的动画效果
	containerScroll : true,
	// 设置为true向ScrollManager注册此容器
	border : false,
	rootVisible : false,
	// 设置为false将隐藏root节点
	margins : '2 2 0 0',
	loader : new Ext.tree.TreeLoader({
		dataUrl : index.treeMenu,
		clearOnLoad : true
	}),
	root : {
		expanded : true,
		id : '0'
	},
	listeners : {
		'click' : function(node, e) { // 点击事件
			if (node.attributes.url) { // 如果是链接 node.isLeaf()
				Share.openTab(node, node.attributes.url);
			} else {
				e.stopEvent();
			}
		}
	}
});
// 菜单面板
index.menuPanel = new Ext.Panel({
	region : 'west',
	title : '功能菜单',
	//iconCls : 'computer',
	margins : '0 2 0 0',
	layout : 'fit',
	width : 180,
	minSize : 100,
	maxSize : 300,
	split : true,
	collapsible : true,
	tools : [ {
		id : 'refresh',
		handler : function() {
			index.menuTree.root.reload();
		}
	} ],
	items : [ index.menuTree ]
});

// tab主面板
index.tabPanel = new Ext.TabPanel({
	id : 'mainTabPanel',
	region : 'center',
	activeTab : 0,
	deferredRender : false,
	enableTabScroll : true,
	// bodyStyle:'height:100%',
	defaults : {
		layout : 'fit',
		autoScroll : true
	},
	plugins : new Ext.ux.TabCloseMenu({
		closeTabText : '关闭标签页',
		closeOtherTabsText : '关闭其他标签页',
		closeAllTabsText : '关闭所有标签页'
	}),
	items : [ {
		id : 'home',
		title : '我的主页',
		//iconCls : 'home',
		closable : false,
		autoScroll : true,
		
	} ],
	listeners : {
		'bodyresize' : function(panel, neww, newh) {
			// 自动调整tab下面的panel的大小
			var tab = panel.getActiveTab();
			var centerpanel = Ext.getCmp(tab.id + "_div_panel");
			if (centerpanel) {
				centerpanel.setHeight(newh - 2);
				centerpanel.setWidth(neww - 2);
			}
		}
	}
});

index.msgArea = new Ext.form.TextArea({
	autoScroll : true,
	readOnly : true,
	region : 'center'
});

index.msgPanel = new Ext.Panel({
	layout : 'border',
	title : '消息窗口',
	region : 'east',
	collapseMode : 'mini',
	width : 200,
	minSize : 100,
	maxSize : 300,
	// True将会使panel折叠并且会自动把展开/折叠
	// (expand/collapse)按钮渲染到顶部工具按钮区域
	collapsible : true,
	collapsed : true,
	// 如果当前panel被折叠为true
	split : true,
	tbar : [ {
		xtype : 'button',
		text : '清屏',
		iconCls : 'cancel',
		handler : function() {
			index.msgArea.reset();
		}
	} ],
	items : [ index.msgArea ]
});
// 初期化页面Layout
index.viewport = new Ext.Viewport({
	layout : 'border',
	items : [ index.headerPanel, index.menuPanel, index.tabPanel,
			index.msgPanel ]
});