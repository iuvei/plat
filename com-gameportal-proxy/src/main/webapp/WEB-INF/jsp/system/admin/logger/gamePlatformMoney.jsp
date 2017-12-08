<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!-- 游戏余额窗口begin -->
<div class="modal fade" id="queryGameMoney">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">查询游戏余额</h4>
      </div>
      <div class="modal-body">
        <table id="table_paylog" class="table table-striped table-bordered table-hover dataTable">
			<thead>
				<tr>
					<th>游戏平台</th>
					<th>游戏余额</th>
				</tr>
			</thead>
			<tbody class="_gameMoney">
			</tbody>
			</table>
      </div>
      <div class="modal-footer">
      	<button type="button" class="btn btn-primary" id="flushMoney" onclick="flushMoney()"> 刷新 </button>
        <button type="button" class="btn btn-danger" data-dismiss="modal">关闭</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<!-- 游戏余额窗口end -->