/**
 * @fileName: StudentDao
 * @author: orange
 * @date: 2019/7/1 17:12
 * @description:
 * @version: 1.0
 */

package cn.shine.dao;


import cn.shine.entity.Student;

import java.util.List;

public interface StudentDao {
	/**
	 * 登陆
	 * @param name 用户名
	 * @param pwd 用户密码
	 * @return 登陆状态
	 */
	boolean login(String name, String pwd);

	List<Student> getStudent();



}