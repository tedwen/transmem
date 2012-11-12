package com.transmem.action;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;

import com.transmem.data.db.Databases;
import com.transmem.data.db.Projects;
import com.transmem.data.db.Articles;
import com.transmem.data.db.Sentences;
import com.transmem.data.db.Users;

/**
 * Action class for updating the translation of a sentence after editing.
 * This action is invoked by Ajax engine, so it should return differently.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan. 2007
 */
public class UpdateTranslation extends BaseAction
{
	private	Logger log_ = Logger.getLogger(UpdateTranslation.class.getName());
	
	public UpdateTranslation()
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
	 *        <li><b>index</b> - which sentence of the paragraph, starting from 0.</li>
	 *        <li><b>trans</b> - string of the translation.</li>
	 *      </ul>
	 *    </li>
	 *    <li>Session attributes
	 *      <ul>
	 *        <li><b>user</b> - Users object created after login or register.</li>
	 *        <li><b>sentences</b> - ArrayList<Sentences>, created and saved by TranslateAction.</li>
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
		log_.entering("UpdateTranslate","execute");

		Session session = param.getSession();

		Users usr = session.getUser();
		if (usr == null) {
			param.sendError(MessageCode.ERR_NOT_LOGIN);
			return;
		}

		ArrayList<Sentences> sentences = session.getSentenceList();
		if (sentences == null) {
			param.sendError(MessageCode.ERR_NULL_ATTRIBUTE);
			return;
		}

		int index = 0;
		try
		{
			index = Integer.parseInt(param.getParameter("index"));
			if (index < 0 || index >= sentences.size())
			{
				param.sendError(MessageCode.ERR_INDEX_OUTOF_BOUNDS);
				return;
			}
		}
		catch (NumberFormatException e)
		{
			param.sendError(MessageCode.ERR_INDEX_OUTOF_BOUNDS);
			return;
		}

		String trans = param.getParameter("trans");
		if (trans == null || trans.equals(""))
		{
			param.sendError(MessageCode.ERR_NULL_PARAM);
			return;
		}

		Sentences sent = sentences.get(index);
		sent.setTranslation(trans);
		Connection conn = null;
		try
		{
			conn = getConnection(param,Databases.CATEGORY_USER,true);
			try
			{
				//log_.info("About to update this sentence:"+sent.getSentence()+"("+sent.getTranslation()+")");
				sent.update(conn);
				//log_.info("Update sentence finished");
				//calculate progress of the article
				int aid = sent.getArticle();
				//log_.info("sent.getArticle() returned: "+aid);
				Sentences snew = new Sentences(conn);
				int total = snew.countByArticle(aid);
				//log_.info("sent.countByArticle('"+aid+"') returned "+total);
				if (total > 0)
				{
					int done = snew.countTranslationsByArticle(aid);
					float val = 100.0f * (float)done / (float)total;
					//log_.info("updateArticleProgress("+aid+","+val+")");
					snew.updateArticleProgress(aid,val);
					//update in-session progress
					updateSession(session, val, conn);
				}
				setNextPage(null);	//Ajax return will be ignored
				//setNextPage(PageLinks.CONFIRM_PAGE);
			}
			catch (SQLException e)
			{
				log_.severe("UpdateTranslation.execute(): SQLException when update sentences");
				param.sendError(MessageCode.ERR_UPDATE_SENTENCE);
			}
		}
		catch (SQLException x)
		{
			log_.severe("UpdateTranslation.execute(): SQLException when getConnection(USER)");
			param.sendError(MessageCode.ERR_DB_CONNECT);
		}
		finally
		{
			if (conn != null)
				try { conn.close();	} catch (SQLException e) {}
		}
	}
	
	/**
	 * Update the article progress in session as well.
	 * @param session - current HttpSession
	 * @param progress - value to set
	 */
	protected void updateSession(Session session, float progress, Connection conn) throws SQLException
	{
		Articles article = session.getSelectedArticle();
		if (article != null)
		{
			article.setProgress(progress);
			//update project's total progress as well
			Projects project = session.getSelectedProject();
			ArrayList<Articles> articles = session.getArticleList();
			float x = 0;
			for (Articles a : articles)
			{
				x += a.getProgress();
			}
			float avg = x / (float)articles.size();
			project.setProgress(avg);
			project.update(conn);
		}
		else
		{
			log_.severe("article not selected while updateSession()");
		}
	}
}
