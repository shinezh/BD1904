<%--
  Created by IntelliJ IDEA.
  User: shine
  Date: 2019/7/4
  Time: 18:27
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    session.invalidate();
%>


<html>
<head>

    <link rel="stylesheet" href="<%= basePath+"css/login.css" %>"/>

    <title>登陆界面</title>
</head>
<body>

<div id="content">
    <div class="login-header">
        <span class="login-header-span">学生数据库管理系统</span>
    </div>
    <form action="/ServletLogin" method="get">
        <div class="login-input-box">
            <span>用户</span>
            <input type="text" placeholder="请输入用户姓名" name="uname">
        </div>
        <div class="login-input-box">
            <span>密码</span>
            <input type="password" placeholder="请输入密码" name="passwd">
        </div>
        <p></p>
        <div class="login-button-box">
            <input type="submit">

        </div>
    </form>


</div>


</body>
</html>
