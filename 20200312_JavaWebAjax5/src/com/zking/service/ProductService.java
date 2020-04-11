package com.zking.service;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.metamodel.ListAttribute;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.zking.dao.DBPool;
import com.zking.dao.JDBCUtils;
import com.zking.entity.PageManager;
import com.zking.entity.Product;
import com.zking.entity.ProductPics;

public class ProductService {
	
	//数据库操作类
	private QueryRunner runner = new QueryRunner(DBPool.getInstance().getDataSource());
	
	//自己封装的JDBC
	JDBCUtils jdbcUtils = new JDBCUtils();
	
	//增加商品信息
	public int addProduct(Product product) {
		try {
			String sql = "insert into product values(null,?,?,?,?)";
			Object[] params = {product.getPro_name(),product.getPro_price(),
					product.getPro_stock(), product.getPro_coverPic()};
			return runner.update(sql, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	//取到最后一次存储的商品id
	public Long getProductId() {
		try {
			String sql = "select last_insert_id()";
			Object[] params = {};
			return runner.query(sql, new ScalarHandler<Long>(1), params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	//取到最后一次存储的商品id
	//自己编写JDBC的方式
	//增加完之后，自动获取增加的自增主键值
	public Long getProductIdJDBC(Product product) {
		try {
			String sql = "insert into product values(null,?,?,?,?)";
			Object[] params = {product.getPro_name(),product.getPro_price(),
					product.getPro_stock(), product.getPro_coverPic()};
			return jdbcUtils.addObject(sql, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Object[][] listToArr(List<ProductPics> list, Long id){
		Object[][] params = new Object[list.size()][3];
		for (int i = 0; i < params.length; i++) {
			params[i][0] = list.get(i).getPic_name();
			params[i][1] = list.get(i).getPic_address();
			params[i][2] = Integer.parseInt(id.toString());
		}
		return params;
	}
	

	//批量存储图片数据
	public int[] addPics(Long id, List<ProductPics> pics) {
		try {
			String sql = "insert into product_pics values(null,?,?,?)";
			Object[][] params = listToArr(pics, id);
			return runner.batch(sql, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public int getTotalCount() {
		return jdbcUtils.getTotalCount("select count(*) from product");
	}

	public List<Product> queryProducts(PageManager pm) {
		try {
			//1.查询出分页的所有Product对象
			String sqlProduct = "SELECT * FROM product limit ?,?";
			//从PageManager中取出分页参数
			Object[] params = {(pm.getPn() - 1) * pm.getLimit(), pm.getLimit()};
			List<Product> products = runner.query(sqlProduct, 
					new BeanListHandler<Product>(Product.class),params);
			//2.根据每一个对象的id查询对应所有的图片，并存储起来
			for (int i = 0; i < products.size(); i++) {
				products.get(i).setPics(runner.query("select * from product_pics where productid = ?", 
					new BeanListHandler<ProductPics>(ProductPics.class), products.get(i).getId()));
			}
			return products;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int deleteProduct(Product product) {
		
		String sql="delete from product where id=?";
		Object [] params= {product.getId()};
		try {
			return runner.update(sql, params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	public int deletePic(Product product) {
		String sql="delete from product_pics where productid = ?";
		
		if(deleteProduct(product)>0) {
			Object [] params= {product.getId()};
			try {
				return runner.update(sql, params);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}
	public int updateProduct(Product product) {
		String sql="update product set pro_name=?,pro_price=?, pro_stock=?,pro_coverPic=? where id=?";
		Object params[] = {product.getPro_name(),product.getPro_price(),product.getPro_stock(),product.getPro_coverPic(),product.getId()};
		try {
			return runner.update(sql,params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	public int[] updatePics(Product product) throws SQLException {
		String sql="delete from product_pics where productid=?";
		Object param[] = {product.getId()};

		if(updateProduct(product)>0) {
			int i=runner.update(sql,param);
			if(i>0) {
				String sql2="insert product_pics values(null,?,?,?)";
				Object [] [] params =listToArr(product.getPics(), (long)product.getId());
				return runner.batch(sql2, params);
				
				
			}
		}
		return null;
	}
	
	public Product queryProduct(Product product) throws SQLException {
		String sql ="select * from product where id=?";
		Object [] param= {product.getId()};
		product =runner.query(sql, param, new BeanHandler<Product>(Product.class));
		
		String sql2 ="select * from product_pics where productid=?";
		Object param2[]= {product.getId()};
		List<ProductPics> pics=runner.query(sql2, param2, new BeanListHandler<ProductPics>(ProductPics.class));
		product.setPics(pics);

		
		return product;
	}
}
