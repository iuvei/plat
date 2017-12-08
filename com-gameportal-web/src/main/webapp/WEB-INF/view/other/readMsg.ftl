<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style type="text/css">
#boxletter {color: #8f6a3e; background: #fff; padding: 2px 30px; color: #c3b9b6; overflow: hidden; }
#boxletter .title-s { padding-bottom:5px; font-size:14px; color:#3064aa; }
#boxletter .row { line-height:37px; overflow:hidden; margin-bottom: 10px; }
#boxletter .row .time { float:left;}
#boxletter .title-s { font: 400 18px/37px "Microsoft Yahei"; color: #333; }
#boxletter .content { line-height: 28px; overflow: auto; color: #333; }
#boxletter .row1 {text-align: center; }
</style>
</head>
<body>
<div id="boxletter" class="bounceIn">
  <div class="title-s">${curBulletin.title}</div>
  <div class="row row1" style="border-bottom:1px solid #d3d3d3; ">
    <div class="time">时间：${curBulletin.editTime?string('yyyy-MM-dd HH:mm:ss')}</div>
  </div>
  <div class="row">
    <div class="content">${curBulletin.content}</div>
  </div>
</div>
</body>
</html>