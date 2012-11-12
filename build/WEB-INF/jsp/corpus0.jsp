<%@ page language="java" errorPage="error.jsp"
	import="com.transmem.data.db.Users,com.transmem.action.Session,
		com.transmem.data.db.CorpusTally,java.util.ArrayList"
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
<title><%=rb.getString("mmenu.corpus")%></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="/transmem/css/mmenu.css">
<link rel="stylesheet" type="text/css" href="/transmem/css/corpus.css">
<SCRIPT language="JavaScript" src="/transmem/js/mmenu.js"></SCRIPT>
<SCRIPT language="JavaScript" src="/transmem/js/corpus.js"></SCRIPT>
</head>

<body class="corpus" id="p1">
<%@include file="mmenu.inc"%>
<!-- corpus page -->
<br>
<div id="tabs_s">
<ul id="tabs">
<li id="tab_1"><a href="" onclick="return showcorpustally()"><%=rb.getString("smenu_corpus_tally")%></a></li>
<li id="tab_2"><a href="" onclick="return showmypool()"><%=rb.getString("smenu_corpus_mypool")%></a></li>
</ul>
</div>
<br>
<p class="corpus_p"><div id="corpus_div">
&nbsp;<%=rb.getString("corpus.lang")%>:&nbsp;
<select id="langpair" onchange="showcorpustally()">
<option value="ENZH"><%=rb.getString("ENZH")%></option>
</select>
<table class="tally_tab">
<tr class="tally_tab_tr">
<th class="tally_tab_th1"><%=rb.getString("corpus.domain")%></th>
<th class="tally_tab_th2"><%=rb.getString("corpus.public")%></th>
<th class="tally_tab_th1"><%=rb.getString("corpus.group")%></th>
<th class="tally_tab_th2"><%=rb.getString("corpus.private")%></th>
<th class="tally_tab_th1"><%=rb.getString("corpus.total")%></th></tr>
<%
	ArrayList<CorpusTally> cts = mysession.getCorpusTally();
	for (CorpusTally ct : cts) {%>
<tr>
<td class="tally_tab_td1"><%=rb.getString("domain."+ct.getDomain())%></td>
<td class="tally_tab_td2"><%=ct.getPublic()%></td>
<td class="tally_tab_td1"><%=ct.getGroup()%></td>
<td class="tally_tab_td2"><%=ct.getPrivate()%></td>
<td class="tally_tab_td1"><%=ct.getTotal()%></td>
</tr>
<%	}%>
</div></p>

</body>
</html>
