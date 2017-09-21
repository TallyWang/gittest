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

public class showmobliedetialinfoServlet extends HttpServlet {
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
	String phone_url_patternStr="";
	String platform="";
	
	try {
		//Do Service
		if (jsonobject.has("showmobliedetialinfo")) {
			SeachKey = jsonobject.getString("showmobliedetialinfo");
		};
		if (!SeachKey.equals("")){
			JsonArray MobliePriceData  = GetMobliePriceInfo.getMobliePriceData(SeachKey);	
			
			if (MobliePriceData.size()==0){
				ResponseJsonObject.put("addcommandname", "wrong");
			}else
			{
				
				for (JsonElement phone_url_pattern : MobliePriceData) {
					JsonObject phone_url_pattern_Object = phone_url_pattern.getAsJsonObject();
					phone_url_patternStr = phone_url_pattern_Object.get("url_pattern").getAsString();
					}
				
				if (jsonobject.has("platform")){
					platform = jsonobject.getString("platform");
				}else{
					
				};	
				
				String web_urlPattern ="";
				String app_line_fb_urlPattern ="";
				
				if(platform.equals("web")){
					//web進線走此條
					web_urlPattern = "https://www.fetnet.net/device/handset/detail/"+ phone_url_patternStr +"/intro";
					ResponseJsonObject.put("varmsg", web_urlPattern);
					printwriter.println(ResponseJsonObject.toString());
					return;
				}else{
					//app/line/fb或其他管道進線
					app_line_fb_urlPattern = "https://m.fetnet.net/device/handset/detail/"+ phone_url_patternStr +"/intro";
					ResponseJsonObject.put("varmsg", app_line_fb_urlPattern);
					printwriter.println(ResponseJsonObject.toString());
					return;	
				}
				
				
				
//				result ="[DropDownList]";
//				for(JsonElement MobliePrice : MobliePriceData){
//					JsonObject typeObject = MobliePrice.getAsJsonObject();
//					
//					 model_type = typeObject.get("model_type").getAsString();
//					 
//					 result = result+ "[option submit=\""+ model_type +"\"]"+ model_type +"[/option]";
//					 
//				}
//				result = result+ "[/DropDownList]";
//				ResponseJsonObject.put("varmsg", result);
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
