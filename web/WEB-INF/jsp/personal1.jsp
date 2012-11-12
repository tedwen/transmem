<%@ page language="java" errorPage="error.jsp"
	import="com.transmem.data.db.Users,com.transmem.data.db.Projects,
		com.transmem.data.db.Articles,
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
<title><%=rb.getString("smenu_personal_info")%></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="/transmem/css/mmenu.css">
<link rel="stylesheet" type="text/css" href="/transmem/css/personal.css">
<SCRIPT language="JavaScript" src="/transmem/js/mmenu.js"></SCRIPT>
<SCRIPT language="JavaScript" src="/transmem/js/personal.js"></SCRIPT>
</head>

<body class="personal" id="p1">
<%@include file="mmenu.inc"%>
<%@include file="personal.inc"%>
<!-- first div -->
<br><div id="personal_div">
<table class="personal_outer" cellspacing="0">
<tr><td>
<table class="personal_inner" cellspacing="1">
<tr><td width="100" align=right class="ptd1"><%=rb.getString("personal1.loginname")%>:</td>
<td class="ptd2"><%=user.getUsername()%></td></tr>
<tr><td align=right class="ptd1"><%=rb.getString("personal1.regdate")%>:</td>
<td class="ptd2"><%=user.getRegdate()%></td></tr>
<tr><td align=right class="ptd1"><%=rb.getString("personal1.memberlevel")%>:</td>
<td class="ptd2"><%=rb.getString("member."+user.getMembership())%></td></tr>
<tr><td align=right class="ptd1"><%=rb.getString("personal1.memberole")%>:</td>
<td class="ptd2"><%=rb.getString("role."+user.getRole())%></td></tr>
<tr><td align=right class="ptd1"><%=rb.getString("personal1.credits")%>:</td>
<td class="ptd2"><%=user.getCredits()%></td></tr>
<tr><td align=right class="ptd1"><%=rb.getString("personal1.points")%>:</td>
<td class="ptd2"><%=user.getPoints()%></td></tr>
<tr><td align=right class="ptd1"><%=rb.getString("personal1.lastvisit")%>:</td>
<td class="ptd2"><%=user.getLastVisit()%></td></tr>
<tr><td align=right class="ptd1"><%=rb.getString("personal1.visitcount")%>:</td>
<td class="ptd2"><%=user.getVisitCount()%></td></tr>
</table>
</td></tr>
</table>
</div>
<br>

</body>
</html>
