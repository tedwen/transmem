package com.transmem.doc;

import java.util.logging.Logger;
import java.io.IOException;

/**
 * Factory pattern class for creation of text handlers for various file format such as .txt, .html.
 * The types are determined by file extension only.
 *
 * @author Ted Wen
 * @date May, 2007
 *
 * <p>Supported file formats:</p>
 * <ul>
 * <li>Plain text file (.txt)</li>
 * <li>HTML file (.htm, .html)</li>
 * </ul>
 */
public class FileParserFactory
{
	private static Logger log_ = Logger.getLogger("FileParserFactory");

	public static IFileParser createFileParser(String filename) throws IOException
	{
		if (filename == null || filename.equals(""))
		{
			log_.severe(".createFileParser(null)");
			throw new IOException("filename is null");
		}
		int n = filename.lastIndexOf('.');
		if (n <= 0)
		{
			log_.warning("File type "+filename+" not supported");
			throw new IOException("file type in "+filename+" not supported");
		}
		String ext = filename.substring(n).toLowerCase();
		if (ext.endsWith(".txt"))
		{
			return new TextParser();
		}
		else if (ext.endsWith(".htm") || ext.endsWith(".html"))
		{
			return new HtmlParser();
		}
		else if (ext.endsWith(".pdf"))
		{
			return new PdfParser();
		}
		else if (ext.endsWith(".rtf"))
		{
			return new RtfParser();
		}
		else if (ext.endsWith(".doc"))
		{
			return new DocParser();
		}
		else
		{
			throw new IOException("Unsupported file format for "+filename);
		}
	}
}
