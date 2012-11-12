package com.transmem.action;

import java.io.File;
import java.io.Writer;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

/**
 * A wrapper (Facade) for HttpServletRequest, MultipartRequest, and HttpServletResponse objects.
 * When a multipart/application form is received, a MultipartRequest will be created
 * and stored in this object, and the action classes visit the two kinds of request in the same way.
 *
 * ServletParams sp = new ServletParams(request, response);
 * String value = sp.getParameter("variable"); //for both simple and multipart request
 * File file = sp.getFile("uploaded_file"); //for multipart request
 *
 */
public class ServletParams
{
	private static Logger log_ = Logger.getLogger(ServletParams.class.getName());

	private	HttpServletRequest request_;
	private	MultipartRequest mrequest_;
	private	HttpServletResponse response_;
	private	Session session_;
	private	String path_;

	public ServletParams(HttpServletRequest request, HttpServletResponse response)
		throws IOException
	{
		if (request == null || response == null)
			throw new IllegalArgumentException("Argument null");

		this.request_ = request;
		this.session_ = new Session(request.getSession());
		this.response_ = response;
		//log_.info("Content Type: "+request.getContentType());
		String ct = request.getContentType();
		if (ct == null)
		{
			log_.warning("request.getContentType() returned null");
		}
		else if (ct.startsWith("multipart/form-data"))
		{
			//log_.info("multipart/form-data content type");
			ServletContext ctx = session_.getServletContext();
			int uploadmax = ((Integer)ctx.getAttribute("UploadSizeMB")).intValue();
			this.path_ = ctx.getRealPath("/") + "upload";
			//log_.info("path="+path);
			this.mrequest_ = new MultipartRequest(request, this.path_,
				uploadmax * 1024 * 1024, new DefaultFileRenamePolicy());
		}
	}

	public HttpServletRequest getRequest()
	{
		return this.request_;
	}
	
	public HttpServletResponse getResponse()
	{
		return this.response_;
	}

	public Writer getWriter() throws IOException
	{
		if (this.response_ == null)
		{
			log_.severe("ServletParams.response_ is null");
			return null;
		}
		return this.response_.getWriter();
	}

	public ServletOutputStream getOutputStream() throws IOException
	{
		if (this.response_ == null)
		{
			log_.severe("ServletParams.response_ is null");
			return null;
		}
		return this.response_.getOutputStream();
	}

	public void setContentType(String contentType)
	{
		if (this.response_ == null)
		{
			log_.severe("ServletParams.response_ is null");
			return;
		}
		this.response_.setContentType(contentType);
	}

	public void setHeader(String key, String value)
	{
		if (this.response_ == null)
		{
			log_.severe("ServletParams.response_ is null");
			return;
		}
		this.response_.setHeader(key, value);
	}

	public void addHeader(String key, String value)
	{
		if (this.response_ == null)
		{
			log_.severe("ServletParams.response_ is null");
			return;
		}
		this.response_.addHeader(key, value);
	}

	public MultipartRequest getMultipartRequest()
	{
		return this.mrequest_;
	}

	public String getParameter(String name)
	{
		if (this.mrequest_ != null)
			return this.mrequest_.getParameter(name);
		if (this.request_ != null)
			return this.request_.getParameter(name);
		return null;
	}

	public Enumeration getParameterNames()
	{
		if (this.mrequest_ != null)
			return this.mrequest_.getParameterNames();
		if (this.request_ != null)
			return this.request_.getParameterNames();
		return null;
	}

	public String[] getParameterValues(String arrayname)
	{
		if (this.mrequest_ != null)
			return this.mrequest_.getParameterValues(arrayname);
		if (this.request_ != null)
			return this.request_.getParameterValues(arrayname);
		return null;
	}

	public Session getSession()
	{
		return this.session_;
	}

	public HttpSession getHttpSession()
	{
		if (this.session_ == null)
			return null;
		return this.session_.getHttpSession();
	}
	
	public String getPath()
	{
		return this.path_;
	}
	
	/**
	 * Return a list of parameter names for <file> elements.
	 */
	public Enumeration getFileNames()
	{
		if (this.mrequest_ != null)
			return mrequest_.getFileNames();
		return null;
	}
	
	public File getFile(String paramname)
	{
		if (this.mrequest_ != null)
			return mrequest_.getFile(paramname);
		return null;
	}
	
	/**
	 * Return the changed filename saved in the path.
	 * @param paramname - name of the file element
	 * @return saved filename without path
	 */
	public String getFilesystemName(String paramname)
	{
		if (this.mrequest_ != null)
			return this.mrequest_.getFilesystemName(paramname);
		return null;
	}

	/**
	 * Return the saved filename with path.
	 * @param paramname - name of the file element
	 * @return saved filename with path
	 */
	public String getFilePathName(String paramname)
	{
		if (this.mrequest_ != null)
		{
			String fs = this.path_ + File.separator;
			return fs + this.mrequest_.getFilesystemName(paramname);
		}
		return null;
	}

	/**
	 * Return original filename without path.
	 * @param paramname - name of the file element
	 * @return original filename
	 */
	public String getOriginalFileName(java.lang.String paramname)
	{
		if (this.mrequest_ != null)
			return this.mrequest_.getOriginalFileName(paramname);
		return null;
	}

	/**
	 * Return the content type of the uploaded file.
	 * @param paramname - name of the file element
	 * @return content type
	 */
	public String getContentType(java.lang.String paramname)
	{
		if (this.mrequest_ != null)
			return this.mrequest_.getContentType(paramname);
		return null;
	}

	public void sendError(int errorcode) throws IOException
	{
		if (this.response_ == null)
		{
			log_.severe("response_ is null in ServletParams");
			return;
		}
		this.response_.sendError(errorcode);
	}

	public void sendError(int errorcode, String errmsg) throws IOException
	{
		if (this.response_ == null)
		{
			log_.severe("response_ is null in ServletParams");
			return;
		}
		this.response_.sendError(errorcode, errmsg);
	}
	
	public void sendError(String message) throws IOException
	{
		sendError(MessageCode.ERRCODE_APP, message);
	}
}
