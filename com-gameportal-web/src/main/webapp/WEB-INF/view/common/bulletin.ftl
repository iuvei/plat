
<!--公告-->
<div class="ui-notices pr size-small" id="ui-notices"><span class="swithFlag pa"></span>
    <div class="wrap pa">
        <ul>
        	<#if bulletionList??>
				<#list bulletionList as bull>
	                <li><a class="ui-block color-normal color-orangeHover ui-textOverflow" href="javascript:;">${bull.title}</a></li>
				</#list>
			</#if>
        </ul>
    </div>
    <a class="more color-normal pa" href="/znx/about.html">了解更多</a>
    <#if bulletionList??>
		<#list bulletionList as bull>
	        <div class="detail pa ui-textJustify" style="display: none;">${bull.content}</div>
		</#list>
	</#if>
</div>
