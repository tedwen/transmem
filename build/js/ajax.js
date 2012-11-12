var url = "http://localhost:8080/transmem/tm";
//dummy ajax receiver
function handleResponse()
{
	try {
		if (g_request.readystate==4) {
			if (g_request.status==200) {
				var resp = g_request.responseText;
				//var func = new Function("return "+resp);
				//var obj = func();
			}
		}
	} catch (err) {
		alert("Error occurred receiving response from server");
	}
}
//ajax sender
function postRequest(url, content, resphandler)
{
	g_request = null;
	if (window.ActiveXObject) {
		g_request = new ActiveXObject("Msxml2.XMLHTTP");
		if (!g_request) {
			g_request = new ActiveXObject("Microsoft.XMLHTTP");
		}
		if (!g_request) {
			alert("Your browser does not support this application");
			return;
		}
	} else if (window.XMLHttpRequest) {
		g_request = new XMLHttpRequest();
	} else {
		alert("Your browser does not support this application");
		return;
	}
	g_request.onreadystatechange = resphandler;
	g_request.open("POST",url,true);
	g_request.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
	g_request.send(content);
}
