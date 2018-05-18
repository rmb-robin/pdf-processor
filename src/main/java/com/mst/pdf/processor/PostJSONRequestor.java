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

	public static String postJSONRequest(String text) throws Exception {
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
