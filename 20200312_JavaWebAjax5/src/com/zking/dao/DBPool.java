package com.zking.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.zking.entity.User;

public class DBPool {
	private DataSource dataSource = null;
	
	//从梦想走向破灭的过程：
	//1.学生时代：事情量 --> 1
	//2.工作之后：--> 10
	//3.有对象之后：--> 50
	//4.结婚之后：--> 100
	//5.有小孩子之后：--> 1000
	
	//结论：
	//1.学生时代能完成事情，一定不要留在出学校。【有梦想，并且为之奋斗！！！】
	//钱不是万能的
	//2.女朋友，一定要在有稳定的收入时候是最合适的。【梦想仍旧在，继续奋斗，燃烧青春！！！】
	//你收入越稳定，越高，对象的事情就越少。
	
	//钱就是万能的。没有任何问题不是因钱而起，也没有任何问题不是钱不能解决的。
	//如果不是因为钱而发生的问题，这不叫问题。
	
	//马克思主义核心思想：物质基础决定上层建筑。
	
	//3.结婚越晚越好。【开始GG，梦想开始出现破裂】
	
	//4之前，感情好都是假象。
	
	//4.没有任何办法的-->生孩子25-30岁之间生孩子。【完全GG，梦想完全破裂，对人生失去希望~】
	//小孩子非常烦恼，烦恼的事情就会发到你的头上，你无故接受了这种黑锅，你就会反抗。
	//有小孩子之后，还能不吵架【0.01%】，我认为这才是真正的感情好。
	
	//25-30：高龄产妇危险。年纪大了心态会变。这个期间的小孩子各方面状况是最好的。
	
	
	private DBPool() {
		try {
			//初始化数据源
			dataSource = DruidDataSourceFactory.createDataSource(getProperties("db.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private static DBPool pool = null;
	
	//获取实例
	public static DBPool getInstance() {
		if (pool == null) {
			pool = new DBPool();
		}
		return pool;
	}
	
	//通过数据源获取连接
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
	
	//获取数据源
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * 	通过这个类的反射获取项目的根目录，最终得到一个Properties对象
	 * @param prosPath Properties文件的路径
	 * @return 加载了Properties文件的对象
	 * @throws Exception 
	 */
	public Properties getProperties(String prosPath) throws Exception {
		//1.创建一个properties对象
		//这个对象里面就有操作这个properties文件所有方法
		Properties pros = new Properties();
		//2.获取源文件的字节输入流
		InputStream inStream = null;
		try {
			//class文件所在的位置的根目录。
			String path = DBPool.class.getResource("/").getPath();
			//将中文的url编码解析%ert%weq
			path = URLDecoder.decode(path, "utf-8");
			inStream = new FileInputStream(path + "\\" + prosPath);
			//3.properties对象加载源文件
			pros.load(inStream);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("Properties文件路径未找到!");
		} catch (IOException e) {
			throw new IOException("Properties文件流加载失败!");
		}
		return pros;
	}
	
	
	public static void main(String[] args) {
		DBPool dbPool= DBPool.getInstance();
		try {
			QueryRunner runner = new QueryRunner(dbPool.getDataSource());
			String sql = "select * from lpm_user";
			List<User> users = runner.query(sql, new BeanListHandler<User>(User.class));
			System.out.println(users);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
