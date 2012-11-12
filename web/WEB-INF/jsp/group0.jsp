<%@ page language="java" errorPage="error.jsp"
	import="com.transmem.data.db.Users,com.transmem.data.db.Groups,com.transmem.data.db.GroupRec,
		com.transmem.action.Session,
		java.util.ArrayList"
%>
<%
	Session mysession = new Session(session);
	Users user = (Users)session.getAttribute("user");
	if (user == null) {
		response.sendError(999,"session.getAttribute('user') return null in articles.jsp");
	}
	java.util.ResourceBundle rb = (java.util.ResourceBundle)session.getAttribute("bundle");
%>
<html>
<head>
<title><%=rb.getString("mmenu.group")%></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="/transmem/css/mmenu.css">
<link rel="stylesheet" type="text/css" href="/transmem/css/group.css">
<SCRIPT language="JavaScript" src="/transmem/js/mmenu.js"></SCRIPT>
<SCRIPT language="JavaScript" src="/transmem/js/group.js"></SCRIPT>
</head>

<body class="group">
<%@include file="mmenu.inc"%>

<!-- group page for common users -->
<p class="group0_p">
<%if (user.getGroup() > 0) {%>
  <%=rb.getString("group.joinedgroup")%>:<%=mysession.getMyGroup().getGroupName()%>&nbsp;
  [<a href="" onclick="return quitgroup()"><%=rb.getString("group.quit")%></a>]
<%} else {%>
  <%=rb.getString("group.nogroup")%>
<%}%>
</p>

<%@include file="groups.inc"%>

<form id="group_form" method="post" action="/transmem/tm">
	<input type="hidden" name="action" value="GroupAction"/>
	<input type="hidden" name="operation"/>
	<input type="hidden" name="grpage"/>
	<input type="hidden" name="order"/>
	<input type="hidden" name="offset"/>
	<input type="hidden" name="group"/>
</form>

</body>
</html>
