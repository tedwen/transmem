<!-- Main Menu -->
<form id="action_form" method="post" action="/transmem/tm">
	<input type="hidden" name="action"/>
	<input type="hidden" name="operation" value="2page"/>
	<input type="hidden" name="arg1"/>
	<input type="hidden" name="arg2"/>
</form>
<table width=100% cellspacing=0 cellpadding=0 border=0>
<tr><td>
<img src="/transmem/images/logo.gif">
</td><td></td>
</tr>
</table>
<table width=100% id="mmenu_t" border=0 cellspacing=0 cellpadding=0>
<tr><td>
<ul id="mmenu">
<li id="mmenu_i1"><a href="" onclick="return turn2page('personal1.jsp')"><%=rb.getString("mmenu.personal")%></a></li>
<li id="mmenu_i2"><a href="" onclick="return gotoProject()"><%=rb.getString("mmenu.project")%></a></li>
<li id="mmenu_i3"><a href="" onclick="return gotoGroup('0')"><%=rb.getString("mmenu.group")%></a></li>
<li id="mmenu_i4"><a href="" onclick="return gotoCorpus()"><%=rb.getString("mmenu.corpus")%></a></li>
<li id="mmenu_i5"><a href="" onclick="return turn2page('forum.jsp')"><%=rb.getString("mmenu.forum")%></a></li>
<li id="mmenu_i6"><a href="" onclick="return turn2page('admin.jsp')"><%=rb.getString("mmenu.admin")%></a></li>
<li id="mmenu_i7"><a href="" onclick="return turn2page('help.jsp')"><%=rb.getString("mmenu.help")%></a></li>
</ul>
</td>
<td align="right" valign="bottom"><font size="-1">
<%=rb.getString("welcome")%>, <%=user.getUsername()%>! [<a href="" onclick="return logout()"><%=rb.getString("logout")%></a>]</font>
&nbsp;&nbsp;</td>
</tr>
</table>
<!-- end of Main Menu -->
