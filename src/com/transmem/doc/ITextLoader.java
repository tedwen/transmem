package com.transmem.doc;

/**
 * Interface for text saving routines. It supports saving of paragraphs and sentences.
 * A concrete class is required to provide the interface.
 *
 * @author Ted Wen
 * @date May, 2007
 */
public interface ITextLoader
{
	public String nextSentence();
}
