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
			// �������\��
			// �C�@����ܴX������
			int pageSize = 3;
			// (pageSize - 1) * pageNow + "," + pageSize);
			// 2-1*3
			// ��e����
			int pageNow = 1;

			// �@���X������(�d��o�쪺)
			int rowCount = 0;

			// �@���X��(�p��o�쪺)
			int pageCount = 0;

			// �����o��rowCount

			// �ʺA������pageNow
			String sPageNow = req.getParameter("pageNowOk");

			if (sPageNow != null) {
				pageNow = Integer.parseInt(sPageNow);
			}

			// �إ߸�Ʈw
			Class.forName("com.mysql.jdbc.Driver");

			// �o��s�u

			ct = DriverManager.getConnection("jdbc:mysql://localhost:3306/db01", "root", "5566");

			ps = ct.prepareStatement("select count(*) from userData");

			rs = ps.executeQuery();

			if (rs.next()) {
				rowCount = rs.getInt(1);
			}

			// �p��pageCount
			if (rowCount % pageSize == 0) {
				pageCount = rowCount / pageSize;
			} else {
				pageCount = rowCount / pageSize + 1;
			}
			if (username != null) {

				pw.println("<body><center>");
				pw.println("<img src=imgs/1.jpg width=250 height=250><br>");
				pw.println("welcome,hello" + username + "�w��^��");
			} else {

				// �p�Gsession�S����T �A�ݬ�cookie���S���T��

				// �q�Ȥ�ݱo��Ҧ�cookie�T��
				Cookie[] allCookies = req.getCookies();

				int i = 0;

				// �p�Gcookie������
				if (allCookies != null) {

					// �q�}�C�����X
					for (i = 0; i < allCookies.length; i++) {
						// �̦����X
						Cookie temp = allCookies[i];
						if (temp.getName().equals("myname")) {
							// �o��cookie����
							myname = temp.getValue();
						} else if (temp.getName().equals("mypassword")) {
							mypassword = temp.getValue();
						}
					}

					if (!myname.equals("") && !mypassword.equals("")) {
						// ��logincl�h����
						
						res.sendRedirect("LoginCl?username=" + myname + "&password=" + mypassword);
						return;

					}

				}
				//��^�n�J����
				res.sendRedirect("LoginServlet");
				return;
			}
			// ps = ct.prepareStatement("select * from userData Limit ' " +
			// (pageSize - 1) * pageNow +" ', ' "+ pageSize+" ' ");
			ps = ct.prepareStatement("select * from userData  Limit " + (pageNow - 1) * pageSize + "," + pageSize);
			rs = ps.executeQuery();

			pw.println("<table border=1>");
			pw.println("<tr><th>ID</th><th>�b��</th><th>�K�X</th><th>EMAIL</th><th>����</th></tr>");
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
			// �W�@��
			if (pageNow != 1) {
				pw.println("<a href=?pageNowOk=" + (pageNow - 1) + ">" + "&nbsp;" + "�W�@�� " + "&nbsp;" + "</a>");
			}
			// ��ܶW�s��
			for (int i = pageNow; i <= pageNow + 4; i++) {
				pw.println("<a href=?pageNowOk=" + i + ">" + "&nbsp;" + i + "&nbsp;" + "</a>");
			}
			// �U�@��
			if (pageNow != pageCount) {
				pw.println("<a href=?pageNowOk=" + (pageNow + 1) + ">" + "&nbsp;" + " �U�@��" + "&nbsp;" + "</a>");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) {
	}

}