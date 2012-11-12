//Generated code by table2java.py, do not modify.
package com.transmem.data.db;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Uploaded articles of all users. It can be a standalone article, or a chapter of a book.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan.2007
 */
public class Articles implements java.io.Serializable
{
	protected static final long serialVersionUID = 114L;
	private Logger log_ = Logger.getLogger(Articles.class.getName());

	private boolean[] setreg_ = new boolean[12];

	private Connection con_;
	private int articleid_; ///ID of article as a long
	private int project_; ///Project ID in T_Projects
	private String title_; ///article title
	private int sentences_; ///number of sentences in the article
	private String langpair_; ///Language pair for the translation
	private int translator_; ///who is working on this article
	private java.sql.Date uploaddate_; ///upload date
	private int paragraph_; ///the paragraph index (not id) currently worked on
	private java.sql.Date startdate_; ///when to start translation
	private float progress_; ///percent done,0.0=unstarted, 1.0 = Finished
	private String fileformat_; ///file format
	private String filename_; ///uploaded filename

	/**
	 * Construct an empty Articles object.
	 *
	 */
	public Articles()
	{
	}

	/**
	 * Construct a Articles object with a Connection instance.
	 *
	 * @param con - Connection object
	 */
	public Articles(Connection con)
	{
		con_ = con;
	}

	/**
	 * Construct with a query on primary key(s)
	 *
	 */
	public Articles(Connection con, int _articleid) throws SQLException
	{
		con_ = con;
		queryByPrimaryKey(_articleid);
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
	 * Getter for F_ArticleID
	 *
	 */
	public int getArticleID()
	{
		return articleid_;
	}

	/**
	 * Setter for F_ArticleID
	 *
	 */
	public void setArticleID(int _articleid)
	{
		articleid_ = _articleid;
		setreg_[0] = true;
	}

	/**
	 * Getter for F_Project
	 *
	 */
	public int getProject()
	{
		return project_;
	}

	/**
	 * Setter for F_Project
	 *
	 */
	public void setProject(int _project)
	{
		project_ = _project;
		setreg_[1] = true;
	}

	/**
	 * Getter for F_Title
	 *
	 */
	public String getTitle()
	{
		return title_;
	}

	/**
	 * Setter for F_Title
	 *
	 */
	public void setTitle(String _title)
	{
		title_ = _title;
		setreg_[2] = true;
	}

	/**
	 * Getter for F_Sentences
	 *
	 */
	public int getSentences()
	{
		return sentences_;
	}

	/**
	 * Setter for F_Sentences
	 *
	 */
	public void setSentences(int _sentences)
	{
		sentences_ = _sentences;
		setreg_[3] = true;
	}

	/**
	 * Getter for F_LangPair
	 *
	 */
	public String getLangPair()
	{
		return langpair_;
	}

	/**
	 * Setter for F_LangPair
	 *
	 */
	public void setLangPair(String _langpair)
	{
		langpair_ = _langpair;
		setreg_[4] = true;
	}

	/**
	 * Getter for F_Translator
	 *
	 */
	public int getTranslator()
	{
		return translator_;
	}

	/**
	 * Setter for F_Translator
	 *
	 */
	public void setTranslator(int _translator)
	{
		translator_ = _translator;
		setreg_[5] = true;
	}

	/**
	 * Getter for F_UploadDate
	 *
	 */
	public java.sql.Date getUploadDate()
	{
		return uploaddate_;
	}

	/**
	 * Setter for F_UploadDate
	 *
	 */
	public void setUploadDate(java.sql.Date _uploaddate)
	{
		uploaddate_ = _uploaddate;
		setreg_[6] = true;
	}

	/**
	 * Getter for F_Paragraph
	 *
	 */
	public int getParagraph()
	{
		return paragraph_;
	}

	/**
	 * Setter for F_Paragraph
	 *
	 */
	public void setParagraph(int _paragraph)
	{
		paragraph_ = _paragraph;
		setreg_[7] = true;
	}

	/**
	 * Getter for F_StartDate
	 *
	 */
	public java.sql.Date getStartDate()
	{
		return startdate_;
	}

	/**
	 * Setter for F_StartDate
	 *
	 */
	public void setStartDate(java.sql.Date _startdate)
	{
		startdate_ = _startdate;
		setreg_[8] = true;
	}

	/**
	 * Getter for F_Progress
	 *
	 */
	public float getProgress()
	{
		return progress_;
	}

	/**
	 * Setter for F_Progress
	 *
	 */
	public void setProgress(float _progress)
	{
		progress_ = _progress;
		setreg_[9] = true;
	}

	/**
	 * Getter for F_FileFormat
	 *
	 */
	public String getFileFormat()
	{
		return fileformat_;
	}

	/**
	 * Setter for F_FileFormat
	 *
	 */
	public void setFileFormat(String _fileformat)
	{
		fileformat_ = _fileformat;
		setreg_[10] = true;
	}

	/**
	 * Getter for F_Filename
	 *
	 */
	public String getFilename()
	{
		return filename_;
	}

	/**
	 * Setter for F_Filename
	 *
	 */
	public void setFilename(String _filename)
	{
		filename_ = _filename;
		setreg_[11] = true;
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
	 * @param _articleid - int
	 */
	public void queryByPrimaryKey(int _articleid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_Articles where F_ArticleID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,_articleid);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				setArticleID(rs.getInt(1));
				setProject(rs.getInt(2));
				setTitle(rs.getString(3));
				setSentences(rs.getInt(4));
				setLangPair(rs.getString(5));
				setTranslator(rs.getInt(6));
				setUploadDate(rs.getDate(7));
				setParagraph(rs.getInt(8));
				setStartDate(rs.getDate(9));
				setProgress(rs.getFloat(10));
				setFileFormat(rs.getString(11));
				setFilename(rs.getString(12));
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
	 * 
	 *
	 * @param pid - int
	 */
	public ArrayList<Articles> queryByProject(int pid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_Articles where F_Project=? order by F_ArticleID";
		ArrayList<Articles> result = new ArrayList<Articles>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,pid);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				Articles s = new Articles();
				s.setArticleID(rs.getInt(1));
				s.setProject(rs.getInt(2));
				s.setTitle(rs.getString(3));
				s.setSentences(rs.getInt(4));
				s.setLangPair(rs.getString(5));
				s.setTranslator(rs.getInt(6));
				s.setUploadDate(rs.getDate(7));
				s.setParagraph(rs.getInt(8));
				s.setStartDate(rs.getDate(9));
				s.setProgress(rs.getFloat(10));
				s.setFileFormat(rs.getString(11));
				s.setFilename(rs.getString(12));
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
	 * 
	 *
	 * @param uid - int
	 */
	public ArrayList<Articles> queryByTranslator(int uid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_Articles where F_Translator=? order by F_ArticleID";
		ArrayList<Articles> result = new ArrayList<Articles>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,uid);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				Articles s = new Articles();
				s.setArticleID(rs.getInt(1));
				s.setProject(rs.getInt(2));
				s.setTitle(rs.getString(3));
				s.setSentences(rs.getInt(4));
				s.setLangPair(rs.getString(5));
				s.setTranslator(rs.getInt(6));
				s.setUploadDate(rs.getDate(7));
				s.setParagraph(rs.getInt(8));
				s.setStartDate(rs.getDate(9));
				s.setProgress(rs.getFloat(10));
				s.setFileFormat(rs.getString(11));
				s.setFilename(rs.getString(12));
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
	 * Count the number of sentences for this article
	 *
	 * @param aid - int
	 */
	public int countSentences(int aid) throws SQLException
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
	 * Count the number of translated sentences for this article
	 *
	 * @param aid - int
	 */
	public int countTranslations(int aid) throws SQLException
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
		String sql = "select * from T_Articles where F_ArticleID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setInt(1, articleid_);
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();
			rs.moveToInsertRow();
			if (setreg_[0]) rs.updateInt(1,articleid_);
			if (setreg_[1]) rs.updateInt(2,project_);
			if (setreg_[2]) rs.updateString(3,title_);
			if (setreg_[3]) rs.updateInt(4,sentences_);
			if (setreg_[4]) rs.updateString(5,langpair_);
			if (setreg_[5]) rs.updateInt(6,translator_);
			if (setreg_[6]) rs.updateDate(7,uploaddate_);
			if (setreg_[7]) rs.updateInt(8,paragraph_);
			if (setreg_[8]) rs.updateDate(9,startdate_);
			if (setreg_[9]) rs.updateFloat(10,progress_);
			if (setreg_[10]) rs.updateString(11,fileformat_);
			if (setreg_[11]) rs.updateString(12,filename_);
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
		String sql = "select * from T_Articles where F_ArticleID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setInt(1, articleid_);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				if (setreg_[1]) rs.updateInt(2,project_);
				if (setreg_[2]) rs.updateString(3,title_);
				if (setreg_[3]) rs.updateInt(4,sentences_);
				if (setreg_[4]) rs.updateString(5,langpair_);
				if (setreg_[5]) rs.updateInt(6,translator_);
				if (setreg_[6]) rs.updateDate(7,uploaddate_);
				if (setreg_[7]) rs.updateInt(8,paragraph_);
				if (setreg_[8]) rs.updateDate(9,startdate_);
				if (setreg_[9]) rs.updateFloat(10,progress_);
				if (setreg_[10]) rs.updateString(11,fileformat_);
				if (setreg_[11]) rs.updateString(12,filename_);
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
	 * Update the F_Paragraph field
	 *
	 * @param aid - int
	 * @param paraid - long
	 */
	public void updateParagraph(int aid, long paraid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "update T_Articles set F_Paragraph=? where F_ArticleID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setLong(1,paraid);
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
	 * Update the F_Sentences field by counting sentences in T_Sentences table
	 *
	 * @param aid - int
	 */
	public void updateSentences(int aid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "update T_Articles set F_Sentences=(select count(*) from T_Sentences where F_Article=?) where F_ArticleID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,aid);
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
	 * @param _articleid - int
	 */
	public void delete(int _articleid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "delete from T_Articles where F_ArticleID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,_articleid);
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
		String sql = "delete from T_Articles where F_ArticleID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,articleid_);
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
