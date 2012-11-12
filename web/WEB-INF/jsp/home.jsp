<%@ page language="java" pageEncoding="utf-8"
	import="java.util.ResourceBundle,java.util.Locale,com.transmem.action.Session" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="keywords" content="translation, memory, multilingual, database, corpus, CAT, TM, language">

<%
	Session mysession = new Session(session);
	ResourceBundle rb = mysession.getResourceBundle();
	if (rb == null) {
		String mylang = mysession.getUserLanguage();
		if (mylang == null) {
			mylang = request.getLocale().getLanguage();
			mysession.setUserLanguage(mylang);
		}
		rb = ResourceBundle.getBundle("Messages",new Locale(mylang,""));
		if (rb != null) {
			mysession.setResourceBundle(rb);
		}
	}
%>

<title><%=rb.getString("home.title")%></title>
<link rel="stylesheet" type="text/css" href="/transmem/css/index.css">
<SCRIPT language="JavaScript" src="/transmem/js/entry.js"></SCRIPT>
<script language="JavaScript" type="text/JavaScript">
function submit_login()
{
	if ( document.login_form.loginname.value == "" || document.login_form.loginname.value == "null" || document.login_form.loginname.value == null || document.login_form.loginname.value == "undefined" )
	{
		alertMessage('<%=rb.getString("login.name.prompt")%>');
        return false;
	}
	if ( document.login_form.passwd.value == "" || document.login_form.passwd.value == "null" || document.login_form.passwd.value == null || document.login_form.passwd.value == "undefined" )
	{
		alertMessage('<%=rb.getString("login.pass.prompt")%>');
        return false;
	}
	document.login_form.submit();
}
</script>
</head>
<body>
<%@ include file="header.inc" %>
<br />

<table border="0" width="780" cellpadding="1" cellspacing="0" bgcolor="#FFFFFF">
<tr>
  <td valign="top" width="220">
	<form id="login_form_id" name="login_form" method="post" action="/transmem/tm">
	<input name="action" value="LoginAction" type="hidden"/>
	<input name="language" type="hidden"/>
    <table border="0" width="100%" cellpadding="15" cellspacing="0" bgcolor="FFFFFF">
     <tr>
      <td width="55%" bgcolor="#ffffff" valign="top">
      <img src="images/returning-users.gif" border="0" alt="Returning Users"><br/>
        <p><font size=-1><%=rb.getString("login.prompt")%></font></p>

		<table border="0" class="login" cellpadding="2" cellspacing="4" width="100%">
			<tr>
			  <td width="60" NOWRAP align="right"><strong><%=rb.getString("login.name")%>:</strong></td>
			  <td><input name="loginname" class="textbox" size="25" type="text"></td>
			</tr><tr>
			  <td align="right" NOWRAP><strong><%=rb.getString("login.pass")%>:</strong></td>
			  <td><input name="passwd" class="textbox" size="25" type="password" onKeyUp="checkSubmit(event)">
			  </td></tr>
		    <tr>
		    <td align="center" colspan="2">
		      <input type=button value=<%=rb.getString("login.word")%> onclick="return submit_login()"/>
			</td></tr>
		</table>
		<p align=left style="margin-left:10px">
			<%=rb.getString("ask.notmember")%>
		   <img src="images/red-arrow.gif" height="8" width="6" border="0" alt="->">&nbsp;
		   <a href="" onclick="return register()"><strong><%=rb.getString("ask.joinnow")%></strong></a>
		    <img src="images/spacer-grey.gif" height="1" width="300" border="0" alt="">
		    <br/>
		    <%=rb.getString("ask.forgotpasswd")%>
	       <img src="images/red-arrow.gif" height="8" width="6" border="0" alt="->">&nbsp;
			<a href="" onclick="return forget()"><strong><%=rb.getString("ask.getmypasswd")%></strong></a>
		</p>
      </td>
    </tr>
  </table>
  </form>
</td>
<td valign="top">
	<table border="0" cellpadding="1" cellspacing="2" width="100%">
		<tr class="cheader">
			<td valign="top">
			</td>
		</tr>
		<tr>
			<td><img src="images/newhint.gif" border="0" alt=<%=rb.getString("ask.joinnow")%>/><br>
			<%@include file="content.inc"%>
			</td>
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
