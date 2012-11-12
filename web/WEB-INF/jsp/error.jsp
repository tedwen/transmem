<%@ page isErrorPage="true" %>
<%
	java.util.ResourceBundle rb = (java.util.ResourceBundle)session.getAttribute("bundle");
%>
<html>
<head><title><%=rb.getString("error.title")%></title></head>
<body>
<h2><%=rb.getString("error.title")%></h2>
<%
	Integer errorcode = (Integer)request.getAttribute("javax.servlet.error.status_code");
	if (errorcode != null)
	{
		out.print("<p><b>Error code:</b> "+errorcode+"</p>");
	}
	String errormsg = (String)request.getAttribute("javax.servlet.error.message");
	if (errormsg != null)
	{
		out.print("<p><b>Error message:</b> ");
		if (errormsg.startsWith("$"))
			out.print(rb.getString(errormsg));
		else
			out.print(errormsg);
		out.print("</p>");
	}
	Throwable obj = (Throwable)request.getAttribute("javax.servlet.error.exception");
	if (obj != null)
	{
		out.print("<p><b>Exception:</b> "+obj.getClass().getName()+"</p>");
		out.print("<p><b>Exception message:</b> "+obj.getMessage()+"</p>");
	}
	String requestUri = (String)request.getAttribute("javax.servlet.error.request_uri");
	if (requestUri != null)
	{
		out.print("<p><b>URL:</b> "+requestUri+"</p>");
	}
%>
</body>
</html>
