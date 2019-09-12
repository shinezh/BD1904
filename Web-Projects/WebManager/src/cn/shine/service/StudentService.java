/**
 * @fileName: StudentService
 * @author: orange
 * @date: 2019/7/3 9:48
 * @description:
 * @version: 1.0
 */

package cn.shine.service;


import cn.shine.entity.Student;

import java.util.List;

public interface StudentService {
	/**
	 * 获取所有表元素信息
	 * @return
	 */
	List<Student> getAll();

	/**
	 * 通过id删除学生
	 * @param sid 要删除的id
	 * @return 是否成功
	 */
	boolean delInfo(int sid);

	/**
	 * 更改信息
	 */
	boolean updateInfo(int sid, String sname, int age, String passwd);

	/**
	 * 增加信息
	 */
	boolean addInfo(Student stu);

	Student selById(int sid);

	boolean login(String uname,String pass);
}