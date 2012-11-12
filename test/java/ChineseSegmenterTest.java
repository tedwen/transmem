import org.junit.*;
import static org.junit.Assert.*;

import com.transmem.nlp.ChineseSegmenter;

public class ChineseSegmenterTest
{
	@Test public void testSegment()
	{
		ChineseSegmenter cs = new ChineseSegmenter();
		assertEquals(null,cs.segment(""));
	}
}
