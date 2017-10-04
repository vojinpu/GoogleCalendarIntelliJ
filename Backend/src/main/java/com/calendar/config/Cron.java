package com.calendar.config;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class Cron extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Calendar cal = Calendar.getInstance();

		System.out.println("Cron job, current time: " + cal.getTime().toString() + "-------------------------------------------------------------------------------------------------------->>");

//		Queue queue = QueueFactory.getDefaultQueue();
//		queue.add(TaskOptions.Builder.withUrl("/api/v1/notification").param("key", "voja"));
//		queue.add(TaskOptions.Builder.withUrl("/api/v1/notification").param("key", "voja").method(TaskOptions.Method.GET));
//		queue.add(TaskOptions.Builder.withUrl("/api/v1/notification").param("delete", "voja").method(TaskOptions.Method.DELETE));

	}

}
