package com.transmem.action;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;

import com.transmem.data.db.Databases;
import com.transmem.data.db.Sentences;
import com.transmem.data.db.Users;

/**
 * Action class for updating a sentence.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan. 2007
 */
public class UpdateSentence extends BaseAction
{
	private	Logger log_ = Logger.getLogger(UpdateSentence.class.getName());

	public UpdateSentence()
	{
		super();
	}

	/**
	 * Update the source sentence.
	 * Request var: index, sent
	 * Session var: sentences - ArrayList<Sentences>
	 */
	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("UpdateSentence","execute");

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

		String st = param.getParameter("sent");
		if (st == null || st.equals(""))
		{
			param.sendError(MessageCode.ERR_NULL_PARAM);
			return;
		}

		Sentences sent = sentences.get(index);
		sent.clearUpdates();
		sent.setSentence(st);
		Connection conn = null;
		try
		{
			conn = getConnection(param,Databases.CATEGORY_USER,true);
			try
			{
				sent.update(conn);
				setNextPage(PageLinks.CONFIRM_PAGE);
			}
			catch (SQLException e)
			{
				log_.severe("SQLException when update sentences"+e);
				param.sendError(MessageCode.ERR_UPDATE_SENTENCE);
			}
		}
		catch (SQLException e)
		{
			log_.severe("SQLException when getConnection(USER)"+e);
			param.sendError(MessageCode.ERR_DB_CONNECT);
		}
		finally
		{
			if (conn != null)
				try { conn.close();	} catch (SQLException e) {}
		}
	}
}
