package com.infotrends.servlet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.infotrends.bean.CacheBean;
import com.infotrends.bean.BasicProblemInfoBean;
import com.infotrends.bean.ProblemBean;
import com.infotrends.bean.ContactPersonalInfoBean;
import com.infotrends.bean.DetailProblemFormOneBean;
import com.infotrends.bean.DetailProblemInfoBean;
import com.infotrends.dao.GetTableData;
import com.infotrends.dao.SendPost;
import com.infotrends.service.GetEndPathMsgData;
import com.infotrends.service.GetScenesPlanData;
import com.infotrends.service.GetScenesRuleData;
import com.infotrends.service.GetScenesStepData;
import com.infotrends.service.SendRuleFunction;
import com.infotrends.util.Util;

public class ExcallServlet extends HttpServlet {
	
	private static Logger mylogger = Logger.getLogger(ExcallServlet.class.getName());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
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
		
		//Do Service
		JsonObject cacheJsonObject = new JsonObject();
		String planName = null;
		String stepId = null;
		String RuleId = null;
		String StepName = null;
		String Msgtype = null;
		String ExtraStep = null;
		String NextFunction = "plan";
		String steptimeout = null;
		String steptimeoutcount = null;
		String rulenextstep = null;
		String rulename = null;
		String ruletimeout = null;
		String ruletimeoutcount = null;
		
		//檢查SessionId是否有進入流程過
		if (CacheBean.XiaoiRobotCache.containsKey(sessionId)) {
			cacheJsonObject = CacheBean.XiaoiRobotCache.get(sessionId);
			planName = cacheJsonObject.get("planname").getAsString();
			stepId = cacheJsonObject.get("stepid").getAsString();
			RuleId = cacheJsonObject.get("ruleid").getAsString();
			NextFunction = cacheJsonObject.get("nextfuction").getAsString();
		}
		
		mylogger.info("ExcallServlet - planName: "+planName);
		mylogger.info("ExcallServlet - stepId: "+stepId);
		mylogger.info("ExcallServlet - RuleId: "+RuleId);
		mylogger.info("ExcallServlet - NextFunction: "+NextFunction);
		
		//取得指令名稱(以了解進入哪一個流程計畫)
		if(jsonobject.has("command")){
			planName = jsonobject.getString("command");
		}
		
		//假如沒有指令名稱，回報錯誤
		if(planName==null){
			ResponseJsonObject.put("content", " planName==null [command==null] ");
			printwriter.println(ResponseJsonObject.toString());
			return;
		}
		
		//程式判斷取值機制 (判斷是否有step與rule，假如沒有，去PlanTable取得StepId)
		if(!NextFunction.equals("step")&&!NextFunction.equals("rule")){
			// Query ScenesPlanData
			JsonArray ScenesPlanData  = GetScenesPlanData.getScenesPlanData(planName);
			mylogger.info("ExcallServlet - ScenesPlanData: "+ScenesPlanData);
			for(JsonElement ScenesPlan : ScenesPlanData){
				JsonObject ScenesPlanJsonObject = ScenesPlan.getAsJsonObject();
				mylogger.info("ExcallServlet - ScenesPlanJsonObject: "+ScenesPlanJsonObject);
				mylogger.info("ExcallServlet - planName: "+planName);
				mylogger.info("ExcallServlet - planname: "+ScenesPlanJsonObject.get("planname").getAsString());
				stepId = ScenesPlanJsonObject.get("stepid").getAsString();
			}
		}
		
		mylogger.info("ExcallServlet - stepId: " + stepId);
		
		//程式判斷取值機制 (判斷是否有rule，假如沒有，去StepTable取得RuleId、回傳內容與Step的名稱)
		if(!NextFunction.equals("rule")){
			// Query ScenesStepData
			JsonArray ScenesStepData = GetScenesStepData.getScenesStepData(stepId);
			mylogger.info("ExcallServlet - ScenesStepData: "+ScenesStepData);
			for(JsonElement ScenesStep : ScenesStepData){
				JsonObject ScenesStepJsonObject = ScenesStep.getAsJsonObject();
				mylogger.info("ExcallServlet - ScenesStepJsonObject: "+ScenesStepJsonObject);
				RuleId = ScenesStepJsonObject.get("ruleid").getAsString();
				StepName = ScenesStepJsonObject.get("stepname").getAsString();
				//未來使用參數
				steptimeout = ScenesStepJsonObject.get("timeout").getAsString();
				steptimeoutcount = ScenesStepJsonObject.get("timeoutcount").getAsString();
				//未來使用參數
			}
		}
		
		mylogger.info("ExcallServlet - RuleId: "+RuleId);
		
		//依據Step紀錄客戶輸入資訊，以便查詢使用
		JsonObject dataCache = new JsonObject();
		if(cacheJsonObject.has("dataCache")){
			dataCache = cacheJsonObject.get("dataCache").getAsJsonObject();
		}
		//判斷送回前端的型態
		dataCache.addProperty(StepName, jsonobject.getString("content"));
		dataCache.addProperty("planname", planName);
		dataCache.addProperty("sessionId", sessionId);
		
		JsonObject ruleRespJsonObject = new JsonObject();
		String rulenextsteptype = null;
		String rulenextstepvalue = null;
		
		//撈取RuleTable以判別要往哪個程式Path執行程式
		if(RuleId.equals("0")){
			rulenextsteptype = "0";
			rulenextstepvalue = "0";
		}else{
			JsonArray ScenesRuleData = GetScenesRuleData.getScenesRuleData(RuleId);
			rulenextstep = Util.getGString(ScenesRuleData.get(0).getAsJsonObject(), "nextstep");
			rulename = Util.getGString(ScenesRuleData.get(0).getAsJsonObject(), "rulename");
			if(!rulenextstep.equals("0")){
				String[] rulenextstepArray = rulenextstep.split("\\(");
				rulenextsteptype = rulenextstepArray[0];
				rulenextstepvalue = rulenextstepArray[1].replaceAll("\\)", "");
			}else{
				rulenextsteptype = "0";
				rulenextstepvalue = "0";
			}
			ruletimeout = Util.getGString(ScenesRuleData.get(0).getAsJsonObject(), "timeout");
			ruletimeoutcount = Util.getGString(ScenesRuleData.get(0).getAsJsonObject(), "timeoutcount");
			ruleRespJsonObject = SendRuleFunction.sendRuleFunction(ScenesRuleData, dataCache.toString(), Integer.valueOf(ruletimeout), Integer.valueOf(ruletimeoutcount));
			mylogger.info("ExcallServlet - ruleRespJsonObject: "+ruleRespJsonObject);
		}
		
		//rule如有回傳文字取代文字
		if(Util.getGString(ruleRespJsonObject, "msg")!=null){
			rulenextsteptype = Util.getGString(ruleRespJsonObject, "nextfuction");
			rulenextstepvalue = Util.getGString(ruleRespJsonObject, "nextstep");
			Msgtype = "text";
		}
		
		//判斷是否有回傳commandname
//		String commandname =  StepName;
		String commandname =  rulename;
		if(Util.getGString(ruleRespJsonObject, "addcommandname")!=null){
			commandname = rulename+"|"+Util.getGString(ruleRespJsonObject, "addcommandname");
		}
		if(Util.getGString(ruleRespJsonObject, "exception")!=null){
//			planName = "exception";
			commandname = Util.getGString(ruleRespJsonObject, "exception");
		}
		mylogger.info("ExcallServlet - commandname: "+commandname);
		if(Util.getGString(ruleRespJsonObject, "nextstep")!=null){
			Msgtype = "text";
			rulenextsteptype = Util.getGString(ruleRespJsonObject, "nextfuction");
			rulenextstepvalue = Util.getGString(ruleRespJsonObject, "nextstep");
		}
		
		//fetnet驗證失敗時導向
		if(jsonobject.has("fetnetverify")){
			if(jsonobject.getString("fetnetverify").equals("false")){
				commandname = rulename+"|"+jsonobject.getString("fetnetverify");
				//跳出流程(待確認)
				rulenextsteptype = "0";
				rulenextstepvalue = "0";
			}
			if(jsonobject.getString("fetnetverify").equals("falseandovercount")){
				commandname = rulename+"|"+jsonobject.getString("fetnetverify");
				//跳出流程(待確認)
				rulenextsteptype = "0";
				rulenextstepvalue = "0";
			}
			if(jsonobject.getString("fetnetverify").equals("noverify")){
				commandname = rulename+"|"+jsonobject.getString("fetnetverify");
				//跳出流程(待確認)
				rulenextsteptype = "0";
				rulenextstepvalue = "0";
			}
		}
		
		String extrasteptype = "rule";
		String extrastepvalue = null;
		while(extrasteptype.equals("rule")){
			
//			if(rulenextsteptype.equals("rule")){
			if(!commandname.equals(rulename)){

				//結束此次對談撈取回傳文字內容
				JsonArray EndPathMsgData = new JsonArray();
				String content = "流程有誤，請確認"; //宣告+防呆
				try{
					EndPathMsgData = GetEndPathMsgData.getEndPathMsgData(planName, commandname);
					Msgtype = Util.getGString(EndPathMsgData.get(0).getAsJsonObject(), "endpathmsgtype");
					
					//處理 content變數
					content = Util.getGString(EndPathMsgData.get(0).getAsJsonObject(), "endpathmsg");
					
					if(Util.getGString(ruleRespJsonObject, "varphonenum")!=null){
						try{
							mylogger.info("ExcallServlet - varphonenum: "+Util.getGString(ruleRespJsonObject, "varphonenum"));
							content = content.replaceAll("\\$\\{varphonenum\\}", Util.getGString(ruleRespJsonObject, "varphonenum"));
						}catch(Exception e){
							mylogger.info("ExcallServlet - Exception: "+e.getMessage());
						}
						
					}
					if(Util.getGString(ruleRespJsonObject, "varmsg")!=null){
						try{
							mylogger.info("ExcallServlet - varmsg: "+Util.getGString(ruleRespJsonObject, "varmsg"));
							content = content.replaceAll("\\$\\{varmsg\\}", Util.getGString(ruleRespJsonObject, "varmsg"));
						}catch(Exception e){
							mylogger.info("ExcallServlet - Exception: "+e.getMessage());
						}
					}
					
					//處理ExtraStep
					ExtraStep = Util.getGString(EndPathMsgData.get(0).getAsJsonObject(), "extrastep");
					mylogger.info("ExcallServlet - ExtraStep: "+ExtraStep);
					if(ExtraStep!=null){
						if(!ExtraStep.equals("0")){
							String[] ExtraStepArray = ExtraStep.split("\\(");
							extrasteptype = ExtraStepArray[0];
							extrastepvalue = ExtraStepArray[1].replaceAll("\\)", "");
						}else{
							extrasteptype = "0";
							extrastepvalue = "0";
						}
						mylogger.info("ExcallServlet - extrasteptype: "+extrasteptype);
						mylogger.info("ExcallServlet - extrastepvalue: "+extrastepvalue);
						rulenextsteptype = extrasteptype;
						rulenextstepvalue = extrastepvalue;
					}else{
						extrasteptype="noExtraStep";
					}
					
					mylogger.info("ExcallServlet - rulenextsteptype: "+rulenextsteptype);
					mylogger.info("ExcallServlet - rulenextstepvalue: "+rulenextstepvalue);
					
					mylogger.info("ExcallServlet - ExtraStep: "+ExtraStep);
					
				}catch(Exception e){
					e.printStackTrace();
					mylogger.info("ExcallServlet - Exception: "+e.getMessage());
					//跳出流程(待確認)
					rulenextsteptype = "0";
					rulenextstepvalue = "0";
					break;
				}
			}
			
			
			
		//當下一個動作也是rule時，撈取RuleTable以判別要往哪個程式Path執行程式
		while(rulenextsteptype.equals("rule")){
			mylogger.info("ExcallServlet - bgrulenextsteptype: "+rulenextsteptype);
			mylogger.info("ExcallServlet - bgrulenextstepvalue: "+rulenextstepvalue);
			JsonArray ScenesRuleDataAgain = GetScenesRuleData.getScenesRuleData(rulenextstepvalue);
			rulenextstep = Util.getGString(ScenesRuleDataAgain.get(0).getAsJsonObject(), "nextstep");
			rulename = Util.getGString(ScenesRuleDataAgain.get(0).getAsJsonObject(), "rulename");
			if(!rulenextstep.equals("0")){
				String[] rulenextstepArray = rulenextstep.split("\\(");
				rulenextsteptype = rulenextstepArray[0];
				rulenextstepvalue = rulenextstepArray[1].replaceAll("\\)", "");
			}else{
				rulenextsteptype = "0";
				rulenextstepvalue = "0";
			}
			ruletimeout = Util.getGString(ScenesRuleDataAgain.get(0).getAsJsonObject(), "timeout");
			ruletimeoutcount = Util.getGString(ScenesRuleDataAgain.get(0).getAsJsonObject(), "timeoutcount");
			ruleRespJsonObject = SendRuleFunction.sendRuleFunction(ScenesRuleDataAgain, dataCache.toString(), Integer.valueOf(ruletimeout), Integer.valueOf(ruletimeoutcount));
			mylogger.info("ExcallServlet - ruleRespJsonObject: "+ruleRespJsonObject);
			
			commandname = rulename;
			
			//判斷是否有回傳commandname
			if(Util.getGString(ruleRespJsonObject, "addcommandname")!=null){
				commandname = rulename+"|"+Util.getGString(ruleRespJsonObject, "addcommandname");
			}
			if(Util.getGString(ruleRespJsonObject, "exception")!=null){
//				planName = "exception";
				commandname = Util.getGString(ruleRespJsonObject, "exception");
			}
			mylogger.info("ExcallServlet - commandname: "+commandname);
			
			//rule如有回傳文字取代文字
			if(Util.getGString(ruleRespJsonObject, "nextstep")!=null){
				Msgtype = "text";
				rulenextsteptype = Util.getGString(ruleRespJsonObject, "nextfuction");
				rulenextstepvalue = Util.getGString(ruleRespJsonObject, "nextstep");
			}
			
			//fetnet驗證失敗時導向
			if(jsonobject.has("fetnetverify")){
				if(jsonobject.getString("fetnetverify").equals("false")){
					commandname = rulename+"|"+jsonobject.getString("fetnetverify");
					//跳出流程(待確認)
					rulenextsteptype = "0";
					rulenextstepvalue = "0";
				}
			}
			
		}
		
		mylogger.info("ExcallServlet - rulenextsteptype: "+rulenextsteptype);
		mylogger.info("ExcallServlet - rulenextstepvalue: "+rulenextstepvalue);
		
		extrasteptype = rulenextsteptype;
		extrastepvalue = rulenextstepvalue;
		
		//結束此次對談撈取回傳文字內容
		JsonArray EndPathMsgData = new JsonArray();
		String content = "流程有誤，請確認"; //宣告+防呆
		try{
			EndPathMsgData = GetEndPathMsgData.getEndPathMsgData(planName, commandname);
			Msgtype = Util.getGString(EndPathMsgData.get(0).getAsJsonObject(), "endpathmsgtype");
			
			//處理 content變數
			content = Util.getGString(EndPathMsgData.get(0).getAsJsonObject(), "endpathmsg");
			
			if(Util.getGString(ruleRespJsonObject, "varphonenum")!=null){
				try{
					mylogger.info("ExcallServlet - varphonenum: "+Util.getGString(ruleRespJsonObject, "varphonenum"));
					content = content.replaceAll("\\$\\{varphonenum\\}", Util.getGString(ruleRespJsonObject, "varphonenum"));
				}catch(Exception e){
					mylogger.info("ExcallServlet - Exception: "+e.getMessage());
				}
				
			}
			if(Util.getGString(ruleRespJsonObject, "varmsg")!=null){
				try{
					mylogger.info("ExcallServlet - varmsg: "+Util.getGString(ruleRespJsonObject, "varmsg"));
					content = content.replaceAll("\\$\\{varmsg\\}", Util.getGString(ruleRespJsonObject, "varmsg"));
				}catch(Exception e){
					mylogger.info("ExcallServlet - Exception: "+e.getMessage());
				}
			}
			
			//處理ExtraStep
			ExtraStep = Util.getGString(EndPathMsgData.get(0).getAsJsonObject(), "extrastep");
			mylogger.info("ExcallServlet - ExtraStep: "+ExtraStep);
			if(ExtraStep!=null){
				if(!ExtraStep.equals("0")){
					String[] ExtraStepArray = ExtraStep.split("\\(");
					extrasteptype = ExtraStepArray[0];
					extrastepvalue = ExtraStepArray[1].replaceAll("\\)", "");
				}else{
					extrasteptype = "0";
					extrastepvalue = "0";
				}
				mylogger.info("ExcallServlet - extrasteptype: "+extrasteptype);
				mylogger.info("ExcallServlet - extrastepvalue: "+extrastepvalue);
				rulenextsteptype = extrasteptype;
				rulenextstepvalue = extrastepvalue;
			}else{
				extrasteptype="noExtraStep";
			}
			
			mylogger.info("ExcallServlet - rulenextsteptype: "+rulenextsteptype);
			mylogger.info("ExcallServlet - rulenextstepvalue: "+rulenextstepvalue);
			
			mylogger.info("ExcallServlet - ExtraStep: "+ExtraStep);
			
		}catch(Exception e){
			e.printStackTrace();
			mylogger.info("ExcallServlet - Exception: "+e.getMessage());
			//跳出流程(待確認)
			rulenextsteptype = "0";
			rulenextstepvalue = "0";
			break;
		}
		
		ResponseJsonObject.put("content", content);
		}
		
		//紀錄本次動作至RobotCache內
		cacheJsonObject.addProperty("planname", planName);
		if(rulenextsteptype.equals("step")){
			stepId = rulenextstepvalue;
		}else
		if(rulenextsteptype.equals("rule")){
			RuleId = rulenextstepvalue;
		}
		cacheJsonObject.addProperty("stepid", stepId);
		cacheJsonObject.addProperty("ruleid", RuleId);
		cacheJsonObject.addProperty("nextfuction", rulenextsteptype);
		cacheJsonObject.add("dataCache", dataCache);
		CacheBean.XiaoiRobotCache.put(sessionId, cacheJsonObject);
		
		//rule如有回傳文字取代文字
		if(Util.getGString(ruleRespJsonObject, "msg")!=null){
			Msgtype = "text";
			rulenextsteptype = Util.getGString(ruleRespJsonObject, "nextfuction");
			rulenextstepvalue = Util.getGString(ruleRespJsonObject, "nextstep");
			ResponseJsonObject.put("content", Util.getGString(ruleRespJsonObject, "msg"));
		}
		
		//假如回傳下個動作名稱為0，即可結束流程，清除Cache
		if(rulenextstepvalue.equals("0")){
			CacheBean.XiaoiRobotCache.remove(sessionId);
		}
		
		ResponseJsonObject.put("timeout", steptimeout);
		ResponseJsonObject.put("timeoutcount", steptimeoutcount);
		
		//程式回傳exception時跳離or...(待驗證)
		if(Util.getGString(ruleRespJsonObject, "exception")!=null){
			CacheBean.XiaoiRobotCache.remove(sessionId);
			ResponseJsonObject.put("timeout", "0");
			ResponseJsonObject.put("timeoutcount", "0");
		}
		
		//增加交互紀錄
		
		//回傳資訊
		ResponseJsonObject.put("sessionId", sessionId);
		ResponseJsonObject.put("Msgtype", Msgtype);
		ResponseJsonObject.put("nextfuction", rulenextsteptype);
		ResponseJsonObject.put("nextstep", rulenextstepvalue);
//		ResponseJsonObject.put("timeout", steptimeout);
//		ResponseJsonObject.put("timeoutcount", steptimeoutcount);
		printwriter.println(ResponseJsonObject.toString());
		return;
		
	}
	
//	public static void main(String[] args){
//		String DBDriver = "net.sourceforge.jtds.jdbc.Driver";
//		String Sql = "Select * from ScenesPlan";
//		String DBUrl = "jdbc:jtds:sqlserver://192.168.10.42:1433;databaseName=RobotDemo";
////		String Sql = "Select DBID from tblSystemCfg";
////		String DBUrl = "jdbc:jtds:sqlserver://192.168.10.42:1433;databaseName=HongLin";
//		String DBUser = "sa";
//		String Password = "password";
//		ExcallServlet testModeXiaoiRobotController = new ExcallServlet();
//		JsonArray test = null;
//		try {
//			test = testModeXiaoiRobotController.getTableDataFromSql2O(DBDriver, Sql, DBUrl, DBUser, Password);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		mylogger.info("test: "+test);
////		System.out.println("test: "+test);
//	}

}
