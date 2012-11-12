package com.transmem.nlp;

import java.io.IOException;

public class EnglishFilter extends GenericFilter
{
	public static final String filepath = "/work/wen/tm/web/WEB-INF/classes/english-stoplist.txt";
	public static final String marks = ".,;!'\"/\\?#%^&*()+=_[]{}";

	public EnglishFilter() throws IOException
	{
		loadStoplist(filepath);
		setPunctuationMarks(marks);
	}
}
