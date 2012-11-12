import org.junit.*;
import static org.junit.Assert.*;

import com.transmem.doc.TextParser;
import com.transmem.doc.ITextSaver;

import java.sql.SQLException;

class TextSaver implements ITextSaver
{
	public void startParagraph(int startpos) throws java.sql.SQLException
	{
		System.out.println("<p start="+startpos+">");
	}

	public void endParagraph(int endpos) throws java.sql.SQLException
	{
		System.out.println("</p>");
	}

	public void saveSentence(String sentence, int startpos, int endpos) throws java.sql.SQLException
	{
		System.out.println("<s start="+startpos+" end="+endpos+">"+sentence+"</s>");
	}
}

public class TextParserTest
{
	private TextSaver saver = null;

	@Before public void setUp()
	{
		saver = new TextSaver();
	}

	@Test public void testParse()
	{
		String filename="data/textparse.txt";
		TextParser tp = new TextParser();
		try
		{
			tp.parse(filename, saver);
		}
		catch (Exception e)
		{
			System.err.println(e);
		}
	}
}
