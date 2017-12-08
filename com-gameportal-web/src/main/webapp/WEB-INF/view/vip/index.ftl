<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<#include "${action_path}common/config.ftl">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${title} - VIP俱乐部</title>
${meta}
<link href="${zy_path}css/main.css" rel="stylesheet" type="text/css" />
<link href="${zy_path}css/menu.css" rel="stylesheet" type="text/css" />
<link href="${zy_path}css/vip.css" rel="stylesheet" type="text/css" />
<link href="${zy_path}css/member.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${zy_path}js/jquery.js"></script>
<script src="${zy_path}js/jquery.fancybox.min.js"></script>
<script src="${zy_path}js/Validform_v5.3.2_min.js"></script>
<script src="${zy_path}js/base.js"></script>
<script src="${zy_path}js/login.js"></script>
<script type="text/javascript" src="${zy_path}js/menu.js"></script>
<script type="text/javascript" src="${zy_path}js/main.js"></script>
<script type="text/javascript" language="javascript" src="${zy_path}js/vip.js"></script>

</head>
<body>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td align="center" valign="top" class="top">
	<!--头部-->	
	<#include "common/top.ftl">
	</td>
  </tr>
  <#include "common/bulletin.ftl" />
  <tr>
    <td height="100" align="center" valign="middle" bgcolor="#e4e4e4">
<!--导航菜单开始-->		
<#include "common/header.ftl">
<!--导航菜单结束-->		
	</td>
  </tr>
  <tr>
    <td align="center" valign="top">
	
<!-- banner图片及公告开始 -->
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
	  <tr>
        <td align="center" valign="top" style="background:url(${zy_path}images/vipimg.jpg) no-repeat top center; background-color:#000000; height:380px;">&nbsp;</td>
      </tr>
	</table>
	</td>
  </tr>
  <tr>
    <td align="center" valign="top" style="padding-top:15px; padding-bottom:15px;">
	
<!-- VIP俱乐部开始 -->		
	<table width="1000" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-bottom:15px;">
      <tr>
        <td align="center" valign="top" style="background-color:#cccccc; padding:10px;">
		
		<table width="980" border="0" align="center" cellpadding="0" cellspacing="0">
            <tr>
              <td align="center" valign="middle" style="padding-right:10px;">
			  <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                  <tr>
                    <td width="56" height="63" align="center" valign="middle" class="iconimg"><img src="${zy_path}images/vip/vip_ts01.png" width="56" height="56" /></td>
                    <td height="63" align="left" valign="middle" class="boticonconenct">返水高达1.0%</td>
                  </tr>
              </table>
			  </td>
              <td align="center" valign="middle" style="padding-right:10px;">
			  <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                  <tr>
                    <td width="47%" height="63" align="center" valign="middle" class="iconimg"><img src="${zy_path}images/vip/vip_ts02.png" width="72" height="56" /></td>
                    <td width="53%" height="63" align="left" valign="middle" class="boticonconenct">晋级礼金</td>
                  </tr>
              </table>
			  </td>
              <td align="center" valign="middle" style="padding-right:10px;"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                  <tr>
                    <td width="41%" height="63" align="center" valign="middle" class="iconimg"><img src="${zy_path}images/vip/vip_ts03.png" width="51" height="63" /></td>
                    <td width="59%" height="63" align="left" valign="middle" class="boticonconenct">生日礼金</td>
                  </tr>
              </table></td>
              <td align="center" valign="middle" style="padding-right:10px;"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                  <tr>
                    <td width="38%" height="63" align="center" valign="middle" class="iconimg"><img src="${zy_path}images/vip/vip_ts04.png" width="57" height="57" /></td>
                    <td width="62%" height="63" align="left" valign="middle" class="boticonconenct">赌城豪华游</td>
                  </tr>
              </table></td>
              <td align="center" valign="middle"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                <tr>
                  <td width="42%" height="63" align="center" valign="middle" class="iconimg"><img src="${zy_path}images/vip/vip_ts05.png" width="57" height="64" /></td>
                  <td width="58%" height="63" align="left" valign="middle" class="boticonconenct">资金安全</td>
                </tr>
              </table></td>
            </tr>
        </table></td>
      </tr>
    </table>
	<table width="1000" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td align="center" valign="top" style="padding-top:15px; padding-bottom:15px; background-color:#CCCCCC;">

			<table width="950" border="0" align="center" cellpadding="0" cellspacing="0">
              <tr>
                <td align="left" valign="top">
<!--星级切换开始-->
<div class="pref_list">
<div class="s_outer">
<div class="s_inner">

<div class="list list_hover">
<div class="handle"><img src="${zy_path}images/vip/vip_lev01.jpg" width="73" height="228" /></div>
<dl>
  <dt>星级VIP：</dt>
  <dd><SPAN>投注额要求：</SPAN>开户即有</dd>
  <dd><SPAN>返水：</SPAN>0.5%</dd>
  <dd><SPAN>晋级礼金：</SPAN>无</dd>
  <dd><SPAN>生日礼金：</SPAN>无</dd>
</dl>
</div>
  
  
<div class="list">
<div class="handle"><img src="${zy_path}images/vip/vip_lev02.jpg" width="73" height="228" /></div>
<dl>
  <dt>银卡VIP：</dt>
  <dd><SPAN>投注额要求：</SPAN>周投注额50万</dd>
  <dd><SPAN>返水：</SPAN>0.6%</dd>
  <dd><SPAN>晋级礼金：</SPAN>188元</dd>
  <dd><SPAN>生日礼金：</SPAN>188元</dd>
</dl>
</div>
  
<div class="list">
<div class="handle"><img src="${zy_path}images/vip/vip_lev03.jpg" width="73" height="228" /></div>
<dl>
  <dt>金卡VIP：</dt>
  <dd><SPAN>投注额要求：</SPAN>周投注额200万</dd>
  <dd><SPAN>返水：</SPAN>0.7%</dd>
  <dd><SPAN>晋级礼金：</SPAN>388元</dd>
  <dd><SPAN>生日礼金：</SPAN>388元</dd>
</dl>
</div>
  
<div class="list">
<div class="handle"><img src="${zy_path}images/vip/vip_lev04.jpg" width="73" height="228" /></div>
<dl>
  <dt>钻石VIP：</dt>
  <dd><SPAN>投注额要求：</SPAN>周投注额500万</dd>
  <dd><SPAN>返水：</SPAN>0.8%</dd>
  <dd><SPAN>晋级礼金：</SPAN>588元</dd>
  <dd><SPAN>生日礼金：</SPAN>588元</dd>
</dl>
</div>
  
<div class="list">
<div class="handle"><img src="${zy_path}images/vip/vip_lev05.jpg" width="73" height="228" /></div>
<dl>
  <dt>黑卡VIP：</dt>
  <dd><SPAN>投注额要求：</SPAN>周投注额1000万</dd>
  <dd><SPAN>返水：</SPAN>1.0%</dd>
  <dd><SPAN>晋级礼金：</SPAN>888元</dd>
  <dd><SPAN>生日礼金：</SPAN>888元</dd>
</dl>
</div>
  
</div>
</div>
</div>
<!--星级切换结束-->				
				</td>
              </tr>
			  <tr>
			  <td class="favoname">钱宝VIP俱乐部</td>
			  </tr>
			  <tr>
			    <td align="left" valign="top" class="conenct">
			  <span class="black">钱宝娱乐致力于打造最受欢迎的顶级线上博彩娱乐品牌，为感谢长期支持钱包娱乐的玩家，我们提供业界最高的优惠政策，尊享最人性的VIP服务！加入钱宝VIP俱乐部的您真正感受到来自钱宝的关怀与贵宾待遇！</span><br /><br />
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="1">
  <tr>
    <td height="40" align="center" valign="middle" class="tabletitle">级别</td>
    <td height="40" align="center" valign="middle" class="tabletitle">投注额要求</td>
    <td height="40" align="center" valign="middle" class="tabletitle">返水</td>
    <td height="40" align="center" valign="middle" class="tabletitle">晋级礼金</td>
    <td height="40" align="center" valign="middle" class="tabletitle">生日礼金</td>
  </tr>
  <tr>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">星级VIP</td>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">开户即有</td>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">0.5%</td>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">—</td>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">—</td>
  </tr>
  <tr>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">银卡VIP</td>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">周投注额50万</td>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">0.6%</td>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">188元</td>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">188元</td>
  </tr>
  <tr>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">金卡VIP</td>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">周投注额200万</td>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">0.7%</td>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">388元</td>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">388元</td>
  </tr>
  <tr>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">钻石VIP</td>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">周投注额500万</td>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">0.8%</td>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">588元</td>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">588元</td>
  </tr>
  <tr>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">黑卡VIP</td>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">周投注额1000万</td>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">1.0%</td>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">888元</td>
    <td height="30" align="center" valign="middle" bgcolor="#e4e4e4">888元</td>
  </tr>
</table><br />
			  <span class="favotitle">活动条款：</span><br />
			  1、申请等级提升请联系在线客服或在线经理，不能越级申请。<br />
			  2、一周定义：北京时间上周一中午12:00至本周一11:59之前为一周。<br />
			  3、有效投注额定义：除违规投注（如对冲、对赌），和局退还本金之外所有产生输赢的投注额计为有效投注额。<br />
			  4、如客户跳跃升级，只可享受最高星级的优惠，如客户为1星级，升为5星级，该会员只可获得五星级的相应优惠。<br />
			  5、礼金发放： 晋级礼金无需申请，只要您达到要求，系统会在周一下午6点前自动发放到您的游戏账户中。<br />
			  6、晋级礼金和生日礼金仅需1倍流水即可出款。<br />
			  7、该活动可与洗码同时享受，不可与首存同时共享。<br />
			  8、任何违规投注：对冲、对赌、多账号、软件投注、无风险投注等投注方式，本网站有权利冻结账户并没收账户所有余额。<br />
			  9、任何个人/团体/组织以不诚实的方式恶意骗取网站红利或其他利益，网站有权冻结账户所有余额。<br />
			  10、钱宝娱乐保留最终解释权。
				</td>
			  </tr>
            </table>
			
			</td>
        </tr>
      </table>
<!-- VIP俱乐部结束 -->	
	
    </td>
  </tr>
  <tr>
    <td align="center" valign="top">
	
<!--底部-->
	<#include "common/footer.ftl">
	
	</td>
  </tr>
</table>
<#include "common/leftpos.ftl">
</body>
</html>