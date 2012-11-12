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
import com.transmem.data.db.Articles;

/**
 * Action class for openning a project to see a list of articles.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan. 2007
 */
public class OpenProjectAction extends BaseAction
{
	private Logger log_ = Logger.getLogger(OpenProjectAction.class.getName());

	public OpenProjectAction()
	{
		super();
	}

	/**
	 * Open a selected project and list all articles on articles page.
	 *
	 */
	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("OpenProjectAction","execute");

		Session session = param.getSession();

		Users usr = session.getUser();
		if (usr == null) {
			log_.severe("session.getAttribute('user') returned null");
			param.sendError(MessageCode.ERR_NOT_LOGIN);
			return;
		}

		Connection conn = null;
		try
		{
			conn = getConnection(param, Databases.CATEGORY_USER);
			int project = Integer.parseInt(param.getParameter("project"));	//save as Integer.toString()
			Articles ar = new Articles(conn);
			ArrayList<Articles> articles = ar.queryByProject(project);
			session.setArticleList(articles);
			session.setSelectedProject(getProjectById(session,project));
			setNextPage(PageLinks.ARTICLE_PAGE);
			log_.info("OpenProjectAction.execute successful,"+articles.size()+" articles loaded for project "+project);
			Roles r = new Roles(conn, usr.getRole());
			int numarts = articles.size();
			if (numarts >= r.getLevel())
			{
				log_.info("enough articles in this project");
				session.setEnoughArticles();
			}
			else
			{
				log_.info("not enough articles in this project");
				session.removeEnoughArticles();
			}
		}
		catch (SQLException e)
		{
			log_.warning("OpenProjectAction.execute: Article.queryArticlesByProject thrown exception: "+e.toString());
			//throw or what?
			param.sendError(MessageCode.ERR_DB_CONNECT);
		}
		catch (NumberFormatException x)
		{
			log_.severe("OpenProjectAction.execute: Error converting getAttribute('project') into int");
			param.sendError(MessageCode.ERR_PARAM_FORMAT);
		}
		finally
		{
			try { conn.close(); } catch (SQLException e) {}
		}

		log_.exiting("OpenProjectAction","execute");
	}
	
	/**
	 * Find the project by its ID.
	 * @param session
	 * @param pid - selected project id
	 * @return Projects object found or null
	 */
	protected Projects getProjectById(Session session, int pid)
	{
		ArrayList<Projects> projects = session.getProjectList();
		for (Projects p : projects)
		{
			if (p.getProjectID() == pid)
			{
				return p;
			}
		}
		return null;
	}
}
