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

public class storedistrictServlet extends HttpServlet {

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
		String planName = jsonobject.getString("planname");
		String STORETYPE="";
		String STORECITY="";
		String STOREDISTRICT="";
		// Do Service


		if (jsonobject.getString(planName+"storedistrict").trim().length()!=0 || !jsonobject.getString(planName+"storedistrict").isEmpty()) {
			STORECITY = jsonobject.getString(planName+"storedistrict");
			System.out.println("## STORECITY: " + STORECITY);
		}else{
			JsonObject cacheJsonObject = CacheBean.XiaoiRobotCache.get("result_city");
			String result_city = cacheJsonObject.get("result_city").getAsString();
			
			ResponseJsonObject.put("varmsg", result_city);
			 
			ResponseJsonObject.put("addcommandname", "wrong");
			printwriter.println(ResponseJsonObject.toString());
			return;
		};
		
		
		JsonObject cacheJsonObject_type = CacheBean.XiaoiRobotCache.get(sessionId);
		STORETYPE =  cacheJsonObject_type.getAsJsonObject("dataCache").get("queryshopsinfostorecity").getAsString();
		
		if (jsonobject.getString(planName+"storedistrict")!="") {
			STORECITY = jsonobject.getString(planName+"storedistrict");
		}
		
		// Do Service
		
		// display District
		JsonArray StoreInfoData = GetStoreInfoData.getStoreInfoData(STORETYPE, STORECITY, "","");

		//防呆，判斷是否手動輸入錯誤
		if(!"[]".equals(StoreInfoData.toString())){

			String storeDistrict = "";
			String result = "[DropDownList]";
			
			for (JsonElement infocity : StoreInfoData) {
				JsonObject districtObject = infocity.getAsJsonObject();
				storeDistrict = districtObject.get("storearea_reg").getAsString();
				result = result + "[option submit=\"" + storeDistrict + "\"]" + storeDistrict + "[/option]";
				}
				
			result = result + "[/DropDownList]";
			
			//儲存下拉式選單供下一層錯誤流程用
			JsonObject cacheJsonObject = new JsonObject();
			cacheJsonObject.addProperty("result_district", result);
			CacheBean.XiaoiRobotCache.put("result_district", cacheJsonObject);

			
			ResponseJsonObject.put("varmsg", result);
			System.out.println("## result: " + result);
			printwriter.println(ResponseJsonObject.toString());
			return;
		}else{
			JsonObject cacheJsonObject = CacheBean.XiaoiRobotCache.get("result_city");
			String result_city = cacheJsonObject.get("result_city").getAsString();
			ResponseJsonObject.put("varmsg", result_city);
			
			ResponseJsonObject.put("addcommandname", "wrong");
			printwriter.println(ResponseJsonObject.toString());
			return;
		}
		
	}

}
