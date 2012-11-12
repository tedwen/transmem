package com.transmem.action;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.sql.SQLException;

import com.transmem.data.db.Users;

/**
 * Action class for logout operation.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan. 2007
 */
public class LogoutAction extends BaseAction
{
	private Logger log_ = Logger.getLogger(LogoutAction.class.getName());

	public LogoutAction()
	{
		super();
	}

	public void execute(ServletParams request) throws ServletException, IOException
	{
		log_.entering("LogoutAction","execute");

		Session session = request.getSession();

		Users usr = session.getUser();
		if (usr != null)
		{
			// decrement online user count
			ServletContext ctx = session.getServletContext();
			int usercount = ((Integer)ctx.getAttribute("UserCount")).intValue();
			if (usercount > 0)
			{
				usercount --;
				ctx.setAttribute("UserCount", usercount);	//assume this is synchronized
				log_.info("UserCount = "+usercount);
			}
			else
			{
				log_.severe("UserCount not synchronised: "+usercount);
			}
			//TODO: update user usage or online info
			session.removeAttribute("user");	//necessary before invalidate?
			session.invalidate();
			setNextPage(PageLinks.HOME_PAGE);
		}
		//else
		//{
		//	log_.warning("Trying logout without a user in session.");
		//	throw new IOException("LoginAction.execute(): Logout error.");
		//}
	}
}
