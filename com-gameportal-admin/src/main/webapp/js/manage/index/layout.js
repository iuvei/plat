Ext.ns("Ext.market.index"); // 自定义一个命名空间
index = Ext.market.index; // 定义命名空间的别名
index = {
	treeMenu : "/manage/treeMenu.do"
}
//初始化声音类soundManager 
var soundManager = new SoundManager();
//soundManager.waitForWindowLoad = true;
//是否打开调试模式，打开话对viewport有一定影响
soundManager.debugMode = false;
//这个是soundManager提供的swf文件所在的文件夹
soundManager.url = '/js/sound/swf';
soundManager.beginDelayedInit();
soundManager.onload = function(){
	//这里面放入你要播放的声音
    //系统声音
    soundManager.createSound({
        id: 'cunkuanSound',
        url: '/js/sound/mp3/cunkuan.mp3',
        //autoLoad: true,//自动加载
        //multiShot: false,//true 在同一时刻只能有一个频段的声音
        //autoPlay: true //自动播放 这个是系统的背景音
        //volume: 100
    });
    //信息音
    soundManager.createSound({
        id: 'loginSound',
        url: '/js/sound/mp3/denglu.mp3'
        //volume: 100
    });
    //加入音
    soundManager.createSound({
        id: 'jiakuanSound',
        url: '/js/sound/mp3/jiakuan.mp3'
        //volume: 100
    });
    soundManager.createSound({
        id: 'koukuanSound',
        url: '/js/sound/mp3/koukuan.mp3'
        //volume: 100
    });
    soundManager.createSound({
        id: 'tikuanSound',
        url: '/js/sound/mp3/tikuan.mp3'
        //volume: 100
    });
}
//创建定时器，提示充值
index.task = {
		run:function(){
			jQuery.ajax({
				url: '/manage/alert.do?'+Math.random(),
				async: true,
				type: 'GET',
				dataType : "json",
				data: {},
				success : function(data){
					try {
						var responseJson=eval("("+data+")");
						if(responseJson.newWithdraw > 0){//新提款
							soundManager.play('tikuanSound');
							jQuery("#newWithdraw").html("<font color='red'>"+responseJson.newWithdraw+"</font>");
						}else{
							jQuery("#newWithdraw").html("0");
						}
						if(responseJson.notadd > 0){ //有加款未处理
							soundManager.play('jiakuanSound');
							jQuery("#notadd").html("<font color='red'>"+responseJson.notadd+"</font>");
						}else{
							jQuery("#notadd").html("0");
						}
						if(responseJson.notdeduct > 0){//有扣款未处理
							soundManager.play('koukuanSound');
							jQuery("#notdeduct").html("<font color='red'>"+responseJson.notdeduct+"</font>");
						}else{
							jQuery("#notdeduct").html("0");
						}
						if(responseJson.recharge > 0){//充值未处理
							soundManager.play('cunkuanSound');
							jQuery("#recharge").html("<font color='red'>"+responseJson.recharge+"</font>");
						}else{
							jQuery("#recharge").html("0");
						}
						
					} catch (e) {
					}
				},
				error: function(){
					
				}
			});
		},
		interval : 12000
}
// 设置主题
//Share.swapStyle();
var loginText = "<b>尊敬的 <samp style='color:#00F'>" + Ext.util.Cookies.get("USERNAME") + "</samp> 用户 , 欢迎登陆,钱宝娱乐现代游戏管理系统</b>";
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
		html:"<div style='background:url(/img/manage/headerBg02.png) repeat-x;height:35px;'><div style='line-height:35px;font-size:14px; color:#FFFFFF;padding-left: 10px;font-weight: bold;width:45%;float:left;'>钱宝娱乐现代游戏管理系统</div><div style='color:#FFFFFF;line-height:35px;'>未处理提款（<span id='newWithdraw'>0</span>）条 &nbsp;&nbsp;&nbsp;加款未处理（<span id='notadd'>0</span>）条&nbsp;&nbsp;&nbsp;扣款未处理（<span id='notdeduct'>0</span>）条&nbsp;&nbsp;&nbsp;存款未处理（<span id='recharge'>0</span>）条</div></div>"
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
	  			window.location.href = "/logout.do?r="+(new Date()).getTime();
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
//Ext.TaskMgr.start(index.task);//启动定时器