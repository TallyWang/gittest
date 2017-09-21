package com.infotrends.service;

import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.infotrends.dao.GetTableData;
import com.infotrends.util.Util;

public class GetScenesRuleData {
	private static Logger mylogger = Logger.getLogger(GetScenesRuleData.class.getName());
	/**
	 * getScenesRuleData
	 * 
	 * @param aRuleId
	 * @return
	 */
	public static JsonArray getScenesRuleData(String aRuleId){
//		String DBDriver = "net.sourceforge.jtds.jdbc.Driver";
//		String DBUrl = "jdbc:jtds:sqlserver://192.168.10.42:1433;databaseName=RobotDemo";
//		String DBUser = "sa";
//		String DBPassword = "password";
		
		String DBDriver = Util.getRobot_DB_Driver();
		String DBUrl = Util.getRobot_DB_URL();
		String DBUser = Util.getRobot_DB_USER();
		String DBPassword = Util.getRobot_DB_PASS();
		
		String Sql = "Select * from ex_ScenesRule where dbid='"+ aRuleId +"'";

		mylogger.info("GetScenesRuleData - Sql: "+ Sql);
		System.out.println("GetScenesRuleData - Sql: "+ Sql);
		
		JsonArray ScenesRuleData = null;
		try {
			ScenesRuleData = GetTableData.getTableDataFromSql2O(DBDriver, Sql, DBUrl, DBUser, DBPassword);
			mylogger.info("ScenesRuleData - Sql: "+ ScenesRuleData);
			System.out.println("ScenesRuleData - Sql: "+ ScenesRuleData);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return ScenesRuleData;
	}
}
