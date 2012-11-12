package com.transmem.nlp;

/**
 * Implements the ISegmenter interface to break a Danish language sentence into separate words.
 * This program relies on the lucene analyzer/tokenizer and snowball stemmer to break the sentense.
 */
public class DanishSegmenter extends EnglishSegmenter
{
	public DanishSegmenter()
	{
		this.language_ = "Danish";
		//this.stopwords_ = new String[]{};
	}
}
