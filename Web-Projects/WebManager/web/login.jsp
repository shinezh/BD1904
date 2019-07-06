<%--
  Created by IntelliJ IDEA.
  User: shine
  Date: 2019/7/4
  Time: 18:27
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style type="text/css">

        body {
            background: url("https://i.loli.net/2019/07/04/5d1de50c9066189421.jpg") no-repeat fixed;
            /* set background tensile */
            background-size: 100% 100%;
            -moz-background-size: 100% 100%;
            margin: 0;
            padding: 0;
        }

        #content {
            background-color: rgba(255, 255, 255, 0.95);
            width: 420px;
            height: 300px;
            border: 1px solid #000000;
            border-radius: 6px;
            padding: 10px;
            margin-top: 15%;
            margin-left: auto;
            margin-right: auto;
            display: block;
        }

        .login-header {
            width: 100%;
            height: 48px;
            margin-bottom: 20px;
            border-bottom: 1px solid #dcdcdc;
            text-align: center;
        }

        .login-header span{
            height: 100%;
            line-height: 48px;
            vertical-align: middle;
            horiz-align: center;
            text-align: center;
            font-family: "等距更纱黑体 CL Medium";
            font-size: x-large;
        }



        .login-input-box {
            font-family: "等距更纱黑体 CL Medium";
            margin-top: 12px;
            width: 100%;
            margin-left: auto;
            margin-right: auto;
            display: inline-block;
        }

        .login-input-box input {
            width: 340px;
            height: 32px;
            margin-left: 18px;
            border: 1px solid #dcdcdc;
            border-radius: 4px;
            padding-left: 42px;
        }

        .login-input-box input:hover {
            border: 1px solid #ff7d0a;
        }

        .login-input-box input:after {
            border: 1px solid #ff7d0a;
        }

        .login-input-box .icon {
            width: 24px;
            height: 24px;
            margin: 6px 4px 6px 24px;
            background-color: #ff7d0a;
            display: inline-block;
            position: absolute;
            border-right: 1px solid #dcdcdc;
        }



        .login-button-box {
            margin-top: 12px;
            width: 100%;
            margin-left: auto;
            margin-right: auto;
            display: inline-block;
        }

        .login-button-box input {
            background-color: #ff7d0a;
            color: #ffffff;
            font-size: 16px;
            width: 386px;
            height: 40px;
            margin-left: 18px;
            border: 1px solid #ff7d0a;
            border-radius: 4px;
        }

        .login-button-box button:hover {
            background-color: #ee7204;
        }

        .login-button-box button:active {
            background-color: #ee7204;
        }


        .logon-box a {
            margin: 30px;
            color: #4a4744;
            font-size: 13px;
            text-decoration: none;
        }

        .logon-box a:hover {
            color: #ff7d0a;
        }

        .logon-box a:active {
            color: #ee7204;
        }
    </style>


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
        <div class="login-button-box">
            <input type="submit">

        </div>
    </form>



</div>



</body>
</html>
