package com.transmem.doc;

import java.util.logging.Logger;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Text parser for plain text-formatted text block that can contain a number of
 * paragraphs. For a given block of plain text, the parser will break into sentences
 * with a number of sentences separated by double line feeds to form a paragraph.
 *
 * @author Ted Wen
 * @date May, 2007
 */
public class TextParser extends FileParser
{
	public static final int BUFSIZE = 512;
	public static final int MAX_SENTS_PER_PARA = 50;

	private Logger log_ = Logger.getLogger(TextParser.class.getName());

	public TextParser()
	{
	}

	/**
	 * Parse the text in the specified file and extract paragraphs and sentences
	 * and call the ITextSaver object to save them elsewhere, ie. database tables.
	 * This routine is designed for Western language and Chinese, not sure about other languages.
	 * 
	 * @param filename - file path and name of the text
	 * @param saver - object of a class that implements the ITextSaver interface
	 */
	public void parse(String filename, ITextSaver saver) throws IOException,SQLException
	{
		if (filename == null || filename.equals(""))
		{
			log_.severe("parse(filename, ITextSaver): filename is null");
			throw new IOException("TextParser.parse(null,saver)");
		}
		if (saver == null)
		{
			log_.severe("TextParser.parse(String,ITextSaver),ITextSaver object is null");
			throw new IOException("ITextSaver is null");
		}

		int sents = 0;
		int fp = 0, bp = 0;

		BufferedInputStream bis = null;
		try
		{
			saver.startParagraph(0);

			FileInputStream fis = new FileInputStream(filename);
			bis = new BufferedInputStream(fis);

			StringBuffer sb = new StringBuffer();
			StringBuffer bsb = new StringBuffer();
			while (true)
			{
				int cn = bis.read();
				fp ++;		//current file pointer
				if (cn < 0)
				{
					if (sb.length() > 0)
					{
						String s = sb.toString().trim();
						if (s.length() > 0)
							saver.saveSentence(s, bp, fp);
					}
					break;
				}
				char c = (char)cn;
				boolean stop = false;
				if (c == '\r')
				{
					c = (char)bis.read();
					fp ++;
				}
				if (c != '\n')
				{
					sb.append(c);
					if (c == '.')
					{
						int n = sb.length();
						//check proceeding digit and sb is less than 5 chars
						if (!(Character.isDigit(sb.charAt(n-1)) && n < 5))
						{
							//check backwards for Mr. Mrs. Dr. etc
							int n1 = n-1;
							while (n1 > 0 && sb.charAt(n1)!=' ') n1 --;
							String s = (n - n1 > 2) ? sb.substring(n1+1,n) : "";
							if (!(s.equalsIgnoreCase("Mr.") || s.equalsIgnoreCase("Mrs.") || 
								s.equalsIgnoreCase("Dr.") ))
							{
								//check following uppercase letter or CJK
								bsb = new StringBuffer();
								while (true)
								{
									cn = bis.read();
									if (cn < 0) break;
									fp ++;
									c = (char)cn;
									if (c == '\r')
									{
										c = (char)bis.read();
										fp ++;
										if (c == '\n')
										{
											cn = bis.read();
											if (cn < 0) break;
											fp ++;
											c = (char)cn;
											if (c == '\r')
											{
												c = (char)bis.read();
												fp ++;
												if (c == '\n')
												{
													//double linefeed, end of paragraph
													if (sb.length() > 0)
													{
														String s0 = sb.toString().trim();
														if (s0.length() > 2)
														{
															saver.saveSentence(s0, bp, fp);
															bp = fp;
															sents ++;
															sb = new StringBuffer(bsb.toString());
														}
													}
													if (sents > 0) 
													{
														saver.endParagraph(fp);
														saver.startParagraph(fp);
														sents = 0;
													}
													continue;
												}
											}
										}
									}
									if (!Character.isWhitespace(c))
									{
										int ct = Character.getType(c);
										if (ct == 1 || ct == 5)	//uppercase or CJK
										{
											bsb.append(c);
											stop = true;
										}
										else if ("\"')]}>".indexOf(c) >= 0)
											sb.append(c);	//." .) etc
										else
											bsb.append(c);
										break;
									}
									else
										bsb.append(' ');	//hard space replaced
								}
							}
						}
					}
					else if (c == '¡£' || c == '£®')	//stop mark
					{
						stop = true;
					}

					if (stop)
					{
						int sbn = sb.length();
						String s = sb.toString();
						if (s.length() > 2)
						{
							saver.saveSentence(s, bp, fp);
							bp = fp;
							sents ++;
							if (sents > MAX_SENTS_PER_PARA)
							{
								saver.endParagraph(fp);
								saver.startParagraph(fp+1);
								sents = 0;
							}
							sb = new StringBuffer(bsb.toString());
						}
						else
							sb.append(bsb);
					}
					else if (bsb.length() > 0)
					{
						sb.append(bsb);
					}
					bsb = new StringBuffer();
				}
				else if (c == '\n')
				{
					sb.append(' ');
					//check next line feed, if yes then end of paragraph
					cn = bis.read();
					if (cn > 0) fp ++;
					if ((char)cn == '\r') 
					{
						cn = bis.read();
						if (cn > 0) fp ++;
					}
					if (cn == -1 || (char)cn == '\n')
					{
						if (sb.length() > 0)
						{
							String s = sb.toString().trim();
							if (s.length() > 2)
							{
								saver.saveSentence(s, bp, fp);
								bp = fp;
								sents ++;
								sb = new StringBuffer(bsb.toString());
							}
						}
						if (sents > 0)
						{
							saver.endParagraph(fp);
							saver.startParagraph(fp+1);
							sents = 0;
						}
					}
					else
						sb.append((char)cn);
				}
			}
			saver.endParagraph(fp);
		}
		catch (IOException ioe)
		{
			log_.severe("IOException while parsing("+filename+"): "+ioe.getMessage());
			throw new IOException("IOException parsing "+filename+","+ioe.getMessage());
		}
		catch (SQLException se)
		{
			log_.severe("SQLException saving paragraph and sentences: "+se.getMessage());
			throw new SQLException("SQLException at ITextSaver call: "+se.getMessage());
		}
		finally
		{
			if (bis != null)
			{
				try	
				{
					bis.close();
				} 
				catch (IOException e) {}
			}
		}
	}

/*
	public void parse(String filename, ITextSaver saver) throws IOException,SQLException
	{
		if (filename == null || filename.equals(""))
		{
			log_.severe("parse(filename, ITextSaver): filename is null");
			throw new IOException("TextParser.parse(null,saver)");
		}
		if (saver == null)
		{
			log_.severe("TextParser.parse(String,ITextSaver),ITextSaver object is null");
			throw new IOException("ITextSaver is null");
		}
		int pos, sents;
		pos = sents = 0;

		FileReader fr = null;
		try
		{
			saver.startParagraph(0);
			fr = new FileReader(filename);
			char[] buf = new char[BUFSIZE];
			StringBuffer sb = new StringBuffer();
			while (true)
			{
				int n = fr.read(buf, 0, buf.length);
				if (n <= 0)
				{
					if (sb.length() > 0)
					{
						saver.saveSentence(sb.toString(),pos,pos+sb.length());
						pos += sb.length();
					}
					break;
				}
				for (int i=0; i<n; i++)
				{
					if (Character.getType(buf[i])!=15)	//15 is Control char
					{
						sb.append(buf[i]);
					}
					if (isFullStop(sb,buf,i))
					{
						if (sb.length() > 0)
						{
							saver.saveSentence(sb.toString(),pos,i+1);
							pos = i + 1;
							sents ++;
							if (sents > MAX_SENTS_PER_PARA)
							{
								saver.endParagraph(i);
								saver.startParagraph(i+1);
								sents = 0;
							}
							sb = new StringBuffer();
						}
					}
					else if (buf[i] == '\n')
					{
						if (i < n && buf[i+1] == '\n')
						{
							saver.endParagraph(i);
							saver.startParagraph(i+2);
							sents = 0;
						}
					}
				}
				pos += n;
			}
			saver.endParagraph(pos);
		}
		catch (IOException ioe)
		{
			log_.severe("IOException while parsing("+filename+"): "+ioe.getMessage());
			throw new IOException("IOException parsing "+filename+","+ioe.getMessage());
		}
		catch (SQLException se)
		{
			log_.severe("SQLException saving paragraph and sentences: "+se.getMessage());
			throw new SQLException("SQLException at ITextSaver call: "+se.getMessage());
		}
		finally
		{
			if (fr != null)
			{
				try	{
					fr.close();
				} catch (IOException e) {}
			}
		}
	}
*/
	/**
	 * Parse the given text block into paragraphs which contain a number of sentences.
	 * The paragraphs are of Paragraph instances and sentences are of Sentence instances.
	 * NOTE: this may cause out of memory exception if text is too large when available memory is low.
	 *
	 * @param text - original text block
	 * @return array list of Paragraph objects that contain array list of sentences.
	 */
/*	public ArrayList<Paragraph> parse(String text) throws LanguageException
	{
		if (text == null)
		{
			log_.severe("TextParser.parse(null)");
			throw new LanguageException("TextParser.parse(null)");
		}
		int n1, n2;
		n1 = n2 = 0;
		ArrayList<Paragraph> paras = new ArrayList<Paragraph>();
		Paragraph para = new Paragraph(n1, n2);
		paras.add(para);
		char bc = ' ';
		for (int i=0; i<text.length(); i++)
		{
			char c = text.charAt(i);
			//TODO: only English/Western and Chinese stop punctuation mark supported
			if (isFullStop(text, c, i))
			{
				n2 = i + 1;
				if (n2 > n1)
				{
					String s = text.substring(n1, n2);
					Sentence st = new Sentence(s, n1, n2);
					para.addSentence(st);
					n1 = n2;
				}
			}
			else if (c == '\n')
			{
				if (bc == '\n')
				{
					if (i > n1)
					{
						n2 = i;
						n1 = i + 1;
						para.setEndPos(n2);
						para = new Paragraph(n1,n2);
						paras.add(para);
					}
				}
				bc = c;
			}
		}
		return paras;
	}*/
}
