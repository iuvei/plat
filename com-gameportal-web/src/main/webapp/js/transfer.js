var regx = /^[1-9]+[0-9]*]*$/;
$(function () {
	$("#transferNum").keyup(function(){    
        $(this).val($(this).val().replace(/[^0-9.]/g,''));    
    }).bind("paste",function(){  //CTR+V事件处理    
        $(this).val($(this).val().replace(/[^0-9.]/g,''));     
    });
            
	$("#transfer_sub").click(function(){
		var transferOut = $("#transferOut").attr("data");
		var transferIn = $("#transferIn").attr("data");
		var transferNum = $("#transferNum");
		var vcode = $("#vcode");
		var el="";
		if(transferOut =='AA'){
			if(parseInt(transferNum.val()) > accountMoneyCount){
       			transferNum.val(accountMoneyCount);
       		}else{
       			transferNum.val(parseInt(transferNum.val()));
       		}
		}
		if(transferIn =='AA'){
			if(transferOut =='MG'){
				el="mgin";
			}else if(transferOut =='AGIN'){
				el="agin";
			}else if(transferOut =='AG'){
				el="ag";
			}else if(transferOut =='BBIN'){
				el="bbin";
			}else if(transferOut =='PT'){
				el="pt";
			}else if(transferOut =='SA'){
				el="sa";
			}
			
			if(parseInt(transferNum.val()) >parseInt($("#"+el).html())){
       			transferNum.val(parseInt($("#"+el).html()));
       		}else{
       			transferNum.val(parseInt(transferNum.val()));
       		}
		}
		if(transferOut =='' || transferOut.length == 0){
			$.msg("请选择转出账户。",3);
			return;
		}
		if(transferIn == '' || transferIn.length == 0){
			$.msg("请选择转入账户。",3);
			return;
		}
		if(transferNum.val() =='' || parseInt(transferNum.val()) <= 0){
			$.msg("当前游戏余额为0不能转账。",3);
			return;
		}
		if(vcode.val().length != 4){
			$("#vcode_tip").html("请输入4位数字验证码。");
			$("#vcode_tip").removeClass("ui-none");
			vcode.focus();
			return;
		}else{
			$("#vcode_tip").addClass("ui-none");
			
		}
		$("#transfer_sub").val('正在提交...');
		$("#transfer_sub").attr('disabled',true);
		$.ajax({
			type : "POST",
			url : actionPath+"manage/capital/gameTransfer.do?timeStamp="+new Date().getTime(),
			data : {
				transferOut : transferOut,
				transferIn : transferIn,
				transferNum : transferNum.val(),
				code : vcode.val(),
				agBill : $("#agBill").val(),
				aginBill : $("#aginBill").val(),
				bbinBill : $("#bbinBill").val(),
				ptBill : $("#ptBill").val(),
				mgBill : $("#mgBill").val(),
				saBill : $("#saBill").val()
			},
			dataType : "json",
			async : true,
			success : function(data) {
				if (data.code == "1") {
					$.msg("转账成功，祝君游戏愉快。",1,success_callback,true);
//					$('#transfer_sq').addClass("ui-none");
//					$('#showMwssage').removeClass("ui-none");
					imgCode2();
					//setTimeout(function(){window.location.href=(actionPasth + "manage/capital/userTransfer.do?timeStamp="+new Date().getTime());},3000);
				} else if (data.code == "0") {
					$.msg(data.info,2);
					$("#transfer_sub").val('确认转账');
					$("#transfer_sub").attr('disabled',false);
				} else if (data.code == "9") {
					$("#vcode_tip").html(data.info);
					$("#vcode_tip").removeClass("ui-none");
					$("#transfer_sub").val('确认转账');
					$("#transfer_sub").attr('disabled',false);
				}else {
					$.msg('网络异常，请稍后再试！',2);
					$("#transfer_sub").val('确认转账');
					$("#transfer_sub").attr('disabled',false);
				}
			},
			error: function(){
				$.msg('网络异常，请稍后再试！',2);
				$("#transfer_sub").val('确认转账');
				$("#transfer_sub").attr('disabled',false);
			}
		});
	});
	
});
$(function(){
	$.ajax({
		type : "POST",
		url : actionPath+"manage/capital/getMGAccountBalance.do",
		dataType : "json",
		data:{
				gpname:'MG'
		},
		async : true,
		success : function(data) {
			if(data!=null){
				for(var i=0;i<data.length;i++){
					var game = data[i];
					$("#mgin").text(game.money);
				}
				
			}
		},
		error: function(){
			//$.msg('网络异常，请稍后再试！',2);
			$("#mgin").html("<font class='color-orange'>网络异常，请稍后再试！</font>");
		}
	});
	$.ajax({
		type : "POST",
		url : actionPath+"manage/capital/getGameBalance.do",
		dataType : "json",
		data:{
				gpname:'AGIN'
		},
		async : true,
		success : function(data) {
			var money = data[data.length-1].money;
			$("#agin").text(money);
		},
		error: function(){
			//$.msg('网络异常，请稍后再试！',2);
			$("#agin").html("<font class='color-orange'>网络异常，请稍后再试！</font>");
		}
	});
	$.ajax({
		type : "POST",
		url : actionPath+"manage/capital/getGameBalance.do",
		dataType : "json",
		data:{
				gpname:'PT'
		},
		async : true,
		success : function(data) {
			var money = data[data.length-1].money;
			$("#pt").text(money);
			/*for(var i=0;i<data.length;i++){
				var game = data[i];
				$("#pt").text(game.money);
			}*/
		},
		error: function(){
			//$.msg('网络异常，请稍后再试！',2);
			$("#pt").html("<font class='color-orange'>网络异常，请稍后再试！</font>");
		}
	});
	/**
	$.ajax({
		type : "POST",
		url : actionPath+"manage/capital/getGameBalance.do",
		dataType : "json",
		data:{
				gpname:'AG'
		},
		async : true,
		success : function(data) {
			var money = data[data.length-1].money;
			$("#ag").text(money);
		},
		error: function(){
			//$.msg('网络异常，请稍后再试！',2);
			$("#ag").html("<font class='color-orange'>网络异常，请稍后再试！</font>");
		}
	});
	*/
	$.ajax({
		type : "POST",
		url : actionPath+"manage/capital/getGameBalance.do",
		dataType : "json",
		data:{
				gpname:'BBIN'
		},
		async : true,
		success : function(data) {
			var money = data[data.length-1].money;
			$("#bbin").text(money);
			/*for(var i=0;i<data.length;i++){
				var game = data[i];
				$("#bbin").text(game.money);
			}*/
		},
		error: function(){
			//$.msg('网络异常，请稍后再试！',2);
			$("#bbin").html("<font class='color-orange'>网络异常，请稍后再试！</font>");
		}
	});
	
	$.ajax({
		type : "POST",
		url : actionPath+"manage/capital/getSAAccountBalance.do",
		dataType : "json",
		data:{
				gpname:'SA'
		},
		async : true,
		success : function(data) {
			var money = data[data.length-1].money;
			$("#sa").text(money);
		},
		error: function(){
			$("#sa").html("<font class='color-orange'>网络异常，请稍后再试！</font>");
		}
	});
});   
function success_callback(){
	window.location.reload();
}