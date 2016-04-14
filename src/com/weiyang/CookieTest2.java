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
			// 從客戶端得到所有cookie訊息
			Cookie[] allCookies = req.getCookies();

			int i = 0;

			// 如果cookie不為空
			if (allCookies != null) {

				// 從陣列中取出
				for (i = 0; i < allCookies.length; i++) {
					// 依次取出
					Cookie temp = allCookies[i];
					if (temp.getName().equals("color1")) {
						// 得到cookie的值
						String val = temp.getValue();

						pw.println("color1=" + val);
						break;
					}
				}
				if (allCookies.length == i) {
					pw.println("cookie過期");
				} 
			}
			else {
				pw.println("不存在color這個cookie或是過期了");
			}

			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
