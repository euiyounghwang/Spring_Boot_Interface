<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page language="java" import="java.util.*" %> 
<%@ page import = "java.util.ResourceBundle" %>

<%
	ResourceBundle resource = ResourceBundle.getBundle("application");
	String company_name = resource.getString("deploy.company_name");
	String is_server_monitoring = resource.getString("server.running.flag");
%>

<!-- #content -->
<div id="content">
	
	<div class="btns_bar">
 	<h2 style="display:inline"><%=company_name%></h2> [Spring <%=org.springframework.core.SpringVersion.getVersion() %>, Java 1.8 ]
 	</div>
	
	<table class="rd_info">
		<colgroup>
			<col style="width:15%;">
			<col style="width:40%;">
			<col style="width:45%;">
		</colgroup>
		<tbody>
			<tr>
				<th>산학연 사외망[개발계 WAS]</th>
				<td><a href="http://192.168.79.69:8101/S23/S23C10/" target="_blank">충주산학연사외사용자개발AP (http://192.168.79.69:8101/S23/S23C10/)</a></td>
				<td></td>
			</tr>
			<tr>
				<th>산학연 사외망[가동계 WAS]</th>
				<td><a href="http://192.168.79.105:8101/S23/S23C10/" target="_blank">충주산학연사외사용자 가동계 #1 (http://192.168.79.105:8101/S23/S23C10/)</a></br>
				<a href="http://192.168.79.106:8101/S23/S23C10/" target="_blank">충주산학연사외사용자 가동계 #2 (http://192.168.79.106:8101/S23/S23C10/)</a></td>
				<td><a href="http://192.168.79.104:8101/S23/S23C10/" target="_blank">가동계 LB (http://192.168.79.104:8101/S23/S23C10/)</a></td>
			</tr>
		</tbody>
	</table>

</div>
<!-- //#content -->
</br>
</br>
</br>
