//Generated code by table2java.py, do not modify.
package com.transmem.data.db;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Language codes defined by ISO-639.1 (2-character codes)
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan.2007
 */
public class LangCodes implements java.io.Serializable
{
	protected static final long serialVersionUID = 100L;
	private Logger log_ = Logger.getLogger(LangCodes.class.getName());

	private boolean[] setreg_ = new boolean[3];

	private Connection con_;
	private String code_; ///such as ZH, EN
	private String name_; ///Name of the language
	private String family_; ///Language family

	/**
	 * Construct an empty LangCodes object.
	 *
	 */
	public LangCodes()
	{
	}

	/**
	 * Construct a LangCodes object with a Connection instance.
	 *
	 * @param con - Connection object
	 */
	public LangCodes(Connection con)
	{
		con_ = con;
	}

	/**
	 * Construct with a query on primary key(s)
	 *
	 */
	public LangCodes(Connection con, String _code) throws SQLException
	{
		con_ = con;
		queryByPrimaryKey(_code);
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
	 * Getter for F_Code
	 *
	 */
	public String getCode()
	{
		return code_;
	}

	/**
	 * Setter for F_Code
	 *
	 */
	public void setCode(String _code)
	{
		code_ = _code;
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
	 * Getter for F_Family
	 *
	 */
	public String getFamily()
	{
		return family_;
	}

	/**
	 * Setter for F_Family
	 *
	 */
	public void setFamily(String _family)
	{
		family_ = _family;
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
	 * @param _code - String
	 */
	public void queryByPrimaryKey(String _code) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_LangCodes where F_Code=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,_code);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				setCode(rs.getString(1));
				setName(rs.getString(2));
				setFamily(rs.getString(3));
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
		String sql = "select * from T_LangCodes where F_Code=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setString(1, code_);
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();
			rs.moveToInsertRow();
			if (setreg_[0]) rs.updateString(1,code_);
			if (setreg_[1]) rs.updateString(2,name_);
			if (setreg_[2]) rs.updateString(3,family_);
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
		String sql = "select * from T_LangCodes where F_Code=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setString(1, code_);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				if (setreg_[1]) rs.updateString(2,name_);
				if (setreg_[2]) rs.updateString(3,family_);
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
	 * @param _code - String
	 */
	public void delete(String _code) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "delete from T_LangCodes where F_Code=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,_code);
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
		String sql = "delete from T_LangCodes where F_Code=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,code_);
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
