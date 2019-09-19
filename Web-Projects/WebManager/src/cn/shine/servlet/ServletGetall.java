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
import java.util.List;

@WebServlet(name = "ServletGetall", urlPatterns = "/getall")
public class ServletGetall extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		//查询所有学生信息
		StudentService stuService = BeanFactory.getStuService();
		PrintWriter out = response.getWriter();
		try{
			List<Student> list = stuService.getAll();

			//记录所有学生信息
			request.setAttribute("list", list);
			request.getRequestDispatcher("WEB-INF/jsp/database.jsp").forward(request,response);

		}catch (Exception e){
			out.print("<script>alert('查询失败!');window.location.href='login.jsp'</script>");
			out.close();
			throw new RuntimeException("查询数据异常");
		}



	}
}
