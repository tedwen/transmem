package com.transmem.nlp;

/**
 * Interface for segmentation classes of various languages.
 * The only method segment will break a string of sentence into separate words as a string array.
 */
public interface ISegmenter
{
	/**
	 * Segment a sentence into separate words.
	 * @param sent - sentence as a string.
	 * @return array of strings each of which is a word in order.
	 */
	public String[] segment(String sent);
}
