package bbva.swift.parser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

public class SwiftMT7XXParserTest {

	@Test
	public void testParseBlocksOneLevel() {
		
		// Test a 1 level brace structure
		String message = "{1:test1}{2:test2}{3:test3}{4:test4}{5:test5}";
		HashMap<Integer, ArrayList<String>> blocks = SwiftMT7XXParser.parseBlocks(message);			
		assertEquals(5, blocks.size());		
		
	}
	
	@Test
	public void testParseBlocksTwoLevels() {
		
		// Test a 2 level brace structure
		String message = "{1:test1}{2:test2}{3:test3}{4:{345:test345}{177:test177}}{5:test5}";		
		HashMap<Integer, ArrayList<String>> blocks = SwiftMT7XXParser.parseBlocks(message);			
		assertEquals(5, blocks.size());
	}
	
	
	@Test
	public void testParseFields() {
		
		// Test a 2 level brace structure
		String message = ":20:test20:32B:test32B-";		
		HashMap<String, String> fields = SwiftMT7XXParser.parseFields(message);			
		assertEquals(2, fields.size());
	}
	
	@Test
	public void testParseFieldsWithBraces() {
		
		// Test a 2 level brace structure
		String message = "{377:none}{577:none2}:20:test20:32B:test32B-";		
		HashMap<String, String> fields = SwiftMT7XXParser.parseFields(message);			
		assertEquals(2, fields.size());
	}
	
	

}
