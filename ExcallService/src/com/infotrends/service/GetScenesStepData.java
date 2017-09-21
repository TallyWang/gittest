package com.infotrends.service;

import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.infotrends.dao.GetTableData;
import com.infotrends.util.Util;

public class GetScenesStepData {
	private static Logger mylogger = Logger.getLogger(GetScenesStepData.class.getName());
	/**
	 * getScenesStepData
	 * 
	 * @param aStepId
	 * @return
	 */
	public static JsonArray getScenesStepData(String aStepId){
//		String DBDriver = "net.sourceforge.jtds.jdbc.Driver";
//		String DBUrl = "jdbc:jtds:sqlserver://192.168.10.42:1433;databaseName=RobotDemo";
//		String DBUser = "sa";
//		String DBPassword = "password";
		String DBDriver = Util.getRobot_DB_Driver();
		String DBUrl = Util.getRobot_DB_URL();
		String DBUser = Util.getRobot_DB_USER();
		String DBPassword = Util.getRobot_DB_PASS();
		
		String Sql = "Select * from ex_ScenesStep where dbid='"+aStepId+"'";

		mylogger.info("getScenesStepData - Sql: " + Sql);
		System.out.println("getScenesStepData - Sql: " + Sql);
		
		JsonArray ScenesStepData = null;
		try {
			ScenesStepData = GetTableData.getTableDataFromSql2O(DBDriver, Sql, DBUrl, DBUser, DBPassword);
			mylogger.info("ScenesStepData - Sql: " + ScenesStepData);
			System.out.println("ScenesStepData - Sql: " + ScenesStepData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ScenesStepData;
	}
}
