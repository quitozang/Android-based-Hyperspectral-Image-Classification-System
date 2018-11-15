package com.zqt;

import sun.misc.BASE64Encoder;

import java.io.*;

import static java.lang.System.out;

public class ServletTest extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        req.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        String splitNum = req.getParameter("split");
        String password = req.getParameter("password");
        String split = "4";
        String result=null;

//        if(username.equals("hello") && password.equals("world")){  //判断后返回你想要返回的数据
//            result = "yes";
//        }else{
//            result="no";
//        }

        Algorithm.submitSPSFC(splitNum);   //run the script of the algotithm on Spark

        out.print(GetImageStr());

//        out.write(result);
        out.flush();
//        out.flush();
//        out.close();
    }


    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("GET");
        out.flush();
        out.close();
    }

    /**
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     * @return
     */
    public static String GetImageStr() {
        // String imgFile =
        // "d://test.jpg";//待处理的图片

        String imgFile = "/home/zangtt/IdeaProjects/MyWeb/data/paviaU.jpg";// //可以
        // String imgFile = "F://upload//软工大作业.ppt";// //可以
        // String imgFile="F://upload//爱的勇气.mp3";//可以
        // String imgFile="F://upload//upload.rar";//可以
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    } // /GetImageStr
}
