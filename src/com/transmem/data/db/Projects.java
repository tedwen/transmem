//Generated code by table2java.py, do not modify.
package com.transmem.data.db;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * A translation project that can involve a single article or
 * a book consisting of several articles to be translated by several people.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan.2007
 */
public class Projects implements java.io.Serializable
{
	protected static final long serialVersionUID = 113L;
	private Logger log_ = Logger.getLogger(Projects.class.getName());

	private boolean[] setreg_ = new boolean[7];

	private Connection con_;
	private int projectid_; ///Auto project id
	private String projectname_; ///
	private String langpair_; ///Language pair for the translation
	private int creator_; ///FK to T_Users
	private java.sql.Date createdate_; ///when this project is created
	private float progress_; ///percent done,0.0=unstarted, 1.0 = Finished
	private int articles_; ///number of articles in this project

	/**
	 * Construct an empty Projects object.
	 *
	 */
	public Projects()
	{
	}

	/**
	 * Construct a Projects object with a Connection instance.
	 *
	 * @param con - Connection object
	 */
	public Projects(Connection con)
	{
		con_ = con;
	}

	/**
	 * Construct with a query on primary key(s)
	 *
	 */
	public Projects(Connection con, int _projectid) throws SQLException
	{
		con_ = con;
		queryByPrimaryKey(_projectid);
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
	 * Getter for F_ProjectID
	 *
	 */
	public int getProjectID()
	{
		return projectid_;
	}

	/**
	 * Setter for F_ProjectID
	 *
	 */
	public void setProjectID(int _projectid)
	{
		projectid_ = _projectid;
		setreg_[0] = true;
	}

	/**
	 * Getter for F_ProjectName
	 *
	 */
	public String getProjectName()
	{
		return projectname_;
	}

	/**
	 * Setter for F_ProjectName
	 *
	 */
	public void setProjectName(String _projectname)
	{
		projectname_ = _projectname;
		setreg_[1] = true;
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
		setreg_[2] = true;
	}

	/**
	 * Getter for F_Creator
	 *
	 */
	public int getCreator()
	{
		return creator_;
	}

	/**
	 * Setter for F_Creator
	 *
	 */
	public void setCreator(int _creator)
	{
		creator_ = _creator;
		setreg_[3] = true;
	}

	/**
	 * Getter for F_CreateDate
	 *
	 */
	public java.sql.Date getCreateDate()
	{
		return createdate_;
	}

	/**
	 * Setter for F_CreateDate
	 *
	 */
	public void setCreateDate(java.sql.Date _createdate)
	{
		createdate_ = _createdate;
		setreg_[4] = true;
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
		setreg_[5] = true;
	}

	/**
	 * Getter for F_Articles
	 *
	 */
	public int getArticles()
	{
		return articles_;
	}

	/**
	 * Setter for F_Articles
	 *
	 */
	public void setArticles(int _articles)
	{
		articles_ = _articles;
		setreg_[6] = true;
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
	 * @param _projectid - int
	 */
	public void queryByPrimaryKey(int _projectid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_Projects where F_ProjectID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,_projectid);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				setProjectID(rs.getInt(1));
				setProjectName(rs.getString(2));
				setLangPair(rs.getString(3));
				setCreator(rs.getInt(4));
				setCreateDate(rs.getDate(5));
				setProgress(rs.getFloat(6));
				setArticles(rs.getInt(7));
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
	 * @param creator - int
	 */
	public ArrayList<Projects> queryProjectsByCreator(int creator) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_Projects where F_Creator=? order by F_ProjectID";
		ArrayList<Projects> result = new ArrayList<Projects>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,creator);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				Projects s = new Projects();
				s.setProjectID(rs.getInt(1));
				s.setProjectName(rs.getString(2));
				s.setLangPair(rs.getString(3));
				s.setCreator(rs.getInt(4));
				s.setCreateDate(rs.getDate(5));
				s.setProgress(rs.getFloat(6));
				s.setArticles(rs.getInt(7));
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
	 * @param member - int
	 */
	public ArrayList<Projects> queryProjectsByMember(int member) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select distinct P.* from T_Projects P left join T_Articles A on A.F_Translator=? and A.F_Project=P.F_ProjectID where P.F_Creator=? order by F_ProjectID";
		ArrayList<Projects> result = new ArrayList<Projects>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,member);
			stmt.setInt(2,member);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				Projects s = new Projects();
				s.setProjectID(rs.getInt(1));
				s.setProjectName(rs.getString(2));
				s.setLangPair(rs.getString(3));
				s.setCreator(rs.getInt(4));
				s.setCreateDate(rs.getDate(5));
				s.setProgress(rs.getFloat(6));
				s.setArticles(rs.getInt(7));
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
	 * Insert record in table with current data object.
	 *
	 */
	public void insert(Connection con) throws SQLException
	{
		String sql = "select * from T_Projects where F_ProjectID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setInt(1, projectid_);
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();
			rs.moveToInsertRow();
			if (setreg_[0]) rs.updateInt(1,projectid_);
			if (setreg_[1]) rs.updateString(2,projectname_);
			if (setreg_[2]) rs.updateString(3,langpair_);
			if (setreg_[3]) rs.updateInt(4,creator_);
			if (setreg_[4]) rs.updateDate(5,createdate_);
			if (setreg_[5]) rs.updateFloat(6,progress_);
			if (setreg_[6]) rs.updateInt(7,articles_);
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
		String sql = "select * from T_Projects where F_ProjectID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setInt(1, projectid_);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				if (setreg_[1]) rs.updateString(2,projectname_);
				if (setreg_[2]) rs.updateString(3,langpair_);
				if (setreg_[3]) rs.updateInt(4,creator_);
				if (setreg_[4]) rs.updateDate(5,createdate_);
				if (setreg_[5]) rs.updateFloat(6,progress_);
				if (setreg_[6]) rs.updateInt(7,articles_);
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
	 * @param _projectid - int
	 */
	public void delete(int _projectid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "delete from T_Projects where F_ProjectID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,_projectid);
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
		String sql = "delete from T_Projects where F_ProjectID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,projectid_);
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
