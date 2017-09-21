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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.infotrends.bean.CacheBean;
import com.infotrends.bean.BasicProblemInfoBean;
import com.infotrends.bean.ProblemBean;
import com.infotrends.bean.ContactPersonalInfoBean;
import com.infotrends.bean.DetailProblemFormOneBean;
import com.infotrends.bean.DetailProblemInfoBean;
import com.infotrends.util.Util;

public class checkpwServlet extends HttpServlet {

	private static Logger mylogger = Logger.getLogger(checkpwServlet.class
			.getName());

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
		String planName = jsonobject.getString("planname");

		// Do Service

		if (jsonobject.getString(planName+"checkpassword").equals("123123")) {
//			ResponseJsonObject.put("sessionId", sessionId);
//			ResponseJsonObject.put("nextfuction", "rule");
//			ResponseJsonObject.put("nextstep", "10005");
			printwriter.println(ResponseJsonObject.toString());
			return;
		} else {
			ResponseJsonObject.put("addcommandname", "wrong");
			ResponseJsonObject.put("nextfuction", "0");
			ResponseJsonObject.put("nextstep", "0");
//			ResponseJsonObject.put("msg", "轉接客服");
			printwriter.println(ResponseJsonObject.toString());
			return;
		}
	}
}
