package com.infotrends.service;

import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.infotrends.dao.GetTableData;
import com.infotrends.util.Util;

public class GetScenesPlanData {
	private static Logger mylogger = Logger.getLogger(GetScenesPlanData.class.getName());
	/**
	 * getScenesPlanData
	 * 
	 * @param aPlanName
	 * @return
	 */
	public static JsonArray getScenesPlanData(String aPlanName){
//		String DBDriver = "net.sourceforge.jtds.jdbc.Driver";
//		String DBUrl = "jdbc:jtds:sqlserver://192.168.10.42:1433;databaseName=RobotDemo";
//		String DBUser = "sa";
//		String DBPassword = "password";
		
		String DBDriver = Util.getRobot_DB_Driver();
		String DBUrl = Util.getRobot_DB_URL();
		String DBUser = Util.getRobot_DB_USER();
		String DBPassword = Util.getRobot_DB_PASS();
		
		String Sql = "Select * from ex_ScenesPlan where planname='"+aPlanName+"'";

		mylogger.info("getScenesPlanData - Sql: " + Sql);
		
		JsonArray ScenesPlanData = null;
		try {
			ScenesPlanData = GetTableData.getTableDataFromSql2O(DBDriver, Sql, DBUrl, DBUser, DBPassword);
			System.out.println(ScenesPlanData.toString());
			mylogger.info("ScenesPlanData - Sql: " + ScenesPlanData);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return ScenesPlanData;
	}
}
