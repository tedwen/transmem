// javascript functions to be used by translate?.jsp
//---------- translatex.jsp -------------
var g_which = 0;
var g_freeze = false;
var g_modified = false;
var g_request;

function fillTargetBox(x)
{
	var tgtbox = document.getElementById("p_tgt");
	if (x < 0) g_which = 0;
	if (x >= g_translations.length) x = g_translations.length - 1;
	var i;
	var st = "";
	for (i = 0; i < g_translations.length; i++) {
		if (i == g_which) {
			st += "<span class=\"tgt\">"+g_translations[i]+"</span> ";
		} else {
			st += g_translations[i]+" ";
		}
	}
	tgtbox.innerHTML = st;
	//alert("fillTargetBox done");
}

function selectSentence(x)
{
	if (g_freeze) return;
	var srcbox = document.getElementById("p_src");
	if (x < 0) x = 0;
	if (x >= g_sentences.length) x = g_sentences.length - 1;
	var i;
	var st = "";
	for (i = 0; i < g_sentences.length; i++) {
		if (i == x) {
			st += "<span class=\"src\">"+g_sentences[i]+"</span> ";
		} else {
			st += g_sentences[i]+" ";
		}
	}
	srcbox.innerHTML = st;
	senbox = document.getElementById("s_src");
	senbox.innerHTML = g_sentences[x];
	editbox = document.getElementById("s_edit");
	editbox.value = g_translations[x];
	fillTargetBox(x);
	labelbox = document.getElementById("s_num");
	n = x + 1;
	labelbox.innerHTML = n + "/" + g_sentences.length;
	editbox.focus();
	//look up examples here
	lookup();
}
function sendTranslation(sentindex)
{
	//use Ajax to send translation back
	g_modified = false;
	var cnt = "action=UpdateTranslation&index="+sentindex;
	cnt += "&trans=" + encodeURIComponent(g_translations[sentindex]);
	//alert(cnt);
	postRequest(url,cnt,handleResponse);
}
function prevSent()
{
	if (g_modified) {
		sendTranslation(g_which);
	}
	if (g_which > 0) g_which --;
	selectSentence(g_which);
}
function nextSent()
{
	//alert("nextSent()");
	if (g_modified) {
		//alert("SendTranslation("+g_which);
		sendTranslation(g_which);
	}
	if (g_which < g_sentences.length-1) g_which ++;
	selectSentence(g_which);
}

function acceptTranslation()
{
	//alert("acceptTranslation()");
	var editbox = document.getElementById("s_edit");
	g_translations[g_which] = editbox.value;
	//alert("[g_which] = "+editbox.value);
	g_modified = true;
	//alert("g_modified="+g_modified);
	fillTargetBox(g_which);
	//alert("fillTargetBox()");
	nextSent();
}
function use(block)
{
	var editbox = document.getElementById("s_edit");
	var txt = editbox.value + g_separator + block.innerHTML;
	editbox.value = txt;
	editbox.focus();
}
function copySelText()
{
	var txt = '';
	if (window.getSelection)
		txt = window.getSelection();
	else if (document.getSelection)
		txt = document.getSelection();
	else if (document.selection)
		txt = document.selection.createRange().text;
	if (txt!='') {
		var editbox = document.getElementById("s_edit");
		var txt2 = editbox.value + g_separator + txt;
		editbox.value = txt2;
		editbox.focus();
	}
}
function checkKeys(ev)
{
	var key;
	if (window.event) {
		key = window.event.keyCode;
	} else {
		key = ev.keyCode;
	}
	if (key == 13) {
		acceptTranslation();
		return false;
	}
	return true;
	//TODO: navigation keys necessary?
}
function openSplit(obj)
{
	var offsetx = 0;
	var offsety = 0;
	var o = obj;
	while (o) {
		offsetx += o.offsetLeft;
		offsety += o.offsetTop;
		o = o.offsetParent;
	}
	if (navigator.userAgent.indexOf("Mac") != -1 && typeof document.body.leftMargin != "undefined") {
		offsetx += document.body.leftMargin;
		offsety += document.body.topMargin;
	}
	var dv = document.getElementById("split");
	dv.style.left = offsetx;
	dv.style.top = offsety;
	var sbox = document.getElementById("split1");
	sbox.value = g_sentences[g_which];
	dv.style.display = "block";
	g_freeze = true;
}
function sendSplit(sentindex,sent1,sent2)
{
	var cnt = "action=SplitSentenceAction&index="+sentindex;
	cnt += "&sent1=" + encodeURIComponent(sent1) + "&sent2=" + encodeURIComponent(sent2);
	postRequest(url,cnt,handleResponse);
}
function split(doit)
{
	var dv = document.getElementById("split");
	dv.style.display = "none";
	g_freeze = false;
	if (doit==1) {
		var first = document.getElementById("split1");
		var second = document.getElementById("split2");
		var sent1 = first.value;
		var sent2 = second.value;
		if (sent1 == g_sentences[g_which]) {
			alert("Sentence not changed");
			return;
		}
		if (sent2 == "") {
			alert("No second sentence");
			return;
		}
		if (sent1 == sent2) {
			alert("Sentence copied");
		}
	
		g_sentences.splice(g_which, 1, sent1, sent2);
		g_translations.splice(g_which+1, 0, "");
		selectSentences(g_which);

		sendSplit(g_which, sent1, sent2);
	}
}
function sendMerge(x)
{
	var cnt = "action=MergeSentenceAction&index="+sentindex;
	postRequest(url,cnt,handleResponse);
}
function merge()
{
	if (confirm("Are you sure to merge this sentence with next?")==true) {
		var s = g_sentences[g_which] + g_sentences[g_which+1];
		g_sentences.splice(g_which,2,s);
		s = g_translations[g_which] + g_translations[g_which+1];
		g_translations.splice(g_which,2,s);
		selectSentences(g_which);
		sendMerge(g_which);
	}
}

function prevPara()
{
	if (g_thispara != "1") {
		//send last translation sentence if not yet
		if (g_modified) {
			sendTranslation(g_which);
		}
		frm = document.getElementById("actform");
		frm.op.value = "prevp";
		frm.submit();
	}
}
function nextPara()
{
	if (g_thispara != g_numparas) {
		//send last translation sentence if not yet
		if (g_modified) {
			sendTranslation(g_which);
		}
		frm = document.getElementById("actform");
		frm.op.value = "nextp";
		frm.submit();
	}
}
function handleExamples()
{
	try {
		if (g_request.readyState==4) {
			if (g_request.status==200) {
				var resp = g_request.responseText;
				var n = resp.indexOf('*');
				var resp1 = resp.substring(0,n);
				var pagetag = document.getElementById("ex_pages");
				pagetag.innerHTML = resp1;
				var place = document.getElementById("examples");
				place.innerHTML = resp.substring(n+1);
				//grab img tags and assign useSentence event handler
				var imgs = place.getElementsByTagName("img");
				//var imgs = document.getElementsByTagName("img.eh");
				if (imgs != null) {
					for (i = 0; i < imgs.length; i++) {
						imgs[i].onclick = useSentence;
					}
				}

				//TODO: change waiting animated gif on example header to still gif
			} else {
				//write error msg in window, and change image to error
				//alert("Error occurred getting examples from server.");
			}
		}
	} catch (err) {
		alert("Error occurred receiving response from server");
		//write message in examples div, and change image to error
	}
}
function lookup()
{
	//alert("about to look up");
	//look up examples for the current sentence via Ajax
	var cnt = "action=QueryCorpus&index="+g_which;
	//alert("lookup param="+url+"?"+cnt);
	postRequest(url,cnt,handleExamples);
	//alert("lookup request sent");
	//TODO: change to use waiting animated gif on example header
}
function turnpage(dir)
{
	var cnt = "action=CorpusPageAction&pagedir="+dir;
	postRequest(url,cnt,handleExamples);
}
function changedomain(sels)
{
	var ds = sels.value;
	var cnt = "action=QueryCorpus&index="+g_which+"&domain="+ds;
	//alert(cnt);
	postRequest(url,cnt,handleExamples);
}
function useSentence(ev)
{
	//alert(ev)
	var obj;
	if (ev.target) obj = ev.target; else obj = ev.srcElement;
	//alert(obj);
	var id = obj.getAttribute("id");
	//alert(id);
	var td = document.getElementById("eh"+id);
	//alert(td);
	var st = td.innerHTML;
	if (st == null) st = td.innerText;
	//alert(st);
	var n1 = st.indexOf(">");
	var n2 = st.lastIndexOf("<");
	if (n2 > n1) {
		s = st.substring(n1+1,n2);
		//if (s == null) s = td.innerHTML;
		//alert(s);
		var editbox = document.getElementById("s_edit");
		editbox.value = editbox.value + s;
	}
}
function changepermit(sels)
{
	var ds = sels.value;
	var cnt = "action=QueryCorpus&index="+g_which+"&permit="+ds;
	//alert(cnt);
	postRequest(url,cnt,handleExamples);
}
window.onload = function()
{
	var btnParaPrev = document.getElementById("p_prev");
	if (btnParaPrev != null) {
		btnParaPrev.onclick = prevPara;
	}
	var btnParaNext = document.getElementById("p_next");
	if (btnParaNext != null) {
		btnParaNext.onclick = nextPara;
	}
	var btnLookup = document.getElementById("btnlookup");
	if (btnLookup != null) {
		btnLookup.onclick = lookup;
	}
	//var btnSplit = document.getElementById("btnsplit");
	//if (btnSplit != null) {
	//	btnSplit.onclick = openSplit(btnSplit);
	//}
	var btnMerge = document.getElementById("btnmerge");
	if (btnMerge != null) {
		btnMerge.onclick = merge;
	}
	var btnAccept = document.getElementById("btnaccept");
	if (btnAccept != null) {
		btnAccept.onclick = acceptTranslation;
	}
	var btnCopyUse = document.getElementById("btcopyuse");
	if (btnCopyUse != null) {
		btnCopyUse.onclick = copySelText;
	}
	var btnSentPrev = document.getElementById("s_prev");
	if (btnSentPrev != null) {
		btnSentPrev.onclick = prevSent;
	}
	var btnSentNext = document.getElementById("s_next");
	if (btnSentNext != null) {
		btnSentNext.onclick = nextSent;
	}
	var paraNum = document.getElementById("p_num");
	paraNum.innerHTML = g_thispara + "/" + g_numparas;

	selectSentence(g_which);
}
//-------------- links.inc -------------
function changelayout()
{
	frm = document.getElementById("link_form");
	frm.action.value = "TranslateAction";
	frm.submit();
}
//---------- end