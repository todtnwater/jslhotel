package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {
	
	// DB 접속 종료
	// connection이 되어 있으면 ~ 
	public static void closeDB(Connection con, 
								PreparedStatement ps, 
								ResultSet rs) {
		
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	// DB 접속
	public static Connection getConnection(){
		Connection con = null;
		
		//1. ORACLE DRIVER LOADING ojdbc6.jar
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Oracle Driver X");
		}
		
		//2. Database 接続
		String db_url      = "jdbc:oracle:thin:@XXX.XXX.XXX/XXXX/XXXXXX";
		String db_user     = "XXXXXX";
		String db_password = "XXXXXX";
		
		try {
			con = DriverManager.getConnection(db_url, db_user, db_password);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("설정 오류!");
		}
		
		return con;
	}	
		
	
	
	
}
