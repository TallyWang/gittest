package com.infotrends.service;

import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.infotrends.dao.SendPost;
import com.infotrends.util.Util;

public class SendRuleFunction {
	private static Logger mylogger = Logger.getLogger(SendRuleFunction.class.getName());
	
	/**
	 * sendRuleFunction
	 * 
	 * @param aScenesRuleData
	 * @param aDataCache
	 * @param timeout
	 * @return
	 */
	public static JsonObject sendRuleFunction(JsonArray aScenesRuleData, String aDataCache, int timeout, int timeoutcount){
//		if(aRuleId.equals("0")){
//			JsonObject ruleRespJsonObject = new JsonObject();
//			ruleRespJsonObject.addProperty("nextstep", "0");
//			return ruleRespJsonObject;
//		}
//		JsonArray ScenesRuleData = this.getScenesRuleData(aRuleId);
		mylogger.info("ExcallServlet - aScenesRuleData: "+aScenesRuleData);
		for(JsonElement ScenesRule : aScenesRuleData){
			JsonObject ScenesRuleJsonObject = ScenesRule.getAsJsonObject();
			mylogger.info("ExcallServlet - ScenesRuleJsonObject: "+ScenesRuleJsonObject);
			String Path = ScenesRuleJsonObject.get("path").getAsString();
			mylogger.info("ExcallServlet - Path: "+Path);
			
			mylogger.info("ExcallServlet - GO Path: "+Util.getProjectPath()+Path);
			String ruleResp = null;
			try {
				ruleResp = SendPost.sendPost(Util.getProjectPath()+Path, aDataCache, timeout, timeoutcount);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mylogger.info("sendRuleFunction - ruleResp: "+ruleResp);
			
			Gson gson = new Gson();
			JsonObject ruleRespJsonObject = gson.fromJson(ruleResp, JsonObject.class);
			
			return ruleRespJsonObject;
			
		}
		mylogger.info("sendRuleFunction - return null");
		return null;
	}
}
