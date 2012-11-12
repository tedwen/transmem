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
<title><%=rb.getString("smenu_personal_edit")%></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="/transmem/css/mmenu.css">
<link rel="stylesheet" type="text/css" href="/transmem/css/personal.css">
<SCRIPT language="JavaScript" src="/transmem/js/mmenu.js"></SCRIPT>
<SCRIPT language="JavaScript" src="/transmem/js/personal.js"></SCRIPT>
</head>

<body class="personal" id="p2">
<%@include file="mmenu.inc"%>
<%@include file="personal.inc"%>
<!-- personal 2 -->
<br>
<form id="update_profile_form" method="post" action="/transmem/tm">
<input type="hidden" name="action" value="UpdateUserAction"/>
<div id="personal_div">
<table class="personal_outer" cellspacing="0">
<tr><td>
<table class="personal_inner" cellspacing="1">
<tr><td width="100" align=right class="ptd1"><%=rb.getString("personal2.realname")%>:</td>
<td class="ptd2"><input type=text name=realname value="<%=user.getRealname()%>"></td></tr>
<tr><td align=right class="ptd1"><%=rb.getString("personal2.sex")%>:</td>
<td class="ptd2"><input type=radio name=sex value="M" checked><%=rb.getString("sex.male")%>
<input type=radio name=sex value="F"><%=rb.getString("sex.female")%></td></tr>
<tr><td align=right class="ptd1"><%=rb.getString("personal2.password")%>:</td>
<td class="ptd2"><input type=password name=passwd></td></tr>
<tr><td align=right class="ptd1"><%=rb.getString("personal2.newpass")%>:</td>
<td class="ptd2"><input type=password name=newpasswd></td></tr>
<tr><td align=right class="ptd1"><%=rb.getString("personal2.newpass2")%>:</td>
<td class="ptd2"><input type=password name=newpasswd2></td></tr>
<tr><td align=right class="ptd1"><%=rb.getString("personal2.email")%>:</td>
<td class="ptd2"><input type=text name=email value="<%=user.getEmail()%>"></td></tr>
<tr><td align=right class="ptd1"><%=rb.getString("personal2.question")%>:</td>
<td class="ptd2"><input type=text name=question value="<%=user.getQuestion()%>"></td></tr>
<tr><td align=right class="ptd1"><%=rb.getString("personal2.answer")%>:</td>
<td class="ptd2"><input type=text name=answer value="<%=user.getAnswer()%>"></td></tr>
<tr><td align=center colspan=2>
<input type=submit value=<%=rb.getString("personal2.submit")%> onclick="submitProfile()">&nbsp;
<input type=reset value=<%=rb.getString("personal2.reset")%>></td></tr>
</table>
</td></tr>
</table>
</div>
</form>

</body>
</html>
