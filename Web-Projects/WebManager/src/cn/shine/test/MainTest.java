/**
 * @fileName: MainTest
 * @author: orange
 * @date: 2019/7/5 0:14
 * @description:
 */

package cn.shine.test;

import cn.shine.framework.TranscationManager;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class MainTest {
	@Test
	public void test() throws SQLException {
		Connection conn = TranscationManager.getConnection();
		System.out.println(conn);
		conn.close();
	}
}