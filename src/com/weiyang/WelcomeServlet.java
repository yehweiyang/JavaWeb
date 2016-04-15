package com.weiyang;

import javax.servlet.http.*;

import com.DB.UserBean;
import com.DB.UserBeanCl;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class WelcomeServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		BufferedReader br = null;
		BufferedWriter bw = null;
		FileWriter fw = null;
		Connection ct = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String myname = null;
			String mypassword = null;
			HttpSession hs = req.getSession(true);
			String username = (String) hs.getAttribute("username");
			res.setContentType("text/html;charset=utf-8");
			PrintWriter pw = res.getWriter();

			pw.println("<body><center>");
			pw.println("<table border=1>");
			pw.println("<img src=imgs/1.jpg width=250 height=250><br>");
			pw.println("welcome,hello" + username + "歡迎回來");

			// 分頁的功能
			// 每一頁顯示幾條紀錄
			int pageSize = 5;
			// (pageSize - 1) * pageNow + "," + pageSize);
			// 2-1*3
			// 當前頁面
			int pageNow = 1;

			// 首先得到rowCount

			// 動態的接收pageNow
			String sPageNow = req.getParameter("pageNowOk");

			if (sPageNow != null) {
				pageNow = Integer.parseInt(sPageNow);
			}

			UserBeanCl ubs = new UserBeanCl();
			ArrayList al = ubs.getResultByPage(pageNow, pageSize);

			pw.println("<tr><th>ID</th><th>帳號</th><th>密碼</th><th>EMAIL</th><th>等級</th></tr>");

			for (int i = 0; i < al.size(); i++) {

				UserBean ub = (UserBean) al.get(i);

				pw.println("<tr>");
				pw.print("<td>" + ub.getUserId() + "</td>");
				pw.print("<td>" + ub.getUsername() + "</td>");
				pw.print("<td>" + ub.getPassword() + "</td>");
				pw.print("<td>" + ub.getEmail() + "</td>");
				pw.print("<td>" + ub.getGrade() + "</td>");
				pw.println("</tr>");
			}

			if (username == null) {

				// 如果session沒有資訊 再看看cookie有沒有訊息

				// 從客戶端得到所有cookie訊息
				Cookie[] allCookies = req.getCookies();

				int i = 0;
				// 如果cookie不為空
				if (allCookies != null) {
					// 從陣列中取出
					for (i = 0; i < allCookies.length; i++) {
						// 依次取出
						Cookie temp = allCookies[i];
						if (temp.getName().equals("myname")) {

							// 得到cookie的值
							myname = temp.getValue();
						} else if (temp.getName().equals("mypassword")) {

							mypassword = temp.getValue();
						}

					}

					/*--------------*/
					if (!myname.equals("") && !mypassword.equals("")) {
						// 到logincl去驗證

						res.sendRedirect("LoginCl?username=" + myname + "&password=" + mypassword);
						return;

					}

				}
				System.out.println("這邊影嗎");
				// 返回登入介面
				res.sendRedirect("LoginServlet");
				return;
			}

			pw.println("<br>");

			pw.println("</center></body>");
			pw.println("</table>");
			// 上一頁
			if (pageNow != 1) {
				pw.println("<a href=?pageNowOk=" + (pageNow - 1) + ">" + "&nbsp;" + "上一頁 " + "&nbsp;" + "</a>");
			}
			// 顯示超連結
			for (int i = pageNow; i <= pageNow + 4; i++) {
				pw.println("<a href=?pageNowOk=" + i + ">" + "&nbsp;" + i + "&nbsp;" + "</a>");
			}
			// 下一頁
			if (pageNow != ubs.fPageCount()) {
				pw.println("<a href=?pageNowOk=" + (pageNow + 1) + ">" + "&nbsp;" + " 下一頁" + "&nbsp;" + "</a>");
			}
			// 返回登入畫面
			pw.println("<a href=/JavaWeb/LoginServlet?check=true>" + "登出" + "</a>");
			pw.println("被訪問了" + this.getServletContext().getAttribute("times") + "次<br>");
			pw.println("你的IP位置是  :" + req.getRemoteAddr()+"<br>");
			pw.println("你的瀏覽器主機是  :" + req.getRemoteHost()+"<br>");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) {
	}

}