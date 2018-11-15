package com.zqt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "ServletLogIn")
public class ServletLogIn extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username"); //zhao
        String password = request.getParameter("password"); //123456
        String split = "4";
        String result = null;
        boolean res = false;
        try {
            res = DBOperation.query(username, password);        //database operation
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (res) {
            result = "yes";
        } else {
            result = "no";
        }

//        Algorithm.submitSPSFC(split);   //run the script of the algotithm on Spark
//        out.print(GetImageStr());

        out.write(result);
        out.flush();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("login");
        out.flush();
        out.close();
    }
}
