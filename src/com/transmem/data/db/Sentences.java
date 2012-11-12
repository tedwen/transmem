//Generated code by table2java.py, do not modify.
package com.transmem.data.db;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Sentences of each article
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan.2007
 */
public class Sentences implements java.io.Serializable
{
	protected static final long serialVersionUID = 116L;
	private Logger log_ = Logger.getLogger(Sentences.class.getName());

	private boolean[] setreg_ = new boolean[8];

	private Connection con_;
	private long sentenceid_; ///PK ID
	private int article_; ///FK to article id
	private long paragraph_; ///FK to paragraph
	private float sequence_; ///order of sentence, 1,1.5,2,3..
	private int startpos_; ///starting position of sentence in original file
	private int endpos_; ///ending position
	private String sentence_; ///
	private String translation_; ///

	/**
	 * Construct an empty Sentences object.
	 *
	 */
	public Sentences()
	{
	}

	/**
	 * Construct a Sentences object with a Connection instance.
	 *
	 * @param con - Connection object
	 */
	public Sentences(Connection con)
	{
		con_ = con;
	}

	/**
	 * Construct with a query on primary key(s)
	 *
	 */
	public Sentences(Connection con, long _sentenceid) throws SQLException
	{
		con_ = con;
		queryByPrimaryKey(_sentenceid);
	}

	/**
	 * Setter for Connection object
	 *
	 */
	public void setConnection(Connection con)
	{
		this.con_ = con;
	}

	/**
	 * Getter for F_SentenceID
	 *
	 */
	public long getSentenceID()
	{
		return sentenceid_;
	}

	/**
	 * Setter for F_SentenceID
	 *
	 */
	public void setSentenceID(long _sentenceid)
	{
		sentenceid_ = _sentenceid;
		setreg_[0] = true;
	}

	/**
	 * Getter for F_Article
	 *
	 */
	public int getArticle()
	{
		return article_;
	}

	/**
	 * Setter for F_Article
	 *
	 */
	public void setArticle(int _article)
	{
		article_ = _article;
		setreg_[1] = true;
	}

	/**
	 * Getter for F_Paragraph
	 *
	 */
	public long getParagraph()
	{
		return paragraph_;
	}

	/**
	 * Setter for F_Paragraph
	 *
	 */
	public void setParagraph(long _paragraph)
	{
		paragraph_ = _paragraph;
		setreg_[2] = true;
	}

	/**
	 * Getter for F_Sequence
	 *
	 */
	public float getSequence()
	{
		return sequence_;
	}

	/**
	 * Setter for F_Sequence
	 *
	 */
	public void setSequence(float _sequence)
	{
		sequence_ = _sequence;
		setreg_[3] = true;
	}

	/**
	 * Getter for F_StartPos
	 *
	 */
	public int getStartPos()
	{
		return startpos_;
	}

	/**
	 * Setter for F_StartPos
	 *
	 */
	public void setStartPos(int _startpos)
	{
		startpos_ = _startpos;
		setreg_[4] = true;
	}

	/**
	 * Getter for F_EndPos
	 *
	 */
	public int getEndPos()
	{
		return endpos_;
	}

	/**
	 * Setter for F_EndPos
	 *
	 */
	public void setEndPos(int _endpos)
	{
		endpos_ = _endpos;
		setreg_[5] = true;
	}

	/**
	 * Getter for F_Sentence
	 *
	 */
	public String getSentence()
	{
		return sentence_;
	}

	/**
	 * Setter for F_Sentence
	 *
	 */
	public void setSentence(String _sentence)
	{
		sentence_ = _sentence;
		setreg_[6] = true;
	}

	/**
	 * Getter for F_Translation
	 *
	 */
	public String getTranslation()
	{
		return translation_;
	}

	/**
	 * Setter for F_Translation
	 *
	 */
	public void setTranslation(String _translation)
	{
		translation_ = _translation;
		setreg_[7] = true;
	}

	/**
	 * Clear all update tags
	 *
	 */
	public void clearUpdates()
	{
		for (int i=0; i<setreg_.length; i++)
		{
			setreg_[i] = false;
		}
	}

	/**
	 * Query by primary key(s), and populate current instance.
	 *
	 * @param _sentenceid - long
	 */
	public void queryByPrimaryKey(long _sentenceid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_Sentences where F_SentenceID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setLong(1,_sentenceid);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				setSentenceID(rs.getLong(1));
				setArticle(rs.getInt(2));
				setParagraph(rs.getLong(3));
				setSequence(rs.getFloat(4));
				setStartPos(rs.getInt(5));
				setEndPos(rs.getInt(6));
				setSentence(rs.getString(7));
				setTranslation(rs.getString(8));
			}
		}
		catch (SQLException e)
		{
			log_.severe(e.toString());
			throw e;
		}
		finally
		{
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			}
			catch (SQLException x)
			{
			}
		}
	}

	/**
	 * Query all sentences in an article
	 *
	 * @param article - int
	 */
	public ArrayList<Sentences> queryByArticle(int article) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_Sentences where F_Article=? order by F_Sequence";
		ArrayList<Sentences> result = new ArrayList<Sentences>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,article);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				Sentences s = new Sentences();
				s.setSentenceID(rs.getLong(1));
				s.setArticle(rs.getInt(2));
				s.setParagraph(rs.getLong(3));
				s.setSequence(rs.getFloat(4));
				s.setStartPos(rs.getInt(5));
				s.setEndPos(rs.getInt(6));
				s.setSentence(rs.getString(7));
				s.setTranslation(rs.getString(8));
				result.add(s);
			}
		}
		catch (SQLException e)
		{
			log_.severe(e.toString());
		}
		finally
		{
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			}
			catch (SQLException x)
			{
			}
		}
		return result;
	}

	/**
	 * Query all sentences of a paragraph in an article
	 *
	 * @param para - long
	 */
	public ArrayList<Sentences> queryByParagraph(long para) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_Sentences where F_Paragraph=? order by F_Sequence";
		ArrayList<Sentences> result = new ArrayList<Sentences>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setLong(1,para);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				Sentences s = new Sentences();
				s.setSentenceID(rs.getLong(1));
				s.setArticle(rs.getInt(2));
				s.setParagraph(rs.getLong(3));
				s.setSequence(rs.getFloat(4));
				s.setStartPos(rs.getInt(5));
				s.setEndPos(rs.getInt(6));
				s.setSentence(rs.getString(7));
				s.setTranslation(rs.getString(8));
				result.add(s);
			}
		}
		catch (SQLException e)
		{
			log_.severe(e.toString());
		}
		finally
		{
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			}
			catch (SQLException x)
			{
			}
		}
		return result;
	}

	/**
	 * Count the number of sentences for an article
	 *
	 * @param aid - int
	 */
	public int countByArticle(int aid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select count(*) from T_Sentences where F_Article=?";
		int result = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,aid);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				result = rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			log_.severe(e.toString());
		}
		finally
		{
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			}
			catch (SQLException x)
			{
			}
		}
		return result;
	}

	/**
	 * Count the number of translated sentences for an article
	 *
	 * @param aid - int
	 */
	public int countTranslationsByArticle(int aid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select count(*) from T_Sentences where F_Article=? and F_Translation is not null";
		int result = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,aid);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				result = rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			log_.severe(e.toString());
		}
		finally
		{
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			}
			catch (SQLException x)
			{
			}
		}
		return result;
	}

	/**
	 * Insert record in table with current data object.
	 *
	 */
	public void insert(Connection con) throws SQLException
	{
		String sql = "select * from T_Sentences where F_SentenceID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setLong(1, sentenceid_);
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();
			rs.moveToInsertRow();
			if (setreg_[0]) rs.updateLong(1,sentenceid_);
			if (setreg_[1]) rs.updateInt(2,article_);
			if (setreg_[2]) rs.updateLong(3,paragraph_);
			if (setreg_[3]) rs.updateFloat(4,sequence_);
			if (setreg_[4]) rs.updateInt(5,startpos_);
			if (setreg_[5]) rs.updateInt(6,endpos_);
			if (setreg_[6]) rs.updateString(7,sentence_);
			if (setreg_[7]) rs.updateString(8,translation_);
			rs.insertRow();
		}
		catch (SQLException e)
		{
			log_.severe(e.toString());
			throw e;
		}
		finally
		{
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			}
			catch (SQLException x)
			{
			}
		}
	}

	public void insert() throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		insert(this.con_);
	}

	/**
	 * Update record in table with current data object.
	 * All fields must be set, better query in first.
	 *
	 */
	public void update(Connection con) throws SQLException
	{
		String sql = "select * from T_Sentences where F_SentenceID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setLong(1, sentenceid_);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				if (setreg_[1]) rs.updateInt(2,article_);
				if (setreg_[2]) rs.updateLong(3,paragraph_);
				if (setreg_[3]) rs.updateFloat(4,sequence_);
				if (setreg_[4]) rs.updateInt(5,startpos_);
				if (setreg_[5]) rs.updateInt(6,endpos_);
				if (setreg_[7]) rs.updateString(8,translation_);
				rs.updateRow();
			}
		}
		catch (SQLException e)
		{
			log_.severe(e.toString());
			throw e;
		}
		finally
		{
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			}
			catch (SQLException x)
			{
			}
		}
	}

	public void update() throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		update(this.con_);
	}

	/**
	 * Update the T_Articles.F_Progress
	 *
	 * @param aid - int
	 * @param progress - float
	 */
	public void updateArticleProgress(int aid, float progress) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "update T_Articles set F_Progress=? where F_ArticleID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setFloat(1,progress);
			stmt.setInt(2,aid);
			stmt.executeUpdate();
		}
		catch (SQLException e)
		{
			log_.severe(e.toString());
		}
		finally
		{
			try {
				if (stmt != null) stmt.close();
			}
			catch (SQLException x)
			{
			}
		}
	}

	/**
	 * Delete record by primary key(s).
	 *
	 * @param _sentenceid - long
	 */
	public void delete(long _sentenceid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "delete from T_Sentences where F_SentenceID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setLong(1,_sentenceid);
			stmt.executeUpdate();
		}
		catch (SQLException e)
		{
			log_.severe(e.toString());
			throw e;
		}
		finally
		{
			try {
				if (stmt != null) stmt.close();
			}
			catch (SQLException x)
			{
			}
		}
	}

	/**
	 * Delete current record by PK with a connection.
	 *
	 */
	public void delete(Connection _con) throws SQLException
	{
		String sql = "delete from T_Sentences where F_SentenceID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setLong(1,sentenceid_);
			stmt.executeUpdate();
		}
		catch (SQLException e)
		{
			log_.severe(e.toString());
			throw e;
		}
		finally
		{
			try {
				if (stmt != null) stmt.close();
			}
			catch (SQLException x)
			{
			}
		}
	}

}
