<!-- begin Header -->
<table border="0" width="760" cellpadding="0" cellspacing="0">
<tr>
	<td>
		<!-- Main Navigation Bar -->
	<div class="logoHeader">
		<div id="left"><a href="/transmem"><img src="/transmem/images/logo.gif" border="0" /></a></div>
		<div id="right"></div>
	</div>
	<br clear="all" />
	</td>
	<td align="right"><% String lang = (String)session.getAttribute("UserLanguage");%>(Only English &amp; Chinese)
	<select name="locale" onchange="selectLanguage(this)">
	<option value="en"<%if (lang.equals("en")) out.write(" selected");%>><%=rb.getString("English")%></option>
	<option value="zh"<%if (lang.equals("zh")) out.write(" selected");%>><%=rb.getString("Chinese")%></option>
	<option value="fr"<%if (lang.equals("fr")) out.write(" selected");%>><%=rb.getString("French")%></option>
	<option value="de"<%if (lang.equals("de")) out.write(" selected");%>><%=rb.getString("German")%></option>
	<option value="jp"<%if (lang.equals("jp")) out.write(" selected");%>><%=rb.getString("Japanese")%></option>
	</select>
	</td>
</tr>
<tr><td>
<ul id="gmenu">
<li class="gmenu_i"><a href="" onclick=""><%=rb.getString("home.aboutus")%></a></li>
<li class="gmenu_i"><a href="" onclick=""><%=rb.getString("home.features")%></a></li>
<li class="gmenu_i"><a href="" onclick=""><%=rb.getString("home.help")%></a></li>
<li class="gmenu_i"><a href="" onclick=""><%=rb.getString("home.stories")%></a></li>
<li class="gmenu_i"><a href="" onclick="return register()"><%=rb.getString("home.register")%></a></li>
</ul>
</td></tr>
</table>
<!-- End yikoo Header -->
