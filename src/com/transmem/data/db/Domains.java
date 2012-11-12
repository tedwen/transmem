//Generated code by table2java.py, do not modify.
package com.transmem.data.db;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Domain fields referenced by the corpora tables
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan.2007
 */
public class Domains implements java.io.Serializable
{
	protected static final long serialVersionUID = 102L;
	private Logger log_ = Logger.getLogger(Domains.class.getName());

	private boolean[] setreg_ = new boolean[3];

	private Connection con_;
	private String domaincode_; ///such as IT
	private String domainname_; ///name of domain
	private String parentdomain_; ///hierarchical chain

	/**
	 * Construct an empty Domains object.
	 *
	 */
	public Domains()
	{
	}

	/**
	 * Construct a Domains object with a Connection instance.
	 *
	 * @param con - Connection object
	 */
	public Domains(Connection con)
	{
		con_ = con;
	}

	/**
	 * Construct with a query on primary key(s)
	 *
	 */
	public Domains(Connection con, String _domaincode) throws SQLException
	{
		con_ = con;
		queryByPrimaryKey(_domaincode);
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
	 * Getter for F_DomainCode
	 *
	 */
	public String getDomainCode()
	{
		return domaincode_;
	}

	/**
	 * Setter for F_DomainCode
	 *
	 */
	public void setDomainCode(String _domaincode)
	{
		domaincode_ = _domaincode;
		setreg_[0] = true;
	}

	/**
	 * Getter for F_DomainName
	 *
	 */
	public String getDomainName()
	{
		return domainname_;
	}

	/**
	 * Setter for F_DomainName
	 *
	 */
	public void setDomainName(String _domainname)
	{
		domainname_ = _domainname;
		setreg_[1] = true;
	}

	/**
	 * Getter for F_ParentDomain
	 *
	 */
	public String getParentDomain()
	{
		return parentdomain_;
	}

	/**
	 * Setter for F_ParentDomain
	 *
	 */
	public void setParentDomain(String _parentdomain)
	{
		parentdomain_ = _parentdomain;
		setreg_[2] = true;
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
	 * @param _domaincode - String
	 */
	public void queryByPrimaryKey(String _domaincode) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_Domains where F_DomainCode=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,_domaincode);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				setDomainCode(rs.getString(1));
				setDomainName(rs.getString(2));
				setParentDomain(rs.getString(3));
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
	 * Query all domains
	 *
	 */
	public ArrayList<Domains> queryDomains() throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_Domains";
		ArrayList<Domains> result = new ArrayList<Domains>();
		java.sql.Statement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				Domains s = new Domains();
				s.setDomainCode(rs.getString(1));
				s.setDomainName(rs.getString(2));
				s.setParentDomain(rs.getString(3));
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
		String sql = "select * from T_Domains where F_DomainCode=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setString(1, domaincode_);
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();
			rs.moveToInsertRow();
			if (setreg_[0]) rs.updateString(1,domaincode_);
			if (setreg_[1]) rs.updateString(2,domainname_);
			if (setreg_[2]) rs.updateString(3,parentdomain_);
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
		String sql = "select * from T_Domains where F_DomainCode=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setString(1, domaincode_);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				if (setreg_[1]) rs.updateString(2,domainname_);
				if (setreg_[2]) rs.updateString(3,parentdomain_);
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
	 * @param _domaincode - String
	 */
	public void delete(String _domaincode) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "delete from T_Domains where F_DomainCode=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,_domaincode);
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
		String sql = "delete from T_Domains where F_DomainCode=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,domaincode_);
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
