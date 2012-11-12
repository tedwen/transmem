package com.transmem.nlp;

/**
 * A dummy stemmer for the Chinese language words.
 * The Chinese words do not need stemming, so this class should not
 * be used.  In case it is used, the applies() method always returns
 * false meaning that the stemming is not applicable.  If the stem
 * method is called anyway, it returns the word without change.
 *
 * @author Ted Wen
 * @version 0.1
 * @update 15/Jan/2007
 */
public class ChineseStemmer implements IStemmer
{
	public ChineseStemmer()
	{
	}

	public boolean applies()
	{
		return false;
	}

	public String stem(String word)
	{
		return word;
	}
}
