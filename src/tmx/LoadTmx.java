package loader;

import java.sql.*;

//import javax.xml.parsers.*;
//import org.w3c.dom.*;
//import org.xml.sax.*;
import java.io.IOException;
import java.io.File;
import java.util.Map;
import java.util.HashMap;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;

import com.transmem.nlp.*;

/**
 * Customized exception object
 */
class LoadException extends Exception
{
	public static final long serialVersionUID = -1;
	public LoadException(String msg)
	{
		super(msg);
	}
}

/**
 * Load a TMX file into the example database.
 *
 * @author Ted Wen
 * @update April, 2007
 *
 * This module works for English-Chinese sentences only.
 * The TMX tu elements contain attribute xml:lang="EN" and ZH respectively.
 * Other language units are ignored.
 * The sentence and translation are inserted into table T_ENZH.
 * Words are stored in the T_ENZHX and T_ZHENX index tables.
 * Segmentation and stemming done by com.transmem.nlp.* modules.
 *
 * Note: to use another host, change the getConnection paramter 'localhost' in openDatabase() method.
 * To enforce security, change username and password from postgres to another restricted user account.
 */
public class LoadTmx extends DefaultHandler
{
	private	Connection conn_ = null;
	private	ResultSet rs_ = null;
	private	ResultSet rs1_ = null;
	private	ResultSet rs2_ = null;
	private	ILinguist english_;
	private ILinguist chinese_;
	private	String	domain_ = "00";
	private	int	from_ = 0;
	private	String sourceName_;
	
	private String lang_;
	private StringBuffer sb_;
	private String en_, zh_;
	private int status_, ptype_;
	private static final int E_NONE = 0;
	private static final int E_TUV = 1;
	private static final int E_SEG = 2;
	private static final int E_PROP = 3;
	private static final int A_DOMAIN = 5;
	private static final int A_SOURCE = 6;

	public LoadTmx(String filename) throws LoadException
	{
		//loadTmxFile(filename);
		parseTmxFile(filename);
	}

	protected long getNextSid() throws LoadException
	{
		try
		{
			String sql = "SELECT nextval('S_ENZH')";
			Statement stmt = this.conn_.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			long sid = 0;
			if (rs.next())
				sid = rs.getLong(1);
			rs.close();
			stmt.close();
			return sid;
		}
		catch (SQLException se)
		{
			throw new LoadException("SQLException occurred: "+se.getMessage());
		}
	}

	protected long saveUnit(String en, String zh) throws LoadException
	{
		if (this.rs_ == null)
		{
			String sql = "SELECT * FROM T_ENZH WHERE F_SID = 0";
			try
			{
				Statement stmt = this.conn_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
				this.rs_ = stmt.executeQuery(sql);
				Statement stmt1 = this.conn_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
				this.rs1_ = stmt1.executeQuery("SELECT * FROM T_ENZHX WHERE F_Word='x'");
				Statement stmt2 = this.conn_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
				this.rs2_ = stmt2.executeQuery("SELECT * FROM T_ZHENX WHERE F_Word='x'");
			}
			catch (SQLException se)
			{
				throw new LoadException("SQLException occurred: "+se.getMessage());
			}
		}
		if (this.rs_ == null)
			throw new LoadException("ResultSet not ready! ");

		long sid = getNextSid();
		//System.out.println("SID = "+sid);
		try
		{
			this.rs_.moveToInsertRow();
			this.rs_.updateLong("F_SID", sid);
			this.rs_.updateString("F_Source", en);
			this.rs_.updateString("F_Target", zh);
			this.rs_.updateString("F_Domain", this.domain_);
			this.rs_.updateInt("F_From", this.from_);
			//this.rs_.updateString("F_Permit", "P");	//default
			//this.rs_.updateInt("F_Owner", 0);	//default
			this.rs_.insertRow();
			return sid;
		}
		catch (SQLException se)
		{
			throw new LoadException("SQLException occurred: "+se.getMessage());
		}
	}

	protected void saveSource() throws LoadException
	{
		PreparedStatement ps = null;
		try {
			String sql = "INSERT INTO T_Sources(F_SourceID,F_Name) VALUES(?,?)";
			ps = this.conn_.prepareStatement(sql);
			ps.setInt(1,this.from_);
			String s = (this.sourceName_==null)?"":this.sourceName_;
			ps.setString(2,s);
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			throw new LoadException(e.getMessage());
		}
		finally
		{
			if (ps != null)
			try { ps.close(); } catch (SQLException x) {}
		}
	}

	protected void index(ResultSet rs, ILinguist linguist, long sid, String sent) throws LoadException
	{
		if (rs == null)
			throw new LoadException("Index ResultSet null");
		try
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
		catch (SQLException se)
		{
			throw new LoadException("SQLException occurred: "+se.getMessage());
		}
		catch (LanguageException le)
		{
			throw new LoadException("LanguageException occurred indexing: "+le.getMessage());
		}
	}

	protected void openDatabase() throws LoadException
	{
		try 
		{
			conn_ = DriverManager.getConnection("jdbc:postgresql://localhost/transmem","tm", "yi4ku4wh3");
			conn_.setAutoCommit(false);
		} 
		catch (SQLException se) 
		{
			throw new LoadException("SQLException occurred: "+se.getMessage());
		}
	}

	protected void closeDatabase()
	{
		if (this.conn_ != null)
		{
			try
			{
				conn_.close();
			}
			catch (SQLException e)
			{
			}
		}
	}
	
	protected void createIndexer() throws LoadException
	{
		try
		{
			LanguageManager.loadLangNames();
			english_ = LanguageManager.createLinguist("EN", LanguageManager.INDEXER);
			chinese_ = LanguageManager.createLinguist("ZH", LanguageManager.INDEXER);
		}
		catch (LanguageException le)
		{
			throw new LoadException("LanguageException occurred while creating linguist: "+le.getMessage());
		}
	}

	public void startDocument() throws SAXException
	{
		//System.out.println("startDocument()");
		try
		{
			openDatabase();
		}
		catch (LoadException le)
		{
			System.err.println(le.getMessage());
			throw new SAXException(le.getMessage());
		}
		try
		{
			createIndexer();
		}
		catch (LoadException le)
		{
			closeDatabase();
			System.err.println(le.getMessage());
			throw new SAXException(le.getMessage());
		}
	}
	public void endDocument() throws SAXException
	{
		//System.out.println("endDocument()");
		try
		{
			conn_.commit();
			closeDatabase();
		}
		catch (SQLException sqle)
		{
			throw new SAXException(sqle.toString());
		}
	}

	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException
	{
		//System.out.println("startElement("+qName+","+atts.getLength()+")");
		//from_ ++;
		//if (qName.equals("tmx")) this.isTmx_ = true;
		if (qName.equals("tuv"))
		{
			this.status_ = E_TUV;
			this.lang_ = atts.getValue("xml:lang");
		}
		else if (qName.equals("seg"))
		{
			this.status_ = E_SEG;
			this.sb_ = new StringBuffer();
		}
		else if (qName.equals("prop"))
		{
			this.status_ = E_PROP;
			String sType = atts.getValue("type");
			if (sType.equalsIgnoreCase("Domain"))
				this.ptype_ = A_DOMAIN;
			else if (sType.equalsIgnoreCase("Source"))
				this.ptype_ = A_SOURCE;
			else
				this.ptype_ = 0;
			//System.out.println("<prop type="+sType+">");
		}
	}
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		//System.out.println("endElement("+qName+")");
		//if (from_ > 10) System.exit(0);
		if (this.status_ == E_SEG)
		{
			//save sentence
			if (this.lang_.equalsIgnoreCase("EN"))
			{
				this.en_ = this.sb_.toString();
			}
			else if (this.lang_.equalsIgnoreCase("ZH"))
			{
				this.zh_ = this.sb_.toString();
			}
			if (zh_ != null && en_ != null && en_.length()>0 && zh_.length()>0 && en_.length()<2000 && zh_.length()<2000)
			{
				try
				{
					long sid = saveUnit(en_, zh_);
					index(this.rs1_, english_, sid, en_);
				}
				catch (LoadException le)
				{
					throw new SAXException(le.toString());
				}
				this.en_ = this.zh_ = null;
			}
			//System.out.println(this.sb_.toString());
			this.status_ = E_NONE;
		}
		else if (qName.equals("header"))
		{
			System.out.print("saveSource: domain="+this.domain_+", from="+this.from_);
			try
			{
				saveSource();
			} catch (LoadException le)
			{
				throw new SAXException(le.toString());
			}
		}
	}

	public void characters(char[] ch, int start, int length) throws SAXException
	{
		//System.out.println("characters: "+length);
		if (this.status_ == E_SEG)
		{
			this.sb_.append(ch, start, length);
		}
		else if (this.status_ == E_PROP)
		{
			//System.out.println(new String(ch,start,length));
			if (this.ptype_ == A_DOMAIN)
			{
				this.domain_ = new String(ch, start, length);
			}
			else if (this.ptype_ == A_SOURCE)
			{
				String s = new String(ch, start, length);
				int n1 = s.indexOf('(');
				int n2 = s.indexOf(')');
				String sn = s.substring(n1+1, n2);
				try {
					this.from_ = Integer.parseInt(sn);
				} catch (Exception e)
				{
				}
				this.sourceName_ = s.substring(n2+1);
			}
			this.status_ = E_NONE;
		}
	}

	/**
	 * Parse an XML (TMX) file using the SAX parser in order to process really large TMX files.
	 */
	public void parseTmxFile(String filename) throws LoadException
	{
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			File f = new File(filename);
			parser.parse(f, this);
		}
		catch (org.xml.sax.SAXException e)
		{
			if (this.conn_ != null)
			{
				closeDatabase();
			}
			throw new LoadException(e.getMessage());
		}
		catch (IOException ioe)
		{
			throw new LoadException(ioe.getMessage());
		}
		catch (javax.xml.parsers.ParserConfigurationException x)
		{
			throw new LoadException(x.toString());
		}
	}
/*
	public void loadTmxFile(String filename) throws LoadException
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(filename);
			Element root = doc.getDocumentElement();
			//System.out.println(root.getNodeName());
			if (!root.getNodeName().equals("tmx"))
			{
				throw new LoadException("Not a valid TMX file.");
			}
			NodeList nodes = root.getElementsByTagName("header");
			if (nodes != null && nodes.getLength() > 0)
			{
				Node header = nodes.item(0);
				nodes = ((Element)header).getElementsByTagName("prop");
				for (int i=0; i<nodes.getLength(); i++)
				{
					Element nd = (Element)nodes.item(i);
					if (nd.getAttribute("type").equalsIgnoreCase("Domain"))
					{
						this.domain_ = nd.getFirstChild().getNodeValue();
						//System.out.println(this.domain_);
					}
					else if (nd.getAttribute("type").equalsIgnoreCase("Source"))
					{
						String s = nd.getFirstChild().getNodeValue();
						int n1 = s.indexOf('(');
						int n2 = s.indexOf(')',n1+1);
						String sn = s.substring(n1+1,n2);
						this.from_ = Integer.parseInt(sn);	//should be negative int
						if (this.from_ > 0) {
							throw new LoadException("Source "+sn+" not negative");
						}
						this.sourceName_ = s.substring(n2+1);
						//INSERT INTO T_Sources(F_SourceID,F_Name) VALUES(this.from_,sourceName_);
					}
				}
			}
			else
				this.domain_ = "00";	//00=general
			//open database connection
			openDatabase();
			//save t_sources
			if (this.from_ < 0)
				saveSource();
			//create indexer
			createIndexer();
			NodeList tus = root.getElementsByTagName("tu");
			//System.out.println(tus.getLength());
			for (int i=0; i<tus.getLength(); i++)
			{
				Node tuv = tus.item(i);
				if (tuv instanceof Element)
				{
					String en, zh;
					en = zh = null;
					NodeList tuvs = ((Element)tuv).getElementsByTagName("tuv");
					for (int k=0; k<tuvs.getLength(); k++)
					{
						Node t = tuvs.item(k);
						if (t instanceof Element)
						{
							Element te = (Element)t;
							//System.out.println(te.getAttribute("xml:lang"));
							if (te.getAttribute("xml:lang").equals("EN"))
							{
								NodeList segs = te.getElementsByTagName("seg");
								Node node = segs.item(0);
								//System.out.println(node.getNodeName());
								//System.out.println(node.getFirstChild().getNodeValue());
								en = node.getFirstChild().getNodeValue();
							}
							else if (te.getAttribute("xml:lang").equals("ZH"))
							{
								NodeList segs = te.getElementsByTagName("seg");
								Node node = segs.item(0);
								if (node != null)
								{
									Node sc = node.getFirstChild();
									if (sc != null)
									{
										zh = sc.getNodeValue();
									}
								}
							}
						}
					}
					if (en != null && zh != null && zh.length()>0 && en.length()<2000 && zh.length()<2000)
					{
						//System.out.println(en+" ==> "+zh);
						long sid = saveUnit(en,zh);
						index(this.rs1_, english_, sid, en);
						//index(this.rs2_, chinese_, sid, zh);
					}
				}
			}
			System.out.println("Commit");
			this.conn_.commit();
		}
		catch (SQLException sqle)
		{
			throw new LoadException("SQLException occurred: "+sqle.getMessage());
		}
		catch (IOException ioe)
		{
			throw new LoadException("IOException occurred: "+ioe.getMessage());
		}
		catch (ParserConfigurationException pce)
		{
			throw new LoadException("ParserConfigurationException occurred: "+pce.getMessage());
		}
		catch (SAXException se)
		{
			throw new LoadException("SAXException occurred: "+se.getMessage());
		}
		finally
		{
			closeDatabase();
		}
	}
*/
	public static void main(String[] args)
	{
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException cnfe) {
			System.out.println("Can't find the PostgreSQL jdbc driver!");
			System.exit(1);
		}
		if (args.length < 1)
		{
			System.out.println("Usage:\n\tjava LoadTmx <tmx_file>");
			return;
		}
		String filename = args[0];
		try
		{
			LoadTmx t = new LoadTmx(filename);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
