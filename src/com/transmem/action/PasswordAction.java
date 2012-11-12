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
import java.util.Random;

import com.transmem.data.db.Databases;
import com.transmem.data.db.Users;
import com.transmem.utils.Security;

/**
 * Action class for checking password.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jun. 2007
 */
public class PasswordAction extends BaseAction
{
	private Logger log_ = Logger.getLogger(PasswordAction.class.getName());

	public PasswordAction()
	{
		super();
	}

	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("PasswordAction","execute");
		log_.info("PasswordAction");

		Session session = param.getSession();

		String op = param.getParameter("operation");

		if (op == null)
		{
			log_.warning("operation is null");
			setNextPage(PageLinks.FORGET_PAGE);
			return;
		}
		String username = param.getParameter("username");
		if (username == null)
		{
			log_.warning("username is null");
			param.sendError("No username");
			return;
		}
		Connection conn = null;
		try
		{
			conn = getConnection(param,Databases.CATEGORY_USER, true);
			Users usr = new Users(conn);
			usr = usr.queryByUsername(username);
			if (usr == null) {
				log_.warning("queryByUsername("+username+") returned null");
				param.sendError("User not found");
				return;
			}
			if (op.equals("email"))
			{
				String email = param.getParameter("email");
				if (usr.equals(email))
				{
					//TODO: send the new password to this email, temporarily to the page
					resetPassword(usr, session);
					setNextPage(PageLinks.NEWPASS_PAGE);
				}
				else
				{
					log_.warning("email not the same");
					param.sendError("Email not the same");
				}
			}
			else if (op.equals("qa"))
			{
				String answer = param.getParameter("answer");
				if (answer != null && answer.equals(usr.getAnswer()))
				{
					resetPassword(usr, session);
					setNextPage(PageLinks.NEWPASS_PAGE);
				}
				else
				{
					log_.warning("answer not correct");
					param.sendError("Answer is not correct");
				}
			}
			else if (op.equals("id"))
			{
				String idtype = param.getParameter("idtype");
				String idnumber = param.getParameter("idnumber");
				if (usr.getIdType().equals(idtype) && usr.getIdNumber().equals(idnumber))
				{
					resetPassword(usr, session);
					setNextPage(PageLinks.NEWPASS_PAGE);
				}
				else
				{
					log_.warning("id number not match");
					param.sendError("ID Number does not match");
				}
			}
			else if (op.equals("gq"))
			{
				param.setContentType("text/html;charset=utf-8");
				param.setHeader("Cache-Control", "no-cache");
				param.getWriter().write(usr.getQuestion());
				setNextPage(null);
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
	
	protected void resetPassword(Users usr, Session session) throws SQLException
	{
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i=0;i<6; i++)
		{
			int n = random.nextInt(chars.length());
			sb.append(chars.charAt(n));
		}
		String tpass = sb.toString();
		String tpassmd5 = Security.md5(tpass);
		usr.clearUpdates();
		usr.setPassword(tpassmd5);	//must be 32 chars long
		usr.update();
		session.setTempPassword(tpass);
		log_.info("new passcode="+tpass+", md5="+tpassmd5);
	}
}
