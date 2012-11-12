package com.transmem.action;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Locale;

import com.transmem.data.db.Databases;
import com.transmem.data.db.Users;
import com.transmem.data.db.Projects;
import com.transmem.utils.Security;

/**
 * Action class for login operation.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan. 2007
 */
public class LoginAction extends BaseAction
{
	public static final int MAX_IDLE_SECONDS = 900;	//15 minutes for user inactive

	private Logger log_ = Logger.getLogger(LoginAction.class.getName());

	public LoginAction()
	{
		super();
	}

	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("LoginAction","execute");

		Session session = param.getSession();

		String uid = param.getParameter("loginname");
		String pwd = param.getParameter("passwd");
		String passmd5 = Security.md5(pwd);

		Connection conn = null;
		try
		{
			conn = getConnection(param,Databases.CATEGORY_USER);
			Users usr = new Users(conn);
			usr.queryLogin(uid, passmd5);
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
/*				Projects prjs = new Projects(conn);
				ArrayList<Projects> projects = prjs.queryProjectsByMember(usr.getUserID());
				session.setProjectList(projects);
				log_.info(projects.size()+" projects loaded and saved in session");
*/
				setNextPage(PageLinks.PERSONAL_PAGE);
			}
			else
			{
				log_.info("Error: username="+uid+",password="+passmd5);
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
		}
	}
}
