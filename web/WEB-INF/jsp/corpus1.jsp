<%@ page language="java" errorPage="error.jsp"
	import="com.transmem.data.db.Users,com.transmem.action.Session,
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
&nbsp;<%=rb.getString("corpus.lang")%>:&nbsp;
<select id="langpair" onchange="showmypool()">
<option value="ENZH"><%=rb.getString("ENZH")%></option>
</select>
<table class="tally_tab">
<tr class="tally_tab_tr">
<th class="tally_tab_th1"><%=rb.getString("corpus.name")%></th>
<th class="tally_tab_th2"><%=rb.getString("corpus.lang")%></th>
<th class="tally_tab_th1"><%=rb.getString("corpus.date")%></th>
<th class="tally_tab_th2"><%=rb.getString("corpus.format")%></th>
<th class="tally_tab_th1"><%=rb.getString("corpus.sentences")%></th>
<th class="tally_tab_th2"><%=rb.getString("corpus.domain")%></th>
<th class="tally_tab_th1"><%=rb.getString("corpus.users")%></th>
<th class="tally_tab_th2"><%=rb.getString("corpus.delete")%></th>
<th class="tally_tab_th1"><%=rb.getString("corpus.modify")%></th>
</tr>
<%
	ArrayList<Sources> srcs = mysession.getCorpusSourceList();
	for (Sources s : srcs) {%>
<tr>
<td class="tally_tab_td1"><%=s.getName()%></td>
<td class="tally_tab_td2"><%=rb.getString(s.getLangPair())%></td>
<td class="tally_tab_td1"><%=s.getDate()%></td>
<td class="tally_tab_td2"><%=s.getFormat()%></td>
<td class="tally_tab_td1"><%=s.getSentences()%></td>
<td class="tally_tab_td2"><%=rb.getString("domain."+s.getDomain())%></td>
<td class="tally_tab_td1"><%if (s.getPermit()==0) out.write(rb.getString("permit.public"));
else if (s.getPermit()<0) out.write(rb.getString("permit.private")); else out.write(rb.getString("permit.group"));
%>
</td>
<td class="tally_tab_td2"><input name="<%=s.getSourceID()%>" type="checkbox"/></td>
<td class="tally_tab_td1"><input type=button value=<%=rb.getString("corpus.modify")%> onclick="editSource(<%=s.getSourceID()%>)"/></td>
</tr>
<%	}%>
<tr><td colspan="9" align="center">
<input type=button value=<%=rb.getString("delete.selected")%> onclick="deleteSources()"/>
</td></tr>
</table>
</div></p>

</body>
</html>
