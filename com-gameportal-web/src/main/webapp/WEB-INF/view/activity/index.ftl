<!DOCTYPE html>
<html>
<head>
	<#include "${action_path}common/config.ftl">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	${meta}
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>${title}--优惠活动</title>
    <link type="text/css" rel="stylesheet" href="${action_path}css/base.css">
    <link type="text/css" rel="stylesheet" href="${action_path}css/promotion.css">
</head>

<body class="bg-gray promotion-page">
<div class="wrapper ui-header" id="ui-header">
    <#--头部-->
    <#include "${action_path}common/top.ftl">
    <#--导航-->
    <#include "${action_path}common/header.ftl">
</div>
<div class="favo_banner"> <img src="/static/images/banner/favo_banenr.jpg" alt="钱宝娱乐"></div>
<div class="container cfx">
    <div class="gb-sidenav type-promtion">
        <h1>全部</h1>
        <ul class="navlist" id="j_nav">
            <#--老虎機:SL,真人:LI,keno:KE,體育:SP-->
            <li> <a href="#j_p_list" data-type="SL"><i class="iconfont icon-slot"></i>老虎机</a> </li>
            <li><a href="#j_p_list" data-type="LI"><i class="iconfont icon-chips"></i>真人娱乐</a> </li>
            <li><a href="#j_p_list" data-type="KE"><i class="iconfont icon-8"></i>彩票</a></li>
            <li><a href="#j_p_list" data-type="SP"><i class="iconfont icon-sport"></i>体育平台</a></li>
        </ul>
    </div>
    <div class="gb-main-r tab-bd">
            <div data-type="|SL|LI" class="promotion-item">
                <div class="promotion-brief">
                    <h1 class="promotion-tit">2016年"钱宝VIP会所"全面大升级 </h1>
                    <div class="pic"> <img src="static/images/activity/VIP.jpg"  width="950" height="170"> </div>
                </div>
                <div class="promotion-content m-content">
                    <h2>钱宝娱乐为会员打造"猴赛雷"VIP会所,享更多优惠,2016年"钱宝VIP会所"全面大升级</h2>
                    <p>只要您当月累计存款够多,月总投注额够大,您都可以加入"钱宝VIP"大家庭，获得"钱宝VIP"称号.更有每月"免费礼金", "高额返水", "生日礼金", "存送优惠"等着您,赶快加入吧!</p>                  
                    <h3>活动对象</h3>
                    <p>钱宝娱乐城新老会员</p>
                    <h3>活动平台</h3>
                    <p>PT,MG经典老虎机，吃角子老虎机及BBIN（糖果派对，连环夺宝）</p>
                  <table class="table">
              <tbody>
              <tr>
                <th width="68"></th>
                <th width="210">星级VIP</th>
                <th width="202">银卡VIP</th>
                <th width="195">金卡VIP</th>
                <th width="195">钻石VIP</th>
                <th colspan="2">黑卡VIP</th>
                </tr>
              <tr>
                <td rowspan="3">晋级条件 <br>
                  （流水）</td>
                <td>A所有平台总投注额30万</td>
                <td>A所有平台月总投注额120万</td>
                <td>A所有平台月总投注额360万</td>
                <td>A所有平台月总投注额600万</td>
                <td colspan="2" rowspan="3">特邀玩家</td>
                </tr>
              <tr>
                <td>B所有老虎机平台总投注额10万</td>
                <td>B所有老虎机月总投注额40万</td>
                <td>B老虎机月总投注额120万</td>
                <td>B老虎机月总投注额200万</td>
                </tr>
              <tr>
                <td>C当月累积存款5万</td>
                <td>C当月累积存款20万</td>
                <td>C当月累积存款60万</td>
                <td>C当月累积存款100万</td>
                </tr>
              <tr>
                <td>保级要求</td>
                <td colspan="6">A ： 所有平台月总流水的20% <br>
                  B ：老虎机平台月总流水的 18%</td>
              </tr>
              </tbody>
            </table>
           <p> 注：1.晋级条件中的A、B、C 满足任一条件即可晋级;保级要求中的A、B满足任一条件即可保级.例: 星级保级要求总流水 80万 * 20% =16万 或 老虎机平台40万 * 15% = 6万</p>
           <table class="table">
              <tbody>
              <tr>
                <th width="142"></th>
                <th width="287">星级VIP</th>
                <th width="266">银卡VIP</th>
                <th width="288">金卡VIP</th>
                <th width="226">钻石VIP</th>
                <th width="314">黑卡VIP</th>
                </tr>
              <tr>
                <td>免费彩金</td>
                <td>8</td>
                <td>38</td>
                <td>88</td>
                <td>188</td>
                <td>588</td>
                </tr>
              <tr>
                <td>晋级礼金</td>
                <td>88</td>
                <td>188</td>
                <td>588</td>
                <td>888</td>
                <td>1888</td>
                </tr>
              <tr>
                <td>生日礼金</td>
                <td>88</td>
                <td>188</td>
                <td>588</td>
                <td>888</td>
                <td>1888</td>
                </tr>
              <tr>
                <td>返水上限</td>
                <td>28888</td>
                <td>28888</td>
                <td>无上限</td>
                <td>无上限</td>
                <td>无上限</td>
                </tr>
              <tr>
                <td>AG存送优惠</td>
                <td></td>
                <td>存款的10%最高888需6倍流水</td>
                <td>存款的15%最高1888需8倍流水</td>
                <td>存款的20%最高3888需15倍流水</td>
                <td>存款的38%最高8888需22倍流水</td>
                </tr>
              <tr>
                <td>老虎机平台次存优惠</td>
                <td>存款的20%
                  最高3888元
                  15倍流水</td>
                <td>存款的20%
                  最高3888元
                  15倍流水</td>
                <td>存款的20%
                  最高3888元
                  15倍流水</td>
                <td>存款的20%
                  最高3888元
                  15倍流水</td>
                <td>存款的25%
                  最高3888元
                  15倍流水</td>
                </tr>
              </tbody>
            </table>
                    <h3>活动规则</h3>
                    <p>1.每个月5号系统自动审核进行升级；系统将以晋级条件中的A、B、C来进行升级。</p>
                    <p>2.会员如达到晋级条件中的A、B或C，也可联系在线客服申请。</p>
                    <p>3.会员的"晋级礼金"只可以领取一次。"免费彩金"每月均可领取。</p>
                    <p>4.会员每月只享有一次晋级的机会，且各等级"晋级礼金"每位会员只可获得一次。</p>
                    <p>5.VIP会员每月"免费礼金"无需申请，每月5号系统自动升降级执行之后，"免费礼金"会在每月6号18:00之前自动派发到账。</p>
                    <p>6.生日礼金(生日礼金需在您生日当天向"在线客服"或"QQ客服"进行申请）、"晋级礼金"及每月"免费礼金"无需流水，可直接提款。</p>
                    <p>7.AG存送优惠VIP会员每个月只享有一次机会；PT/MG/SA次存优惠会员每个月不限申请次数。</p>
                    <p>8.所有会员申请优惠的会员等级按照每个月5号的等级为准。（例如：该玩家申请优惠时1号的等级为"金卡VIP"，15号的等级为星级VIP，则按照5号晋级之后等级进行计算）</p>
                    <p>9.未达到保级要求将自动降级，将不再另行通知。</p>
                    <p>10.本优惠禁止倍投，违反规定，将扣除违规盈利，退还优惠重新投注。</p>
                    <p>11.钱宝娱乐拥有最终解释权和裁决权。</p>
                    <a href="javascript:;" class="btn-reback">返回</a> </div>
            </div>
            
 
            <div data-type="|SL" class="promotion-item">
                <div class="promotion-brief">
                    <h1 class="promotion-tit">每日签到，彩金送不停，天天签，天天有！</h1>
                    <div class="pic"> <img src="static/images/activity/20170608_2.jpg"  width="950" height="170"> </div>
                </div>
                <div class="promotion-content m-content">
                 <h3>活动对象</h3>
                 <p>钱宝娱乐城所有有效会员</p>
                 <h3>活动时间</h3>
                 <p>2017年6月12日-网站提前24小时公告</p>
                  <h3>活动内容</h3>
                 <p>会员每日签到即可获得免费彩金，天天签，天天有。</p>
                  <h3>活动游戏</h3>
                 <p>钱宝所有平台游戏</p>
                  <table class="table">
	              <tbody>
	              <tr>
	                <th>连续签到天数</th>
	                <th>可获签到彩金</th>
	              </tr>
				  <tr>
	                <td>1-2天</td>
	                <td>0.5元</td>
	              </tr>
	                <tr>
	                <td>3天</td>
	                <td>1元</td>
	              </tr>
	              <tr>
	                <td>4-5天</td>
	                <td>1.5元</td>
	              </tr>
	             <tr>
	                <td>6-7天</td>
	                <td>2元</td>
	              </tr>
	              <tr>
	                <td>全勤奖</td>
	                <td>28元</td>
	              </tr>
	              </tbody>
	            </table>
              <h3>活动规则</h3>
              <p>1.签到定义：会员当日需存款总额大于等于100元，签到彩金为 0.5元，每个会员每日只能签到一次，彩金均无流水要求即可提款。</p>
              <p>2.一周7天中途有断开一天没签到，签到彩金降为 0.5元；</p> 
              <p>3.会员在 “账户管理”——“优惠领取”——“钱宝今日签到”里面进行自助领签到彩金。</p> 
              <p>4.会员连续签到七天，即可得到<label style="color:red;font-weight:bold;"> 周彩金9元 </label>签到彩金，若会员连续签到一个月，即可获得<label style="color:red;font-weight:bold;"> 全勤奖28元 </label>签到彩金。</p> 
              <p>5.钱宝娱乐城保留对本优惠的修订、终止和最终解释权。以及在无通知的情况下作出改变本次活动的权利。或者通过邮件方式通知会员、作出改变的权利。本次活动的最终解释权归钱宝娱乐城所有。</p>  
              	<a href="javascript:;" class="btn-reback">返回</a>
                </div>
            </div>
            
             <div data-type="|SL" class="promotion-item">
                <div class="promotion-brief">
                    <h1 class="promotion-tit">钱宝积分换现金，每日返水+积分，投注疯狂送！</h1>
                    <div class="pic"> <img src="static/images/activity/20170610_2.jpg"  width="950" height="170"> </div>
                </div>
                <div class="promotion-content m-content">
                 <h3>活动对象</h3>
                 <p>钱宝娱乐城所有有效会员</p>
                 <h3>活动时间</h3>
                 <p>2017年6月12日-网站提前24小时公告</p>
                  <h3>活动游戏</h3>
                 <p>钱宝所有平台游戏</p>
                  <h3>活动内容</h3>
                 <p>您投注，高额返水+积分，人生处处有逆袭。在钱宝娱乐城所有游戏中，每10元的投注额可为您赢1积分。<br/>
                 	举例来说：如果您在 老虎机游戏中总共投注了1000人民币，那么您同时获得了100个积分。这些积分是可以兑换为现金的！您投注越多，那么您获得积分也就越多！
                 </p>
                  <table class="table">
	              <tbody>
	              <tr>
	                <th>会员等级</th>
	                <th>兑换规则</th>
	              </tr>
				   <tr>
	                <td>新会员&星级VIP</td>
	                <td>500积分兑换1元</td>
	              </tr>
	             	<tr>
	                <td>银卡VIP</td>
	                <td>400积分兑换1元</td>
	              </tr>
	              <tr>
	                <td>金卡VIP</td>
	                <td>300积分兑换1元</td>
	              </tr>
	               <tr>
	                <td>钻石VIP</td>
	                <td>250积分兑换1元</td>
	              </tr>
	              <tr>
	                <td>黑卡VIP</td>
	                <td>200积分兑换1元</td>
	              </tr>
	              </tbody>
	            </table>
              <h3>活动规则</h3>
              <p>1.积分将于每天下午14:00进行派发，且无流水要求即可提款。</p>
              <p>2.每位玩家、每户、每一住址、每一电子邮箱地址及共享电脑环境只能拥有一个帐户。</p> 
              <p>3.会员在 “账户管理”——“优惠领取”——“钱宝积分兑换”里面进行自助兑换现金。</p> 
              <p>4.钱宝娱乐城保留对本优惠的修订、终止和最终解释权。以及在无通知的情况下作出改变本次活动的权利。或者通过邮件方式通知会员、作出改变的权利。本次活动的最终解释权归钱宝娱乐城所有。</p>  
              	<a href="javascript:;" class="btn-reback">返回</a>
                </div>
            </div>
			
            <div data-type="|SL" class="promotion-item">
                <div class="promotion-brief">
                    <h1 class="promotion-tit">钱宝娱乐PT/MG/SA次次存笔笔送，送到您心满意足</h1>
                    <div class="pic"> <img src="static/images/activity/cccs.jpg"  width="950" height="170"> </div>
                </div>
                <div class="promotion-content m-content">
                 <h3>活动对象</h3>
                 <p>钱宝娱乐城新老会员</p>
                 <h3>活动时间</h3>
                 <p>本活动长期有效</p>
                  <table class="table">
              <tbody>
              <tr>
                <th>等级</th>
                <th>存款要求</th>
                <th>红利</th>
                <th>最高红利</th>
                <th>流水倍数</th>
              </tr>
			   <tr>
                <td>新会员</td>
                <td>次次存送（10元以上）</td>
                <td>本金*10%(或20%)</td>
                <td>2888</td>
                <td>10倍（或20倍）</td>
              </tr>
              <tr>
                <td>星级VIP</td>
                <td>次次存送（10元以上）</td>
                <td>本金*10%(或20%)</td>
                <td>2888</td>
                <td>10倍（或18倍）</td>
              </tr>
              <tr>
                <td>银卡VIP</td>
                <td>次次存送（10元以上）</td>
                <td>本金*10%(或20%)</td>
                <td>2888</td>
                <td>9倍（或15倍）</td>
              </tr>
              <tr>
                <td>金卡VIP</td>
                <td>次次存送（10元以上）</td>
                <td>本金*10%(或20%)</td>
                <td>2888</td>
                <td>8倍（或14倍）</td>
              </tr>
              <tr>
                <td>钻石VIP</td>
                <td>次次存送（10元以上）</td>
                <td>本金*10%(或20%)</td>
                <td>2888</td>
                <td>6倍（或13倍）</td>
              </tr>
              <tr>
                <td>黑卡VIP</td>
                <td>次次存送（10元以上）</td>
                <td>本金*10%(或20%)</td>
                <td>2888</td>
                <td>5倍（或12倍）</td>
              </tr>
              </tbody>
            </table>
              <h3>活动规则</h3>
              <p>1.会员首先登陆PT/MG/SA平台进行激活。</p>
              <p>2."钱宝娱乐"会员首次注册并激活游戏平台即可享受"10%存送优惠"，该优惠的本金和红利必须全额转入PT/MG/SA平台游戏，并在的PT/MG/SA平台中完成投注流水要求。</p> 
              <p>3.此优惠无需和在线客服或者QQ客服申请，在官网中点击"账户管理"—— "钱包管理"——"我要存款"——"最新优惠"，选择"10%存送优惠"。 
温馨提示：若先将本金先转入游戏就无法申请该优惠。</p> 
              <p>4.提款要求：（本金+优惠）*10，例如：存款本金10元完成（10+1.5）*10即可提款。 
温馨提示：钱宝娱乐最低100元可申请提款。</p> 
              <p>5.参加笔笔存活动需全程在PT娱乐场-"经典老虎机"和"吃角子老虎"以及MG、SA平台内投注。所有jackpot游戏，捕鱼游戏，"21点"游戏，曲棍球游戏，所有"轮盘"游戏，所有"百家乐"游戏，所有"骰宝"游戏，所有"视频扑克"游戏及"刮刮乐"游戏等，"多旋转老虎机"和"老虎机奖金翻倍"投注盈利将不计算在内。若会员在其他游戏场馆游戏，钱宝将扣除会员盈利；（该活动不能投注《黄金之旅》，若发现违规投注"钱宝娱乐"有权冻结本金和非法盈利额。）</p> 
              <p>6.此项优惠活动只针对娱乐性质的会员，如发现用户拥有超过一个账号，包括同一姓名，同一邮件地址，同一注册电话，同一/相似的IP地址，同一借记卡/信用卡，系统将自动判定账号安全等级不足，将无法申请到此项优惠。但是不影响玩家申请次存等其他优惠。</p>
			  <p>7、所有PT/MG/SA游戏禁止多窗口投注行为，否则您的违规盈利将被扣除，退还本金。</p>
			  <p>8.该活动单笔投注>=22元视为无效投注，将扣除您的违规盈利，退还本金。</p> 
              <p>9.该优惠与返水、救援金共享，若发现一笔存款参加多个存送优惠"钱宝娱乐"将取消红利。</p>  
              <p>10.本活动钱宝娱乐具有最终解释权。</p>  
              
                    <a href="javascript:;" class="btn-reback">返回</a>
                </div>
            </div>
            <div data-type="|SL" class="promotion-item">
                <div class="promotion-brief">
                    <h1 class="promotion-tit">返水无上限 最高1.2% </h1>
                    <div class="pic"> <img src="static/images/activity/xm1.jpg" width="950" height="170"> </div>
                </div>
                <div class="promotion-content m-content">
                    <h3>活动对象</h3>
                    <p>钱宝娱乐城新老会员</p>
              <table class="table">
              <tbody>
              <tr>
                <th width="57"></th>
                <th width="230">AG、AGIN、BBIN（真人视讯）</th>
                <th width="83">彩票</th>
                <th width="189">PT经典老虎机吃角子老虎机</th>
                <th width="78">PT<br>
                (其他游戏)</th>
                <th width="83">MG、SA老虎机 </th>
                <th width="34">体育</th>
              </tr>
			   <tr>
                <td>新会员</td>
                <td>0.6%</td>
                <td>0.0%</td>
                <td>0.6%</td>
                <td>0.4%</td>
                <td>0.6%</td>
                <td>0.4%</td>
              </tr>
              <tr>
                <td>星级VIP</td>
                <td>0.6%</td>
                <td>0.0%</td>
                <td>0.8%</td>
                <td>0.4%</td>
                <td>0.8%</td>
                <td>0.4%</td>
              </tr>
              <tr>
                <td>银卡VIP</td>
                <td>0.7%</td>
                <td>0.0%</td>
                <td>0.8%</td>
                <td>0.4%</td>
                <td>0.8%</td>
                <td>0.4%</td>
              </tr>
              <tr>
                <td>金卡VIP</td>
                <td>0.8%</td>
                <td>0.4%</td>
                <td>0.8%</td>
                <td>0.8%</td>
                <td>0.8%</td>
                <td>0.4%</td>
              </tr>
              <tr>
                <td>钻石VIP</td>
                <td>1.0%</td>
                <td>0.4%</td>
                <td>1%</td>
                <td>1%</td>
                <td>1%</td>
                <td>0.4%</td>
              </tr>
              <tr>
                <td>黑卡VIP</td>
                <td>1.2% </td>
                <td>0.6%</td>
                <td>1%</td>
                <td>1%</td>
                <td>1%</td>
                <td>0.4%</td>
              </tr>
              </tbody>
            </table>
            <h3>活动规则:</h3>
            <p>1、所有会员均可参加此活动，无需报名申请；</p>   
            <p>2、所有平台均按照有效投注额进行计算；新会员、"星级VIP"及"银卡VIP"返水当日最高可达28888元，金卡VIP及以上等级会员当日返水无上限;</p>
            <p>3、系统派发返水时间：每天下午16:00前系统将自动派发所有平台有效投注额的返水，体育平台需等当日赛事全部结束后结算，并在结算后自动添加到会员账户。(上述为北京时间);</p>
            <p>4、系统结算时间范围： AG、AGIN平台结算前一天中午12点到今天中午12点的有效投注额；体育结算前一天中午12点到今天中午12点的有效投注额， PT、MG、SA结算前一天0点到23点59分59秒的有效投注额。 (上述为北京时间)。</p>
            <p>5、如会员出现对押等不正当投注，将被取消该活动参加资格，并有权扣除账户款项关闭账号。</p>
			<p>6、返水金额最低1RMB起发，每日最高限额28888，一倍流水投注即可申请提款。</p>
		    <p>7、不欢迎倍投，如果您触犯了规定，将被扣除违规盈利，退还本金。</p>
			<p>8、除BBIN电子游戏”老虎机”和”真人娱乐”之外的游戏不享受此活动。所有和局、无效、对赌以及取消注单的投注将无法累计获得返水，另外以下游戏的投注也不计算返水：真人21点和德州扑克，所有刮刮卡、电动扑克、桌上游戏、大型电玩和老虎机奖金翻倍投注将不计算在内，如投注此类游戏将扣除所有红利以及赢利。</p>
            <p>8、此活动最终解释权归钱宝所有。</p>          
           <a href="javascript:;" class="btn-reback">返回</a>
                </div>
            </div>
            <div data-type="|SL" class="promotion-item">
                <div class="promotion-brief">
                    <h1 class="promotion-tit">老虎机救援金 最高赠送5888元</h1>
                    <div class="pic"> <img src="static/images/activity/jyj.jpg" width="950" height="170"> </div>
                </div>
                <div class="promotion-content m-content">
                    <p>天有不测风云，"玩"有旦夕祸福，来钱宝让你玩的有后备，玩得有动力！都说老虎吃人，要是没有一点点的底气怎么去奋斗？常在河边走，总会湿鞋，遇到这样的事情不要怕。来钱宝游戏老虎机有保障！只要是在钱宝的 经典老虎机和电动吃角子老虎机（不包含累计游戏） 游戏每日存款200负盈利200以上都可以领取我们的打虎"救援金！"最高5888哦！</p>
                 <h3>活动对象</h3>
                 <p>钱宝娱乐城新老会员</p>
                  <h3>活动时间</h3>
                 <p>本活动长期有效</p>
                 <p>活动内容：</p>
                <table class="table">
              <tbody>
              <tr>
                <th>负盈利</th>
                <th>保险比例</th>
                <th>上限金额</th>
				<th>提款要求</th>
              </tr>
              <tr>
                <td>>=200元</td>
                <td>5%</td>
                <td>5888</td>
				<td>10倍流水</td>
              </tr>
              <tr>
                <td>&gt;=200元</td>
                <td>10%</td>
                <td>5888</td>
				<td>12倍流水</td>
              </tr>
              <tr>
                <td>&gt;=200元</td>
                <td>20%(金卡VIP以上专属)</td>
                <td>5888</td>
				<td>20倍流水</td>
              </tr>
              </tbody>
            </table>
            <h3>活动规则</h3>
            <p>1.您在PT"经典老虎机"和"电动吃角子老虎机"﹑MG、SA老虎机平台中单日负盈利200及以上（扣除当天领取的优惠之后）总和-200以上，即可以在账户管理自助申请该优惠。</p>
            <p>2.计算公式：负盈利=会员当日存款-所有平台优惠（幸运抽奖、返水等）
				PS1.负盈利定义：钱宝会员在“老虎机平台”的负盈利，其它“游戏平台”不计算在内，所有平台红利指在网站享受的所有优惠 
				PS2.优惠定义：全部平台的返水;全部平台优惠;红包 
				举例说明：您当天存款200元，优惠10元，返水10元;在老虎机平台产生200元的负盈利，救援金=（200元-20元）*相应比例</p>
            <p>3.“救援金”领取方式-您当天负赢利达到200元以上且主账户与游戏账户总余额不高于5元，请进入“账户管理”→“优惠领取”→“自助救援金”，选择相应的比例，点击“马上领取”即可。（计算您当天00点到23点59分59秒的输赢值）。 温馨提示:"救援金"仅限当天领取，规定时间内未领取会自动取消不予计算。</p>
            <p>4. "救援金"优惠只能在PT\MG老虎机游戏中进行投注，禁止多窗口投注行为，所有jackpot游戏，捕鱼游戏，"21点"游戏，曲棍球游戏，所有"轮盘"游戏，所有"百家乐"游戏，所有"骰宝"游戏，所有"视频扑克"游戏及"刮刮乐"游戏等，"多旋转老虎机"和"老虎机奖金翻倍"投注盈利将不计算在内。若会员在其他游戏场馆游戏，钱宝将扣除会员盈利。</p>
			<p>(该活动不能投注《黄金之旅》)。若发现违规投注"钱宝娱乐"有权冻结本金和非法盈利额。）</p>
			<p>5、所有PT/MG/SA游戏禁止多窗口投注行为，否则您的违规盈利将被扣除，退还本金。</p>
            <p>6.该活动不与"存送活动"共享。</p>
			<p>7.该活动单笔投注>=22元视为无效投注，将扣除您的违规盈利，退还本金。</p>
            <p>8.钱宝娱乐保留对本次活动的修改、修订和最终解释权，以及在无通知情况下修改本次活动的权利。</p>
            
            <a href="javascript:;" class="btn-reback">返回</a>
                </div>
            </div>
            <div data-type="|SL" class="promotion-item">
                <div class="promotion-brief">
                    <h1 class="promotion-tit">首存优惠规则，首存100%，首存50送58，首存100送88(三选一)</h1>
                    <div class="pic"> <img src="static/images/activity/scz.jpg" width="950" height="170"> </div>
                </div>
                <div class="promotion-content m-content">
                    <h2>感谢您选择钱宝娱乐，钱宝娱乐2016助您发发发！即日起：首次注册的会员存款成功即可申请存款金额100%的发财礼金。祝您在钱宝娱乐猴年行大运。</h2>
                    <h3>活动对象：</h3>
                    <p>钱宝娱乐所有会员</p>
                    <h3>活动时间：</h3>
                    <p>本活动长期有效!</p>
                    <h3>活动时间：</h3>
                    <table class="table">
              <tbody>
              <tr>
                <th>存款要求</th>
                <th>红利</th>
                <th>最高红利</th>
                <th>流水倍数</th>
              </tr>
              <tr>
                <td>首次存款（10元以上）</td>
                <td>本金*100%</td>
                <td>1688</td>
                <td>25倍</td>
              </tr>
			   <tr>
                <td>首存50</td>
                <td>58</td>
                <td>58</td>
                <td>15倍</td>
              </tr>
			     <tr>
                <td>首存100</td>
                <td>88</td>
                <td>88</td>
                <td>15倍</td>
              </tr>
              </tbody>
            </table>
             <h3>活动规则</h3>
             <p>1.会员请先登陆PT/MG/SA平台进行激活。</p>
             <p>2."钱宝娱乐"会员首次存款并激活游戏平台即可享受"100%存送优惠"，该优惠的本金和红利必须全额转入PT/MG/SA平台游戏，并在的PT/MG/SA平台中完成投注要求。</p>
             <p>3.本优惠禁止投注任何老虎机平台的“jackpot”游戏。</p>
             <p>4.此优惠无需和在线客服或者QQ客服申请，在官网中点击"账户管理"——"钱包管理"——"我要存款"——"最新优惠"，选择"100%存送优惠"。 
温馨提示：若先将本金先转入游戏就无法申请"首存优惠"。</p>
             <p>5.提款要求：（本金+优惠）*25，例如：存款本金10元完成（10+10）*25即可提款。 
温馨提示：钱宝娱乐最低100元可申请提款。</p>
             <p>6.参加首存活动需全程在PT娱乐场-"经典老虎机"和"吃角子老虎"以及MG平台内投注。所有jackpot游戏，捕鱼游戏，"21点"游戏，曲棍球游戏，所有"轮盘"游戏，所有"百家乐"游戏，所有"骰宝"游戏，所有"视频扑克"游戏及"刮刮乐"游戏等，"多旋转老虎机"和"老虎机奖金翻倍"投注盈利将不计算在内。若会员在其他游戏场馆游戏，钱宝将扣除会员盈利；（该活动不能投注PT的《黄金之旅》《古怪猴子》《大蓝》，不能投注MG的《花旗骰(CRAPS)》，《守财奴》，《超级红利pk》，《花花公子》。若发现违规投注"钱宝娱乐"有权冻结本金和非法盈利额。）</p>
             <p>7.此项优惠活动只针对娱乐性质的会员，如发现用户拥有超过一个账号，包括同一姓名，同一邮件地址，同一注册电话，同一/相似的IP地址，同一借记卡/信用卡，，系统将自动判定账号安全等级不足，将无法申请到此项优惠。但是不影响玩家申请次存等其他优惠。</p>  
             <p>8. 该优惠不与返水共享，不与其他"存送优惠"及需要"流水倍数提款的优惠"（救援金等）共享，若发现一笔存款参加多个存送优惠"钱宝娱乐"将取消红利。</p>
			 <p>9、所有PT/MG/SA游戏禁止多窗口投注行为，否则您的违规盈利将被扣除，退还本金。</p>
			 <p>10. 所有首存活动，单笔投注>=22元视为无效投注，将扣除您的违规盈利，退还本金。</p>      
             <p>11.所有首存活动，您可以任选其一参加，无需联系在线客服，我要存款--最新优惠，凡是可选的优惠均可参加。</p>      
			 <p>12.其他任何临时开放活动，如存200送50，存500送100，游戏规则与首存100%相同。</p>
             <p>13.本活动钱宝娱乐具有最终解释权。</p>      
                    
                    <a href="javascript:;" class="btn-reback">返回</a>
                </div>
            </div>
			
		<div data-type="|SL" class="promotion-item">
          <div class="promotion-brief">
         <h1 class="promotion-tit">钱宝抢红包，天天抢不完</h1>
         <div class="pic"> <img src="static/images/activity/red_bags.jpg"  width="950" height="170"> </div>
         </div>
         <div class="promotion-content m-content">
         <h2>钱宝抢红包，天天抢不完，给你意外惊喜</h2>
         <h3>活动对象</h3>
         <p>钱宝娱乐城新老会员</p>
         <h3>活动时间</h3>
         <p>本活动长期有效</p>
         <h3>活动规则</h3>
         <p>1.5天内存款≥500元即可获得一次抢红包的机会。</p>
         <p>2.红包金额固定为28元，无限数量。</p> 
         <p>3.每人每日限领1个红包。</p> 
         <p>4.红包只限PT/MG/SA平台进行游戏。</p> 
         <p>5.中心钱包余额和第三方各平台游戏总余额不能超过2元。</p>
         <p>6.红包不与返水共享。</p>
		 <p>7.红包最高出款168元。</p>
		 <p>8.本活动解释权归钱宝娱乐所有。</p>
         <a href="javascript:;" class="btn-reback">返回</a>
        </div>
       </div>
            <div data-type="|SL|KE|SP" class="promotion-item">
                <div class="promotion-brief">
                    <h1 class="promotion-tit">注册送8-88元体MG/SA老虎机验金 100元即可提款</h1>
                    <div class="pic"> <img src="static/images/activity/tyj.jpg" width="950" height="170"> </div>
                </div>
                <div class="promotion-content m-content">
                <p>钱宝娱乐为了回馈广大玩家的大力支持，特开设新会员"体验金"活动，最高88元体验金注册后即可领取，希望各位玩家能够"多多游戏"、"多多盈利"，还请各位会员帮"钱宝"多多宣传，呼朋唤友一起来赢钱吧！</p>     
                <h3>活动时间</h3>
                <p>本活动长期有效</p>
                <h3>活动对象</h3>
                <p>钱宝娱乐新注册会员</p>
                <h3>活动内容</h3>
                <h2>首次在钱宝娱乐注册的会员，即可申请MG/SA体验金，无需联系客服，体验金无流水要求，满100元即可提款,最高提款上限为118元RMB。</h2>
                <h3>活动规则</h3>
                <p>1.此活动无需向客服申请，登录账号后选择 账户管理-优惠自助-8-88元体验金，申请体验金需先绑定"邮箱"及"手机"方可申请成功。</p>
                <p>2.体验金优惠每位会员 只限领取一次。</p>
                <p>3.MG/SA体验金需全程在MG/SA娱乐平台投注，其他任何平台投注皆不能提款。</p>
                <p>4.此项优惠活动只针对娱乐性质的会员，如发现用户拥有超过一个账户，包括同一姓名、同一邮件地址、同一/相似IP地址、同一住址、同一借记卡/信用卡、同一银行账户、同一电脑等其他任何不正常投注行为，一经发现， 钱宝娱乐将保留冻结会员的账户盈利及余额的权利。</p>
                <p>5.本活动与官网其他活动共享，不与返水共享。</p>
				<p>6.钱宝娱乐一切免费赠送的彩金，红包，优惠规则，出款规则均与此活动相同。</p>
                <p>7.本活动的最终解释权归钱宝娱乐所有。</p>
                 
                <a href="javascript:;" class="btn-reback">返回</a>
                </div>
            </div>
            <!--  百家乐优惠  -->
             <div data-type="|LI" class="promotion-item">
                <div class="promotion-brief">
                    <h1 class="promotion-tit">百家乐首存送33%，最高赠送588元</h1>
                    <div class="pic"> <img src="static/images/activity/live.jpg" width="950" height="170"> </div>
                </div>
                <div class="promotion-content m-content">
                <p>开户有礼，新加入会员首次存款后即可申请真人娱乐开户礼金，最高“588”元，如此丰厚大礼，您还犹豫什么？</p>     
                <h3>活动时间</h3>
                <p>本活动长期有效</p>
                <h3>活动对象</h3>
                <p>钱宝娱乐所有会员</p>
                <table class="table">
	              <tbody>
	              <tr>
	                <th>存款要求</th>
	                <th>红利</th>
	                <th>最高红利</th>
	                <th>流水倍数</th>
	              </tr>
	              <tr>
	                <td rowspan="3">首次存款（100元以上）</td>
	                <td>10%</td>
	                <td>588</td>
	                <td>8倍</td>
	              </tr>
	               <tr>
	                <td>20%</td>
	                <td>588</td>
	                <td>16倍</td>
	              </tr>
	               <tr>
	                <td>33%</td>
	                <td>588</td>
	                <td>25倍</td>
	              </tr>
	              </tbody>
	            </table>
                <h3>活动规则</h3>
                <p>1.会员请先登陆AG/BBIN平台进行激活。</p>
                <p>2."钱宝娱乐"会员首次存款并激活游戏平台即可享受"10%-33%不等的存送优惠"，该优惠的本金和红利必须全额转入AG/BBIN平台游戏，并且只能AG/BBIN平台中投注真人百家乐娱乐，否则您的违规盈利将被扣除。</p>
                <p>3.此优惠无需和在线客服或者QQ客服申请，在官网中点击"账户管理"——"自助优惠"——"资金管理"——"存款"——"优惠活动"，选择"10%-33%存送优惠"。  温馨提示：若先将本金先转入游戏就无法申请"首存优惠"。</p>
                <p>4.提款要求：（本金+优惠）*相应红利流水倍数要求，例如：存款本金100元申请10%优惠（100+10）*8即可提款。  温馨提示：钱宝娱乐最低100元可申请提款。</p>
                <p>5.本活动禁止任何非娱乐性质的玩家套取红利，任何无风险投注将不计算流水，如百家乐同时投注“庄”“闲”，轮盘中同时投注“红”“黑”，“大”“小”“单”“双”等。</p>
				<p>6.连续压闲超过5笔，将视为套利行为，扣除违规盈利，退还本金；连续压庄不受限制。</p>
				<p>7.此项优惠活动只针对娱乐性质的会员，如发现用户拥有超过一个账号，包括同一姓名，同一邮件地址，同一注册电话，同一/相似的IP地址，同一借记卡/信用卡，，系统将自动判定账号安全等级不足，将无法申请到此项优惠。但是不影响玩家申请次存等其他优惠。</p>
                <p>8.该优惠不与返水共享，不与其他"存送优惠"及需要"流水倍数提款的优惠"（救援金等）共享，若发现一笔存款参加多个存送优惠"钱宝娱乐"将取消红利。</p>
				<p>9.本优惠禁止倍投，违反规定，将扣除违规盈利，退还优惠重新投注。</p>
                <p>10.本活动钱宝娱乐具有最终解释权。</p>
                <a href="javascript:;" class="btn-reback">返回</a>
                </div>
            </div>
            
            <!--  百家乐优惠  -->
             <div data-type="|LI" class="promotion-item">
                <div class="promotion-brief">
                    <h1 class="promotion-tit">业内首创3%-5%百家乐救援金，助你创造奇迹</h1>
                    <div class="pic"> <img src="static/images/activity/live_help.jpg" width="950" height="170"> </div>
                </div>
                <div class="promotion-content m-content">
                <p>无论何时，只要可能。正所谓“有赌未必输”手中有筹码,可创造奇迹，钱宝娱乐助您打回本金的和盈利。即日起，在真人视讯平台AG/BBIN产生负盈利即可申请救援金，最高5888哦！</p>     
                <h3>活动时间</h3>
                <p>本活动长期有效</p>
                <h3>活动对象</h3>
                <p>钱宝娱乐所有会员</p>
                <table class="table">
	              <tbody>
	              <tr>
	                <th>负盈利</th>
	                <th>保险比例</th>
	                <th>上限金额</th>
	                <th>提款要求</th>
	              </tr>
	              <tr>
	                <td>>=500元</td>
	                <td>3%</td>
	                <td>5888</td>
	                <td>8倍</td>
	              </tr>
	               <tr>
	               	<td>>=9,999元</td>
	                <td>4%</td>
	                <td>5888</td>
	                <td>8倍</td>
	              </tr>
	               <tr>
	                <td>>=99,999元</td>
	                <td>5%</td>
	                <td>5888</td>
	                <td>8倍</td>
	              </tr>
	              </tbody>
	            </table>
                <h3>活动规则</h3>
                <p>1.您在AG/BBIN真人游戏中(仅限真人游戏能领取救援金)单日负盈利500及以上（扣除当天领取的优惠之后）总和负500以上，即可以在账户管理自助申请该优惠。</p>
                <p>2.计算公式：负盈利=会员当日存款-所有平台优惠（幸运抽奖、返水等） PS1.负盈利定义：钱宝会员在“AG/BBIN”的负盈利，其它“游戏平台”不计算在内，所有平台红利指在网站享受的所有优惠 PS2.优惠定义：全部平台的返水;全部平台优惠;红包 举例说明：您当天存款500元，优惠10元，返水10元;在AG/BBIN平台产生500元的负盈利，救援金=（500元-20元）*相应比例</p>
                <p>3.“救援金”领取方式-您当天负赢利达到500元及以上且主账户与游戏账户总余额不高于5元，登录钱宝官网，进入账户管理--优惠领取---自助救援金，自动申请。 温馨提示:"救援金"仅限当天领取，规定时间内未领取会自动取消不予计算。</p>
                <p>4.该活动不与"百家乐首存活动"共享。</p>
				<p>5.连续压闲超过5笔，将视为套利行为，扣除违规盈利，退还本金；连续压庄不受限制。</p>
				<p>6.本优惠禁止倍投，违反规定，将扣除违规盈利，退还优惠重新投注。</p>
                <p>7.钱宝娱乐保留对本次活动的修改、修订和最终解释权，以及在无通知情况下修改本次活动的权利。</p>
                <a href="javascript:;" class="btn-reback">返回</a>
                </div>
            </div>
            
             <div data-type="|LI" class="promotion-item">
                <div class="promotion-brief">
                    <h1 class="promotion-tit">返水无上限 最高1.2% </h1>
                    <div class="pic"> <img src="static/images/activity/xm2.jpg" width="950" height="170"> </div>
                </div>
                <div class="promotion-content m-content">
                    <h3>活动对象</h3>
                    <p>钱宝娱乐城新老会员</p>
		              <table class="table">
		              <tbody>
		              <tr>
		                <th width="57"></th>
		                <th width="230">AG、AGIN、BBIN（真人视讯）</th>
		                <th width="83">彩票</th>
		                <th width="189">PT经典老虎机吃角子老虎机</th>
		                <th width="78">PT<br>
		                (其他游戏)</th>
		                <th width="83">MG老虎机 </th>
		                <th width="34">体育</th>
		              </tr>
					   <tr>
		                <td>新会员</td>
		                <td>0.6%</td>
		                <td>0.0%</td>
		                <td>0.6%</td>
		                <td>0.4%</td>
		                <td>0.6%</td>
		                <td>0.4%</td>
		              </tr>
		              <tr>
		                <td>星级VIP</td>
		                <td>0.6%</td>
		                <td>0.0%</td>
		                <td>0.8%</td>
		                <td>0.4%</td>
		                <td>0.8%</td>
		                <td>0.4%</td>
		              </tr>
		              <tr>
		                <td>银卡VIP</td>
		                <td>0.7%</td>
		                <td>0.0%</td>
		                <td>0.8%</td>
		                <td>0.4%</td>
		                <td>0.8%</td>
		                <td>0.4%</td>
		              </tr>
		              <tr>
		                <td>金卡VIP</td>
		                <td>0.8%</td>
		                <td>0.4%</td>
		                <td>0.8%</td>
		                <td>0.8%</td>
		                <td>0.8%</td>
		                <td>0.4%</td>
		              </tr>
		              <tr>
		                <td>钻石VIP</td>
		                <td>1.0%</td>
		                <td>0.4%</td>
		                <td>1%</td>
		                <td>1%</td>
		                <td>1%</td>
		                <td>0.4%</td>
		              </tr>
		              <tr>
		                <td>黑卡VIP</td>
		                <td>1.2% </td>
		                <td>0.6%</td>
		                <td>1%</td>
		                <td>1%</td>
		                <td>1%</td>
		                <td>0.4%</td>
		              </tr>
		              </tbody>
		            </table>
		            <h3>活动规则:</h3>
		            <p>1、所有会员均可参加此活动，无需报名申请；</p>   
		            <p>2、所有平台均按照有效投注额进行计算；新会员、"星级VIP"及"银卡VIP"返水当日最高可达28888元，金卡VIP及以上等级会员当日返水无上限;</p>
		            <p>3、系统派发返水时间：每天下午16:00前系统将自动派发所有平台有效投注额的返水，体育平台需等当日赛事全部结束后结算，并在结算后自动添加到会员账户。(上述为北京时间);</p>
		            <p>4、系统结算时间范围： AG、AGIN平台结算前一天中午12点到今天中午12点的有效投注额；体育结算前一天中午12点到今天中午12点的有效投注额， PT、MG结算前一天0点到23点59分59秒的有效投注额。 (上述为北京时间)。</p>
		            <p>5、如会员出现对押等不正当投注，将被取消该活动参加资格，并有权扣除账户款项关闭账号。</p>
					<p>6、返水金额最低1RMB起发，每日最高限额28888，一倍流水投注即可申请提款</p>
					<p>7、不欢迎倍投，如果您触犯了规定，将被扣除违规盈利，退还本金。</p>
					<p>8、除BBIN电子游戏”老虎机”和”真人娱乐”之外的游戏不享受此活动。所有和局、无效、对赌以及取消注单的投注将无法累计获得返水，另外以下游戏的投注也不计算返水：真人21点和德州扑克，所有刮刮卡、电动扑克、桌上游戏、大型电玩和老虎机奖金翻倍投注将不计算在内，如投注此类游戏将扣除所有红利以及赢利。</p>
					<p>9.本优惠禁止倍投，违反规定，将扣除违规盈利，退还优惠重新投注。</p>
		            <p>10、此活动最终解释权归钱宝所有。</p>          
		           <a href="javascript:;" class="btn-reback">返回</a>
                </div>
            </div>
            
        </div>
    </div>
</div>
<script src="/js/jquery.js"></script>
<#--客服-->
<#include "${action_path}common/rightSlider.ftl">
<#--底部-->
<#include "${action_path}common/footer.ftl">
<script src="${action_path}js/jquery.js"></script>
<script src="${action_path}js/jquery.cookie.js"></script>
<script src="${action_path}static/js/public/utils.js"></script>
<script src="${action_path}static/js/public/common.js"></script>
<script src="${action_path}js/base.js"></script>
<script src="${action_path}js/login.js"></script>
<script>
    $(function(){
        showItem();
        toggleShow();

        function showItem(){
            var $promotions=$('#j_p_list').find('.promotion-item');
            var $menu=$('#j_nav').find('a');

            $menu.on('click',function(){
                var type=$(this).data('type');
                var parent=$(this).parent();
                parent.toggleClass('active').siblings().removeClass('active');
                if(type==='ALL'){
                    $promotions.show();
                    return false;
                }
                $.each($promotions,function(index,ele){
                    var $this=$(ele);
                    $this.data('type').indexOf('|'+type)!==-1?$this.show():$this.hide();

                });

                return false;
            });
            console.log($menu.first());
            $('#j_nav').find('a').first().trigger('click');
        };


        function toggleShow(){
            $('.promotion-brief').on('click',function(){
                $(this).siblings('.promotion-content').slideToggle();
            });
            $(document).on('click','.promotion-content .btn-reback',function(){
                var top= $(this).parents('.promotion-item').offset().top;
                $(this).parents('.promotion-content').slideUp();
                $("html, body").animate({
                    scrollTop: top
                }, 350);
            });
        }

    });
</script>
</body>
</html>