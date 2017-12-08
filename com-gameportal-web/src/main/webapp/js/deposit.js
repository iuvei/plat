var regx = /^[1-9]+[0-9]*]*$/;
var actionPath ="/";
        $(function () {
            //选择存款方式
            $("#depositWay").find("li").click(function () {
                if ($(this).hasClass("active")) return false;

                $(this).addClass("active").siblings("li").removeClass("active");
                var _index = $(this).index();
                $("#depositWayLayer").children().eq(_index).removeClass("ui-none").siblings().addClass("ui-none");
            });
            
            /**
            $("#banks").find("li").click(function(){
    			//微信支付
    			if($(this).text().indexOf("微信")>-1){
    				$.confirm("请扫此二维码加入好友后进行付款")
    				.find(".btn").remove()
    				.end().find("p").removeClass("color-dark").addClass("color-brown ui-alignCenter").css({"min-height":"0"})
    				.after("<p class=\"size-big color-brown ui-alignCenter\">请只对本网站提供的二维码进行付款哦！</p>")
    				.after("<img src=\"/static/images/user/capital/qbwx.jpg\" style=\"display:block;width:180px;height:185px;margin:0 auto;\" alt=\"添加微信好友\" />");
    			}
    		});
    		*/

            //选择在线支付存款通道
            $("#payWay").find("li").click(function () {
                $(this).find(".ui-radio").addClass("checked")
                    .closest("li").siblings().find(".ui-radio").removeClass("checked");
            });
            
            $("#wxpayWay").find("li").click(function () {
                $(this).find(".ui-radio").addClass("checked")
                    .closest("li").siblings().find(".ui-radio").removeClass("checked");
            });
            
            $("#alipayWay").find("li").click(function () {
                $(this).find(".ui-radio").addClass("checked")
                    .closest("li").siblings().find(".ui-radio").removeClass("checked");
            });
            
            $("#qqWay").find("li").click(function () {
                $(this).find(".ui-radio").addClass("checked")
                    .closest("li").siblings().find(".ui-radio").removeClass("checked");
            });
            
            
            $(".ul-data").delegate("li","click",function(e){
            	if($(this).attr("data") =='1' ||  $(this).attr("data") =='2' ||$(this).attr("data") =='3' 
            		|| $(this).attr("data") =='4' ||  $(this).attr("data") =='8' ||  $(this).attr("data") =='9' ){
            		//$(this).closest(".ui-ddl").next("span").html('&emsp;该优惠只针对PT平台老虎机游戏');
    				return;
    			}else if($(this).attr("data") =='5'){
    				//$(this).closest(".ui-ddl").next("span").html('&emsp;限于老虎机电子游戏,投注10倍流水');
    				return;
    			}else if($(this).attr("data") =='6'){
    				//$(this).closest(".ui-ddl").next("span").html('&emsp;限于老虎机电子游戏,投注15倍流水');
    				return;
    			}else{
    				//$(this).closest(".ui-ddl").next("span").html('&emsp;有新的优惠活动可以参加哦^_^');
    				return;
    			}
            });
            
            $("#banks").delegate("li","click",function(e){
            	var selV = $(this).attr("data");  //值
				var selT = $(this).text();  //text
				if(selV==""){ // 实际未选择
					//$('#depositBankname').val('');
					$('#cardNum').val('');
					$('#bankPerson').val('');
					$('#depositOpenbank').val('');
				}else{
					//$('#depositBankname').val(selT);
					var arr = selV.split('::');
					var cardNum=arr[0];
					if(selT=="支付宝" || selT=="钱宝微信"){
						$('#cardNum').val(cardNum);
						$("#nickNameDiv").removeClass("ui-none");
					}else{
						$('#cardNum').val(cardNum.replace(cardNum.substring(0,cardNum.length-4), '***************'));
						$("#nickNameDiv").addClass("ui-none");
					}
					$('#showBankCard').val(cardNum);
					$('#bankPerson').val(arr[1]);
					$('#depositOpenbank').val(arr[2]);
				}
            });
            
            $(".ul-data").delegate("li","click",function(e){
            	var selV = $(this).attr("data");
            	debugger;
				if(selV !=""){
					$(this).closest(".ui-inputBox").next().find(":checkbox").attr("checked",true);
				}else{
					$(this).closest(".ui-inputBox").next().find(":checkbox").removeAttr("checked");
				}
				
            });
            
            $("#cdepositOther_zxzfb").click(function(){
				var cardNum = $("#cccid_zxzfb");
				var depositBankname = $("#payName_zxzfb");
				var depositFigure = $("#cotherFigure_zxzfb");
				var ordernumber = $("#ordernumber");
				var vcode = $("#cotherCode_zxzfb");
			
				if(depositBankname.val() == ''){
					$("#payName_zxzfb_tip").html("付款支付宝姓名。");
					$("#payName_zxzfb_tip").removeClass("ui-none");
					return;
				}else{
					$("#payName_zxzfb_tip").addClass("ui-none");
				}
				
				if(depositFigure.val() == '' || depositFigure.val().length ==0){
					$("#cotherFigure_zxzfb_tip").html("请输入您要充值的金额。");
					$("#cotherFigure_zxzfb_tip").removeClass("ui-none");
					$("#cotherFigure_zxzfb").focus();
					return;
				}else if(!regx.test(depositFigure.val())){
					$("#cotherFigure_zxzfb_tip").html("存款金额只允许输入整数。");
					$("#cotherFigure_zxzfb_tip").removeClass("ui-none");
					$("#cotherFigure_zxzfb").focus();
					return;
				}else if(parseInt(depositFigure.val())<10){
					$("#cotherFigure_zxzfb_tip").html("支付宝在线转账最低金额10元。");
					$("#cotherFigure_zxzfb_tip").removeClass("ui-none");
					$("#cotherFigure_zxzfb").focus();
					return;
				}else{
					$("#cotherFigure_zxzfb_tip").addClass("ui-none");
				}
				if(vcode.val() =='' || vcode.val().length == 0){
					$("#cotherCode_zxzfb_tip").html("请输入验证码。");
					$("#cotherCode_zxzfb_tip").removeClass("ui-none");
					$("#cotherCode_zxzfb").focus();
					return;
				}else{
					$("#cotherCode_zxzfb_tip").addClass("ui-none");
				}
				
				var tbk_company_memo = $("#ctbk_memo_zxzfb").attr("data");
				if(tbk_company_memo != ''){
					if(!$("#cmemocheck_zxzfb").is(":checked")){
						$.msg('您选择了参加优惠活动，请阅读并优惠协议。',3);
						$("#company_memocheck").focus();
						return;
					}
					if(!submitConfirm()){
						return;
					}
				}
				
				
				if(!vaidate(tbk_company_memo,depositFigure.val(),depositFigure)){
					return;
				}
				
				$("#cdepositOther_zxzfb").val('正在提交...');
				$("#cdepositOther_zxzfb").attr('disabled',true);
				$.ajax({
					url: actionPath+"manage/capital/saveDeposit.do",
					async: true,
					type: 'POST',
					dataType : "json",
					data: {
						ccid : cardNum.val(),
						depositFigure : depositFigure.val(),
						hd : tbk_company_memo,
						ordernumber:ordernumber.val(),
						nickName:depositBankname.val(),
						code : vcode.val()
					},
					success: function(data){
					 	if (data.code == "1") {
					 		$.remind("恭喜您，支付宝转账申请已提交！",success_callback);
							imgCode5();
						} else if (data.code == "0") {
							$.msg(data.info,2);
							$("#cdepositOther_zxzfb").val('确认存款');
							$("#cdepositOther_zxzfb").attr('disabled',false);
						} else if (data.code == "9") {
							$("#cdepositOther_zxzfb").val('确认存款');
							$("#cdepositOther_zxzfb").attr('disabled',false);
							$("#cotherCode_zxzfb_tip").html(data.info);
							$("#cotherCode_zxzfb_tip").removeClass("ui-none");
						}else {
							$.msg('网络异常，请稍后再试！',2);
							$("#cdepositOther_zxzfb").val('确认存款');
							$("#cdepositOther_zxzfb").attr('disabled',false);
						}
					},
					error: function(){
						$.msg('网络异常，请稍后再试！',2);
						$("#cdepositOther_zxzfb").val('确认存款');
						$("#cdepositOther_zxzfb").attr('disabled',false);
					}	
				});	
			});
            
            $("#depositATM_sub").click(function(){
				var cardNum = $("#cardNum");
				var bankPerson = $("#bankPerson");
				var depositBankname = $("#depositBankname");
				var depositOpenbank = $("#depositOpenbank");
				var depositFigure = $("#depositFigure");
				var ordernumber = $("#ordernumber");
				var vcode = $("#vcode");
				if(depositBankname.attr("data") =='' || depositBankname.attr("data").length == 0){
					$("#depositBankname_tip").html("请选择存款的银行。");
					$("#depositBankname_tip").removeClass("ui-none");
					return;
				}else{
					$("#depositBankname_tip").addClass("ui-none");
				}
				if(cardNum.val() == '' || cardNum.val().length ==0){
					$("#cardNum_tip").html("存入卡号不能为空。");
					$("#cardNum_tip").removeClass("ui-none");
					return;
				}else{
					$("#cardNum_tip").addClass("ui-none");
				}
				if(bankPerson.val() == '' || bankPerson.val().length == 0){
					$("#bankPerson_tip").html("开户人姓名不能为空。");
					$("#bankPerson_tip").removeClass("ui-none");
					return;
				}else{
					$("#bankPerson_tip").addClass("ui-none");
				}
				if(depositBankname.text()=='支付宝' && $("#nickName").val() ==''){
					$("#nickName_tip").html("请输入付款支付宝昵称。");
					$("#nickName_tip").removeClass("ui-none");
					$("#nickName").focus();
					return;
				}else{
					$("#nickName_tip").addClass("ui-none");
				}
				if(depositBankname.text()=='钱宝微信' && $("#nickName").val() ==''){
					$("#nickName_tip").html("请输入付款微信昵称。");
					$("#nickName_tip").removeClass("ui-none");
					$("#nickName").focus();
					return;
				}else{
					$("#nickName_tip").addClass("ui-none");
				}
				if(depositFigure.val() == '' || depositFigure.val().length ==0){
					$("#depositFigure_tip").html("请输入您要充值的金额。");
					$("#depositFigure_tip").removeClass("ui-none");
					$("#depositFigure").focus();
					return;
				}else if(!regx.test(depositFigure.val())){
					$("#depositFigure_tip").html("存款金额只允许输入整数。");
					$("#depositFigure_tip").removeClass("ui-none");
					$("#depositFigure").focus();
					return;
				}else if(parseInt(depositFigure.val())<10){
					$("#depositFigure_tip").html("公司入款最低金额10元。");
					$("#depositFigure_tip").removeClass("ui-none");
					$("#depositFigure").focus();
					return;
				}else{
					$("#depositFigure_tip").addClass("ui-none");
				}
				if(vcode.val() =='' || vcode.val().length == 0){
					$("#vcode_tip").html("请输入验证码。");
					$("#vcode_tip").removeClass("ui-none");
					$("#vcode").focus();
					return;
				}else{
					$("#vcode_tip").addClass("ui-none");
				}
				
				var tbk_company_memo = $("#tbk_company_memo").attr("data");
				if(tbk_company_memo != ''){
					if(!$("#company_memocheck").is(":checked")){
						$.msg('您选择了参加优惠活动，请阅读并优惠协议。',3);
						$("#company_memocheck").focus();
						return;
					}
					if(!submitConfirm()){
						return;
					}
				}
				
				
				if(!vaidate(tbk_company_memo,depositFigure.val(),depositFigure)){
					return;
				}
				
				$("#depositATM_sub").val('正在提交...');
				$("#depositATM_sub").attr('disabled',true);
				$.ajax({
					url: actionPath+"manage/capital/saveDeposit.do",
					async: true,
					type: 'POST',
					dataType : "json",
					data: {
						cardNum : cardNum.val(),
						bankPerson : bankPerson.val(),
						depositBankname : depositBankname.text(),
						depositOpenbank : depositOpenbank.val(),
						depositFigure : depositFigure.val(),
						hd : tbk_company_memo,
						ordernumber:ordernumber.val(),
						nickName:$("#nickName").val(),
						code : vcode.val()
					},
					success: function(data){
					 	if (data.code == "1") {
					 		var channel = depositBankname.text();
					 		if(channel =='支付宝'){
					 			$("#orderNO_return").text(data.ordernumber);
					 			$("#depositATM_sub").val('确认存款');
								$("#depositATM_sub").attr('disabled',false);
								//$("#depositWayLayer").addClass("ui-none");
								$("#depositWayLayer").children().eq(0).addClass("ui-none")
								$("#showMwssage").removeClass("ui-none");
								$("#showMwssage").find(".alipay").removeClass("ui-none");
								$("#showMwssage").find(".wechat").addClass("ui-none");
								$("#showMwssage").find(".bankCard").addClass("ui-none");
					 		}else if(channel =='钱宝微信'){
					 			$("#orderNO_return").text(data.ordernumber);
					 			$("#depositATM_sub").val('确认存款');
								$("#depositATM_sub").attr('disabled',false);
								//$("#depositWayLayer").addClass("ui-none");
								$("#depositWayLayer").children().eq(0).addClass("ui-none")
								$("#showMwssage").removeClass("ui-none");
								$("#showMwssage").find(".alipay").addClass("ui-none");
								$("#showMwssage").find(".wechat").removeClass("ui-none");
								$("#showMwssage").find(".bankCard").addClass("ui-none");
					 		}else{
								$("#depositWayLayer").children().eq(0).addClass("ui-none")
								$("#showMwssage").removeClass("ui-none");
								$("#nickNameDiv").addClass("ui-none");
								$("#showMwssage").find(".alipay").addClass("ui-none");
								$("#showMwssage").find(".wechat").addClass("ui-none");
								$("#showMwssage").find(".bankCard").removeClass("ui-none");
					 		}
							//imgCode2();
						} else if (data.code == "0") {
							$.msg(data.info,2);
							$("#depositATM_sub").val('确认存款');
							$("#depositATM_sub").attr('disabled',false);
						} else if (data.code == "9") {
							$("#depositATM_sub").val('确认存款');
							$("#depositATM_sub").attr('disabled',false);
							$("#vcode_tip").html(data.info);
							$("#vcode_tip").removeClass("ui-none");
						}else {
							$.msg('网络异常，请稍后再试！',2);
							$("#depositATM_sub").val('确认存款');
							$("#depositATM_sub").attr('disabled',false);
						}
					},
					error: function(){
						$.msg('网络异常，请稍后再试！',2);
						$("#depositATM_sub").val('确认存款');
						$("#depositATM_sub").attr('disabled',false);
					}	
				});	
			});
        
        /***************在线支付*********************/
        var pay_url = "";
        $("#depositOther_sub").click(function(){
        	var bankCode=$("#bank_code").attr("data");
        	if(bankCode ==''){
				$("#bankCode_tip").html("请选择银行代码。");
				$("#bankCode_tip").removeClass("ui-none");
				return;
			}else{
				$("#bankCode_tip").addClass("ui-none");
			}
			var otherFigure = $('#otherFigure').val();
			if(otherFigure ==''){
				$("#otherFigure_tip").html("请输入充值金额！");
				$("#otherFigure_tip").removeClass("ui-none");
				$("#otherFigure").focus();
				return;
			}else if (parseInt(otherFigure) <=0){
				$("#otherFigure_tip").html("充值金额输入错误！");
				$("#otherFigure_tip").removeClass("ui-none");
				$("#otherFigure").focus();
				return;
			}else if(!regx.test(otherFigure)){
				$("#otherFigure_tip").html("存款金额只允许输入整数。");
				$("#otherFigure_tip").removeClass("ui-none");
				$("#otherFigure").focus();
				return;
			}else if(parseInt(otherFigure)<10){
				$("#otherFigure_tip").html("在线支付最低存款10元。");
				$("#otherFigure_tip").removeClass("ui-none");
				$("#otherFigure").focus();
				return;
			}else{
				$("#otherFigure_tip").addClass("ui-none");
			}
			var otherCode = $("#otherCode").val();
			if(otherCode.length !=4){
				$("#otherCode_tip").html("请输入4位数字验证码。");
				$("#otherCode_tip").removeClass("ui-none");
				$("#otherCode").focus();
				return;
			}else{
				$("#otherCode_tip").addClass("ui-none");
			}
			var tbk_memo = $("#tbk_memo").attr("data");
			if(tbk_memo != ''){
				if($("#memocheck").is(":checked")==false){
					$.msg('您选择了参加优惠活动，请阅读并优惠协议。',2);
					$("#memocheck").focus();
					return;
				}
				if(!submitConfirm()){
					return;
				}
			}
			
			
			if(!vaidate(tbk_memo,$('#otherFigure').val(),$('#otherFigure'))){
				return;
			}
			
			var ppid=$("#payWay").find(" .checked").attr("data");
			$("#depositOther_sub").attr('disabled','disabled');
			
			var continueFlag = 0;
			$.ajax({
				url: actionPath+"manage/capital/validateOtherCode.do",
				async: false,//同步
				type: 'POST',
				dataType : "json",
				data: {
					"otherCode" : otherCode,
					"type": 1
				},
				success: function(data){
				 	$("#depositOther_sub").attr('disabled',false);
				 	if (data.code == "1") {
				 		continueFlag = 1;
					}else{
						$.msg(data.info,2);
						return ;
					}
				},
				error: function(){
					$.msg("网络异常，请稍后再试！",2);
					$("#depositOther_sub").attr('disabled',false);
					return ;
				}	
			});
			
			if(continueFlag == 1){
				depositSubmit(ppid,otherFigure,vuid,tbk_memo);
			}
			
		});
		
        function depositSubmit(ppid,otherFigure,vuid,tbk_memo){
        	var bankCode=$("#bank_code").attr("data");
        	if(ppid =='44'){ //国盛通网银
        		if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
	                setTimeout(function () {
	                    newWindow.location.href = "http://app.qianbaobet.com/pay/gstwy/"+ppid+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo+"&bankCode="+bankCode;
	                }, 0);
	                reset();
				}else{
					pay_url = "http://app.qianbaobet.com/pay/gstwy/"+ppid+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo+"&bankCode="+bankCode;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
        	}else if(ppid =='11'){ //通汇支付2
        		if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
	                setTimeout(function () {
	                    newWindow.location.href = "http://pay.qianbaobet.com/pay/tht/"+ppid+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo+"&bankCode="+bankCode;
	                }, 0);
	                reset();
				}else{
					pay_url = "http://pay.qianbaobet.com/pay/tht/"+ppid+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo+"&bankCode="+bankCode;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
        	}else if(ppid =='10'){ //摩宝支付
				if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
	                setTimeout(function () {
	                    newWindow.location.href = "http://app.glastron-boat.com/pay/mbob/"+ppid+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
	                }, 0);
	                reset();
				}else{
					pay_url = "http://app.glastron-boat.com/pay/mbob/"+ppid+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
			}else if(ppid =='54'){ //新天成网银
        		if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
	                setTimeout(function () {
	                    newWindow.location.href = "http://app.qianbaobet.com/pay/xtcwy/"+ppid+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo+"&bankCode="+bankCode;
	                }, 0);
	                reset();
				}else{
					pay_url = "http://app.qianbaobet.com/pay/xtcwy/"+ppid+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo+"&bankCode="+bankCode;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
        	}else if(ppid =='213'){ //优付网银
        		if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
	                setTimeout(function () {
	                    newWindow.location.href = "http://app.qianbaobet.com/pay/ypwy/"+ppid+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo+"&bankCode="+bankCode;
	                }, 0);
	                reset();
				}else{
					pay_url = "http://app.qianbaobet.com/pay/ypwy/"+ppid+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo+"&bankCode="+bankCode;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
        	}else if(ppid =='221'){ //速汇宝网银
        		if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
	                setTimeout(function () {
	                    newWindow.location.href = "http://app.tabaojj.top/pay/shbwy/"+ppid+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo+"&bankCode="+bankCode;
	                }, 0);
	                reset();
				}else{
					pay_url = "http://app.tabaojj.top/pay/shbwy/"+ppid+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo+"&bankCode="+bankCode;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
        	}
        }
        
		function cb_ok(){
			var newWindow = top.window.open('', '_blank');
            setTimeout(function () {
                newWindow.location.href = pay_url;
            }, 0);
            reset();
		}
		function cb_ok_wx(){
			var newWindow = top.window.open('', '_blank');
			setTimeout(function () {
				newWindow.location.href = pay_url;
			}, 0);
			$("#wxFigure").val("");
            $("#wxCode").val("");
            imgCode4();
		}
		
		function reset(){
			$("#otherFigure").val("");
			$("#otherCode").val("");
			imgCode();
		}
		
		// 支付宝扫码存款
		$("#depositOther_subqr").click(function(){
		 	var money = $("#otherFigureqr");
		 	var otherCodeqr = $("#otherCodeqr");
			var tbk_memo_qr = $("#tbk_memoqr").attr("data");
			if(tbk_memo_qr != ''){
				if(!$("#memocheckqr").is(":checked")){
					$.msg('您选择了参加优惠活动，请阅读并优惠协议。',3);
					$("#memocheckqr").focus();
					return;
				}
				if(!submitConfirm()){
					return;
				}
			}
			if(money.val() == ''){
				$("#otherFigureqr_tip").html("请输入您要存入金额。");
				$("#otherFigureqr_tip").removeClass("ui-none");
				$("#otherFigureqr").focus();
				return;
			}else if(!regx.test(money.val())){
				$("#otherFigureqr_tip").html("存款金额只允许输入整数。");
				$("#otherFigureqr_tip").removeClass("ui-none");
				$("#otherFigureqr").focus();
				return;
			}else if(parseInt(money.val())<10){
				$("#otherFigureqr_tip").html("支付宝扫码支付最低金额10元。");
				$("#otherFigureqr_tip").removeClass("ui-none");
				$("#otherFigureqr").focus();
				return;
			}else{
				$("#otherFigureqr_tip").addClass("ui-none");
			}
			if($("#nickNameqr").val() ==''){
				$("#nickNameqr_tip").html("请输入付款支付宝昵称。");
				$("#nickNameqr_tip").removeClass("ui-none");
				$("#nickNameqr").focus();
				return;
			}else{
				$("#nickNameqr_tip").addClass("ui-none");
			}
			if(otherCodeqr.val().length != 4){
				$("#otherCodeqr_tip").html("请输入验证码。");
				$("#otherCodeqr_tip").removeClass("ui-none");
				otherCodeqr.focus();
				return;
			}else{
				$("#otherCodeqr_tip").addClass("ui-none");
			}
			
			if(!vaidate(tbk_memo_qr,money.val(),money)){
				return;
			}
			
			$("#depositOther_subqr").val("正在提交...");
			$("#depositOther_subqr").attr('disabled',true);
			$.ajax({
				url: actionPath+"manage/capital/saveDeposit.do",
				async: true,
				type: 'POST',
				dataType : "json",
				data: {
					ccid : $("#ccid").val(),
					depositFigure : money.val(),
					hd : tbk_memo_qr,
					depositTime : $("#depositTime").val(),
					ordernumber:$("#ordernumber").val(),
					nickName:$("#nickNameqr").val(),
					code : otherCodeqr.val()
				},
				success: function(data){
				 	if (data.code == "1") {
				 		$.remind("恭喜您，支付宝扫码支付订单申请已提交！",success_callback);
					} else if (data.code == "0") {
						$.msg(data.info,2);
						$("#depositOther_subqr").val('确认存款');
						$("#depositOther_subqr").attr('disabled',false);
					} else if (data.code == "9") {
						$("#otherCodeqr_tip").html(data.info);
						$("#otherCodeqr_tip").removeClass("ui-none");
						$("#depositOther_subqr").val('确认存款');
						$("#depositOther_subqr").attr('disabled',false);
					}else {
						$.msg('网络异常，请稍后再试！',2);
						$("#depositOther_subqr").val('确认存款');
						$("#depositOther_subqr").attr('disabled',false);
					}
				},
				error: function(){
					$.msg('网络异常，请稍后再试！',2);
					$("#depositOther_subqr").val('确认存款');
					$("#depositOther_subqr").attr('disabled',false);
				}	
			});	
		});
		
		// QQ钱包扫码存款
		$("#depositOther_subqq").click(function(){
		 	var money = $("#otherFigureqq");
		 	var otherCodeqr = $("#otherCodeqq");
			var tbk_memo_qr = $("#tbk_memoqq").attr("data");
			if(tbk_memo_qr != ''){
				if(!$("#memocheckqq").is(":checked")){
					$.msg('您选择了参加优惠活动，请阅读并优惠协议。',3);
					$("#memocheckqq").focus();
					return;
				}
				if(!submitConfirm()){
					return;
				}
			}
			if(money.val() == ''){
				$("#otherFigureqq_tip").html("请输入您要存入金额。");
				$("#otherFigureqq_tip").removeClass("ui-none");
				$("#otherFigureqq").focus();
				return;
			}else if(!regx.test(money.val())){
				$("#otherFigureqq_tip").html("存款金额只允许输入整数。");
				$("#otherFigureqq_tip").removeClass("ui-none");
				$("#otherFigureqq").focus();
				return;
			}else if(parseInt(money.val())<10){
				$("#otherFigureqq_tip").html("QQ扫码支付最低金额10元。");
				$("#otherFigureqq_tip").removeClass("ui-none");
				$("#otherFigureqq").focus();
				return;
			}else{
				$("#otherFigureqq_tip").addClass("ui-none");
			}
			if($("#nickNameqq").val() ==''){
				$("#nickNameqq_tip").html("请输入付款QQ钱包昵称。");
				$("#nickNameqq_tip").removeClass("ui-none");
				$("#nickNameqq").focus();
				return;
			}else{
				$("#nickNameqq_tip").addClass("ui-none");
			}
			if(otherCodeqr.val().length != 4){
				$("#otherCodeqq_tip").html("请输入验证码。");
				$("#otherCodeqq_tip").removeClass("ui-none");
				otherCodeqr.focus();
				return;
			}else{
				$("#otherCodeqq_tip").addClass("ui-none");
			}
			
			if(!vaidate(tbk_memo_qr,money.val(),money)){
				return;
			}
			
			$("#depositOther_subqq").val("正在提交...");
			$("#depositOther_subqq").attr('disabled',true);
			$.ajax({
				url: actionPath+"manage/capital/saveDeposit.do",
				async: true,
				type: 'POST',
				dataType : "json",
				data: {
					ccid : $("#ccidqq").val(),
					depositFigure : money.val(),
					hd : tbk_memo_qr,
					depositTime : $("#depositTime").val(),
					ordernumber:$("#ordernumber").val(),
					nickName:$("#nickNameqq").val(),
					code : otherCodeqr.val()
				},
				success: function(data){
				 	if (data.code == "1") {
				 		$.remind("恭喜您，QQ扫码支付订单申请已提交！",success_callback);
					} else if (data.code == "0") {
						$.msg(data.info,2);
						$("#depositOther_subqq").val('确认存款');
						$("#depositOther_subqq").attr('disabled',false);
					} else if (data.code == "9") {
						$("#otherCodeqq_tip").html(data.info);
						$("#otherCodeqq_tip").removeClass("ui-none");
						$("#depositOther_subqq").val('确认存款');
						$("#depositOther_subqq").attr('disabled',false);
					}else {
						$.msg('网络异常，请稍后再试！',2);
						$("#depositOther_subqq").val('确认存款');
						$("#depositOther_subqq").attr('disabled',false);
					}
				},
				error: function(){
					$.msg('网络异常，请稍后再试！',2);
					$("#depositOther_subqq").val('确认存款');
					$("#depositOther_subqq").attr('disabled',false);
				}	
			});	
		});
		
		// 微信扫码存款
		$("#cdepositOther_subqr").click(function(){
		 	var money = $("#cotherFigureqr");
		 	var otherCodeqr = $("#cotherCodeqr");
			var tbk_memo_qr = $("#ctbk_memoqr").attr("data");
			if(tbk_memo_qr != ''){
				if(!$("#cmemocheckqr").is(":checked")){
					$.msg('您选择了参加优惠活动，请阅读并优惠协议。',3);
					$("#cmemocheckqr").focus();
					return;
				}
				if(!submitConfirm()){
					return;
				}
			}
			if(money.val() == ''){
				$("#cotherFigureqr_tip").html("请输入您要存入金额。");
				$("#cotherFigureqr_tip").removeClass("ui-none");
				$("#cotherFigureqr").focus();
				return;
			}else if(!regx.test(money.val())){
				$("#cotherFigureqr_tip").html("存款金额只允许输入整数。");
				$("#cotherFigureqr_tip").removeClass("ui-none");
				$("#cotherFigureqr").focus();
				return;
			}else if(parseInt(money.val())<10){
				$("#cotherFigureqr_tip").html("微信扫码支付最低金额10元。");
				$("#cotherFigureqr_tip").removeClass("ui-none");
				$("#cotherFigureqr").focus();
				return;
			}else{
				$("#cotherFigureqr_tip").addClass("ui-none");
			}
			if($("#cnickNameqr").val() ==''){
				$("#cnickNameqr_tip").html("请输入付款微信昵称。");
				$("#cnickNameqr_tip").removeClass("ui-none");
				$("#cnickNameqr").focus();
				return;
			}else{
				$("#cnickNameqr_tip").addClass("ui-none");
			}
			if(otherCodeqr.val().length != 4){
				$("#cotherCodeqr_tip").html("请输入验证码。");
				$("#cotherCodeqr_tip").removeClass("ui-none");
				otherCodeqr.focus();
				return;
			}else{
				$("#cotherCodeqr_tip").addClass("ui-none");
			}
			
			if(!vaidate(tbk_memo_qr,money.val(),money)){
				return;
			}
			
			$("#cdepositOther_subqr").val("正在提交...");
			$("#cdepositOther_subqr").attr('disabled',true);
			$.ajax({
				url: actionPath+"manage/capital/saveDeposit.do",
				async: true,
				type: 'POST',
				dataType : "json",
				data: {
					ccid : $("#cccid").val(),
					depositFigure : money.val(),
					hd : tbk_memo_qr,
					depositTime : $("c#depositTime").val(),
					ordernumber:$("#ordernumber").val(),
					nickName:$("#cnickNameqr").val(),
					code : otherCodeqr.val()
				},
				success: function(data){
				 	if (data.code == "1") {
				 		$.remind("恭喜您，微信扫码支付订单申请已提交！",success_callback);
					} else if (data.code == "0") {
						$.msg(data.info,2);
						$("#cdepositOther_subqr").val('确认存款');
						$("#cdepositOther_subqr").attr('disabled',false);
					} else if (data.code == "9") {
						$("#cotherCodeqr_tip").html(data.info);
						$("#cotherCodeqr_tip").removeClass("ui-none");
						$("#cdepositOther_subqr").val('确认存款');
						$("#cdepositOther_subqr").attr('disabled',false);
					}else {
						$.msg('网络异常，请稍后再试！',2);
						$("#cdepositOther_subqr").val('确认存款');
						$("#cdepositOther_subqr").attr('disabled',false);
					}
				},
				error: function(){
					$.msg('网络异常，请稍后再试！',2);
					$("#cdepositOther_subqr").val('确认存款');
					$("#cdepositOther_subqr").attr('disabled',false);
				}	
			});	
		});
		
		// 第三方微信扫码支付
		$("#depositOther_wx").click(function(){
			var otherFigure = $('#wxFigure').val();
			if(otherFigure ==''){
				$("#wxFigure_tip").html("请输入充值金额！");
				$("#wxFigure_tip").removeClass("ui-none");
				otherFigure.focus();
				return;
			}else if (parseInt(otherFigure) <=0){
				$("#wxFigure_tip").html("充值金额输入错误！");
				$("#wxFigure_tip").removeClass("ui-none");
				otherFigure.focus();
				return;
			}else if(!regx.test(otherFigure)){
				$("#wxFigure_tip").html("存款金额只允许输入整数。");
				$("#wxFigure_tip").removeClass("ui-none");
				otherFigure.focus();
				return;
			}else if(parseInt(otherFigure)<10){
				$("#wxFigure_tip").html("微信支付最低存款10元。");
				$("#wxFigure_tip").removeClass("ui-none");
				otherFigure.focus();
				return;
			}else{
				$("#wxFigure_tip").addClass("ui-none");
			}
			
			var otherCode = $("#wxCode").val();
			if(otherCode.length != 4){
				$("#wxCode_tip").html("请输入4位数字验证码。");
				$("#wxCode_tip").removeClass("ui-none");
				otherCode.focus();
				return;
			}else{
				$("#wxCode_tip").addClass("ui-none");
			}
			var tbk_memo = $("#wx_memo").attr("data");
			if(tbk_memo != ''){
				if($("#memocheckwx").is(":checked")==false){
					$.msg('您选择了参加优惠活动，请阅读并优惠协议。',3);
					$("#memocheckwx").focus();
					return;
				}
				if(!submitConfirm()){
					return;
				}
			}

			if(!vaidate(tbk_memo,otherFigure,$('#wxFigure'))){
				return;
			}
			
			var submit_flg_wx = 0;
			$("#depositOther_wx").attr('disabled','disabled').val('正在提交...');
			$.ajax({
				url: actionPath+"manage/capital/validateOtherCode.do",
				async: false,//同步
				type: 'POST',
				dataType : "json",
				data: {
					"otherCode" : otherCode,
					"type": 2
				},
				success: function(data){
				 	$("#depositOther_wx").attr('disabled',false);
				 	$("#depositOther_wx").val('确认存款');
				 	if (data.code == "1") {
				 		submit_flg_wx = 1;
					}else {
						$("#depositOther_wx").val('确认存款');
						$.msg(data.info,2);
						return ;
					}
				},
				error: function(){
					$.msg('网络异常，请稍后再试！',2);
					$("#depositOther_wx").attr('disabled',false);
					return;
				}	
			});	
			
			if(submit_flg_wx == 1){
				sbtWX(vuid,otherFigure,tbk_memo);
			}
		});
		
		function sbtWX(vuid,otherFigure,tbk_memo){
	 		var cont=$("#wxpayWay").find(" .checked").attr("data");
	 		if(cont ==6){
	 			if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
		            setTimeout(function () {
		                newWindow.location.href = "http://app.qianbaobet.com/pay/wxth/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
		            }, 0);
		            reset();
				}else{
					pay_url = "http://app.qianbaobet.com/pay/wxth/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
	 		}else if(cont ==4){
	 			if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
		            setTimeout(function () {
		                newWindow.location.href = "http://app.glastron-boat.com/pay/mbo/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
		            }, 0);
		            reset();
				}else{
					pay_url = "http://app.glastron-boat.com/pay/mbo/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
	 		}else if(cont ==7){
	 			if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
		            setTimeout(function () {
		                newWindow.location.href = "http://app.hhh656.pw/pay/ybw/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
		            }, 0);
		            reset();
				}else{
					pay_url = "http://app.hhh656.pw/pay/ybw/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
	 		}else if(cont ==46){ //国盛通微信
	 			if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
		            setTimeout(function () {
		                newWindow.location.href = "http://app.qianbaobet.com/pay/gstwx/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
		            }, 0);
		            reset();
				}else{
					pay_url = "http://app.qianbaobet.com/pay/gstwx/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
	 		}else if(cont ==235){ //泽圣微信
	 			if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
		            setTimeout(function () {
		                newWindow.location.href = "http://app.qianbaobet.com/pay/zswx/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
		            }, 0);
		            reset();
				}else{
					pay_url = "http://app.qianbaobet.com/pay/zswx/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
	 		}else if(cont ==50){ //新天诚微信
	 			if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
		            setTimeout(function () {
		                newWindow.location.href = "http://app.qianbaobet.com/pay/xtcwx/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
		            }, 0);
		            reset();
				}else{
					pay_url = "http://app.qianbaobet.com/pay/xtcwx/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
	 		}else if(cont ==57){ //掌灵微信
	 			if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
		            setTimeout(function () {
		                newWindow.location.href = "http://app.qianbaobet.com/pay/zlwx/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
		            }, 0);
		            reset();
				}else{
					pay_url = "http://app.qianbaobet.com/pay/zlwx/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
	 		}else if(cont ==200){ //lepay微信
	 			if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
		            setTimeout(function () {
		                newWindow.location.href = "http://app.qianbaobet.com/pay/lpwx/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
		            }, 0);
		            reset();
				}else{
					pay_url = "http://app.qianbaobet.com/pay/lpwx/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
	 		}else if(cont ==207){ //掌智微信
	 			if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
		            setTimeout(function () {
		                newWindow.location.href = "http://app.hhh656.pw/pay/zzwx/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
		            }, 0);
		            reset();
				}else{
					pay_url = "http://app.hhh656.pw/pay/zzwx/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
	 		}else if(cont ==211){ //优付微信
	 			if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
		            setTimeout(function () {
		                newWindow.location.href = "http://app.hhh656.pw/pay/ypwx/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
		            }, 0);
		            reset();
				}else{
					pay_url = "http://app.hhh656.pw/pay/ypwx/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
	 		}else if(cont ==220){ //速汇宝微信
	 			if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
		            setTimeout(function () {
		                newWindow.location.href = "http://app.tabaojj.top/pay/shbwx/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
		            }, 0);
		            reset();
				}else{
					pay_url = "http://app.tabaojj.top/pay/shbwx/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
	 		}
		}
		
		
		// 第三方支付宝扫码
		$("#depositOther_onlineAlipay").click(function(){
			var otherFigure = $('#onlineAlipayFigure').val();
			if(otherFigure ==''){
				$("#onlineAlipayFigure_tip").html("请输入充值金额！");
				$("#onlineAlipayFigure_tip").removeClass("ui-none");
				otherFigure.focus();
				return;
			}else if (parseInt(otherFigure) <=0){
				$("#onlineAlipayFigure_tip").html("充值金额输入错误！");
				$("#onlineAlipayFigure_tip").removeClass("ui-none");
				otherFigure.focus();
				return;
			}else if(!regx.test(otherFigure)){
				$("#onlineAlipayFigure_tip").html("存款金额只允许输入整数。");
				$("#onlineAlipayFigure_tip").removeClass("ui-none");
				otherFigure.focus();
				return;
			}else if(parseInt(otherFigure)<10){
				$("#onlineAlipayFigure_tip").html("微信支付最低存款10元。");
				$("#onlineAlipayFigure_tip").removeClass("ui-none");
				otherFigure.focus();
				return;
			}else{
				$("#onlineAlipayFigure_tip").addClass("ui-none");
			}
			
			var otherCode = $("#onlineAlipayCode").val();
			if(otherCode.length != 4){
				$("#onlineAlipayCode_tip").html("请输入4位数字验证码。");
				$("#onlineAlipayCode_tip").removeClass("ui-none");
				otherCode.focus();
				return;
			}else{
				$("#onlineAlipayCode_tip").addClass("ui-none");
			}
			var tbk_memo = $("#onlineAlipay_memo").attr("data");
			if(tbk_memo != ''){
				if($("#onlineAlipaycheckwx").is(":checked")==false){
					$.msg('您选择了参加优惠活动，请阅读并优惠协议。',3);
					$("#onlineAlipaycheckwx").focus();
					return;
				}
				if(!submitConfirm()){
					return;
				}
			}
			if(!vaidate(tbk_memo,otherFigure,$('#onlineAlipayFigure'))){
				return;
			}
			
			var submit_flg_alipay = 0;
			$("#depositOther_onlineAlipay").attr('disabled','disabled').val('正在提交...');
			$.ajax({
				url: actionPath+"manage/capital/validateOtherCode.do",
				async: false,//同步
				type: 'POST',
				dataType : "json",
				data: {
					"otherCode" : otherCode,
					"type": 2
				},
				success: function(data){
				 	$("#depositOther_onlineAlipay").attr('disabled',false);
				 	$("#depositOther_onlineAlipay").val('确认存款');
				 	if (data.code == "1") {
				 		submit_flg_alipay = 1;
					}else {
						$("#depositOther_onlineAlipay").val('确认存款');
						$.msg(data.info,2);
						return ;
					}
				},
				error: function(){
					$.msg('网络异常，请稍后再试！',2);
					$("#depositOther_onlineAlipay").attr('disabled',false);
					return;
				}	
			});	
			
			if(submit_flg_alipay == 1){
				sbtAlipay(vuid,otherFigure,tbk_memo);
			}
			
		});
		
		function sbtAlipay(vuid,otherFigure,tbk_memo){
	 		var cont=$("#alipayWay").find(" .checked").attr("data");
	 		if(cont ==1){
	 			if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
		            setTimeout(function () {
		                newWindow.location.href = "http://app.qianbaobet.com/pay/alipay/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
		            }, 0);
		            reset();
				}else{
					pay_url = "http://app.qianbaobet.com/pay/alipay/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
	 		}else if(cont ==2){
	 			if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
		            setTimeout(function () {
		                newWindow.location.href = "http://app.hhh656.pw/pay/zfb/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
		            }, 0);
		            reset();
				}else{
					pay_url = "http://app.hhh656.pw/pay/zfb/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
	 		}else if(cont ==47){ //国盛通支付宝
	 			if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
		            setTimeout(function () {
		                newWindow.location.href = "http://app.qianbaobet.com/pay/gstzfb/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
		            }, 0);
		            reset();
				}else{
					pay_url = "http://app.qianbaobet.com/pay/gstzfb/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
	 		}else if(cont ==233){ //泽圣支付宝
	 			if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
		            setTimeout(function () {
		                newWindow.location.href = "http://app.qianbaobet.com/pay/zszfb/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
		            }, 0);
		            reset();
				}else{
					pay_url = "http://app.qianbaobet.com/pay/zszfb/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
	 		}else if(cont ==51){ //新天诚支付宝
	 			if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
		            setTimeout(function () {
		                newWindow.location.href = "http://app.qianbaobet.com/pay/xtczfb/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
		            }, 0);
		            reset();
				}else{
					pay_url = "http://app.qianbaobet.com/pay/xtczfb/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
	 		}else if(cont ==55){ //掌灵支付宝
	 			if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
		            setTimeout(function () {
		                newWindow.location.href = "http://app.qianbaobet.com/pay/zlzfb/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
		            }, 0);
		            reset();
				}else{
					pay_url = "http://app.qianbaobet.com/pay/zlzfb/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
	 		}else if(cont ==201){ //lepay支付宝
	 			if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
		            setTimeout(function () {
		                newWindow.location.href = "http://app.qianbaobet.com/pay/lpzfb/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
		            }, 0);
		            reset();
				}else{
					pay_url = "http://app.qianbaobet.com/pay/lpzfb/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
	 		}else if(cont ==208){ //掌智支付宝
	 			if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
		            setTimeout(function () {
		                newWindow.location.href = "http://app.hhh656.pw/pay/zzzfb/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
		            }, 0);
		            reset();
				}else{
					pay_url = "http://app.hhh656.pw/pay/zzzfb/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
	 		}else if(cont ==223){ //优付支付宝
	 			if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
		            setTimeout(function () {
		                newWindow.location.href = "http://app.hhh656.pw/pay/ypzfb/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
		            }, 0);
		            reset();
				}else{
					pay_url = "http://app.hhh656.pw/pay/ypzfb/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
	 		}else if(cont ==225){ //速汇宝支付宝
	 			if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
		            setTimeout(function () {
		            	newWindow.location.href = "http://app.tabaojj.top/pay/shbzfb/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
		            }, 0);
		            reset();
				}else{
					pay_url = "http://app.tabaojj.top/pay/shbzfb/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
	 		}else if(cont ==227){ //hebao支付宝
	 			if(parseInt(otherFigure) <=2000){
					var newWindow = top.window.open('', '_blank');
		            setTimeout(function () {
		            	newWindow.location.href = "http://app.qianbaobet.com/pay/hebaozfb/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
		            }, 0);
		            reset();
				}else{
					pay_url = "http://app.qianbaobet.com/pay/hebaozfb/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
					$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
				}
	 		}
		}
		
		// 第三方支QQ扫码
		$("#depositOther_onlineQQ").click(function(){
			var otherFigure = $('#onlineQQFigure').val();
			if(otherFigure ==''){
				$("#onlineQQFigure_tip").html("请输入充值金额！");
				$("#onlineQQFigure_tip").removeClass("ui-none");
				otherFigure.focus();
				return;
			}else if (parseInt(otherFigure) <=0){
				$("#onlineQQFigure_tip").html("充值金额输入错误！");
				$("#onlineQQFigure_tip").removeClass("ui-none");
				otherFigure.focus();
				return;
			}else if(!regx.test(otherFigure)){
				$("#onlineQQFigure_tip").html("存款金额只允许输入整数。");
				$("#onlineQQFigure_tip").removeClass("ui-none");
				otherFigure.focus();
				return;
			}else if(parseInt(otherFigure)<10){
				$("#onlineQQFigure_tip").html("微信支付最低存款10元。");
				$("#onlineQQFigure_tip").removeClass("ui-none");
				otherFigure.focus();
				return;
			}else{
				$("#onlineQQFigure_tip").addClass("ui-none");
			}
			
			var otherCode = $("#onlineQQCode").val();
			if(otherCode.length != 4){
				$("#onlineQQCode_tip").html("请输入4位数字验证码。");
				$("#onlineQQCode_tip").removeClass("ui-none");
				otherCode.focus();
				return;
			}else{
				$("#onlineQQCode_tip").addClass("ui-none");
			}
			var tbk_memo = $("#onlineQQ_memo").attr("data");
			if(tbk_memo != ''){
				if($("#onlineQQcheckwx").is(":checked")==false){
					$.msg('您选择了参加优惠活动，请阅读并优惠协议。',3);
					$("#onlineQQcheckwx").focus();
					return;
				}
				if(!submitConfirm()){
					return;
				}
			}
			if(!vaidate(tbk_memo,otherFigure,$('#onlineQQFigure'))){
				return;
			}
			
			var submit_flg_qq = 0;
			$("#depositOther_onlineQQ").attr('disabled','disabled').val('正在提交...');
			$.ajax({
				url: actionPath+"manage/capital/validateOtherCode.do",
				async: false,//同步
				type: 'POST',
				dataType : "json",
				data: {
					"otherCode" : otherCode,
					"type": 2
				},
				success: function(data){
				 	$("#depositOther_onlineQQ").attr('disabled',false);
				 	$("#depositOther_onlineQQ").val('确认存款');
				 	if (data.code == "1") {
				 		submit_flg_qq = 1;
					}else {
						$("#depositOther_onlineQQ").val('确认存款');
						$.msg(data.info,2);
						return ;
					}
				},
				error: function(){
					$.msg('网络异常，请稍后再试！',2);
					$("#depositOther_onlineQQ").attr('disabled',false);
					return;
				}	
			});	
			
			if(submit_flg_qq == 1){
				var cont=$("#qqWay").find(" .checked").attr("data");
				if(cont ==205){ //新天诚QQ扫码
		 			if(parseInt(otherFigure) <=2000){
						var newWindow = top.window.open('', '_blank');
			            setTimeout(function () {
			                newWindow.location.href = "http://app.qianbaobet.com/pay/xtcqq/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
			            }, 0);
			            reset();
					}else{
						pay_url = "http://app.qianbaobet.com/pay/xtcqq/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
						$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
					}
		 		}else if(cont ==229){ //PC-速汇宝-QQ扫码
		 			if(parseInt(otherFigure) <=2000){
						var newWindow = top.window.open('', '_blank');
			            setTimeout(function () {
			                newWindow.location.href = "http://app.tabaojj.top/pay/shbqq/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
			            }, 0);
			            reset();
					}else{
						pay_url = "http://app.tabaojj.top/pay/shbqq/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
						$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
					}
		 		}else if(cont ==231){ //PC-乐付-QQ扫码
		 			if(parseInt(otherFigure) <=2000){
						var newWindow = top.window.open('', '_blank');
			            setTimeout(function () {
			                newWindow.location.href = "http://app.qianbaobet.com/pay/lpqq/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
			            }, 0);
			            reset();
					}else{
						pay_url = "http://app.qianbaobet.com/pay/lpqq/"+cont+".do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo;
						$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
					}
		 		}
			}
			
		});
		
		  // 点卡支付
		$("#depositOther_dk").click(function(){
				var cardType = $("#dk_type").attr("data");
				if(cardType ==''){
					$("#dk_type_tip").html("请选择支付点卡！");
					$("#dk_type_tip").removeClass("ui-none");
					return;
				}else{
					$("#dk_type_tip").addClass("ui-none");
				}
				var otherFigure = $('#otherFiguredk').val();
				if(otherFigure ==''){
					$("#otherFiguredk_tip").html("请输入充值金额！");
					$("#otherFiguredk_tip").removeClass("ui-none");
					otherFigure.focus();
					return;
				}else if (parseInt(otherFigure) <=0){
					$("#otherFiguredk_tip").html("充值金额输入错误！");
					$("#otherFiguredk_tip").removeClass("ui-none");
					otherFigure.focus();
					return;
				}else if(!regx.test(otherFigure)){
					$("#otherFiguredk_tip").html("存款金额只允许输入整数。");
					$("#otherFiguredk_tip").removeClass("ui-none");
					otherFigure.focus();
					return;
				}else if(parseInt(otherFigure)<20){
					$("#otherFiguredk_tip").html("点卡支付最低存款20元。");
					$("#otherFiguredk_tip").removeClass("ui-none");
					otherFigure.focus();
					return;
				}else{
					$("#otherFiguredk_tip").addClass("ui-none");
				}
				
				var card_no = $("#card_no");
				if(card_no.val() == ''){
					$("#card_no_tip").html("请输入点卡卡号。");
					$("#card_no_tip").removeClass("ui-none");
					card_no.focus();
					return;
				}else{
					$("#card_no_tip").addClass("ui-none");
				}
				var card_password = $("#card_password");
				if(card_password.val() ==''){
					$("#card_password_tip").html("请输入点卡密码。");
					$("#card_password_tip").removeClass("ui-none");
					card_password.focus();
					return;
				}else{
					$("#card_password_tip").addClass("ui-none");
				}
				
				var otherCode = $("#otherCodedk").val();
				if(otherCode.length != 4){
					$("#otherCodedk_tip").html("请输入4位数字验证码。");
					$("#otherCodedk_tip").removeClass("ui-none");
					otherCode.focus();
					return;
				}else{
					$("#otherCodedk_tip").addClass("ui-none");
				}
				var tbk_memo = $("#tbk_memodk").attr("data");
				if(tbk_memo != ''){
					if($("#memocheckwx").is(":checked")==false){
						$.msg('您选择了参加优惠活动，请阅读并优惠协议。',3);
						$("#memocheckwx").focus();
						return;
					}
					if(!submitConfirm()){
						return;
					}
				}
				if(!vaidate(tbk_memo,otherFigure,$('#otherFiguredk'))){
					return;
				}
				
				var submit_flg_wx = 0;
				$("#depositOther_dk").attr('disabled','disabled').val('正在提交...');
				$.ajax({
					url: actionPath+"manage/capital/validateOtherCode.do",
					async: false,//同步
					type: 'POST',
					dataType : "json",
					data: {
						"otherCode" : otherCode,
						"type": 2
					},
					success: function(data){
					 	$("#depositOther_dk").attr('disabled',false);
					 	$("#depositOther_dk").val('确认存款');
					 	if (data.code == "1") {
					 		submit_flg_wx = 1;
						}else {
							$("#depositOther_dk").val('确认存款');
							$.msg(data.info,2);
							return ;
						}
					},
					error: function(){
						$.msg('网络异常，请稍后再试！',2);
						$("#depositOther_dk").attr('disabled',false);
						return;
					}	
				});	
				
				if(submit_flg_wx == 1){
		 			if(parseInt(otherFigure) <=2000){
						var newWindow = top.window.open('', '_blank');
			            setTimeout(function () {
			                newWindow.location.href = "http://app.qianbaobet.com/pay/xtcdk/100.do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo+"&cardNo="+card_no.val()+"&cardpwd="+card_password.val()+"&cardType="+cardType;
			            }, 0);
			            reset();
					}else{
						pay_url = "http://app.qianbaobet.com/pay/xtcdk/100.do?vuid="+vuid+"&totalAmount="+otherFigure+"&hd="+tbk_memo+"&cardNo="+card_no.val()+"&cardpwd="+card_password.val()+"&cardType="+cardType;
						$.confirm('温馨的提醒您，若采用公司入款，将会有0.5%手续费返还喔。', cb_ok, null, '继续支付','取消',true);
					}
				}
			});
		
    });
    
    function success_callback(){
		window.location.reload();
	}
    
    function submitConfirm(){
    	return confirm("请再次确定您已仔细阅读该活动内容，并接受钱宝娱乐该优惠所列举的限制条件？");
    }
    
    function vaidate(hd,amount,target){
		if(hd =='1' && parseInt(amount)<100){
			$.msg('您选择了参加了首存优惠活动，需要最低存款100元哦。',3);
			target.focus();
			return false;
		}else if(hd=='2' && parseInt(amount)<50){
			$.msg('您选择了参加了首存优惠活动，需要最低存款50元哦。',3);
			target.focus();
			return false;
		}else if((hd =='3' || hd=='4') && parseInt(amount)<10){
			$.msg('您选择了参加了首存或次存优惠活动，需要最低存款10元哦。',3);
			target.focus();
			return false;
		}else if((hd =='5' || hd=='6') && parseInt(amount)<10){
			$.msg('您选择了参加PT笔笔存优惠活动，需要最低存款10元哦。',3);
			target.focus();
			return false;
		}else if(hd =='8' && parseInt(amount)<200){
			$.msg('您选择了参加的优惠活动，需要最低存款200元哦。',3);
			target.focus();
			return false;
		}else if(hd =='9' && parseInt(amount)<500){
			$.msg('您选择了参加的优惠活动，需要最低存款500元哦。',3);
			target.focus();
			return false;
		}else if((hd =='11' || hd =='12' || hd =='13') && parseInt(amount)<100){
			$.msg('您选择了参加的优惠活动，需要最低存款100元哦。',3);
			target.focus();
			return false;
    	}else if(hd =='15' && parseInt(amount) < 1000){
			$.msg('您选择了参加的优惠活动，需要存款1000元哦。',3);
			target.focus();
			return false;
    	}else if(hd =='16' && parseInt(amount) < 2000){
			$.msg('您选择了参加的优惠活动，需要存款2000元哦。',3);
			target.focus();
			return false;
    	}else if(hd =='17' && parseInt(amount) < 50){
			$.msg('您选择了参加的优惠活动，需要存款50元哦。',3);
			target.focus();
			return false;
    	}
    	return true;
    }