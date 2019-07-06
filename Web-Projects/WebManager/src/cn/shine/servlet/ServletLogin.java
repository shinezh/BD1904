package cn.shine.servlet;

import cn.shine.dao.StudentDao;
import cn.shine.dao.impl.StudentDaoImpl;
import cn.shine.entity.Student;
import cn.shine.framework.BeanFactory;
import cn.shine.service.StudentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "ServletLogin", urlPatterns = "/ServletLogin")
public class ServletLogin extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doGet(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		//接收数据
		String uname = request.getParameter("uname");
		String passwd = request.getParameter("passwd");
		//调用后台数据
		StudentDao dao = new StudentDaoImpl();



		//判断登陆
		boolean flag = dao.login(uname, passwd);
		if (flag) {
			System.out.println("Login success");
			//登陆成功
			//记录用户名
			request.setAttribute("name", uname);
			HttpSession session = request.getSession();
			session.setAttribute("name", uname);
			request.getRequestDispatcher("/WEB-INF/jsp/success.jsp").forward(request,response);

		} else {
			//登陆失败
			System.out.println("Login failed");
			PrintWriter out = response.getWriter();
			out.print("<script>alert('登录失败!');window.location.href='login.jsp'</script>");
			out.close();
		}
	}
}
