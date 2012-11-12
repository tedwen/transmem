package com.transmem.doc;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Base class for all document file handlers.
 *
 * @author Ted Wen
 * @date May, 2007
 */
public abstract class FileParser implements IFileParser
{
	public abstract void parse(String filename, ITextSaver saver) throws IOException,SQLException;

	/**
	 * Check whether the character in sentence at pos is a stop mark.
	 * Only English full stop and Chinese stop are checked here.
	 * @param sentence - the sentence text
	 * @param c - character at position pos
	 * @param pos - index of c in sentence
	 * @return true if this is a stop of a sentence
	 */
	protected boolean isFullStop(String sentence, char c, int pos)
	{
		if (c == '.')
		{
			int pn = pos-1;
			while (pn > 0 && sentence.charAt(pn)!=' ') pn--;
			String s = sentence.substring(pn, pos);
			if (s.equalsIgnoreCase(" Mr") || s.equalsIgnoreCase(" Mrs") || s.equalsIgnoreCase(" Dr") ||
				s.equalsIgnoreCase(" Ms"))
				return false;
			int nn = pos+1;
			while (nn < sentence.length() && Character.isWhitespace(sentence.charAt(nn))) nn++;
			if (!Character.isLowerCase(sentence.charAt(nn)))
				return true;
			return false;
		}
		else if (c=='¡£' || c=='£®')
		{
			return true;
		}
		return false;
	}
	
	protected boolean isFullStop(StringBuffer sb, char[] buf, int i)
	{
		return false;
	}
}
