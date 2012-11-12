//Generated code by table2java.py, do not modify.
package com.transmem.data.db;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Global Parameters and values
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan.2007
 */
public class Parameters implements java.io.Serializable
{
	protected static final long serialVersionUID = 101L;
	private Logger log_ = Logger.getLogger(Parameters.class.getName());

	private boolean[] setreg_ = new boolean[3];

	private Connection con_;
	private String name_; ///name of parameter
	private String value_; ///parameter value as string
	private String type_; ///data type, int,char,string,float

	/**
	 * Construct an empty Parameters object.
	 *
	 */
	public Parameters()
	{
	}

	/**
	 * Construct a Parameters object with a Connection instance.
	 *
	 * @param con - Connection object
	 */
	public Parameters(Connection con)
	{
		con_ = con;
	}

	/**
	 * Construct with a query on primary key(s)
	 *
	 */
	public Parameters(Connection con, String _name) throws SQLException
	{
		con_ = con;
		queryByPrimaryKey(_name);
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
		setreg_[0] = true;
	}

	/**
	 * Getter for F_Value
	 *
	 */
	public String getValue()
	{
		return value_;
	}

	/**
	 * Setter for F_Value
	 *
	 */
	public void setValue(String _value)
	{
		value_ = _value;
		setreg_[1] = true;
	}

	/**
	 * Getter for F_Type
	 *
	 */
	public String getType()
	{
		return type_;
	}

	/**
	 * Setter for F_Type
	 *
	 */
	public void setType(String _type)
	{
		type_ = _type;
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
	 * @param _name - String
	 */
	public void queryByPrimaryKey(String _name) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_Parameters where F_Name=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,_name);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				setName(rs.getString(1));
				setValue(rs.getString(2));
				setType(rs.getString(3));
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
	 * Query the value of a parameter by its name
	 *
	 * @param name - String
	 */
	public String getValue(String name) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select F_Value from T_Parameters where F_Name=?";
		String result = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,name);
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
	 * Insert record in table with current data object.
	 *
	 */
	public void insert(Connection con) throws SQLException
	{
		String sql = "select * from T_Parameters where F_Name=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setString(1, name_);
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();
			rs.moveToInsertRow();
			if (setreg_[0]) rs.updateString(1,name_);
			if (setreg_[1]) rs.updateString(2,value_);
			if (setreg_[2]) rs.updateString(3,type_);
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
		String sql = "select * from T_Parameters where F_Name=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setString(1, name_);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				if (setreg_[1]) rs.updateString(2,value_);
				if (setreg_[2]) rs.updateString(3,type_);
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
	 * @param _name - String
	 */
	public void delete(String _name) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "delete from T_Parameters where F_Name=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,_name);
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
		String sql = "delete from T_Parameters where F_Name=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,name_);
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
