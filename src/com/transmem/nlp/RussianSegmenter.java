package com.transmem.nlp;

/**
 * Implements the ISegmenter interface to break a Russian language sentence into separate words.
 * This program relies on the lucene analyzer/tokenizer and snowball stemmer to break the sentense.
 */
public class RussianSegmenter extends EnglishSegmenter
{
	public RussianSegmenter()
	{
		this.language_ = "Russian";
		//this.stopwords_ = new String[]{};
	}
}
