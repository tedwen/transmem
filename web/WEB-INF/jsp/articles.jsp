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
	Projects project = (Projects)session.getAttribute("project");
	if (project == null) {
		response.sendError(999,"session.getAttribute('project') return null in articles.jsp");
	}
	ArrayList<Projects> projects = (ArrayList<Projects>)session.getAttribute("projects");
	ArrayList<Articles> articles = (ArrayList<Articles>)session.getAttribute("articles");
	if (articles == null) {
		response.sendError(999,"session.getAttribute('articles') returned null in articles.jsp");
	}
	boolean isOwner = (project.getCreator() == user.getUserID());
	java.util.ResourceBundle rb = (java.util.ResourceBundle)session.getAttribute("bundle");
%>

<html>
<head>
<title><%=rb.getString("article.title")%></title>
<link rel="stylesheet" type="text/css" href="/transmem/css/mmenu.css">
<link rel="stylesheet" type="text/css" href="/transmem/css/tabs.css">
<SCRIPT language="JavaScript" src="/transmem/js/mmenu.js"></SCRIPT>
<SCRIPT language="JavaScript" src="/transmem/js/ajax.js"></SCRIPT>
<SCRIPT language="JavaScript" src="/transmem/js/arts.js"></SCRIPT>

</head>
<body class="project">
<%@include file="mmenu.inc"%>

<div id="tabs_s">
<form id="link_form" action="/transmem/tm" method="post">
<input type="hidden" name="action" value="LinkAction"/>
<input type="hidden" name="page" value="projects"/>
<ul id="tabs">
<li id="tab_1"><a href="" onclick="return showprojects()"><%=rb.getString("links.inc.project")%></a></li>
</ul>
</form>
</div>
<br>
<p>
<table class="ptab" width="98%" align="center" cellpadding="0" cellspacing="2">
  <tr>
    <td valign="top">
      <!-- project list -->
      <table class="project_tab" cellspacing="1" width="100%">
        <tr class="pheader">
          <th><%=rb.getString("article.name")%></th>
		  <th><%=rb.getString("article.sents")%></th>
		  <th><%=rb.getString("header.lang")%></th>
		  <th><%=rb.getString("header.date")%></th>
		  <th><%=rb.getString("translator")%></th>
		  <th><%=rb.getString("header.progress")%></th>
		  <th><%=rb.getString("header.download")%></th>
		  <th><%=rb.getString("header.share")%></th>
		  <th><%=rb.getString("header.delete")%></th>
        </tr>
        <%
        	for (int i=0; i<articles.size(); i++) {
        		Articles ar = articles.get(i);
        		int aid = ar.getArticleID();
        		%>
        <tr class="prow">
          <td class="la">
		    <span class="atitle" onclick="translateArticle('<%=aid%>')">
			  <%=ar.getTitle()%>
			</span>
		  </td>
		  <td><%=ar.getSentences()%></td>
		  <td><%=rb.getString(ar.getLangPair())%></td>
		  <td><%=ar.getUploadDate()%></td>
		  <td>
		  	<% if (ar.getTranslator()==user.getUserID()) {%>
		  		<span id="ar<%=aid%>"><%=user.getUsername()%></span>
		  	<%} else {
		  		com.transmem.data.db.Databases dbs = (com.transmem.data.db.Databases)application.getAttribute("databases");
		  		java.sql.Connection conn = null;
		  		String uname = String.valueOf(ar.getTranslator());
		  		try {
					conn = dbs.getConnection(com.transmem.data.db.Databases.CATEGORY_USER);
			  		Users u = new Users(conn);
			  		uname = u.queryNameByID(ar.getTranslator());
			  	} catch (java.sql.SQLException x) {}
			  	finally { if (conn != null) { try {conn.close();} catch (java.sql.SQLException x2) {} } }
		  		%><span id="ar<%=aid%>"><%=uname%></span><%
		  	}
		  	if (user.getGroup() > 0 && user.getRole().equals("G")) {%>
&nbsp;<img src="images/man.gif" onclick="selectTranslator(this,<%=aid%>)"/>
		  	<%}
			  %>
		  </td>
		  <td><%=String.format("%.2f",ar.getProgress())%>%</td>
		  <td><%
		  	if (ar.getProgress()>0.0) {
		  		%><img src="images/download.gif" onclick="downloadTarget('<%=aid%>');"/><%
		  	} else {
		  		out.write("&nbsp;");
		  	}
		  %></td>
		  <td><%
		  	if (ar.getProgress()>0.0) {
		  		%><img src="images/download.gif" onclick="shareTranslation(0,'<%=aid%>');"/><%
		  	}
		  %></td>
		  <td>
		  <% if (isOwner) { %>
		  <img src="images/trashbin.gif" onclick="deleteArticle('<%=aid%>')"/>
		  <% } else { out.write('-'); } %>
		  </td>
        </tr>
        		<%
        	}
        %>
      </table>
      <% if (isOwner) {%><br>
      <form id="submit_article" action="/transmem/tm" method="post" enctype="multipart/form-data">
        <input type="hidden" name="action" value="AddArticleAction"/>
      <table class="add">
      	<tr>
      		<td width="20" align="center"><img src="images/add.gif"/></td>
      		<td><%=rb.getString("article.name")%>:</td>
			<td><input type="text" name="title"/></td>
			<td><%=rb.getString("header.lang")%>:</td>
			<td><select name="langPair">
			  <option value="ENZH"><%=rb.getString("ENZH")%></option>
			  <option value="ZHEN"><%=rb.getString("ZHEN")%></option>
			</select></td>
      	</tr>
      	<tr><td></td>
      		<td><%=rb.getString("uploadfile")%>:</td>
			<td colspan="3"><input type="file" name="articleFile"/></td>
		</tr><tr><td></td>
      		<td colspan="4"><input type="button" value="Submit" onclick="addArticle()"/></td>
      	</tr>
      </table>
      </form>
      <% } %>
    </td>
  </tr>
</table>
</p>
<!-- insert footer here -->

 <form id="article_form" method="post" action="/transmem/tm">
  	<input type="hidden" name="action" />
   	<input type="hidden" name="article" />
   	<input type="hidden" name="translator" />
 </form>

<div id="selector" style="position:absolute; display:none">
  <form id="select_form">
    <input type="hidden" name="article"/>
    <input type="hidden" name="translator"/>
    <table border="1">
    	<tr><td>Please select a translator:</td></tr><tr><td>
	  	<div id="candidates"></div>
  		</td></tr><tr><td align="center">
  		<input type="button" value="Submit" onclick="replaceTranslator(1)"/>&nbsp;
  		<input type="button" value="Cancel" onclick="replaceTranslator(0)"/>
  		</td></tr>
  	</table>
  </form>
</div>

<div id="share_dv" style="border:1px solid blue; position:absolute; display:none">
  <form id="share_form">
	<input type="hidden" name="aid"/>
	<table border="1">
	<tr><td><%=rb.getString("permit.select")%>:</td></tr><tr><td>
		<input type="radio" name="permit" value="P" checked/><%=rb.getString("permit.public")%><br>
		<input type="radio" name="permit" value="G"/><%=rb.getString("permit.group")%><br>
		<input type="radio" name="permit" value="O"/><%=rb.getString("permit.owner")%>
   	</td></tr><tr><td><%=rb.getString("domain.select")%>:</td></tr><tr><td>
   		<select name="domain">
		<%
			String selectedDomain = (String)session.getAttribute("domain");
			if (selectedDomain == null) selectedDomain = "00";
			ArrayList<String> domains = (ArrayList<String>)application.getAttribute("g_domains");
			if (domains == null) {%>
				<option value="00" selected><%=rb.getString("domain.00")%></option><br>
				<option value="IT"><%=rb.getString("domain.IT")%></option>
			<%} else {
				for (String dms : domains) {%>
					<option value="<%=dms%>"<%
						if (selectedDomain.equals(dms)) out.write(" checked");
					%>><%=rb.getString(dms)%></option><br/>
				<%}
			}
		%>
		</select>
	</td></tr><tr><td align="center">
  		<input type="button" value="Submit" onclick="shareTranslation(1,null)"/>&nbsp;
  		<input type="button" value="Cancel" onclick="shareTranslation(-1,null)"/>
  	</td></tr>
  	</table>
  </form>
</div>

</body>
</html>
