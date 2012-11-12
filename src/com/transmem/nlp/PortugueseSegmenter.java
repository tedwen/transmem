package com.transmem.nlp;

/**
 * Implements the ISegmenter interface to break a Portuguese language sentence into separate words.
 * This program relies on the lucene analyzer/tokenizer and snowball stemmer to break the sentense.
 */
public class PortugueseSegmenter extends EnglishSegmenter
{
	public PortugueseSegmenter()
	{
		this.language_ = "Portuguese";
		//this.stopwords_ = new String[]{};
	}
}
