package cn.shine.servlet;

import cn.shine.entity.Student;
import cn.shine.framework.BeanFactory;
import cn.shine.service.StudentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.beancontext.BeanContext;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.http.HttpResponse;

@WebServlet(name = "ServletDelete",urlPatterns = "/delete")
public class ServletDelete extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		//初始化服务类对象
		StudentService stuService = BeanFactory.getStuService();



		//接受数据
		//删除学生
		int delId = Integer.parseInt(request.getParameter("del_stuid"));
		boolean delTag = stuService.delInfo(delId);

		PrintWriter out = response.getWriter();
		if (delTag) {
			out.print("<script>alert('操作成功!');javascript:history.back(-1);</script>");
			out.close();
		} else {
			out.print("<script>alert('操作失败!');javascript:history.back(-1);</script>");
			out.close();
		}



	}
}
