//Generated code by table2java.py, do not modify.
package com.transmem.data.db;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Users table
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan.2007
 */
public class Users implements java.io.Serializable
{
	protected static final long serialVersionUID = 110L;
	private Logger log_ = Logger.getLogger(Users.class.getName());

	private boolean[] setreg_ = new boolean[23];

	private Connection con_;
	private int userid_; ///auto increment user ID
	private String username_; ///unique login name
	private String realname_; ///real name or nickname
	private String sex_; ///Male or Female
	private String password_; ///MD5 passcode
	private java.sql.Date regdate_; ///register date
	private java.sql.Date birthday_; ///birthdate YYYYMMDD
	private String membership_; ///Translator,Company,etc
	private String role_; ///Role ID
	private int points_; ///Number of points the user can buy
	private int credits_; ///Special points for ranking
	private String email_; ///email address
	private String mobile_; ///mobile phone number
	private String idtype_; ///Identity type, ID, PP, etc
	private String idnumber_; ///Identity number
	private String question_; ///question given by user
	private String answer_; ///answer to the question for passcode reset
	private String state_; ///Active, Disabled, Expired
	private int work_; ///Article ID the user is working on
	private int group_; ///group the user joined
	private java.sql.Date lastvisit_; ///date of last visit
	private int visitcount_; ///number of visits
	private int shared_; ///number of shared sentences

	/**
	 * Construct an empty Users object.
	 *
	 */
	public Users()
	{
	}

	/**
	 * Construct a Users object with a Connection instance.
	 *
	 * @param con - Connection object
	 */
	public Users(Connection con)
	{
		con_ = con;
	}

	/**
	 * Construct with a query on primary key(s)
	 *
	 */
	public Users(Connection con, int _userid) throws SQLException
	{
		con_ = con;
		queryByPrimaryKey(_userid);
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
	 * Getter for F_UserID
	 *
	 */
	public int getUserID()
	{
		return userid_;
	}

	/**
	 * Setter for F_UserID
	 *
	 */
	public void setUserID(int _userid)
	{
		userid_ = _userid;
		setreg_[0] = true;
	}

	/**
	 * Getter for F_Username
	 *
	 */
	public String getUsername()
	{
		return username_;
	}

	/**
	 * Setter for F_Username
	 *
	 */
	public void setUsername(String _username)
	{
		username_ = _username;
		setreg_[1] = true;
	}

	/**
	 * Getter for F_Realname
	 *
	 */
	public String getRealname()
	{
		return realname_;
	}

	/**
	 * Setter for F_Realname
	 *
	 */
	public void setRealname(String _realname)
	{
		realname_ = _realname;
		setreg_[2] = true;
	}

	/**
	 * Getter for F_Sex
	 *
	 */
	public String getSex()
	{
		return sex_;
	}

	/**
	 * Setter for F_Sex
	 *
	 */
	public void setSex(String _sex)
	{
		sex_ = _sex;
		setreg_[3] = true;
	}

	/**
	 * Getter for F_Password
	 *
	 */
	public String getPassword()
	{
		return password_;
	}

	/**
	 * Setter for F_Password
	 *
	 */
	public void setPassword(String _password)
	{
		password_ = _password;
		setreg_[4] = true;
	}

	/**
	 * Getter for F_Regdate
	 *
	 */
	public java.sql.Date getRegdate()
	{
		return regdate_;
	}

	/**
	 * Setter for F_Regdate
	 *
	 */
	public void setRegdate(java.sql.Date _regdate)
	{
		regdate_ = _regdate;
		setreg_[5] = true;
	}

	/**
	 * Getter for F_Birthday
	 *
	 */
	public java.sql.Date getBirthday()
	{
		return birthday_;
	}

	/**
	 * Setter for F_Birthday
	 *
	 */
	public void setBirthday(java.sql.Date _birthday)
	{
		birthday_ = _birthday;
		setreg_[6] = true;
	}

	/**
	 * Getter for F_Membership
	 *
	 */
	public String getMembership()
	{
		return membership_;
	}

	/**
	 * Setter for F_Membership
	 *
	 */
	public void setMembership(String _membership)
	{
		membership_ = _membership;
		setreg_[7] = true;
	}

	/**
	 * Getter for F_Role
	 *
	 */
	public String getRole()
	{
		return role_;
	}

	/**
	 * Setter for F_Role
	 *
	 */
	public void setRole(String _role)
	{
		role_ = _role;
		setreg_[8] = true;
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
		setreg_[9] = true;
	}

	/**
	 * Getter for F_Credits
	 *
	 */
	public int getCredits()
	{
		return credits_;
	}

	/**
	 * Setter for F_Credits
	 *
	 */
	public void setCredits(int _credits)
	{
		credits_ = _credits;
		setreg_[10] = true;
	}

	/**
	 * Getter for F_Email
	 *
	 */
	public String getEmail()
	{
		return email_;
	}

	/**
	 * Setter for F_Email
	 *
	 */
	public void setEmail(String _email)
	{
		email_ = _email;
		setreg_[11] = true;
	}

	/**
	 * Getter for F_Mobile
	 *
	 */
	public String getMobile()
	{
		return mobile_;
	}

	/**
	 * Setter for F_Mobile
	 *
	 */
	public void setMobile(String _mobile)
	{
		mobile_ = _mobile;
		setreg_[12] = true;
	}

	/**
	 * Getter for F_IdType
	 *
	 */
	public String getIdType()
	{
		return idtype_;
	}

	/**
	 * Setter for F_IdType
	 *
	 */
	public void setIdType(String _idtype)
	{
		idtype_ = _idtype;
		setreg_[13] = true;
	}

	/**
	 * Getter for F_IdNumber
	 *
	 */
	public String getIdNumber()
	{
		return idnumber_;
	}

	/**
	 * Setter for F_IdNumber
	 *
	 */
	public void setIdNumber(String _idnumber)
	{
		idnumber_ = _idnumber;
		setreg_[14] = true;
	}

	/**
	 * Getter for F_Question
	 *
	 */
	public String getQuestion()
	{
		return question_;
	}

	/**
	 * Setter for F_Question
	 *
	 */
	public void setQuestion(String _question)
	{
		question_ = _question;
		setreg_[15] = true;
	}

	/**
	 * Getter for F_Answer
	 *
	 */
	public String getAnswer()
	{
		return answer_;
	}

	/**
	 * Setter for F_Answer
	 *
	 */
	public void setAnswer(String _answer)
	{
		answer_ = _answer;
		setreg_[16] = true;
	}

	/**
	 * Getter for F_State
	 *
	 */
	public String getState()
	{
		return state_;
	}

	/**
	 * Setter for F_State
	 *
	 */
	public void setState(String _state)
	{
		state_ = _state;
		setreg_[17] = true;
	}

	/**
	 * Getter for F_Work
	 *
	 */
	public int getWork()
	{
		return work_;
	}

	/**
	 * Setter for F_Work
	 *
	 */
	public void setWork(int _work)
	{
		work_ = _work;
		setreg_[18] = true;
	}

	/**
	 * Getter for F_Group
	 *
	 */
	public int getGroup()
	{
		return group_;
	}

	/**
	 * Setter for F_Group
	 *
	 */
	public void setGroup(int _group)
	{
		group_ = _group;
		setreg_[19] = true;
	}

	/**
	 * Getter for F_LastVisit
	 *
	 */
	public java.sql.Date getLastVisit()
	{
		return lastvisit_;
	}

	/**
	 * Setter for F_LastVisit
	 *
	 */
	public void setLastVisit(java.sql.Date _lastvisit)
	{
		lastvisit_ = _lastvisit;
		setreg_[20] = true;
	}

	/**
	 * Getter for F_VisitCount
	 *
	 */
	public int getVisitCount()
	{
		return visitcount_;
	}

	/**
	 * Setter for F_VisitCount
	 *
	 */
	public void setVisitCount(int _visitcount)
	{
		visitcount_ = _visitcount;
		setreg_[21] = true;
	}

	/**
	 * Getter for F_Shared
	 *
	 */
	public int getShared()
	{
		return shared_;
	}

	/**
	 * Setter for F_Shared
	 *
	 */
	public void setShared(int _shared)
	{
		shared_ = _shared;
		setreg_[22] = true;
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
	 * @param _userid - int
	 */
	public void queryByPrimaryKey(int _userid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_Users where F_UserID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,_userid);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				setUserID(rs.getInt(1));
				setUsername(rs.getString(2));
				setRealname(rs.getString(3));
				setSex(rs.getString(4));
				setPassword(rs.getString(5));
				setRegdate(rs.getDate(6));
				setBirthday(rs.getDate(7));
				setMembership(rs.getString(8));
				setRole(rs.getString(9));
				setPoints(rs.getInt(10));
				setCredits(rs.getInt(11));
				setEmail(rs.getString(12));
				setMobile(rs.getString(13));
				setIdType(rs.getString(14));
				setIdNumber(rs.getString(15));
				setQuestion(rs.getString(16));
				setAnswer(rs.getString(17));
				setState(rs.getString(18));
				setWork(rs.getInt(19));
				setGroup(rs.getInt(20));
				setLastVisit(rs.getDate(21));
				setVisitCount(rs.getInt(22));
				setShared(rs.getInt(23));
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
	 * Check whether a username exists in the table. It returns 1 if found, otherwise 0.
	 *
	 * @param uname - String
	 */
	public int checkUsername(String uname) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select count(*) from T_Users where F_Username=?";
		int result = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,uname);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				result = rs.getInt(1);
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
	 * Query a record for login, must match username and password.
	 *
	 * @param uname - String
	 * @param pcode - String
	 */
	public Users queryLogin(String uname, String pcode) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_Users where F_Username=? and F_Password=?";
		Users result = this;	//populate this instance
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,uname);
			stmt.setString(2,pcode);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				result.setUserID(rs.getInt(1));
				result.setUsername(rs.getString(2));
				result.setRealname(rs.getString(3));
				result.setSex(rs.getString(4));
				result.setPassword(rs.getString(5));
				result.setRegdate(rs.getDate(6));
				result.setBirthday(rs.getDate(7));
				result.setMembership(rs.getString(8));
				result.setRole(rs.getString(9));
				result.setPoints(rs.getInt(10));
				result.setCredits(rs.getInt(11));
				result.setEmail(rs.getString(12));
				result.setMobile(rs.getString(13));
				result.setIdType(rs.getString(14));
				result.setIdNumber(rs.getString(15));
				result.setQuestion(rs.getString(16));
				result.setAnswer(rs.getString(17));
				result.setState(rs.getString(18));
				result.setWork(rs.getInt(19));
				result.setGroup(rs.getInt(20));
				result.setLastVisit(rs.getDate(21));
				result.setVisitCount(rs.getInt(22));
				result.setShared(rs.getInt(23));
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
	 * Query a unique record by username
	 *
	 * @param uname - String
	 */
	public Users queryByUsername(String uname) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_Users where F_Username=?";
		Users result = this;	//populate this instance
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setString(1,uname);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				result.setUserID(rs.getInt(1));
				result.setUsername(rs.getString(2));
				result.setRealname(rs.getString(3));
				result.setSex(rs.getString(4));
				result.setPassword(rs.getString(5));
				result.setRegdate(rs.getDate(6));
				result.setBirthday(rs.getDate(7));
				result.setMembership(rs.getString(8));
				result.setRole(rs.getString(9));
				result.setPoints(rs.getInt(10));
				result.setCredits(rs.getInt(11));
				result.setEmail(rs.getString(12));
				result.setMobile(rs.getString(13));
				result.setIdType(rs.getString(14));
				result.setIdNumber(rs.getString(15));
				result.setQuestion(rs.getString(16));
				result.setAnswer(rs.getString(17));
				result.setState(rs.getString(18));
				result.setWork(rs.getInt(19));
				result.setGroup(rs.getInt(20));
				result.setLastVisit(rs.getDate(21));
				result.setVisitCount(rs.getInt(22));
				result.setShared(rs.getInt(23));
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
	 * Query all users in a given group
	 *
	 * @param group - int
	 */
	public ArrayList<Users> queryByGroup(int group) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select * from T_Users where F_Group=? order by F_Username";
		ArrayList<Users> result = new ArrayList<Users>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,group);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				Users s = new Users();
				s.setUserID(rs.getInt(1));
				s.setUsername(rs.getString(2));
				s.setRealname(rs.getString(3));
				s.setSex(rs.getString(4));
				s.setPassword(rs.getString(5));
				s.setRegdate(rs.getDate(6));
				s.setBirthday(rs.getDate(7));
				s.setMembership(rs.getString(8));
				s.setRole(rs.getString(9));
				s.setPoints(rs.getInt(10));
				s.setCredits(rs.getInt(11));
				s.setEmail(rs.getString(12));
				s.setMobile(rs.getString(13));
				s.setIdType(rs.getString(14));
				s.setIdNumber(rs.getString(15));
				s.setQuestion(rs.getString(16));
				s.setAnswer(rs.getString(17));
				s.setState(rs.getString(18));
				s.setWork(rs.getInt(19));
				s.setGroup(rs.getInt(20));
				s.setLastVisit(rs.getDate(21));
				s.setVisitCount(rs.getInt(22));
				s.setShared(rs.getInt(23));
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
	 * Query a user's name by his ID
	 *
	 * @param id - int
	 */
	public String queryNameByID(int id) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select F_Username from T_Users where F_UserID=?";
		String result = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,id);
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
	 * Count the number of projects the author created
	 *
	 * @param user - int
	 */
	public int countProjects(int user) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select count(*) from T_Projects where F_Creator=?";
		int result = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,user);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				result = rs.getInt(1);
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
	 * Count the number of articles in all projects the author uploaded
	 *
	 * @param user - int
	 */
	public int countArticles(int user) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select count(*) from T_Articles A,T_Projects P where P.F_Creator=? and A.F_Project=P.F_ProjectID";
		int result = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,user);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				result = rs.getInt(1);
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
	 * Count the number of articles translated by the author
	 *
	 * @param user - int
	 */
	public int countTranslations(int user) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select count(*) from T_Articles where F_Translator=? and F_Progress>0.9";
		int result = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,user);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				result = rs.getInt(1);
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
	 * Query the group name of group the user joined
	 *
	 * @param group - int
	 */
	public String queryGroupName(int group) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select F_GroupName from T_Groups where F_GroupID=?";
		String result = "";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,group);
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
	 * Query language pairs a translator has done
	 *
	 * @param translator - int
	 */
	public ArrayList<LangPair> queryLangPairByTranslator(int translator) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "select distinct F_LangPair from T_Articles where F_Translator=?";
		ArrayList<LangPair> result = new ArrayList<LangPair>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,translator);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				LangPair s = new LangPair();
				s.setLp(rs.getString(1));
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
		String sql = "select * from T_Users where F_UserID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setInt(1, userid_);
			stmt.setFetchSize(1);
			rs = stmt.executeQuery();
			rs.moveToInsertRow();
			if (setreg_[0]) rs.updateInt(1,userid_);
			if (setreg_[1]) rs.updateString(2,username_);
			if (setreg_[2]) rs.updateString(3,realname_);
			if (setreg_[3]) rs.updateString(4,sex_);
			if (setreg_[4]) rs.updateString(5,password_);
			if (setreg_[5]) rs.updateDate(6,regdate_);
			if (setreg_[6]) rs.updateDate(7,birthday_);
			if (setreg_[7]) rs.updateString(8,membership_);
			if (setreg_[8]) rs.updateString(9,role_);
			if (setreg_[9]) rs.updateInt(10,points_);
			if (setreg_[10]) rs.updateInt(11,credits_);
			if (setreg_[11]) rs.updateString(12,email_);
			if (setreg_[12]) rs.updateString(13,mobile_);
			if (setreg_[13]) rs.updateString(14,idtype_);
			if (setreg_[14]) rs.updateString(15,idnumber_);
			if (setreg_[15]) rs.updateString(16,question_);
			if (setreg_[16]) rs.updateString(17,answer_);
			if (setreg_[17]) rs.updateString(18,state_);
			if (setreg_[18]) rs.updateInt(19,work_);
			if (setreg_[19]) rs.updateInt(20,group_);
			if (setreg_[20]) rs.updateDate(21,lastvisit_);
			if (setreg_[21]) rs.updateInt(22,visitcount_);
			if (setreg_[22]) rs.updateInt(23,shared_);
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
		String sql = "select * from T_Users where F_UserID=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			stmt.setInt(1, userid_);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				if (setreg_[1]) rs.updateString(2,username_);
				if (setreg_[2]) rs.updateString(3,realname_);
				if (setreg_[3]) rs.updateString(4,sex_);
				if (setreg_[4]) rs.updateString(5,password_);
				if (setreg_[5]) rs.updateDate(6,regdate_);
				if (setreg_[6]) rs.updateDate(7,birthday_);
				if (setreg_[7]) rs.updateString(8,membership_);
				if (setreg_[8]) rs.updateString(9,role_);
				if (setreg_[9]) rs.updateInt(10,points_);
				if (setreg_[10]) rs.updateInt(11,credits_);
				if (setreg_[11]) rs.updateString(12,email_);
				if (setreg_[12]) rs.updateString(13,mobile_);
				if (setreg_[13]) rs.updateString(14,idtype_);
				if (setreg_[14]) rs.updateString(15,idnumber_);
				if (setreg_[15]) rs.updateString(16,question_);
				if (setreg_[16]) rs.updateString(17,answer_);
				if (setreg_[17]) rs.updateString(18,state_);
				if (setreg_[18]) rs.updateInt(19,work_);
				if (setreg_[19]) rs.updateInt(20,group_);
				if (setreg_[20]) rs.updateDate(21,lastvisit_);
				if (setreg_[21]) rs.updateInt(22,visitcount_);
				if (setreg_[22]) rs.updateInt(23,shared_);
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
	 * Reset all user group to zero for a given group to be deleted
	 *
	 * @param group - int
	 */
	public void resetGroup(int group) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "update T_Users set F_Group=0 where F_Group=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,group);
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
	 * @param _userid - int
	 */
	public void delete(int _userid) throws SQLException
	{
		if (this.con_ == null)
		{
			log_.severe("Connetion object null");
			throw new SQLException("Connection object not set.");
		}
		String sql = "delete from T_Users where F_UserID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,_userid);
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
		String sql = "delete from T_Users where F_UserID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = con_.prepareStatement(sql);
			stmt.setInt(1,userid_);
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
