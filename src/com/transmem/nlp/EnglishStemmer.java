package com.transmem.nlp;

import java.util.Hashtable;

/**
 * A stemmer for the English language words.
 *
 * @author Ted Wen
 * @version 0.1
 * @update 15/Jan/2007
 */
public class EnglishStemmer implements IStemmer
{
	private	boolean useIrregulars_;
	private	Hashtable<String,String> irregulars;

	public EnglishStemmer()
	{
		loadIrregulars();
	}

	/**
	 * Always returns true for EnglishStemmer
	 */
	public boolean applies()
	{
		return true;
	}
	
	/**
	 * Set or reset use_irregular words switch.
	 */
	public void useIrregularTable(boolean set)
	{
		this.useIrregulars_ = set;
	}

	/**
	 * Makes the stem of a word. If it's a irregular word, then get the stem from a hashtable.
	 * If word is null or empty, return "".
	 */
	public String stem(String word)
	{
		if (word != null && word.length() > 0)
		{
			word = word.toLowerCase();
			String s = null;
			if (this.useIrregulars_)
			{
				s = this.irregulars.get(word);
				if (s != null)
				{
					return s;
				}
			}
			s = PorterStemmer.stem(word);
			return s;
		}
		else
		{
			return "";
		}
	}

	/**
	 * Load irregular words into hashtable for fast query.
	 */
	private void loadIrregulars()
	{
		String[] irregular_words = {
			"took","take",
			"taken","take",
		};
		int n = irregular_words.length / 2;
		this.irregulars = new Hashtable<String,String>(n);
		for (int i=0; i<n; i+=2)
		{
			this.irregulars.put(irregular_words[i],irregular_words[i+1]);
		}
	}
}
