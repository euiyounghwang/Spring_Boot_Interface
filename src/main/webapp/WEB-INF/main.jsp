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

<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<%
	if (is_server_monitoring.equals("Y")) {

%>
<title><%=company_name%> 검색 모니터링</title>
<%
}
else { 
%>
<title><%=request.getContextPath()%> 검색 모니터링</title>
<%
	}
%>

<script src="<%=request.getContextPath()%>/js/common/webfontloader.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/datatables.min.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery-ui.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/buttons.dataTables.min.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">

<script src="<%=request.getContextPath()%>/js/common/jquery-1.12.4.min.js"></script>
<script src="<%=request.getContextPath()%>/js/common/jquery-ui.js"></script>
<script src="<%=request.getContextPath()%>/js/common/jquery.serializeObject.min.js"></script>


<script src="<%=request.getContextPath()%>/js/common/chart.js"></script>
<script src="<%=request.getContextPath()%>/js/common/chart_common.js"></script>
<script src="<%=request.getContextPath()%>/js/common/utils.js"></script>

<script src="<%=request.getContextPath()%>/js/common/datatables.min.js"></script>
<script src="<%=request.getContextPath()%>/js/common/dataTables.buttons.min.js"></script>
<script src="<%=request.getContextPath()%>/js/common/full_numbers_no_ellipses.js"></script>

<script src="<%=request.getContextPath()%>/js/common/jszip.min.js"></script>
<script src="<%=request.getContextPath()%>/js/common/pdfmake.min.js"></script>
<script src="<%=request.getContextPath()%>/js/common/vfs_fonts.js"></script>

<script src="<%=request.getContextPath()%>/js/common/buttons.html5.min.js"></script>

<script src="<%=request.getContextPath()%>/js/common/jquery.form.js"></script> 

<script src="<%=request.getContextPath()%>/js/common.js"></script>

</head>
<body>
<!-- #wrapper -->
<div id="wrapper">
	<!-- #header -->
	<div id="header">
		<div id="header_top">
			<%
					if (is_server_monitoring.equals("Y")) {
			%>
			<span class="logo"><a href="#" onclick="$('#a_firstmenu').trigger('click');">모니터링 시스템</a></span>
			<%
			} else {
			%>
			<span class="logo"><a href="#" onclick="$('#a_firstmenu').trigger('click');">모니터링 시스템</a></span>
			<!-- <span class="logo"><a href="#" onclick="$('#a_firstmenu').trigger('click');">모니터링 시스템 <span id="taskName"></span></a></span> -->
			<%
			}
			%>
			<ul class="info">
				<li><span id="line-notice"/></li>
				<!-- li><span class="ic_user">홍길동</span></li-->
				<!-- li><a href="#n" class="ic_help">도움말</a></li-->
				<li id="li_start"><a href="javascript:;" onclick="search_engine_start();" id="butt_start"><span class="ui-icon ui-icon-play">Start</span>Start</a></li>
				<li id="li_stop"><a href="javascript:;" onclick="search_engine_stop();" id="butt_stop"><span class="ui-icon ui-icon-pause">Stop</span>Stop</a></li>
			</ul>
		</div>
		<div id="gnb">
			<h1 id="gnb_title"></h1>
			<%
					if (is_server_monitoring.equals("Y")) {
			%>
				<ul>
				<li><a href="#n" onclick="loadCenterPage('search_monitoring/search_engine_status_list?version=7.9.0', 'Y','검색엔진 검색엔진상태관리');" id="a_firstmenu"><span>서버모니터링</span></a>
					<ul>
				    	<li><a href="#n" onclick="loadCenterPage('search_monitoring/search_engine_status_list?version=7.9.0', 'Y','검색엔진상태관리');"><span>검색엔진상태관리(가동계)</span></a></li>
				  </ul>
				</li>
				</ul>
			<% 
			} else {
			%>
			<ul>
				<li><a href="#n" onclick="loadCenterPage('search_monitoring/search_engine_status_list?version=7.9.0', 'Y','검색엔진 Dashboard');" id="a_firstmenu"><span>서버모니터링</span></a>
					<ul>
				    	<!--  <li><a href="#n" onclick="loadCenterPage('search_monitoring/search_engine_indices_list?version=6.5.4', 'Y','검색엔진 Dashboard');"><span>검색엔진 Dashboard</span></a></li>-->
				    	<li><a href="#n" onclick="loadCenterPage('search_monitoring/search_engine_status_list?version=7.9.0', 'Y','검색엔진상태관리');"><span>검색엔진상태관리(가동계)</span></a></li>
				   </ul>
				</li>
				<li><a href="#n" onclick="loadCenterPage('server_management/resource_list', 'Y','서버대상항목관리');"><span>서버관리</span></a>
					<ul>
				    	<li><a href="#n" onclick="loadCenterPage('server_management/resource_list', 'Y','서버대상항목관리');"><span>서버대상항목관리</span></a></li>
				    	<!-- <li><a href="#n" onclick="loadCenterPage('server_management/configure_list', 'Y','미리보기');"><span>미리보기</span></a></li> -->
				    	<!-- <li><a href="#n" onclick="loadCenterPage('server_management/cronjob_list', 'Y','서버별 CronJob 관리');"><span>서버별 CronJob 관리</span></a></li> -->
					</ul>
				</li>
				<li><a href="#n" onclick="loadCenterPage('interface_management/interface_verify', 'Y','인터페이스검증');"><span>인터페이스관리</span></a>
					<ul>
					<li><a href="#n" onclick="loadCenterPage('interface_management/interface_verify', 'Y','인터페이스 검증');"><span>인터페이스 검증</span></a></li>
				    	<li><a href="#n" onclick="loadCenterPage('interface_management/sql_excute', 'Y','SQL 실행');"><span>SQL 실행</span></a></li>
				    	<!-- <li><a href="#n" onclick="loadCenterPage('interface_management/sql_list', 'Y','SQL 관리');"><span>SQL 관리</span></a></li> -->
				     	<li><a href="#n" onclick="loadCenterPage('interface_management/developer_link', 'Y','운영사이트 관리자접속');"><span>운영사이트 관리자접속</span></a></li>
					</ul>
				</li>
				<li><a href="#n" onclick="loadCenterPage('log_management/was_log_list', 'Y','WAS로그');"><span>실시간로그</span></a>
					<ul>
							<li><a href="#n" onclick="loadCenterPage('log_management/was_log_list', 'Y','WAS서버로그');"><span>WAS서버로그</span></a></li>
				    	<!--<li><a href="#n" onclick="loadCenterPage('log_management/ai_server_log_list', 'Y','AI서버로그');"><span>AI서버로그</span></a></li>
				    	<li><a href="#n" onclick="loadCenterPage('log_management/ai_extract_result_list', 'Y','AI검출결과로그');"><span>AI검출결과로그</span></a></li>
				    	<li><a href="#n" onclick="loadCenterPage('log_management/traindata_log_list', 'Y','학습데이터');"><span>AI학습데이터</span></a></li>
				    	<li><a href="#n" onclick="loadCenterPage('log_management/queue_log_list', 'Y');"><span>시스템변경정보로그</span></a></li>
				    	<li><a href="#n" onclick="loadCenterPage('log_management/feed_log_list', 'Y');"><span>서버별색인로그</span></a></li>
				    	<li><a href="#n" onclick="loadCenterPage('log_management/ecm_auth_log_list', 'Y');"><span>문서권한조회로그</span></a></li>
				    	<li><a href="#n" onclick="alert('준비중입니다.');"><span>검색엔진서버로그</span></a></li> -->
					</ul>
				</li>
				<li><a href="#n" onclick="loadCenterPage('interface_management/developer_link', 'Y','운영사이트 관리자접속' );"><span>운영사이트 관리</span></a>
					<ul>
							<li><a href="#n" onclick="loadCenterPage('interface_management/developer_link', 'Y','운영사이트 관리자접속');"><span>운영사이트 관리자접속</span></a></li>
					</ul>
				</li>
			</ul>
			<%
			}
			%>
		</div>
		<div id="util">
		</div>
		<div id="depth2_bar"></div>
	</div>
	<!-- //#header -->
	<!-- #container -->
	<div id="container">
		
		<!-- #right -->
		<div id="right" style="display:none;">
		</div>
		<!-- //#right -->		
		<!-- #left -->
		<div id="left" style="display:none;">
		</div>
		<!-- //#left -->
		
		<!-- #center -->
		<div id="center">
		</div>
		<!-- //#center -->
		
		
		<!-- #full -->
		<div id="full" style="display:none;">
		</div>
		<!-- //#full -->
	</div>
	<!-- //#container -->
	
	<div id="blank"></div>
</div>
<div id="ajax_load_indicator" style="display:none;">
	<img id='loadingImg' src='<%=request.getContextPath()%>/img/loding.gif' style="display:none;" />
</div>
<script type="text/javascript">
$( document ).ready(function() {
	//초화면 셋팅
	$('#a_firstmenu').trigger('click');
	
	getApplicationTask();
});
</script>

<!-- //#wrapper -->
</body>

</html>
