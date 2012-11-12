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
import com.transmem.data.db.Projects;

/**
 * Action class for closing a project.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan. 2007
 */
public class CloseProjectAction extends BaseAction
{
	public CloseProjectAction()
	{
		super();
	}
	
	public void execute(ServletParams request)
		throws ServletException, IOException
	{
	}
}
