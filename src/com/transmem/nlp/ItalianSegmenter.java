package com.transmem.nlp;

/**
 * Implements the ISegmenter interface to break a Italian language sentence into separate words.
 * This program relies on the lucene analyzer/tokenizer and snowball stemmer to break the sentense.
 */
public class ItalianSegmenter extends EnglishSegmenter
{
	public ItalianSegmenter()
	{
		this.language_ = "Italian";
		//this.stopwords_ = new String[]{};
	}
}
