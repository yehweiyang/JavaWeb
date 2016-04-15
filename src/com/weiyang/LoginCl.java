package com.weiyang;

import javax.servlet.http.*;

import com.DB.ConnDB;
import com.DB.UserBeanCl;

import java.io.*;
import java.sql.*;

public class LoginCl extends HttpServlet {

	BufferedReader br = null;
	BufferedWriter bw = null;
	FileWriter fw = null;

	// 覆寫init函數
	@Override
	public void init() {

		try {

			// 增加網頁訪問次數
			// 建立一FileReader
			FileReader f = new FileReader("C:\\Users\\wei-yang\\workspace\\JavaWeb\\mycounter.txt");

			br = new BufferedReader(f);

			String numVal = br.readLine();

			System.out.println("numVal=" + numVal);

			this.getServletContext().setAttribute("times", numVal);

			int times = 0;

			System.out.println("init 被 呼叫");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	// 覆寫detroy函數
	public void destroy() {
		try {

			// 將新的次數寫入

			fw = new FileWriter("C:\\Users\\wei-yang\\workspace\\JavaWeb\\mycounter.txt");
			bw = new BufferedWriter(fw);
			bw.write(this.getServletContext().getAttribute("times").toString());

			System.out.println("destroy被呼叫");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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

			ConnDB cb = new ConnDB();
			ct = cb.getConn();

			UserBeanCl ubc = new UserBeanCl();

			if (ubc.checkUser(username, password)) {

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

				String _times =this.getServletContext().getAttribute("times").toString() ;
				int times_ = Integer.parseInt(_times)+1;
				this.getServletContext().setAttribute("times", times_);

				// 寫道你要到的Servlet的那個url
				res.sendRedirect("WelcomeServlet");

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