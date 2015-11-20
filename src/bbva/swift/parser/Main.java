package bbva.swift.parser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import bbva.swift.SwiftMT7XX;

public class Main {
	public static void main(String[] args) {

		try {

			if (args.length < 1) {
				System.err.println(
						"Could not parse the file requested, filename to be parsed is required. \nUsage: java -jar SwiftParserXX.jar filename");
				System.exit(1);
			}

			/* Read the content of the swift file */
			HashMap<String, List<String>> swiftMessages = SwiftMT7XXParser.readSwiftFolder(args[0]);

			/*
			 * Parse the content of each swift message into a SwiftMT7XX object
			 */
			List<SwiftMT7XX> mt7XXMessages = new ArrayList<SwiftMT7XX>();

			for (Iterator<String> itFile = swiftMessages.keySet().iterator(); itFile.hasNext();) {

				String fileName = itFile.next();
				for (Iterator<String> it = swiftMessages.get(fileName).iterator(); it.hasNext();) {
					String message = it.next();

					try {
						mt7XXMessages.add(SwiftMT7XXParser.parseMT7XX(message, fileName));
					} catch (IllegalArgumentException ex) {
						System.err.println(ex.getMessage());
					}
				}
			}

			/* Print the fields defined in the output_config.cfg in a csv */
			SwiftMT7XXParser.writeCSV(mt7XXMessages, args[0] + "/output.csv");

		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (URISyntaxException e) {
			System.err.println(e.getMessage());
		}

	}

}