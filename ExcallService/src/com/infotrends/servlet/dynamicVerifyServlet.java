package com.infotrends.servlet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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

public class dynamicVerifyServlet extends HttpServlet {
	
	private static Logger mylogger = Logger.getLogger(dynamicVerifyServlet.class.getName());
	
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
		
		//選擇問題
		List<String> questionsList = new ArrayList<>();
		questionsList.add("8");
		questionsList.add("4");
		questionsList.add("9");
		questionsList.add("11");
		questionsList.add("13");
		
		Map<String, String> questions = new HashMap<>();
		questions.put("8", "請問您的帳單寄送方式為何？");
		questions.put("4", "請問您自動轉帳是用什麼方式扣款的？");
		questions.put("9", "請問您最近一次的繳款方式？");
		questions.put("11", "請問您最近一期帳單繳費地點？");
		questions.put("13", "請問您最後一次儲值金額？");
		
		Random random = new Random();
		int randomInt = random.nextInt(5);
		String randomQuestionItem = questionsList.get(randomInt);
		String randomQuestion = questions.get(randomQuestionItem);
		
		JsonObject cacheJsonObject = CacheBean.XiaoiRobotCache.get(sessionId);
		cacheJsonObject.addProperty("dynamicverifyquestionitem", randomQuestionItem);
		
//		ResponseJsonObject.put("addcommandname", "8");
		ResponseJsonObject.put("varmsg", randomQuestion);
//		ResponseJsonObject.put("nextstep", "10003");
		printwriter.println(ResponseJsonObject.toString());
		return;
	}

}
