package com.infotrends.service;

import java.util.Calendar;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.infotrends.dao.GetTableData;
import com.infotrends.util.Util;

public class GetStoreInfoData {
	private static Logger mylogger = Logger.getLogger(GetScenesPlanData.class.getName());
	
	/**
	 * @param storetype
	 * @param storearea
	 * @param storearea_reg
	 * @return
	 */
	public static JsonArray getStoreInfoData(String storetype, String storecity, String storearea_reg, String storename){
//		String DBDriver = "net.sourceforge.jtds.jdbc.Driver";
//		String DBUrl = "jdbc:jtds:sqlserver://192.168.10.42:1433;databaseName=RobotDemo";
//		String DBUser = "sa";
//		String DBPassword = "password";
		String DBDriver = Util.getRobot_DB_Driver();
		String DBUrl = Util.getRobot_DB_URL();
		String DBUser = Util.getRobot_DB_USER();
		String DBPassword = Util.getRobot_DB_PASS();	
		
		String sql="";
		sql = "SELECT STORETYPE from EX_STORE_INFO where STATUS='Y' and STORETYPE is not NULL GROUP BY STORETYPE";

		if (storetype!=""){
			//撈縣市
			sql = "SELECT STOREAREA from EX_STORE_INFO where STATUS='Y' and STORETYPE is not NULL and STOREAREA is not NULL and STORETYPE='"+ storetype +"' GROUP BY STOREAREA";		
		}
		
		if (storetype!="" && storecity!=""){
			//撈分區
			sql = "SELECT STOREAREA_REG from EX_STORE_INFO WHERE STATUS='Y' and STORETYPE is not NULL and STOREAREA is not NULL and STOREAREA_REG is not NULL and STORETYPE='"+ storetype +"' and STOREAREA='"+ storecity +"' GROUP BY STOREAREA_REG";	
		}
		
		if (storetype!="" && storecity!="" && storearea_reg!=""){
			//撈門市
			sql = "SELECT * from EX_STORE_INFO WHERE STATUS='Y' and STORETYPE='"+ storetype +"' and STOREAREA='"+ storecity +"' and STOREAREA_REG='"+ storearea_reg +"'";
		}
		
		if(storename!=""){
			//顯示門市詳細資訊
			sql= "select STORENAME, BUSINESSSTARTTIME, BUSINESSENDTIME, BUSINESSSATSTARTTIME, BUSINESSSATENDTIME, BUSINESSSUNSTARTTIME, BUSINESSSUNENDTIME, STOREADDRESS from EX_STORE_INFO where STORENAME='"+ storename +"'";
		}
		
		
	    mylogger.info("GetStoreInfoData -> getStoreInfoData -> sql_district: " + sql);
		   

		JsonArray storeInfoData = null;
		try {
			storeInfoData = GetTableData.getTableDataFromSql2O(DBDriver, sql, DBUrl, DBUser, DBPassword);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return storeInfoData;
	}
	
}
