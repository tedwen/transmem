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
 * Action class for splitting a sentence into two.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan. 2007
 */
public class SplitSentence extends BaseAction
{
	private	Logger log_ = Logger.getLogger(SplitSentence.class.getName());
	
	public SplitSentence()
	{
		super();
	}

	/**
	 * Split a source sentence into two sentences.
	 * The second sentence is inserted into sentences array and the DB table.
	 * Variables in param:
	 *		index - which sentence ranging 0 to sentences.size()-1
	 *		sent1, sent2 - two sentences split from the first at index
	 * Variables in session:
	 *		sentences - ArrayList<Sentences>
	 */
	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("SplitSentence","execute");

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
		String st1 = param.getParameter("sent1");
		String st2 = param.getParameter("sent2");
		if (st1==null || st2==null || st1.equals("") || st2.equals(""))
		{
			param.sendError(MessageCode.ERR_NULL_PARAM);
			return;
		}
		Sentences sent1 = sentences.get(index);
		sent1.setSentence(st1);
		Sentences sent2 = sentences.get(index+1);
		float sent2Id = sent1.getSequence() + (sent2.getSequence()-sent1.getSequence())/2;
		sent2 = new Sentences();
		sent2.setSequence(sent2Id);
		sent2.setSentence(st2);
		sentences.add(index+1,sent2);
		Connection conn = null;
		try
		{
			conn = getConnection(param,Databases.CATEGORY_USER,true);
			//set the two update operations succeed or fail together
			conn.setAutoCommit(false);
			try
			{
				sent1.update(conn);
				setNextPage(PageLinks.CONFIRM_PAGE);
			}
			catch (SQLException e)
			{
				conn.rollback();
				log_.severe("SplitSentence.execute(): SQLException when update sentences");
				param.sendError(MessageCode.ERR_UPDATE_SENTENCE);
			}
			try
			{
				sent2.insert(conn);
				conn.commit();
				setNextPage(PageLinks.CONFIRM_PAGE);
			}
			catch (SQLException e)
			{
				conn.rollback();
				log_.severe("SplitSentence.execute(): SQLException when insert sentences");
				param.sendError(MessageCode.ERR_INSERT_SENTENCE);
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
}
