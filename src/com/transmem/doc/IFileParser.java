package com.transmem.doc;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Interface for document file handler classes.
 *
 * @author Ted Wen
 * @date May, 2007
 */
public interface IFileParser
{
	public void parse(String filename, ITextSaver saver) throws IOException,SQLException;
}
