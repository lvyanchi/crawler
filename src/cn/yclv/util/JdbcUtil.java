package cn.yclv.util;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.mysql.jdbc.Connection;

public class JdbcUtil {
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String url = "jdbc:mysql://localhost:3306/baihe?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true";
	private static final String username = "root";
	private static final String password = "yclv";
	
	public static Connection getConnection() {
	    Connection conn = null;
	    try {
	        Class.forName(driver); //classLoader,加载对应驱动
	        conn = (Connection) DriverManager.getConnection(url, username, password);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return conn;
	}
	
	
	/**
	 * 传入表名查询所有对象
	 * @param conn
	 * @param tableName
	 * @throws SQLException
	 */
	public static void queryAll(Connection conn, String tableName) throws SQLException {
		String sql = "select * from " + tableName;
		Statement stmt = conn.prepareStatement(sql);
		ResultSet resultSet = stmt.executeQuery(sql);
		while(resultSet.next()){
		}
	}
	
	
	public static void insertByStmt(Statement stmt, String sql) throws SQLException {
		stmt.executeUpdate(sql);
	}
	
	
	
}
