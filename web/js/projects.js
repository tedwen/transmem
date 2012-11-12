function deleteProject(pid)
{
	frm = document.getElementById("project_form");
	frm.action.value = "DeleteProjectAction";
	frm.project.value = pid;
	//alert("delete project:"+pid);
	frm.submit();
}
function openProject(pid)
{
	frm = document.getElementById("project_form");
	frm.action.value = "OpenProjectAction";
	frm.project.value = pid;
	//alert("open project:"+pid);
	frm.submit();
}
function submitProject()
{
	frm = document.getElementById("new_project");
	if (frm.projectName.value == "") {
		alert("Please enter project name");
		return false;
	}
	//alert("submit:"+frm.projectName.value);
	frm.submit();
	return true;
}
