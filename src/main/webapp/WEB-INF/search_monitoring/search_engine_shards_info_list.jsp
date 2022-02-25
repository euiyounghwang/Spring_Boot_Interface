<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.util.*" %> 
<%@ page import = "java.util.ResourceBundle" %>

<%
	ResourceBundle resource = ResourceBundle.getBundle("application");
	String chain_code = resource.getString("server.chain_code");
%>

<!-- #content -->
<br /><br />
<div style="position: absolute; padding: 3px; border: 0px solid #484848; border-radius:3px; left: 0px; top: 230px; width:97%;">
     <p align='center'>
            <div id="LoadingImage" align="center" style="display: none">
                <!--  <img src="img/InternetSlowdown_Day.gif" />-->
                <img src="img/loading_small.gif" />
            </div>
     </p>
</div>
<div id="short_info" align="center" style="display: none">
	<input type="hidden" id="version_params" class="test_class" name="version_params" value='<%=request.getParameter("version")%>' />
  	<table id='short_info_table' border=0 width="97%">
 			<tr height="40px;" bgColor="#ffffff" valgn='center'>
 				<td align="left" width="95%">Nodes:&nbsp;<b><label id='number_of_nodes' /></b>&nbsp;&nbsp;&nbsp;
 				Indices:&nbsp;<b><label id='number_of_indices' /></b>&nbsp;&nbsp;&nbsp;
 				Memory:&nbsp;<b><label id='heap_used_in_bytes' /> / <label id='heap_max_in_bytes' /> (<label id='heap_percent' />)</b>&nbsp;&nbsp;&nbsp;
 				Total Shards:&nbsp;<b><label id='number_of_total_shards' /></b>&nbsp;&nbsp;&nbsp;
 				Unassigned Shards:&nbsp;<b><label id='unassigned_alert_message' /></b>&nbsp;&nbsp;&nbsp;
 				Documents:&nbsp;<b><label id="number_of_doucuments" /></b>&nbsp;&nbsp;&nbsp;
 				Data:&nbsp;<b><label id="number_of_disk" /></b>&nbsp;&nbsp;&nbsp;
 				Uptime:&nbsp;<b><label id='elastic_uptime' /></b>&nbsp;&nbsp;&nbsp;
 				Version:&nbsp;<b><label id="elastic_version" /></b>&nbsp;&nbsp;&nbsp;
 				Health:&nbsp; <label id='cluster_health_alert' />&nbsp;&nbsp;&nbsp;<b><label id='status' /></b>
 				</td>
 				<td>
 				<label id="loading" />
			  	</td>
 			</tr>
	</table>
</div>	
<br />
<div id="content" style="display: none">
  <table class="grid_table" id="engine_node_table"  border=0 width="97%">
		<colgroup>
			 <col style="width: 20px;">
			 <col style="width: 190px;">
			 <col style="width: 170px;">
			 <col style="width: 190px;">
			 <col style="width: 190px;">
			 <col style="width: 190px;">
			 <col style="width: 190px;">
			 <col style="width: 170px;">
		</colgroup>
		<thead>
			 <tr height="50px">
			 	<th align="center"></th>
				<th align="center">Node</th>
				<th align="center">Status</th>
				<th align="center">CPU Usage</th>
				<th align="center">Load Average</th>
				<th align="center">JVM Memory</th>
				<th align="center">Disk Free Space</th>
				<th align="center">Docs</th> 
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
	<table class="grid_table" id="engine_node_make_table" border=0 width="97%" />
</div>
<!-- //#content -->

<script src="<%=request.getContextPath()%>/js/search_monitoring/search_engine_shards_info_list.js"></script>

<style type="text/css">
    .dataTables_processing {
        top: 110px !important;
        z-index: 11000 !important;
    }
    
    td>table{
    	width:100% !important; 
    }
</style>