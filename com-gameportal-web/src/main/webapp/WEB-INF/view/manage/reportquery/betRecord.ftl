<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<#include "${action_path}common/config.ftl">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>钱宝--帐户中心--自助查流水</title>
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
				callback:selBetRecord //异步刷新表格
			};
			$("#pages").paging(settings);
		</#if>
    })
    
    function selBetRecord(){
    	$("#pageNo").val($(this).text());
    	$("#betflag").val($("#bettype").attr("data"));
		$("#platformcode").val($("#selectPlat").attr("data"));
		$("#betRecordForm").submit();
    }
    </script>
    
</head>

<body>
    <div class="title size-bigger">自助流水查询<span class="color-brown size-normal">&emsp;（只提供最近30天的游戏记录，您的最新游戏记录会延迟5-10分钟）</span></div>
    
    <div class="main">
        <div class="user-layer ui-mt30">
            <form id="betRecordForm" action="${action_path}manage/reportQuery/betRecord.do" method="post">
                <div class="ui-inputBox">
                    <span class="name fl">选择日期：</span>
                    <div class="fl ui-datePicker ui-radius">
                        <div class="ui-calendar ui-radius"></div>
                        <input class="size-small" type="text" id="starttime" name="starttime" value="${starttime}" placeholder="开始日期" />
                        <span></span>
                    </div><span class="name fl">&nbsp;--&nbsp;</span>
                    <div class="fl ui-datePicker ui-radius">
                        <div class="ui-calendar ui-radius"></div>
                        <input class="size-small" type="text" name="endtime" id="endtime" value="${endtime}" placeholder="结束日期" />
                        <span></span>
                    </div>
                </div>
                <div class="ui-inputBox">
                    <span class="name fl">游戏平台：</span>
                    <div class="ui-ddl fl size-small color-gray ui-radius">
                    	<#if platformcode==''>
	                        <div class="input fl ui-hand ui-textOverflow" data="" id="selectPlat">全部</div>
                    	<#else>
	                        <div class="input fl ui-hand ui-textOverflow" data="${platformcode}" id="selectPlat">${platformcode}</div>
                    	</#if>
                        <span class="fl"></span>
                        <ul class="ui-radius">
                            <li data="">全部</li>
                            <li data="AGIN">AG国际厅</li>
                            <li data="BBIN">BBIN波音</li>
                            <li data="PT">PT老虎机</li>
                            <li data="MG">MG国际厅</li>
                            <li data="SA">SA老虎机</li>
                        </ul>
                    </div>
                    <span class="name fl">&emsp;&emsp;注单类型：</span>
                    <div class="ui-ddl fl size-small color-gray ui-radius">
				  		<#if betflag==''>
	                        <div class="input fl ui-hand ui-textOverflow" data="" id="bettype">全部</div>
				  		<#elseif betflag==1>
	                        <div class="input fl ui-hand ui-textOverflow" data="1" id="bettype">返水</div>
				  		<#elseif betflag==0>
	                        <div class="input fl ui-hand ui-textOverflow" data="0" id="bettype">不返水</div>
				  		</#if>
                        <span class="fl"></span>
                        <ul class="ui-radius">
                            <li data="">全部</li>
                            <li data="1">返水</li>
                            <li data="0">不返水</li>
                        </ul>
                    </div>
                    <input type="hidden" id="betflag" name="betflag" />
                    <input type="hidden" id="platformcode" name="platformcode" />
                    <input type="hidden" id="pageNo" name="pageNo" />
                    <input type="button" onclick="selBetRecord()" id="betRecord_sub" class="query ui-btn fl size-normal ui-radius color-white bg-orange bg-brownHover ui-transition" value="查询投注记录" />
                </div>
            </form>

            <table class="ui-table ui-mt10 ui-alignCenter">
                <colgroup>
                    <col width="60">
                    <col />
                    <col width="100">
                    <col width="90">
                    <col width="90">
                    <col width="90">
                    <col width="80">
                    <col width="60">
                </colgroup>
                <tr>
                    <th>游戏平台</th>
                    <th>游戏</th>
                    <th>投注时间</th>
                    <th>投注单号</th>
                    <th>投注金额</th>
                    <th>有效投注金额</th>
                    <th>输赢</th>
                    <th>是否返水</th>
                </tr>
                <#if gamebetlog??>
		             <#list gamebetlog as betlog>
		                <tr>
		                    <td class="left">${betlog.platformcode}</td>
		                    <td class="left">${betlog.gamename}</td>
		                    <td class="left" style="width:18%;">${betlog.betdate?date}</td>
		                    <td class="left">${betlog.betno}</td>
		                    <td class="left">${betlog.betamount}</td>
		                    <td class="left">${betlog.validBetAmount}</td>
		                    <td>
								<#if betlog.profitamount lt 0>
				              		${betlog.profitamount}
				                <#else>
				                	${betlog.profitamount}
				                </#if>
							</td>
		                    <td>
								<#if betlog.flag == 1>返水<#else>不返水</#if>
							</td>
		                </tr>
		             </#list>
	                <tr>
	                    <td colspan="4">小计：</td>
	                    <td>${betpointsTotal}</td>
	                    <td>${availablebetTotal}</td>
	                    <td><#if winorlossTotal lt 0>${winorlossTotal}<#else>${winorlossTotal}</#if></td>
	                    <td></td>
	                </tr>
	                <tr>
	                    <td colspan="4">总计：</td>
	                    <td>${betpointsAllPage}</td>
	                    <td>${availablebetAllPage}</td>
	                    <td><#if winorlossAllPage lt 0>${winorlossAllPage}<#else>${winorlossAllPage}</#if></td>
	                    <td></td>
	                </tr>
                <#else>
                	<tr>
                    <td colspan="8">您还没有投注记</td>
                </tr>
                </#if>
            </table>
            <div class="pages" id="pages"></div>
        </div>
    </div>

</body>

</html>