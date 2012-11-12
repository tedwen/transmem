package com.transmem;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;
import javax.sql.DataSource;
import java.io.IOException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.logging.Logger;	//set logging output file somewhere
import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import com.transmem.data.db.Databases;
import com.transmem.data.db.Languages;
import com.transmem.action.*;

/**
 * Controller servlet for the whole web app.
 */
public class TransmemServlet extends HttpServlet
{
	private static final long serialVersionUID = 1;
	private	Logger	log;

	/**
	 * Init the servlet.
	 * Obtain a datasource from the container context.
	 * The datasource needs to be set up in the web.xml as resource-ref element.
	 */
	public void init()
	{
		log = Logger.getLogger(TransmemServlet.class.getName());
		try
		{
			Context initCtx = new InitialContext();
			Context envCtx = (Context)initCtx.lookup("java:comp/env");
			
			ServletConfig sc = getServletConfig();
			ServletContext sctx = getServletContext();
			Map<String, DataSource> dss = new HashMap<String, DataSource>();
			Enumeration names = sc.getInitParameterNames();
			Databases dbs = new Databases();
			while (names.hasMoreElements())
			{
				String dskey = (String)names.nextElement();
				String[] parts = dskey.split("-");	//ds-MAIN-READWRITE
				if (parts != null && parts.length == 3 && parts[0].equals("ds"))
				{
					String dsname = sc.getInitParameter(dskey);
					DataSource ds = null;
					if (dss.containsKey(dsname))
						ds = dss.get(dsname);
					else
					{
						ds = (DataSource)envCtx.lookup(dsname);
						if (ds != null)
							dss.put(dsname, ds);
						else
							log.severe("envCtx.lookup("+dsname+") returned null");
					}
					if (ds != null)
					{
						dbs.addDataSource(ds, parts[1], parts[2]);
					}
				}
			}
			sctx.setAttribute("databases", dbs);
			//other initial parameters
			int uploadsizemb = 1;	//default 1 MB of maximum uploaded file size
			String sus = sc.getInitParameter("MaxUploadFileSizeMB");
			if (sus != null)
			{
				try
				{
					uploadsizemb = Integer.parseInt(sus);
				}
				catch (Exception e)
				{
				}
			}
			sctx.setAttribute("UploadSizeMB", new Integer(uploadsizemb)); //set it in web.xml
			//load supported language pairs into memory
			Connection con = dbs.getConnection(Databases.CATEGORY_MAIN);
			Languages langs = new Languages(con);
			ArrayList langpairs = langs.querySupportedPairs();
			con.close();
			if (langpairs != null)
				sctx.setAttribute("langpairs",langpairs);

			log.fine("TransmemServlet.init() done");
		}
		catch (java.sql.SQLException x)
		{
			log.severe("TransmemServlet.init() failed: "+x.toString());
		}
		catch (javax.naming.NamingException e)
		{
			System.out.println(e.toString());
			log.severe("TransmemServlet.init() failed: "+e.toString());
		}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		log.entering("TransmemServlet","doPost");
		
		String view = "/";
		//wrap request and response in ServletParams object and pass to action.execute
		ServletParams sp = new ServletParams(request, response);
		//get request parameter 'action' from form post
		String sAction = sp.getParameter("action");
		if (sAction != null)
		{
			//create appropriate IAction object from the action class name
			IAction action = ActionFactory.createAction(sAction);
			if (action != null)
			{
				action.execute(sp);
				view = action.getNextPage();
			}
			else
			{
				log.severe("helper.getAction() returned null");
			}
		}
		else
		{
			log.warning("Request without an action");
		}

		if (view != null)	//view is null for Ajax responses
		{
			RequestDispatcher dispatcher = request.getRequestDispatcher(view);
			dispatcher.forward(request, response);
		}
		
		log.exiting("TransmemServlet","doPost");
	}

	public void destroy()
	{
		log.entering("TransmemServlet","destroy");
		//clean up datasource
		getServletContext().removeAttribute("databases");
		getServletContext().removeAttribute("langpairs");
		log.exiting("TransmemServlet","destroy");
	}
}
