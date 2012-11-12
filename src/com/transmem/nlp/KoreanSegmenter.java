package com.transmem.nlp;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;

import java.io.Reader;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Implements the ISegmenter interface to break a Korean sentence into separate words.
 * It is based on the lucene Paoding segmentation program. It needs a list of dictionary files
 * which must be placed in the dic folder under the current package.
 */
public class KoreanSegmenter implements ISegmenter
{
	public static final Logger log_ = Logger.getLogger(KoreanSegmenter.class.getName());

	public String[] segment(String sent) throws LanguageException
	{
		String[] tokens = null;
		Reader reader = new StringReader(sent);
		try
		{
			//TODO: CJKAnalyzer cannot break Korean characters into meaningful words!
			Analyzer analyzer = new CJKAnalyzer();
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
