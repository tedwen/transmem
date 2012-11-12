package com.transmem.action;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import com.transmem.data.db.Databases;
import com.transmem.data.db.Users;

/**
 * Asynchronous action class for checking availability of user name.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jun. 2007
 */
public class Username extends BaseAction
{
	private Logger log_ = Logger.getLogger(Username.class.getName());

	public Username()
	{
		super();
	}

	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("Username","execute");

		Session session = param.getSession();

		param.setContentType("text/html;charset=utf-8");
		param.setHeader("Cache-Control", "no-cache");
		String uid = param.getParameter("username");
		if (uid==null || uid.length()<4) {
			param.getWriter().write("-Invalid name");
			return;
		}

		Connection conn = null;
		try
		{
			conn = getConnection(param,Databases.CATEGORY_USER);
			Users usr = new Users(conn);
			int found = usr.checkUsername(uid);
			ResourceBundle rb = session.getResourceBundle();
			if (found > 0)
			{
				param.getWriter().write("+"+rb.getString("reg.info.nameused"));
			}
			else
			{
				param.getWriter().write("-"+rb.getString("reg.info.nameavail"));
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
