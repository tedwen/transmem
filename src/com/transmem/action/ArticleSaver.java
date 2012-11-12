package com.transmem.action;

import com.transmem.doc.ITextSaver;
import com.transmem.data.db.Paragraphs;
import com.transmem.data.db.Sentences;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * ArticleSaver implements the ITextSaver interface for saving paragraphs and sentences.
 * The save methods in this class are callback functions to be called by IFileParser objects.
 *
 * @author Ted Wen
 * @date April, 2007
 */
public class ArticleSaver implements ITextSaver
{
	private static Logger log_ = Logger.getLogger(ArticleSaver.class.getName());

	private Connection con_;
	private	int artid_, parapos_, sentcount_;
	private	long paraid_, sentid_;
	private	float seq_;

	public ArticleSaver(Connection con, int artid)
	{
		if (con == null)
		{
			throw new IllegalArgumentException("Connection is null");
		}
		this.con_ = con;
		this.artid_ = artid;
	}

	/**
	 * Get next value from a sequence.
	 * @param seq - sequence name in the DBMS
	 * @return a long value from the next value in the sequence
	 */
	protected long getNextSequenceValue(String seq) throws SQLException
	{
		String sql = "SELECT nextval('"+seq+"')";	//e.g. S_ENZH
		java.sql.Statement stmt = this.con_.createStatement();
		java.sql.ResultSet rs = stmt.executeQuery(sql);
		long sid = 0;
		if (rs.next())
			sid = rs.getLong(1);
		rs.close();
		stmt.close();
		return sid;
	}

	/**
	 * Start a paragraph at the given position, without knowing the end yet.
	 * The starting position is kept until endParagraph is called.
	 * The paragraph ID which is a long sequence value is obtained here and
	 * will be used for all sentences to be saved hereafter.
	 *
	 * @param parastart - starting position of this paragraph in the file.
	 */
	public void startParagraph(int parastart) throws SQLException
	{
		this.paraid_ = getNextSequenceValue("S_Paragraphs");
		this.parapos_ = parastart;
		this.seq_ = 0;
		this.sentcount_ = 0;
	}

	/**
	 * End a paragraph and actually save the paragraph to the data base table.
	 *
 	 * @param paraend - ending position of this paragraph in the file.
	 */
	public void endParagraph(int paraend) throws SQLException
	{
		if (this.paraid_ <= 0)
		{
			log_.warning("endParagraph called before startParagraph");
			return;
		}
		if (this.sentcount_ <= 0)
		{
			log_.warning("Empty paragraph, not saved");
			return;
		}
		Paragraphs para = new Paragraphs(this.con_);
		para.setParagraphID(this.paraid_);
		para.setArticle(this.artid_);
		para.setStartPos(this.parapos_);
		para.setEndPos(paraend);
		para.insert();
		this.paraid_ = 0;
		this.sentcount_ = 0;
		log_.info("<p start="+paraid_+" end="+paraend+">");
	}

	/**
	 * Save a sentence into the database table T_Sentences.
	 * @param sent - string of the sentence
	 * @param start - starting position of the sentence in the file
	 * @param end - ending position of the sentence in the file
	 */
	public void saveSentence(String sent, int start, int end) throws SQLException
	{
		if (this.paraid_ <= 0)
		{
			startParagraph(start);
		}
		long sid = getNextSequenceValue("S_Sentences");
		Sentences sts = new Sentences(this.con_);
		sts.setSentenceID(sid);
		sts.setArticle(this.artid_);
		sts.setParagraph(this.paraid_);
		sts.setSequence(this.seq_);
		this.seq_ += 1.0;
		sts.setSentence(sent);
		sts.setStartPos(start);
		sts.setEndPos(end);
		sts.insert();
		log_.info("<s start="+start+" end="+end+">"+sent+"</s>");
		this.sentcount_ ++;
	}
}
