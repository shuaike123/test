package com.zking.entity;

public class ProductPics {
	
	private Integer id;//图片编号
	private String pic_name;//图片名称
	private String pic_address;//图片地址
	private Integer productid;//对应的商品编号
	
	public ProductPics() {
	}
	public ProductPics(Integer id, String pic_name, String pic_address, Integer productid) {
		this.id = id;
		this.pic_name = pic_name;
		this.pic_address = pic_address;
		this.productid = productid;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPic_name() {
		return pic_name;
	}
	public void setPic_name(String pic_name) {
		this.pic_name = pic_name;
	}
	public String getPic_address() {
		return pic_address;
	}
	public void setPic_address(String pic_address) {
		this.pic_address = pic_address;
	}
	public Integer getProductid() {
		return productid;
	}
	public void setProductid(Integer productid) {
		this.productid = productid;
	}
	@Override
	public String toString() {
		return "ProductPics [id=" + id + ", pic_name=" + pic_name + ", pic_address=" + pic_address + ", productid="
				+ productid + "]";
	}
	
}
