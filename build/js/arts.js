//-------- articles.jsp ----------
function translateArticle(aid)
{
	//alert("translate article:"+aid);
	frm = document.getElementById("article_form");
	frm.action.value = "TranslateAction";
	frm.article.value = aid;
	frm.submit();
}
function deleteArticle(aid)
{
	//alert("delete article: "+aid);
	frm = document.getElementById("article_form");
	frm.action.value = "DeleteArticleAction";
	frm.article.value = aid;
	frm.submit();
}
function downloadTarget(aid)
{
	frm = document.getElementById("article_form");
	frm.action.value = "DownloadAction";
	frm.article.value = aid;
	frm.submit();
	return true;
}
function shareTranslation(aim,aid)
{
	var dv = document.getElementById("share_dv");
	var frm = document.getElementById("share_form");
	//alert(frm.permit[0].value+","+frm.domain.value);
	var i, px = 0;
	for (i=0; i<frm.permit.length; i++)
		if (frm.permit[i].checked)
			px = i;
	if (aim == 0)
	{
		frm.aid.value = aid;
		centerOnWindow(dv);
		dv.style.display = "block";
	}
	else if (aim == 1)
	{
		var cnt = "action=ShareTranslation&aid="+frm.aid.value;
		cnt += "&domain="+frm.domain.value+"&permit="+frm.permit[px].value;
		//alert(cnt);
		postRequest(url,cnt,handleResponse);
		dv.style.display = "none";
		dv.visibility = "hidden";
	}
	else {
		dv.style.display = "none";
		dv.visibility = "hidden";
	}
	//return true;
}
function addArticle()
{
	//alert("submit a new article");
	frm = document.getElementById("submit_article");
	if (frm.title.value=="") {
		alert("Please enter article name");
	}
	else if (frm.articleFile.value=="") {
		alert("Please attach a file");
	} else {
		fname = frm.articleFile.value;
		n = fname.lastIndexOf('.');
		if (n > 0) {
			fext = fname.substring(n).toLowerCase();
			if (fext == ".txt")
			{
				frm.submit();
			}
			else
				alert("Only .txt file supported at the moment.");
		}
		else
			alert("Not valid file type");
	}
}
function showprojects()
{
	frm = document.getElementById("link_form");
	frm.submit();
	return false;
}
function getObjectWidth(obj)  {
    var result = 0;
    if (obj.offsetWidth) {
        result = obj.offsetWidth;
    } else if (obj.clip && obj.clip.width) {
        result = obj.clip.width;
    } else if (obj.style && obj.style.pixelWidth) {
        result = obj.style.pixelWidth;
    } else if (obj.clientWidth) {
		result = obj.clientWidth;
	}
    //alert("object width="+result);
    return parseInt(result);
}
function getObjectHeight(obj)  {
    var result = 0;
    if (obj.offsetHeight) {
        result = obj.offsetHeight;
    } else if (obj.clip && obj.clip.height) {
        result = obj.clip.height;
    } else if (obj.style && obj.style.pixelHeight) {
        result = obj.style.pixelHeight;
    } else if (obj.clientHeight) {
		result = obj.clientHeight;
	}
    //alert("object height="+result);
    return parseInt(result);
}
function getInsideWindowWidth( ) {
    if (window.innerWidth) {
        return window.innerWidth;
    } else if (document.compatMode && document.compatMode.indexOf("CSS1") >= 0) {
        // measure the html element's clientWidth
        return document.body.parentElement.clientWidth;
    } else if (document.body && document.body.clientWidth) {
        return document.body.clientWidth;
    }
    return 0;
}
function getInsideWindowHeight( ) {
    if (window.innerHeight) {
        return window.innerHeight;
    } else if (document.compatMode && document.compatMode.indexOf("CSS1") >= 0) {
        // measure the html element's clientHeight
        return document.body.parentElement.clientHeight;
    } else if (document.body && document.body.clientHeight) {
        return document.body.clientHeight;
    }
    return 0;
}
function centerOnWindow(obj) {
    // window scroll factors
    var scrollX = 0, scrollY = 0;
    if (document.body && typeof document.body.scrollTop != "undefined") {
        scrollX += document.body.scrollLeft;
        scrollY += document.body.scrollTop;
        if (document.body.parentNode && 
            typeof document.body.parentNode.scrollTop != "undefined") {
            scrollX += document.body.parentNode.scrollLeft;
            scrollY += document.body.parentNode.scrollTop;
        }
    } else if (typeof window.pageXOffset != "undefined") {
        scrollX += window.pageXOffset;
        scrollY += window.pageYOffset;
    }
    var x = Math.round((getInsideWindowWidth( )/2) - (getObjectWidth(obj)/2)) + scrollX;
    var y = Math.round((getInsideWindowHeight( )/2) - (getObjectHeight(obj)/2)) + scrollY;
    if (document.body && document.body.style) {
		obj = obj.style;
		var units = (typeof obj.left == "string") ? "px" : 0;
		obj.left = x + units;
		obj.top = y + units;
	} else if (document.layers) {
		obj.moveTo(x, y);
	}
    obj.visibility = "visible";
}
function selectTranslator(obj, aid)
{
	var dv = document.getElementById("selector");
	dv.style.display = "block";
	centerOnWindow(dv);
	var frm = document.getElementById("select_form");
	frm.article.value = aid;
	var cnt = "action=ReplaceTranslator&aid="+aid+"&translator=0";
	postRequest(url, cnt, receiveCandidates);
	return false;
}
function receiveCandidates()
{
	try {
		if (g_request.readyState==4) {
			if (g_request.status==200) {
				var resp = g_request.responseText;
				if (resp.charAt(0)=='(') {
					var n = resp.indexOf(")");
					var aid = resp.substring(1,n);
					var sname = resp.substring(n+1);
					var elem = "ar"+aid
					var spanobj = document.getElementById(elem);
					if (spanobj != null)
						spanobj.innerHTML = sname;
				} else {
					var place = document.getElementById("candidates");
					place.innerHTML = resp;
				}
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
function replaceTranslator(doit)
{
	if (doit == 1)
	{
		frm = document.getElementById("select_form");
		var cnt = "action=ReplaceTranslator&aid="+frm.article.value+"&translator="+frm.translator.value;
		//alert(cnt);
		postRequest(url, cnt, receiveCandidates);
	}
	var dv = document.getElementById("selector");
	dv.style.display = "none";
	dv.visibility = "hidden";
	return false;
}
function setTranslator(tid)
{
	frm = document.getElementById("select_form");
	frm.translator.value = tid;
}

