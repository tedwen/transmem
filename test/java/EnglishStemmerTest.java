import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import com.transmem.nlp.EnglishStemmer;

public class EnglishStemmerTest
{
	@Test public void testApplies()
	{
		EnglishStemmer es = new EnglishStemmer();
		assertTrue("EnglishStemmer.applies() should return true",es.applies());
	}

	@Test public void testStem()
	{
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<String> stems = new ArrayList<String>();
		loadWords(words, stems);
		EnglishStemmer es = new EnglishStemmer();
		for (int i=0; i<words.size(); i++)
		{
			String stem = es.stem(words.get(i));
			assertEquals("word: "+i, stems.get(i), stem);
		}
	}

	private void loadWords(ArrayList<String> words, ArrayList<String> stems)
	{
		String path = "./data/words.txt";
		File f = new File(path);
		if (f.exists()==false)
		{
			path = "./test/data/words.txt";
			f = new File(path);
			if (f.exists()==false)
			{
				path = "../data/words.txt";
				f = new File(path);
				if (f.exists()==false)
				{
					System.err.println("Error: data/words.txt not found!");
					return;
				}
			}
		}
		String path2 = path.substring(0,path.lastIndexOf('/'))+"/stems.txt";
		String line = null;
		BufferedReader br1 = null;
		BufferedReader br2 = null;
		try
		{
			br1 = new BufferedReader(new FileReader(f));
			while ((line = br1.readLine())!=null)
			{
				words.add(line.trim());
			}
			br2 = new BufferedReader(new FileReader(path2));
			while ((line = br2.readLine())!=null)
			{
				stems.add(line.trim());
			}

		}
		catch (IOException e)
		{
			System.err.println(e.toString());
		}
		finally
		{
			try {
				if (br1 != null) br1.close();
				if (br2 != null) br2.close();
			} catch (IOException x) {}
		}
	}
}
