package com.zking.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;


public class JDBCUtils {

	//数据库操作类
	private QueryRunner runner = new QueryRunner(DBPool.getInstance().getDataSource());	
	/**
	 * 	更新数据--返回更新的数据条数
	 */
	public int updateObject(String sql, Object...params) {
		Connection conn = null;
		PreparedStatement pst = null;
		int n = 0;
		try {
			conn = DBPool.getInstance().getConnection();
			pst = conn.prepareStatement(sql);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					pst.setObject(i + 1, params[i]);
				}
			}
			n = pst.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, pst);
		}
		return n;
	}
	
	/**
	 * 	增加数据--返回更新的数据的自增主键值
	 * @param sql 增加sql
	 * @param params 参数
	 * @return 这个增加语句产生的主键值
	 */
	public Long addObject(String sql, Object...params) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Long n = new Long(0);
		try {
			conn = DBPool.getInstance().getConnection();
			//autoGeneratedKeys：自动生成主键
			//RETURN_GENERATED_KEYS：返回生成的主键
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					pst.setObject(i + 1, params[i]);
				}
			}
			pst.executeUpdate();
			//获取返回的主键
			rs = pst.getGeneratedKeys();
			//获取结果集中的返回的主键
			n = rs.next()?rs.getLong(1):-1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, pst);
		}
		return n;
	}
	
	
	/**
	 * 	查询所有数据条数
	 */
	public int getTotalCount(String sql) {
		try {
			return Integer.parseInt(runner.query(sql, new ScalarHandler<Object>()).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
	//释放资源
	public void close(Connection conn, Statement st, ResultSet rs) {
		try {
			if (conn != null) {
				conn.close();
			}
			if (st != null) {
				st.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close(Connection conn, Statement st) {
		try {
			if (conn != null) {
				conn.close();
			}
			if (st != null) {
				st.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}
