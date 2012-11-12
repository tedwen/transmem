package com.transmem.nlp;

/**
 * Implements the ISegmenter interface to break a Norwegian language sentence into separate words.
 * This program relies on the lucene analyzer/tokenizer and snowball stemmer to break the sentense.
 */
public class NorwegianSegmenter extends EnglishSegmenter
{
	public NorwegianSegmenter()
	{
		this.language_ = "Norwegian";
		//this.stopwords_ = new String[]{};
	}
}
