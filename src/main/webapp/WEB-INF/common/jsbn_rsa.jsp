<%--
 * @FileName	: jsbn_rsa.jsp
 * @Author      : separk
 * @Date        : 2018. 06. 25. 
 * @Version		: 
 * @Description	: jsbn library의 RSA 암호화 (http://www-cs-students.stanford.edu/~tjw/jsbn/LICENSE)
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
   		 pageEncoding="UTF-8"%>
<%@ page import="java.security.KeyFactory" %>
<%@ page import="java.security.KeyPair" %>
<%@ page import="java.security.KeyPairGenerator" %>
<%@ page import="java.security.PrivateKey" %>
<%@ page import="java.security.PublicKey" %>
<%@ page import="java.security.spec.RSAPublicKeySpec" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/common/U40A_jsbn_jsbn.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/common/U40A_jsbn_rsa.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/common/U40A_jsbn_prng4.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/common/U40A_jsbn_rng.js"></script>
<%
KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
generator.initialize(2048);

KeyPair keyPair = generator.genKeyPair();
KeyFactory keyFactory = KeyFactory.getInstance("RSA");

PublicKey publicKey = keyPair.getPublic();
PrivateKey privateKey = keyPair.getPrivate();

session = request.getSession();
// 세션에 공개키의 문자열을 키로하여 개인키를 저장한다.
session.setAttribute("__rsaPrivateKey__", privateKey);

// 공개키를 문자열로 변환하여 JavaScript RSA 라이브러리 넘겨준다.
RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);

String publicKeyModulus = publicSpec.getModulus().toString(16);
String publicKeyExponent = publicSpec.getPublicExponent().toString(16);
%>

<input type="hidden" id="rsaPublicKeyModulus" value="<%=publicKeyModulus%>" />
<input type="hidden" id="rsaPublicKeyExponent" value="<%=publicKeyExponent%>" />