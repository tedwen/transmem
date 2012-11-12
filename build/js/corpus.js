function showcorpustally()
{
	var frm = document.getElementById("action_form");
	if (frm != null) {
		frm.action.value = "CorpusAction";
		frm.operation.value = "tally";
		var langobj = document.getElementById("langpair");
		if (langobj != null)
			frm.arg1.value = langobj.value;
		frm.submit();
	} else
		alert("action_form not found");
	return false;
}
function showmypool()
{
	var frm = document.getElementById("action_form");
	if (frm != null) {
		frm.action.value = "CorpusAction";
		frm.operation.value = "sources";
		var langobj = document.getElementById("langpair");
		if (langobj != null)
			frm.arg1.value = langobj.value;
		frm.submit();
	} else
		alert("action_form not found");
	return false;
}
function deleteSources()
{
	var i, node, s;
	var nodes = document.getElementsByTagName("input");
	for (i=0; i<nodes.length; i++) {
		if (nodes[i].type=="checkbox") {
			s += nodes[i].name + ",";
		}
	}
	alert(s);
	var frm = document.getElementById("action_form");
	frm.action.value = "CorpusAction";
	frm.operation.value = "delsources";
	frm.arg2.value = s;
	frm.submit();
	return false;
}
function editSource(sid)
{
	var frm = document.getElementById("action_form");
	frm.action.value = "CorpusAction";
	frm.operation.value = "units";
	frm.arg2.value = sid;
	frm.submit();
	//alert("edit:"+sid);
	return false;
}
function deleteUnits()
{
	var i, node, s;
	var nodes = document.getElementByTagNames("input");
	for (i=0; i<nodes.length; i++) {
		if (nodes[i].type=="checkbox") {
			s += nodes[i].name;
		}
	}
	alert(s);
	var frm = document.getElementById("action_form");
	frm.action.value = "CorpusAction";
	frm.operation.value = "delunits";
	frm.arg2.value = s;
	frm.submit();
	return false;
}
function editUnit(sid)
{
	var srcobj = document.getElementById("s."+sid);
	var tgtobj = document.getElementById("t."+sid);
	alert("Source="+srcobj.innerHTML);
	alert("Target="+tgtobj.innerHTML);
}
