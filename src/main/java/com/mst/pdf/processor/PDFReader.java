package com.mst.pdf.processor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

import com.google.gson.Gson;
import com.mst.pdf.util.Constants;
import com.mst.pdf.util.Utility;;

/**
 * @author Sandeep
 *
 */
public class PDFReader {

	static Logger logger = Logger.getLogger(PDFReader.class);

	public static void main(String[] args) {
		Utility.loadProperties();
		int noOfOrganizations = Integer.valueOf(Utility.getProperty(Constants.NO_OF_ORGANIZATIONS));
		for (int orgNo = 1; orgNo <= noOfOrganizations; orgNo++) {
			readDirectory(orgNo);

		}
	}

	/**
	 * The method reads files from the parent directory (fileDirectory), stripes out
	 * extra contents, converts to JSON object and posts the JSON payload to MongoDB
	 * API Service.
	 *
	 */
	public static void readDirectory(int orgNo) {
		String inputDirectory = Utility.getProperty(Constants.INPUT_DIRECTORY_PATH + orgNo);
		String outputDirectory = Utility.getProperty(Constants.OUTPUT_DIRECTORY_PATH + orgNo);
		String orgID = Utility.getProperty(Constants.ORG_ID + orgNo);

		logger.info("Input directory : " + inputDirectory);
		logger.info("Output directory : " + outputDirectory);

		InputStream inputStream = null;
		try {
			File dir = new File(inputDirectory);
			String[] fileNames = dir.list();
			if (fileNames == null)
				throw new Exception("Directory " + inputDirectory + " not found, verify the configuration.");

			for (String fileName : fileNames) {
				logger.info("File being processed : " + fileName);

				inputStream = new BufferedInputStream(new FileInputStream(new File(inputDirectory + fileName)));
				Parser parser = new AutoDetectParser();
				ContentHandler handler = new BodyContentHandler();
				parser.parse(inputStream, handler, new Metadata(), new ParseContext());
				logger.debug("The raw content read from the file : " + handler.toString());

				String contentAfterPhoneNo = cleanUpContent(handler.toString());
				logger.debug("The processed content of the file : " + contentAfterPhoneNo);

				SentenceTextRequest sentenceTextRequest = createSentenceTextRequestObj(contentAfterPhoneNo);
				// Added Org ID
				sentenceTextRequest.getDiscreteData().setOrganizationId(orgID);

				// the first few letters before "_" of the file name
				sentenceTextRequest.getDiscreteData().setPatientAccount(fileName.substring(0, fileName.indexOf("_")));
				logger.debug(
						"The object of SentenceTextRequest for converting to JSON : " + sentenceTextRequest.toString());

				// System.out.println("sentenceTextRequest : " + sentenceTextRequest);

				String jsonObj = new Gson().toJson(sentenceTextRequest);
				logger.debug("jsonObj of " + fileName + " : " + jsonObj);
				String response = PostJSONRequestor.postJSONRequest(jsonObj);
				logger.debug("API response for " + fileName + " : " + response);
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error(e.getStackTrace());
				}
				// once the file is processed, move it to output directory
				Files.move(Paths.get(inputDirectory + fileName), Paths.get(outputDirectory + fileName),
						StandardCopyOption.REPLACE_EXISTING);
				logger.info("File successfully processed and moved to output directory : " + fileName);
			}
		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error(e.getStackTrace());
				}
			}
		}
	}

	/**
	 * The method stripes out extra contents like text before the phone number,
	 * extra new line characters, replaces / with space, ! with ..
	 *
	 */
	private static String cleanUpContent(String fileContent) {
		fileContent = fileContent.trim().replaceAll("[\n]{2,}", " ");
		fileContent = fileContent.replaceAll("\t", " ");

		Pattern pattern = Pattern.compile("\\d\\d\\d-\\d\\d\\d\\d");
		Matcher match = pattern.matcher(fileContent);
		match.find();

		String contentAfterPhoneNo = fileContent.substring(match.end()).trim();
		contentAfterPhoneNo = contentAfterPhoneNo.replaceAll("/", " ");
		contentAfterPhoneNo = contentAfterPhoneNo.replace("!", ".");
		contentAfterPhoneNo = contentAfterPhoneNo.trim().replaceAll("[\n]{2,}", " ");

		return contentAfterPhoneNo;
	}

	/**
	 * The method create the object of SentenceTextRequest and returns the newly
	 * created object.
	 *
	 */
	private static SentenceTextRequest createSentenceTextRequestObj(String fileContent) {
		SentenceTextRequest sentenceTextRequest = new SentenceTextRequest();
		sentenceTextRequest.setText(fileContent);
		sentenceTextRequest.setDiscreteData(new DiscreteData());
		return sentenceTextRequest;
	}
}
