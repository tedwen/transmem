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
 * Action class for user registration.
 * Note: the error strings are defined as codes that JSP pages can interprete
 * into a language-specific string according to user locale. It can be done
 * with a resource bundle.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan. 2007
 */
public class RegisterAction extends BaseAction
{
	private Logger log_ = Logger.getLogger(RegisterAction.class.getName());

	public static final String REGISTER_ERROR_NOUSERNAME = "RE_NONAME";
	public static final String REGISTER_ERROR_NOPASSWORD = "RE_NOPASS";
	public static final String REGISTER_ERROR_NOEMAIL = "RE_NOEMAIL";
	public static final String REGISTER_ERROR_NOQUESTION = "RE_NOQUESTION";
	public static final String REGISTER_ERROR_NOANSWER = "RE_NOANSWER";
	public static final String REGISTER_ERROR_SAVEFAILURE = "RE_SAVEFAILURE";

	public RegisterAction()
	{
		super();
	}

	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("RegisterAction","execute");

		//check for submitted register
		Session session = param.getSession();

		//check whether from the home page, TODO: use image to login?
		String subreg = param.getParameter("subreg");
		log_.info("param.getParameter('subreg') returned "+subreg);
		if (subreg != null && subreg.equals("20020208")) 
		{
			Connection conn = null;
			try
			{
				conn = getConnection(param,Databases.CATEGORY_USER,true);
			} 
			catch (SQLException ex)
			{
				log_.severe("error getConnection(USER)."+ex);
				param.sendError(MessageCode.ERR_DB_CONNECT);
				return;
			}
			Users usr = new Users(conn);
			boolean nextPageSet = false;
			session.setTempUser(usr);
			String usrname = param.getParameter("username");
			if (usrname == null || usrname.equals(""))
			{
				log_.warning("param.getParameter('username') returned null or empty");
				session.setAttribute("tuser_error", REGISTER_ERROR_NOUSERNAME);
			}
			else
			{
				usr.setUsername(usrname);
				String passwd = param.getParameter("password");
				if (passwd == null || passwd.equals(""))
				{
					log_.warning("param.getParameter('password') returned null or empty");
					session.setAttribute("tuser_error", REGISTER_ERROR_NOPASSWORD);
				}
				else
				{
					String passmd5 = Security.md5(passwd);
					usr.setPassword(passmd5);
					String email = param.getParameter("email");
					if (email == null || email.equals(""))
					{
						log_.warning("param.getParameter('email') returned null or empty");
						session.setAttribute("tuser_error", REGISTER_ERROR_NOEMAIL);
					}
					else
					{
						usr.setEmail(email);
						String question = param.getParameter("question");
						if (question == null || question.equals(""))
						{
							log_.warning("param.getParameter('question') returned null or empty");
							session.setAttribute("tuser_error", REGISTER_ERROR_NOQUESTION);
						}
						else
						{
							usr.setQuestion(question);
							String answer = param.getParameter("answer");
							if (answer == null || answer.equals(""))
							{
								log_.warning("param.getParameter('answer') returned null or empty");
								session.setAttribute("tuser_error", REGISTER_ERROR_NOANSWER);
							}
							else
							{
								usr.setAnswer(answer);
								//TODO: add other options to save
								String realname = param.getParameter("realname");
								if (realname == null || realname.equals("")) realname = usr.getUsername();
								usr.setRealname(realname);
								String sex = param.getParameter("sex");
								usr.setSex(sex);
								String mobile = param.getParameter("mobile");
								if (mobile != null && mobile.length()>0) usr.setMobile(mobile);
								String idtype = param.getParameter("idtype");
								if (idtype != null) usr.setIdType(idtype);
								String idnumber = param.getParameter("idnumber");
								if (idnumber != null && idnumber.length()>0) usr.setIdNumber(idnumber);
								String member = param.getParameter("member");
								if (member != null) usr.setMembership(member);
								try
								{
									usr.setUserID(getSequenceInt(conn,"S_Users"));
									usr.insert();
									session.removeAttribute("tuser_error");
									session.removeAttribute("subreg");
									session.removeTempUser();
									session.removeTempUser();
									session.setUser(usr);
									setNextPage(PageLinks.PERSONAL_PAGE);

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
									log_.info("UserCount = "+uc);
					
									//load projects for this user and store in session heap
/*									Projects prjs = new Projects(conn);
									ArrayList<Projects> projects = prjs.queryProjectsByMember(usr.getUserID());
									session.setProjectList(projects);
									log_.info(projects.size()+" projects loaded and saved in session");
*/
									nextPageSet = true;
								}
								catch (SQLException e)
								{
									log_.severe("Insert Users failed, SQLException:"+e);
									session.setAttribute("tuser_error", REGISTER_ERROR_SAVEFAILURE);
								}
								finally
								{
									if (conn != null)
										try { conn.close(); } catch (SQLException x) {}
								}
							} // answer
						} //question
					} //email
				} //passwd
			} //username
			if (!nextPageSet)
			{
				//TODO: show error message here
				setNextPage(PageLinks.REGISTER_PAGE);
			}
		}
		else
		{
			log_.severe("param.getParameter('subreg') not return correct value");
			setNextPage(PageLinks.REGISTER_PAGE);
		}
		log_.exiting("RegisterAction","execute");
	}
}
