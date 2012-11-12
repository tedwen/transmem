//Generated code by table2java.py, do not modify.
package com.transmem.data.db;

/**
 * Data encapsulation class from struct in database table definition.
 * This class is generated for Groups.
 */
public class GroupRec
{
	private int id_;
	private String name_;
	private short publicity_;
	private java.sql.Date date_;
	private int members_;
	private String leader_;

	public int getId()
	{
		return id_;
	}

	public void setId(int _id)
	{
		id_ = _id;
	}

	public String getName()
	{
		return name_;
	}

	public void setName(String _name)
	{
		name_ = _name;
	}

	public short getPublicity()
	{
		return publicity_;
	}

	public void setPublicity(short _publicity)
	{
		publicity_ = _publicity;
	}

	public java.sql.Date getDate()
	{
		return date_;
	}

	public void setDate(java.sql.Date _date)
	{
		date_ = _date;
	}

	public int getMembers()
	{
		return members_;
	}

	public void setMembers(int _members)
	{
		members_ = _members;
	}

	public String getLeader()
	{
		return leader_;
	}

	public void setLeader(String _leader)
	{
		leader_ = _leader;
	}

}
