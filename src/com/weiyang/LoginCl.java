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

			// �����Τ���M�K�X
			String username = req.getParameter("username");
			String password = req.getParameter("password");

			System.out.println("�ݷ|�X�{�X��");
			// �إ߸�Ʈw
			Class.forName("com.mysql.jdbc.Driver");

			// �o��s�u

			ct = DriverManager.getConnection("jdbc:mysql://localhost:3306/db01", "root", "5566");

			// �إ�Statement
			sm = ct.createStatement();

			// rs = sm.executeQuery(
			// "select * from userData where username=' " + username + " ' and
			// password='" + password + "'"+"LIMIT 1");
			rs = sm.executeQuery("select  * from userData  where username='" + username + "' LIMIT 1");

			if (rs.next()) {
				String dbPassword = rs.getString(3);
				if (dbPassword.equals(password)) {
					// �X�k
					String keep = req.getParameter("keep");
					if (keep != null) {

						// �N�b���M�K�X�O�s�b�Ȥ��
						Cookie myname = new Cookie("myname", username);
						Cookie mypassword = new Cookie("mypassword", password);

						// �]�m�ɶ�
						myname.setMaxAge(60 * 60 * 24 * 14);
						mypassword.setMaxAge(60 * 60 * 24 * 14);

						// �O�s��Ȥ��
						res.addCookie(myname);
						res.addCookie(mypassword);
					}
					HttpSession hs = req.getSession(true);
					hs.setAttribute("username", username);

					// �g�D�A�n�쪺Servlet������url
					res.sendRedirect("WelcomeServlet");
				}
			} else {

				// ���X�k
				System.out.println("������ ������");

				// �g�D�A�n�쪺Servlet������url
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