package com.transmem.action;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;

//import com.transmem.data.db.Databases;
import com.transmem.data.db.Users;
//import com.transmem.data.db.Projects;

/**
 * Action class for User profile operations.
 *
 * @version 0.1
 * @author Ted Wen
 * @date May. 2007
 */
public class ProfileAction extends BaseAction
{
	private Logger log_ = Logger.getLogger(ProfileAction.class.getName());

	public ProfileAction()
	{
		super();
	}

	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("ProfileAction","execute");

		Session session = param.getSession();

		Users user = session.getUser();
		if (user == null) {
			log_.severe("session.getAttribute('user') returned null");
			param.sendError(MessageCode.ERR_NOT_LOGIN);
			return;
		}

		String op = param.getParameter("operation");
		log_.info("op="+op+",page="+param.getParameter("arg1"));

		if (op.equals("2page"))
		{
			String page = param.getParameter("arg1");
			setNextPage(PageLinks.JSP_HOME+page);
			log_.info("Redirect page to "+PageLinks.JSP_HOME+page);
		}
/*
		Connection conn = null;
		try
		{
			conn = getConnection(param,Databases.CATEGORY_USER);
			Users usr = new Users(conn);
			usr.queryLogin(uid, pwd);
			if (usr.getUserID() > 0)
			{
				session.setUser(usr);
				session.setMaxInactiveInterval(MAX_IDLE_SECONDS);

				// Increment online user count
				ServletContext ctx = session.getServletContext();
				Integer uc = (Integer)ctx.getAttribute("UserCount");
				if (uc == null)
				{
					uc = new Integer(1);
				}
				else
				{
					int usercount = uc.intValue() + 1;
					uc = new Integer(usercount);
				}
				ctx.setAttribute("UserCount", uc);	//assuming ServletContext is thread-safe
				//log_.info("UserCount = "+uc);

				//load projects for this user and store in session heap
				Projects prjs = new Projects(conn);
				ArrayList<Projects> projects = prjs.queryProjectsByMember(usr.getUserID());
				session.setProjectList(projects);
				log_.info(projects.size()+" projects loaded and saved in session");

				//redirect to the project page
				//setNextPage(PageLinks.PROJECT_PAGE);
				setNextPage(PageLinks.PERSONAL_PAGE);
			}
			else
			{
				log_.info("Invalid username or password.");
				//throw new IOException("LoginAction.execute(): Invalid username or password.");
				//TODO: set errorcode and message
				param.sendError(MessageCode.ERR_INVALID_USERPASS);
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
		}*/
	}
}
