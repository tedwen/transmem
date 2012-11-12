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
<SCRIPT language="JavaScript" src="/transmem/js/arts.js"></SCRIPT>
<SCRIPT language="JavaScript" src="/transmem/js/group.js"></SCRIPT>
</head>

<body class="group" id="p1">
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
<p class="group0_p"><div id="grouplist_div">
<form id="group_edit_form" method="post" action="/transmem/tm">
<input type="hidden" name="action" value="GroupAction"/>
<input type="hidden" name="operation"/>
<table class="mygroup_tab">
<%if (user.getGroup() > 0) {
	Groups mygroup = mysession.getMyGroup();
%>
<tr>
<td class="mygroup_tab_td1"><%=rb.getString("group.name")%>:</td>
<td class="mygroup_tab_td2" align=left><input type="text" name="GroupName" value="<%=mygroup.getGroupName()%>"/></td>
</tr>
<tr>
<td class="mygroup_tab_td1"><%=rb.getString("group.publicity")%>:</td>
<td class="mygroup_tab_td2"><select name="Publicity"><%
if (mygroup.getPublicity()==3) 
	out.write("<option value=\"3\" selected>"+rb.getString("group.publicity.total")+"</option>");
else
	out.write("<option value=\"3\">"+rb.getString("group.publicity.total")+"</option>");
if (mygroup.getPublicity()==2)
	out.write("<option value=\"2\" selected>"+rb.getString("group.publicity.half")+"</option>");
else
	out.write("<option value=\"2\">"+rb.getString("group.publicity.half")+"</option>");
if (mygroup.getPublicity()==1)
	out.write("<option value=\"1\" selected>"+rb.getString("group.publicity.none")+"</option>");
else
	out.write("<option value=\"1\">"+rb.getString("group.publicity.none")+"</option>");
if (mygroup.getPublicity()==0)
	out.write("<option value=\"0\" selected>"+rb.getString("group.publicity.hidden")+"</option>");
else
	out.write("<option value=\"0\">"+rb.getString("group.publicity.hidden")+"</option>");
%></select></td>
</tr>
<tr>
<td class="mygroup_tab_td1"><%=rb.getString("group.info")%>:</td>
<td class="mygroup_tab_td2"><textarea name="Description"><%=mygroup.getDesc()%></textarea></td>
</tr>
<tr>
<td class="mygroup_tab_td1"><%=rb.getString("group.date")%>:</td>
<td class="mygroup_tab_td2"><%=mygroup.getCreateDate()%></td>
</tr>
<tr>
<td colspan="2" align="center">
<input type=button value=<%=rb.getString("group.update")%> onclick="return updategroup()"/>&nbsp;
<input type=button value=<%=rb.getString("group.dismiss")%> onclick="return deletegroup()"/>&nbsp;
<input type=button value=<%=rb.getString("group.changeleader")%> onclick="return changeleader()"/>
</td>
</tr>
<tr>
<td class="mygroup_tab_td1"><%=rb.getString("group.points")%>:</td>
<td class="mygroup_tab_td2"><%=mygroup.getPoints()%>&nbsp;
<input type=button value=<%=rb.getString("group.addgroupoints")%> onclick="return addgroupoints()"/>:
<input type=text name="points"/>&nbsp;(<%=rb.getString("group.mypoints")%>:<span id="mypoints"><%=user.getPoints()%></span>)
</td>
</tr>
<tr>
<td colspan="2">
<br><%=rb.getString("group.memberlist")%>:<br>
<table class="members_tab">
<tr><th class="group_tab_th1"><%=rb.getString("personal1.loginname")%></th>
<th class="group_tab_th2"><%=rb.getString("personal1.credits")%></th>
<th class="group_tab_th1"><%=rb.getString("personal1.points")%></th>
<th class="group_tab_th2"><%=rb.getString("group.kick")%></th></tr>
<% ArrayList<Users> members = mysession.getMemberList();
   for (Users u : members) {%>
     <tr><td class="group_tab_td1"><%=u.getUsername()%></td>
     <td class="group_tab_td2"><%=u.getCredits()%></td>
     <td class="group_tab_td1"><%=u.getPoints()%>&nbsp;
       <img src="/transmem/images/add.gif" onclick="addpoints(<%=u.getUserID()%>,'<%=u.getUsername()%>')"/></td>
     <td class="group_tab_td2"><%if (u.getUserID()!=user.getUserID()) {%>
	 	<img src="/transmem/images/remove.gif" onclick="kickmember(<%=u.getUserID()%>)"/><%}%>
	 </td>
     </tr>
<% }%>
</table>
</td>
</tr>
<%} else {%>
<tr>
<td class="mygroup_tab_td1"><%=rb.getString("group.name")%>:</td>
<td class="mygroup_tab_td2"><input type="text" name="GroupName"/></td>
</tr>
<tr>
<td class="mygroup_tab_td1"><%=rb.getString("group.publicity")%>:</td>
<td class="mygroup_tab_td2"><select name="Publicity">
<option value="3"><%=rb.getString("group.publicity.total")%></option>
<option value="2"><%=rb.getString("group.publicity.half")%></option>
<option value="1"><%=rb.getString("group.publicity.none")%></option>
<option value="0"><%=rb.getString("group.publicity.hidden")%></option>
</select>
</tr>
<tr>
<td class="mygroup_tab_td1"><%=rb.getString("group.info")%></td>
<td class="mygroup_tab_td2"><textarea name="Description"></textarea></td>
</tr>
<tr>
<td colspan="2" align="center"><input type=button onclick="return creategroup()" value=<%=rb.getString("group.create")%>/></td>
</tr>
<%}%>
</table>
</form>
</div></p>

<form id="group_form" method="post" action="/transmem/tm">
	<input type="hidden" name="action" value="GroupAction"/>
	<input type="hidden" name="operation"/>
	<input type="hidden" name="grpage"/>
	<input type="hidden" name="order"/>
	<input type="hidden" name="offset"/>
	<input type="hidden" name="group"/>
	<input type="hidden" name="userid"/>

<div id="addpointsbox" style="position:absolute;display:none;border:1px solid blue;background:yellow;padding:5px">
	<p><%=rb.getString("group.member.name")%>:<span id="apb_mname"></span></p>
	<p><%=rb.getString("group.points")%>:<span id="apb_gpoints"></span></p>
	<p><%=rb.getString("group.distribute.points")%>:<input type=text name="points"/></p>
	<center><input type=button value=<%=rb.getString("submit")%> onclick="addmemberpoints(1)"/>
	<input type=button value=<%=rb.getString("cancel")%> onclick="return addmemberpoints(0)"/>
	</center>
</div>

</form>

</body>
</html>
