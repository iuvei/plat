<!DOCTYPE html>
<html>
  <head>
    <#include "/common/config.ftl">
	${meta}
    <title>${title}-绑定银行卡</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1, maximum-scale=1">
    <link rel="shortcut icon" href="/favicon.ico">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <link rel="stylesheet" href="//g.alicdn.com/msui/sm/0.6.2/css/sm.min.css">
    <link rel="stylesheet" href="//g.alicdn.com/msui/sm/0.6.2/css/sm-extend.min.css">
	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/li.css"/>
	<style type="text/css">
		header{height:2.5rem;}
		header .header_left{line-height: 2.5rem;width:7rem;font-size: .92rem;height: 2.5rem;}
		header .header_right{line-height: 2rem;margin-right: .2rem;font-size: .72rem;}
		.img_01{float: left;width:1.4rem;height:1.3rem}
		.qka_box{border:2px solid #ccc;width:96%;margin:0 auto;padding:.3rem;border-radius: 5px;margin-top:.5rem;}
		.qka_box .picker_style{width:88%;float: right;height: 1.3rem;font-size: .75rem;
				background-size: cover;border: 0;background: url(${zy_path}mstatic/images/ck_03.png) no-repeat center right;}
		.qka_box2{position: relative;border-radius: 0;}
		.qka_box2 .picker_style{background:url(${zy_path}mstatic/images/login_01.jpg) no-repeat center left; background-size: contain;
				padding-left:35%;}
		.qka_box2 span{display: block;position: absolute;left:12%;top:.4rem;font-size: .75rem;color:#96999b}
		p{margin:.2rem 0;color:#96999b}
		.qka_box3 .qka_box .picker_style{background: url(${zy_path}mstatic/images/login_03.jpg) no-repeat center left;background-size: contain;
		padding-left:55% ;}
		.qka_box3 span{left:22%}
		.qka_box3 .img3{margin-left: 2%;height:2.1rem;position: relative;top:.5rem;border:2px solid #ccc;padding:.3rem;}
		.qka_box3 .fr{font-size: .72rem;position: relative;top:1rem;}
		.qka_box4{padding:0;border:0;margin-bottom: 80px;}
		.qka_box4 input[type=button]{width:100%;height:2.2rem;font-size: .92rem;background-color: #417be7;color:#fff;border:0}
		
		.qka_box5{position: relative;border-radius: 0;}
		.qka_box5 .picker_style{background:url(${zy_path}mstatic/images/qk_01.png) no-repeat center left; background-size: contain;
				padding-left:35%;}
		.qka_box5 span{display: block;position: absolute;left:12%;top:.4rem;font-size: .75rem;color:#96999b}
		
		.qka_box6{position: relative;border-radius: 0;}
		.qka_box6 .picker_style{background:url(${zy_path}mstatic/images/qk_02.png) no-repeat center left; background-size: contain;
				padding-left:35%;}
		.qka_box6 span{display: block;position: absolute;left:12%;top:.4rem;font-size: .75rem;color:#96999b}
		
		
		.qka_box7{position: relative;border-radius: 0;}
		.qka_box7 .picker_style{background:url(${zy_path}mstatic/images/qk_03.png) no-repeat center left; background-size: contain;
				padding-left:35%;}
		.qka_box7 span{display: block;position: absolute;left:12%;top:.4rem;font-size: .75rem;color:#96999b}
		
		
		footer ul{list-style: none;padding:0;margin:0;}
		footer ul li img{width:1rem;height:1rem;margin-top:.2rem;}
		footer ul li p{font-size: .72rem;}
		.page, .page-group{
		 	overflow-x: hidden;
		    overflow-y:auto !important;
		}
		.page-group.page-current, .page.page-current {
		    overflow-x: hidden;
		    overflow-y:auto !important;
		}
		.f12{color: #cc0000;
		    margin-top: .1rem;
		    width: 100%;
		    clear: left;
		    font-size: .26rem;
		}
	</style>
  </head>
  <body style="background-color: #fff;">
    <div class="page-group">
        <div class="page page-current" style="background-color:#fff;overflow-y:auto;">
        	<!--后台头部-->
			<header class="ht_header">
				<div class="header_left">
					<span onclick="history.go(-1);">
						<img src="${zy_path}mstatic/images/ck_04.png" style="width:.5rem;position: relative;top:.05rem;left:-.2rem"/> 资金管理</span>
				</div>
				<div class="header_right">
					${userInfo.account} | <span id="layout" onclick="${action_path}loginOutmp.html">退出</span>
				</div>
			</header>
			<!--后台头部结束-->
			<!--选择银行-->
			<#if card??>
			<section class="wrap_01 overflow qka_box qka_box5">
				<input type="text"  placeholder="" class="picker_style" style="width:100%" value="${card.bankname}" readonly />
				<span>银行名称:</span>
			</section>
			<p class="f12" style="margin-left: 10px;">&nbsp;</p>
        	<!--选择省份-->
        	<section class="wrap_01 overflow qka_box qka_box6">
				<input type="text"  placeholder="" class="picker_style" style="width:100%" value="${card.province}${card.city}" readonly />
				<span>开户省市:</span>
			</section>
			<p class="f12" style="margin-left: 10px;">&nbsp;</p>
			<!--开户行-->
			<section class="wrap_01 overflow qka_box qka_box7">
				<input type="text"  placeholder="" class="picker_style" style="width:100%" value="${card.openingbank}" readonly/>
				<span>开户行:</span>
			</section>
			<p class="f12" style="margin-left: 10px;">&nbsp;</p>
			<#else>
			<section class="wrap_01 overflow qka_box">
				<img src=" ${zy_path}mstatic/images/qk_01.png" class="img_01"/>
				<input type="text" id='bank' placeholder="选择银行" class="picker_style"/>
			</section>
			<p class="f12" style="margin-left: 10px;">&nbsp;</p>
        	<!--选择省份-->
        	<section class="wrap_01 overflow qka_box">
				<img src="${zy_path}mstatic/images/qk_02.png" class="img_01"/>
				<input type="text" id='city' placeholder="选择省市" class="picker_style"/>
			</section>
			<p class="f12" style="margin-left: 10px;">&nbsp;</p>
			<!--开户行-->
			<section class="wrap_01 overflow qka_box qka_box2">
				<input type="text"  placeholder="" class="picker_style" style="width:100%" id="bankDeposit"/>
				<span>开户行:</span>
			</section>
			<p class="f12" style="margin-left: 10px;">&nbsp;</p>
			</#if>
			<section class="wrap_01 overflow qka_box qka_box2">
				<#if userInfo?? && userInfo.uname?if_exists>
                	 <input name="bankPerson" id="bankPerson" type="hidden" value="${userInfo.uname!}" readonly />
		             <input type="text"  placeholder="" class="picker_style" style="width:100%" value="${userInfo.uname}" id="bankDeposit" readonly/>
                <#else>
                	<input type="text"  placeholder="" class="picker_style" style="width:100%" id="bankPerson"/>
                </#if>
				<span>开户姓名:</span>
			</section>
			<p class="f12" style="margin-left: 10px;">&nbsp;</p>
			<!--银行账户-->
			<#if card??>
				<section class="wrap_01 overflow qka_box qka_box2">
					<input type="text"  placeholder="" class="picker_style" style="width:100%" value="${card.cardnumber}"/>
					<span>银行卡号:</span>
				</section>
				<p class="f12" style="margin-left: 10px;">&nbsp;</p>
			<#else>
				<section class="wrap_01 overflow qka_box qka_box2">
					<input type="text"  placeholder="" class="picker_style" style="width:100%" id="cardNum"/>
					<span>银行卡号:</span>
				</section>
				<p class="f12" style="margin-left: 10px;">&nbsp;</p>
			</#if>
			<#if !card??>
				<!--确认卡号-->
				<section class="wrap_01 overflow qka_box qka_box2">
					<input type="text"  placeholder="" class="picker_style" style="width:100%" id="cardNumOK"/>
					<span>确认卡号:</span>
				</section>
				<p class="f12" style="margin-left: 10px;">&nbsp;</p>
				<!--验证码-->
				<div class="overflow wrap_01 qka_box3">
					<section class="wrap_01 overflow qka_box qka_box2 fl" style="width:50%">
						<input type="text"  placeholder="" class="picker_style" style="width:100%" id="vcode" maxlength="4"/>
						<span>验证码:</span>
					</section>
					<img id="imgCodeAgent" src="${action_path}validationCode/agentcode.do" class="img3 fl" onclick="imgCode2();">
					<a href="javascript:;" class="fr" onclick="imgCode2();">刷新验证码</a>
				</div>
				<p class="f12" style="margin-left: 10px;">&nbsp;</p>
				<div class="qka_box qka_box4">
					<input class="grey_2 quxiao" href="javascript:;" type="button" value="提 交" id="submitBtn"/>
				</div>
			</#if>
        	<footer>
				<ul>
					<li><a href="/indexmp.html"><img src="${zy_path}mstatic/images/icon_01.png" /><p>首页</p></a></li>
					<li><a href="/favomp.html"><img src="${zy_path}mstatic/images/icon_02.png" /><p>活动</p></a></li>
					<li><a href="http://chat6.livechatvalue.com/chat/chatClient/chatbox.jsp?companyID=672531&configID=47081&jid=3980564989" target="_blank"><img src="${zy_path}mstatic/images/icon_03.png" /><p>客服</p></a></li>
					<li><a href="/mobile/accountmp.html"><img src="${zy_path}mstatic/images/icon_04.png" /><p>资金管理</p></a></li>
				</ul>
			</footer>
        </div>
    </div>
	
    <script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
    <script type='text/javascript' src='//g.alicdn.com/msui/sm/0.6.2/js/sm.min.js' charset='utf-8'></script>
    <script type='text/javascript' src='//g.alicdn.com/msui/sm/0.6.2/js/sm-extend.min.js' charset='utf-8'></script>
    <script type="text/javascript" src="${zy_path}mstatic/js/myjs/code.js" charset="utf-8"></script>
    <script type="text/javascript" src="${zy_path}mstatic/js/base.js" charset="utf-8"></script>
	<script>
	   var bank = ['中国工商银行', '中国建设银行', '中国银行', '中国农业银行','交通银行','招商银行','中国邮政储蓄银行','中信银行','光大银行','民生银行','兴业银行',
        '华夏银行','上海浦东发展银行','广东发展银行','平安银行','国家开发银行','中国进出口银行','中国农业发展银行'];
		$("#bank").picker({
                title: "请选择选择银行",
                cols: [
                    {
                        textAlign: 'center',
                        values: bank,
                    }
                ],
                onClose: function(picker) {
                }
            });
		
		 	//省
            var province = ['北京','上海','天津','重庆','四川','贵州','云南','西藏','河南','湖北','湖南','广东','广西','陕西','甘肃','青海','宁夏','新疆','河北',
          		'山西','内蒙古','江苏','浙江','安徽','福建','江西','山东','辽宁','吉林','黑龙江','海南','台湾','香港','澳门'
            ];
            //市区
            var city = [
                 ['市辖区', '县'],
                 ['市辖区', '县'],
                 ['市辖区', '县'],
                 ['市辖区', '县'],
                 ['成都市','自贡市','攀枝花市','泸州市','德阳市','绵阳市','广元市','遂宁市','内江市','乐山市','南充市','眉山市','宜宾市','广安市','达州市','雅安市','巴中市','资阳市','阿坝藏族羌族自治州','甘孜藏族自治州','凉山彝族自治州'],
                 ['贵阳市','六盘水市','遵义市','安顺市','铜仁地区','黔西南布依族苗族自治州','毕节地区','黔东南苗族侗族自治州','黔南布依族苗族自治州'],
                 ['昆明市','曲靖市','玉溪市','保山市','昭通市','丽江市','思茅市','临沧市','楚雄彝族自治州','红河哈尼族彝族自治州','文山壮族苗族自治州','西双版纳傣族自治州',
                 '大理白族自治州','德宏傣族景颇族自治州','怒江傈僳族自治州','迪庆藏族自治州'],
                 ['拉萨市','昌都地区','山南地区','日喀则地区','那曲地区','阿里地区','林芝地区'],
                 ['郑州市','开封市','洛阳市','平顶山市','安阳市','鹤壁市','新乡市','焦作市','濮阳市','许昌市','漯河市','三门峡市','南阳市','商丘市','信阳市','周口市','驻马店市'],
                 ['武汉市','黄石市','十堰市','宜昌市','襄樊市','鄂州市','荆门市','孝感市','荆州市','黄冈市','咸宁市','随州市','恩施土家族苗族自治州','省直辖行政单位'],
                 ['长沙市','株洲市','湘潭市','衡阳市','邵阳市','岳阳市','常德市','张家界市','益阳市','郴州市','永州市','怀化市','娄底市','湘西土家族苗族自治州'],
                 ['广州市','韶关市','深圳市','珠海市','汕头市','佛山市','江门市','湛江市','茂名市','肇庆市','惠州市','梅州市','汕尾市','河源市','阳江市','清远市','东莞市','中山市','潮州市','揭阳市','云浮市'],
                 ['南宁市','柳州市','桂林市','梧州市','北海市','防城港市','钦州市','贵港市','玉林市','百色市','贺州市','河池市','来宾市','崇左市'],
                 ['西安市','铜川市','宝鸡市','咸阳市','渭南市','延安市','汉中市','榆林市','安康市','商洛市'],
                 ['兰州市','嘉峪关市','金昌市','白银市','天水市','武威市','张掖市','平凉市','酒泉市','庆阳市','定西市','陇南市','临夏回族自治州','甘南藏族自治州'],
                 ['西宁市','海东地区','海北藏族自治州','黄南藏族自治州','海南藏族自治州','果洛藏族自治州','玉树藏族自治州','海西蒙古族藏族自治州'],
                 ['银川市','石嘴山市','吴忠市','固原市','中卫市'],
                 ['乌鲁木齐市','克拉玛依市','吐鲁番地区','哈密地区','昌吉回族自治州','博尔塔拉蒙古自治州','巴音郭楞蒙古自治州','阿克苏地区','克孜勒苏柯尔克孜自治州','喀什地区','和田地区','伊犁哈萨克自治州','塔城地区','阿勒泰地区','省直辖行政单位'],
                 ['石家庄市','唐山市','秦皇岛市','邯郸市','邢台市','保定市','张家口市','承德市','沧州市','廊坊市','衡水市'],
                 ['太原市','大同市','阳泉市','长治市','晋城市','朔州市','晋中市','运城市','忻州市','临汾市','吕梁市'],
                 ['呼和浩特市','包头市','乌海市','赤峰市','通辽市','鄂尔多斯市','呼伦贝尔市','巴彦淖尔市','乌兰察布市','兴安盟','锡林郭勒盟','阿拉善盟'],
                 ['南京市','无锡市','徐州市','常州市','苏州市','南通市','连云港市','淮安市','盐城市','扬州市','镇江市','泰州市','宿迁市'],
                 ['杭州市','宁波市','温州市','嘉兴市','湖州市','绍兴市','金华市','衢州市','舟山市','台州市','丽水市'],
                 ['合肥市','芜湖市','蚌埠市','淮南市','马鞍山市','淮北市','铜陵市','安庆市','黄山市','滁州市','阜阳市','宿州市','巢湖市','六安市','亳州市','池州市','宣城市'],
                 ['福州市','厦门市','莆田市','三明市','泉州市','漳州市','南平市','龙岩市','宁德市'],
                 ['南昌市','景德镇市','萍乡市','九江市','新余市','鹰潭市','赣州市','吉安市','宜春市','抚州市','上饶市'],
                 ['济南市','青岛市','淄博市','枣庄市','东营市','烟台市','潍坊市','济宁市','泰安市','威海市','日照市','莱芜市','临沂市','德州市','聊城市','滨州市','荷泽市'],
                 ['沈阳市','大连市','鞍山市','抚顺市','本溪市','丹东市','锦州市','营口市','阜新市','辽阳市','辽阳市','铁岭市','朝阳市','葫芦岛市'],
                 ['长春市','吉林市','四平市','辽源市','通化市','白山市','松原市','白城市','延边朝鲜族自治州'],
                 ['哈尔滨市','齐齐哈尔市','鸡西市','鹤岗市','双鸭山市','大庆市','伊春市','佳木斯市','七台河市','牡丹江市','黑河市','绥化市','大兴安岭地区'],
                 ['海口市','三亚市','省直辖县级行政单位'],
                 [''],
                 [''],
                 ['']
            ];
            //选择城市
            $("#city").picker({
                title: "请选择城市",
                cols: [
                    {
                        textAlign: 'left',
                        values:province ,
                        onChange: function (picker) {
                            var provinceIndex = picker.cols[0].activeIndex ;
                            if(picker.cols[1].replaceValues){
                                picker.cols[1].replaceValues(city[provinceIndex]);
                            }
                        }
                    },
                    {
                        values: city[0],
                        width: 120,
                    }
                ],
                onClose: function(picker) {
                }
            });
            
        	$("#submitBtn").click(function(){
			var banks = $("#bank").data("picker");
			var citys = $("#city").data("picker");
			var bankDeposit = $("#bankDeposit");
			var bankPerson = $("#bankPerson");
			var cardNum = $("#cardNum");
			var cardNumOK = $("#cardNumOK");
			var vcode = $("#vcode");
			if(banks.value ==undefined || banks.value[0] ==''){
				$("#bank").parent().next().html('* 请选择银行!');
				$("#bank").focus();
				return;
			}else{
				$("#bank").parent().next().html('&nbsp;');
			}
			if(citys.value == undefined || citys.value[0] ==''){
				$("#city").parent().next().html("* 请选择开户省市!");
				$("#city").focus();
				return;
			}else{
				$("#city").parent().next().html("&nbsp;");
			}
			if(bankDeposit.val() == '' || bankDeposit.val().length == 0){
				bankDeposit.parent().next().html("* 请输入开户行!");
				bankDeposit.focus();
				return;
			}else{
				bankDeposit.parent().next().html("&nbsp;");
			}
			
			if(bankPerson.val() == '' || bankPerson.val().length == 0){
				bankPerson.parent().next().html("* 请输入开户姓名!");
				$("#bankPerson").focus();
				return;
			}else{
				bankPerson.parent().next().html("&nbsp;");
			}
			
			if(base.valid.isNanme(bankPerson.val()) == false){
				bankPerson.parent().next().html("* 开户姓名输入不正确!");
				bankPerson.focus();
				return;
			}else{
				bankPerson.parent().next().html("&nbsp;");
			}
			
			if(cardNum.val() == '' || cardNum.val().length == 0){
				cardNum.parent().next().html("* 请输入银行卡号!");
				$("#cardNum").focus();
				return;
			}else if(cardNum.val().length <15){
				cardNum.parent().next().html("* 请输入正确的银行卡号!");
				$("#cardNum").focus();
				return;
			}else{
				cardNum.parent().next().html("&nbsp;");
			}
			
			if(cardNumOK.val() == '' || cardNumOK.val().length == 0){
				cardNumOK.parent().next().html("* 请确认银行卡号!");
				$("#cardNumOK").focus();
				return;
			}else{
				cardNumOK.parent().next().html("&nbsp;");
			}
			
			if(cardNumOK.val() != cardNum.val()){
				cardNumOK.parent().next().html("* 两次卡号不一致，请检查后重新输入!");
				$("#cardNumOK").focus();
				return;
			}else{
				cardNumOK.parent().next().html("&nbsp;");
			}
			
			if(vcode.val() == '' || vcode.val().length == 0){
				vcode.parent().parent().next().html("* 请输入验证码!");
				$("#vcode").focus();
				return;
			}else{
				vcode.parent().parent().next().html("&nbsp;");
			}
			
			if($("#submitBtn").hasClass('_click')){
				return;
			}
			$("#submitBtn").val('正在提交...');
			$("#submitBtn").addClass('_click');
			$.ajax({
				type : "POST",
				url : "/mobile/saveCardPackage.do",
				data : {
					bank : encodeURI(banks.value[0]),
					province : encodeURI(citys.value[0]),
					city : encodeURI(citys.value[1]),
					bankDeposit : encodeURI(bankDeposit.val()),
					bankPerson : encodeURI(bankPerson.val()),
					cardNum : cardNum.val(),
					code : vcode.val()
				},
				async : true,
				success : function(data) {
					data = eval('('+data+')');
					if (data.code == "1") {
				 		vcode.parent().parent().next().html("* 恭喜，您的银行卡绑定成功!");
				 		setTimeout('refresh()',1000);  
					} else if (data.code == "0") {
						vcode.parent().parent().next().html("* "+data.info);
						$("#submitBtn").val('提 交');
						$("#submitBtn").removeClass('_click');
					}else if (data.code == "9") {
						$("#submitBtn").val('提 交');
						vcode.parent().parent().next().html("* "+data.info);
						$("#submitBtn").removeClass('_click');
						$("#vcode").focus();
					} else {
						vcode.parent().parent().next().html("* 网络异常，请稍后再试!");
						$("#submitBtn").val('提 交');
						$("#submitBtn").removeClass('_click');
					}
				}
			})
		});
            
		function refresh(){
			window.location.reload();
		}
	</script>
  </body>
</html>