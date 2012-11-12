package com.transmem.action;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import com.transmem.nlp.*;
import com.transmem.data.db.Databases;
import com.transmem.data.db.Projects;
import com.transmem.data.db.Articles;
import com.transmem.data.db.Sources;
import com.transmem.data.db.Sentences;
import com.transmem.data.db.Languages;
import com.transmem.data.db.Users;

/**
 * Action class for share the translation of an article with others by storing
 * the sentences and translations into the corpus.
 *
 * This action is invoked by Ajax engine, so it should return differently.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan. 2007
 */
public class ShareTranslation extends BaseAction
{
	private	Logger log_ = Logger.getLogger(ShareTranslation.class.getName());

	private	ILinguist source_, target_;
	private	ResultSet rs_, rs1_, rs2_;
	
	public ShareTranslation()
	{
		super();
	}

	/**
	 * Accept user's translation for the current sentence.
	 *
	 * <p class="doc">
	 *  <ul>
	 *    <li>Request parameters
	 *      <ul>
	 *        <li><b>aid</b> - string of article ID</li>
	 *		  <li><b>domain</b> - domain of the aricle</li>
	 *		  <li><b>permit</b> - who to expose the sentences</li>
	 *      </ul>
	 *    </li>
	 *    <li>Session attributes
	 *      <ul>
	 *        <li><b>user</b> - Users object created after login or register.</li>
	 *        <li><b>articles</b> - array list of articles.</li>
	 *      </ul>
	 *    </li>
	 *    <li>Response page
	 *      <ul>
	 *        <li> - not used as a whole web page but for Ajax response</li>
	 *      </ul>
	 *    </li>
	 *  </ul>
	 * </p>
	 *
	 * @param param - HttpServletRequest object
	 * @param response - HttpServletResponse object
	 */
	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("ShareTranslation","execute");

		Session session = param.getSession();

		Users usr = session.getUser();
		if (usr == null) {
			param.sendError(MessageCode.ERR_NOT_LOGIN);
			return;
		}
		int uid = usr.getUserID();
		int group = usr.getGroup();

		String said = param.getParameter("aid");
		if (said == null)
		{
			log_.warning("ShareTranslation called without aid parameter");
			return;
		}
		ArrayList<Articles> articles = session.getArticleList();
		if (articles == null)
		{
			log_.severe("session.getArticleList() return null");
			param.sendError(MessageCode.ERR_NO_ARTICLE);
			return;
		}

		int aid = 0;
		Articles article = null;
		try
		{
			aid = Integer.parseInt(said);
			for (Articles a : articles)
			{
				if (a.getArticleID()==aid)
				{
					article = a;
					break;
				}
			}
		}
		catch (NumberFormatException e)
		{
			log_.severe("Invalid aid:"+said);
			return;
		}

		if (article == null)
		{
			log_.severe("Article given by ID "+said+" not found in list");
			return;
		}
		
		String langpair = article.getLangPair();//ENZH
		String scode = langpair.substring(0,2);	//EN
		String tcode = langpair.substring(2);	//ZH

		String domain = param.getParameter("domain");
		String permit = param.getParameter("permit");
		int npermit = 0;
		if (permit==null || permit.equals("P"))
			npermit = 0;
		else if (permit.equals("G"))
			npermit = group;
		else
			npermit = -1;
		
		log_.info("About to save aid="+aid+",lang="+langpair+",domain="+domain+",permit="+permit+",user="+uid);
		
		Connection conn = null;
		try
		{
			createIndexer(scode, tcode);
			log_.info("createIndexer("+scode+","+tcode+") done");

			conn = getConnection(param,Databases.CATEGORY_USER,true);
			conn.setAutoCommit(false);
			//get table name for the langpair
			String tableName = getTableName(conn, langpair);
			boolean swap = tableName.indexOf(scode)>2;
			log_.info("Table name = "+tableName+", swap = "+swap);
			//delete sentences from corpus if already there
			ArrayList<Long> sids = revokeSentences(conn, tableName, aid);
			log_.info("SIDs revoked : "+sids.size());
			//load sentences for the article and save to T_ENZH and T_Sources
			ArrayList<Sentences> sents = querySentences(conn, aid);
			log_.info("Sentences in article : "+sents.size());
			//copy sentences to corpus
			String seqname = "S"+tableName.substring(1);
			//create resultsets for corpus, indexes
			createResultSets(conn, scode, tcode, tableName);
			//log_.info("createResultSets() finished");
			for (int i=0; i<sents.size(); i++)
			{
				long sid = 0;
				if (i >= sids.size())
					sid = getSequenceLong(conn, seqname);
				else
					sid = sids.get(i).longValue();
				//saveSentence(conn, sid, sents.get(i));
				Sentences sent = sents.get(i);
				if (sent.getSentence()==null || sent.getTranslation()==null)
					continue;
				//log_.info("About to save sentence "+i+", sid="+sid);
				saveUnit(sid, sent, swap, aid, uid, domain, npermit);
				//log_.info("Sentence "+i+" saved, calling index()");
				index(this.rs1_, source_, sid, swap?sent.getTranslation():sent.getSentence());
				//log_.info("Index for sentence "+i+" finished");
				//index(this.rs2_, source_, sid, swap?sent.getSentence():sent.getTranslation());
			}
			//save into t_sources
			log_.info("Saving source: aid="+aid+", title="+article.getTitle());
			saveSource(conn, aid, article.getTitle());
			//done
			log_.info("Sentences saved for aid "+aid);
			conn.commit();
		}
		catch (SQLException x)
		{
			try { conn.rollback(); } catch (SQLException e) {}
			log_.severe("SQLException when getConnection(USER)"+x);
			param.sendError(MessageCode.ERR_DB_CONNECT);
		}
		catch (LanguageException le)
		{
			log_.severe(le.toString());
			param.sendError("linguistic methods failed: "+le.toString());
		}
		finally
		{
			try {
				if (this.rs_ != null) this.rs_.close();
				if (this.rs1_ != null) this.rs1_.close();
				if (this.rs2_ != null) this.rs2_.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {}
		}
	}

	protected ArrayList<Sentences> querySentences(Connection conn, int aid) throws SQLException
	{
		Sentences st = new Sentences(conn);
		return st.queryByArticle(aid);
	}

	/**
	 * Collect the SIDs used by previous upload for reuse, and delete those for repopulation.
	 * @param conn
	 * @param tableName
	 * @param aid - Article ID
	 * @return array list of example sentence IDs for reuse
	 */
	protected ArrayList<Long> revokeSentences(Connection conn, String tableName, int aid) throws SQLException
	{
		ArrayList<Long> sids = new ArrayList<Long>();
		String sql = "SELECT F_SID FROM "+tableName+" WHERE F_From="+aid;
		Statement stmt = null;
		try
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				sids.add(new Long(rs.getLong(1)));
			}
			rs.close();
			stmt.close();
			log_.info("selected "+sids.size()+" sentences");
			//delete these sentences
			if (sids.size() > 0)
			{
				sql = "DELETE FROM "+tableName+" WHERE F_From="+aid;
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);
				stmt.close();
				log_.info("deleted these from the table");
			}
			//delete from t_sources as well
			Sources src = new Sources(conn);
			src.delete(aid);
			log_.info("and deleted from t_source table");
		}
		catch (SQLException e)
		{
			log_.warning(e.toString());
		}
		return sids;
	}

	protected String getTableName(Connection conn, String langpair) throws SQLException
	{
		Languages ls = new Languages(conn);
		return ls.queryTableName(langpair);
	}
	
//--- indexing and sentence saving units can be separate into another class ---------
	protected void createIndexer(String scode, String tcode) throws LanguageException
	{
		LanguageManager.loadLangNames();
		source_ = LanguageManager.createLinguist(scode, LanguageManager.INDEXER);
		target_ = LanguageManager.createLinguist(tcode, LanguageManager.INDEXER);
	}

	/**
	 * Create three resultset: corpus table, source index table, target index table.
	 * The source index table maps the source language in the corpus, not the sentence source.
	 * So if the corpus is ENZH and the sentence is Chinese-English, then the source of sentence
	 * is the target of the corpus. In other words, rs1_ is always EN, rs2_ is always ZH_.
	 */
	protected void createResultSets(Connection conn, String scode, String tcode, String tablename) throws SQLException
	{
		String sql = "SELECT * FROM "+tablename+" WHERE F_SID = 0";
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		this.rs_ = stmt.executeQuery(sql);
		Statement stmt1 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		this.rs1_ = stmt1.executeQuery("SELECT * FROM "+tablename+"X WHERE F_Word='x'");
		Statement stmt2 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		String xtab2 = "T_"+tablename.substring(4,6)+tablename.substring(2,4)+"X";
		log_.info("2nd index table name is "+xtab2);
		this.rs2_ = stmt2.executeQuery("SELECT * FROM "+xtab2+" WHERE F_Word='x'");
	}

	protected void index(ResultSet rs, ILinguist linguist, long sid, String sent) 
		throws SQLException, LanguageException
	{
		//System.out.println(sent);
		String[] words = linguist.indexkeys(sent);
		if (words.length > 0)
		{
			Map<String,String> dup = new HashMap<String,String>();
			int i = 0;
			for (String word : words)
			{
				//System.out.print(word+" ");
				if (!dup.containsKey(word))
				{
					dup.put(word, word);
					rs.moveToInsertRow();
					rs.updateString("F_Word", word);
					rs.updateLong("F_SID", sid);
					rs.updateInt("F_Offset", i++);
					rs.insertRow();
				}
			}
			//System.out.println();
		}
	}

	protected void saveUnit(long sid, Sentences sent, boolean swap, int aid, 
		int uid, String domain, int permit) throws SQLException
	{
		//System.out.println("SID = "+sid);
		this.rs_.moveToInsertRow();
		this.rs_.updateLong("F_SID", sid);
		this.rs_.updateString("F_Source", swap?sent.getTranslation():sent.getSentence());
		this.rs_.updateString("F_Target", swap?sent.getSentence():sent.getTranslation());
		this.rs_.updateString("F_Domain", domain);
		this.rs_.updateInt("F_From", aid);
		this.rs_.updateInt("F_Permit", permit);
		this.rs_.updateInt("F_Owner", uid);
		this.rs_.insertRow();
	}

	protected void saveSource(Connection conn, int from, String sname) throws SQLException
	{
		assert(conn != null);
		PreparedStatement ps = null;
		try {
			String sql = "INSERT INTO T_Sources(F_SourceID,F_Name) VALUES(?,?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, from);
			String s = (sname==null)?" ":sname;
			ps.setString(2, s);
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			throw new SQLException(e.getMessage());
		}
		finally
		{
			if (ps != null)
			try { ps.close(); } catch (SQLException x) {}
		}
	}

}
