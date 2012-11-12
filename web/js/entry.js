var g_request;
function alertMessage(msgcode)
{
	var dv = document.createElement(dv);
	dv.innerHTML = msgcode;
	alert(dv.innerHTML);
	document.removeElement(dv);
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
function selectLanguage(langselect)
{
	var frm = document.getElementById("login_form_id");
	frm.action.value = "LocaleAction";
	frm.language.value = langselect.value;
	frm.submit();
}
function gohome()
{
	location="/transmem/index.jsp";
}
function register()
{
	var frm = document.getElementById("login_form_id");
	frm.action.value = "RegisterAction";
	frm.submit();
	return false;
}
function forget()
{
	var frm = document.getElementById("login_form_id");
	frm.action.value = "PasswordAction";
	frm.submit();
	return false;
}
function checkname()
{
	var frm = document.getElementById("login_form_id");
	var lname = frm.username.value;
	if (lname == null || lname == "") {
		return false;
	}
	var cnt = "action=Username&username="+encodeURIComponent(lname);
	postRequest(url,cnt,handleQuery);
	return false;
}
function handleQuery()
{
	try {
		if (g_request.readyState==4) {
			if (g_request.status==200) {
				var resp = g_request.responseText;
				if (resp!=null) {
					var pf = resp.substring(0,1);
					if (pf == "-") {
						var tb = document.getElementById("nameused");
						tb.innerHTML = "";
						var tb = document.getElementById("namenotused");
						tb.innerHTML = resp.substring(1)+"! ";
					} else {
						var tb = document.getElementById("namenotused");
						tb.innerHTML = "";
						var tb = document.getElementById("nameused");
						tb.innerHTML = resp.substring(1)+"! ";
					}
				}
			}
		}
	} catch (err) {
		alert("Error occurred receiving response from server");
	}
}
