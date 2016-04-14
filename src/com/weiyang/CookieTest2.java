package com.weiyang;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieTest2 extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse res) {

		res.setContentType("text/html;charset=utf-8");
		PrintWriter pw;
		try {
			pw = res.getWriter();
			// �q�Ȥ�ݱo��Ҧ�cookie�T��
			Cookie[] allCookies = req.getCookies();

			int i = 0;

			// �p�Gcookie������
			if (allCookies != null) {

				// �q�}�C�����X
				for (i = 0; i < allCookies.length; i++) {
					// �̦����X
					Cookie temp = allCookies[i];
					if (temp.getName().equals("color1")) {
						// �o��cookie����
						String val = temp.getValue();

						pw.println("color1=" + val);
						break;
					}
				}
				if (allCookies.length == i) {
					pw.println("cookie�L��");
				} 
			}
			else {
				pw.println("���s�bcolor�o��cookie�άO�L���F");
			}

			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
