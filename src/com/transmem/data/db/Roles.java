//Generated code by table2java.py, do not modify.
package com.transmem.data.db;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Role table
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan.2007
 */
public class Roles implements java.io.Serializable
{
	protected static final long serialVersionUID = 108L;
	private Logger log_ = Logger.getLogger(Roles.class.getName());

	private boolean[] setreg_ = new boolean[3];

	private Connection con_;
	private String roleid_; ///role ID as a character
	private String rolename_; ///Role name
	private int level_; ///number for sorting purpose, the higher, the more rights, also the number of projects he can create

	/**
	 * Construct an empty Roles object.
	 *
	 */
	public Roles()
	{
	}

	/**
	 * Construct a Roles object with a Connection instance.
	 *
	 * @param con - Connection object
	 */
	public Roles(Connection con)
	{
		con_ = con;
	}

	/**
	 * Construct with a query on primary key(s)
	 *
	 */
	public Roles(Connection con, String _roleid) throws SQLException
	{
		con_ = con;
		queryByPrimaryKey(_roleid);
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
	 * Getter for F_RoleID
	 *
	 */
	public String getRoleID()
	{
		return roleid_;
	}

	/**
	 * Setter for F_RoleID
	 *
	 */
	public void setRoleID(String _roleid)
	{
		roleid_ = _roleid;
		setreg_[0] = true;
	}

	/**
	 * Getter for F_RoleName
	 *
	 */
	public String getRoleName()
	{
		return rolename_;
	}

	/**
	 * Setter for F_RoleName
	 *
	 */
	public void setRoleName(String _rolename)
	{
		rolename_ = _rolename;
		setreg_[1] = true;
	}

	/**
	 * Getter for F_Level
	 *
	 */
	public int getLevel()
	{
		return level_;
	}

	/**
	 * Setter for F_Level
	 *
	 */
	public void setLevel(int _level)
	{
		level_ = _level;
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
	 * @param _roleid - String
	 */
	public void queryByPrimaryKey(String _roleid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_Roles where F_RoleID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,_roleid);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				setRoleID(rs.getString(1));
				setRoleName(rs.getString(2));
				setLevel(rs.getInt(3));
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
	 * Insert record in table with current data object.
	 *
	 */
	public void insert(Connection con) throws SQLException
	{
		String sql = "select * from T_Roles where F_RoleID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setString(1, roleid_);
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();
			rs.moveToInsertRow();
			if (setreg_[0]) rs.updateString(1,roleid_);
			if (setreg_[1]) rs.updateString(2,rolename_);
			if (setreg_[2]) rs.updateInt(3,level_);
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
		String sql = "select * from T_Roles where F_RoleID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setString(1, roleid_);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				if (setreg_[1]) rs.updateString(2,rolename_);
				if (setreg_[2]) rs.updateInt(3,level_);
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
	 * @param _roleid - String
	 */
	public void delete(String _roleid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "delete from T_Roles where F_RoleID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,_roleid);
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
		String sql = "delete from T_Roles where F_RoleID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,roleid_);
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
