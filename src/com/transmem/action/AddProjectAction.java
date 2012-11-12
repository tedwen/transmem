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
 * Action class for adding a new project.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan. 2007
 */
public class AddProjectAction extends BaseAction
{
	private Logger log_ = Logger.getLogger(AddProjectAction.class.getName());

	public AddProjectAction()
	{
		super();
	}
	
	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("AddProjectAction","execute");

		String prjName = param.getParameter("projectName");
		String langPair = param.getParameter("langPair");

		if (prjName==null || prjName.length()<2 || langPair==null || langPair.length()<2) {
			log_.severe("AddProjectAction.execute: invalid projectName or langPair parameters in JSP");
			param.sendError(MessageCode.ERR_PROJECT_PARAMS);
			return;
		}

		Session session = param.getSession();

		Users usr = session.getUser();
		if (usr == null)
		{
			param.sendError(MessageCode.ERR_NOT_LOGIN);
			return;
		}

		Connection conn = null;
		try
		{
			conn = getConnection(param,Databases.CATEGORY_USER,true);
		}
		catch (SQLException x)
		{
			log_.severe("SQLException when getConnection(USER)"+x);
			param.sendError(MessageCode.ERR_DB_CONNECT);
			return;
		}
		Projects project = new Projects(conn);
		project.setProjectName(prjName);
		project.setLangPair(langPair);
		project.setCreator(usr.getUserID());
		try
		{
			Roles r = new Roles(conn, usr.getRole());
			usr.setConnection(conn);
			int numprjs = usr.countProjects(usr.getUserID());
			if (numprjs >= r.getLevel())
			{
				param.sendError("Too many projects for this level");
				//TODO: show a warning message instead of error page
			}
			else
			{
				project.insert();
				ArrayList<Projects> projects = project.queryProjectsByMember(usr.getUserID());
				session.setProjectList(projects);
				setNextPage(PageLinks.PROJECT_PAGE);
				numprjs ++;
				log_.info("New project stored in DB, and "+projects.size()+" projects reloaded into session");
			}
			if (numprjs >= r.getLevel())
			{
				session.setEnoughProjects();
			}
		}
		catch (SQLException x)
		{
			log_.severe("SQLException when insert project"+x);
			param.sendError(MessageCode.ERR_INSERT_PROJECT);
		}
		finally
		{
			if (conn != null)
				try { conn.close();	} catch (SQLException e) {}
		}
	}
}
