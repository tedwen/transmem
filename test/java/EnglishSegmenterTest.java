import org.junit.*;
import static org.junit.Assert.*;

import com.transmem.nlp.EnglishSegmenter;

public class EnglishSegmenterTest
{
	private class Sentence
	{
		public String sentence;
		public String[] tokens;
		public Sentence(String s, String[] t)
		{
			sentence = s;
			tokens = t;
		}
	}

	@Test public void testSegment()
	{
		EnglishSegmenter es = new EnglishSegmenter();
		Sentence[] sents = new Sentence[]{
			new Sentence("I'm a programmer.",new String[]{"I","'","m","a","programmer","."}),
			new Sentence(" A space before.",new String[]{"A","space","before","."}),
			new Sentence(" Spaces at ends. ",new String[]{"Spaces","at","ends","."}),
			new Sentence("Whitespaces\there\r\n",new String[]{"Whitespaces","here"}),
			new Sentence("#!random^marks%^&and(",new String[]{"#","!","random","^","marks","%","^","&","and","("}),
			new Sentence("hyphen-ated words",new String[]{"hyphen","-","ated","words"}),
			new Sentence("中文代码 in it。",new String[]{"中文代码","in","it","。"})
		};
		for (Sentence s: sents)
		{
			String[] tokens = es.segment(s.sentence);
			assertEquals("Token count should be equal for '"+s.sentence+"'",s.tokens.length,tokens.length);
			for (int i=0; i<tokens.length; i++)
			{
				assertEquals("Token "+i+" in '"+s.sentence+"'",s.tokens[i],tokens[i]);
			}
		}
	}
}
