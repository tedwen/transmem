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
<title><%=rb.getString("mmenu.admin")%></title>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<link rel="stylesheet" type="text/css" href="/transmem/css/mmenu.css">
<SCRIPT language="JavaScript" src="/transmem/js/mmenu.js"></SCRIPT>
</head>

<body class="admin">
<%@include file="mmenu.inc"%>
<!-- admin page -->
<p>
管理员专用页面
</p>

</body>
</html>
