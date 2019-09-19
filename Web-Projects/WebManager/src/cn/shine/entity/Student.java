/**
 * @fileName: Student
 * @author: orange
 * @date: 2019/6/24 14:21
 * @description: 学生实体类
 * @version: 1.0
 */

package cn.shine.entity;

import java.util.StringJoiner;

public class Student {
	private int sid;
	private String sname;
	private int age;
	private String passwd;

	public Student(int sid, String sname, int age) {
		this.sid = sid;
		this.sname = sname;
		this.age = age;
	}

	public Student(int sid, String sname, int age, String passwd) {
		this.sid = sid;
		this.sname = sname;
		this.age = age;
		this.passwd = passwd;
	}

	public Student() {
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Student.class.getSimpleName() + "[", "]").add("sid=" + sid).add("sname='" + sname + "'").add("age=" + age).add("passwd='" + passwd + "'").toString();
	}
}