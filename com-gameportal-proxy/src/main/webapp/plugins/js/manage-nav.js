$(function(){
	var url = top.location.href;
	if(url.indexOf("/index") > 0){
		$("#manage-index").addClass('active');
	}else if(url.indexOf("/queryProxyClearingLog") > 0){
		$("#manage-proxyclearLog").addClass('active');
	}else if(url.indexOf("/queryDepositLog") > 0){
		$("#manage-deposit").addClass('active');
	}else if(url.indexOf("/queryPayOrderLog") > 0){
		$("#manage-payorder").addClass('active');
	}else if(url.indexOf("/queryPunishOrderLog") > 0){
		$("#manage-punish").addClass('active');
	}else if(url.indexOf("/queryDownUser") > 0){
		$("#manage-downuser").addClass('active');
	}else if(url.indexOf("/queryProxyUserReport") > 0){
		$("#manage-report").addClass('active');
	}else if(url.indexOf("/queryProxyUserXimaLog") > 0){
		$("#manage-ximadetail").addClass('active');
	}else if(url.indexOf("/queryProxyClearing") > 0){
		$("#manage-proxyclear").addClass('active');
	}
});