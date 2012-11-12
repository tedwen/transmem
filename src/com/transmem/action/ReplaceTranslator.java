package com.transmem.action;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.transmem.data.db.Databases;
import com.transmem.data.db.Projects;
import com.transmem.data.db.Articles;
import com.transmem.data.db.Sources;
import com.transmem.data.db.Sentences;
import com.transmem.data.db.Languages;
import com.transmem.data.db.Users;

/**
 * Action class for changing the translator of an article by a group member.
 * This action can only be invoked by a group leader.
 *
 * This action is invoked by Ajax engine, so it should return differently.
 *
 * @version 0.1
 * @author Ted Wen
 * @date May. 2007
 */
public class ReplaceTranslator extends BaseAction
{
	private	Logger log_ = Logger.getLogger(ReplaceTranslator.class.getName());
	
	public ReplaceTranslator()
	{
		super();
	}

	/**
	 * Accept user's translation for the current sentence.
	 *
	 * <p class="doc">
	 *  <ul>
	 *    <li>Request parameters
	 *      <ul>
	 *        <li><b>aid</b> - string of article ID</li>
	 *		  <li><b>translator</b> - ID of new translator, '0' to get group members</li>
	 *      </ul>
	 *    </li>
	 *    <li>Session attributes
	 *      <ul>
	 *        <li><b>user</b> - Users object created after login or register.</li>
	 *        <li><b>articles</b> - array list of articles.</li>
	 *      </ul>
	 *    </li>
	 *    <li>Response page
	 *      <ul>
	 *        <li> - not used as a whole web page but for Ajax response</li>
	 *      </ul>
	 *    </li>
	 *  </ul>
	 * </p>
	 *
	 * @param param - HttpServletRequest object
	 * @param response - HttpServletResponse object
	 */
	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("ShareTranslation","execute");

		Session session = param.getSession();

		setNextPage(null);	//for Ajax return
		param.setContentType("text/html;charset=utf-8");
		param.setHeader("Cache-Control", "no-cache");

		Users usr = session.getUser();
		if (usr == null) {
			param.sendError(MessageCode.ERR_NOT_LOGIN);
			return;
		}
		if (!usr.getRole().equals("G"))
		{
			log_.warning("Non Group leader executes ReplaceTranslator");
			return;
		}

		String said = param.getParameter("aid");
		if (said == null)
		{
			log_.warning("ReplaceTranslator called without aid parameter");
			return;
		}
		ArrayList<Articles> articles = session.getArticleList();
		if (articles == null)
		{
			log_.severe("session.getArticleList() return null");
			param.sendError(MessageCode.ERR_NO_ARTICLE);
			return;
		}

		int aid = 0;
		Articles article = null;
		try
		{
			aid = Integer.parseInt(said);
			for (Articles a : articles)
			{
				if (a.getArticleID()==aid)
				{
					article = a;
					break;
				}
			}
		}
		catch (NumberFormatException e)
		{
			log_.severe("Invalid aid:"+said);
			return;
		}

		if (article == null)
		{
			log_.severe("Article given by ID "+said+" not found in list");
			return;
		}
		//default return without group members
		String result_html = "<input type=\"radio\" name=\"translator\" value=\""+usr.getUserID()+"\" checked/>"+usr.getUsername()+"</input>";
		int groupid = usr.getGroup();
		if (groupid == 0)
		{
			log_.warning("article.getGroup() is 0 while asking for group members");
			//response with user himself (id and name)
			param.getWriter().write(result_html);
			return;
		}
		String stranslator = param.getParameter("translator");

		Connection conn = null;
		try
		{
			conn = getConnection(param,Databases.CATEGORY_USER,true);

			if (stranslator == null || stranslator.equals("0"))
			{
				//translator unknown, get a list of candidates for the group
				Users us = new Users(conn);
				ArrayList<Users> uss = us.queryByGroup(groupid);
				StringBuffer sb = new StringBuffer();
				/*
				 Assume the receiving div is contained inside a form element,
				 with a hidden input element of translator.
				 */
				for (Users u : uss)
				{
					sb.append("<input type=\"radio\" name=\"translator\" value=\"");
					sb.append(u.getUserID());
					sb.append("\"");
					if (u.getUserID()==article.getTranslator())
						sb.append(" checked");
					sb.append(" onclick=\"setTranslator(");
					sb.append(u.getUserID());
					sb.append(")\">");
					sb.append(u.getUsername());
					sb.append("</input><br>");
				}
				log_.info(sb.toString());
				param.getWriter().write(sb.toString());
			}
			else
			{
				//if translator different, replace it for the article
				int translator = Integer.parseInt(stranslator);
				if (translator != 0)
				{
					article.clearUpdates();	//TODO: document this! it must be called before directly updating
					article.setTranslator(translator);
					article.update(conn);
					//update the page
					Users u = new Users(conn);
					String transname = u.queryNameByID(translator);
					String s = "("+aid+")"+transname;
					//log_.info("sendback: "+s);
					param.getWriter().write(s);
				} else
					param.getWriter().write("("+aid+")"+usr.getUsername());
			}

		}
		catch (SQLException x)
		{
			log_.severe("SQLException: "+x);
			//param.sendError(MessageCode.ERR_DB_CONNECT);
			param.getWriter().write(result_html);
		}
		finally
		{
			if (conn != null)
				try { conn.close();	} catch (SQLException e) {}
		}
	}
}
