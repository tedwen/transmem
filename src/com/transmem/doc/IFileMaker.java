package com.transmem.doc;

/**
 * Interface for classes to generate a type of document.
 *
 * @author Ted Wen
 * @date May, 2007
 */
public interface IFileMaker
{
	public void generate(String filename, ITextLoader loader);
}
