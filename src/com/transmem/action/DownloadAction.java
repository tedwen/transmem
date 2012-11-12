package com.transmem.action;

import java.util.logging.Logger;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.transmem.data.db.Databases;
import com.transmem.data.db.Users;

/**
 * Action class for downloading an article.
 *
 * @version 0.1
 * @author Ted Wen
 * @date Jan. 2007
 */
public class DownloadAction extends BaseAction
{
	private static Logger log_ = Logger.getLogger(DownloadAction.class.getName());

	public DownloadAction()
	{
		super();
	}

	/**
	 * Download the translated sentences as a text stream.
	 *
	 * <p class="doc">
	 	<ul>
	 		<li>Request parameters
	 			<ul>
	 				<li>user - user object created after login or register</li>
	 				<li>article - article id string for the article to download</li>
	 			</ul>
	 		</li>
	 	</ul>
	 * </p>
	 */
	public void execute(ServletParams param) throws ServletException, IOException
	{
		log_.entering("TranslateAction","execute");

		Session session = param.getSession();

		Users usr = session.getUser();
		if (usr == null)
		{
			param.sendError(MessageCode.ERR_NOT_LOGIN);
			return;
		}
		int aid = 0;
		String aids = param.getParameter("article");
		if (aids == null || aids.equals(""))
		{
			param.sendError(MessageCode.ERR_NULL_PARAM);
			return;
		}

		Connection conn = null;
		//PrintWriter pw = param.getResponse().getWriter();
		param.setContentType("text/plain;charset=utf-8");
		param.setHeader("Cache-Control", "no-cache");
		param.addHeader("Content-Disposition","attachment; filename="+aids+".txt");
		ServletOutputStream os = param.getOutputStream();
		try
		{
			aid = Integer.parseInt(aids);
			conn = getConnection(param,Databases.CATEGORY_USER);
			//load paragraphs first
			String sql = "SELECT F_ParagraphID FROM T_Paragraphs WHERE F_Article=? ORDER BY F_ParagraphID";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, aid);
			ResultSet rs = ps.executeQuery();
			ArrayList<Long> paras = new ArrayList<Long>();
			while (rs.next())
			{
				paras.add(new Long(rs.getLong(1)));
			}
			rs.close();
			ps.close();
			//load all paragraphs
			byte[] linefeed = "\n\n".getBytes();
			sql = "SELECT F_Translation FROM T_Sentences WHERE F_Article=? AND F_Paragraph=? ORDER BY F_Sequence";
			for (Long p : paras)
			{
				ps = conn.prepareStatement(sql);
				ps.setInt(1, aid);
				ps.setLong(2, p.longValue());
				rs = ps.executeQuery();
				while (rs.next())
				{
					String s = rs.getString(1);
					log_.info(s);
					byte[] bs = s.getBytes("GBK");	//TODO: GB code only!
					if (s != null)
						os.write(bs);
						//pw.print(s);
				}
				rs.close();
				ps.close();
				os.write(linefeed);
			} //for
		}
		catch (Exception e)
		{
			log_.severe(e.getMessage());
			//pw.print("Error sending translations");
		}
		finally
		{
			if (conn != null)
			{
				try {
					conn.close();
				} catch (Exception x) {}
			}
			//pw.close();
			os.close();
		}
	}
}
