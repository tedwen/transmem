package com.transmem.nlp;

import java.util.ArrayList;

/**
 * Implements the ISegmenter interface to break an English sentence into separate words.
 * It simply breaks the sentence by spaces,punctuation characters.
 */
public class EnglishSegmenter implements ISegmenter
{
	/**
	 * Break an English sentence to a string array of words and punctuation marks with spaces removed.
	 * The recognised tokens are words (a sequence of letters) or numbers or mixed.
	 * Hyphenated words are separated as two words with the mark '-' reserved as one token in between.
	 * It can be interpreted by higher-level programs for a hyphen or minus mark.
	 *
	 * @param sent - sentence as a string
	 * @return array of strings of words and marks
	 */
	public String[] segment(String sent)
	{
		ArrayList<String> ar = new ArrayList<String>();
		char[] chars = sent.toCharArray();
		int n = chars.length;
		int i = 0;
		int x = -1;
		while (i < n)
		{
			char c = chars[i];
			if (Character.isWhitespace(c))
			{
				if (x < 0)
				{
					x = 0;
				}
				if (i > x)
				{
					//String s = sent.substring(x, i);
					//String s = new String(chars, x, i-x);  //which is faster and robust?
					ar.add(sent.substring(x, i));
				}
				x = i + 1;
			}
			else if (!Character.isLetterOrDigit(c))
			{
				if (x < 0)
				{
					x = 0;
				}
				if (i > x)
				{
					ar.add(sent.substring(x, i));
				}
				String s = String.valueOf(c);
				ar.add(s);
				x = i + 1;
			}
			i ++;
		}
		if (i > x && x >= 0)
		{
			ar.add(sent.substring(x, i));
		}
		return (String[])ar.toArray(new String[ar.size()]);
	}
}
