<#include "common/config.ftl">
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${title} - 找回密码</title>
${meta}
<link href="${zy_path}css/main.css" rel="stylesheet" type="text/css" />
<link href="${zy_path}css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" language="javascript" src="${zy_path}js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" language="javascript" src="${zy_path}js/menu.js"></script>
<script type="text/javascript" language="javascript" src="${zy_path}js/main.js"></script>
</head>
<body>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td align="center" valign="top" class="top">
	
<!--头部-->	
	<#include "common/top.ftl">
	
	</td>
  </tr>
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
       		<#include "common/bulletin.ftl" />
          </tr>
        </table>
		
		</td>
      </tr>
    </table>
<!-- banner图片及公告结束 -->
	
	</td>
  </tr>
  <tr>
    <td align="center" valign="top" style="padding-top:15px; padding-bottom:15px;">
	<!--忘记密码开始-->
<table width="1000" border="0" align="center" cellpadding="0" cellspacing="0">
				<tbody><tr>
					<td align="center" valign="top" style="background-color:#CCCCCC;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tbody><tr>
								<td width="20%" class="regtitle">忘记密码</td>
								<td width="80%" style="border-top:solid 3px #ada9aa;"><p>第二步：输入您的正确资料</p></td>
							</tr>
						</tbody></table>
					</td>
				</tr>
				<tr>
					<td align="center" valign="top">
						<table width="1000" border="0" align="center" cellpadding="0" cellspacing="0">
							<tbody><tr>
								<td align="center" valign="top" style="padding:10px; background:url(${zy_path}images/line.jpg) repeat-x top; background-color:#CCCCCC;">
			
									<table width="100%" border="0" align="center" cellpadding="0" cellspacing="5">
										<tbody><tr>
											<td width="20%" align="right" valign="middle" class="regname"><span class="regnameclr">*</span> 邮箱地址：</td>
											<td width="80%" align="left" valign="middle">
												<input name="txtEmail" type="text" id="cphContent_cphContent_txtEmail" style="padding: 5px 10px;">
												<span id="cphContent_cphContent_rfvEmail" class="alert alert-error" style="display:none;"></span>
												<span id="cphContent_cphContent_revEmail" class="alert alert-error" style="display:none;"></span>
											</td>
										</tr>
										<tr>
											<td width="20%" align="right" valign="middle" class="regname"><span class="regnameclr">*</span>&nbsp;安全问题：</td>
											<td width="80%" align="left" valign="middle">
												<span id="cphContent_cphContent_lblQuestion">您使用的计算机是什么品牌？</span>
											</td>
										</tr>
										<tr>
											<td width="20%" align="right" valign="middle" class="regname"><span class="regnameclr">*</span> 安全答案：</td>
											<td width="80%" align="left" valign="middle">
												<input name="txtAnswer" type="text" id="cphContent_cphContent_txtAnswer" style="padding: 5px 10px;">
												<span id="cphContent_cphContent_rfvAnswer" class="alert alert-error" style="display:none;"></span>
											</td>
										</tr>
									
										<tr>
											<td align="right" valign="middle" class="regname">&nbsp;</td>
											<td align="left" valign="middle">
												<table width="180" border="0" cellpadding="0" cellspacing="0">
													<tbody><tr>
														<td align="center" valign="middle" class="regbtn">
															<a id="cphContent_cphContent_LbtnSubmit" href='#'>送出</a>
														</td>
													</tr>
												</tbody></table>
											</td>
										</tr>
									</tbody></table>
								</td>
							</tr>
						</tbody></table>
					</td>
				</tr>
			</tbody></table>
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