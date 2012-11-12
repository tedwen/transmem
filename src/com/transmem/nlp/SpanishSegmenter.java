package com.transmem.nlp;

/**
 * Implements the ISegmenter interface to break a Spanish language sentence into separate words.
 * This program relies on the lucene analyzer/tokenizer and snowball stemmer to break the sentense.
 */
public class SpanishSegmenter extends EnglishSegmenter
{
	public SpanishSegmenter()
	{
		this.language_ = "Spanish";
		//this.stopwords_ = new String[]{};
	}
}
