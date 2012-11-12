<br>
<div id="tabs_s">
<form id="link_form" method="post" action="/transmem/tm">
   	<input type="hidden" name="action" />
    <input type="hidden" name="page" />
    <table border="0" width="100%" cellspacing="0" cellspadding="0">
    	<tr>
    		<td>
<ul id="tabs">
<li id="tab_1"><a href="" onclick="return linkpage('projects')"><%=rb.getString("links.inc.project")%></a></li>
<li id="tab_2"><a href="" onclick="return linkpage('articles')"><%=rb.getString("links.inc.article")%></a></li>
</ul>
			</td>
			<td align="right">
			<%
				Integer Layout = (Integer)session.getAttribute("translayout");
				int layout = 1;
				if (Layout != null)
					layout = Layout.intValue();
				String sinput = "<input type=\"radio\" name=\"layout\" value=";
				String sclick = " onclick=\"changelayout()\">";
				for (int i=0; i<3; i++) {
					out.print(sinput);
					out.print(i);
					if (layout == i)
						out.print(" disabled >");
					else
						out.print(sclick);
					out.print(rb.getString("trans.template"));
					out.print(i+1);
					out.print("</input>");
				}
			%>
			</td>
    	</tr>
    </table>
</form>
</div>
