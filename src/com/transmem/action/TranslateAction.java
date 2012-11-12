package com.transmem.action;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;

import com.transmem.data.db.Databases;
import com.transmem.data.db.Articles;
import com.transmem.data.db.Languages;
import com.transmem.data.db.Paragraphs;
import com.transmem.data.db.Sentences;
import com.transmem.data.db.Users;

/**
 * Action class for starting translation of an article.
 * This action is invoked from the articles.jsp page by selecting an article link.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan. 2007
 */
public class TranslateAction extends BaseAction
{
	private	Logger log_ = Logger.getLogger(TranslateAction.class.getName());

	public TranslateAction()
	{
		super();
	}
	
	/**
	 * Start translation of an article.
	 *
	 * <p class="code">
	 	<ul>
	 		<li>Session attributes:<br>
	 			<ul>
	 				<li>user : Users</li>
	 				<li>articles : list of Articles objects</li>
	 				<li>article : selected article ID, not avail first call</li>
	 				<li>translayout : Integer for which translate template page</li>
	 				<li>domains : a list of domain IDs like IT, loaded from corpus by this action</li>
	 			</ul>
	 		</li>
	 		<li>Request parameters:<br>
	 			<ul>
	 				<li>article - ID of an Articles object</li>
	 				<li>op - prevp or nextp, exist only when turning paragraph pages</li>
	 			</ul>
	 		</li>
	 	</ul>
	 * </p>
	 */
	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("TranslateAction","execute");

		Session session = param.getSession();

		Users usr = session.getUser();
		if (usr == null)
		{
			param.sendError(MessageCode.ERR_NOT_LOGIN);
			return;
		}

		Articles article = null;
		String aids = param.getParameter("article");
		if (aids != null)
		{
			log_.info("---------new article: "+aids);
			article = getArticleByIds(session, aids);
			session.setSelectedArticle(article);
			session.setLangPair(article.getLangPair());
			session.removeParagraphList();
		}
		else
		{
			article = session.getSelectedArticle();
			log_.info("=========old article: "+article.getArticleID());
		}
		if (article == null)
		{
			log_.severe("article not selected or put in session");
			param.sendError(MessageCode.ERR_NO_ARTICLE);
			return;
		}
		int aid = article.getArticleID();
		int translayout = getTranslateLayout(param, session);

		Connection conn = null;
		try
		{
			conn = getConnection(param,Databases.CATEGORY_USER);
			//get domains from the corpus for this language pair
			session.setDomainList(loadDomains(conn,article.getLangPair()));
			session.setSelectedDomain("00");
			//load a paragraph in session, if no paragraph available, report error
			Paragraphs paras = new Paragraphs(conn);
			ArrayList<Paragraphs> paragraphs = session.getParagraphList();
			if (paragraphs == null)
			{
				paragraphs = paras.queryByArticle(aid);
				if (paragraphs == null)
				{
					log_.severe("Article "+aid+" has not paragraphs");
					param.sendError(MessageCode.ERR_NO_PARAS);
					return;
				}
				session.setParagraphList(paragraphs);
			}
			int paraCount = paragraphs.size();
			if (paraCount < 1)
			{
				param.sendError(MessageCode.ERR_NO_PARAS);
				return;
			}
			//determine which paragraph to load, T_Articles.F_LastPara.
			int parax = article.getParagraph();
			//check whether prevp or nextp is called and turn paragraph if necessary
			String op = param.getParameter("op");
			if (op != null)
			{
				if (op.equals("prevp"))
				{
					if (parax > 0) parax --;
				} 
				else if (op.equals("nextp"))
				{
					if (parax < paraCount-1) parax ++;
				}
			}
			if (parax < 0 || parax >= paragraphs.size())
			{
				parax = 0;
			}
			if (parax != article.getParagraph())
			{
				article.setParagraph(parax);	//in memory
				Articles tempa = new Articles(conn);
				tempa.updateParagraph(aid, parax);	//update db as well
			}
			long paraid = paragraphs.get(parax).getParagraphID();
			//load all sentences from T_Sentences with translations
			Sentences sent = new Sentences(conn);
			ArrayList<Sentences> sents = sent.queryByParagraph(paraid);
			//store in session for jsp
			session.setSentenceList(sents);
			session.setThisParagraph(String.valueOf(parax+1));	//for display in JSP
			session.setNumParagraphs(String.valueOf(paraCount));
			//log_.info("TranslateAction.execute "+sents.size()+" sentences added in session");
			setNextPage(PageLinks.TRANSLATE_PAGES[translayout]);
		}
		catch (SQLException x)
		{
			log_.severe("SQLException when getConnection(USER):"+x);
			param.sendError(MessageCode.ERR_DB_CONNECT);
		}
		finally
		{
			if (conn != null)
				try { conn.close();	} catch (SQLException e) {}
		}

	}
	
	/**
	 * Load domain strings from corpus table specified by the language pair.
	 * @param conn - Connection reference
	 * @param langpair - ENZH etc
	 * @return ArrayList of strings of domain IDs or null if not found
	 */
	protected ArrayList<String> loadDomains(Connection conn, String langpair)
	{
		try
		{
			Languages ls = new Languages(conn);
			String tablename = ls.queryTableName(langpair);
			String sql = "SELECT DISTINCT F_Domain FROM "+tablename+" ORDER BY F_Domain";
			java.sql.Statement stmt = conn.createStatement();
			java.sql.ResultSet rs = stmt.executeQuery(sql);
			ArrayList<String> domains = new ArrayList<String>();
			while (rs.next())
			{
				domains.add(rs.getString(1));
			}
			rs.close();
			stmt.close();
			return domains;
		}
		catch (SQLException x)
		{
			log_.severe(x.toString());
			return null;
		}
	}

	/**
	 * Search the articles in session for the given article index.
	 * @session - Session reference
	 * @param aids - ID of article
	 * @return Articles object found or null
	 */
	protected Articles getArticleByIds(Session session, String aids)
	{
		if (aids == null)
		{
			log_.severe("request.getParameter('article') returned null");
			return null;
		}
		try
		{
			int aid = Integer.parseInt(aids);
			ArrayList<Articles> articles = session.getArticleList();
			for (Articles a : articles)
			{
				if (a.getArticleID() == aid)
				{
					return a;
				}
			}
			log_.severe("article ID ("+aids+") from request is not found");
		}
		catch (NumberFormatException e)
		{
			log_.severe("article ID ("+aids+") from request is not a valid number");
		}
		return null;
	}

	/**
	 * Get the template for translation page, 0, 1, or 2 at the moment.
	 * If request to change, the param.getParameter('layout') should return a string of '0','1', or '2'.
	 *
	 * TODO: It can be loaded from preference database and stored in the session once the user logged in.
	 * If no such preference set, the default page layout is 1.
	 *
	 * @param session - HttpSession
	 * @return layout number 0,1, or 2
	 */
	protected int getTranslateLayout(ServletParams param, Session session)
	{
		int translayout = 1;
		Integer tsln = session.getTranslateLayout();
		if (tsln != null)
		{
			int n = tsln.intValue();
			if (n != translayout && n >= 0 && n < 3)
				translayout = n;
			String slayout = param.getParameter("layout");
			log_.info("Layout = "+slayout);
			if (slayout != null)
			{
				try
				{
					int l = Integer.parseInt(slayout);
					if (l != translayout)
					{
						session.setTranslateLayout(new Integer(l));
						translayout = l;
					}
				}
				catch (Exception e)
				{
					log_.warning("layout parameter wrong from links.inc: "+slayout);
				}
			}
		}
		else
		{
			session.setTranslateLayout(new Integer(translayout));
		}
		return translayout;
	}
}
