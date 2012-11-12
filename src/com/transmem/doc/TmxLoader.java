package com.transmem.doc;

import java.io.IOException;
import java.io.File;
import java.util.logging.Logger;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;

/**
 * TMX Loader class parses a TMX file and sort out sentence pairs and call the specified
 * unit saver to save the pair. If the TMX contains more than two languages, only the
 * first two are loaded. The xml:lang attribute must exist on the tuv element.
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
public class TmxLoader extends DefaultHandler
{
	public static final Logger log_ = Logger.getLogger(TmxLoader.class.getName());

	private static final int E_NONE = 0;
	private static final int E_TUV = 1;
	private static final int E_SEG = 2;
	private static final int E_PROP = 3;
	private	static final int E_SRC = 16;
	private	static final int E_DST = 32;	//ORed on E_TUV and E_SEG to tell src or dst
	private static final int A_DOMAIN = 5;
	private static final int A_SOURCE = 6;

	private	IUnitSaver	saver_ = null;
	private String[] langs_ = new String[2];
	private StringBuffer sb_;
	private String src_, dst_;
	private int status_, ptype_, tuvcount_;
	private	boolean first_ = true;

	public TmxLoader(String filename, IUnitSaver saver) throws IOException
	{
		this.saver_ = saver;
		parseTmxFile(filename);
	}

	/**
	 * Parse an TMX file using the SAX parser in order to process really large TMX files.
	 */
	public void parseTmxFile(String filename) throws IOException
	{
		try 
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			File f = new File(filename);
			parser.parse(f, this);
		}
		catch (org.xml.sax.SAXException e)
		{
			throw new IOException("SAXException: "+e.toString());
		}
		catch (IOException ioe)
		{
			throw ioe;
		}
		catch (javax.xml.parsers.ParserConfigurationException x)
		{
			throw new IOException("ParserConfigurationException: "+x.toString());
		}
	}

	public void startDocument() throws SAXException
	{
		//System.out.println("startDocument()");
		if (this.saver_ != null)
			this.saver_.start();
	}

	public void endDocument() throws SAXException
	{
		//System.out.println("endDocument()");
		if (this.saver_ != null)
			this.saver_.end();
	}

	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException
	{
		if (qName.equals("tuv"))
		{
			this.tuvcount_ ++;
			this.status_ = E_TUV;
			String lang = atts.getValue("xml:lang");
			if (lang == null) 
			{
				throw new SAXException("TMX tuv element contains no xml:lang attribute");
			}
			if (this.first_)
			{
				if (this.tuvcount_ < 3)
				{
					String langcode = lang.substring(0,2).toUpperCase();
					switch (this.tuvcount_)
					{
						case 1:
							this.status_ |= E_SRC;
							saver_.setProperty("srclang", langcode);
							this.langs_[0] = langcode;
							break;
						case 2:
							this.status_ |= E_DST;
							saver_.setProperty("dstlang", langcode);
							this.langs_[1] = langcode;
							this.first_ = false;
							break;
					}
				}
			}
			else
			{
				if (lang.equals(this.langs_[0]))
					this.status_ |= E_SRC;
				else if (lang.equals(this.langs_[1]))
					this.status_ |= E_DST;
			}
		}
		else if (qName.equals("seg"))
		{
			this.status_ = E_SEG | this.status_ & 0xF0;	//previous status should be E_TUV
			this.sb_ = new StringBuffer();
		}
		else if (qName.equals("tu"))
		{
			this.tuvcount_ = 0;
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
		if ((this.status_ & E_SEG) == E_SEG)
		{
			if ((this.status_ & E_SRC) == E_SRC)
			{
				//source language sentence
				this.src_ = this.sb_.toString();
			}
			else if ((this.status_ & E_DST) == E_DST)
			{
				this.dst_ = this.sb_.toString();
			}
			if (src_ != null && dst_ != null && src_.length() > 0 && dst_.length() > 0)
			{
				if (this.saver_ != null)
				{
					try 
					{
						this.saver_.saveUnit(this.src_, this.dst_);
					} 
					catch (java.sql.SQLException sqle)
					{
						log_.severe("SQLException in saveUnit: "+sqle.toString());
						throw new SAXException("SQLException in saveUnit: "+sqle.toString());
					}
				}
				this.src_ = this.dst_ = null;
			}
			//System.out.println(this.sb_.toString());
			this.status_ = E_NONE;
		}
	}

	/**
	 * SAX event handler to receive characters from elements.
	 */
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		//System.out.println("characters: "+length);
		if (((this.status_ & E_SEG) == E_SEG) && ((this.status_ & 0xF0) > 0))
		{
			this.sb_.append(ch, start, length);
		}
		else if (this.status_ == E_PROP)
		{
			//System.out.println(new String(ch,start,length));
			if (this.ptype_ == A_DOMAIN)
			{
				//this.domain_ = new String(ch, start, length);
				if (this.saver_ != null)
					this.saver_.setProperty("Domain", new String(ch, start, length));
			}
			else if (this.ptype_ == A_SOURCE)
			{
				String s = new String(ch, start, length);
				int n1 = s.indexOf('(');
				int n2 = s.indexOf(')');
				String sn = s.substring(n1+1, n2);
				try 
				{
					//this.from_ = Integer.parseInt(sn);
					if (this.saver_ != null)
						this.saver_.setProperty("Source", sn);
				} 
				catch (Exception e)
				{
				}
				//this.sourceName_ = s.substring(n2+1);
			}
			this.status_ = E_NONE;
		}
	}
}
