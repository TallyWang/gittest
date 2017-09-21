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
import com.infotrends.dao.GetTableData;
import com.infotrends.service.GetMobliePriceInfo;

public class showmoblieinfoServlet extends HttpServlet {
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
	String sessionId = jsonobject.getString("sessionId");
	String SeachKey = "";
	String model_type = "";
	String result ="";
	try {
		//Do Service
		if (jsonobject.has("showmoblieinfo")) {
			SeachKey = jsonobject.getString("showmoblieinfo");
		};
		if (!SeachKey.equals(""))
		{
			JsonArray MobliePriceData  = GetMobliePriceInfo.getMobliePriceData(SeachKey);	
			
			if (MobliePriceData.size()==0)
			{
				ResponseJsonObject.put("addcommandname", "wrong");
			}
			else
			{
				result ="[DropDownList]";
				for(JsonElement MobliePrice : MobliePriceData){
					JsonObject typeObject = MobliePrice.getAsJsonObject();
					
					 model_type = typeObject.get("model_type").getAsString();
					 
					 result = result+ "[option submit=\""+ model_type +"\"]"+ model_type +"[/option]";
					 
				}
				result = result+ "[/DropDownList]";
				ResponseJsonObject.put("varmsg", result);
			}
		}
		else
		{
			ResponseJsonObject.put("addcommandname", "wrong");
		}
		
		System.out.println(result);
		
	
		printwriter.println(ResponseJsonObject.toString());
	} catch (Exception e) {
		e.printStackTrace();
	}
	return;
}


}
