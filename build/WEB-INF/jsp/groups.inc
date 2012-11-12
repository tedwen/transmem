<!-- groups.inc shows a table of public groups -->
<%
	ArrayList<GroupRec> groups = mysession.getGroupList();
%>
<div id="grouplist_div">
<%=rb.getString("group.list")%><br>
<table class="group_tab">
 <tr class="group_tab_headerow">
   <th class="group_tab_th1"><%=rb.getString("group.name")%><img src="/transmem/images/sort.gif" onclick="sortGroupByName()"/></th>
   <th class="group_tab_th2"><%=rb.getString("group.members")%><img src="/transmem/images/sort.gif" onclick="sortGroupByMembers()"/></th>
   <th class="group_tab_th1"><%=rb.getString("group.date")%><img src="/transmem/images/sort.gif" onclick="sortGroupByDate()"/></th>
   <th class="group_tab_th2"><%=rb.getString("group.leader")%></th>
   <th class="group_tab_th1"><%=rb.getString("group.info")%></th>
   <th class="group_tab_th2"><%=rb.getString("group.join")%></th>
 </tr>
<%
 for (GroupRec g : groups)
 {%>
   <tr class="group_tab_row">
    <td class="group_tab_td0"><%=g.getName()%></td>
    <td class="group_tab_td2"><%=g.getMembers()%></td>
    <td class="group_tab_td1"><%=g.getDate()%></td>
    <td class="group_tab_td2"><%=g.getLeader()%></td>
    <td class="group_tab_td1"><img src="/transmem/images/info.gif" onclick="groupinfo(<%=g.getId()%>)"/></td>
    <td class="group_tab_td2"><%
    if (user.getGroup()==0 && g.getPublicity()>1) {%>
     <img src="/transmem/images/join.gif" onclick="joingroup(<%=g.getId()%>)"/><%
    } else {
     out.write("&nbsp;");
    }%></td>
   </tr>
 <%}
%>
</table>
</div>
