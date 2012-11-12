package com.transmem.action;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.Locale;

/**
 * Action class for changing user locale operation.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jun. 2007
 */
public class LocaleAction extends BaseAction
{
	private Logger log_ = Logger.getLogger(LocaleAction.class.getName());

	public LocaleAction()
	{
		super();
	}

	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("LocaleAction","execute");

		Session session = param.getSession();

		String userlang = param.getParameter("language");
		if (userlang != null)
		{
			session.setUserLanguage(userlang);
			session.removeResourceBundle();
		}
		setNextPage(PageLinks.HOME_PAGE);
	}
}
