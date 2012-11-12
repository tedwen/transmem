<%@ page language="java" errorPage="jsperror.jsp?from=index" import="java.util.ResourceBundle" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="keywords" content="translation, memory, multilingual, database, corpus, CAT, TM, language">

<%
	ResourceBundle rb = (ResourceBundle)session.getAttribute("bundle");
	if (rb == null) {
		java.util.Locale locale = new java.util.Locale("zh");
		rb = ResourceBundle.getBundle("Messages",locale);
		if (rb != null) {
			session.setAttribute("bundle", rb);
		}
	}
%>

<title><%=rb.getString("home.title")%></title>
<style type="text/css">

.login td,.textbox {
	color: #008BD6;
	font-size: 14px;
}
.textbox {
	width: 110px;
	height: 18px;
	border: 1px solid #6AB5FF;
}
.login img {
	border: 0;
	cursor: hand;
	cursor: pointer;
}
table.content {
	background-color: #ffffff;
	border: 1px solid #b0bec7;
}
.cheader {
	color: #006699;
	background: #84AACE url(images/mbar2.jpg) repeat-x;
	height: 22px;
}
li {
	color: #0066CC;
	margin-top: 10px;
	background-image:url('images/black_divide.gif');
	background-repeat:repeat-x;
	background-position:bottom;
}
</style>

<script language="JavaScript" type="text/JavaScript">
function submit_login()
{
	if ( document.login_form.loginname.value == "" || document.login_form.loginname.value == "null" || document.login_form.loginname.value == null || document.login_form.loginname.value == "undefined" )
	{
		alert("<%=rb.getString("login.name.prompt")%>");
        return false;
	}
	if ( document.login_form.passwd.value == "" || document.login_form.passwd.value == "null" || document.login_form.passwd.value == null || document.login_form.passwd.value == "undefined" )
	{
		alert("<%=rb.getString("login.pass.prompt")%>");
        return false;
	}
	document.login_form.submit();
} 
function checkSubmit(ev)
{
	var key;
	if (window.event) {
		key = window.event.keyCode;
	} else {
		key = ev.keyCode;
	}
	if (key == 13) {
		submit_login();
	}
}
</script>
</head>
<body>

<%@ include file="header.inc" %>

<table class="outer" cellspacing="0" cellpadding="1" width="98%">
<tbody><tr>
<td valign="top" width="190">
	<form name="login_form" method="post" action="/transmem/tm">
	<input name="action" value="LoginAction" type="hidden">
  <table bgcolor="#aad5f5" cellpadding="0" cellspacing="1" height="143" width="190">
    <tbody><tr>
      <td background="images/vbar.jpg" bgcolor="#ffffff" valign="top">
		<table border="0" cellpadding="0" cellspacing="1" width="100%">
		  <tr><td>
			  <img src="images/login_title.jpg" height="22" width="186">
			</td></tr>
		</table>
		<table class="login" cellpadding="2" cellspacing="4" width="100%">
			<tr>
			  <td width="50" nowrap align="right"><%=rb.getString("login.name")%>:</td>
			  <td><input name="loginname" class="textbox" size="16" type="text"></td>
			</tr><tr>
			  <td align="right"><%=rb.getString("login.pass")%>:</td>
			  <td><input name="passwd" class="textbox" size="16" type="password" onKeyUp="checkSubmit(event)">
			  </td></tr>
		    <tr>
		    <td align="center" colspan="2">
			  <img onclick="return submit_login();" src="images/login_btn.jpg" border="0">&nbsp;
			  <img src="images/reg_btn.jpg" border="0" onclick="location='register.jsp'">
			</td></tr>
			<tr>
			<td colspan="2" align="center">
			  <img src="images/forget_btn.gif" border="0" onclick="location='forget.jsp'">
			</td></tr>
		</table>
      </td>
    </tr>
  </tbody></table>
  </form>
</td>
<td valign="top">
	<table class="content" cellpadding="1" cellspacing="2" width="500">
		<tr class="cheader">
			<td valign="top">
			</td>
		</tr>
		<tr>
			<td><%@include file="content.inc"%>
		</tr>
	</table>
</td>
</tr></tbody>
</table>
<p>
</p>
<%@ include file="footer.inc" %>

</body>
</html>
