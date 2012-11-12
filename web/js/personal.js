function submitProfile()
{
	frm = document.getElementById("update_profile_form");
	if (frm != null) {
		if (frm.passwd.value==null || frm.passwd.value.equals("")) {
			alert("Password not given");
			return false;
		}
		else if (frm.newpasswd.value != null && frm.newpasswd.value.length()>0) {
			if (frm.newpasswd.value != frm.newpasswd2.value)) {
				alert("New password is not the same");
				return false;
			}
		}
		return true;
	}
	else
		alert("update_profile_form not found!");
	return false;
}
