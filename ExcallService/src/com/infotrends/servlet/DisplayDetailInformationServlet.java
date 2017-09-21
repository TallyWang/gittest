package com.infotrends.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.infotrends.bean.CacheBean;
import com.infotrends.service.GetStoreInfoData;
import com.infotrends.servlet.fetnetverifyServlet;

public class DisplayDetailInformationServlet extends HttpServlet {

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
		String STORETYPE = "";
		String STORECITY = "";
		String STOREDISTRICT = "";
		String SELECTEDSTORE = "";
		JsonArray StoreInfoData = null;

		
		if (jsonobject.getString(planName+"displaydetailinformation").trim().length()!=0 || !jsonobject.getString(planName+"displaydetailinformation").isEmpty()) {
			SELECTEDSTORE = jsonobject.getString(planName+"displaydetailinformation");
			System.out.println("## SELECTEDSTORE: " + SELECTEDSTORE);	
		}else{
			JsonObject cacheJsonObject = CacheBean.XiaoiRobotCache.get("result_displaystore");
			String result_displaystore = cacheJsonObject.get("result_displaystore").getAsString();
			
			ResponseJsonObject.put("varmsg", result_displaystore);
			 
			ResponseJsonObject.put("addcommandname", "wrong");
			printwriter.println(ResponseJsonObject.toString());
			return;
		};
		
		
		
		// Do Service
			SELECTEDSTORE = jsonobject.getString(planName + "displaydetailinformation");

			if (SELECTEDSTORE.contains("門市")) {
				SELECTEDSTORE = SELECTEDSTORE.replace("門市", "");
			} else {
			};

			StoreInfoData = GetStoreInfoData.getStoreInfoData("", "", "", SELECTEDSTORE);
			System.out.println("## StoreInfoData: " + StoreInfoData);

			
			//防呆，判斷是否手動輸入錯誤
			if(!"[]".equals(StoreInfoData.toString())){

				// 檢查門市是否有值
				// 有值帶出詳細資訊(組字串)
				String weekDayTimeStr = "";
				String saturdayTime = "";
				String sundayTime = "";
				String address = "";
				
				for (JsonElement infoDistrict : StoreInfoData) {
					JsonObject informationObject = infoDistrict.getAsJsonObject();
					weekDayTimeStr = "週一~週五 "
							+ informationObject.get("businessstarttime").getAsString()
							+ "~"
							+ informationObject.get("businessendtime").getAsString()
							+ "\n";
					
					saturdayTime = "週六 "
							+ informationObject.get("businesssatstarttime")
									.getAsString() + "~"
							+ informationObject.get("businesssatendtime").getAsString()
							+ "\n";
					
					sundayTime = "週日 "
							+ informationObject.get("businesssunstarttime")
									.getAsString() + "~"
							+ informationObject.get("businesssunendtime").getAsString()
							+ "\n";
					
					address = "地址："
							+ informationObject.get("storeaddress").getAsString();
				}

				String result = SELECTEDSTORE +"門市 營業時間：\n"+ weekDayTimeStr + saturdayTime + sundayTime + address;

				
				//儲存下拉式選單供下一層錯誤流程用
				JsonObject cacheJsonObject = new JsonObject();
				cacheJsonObject.addProperty("result_displaydetailinformation", result);
				CacheBean.XiaoiRobotCache.put("result_displaydetailinformation", cacheJsonObject);
				
				ResponseJsonObject.put("varmsg",result);
				System.out.println("## result: " + result);
				
				printwriter.println(ResponseJsonObject.toString());
				return;
				
			}else{
				JsonObject cacheJsonObject = CacheBean.XiaoiRobotCache.get("result_storename");
				String result_storename = cacheJsonObject.get("result_storename").getAsString();
				ResponseJsonObject.put("varmsg", result_storename);
				
				ResponseJsonObject.put("addcommandname", "wrong");
				printwriter.println(ResponseJsonObject.toString());
				return;
			}
	}

}
