package com.tian.web.adv;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * 获取用户提交信息
 */
public class SubInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static final Logger LOGGER = Logger.getLogger(SubInfo.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SubInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		String gg_content =req.getParameter("gg_content");
		String gg_user =req.getParameter("gg_user");
		String gg_url =req.getParameter("gg_url");
		String gg_match =req.getParameter("gg_match");
		//插入数据库
		
		System.out.println(gg_content);
		
		PrintWriter out = resp.getWriter();// 获取输出流
		String message = null;

	}

}
