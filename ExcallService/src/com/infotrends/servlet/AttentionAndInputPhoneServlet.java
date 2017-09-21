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

public class AttentionAndInputPhoneServlet  extends HttpServlet {

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

//		mylogger.info("attentionServlet - pass: " + jsonobject.getString(planName+"attention"));	
		
//		檢查選項號碼
		String SelectedOption =jsonobject.getString("ganpattentionandinputphone");

		
		if(SelectedOption.equals("1")||SelectedOption.equals("2")){
			System.out.println("SelectedOption check: Pass");
			//顯示注意需知、輸入連絡電話
			
		}else{
			//重新輸入選項號碼
			
			ResponseJsonObject.put("addcommandname", "wrong");
//			ResponseJsonObject.put("nextfuction", "step");
//			ResponseJsonObject.put("nextstep", "1101");
		};
		
		
		printwriter.println(ResponseJsonObject.toString());
		return;
	}



}
