package com.transmem.action;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.OutputStream;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Blob;
import java.util.ArrayList;

import com.transmem.data.db.Databases;
import com.transmem.data.db.Projects;
import com.transmem.data.db.Articles;
import com.transmem.data.db.Users;
import com.transmem.data.db.Roles;
import com.transmem.doc.ITextSaver;
import com.transmem.doc.IFileParser;
import com.transmem.doc.FileParserFactory;

/**
 * Action class for uploading an article.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan. 2007
 */
public class AddArticleAction extends BaseAction
{
	private Logger log_ = Logger.getLogger(AddArticleAction.class.getName());

	public AddArticleAction()
	{
		super();
	}

	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("AddArticleAction","execute");

		Session session = param.getSession();

		String filename = param.getFilePathName("articleFile");	//filename cannot get from param.getParameter
		//log_.info("articleFile is "+filename);

		String fileFormat = getFileFormat(filename);
		if (fileFormat==null || fileFormat.equals("")) {
			log_.severe("fileFormat is ''");
			param.sendError(MessageCode.ERR_UNSUPPORTED_FILE_FORMAT);
			return;
		}

		Users usr = session.getUser();
		if (usr == null)
		{
			param.sendError(MessageCode.ERR_NOT_LOGIN);
			return;
		}

		Projects project = session.getSelectedProject();
		if (project == null)
		{
			log_.severe("session.getAttribute('projectid') returned null");
			param.sendError(MessageCode.ERR_NO_PROJECT);
			return;
		}
		int projectid = project.getProjectID();

		Connection conn = null;
		try
		{
			conn = getConnection(param,Databases.CATEGORY_USER,true);
			if (conn == null) {
				log_.severe("getConnection(USER,true) return null");
			}
			conn.setAutoCommit(false);
		}
		catch (SQLException x)
		{
			log_.severe("SQLException when getConnection(USER)"+x.toString());
			param.sendError(MessageCode.ERR_DB_CONNECT);
			return;
		}
		try
		{
			Roles roles = new Roles(conn, usr.getRole());
			int numarts = project.getArticles();
			if (numarts >= roles.getLevel())
			{
				param.sendError("Too many articles for this project");
				return;
			}
			log_.info("Preparing to save article");
			//TODO: It may be better to separate add article item and upload article
			int articleid = (int)getSequenceLong(conn, "S_Articles");
			//log_.info("getSequenceLong(S_Articles) return "+articleid);

			String title = param.getParameter("title");
			String langPair = param.getParameter("langPair");
			log_.info("title and langPair are "+title+","+langPair);

			Articles article = new Articles(conn);
			article.setArticleID(articleid);
			article.setProject(projectid);
			article.setTitle(title);
			article.setLangPair(langPair);
			article.setTranslator(usr.getUserID());
			article.setFileFormat(fileFormat);
			//save the whole file as a Blob object in Articles table
			//TODO: or store the filename into the table, it will be used for output generation
			//saveArticleBlob(uploadedFilename, article);
			article.insert();
			log_.info("article inserted, now parse and save sentences");
			//parse uploaded text and save paras and sentens in tables
			parseAndSaveArticle(filename, articleid, conn);
			//update article's number of sentences
			article.updateSentences(article.getArticleID());
			//update number of articles in project
			project.clearUpdates();
			project.setArticles(++numarts);
			project.update(conn);
			//commit updates
			conn.commit();
			//reload articles for the project
			ArrayList<Articles> articles = article.queryByProject(projectid);
			//and replace session attribute 'articles'
			session.setArticleList(articles);
			if (numarts >= roles.getLevel())
			{
				session.setEnoughArticles();
			}
			else
			{
				session.removeEnoughArticles();
			}
			setNextPage(PageLinks.ARTICLE_PAGE);
			log_.info("inserted and reload "+articles.size()+" articles");
		}
		catch (SQLException x)
		{
			//any error occurs, rollback all operations, let user retry submission
			try
			{
				conn.rollback();
			} catch (SQLException e)
			{
				log_.severe("rollback failed in AddArticleAction.execute");
			}
			log_.severe("AddArticleAction.execute(): SQLException when insert article:"+x);
			param.sendError(MessageCode.ERR_INSERT_ARTICLE);
		}
		finally
		{
			if (conn != null)
				try { conn.close();	} catch (SQLException e) {}
		}
	}

	/**
	 * Parse the text in the uploaded file and save the result into paragraphs and sentences tables.
	 * @param filename - file path and name of the uploaded file
	 */
	protected void parseAndSaveArticle(String filename, int articleid, Connection conn) throws IOException,
		SQLException
	{
		IFileParser parser = FileParserFactory.createFileParser(filename);
		//log_.info("FileParserFactory.createFileParser("+filename+") return "+parser);
		ITextSaver saver = new ArticleSaver(conn, articleid);
		//log_.info("Saver ready, will parse the text");
		parser.parse(filename, saver);
		log_.info("Parse success");
	}

	/**
	 * Save the uploaded file into the Articles database table as a BLOB field.
	 * @param filename - file path and name of the uploaded file
	 * @param article - Articles object to store the blob
	 */
	protected void saveArticleBlob(String filename, Articles article) throws IOException,SQLException
	{
/*
		Blob blob = new Blob();
		OutputStream  os = null;
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(filename);
			os = blob.setBinaryStream(1);
			byte[] buf = new byte[512];
			while (true)
			{
				int n = fis.read(buf);
				if (n <= 0) break;
				os.write(buf, 0, n);
			}
			article.setFile(blob);
		}
		catch (SQLException se)
		{
			log_.warning("SQLException while setBinaryStream and write to it.");
			throw new SQLException(se.getMessage());
		}
		catch (IOException ioe)
		{
			log_.warning("File "+filename+" cannot be opened");
			throw new IOException(ioe.getMessage());
		}
		finally
		{
			if (fis != null)
			{
				try	{
					fis.close();
				} catch (IOException e)
				{
				}
			}
			if (os != null)
			{
				try	{
					os.close();
				} catch (IOException e)
				{
				}
			}
		}
*/
	}

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
	}
}
