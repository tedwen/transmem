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
 * Action class for mergeing two sentences into one.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan. 2007
 */
public class MergeSentence extends BaseAction
{
	private	Logger log_ = Logger.getLogger(MergeSentence.class.getName());

	public MergeSentence()
	{
		super();
	}

	/**
	 * Merge the current sentence with the next into one.
	 * Request var: index, sent
	 * Session vars: sentences
	 */
	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("MergeSentence","execute");

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
			if (index < 0 || index >= sentences.size()-1)
			{
				log_.severe("sentence index out of bounds:"+index);
				param.sendError(MessageCode.ERR_INDEX_OUTOF_BOUNDS);
				return;
			}
		}
		catch (NumberFormatException e)
		{
			param.sendError(MessageCode.ERR_INDEX_OUTOF_BOUNDS);
			return;
		}

		String s = param.getParameter("sent");
		Sentences sent = sentences.get(index);
		sent.setSentence(s);
		Sentences deleted = sentences.remove(index+1);
		Connection conn = null;
		try
		{
			conn = getConnection(param,Databases.CATEGORY_USER,true);
			try
			{
				sent.update(conn);
				deleted.delete(conn);	//add delete(Connection)
				setNextPage(PageLinks.CONFIRM_PAGE);
				//called via Ajax, do nothing, just return
			}
			catch (SQLException e)
			{
				log_.severe("SQLException when update sentences"+e);
				param.sendError(MessageCode.ERR_UPDATE_SENTENCE);
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
