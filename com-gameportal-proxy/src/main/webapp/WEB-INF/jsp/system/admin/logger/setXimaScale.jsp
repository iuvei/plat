<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!-- 设置洗码比例窗口begin -->
<div class="modal fade" id="setXimaScaleModal">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">设置会员洗码比例</h4>
      </div>
      <div class="modal-body">
        <form id="setXimaScaleForm" name="setXimaScaleForm" action="" method="post">
        	<div class="input-group">	
				洗码比例：<input type="text" name="ximaScale" id="ximaScale" placeholder="请输入洗码比例" class="form-control" maxlength="10"/>
			</div>
        </form>
      </div>
      <div class="modal-footer">
      	<button type="button" class="btn btn-info btn-small" id="saveXimaScale" onclick="saveXimaScale()"> 保存 </button>
        <button type="button" class="btn btn-danger btn-small"  onclick="resetSetXimaScaleFrm();">重置</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<!-- 设置洗码比例窗口end -->