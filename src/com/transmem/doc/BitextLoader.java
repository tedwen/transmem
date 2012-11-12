package com.transmem.doc;

import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.sql.SQLException;

/**
 * Bitext loader loads a bilingual parallel text from two separate files.
 * The files should be in plain text format with proper punctuation marks
 * for each sentence.  In other words, a sentence in the source language
 * text should have a corresponding sentence in the translation text.
 * The current implementation does not include automatic alignment, it is
 * the user's responsibility to guarantee that the sentences are aligned.
 * This class implements ITextSaver for TextParser to parse the text into
 * sentences.
 */
public class BitextLoader implements ITextSaver
{
	public static final Logger log_ = Logger.getLogger(BitextLoader.class.getName());

	class Record
	{
		public int index_;
		public int length_;
		public Record link_;	//link to the other language sentence
		
		public Record(int index, int length)
		{
			this.index_ = index;
			this.length_ = length;
			this.link_ = null;
		}
	}

	private	IUnitSaver	saver_ = null;
	private String sencoding_, tencoding_;
	private	File[] tempfiles_;	//temporary filenames
	private PrintWriter[] tempwriters_;
	private	ArrayList<Record>[] slens_;	//length of sentences
	private	int curfile_, index_;

	/**
	 * Construct BitextLoader with two file names.
	 */
	public BitextLoader(String sfilename, String tfilename,
		String sencoding, String tencoding, IUnitSaver saver) throws IOException,SQLException
	{
		this.saver_ = saver;
		this.sencoding_ = sencoding;
		this.tencoding_ = tencoding;

		parseFiles(sfilename, tfilename);
	}

	/**
	 * Parse the two files to get sentences.
	 * @param sfilename - source text filename
	 * @param tfilename - target text filename
	 */
	protected void parseFiles(String sfilename, String tfilename) throws IOException,SQLException
	{
		this.slens_ = new ArrayList[2];//{new ArrayList<Record>(),new ArrayList<Record>()};
		this.slens_[0] = new ArrayList<Record>();
		this.slens_[1] = new ArrayList<Record>();
		//parse two files one by one, saving sentences into temporary text files
		this.tempfiles_ = new File[2];
		this.tempfiles_[0] = File.createTempFile("tmbis",".tmp");
		log_.info("Temporary file created 4 src: "+tempfiles_[0]);
		this.tempwriters_[0] = new PrintWriter(this.tempfiles_[0]);
		this.curfile_ = 0;
		this.index_ = 0;
		TextParser tps = new TextParser();
		tps.parse(sfilename, this.sencoding_, this);
		this.tempwriters_[0].close();
		this.tempfiles_[1] = File.createTempFile("tmbid",".tmp");
		log_.info("Temporary file created 4 dst: "+tempfiles_[1]);
		this.tempwriters_[1] = new PrintWriter(this.tempfiles_[1]);
		this.curfile_ = 1;
		this.index_ = 0;
		TextParser tpd = new TextParser();
		tpd.parse(tfilename, this.tencoding_, this);
		this.tempwriters_[1].close();
		//merge and align the sentences
		boolean saved = false;
		if ((this.slens_[0].size() > 0) && (this.slens_[0].size() == this.slens_[1].size()))
		{
			mergeAndAlign();
			saved = true;
		}
		//finally delete the temp files
		this.tempfiles_[0].delete();
		this.tempfiles_[1].delete();
		log_.info("Temporary files deleted");
		File f = new File(sfilename);
		f.delete();
		f = new File(tfilename);
		f.delete();
		log_.info("Uploaded files deleted");
		if (!saved)
			throw new IOException("Sentences do not match");
	}

	/**
	 * Align the sentences from the two files and merge if necessary.
	 * A true aligner is an AI research topic, so we ignore that at the moment,
	 * and leave the un-matched sentences empty. The user is then responsible
	 * to edit the sentences and align manually.
	 */
	public void mergeAndAlign() throws SQLException,IOException
	{
		BufferedReader reader1 = new BufferedReader(new FileReader(this.tempfiles_[0]));
		BufferedReader reader2 = new BufferedReader(new FileReader(this.tempfiles_[1]));
		while (true)
		{
			String s1 = reader1.readLine();
			String s2 = reader2.readLine();
			if (s1 == null && s2 == null) break;
			this.saver_.saveUnit(s1, s2);
		}
		reader1.close();
		reader2.close();

	}
	// Interface method, not used here
	public void startParagraph(int startpos) throws java.sql.SQLException
	{
	}
	// Interface method, not used here
	public void endParagraph(int endpos) throws java.sql.SQLException
	{
	}
	/**
	 * Called by a TextParser object when a sentence is ready to save.
	 * @param sentence - String as a sentence
	 * @param startpos - ignored
	 * @param endpos - ignored
	 */
	public void saveSentence(String sentence, int startpos, int endpos) throws java.sql.SQLException
	{
		this.tempwriters_[this.curfile_].println(sentence);
		this.slens_[this.curfile_].add(new Record(this.index_, sentence.length()));
		this.index_ ++;
	}
}
