package com.transmem.doc;

/**
 * Interface for saving paragraphs and sentences.
 *
 * @author Ted Wen
 * @date May, 2007
 */
public interface ITextSaver
{
	public void startParagraph(int startpos) throws java.sql.SQLException;	//save paragraph
	public void endParagraph(int endpos) throws java.sql.SQLException;
	public void saveSentence(String sentence, int startpos, int endpos) throws java.sql.SQLException;	//save sentence in a paragraph
}
