package com.transmem.nlp;

public class Linguist implements ILinguist
{
	private String langCode_;
	private String langName_;

	private	ISegmenter segmenter_;
	private	IFilter filter_;
	private IStemmer stemmer_;
	private ITagger tagger_;
	private IParser parser_;

	private IAligner aligner_;
	//private ISpellChecker spellchecker_;

	public Linguist()
	{
	}

	public Linguist(String code, String name)
	{
		this.langCode_ = code;
		this.langName_ = name;
	}

	public String getLangCode()
	{
		return this.langCode_;
	}
	public void setLangCode(String code)
	{
		this.langCode_ = code;
	}
	public String getLangName()
	{
		return this.langName_;
	}
	public void setLangName(String name)
	{
		this.langName_ = name;
	}

	public ISegmenter getSegmenter()
	{
		return this.segmenter_;
	}
	public void setSegmenter(ISegmenter segmenter)
	{
		this.segmenter_ = segmenter;
	}
	public IFilter getFilter()
	{
		return this.filter_;
	}
	public void setFilter(IFilter filter)
	{
		this.filter_ = filter;
	}
	public IStemmer getStemmer()
	{
		return this.stemmer_;
	}
	public void setStemmer(IStemmer stemmer)
	{
		this.stemmer_ = stemmer;
	}
	public ITagger getTagger()
	{
		return this.tagger_;
	}
	public void setTagger(ITagger tagger)
	{
		this.tagger_ = tagger;
	}
	public IParser getParser()
	{
		return this.parser_;
	}
	public void setParser(IParser parser)
	{
		this.parser_ = parser;
	}

	public String[] segment(String sent) throws LanguageException
	{
		if (this.segmenter_ == null)
		{
			throw new LanguageException(LanguageException.NullPointerException,"segmenter not assigned");
		}
		return this.segmenter_.segment(sent);
	}

	public String[] filter(String[] words) throws LanguageException
	{
		if (this.filter_ == null)
		{
			throw new LanguageException(LanguageException.NullPointerException,"filter not assigned");
		}
		return this.filter_.filter(words);
	}

	public String[] stem(String[] words) throws LanguageException
	{
		if (this.stemmer_ == null)
		{
			throw new LanguageException(LanguageException.NullPointerException,"stemmer not assigned");
		}
		String[] s = new String[words.length];
		for (int i=0; i<words.length; i++)
		{
			s[i] = this.stemmer_.stem(words[i]);
		}
		return s;
	}

	/**
	 * indexkeys returns a list of words as indexes in a given sentence.
	 * @param sent - sentence as a String
	 * @return array of words(stems) suitable for indexing as keys
	 */
	public String[] indexkeys(String sent) throws LanguageException
	{
		String[] words = segment(sent);
		String[] words2 = filter(words);
		String[] stems = stem(words2);
		return stems;
	}
}
