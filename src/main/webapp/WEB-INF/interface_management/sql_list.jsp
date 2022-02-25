<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- #content -->

<div id="content">
  <div class="btns_bar">
  	<div class="left">
  		<button class="btn1" onclick="loadCenterPage('interface_management/sql_upsert', 'Y');"><span>등록</span></button>
  		<button class="btn2" id="btn_delete" disabled><span>삭제</span></button>
  		<span class="sep"></span>
  		<button class="btn2" id="btn_refresh"><span>새로고침</span></button>
  	</div>
  	<div class="right">
  	</div>
  </div>
	<table class="grid_table" style="width:100%">
		<colgroup>
			<col style="width:60px;">
			<col style="width:150px;">
			<col style="width:40%;">
			<col>
		</colgroup>
		<thead>
			<tr>
				<th><input type="checkbox" id="check-all-menu"></th>
				<th>DB 자원</th>
				<th>SQL 자원</th>
				<th>SQL 설명</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
</div>
<!-- //#content -->
<script src="/monitoring/js/interface_management/sql_list.js"></script>