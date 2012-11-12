import junit.framework.*;

import com.transmem.data.tm.*;
import com.transmem.nlp.LanguageException;

import java.util.ArrayList;
import java.sql.*;

public class CorpusTest extends TestCase
{
/*
	private Connection con_ = null;

	public void setUp()
	{
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException cnfe) {
			System.out.println("Couldn't find the driver!");
			cnfe.printStackTrace();
		}

		this.con_ = null;
		try {
			this.con_ = DriverManager.getConnection("jdbc:postgresql://localhost/transmem","postgres", "postgres");
		} catch (SQLException se) {
			System.out.println("Couldn't connect: print out a stack trace and exit.");
			se.printStackTrace();
		}
	}
	
	public void tearDown()
	{
		if (this.con_ != null)
		{
			try
			{
				this.con_.close();
			}
			catch (SQLException e)
			{
			}
		}
	}
	
	public void testConstructor()
	{
		Corpus c = new Corpus(this.con_, "EN", "ZH");
		assertEquals("T_ENZH", c.getTableName());
		assertEquals("T_ENZHx", c.getIndexName());
	}

	public void testQuery()
	{
		Corpus c = new Corpus(this.con_, "EN", "ZH");
		try
		{
			int n = c.query("this is my test about corpus.");
			//System.out.println(n);
			assertTrue(n > 0);
		}
		catch (LanguageException le)
		{
		}
	}

	public void testGetPage()
	{
		Corpus c = new Corpus(this.con_, "EN", "ZH");
		try
		{
			int n = c.query("this is my test about corpus.");
			//System.out.println(n);
			ArrayList<Example> sents = c.getPage(0);
			for (Example e : sents)
			{
				System.out.println(e.getSource());
			}
		}
		catch (LanguageException le)
		{
		}
	}
*/
}
