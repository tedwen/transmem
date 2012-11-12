<script type="text/javascript">
var g_numparas = <%=(String)session.getAttribute("numparas")%>;
var g_thispara = <%=(String)session.getAttribute("thispara")%>;
<%
	java.util.ArrayList<com.transmem.data.db.Sentences> ss = (java.util.ArrayList<com.transmem.data.db.Sentences>)session.getAttribute("sentences");
	out.write("var g_sentences = [");
	//out.write("\""+ss.get(0).getSentence()+"\"");
	boolean first = true;
	for (int i=0; i<ss.size(); i++) {
		String st = ss.get(i).getSentence();
		if (st != null) {
			if (st.indexOf('"')>=0)
				st = st.replace('"','\'');
			if (first)
				first = false;
			else
				out.write(',');
			out.write('"');
			out.write(st);
			out.write('"');
		}
		//out.write(",\""+ss.get(i).getSentence()+"\"");
	}
	out.write("];\n");
	out.write("var g_translations = [");
	String s = ss.get(0).getTranslation();
	if (s == null) s = "";
	//out.write("\""+s+"\"");
	out.print("\""+s+"\"");
	for (int i=1; i<ss.size(); i++) {
		s = ss.get(i).getTranslation();
		if (s == null) s = "";
		out.write(",\""+s+"\"");
	}
	out.write("];\n");
	String langpair = (String)session.getAttribute("langpair");
	if (langpair.equals("ENZH"))
		out.write("var g_separator = \"\";\n");
	else if (langpair.equals("ZHEN"))
		out.write("var g_separator = \" \";\n");
%>
</script>
