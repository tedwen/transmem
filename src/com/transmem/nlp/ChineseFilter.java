package com.transmem.nlp;

import java.io.IOException;

public class ChineseFilter extends GenericFilter
{
	public static final String filepath = "/work/wen/tm/web/WEB-INF/classes/chinese-stoplist.txt";

	public ChineseFilter() throws IOException
	{
		loadStoplist(filepath);
	}
}
