<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- #content -->
<div class="search_box">
	<table>
		<tbody>
			<tr>
				<td><span class="label">서버구분</span></td>
				<td>
				<select style="width:140px;" id="sel_server_type">
				</select>							
				</td>												
				<td><span class="label">서버환경</span></td>
				<td>
				<select style="width:140px;" id="sel_infra_type">
				</select>							
				</td>
				<td><span class="label">호스트</span></td>
				<td>
				<select style="width:140px;" id="sel_host">
				</select>
				</td>
				<td><button class="btn1" id="btn_refresh">조회</button></td>				
			</tr>			
		</tbody>
	</table>
</div>	
<br>
  
<div id="content">
  <div id="data_insert" style="display: none;">
    <div class="btns_bar">
    	<div class="left">
    		<button class="btn1" onclick="saveSeverInfo();"><span>저장</span></button>
    		<button class="btn2" onclick="closeInsertDiv();"><span>취소</span></button>
    	</div>
    </div>
    <div class="write_box">
    	<table>
    		<colgroup>
				<col style="width:140px;">
				<col style="width:140px;">
				<col style="width:120px;">
				<col style="width:130px;">
				<col style="width:130px;">
				<col style="width:130px;">
				<col style="width:130px;">
				<col style="width:90px;">
				<col style="width:130px;">
				<col style="width:130px;">
				<col style="width:200px;">
    		</colgroup>
    		<thead>
    			<tr>
					<th>*SERVER_TYPE</th>
					<th>*INFRA_TYPE</th>
					<th>*HOST_NAME</th>
					<th>SERVER_NAME</th>
					<th>POSCO_NAT_IP</th>
					<th>*REAL_IP</th> 
					<th>SERVICE_IP</th>
					<th>PORT</th>
					<th>USER_ID</th>
					<th>USER_PW</th>
					<th>DESCRIPTION</th>
    			</tr>
    		</thead>
    		<tbody>
    			<tr>
					<td class="tac"><input type="hidden" id="id" name="id">
					<select style="width:140px;" id="server_type" name='server_type'></select></td>
    				<td class="tac"><select style="width:140px;" id="infra_type" name='infra_type'></select></td>
    				<td class="tac"><input type="text" id="host_name" name ="host_name" style="width: 120px;"></td>
    				<td class="tac"><input type="text" id="server_name" name ="server_name" style="width: 130px;"></td>
    				<td class="tac"><input type="text" id="posco_nat_ip" name ="posco_nat_ip" style="width: 130px;"></td>
    				<td class="tar"><input type="text" id="real_ip" name ="real_ip" style="width: 130px;"></td>
    				<td class="tac"><input type="text" id="service_ip" name ="service_ip" style="width: 130px;"></td>
    				<td class="tac"><input type="text" id="port" name ="port" style="width: 90px;"></td>
    				<td class="tac"><input type="text" id="user_id" name ="user_id" style="width: 130px;"></td>
    				<td class="tac"><input type="text" id="user_pw" name ="user_pw" style="width: 130px;"></td>
    				<td class="tac"><input type="text" id="description" name ="description" style="width: 200px;"></td>
    			</tr>
    		</tbody>
    	</table>
  	</div>
  </div>	
  
  <div class="btns_bar">
  	<div class="left">
  		<button class="btn1" onclick="closeInsertDiv();openInsertDiv();"><span>등록</span></button>
  		<button class="btn2" id="btn_delete" disabled><span>삭제</span></button>
  		<span class="sep"></span>
  		<button class="btn2" id="btn_refresh"><span>새로고침</span></button>
  	</div>
  	<div class="right">
  	</div>
  </div>

	<table class="grid_table">
		<colgroup>
			<col style="width:30px;">
			<col style="width:100px;">
			<col style="width:100px;">
			<col style="width:130px;">
			<col style="width:130px;">
			<col style="width:100px;">
			<col style="width:110px;">
			<col style="width:100px;">
			<col style="width:80px;">
			<col style="width:100px;">
			<col style="width:100px;">
			<col>
		</colgroup>
		<thead>
			<tr>				
				<th><input type="checkbox"  id="check-all-menu"></th>
				<th>SERVER_TYPE</th>
				<th>INFRA_TYPE</th>
				<th>HOST_NAME</th>
				<th>SERVER_NAME</th>
				<th>POSCO_NAT_IP</th>
				<th>REAL_IP</th> 
				<th>SERVICE_IP</th>
				<th>PORT</th>
				<th>USER_ID</th>
				<th>USER_PW</th>
				<th>DESCRIPTION</th>
			</tr>
		</thead>
		<tbody>		
		</tbody>
	</table>
</div>

<script src="<%=request.getContextPath()%>/js/server_management/resource_list.js"></script>

<style type="text/css">
    .dataTables_processing {
        top: 110px !important;
        z-index: 11000 !important;
    }
    
    td>table{
    	width:100% !important; 
    }
</style>