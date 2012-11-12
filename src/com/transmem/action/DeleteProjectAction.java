package com.transmem.action;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;

import com.transmem.data.db.Databases;
import com.transmem.data.db.Projects;
import com.transmem.data.db.Users;
import com.transmem.data.db.Roles;

/**
 * Action class for deleting a project.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan. 2007
 */
public class DeleteProjectAction extends BaseAction
{
	private Logger log_ = Logger.getLogger(DeleteProjectAction.class.getName());

	public DeleteProjectAction()
	{
		super();
	}

	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("AddProjectAction","execute");

		Session session = param.getSession();

		Users usr = session.getUser();
		if (usr == null)
		{
			log_.severe("User not login");
			param.sendError(MessageCode.ERR_NOT_LOGIN);
			return;
		}
		
		String projectids = param.getParameter("project");
		int pid = 0;
		try
		{
			pid = Integer.parseInt(projectids);
		}
		catch (NumberFormatException e)
		{
			log_.severe("NumberFormatException when converting request parameter project to int:"+projectids);
			param.sendError(MessageCode.ERR_PROJECT_ID);
			return;
		}

		Connection conn = null;
		try
		{
			conn = getConnection(param,Databases.CATEGORY_USER,true);
			try
			{
				Projects project = new Projects(conn);
 				project.queryByPrimaryKey(pid);
				if (project.getProjectID()==pid)
				{
					try
					{
						project.delete(pid);
					} 
					catch (SQLException dx) 
					{
						log_.severe("SQLException when delete project"+dx);
						param.sendError(MessageCode.ERR_DELETE_PROJECT);
						return;
					}
					ArrayList<Projects> projects = project.queryProjectsByMember(usr.getUserID());
					session.setProjectList(projects);
					setNextPage(PageLinks.PROJECT_PAGE);
					//check for number projects
					Roles r = new Roles(conn, usr.getRole());
					int numprjs = projects.size();
					if (numprjs < r.getLevel())
					{
						session.removeEnoughProjects();
					}
					log_.info("project "+pid+" deleted, and "+projects.size()+" projects reloaded into session");
				}
				else
				{
					log_.warning("project id different,pid="+pid+" while loaded="+project.getProjectID());
					param.sendError(MessageCode.ERR_PROJECT_ID);
					return;
				}
			}
			catch (SQLException x)
			{
				log_.severe("SQLException when query project:"+pid);
				param.sendError(MessageCode.ERR_PROJECT_QUERY);
			}
		}
		catch (SQLException x)
		{
			log_.severe("SQLException when getConnection(USER)"+x);
			param.sendError(MessageCode.ERR_DB_CONNECT);
			return;
		}
		finally
		{
			if (conn != null)
				try { conn.close();	} catch (SQLException e) {}
		}
	}
}
