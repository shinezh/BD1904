<%@ page import="cn.shine.entity.Student" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: shine
  Date: 2019/7/6
  Time: 11:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html>
<head>
    <title>数据库信息</title>
    <link rel="stylesheet" href="<%= basePath+"css/page.css" %>" />
</head>

<%
    List<Student> list = (List<Student>) request.getAttribute("list");

%>

<body>
<div class="outside">
    <div id="div_table" style="display: block;width: 500px;">
        <table>
            <caption>STUDENT信息表</caption>
            <tr>
                <th>学号（ID）</th>
                <th>姓名</th>
                <th>年龄</th>
                <th>密码</th>
            </tr>
            <%
                if (list == null)
                    out.print("<script>alert('数据库无信息。');javascript:history.back(-1);</script>");
                else {
                    out.print("<script>alert('查询成功。');</script>");
                }

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
        <p></p>
        <input style="width: 500px" class="submit_button" type="button" name="return" value="返回主页面" onclick="javascript:history.back(-1);">
    </div>


</div>
</body>
</html>
