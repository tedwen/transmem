package com.transmem.data.tm;

import com.transmem.nlp.*;
import com.transmem.data.db.*;
import com.transmem.data.db.Databases;
//import com.transmem.nlp.LanguageException;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

/**
 * Corpus
 *
 * The Corpus class can be instantiated for each session to query the sentence
 * database for similar sentences and phrases.
 * The query method accepts a sentence of source language and queries a list of
 * similar sentences for it and presents the translations.
 * The corpus is determined by the constructor according to the language argument.
 *
 * @author Ted Wen
 * @update April, 2007
 *
 */
public class Corpus
{
	private static final Logger log_ = Logger.getLogger(Corpus.class.getName());

	private	String	source_;
	private	String	target_;
	private	boolean	swap_;	//if source is ZH, target is EN, tablename is ENZH, then swap is true
	private	String	corpusName_;
	private	String	indexName_;
	private	ISegmenter	segmenter_;
	private	IStemmer	stemmer_;
	private	Databases	dbs_;
	private	int	maxSids_ = 500;	//maximum number of example sentences allowed
	private	ArrayList<Long> sids_;
	private	int pageLen_ = 10;	//number of sentences per page to return

	/**
	 * Construct a Corpus instance with a source and target language code.
	 * @param dbs - Databases object
	 * @param langpair - 4-letter language pair like ENZH
	 */
	public Corpus(Databases dbs, String langpair)
	{
		assert(dbs != null);
		this.dbs_ = dbs;
		this.source_ = langpair.substring(0,2);
		this.target_ = langpair.substring(2);
		try
		{
			this.corpusName_ = queryTablename(langpair);
			if (this.corpusName_.indexOf(langpair)<0)
			{
				this.swap_ = true;
				this.indexName_ = "T_"+langpair+"x";
			}
			else
				this.indexName_ = this.corpusName_ + "x";
			this.segmenter_ = LanguageManager.createSegmenter(source_);
			this.stemmer_ = LanguageManager.createStemmer(source_);
		}
		catch (LanguageException le)
		{
			log_.severe("LanguageManager.createXXX("+source_+") failed: "+le.getMessage());
		}
	}

	/**
	 * Construct a Corpus instance with a source and target language code.
	 * @param dbs - Databases object
	 * @param source - 2-letter language code like EN, ZH
	 * @param target - 2-letter language code like EN, ZH
	 */
	public Corpus(Databases dbs, String source, String target)
	{
		assert(dbs != null);
		this.dbs_ = dbs;
		this.source_ = source;
		this.target_ = target;
		try
		{
			this.corpusName_ = queryTablename(source,target);
			if (this.corpusName_.indexOf(source)>2)
			{
				this.swap_ = true;
				this.indexName_ = "T_"+target+source+"x";
			}
			else
				this.indexName_ = this.corpusName_ + "x";
			this.segmenter_ = LanguageManager.createSegmenter(source);
			this.stemmer_ = LanguageManager.createStemmer(source);
		}
		catch (LanguageException le)
		{
			log_.severe("LanguageManager.createXXX("+source+") failed: "+le.getMessage());
		}
	}

	public int getMaximumSids()
	{
		return this.maxSids_;
	}

	public void setMaximumSids(int maxsids)
	{
		this.maxSids_ = maxsids;
	}
	
	public int getPageLen()
	{
		return this.pageLen_;
	}
	
	public void setPageLen(int len)
	{
		this.pageLen_ = len;
	}
	
	public String getTableName()
	{
		return this.corpusName_;
	}
	
	public String getIndexName()
	{
		return this.indexName_;
	}
	
	/**
	 * Query tablename from languages table for given langpair 4 letters.
	 * @param langpair - like ENZH
	 * @return String of table name for the language pair
	 */
	protected String queryTablename(String langpair) throws LanguageException
	{
		if (this.dbs_ == null)
			throw new LanguageException("Connection not given");
		
		Connection con = null;
		try
		{
			con = this.dbs_.getConnection(Databases.CATEGORY_CORPUS);
			Languages lang = new Languages(con);
			return lang.queryTableName(langpair);
		}
		catch (SQLException se)
		{
			throw new LanguageException("SQLException occurred while query T_Languages("+langpair+")");
		}
		finally
		{
			if (con != null)
			{
				try
				{
					con.close();
				}
				catch (SQLException se)
				{
				}
			}
		}
	}

	/**
	 * Query tablename from languages table for given source and target 2-letter language codes.
	 * @param scode - source language code, 2-letter code for F_SourceCode
	 * @param tcode - 2-letter language code for F_TargetCode
	 * @return String of table name for the language pair
	 */
	protected String queryTablename(String scode, String tcode) throws LanguageException
	{
		if (this.dbs_ == null)
			throw new LanguageException("Connection not given");
		
		Connection con = null;
		try
		{
			con = this.dbs_.getConnection(Databases.CATEGORY_CORPUS);
			Languages lang = new Languages(con);
			return lang.queryTableNameBySourceTarget(scode, tcode);
		}
		catch (SQLException se)
		{
			throw new LanguageException("SQLException occurred while query T_Languages("+scode+","+tcode+")");
		}
		finally
		{
			if (con != null)
			{
				try
				{
					con.close();
				}
				catch (SQLException se)
				{
				}
			}
		}
	}

	/**
	 * Query example source and target language sentences from the corpus for a given sentence in source language.
	 * The method returns the number of sentences found in the corpus.
	 * About domain: If the specified domain is 00, then every domain is searched, otherwise, only a specified domain.
	 * About permit: If unspecified, only Public sentences are searched, if Group is required, then
	 * the G sentences with owner is in the group are visited first, then union with Public ones; if Owner is required, then
	 * the O sentences with owner is the user are visited first, then public ones.
	 *
	 * @param userid - user's ID
	 * @param group - user's group ID
	 * @param s - sentence in source language
	 * @param domain - industrial domain tag like 'IT', '00' means all domains
	 * @param permit - permit tag like 'P','G','O'
	 * @return number of pages
	 */
	public int query(int userid, int group, String s, String domain, String permit) throws LanguageException
	{
		if (this.segmenter_ == null || this.stemmer_ == null)
		{
			throw new LanguageException("Segmenter and/or Stemmer not created, possibly language pair not supported");
		}
		//1. segment the sentence into words
		String[] words = segmenter_.segment(s);
		//2. if stemmer applies, then find the stems for these tokens
		String[] stems = new String[words.length];
		boolean b = stemmer_.applies();
		for (int i=0; i<words.length; i++)
		{
			stems[i] = b ? stemmer_.stem(words[i]) : words[i];
		}
		//3. prepare SQL query to get SID
		//select sid,count(sid) as c from t_enzh where f_word in ('','') group by sid order by c desc
		String sql = makeQueryStatement(userid, group, indexName_, corpusName_, stems, domain, permit);
		//4. query on connection
		log_.info("Corpus.query, SQL:"+sql);
		this.sids_ = new ArrayList<Long>();
		Connection con = null;
		try
		{
			con = this.dbs_.getConnection(Databases.CATEGORY_CORPUS);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			int n = 0;
			//TODO: compute distance to s and rank these sentences accordingly
			while (rs.next())
			{
				long sid = rs.getLong(1);
				this.sids_.add(new Long(sid));
				//log_.info("SID="+sid+",count="+rs.getInt(2));
				if (n++ > this.maxSids_) break;
			}
			rs.close();
			stmt.close();
			log_.info(n+" SIDs found for this query");
			//int pages = n / this.pageLen_;
			//if (n % this.pageLen_ > 0) pages ++;
			return n / this.pageLen_;
		}
		catch (SQLException se)
		{
			throw new LanguageException("SQLException occurred: "+se.getMessage());
		}
		finally
		{
			if (con != null)
			{
				try
				{
					con.close();
				}
				catch (SQLException se)
				{
				}
			}
		}
	}
	
	/**
	 * Make a SQL query statement for this query. It considers both domain and permit.
	 *
	 * @param indexName - table name of the index
	 * @param userid - user's ID
	 * @param group - group ID the user is in
	 * @param indexName - name of index table
	 * @param domain - industrial domain to limit
	 * @param permit - permitted sentences to query
	 * @return SQL string
	 */
	protected String makeQueryStatement(int userid, int group, String indexName, String tableName, String[] stems, String domain, String permit)
	{
		//select distinct x.f_sid,count(x.f_sid) as c from t_enzhx x,t_enzh a
		//where x.f_sid=a.f_sid and a.f_permit='P' and a.f_domain='00' and
		//a.f_owner=1 and f_word in ('javascript','not','java')
		//group by x.f_sid order by c desc;
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT DISTINCT x.F_SID,COUNT(x.F_SID) AS c FROM ");
		sb.append(indexName);
		sb.append(" x,");
		sb.append(tableName);
		sb.append(" a WHERE x.F_SID=a.F_SID");
		//check domain, if not 00, apply
		if (domain != null && !domain.equals("00"))
		{
			sb.append(" AND a.F_Domain='");
			sb.append(domain);
			sb.append('\'');
		}
		//check permit for 'O', 'G' + 'P'
		if (permit == null || permit.equals("P"))
			sb.append(" AND a.F_Permit=0");
		else if (permit.equals("O"))
		{
			//sb.append(" AND (a.F_Permit='P' OR a.F_Permit='O' AND a.F_Owner=");
			sb.append(" AND (a.F_Permit=0 OR a.F_Permit=-1 AND a.F_Owner=");
			sb.append(userid);
			sb.append(')');
		}
		else //if (permit.equals("G"))
		{
			//sb.append(" AND (a.F_Permit='P' OR a.F_Permit='G' AND a.F_Owner IN (SELECT F_UserID FROM T_Users WHERE F_Group=");
			sb.append(" AND (a.F_Permit=0 OR a.F_Permit=");
			sb.append(group);
			sb.append("))");
		}

		sb.append(" AND F_Word IN (");
		boolean first = true;
		for (String w : stems)
		{
			if (!first)
			{
				sb.append(',');
			}
			else
			{
				first = false;
			}
			sb.append('\'');
			sb.append(w);
			sb.append('\'');
		}
		sb.append(") GROUP BY x.F_SID ORDER BY c DESC");
		return sb.toString();
	}

	/**
	 * Query and return a page of example sentences from queried results.
	 * The query method must have been called before calling this method.
	 * @param which - index of page starting from 0,1,...n
	 * @return array list of Example objects containing source and target sentences
	 */
	public ArrayList<Example> getPage(int which) throws LanguageException
	{
		ArrayList<Example> exs = new ArrayList<Example>();
		if (this.sids_.size() > which)
		{
			int n = (which + 1) * this.pageLen_;
			if (n > this.sids_.size()) n = this.sids_.size();
			for (int i=0; i<n; i++) exs.add(null);
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT F_SID,");
			if (this.swap_)
				sb.append("F_Target,F_Source FROM");
			else
				sb.append("F_Source,F_Target FROM ");
			sb.append(this.corpusName_);
			sb.append(" WHERE F_SID IN (");
			int x = which * this.pageLen_;
			for (int i=x; i<n; i++)
			{
				if (i > x) sb.append(',');
				sb.append(this.sids_.get(i).longValue());
			}
			sb.append(")");
			Connection con = null;
			try
			{
				log_.info("Corpus.getPage "+which+", SQL="+sb.toString());
				con = this.dbs_.getConnection(Databases.CATEGORY_CORPUS);
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sb.toString());
				while (rs.next())
				{
					Example e = new Example(rs.getString(2), rs.getString(3));
					long sid = rs.getLong(1);
					//log_.info("sid="+sid);
					int pos = this.sids_.indexOf(new Long(sid));
					//log_.info("pos="+pos);
					exs.set(pos, e);
					//log_.info("exs.set("+pos+",e)");
				}
				rs.close();
				stmt.close();
			}
			catch (SQLException se)
			{
				throw new LanguageException("SQLException occurred: "+se.getMessage());
			}
			finally
			{
				if (con != null)
				{
					try
					{
						con.close();
					}
					catch (SQLException se)
					{
					}
				}
			}
		}
		return exs;
	}

	/**
	 * Query sentences for a page of SIDs and format them into HTML list.
	 * @param which - page index
	 * @return string of html-tagged examples
	 */
	public String getPageAsHtml(int which) throws LanguageException
	{
		ArrayList<Example> exs = getPage(which);
		StringBuffer sb = new StringBuffer();

		int x = which * this.pageLen_ + 1;
		//prepend the page number as a string
		sb.append(x);
		sb.append('/');
		sb.append(this.sids_.size());
		sb.append('*');
		//
		sb.append("<table cellspacing=0 cellpadding=0>");
		for (Example e : exs)
		{
			if (e != null) {
			sb.append("<tr><td>");
			sb.append(x);
			sb.append(".</td><td><font color=#C66A00>");
			sb.append(e.getSource());
			sb.append("</font></td></tr><tr><td valign=\"bottom\"><img src=\"images/b11.gif\" id=\"x");
			sb.append(x);
			sb.append("\"/></td><td id=\"ehx");
			sb.append(x);
			sb.append("\"><font color=#003399>");
			sb.append(e.getTarget());
			sb.append("</font></td></tr>");
			x ++;
			}
		}
		sb.append("</table>");
		return sb.toString();
	}
}
