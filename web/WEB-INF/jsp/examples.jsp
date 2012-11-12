<%@ page language="java"%>
<%
	String exams = (String)session.getAttribute("examples");
	out.write(exams);
%>
