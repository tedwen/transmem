//Generated code by table2java.py, do not modify.
package com.transmem.data.db;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * The supported file formats such as DOC, PDF, HTM, TXT
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan.2007
 */
public class FileFormats implements java.io.Serializable
{
	protected static final long serialVersionUID = 112L;
	private Logger log_ = Logger.getLogger(FileFormats.class.getName());

	private boolean[] setreg_ = new boolean[2];

	private Connection con_;
	private String formatid_; ///format id as file extension
	private String formatname_; ///name of the format

	/**
	 * Construct an empty FileFormats object.
	 *
	 */
	public FileFormats()
	{
	}

	/**
	 * Construct a FileFormats object with a Connection instance.
	 *
	 * @param con - Connection object
	 */
	public FileFormats(Connection con)
	{
		con_ = con;
	}

	/**
	 * Construct with a query on primary key(s)
	 *
	 */
	public FileFormats(Connection con, String _formatid) throws SQLException
	{
		con_ = con;
		queryByPrimaryKey(_formatid);
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
	 * Getter for F_FormatID
	 *
	 */
	public String getFormatID()
	{
		return formatid_;
	}

	/**
	 * Setter for F_FormatID
	 *
	 */
	public void setFormatID(String _formatid)
	{
		formatid_ = _formatid;
		setreg_[0] = true;
	}

	/**
	 * Getter for F_FormatName
	 *
	 */
	public String getFormatName()
	{
		return formatname_;
	}

	/**
	 * Setter for F_FormatName
	 *
	 */
	public void setFormatName(String _formatname)
	{
		formatname_ = _formatname;
		setreg_[1] = true;
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
	 * @param _formatid - String
	 */
	public void queryByPrimaryKey(String _formatid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_FileFormats where F_FormatID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,_formatid);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				setFormatID(rs.getString(1));
				setFormatName(rs.getString(2));
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
	 * Return all file formats
	 *
	 */
	public ArrayList<FileFormats> queryAllFileFormats() throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_FileFormats";
		ArrayList<FileFormats> result = new ArrayList<FileFormats>();
		java.sql.Statement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				FileFormats s = new FileFormats();
				s.setFormatID(rs.getString(1));
				s.setFormatName(rs.getString(2));
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
		String sql = "select * from T_FileFormats where F_FormatID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setString(1, formatid_);
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();
			rs.moveToInsertRow();
			if (setreg_[0]) rs.updateString(1,formatid_);
			if (setreg_[1]) rs.updateString(2,formatname_);
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
		String sql = "select * from T_FileFormats where F_FormatID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setString(1, formatid_);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				if (setreg_[1]) rs.updateString(2,formatname_);
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
	 * @param _formatid - String
	 */
	public void delete(String _formatid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "delete from T_FileFormats where F_FormatID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,_formatid);
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
		String sql = "delete from T_FileFormats where F_FormatID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,formatid_);
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
