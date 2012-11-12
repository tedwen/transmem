package com.transmem.action;

import java.io.IOException;
import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

/**
 * Interface for action classes.
 *
 * @verion 0.1
 * @author Ted Wen
 * @date Jan. 2007
 */
public interface IAction
{
	/**
	 * Execute an action from servlet
	 * @param params - ServletParams object containing HttpServletRequest and HttpServletResponse
	 */
	public void execute(ServletParams params)
		throws ServletException, IOException;

	/**
	 * Getter returns URL of page specified by execution of action.
	 */
	public String getNextPage();

}
