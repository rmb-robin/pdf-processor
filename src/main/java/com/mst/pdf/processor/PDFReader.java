package com.mst.pdf.processor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
	static {
		Utility.loadProperties();
	}

	public static void main(String[] args) {
		PDFReader pdfReader = new PDFReader();
		pdfReader.processPDFFiles();
	}

	/**
	 * The method to process PDF files.
	 * 
	 */

	private void processPDFFiles() {
		int noOfOrganizations = Integer.valueOf(Utility.getProperty(Constants.NO_OF_ORGANIZATIONS));
		List<Path> listOfFiles = null;
		for (int orgNo = 1; orgNo <= noOfOrganizations; orgNo++) {
			listOfFiles = readNFilesOnly(orgNo);
			processFiles(listOfFiles, orgNo);
		}
	}

	/**
	 * The method to read only N files from the directory.
	 * 
	 * @param orgNo
	 * @return
	 */

	private List<Path> readNFilesOnly(int orgNo) {
		String inputDirectory = Utility.getProperty(Constants.INPUT_DIRECTORY_PATH + orgNo);
		List<Path> listOfFiles = null;
		logger.info("Input directory : " + inputDirectory);
		try {
			if (inputDirectory == null)
				throw new Exception("Directory " + inputDirectory + " not found, verify the configuration.");

			Path folder = Paths.get(inputDirectory);
			final int noOfFilesPerBatch = Integer.valueOf(Utility.getProperty(Constants.NO_OF_FILES_PER_BATCH + orgNo))
					+ 1;
			listOfFiles = Files.walk(folder).limit(noOfFilesPerBatch).map(p -> (Path) p).collect(Collectors.toList());

			listOfFiles.remove(0);// removing the parent directory as this is not needed in this collection.

			// listOfFiles.forEach(System.out::println);

		} catch (IOException e) {
			logger.error("Error while reading N files from the directory : " + e.getMessage());
		} catch (Exception e) {
			logger.error("Error while reading N files from the directory : " + e.getMessage());
		}
		return listOfFiles;
	}

	/**
	 * The method reads given N files from the parent directory (listOfFiles),
	 * stripes out extra contents, converts to JSON object and posts the JSON
	 * payload to MongoDB API Service.
	 *
	 */
	public void processFiles(List<Path> listOfFiles, int orgNo) {
		String outputDirectory = Utility.getProperty(Constants.OUTPUT_DIRECTORY_PATH + orgNo);
		String orgID = Utility.getProperty(Constants.ORG_ID + orgNo);

		logger.info("Output directory : " + outputDirectory);

		InputStream inputStream = null;
		String fileName = null;
		try {
			for (Path filePath : listOfFiles) {
				logger.info("File being processed : " + filePath);

				inputStream = new BufferedInputStream(new FileInputStream(new File(filePath.toString())));
				Parser parser = new AutoDetectParser();
				ContentHandler handler = new BodyContentHandler();
				parser.parse(inputStream, handler, new Metadata(), new ParseContext());
				logger.debug("The raw content read from the file : " + handler.toString());

				String contentAfterPhoneNo = cleanUpContent(handler.toString());
				logger.debug("The processed content of the file : " + contentAfterPhoneNo);

				SentenceTextRequest sentenceTextRequest = createSentenceTextRequestObj(contentAfterPhoneNo);
				// Added Org ID
				sentenceTextRequest.getDiscreteData().setOrganizationId(orgID);

				fileName = filePath.getFileName().toString();
				// the first few letters before "_" of the file name
				sentenceTextRequest.getDiscreteData().setPatientAccount(fileName.substring(0, fileName.indexOf("_")));
				logger.debug("The object of SentenceTextRequest ready for converting to JSON : "
						+ sentenceTextRequest.toString());

				// System.out.println("sentenceTextRequest : " + sentenceTextRequest);

				String jsonObj = new Gson().toJson(sentenceTextRequest);
				logger.debug("jsonObj of " + fileName + " : " + jsonObj);
				logger.info("Posting the JSON object to API Service for " + fileName);
				String response = PostJSONRequestor.getInstance().postJSONRequest(jsonObj);
				logger.debug("API response for " + fileName + " : " + response);
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error(e.getStackTrace());
				}
				// once the file is processed, move it to output directory
				Files.move(filePath, Paths.get(outputDirectory + fileName), StandardCopyOption.REPLACE_EXISTING);
				logger.info("File successfully processed and moved to output directory : " + fileName);
			}
		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());
		}
	}

	/**
	 * The method stripes out extra contents like text before the phone number,
	 * extra new line characters, replaces / with space, ! with ..
	 *
	 */
	private String cleanUpContent(String fileContent) {
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
	private SentenceTextRequest createSentenceTextRequestObj(String fileContent) {
		SentenceTextRequest sentenceTextRequest = new SentenceTextRequest();
		sentenceTextRequest.setText(fileContent);
		sentenceTextRequest.setDiscreteData(new DiscreteData());
		return sentenceTextRequest;
	}
}
