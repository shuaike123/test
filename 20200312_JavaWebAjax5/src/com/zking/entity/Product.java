package com.zking.entity;

import java.util.List;

public class Product {
	
	private Integer id;//编号
	private String pro_name;//商品名称
	private double pro_price;//商品价格
	private Integer pro_stock;//库存
	private String pro_coverPic;//封面图片
	private List<ProductPics> pics;//存对应所有图片的对象
	public Product() {
	}
	public Product(Integer id, String pro_name, Double pro_price, Integer pro_stock, String pro_coverPic,
			List<ProductPics> pics) {
		this.id = id;
		this.pro_name = pro_name;
		this.pro_price = pro_price;
		this.pro_stock = pro_stock;
		this.pro_coverPic = pro_coverPic;
		this.pics = pics;
	}	

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPro_name() {
		return pro_name;
	}
	public void setPro_name(String pro_name) {
		this.pro_name = pro_name;
	}
	public double getPro_price() {
		return pro_price;
	}
	public void setPro_price(double pro_price) {
		this.pro_price = pro_price;
	}
	public Integer getPro_stock() {
		return pro_stock;
	}
	public void setPro_stock(Integer pro_stock) {
		this.pro_stock = pro_stock;
	}
	public String getPro_coverPic() {
		return pro_coverPic;
	}
	public void setPro_coverPic(String pro_coverPic) {
		this.pro_coverPic = pro_coverPic;
	}
	public List<ProductPics> getPics() {
		return pics;
	}
	public void setPics(List<ProductPics> pics) {
		this.pics = pics;
	}
	@Override
	public String toString() {
		return "Product [id=" + id + ", pro_name=" + pro_name + ", pro_price=" + pro_price + ", pro_stock=" + pro_stock
				+ ", pro_coverPic=" + pro_coverPic + ", pics=" + pics + "]";
	}
	
}
