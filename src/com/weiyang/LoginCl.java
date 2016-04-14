package com.weiyang;

import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class LoginCl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		Connection ct = null;
		Statement sm = null;
		ResultSet rs = null;
		try {

			// 接收用戶明和密碼
			String username = req.getParameter("username");
			String password = req.getParameter("password");

			System.out.println("看會出現幾次");
			// 建立資料庫
			Class.forName("com.mysql.jdbc.Driver");

			// 得到連線

			ct = DriverManager.getConnection("jdbc:mysql://localhost:3306/db01", "root", "5566");

			// 建立Statement
			sm = ct.createStatement();

			// rs = sm.executeQuery(
			// "select * from userData where username=' " + username + " ' and
			// password='" + password + "'"+"LIMIT 1");
			rs = sm.executeQuery("select  * from userData  where username='" + username + "' LIMIT 1");

			if (rs.next()) {
				String dbPassword = rs.getString(3);
				if (dbPassword.equals(password)) {
					// 合法
					String keep = req.getParameter("keep");
					if (keep != null) {

						// 將帳號和密碼保存在客戶端
						Cookie myname = new Cookie("myname", username);
						Cookie mypassword = new Cookie("mypassword", password);

						// 設置時間
						myname.setMaxAge(60 * 60 * 24 * 14);
						mypassword.setMaxAge(60 * 60 * 24 * 14);

						// 保存到客戶端
						res.addCookie(myname);
						res.addCookie(mypassword);
					}
					HttpSession hs = req.getSession(true);
					hs.setAttribute("username", username);

					// 寫道你要到的Servlet的那個url
					res.sendRedirect("WelcomeServlet");
				}
			} else {

				// 不合法
				System.out.println("有內鬼 停止交易");

				// 寫道你要到的Servlet的那個url
				res.sendRedirect("LoginServlet");
			}

		}

		catch (Exception ex) {
			ex.printStackTrace();
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}

				if (sm != null) {
					sm.close();
				}

				if (ct != null) {
					ct.close();
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		doGet(req, res);

	}

}