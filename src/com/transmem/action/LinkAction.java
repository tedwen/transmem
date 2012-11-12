package com.transmem.action;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;

import com.transmem.data.db.Databases;
import com.transmem.data.db.Users;
import com.transmem.data.db.Roles;
import com.transmem.data.db.Projects;

/**
 * Action class for redirection to either page projects.jsp or articles.jsp.
 *
 * @version 0.1
 * @author Ted Wen
 * @date May. 2007
 */
public class LinkAction extends BaseAction
{
	private	Logger log_ = Logger.getLogger(LinkAction.class.getName());

	public LinkAction()
	{
		super();
	}

	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("TranslateAction","execute");

		Session session = param.getSession();

		Users usr = session.getUser();
		if (usr == null)
		{
			param.sendError(MessageCode.ERR_NOT_LOGIN);
			return;
		}
		
		String page = param.getParameter("arg1");
		if (page == null)
		{
			log_.warning("param.getParameter('arg1') returned null");
			param.sendError(MessageCode.ERR_INVALID_LINKPAGE);
			return;
		}

		if (page.equals("projects"))
		{
			if (session.getProjectList()==null)
			{
				loadProjects(usr, session, param);
				if (session.getProjectList()==null)
				{
					log_.warning("Jump to projects page before projects loaded");
					param.sendError(MessageCode.ERR_INVALID_LINKPAGE);
					return;
				}
			}
			setNextPage(PageLinks.PROJECT_PAGE);
		}
		else if (page.equals("articles"))
		{
			if (session.getArticleList()==null)
			{
				log_.warning("Jump to articles page before articles loaded");
				param.sendError(MessageCode.ERR_INVALID_LINKPAGE);
				return;
			}
			setNextPage(PageLinks.ARTICLE_PAGE);
		}
		else
		{
			log_.warning("Not supported link page: "+page);
			param.sendError(MessageCode.ERR_INVALID_LINKPAGE);
			return;
		}
	}
	
	protected void loadProjects(Users usr, Session session, ServletParams param)
		throws ServletException, IOException
	{
		log_.info("enter loadProjects");
		Connection conn = null;
		try
		{
			conn = getConnection(param,Databases.CATEGORY_USER);
			Projects prjs = new Projects(conn);
			ArrayList<Projects> projects = prjs.queryProjectsByMember(usr.getUserID());
			session.setProjectList(projects);
			log_.info(projects.size()+" projects loaded and saved in session");
			Roles r = new Roles(conn, usr.getRole());
			usr.setConnection(conn);
			int numprjs = usr.countProjects(usr.getUserID());
			if (numprjs >= r.getLevel())
			{
				session.setEnoughProjects();
			}
			else
			{
				session.removeEnoughProjects();
			}
		}
		catch (SQLException x)
		{
			log_.severe("SQLException when getConnection(USER)"+x);
			param.sendError(MessageCode.ERR_DB_CONNECT);
		}
		finally
		{
			if (conn != null)
				try { conn.close();	} catch (SQLException e) {}
		}
	}
}
