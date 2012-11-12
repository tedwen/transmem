package com.transmem.nlp;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Logger;

public class GenericFilter implements IFilter
{
	private Logger log_ = Logger.getLogger(GenericFilter.class.getName());
	private Hashtable<String,String> stoplist_ = new Hashtable<String,String>();
	private	int minchars_ = 2;
	private	String punctmarks_ = "";

	protected void loadStoplist(String filespec) throws IOException
	{
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new FileReader(filespec));
			String line = null;
			while ((line = br.readLine())!=null)
			{
				stoplist_.put(line,line);
			}
		}
		catch (IOException ioe)
		{
			log_.severe("GenericFilter.loadStoplist('"+filespec+"') IOException:"+ioe);
			throw new IOException(ioe.getMessage());
		}
		finally
		{
			if (br != null)
				try { br.close(); } catch (IOException e) {}
		}
	}
	
	protected int getMinChars()
	{
		return this.minchars_;
	}
	protected void setMinChars(int minchars)
	{
		this.minchars_ = minchars;
	}
	protected String getPuctuationMarks()
	{
		return this.punctmarks_;
	}
	protected void setPunctuationMarks(String marks)
	{
		this.punctmarks_ = marks;
	}

	public boolean isStopWord(String word)
	{
		return stoplist_.containsKey(word);
	}

	public String[] filter(String[] words)
	{
		ArrayList<String> ws = new ArrayList<String>(words.length);
		for (int i=0; i<words.length; i++)
		{
			if (words[i].length() < this.minchars_)
				continue;
			if (this.punctmarks_.indexOf(words[i])>=0)
				continue;
			if (Character.isDigit(words[i].charAt(0)))
				continue;
			if (!stoplist_.containsKey(words[i]))
			{
				ws.add(words[i]);
			}
		}
		return ws.toArray(new String[ws.size()]);
	}
}
