/**
 * @fileName: StudentDaoImpl
 * @author: orange
 * @date: 2019/7/1 17:14
 * @description:
 * @version: 1.0
 */

package cn.shine.dao.impl;


import cn.shine.dao.StudentDao;
import cn.shine.entity.Student;
import cn.shine.framework.TranscationManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements StudentDao {

	Connection connection = null;
	PreparedStatement pst = null;
	ResultSet rs = null;


	@Override
	public boolean login(String name, String pwd) {
		boolean tag = false;
		connection = TranscationManager.getConnection();
		String sql = "select passwd from student where sname=? ";
		try {
			pst = connection.prepareStatement(sql);
			pst.setString(1,name);
			rs = pst.executeQuery();
			while (rs.next()){
				if (rs.getString("passwd").equals(pwd)){
					tag = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		TranscationManager.releaseAll();
		return tag;
	}

	@Override
	public List<Student> getStudent() {
		List<Student> list = new ArrayList<>();
		connection = TranscationManager.getConnection();
		String sql = "select sid,sname,age,passwd from student";
		try {
			pst = connection.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				int sid = rs.getInt("sid");
				String sname = rs.getString("sname");
				int age = rs.getInt("age");
				String passwd = rs.getString("passwd");
				Student student = new Student(sid,sname,age,passwd);
				list.add(student);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		TranscationManager.releaseAll();
		try {
			if (rs!=null){
				rs.close();
			}
			if (pst!=null){
				pst.close();
			}
			if(connection!=null){
				connection.close();
			}
		} catch (Exception e) {
			throw new RuntimeException("关闭异常"+e.getMessage());
		}
		return list;
	}



}