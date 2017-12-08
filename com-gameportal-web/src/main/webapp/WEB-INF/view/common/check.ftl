<#include "common/config.ftl">
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>${title}--页面检测</title>
	${meta}
    <link type="text/css" rel="stylesheet" href="/static/css/public/base.css" />
    <script type="text/javascript" src="/static/lib/jquery.js"></script>
    <script type="text/javascript" src="/static/js/public/utils.js"></script>
    <script type="text/javascript" src="/static/js/public/common.js"></script>
    <script type="text/javascript" src="/static/js/public/check.js"></script>
    <style type="text/css">
        .main {
            width: 900px;
            margin: 40px 0 0 50px;
        }
        
        .main h1 {
            height: 40px;
            line-height: 40px;
            font-weight: normal;
        }
        
        .main table {
            width: 100%;
            padding: 20px 0;
        }
        
        .main table td {
            height: 28px;
        }
        
        .main table .lc {
            text-align: right;
        }
        
        .main table .rc {
            text-indent: 1em;
        }
    </style>
    <script type="text/javascript">
        //服务器获取
        var beginTime = new Date().getTime(),
            serverTime = '2015年12月12日 12:12:12',
            ip = '88.88.88.88',
            cdn = false;
    </script>
</head>

<body>
    <#include "common/header.ftl" >

    <div class="wrapper ui-contents border-bottomBrownLight ui-transition" id="container">
        <div class="inner">
            <#include "${zy_path}common/bulletin.ftl" />

            <div class="main pa" id="main">
                <h1 class="bg-orange size-bigger ui-alignCenter">页面检测 - 以下请截屏</h1>
                <table class="color-blue bg-white">
                    <colgroup>
                        <col width="340" />
                        <col />
                    </colgroup>
                    <tbody>
                        <tr>
                            <td class="lc">浏览器：</td>
                            <td class="rc">
                                <div id="browser"></div>
                            </td>
                        </tr>
                        <tr>
                            <td class="lc">操作系统：</td>
                            <td class="rc">
                                <div id="os"></div>
                            </td>
                        </tr>
                        <tr>
                            <td class="lc">语言：</td>
                            <td class="rc">
                                <div id="lang"></div>
                            </td>
                        </tr>
                        <tr>
                            <td class="lc">&nbsp;</td>
                            <td class="rc">&nbsp;</td>
                        </tr>
                        <tr>
                            <td class="lc">服务器时间：</td>
                            <td class="rc">
                                <div id="serverT">${date}</div>
                            </td>
                        </tr>
                        <tr>
                            <td class="lc">本地时间：</td>
                            <td class="rc">
                                <div id="localT"></div>
                            </td>
                        </tr>
                        <tr>
                            <td class="lc">本地时区格林威治GMT：</td>
                            <td class="rc">
                                <div id="GMT"></div>
                            </td>
                        </tr>
                        <tr>
                            <td class="lc">&nbsp;</td>
                            <td class="rc">&nbsp;</td>
                        </tr>
                        <tr>
                            <td class="lc">ip地址：</td>
                            <td class="rc">
                                <div id="ip">${clientIp}</div>
                            </td>
                        </tr>
                        <tr>
                            <td class="lc">&nbsp;</td>
                            <td class="rc">&nbsp;</td>
                        </tr>
                        <tr>
                            <td class="lc">移动设备：</td>
                            <td class="rc">
                                <div id="mobile"></div>
                            </td>
                        </tr>
                        <tr>
                            <td class="lc">初始窗口：</td>
                            <td class="rc">
                                <div id="screen"></div>
                            </td>
                        </tr>
                        <tr>
                            <td class="lc">渲染时间：</td>
                            <td class="rc">
                                <div id="renderTime"></div>
                            </td>
                        </tr>
                        <tr>
                            <td class="lc">&nbsp;</td>
                            <td class="rc">&nbsp;</td>
                        </tr>
                        <tr>
                            <td class="ui-alignCenter" colspan="2">提示：如果您已启动qq，请按组合键：ctrl+alt+A 进行截图，然后在聊天窗口中按组合键 ctrl+v 进行粘贴</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <#include "${zy_path}common/footer.ftl" />
	<#include "${zy_path}common/rightSlider.ftl" />
</body>

</html>