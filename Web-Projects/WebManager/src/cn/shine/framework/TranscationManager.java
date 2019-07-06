/**
 * @fileName: TranscationManager
 * @author: orange
 * @date: 2019/7/3 9:52
 * @description: 事务管理类
 * @version: 1.0
 */

package cn.shine.framework;

import java.sql.Connection;
import java.sql.SQLException;

public class TranscationManager {

	private static MyDataSource dataSource = new MyDataSource();
	private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();


	public static Connection getConnection() {
		Connection conn = threadLocal.get();
		if (conn == null) {
			conn = dataSource.getConnection();
			threadLocal.set(conn);
		}
		return conn;
	}

	/**
	 * 开启事务
	 * 将自动提交设置为false
	 */
	public static void start() {
		Connection conn = threadLocal.get();
		if (conn == null) {
			conn = dataSource.getConnection();
			threadLocal.set(conn);
		}
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			throw new RuntimeException("事务开启失败" + e.getMessage());
		}

	}

	/**
	 * 提交事务
	 */
	public static void commitTrans() {
		Connection conn = threadLocal.get();
		if (conn == null) {
			conn = dataSource.getConnection();
			threadLocal.set(conn);
		} else {
			try {
				conn.commit();
				conn.setAutoCommit(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 回滚事务
	 */
	public static void rollBack() {
		Connection conn = threadLocal.get();
		if (conn == null) {
			conn = dataSource.getConnection();
			threadLocal.set(conn);
		} else {
			try {
				conn.rollback();
				conn.setAutoCommit(true);
			} catch (Exception e) {
				throw new RuntimeException("回滚事务失败" + e.getMessage());
			}
		}

	}

	public static void releaseAll() {
		Connection conn = threadLocal.get();
		if (conn != null) {
			try {
				conn.close();
				threadLocal.remove();
			} catch (SQLException e) {
				throw new RuntimeException("释放资源异常，无法正常关闭连接" + e.getMessage());
			}
		}
	}
}