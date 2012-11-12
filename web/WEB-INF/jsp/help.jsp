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
<title><%=rb.getString("mmenu.help")%></title>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<link rel="stylesheet" type="text/css" href="/transmem/css/mmenu.css">
<SCRIPT language="JavaScript" src="/transmem/js/mmenu.js"></SCRIPT>
</head>

<body class="help">
<%@include file="mmenu.inc"%>
<!-- help page -->
<p>
暂时没有内容。 
</p>

</body>
</html>
