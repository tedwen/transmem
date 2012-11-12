package com.transmem.action;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import java.io.IOException;

import com.transmem.data.db.Users;

/**
 * Action class for changing of session variables, asynchronous.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jun. 2007
 */
public class SessionMan extends BaseAction
{
	private Logger log_ = Logger.getLogger(SessionMan.class.getName());

	public SessionMan()
	{
		super();
	}

	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("SessionMan","execute");

		Session session = param.getSession();

		Users usr = session.getUser();
		if (usr == null) {
			param.sendError(MessageCode.ERR_NOT_LOGIN);
			return;
		}

		String skey = param.getParameter("key");
		String sval = param.getParameter("value");

		if (skey != null && sval != null)
		{
			session.setAttribute(skey, sval);
		}
	}
}
