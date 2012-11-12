package com.transmem.nlp;

/**
 * Implements the ISegmenter interface to break a German language sentence into separate words.
 * This program relies on the lucene analyzer/tokenizer and snowball stemmer to break the sentense.
 */
public class GermanSegmenter extends EnglishSegmenter
{
	public GermanSegmenter()
	{
		this.language_ = "German";
		//this.stopwords_ = new String[]{};
	}
}
