package com.transmem.nlp;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.th.ThaiAnalyzer;

import java.io.Reader;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Implements the ISegmenter interface to break a Thai language sentence into separate words.
 * This program relies on the lucene analyzer/tokenizer and snowball stemmer to break the sentense.
 */
public class ThaiSegmenter implements ISegmenter
{
	public static final Logger log_ = Logger.getLogger(ThaiSegmenter.class.getName());

	public ThaiSegmenter()
	{
	}

	public String[] segment(String sent) throws LanguageException
	{
		String[] tokens = null;
		Reader reader = new StringReader(sent);
		try
		{
			Analyzer analyzer = new ThaiAnalyzer();
			TokenStream ts = analyzer.tokenStream("", reader);
			Token t;
			ArrayList<String> tlist = new ArrayList<String>();
			while ((t = ts.next())!=null)
			{
				//System.out.println(t);
				tlist.add( t.termText() );
			}
			tokens = tlist.toArray(new String[tlist.size()]);
		}
		catch (Exception e)
		{
			log_.severe(e.toString());
			throw new LanguageException(e.toString());
		}
		return tokens;
	}
}
