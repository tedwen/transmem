package com.transmem.action;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;

import com.transmem.data.db.Databases;
import com.transmem.data.db.Users;
import com.transmem.utils.Security;

/**
 * Action class for user data update.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jun. 2007
 */
public class UpdateUserAction extends BaseAction
{
	private Logger log_ = Logger.getLogger(UpdateUserAction.class.getName());

	public static final String REGISTER_ERROR_NOUSERNAME = "RE_NONAME";
	public static final String REGISTER_ERROR_NOPASSWORD = "RE_NOPASS";
	public static final String REGISTER_ERROR_NOEMAIL = "RE_NOEMAIL";
	public static final String REGISTER_ERROR_NOQUESTION = "RE_NOQUESTION";
	public static final String REGISTER_ERROR_NOANSWER = "RE_NOANSWER";
	public static final String REGISTER_ERROR_SAVEFAILURE = "RE_SAVEFAILURE";

	public UpdateUserAction()
	{
		super();
	}

	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.info("entering UpdateUserAction");

		//check for submitted register
		Session session = param.getSession();

		Users usr = session.getUser();
		if (usr == null) {
			log_.warning("user not login");
			param.sendError("User not login");
			return;
		}
		
		String realname = param.getParameter("realname");
		String sex = param.getParameter("sex");
		String oldpass = param.getParameter("passwd");
		String newpass = param.getParameter("newpasswd");
		String email = param.getParameter("email");
		String mobile = param.getParameter("mobile");
		String question = param.getParameter("question");
		String answer = param.getParameter("answer");
		
		if (oldpass == null || oldpass.equals("")) {
			log_.warning("password not given to update profile");
			param.sendError("Password must be given to update your record");
			return;
		}
		String oldpassmd5 = Security.md5(oldpass);
		if (!oldpassmd5.equals(usr.getPassword())) {
			log_.warning("password not correct to update profile");
			param.sendError("Password not correct");
			return;
		}

		usr.clearUpdates();
		boolean updated = false;
		if (!realname.equals("") && !realname.equals(usr.getRealname()))
		{
			log_.info("realname from "+usr.getRealname()+" to "+realname);
			usr.setRealname(realname);
			updated = true;
		}
		if (!sex.equals("") && !sex.equals(usr.getSex()))
		{
			log_.info("sex from "+usr.getSex()+" to "+sex);
			usr.setSex(sex);
			updated = true;
		}
		if (!newpass.equals("") && !newpass.equals(oldpass))
		{
			log_.info("password from "+oldpass+" to "+newpass);
			usr.setPassword(Security.md5(newpass));
			updated = true;
		}
		if (!email.equals("") && !email.equals(usr.getEmail()))
		{
			int n1 = email.indexOf('@');
			int n2 = email.lastIndexOf('.');
			if (n1 > 1 && n2 > n1) {
				log_.info("email from "+usr.getEmail()+" to "+email);
				usr.setEmail(email);
				updated = true;
			}
		}
		if (!mobile.equals("") && !mobile.equals(usr.getMobile()))
		{
			if (isNumber(mobile)) {
				log_.info("mobile from "+usr.getMobile()+" to "+mobile);
				usr.setMobile(mobile);
				updated = true;
			}
		}
		if (!question.equals("") && !question.equals(usr.getQuestion()))
		{
			log_.info("question from "+usr.getQuestion()+" to "+question);
			usr.setQuestion(question);
			updated = true;
		}
		if (!answer.equals("") && !answer.equals(usr.getAnswer()))
		{
			log_.info("answer from "+usr.getAnswer()+" to "+answer);
			usr.setAnswer(answer);
			updated = true;
		}
		
		if (updated)
		{
			Connection conn = null;
			try
			{
				conn = getConnection(param,Databases.CATEGORY_USER,true);
				usr.update(conn);
				log_.info("profile data updated");
				setNextPage(PageLinks.PERSONAL2_PAGE);
			}
			catch (SQLException ex)
			{
				log_.severe("error getConnection(USER)."+ex);
				param.sendError(MessageCode.ERR_DB_CONNECT);
				return;
			}
			finally
			{
				if (conn != null)
					try { conn.close(); } catch (SQLException x) {}
			}
		}
	}

	private boolean isNumber(String s)
	{
		boolean number = true;
		for (int i=0; i<s.length(); i++)
		{
			if (s.charAt(i)<'0' || s.charAt(i)>'9')
			{
				number = false;
				break;
			}
		}
		return number;
	}
}
