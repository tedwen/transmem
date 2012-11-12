package com.transmem.nlp;

import java.util.Hashtable;
import java.util.logging.Logger;

/**
 * Wrap language tools in a object factory. The tools are used to segment a sentence,
 * filter words by stoplist, stemming words for indexing, chunk-parsing, and tagging.
 * This class offers static methods to be shared by all instances in the application scope.
 */
public class LanguageManager
{
	public static final Logger log_ = Logger.getLogger(LanguageManager.class.getName());
	
	public static final int SEGMENTER = 1;
	public static final int FILTER = 2;
	public static final int STEMMER = 4;
	public static final int TAGGER = 8;
	public static final int PARSER = 16;

	public static final int ALIGNER = 32;	//TODO: aligning involves 2 languages, consider later

	public static final int INDEXER = SEGMENTER | FILTER | STEMMER;	///to be used for indexing a sentence
	public static final int ALL = 255;	//all tools included

	private static Hashtable<String,String> langnames_ = null;
	private static Hashtable<String,Object> tools_ = null;

	public static void setLanguageNames(Hashtable<String,String> langnames)
	{
		langnames_ = langnames;
	}

	public static String getLangNameByCode(String langCode)
	{
		return langnames_.get(langCode);
	}

	public static void addLanguageName(String code, String name)
	{
		langnames_.put(code, name);
	}

	/**
	 * Create object for a class.
	 * @param className - class name as string.
	 * @return Object that can be casted to a derived class.
	 */
	private static Object createObject(String className) throws LanguageException
	{
		if (tools_ == null)
		{
			tools_ = new Hashtable<String,Object>();
		}
		else if (tools_.containsKey(className))
		{
			return tools_.get(className);
		}

		Class classObject = null;
		Object object = null;
		try
		{
			if (className.indexOf('.')<0)
			{
				classObject = Class.forName("com.transmem.nlp."+className);
			}
			else
			{
				classObject = Class.forName(className);
			}
			object = classObject.newInstance();
			
			tools_.put(className,object);

			return object;
		}
		catch (ClassNotFoundException cfe)
		{
			log_.severe("LanguageManager.createObject('"+className+"') class not found exception");
			throw new LanguageException("Class '"+className+"' not found");
		}
		catch (Exception e)
		{
			//System.err.println(e.toString());
			//log
			log_.severe("LanguageManager.createObject('"+className+"') instantiate exception:"+e);
			throw new LanguageException("Class '"+className+"' instantiation error: "+e);
		}
	}

	/**
	 * Create ISegmenter object.
	 * @param name - language name such as Chinese.
	 */
	public static ISegmenter createSegmenter(String name) throws LanguageException
	{
		assert(name != null);
		if (name.length()==2)
		{
			if (langnames_ == null)
			{
				loadLangNames();
			}
			name = langnames_.get(name);
			if (name == null)
			{
				throw new LanguageException("createSegmenter("+name+") error: invalid name");
			}
		}
		if (!name.endsWith("Segmenter"))
		{
			return (ISegmenter)createObject(name+"Segmenter");
		}
		else
		{
			return (ISegmenter)createObject(name);
		}
	}

	/**
	 * Create IStemmer object.
	 * @param name - language name such as English
	 */
	public static IStemmer createStemmer(String name) throws LanguageException
	{
		assert(name != null);
		if (name.length()==2)
		{
			if (langnames_ == null)
			{
				loadLangNames();
			}
			name = langnames_.get(name);
			if (name == null)
			{
				throw new LanguageException("createStemmer("+name+") error: invalid name");
			}
		}
		if (!name.endsWith("Stemmer"))
		{
			return (IStemmer)createObject(name+"Stemmer");
		}
		else
		{
			return (IStemmer)createObject(name);
		}
	}

	/**
	 * Create IFilter object.
	 * @param name - English, Chinese
	 */
	public static IFilter createFilter(String name) throws LanguageException
	{
		assert(name != null);
		if (name.length()==2)
		{
			if (langnames_ == null)
			{
				loadLangNames();
			}
			name = langnames_.get(name);
			if (name == null)
			{
				throw new LanguageException("createFilter("+name+") error: invalid name");
			}
		}
		if (!name.endsWith("Filter"))
		{
			return (IFilter)createObject(name+"Filter");
		}
		else
		{
			return (IFilter)createObject(name);
		}
	}

	/**
	 * Create IAligner object.
	 * @param name - English, Chinese ?
	 */
	public static IAligner createAligner(String name) throws LanguageException
	{
		assert(name != null);
		if (name.length()==2)
		{
			if (langnames_ == null)
			{
				loadLangNames();
			}
			name = langnames_.get(name);
			if (name == null)
			{
				throw new LanguageException("createAligner("+name+") error: invalid name");
			}
		}
		if (!name.endsWith("Aligner"))
		{
			return (IAligner)createObject(name+"Aligner");
		}
		else
		{
			return (IAligner)createObject(name);
		}
	}
	
	/**
	 * Create ITagger object.
	 * @param name - English, Chinese
	 */
	public static ITagger createTagger(String name) throws LanguageException
	{
		assert(name != null);
		if (name.length()==2)
		{
			if (langnames_ == null)
			{
				loadLangNames();
			}
			name = langnames_.get(name);
			if (name == null)
			{
				throw new LanguageException("createTagger("+name+") error: invalid name");
			}
		}
		if (!name.endsWith("Tagger"))
		{
			return (ITagger)createObject(name+"Tagger");
		}
		else
		{
			return (ITagger)createObject(name);
		}
	}

	/**
	 * Create IParser object.
	 * @param name - language name such as English, Chinese.
	 */
	public static IParser createParser(String name) throws LanguageException
	{
		assert(name != null);
		if (name.length()==2)
		{
			if (langnames_ == null)
			{
				loadLangNames();
			}
			name = langnames_.get(name);
			if (name == null)
			{
				throw new LanguageException("createParser("+name+") error: invalid name");
			}
		}
		if (!name.endsWith("Parser"))
		{
			return (IParser)createObject(name+"Parser");
		}
		else
		{
			return (IParser)createObject(name);
		}
	}

	/**
	 * Create a ILinguist object for the given language and selected tools.
	 * The language can be specified by the 2-letter code such as 'EN' for English, 'ZH' for Chinese.
	 * The selected tools are specified as an int with wanted tools combined, for example,
	 * STEMMER | FILTER means stemmer and filter tools are wanted in the linguist object.
	 * @param langcode - 2-letter language code
	 * @param tools - int for tools
	 * @return ILinguist object
	 * @throws LanguageException for invalid langcode or failure of creating the tool object.
	 */
	public static ILinguist createLinguist(String langcode, int tools) throws LanguageException
	{
		if (langnames_ == null)
		{
			loadLangNames();
		}
		if (langnames_ == null)
		{
			throw new LanguageException("LanguageManager.createLinguist('"+langcode+"'), langnames not loaded from DB");
		}
		String langName = langnames_.get(langcode);
		if (langName == null)
		{
			throw new LanguageException("LanguageManger.createLinguist('"+langcode+"') exception: langcode not found");
		}
		Linguist linguist = new Linguist(langcode, langName);
		//System.out.println("langName="+langName);
		if ((tools & LanguageManager.SEGMENTER) == LanguageManager.SEGMENTER)
		{
			linguist.setSegmenter(createSegmenter(langName));
		}
		if ((tools & LanguageManager.FILTER) == LanguageManager.FILTER)
		{
			linguist.setFilter(createFilter(langName));
		}
		if ((tools & LanguageManager.STEMMER) == LanguageManager.STEMMER)
		{
			linguist.setStemmer(createStemmer(langName));
		}
		//if ((tools & LanguageManager.ALIGNER) == LanguageManager.ALIGNER)
		//{
		//	linguist.setAligner(createAligner(langName));
		//}
		if ((tools & LanguageManager.TAGGER) == LanguageManager.TAGGER)
		{
			linguist.setTagger(createTagger(langName));
		}
		if ((tools & LanguageManager.PARSER) == LanguageManager.PARSER)
		{
			linguist.setParser(createParser(langName));
		}

		return linguist;
	}

	/**
	 * Load language names into the static hashtable with the 2-letter language codes as the key.
	 * The language names should be capitalised (first-letter uppercase) single word (no spaces or symbols).
	 */
	public static void loadLangNames()
	{
		//load from DB, or simply hard-code it here for the moment
		langnames_ = new Hashtable<String,String>();
		langnames_.put("EN","English");
		langnames_.put("ZH","Chinese");
		langnames_.put("AR","Arabic");
		langnames_.put("DE","German");
		langnames_.put("EL","Greek");
		langnames_.put("ES","Spanish");
		langnames_.put("FR","French");
		langnames_.put("IT","Italian");
		langnames_.put("JA","Japanese");
		langnames_.put("KO","Korean");
		langnames_.put("RU","Russian");
		//log_.info("langnames_ hashtable created with "+langnames_.size()+" instances added.");
	}
}
