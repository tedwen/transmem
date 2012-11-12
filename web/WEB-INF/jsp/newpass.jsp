<%@ page language="java" errorPage="error.jsp"
	import="com.transmem.action.Session"
%>
<%
	Session mysession = new Session(session);
	String newpass = mysession.getTempPassword();
	java.util.ResourceBundle rb = mysession.getResourceBundle();
%>
<html>
<head>
<title><%=rb.getString("register.title")%></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>

<body>
<p>
<h3>
<%=rb.getString("reg.pass")%>:<%=newpass%>
</h3>
</p>

</body>
</html>
