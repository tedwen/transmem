//Generated code by table2java.py, do not modify.
package com.transmem.data.db;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * English word index for English-Chinese sentences
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan.2007
 */
public class ENZHX implements java.io.Serializable
{
	protected static final long serialVersionUID = 106L;
	private Logger log_ = Logger.getLogger(ENZHX.class.getName());

	private boolean[] setreg_ = new boolean[3];

	private Connection con_;
	private String word_; ///English word as index
	private long sid_; ///Sentence ID
	private short offset_; ///Which word in the source sentence

	/**
	 * Construct an empty ENZHX object.
	 *
	 */
	public ENZHX()
	{
	}

	/**
	 * Construct a ENZHX object with a Connection instance.
	 *
	 * @param con - Connection object
	 */
	public ENZHX(Connection con)
	{
		con_ = con;
	}

	/**
	 * Construct with a query on primary key(s)
	 *
	 */
	public ENZHX(Connection con, String _word, long _sid) throws SQLException
	{
		con_ = con;
		queryByPrimaryKey(_word,_sid);
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
	 * Getter for F_Word
	 *
	 */
	public String getWord()
	{
		return word_;
	}

	/**
	 * Setter for F_Word
	 *
	 */
	public void setWord(String _word)
	{
		word_ = _word;
		setreg_[0] = true;
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
		setreg_[1] = true;
	}

	/**
	 * Getter for F_Offset
	 *
	 */
	public short getOffset()
	{
		return offset_;
	}

	/**
	 * Setter for F_Offset
	 *
	 */
	public void setOffset(short _offset)
	{
		offset_ = _offset;
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
	 * @param _word - String
	 * @param _sid - long
	 */
	public void queryByPrimaryKey(String _word, long _sid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_ENZHX where F_Word=? AND F_SID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,_word);
			stmt.setLong(2,_sid);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				setWord(rs.getString(1));
				setSID(rs.getLong(2));
				setOffset(rs.getShort(3));
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
		String sql = "select * from T_ENZHX where F_Word=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setString(1, word_);
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();
			rs.moveToInsertRow();
			if (setreg_[0]) rs.updateString(1,word_);
			if (setreg_[1]) rs.updateLong(2,sid_);
			if (setreg_[2]) rs.updateShort(3,offset_);
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
		String sql = "select * from T_ENZHX where F_Word=? AND F_SID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setString(1, word_);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				if (setreg_[2]) rs.updateShort(3,offset_);
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
	 * @param _word - String
	 * @param _sid - long
	 */
	public void delete(String _word, long _sid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "delete from T_ENZHX where F_Word=? AND F_SID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,_word);
			stmt.setLong(2,_sid);
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
		String sql = "delete from T_ENZHX where F_Word=? AND F_SID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,word_);
			stmt.setLong(2,sid_);
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
