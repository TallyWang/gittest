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
import org.sql2o.logging.SysOutLogger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.infotrends.bean.CacheBean;
import com.infotrends.service.GetStoreInfoData;
import com.infotrends.servlet.fetnetverifyServlet;

public class displaystoreServlet extends HttpServlet {


	private static Logger mylogger = Logger.getLogger(fetnetverifyServlet.class
			.getName());

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
		System.out.println(requestBody);

		// 解析Body(JSON)
		JSONObject jsonobject = new JSONObject(requestBody);
		System.out.println(jsonobject.toString());

		String sessionId = jsonobject.getString("sessionId");
		System.out.println("displaystoreServlet - sessionId: " + sessionId);
		
		String planName = jsonobject.getString("planname");
		String STORETYPE="";
		String STORECITY="";
		String STOREDISTRICT="";
		
		
		if (jsonobject.getString(planName+"displaystore").trim().length()!=0 || !jsonobject.getString(planName+"displaystore").isEmpty()) {
			STOREDISTRICT = jsonobject.getString(planName+"displaystore");
			System.out.println("## STOREDISTRICT: " + STOREDISTRICT);	
		}else{
			JsonObject cacheJsonObject = CacheBean.XiaoiRobotCache.get("result_district");
			String result_district = cacheJsonObject.get("result_district").getAsString();
			
			ResponseJsonObject.put("varmsg", result_district);
			 
			ResponseJsonObject.put("addcommandname", "wrong");
			printwriter.println(ResponseJsonObject.toString());
			return;
		};
		
		
		//從XiaoiRobotCache取出已選類別、縣市
		JsonObject cacheJsonObject_type = CacheBean.XiaoiRobotCache.get(sessionId);
		STORETYPE =  cacheJsonObject_type.getAsJsonObject("dataCache").get("queryshopsinfostorecity").getAsString();
		
		JsonObject cacheJsonObject_city = CacheBean.XiaoiRobotCache.get(sessionId);
		STORECITY =  cacheJsonObject_city.getAsJsonObject("dataCache").get("queryshopsinfostoredistrict").getAsString();
	
		
		// Do Service
		
		JsonArray StoreInfoData = GetStoreInfoData.getStoreInfoData(STORETYPE, STORECITY, STOREDISTRICT,"");
		
		
		//防呆，判斷是否手動輸入錯誤
		if(!"[]".equals(StoreInfoData.toString())){

			String storeName = "";
			String result = "[DropDownList]";
			
			for (JsonElement infocity : StoreInfoData) {
				JsonObject displaystoreObject = infocity.getAsJsonObject();
				storeName = displaystoreObject.get("storename").getAsString();
				result = result + "[option submit=\"" + storeName + "\"]" + storeName + "[/option]";
				}
				
			result = result + "[/DropDownList]";
			
			//儲存下拉式選單供下一層錯誤流程用
			JsonObject cacheJsonObject = new JsonObject();
			cacheJsonObject.addProperty("result_storename", result);
			CacheBean.XiaoiRobotCache.put("result_storename", cacheJsonObject);

			
			ResponseJsonObject.put("varmsg", result);
			System.out.println("## result: " + result);
			printwriter.println(ResponseJsonObject.toString());
			return;
		}else{
			JsonObject cacheJsonObject = CacheBean.XiaoiRobotCache.get("result_district");
			String result_district = cacheJsonObject.get("result_district").getAsString();
			ResponseJsonObject.put("varmsg", result_district);
			
			ResponseJsonObject.put("addcommandname", "wrong");
			printwriter.println(ResponseJsonObject.toString());
			return;
		}
	}	
}
