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

public class InputTransferredNumberServlet  extends HttpServlet {

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

		//檢查聯絡時間是否輸入、判斷是否需顯示攜碼(不需要則引導至110801)、輸入欲攜碼號碼、檢查派送哪支工單API (在Servlet中引導nextstep)
		
		JsonObject cacheJsonObject = CacheBean.XiaoiRobotCache.get(sessionId);
		String customerTime = cacheJsonObject.getAsJsonObject("dataCache").get("ganpinputtransferrednumber").getAsString();
																				
		//檢查連絡時間是否輸入
		if(customerTime.equals("上午")||customerTime.equals("下午")||customerTime.equals("不限")){
			System.out.println("Inputtransferrednumber - customerTime: Pass");
		}else{
			//連絡時間未輸入
			ResponseJsonObject.put("addcommandname", "wrong");

			printwriter.println(ResponseJsonObject.toString());
			return;
		};
		
		
		String optioNnumber = cacheJsonObject.getAsJsonObject("dataCache").get("ganpattentionandinputphone").getAsString();

		
		if(optioNnumber.equals("2")){
			//請輸入攜碼號碼
			System.out.println("optioNnumber equals 2: Pass");
		}else{
			//導入7128工單
			ResponseJsonObject.put("nextfuction", "step");
			ResponseJsonObject.put("nextstep", "1106");	
		};
		
		
		printwriter.println(ResponseJsonObject.toString());
		return;
	}



}
