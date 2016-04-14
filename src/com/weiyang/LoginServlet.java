 package com.weiyang;
import javax.servlet.http.*;
import java.io.*;

public class LoginServlet extends HttpServlet
{

    public void doGet(HttpServletRequest req, HttpServletResponse res)
    {

        try
        {
        	
        	//中文亂碼
        	res.setContentType("text/html;charset=utf-8");
            PrintWriter pw = res.getWriter();
            pw.println("<html>");
            pw.println("<body>");
            pw.println("<h1>登入介面</h1>");
            pw.println("<form action=LoginCl method=post>");
            pw.println("用戶名:<input type=text name=username><br>");
            pw.println("密碼:<input type=password name=password><br>");
            pw.println("<input type=checkbox name=keep value=2>兩周內不再重新登入<br>");
            pw.println("<input type=submit value=login>");
            pw.println("</form>");
            pw.println("</body>");
            pw.println("</html>");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    public void doPost(HttpServletRequest req, HttpServletResponse res){}


}