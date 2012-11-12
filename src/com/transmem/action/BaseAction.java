package com.transmem.action;

import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.transmem.data.db.Databases;
import com.transmem.data.db.Languages;

/**
 * Abstract class to implement the IAction interface.
 * It implements some shared methods such as getNextPage.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan. 2007
 */
public abstract class BaseAction implements IAction
{
	protected	String	nextPage_;

	public BaseAction()
	{
	}

	public abstract void execute(ServletParams params)
		throws ServletException, IOException;

	public String getNextPage()
	{
		return this.nextPage_;
	}

	public void setNextPage(String nextPage)
	{
		this.nextPage_ = nextPage;
	}
/*
	public void setErrorPage(HttpSession session, String errorCode, String errorMsg)
		throws ServletException
	{
		session.setAttribute("errorcode",errorCode);
		session.setAttribute("errors",errorMsg);
		this.nextPage_ = PageLinks.ERROR_PAGE;
	}
*/
	/**
	 * Search for a java.sql.Connection from the instance of Databases stored as an attribute in ServletContext.
	 * The Databases instance keeps a list of DataSource objects created at the initialization of the servlet.
	 *
	 * @param request - ServletParams for this session
	 * @param cat - Databases.Category
	 * @param forWrite - true if SQL operation is for update, false for query only
	 * @return java.sql.Connection
	 */
	public Connection getConnection(ServletParams request, String cat, boolean forWrite)
		throws ServletException, IOException, SQLException
	{
		ServletContext ctx = request.getSession().getServletContext();
		Databases dbs = (Databases)ctx.getAttribute("databases");
		if (dbs == null)
			throw new IOException("BaseAction.getConnection(): ServletContext contains no 'databases' attribute.");
		return dbs.getConnection(cat, forWrite);
	}

	public Connection getConnection(ServletParams request, String cat)
		throws ServletException, IOException, SQLException
	{
		return getConnection(request, cat, false);
	}

	/**
	 * Return a Databases object which can be used to get a database connection from the pool.
	 * @param request - HttpServletRequest for the session
	 * @return Databases object
	 */
	protected Databases getDatabases(ServletParams request) throws ServletException, IOException, SQLException
	{
		ServletContext ctx = request.getSession().getServletContext();
		Databases dbs = (Databases)ctx.getAttribute("databases");
		return dbs;
	}

	/**
	 * Query for the next sequence value from a specific sequence. It must be a 8-byte long value.
	 *
	 * @param conn - Connection object (not closed in this method)
	 * @param seq - name of the sequence
	 * @return long value of the sequence value
	 */
	protected long getSequenceLong(Connection conn, String seq) throws SQLException
	{
		String sql = "SELECT nextval('"+seq+"')";	//e.g. S_ENZH
		java.sql.Statement stmt = conn.createStatement();
		java.sql.ResultSet rs = stmt.executeQuery(sql);
		long sid = 0;
		if (rs.next())
			sid = rs.getLong(1);
		rs.close();
		stmt.close();
		return sid;
	}

	/**
	 * Query for the next sequence value from a specific sequence. It must be a 4-byte int value.
	 *
	 * @param conn - Connection object (not closed in this method)
	 * @param seq - name of the sequence
	 * @return int value of the sequence value
	 */
	protected int getSequenceInt(Connection conn, String seq) throws SQLException
	{
		String sql = "SELECT nextval('"+seq+"')";	//e.g. S_ENZH
		java.sql.Statement stmt = conn.createStatement();
		java.sql.ResultSet rs = stmt.executeQuery(sql);
		int sid = 0;
		if (rs.next())
			sid = rs.getInt(1);
		rs.close();
		stmt.close();
		return sid;
	}
	
	/**
	 * Query the tablename for a given language pair like ENZH and ZHEN returning T_ENZH.
	 * @param conn - Connection object
	 * @param langpair - String for language pair like ENZH
	 * @return String of table name for the corpus
	 */
	protected String getCorpusTableName(Connection conn, String langpair) throws SQLException
	{
		Languages lang = new Languages(conn);
		String tabname = lang.queryTableName(langpair);
		return tabname;
	}
}
