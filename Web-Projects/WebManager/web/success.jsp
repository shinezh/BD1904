<%@ page import="cn.shine.entity.Student" %>
<%@ page import="java.util.List" %>
<%@ page import="cn.shine.service.StudentService" %><%--
  Created by IntelliJ IDEA.
  User: shine
  Date: 2019/7/1
  Time: 17:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登陆成功页面</title>
    <style type="text/css">
        body {
            text-align: center;
        }

        .divall {
            padding: 0;
            width: 600px;
            margin: 0 auto;
        }

        #div_table {
            display: none;
        }

        p {
            height: 10px;
        }

        table {
            border: 1px;
            width: 100%;
            margin: 0 auto;
            text-align: center;
            border-collapse: collapse;
            font-family: Futura, Arial, sans-serif;

        }

        h2 {
            font-family: "Microsoft YaHei UI";
        }

        caption {
            font-size: larger;
            margin: 1em auto;
        }

        th,
        td {
            padding: 1em;
        }

        th {
            background: #555;
            /* border: 1px solid #777; */
            color: #fff;
        }

        tbody tr:nth-child(odd) {
            background: #ccc;
        }


        .trans input {
            width: 300px;
            height: 35px;
            margin: 0;
            padding: 0px;
        }

        .table2 {
            width: 300px;
            margin: 0 auto;
            text-align: center;
            border-collapse: collapse;
            font-family: Futura, Arial, sans-serif;
        }


        .table2 th,
        td {
            padding: 1em;
        }

        .table2 tr td {
            padding: 1em;
        }

        .table2 tr td input {

            width: 45px;
            padding: 0px;

        }

        .submit_button {
            height: 50px;
            background: #e3e3e3;
            border: 1px solid #bbb;
            border-radius: 3px;
            -webkit-box-shadow: inset 0 0 1px 1px #f6f6f6;
            box-shadow: inset 0 0 1px 1px #f6f6f6;
            color: #333;
            font: bold 12px/1 "helvetica neue", helvetica, arial, sans-serif;
            padding: 8px 0 9px;
            text-align: center;
            text-shadow: 0 1px 0 #fff;
            width: 150px;
        }

        .submit_button:hover {
            background: #d9d9d9;
            -webkit-box-shadow: inset 0 0 1px 1px #eaeaea;
            box-shadow: inset 0 0 1px 1px #eaeaea;
            color: #222;
            cursor: pointer;
        }

        .submit_button:active {
            background: #d0d0d0;
            -webkit-box-shadow: inset 0 0 1px 1px #e3e3e3;
            box-shadow: inset 0 0 1px 1px #e3e3e3;
            color: #000;
        }

        #add_div {
            display: none;
        }

        #update_div {
            display: none;
        }

        #del_div {
            display: none;
        }
    </style>


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

<%
    List<Student> list = (List<Student>) request.getAttribute("list");
    StudentService stuService = (StudentService) request.getAttribute("stuService");
    session.setAttribute("studentService",stuService);
%>

<body>
<h2 style="text-align: center">欢迎 ： <%=session.getAttribute("name")%>
</h2>

<div class="divall">
    <div id="div_table">
        <table>
            <caption>STUDENT信息表</caption>
            <tr>
                <th>学号（ID）</th>
                <th>姓名</th>
                <th>年龄</th>
                <th>密码</th>
            </tr>
            <%
                for (int i = 0; i < list.size(); i++) {
            %>
            <tr>
                <td><%=list.get(i).getSid()%>
                </td>
                <td><%=list.get(i).getSname()%>
                </td>
                <td><%=list.get(i).getAge()%>
                </td>
                <td><%=list.get(i).getPasswd()%>
                </td>
            </tr>
            <%}%>
        </table>
    </div>
    <p style="height: 20px"></p>
    <div class="trans">
        <div class="getall">
            <input class="submit_button" type="submit" name="getAll_submit" value="查询数据库所有学生信息"
                   onclick="display('div_table')">
            <p style="height: 10px"></p>
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
               onclick="display('update_div')">
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
    </div>
</div>


</body>
</html>
