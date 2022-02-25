<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="content">	
	<div class="btns_bar">
		<div class="left">
			<h2>SQL관리 (상세)</h2>
		</div>
	  	<div class="right">
	  		<button class="btn2" onclick="loadCenterPage('interface_management/sql_list', 'Y');"><span>목록으로</span></button>
	  		<span class="sep"></span>
	  		<button class="btn1" onclick="saveSql();"><span>저장</span></button>
	  	</div>
	</div>
	<input type="hidden" id="OBJECT_ID" value="${OBJECT_ID}"/> 
	<table class="rd_info">
		<colgroup>
			<col style="width:1px;">
			<col>
		</colgroup>
		<tbody>
			<tr>
				<th>SQL 자원</th>
				<td><input type="text" id="INFO_TITLE" name ="INFO_TITLE" style="width: 100%;"/></td>
			</tr>
			<tr>
				<th>SQL 설명</th>
				<td><textarea class="ui-widget" id="DESCRIPTION" name ="DESCRIPTION" style="width:100%; height:100px;"></textarea></td>
			</tr>
			<tr>
				<th>데이터 이상 유무 확인</th>
				<td><input type="checkbox" id="DATA_SUIT_CHK" name ="DATA_SUIT_CHK"/></td></td>
			</tr>
			<tr>
				<th>DB자원 선택</th>
				<td><select id="sel_host" style="width: 400px;"></select></td></td>
			</tr>
			<tr>
			<td colspan="2">
				<div class="dashboard">
					<div class="panel">
						<div class="section">
							<h2>SQL 소스</h2>
							<textarea class="ui-widget" id="SQL_CONT" name ="SQL_CONT" style="width:100%; height:335px;"></textarea>
						</div>
					</div>
					
					<div class="panel">
						<div class="section">
							<h2>파라미터</h2>
							<div class="btns_bar">
							  	<div class="right">
							  		<button class="btn1" onclick="addRow('', '','');"><span>행추가</span></button>
							  		<button class="btn2" id="btn_delete" disabled><span>행삭제</span></button>
							  	</div>
							</div>
							<div class="section nomal">
								<table class="nomal_table"  id="param_table">
									<colgroup>
										<col style="width:40px;">
										<col style="width:50%;">
										<col>
										<col style="width:60px;">
									</colgroup>
									<thead>
										<tr>
											<th><input type="checkbox" id="check-all-menu"></th>
											<th>변수명</th>
											<th>설명</th>
											<th>함수사용</th>
										</tr>
									</thead>
									<tbody id="my-tbody">
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</td>
			</tr>
		</tbody>
	</table>
</div>

<script src="/monitoring/js//interface_management/sql_upsert.js"></script>