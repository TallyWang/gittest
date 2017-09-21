package com.infotrends.filter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.infotrends.util.Util;

public class SystemListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        String robot_DB_Driver = context.getInitParameter("Robot_DB_Driver");
        String robot_DB_URL = context.getInitParameter("Robot_DB_URL");
        String robot_DB_USER = context.getInitParameter("Robot_DB_USER");
        String robot_DB_PASS = context.getInitParameter("Robot_DB_PASS");
        String projectPath = context.getInitParameter("ProjectPath");
        
        Util.setRobot_DB_Driver(robot_DB_Driver);
        Util.setRobot_DB_URL(robot_DB_URL);
        Util.setRobot_DB_USER(robot_DB_USER);
        Util.setRobot_DB_PASS(robot_DB_PASS);
        Util.setProjectPath(projectPath);
	}


}
