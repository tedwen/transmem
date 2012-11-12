function turn2page(page)
{
	frm = document.getElementById("action_form");
	if (frm != null) {
		frm.action.value = "ProfileAction";
		frm.arg1.value = page;
		frm.submit();
	}
	return false;
}
function logout()
{
	frm = document.getElementById("action_form");
	if (frm != null) {
		frm.action.value = "LogoutAction";
		frm.submit();
	}
	return false;
}
function gotoProject()
{
	frm = document.getElementById("action_form");
	if (frm != null) {
		frm.action.value = "LinkAction";
		frm.arg1.value = "projects";
		frm.submit();
	}
	return false;
}
function gotoGroup(arg)
{
	frm = document.getElementById("action_form");
	if (frm != null) {
		frm.action.value = "GroupAction";
		frm.operation.value = "show";
		frm.arg1.value = arg;
		frm.submit();
	}
	return false;
}
function gotoCorpus()
{
	frm = document.getElementById("action_form");
	if (frm != null) {
		frm.action.value = "CorpusAction";
		frm.operation.value = null;
		frm.submit();
	}
	return false;
}
