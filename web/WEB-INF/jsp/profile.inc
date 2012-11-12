      <div class="profile">
      <table cellspacing="0">
        <tr><td nowrap><%=rb.getString("profile.name")%></td><td><%= user.getUsername() %></td></tr>
        <tr><td><%=rb.getString("profile.points")%></td><td><%= user.getPoints() %></td></tr>
        <tr><td><%=rb.getString("profile.credits")%></td><td><%= user.getCredits() %></td></tr>
        <tr><td><%=rb.getString("profile.projects")%></td><td><%= projects.size() %></td></tr>
        <tr><td></td><td></td></tr>
        <tr><td></td><td></td></tr>
        <tr><td></td><td></td></tr>
      </table>
      </div>
