package com.transmem.action;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.OutputStream;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
//import java.sql.Blob;
import java.util.ArrayList;
import java.util.Map;

import com.transmem.data.db.Databases;
import com.transmem.data.db.Projects;
import com.transmem.data.db.Articles;
import com.transmem.data.db.Users;
import com.transmem.data.db.Roles;
import com.transmem.data.db.Corpora;
import com.transmem.data.db.CorpusTally;
import com.transmem.data.db.Sources;
import com.transmem.data.tm.Corpus;
import com.transmem.doc.IUnitSaver;
import com.transmem.doc.TmxLoader;
import com.transmem.doc.BitextLoader;
import com.transmem.nlp.ISegmenter;
import com.transmem.nlp.LanguageManager;
import com.transmem.nlp.LanguageException;

//import com.transmem.doc.FileParserFactory;

/**
 * Action class for uploading a pair of bilingual text files or a TMX file.
 * The sentences will be aligned and saved in the selected language corpus.
 * It returns to the edit page for editing the sentences.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jun. 2007
 */
public class UploadAction extends BaseAction implements IUnitSaver
{
	private Logger log_ = Logger.getLogger(UploadAction.class.getName());

	private	Session session_;
	private	Connection conn_;
	private	int uid_;
	private	String format_;
	private	String domain_;
	private	int permit_;
	private	int from_;
	private	boolean	failed_;
	private	String seqname_;
	private String srclang_;
	private String dstlang_;
	private ISegmenter sourceSegmenter_;
	private ISegmenter targetSegmenter_;
	private	ResultSet rs_, rs1_, rs2_;
	private int units_;
	private	String title_;

	public UploadAction()
	{
		super();
	}

	/**
	 * Execute action for saving bilingual sentences from an uploaded TMX file
	 * or two text files. The sentences may not be loaded as they are. If a sentence is too long
	 * (greater than 1000 characters) it may be broken into two or even more sentences. In this
	 * case, the translation sentence will be aligned to the separate sub-sentences. The alignment may be
	 * incorrect and need manual editing. After the sentences are loaded, it returns to the sentence
	 * compare page where the author can edit and correct the text sentence by sentence.
	 * 
	 * Request Parameters:
	 * filetype : either 'tmx' or 'bitext'
	 * bisfile, bidfile: when filetype is bitext, these are the source and target language files
	 * tmxfile: the filename for the tmx file
	 * 
	 */
	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("UploadAction","execute");

		param.setContentType("text/html;charset=utf-8");
		
		this.session_ = param.getSession();

		Users usr = session_.getUser();
		if (usr == null)
		{
			param.sendError(MessageCode.ERR_NOT_LOGIN);
			return;
		}
		this.uid_ = usr.getUserID();

		this.failed_ = false;
		this.from_ = 0;
		this.units_ = 0;

		this.domain_ = "00";
		if (param.getParameter("domain")!=null)
			this.domain_ = param.getParameter("domain");

		String spermit = param.getParameter("permit");
		this.permit_ = 0;
		if (spermit==null || spermit.equals("P"))
			this.permit_ = 0;
		else if (spermit.equals("G"))
			this.permit_ = usr.getGroup();
		else
			this.permit_ = -1;	//private use

		this.srclang_ = param.getParameter("bislang");
		this.dstlang_ = param.getParameter("bidlang");
		
		this.title_ = "Untitled document";
		if (param.getParameter("filetitle")!=null)
		{
			this.title_ = param.getParameter("filetitle");
			try 
			{
				byte[] sb = this.title_.getBytes("ISO-8859-1");
				this.title_ = new String(sb, "UTF-8");
			} 
			catch (java.io.UnsupportedEncodingException e)
			{
            }
		}

		String filetype = param.getParameter("filetype");
		if (filetype.equals("tmx"))
		{
			this.format_ = "TMX";
			parseTmx(this.session_, param);
			setNextPage(PageLinks.MYCORPUS_PAGE);
		}
		else
		{
			this.format_ = "TXT";
			parseBitext(this.session_, param);
			setNextPage(PageLinks.MYSENTENCES_PAGE);
		}
	}

	/**
	 * Parse a TMX file to filter out the sentence pairs and save in the database.
	 * A valid TMX file should follow the standard, with the following essential tags:
	 * &lt;?xml encoding="UTF-8"?&gt;
	 * &lt;tmx&gt;
	 * &lt;header&gt;
	 * &lt;prop type="Domain"&gt;the domain id such as IT&lt/prop&gt;
	 * <prop type="Source">(-1)what dictionary</prop>
	 * </head>
	 * <body>
	 * <tu tuid="xx">
	 * <tuv xml:lang="EN">
	 * <seg><![CDATA[sentence...]]></seg>
	 * </tuv>
	 * <tuv xml:lang="ZH">
	 * <seg>...</seg>
	 * </tuv>
	 * </tu>
	 * </body>
	 */
	protected void parseTmx(Session session, ServletParams param) throws ServletException, IOException
	{
		String tmxfilename = param.getFilePathName("tmxfile");
		if (tmxfilename == null) {
			java.util.Enumeration en = param.getFileNames();
			while (en.hasMoreElements()) {
				log_.info((String)en.nextElement());
			}
		}

		log_.info("About to parse file '"+tmxfilename+"'");
		//create a loader which will call this.saveUnit to save a sentence pair
		TmxLoader loader = new TmxLoader(tmxfilename, this);

		log_.info("TMX file '"+tmxfilename+"' finished.");
	}

	/**
	 * Parse two text files by aligning sentence by sentence.
	 */
	protected void parseBitext(Session session, ServletParams param) throws ServletException, IOException
	{
		String sfilename = param.getFilePathName("bisfile");
		String tfilename = param.getFilePathName("bidfile");
		String slang = param.getParameter("bislang");
		String tlang = param.getParameter("bidlang");
		String sencoding = param.getParameter("senc");
		String tencoding = param.getParameter("denc");

		log_.info(String.format("About to parse file '%s' and '%s'",sfilename,tfilename));
		//create a loader which will call this.saveUnit to save a sentence pair
		BitextLoader loader = new BitextLoader(sfilename, tfilename, sencoding, tencoding, this);
	}

	/**
	 * IUnitSaver interface method, called by Loader when parsing starts
	 */
	public void start()
	{
		this.conn_ = null;
		try
		{
			this.conn_ = getConnection(this.session_.getHttpSession(),Databases.CATEGORY_MAIN,true);
			if (this.conn_ == null) {
				log_.severe("getConnection(MAIN,true) return null");
			}
			this.conn_.setAutoCommit(false);
		}
		catch (IOException ioe)
		{
			log_.severe(ioe.toString());
			this.failed_ = true;
		}
		catch (ServletException se)
		{
			log_.severe(se.toString());
			this.failed_ = true;
		}
		catch (SQLException x)
		{
			log_.severe("SQLException when getConnection(USER)"+x.toString());
			this.failed_ = true;
			//param.sendError(MessageCode.ERR_DB_CONNECT);
			//return;
		}
	}

	/**
	 * IUnitSaver interface method, called by Loader when parsing ends
	 */
	public void end()
	{
		if (this.conn_ != null)
		{
			try
			{
				if (!this.failed_)
				{
					//save into t_sources
					log_.info("Saving source: aid="+this.from_+", title="+this.title_);
					Corpus.saveSource(this.conn_, this.from_, this.title_, this.format_, this.uid_, 
						this.srclang_, this.dstlang_, this.units_, this.domain_, this.permit_);
					//save t_corpora
					Corpus.saveCorpora(this.conn_, this.srclang_, this.dstlang_, this.domain_, this.permit_, this.units_);
					//done
					log_.info(this.units_+" units saved");
					//update variables in session
					updateSession(this.conn_, this.session_);
					//add points to the user if shared by the public
					if (this.permit_ == 0)
						addPoints(this.conn_, this.session_, this.units_);
					this.conn_.commit();
				}
				else
					this.conn_.rollback();
				this.conn_.close();
				this.conn_ = null;
			}
			catch (SQLException x)
			{
				try { this.conn_.rollback(); } catch (SQLException se) {}
				log_.warning("conn.close() exception");
			}
		}
	}

	/**
	 * IUnitSaver interface method, called by Loader when a property is ready
	 */
	public void setProperty(String key, String value)
	{
		if (key.equalsIgnoreCase("Domain"))
		{
			this.domain_ = value;
		}
		else if (key.equalsIgnoreCase("Source"))
		{
			if (value.indexOf('(')>=0)
			{
				value = value.substring(value.indexOf('(')+1,value.indexOf(')'));
			}
			try
			{
				this.from_ = Integer.parseInt(value);
			}
			catch (NumberFormatException nfe)
			{
				log_.warning("setPropery('Source',"+value+"), bad number");
			}
		}
		else if (key.equalsIgnoreCase("srclang"))	//valid for TMX
		{
			this.srclang_ = value;
			log_.info("Set source language to "+value);
		}
		else if (key.equalsIgnoreCase("dstlang"))
		{
			this.dstlang_ = value;
			log_.info("Set target language to "+value);
		}
	}

	/**
	 * Update session variables for examples tally and my list.
	 */
	private void updateSession(Connection conn, Session session) throws SQLException
	{
		Corpora c = new Corpora(conn);
		ArrayList<CorpusTally> cts = c.queryCorpusStats(this.srclang_, this.dstlang_);
		log_.info("queryCorpusStats return "+cts.size());
		session.setCorpusTally(cts);
		//load corpus tables into session
		ArrayList<String> corpusNames = c.queryCorpusTables();
		log_.info("Corpora.queryCorpusTables() return "+corpusNames.size()+" names");
		session.setCorpusNames(corpusNames);
		//load my sources
		Sources s = new Sources(conn);
		ArrayList<Sources> srcs = s.queryByOwner(this.uid_);
		log_.info(srcs.size()+" sources loaded for user "+this.uid_);
		session.setCorpusSourceList(srcs);
	}

	/**
	 * IUnitSaver interface method, called by Loader when a pair of sentences ready for saving
	 */
	public void saveUnit(String src, String dst) throws java.sql.SQLException
	{
		if (this.sourceSegmenter_==null || this.targetSegmenter_==null)
		{
			createDatasource();
		}
		long sid = getSequenceLong(this.conn_, this.seqname_);
		log_.info("About to save sentence sid="+sid);
		this.rs_.moveToInsertRow();
		this.rs_.updateLong("F_SID", sid);
		this.rs_.updateString("F_Source", src);
		this.rs_.updateString("F_Target", dst);
		this.rs_.updateString("F_Domain", this.domain_);
		if (this.from_ == 0)
			this.from_ = getSequenceInt(this.conn_, "S_Sources");
		this.rs_.updateInt("F_From", this.from_);
		this.rs_.updateInt("F_Permit", this.permit_);
		this.rs_.updateInt("F_Owner", this.uid_);
		this.rs_.insertRow();
		//log_.info("Sentence "+i+" saved, calling index()");
		try
		{
		Corpus.makeIndices(this.rs1_, this.sourceSegmenter_, sid, src);
		//log_.info("Index for sentence "+i+" finished");
		Corpus.makeIndices(this.rs2_, this.targetSegmenter_, sid, dst);
		}
		catch (LanguageException le)
		{
			log_.severe("Error at Corpus.makeIndices(): "+le.toString());
			throw new SQLException("LanguageException at Corpus.makeIndices():"+le.toString());
		}
		//
		this.units_ ++;
	}

	private void createDatasource() throws SQLException
	{
		try
		{
			//check availability of table, if not create it
			Corpora cs = new Corpora(this.conn_);
			if (cs.countTableByPair(this.srclang_, this.dstlang_) <= 0)
			{
				Corpus.createCorpusTables(this.srclang_, this.dstlang_, this.conn_);
				log_.info("Corpus tables for "+this.srclang_+"->"+this.dstlang_+" are created");
			}
			createSegmenters(this.session_, this.srclang_, this.dstlang_);
			log_.info("createSegmenters("+this.srclang_+","+this.dstlang_+") done");
			//get table name for the lang pair
			String tableName = makeCorpusTableName(this.srclang_, this.dstlang_);
			//delete sentences from corpus if already there
			this.seqname_ = "S"+tableName.substring(1);
		//	ArrayList<Long> sids = revokeSentences(conn, tableName, aid);
			//create resultsets for corpus, indexes
			createResultSets(this.conn_, this.srclang_, this.dstlang_, tableName);
			log_.info("createResultSets() finished");
		}
		catch (SQLException e)
		{
			log_.warning("saveUnit caused SQLException: "+e.toString());
			this.failed_ = true;
			throw e;
		}
		catch (LanguageException le)
		{
			log_.warning("createDatasource() caused LanguageException: "+le.toString());
			this.failed_ = true;
			throw new SQLException("LanguageException at createSegmenters:"+le.toString());
		}
	}

	/**
	 * Create segmenters based on the specified language codes.
	 * The ISegmenter objected are created and saved in the member variables: source_, target_.
	 * @param session - Session for the Http session
	 * @param scode - source language code like EN
	 * @param tcode - target language code like ZH
	 */
	private void createSegmenters(Session session, String scode, String tcode) throws LanguageException
	{
		javax.servlet.ServletContext app = session.getHttpSession().getServletContext();
		Map<String,String> codenames = (Map<String,String>)app.getAttribute("languages");
		String slang = codenames.get(scode);
		String tlang = codenames.get(tcode);
		this.sourceSegmenter_ = LanguageManager.createSegmenter(slang);
		this.targetSegmenter_ = LanguageManager.createSegmenter(tlang);
	}

	/**
	 * Create three resultset: corpus table, source index table, target index table.
	 * The source index table maps the source language in the corpus, not the sentence source.
	 * @param conn - Connection reference
	 * @param scode - source language code like EN
	 * @param tcode - target language code like ZH
	 */
	protected void createResultSets(Connection conn, String scode, String tcode, String tablename) throws SQLException
	{
		String index1 = String.format("%s_%sX",tablename,scode);
		String index2 = String.format("%s_%sX",tablename,tcode);

		String sql = "SELECT * FROM "+tablename+" WHERE F_SID = 0";
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		this.rs_ = stmt.executeQuery(sql);

		Statement stmt1 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		this.rs1_ = stmt1.executeQuery("SELECT * FROM "+index1+" WHERE F_Word='x'");

		Statement stmt2 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		this.rs2_ = stmt2.executeQuery("SELECT * FROM "+index2+" WHERE F_Word='x'");
	}

	/**
	 * Add points to the user who contributed example sentences to the public resevoir.
	 * The number of points is determined by the number of sentences times the rate of points per sentence.
	 * @param conn - Connection ref
	 * @param session - Session ref
	 * @param sents - number of sentences
	 */
	protected void addPoints(Connection conn, Session session, int sents) throws SQLException
	{
		int rate = session.getPointsPerShare().intValue();
		Users usr = session.getUser();
		usr.setConnection(conn);
		usr.clearUpdates();
		usr.setPoints(usr.getPoints() + rate * sents);
		usr.update();
	}

/*
	protected String getFileFormat(String filename)
	{
		String fileformat = "";
		if (filename.length() < 4)
		{
			return "";
		}
		fileformat = filename.substring(filename.lastIndexOf('.')+1).toUpperCase();
		if (fileformat.equalsIgnoreCase("html")) {
			fileformat = "HTM";
		} else if (fileformat.length() != 3) {
			return "";
		}
		return fileformat;
	}*/
}
