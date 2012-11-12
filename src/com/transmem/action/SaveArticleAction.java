package com.transmem.action;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;

import com.transmem.data.db.Databases;
import com.transmem.data.db.Articles;

/**
 * Action class for saving an article.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan. 2007
 */
public class SaveArticleAction extends BaseAction
{
	public SaveArticleAction()
	{
		super();
	}
	
	public void execute(ServletParams request)
		throws ServletException, IOException
	{
	}
}
