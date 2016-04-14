package com.weiyang;

import javax.servlet.http.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class WelcomeServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res) {

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
			// 分頁的功能
			// 每一頁顯示幾條紀錄
			int pageSize = 3;
			// (pageSize - 1) * pageNow + "," + pageSize);
			// 2-1*3
			// 當前頁面
			int pageNow = 1;

			// 共有幾條紀錄(查表得到的)
			int rowCount = 0;

			// 共有幾頁(計算得到的)
			int pageCount = 0;

			// 首先得到rowCount

			// 動態的接收pageNow
			String sPageNow = req.getParameter("pageNowOk");

			if (sPageNow != null) {
				pageNow = Integer.parseInt(sPageNow);
			}

			// 建立資料庫
			Class.forName("com.mysql.jdbc.Driver");

			// 得到連線

			ct = DriverManager.getConnection("jdbc:mysql://localhost:3306/db01", "root", "5566");

			ps = ct.prepareStatement("select count(*) from userData");

			rs = ps.executeQuery();

			if (rs.next()) {
				rowCount = rs.getInt(1);
			}

			// 計算pageCount
			if (rowCount % pageSize == 0) {
				pageCount = rowCount / pageSize;
			} else {
				pageCount = rowCount / pageSize + 1;
			}
			if (username != null) {

				pw.println("<body><center>");
				pw.println("<img src=imgs/1.jpg width=250 height=250><br>");
				pw.println("welcome,hello" + username + "歡迎回來");
			} else {

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

					if (!myname.equals("") && !mypassword.equals("")) {
						// 到logincl去驗證
						
						res.sendRedirect("LoginCl?username=" + myname + "&password=" + mypassword);
						return;

					}

				}
				//返回登入介面
				res.sendRedirect("LoginServlet");
				return;
			}
			// ps = ct.prepareStatement("select * from userData Limit ' " +
			// (pageSize - 1) * pageNow +" ', ' "+ pageSize+" ' ");
			ps = ct.prepareStatement("select * from userData  Limit " + (pageNow - 1) * pageSize + "," + pageSize);
			rs = ps.executeQuery();

			pw.println("<table border=1>");
			pw.println("<tr><th>ID</th><th>帳號</th><th>密碼</th><th>EMAIL</th><th>等級</th></tr>");
			while (rs.next()) {

				pw.println("<tr>");
				pw.print("<td>" + rs.getInt(1) + "</td>");
				pw.print("<td>" + rs.getString(2) + "</td>");
				pw.print("<td>" + rs.getString(3) + "</td>");
				pw.print("<td>" + rs.getString(4) + "</td>");
				pw.print("<td>" + rs.getInt(5) + "</td>");
				pw.println("</tr>");
			}

			pw.println("</body></center>");
			// 上一頁
			if (pageNow != 1) {
				pw.println("<a href=?pageNowOk=" + (pageNow - 1) + ">" + "&nbsp;" + "上一頁 " + "&nbsp;" + "</a>");
			}
			// 顯示超連結
			for (int i = pageNow; i <= pageNow + 4; i++) {
				pw.println("<a href=?pageNowOk=" + i + ">" + "&nbsp;" + i + "&nbsp;" + "</a>");
			}
			// 下一頁
			if (pageNow != pageCount) {
				pw.println("<a href=?pageNowOk=" + (pageNow + 1) + ">" + "&nbsp;" + " 下一頁" + "&nbsp;" + "</a>");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) {
	}

}