//這是一個處理類別(處理users表) <-- -->操作UserBean
package com.DB;

import java.sql.*;
import java.util.ArrayList;

import javax.servlet.http.Cookie;

public class UserBeanCl {

	// 業務邏輯
	private Connection ct = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private ArrayList<UserBean> al = null;
	public int pageCount = 0;

	// 回傳pageCount
	public int fPageCount() {
		return this.pageCount;
	}

	// 分頁顯示
	public ArrayList getResultByPage(int pageNow, int pageSize) {

		try {
			al = new ArrayList<UserBean>();

			int rowCount = 0; // 共有幾條紀錄(查表得到的)

			; // 共有幾頁(計算得到的)

			ConnDB cd = new ConnDB();
			ct = cd.getConn();

			ps = ct.prepareStatement("select count(*) from userdata");

			rs = ps.executeQuery();

			if (rs.next()) {
				rowCount = rs.getInt(1);
			}

			// 計算pageCount
			if (rowCount % pageSize == 0) {
				pageCount = rowCount / pageSize;

			} else {
				pageCount = rowCount / pageSize + 1;

			}

			// ps = ct.prepareStatement("select * from userData Limit ' " +
			// (pageSize - 1) * pageNow +" ', ' "+ pageSize+" ' ");
			ps = ct.prepareStatement("select * from userData  Limit " + (pageNow - 1) * pageSize + "," + pageSize);
			rs = ps.executeQuery();

			// 將結果封裝到UserBean裡面
			while (rs.next()) {
				UserBean ub = new UserBean();
				ub.setUserId(rs.getInt(1));
				ub.setUsername(rs.getString(2));
				ub.setPassword(rs.getString(3));
				ub.setEmail(rs.getString(4));
				ub.setGrade(rs.getInt(5));
				al.add(ub);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.close();
		}

		return al;

	}

	// 驗證使用者
	public boolean checkUser(String u, String p) {
		boolean b = false;
		try {

			ConnDB cd = new ConnDB();
			ct = cd.getConn();
			ps = ct.prepareStatement("select password from userData where username=? Limit 1");
			ps.setString(1, u);
			rs = ps.executeQuery();

			if (rs.next()) {
				String dbPassword = rs.getString(1);
				if (dbPassword.equals(p)) {
					b = true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			this.close();
		}
		return b;
	}

	// 關閉連線
	public void close() {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (ps != null) {
				ps.close();
				ps = null;
			}

			if (ct != null) {
				ct.close();
				ct = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
