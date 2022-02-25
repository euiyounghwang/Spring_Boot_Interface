<%--
 * @FileName	: 
 * @Author      : separk
 * @Date        : 2018.06.15 
 * @Version		: 1.0
 * @Description	: devLogin 인증
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
<script src="<%=request.getContextPath()%>/js/common/jquery-1.12.4.min.js"></script>
<script src="<%=request.getContextPath()%>/js/common/U40A_devLogin_certification.js"></script>
<%-- jsbn library의 RSA 암호화 사용  --%>
<%@include file="../common/jsbn_rsa.jsp" %>

</head>
<body>

<br><br><br><br><br><br><br><br>
<input type="hidden" name="query" value="test">
<table border="1" align="center" cellpadding="5">
	<tr>
		<td align="center">
			인증번호
		</td>
		<td align="left">
			<input type="password" id="password">
		</td>
	</tr>
	<tr>
		<td colspan="2" align="center">
			<input type="button" name="doLogin" value="로그인" onclick="validateEncryptedForm(); return false;">
			<input type="hidden" name="securedPassword" id="securedPassword" value="" />
		</td>
	</tr>
</table>
<!-- </form> -->
</body>
</html>