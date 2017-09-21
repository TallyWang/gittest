package com.infotrends.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Util {
	private static String Robot_DB_Driver;
	private static String Robot_DB_URL;
	private static String Robot_DB_USER;
	private static String Robot_DB_PASS;
	private static String ProjectPath;
	private static int testnext;
	
	public static String getRobot_DB_Driver() {
		return Robot_DB_Driver;
	}
	public static void setRobot_DB_Driver(String robot_DB_Driver) {
		Robot_DB_Driver = robot_DB_Driver;
	}
	public static String getRobot_DB_URL() {
		return Robot_DB_URL;
	}
	public static void setRobot_DB_URL(String robot_DB_URL) {
		Robot_DB_URL = robot_DB_URL;
	}
	public static String getRobot_DB_USER() {
		return Robot_DB_USER;
	}
	public static void setRobot_DB_USER(String robot_DB_USER) {
		Robot_DB_USER = robot_DB_USER;
	}
	public static String getRobot_DB_PASS() {
		return Robot_DB_PASS;
	}
	public static void setRobot_DB_PASS(String robot_DB_PASS) {
		Robot_DB_PASS = robot_DB_PASS;
	}
	public static String getProjectPath() {
		return ProjectPath;
	}
	public static void setProjectPath(String projectPath) {
		ProjectPath = projectPath;
	}
	
	public static int getTestnext() {
		return testnext;
	}
	public static void setTestnext(int testnext) {
		Util.testnext = testnext;
	}
	public static JsonObject getGJsonObject(String aMsg){
		JsonParser jsonParser = new JsonParser(); 
		JsonObject msgJson = jsonParser.parse(aMsg).getAsJsonObject();
		return msgJson;
	}
	public static JsonArray getGJsonArray(String aMsg){
		JsonParser jsonParser = new JsonParser(); 
		JsonArray msgJson = jsonParser.parse(aMsg).getAsJsonArray();
		return msgJson;
	}
	public static String getGString(JsonObject aObj, String aKey){
		return (aObj.get(aKey) != null && !(aObj.get(aKey)instanceof JsonNull))?aObj.get(aKey).getAsString():null;
	}
}