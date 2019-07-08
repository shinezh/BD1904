
<%@ page import="cn.shine.entity.Student" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: shine
  Date: 2019/7/1
  Time: 17:00
  To change this template use File | Settings | File Templates.
--%>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%
    //防止直接输入success.jsp 若未登录则要求先登录
    String admin_name = (String) session.getValue("name");
    if (admin_name == null)
        out.print("<script>alert('请先登陆。');window.location.href='login.jsp';</script>");
%>
<html>

<head>
    <title>登陆成功页面</title>

    <link rel="stylesheet" href="<%= basePath+"css/page.css" %>" />

    <script type="text/javascript">
        function display(id) {
            let target = document.getElementById(id);
            if (target.style.display === "block") {
                target.style.display = "none";
            } else {
                target.style.display = "block";
            }
        }

    </script>
</head>


<body>
<h2 style="text-align: center">欢迎 ： <%=session.getAttribute("name")%>
</h2>

<div class="divall">

    <div class="trans">
        <div class="getall">
            <form method="get" action="/getall">
                <input class="submit_button" type="submit" name="getAll_submit" value="查询数据库所有学生信息">
                <p style="height: 10px"></p>
            </form>
        </div>
        <input class="submit_button" type="text" name="del_stuid_submit" value="根据学生ID删除信息"
               onclick="display('del_div')">
        <div class="delstu" id="del_div">
            <form action="/delete" method="get">
                <p style="height: 00px"></p>
                <input type="text" name="del_stuid" placeholder="请输入需要删除的学生学号（ID）">
                <p style="margin: 0"></p>
                <input style="background-color:floralwhite" class="submit_button" type="submit"
                       name="del_stuid_submit" value="确认删除">
            </form>
        </div>
        <p></p>
        <input class="submit_button" type="text" name="update_stuid_submit" value="根据学生ID修改信息"
               onclick=display("update_div") {
               }>
        <div class="updatestu" id="update_div">
            <p style="height: 00px"></p>
            <form action="/update" method="get">
                <table class="table2">
                    <tr>
                        <th>学号</th>
                        <th>姓名</th>
                        <th>年龄</th>
                        <th>密码</th>
                    </tr>
                    <tr>
                        <td><input type="text" name="update_id"></td>
                        <td><input type="text" name="update_name"></td>
                        <td><input type="text" name="update_age"></td>
                        <td><input type="text" name="update_passwd"></td>
                    </tr>
                </table>
                <input style="background-color:floralwhite" class="submit_button" type="submit" name="update_submit"
                       value="确认更新信息">
            </form>
        </div>
        <p></p>

        <input class="submit_button" type="text" name="update_stuid_submit" value="增加学生信息"
               onclick="display('add_div')">
        <div class="addstu" id="add_div">
            <p style="height: 00px"></p>
            <form action="/addstu" method="get" id="add_form">
                <table class="table2">
                    <tr>
                        <th>学号</th>
                        <th>姓名</th>
                        <th>年龄</th>
                        <th>密码</th>
                    </tr>
                    <tr>
                        <td><input type="text" name="add_id"></td>
                        <td><input type="text" name="add_name"></td>
                        <td><input type="text" name="add_age"></td>
                        <td><input type="text" name="add_passwd"></td>
                    </tr>
                </table>
                <input style="background-color:floralwhite" class="submit_button" type="submit" name="add_submit"
                       value="确认增加">
            </form>
        </div>
        <p></p>
        <input class="submit_button" type="button" name="exit_button" style="background-color: crimson;color: #f6f6f6" value="退出登录"
               onclick="window.location.href='logout.jsp';"%>
    </div>
</div>


</body>
</html>
