package com.transmem.nlp;

/**
 * Implements the ISegmenter interface to break a Swedish language sentence into separate words.
 * This program relies on the lucene analyzer/tokenizer and snowball stemmer to break the sentense.
 */
public class SwedishSegmenter extends EnglishSegmenter
{
	public SwedishSegmenter()
	{
		this.language_ = "Swedish";
		//this.stopwords_ = new String[]{};
	}
}
