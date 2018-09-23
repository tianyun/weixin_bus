package com.tian.web.adv;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 验证码验证
 * @author tianyun
 *
 */
public class CheckMsg {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		String validateC = (String) request.getSession().getAttribute("validateCode");
		String veryCode = request.getParameter("c");
		PrintWriter out = response.getWriter();
		if (veryCode == null || "".equals(veryCode)) {
			out.println("验证码为空");
		} else {
			if (validateC.equals(veryCode)) {
				out.println("验证码正确");
			} else {
				out.println("验证码错误");
			}
		}
		out.flush();
		out.close();
	}
}