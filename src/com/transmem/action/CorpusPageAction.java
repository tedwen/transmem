package com.transmem.action;

import java.util.logging.Logger;
import java.io.IOException;
import javax.servlet.ServletException;

import com.transmem.data.db.Users;
import com.transmem.data.tm.*;
import com.transmem.nlp.LanguageException;

public class CorpusPageAction extends BaseAction
{
	private	Logger log_ = Logger.getLogger(CorpusPageAction.class.getName());

	public CorpusPageAction()
	{
		super();
	}

	/**
	 * Query a page of sentences.
	 * A list of sentence IDs is kept in the session memory. The list is loaded
	 * by QueryCorpus and saved in a Corpus object.
	 *
	 * <p class="code">
	 	<ul>
	 		<li>Session attributes:<br>
	 			<ul>
	 				<li>user : Users</li>
	 				<li>corpus : Corpus</li>
	 			</ul>
	 		</li>
	 		<li>Request parameters:<br>
	 			<ul>
	 				<li>pagedir : prev or next page to turn to</li>
	 			</ul>
	 		</li>
	 	</ul>
	 * </p>
	 *
	 * param vars: which page to query
	 * Session vars: corpus object that contains the sentence IDs
	 */
	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("CorpusPageAction","execute");

		Session session = param.getSession();

		Users usr = session.getUser();
		if (usr == null) {
			log_.severe("User not login");
			param.sendError(MessageCode.ERR_NOT_LOGIN);
			return;
		}

		setNextPage(null);	//for Ajax return
		param.setContentType("text/html;charset=utf-8");
		param.setHeader("Cache-Control", "no-cache");

		String dir = param.getParameter("pagedir");	//prev or next

		String result = null;
		Corpus corpus = session.getCorpus();
		if (corpus == null)
		{
			result = "0/0* No examples available.";
		}
		else
		{
			try
			{
				int pages = session.getSentencePages().intValue();
				int page = session.getSentencePage().intValue();
				if (dir.equals("next"))
				{
					if (page < pages) page ++;
				}
				else
				{
					if (page > 0) page --;
				}
				session.setSentencePage(new Integer(page));
				result = corpus.getPageAsHtml(page);
			}
			catch (LanguageException e)
			{
				log_.severe("corpus.getPage exception: "+e.getMessage());
			}
		}
		param.getWriter().write(result);
	}
}
