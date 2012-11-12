package com.transmem.nlp;

/**
 * Implements the ISegmenter interface to break a French language sentence into separate words.
 * This program relies on the lucene analyzer/tokenizer and snowball stemmer to break the sentense.
 */
public class FrenchSegmenter extends EnglishSegmenter
{
	public FrenchSegmenter()
	{
		this.language_ = "French";
		//this.stopwords_ = new String[]{};
	}
}
