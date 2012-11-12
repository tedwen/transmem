<%@ page language="java" errorPage="error.jsp"
	import="com.transmem.data.db.Users,
		com.transmem.data.db.Databases,
		com.transmem.data.db.LangPair,
		java.util.ArrayList,java.sql.Connection"
%>
<%
	Users user = (Users)session.getAttribute("user");
	if (user == null) {
		response.sendError(999,"session.getAttribute('user') return null in articles.jsp");
	}
	Databases dbs = (Databases)application.getAttribute("databases");
	Connection con = dbs.getConnection(Databases.CATEGORY_USER);
	user.setConnection(con);
	java.util.ResourceBundle rb = (java.util.ResourceBundle)session.getAttribute("bundle");
%>
<html>
<head>
<title><%=rb.getString("smenu_personal_stat")%></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="/transmem/css/mmenu.css">
<link rel="stylesheet" type="text/css" href="/transmem/css/personal.css">
<SCRIPT language="JavaScript" src="/transmem/js/mmenu.js"></SCRIPT>
<SCRIPT language="JavaScript" src="/transmem/js/personal.js"></SCRIPT>
</head>

<body class="personal" id="p3">
<%@include file="mmenu.inc"%>
<%@include file="personal.inc"%>
<!-- personal 2 -->
<br><div id="personal_div">
<table class="personal_outer" cellspacing="0">
<tr><td>
<table class="personal_inner" cellspacing="1">
<tr><td width="100" align=right class="ptd1"><%=rb.getString("personal3.projects")%>:</td>
<td class="ptd2"><%=user.countProjects(user.getUserID())%></td></tr>
<tr><td align=right class="ptd1"><%=rb.getString("personal3.articles")%>:</td>
<td class="ptd2"><%=user.countArticles(user.getUserID())%></td></tr>
<tr><td align=right class="ptd1"><%=rb.getString("personal3.translations")%>:</td>
<td class="ptd2"><%=user.countTranslations(user.getUserID())%></td></tr>
<tr><td align=right class="ptd1"><%=rb.getString("personal3.languages")%>:</td>
<td class="ptd2"><%
  ArrayList<LangPair> lps = user.queryLangPairByTranslator(user.getUserID());
  for (LangPair lp : lps) {
  	out.write(rb.getString(lp.getLp()));
  	out.write(',');
  }
%></td></tr>
<tr><td align=right class="ptd1"><%=rb.getString("personal3.sentences")%>:</td>
<td class="ptd2"><%=user.getShared()%></td></tr>
<tr><td align=right class="ptd1"><%=rb.getString("personal3.groups")%>:</td>
<td class="ptd2"><%=user.queryGroupName(user.getGroup())%></td></tr>
</table>
</td></tr>
</table>
</div>
<%
  if (con != null) {
    con.close();
  }
%>
</body>
</html>
