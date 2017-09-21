package com.infotrends.dao;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import com.google.gson.JsonObject;

public class SendPost {
	private static Logger mylogger = Logger.getLogger(SendPost.class.getName());
	/**
	 * HTTP POST request
	 * @param url
	 * @param urlParameters
	 * @return
	 * @throws Exception
	 */
	public static String sendPost(String url, String urlParameters, int timeout, int timeoutcount) {

		StringBuffer response = new StringBuffer();
		int responseCode = 0;
		
		for (int i = 0 ; i < timeoutcount ; i++) {
			 try {
				 URL obj = new URL(url);
					HttpURLConnection con = (HttpURLConnection) obj.openConnection();

					// add reuqest header
					con.setRequestMethod("POST");
					con.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
					con.setConnectTimeout(timeout*1000);
					con.setReadTimeout(timeout*1000);

					// Send post request
					con.setDoOutput(true);
					DataOutputStream wr = new DataOutputStream(con.getOutputStream());
					wr.write(urlParameters.getBytes("UTF-8"));
					wr.flush();
					wr.close();

					responseCode = con.getResponseCode();
					mylogger.info(
							"sendPost - \nSending 'POST' request to URL : " + url);
					mylogger.info(
							"sendPost - Post parameters : " + urlParameters);
					mylogger.info("sendPost - Response Code : " + responseCode);

					BufferedReader in = new BufferedReader(new InputStreamReader(
							con.getInputStream(), "UTF-8"));
					String inputLine;
					

					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();
			 } catch (IOException e) {
				 mylogger.info("sendPost - IOException : " + e.getMessage());
				 continue;
			 }
			 mylogger.info("sendPost - success");
			 break;
		}
		

		// print result
		mylogger.info("sendPost - " + response.toString());
		mylogger.info("sendPost - " + response.toString());
		if(responseCode!=200){
			JsonObject errorresponse = new JsonObject();
			errorresponse.addProperty("exception", "timeout");
			return errorresponse.toString();
		}

		return response.toString();
	}
	
	public static void main(String[] args){
		
		String resq = sendPost("http://192.168.10/123123", "123", 5, 3);
		System.out.println("resq: "+resq);
	}
}
