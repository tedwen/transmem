<%@ page language="java" errorPage="error.jsp"
	import="com.transmem.data.db.Users,com.transmem.action.Session,com.transmem.data.db.Transunit,
		com.transmem.data.db.Sources,java.util.ArrayList"
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

<body class="corpus" id="p2">
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
<table class="tally_tab">
<tr class="tally_tab_tr">
<th class="tally_tab_th1"><%=rb.getString("corpus.number")%></th>
<th class="tally_tab_th2"><%=rb.getString("corpus.source")%></th>
<th class="tally_tab_th1"><%=rb.getString("corpus.target")%></th>
<th class="tally_tab_th2"><%=rb.getString("corpus.delete")%></th>
<th class="tally_tab_th1"><%=rb.getString("corpus.modify")%></th>
</tr>
<%
	ArrayList<Transunit> srcs = mysession.getCorpusUnitList();
	int i = 0;
	for (Transunit s : srcs) {
		i ++;%>
<tr>
<td class="tally_tab_td1"><%=i%></td>
<td class="tally_tab_td2" id="s.<%=s.getSid()%>"><%=s.getSource()%></td>
<td class="tally_tab_td1" id="t.<%=s.getSid()%>"><%=s.getTarget()%></td>
<td class="tally_tab_td2"><input name="<%=s.getSid()%>" type="checkbox"/></td>
<td class="tally_tab_td1">
<input type=button value=<%=rb.getString("corpus.modify")%> onclick="editUnit(<%=s.getSid()%>)"/></td>
</tr>
<%	}%>
<tr><td colspan="9" align="center">
<input type=button value=<%=rb.getString("delete.selected")%> onclick="deleteSentences()"/>
</td></tr>
</table>
</div></p>

</body>
</html>
