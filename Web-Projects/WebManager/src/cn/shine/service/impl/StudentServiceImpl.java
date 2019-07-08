/**
 * @fileName: StudentServiceImpl
 * @author: orange
 * @date: 2019/7/4 17:25
 * @description: 服务接口类
 * @version: 1.0
 */

package cn.shine.service.impl;

import cn.shine.entity.Student;
import cn.shine.framework.TranscationManager;
import cn.shine.service.StudentService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentServiceImpl implements StudentService {

	private PreparedStatement pst = null;
	private ResultSet rs = null;
	private Connection connection = TranscationManager.getConnection();

	@Override
	public List<Student> getAll() {
		List<Student> list = new ArrayList<>();
		try {
			String sql = "select * from student";
			pst = connection.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				Student stu = new Student();
				stu.setSid(rs.getInt("sid"));
				stu.setSname(rs.getString("sname"));
				stu.setAge(rs.getInt("age"));
				stu.setPasswd(rs.getString("passwd"));
				list.add(stu);
			}
			System.out.println("查询成功！");
			return list;
		} catch (Exception e) {
			throw new RuntimeException("查询异常" + e.getMessage());
		} finally {
			//释放资源
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e) {
				throw new RuntimeException("释放资源异常" + e.getMessage());
			}
		}
	}

	@Override
	public boolean delInfo(int sid) {
		System.out.println("进入删除方法");
		String sql = "DELETE from student where sid=?";
		try {
			pst = connection.prepareStatement(sql);
			pst.setInt(1, sid);
			int num = pst.executeUpdate();
			if (num != 0) {
				System.out.println("删除成功");
				return true;
			} else {
				System.out.println("删除失败");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateInfo(int sid, String sname, int age, String passwd) {
		String sql = "UPDATE student SET sname=?,age=?,passwd=? where sid=?";
		try {
			pst = connection.prepareStatement(sql);
			pst.setString(1, sname);
			pst.setInt(2, age);
			pst.setString(3, passwd);
			pst.setInt(4, sid);
			int num = pst.executeUpdate();
			if (num != 0) {
				System.out.println("更新成功");
				return true;
			} else {
				System.out.println("更新失败");
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			//释放资源
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e) {
				throw new RuntimeException("释放资源异常" + e.getMessage());
			}
		}
	}

	@Override
	public boolean addInfo(Student student) {
		String sql = "INSERT INTO student(sid,sname,age,passwd) values (?,?,?,?)";
		int id = student.getSid();
		Student stu = null;
		stu = selById(id);
		if (stu != null) {
			System.out.println("主键重复，请更改数据后重新更新。");
			return false;
		}
		try {
			pst = connection.prepareStatement(sql);
			pst.setInt(1, student.getSid());
			pst.setString(2, student.getSname());
			pst.setInt(3, student.getAge());
			pst.setString(4, student.getPasswd());

			int num = pst.executeUpdate();
			if (num != 0) {
				System.out.println("增加成功");
				return true;

			} else {
				System.out.println("增加失败");
				return false;
			}
		} catch (Exception e) {
			throw new RuntimeException("增加信息异常" + e.getMessage());
		} finally {
			//释放资源
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e) {
				throw new RuntimeException("释放资源异常" + e.getMessage());
			}
		}
	}

	@Override
	public Student selById(int sid) {
		String sql= "select * from student where id =?";
		try{
			pst =connection.prepareStatement(sql);
			pst.setInt(1,sid);
			rs= pst.executeQuery();
			Student stu = new Student();
			if (rs==null){
				System.out.println("数据表中无此ID");
				return null;
			}
			while (rs.next()){
				stu.setSid(rs.getInt("sid"));
				stu.setSname(rs.getString("sname"));
				stu.setAge(rs.getInt("age"));
				stu.setPasswd(rs.getString("passwd"));
			}
			return stu;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
		finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e) {
				throw new RuntimeException("释放资源异常" + e.getMessage());
			}
		}

	}

	@Override
	public boolean login(String uname,String pass) {
		boolean tag = false;
		String sql = "select passwd from student where sname=? ";
		try {
			pst = connection.prepareStatement(sql);
			pst.setString(1,uname);
			rs = pst.executeQuery();
			while (rs.next()){
				if (rs.getString("passwd").equals(pass)){
					tag = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		TranscationManager.releaseAll();
		try {
			if (rs != null) {
				rs.close();
			}
			if (pst != null) {
				pst.close();
			}
		} catch (Exception e) {
			throw new RuntimeException("释放资源异常" + e.getMessage());
		}
		return tag;
	}
}