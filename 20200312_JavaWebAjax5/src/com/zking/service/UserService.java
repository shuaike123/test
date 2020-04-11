package com.zking.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.zking.dao.DBPool;
import com.zking.entity.PageManager;
import com.zking.entity.User;

public class UserService {

	//dao中的数据库连接对象
	private DBPool dbPool = DBPool.getInstance();
	private QueryRunner runner = new QueryRunner(dbPool.getDataSource());
	
	/**
	 * 	查询数据--分页
	 * @param pn 当前页
	 * @param limit 每页显示数量
	 */
	public List<User> queryUsers(PageManager pm) {
		try {
			String sql = "select * from lpm_user limit ?,?";
			//从PageManager中取出分页参数
			Object[] params = {(pm.getPn() - 1) * pm.getLimit(), pm.getLimit()};
			return runner.query(sql, new BeanListHandler<User>(User.class),params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 	查询所有数据条数
	 */
	public int getTotalCount() {
		try {
			String sql = "select count(*) from lpm_user";
			return Integer.parseInt(runner.query(sql, new ScalarHandler<Object>()).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 	通过传递用户数据增加到数据库
	 * @param user 封装了增加的数据实体类
	 * @return
	 */
	public int addUser(User user) {
		String sql = "insert into lpm_user values(null,?,?)";
    	Object[] params = {user.getUsername(), user.getPassword()};
    	try {
			return runner.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	

	/**
	 * 	根据传递的用户名核对是否已存在
	 * @param user 存储参数的实体类
	 */
	public User checkUser(User user) {
		//为什么不直接传递username
		//如果这个地方只传递username，如果方法内容做了修改，多了个参数
		String sqlCheck = "select * from lpm_user where username=?";
		Object[] paramsCheck = {user.getUsername()};
		try {
			return runner.query(sqlCheck, new BeanHandler<User>(User.class), paramsCheck);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 	登陆操作
	 * @param user 存储参数的实体类
	 * @return 返回这个登陆用户的所有信息
	 */
	public User login(User user) {
		String sql = "select * from lpm_user where username=? and password=?";
		Object[] params = {user.getUsername(), user.getPassword()};
		try {
			return runner.query(sql, new BeanHandler<User>(User.class), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 	根据id删除一个用户
	 */
	public int deleteUser(User user) {
		String sql = "delete from lpm_user where id=?";
    	Object[] params = {user.getId()};
    	try {
			return runner.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * 
	 * @return 所有的用户数据集合
	 * @throws SQLException 
	 */
	public List<User> allUsers() throws SQLException{
		
		String sql="select * from lpm_user";
		List<User> users=runner.query(sql, new BeanListHandler<User>(User.class));
		return users;
	}

	/**
	 * 	查询单个用户信息
	 */
	public User queryUser(User user) {
		String sql = "select * from lpm_user where id=?";
    	Object[] params = {user.getId()};
		try {
			return runner.query(sql, new BeanHandler<User>(User.class),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 	修改用户数据
	 */
	public int updateUser(User user) {
		String sql = "update lpm_user set username=?,password=? where id=?";
	   	Object[] params = {user.getUsername(), user.getPassword(), user.getId()};
	   	try {
			return runner.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<Object[]> getIds() {
		String sql = "select id from lpm_user";
		Object[] params = {};
		try {
			return runner.query(sql, new ArrayListHandler(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int deleteAll(String[] ids) {
		StringBuilder sbd = new StringBuilder();
		for (int i = 0; i < ids.length; i++) {
			sbd.append("?,");
		}
		//去掉最后一个逗号
		sbd.setLength(sbd.length() - 1);
		String sql = "delete from lpm_user where id in ("+sbd.toString()+")";
    	Object[] params = ids;
    	try {
			return runner.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
