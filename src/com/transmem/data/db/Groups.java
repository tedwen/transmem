//Generated code by table2java.py, do not modify.
package com.transmem.data.db;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * groups table
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan.2007
 */
public class Groups implements java.io.Serializable
{
	protected static final long serialVersionUID = 109L;
	private Logger log_ = Logger.getLogger(Groups.class.getName());

	private boolean[] setreg_ = new boolean[8];

	private Connection con_;
	private int groupid_; ///Group ID
	private String groupname_; ///name of the group
	private int leader_; ///user id for the group leader
	private java.sql.Date createdate_; ///create date
	private short publicity_; ///0=hidden,1=no_apply,2=apply,3=join
	private int points_; ///group points
	private int members_; ///number of members
	private String desc_; ///description of the group

	/**
	 * Construct an empty Groups object.
	 *
	 */
	public Groups()
	{
	}

	/**
	 * Construct a Groups object with a Connection instance.
	 *
	 * @param con - Connection object
	 */
	public Groups(Connection con)
	{
		con_ = con;
	}

	/**
	 * Construct with a query on primary key(s)
	 *
	 */
	public Groups(Connection con, int _groupid) throws SQLException
	{
		con_ = con;
		queryByPrimaryKey(_groupid);
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
	 * Getter for F_GroupID
	 *
	 */
	public int getGroupID()
	{
		return groupid_;
	}

	/**
	 * Setter for F_GroupID
	 *
	 */
	public void setGroupID(int _groupid)
	{
		groupid_ = _groupid;
		setreg_[0] = true;
	}

	/**
	 * Getter for F_GroupName
	 *
	 */
	public String getGroupName()
	{
		return groupname_;
	}

	/**
	 * Setter for F_GroupName
	 *
	 */
	public void setGroupName(String _groupname)
	{
		groupname_ = _groupname;
		setreg_[1] = true;
	}

	/**
	 * Getter for F_Leader
	 *
	 */
	public int getLeader()
	{
		return leader_;
	}

	/**
	 * Setter for F_Leader
	 *
	 */
	public void setLeader(int _leader)
	{
		leader_ = _leader;
		setreg_[2] = true;
	}

	/**
	 * Getter for F_CreateDate
	 *
	 */
	public java.sql.Date getCreateDate()
	{
		return createdate_;
	}

	/**
	 * Setter for F_CreateDate
	 *
	 */
	public void setCreateDate(java.sql.Date _createdate)
	{
		createdate_ = _createdate;
		setreg_[3] = true;
	}

	/**
	 * Getter for F_Publicity
	 *
	 */
	public short getPublicity()
	{
		return publicity_;
	}

	/**
	 * Setter for F_Publicity
	 *
	 */
	public void setPublicity(short _publicity)
	{
		publicity_ = _publicity;
		setreg_[4] = true;
	}

	/**
	 * Getter for F_Points
	 *
	 */
	public int getPoints()
	{
		return points_;
	}

	/**
	 * Setter for F_Points
	 *
	 */
	public void setPoints(int _points)
	{
		points_ = _points;
		setreg_[5] = true;
	}

	/**
	 * Getter for F_Members
	 *
	 */
	public int getMembers()
	{
		return members_;
	}

	/**
	 * Setter for F_Members
	 *
	 */
	public void setMembers(int _members)
	{
		members_ = _members;
		setreg_[6] = true;
	}

	/**
	 * Getter for F_Desc
	 *
	 */
	public String getDesc()
	{
		return desc_;
	}

	/**
	 * Setter for F_Desc
	 *
	 */
	public void setDesc(String _desc)
	{
		desc_ = _desc;
		setreg_[7] = true;
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
	 * @param _groupid - int
	 */
	public void queryByPrimaryKey(int _groupid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_Groups where F_GroupID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,_groupid);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				setGroupID(rs.getInt(1));
				setGroupName(rs.getString(2));
				setLeader(rs.getInt(3));
				setCreateDate(rs.getDate(4));
				setPublicity(rs.getShort(5));
				setPoints(rs.getInt(6));
				setMembers(rs.getInt(7));
				setDesc(rs.getString(8));
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
	 * 
	 *
	 * @param offset - int
	 * @param limit - int
	 */
	public ArrayList<GroupRec> queryPublicGroups(int offset, int limit) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select F_GroupID,F_GroupName,F_Publicity,F_CreateDate,F_Members,F_Realname from T_Groups G,T_Users U where G.F_Publicity>0 and G.F_Leader=U.F_UserID order by G.F_GroupName limit ? offset ?";
		ArrayList<GroupRec> result = new ArrayList<GroupRec>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,limit);
			stmt.setInt(2,offset);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				GroupRec s = new GroupRec();
				s.setId(rs.getInt(1));
				s.setName(rs.getString(2));
				s.setPublicity(rs.getShort(3));
				s.setDate(rs.getDate(4));
				s.setMembers(rs.getInt(5));
				s.setLeader(rs.getString(6));
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
	 * 
	 *
	 * @param offset - int
	 * @param limit - int
	 */
	public ArrayList<GroupRec> queryLatestPublicGroups(int offset, int limit) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select F_GroupID,F_GroupName,F_Publicity,F_CreateDate,F_Members,U.F_Realname from T_Groups G,T_Users U where G.F_Publicity>0 and G.F_Leader=U.F_UserID order by G.F_CreateDate desc limit ? offset ?";
		ArrayList<GroupRec> result = new ArrayList<GroupRec>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,limit);
			stmt.setInt(2,offset);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				GroupRec s = new GroupRec();
				s.setId(rs.getInt(1));
				s.setName(rs.getString(2));
				s.setPublicity(rs.getShort(3));
				s.setDate(rs.getDate(4));
				s.setMembers(rs.getInt(5));
				s.setLeader(rs.getString(6));
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
	 * 
	 *
	 * @param offset - int
	 * @param limit - int
	 */
	public ArrayList<GroupRec> queryLargestPublicGroups(int offset, int limit) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select F_GroupID,F_GroupName,F_Publicity,F_CreateDate,F_Members,U.F_Realname from T_Groups G,T_Users U where G.F_Publicity>0 and G.F_Leader=U.F_UserID order by G.F_Members desc limit ? offset ?";
		ArrayList<GroupRec> result = new ArrayList<GroupRec>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,limit);
			stmt.setInt(2,offset);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				GroupRec s = new GroupRec();
				s.setId(rs.getInt(1));
				s.setName(rs.getString(2));
				s.setPublicity(rs.getShort(3));
				s.setDate(rs.getDate(4));
				s.setMembers(rs.getInt(5));
				s.setLeader(rs.getString(6));
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
		String sql = "select * from T_Groups where F_GroupID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setInt(1, groupid_);
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();
			rs.moveToInsertRow();
			if (setreg_[0]) rs.updateInt(1,groupid_);
			if (setreg_[1]) rs.updateString(2,groupname_);
			if (setreg_[2]) rs.updateInt(3,leader_);
			if (setreg_[3]) rs.updateDate(4,createdate_);
			if (setreg_[4]) rs.updateShort(5,publicity_);
			if (setreg_[5]) rs.updateInt(6,points_);
			if (setreg_[6]) rs.updateInt(7,members_);
			if (setreg_[7]) rs.updateString(8,desc_);
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
		String sql = "select * from T_Groups where F_GroupID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setInt(1, groupid_);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				if (setreg_[1]) rs.updateString(2,groupname_);
				if (setreg_[2]) rs.updateInt(3,leader_);
				if (setreg_[3]) rs.updateDate(4,createdate_);
				if (setreg_[4]) rs.updateShort(5,publicity_);
				if (setreg_[5]) rs.updateInt(6,points_);
				if (setreg_[6]) rs.updateInt(7,members_);
				if (setreg_[7]) rs.updateString(8,desc_);
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
	 * @param _groupid - int
	 */
	public void delete(int _groupid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "delete from T_Groups where F_GroupID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,_groupid);
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
		String sql = "delete from T_Groups where F_GroupID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,groupid_);
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
