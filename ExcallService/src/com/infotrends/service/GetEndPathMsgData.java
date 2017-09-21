package com.infotrends.service;

import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.infotrends.dao.GetTableData;
import com.infotrends.util.Util;

public class GetEndPathMsgData {
	private static Logger mylogger = Logger.getLogger(GetEndPathMsgData.class.getName());
	
	public static JsonArray getEndPathMsgData(String aType, String aCommandname){
//		String DBDriver = "net.sourceforge.jtds.jdbc.Driver";
//		String DBUrl = "jdbc:jtds:sqlserver://192.168.10.42:1433;databaseName=RobotDemo";
//		String DBUser = "sa";
//		String DBPassword = "password";
		String DBDriver = Util.getRobot_DB_Driver();
		String DBUrl = Util.getRobot_DB_URL();
		String DBUser = Util.getRobot_DB_USER();
		String DBPassword = Util.getRobot_DB_PASS();
		String Sql = "Select * from ex_EndPathMsg where type='"+aType+"' and commandname='"+aCommandname+"'";

		mylogger.info("getEndPathMsgData - Sql: "+Sql);
		
		JsonArray EndPathMsgData = null;
		try {
			EndPathMsgData = GetTableData.getTableDataFromSql2O(DBDriver, Sql, DBUrl, DBUser, DBPassword);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EndPathMsgData;
	}
}
