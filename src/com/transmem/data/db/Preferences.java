//Generated code by table2java.py, do not modify.
package com.transmem.data.db;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Users preferences as item key/value.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan.2007
 */
public class Preferences implements java.io.Serializable
{
	protected static final long serialVersionUID = 111L;
	private Logger log_ = Logger.getLogger(Preferences.class.getName());

	private boolean[] setreg_ = new boolean[4];

	private Connection con_;
	private int preid_; ///auto-increment id
	private int user_; ///User ID
	private String item_; ///Item name to configure
	private String value_; ///Value of the item

	/**
	 * Construct an empty Preferences object.
	 *
	 */
	public Preferences()
	{
	}

	/**
	 * Construct a Preferences object with a Connection instance.
	 *
	 * @param con - Connection object
	 */
	public Preferences(Connection con)
	{
		con_ = con;
	}

	/**
	 * Construct with a query on primary key(s)
	 *
	 */
	public Preferences(Connection con, int _preid) throws SQLException
	{
		con_ = con;
		queryByPrimaryKey(_preid);
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
	 * Getter for F_PreID
	 *
	 */
	public int getPreID()
	{
		return preid_;
	}

	/**
	 * Setter for F_PreID
	 *
	 */
	public void setPreID(int _preid)
	{
		preid_ = _preid;
		setreg_[0] = true;
	}

	/**
	 * Getter for F_User
	 *
	 */
	public int getUser()
	{
		return user_;
	}

	/**
	 * Setter for F_User
	 *
	 */
	public void setUser(int _user)
	{
		user_ = _user;
		setreg_[1] = true;
	}

	/**
	 * Getter for F_Item
	 *
	 */
	public String getItem()
	{
		return item_;
	}

	/**
	 * Setter for F_Item
	 *
	 */
	public void setItem(String _item)
	{
		item_ = _item;
		setreg_[2] = true;
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
	 * @param _preid - int
	 */
	public void queryByPrimaryKey(int _preid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_Preferences where F_PreID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,_preid);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				setPreID(rs.getInt(1));
				setUser(rs.getInt(2));
				setItem(rs.getString(3));
				setValue(rs.getString(4));
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
	 * Get all preference settings for a user.
	 *
	 * @param user - int
	 */
	public ArrayList<UserPrefs> queryPreferencesByUser(int user) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select F_Item,F_Value from T_Preferences where F_User=?";
		ArrayList<UserPrefs> result = new ArrayList<UserPrefs>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,user);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				UserPrefs s = new UserPrefs();
				s.setItem(rs.getString(1));
				s.setValue(rs.getString(2));
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
		String sql = "select * from T_Preferences where F_PreID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setInt(1, preid_);
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();
			rs.moveToInsertRow();
			if (setreg_[0]) rs.updateInt(1,preid_);
			if (setreg_[1]) rs.updateInt(2,user_);
			if (setreg_[2]) rs.updateString(3,item_);
			if (setreg_[3]) rs.updateString(4,value_);
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
		String sql = "select * from T_Preferences where F_PreID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setInt(1, preid_);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				if (setreg_[1]) rs.updateInt(2,user_);
				if (setreg_[2]) rs.updateString(3,item_);
				if (setreg_[3]) rs.updateString(4,value_);
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
	 * @param _preid - int
	 */
	public void delete(int _preid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "delete from T_Preferences where F_PreID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,_preid);
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
		String sql = "delete from T_Preferences where F_PreID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,preid_);
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
