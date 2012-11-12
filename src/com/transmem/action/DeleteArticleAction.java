package com.transmem.action;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;

import com.transmem.data.db.Databases;
import com.transmem.data.db.Projects;
import com.transmem.data.db.Articles;
import com.transmem.data.db.Users;
import com.transmem.data.db.Roles;

/**
 * Action class for removing a selected article.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan. 2007
 */
public class DeleteArticleAction extends BaseAction
{
	private Logger log_ = Logger.getLogger(DeleteArticleAction.class.getName());

	public DeleteArticleAction()
	{
		super();
	}
	
	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("DeleteArticleAction","execute");

		Session session = param.getSession();

		Users usr = session.getUser();
		if (usr == null) {
			param.sendError(MessageCode.ERR_NOT_LOGIN);
			return;
		}

		String said = param.getParameter("article");
		if (said == null || said.length()<1) {
			log_.severe("parameter article is null or empty");
			param.sendError(MessageCode.ERR_NULL_PARAM);
			return;
		}
		int aid = 0;
		try
		{
			aid = Integer.parseInt(said);
		}
		catch (NumberFormatException e) 
		{
			log_.severe("parameter article is not number: "+said);
			param.sendError(MessageCode.ERR_PARAM_FORMAT);
			return;
		}

		Projects project = session.getSelectedProject();
		if (project == null)
		{
			param.sendError(MessageCode.ERR_NO_PROJECT);
			return;
		}

		Connection conn = null;
		try
		{
			conn = getConnection(param,Databases.CATEGORY_USER,true);
			conn.setAutoCommit(false);
			Articles article = new Articles(conn);
			try
			{
				article.delete(aid);
				//update number of articles in project
				project.clearUpdates();
				project.setArticles(project.getArticles()-1);
				project.update(conn);
				conn.commit();
			}
			catch (SQLException e)
			{
				log_.severe("DeleteArticleAction.execute(): failed to delete article "+aid);
				param.sendError(MessageCode.ERR_DELETE_ARTICLE);
				return;
			}
			int pid = project.getProjectID();
			ArrayList<Articles> articles = article.queryByProject(pid);
			session.setArticleList(articles);
			Roles roles = new Roles(conn, usr.getRole());
			if (articles.size() < roles.getLevel())
			{
				session.removeEnoughArticles();
			}
			setNextPage(PageLinks.ARTICLE_PAGE);
			log_.info("DeleteArticleAction.execute(),article "+aid+" deleted and reload "+articles.size()+" articles");
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
