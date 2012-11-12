<div id="split">
 <table border="1" cellspacing="0">
 <tr><td>
  <textarea id="split1" cols="40"></textarea></td></tr>
  <tr><td>
  <textarea id="split2" cols="40"></textarea></td></tr>
  <tr><td align="center">
  <input type="button" value="OK" onclick="split(1)">
  <input type="button" value="Cancel" onclick="split(0)">
  </td>
  </tr></table>
<form id="actform" method="post" action="/transmem/tm">
    <input type="hidden" name="action" value="TranslateAction"/>
    <input type="hidden" name="op"/>
</form>
</div>
