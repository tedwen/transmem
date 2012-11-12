package com.transmem.action;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;

import com.transmem.data.db.Databases;
import com.transmem.data.db.Users;
import com.transmem.data.db.Groups;
import com.transmem.data.db.GroupRec;
import com.transmem.data.db.Roles;

/**
 * Action class for Group-related operations.
 *
 * @version 0.1
 * @author Ted Wen
 * @date May. 2007
 */
public class GroupAction extends BaseAction
{
	private Logger log_ = Logger.getLogger(GroupAction.class.getName());
	
	private int pagelimit_ = 25;

	public GroupAction()
	{
		super();
	}
	
	/**
	 * Execute GroupAction operations.
	 *
	 * Request parameters:
	 *		operation: 
	 *			creategroup
	 *			updategroup
	 *			deletegroup
	 *			invite
	 *			kick
	 *			changeleader
	 *			points2group
	 *			points2member
	 *			joingroup
	 *			quitgroup
	 *		variables:
	 *			userid
	 *			points
	 *			group
	 *			grpage
	 */
	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("GroupAction","execute");

		Session session = param.getSession();

		Users user = session.getUser();
		if (user == null) {
			log_.severe("session.getAttribute('user') returned null");
			param.sendError(MessageCode.ERR_NOT_LOGIN);
			return;
		}

		String op = param.getParameter("operation");
		
		Connection conn = null;
		try
		{
			conn = getConnection(param,Databases.CATEGORY_MAIN, true);
			conn.setAutoCommit(false);

			if (op == null)
			{
				//no operation, just show page
			}
			else if (op.equals("creategroup"))
			{
				//create a new group
				createGroup(conn, user, param);
			}
			else if (op.equals("updategroup"))
			{
				//update group info
				updateGroup(conn, user, param);
			}
			else if (op.equals("deletegroup"))
			{
				//dismiss a group
				deleteGroup(conn, user);
				//TODO: notify all members
			}
			else if (op.equals("invite"))
			{
				//invite sb into the group
				invite2group(conn, user, param);
				//TODO: notify user
			}
			else if (op.equals("kick"))
			{
				//kick member out of group
				kickmember(conn, user, param);
				//TODO: notify user
			}
			else if (op.equals("changeleader"))
			{
				changeLeader(conn, user, param);
				//TODO: notify all members
			}
			else if (op.equals("points2group"))
			{
				//transfer leaders points to group account
				transferPointsToGroup(conn, user, param);
				//TODO: notify all members
			}
			else if (op.equals("points2member"))
			{
				//transfer some points from group account to a member
				transferPointsToMember(conn, user, param);
				//TODO: notify member
			}
			else if (op.equals("joingroup"))
			{
				//common user joins a group, check group publicity
				joinGroup(conn, user, param);
				//TODO: notify all members
			}
			else if (op.equals("quitgroup"))
			{
				//member quit a group
				quitGroup(conn, user);
				//TODO: notify all members
			}
			//next, send back proper page to show my group or a list of other groups
			if (user.getRole().equals("G"))
			{
				String grpage = param.getParameter("grpage");
				if (grpage == null || grpage.equals("mygroup"))
				{
					if (user.getGroup() > 0)
					{
						//User is a group leader and has a group
						Groups g = new Groups(conn, user.getGroup());
						session.setMyGroup(g);
						Users usrs = new Users(conn);
						ArrayList<Users> members = usrs.queryByGroup(user.getGroup());
						session.setMemberList(members);
					}
					else
					{
						//User can be a group leader, but has no group
						session.removeMyGroup();
					}
					setNextPage(PageLinks.MYGROUP_PAGE);
				}
				else
				{
					//grpage == 'list', display others
					loadGroups(conn, session, param);
					setNextPage(PageLinks.GROUPLIST_PAGE);
				}
			}
			else
			{
				//user is not group leader
				if (user.getGroup() > 0)
				{
					//user is in a group
					Groups grp = new Groups(conn, user.getGroup());
					session.setMyGroup(grp);
					loadGroups(conn, session, param);
				}
				else
				{
					//user has no group
					session.removeMyGroup();
					loadGroups(conn, session, param);
				}
				setNextPage(PageLinks.GROUP_PAGE);
			}
			conn.commit();
		}
		catch (SQLException e)
		{
			try { conn.rollback(); } catch (Exception x) {}
			log_.severe(e.getMessage());
			param.sendError(900,e.toString());
		}
		finally
		{
			if (conn != null)
				try { conn.close(); } catch (SQLException x) {}
		}
	}
	
	/**
	 * Load a page of public groups required as ArrayList<GroupRec>, and store in session.
	 * @param conn - Connection reference
	 * @param session - Session to hold attributes
	 * @param param - ServletParams containing request parameters
	 */
	protected void loadGroups(Connection conn, Session session, ServletParams param) throws SQLException
	{
		String orderby = param.getParameter("order");
		if (orderby == null)
			orderby = session.getGroupOrder();
		else
			session.setGroupOrder(orderby);
		//determine which page to load
		String offsets = param.getParameter("offset");
		if (offsets == null)
			offsets = session.getGroupOffset();
		else
			session.setGroupOffset(offsets);

		int offset = 0;
		if (offsets != null)
		{
			try
			{
				offset = Integer.parseInt(offsets);
			}
			catch (Exception x)
			{
			}
		}
		Groups g = new Groups(conn);
		ArrayList<GroupRec> grs = null;
		if (orderby == null)
			grs = g.queryPublicGroups(offset, this.pagelimit_);
		else if (orderby.equals("date"))
			grs = g.queryLatestPublicGroups(offset, this.pagelimit_);
		else if (orderby.equals("members"))
			grs = g.queryLargestPublicGroups(offset, this.pagelimit_);
		else
			grs = g.queryPublicGroups(offset, this.pagelimit_);
		session.setGroupList(grs);
	}

	/**
	 * Get user id string from request paraemter 'userid' and create Users object from DB.
	 */
	private Users getUser(Connection conn, ServletParams param) throws SQLException
	{
		String uids = param.getParameter("userid");
		if (uids == null)
		{
			throw new SQLException("userid not specified");
		}
		try
		{
			int uid = Integer.parseInt(uids);
			if (uid <= 0)
			{
				throw new SQLException("userid zero or negative");
			}
			return new Users(conn, uid);
		}
		catch (NumberFormatException nfe)
		{
			throw new SQLException("userid '"+uids+"' invalid");
		}
	}

	/**
	 * Find out the points from request parameter 'points'.
	 */
	private int getPoints(ServletParams param) throws SQLException
	{
		String pts = param.getParameter("points");
		if (pts == null)
		{
			throw new SQLException("No points to transfer");
		}
		int points = 0;
		try
		{
			points = Integer.parseInt(pts);
		}
		catch (NumberFormatException ne)
		{
			throw new SQLException("Points invalid");
		}
		if (points <= 0)
		{
			throw new SQLException("Points zero or negative");
		}
		return points;
	}

	/**
	 * Transfer some points from group leader account to the group account.
	 * @param conn - Connection to use
	 * @param user - Users of the group leader
	 * @param param - request paraemters
	 */
	protected void transferPointsToGroup(Connection conn, Users user, ServletParams param) throws SQLException
	{
		log_.info("enter transferPointsToGroup");
		int points = getPoints(param);
		Groups grp = new Groups(conn, user.getGroup());
		if (grp.getLeader() != user.getUserID())
		{
			throw new SQLException("User not group leader");
		}
		if (user.getPoints()<points)
		{
			throw new SQLException("User does not have enough points");
		}
		grp.clearUpdates();
		grp.setPoints(grp.getPoints()+points);
		log_.info("about to add "+points+" points to group");
		grp.update();
		user.clearUpdates();
		user.setPoints(user.getPoints()-points);
		log_.info("about decrease points from group leader");
		user.update(conn);
		log_.info("transaction complete");
	}

	/**
	 * Transfer some group points to the specified member.
	 * @param conn - Connection to use
	 * @param user - Users of the group leader
	 * @param param - request paraemters
	 */
	protected void transferPointsToMember(Connection conn, Users user, ServletParams param) throws SQLException
	{
		log_.info("enter transferPointsToMember");
		//get points
		int points = getPoints(param);
		//group to get points from
		Groups grp = new Groups(conn, user.getGroup());
		if (grp.getLeader() != user.getUserID())
		{
			throw new SQLException("User not group leader");
		}
		if (grp.getPoints()<points)
		{
			throw new SQLException("Group does not have enough points");
		}
		//find user to transfer points to
		Users member = getUser(conn, param);
		if (member.getGroup()!=grp.getGroupID())
		{
			throw new SQLException("User not a group member");
		}
		member.clearUpdates();
		member.setPoints(member.getPoints()+points);
		log_.info("about to add "+points+" points to member "+member.getUsername());
		member.update();
		grp.clearUpdates();
		grp.setPoints(grp.getPoints()-points);
		log_.info("about to delete points from group");
		grp.update();
		log_.info("transaction complete");
	}
	
	/**
	 * A common user joins a selected group.
	 */
	protected void joinGroup(Connection conn, Users user, ServletParams param) throws SQLException
	{
		String grps = param.getParameter("group");
		if (grps == null)
		{
			throw new SQLException("Parameter group not specified");
		}
		int grpid = 0;
		try
		{
			grpid = Integer.parseInt(grps);
		}
		catch (NumberFormatException nfe)
		{
			throw new SQLException("Parameter group '"+grps+"' invalid group ID");
		}
		Groups grp = new Groups(conn, grpid);
		if (grp.getPublicity()==3)
		{
			user.clearUpdates();
			user.setGroup(grpid);
			user.update(conn);
			grp.clearUpdates();
			grp.setMembers(grp.getMembers()+1);
			grp.update();
		}
		else if (grp.getPublicity()==2)
		{
			//TODO: send a message to group leader
			log_.info("User '"+user.getRealname()+"' applies to join group "+grpid);
		}
		else
		{
			log_.warning("User '"+user.getRealname()+"' wants to join a closed group: "+grpid);
		}
	}

	/**
	 * A group member quits the group.
	 */
	protected void quitGroup(Connection conn, Users user) throws SQLException
	{
		if (user.getGroup() <= 0)
			throw new SQLException("User has not joined a group");
		Groups grp = new Groups(conn, user.getGroup());
		if (grp != null && grp.getLeader()==user.getUserID())
			throw new SQLException("Group leader cannot quit");
		//if (grp == null)
		//	throw new Exception("Group "+user.getGroup()+" no longer exists");
		user.clearUpdates();
		user.setGroup(0);
		user.update(conn);
		grp.clearUpdates();
		grp.setMembers(grp.getMembers()-1);
		grp.update();
	}

	/**
	 * Change group leader to a member.
	 * @param conn - Connection to use
	 * @param user - Users of the group leader
	 * @param param - request paraemters
	 */
	protected void changeLeader(Connection conn, Users user, ServletParams param) throws SQLException
	{
		log_.info("enter changeLeader");
		String member = param.getParameter("userid");
		if (member == null)
		{
			throw new SQLException("User not specified to change leader");
		}
		int uid = 0;
		try
		{
			uid = Integer.parseInt(member);
		}
		catch (NumberFormatException ne)
		{
			throw new SQLException("User ID invalid for changing leader");
		}
		Groups grp = new Groups(conn, user.getGroup());
		if (grp.getLeader() != user.getUserID())
		{
			throw new SQLException("User not group leader");
		}
		Users usr = new Users(conn, uid);
		if (usr.getGroup()!=grp.getGroupID())
		{
			throw new SQLException("User not a group member");
		}
		Roles r = new Roles(conn, usr.getRole());
		if (r.getLevel() < 1000)
		{
			throw new SQLException("Member '"+usr.getRealname()+"' not super user");
		}
		log_.info("about to set leader to "+uid);
		grp.setLeader(uid);
		log_.info("group leader changed");
	}
	
	/**
	 * Invite somebody to join the group. It's automatically joined.
	 * @param conn - Connection to use
	 * @param user - Users of the group leader
	 * @param param - request paraemters
	 */
	protected void invite2group(Connection conn, Users user, ServletParams param) throws SQLException
	{
		log_.info("enter invite2group");
		String suser = param.getParameter("userid");
		if (suser != null)
		{
			int uid = 0;
			try
			{
				uid = Integer.parseInt(suser);
			}
			catch (Exception x)
			{
				throw new SQLException("User ID invalid");
			}
			int grpid = user.getGroup();
			if (grpid > 0)
			{
				Groups grp = new Groups(conn, grpid);
				if (user.getUserID() == grp.getLeader())
				{
					Users member = new Users(conn, uid);
					if (member.getGroup()==0)
					{
						member.clearUpdates();
						member.setGroup(grpid);
						log_.info("about to update member, set group to "+grpid);
						member.update(conn);
						grp.clearUpdates();
						grp.setMembers(grp.getMembers()+1);
						log_.info("about to update group, add member");
						grp.update();
					}
					else
					{
						log_.info("User has joined another group");
						throw new SQLException("User has joined another group");
					}
				}
				else
				{
					log_.warning("Not group leader, can't invite member");
					throw new SQLException("You are not group leader");
				}
			}
			else
			{
				log_.warning("User "+user.getUsername()+" has no group, but wants to add sb");
			}
		}
		else
		{
			log_.warning("Invite nobody to group");
			throw new SQLException("Invite nobody to group");
		}
	}
	
	/**
	 * The leader kick a group member out of the group.
	 * @param conn - Connection
	 * @param user - Users object for the leader
	 * @param param - request containing parameters
	 */
	protected void kickmember(Connection conn, Users user, ServletParams param) throws SQLException
	{
		log_.info("enter kickmember");
		int uid = 0;
		String su = param.getParameter("uid");
		if (su == null)
		{
			throw new SQLException("Member uid not given");
		}
		if (user.getGroup()==0)
		{
			throw new SQLException("User has no group");
		}
		Groups grp = new Groups(conn, user.getGroup());
		if (user.getUserID()!=grp.getLeader())
		{
			throw new SQLException("User is not group leader");
		}
		try
		{
			uid = Integer.parseInt(su);
		}
		catch (Exception x)
		{
			throw new SQLException("Member uid invalid");
		}
		Users usr = new Users(conn, uid);
		usr.clearUpdates();
		usr.setGroup(0);
		log_.info("about to update user, clear group");
		usr.update();
		grp.clearUpdates();
		grp.setMembers(grp.getMembers()-1);
		log_.info("about to update group, decrease member");
		grp.update();
		log_.info("group updated");
	}

	/**
	 * Create a Group account and insert into T_Groups table
	 * @param conn - Connection to use
	 * @param user - Users object containing user ID for the group leader/creator
	 * @param param - parameter containing request parameters
	 */
	protected void createGroup(Connection conn, Users user, ServletParams param) throws SQLException
	{
		log_.info("enter createGroup");
		String groupName = param.getParameter("GroupName");
		String publicity = param.getParameter("Publicity");
		String description = param.getParameter("Description");
		short pub = 3;
		try
		{
			pub = (short)Integer.parseInt(publicity);
		}
		catch (NumberFormatException fe)
		{
		}
		int gid = getSequenceInt(conn, "S_Groups");
		Groups grp = new Groups(conn);
		grp.setGroupID(gid);
		grp.setGroupName(groupName);
		grp.setLeader(user.getUserID());
		grp.setPublicity(pub);
		grp.setDesc(description);
		log_.info("about to insert group");
		grp.insert();
		log_.info("group "+gid+" inserted");
	}
	
	/**
	 * Update a group info
	 * @param conn
	 * @param user - group leader
	 * @param param - request containing parameters
	 */
	protected void updateGroup(Connection conn, Users user, ServletParams param) throws SQLException
	{
		log_.info("enter updateGroup");
		String groupName = param.getParameter("GroupName");
		String publicity = param.getParameter("Publicity");
		String description = param.getParameter("Description");
		short pub = 3;
		if (publicity != null)
		try
		{
			pub = (short)Integer.parseInt(publicity);
		}
		catch (NumberFormatException fe)
		{
		}
		int gid = user.getGroup();
		if (gid < 1)
		{
			throw new SQLException("User has not group to update");
		}
		Groups grp = new Groups(conn, gid);
		grp.clearUpdates();
		grp.setGroupName(groupName);
		grp.setLeader(user.getUserID());
		grp.setPublicity(pub);
		grp.setDesc(description);
		log_.info("about to update group, gid="+gid);
		grp.update();
		log_.info("update done");
	}
	
	/**
	 * Delete a group by the creator/leader
	 * @param conn - Connection to use
	 * @param user - Users object as the group owner
	 */
	protected void deleteGroup(Connection conn, Users user) throws SQLException
	{
		log_.info("enter deleteGroup");
		int grpid = user.getGroup();
		if (grpid > 0)
		{
			Groups grp = new Groups(conn, grpid);
			if (grpid == grp.getLeader())
			{
				//reset all user group for this group
				user.setConnection(conn);
				log_.info("about to clear members");
				user.resetGroup(grpid);
				//then delete the group
				log_.info("about to delete the group");
				grp.delete(conn);
				log_.info("group "+grpid+" deleted");
			}
			else
			{
				log_.warning("User '"+user.getUsername()+"' not group leader to delete group "+grp.getGroupID());
				throw new SQLException("No right to delete other's group");
			}
		}
		else
		{
			log_.warning("User '"+user.getUsername()+"' has no group to delete");
			throw new SQLException("User no group to delete");
		}
	}
}
