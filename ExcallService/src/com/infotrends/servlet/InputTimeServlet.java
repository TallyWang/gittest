package com.infotrends.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.infotrends.bean.CacheBean;
import com.infotrends.service.GetStoreInfoData;
import com.infotrends.servlet.fetnetverifyServlet;

public class InputTimeServlet  extends HttpServlet {

	private static Logger mylogger = Logger.getLogger(fetnetverifyServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// super.doPost(req, resp);

		JSONObject ResponseJsonObject = new JSONObject();

		// 取得Body內容(JSON)
		resp.setContentType("application/json; charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");

		PrintWriter printwriter = resp.getWriter();
		BufferedReader reader = req.getReader();

		String input = null;
		StringBuilder requestBodyBuilder = new StringBuilder();

		while ((input = reader.readLine()) != null) {
			requestBodyBuilder.append(input.trim());
		}

		String requestBody = requestBodyBuilder.toString().trim();

		// 解析Body(JSON)
		JSONObject jsonobject = new JSONObject(requestBody);
		System.out.println("## jsonobject: " + jsonobject.toString());

		
		String sessionId = jsonobject.getString("sessionId");
		String planName = jsonobject.getString("planname");
	
		JsonObject cacheJsonObject = CacheBean.XiaoiRobotCache.get(sessionId);
		String customerName = cacheJsonObject.getAsJsonObject("dataCache").get("ganpinputtime").getAsString();

		
		if(customerName.trim().length()==0 || customerName.trim().isEmpty() || customerName.equals(null)||  customerName.trim()=="" || "".equals(customerName) || customerName == null || customerName.equals("")){
			ResponseJsonObject.put("addcommandname", "wrong");
		}else{
			System.out.println("InputNameServlet - pass");
		};
		
		
		printwriter.println(ResponseJsonObject.toString());
		return;
	}



}
