<div class="nav-helper size-tiny">
    <div class="container">
        <div class="fl">
            <i class="iconfont icon-volume"></i>
            <marquee id="j-top-scroll" data-marquee="" class="top-mqrquee-list" behavior="scroll" direction="left" loop="infinite" scrollamount="3" width="640" height="30">
                <#if bulletionList??>
					<#list bulletionList as bull>
						<a class="marquee-item" target="_blank" href="/lxwm/about.html">${bull_index+1}、${bull.content}</a>
					</#list>
				</#if>
            </marquee>
            <a target="_blank" class="c-blue btn-more" href="/znx/about.html">【更多&gt;&gt;】</a>
            <span class="time" id="timerCount">GMT+8 2016-04-19 18:13:35</span>
        </div>
        <ul class="helper">
            <li>
                <a href="/lxwm/about.html#tab-ad">帮助中心</a>
            </li>
            <li>
                <a href="javascript:;" onClick="joinFavorite(window.location.href,document.title);">加入收藏</a>
            </li>
            <li>
                <a href="/lxwm/about.html#tab-callwe">联系我们</a>
            </li>
        </ul>
    </div>
</div>
