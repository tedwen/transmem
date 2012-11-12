//Generated code by table2java.py, do not modify.
package com.transmem.data.db;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Source of example sentences, eg. Article, or dictionary
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan.2007
 */
public class Sources implements java.io.Serializable
{
	protected static final long serialVersionUID = 104L;
	private Logger log_ = Logger.getLogger(Sources.class.getName());

	private boolean[] setreg_ = new boolean[9];

	private Connection con_;
	private int sourceid_; ///Source ID,-999..-1 reserved,positive for articleID,less than-999 for uploads
	private String name_; ///Name of article or book
	private java.sql.Date date_; ///date of submission
	private String format_; ///file format if necessary
	private int owner_; ///owner or submitter of the article
	private String langpair_; ///
	private int sentences_; ///
	private String domain_; ///
	private int permit_; ///

	/**
	 * Construct an empty Sources object.
	 *
	 */
	public Sources()
	{
	}

	/**
	 * Construct a Sources object with a Connection instance.
	 *
	 * @param con - Connection object
	 */
	public Sources(Connection con)
	{
		con_ = con;
	}

	/**
	 * Construct with a query on primary key(s)
	 *
	 */
	public Sources(Connection con, int _sourceid) throws SQLException
	{
		con_ = con;
		queryByPrimaryKey(_sourceid);
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
	 * Getter for F_SourceID
	 *
	 */
	public int getSourceID()
	{
		return sourceid_;
	}

	/**
	 * Setter for F_SourceID
	 *
	 */
	public void setSourceID(int _sourceid)
	{
		sourceid_ = _sourceid;
		setreg_[0] = true;
	}

	/**
	 * Getter for F_Name
	 *
	 */
	public String getName()
	{
		return name_;
	}

	/**
	 * Setter for F_Name
	 *
	 */
	public void setName(String _name)
	{
		name_ = _name;
		setreg_[1] = true;
	}

	/**
	 * Getter for F_Date
	 *
	 */
	public java.sql.Date getDate()
	{
		return date_;
	}

	/**
	 * Setter for F_Date
	 *
	 */
	public void setDate(java.sql.Date _date)
	{
		date_ = _date;
		setreg_[2] = true;
	}

	/**
	 * Getter for F_Format
	 *
	 */
	public String getFormat()
	{
		return format_;
	}

	/**
	 * Setter for F_Format
	 *
	 */
	public void setFormat(String _format)
	{
		format_ = _format;
		setreg_[3] = true;
	}

	/**
	 * Getter for F_Owner
	 *
	 */
	public int getOwner()
	{
		return owner_;
	}

	/**
	 * Setter for F_Owner
	 *
	 */
	public void setOwner(int _owner)
	{
		owner_ = _owner;
		setreg_[4] = true;
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
		setreg_[5] = true;
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
		setreg_[6] = true;
	}

	/**
	 * Getter for F_Domain
	 *
	 */
	public String getDomain()
	{
		return domain_;
	}

	/**
	 * Setter for F_Domain
	 *
	 */
	public void setDomain(String _domain)
	{
		domain_ = _domain;
		setreg_[7] = true;
	}

	/**
	 * Getter for F_Permit
	 *
	 */
	public int getPermit()
	{
		return permit_;
	}

	/**
	 * Setter for F_Permit
	 *
	 */
	public void setPermit(int _permit)
	{
		permit_ = _permit;
		setreg_[8] = true;
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
	 * @param _sourceid - int
	 */
	public void queryByPrimaryKey(int _sourceid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_Sources where F_SourceID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,_sourceid);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				setSourceID(rs.getInt(1));
				setName(rs.getString(2));
				setDate(rs.getDate(3));
				setFormat(rs.getString(4));
				setOwner(rs.getInt(5));
				setLangPair(rs.getString(6));
				setSentences(rs.getInt(7));
				setDomain(rs.getString(8));
				setPermit(rs.getInt(9));
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
	 * Query all uploads for an owner
	 *
	 * @param owner - int
	 */
	public ArrayList<Sources> queryByOwner(int owner) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_Sources where F_Owner=? order by F_Date";
		ArrayList<Sources> result = new ArrayList<Sources>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,owner);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				Sources s = new Sources();
				s.setSourceID(rs.getInt(1));
				s.setName(rs.getString(2));
				s.setDate(rs.getDate(3));
				s.setFormat(rs.getString(4));
				s.setOwner(rs.getInt(5));
				s.setLangPair(rs.getString(6));
				s.setSentences(rs.getInt(7));
				s.setDomain(rs.getString(8));
				s.setPermit(rs.getInt(9));
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
		String sql = "select * from T_Sources where F_SourceID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setInt(1, sourceid_);
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();
			rs.moveToInsertRow();
			if (setreg_[0]) rs.updateInt(1,sourceid_);
			if (setreg_[1]) rs.updateString(2,name_);
			if (setreg_[2]) rs.updateDate(3,date_);
			if (setreg_[3]) rs.updateString(4,format_);
			if (setreg_[4]) rs.updateInt(5,owner_);
			if (setreg_[5]) rs.updateString(6,langpair_);
			if (setreg_[6]) rs.updateInt(7,sentences_);
			if (setreg_[7]) rs.updateString(8,domain_);
			if (setreg_[8]) rs.updateInt(9,permit_);
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
		String sql = "select * from T_Sources where F_SourceID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setInt(1, sourceid_);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				if (setreg_[1]) rs.updateString(2,name_);
				if (setreg_[2]) rs.updateDate(3,date_);
				if (setreg_[3]) rs.updateString(4,format_);
				if (setreg_[4]) rs.updateInt(5,owner_);
				if (setreg_[5]) rs.updateString(6,langpair_);
				if (setreg_[6]) rs.updateInt(7,sentences_);
				if (setreg_[7]) rs.updateString(8,domain_);
				if (setreg_[8]) rs.updateInt(9,permit_);
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
	 * @param _sourceid - int
	 */
	public void delete(int _sourceid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "delete from T_Sources where F_SourceID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,_sourceid);
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
		String sql = "delete from T_Sources where F_SourceID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,sourceid_);
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
