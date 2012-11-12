function sortGroupByName()
{
	frm = document.getElementById("group_form");
	if (frm != null) {
		//frm.action.value = "GroupAction";
		frm.operation.value = "sort";
		frm.order.value = "name";
		frm.submit();
	} else
		alert("group_form not found");
	return false;
}
function sortGroupByMembers()
{
	frm = document.getElementById("group_form");
	if (frm != null) {
		//frm.action.value = "GroupAction";
		frm.operation.value = "sort";
		frm.order.value = "members";
		frm.submit();
	} else
		alert("group_form not found");
	return false;
}
function sortGroupByDate()
{
	frm = document.getElementById("group_form");
	if (frm != null) {
		//frm.action.value = "GroupAction";
		frm.operation.value = "sort";
		frm.order.value = "date";
		frm.submit();
	} else
		alert("group_form not found");
	return false;
}
function joingroup(gid)
{
	frm = document.getElementById("group_form");
	if (frm != null) {
		//frm.action.value = "GroupAction";
		frm.operation.value = "joingroup";
		frm.group.value = gid;
		frm.submit();
	} else
		alert("group_form not found");
	return false;
}
function quitgroup()
{
	frm = document.getElementById("group_form");
	if (frm != null) {
		//frm.action.value = "GroupAction";
		frm.operation.value = "quitgroup";
		frm.submit();
	} else
		alert("group_form not found");
	return false;
}
function creategroup()
{
	frm = document.getElementById("group_edit_form");
	if (frm != null) {
		frm.operation.value = "creategroup";
		frm.submit();
	} else
		alert("group_edit_form not found");
	return false;
}
function updategroup()
{
	frm = document.getElementById("group_edit_form");
	if (frm != null) {
		frm.operation.value = "updategroup";
		frm.submit();
	} else
		alert("group_edit_form not found");
	return false;
}
function deletegroup()
{
	frm = document.getElementById("group_edit_form");
	if (frm != null) {
		frm.operation.value = "deletegroup";
		frm.submit();
	} else
		alert("group_edit_form not found");
	return false;
}
function changeleader()
{
	frm = document.getElementById("group_form");
	if (frm != null) {
		frm.operation.value = "changeleader";
		alert("not implemented yet");
	} else
		alert("group_form not found");
	return false;
}
function groupinfo()
{
	alert("Not implemented");
}
function showmygroup()
{
	frm = document.getElementById("group_form");
	if (frm != null) {
		frm.operation.value = null;
		frm.grpage.value = "mygroup";
		frm.submit();
	} else
		alert("group_form not found");
	return false;
}
function showgrouplist()
{
	frm = document.getElementById("group_form");
	if (frm != null) {
		frm.operation.value = null;
		frm.grpage.value = "list";
		frm.submit();
	} else
		alert("group_form not found");
	return false;
}
function addgroupoints()
{
	frm = document.getElementById("group_edit_form");
	if (frm != null) {
		var gpts = frm.points.value;
		if (gpts=="") {
			alert("No points");
			return false;
		}
		var mpt = document.getElementById("mypoints");
		var mpts = mpt.innerText;
		if (mpts == null) mpts = mpt.innerHTML;
		var ngpts = parseInt(gpts);
		if (ngpts <= 0) {
			alert("Points <= 0");
			return false;
		}
		var nmpts = parseInt(mpts);
		if (ngpts > nmpts) {
			alert("You have not enough points");
			return false;
		}
		alert("add "+gpts+" points to group");
		frm.operation.value = "points2group";
		frm.submit();
	} else
		alert("group_edit_form not found");
	return false;
}
function addpoints(uid,uname)
{
	//show transfer box
	var frm = document.getElementById("group_form");
	frm.userid.value = uid;
	var mname = document.getElementById("apb_mname");
	mname.innerHTML = uname;
	var dv = document.getElementById("addpointsbox");
	dv.style.display = "block";
	centerOnWindow(dv);
}
function addmemberpoints(doit)
{
	var dv = document.getElementById("addpointsbox");
	dv.style.display = "none";
	if (doit == false) {
		return false;
	}
	frm = document.getElementById("group_form");
	if (frm != null) {
		frm.operation.value = "points2member";
		if (frm.points.value == "") {
			alert("Points not given");
			return false;
		}
		frm.submit();
	} else
		alert("group_form not found");
	return false;
}
function kickmember()
{
	frm = document.getElementById("group_form");
	if (frm != null) {
		frm.operation.value = "kick";
		frm.user.value = usr;
		frm.submit();
	} else
		alert("group_form not found");
	return false;
}
