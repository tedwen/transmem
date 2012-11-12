<table class="extab" width="100%">
	<tr class="exr1">
		<td class="exr1c1">
			<select name="domain" onchange="changedomain(this)">
			<%
				String selectedDomain = (String)session.getAttribute("domain");
				if (selectedDomain == null) selectedDomain = "00";
				java.util.ArrayList<String> domains = (java.util.ArrayList<String>)session.getAttribute("domains");
				if (domains == null) {%>
					<option value="00"><%=rb.getString("domain.00")%></option>
				<%} else {
					for (String dms : domains) {%>
						<option value="<%=dms%>"<%
							if (selectedDomain.equals(dms)) out.write(" selected");
						%>><%=rb.getString("domain."+dms)%></option>
					<%}
				}
			%>
			</select>&nbsp;
			<select name="permit" onchange="changepermit(this)">
				<option value="P"><%=rb.getString("permit.public")%></option>
				<option value="G"><%=rb.getString("permit.group")%></option>
				<option value="O"><%=rb.getString("permit.owner")%></option>
			</select>
		</td>
		<td class="exr1c2" align="right">
			<img src="images/prev.gif" onclick="turnpage('prev')"/>
			<span id="ex_pages">0/0</span>
    		<img src="images/next.gif" onclick="turnpage('next')"/>
		</td>
	</tr>
	<tr class="exr2">
		<td class="exr2c2" colspan="2">
			<div id="examples"><%=rb.getString("trans.lookup.running")%></div>
		</td>
	</tr>
</table>
