<%@ page language="java" errorPage="error.jsp"
	import="com.transmem.data.db.Users,
		java.util.ArrayList"
%>
<%
	Users user = (Users)session.getAttribute("user");
	if (user == null) {
		response.sendError(999,"session.getAttribute('user') return null in articles.jsp");
	}
	java.util.ResourceBundle rb = (java.util.ResourceBundle)session.getAttribute("bundle");
%>
<html>
<head>
<title><%=rb.getString("smenu_personal_buy")%></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="/transmem/css/mmenu.css">
<link rel="stylesheet" type="text/css" href="/transmem/css/personal.css">
<SCRIPT language="JavaScript" src="/transmem/js/mmenu.js"></SCRIPT>
<SCRIPT language="JavaScript" src="/transmem/js/personal.js"></SCRIPT>
</head>

<body class="personal" id="p4">
<%@include file="mmenu.inc"%>
<%@include file="personal.inc"%>
<!-- personal 2 -->
<br><div id="personal_div">
 Under construction...
</div>

</body>
</html>
