package com.mst.pdf.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.mst.pdf.util.Constants;
import com.mst.pdf.util.Utility;

public class PostJSONRequestor {
	static Logger logger = Logger.getLogger(PostJSONRequestor.class);
	private static PostJSONRequestor instance = null;

	private PostJSONRequestor() {
	}

	public static PostJSONRequestor getInstance() {

		if (instance == null) {
			synchronized (PostJSONRequestor.class) {
				if (instance == null) {
					instance = new PostJSONRequestor();
				}
			}
		}
		return instance;
	}

	private HttpURLConnection getHttpURLConnection() {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(Utility.getProperty(Constants.REQUEST_DB_URL));
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json");
		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());
		}
		return connection;
	}

	public void closeHttpURLConnection(HttpURLConnection connection) {
		try {
			if (connection != null)
				connection.disconnect();
		} catch (Exception e) {
			logger.error("Error while closing the connection : " + e.getMessage());
		}
	}

	public String postJSONRequest(String text) throws Exception {
		// set URL to Post Request
		String responseMsg = null;
		OutputStreamWriter streamWriter = null;
		HttpURLConnection connection = null;
		try {
			connection = getHttpURLConnection();
			streamWriter = new OutputStreamWriter(connection.getOutputStream());

			streamWriter.write(text);
			streamWriter.flush();
			streamWriter.close();

			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			} catch (IOException ioe) {
				logger.error("IOException : " + ioe.getMessage());
				if (!ioe.getMessage().contains("HTTP response code: 500"))
					throw ioe;
			}
			String response = null;
			StringBuffer buffer = new StringBuffer();

			while (br != null && (response = br.readLine()) != null) {
				buffer.append(response);
			}
			responseMsg = connection.getResponseCode() + "~" + buffer.toString();
		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());
			throw e;
		} finally {
			this.closeHttpURLConnection(connection);
		}
		return responseMsg;
	}

	public static String postJSONRequest_01(String text) throws Exception {
		// set URL to Post Request
		String responseMsg = null;

		HttpURLConnection conn = null;

		try {
			URL url = new URL(Utility.getProperty(Constants.REQUEST_DB_URL));
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");
			OutputStreamWriter streamWriter = new OutputStreamWriter(conn.getOutputStream());

			streamWriter.write(text);
			streamWriter.flush();

			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} catch (IOException ioe) {
				logger.error("IOException : " + ioe.getMessage());
				if (!ioe.getMessage().contains("HTTP response code: 500"))
					throw ioe;
			}
			String response = null;
			StringBuffer buffer = new StringBuffer();

			while (br != null && (response = br.readLine()) != null) {
				buffer.append(response);
			}
			responseMsg = conn.getResponseCode() + "~" + buffer.toString();
		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());
			throw e;
		} finally {
			if (conn != null)
				conn.disconnect();
		}

		return responseMsg;
	}
}
