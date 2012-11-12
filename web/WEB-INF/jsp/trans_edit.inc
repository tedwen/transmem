		    <%
		    String rbs_lookup = rb.getString("trans_edit.lookup");
		    String rbs_split = rb.getString("trans_edit.split");
		    String rbs_merge = rb.getString("trans_edit.merge");
		    String rbs_accept = rb.getString("trans_edit.accept");
		    String rbs_copy = rb.getString("trans_edit.copy");
		    %>
			<table width="100%" cellspacing="1" bgcolor="#FFCC99" class="translate">
		      <tr>
		        <td>
		          <div id="s_src">source sentence
		          </div>
		        </td>
		        <td width="40">
		          <img src="images/lookup.gif" id="btnlookup" TITLE="header=[<%=rbs_lookup%>] body=[]" /><br/>
		          <img src="images/split.gif" id="btnsplit" onclick="openSplit(this);" TITLE="header=[<%=rbs_split%>] body=[]"/><br/>
		          <img src="images/merge.gif" id="btnmerge" TITLE="header=[<%=rbs_merge%>] body=[]"/><br/>
		        </td>
		      </tr>
		      <tr>
		        <td>
		          <div id="s_tgt">
		            <textarea id="s_edit" rows="5" onkeypress="return checkKeys(event)">[edit]</textarea>
		          </div>
		        </td>
		        <td class="accept">
		          <img src="images/accept.gif" id="btnaccept" TITLE="header=[<%=rbs_accept%>] body=[]"/><br/><br/>
		          <img src="images/toleft.gif" id="btcopyuse" TITLE="header=[<%=rbs_copy%>] body=[]"/>
				</td>
		      </tr>
		    </table>
