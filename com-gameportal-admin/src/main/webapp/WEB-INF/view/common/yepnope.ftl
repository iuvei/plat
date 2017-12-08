<script type="text/javascript" src="/js/loader/yepnope.min.js"></script>
<script type="text/javascript">
	pagesize = eval('(${fields.pagesize})');
	yepnope({
			load : [ //extjs
			         "/js/ext/resources/css/ext-all.css",
			         "/js/ext/resources/css/icon.css",
			         "/js/ext/examples/ux/fileuploadfield/css/fileuploadfield.css",
			         "/img/Spinner.css",
			         "/js/ext/adapter/ext/ext-base.js",
					 "/js/ext/ext-all.js", 
					 "/js/ext/locale/ext-lang-zh_CN.js", 
					 "/js/ext/examples/ux/TabCloseMenu.js",
					 "/js/ext/examples/ux/SearchField.js",
					 "/js/ext/examples/ux/ProgressBarPager.js",
					 "/js/ext/examples/ux/ExtMD5.js",
					 "/js/ext/examples/ux/fileuploadfield/FileUploadField.js",
					 // 日期控件
					 "/js/ext/Spinner.js",
					 "/js/ext/SpinnerField.js",
					 "/js/ext/DateTimeField.js",
					 //声音
					 "/js/sound/soundmanager2-min.js",
					 //通用
					 "/js/ext/Ext.ux.override.js",
					 "/js/loader/share.js",
					 //Cookies
					 "/js/ext/src/util/Cookies.js",
					 //报表
					 "/js/reportform/amline/swfobject.js",
					 //jquery
					 "/js/ext/adapter/jquery/jquery-1.7.1.min.js", 
					 "/js/ext/adapter/jquery/jquery.json-2.2-min.js", 
					 "/js/ext/adapter/jquery/jquery.center-min.js",
					 ],
			complete : function() {
				Ext.QuickTips.init();
				Ext.form.Field.prototype.msgTarget = 'title';//qtip,title,under,side
				Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
				ctx = "";
				Ext.BLANK_IMAGE_URL = '/js/ext/resources/images/default/s.gif';
				}
			});
</script>