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
import com.infotrends.dao.GetTableData;
import com.infotrends.service.GetStoreInfoData;
import com.infotrends.servlet.fetnetverifyServlet;

public class storetypeServlet extends HttpServlet {

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

		// 解析Body(JSON)
		JSONObject jsonobject = new JSONObject(requestBody);
		String sessionId = jsonobject.getString("sessionId");

		try {
			// Do Service
			JsonArray StoreInfoData = GetStoreInfoData.getStoreInfoData("", "", "", "");

			String storeType = "";
			String result = "[DropDownList]";

			for (JsonElement infoType : StoreInfoData) {
				JsonObject typeObject = infoType.getAsJsonObject();

				storeType = typeObject.get("storetype").getAsString();

				result = result + "[option submit=\"" + storeType + "\"]" + storeType + "[/option]";

			}
			result = result + "[/DropDownList]";
			
			//儲存下拉式選單供下一層錯誤流程用
			JsonObject cacheJsonObject = new JsonObject();
			cacheJsonObject.addProperty("result_type", result);
			CacheBean.XiaoiRobotCache.put("result_type", cacheJsonObject);
			System.out.println(CacheBean.XiaoiRobotCache.toString());
			
			
			ResponseJsonObject.put("varmsg", result);
			System.out.println(result);

			printwriter.println(ResponseJsonObject.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

}
