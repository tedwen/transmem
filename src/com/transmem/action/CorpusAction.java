package com.transmem.action;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.transmem.data.db.Databases;
import com.transmem.data.db.Users;
import com.transmem.data.db.Corpora;
import com.transmem.data.db.CorpusTally;
import com.transmem.data.db.Sources;
import com.transmem.data.db.Transunit;

/**
 * Action class for Corpus-related operations.
 *
 * @version 0.1
 * @author Ted Wen
 * @date May. 2007
 */
public class CorpusAction extends BaseAction
{
	private Logger log_ = Logger.getLogger(CorpusAction.class.getName());
	
	public CorpusAction()
	{
		super();
	}
	
	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("CorpusAction","execute");

		Session session = param.getSession();

		Users user = session.getUser();
		if (user == null) {
			log_.severe("session.getAttribute('user') returned null");
			param.sendError(MessageCode.ERR_NOT_LOGIN);
			return;
		}

		String langpair = param.getParameter("arg1");
		if (langpair == null || langpair.length()<4) {
			langpair = session.getLangPair();
			if (langpair == null || langpair.length()<4) {
				langpair = "ENZH";	//TODO: make this depend on user's
			}
		}

		String op = param.getParameter("operation");
		if (op == null || op.length()<1)
		{
			//param.sendError("Form operation not specified");
			op = session.getCorpusOp();
			if (op == null)
			{
				op = "tally";
			}
		}
		if (op.equals("tally"))
		{
			if (session.getCorpusTally()!=null)
			{
				setNextPage(PageLinks.CORPUS_TALLY_PAGE);
				session.setCorpusOp(op);
				return;
			}
		}
		else if (op.equals("sources"))
		{
			if (session.getCorpusSourceList()!=null)
			{
				setNextPage(PageLinks.MYCORPUS_PAGE);
				session.setCorpusOp(op);
				return;
			}
		}
		else if (op.equals("units"))
		{
			if (session.getCorpusUnitList()!=null)
			{
				setNextPage(PageLinks.MYSENTENCES_PAGE);
				session.setCorpusOp(op);
				return;
			}
		}
		else
		{
			op = "tally";
		}

		log_.info("Operation="+op+", langpair="+langpair);

		String srcid = null;
		Connection conn = null;
		try
		{
			conn = getConnection(param,Databases.CATEGORY_USER);
			conn.setAutoCommit(false);

			if (op.equals("delsources"))
			{
				if (param.getParameter("arg2")!=null)
				{
					deleteSources(conn, langpair, param.getParameter("arg2"));
				}
				op = "sources";
			}
			else if (op.equals("delunits"))
			{
				if (param.getParameter("arg2")!=null)
				{
					deleteUnits(conn, langpair, param.getParameter("arg2"));
				}
				op = "units";
				srcid = session.getSourceID();
			}
			if (op.equals("tally"))
			{
				Corpora c = new Corpora(conn);
				ArrayList<CorpusTally> cts = c.queryCorpusStats(langpair);
				log_.info("queryCorpusStats return "+cts.size());
				session.setCorpusTally(cts);
				setNextPage(PageLinks.CORPUS_TALLY_PAGE);
				log_.info("Redirecting to "+PageLinks.CORPUS_TALLY_PAGE);
			}
			else if (op.equals("sources"))
			{
				Sources s = new Sources(conn);
				log_.info("loading sources for user "+user.getUserID());
				ArrayList<Sources> srcs = s.queryByOwner(user.getUserID());
				log_.info(srcs.size()+" sources loaded for user "+user.getUserID());
				session.setCorpusSourceList(srcs);
				setNextPage(PageLinks.MYCORPUS_PAGE);
			}
			else if (op.equals("units"))
			{
				String tablename = getCorpusTableName(conn, langpair);
				if (srcid == null) srcid = param.getParameter("arg2");
				if (srcid == null) srcid = session.getSourceID(); else session.setSourceID(srcid);
				int srcidn = Integer.parseInt(srcid);
				//TODO: retrieve page by page!
				String sql = "SELECT * FROM "+tablename+" WHERE F_From=?";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setInt(1, srcidn);
				ResultSet rs = ps.executeQuery();
				ArrayList<Transunit> sents = new ArrayList<Transunit>();
				while (rs.next())
				{
					Transunit tu = new Transunit();
					tu.setSid(rs.getLong(1));	//F_SID
					tu.setSource(rs.getString(2));	//F_Source
					tu.setTarget(rs.getString(3));	//F_Target
					sents.add(tu);
					/*sent[0] = rs.getLong(1);	//F_SID
					sent[1] = rs.getString(2);	//F_Source
					sent[2] = rs.getString(3);	//F_Target
					sent[3] = rs.getString(4);	//F_Domain
					sent[4] = rs.getInt(5);		//F_Owner
					sent[5] = rs.getInt(6);		//F_Permit
					sent[6] = rs.getInt(7);		//F_From
					sents.add(sent);*/
				}
				rs.close();
				ps.close();
				session.setCorpusUnitList(sents);
				setNextPage(PageLinks.MYSENTENCES_PAGE);
			}
			//finally, remember the display page
			conn.commit();
			session.setCorpusOp(op);
			//log_.info("Corpus Op="+op);
		}
		catch (NumberFormatException nfe)
		{
			try {conn.rollback();} catch (SQLException xe) {}
			log_.warning(nfe.getMessage());
			param.sendError(999,nfe.toString());
		}
		catch (SQLException e)
		{
			try {conn.rollback();} catch (SQLException xe) {}
			log_.severe(e.getMessage());
			param.sendError(999,e.toString());
		}
		finally
		{
			if (conn != null)
				try { conn.close(); } catch (SQLException x) {}
		}
	}

	/**
	 * Delete some source records.
	 * These records may be referenced by some translation units in different tables such as T_ENZH.
	 * To delete these source records, the referencing units must be deleted first.
	 * Another note is that this feature might be abused by hackers. therefore, it must be strictly
	 * checked for user credibility and it is necessary to backup the deleted records into some
	 * other tables in order to recover if the records are deleted by accident or hacking.
	 * @param conn - Connection
	 * @param langpair - ENZH
	 * @param sids - Source IDs as a string separated by commas
	 */
	protected void deleteSources(Connection conn, String langpair, String sids) throws SQLException
	{
		//1.remove leading or trailing commas if any
		if (sids.startsWith(",")) sids = sids.substring(1);
		if (sids.endsWith(",")) sids = sids.substring(0,sids.length()-1);
		//2. delete units first (assuming no Cascaded delete on DBMS)
		String tablename = getCorpusTableName(conn,langpair);
		//2.1 backup these units
		//TODO:
		//2.2 delete these units
		String sql = "DELETE FROM "+tablename+" WHERE F_From IN ("+sids+")";
		java.sql.Statement st = conn.createStatement();
		try {
			st.executeUpdate(sql);
		} catch (SQLException se) {
			throw new SQLException(se.getMessage());
		} finally {
			st.close();
		}
		//3. delete the list from t_sources
		//3.1 backup the source first
		//TODO:
		//3.2 delete the sources
		sql = "DELETE FROM T_Sources WHERE F_SourceID IN ("+sids+")";
		st = conn.createStatement();
		try {
			st.executeUpdate(sql);
		} catch (SQLException se) {
			throw new SQLException(se.getMessage());
		} finally {
			st.close();
		}
	}

	/**
	 * Delete some translation units from example table.
	 * @param conn - Connection
	 * @param langpair - ENZH
	 * @param sids - Sentence IDs as a string separated by commas
	 */
	protected void deleteUnits(Connection conn, String langpair, String sids) throws SQLException
	{
		//1.remove leading or trailing commas if any
		if (sids.startsWith(",")) sids = sids.substring(1);
		if (sids.endsWith(",")) sids = sids.substring(0,sids.length()-1);
		//2.determine table name from langpair
		String tablename = getCorpusTableName(conn,langpair);
		//3.backup these deleted records to another table
		//TODO:
		//4.delete from corpus
		String sql = "DELETE FROM "+tablename+" WHERE F_SID IN ("+sids+")";
		java.sql.Statement st = conn.createStatement();
		try {
			st.executeUpdate(sql);
		} catch (SQLException se) {
			throw new SQLException(se.getMessage());
		} finally {
			st.close();
		}
	}

}
