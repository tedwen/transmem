//Generated code by table2java.py, do not modify.
package com.transmem.data.db;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Paragraphs of each article
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan.2007
 */
public class Paragraphs implements java.io.Serializable
{
	protected static final long serialVersionUID = 115L;
	private Logger log_ = Logger.getLogger(Paragraphs.class.getName());

	private boolean[] setreg_ = new boolean[4];

	private Connection con_;
	private long paragraphid_; ///Global paragraph ID
	private int article_; ///FK to article id
	private int startpos_; ///starting position of paragraph in original file
	private int endpos_; ///ending position

	/**
	 * Construct an empty Paragraphs object.
	 *
	 */
	public Paragraphs()
	{
	}

	/**
	 * Construct a Paragraphs object with a Connection instance.
	 *
	 * @param con - Connection object
	 */
	public Paragraphs(Connection con)
	{
		con_ = con;
	}

	/**
	 * Construct with a query on primary key(s)
	 *
	 */
	public Paragraphs(Connection con, long _paragraphid) throws SQLException
	{
		con_ = con;
		queryByPrimaryKey(_paragraphid);
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
	 * Getter for F_ParagraphID
	 *
	 */
	public long getParagraphID()
	{
		return paragraphid_;
	}

	/**
	 * Setter for F_ParagraphID
	 *
	 */
	public void setParagraphID(long _paragraphid)
	{
		paragraphid_ = _paragraphid;
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
		setreg_[2] = true;
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
		setreg_[3] = true;
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
	 * @param _paragraphid - long
	 */
	public void queryByPrimaryKey(long _paragraphid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_Paragraphs where F_ParagraphID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setLong(1,_paragraphid);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				setParagraphID(rs.getLong(1));
				setArticle(rs.getInt(2));
				setStartPos(rs.getInt(3));
				setEndPos(rs.getInt(4));
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
	 * Query all paragraphs in an article
	 *
	 * @param article - int
	 */
	public ArrayList<Paragraphs> queryByArticle(int article) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_Paragraphs where F_Article=? order by F_ParagraphID";
		ArrayList<Paragraphs> result = new ArrayList<Paragraphs>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,article);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				Paragraphs s = new Paragraphs();
				s.setParagraphID(rs.getLong(1));
				s.setArticle(rs.getInt(2));
				s.setStartPos(rs.getInt(3));
				s.setEndPos(rs.getInt(4));
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
	 * Count number of paragraphs for an article
	 *
	 * @param article - int
	 */
	public int countParagraphs(int article) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select count(*) from T_Paragraphs where F_Article=? order by F_ParagraphID";
		int result = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,article);
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
		String sql = "select * from T_Paragraphs where F_ParagraphID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setLong(1, paragraphid_);
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();
			rs.moveToInsertRow();
			if (setreg_[0]) rs.updateLong(1,paragraphid_);
			if (setreg_[1]) rs.updateInt(2,article_);
			if (setreg_[2]) rs.updateInt(3,startpos_);
			if (setreg_[3]) rs.updateInt(4,endpos_);
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
		String sql = "select * from T_Paragraphs where F_ParagraphID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setLong(1, paragraphid_);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				if (setreg_[1]) rs.updateInt(2,article_);
				if (setreg_[2]) rs.updateInt(3,startpos_);
				if (setreg_[3]) rs.updateInt(4,endpos_);
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
	 * Delete record by primary key(s).
	 *
	 * @param _paragraphid - long
	 */
	public void delete(long _paragraphid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "delete from T_Paragraphs where F_ParagraphID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setLong(1,_paragraphid);
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
		String sql = "delete from T_Paragraphs where F_ParagraphID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setLong(1,paragraphid_);
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
