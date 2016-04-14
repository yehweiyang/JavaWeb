package com.DB;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnDB {
	private Connection ct = null;

	public Connection getConn() {
		try {
			// 建立資料庫
			Class.forName("com.mysql.jdbc.Driver");
			// 得到連線
			ct = DriverManager.getConnection("jdbc:mysql://localhost:3306/db01", "root", "5566");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ct;
	}
}
