 package com.weiyang;
import javax.servlet.http.*;
import java.io.*;

public class LoginServlet extends HttpServlet
{

    public void doGet(HttpServletRequest req, HttpServletResponse res)
    {

        try
        {
        	
        	//����ýX
        	res.setContentType("text/html;charset=utf-8");
            PrintWriter pw = res.getWriter();
            pw.println("<html>");
            pw.println("<body>");
            pw.println("<h1>�n�J����</h1>");
            pw.println("<form action=LoginCl method=post>");
            pw.println("�Τ�W:<input type=text name=username><br>");
            pw.println("�K�X:<input type=password name=password><br>");
            pw.println("<input type=checkbox name=keep value=2>��P�����A���s�n�J<br>");
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