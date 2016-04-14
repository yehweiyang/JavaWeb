package com.weiyang;

import javax.servlet.http.*;
import javax.websocket.Session;

import java.io.*;

public class LoginServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res) {

		res.setContentType("text/html/charset=utf-8");

		try {

			String check = req.getParameter("check");

			HttpSession hs = req.getSession(true);

			if (Boolean.parseBoolean(check)) {
				hs.removeAttribute("username");
			}
			if (hs.getAttribute("username") != null) {
				res.sendRedirect("WelcomeServlet");
			}

			res.setContentType("text/html;charset=utf-8");
			PrintWriter pw = res.getWriter();
			pw.println("<html>");
			pw.println("<body>");
			pw.println("<h1>登入介面</h1>");
			pw.println("<form action=LoginCl method=post>");
			pw.println("用戶名:<input type=text name=username><br>");
			pw.println("密碼:<input type=password name=password><br>");
			pw.println("<input type=checkbox name=keep value=2>兩周內不再重新登入<br>");
			pw.println("<input type=submit value=確定>");
			pw.println("</form>");
			pw.println("</body>");
			pw.println("</html>");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) {
	}

}