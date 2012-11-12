<%@ page language="java" errorPage="error.jsp"
	import="com.transmem.data.db.Users,com.transmem.data.db.Projects,
	com.transmem.action.Session,java.util.ArrayList"
%>

<%
	Session mysession = new Session(session);
	Users user = (Users)session.getAttribute("user");
	if (user == null) {
		response.sendError(999,"$ERR_NOT_LOGIN");
	}
	ArrayList<Projects> projects = (ArrayList<Projects>)session.getAttribute("projects");
	if (projects == null) {
		response.sendRedirect("/index.jsp");
	}
	java.util.ResourceBundle rb = (java.util.ResourceBundle)session.getAttribute("bundle");
%>

<html>
<head>
<title><%=rb.getString("project.title")%></title>
<link rel="stylesheet" type="text/css" href="/transmem/css/mmenu.css">
<link rel="stylesheet" type="text/css" href="/transmem/css/tabs.css">
<SCRIPT language="JavaScript" src="/transmem/js/mmenu.js"></SCRIPT>
<SCRIPT language="JavaScript" src="/transmem/js/projects.js"></SCRIPT>
</head>

<body class="project">
<%@include file="mmenu.inc"%>
<p>
<table class="ptab" width="98%" align="center" cellpadding="0" cellspacing="2">
  <tr>
    <td valign="top">
      <!-- project list -->
      <table class="project_tab" cellspacing="1" width="100%">
        <tr class="pheader">
          <th><%=rb.getString("header.project")%></th>
		  <th><%=rb.getString("header.articles")%></th>
		  <th><%=rb.getString("header.lang")%></th>
		  <th><%=rb.getString("header.date")%></th>
		  <th><%=rb.getString("header.progress")%></th>
		  <th><%=rb.getString("header.delete")%></th>
        </tr>
       <%
        	for (int i=0; i<projects.size(); i++) {
        		Projects pr = projects.get(i);
        		%>
        <tr class="prow">
          <td class="la">
		  	<span id="<%= pr.getProjectID() %>" class="pname" onclick="openProject('<%= pr.getProjectID() %>')">
		  	<%= pr.getProjectName() %>
			</span>
		  </td>
		  <td><%= pr.getArticles() %></td>
		  <td><%= rb.getString(pr.getLangPair()) %></td>
		  <td><%= pr.getCreateDate() %></td>
		  <td><%= String.format("%.2f",pr.getProgress()) %>%</td>
		  <td><img src="images/trashbin.gif" onclick="deleteProject('<%= pr.getProjectID() %>')"/></td>
        </tr>
        		<%
        	}
        %>
      </table>
<% if (mysession.getEnoughProjects()==null) {%>
      <form id="new_project" action="/transmem/tm" method="post">
        <input type="hidden" name="action" value="AddProjectAction"/>
        <p/>
      <table class="add">
      	<tr>
      		<td width="20" align="center"><img src="images/add.gif"/></td>
      		<td><%=rb.getString("header.project")%>:</td>
			<td><input type="text" name="projectName"/></td>
			<td><%=rb.getString("header.lang")%>:</td>
			<td><select name="langPair">
			  <option value="ENZH"><%=rb.getString("ENZH")%></option>
			  <option value="ZHEN"><%=rb.getString("ZHEN")%></option>
			</select></td>
      	</tr>
<!--      	<tr>
      		<td></td>
      		<td><%=rb.getString("uploadfile")%>:</td>
			<td><input type="file" name="articleFile"/></td>
			<td><%=rb.getString("article.name")%>:</td>
			<td><input type="text" name="title"/></td>
      	</tr>-->
      	<tr>
      		<td></td>
      		<td colspan="4"><input type="button" value="Submit" onclick="return submitProject();"/></td>
      	</tr>
      </table>
      </form><%}%>
    </td>
  </tr>
</table>
</p>

<!-- footer here -->

<form id="project_form" method="post" action="/transmem/tm">
   	<input type="hidden" name="action" />
    <input type="hidden" name="project" />
</form>

</body>
</html>
