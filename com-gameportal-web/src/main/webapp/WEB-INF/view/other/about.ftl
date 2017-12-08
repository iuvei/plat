<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<#include "${action_path}common/config.ftl">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${title} - 关于钱宝娱乐</title>
	${meta}
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <link type="text/css" rel="stylesheet" href="${action_path}css/base.css">
    <link type="text/css" rel="stylesheet" href="${zy_path}static/css/public/base.css" />
    <script type="text/javascript" src="/static/lib/jquery.js"></script>
    <script type="text/javascript" src="/plugins/attention/zDialog/zDrag.js"></script>
	<script type="text/javascript" src="/plugins/attention/zDialog/zDialog.js"></script>
	<script type="text/javascript" language="javascript">    
	var diag = new Dialog();    
	</script>
</head>

<body class="bg-gray">
<div class="wrapper ui-header" id="ui-header">
    <#--头部-->
    <#include "${action_path}common/top.ftl">
    <#--导航-->
    <#include "${action_path}common/header.ftl">
</div>
<br>
<div class="container cfx">
   	<#include "${action_path}common/leftpos.ftl">
   	
    <div class="gb-main-r tab-bd bg-white">
     <div id="tab-notice" class="tab-panel active">
            <div class="m-content aboutus-box">
                <h1><i class="iconfont icon-8"></i>站内信  NOTICE</h1>
                <hr class="line">
				  <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" >
					  <tr>
					    <td align="left" valign="top" class="conenct">
					    <#if bulletionList??>
					    	<div class="letter-info" id="show-letter">
					    		<ul>
									<#list bulletionList as bull>
										<li><div class="square"></div><a href="javascript:showBull(${bull.id});" data-letterid="${userBulletin?exists && userBulletin.bid?index_of(bull.id?string)!=-1}" class="letter">${bull_index+1}、${bull.title}&nbsp;&nbsp;<span class="s">${bull.editTime?string('yyyy-MM-dd HH:mm:ss')}&nbsp;&nbsp;<#if userInfo??>(<#if userBulletin?exists && userBulletin.bid?index_of(bull.id?string)!=-1>已读<#else><font color="red">未读</font></#if>)</#if></span></a></li>	
									</#list>
								</ul>
							</div>
						</#if>
		          		</td>
				  	</tr>
				  </table>
            </div>
        </div>
        <div id="tab-aboutus" class="tab-panel">
            <div class="m-content aboutus-box">
                <h1><i class="iconfont icon-8"></i>关于钱宝 About Us</h1>
                <hr class="line">
				 <h2 class="tit">钱宝娱乐简介</h2>
                <p>   钱宝娱乐是菲律宾钱宝实业集团旗下的直营网站之一，菲律宾钱宝实业集团主要经营线上娱乐房地产汽车业务。钱宝娱乐是目前亚洲市场上最具公信力及最受玩家欢迎的大型博彩网站之一，从2006年开始致力于互联网为用户提供高质数及多元化的娱乐服务，凭着周到的客户服务能力，完善的技术保障和支持能力，高效健全的支付平台，至今钱宝娱乐在全球己拥有超过一千万注册账户。钱宝娱乐定坚必守「不断创新，力求进步」的宗旨，不断竭诚为客护提供更多元化的休闲娱乐。钱宝娱乐隶属于—LUCKY STAR ENTERTAINMENT LIMITED。注册于维京群岛，并获得菲律宾FIRSTCAGAYAN博彩牌照NO.020-A。公司已有10年历史。我们于2005年成功打入互联网博彩市场，公司总部设在菲律宾马尼拉。钱宝娱乐与世界领先合法博彩娱乐PT平台，MG平台，AG平台及BBIN平台系统商进行技术上合作。提供有亚洲最多元，专业，顶尖，公正，安全的线上娱乐产品服务。 钱宝娱乐提供给玩家的娱乐产品丰富多样化，有多种特色真人线上游戏及老虎机任您选择。加入钱宝娱乐，畅享无与伦比的游戏体验。钱宝娱乐绝对是您最明智的选择! 
                </p>

                <h2 class="tit">专业运营</h2>
                <p>钱宝娱乐坚持为客户打造一个稳定、顺畅、安全和高度信任的网络服务平台。依靠具有多年经验的强大国际博彩专业团队为基础，我们建立了一整套国际领先的管理体系。我们拥有世界一流博彩企业的坚实品牌基础和遍及极具规模的客户群，并拥有具有独到眼光，在任何时候都以客户利益至上的市场策略专家团队作强力后盾。我们投入了大量的时间和精力用于广泛地分析和界定客户的需求，目的在于能够为客户提供全方位国际化的优质服务。钱宝娱乐在专业运作方面取得了各个方面的权威机构认证，如ISO9001优质质量管理体认证。我们自身的积极努力被业界所肯定，更与同行业知名公司通力合作创造更辉煌的业绩。我们依靠强大的运营能力，专业优质的客户服务能力，尖端的开发能力及技术支援能力，成为全球网上服务平台的成功典范。</p>
                <h2 class="tit">账户与资金</h2>
				<p>玩家网上支付和所有银行交易由国际金融机构在高标准的安全和机密的网络端口（SSL 128 bit encryption Standard）中进行。进入玩家帐户数据也必须有玩家唯一的登录ID和密码，确保客户的资金安全有保障。同时也采用最先进的加密措施来保证玩家的游戏安全，7×24小时的后台检测和监控，确保我们可以提供一个完全公正和安全的网络游戏空间。客户在本平台的所有活动均严格保密，我们绝不会向第三方（包括政府）透露客户数据。</p>
                <h2 class="tit">诚信为本</h2>
				<p>时至今日，我们于服务水平及客户满意度方面亦已竖立威信，通过强大的游戏营运能力，週到的客户服务能力，完善的技术保障和支持能力，高效健全的支付平台，形成了面向用户的综合性互动电子游戏平台。各年龄层的玩家均可籍由钱宝娱乐真人互动游戏平台与其它成千上万的玩家进行互动，体验互动电子娱乐带来的乐趣。作为钱宝娱乐实现“全球最大的真人游戏平台”的战略目标的重要部分，我们通过对软硬件、内容、网络以及服务的整合，为用户提供更高质的娱乐服务。</p>
				<h2 class="tit">付款机制</h2>
				<p>因应国际间有关各国货币汇率随时变动及外币管制等诸多因素，我们为了让中国区会员能尽情享受最快速、最安全的收获成果，特别规划出完整的的人民币付款机制。当阁下成为我们的会员时，就等同于拥有了一个亚洲通用无存款记录的保险箱 ，您可在亚洲境内的任何一个地方要求提款，每年的365天，提款迅速，安全是我们给会员最好的信用保障！</p>
				<h2 class="tit">特色及优势</h2>
				<p>多渠道的存款方式，采用256位SSL国际标准加密技术及专业的客服团队7*24为您服务。我们秉承客户至上的理念，为您提供安全，便捷，放心优质的服务。</p>
				<p>01秒存，自助优惠，技术团队独立研发，业界领先。<br>
				02高佣金比例的合作模式，您可以赚的比我们还多。<br>
				03多产品，多平台，多项目，让会员玩得更尽兴。<br>
				04在市场上享有优质的公信力，毋庸置疑。<br>
				05详细的分析报表功能，让您钱途无量，事事顺利。</p>
            </div>
        </div>
        <div id="tab-ad" class="tab-panel">
        	  <div class="m-content aboutus-box">
                <h1><i class="iconfont icon-8"></i>新手帮助</h1>
                <hr class="line">
                <h2 class="tit">1、如何免费开户？</h2>
                <p>只需20秒，您就可以拥有一个钱宝娱乐的账户。点击右上角黄色的“免费注册”按钮，输入您对应的用户名、密码、电子邮箱后就可以轻松开户。</p>
                <h2 class="tit">2、忘记用户名怎么办？</h2>
				<p>如果由于长时间未登录或其他原因忘记您的会员账号用户名，请您点击联系在线客服并提供您的注册邮箱、绑定手机、绑定姓名或其他相关注册信息，我们在线客服会在审核通过后为您提供正确的用户名。</p>
                <h2 class="tit">3、忘记密码怎么办？</h2>
				<p>在输入密码前请确保您的密码至少为6个字符，假如您不小心忘记密码，您可以点击“忘记密码？”进行找回密码操作。找回密码的前提是您已经绑定手机或已经绑定邮箱。如果您都没有绑定，请您点击此处联系在线客服。</p>
				<h2 class="tit">4、我的个人资料安全吗？</h2>
				<p>钱宝娱乐是建立于菲律宾马尼拉的合法博彩公司。我们所有的数据都是通过安全的输送管道（SSL 128位元加密标准）传送，储存在外界无法入侵的安全操作系统中。我们除非得到客户本人的同意或者法律的要求，否则我们不会将客户任何资料透漏给第三方。</p>
				<h2 class="tit">5、允许我在钱宝娱乐投注吗？</h2>
				<p>在浏览和体验前，请确保您已年满18岁并同意我们的规则与条款。不同的国家有不同的监管条例，投注前请遵循当地法规与法律。若因上述原因造成的任何损失，钱宝娱乐官方概不负责。</p>
            </div>
        </div>
        <div id="tab-protocol" class="tab-panel">
             <div class="m-content aboutus-box">
                <h1>用户协议</h1>
                <hr class="line">
				<h2 class="tit">1. 账户条例</h2>
               		1.1 用户声明并承诺，用户在注册开户时所提供的资料，包括用户姓名、电邮地址、电话号码及ip地址，均真实、准确、完整。<br /> 
					1.2 用户必须符合法定年龄（用户其居住地区的法律年龄限制），在不违反的情况下方可使用本网站的服务。用户如违法当地法律问题，本公司将不承担因客户违反当地博彩法律所引致之任何责任。<br />
					1.3 用户所拥有的账户姓名必须与以下的身份证明一致：<br /> 
					1.3.1 身份证上的姓名。<br /> 
					1.3.2 银行账户的姓名。<br />
					1.4 用户拥有超过一个账户，包括同一姓名，同一邮件地址，同一家庭，同一住址，同一借记卡/信用卡，同一银行账户，同一电脑（大学，团体，网吧，图书馆，办公室等），被视为联合账户或可疑账户，本网站有权在不通知的情况下冻结或关闭相关账户，并不退还款项。<br /> 
					1.5 所有账户只限持有者一人使用，用户有责任确保用户的账户及密码隐私安全，如用户有意/无意透露或泄露账户资料导致被盗，或第三方以任何形式获得用户的账户及密码及进行游戏导致用户账户损失，所有损失及责任需由用户全部承担。<br /> 
					1.6 所有账户若出现资金有误，用户有义务通知本网站作账户调整及不可使用相关错误金额，任何情况下，相关错误金额被视为无效金额，若用户使用该笔金额进行投注，所有投注被视为无效，一律取消并返还於本网站。<br /> 
					1.7 倍投在钱宝娱乐属于套利行为，将不享有任何反水。如果您的单笔倍投金额超过存款金额，将扣除盈利退还本金。<br />
					1.8 禁止职业算牌客，全压庄闲大小的投注方式，若经发现本网站有权在不通知的情况下冻结或关闭相关账户，并不退还款项。</p>
                <h2 class="tit">2. 提款条例</h2>
               		2.1 用户每日最高派彩金额为1000万人民币，即所有玩法总和的派彩金额。<br />
					2.2 关于老虎机累计奖池平台费收取,若您中累计大奖在人民币5万以上，我们将会收取您20%的平台费用于平台建设。
					2.3 所有提款申请必须符合本网站的提款政策，如存款未达到全额投注，即无法通过提款审核，提款申请将会被驳回。<br /> 
					2.4 若有需要，用户必须提供个人资料如身份证副本、驾驶执照副本、银行交易记录截图、居民户口簿副本等资料。<br /> 
					2.5 用户被严禁使用他人或第三者银行账户或银行卡进行取款业务。<br />
					2.6 严禁客户用非真实ip地址登录游戏(vpn,远程主机等)。</p>
				<h2 class="tit">3. 存款条例</h2>
					3.1 通过网银自助存款方式的会员，如果忘记填写附言，请及时联系在线客服。特别说明如果出现存款人姓名与游戏账户姓名不一致的情况，请配合提供网银电子回单，以便财务人员进行补单。<br />
					3.2 通过第三方支付进行存款的会员，如果出现掉单的情况，请及时联系在线客服，并提供订单号，存款金额，存款时间，以便我们尽快为您补单。</p>
				<h2 class="tit">4. 优惠条例</h2>
					4.1 若要获得本网站所有的优惠红利或回赠，用户必须符合所有条例及附加条款，方可享有相关优惠。<br /> 
					4.2 用户涉及个人或团体以不正当的手法如下述，进行骗取或诈骗相关优惠奖金，本网站有权在不通知的情况下冻结或关闭相关账户，并不退还款项，用户将会被列入黑名单，并保留追究权利。<br />
					4.2.1 用户拥有超过一个账户，包括同一姓名、同一邮件地址、同一住址、同一借记卡/信用卡、同一银行账户、同一电脑（大学，团体，网吧，图书馆，办公室等），被视为联合账户或可疑账户。<br /> 
					4.2.2 无风险投注，对赌行为。<br /> 
					4.3 钱宝娱乐有权根据会员情况调整会员投注限额。<br />
					4.4 钱宝娱乐拥有对优惠活动的最终解释及修改权。</p>
            </div>
        </div>
        <div id="tab-lxbc" class="tab-panel">
           <div class="m-content aboutus-box">
                <h1>免责条款</h1>
                <hr class="line">
				<p>享用本公司提供的服务是客户本人的意愿，其风险应由客户本人承担。参与我们服务的同时就说明客户认同本公司所提供的服务是正常、合理、公正、公平的。</p>
				<p>某些法律规则并未明文规定在线和非在线博彩是否合法，而有些法律规则已有明确规定。如果按照客户当地法律博彩属不合法行为，本公司不提倡阁下参与我们的服务。在博彩被视为不合法行为的任何国家本公司不提倡其国民加入。客户有责任确保任何时候您的博彩行为在您所属地是属合法行为。</p>
				<p>对于任何情况下由于卫星收讯不良或延误，网路中断，或个人使用网站服务时的失误，疏忽，或对网站内容的误解所产生的任何损失，钱宝娱乐概不负责。</p>
				<p>由于第三方对公司网站和信息提供的服务（比如无线通讯的提供者等）而导致的拖延、违约、或不提供服务的情况，本公司不提供担保也不承担责任。</p>
				<p>客户访问或使用我们网站服务、软件、信息服务、或下载、安装、使用我们软件而造成的任何直接、间接或重大或普通亏损或经济损失，不论钱宝娱乐是否被告知其可能性，钱宝娱乐以及相关合伙公司，合作伙伴，合伙人，职员，代理将不承担责任。</p>
				<p>客户需认同本协议部分或全部内容是临时性的，网站部分信息可能还需修改和更正。所以这些信息仅供参考，在客户与本司之间并不构成协议，合同，和担保的依据。</p>
				<p>客户认同所有免责条款都是在公平公正，风险及利益分布合理的情况下成立的，它既反映了钱宝娱乐对客户的关注，也体现了客户对钱宝娱乐的理解与支持。进一步认同此条款的合法性和可实施性。</p>
				<p>如果客户电脑配件上的赛果与我们服务器的赛果出现不一致的情况，我们服务器所显示的结果将为最终赛果。另外，客户也认同对于客户参与我们服务的游戏规则，投注状况钱宝娱乐拥有最终解释权。</p>
				<p>钱宝娱乐尽全力为客户提供最优质，最安全可靠的服务，然而我们并不担保我们所提供的服务的稳定性，及时性和准确性，或不担保错误及时更正或网站不受病毒或蠕虫干扰。</p>
				<p>钱宝娱乐绝对有权临时或永久性地立即暂停，终止，修改，删除或添加服务内容，而无需通知客户，对因其而产生的任何损失钱宝娱乐概不负责。</p>
            </div>
        </div>
        <div id="tab-privacy" class="tab-panel">
        	<div class="m-content aboutus-box">
	            <h1>隐私保护</h1>
	            <hr class="line">
	            <p>我们尊重您的私隐权并保证您数据的安全，这是我们的头等重任。</p>
				<p>钱宝娱乐绝对不会泄露您的个人资料给第三方，除非收到法庭传单或应可行法律的要求及判决。我们有权通过网站向有关付款平台服务提供商以及金融保险机构提供必要的个人信息以完成付款要求。所有用户提供的个人信息，其传送都将通过安全端口（ 256位 SSL 加密标准）并存放在公众无法进入的保密环境之下。所有数据的内部出入都受到严格的限制及严密的监控。</p>
				<p>钱宝娱乐与我们的合作伙伴会通过邮件将您可能感兴趣的促销优惠的消息发送给您。</p>
				<p>钱宝娱乐不会透露任何能识别个人身份的资料给任何第三方，这是我们隐私权政策的宗旨。</p>
			</div>
        </div>
        <div id="tab-disclaimer" class="tab-panel">
            <div class="m-content aboutus-box">
                <h1>免责申明</h1>
                <hr class="line">
				<h2 class="tit">问题性博彩问卷调查</h2>
               		<span class="samlltitle">如果您担心博彩会占据您的（或他人的）生活，以下提问可以帮您找到答案：</span><br />
					1、您会否因为博彩而不上班或不上学？<br />
					2、您博彩是否为了逃避烦闷或不愉快的生活？<br />
					3、当您博彩花光钱时，您是否感到失望甚至绝望，并且希望马上能再进行赌博？<br />
					4、您会否博彩到最后一分钱彩罢休？<br />
					5、您有否曾经用谎言掩饰您花在博彩上的时间和金钱？<br />
					6、是否有人批评过您的博彩行为？<br /> 
					7、您是否对您的家人，朋友或爱好失去了兴趣？<br />
					8、您输了之后，是否马上想把输的钱赢回来？<br /> 
					9、您博彩是不是因为争执，沮丧或失望？<br /> 
					10、您有没有因为博彩而感到失落甚至轻生？<br />
					在这些问题中您回答的‘是’越多，您就越接近具备严重博彩问题。</p>
                <h2 class="tit">关于控制您投注的信息</h2>
               		<span class="samlltitle">在玩家用他们的方式博彩的同时，博彩也成了某些人的问题。以下提示也许能帮助您控制博彩问题：</span><br />
					1、博彩只是一种娱乐，不应被视为赚钱的方法；<br />
					2、避免有把输的钱赢回来的想法；<br />
					3、在您经济允许的范围内进行投注；<br />
					4、时刻控制花在博彩上的时间和金钱；<br />
					5、如果您暂时不想博彩了，可以联系客服为您关闭账户。<br />
				<h2 class="tit">家长控制</h2>
					父母或者监护人可以使用一系列的第三方软件来监控或者限制电脑的互联网使用:<br />
					1、Net Nanny过滤软件防止儿童浏览不适宜的网站内容: www.netnanny.com<br />
					2、CYBERsitter过滤软件允许父母增加自定义过滤网站: www.cybersitter.com</p>
            </div>
        </div>
         <div id="tab-callwe" class="tab-panel">
            <div class="m-content aboutus-box">
                <h1>联系我们</h1>
                <hr class="line">
				<p><span class="samlltitle">欢迎您通过以下的联系方式随时联系我们！</span><p />
				钱宝客服中心：为您提供7/24小时&nbsp;&nbsp;<a href="http://chat6.livechatvalue.com/chat/chatClient/chatbox.jsp?companyID=672531&configID=47081&jid=3980564989" target="_blank">联系在线客服</a><p/>
				钱宝官网域名：www.qianbao777.com<p />
				备用域名：请加客服QQ，或发邮件到官网邮箱咨询<p/>
				钱宝QQ客服：65360505<p />
				钱宝QQ售后：1829193761<p />
				邮箱：qianbao777@gmail.com <br/>
            </div>
        </div>
    </div>
</div>
<!--底部-->
<#include "${action_path}common/footer.ftl">
</html>

<script type="text/javascript">
function showBull(bid){
 diag.Drag=true;
 diag.Title ="详细信息";
 diag.URL = '${action_path}readMsg.do?bid='+bid+"&vt="+new Date().getTime();
 diag.Width = 550;
 diag.Height = 250;
 diag.CancelEvent = function(){
	diag.close();
	var ubid ="";
	<#if userBulletin??>
		var ubid ="${userBulletin.bid}";
	</#if>
	<#if userInfo??>
	if(ubid =="" || (ubid+",").indexOf(bid) ==-1){
		window.location.reload();
	}
	</#if>
 };
 diag.show();
 var html = $("#_DialogDiv_0").html();
 html = html.replace(new RegExp("plugins","gm"),'/plugins');
 $("#_DialogDiv_0").html(html);
 return;
}

function getParameter(param)
{
var query = window.location.search;
var iLen = param.length;
var iStart = query.indexOf(param);
if (iStart == -1)
　return "";
iStart += iLen + 1;
var iEnd = query.indexOf("&", iStart);
if (iEnd == -1)
　return query.substring(iStart);
return query.substring(iStart, iEnd);
}

 //选择菜单
var hash = 'znx';
var url = window.location.href;
if(url.indexOf("#")>-1){
	hash = url.split("#")[1];
} 
$target = $("[href=#" + hash + "]");
$target.length === 1 ? $target.click() : $links.eq(1).click();
</script>