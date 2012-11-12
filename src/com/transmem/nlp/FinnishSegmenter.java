package com.transmem.nlp;

/**
 * Implements the ISegmenter interface to break a Finnish language sentence into separate words.
 * This program relies on the lucene analyzer/tokenizer and snowball stemmer to break the sentense.
 */
public class FinnishSegmenter extends EnglishSegmenter
{
	public FinnishSegmenter()
	{
		this.language_ = "Finnish";
		//this.stopwords_ = new String[]{};
	}
}
