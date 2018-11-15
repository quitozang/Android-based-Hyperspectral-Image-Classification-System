<%--
  Created by IntelliJ IDEA.
  User: zangtt
  Date: 18-11-7
  Time: 下午9:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
  <form action="/MyWeb/A?username=hello&password=world" method="Post">
      用户名：<input type="text" value="请输入用户名" name="user" size="20px"><br>
      密码：<input type="password" value="请输入密码" name="pswd" size="20px"><br>

      <input type="submit" value="提交" size="10px">
  </form>
  </body>
</html>
