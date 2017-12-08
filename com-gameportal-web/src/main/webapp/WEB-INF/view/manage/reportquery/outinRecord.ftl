<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<#include "${action_path}common/config.ftl">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>钱包娱乐--帐户中心--存提款明细</title>
    <link type="text/css" rel="stylesheet" href="/static/css/public/base.css" />
    <link type="text/css" rel="stylesheet" href="/static/css/user/user.css" />
    <script type="text/javascript" src="/static/lib/jquery.js"></script>
    <script type="text/javascript" src="/static/js/public/utils.js"></script>
    <script type="text/javascript" src="/static/js/user/user.js"></script>
    <script type="text/javascript" >
    $(function(){
		<#if total gt 0>
			var settings={
				currentPage:${pageNo},//当前页
				totalRecoreds:${total},//总计录数
				recordsPerPage:10,//每页X条
				callback:selOutInRecord //异步刷新表格
			};
			$("#pages").paging(settings);
		</#if>
    })
    
    function selOutInRecord(){
    	$("#pageNo").val($(this).text());
    	$("#paytyple").val($("#exchangeType").attr("data"));
		$("#status").val($("#statusType").attr("data"));
		$("#outinRecordForm").submit();
    }
    function cancelOrder(poid){
    	if(!$("#cancel_"+poid).hasClass("_click")){
	    	if(confirm("您确定要撤销此提款订单吗？")){
	    		$("#cancel_"+poid).addClass("_click");
	    		$("#cancel_"+poid).text('订单撤销中...');
	    		$.ajax({
					url: actionPath+"manage/user/cancelWithdraw.do",
					async: true,
					type: 'POST',
					dataType : "json",
					data: {
						poid : poid
					},
					success: function(data){
					 	if (data.code == "0") {
					 		$("#cancel_"+poid).text('订单撤销成功');
					 		$.remind("恭喜您，该提款订单撤销成功！",function(){
					 			window.location.reload();
					 		});
						} else {
							$.msg(data.info,2);
							$("#cancel_"+poid).removeClass("_click");
							$("#cancel_"+poid).text('您可点击此处撤销此订单');
						}
					},
					error: function(){
						$.msg('网络异常，请稍后再试！',2);
						$("#cancel_"+poid).removeClass("_click");
						$("#cancel_"+poid).text('您可点击此处撤销此订单');
					}	
				});	
	    	}
    	}
    }
    </script>
</head>

<body>
    <div class="title size-bigger">存提款明细<span class="size-normal" style="color:#d9534f;">&emsp;（状态为发起的提款订单，您可以进行撤销操作）</span></div>

    <div class="main">
        <div class="user-layer ui-mt30">
            <form id="outinRecordForm" action="${action_path}manage/reportQuery/outinRecord.do" method="post">
                <div class="ui-inputBox">
                    <span class="name fl">选择日期：</span>
                    <div class="fl ui-datePicker ui-radius short">
                        <div class="ui-calendar ui-radius"></div>
	                        <input class="size-small" name="starttime" type="text" value="${starttime}" placeholder="开始日期" />
                        <span></span>
                    </div><span class="name fl">&nbsp;--&nbsp;</span>
                    <div class="fl ui-datePicker ui-radius short">
                        <div class="ui-calendar ui-radius"></div>
                        <input class="size-small" name="endtime" type="text" value="${endtime}" placeholder="结束日期" />
                        <span></span>
                    </div>
                    <span class="name fl">&emsp;&emsp;类型：</span>
                    <div class="ui-ddl fl size-small color-gray ui-radius short">
	                    <#assign paytyple = "${paytyple! - 1}">
	                    <#if paytyple==0>
	                        <div class="input fl ui-hand ui-textOverflow" data="0" id="exchangeType">存款</div>
	                    <#elseif paytyple==1>
	                        <div class="input fl ui-hand ui-textOverflow" data="1" id="exchangeType">取款</div>
	                    <#elseif paytyple==2>
	                        <div class="input fl ui-hand ui-textOverflow" data="2" id="exchangeType">赠送</div>
	                    <#elseif paytyple==3>
	                        <div class="input fl ui-hand ui-textOverflow" data="3" id="exchangeType">扣款</div>
	                    <#else>
	                    	<div class="input fl ui-hand ui-textOverflow" data="-1" id="exchangeType">全部</div>
	                    </#if>
                        <span class="fl"></span>
                        <ul class="ui-radius">
                            <li data="-1">全部</li>
                            <li data="0">存款</li>
                            <li data="1">取款</li>
                            <li data="2">赠送</li>
                            <li data="3">扣款</li>
                        </ul>
                    </div>
                    <span class="name fl">&emsp;状态：</span>
                    <div class="ui-ddl fl size-small color-gray ui-radius short">
                        
                        <#assign status="${status!-1}">
                        <#if status==-1>
	                        <div class="input fl ui-hand ui-textOverflow" data="${status}" id="statusType">全部</div>
                        <#elseif status==2>
	                        <div class="input fl ui-hand ui-textOverflow" data="${status}" id="statusType">处理中</div>
                        <#elseif status==3>
	                        <div class="input fl ui-hand ui-textOverflow" data="${status}" id="statusType">成功</div>
                        <#elseif status==4>
	                        <div class="input fl ui-hand ui-textOverflow" data="${status}" id="statusType">失败</div>
	                    </#if>
	                    	
                        <span class="fl"></span>
                        <ul class="ui-radius">
                            <li data="-1">全部</li>
                            <li data="3">成功</li>
                            <li data="4">失败</li>
                            <li data="2">处理中</li>
                            <li data="0">作废</li>
                        </ul>
                    </div>
                    <input type="hidden" id="paytyple" name="paytyple" />
                    <input type="hidden" id="status" name="status" />
                    <input type="hidden" id="pageNo" name="pageNo" />
                    <input type="button" onclick="selOutInRecord()" class="query ui-btn fl size-normal ui-radius color-white bg-orange bg-brownHover ui-transition" id="outinRecord_sub" value="查询" />
                </div>
            </form>

            <table class="ui-table ui-mt10 ui-alignCenter">
                <colgroup>
                    <col width="160" />
                    <col width="100" />
                    <col width="70" />
                    <col width="80" />
                    <col width="60" />
                    <col width="60" />
                    <col />
                </colgroup>
                <tr>
                    <th>时间</th>
                    <th>单号</th>
                    <th>金额</th>
                    <th>支付方式</th>
                    <th>类型</th>
                    <th>状态</th>
                    <th>备注</th>
                </tr>
                <#if payOrderList && payOrderList?size gt 0>
	          		<#list payOrderList as payorder>
		                <tr>
		                    <td class="left">${payorder.deposittime?date}</td>
		                    <td class="left">${payorder.poid}</td>
		                    <td class="left">${payorder.amount}</td>
		                    <td class="left">
								<#if payorder.paymethods==0 >
				            		公司入款
				            	<#elseif payorder.paymethods==1>
				            		网上支付
				            	<#else>
				            		其他
				            	</#if>
							</td>
		                    <td class="left">
								<#if payorder.paytyple==0 >
			                		<font color="#5cb85c">存款</font>
			                	<#elseif payorder.paytyple == 1>
			                		<font color="#337ab7">提款</font>
			                	<#elseif payorder.paytyple == 2>
			                		<font color="#d9534f">赠送</font>
			                	<#elseif payorder.paytyple == 3>
			                		<font color="#31b0d5">扣款</font>
			                	</#if>
							</td>
		                    <td class="left" style="width:80px;">
								<#if payorder.status==0 >
			                    	作废
			                    <#elseif payorder.status==1>
			                    	<font color="#ec971f">发起</font>
			                    <#elseif payorder.status==2>
			                    	<font color="#c9302c">处理中</font>
			                    <#elseif payorder.status==3>
			                    	<font color="#5cb85c">成功</font>
			                    <#elseif payorder.status==4>
			                    	失败
			                    </#if>
							</td>
		                    	
		                    <td>
								<#if payorder.paytyple==2 >
					            	${payorder.cwremarks}
					            <#elseif payorder.paytyple==1 && payorder.status==1>
					            	<a href="javascript:cancelOrder('${payorder.poid}');" style="color:blue;text-decoration:underline" id="cancel_${payorder.poid}">撤销订单</a>
					            <#else>
					            	<#if payorder.status==3 >
					            		成功
					            	<#else>
					            		<#if payorder.remarks??>${payorder.remarks}<#elseif payorder.cwremarks??>${payorder.cwremarks}<#else>${payorder.kfremarks}</#if>
					            	</#if>
					            </#if>
							</td>
		                </tr>
		             </#list>
                <#else>
                	<tr>
                    <td colspan="7">您还没有出入款记录！</td>
                	</tr>
                </#if>
            </table>
            <div class="pages" id="pages"></div>
        </div>
    </div>

</body>

</html>