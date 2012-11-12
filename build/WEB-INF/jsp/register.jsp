<%@ page language="java"
	import="com.transmem.data.db.Users,com.transmem.action.Session" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%
	Session mysession = new Session(session);
	java.util.ResourceBundle rb = (java.util.ResourceBundle)session.getAttribute("bundle");
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<meta http-enquiv="Content-Type" content="text/html; charset=utf-8"/>
<title><%=rb.getString("register.title")%></title>
<link rel="stylesheet" type="text/css" href="/transmem/css/index.css">
<link rel="stylesheet" type="text/css" href="/transmem/css/reg.css">
<SCRIPT language="JavaScript" src="/transmem/js/ajax.js"></SCRIPT>
<SCRIPT language="JavaScript" src="/transmem/js/entry.js"></SCRIPT>
<script language="JavaScript" type="text/JavaScript">
function verify_submit()
{
	if (document.reg_form.username.value=="" || document.reg_form.username.value == "null" || document.reg_form.username.value == null || document.reg_form.username.value == "undefined" ) {
		alertMessage('<%=rb.getString("reg.prompt.nousername")%>');
		return false;
	}
	if (document.reg_form.password.value=="" || document.reg_form.password.value == "null" || document.reg_form.password.value == null || document.reg_form.password.value == "undefined" ) {
		alertMessage('<%=rb.getString("reg.prompt.nopasswd")%>');
		return false;
	}
	if (document.reg_form.email.value=="" || document.reg_form.email.value == "null" || document.reg_form.email.value == null || document.reg_form.email.value == "undefined" ) {
		alertMessage('<%=rb.getString("reg.prompt.noemail")%>');
		return false;
	}
	if (document.reg_form.password.value != document.reg_form.password2.value) {
		alertMessage('<%=rb.getString("reg.prompt.pwdnotsame")%>');
		return false;
	}
	if (document.reg_form.question.value=="" || document.reg_form.question.value=="null" || document.reg_form.question.value==null || document.reg_form.question.value=="undefined") {
		alertMessage('<%=rb.getString("reg.prompt.noquestion")%>');
		return false;
	}
	if (document.reg_form.answer.value=="" || document.reg_form.answer.value=="null" || document.reg_form.answer.value==null || document.reg_form.answer.value=="undefined") {
		alertMessage('<%=rb.getString("reg.prompt.noanswer")%>');
		return false;
	}
	if (!document.reg_form.accept.checked) {
		alertMessage('<%=rb.getString("reg.prompt.accept")%>');
		return false;
	}
	document.reg_form.submit();
	return true;
}
</script>
</head>
<body>
<%@include file="header.inc"%>
<%
	String usrname,realname,sex,email,mobile,idtype,idnumber,question,answer,member;
	usrname = realname = email = mobile = "";
	sex = "M";
	idtype = idnumber = question = answer = "";
	member = "T";
	Users user = mysession.getTempUser();
	if (user != null) {
		usrname = user.getUsername();
		realname = user.getRealname();
		sex = user.getSex();
		email = user.getEmail();
		mobile = user.getMobile();
		idtype = user.getIdType();
		idnumber = user.getIdNumber();
		question = user.getQuestion();
		answer = user.getAnswer();
		member = user.getMembership();
		if (usrname == null) usrname = "";
		if (realname == null) realname = "";
		if (sex == null) sex = "M";
		if (email == null) email = "";
		if (mobile == null) mobile = "";
		if (idtype == null) idtype = "";
		if (idnumber == null) idnumber = "";
		if (question == null) question = "";
		if (answer == null) answer = "";
		if (member == null) member = "T";
	}
%>
<br>
<form id="login_form_id" name="reg_form" method="post" action="/transmem/tm">
<input type="hidden" name="subreg" value="20020208"/>
<input type="hidden" name="action" value="RegisterAction"/>
<input type="hidden" name="language"/>
<div id="reg_div">
<h3><%=rb.getString("reg.table")%></h3>
<% if (session.getAttribute("tuser_error")!=null) {%>
<div id="msg_area"><%=rb.getString((String)session.getAttribute("tuser_error"))%></div>
<%session.removeAttribute("tuser_error");}%>
<table id="reg_tab">
	<tr><td><%=rb.getString("reg.name")%><b>*</b>:</td>
		<td><input type="text" name="username" value="<%= usrname %>">
		<a href="" onclick="return checkname()"><%=rb.getString("reg.checkname")%></a></td>
		<td class="reg_tdr"><span id="namenotused"></span><span id="nameused"></span><%=rb.getString("reg.info.name")%></td></tr>
	<tr><td><%=rb.getString("reg.pass")%><b>*</b>:</td><td><input type="password" name="password"></td>
		<td class="reg_tdr"><%=rb.getString("reg.info.pass")%></td></tr>
	<tr><td><%=rb.getString("reg.confirm")%><b>*</b>:</td><td><input type="password" name="password2"></td>
		<td class="reg_tdr"><%=rb.getString("reg.info.pass2")%></td></tr>
	<tr><td><%=rb.getString("reg.realname")%><b>*</b>:</td><td><input type="text" name="realname" value="<%= realname %>"></td>
		<td class="reg_tdr"><%=rb.getString("reg.info.realname")%></td></tr>
	<tr><td><%=rb.getString("reg.sex")%><b>*</b>:</td>
	    <td><input type="radio" name="sex" value="M"<% if (sex.equals("M")) out.write(" checked"); %>"><%=rb.getString("sex.male")%>
	    <input type="radio" name="sex" value="F"<% if (sex.equals("F")) out.write(" checked"); %>"><%=rb.getString("sex.female")%>
		</td><td class="reg_tdr"></td></tr>
	<tr><td><%=rb.getString("reg.email")%><b>*</b>: </td><td><input type="text" name="email" value="<%= email %>"></td>
		<td class="reg_tdr"><%=rb.getString("reg.info.email")%></td></tr>
	<tr><td><%=rb.getString("reg.mobile")%>: </td><td><input type="text" name="mobile" value="<%= mobile %>"></td>
		<td class="reg_tdr"><%=rb.getString("reg.info.mobile")%></td></tr>
	<tr><td><%=rb.getString("reg.idtype")%>: </td><td><select name="idtype">
		<option value="ID"<% if (idtype.equals("ID")) out.write(" selected"); %>><%=rb.getString("reg.type.id")%></option>
		<option value="PP"<% if (idtype.equals("PP")) out.write(" selected"); %>><%=rb.getString("reg.type.passport")%></option>
		<option value="OT"<% if (idtype.equals("OT")) out.write(" selected"); %>><%=rb.getString("reg.type.other")%></option>
		</select></td><td class="reg_tdr"><%=rb.getString("reg.info.idtype")%></td></tr>
	<tr><td><%=rb.getString("reg.idnumber")%>: </td><td><input type="text" name="idnumber" value="<%= idnumber %>"></td>
		<td class="reg_tdr"><%=rb.getString("reg.info.idnum")%></td></tr>
	<tr><td><%=rb.getString("reg.quest")%>:</td><td><input type="text" name="question" value="<%= question %>"></td>
		<td class="reg_tdr"><%=rb.getString("reg.info.quest")%></td></tr>
	<tr><td><%=rb.getString("reg.answer")%>:</td><td><input type="text" name="answer" value="<%= answer %>"></td>
		<td class="reg_tdr"><%=rb.getString("reg.info.answer")%></td></tr>
	<tr><td><%=rb.getString("reg.member")%>: </td><td><select name="member">
		<option value="T"<% if (member.equals("T")) out.write(" selected"); %>><%=rb.getString("reg.member.translator")%>
		<option value="C"<% if (member.equals("C")) out.write(" selected"); %>><%=rb.getString("reg.member.company")%>
		<option value="S"<% if (member.equals("S")) out.write(" selected"); %>><%=rb.getString("reg.member.sponsor")%>
		<option value="O"<% if (member.equals("O")) out.write(" selected"); %>><%=rb.getString("reg.member.other")%>
		</select></td><td class="reg_tdr"><%=rb.getString("reg.info.member")%></td></tr>
	<tr><td colspan="3" align="center">
		<input type=checkbox name="accept"><%=rb.getString("reg.accept")%>&nbsp;
		<a href="/transmem/terms.html" target=_blank><%=rb.getString("reg.terms")%></a>
	</tr>
	<tr><td colspan="3" align="center">
		<input type="button" value="<%=rb.getString("submit")%>" onclick="return verify_submit();"/>
		<input type="button" value="<%=rb.getString("cancel")%>" onclick="gohome()"/>
	</td></tr>
</table>
</div>
</form>

<!-- footer -->

</body>
</html>
