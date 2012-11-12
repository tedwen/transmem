package com.transmem.nlp;

/**
 * Implements the ISegmenter interface to break a Dutch language sentence into separate words.
 * This program relies on the lucene analyzer/tokenizer and snowball stemmer to break the sentense.
 */
public class DutchSegmenter extends EnglishSegmenter
{
	public DutchSegmenter()
	{
		this.language_ = "Dutch";
		//this.stopwords_ = new String[]{};
	}
}
