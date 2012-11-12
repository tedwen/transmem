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
import com.transmem.data.tm.*;
import com.transmem.nlp.LanguageException;

/**
 * Action class for looking up examples for a sentence.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan. 2007
 */
public class QueryCorpus extends BaseAction
{
	private	Logger log_ = Logger.getLogger(QueryCorpus.class.getName());

	public QueryCorpus()
	{
		super();
	}

	/**
	 * Look up examples for a sentence.
	 * param vars: index of sentence
	 * Session vars: sentences
	 */
	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("QueryCorpus","execute");

		Session session = param.getSession();

		Users usr = session.getUser();
		if (usr == null) {
			param.sendError(MessageCode.ERR_NOT_LOGIN);
			return;
		}
		//break the sentence into words
		ArrayList<Sentences> sentences = session.getSentenceList();
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
		
		//check domain and permit options
		String domain = param.getParameter("domain");
		if (domain == null) domain = session.getSelectedDomain();
		if (domain != null && !domain.equals(session.getSelectedDomain()))
		{
			session.setSelectedDomain(domain);
		}
		String permit = param.getParameter("permit");
		if (permit == null) permit = session.getSelectedPermit();
		if (permit != null && !permit.equals(session.getSelectedPermit()))
		{
			session.setSelectedPermit(permit);
		}

		log_.info("QueryCorpus, index="+index);
		String st = sentences.get(index).getSentence();
		//call Segmenter to break sentence into words
		String langpair = session.getLangPair();
		if (langpair==null || langpair.length()!=4)
		{
			param.sendError(MessageCode.ERR_NULL_ATTRIBUTE);
			return;
		}

		Corpus corpus = session.getCorpus();
		if (corpus != null)
		{
			session.removeCorpus();
		}
		try
		{
			corpus = new Corpus(getDatabases(param),langpair.substring(0,2),langpair.substring(2));
		}
		catch (SQLException e)
		{
			log_.severe(e.toString());
			param.sendError(MessageCode.ERR_CORPUS_INIT);
			return;
		}
		session.setCorpus(corpus);
		setNextPage(null);	//for Ajax return
		param.setContentType("text/html;charset=utf-8");
		param.setHeader("Cache-Control", "no-cache");
		try
		{
			int pages = corpus.query(usr.getUserID(), usr.getGroup(), st, domain, permit);
			session.setSentencePages(new Integer(pages));
			session.setSentencePage(new Integer(0));
			String examples = corpus.getPageAsHtml(0);
			param.getWriter().write(examples);
		}
		catch (LanguageException le)
		{
			param.getWriter().write("0/0* No examples available.");
		}
		log_.info("End of QueryCorpus");
	}
}
