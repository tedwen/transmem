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
<link rel="stylesheet" type="text/css" href="/transmem/css/personal.css">
<SCRIPT language="JavaScript" src="/transmem/js/mmenu.js"></SCRIPT>
<SCRIPT language="JavaScript" src="/transmem/js/group.js"></SCRIPT>
<SCRIPT language="JavaScript" src="/transmem/js/personal.js"></SCRIPT>
</head>

<body class="group" id="p2">
<%@include file="mmenu.inc"%>
<!-- group page for common users -->
<br>
<div id="tabs_s">
<ul id="tabs">
<li id="tab_1"><a href="" onclick="return showmygroup()"><%=rb.getString("smenu_group_mygroup")%></a></li>
<li id="tab_2"><a href="" onclick="return showgrouplist()"><%=rb.getString("smenu_group_list")%></a></li>
</ul>
</div>
<br>
<p class="group0_p">
<%@include file="groups.inc"%>
</p>

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
