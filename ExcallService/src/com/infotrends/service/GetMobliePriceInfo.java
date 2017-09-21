package com.infotrends.service;

import java.util.Calendar;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.infotrends.dao.GetTableData;
import com.infotrends.util.Util;

public class GetMobliePriceInfo {
	private static Logger mylogger = Logger.getLogger(GetScenesPlanData.class.getName());
	
	public static JsonArray getMobliePriceData(String SeachKey){

		String DBDriver = Util.getRobot_DB_Driver();
		String DBUrl = Util.getRobot_DB_URL();
		String DBUser = Util.getRobot_DB_USER();
		String DBPassword = Util.getRobot_DB_PASS();	
		
		
		String sql="";
		sql = "SELECT * from (select BRAND||MODEL_TYPE as SeachKey,SID,URL_PATTERN,MODEL_TYPE,DEVICE_TYPE from  EX_MOBILE_INFO) where replace(UPPER(SeachKey),' ','') like replace(UPPER('%" + SeachKey + "%'),' ','')";

		
		
	    mylogger.info("GetMobliePriceInfo -> getMobliePriceData -> sql: " + sql);
		   

		JsonArray MobliePriceData = null;
		try {
			MobliePriceData = GetTableData.getTableDataFromSql2O(DBDriver, sql, DBUrl, DBUser, DBPassword);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MobliePriceData;
	}
	
}
