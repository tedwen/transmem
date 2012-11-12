//Generated code by table2java.py, do not modify.
package com.transmem.data.db;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Language pairs for translation supported by the system.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan.2007
 */
public class Languages implements java.io.Serializable
{
	protected static final long serialVersionUID = 101L;
	private Logger log_ = Logger.getLogger(Languages.class.getName());

	private boolean[] setreg_ = new boolean[6];

	private Connection con_;
	private String pairid_; ///Language pair ID = source plus target language code
	private String sourcecode_; ///source language code, EN,ZH
	private String targetcode_; ///target language code
	private String displaycode_; ///code for display using resource bundle
	private String tablename_; ///table name for this pair, two pairs share the same corpus table
	private long sentences_; ///number of sentence pairs for this pair

	/**
	 * Construct an empty Languages object.
	 *
	 */
	public Languages()
	{
	}

	/**
	 * Construct a Languages object with a Connection instance.
	 *
	 * @param con - Connection object
	 */
	public Languages(Connection con)
	{
		con_ = con;
	}

	/**
	 * Construct with a query on primary key(s)
	 *
	 */
	public Languages(Connection con, String _pairid) throws SQLException
	{
		con_ = con;
		queryByPrimaryKey(_pairid);
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
	 * Getter for F_PairID
	 *
	 */
	public String getPairID()
	{
		return pairid_;
	}

	/**
	 * Setter for F_PairID
	 *
	 */
	public void setPairID(String _pairid)
	{
		pairid_ = _pairid;
		setreg_[0] = true;
	}

	/**
	 * Getter for F_SourceCode
	 *
	 */
	public String getSourceCode()
	{
		return sourcecode_;
	}

	/**
	 * Setter for F_SourceCode
	 *
	 */
	public void setSourceCode(String _sourcecode)
	{
		sourcecode_ = _sourcecode;
		setreg_[1] = true;
	}

	/**
	 * Getter for F_TargetCode
	 *
	 */
	public String getTargetCode()
	{
		return targetcode_;
	}

	/**
	 * Setter for F_TargetCode
	 *
	 */
	public void setTargetCode(String _targetcode)
	{
		targetcode_ = _targetcode;
		setreg_[2] = true;
	}

	/**
	 * Getter for F_DisplayCode
	 *
	 */
	public String getDisplayCode()
	{
		return displaycode_;
	}

	/**
	 * Setter for F_DisplayCode
	 *
	 */
	public void setDisplayCode(String _displaycode)
	{
		displaycode_ = _displaycode;
		setreg_[3] = true;
	}

	/**
	 * Getter for F_TableName
	 *
	 */
	public String getTableName()
	{
		return tablename_;
	}

	/**
	 * Setter for F_TableName
	 *
	 */
	public void setTableName(String _tablename)
	{
		tablename_ = _tablename;
		setreg_[4] = true;
	}

	/**
	 * Getter for F_Sentences
	 *
	 */
	public long getSentences()
	{
		return sentences_;
	}

	/**
	 * Setter for F_Sentences
	 *
	 */
	public void setSentences(long _sentences)
	{
		sentences_ = _sentences;
		setreg_[5] = true;
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
	 * @param _pairid - String
	 */
	public void queryByPrimaryKey(String _pairid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_Languages where F_PairID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,_pairid);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				setPairID(rs.getString(1));
				setSourceCode(rs.getString(2));
				setTargetCode(rs.getString(3));
				setDisplayCode(rs.getString(4));
				setTableName(rs.getString(5));
				setSentences(rs.getLong(6));
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
	 * Query a list of supported language pairs for translation.
	 *
	 */
	public ArrayList<SupportedPairs> querySupportedPairs() throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select F_PairID,F_DisplayCode from T_Languages";
		ArrayList<SupportedPairs> result = new ArrayList<SupportedPairs>();
		java.sql.Statement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				SupportedPairs s = new SupportedPairs();
				s.setId(rs.getString(1));
				s.setName(rs.getString(2));
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
	 * Query table name for a given language pair
	 *
	 * @param pair - String
	 */
	public String queryTableName(String pair) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select F_TableName from T_Languages where F_PairID=?";
		String result = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,pair);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				result = rs.getString(1);
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
	 * Query table name for a given language source and target code
	 *
	 * @param src - String
	 * @param tgt - String
	 */
	public String queryTableNameBySourceTarget(String src, String tgt) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select F_TableName from T_Languages where F_SourceCode=? and F_TargetCode=?";
		String result = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,src);
			stmt.setString(2,tgt);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				result = rs.getString(1);
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
	 * Query a list of unique tables
	 *
	 */
	public ArrayList<Languages> queryTables() throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select distinct F_TableName from T_Languages";
		ArrayList<Languages> result = new ArrayList<Languages>();
		java.sql.Statement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				Languages s = new Languages();
				s.setPairID(rs.getString(1));
				s.setSourceCode(rs.getString(2));
				s.setTargetCode(rs.getString(3));
				s.setDisplayCode(rs.getString(4));
				s.setTableName(rs.getString(5));
				s.setSentences(rs.getLong(6));
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
		String sql = "select * from T_Languages where F_PairID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setString(1, pairid_);
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();
			rs.moveToInsertRow();
			if (setreg_[0]) rs.updateString(1,pairid_);
			if (setreg_[1]) rs.updateString(2,sourcecode_);
			if (setreg_[2]) rs.updateString(3,targetcode_);
			if (setreg_[3]) rs.updateString(4,displaycode_);
			if (setreg_[4]) rs.updateString(5,tablename_);
			if (setreg_[5]) rs.updateLong(6,sentences_);
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
		String sql = "select * from T_Languages where F_PairID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setString(1, pairid_);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				if (setreg_[1]) rs.updateString(2,sourcecode_);
				if (setreg_[2]) rs.updateString(3,targetcode_);
				if (setreg_[3]) rs.updateString(4,displaycode_);
				if (setreg_[4]) rs.updateString(5,tablename_);
				if (setreg_[5]) rs.updateLong(6,sentences_);
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
	 * @param _pairid - String
	 */
	public void delete(String _pairid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "delete from T_Languages where F_PairID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,_pairid);
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
		String sql = "delete from T_Languages where F_PairID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,pairid_);
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
