<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="content">	
	<div class="btns_bar">
	  	<div class="left">
	  		<span class="last_info">DB자원 선택</span>
	  		<select id="sel_host" style="width: 400px;"></select>
	  	</div>
	  	
	  	<div class="right">
			<button class="btn1" onclick="excuteSql();"><span>▤ SQL 실행</span></button>
	  	</div>
	</div>
		<div class="dashboard">
			<div class="panel4">
				<div class="section">
					<h2>SQL 소스</h2>
					<textarea class="ui-widget" id="SQL_CONT" name ="SQL_CONT" style="width:100%; height:335px;" readonly></textarea>
				</div>
			</div>
			<div class="panel2">
				<div class="section">
					<h2>파라미터</h2>
					<div class="btns_bar" style="display:none">
					  	<div class="right">  		
					  		<button class="btn1" onClick="addRow('', '','')"><span>행추가</span></button>
					  		<button class="btn2" id="btn_delete" disabled><span>행삭제</span></button>
					  	</div>
					  </div>
					<div class="section nomal">
						<table class="nomal_table" id="param_table">
						<colgroup>
							<col style="width:40px;">
							<col style="width:40%;">
							<col>
							<col style="width:40px;">
						</colgroup>
						<thead>
							<tr>
								<th><input type="checkbox" id="check-all-menu"></th>
								<th>변수명</th>
								<th>변수값</th>
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
		<div class="dashboard">
			<div class="panel2">
				<div class="section">
					<h2>SQL 정보</h2>
					<textarea class="ui-widget" id="INFO_TITLE" name ="INFO_TITLE" style="width:100%; height:335px;" readonly></textarea>
				</div>
			</div>
			<div class="panel3">
				<div class="section" id="result_div">
					<h2>실행결과</h2>
					<textarea class="ui-widget" id="SQL_RESULT" name ="SQL_RESULT" style="width:100%; height:335px;" readonly></textarea>
<!-- 					<table class="grid_table" id="result_table" style="display:none;width:100%;"> -->
<!-- 					</table> -->
				</div>
			</div>
		</div>
			
	
</div>

<script src="<%=request.getContextPath()%>/js/interface_management/sql_excute.js"></script>