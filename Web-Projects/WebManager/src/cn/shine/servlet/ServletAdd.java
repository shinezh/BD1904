package cn.shine.servlet;

import cn.shine.entity.Student;
import cn.shine.framework.BeanFactory;
import cn.shine.service.StudentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ServletAdd", urlPatterns = "/addstu")
public class ServletAdd extends HttpServlet {
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


		//增加信息
		int addId = Integer.parseInt(request.getParameter("add_id"));
		String addName = (String) request.getParameter("add_name");
		int addAge = Integer.parseInt(request.getParameter("add_age"));
		String addPasswd = (String) request.getParameter("add_passwd");
		Student addStu = new Student(addId, addName, addAge, addPasswd);
		boolean addTag = stuService.addInfo(addStu);

		PrintWriter out = response.getWriter();
		if (addTag) {
			out.print("<script>alert('操作成功!');javascript:history.back(-1);</script>");
			out.close();
		} else {
			out.print("<script>alert('操作失败!');javascript:history.back(-1);</script>");
			out.close();
		}

	}
}
