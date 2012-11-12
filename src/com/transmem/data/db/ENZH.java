//Generated code by table2java.py, do not modify.
package com.transmem.data.db;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * English-Chinese sentences
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan.2007
 */
public class ENZH implements java.io.Serializable
{
	protected static final long serialVersionUID = 105L;
	private Logger log_ = Logger.getLogger(ENZH.class.getName());

	private boolean[] setreg_ = new boolean[7];

	private Connection con_;
	private long sid_; ///Sentence ID as long
	private String source_; ///English sentence
	private String target_; ///Chinese sentence
	private String domain_; ///domain, default=general
	private int owner_; ///provider of this entry
	private int permit_; ///0=Public,positive=GroupID,-1=Owner
	private int from_; ///article ID if any, 0=unknown, negative from dictionaries

	/**
	 * Construct an empty ENZH object.
	 *
	 */
	public ENZH()
	{
	}

	/**
	 * Construct a ENZH object with a Connection instance.
	 *
	 * @param con - Connection object
	 */
	public ENZH(Connection con)
	{
		con_ = con;
	}

	/**
	 * Construct with a query on primary key(s)
	 *
	 */
	public ENZH(Connection con, long _sid) throws SQLException
	{
		con_ = con;
		queryByPrimaryKey(_sid);
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
	 * Getter for F_SID
	 *
	 */
	public long getSID()
	{
		return sid_;
	}

	/**
	 * Setter for F_SID
	 *
	 */
	public void setSID(long _sid)
	{
		sid_ = _sid;
		setreg_[0] = true;
	}

	/**
	 * Getter for F_Source
	 *
	 */
	public String getSource()
	{
		return source_;
	}

	/**
	 * Setter for F_Source
	 *
	 */
	public void setSource(String _source)
	{
		source_ = _source;
		setreg_[1] = true;
	}

	/**
	 * Getter for F_Target
	 *
	 */
	public String getTarget()
	{
		return target_;
	}

	/**
	 * Setter for F_Target
	 *
	 */
	public void setTarget(String _target)
	{
		target_ = _target;
		setreg_[2] = true;
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
		setreg_[5] = true;
	}

	/**
	 * Getter for F_From
	 *
	 */
	public int getFrom()
	{
		return from_;
	}

	/**
	 * Setter for F_From
	 *
	 */
	public void setFrom(int _from)
	{
		from_ = _from;
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
	 * @param _sid - long
	 */
	public void queryByPrimaryKey(long _sid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_ENZH where F_SID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setLong(1,_sid);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				setSID(rs.getLong(1));
				setSource(rs.getString(2));
				setTarget(rs.getString(3));
				setDomain(rs.getString(4));
				setOwner(rs.getInt(5));
				setPermit(rs.getInt(6));
				setFrom(rs.getInt(7));
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
	 * Query translation units by owner
	 *
	 * @param owner - int
	 */
	public ArrayList<Transunit> queryUnitsByOwner(int owner) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select F_SID,F_Source,F_Target from T_ENZH where F_Owner=?";
		ArrayList<Transunit> result = new ArrayList<Transunit>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,owner);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				Transunit s = new Transunit();
				s.setSid(rs.getLong(1));
				s.setSource(rs.getString(2));
				s.setTarget(rs.getString(3));
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
		String sql = "select * from T_ENZH where F_SID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setLong(1, sid_);
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();
			rs.moveToInsertRow();
			if (setreg_[0]) rs.updateLong(1,sid_);
			if (setreg_[1]) rs.updateString(2,source_);
			if (setreg_[2]) rs.updateString(3,target_);
			if (setreg_[3]) rs.updateString(4,domain_);
			if (setreg_[4]) rs.updateInt(5,owner_);
			if (setreg_[5]) rs.updateInt(6,permit_);
			if (setreg_[6]) rs.updateInt(7,from_);
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
		String sql = "select * from T_ENZH where F_SID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setLong(1, sid_);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				if (setreg_[1]) rs.updateString(2,source_);
				if (setreg_[2]) rs.updateString(3,target_);
				if (setreg_[3]) rs.updateString(4,domain_);
				if (setreg_[4]) rs.updateInt(5,owner_);
				if (setreg_[5]) rs.updateInt(6,permit_);
				if (setreg_[6]) rs.updateInt(7,from_);
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
	 * Delete all sentences from a source
	 *
	 * @param source - int
	 */
	public void deleteBySource(int source) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "delete from T_ENZH where F_From=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,source);
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
	 * Delete all sentences from an owner
	 *
	 * @param owner - int
	 */
	public void deleteByOwner(int owner) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "delete from T_ENZH where F_Owner=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,owner);
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
	 * Delete all sentences by an owner from this source
	 *
	 * @param owner - int
	 * @param source - int
	 */
	public void deleteBySourceAndOwner(int owner, int source) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "delete from T_ENZH where F_Owner=? and F_From=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,owner);
			stmt.setInt(2,source);
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
	 * @param _sid - long
	 */
	public void delete(long _sid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "delete from T_ENZH where F_SID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setLong(1,_sid);
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
		String sql = "delete from T_ENZH where F_SID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setLong(1,sid_);
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
