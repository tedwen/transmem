package com.transmem.nlp;

/**
 * Interface for stemmer implementations of various languages.
 */
public interface IStemmer
{
	/**
	 * Test whether this language requires stemming.
	 * @return true if yes.
	 */
	public boolean applies();
	
	/**
	 * Find the stem of the given word.
	 * @param word - word in a sentence.
	 * @return stem of the word.
	 */
	public String stem(String word);
}
