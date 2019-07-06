package cn.shine.servlet;

import cn.shine.framework.BeanFactory;
import cn.shine.service.StudentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ServletUpdate",urlPatterns = "/update")
public class ServletUpdate extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		//初始化服务类对象
		StudentService stuService = BeanFactory.getStuService();




		//修改信息
		int updateId =Integer.parseInt( request.getParameter("update_id"));
		String updateName = (String) request.getParameter("update_name");
		int updateAge=Integer.parseInt(request.getParameter("update_age"));
		String updatePasswd= (String) request.getParameter("update_passwd");
		boolean updateTag = stuService.updateInfo(updateId,updateName,updateAge,updatePasswd);
		PrintWriter out = response.getWriter();
		if (updateTag) {
			out.print("<script>alert('操作成功!');javascript:history.back(-1);</script>");
			out.close();
		} else {
			out.print("<script>alert('操作失败!');javascript:history.back(-1);</script>");
			out.close();
		}


	}
}
