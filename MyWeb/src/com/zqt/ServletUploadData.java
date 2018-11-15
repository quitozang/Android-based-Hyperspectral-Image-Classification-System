package com.zqt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ServletUploadData")
public class ServletUploadData extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        String splitNum = request.getParameter("split");
        String password = request.getParameter("password");
        String dataName = "4";
        String result=null;

//        if(username.equals("hello") && password.equals("world")){  //判断后返回你想要返回的数据
//            result = "yes";
//        }else{
//            result="no";
//        }

        String res = Algorithm.uploadData(splitNum);

        out.write(res);
        out.flush();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("upload data");
        out.flush();
        out.close();
    }
}
