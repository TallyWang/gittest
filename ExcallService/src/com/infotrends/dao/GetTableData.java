package com.infotrends.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GetTableData {
	private static Logger mylogger = Logger.getLogger(GetTableData.class.getName());
	/**
	 * getTableDataFromSql2O
	 * 
	 * @param aDBDriver
	 * @param aSqlCommands
	 * @param aDBUrl
	 * @param aDBUser
	 * @param aPassword
	 * @return
	 * @throws Exception
	 */
	public static JsonArray getTableDataFromSql2O(String aDBDriver, String aSqlCommands, String aDBUrl, String aDBUser, String aPassword) throws Exception {
		try {
			Class.forName(aDBDriver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Sql2o sql2o = new Sql2o(aDBUrl, aDBUser, aPassword);

		JsonArray jsonarray = new JsonArray();
		
		List<Map<String, Object>> list = new ArrayList<>();
		try (Connection con = sql2o.open()){
			list = con.createQuery(aSqlCommands)
					.executeAndFetchTable().asList();
			
			for(Map<String, Object> map : list){
		    	JsonObject jsonobject = new JsonObject();
			    for(String key : map.keySet()){
			    	if(map.get(key)!=null){
			    		jsonobject.addProperty(key, map.get(key).toString());
			    	}
			    }
			    jsonarray.add(jsonobject);
		    }
		}
		return jsonarray;
	}
}
