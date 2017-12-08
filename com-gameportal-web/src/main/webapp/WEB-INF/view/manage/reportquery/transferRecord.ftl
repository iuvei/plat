<#include "common/config.ftl">
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>钱宝娱乐--帐户中心--转账记录查询  </title>
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
				callback:selTransferRecord //异步刷新表格
			};
			$("#pages").paging(settings);
		</#if>
    })
    
    function selTransferRecord(){
    	$("#pageNo").val($(this).text());
		$("#limitRecordForm").submit();
    }
    </script>
</head>

<body>
    <div class="title size-bigger">转账记录查询<span class="color-brown size-normal">&emsp;（只能查询最近7天的转账记录）</span></div>

    <div class="main">
        <div class="user-layer ui-mt30">
            <form id="limitRecordForm" action="${action_path}manage/reportQuery/transferRecord.do" method="post">
                <div class="ui-inputBox">
                    <span class="name fl">选择日期：</span>
                    <div class="fl ui-datePicker ui-radius">
                        <div class="ui-calendar ui-radius"></div>
                        <input class="size-small"  name="starttime" type="text" value="${starttime}" placeholder="开始日期" />
                        <span></span>
                    </div><span class="name fl">&nbsp;--&nbsp;</span>
                    <div class="fl ui-datePicker ui-radius">
                        <div class="ui-calendar ui-radius"></div>
                        <input class="size-small"  name="endtime" type="text" value="${endtime}" placeholder="结束日期" />
                        <span></span>
                    </div>
                    <input type="hidden" id="pageNo" name="pageNo" />
                    <input type="button" onclick="selTransferRecord()" class="query ui-btn fl size-normal ui-radius color-white bg-orange bg-brownHover ui-transition" id="limitRecord_sub" value="查询" />
                </div>
            </form>

            <table class="ui-table ui-mt10 ui-alignCenter">
                <colgroup>
                    <col width="160" />
                    <col width="120" />
                    <col width="100" />
                    <col width="120" />
                    <col width="100" />
                    <col />
                </colgroup>
                <tr>
                    <th>时间</th>
                    <th>平台</th>
                    <th>操作类型</th>
                    <th>平台</th>
                    <th>转入/出金额</th>
                    <th>钱包剩余额度</th>
                </tr>
                <#if gameTransfers && gameTransfers?size gt 0 >
            		<#list gameTransfers as transfer>
		                <tr>
		                    <td class="left">${transfer.createDate?string('yyyy-MM-dd HH:mm:ss')}</td>
		                    <td class="left">${transfer.gamename}</td>
		                    <td class="left"> 
		                    	<#if transfer.gpid gt 0>
					              	转入
					            <#else>
					             	 转出
				                </#if>
		                    </td>
		                    <td class="left">${transfer.togamename}</td>
		                    <td class="left">${transfer.amount}</td>
		                    <td>${transfer.balance!'未记录'}</td>
		                </tr>
		             </#list>
                <#else>
                	<tr>
                    	<td colspan="6">您还没有转账记录！</td>
                	</tr>
                </#if>
            </table>
            <div class="pages" id="pages"></div>
        </div>
    </div>

</body>

</html>