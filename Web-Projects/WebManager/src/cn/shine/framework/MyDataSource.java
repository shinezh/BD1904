/**
 * @fileName: MyDataSource
 * @author: orange
 * @date: 2019/7/3 18:32
 * @description: 线程池
 * @version: 1.0
 */

package cn.shine.framework;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MyDataSource implements DataSource {
	/*
	 * 使用静态块代码，初始化连接池，创建连接池的中最小链接数量连接，
	 * 创建linkedlist集合，将这些连接放入集合中
	 */

	private static LinkedList<Connection> linkedlist = new LinkedList<Connection>();
	/**
	 * 读取jdbc配置文件
	 */
	private static ResourceBundle bundle = ResourceBundle.getBundle("jdbc");
	private static String driver = bundle.getString("jdbc.driver");
	private static String url =bundle.getString("jdbc.url");
	private static String username = bundle.getString("jdbc.username");
	private static String password = bundle.getString("jdbc.password");

	/**
	 * 连接池初始大小
	 */
	private static int jdbcConnectionInitSize = 5;

	static {
		try {
			Class.forName(driver);
			//创建最小连接数个数据库连接对象以备使用
			for (int i = 0; i < jdbcConnectionInitSize; i++) {
				Connection conn = DriverManager.getConnection(url, username, password);
				//将创建好的数据库连接对象添加到Linkedlist集合中
				linkedlist.add(conn);
			}
			System.out.println("==========连接池初始化成功==========");
		} catch (Exception e) {
			throw new RuntimeException("初始化连接池异常" + e.getMessage());
		}
	}

	@Override
	public Connection getConnection() {
		if (linkedlist.size() > 0) {
			//从连接池中取出一个连接调用
			Connection conn = linkedlist.removeFirst();
			return (Connection) Proxy.newProxyInstance(conn.getClass().getClassLoader(), conn.getClass().getInterfaces(), new InvocationHandler() {
				@Override
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
					if ("close".equals(method.getName())) {
						linkedlist.add(conn);
						return null;
					} else {
						return method.invoke(conn, args);
					}
				}
			});
		} else {
			System.out.println("连接池创建失败,连接池已用完。");
		}
		return null;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return null;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {

	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {

	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}
}