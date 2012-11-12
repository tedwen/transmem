//Generated code by table2java.py, do not modify.
package com.transmem.data.db;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Summary of all corpus statistics, number of sentences
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan.2007
 */
public class Corpora implements java.io.Serializable
{
	protected static final long serialVersionUID = 103L;
	private Logger log_ = Logger.getLogger(Corpora.class.getName());

	private boolean[] setreg_ = new boolean[7];

	private Connection con_;
	private int corpusid_; ///PK
	private String langpair_; ///
	private String domain_; ///domain
	private int public_; ///number of public sentences
	private int group_; ///number of group sentences
	private int private_; ///number of private sentences
	private int total_; ///total of public,group and private sentences

	/**
	 * Construct an empty Corpora object.
	 *
	 */
	public Corpora()
	{
	}

	/**
	 * Construct a Corpora object with a Connection instance.
	 *
	 * @param con - Connection object
	 */
	public Corpora(Connection con)
	{
		con_ = con;
	}

	/**
	 * Construct with a query on primary key(s)
	 *
	 */
	public Corpora(Connection con, int _corpusid) throws SQLException
	{
		con_ = con;
		queryByPrimaryKey(_corpusid);
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
	 * Getter for F_CorpusID
	 *
	 */
	public int getCorpusID()
	{
		return corpusid_;
	}

	/**
	 * Setter for F_CorpusID
	 *
	 */
	public void setCorpusID(int _corpusid)
	{
		corpusid_ = _corpusid;
		setreg_[0] = true;
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
		setreg_[1] = true;
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
		setreg_[2] = true;
	}

	/**
	 * Getter for F_Public
	 *
	 */
	public int getPublic()
	{
		return public_;
	}

	/**
	 * Setter for F_Public
	 *
	 */
	public void setPublic(int _public)
	{
		public_ = _public;
		setreg_[3] = true;
	}

	/**
	 * Getter for F_Group
	 *
	 */
	public int getGroup()
	{
		return group_;
	}

	/**
	 * Setter for F_Group
	 *
	 */
	public void setGroup(int _group)
	{
		group_ = _group;
		setreg_[4] = true;
	}

	/**
	 * Getter for F_Private
	 *
	 */
	public int getPrivate()
	{
		return private_;
	}

	/**
	 * Setter for F_Private
	 *
	 */
	public void setPrivate(int _private)
	{
		private_ = _private;
		setreg_[5] = true;
	}

	/**
	 * Getter for F_Total
	 *
	 */
	public int getTotal()
	{
		return total_;
	}

	/**
	 * Setter for F_Total
	 *
	 */
	public void setTotal(int _total)
	{
		total_ = _total;
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
	 * @param _corpusid - int
	 */
	public void queryByPrimaryKey(int _corpusid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_Corpora where F_CorpusID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,_corpusid);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				setCorpusID(rs.getInt(1));
				setLangPair(rs.getString(2));
				setDomain(rs.getString(3));
				setPublic(rs.getInt(4));
				setGroup(rs.getInt(5));
				setPrivate(rs.getInt(6));
				setTotal(rs.getInt(7));
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
	 * Query the statistics from the T_Corpora table
	 *
	 * @param lang - String
	 */
	public ArrayList<CorpusTally> queryCorpusStats(String lang) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select f_Domain,f_Public,f_Group,f_Private,f_Total from T_Corpora where F_LangPair=? union select 'zz' as f_Domain,sum(f_Public),sum(f_Group),sum(f_Private),sum(f_Total) from T_Corpora where F_LangPair=? order by f_Domain";
		ArrayList<CorpusTally> result = new ArrayList<CorpusTally>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,lang);
			stmt.setString(2,lang);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				CorpusTally s = new CorpusTally();
				s.setDomain(rs.getString(1));
				s.setPublic(rs.getInt(2));
				s.setGroup(rs.getInt(3));
				s.setPrivate(rs.getInt(4));
				s.setTotal(rs.getInt(5));
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
		String sql = "select * from T_Corpora where F_CorpusID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setInt(1, corpusid_);
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();
			rs.moveToInsertRow();
			if (setreg_[0]) rs.updateInt(1,corpusid_);
			if (setreg_[1]) rs.updateString(2,langpair_);
			if (setreg_[2]) rs.updateString(3,domain_);
			if (setreg_[3]) rs.updateInt(4,public_);
			if (setreg_[4]) rs.updateInt(5,group_);
			if (setreg_[5]) rs.updateInt(6,private_);
			if (setreg_[6]) rs.updateInt(7,total_);
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
		String sql = "select * from T_Corpora where F_CorpusID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setInt(1, corpusid_);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				if (setreg_[1]) rs.updateString(2,langpair_);
				if (setreg_[2]) rs.updateString(3,domain_);
				if (setreg_[3]) rs.updateInt(4,public_);
				if (setreg_[4]) rs.updateInt(5,group_);
				if (setreg_[5]) rs.updateInt(6,private_);
				if (setreg_[6]) rs.updateInt(7,total_);
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
	 * @param _corpusid - int
	 */
	public void delete(int _corpusid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "delete from T_Corpora where F_CorpusID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,_corpusid);
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
		String sql = "delete from T_Corpora where F_CorpusID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,corpusid_);
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
