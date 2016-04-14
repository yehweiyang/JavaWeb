//這是我的第一個servlet 使用時限servlet介面的方式來開發

package com.weiyang;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieTest1 extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		
		res.setContentType("text/html;charset=utf-8");
		try {
			PrintWriter pw = res.getWriter();
			Cookie myCookie = new Cookie("color1", "red");
			myCookie.setMaxAge(60);
			
			res.addCookie(myCookie);
			
			pw.println("已經建立cookie");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}

    
    }