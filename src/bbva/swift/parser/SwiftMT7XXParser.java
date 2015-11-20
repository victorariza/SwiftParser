
package bbva.swift.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bbva.swift.MT7TYPE;
import bbva.swift.SwiftMT7XX;

/**
 * Class that provide the utilities needed to parse swift MT7XX message group
 * 
 * @author U509307
 *
 */
public class SwiftMT7XXParser {

	// Block pattern
	private static final String BLOCK_PATTERN = "\\{(\\d:((\\{.*?\\})*).*?)\\}";
	private static final String FIELD_PATTERN = "((\\{.*\\})|(\\d{2}(\\p{Upper}?):.*?(:|-)))";

	// Delimiter used in CSV file
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String COMMA_SEPARATOR = ";";

	private static final ArrayList<String> OUTPUT_FIELDS = new ArrayList<String>();

	/**
	 * Receive a folder path and return a map with the file name and the list of messages per file
	 * in a String format
	 * 
	 * @param folderPath
	 * @return
	 * @throws IOException
	 */
	public static HashMap<String, List<String>> readSwiftFolder(String folderPath) throws IOException {

		HashMap<String, List<String>> messageList = new HashMap<String, List<String>>();

		// Read all the files in the given folder
		File dir = new File(folderPath);

		File[] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".obk");
			}
		});

		for (int i = 0; i < files.length; i++) {			
			messageList.put(files[i].getName(), readSwiftMessage(files[i]));
		}

		System.out.println("Number of files: " + files.length);
		System.out.println("Number of messages: " + messageList.size());

		return messageList;

	}

	/**
	 * Receive a file and parse it to a String
	 * 
	 * @param filePath
	 * @return content of the file itself
	 * @throws IOException
	 */
	public static List<String> readSwiftMessage(File file) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(file));
		String swiftString = "";

		try {

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				swiftString += sCurrentLine;
			}

		} finally {
			br.close();
		}

		List<String> swiftMessages = Arrays.asList(swiftString.split("\\$"));

		return swiftMessages;
	}

	/**
	 * Receive and string and parse it to a SwiftMT7XX message
	 * 
	 * @param swiftMessage
	 * @return
	 */
	public static SwiftMT7XX parseMT7XX(String swiftMessage, String fileName) {

		// format = {:20XXX
		// Parse the message type, we want to extract the XXX that is the
		// message type
		int beginIndex = swiftMessage.indexOf("{2:") + 4;
		int iType = Integer.parseInt(swiftMessage.substring(beginIndex, beginIndex + 3));
		MT7TYPE type = MT7TYPE.getValue(iType);

		if (type == MT7TYPE.NONE) {
			throw new IllegalArgumentException("Message type '" + iType + "' is not an MT7XX");
		}

		HashMap<String, String> fields = new HashMap<String, String>();

		ArrayList<String> fourthBlocks = parseBlocks(swiftMessage).get(4);
		for (Iterator<String> it = fourthBlocks.iterator(); it.hasNext();) {
			String block = it.next();
			fields.putAll(parseFields(block));
		}

		SwiftMT7XX mt7XX = new SwiftMT7XX(type, fileName, fields);

		return mt7XX;

	}

	/**
	 * Return a map of blocks where the same block number could be related to
	 * several blocks grouped in an ArrayList
	 * 
	 * @param message
	 * @return
	 */
	public static HashMap<Integer, ArrayList<String>> parseBlocks(String message) {

		HashMap<Integer, ArrayList<String>> allBlocks = new HashMap<Integer, ArrayList<String>>();

		Pattern pattern = Pattern.compile(BLOCK_PATTERN);
		Matcher matcher = pattern.matcher(message);

		while (matcher.find()) {
			/*
			 * The block format is a 4:XXXXXX so we split between the block
			 * number and the block content
			 */
			String block = matcher.group(1);
			int blockNumber = Integer.parseInt(block.substring(0, 1));
			block = block.substring(2, block.length());

			ArrayList<String> currentBlocks;
			if ((currentBlocks = allBlocks.get(blockNumber)) == null) {
				currentBlocks = new ArrayList<String>();
			}

			currentBlocks.add(block);
			allBlocks.put(blockNumber, currentBlocks);
		}

		return allBlocks;

	}

	/**
	 * Return the map with the pair id-value of each field in the block The
	 * format of each field is DD:"value of the field"
	 * 
	 * @param message
	 * @return
	 */
	public static HashMap<String, String> parseFields(String message) {
		HashMap<String, String> fields = new HashMap<String, String>();

		Pattern pattern = Pattern.compile(FIELD_PATTERN);
		Matcher matcher = pattern.matcher(message);

		while (matcher.find()) {
			/*
			 * The block format is a 4:XXXXXX so we split between the block
			 * number and the block content
			 */
			String field = matcher.group(1);

			// we are not considering the sub-blocks but just the fields
			if (!field.substring(0, 1).equals("{")) {
				int fieldNumberLength = field.indexOf(":");
				String fieldNumber = field.substring(0, fieldNumberLength);
				String fieldValue = field.substring(fieldNumberLength + 1, field.length() - 1);

				fields.put(fieldNumber, fieldValue);
			}

		}

		return fields;

	}

	/**
	 * Write a CSV file with the information stored in the MT7XX messages
	 * 
	 * @param mt7XXMessages
	 * @param path
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void writeCSV(List<SwiftMT7XX> mt7XXMessages, String path) throws IOException, URISyntaxException {

		readOutputConfig();

		FileWriter out = new FileWriter(path);

		try {

			/* Print the header*/
			String line = "TYPE" + COMMA_SEPARATOR + "FILE_NAME";			
			
			for (Iterator<String> itFields = OUTPUT_FIELDS.iterator(); itFields.hasNext();) {
				line += COMMA_SEPARATOR;
				line += itFields.next();							
			}
			
			line += NEW_LINE_SEPARATOR;
			
			out.write(line);
			
			/* Print the content of each message */			
			for (Iterator<SwiftMT7XX> it = mt7XXMessages.iterator(); it.hasNext();) {
				SwiftMT7XX message = it.next();
				line = "";

				line += message.getType() + COMMA_SEPARATOR + message.getFileName();

				for (Iterator<String> itFields = OUTPUT_FIELDS.iterator(); itFields.hasNext();) {
					String field = itFields.next();
					line += COMMA_SEPARATOR;
					
					/* If the field value is not defined we include a NA value in the corresponding place */ 
					String fieldValue;					
					if ((fieldValue = message.getFieldValue(field)) == null){												
						fieldValue = "NA";
					}
					line += fieldValue;
				}

				line += NEW_LINE_SEPARATOR;

				out.write(line);
			}
		} finally {
			out.close();
		}

	}

	/**
	 * Read the config file that define the fields that will be printed in the
	 * output file
	 * 
	 * @throws URISyntaxException
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	private static void readOutputConfig() throws URISyntaxException, NumberFormatException, IOException {

		OUTPUT_FIELDS.clear();

		File file = new File("resources/output_config.cfg");
		BufferedReader reader = new BufferedReader(new FileReader(file));

		try {
			String line;
			while ((line = reader.readLine()) != null) {
				OUTPUT_FIELDS.add(line);
			}
		} finally {
			reader.close();
		}

	}

}
