<%@ page language="java" errorPage="error.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%
	java.util.ResourceBundle rb = (java.util.ResourceBundle)session.getAttribute("bundle");
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<meta http-enquiv="Content-Type" content="text/html; charset=utf-8"/>
<title><%=rb.getString("register.title")%></title>
<link rel="stylesheet" type="text/css" href="/transmem/css/index.css">
<SCRIPT language="JavaScript" src="/transmem/js/ajax.js"></SCRIPT>
<SCRIPT language="JavaScript" src="/transmem/js/entry.js"></SCRIPT>
<style type="text/css">
div#forget_div {
	border : 1px solid #CDEBFE;
	padding : 10px 20px;
}
p.fgp {
	border-top : 1px solid lightgray;
}
</style>

<script language="javascript" type="text/javascript">
var g_request;
function resetbyemail()
{
	var frm = document.getElementById("login_form_id");
	if (frm.username.value == "") {
		alert("Username not given");
		return false;
	}
	if (frm.email.value == "") {
		alert("Email not valid");
		return false;
	}
	var s = frm.email.value;
	var n1 = s.indexOf("@");
	var n2 = s.lastIndexOf(".");
	if (n1 > 1 && n2 > n1) {
		frm.operation.value = "email";
		frm.submit();
		return true;
	} else {
		alert("Email not valid");
		return false;
	}
}
function getQuestion()
{
	var frm = document.getElementById("login_form_id");
	var uname = frm.username.value;
	if (uname == "") {
		alert("Username empty");
		return false;
	}
	var cnt = "action=PasswordAction&operation=gq&username="+uname
	//send request
	postRequest(url,cnt,handleQuestion);
	return false;
}
function handleQuestion()
{
	try {
		if (g_request.readyState==4) {
			if (g_request.status==200) {
				var resp = g_request.responseText;
				var sp = document.getElementById("span_quest");
				sp.innerHTML = resp
				var frm = document.getElementById("login_form_id");
				frm.question.value = resp;
			}
		}
	} catch (err) {
		alert("Error occurred receiving response from server");
	}
}
function resetbyquestion()
{
	var frm = document.getElementById("login_form_id");
	if (frm.question.value == "") {
		alert("Enter a valid username");
		return false;
	}
	if (frm.answer.value == "") {
		alert("No answer");
		return false;
	}
	frm.operation.value = "qa";
	frm.submit();
	return true;
}
function resetbyid()
{
	var frm = document.getElementById("login_form_id");
	if (frm.username.value == "") {
		alert("No Username");
		return false;
	}
	if (frm.idnumber.value == "") {
		alert("No ID Number");
		return false;
	}
	frm.operation.value = "id";
	frm.submit();
	return true;
}
</script>
</head>
<body>
<%@include file="header.inc"%>

<br/>
<div id="forget_div">
<form id="login_form_id" method="post" action="/transmem/tm">
<input type=hidden name="action" value="PasswordAction"/>
<input type=hidden name="operation"/>
<input type=hidden name="question"/>
<input type="hidden" name="language"/>
<p id="p0"><%=rb.getString("forget.info0")%><br/>
<%=rb.getString("reg.name")%>:<input type="text" name="username"/>
</p>
<p class="fgp">
<%=rb.getString("forget.info1")%><br/>
<%=rb.getString("forget.in.email")%>:<input type="text" name="email" size=25/>(<%=rb.getString("forget.in.email1")%>)<br/>
<input type=button value=<%=rb.getString("submit")%> onclick="return resetbyemail()"/>
</p>
<p class="fgp">
<%=rb.getString("forget.info2")%><br/>
<a href="" onclick="return getQuestion()"><%=rb.getString("forget.getquestion")%></a><br/>
<table><tr><td>
<%=rb.getString("reg.quest")%>:</td><td><span id="span_quest"></span></td></tr>
<tr><td>
<%=rb.getString("reg.answer")%>:</td><td><input type="text" name="answer"/></td></tr></table>
<input type="button" value=<%=rb.getString("submit")%> onclick="return resetbyquestion()"/>
</p>
<p class="fgp">
<%=rb.getString("forget.info3")%><br/>
<table><tr><td>
<%=rb.getString("reg.idtype")%>:</td><td>
<select name="idtype">
<option value="ID"><%=rb.getString("reg.type.id")%></option>
<option value="PP"><%=rb.getString("reg.type.passport")%></option>
<option value="OT"><%=rb.getString("reg.type.other")%></option>
</select></td></tr><tr><td>
<%=rb.getString("reg.idnumber")%>:</td><td><input type="text" name="idnumber"/></td></tr></table>
<input type=button value=<%=rb.getString("submit")%> onclick="return resetbyid()">
</p>
</form>
</div>

</body>
</html>
