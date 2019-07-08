/**
 * @fileName: MainTest
 * @author: orange
 * @date: 2019/7/5 0:14
 * @description:
 */

package cn.shine.test;

import cn.shine.framework.TranscationManager;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainTest {
	@Test
	public void test() throws SQLException {
		Connection conn = TranscationManager.getConnection();
		System.out.println(conn);
		conn.close();
	}

	@Test
	public void test2() throws IOException {
		ResourceBundle bundle = ResourceBundle.getBundle("jdbc");
		String url = bundle.getString("jdbc.url");
		System.out.println(url);
	}
}