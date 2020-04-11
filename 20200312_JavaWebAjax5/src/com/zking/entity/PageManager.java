package com.zking.entity;

public class PageManager {
	private int pn = 1;//当前页
	private int totalCount = 0;//总数据数
	private int pages;//总页数
	private int limit = 5;//每页显示数据条数
	public PageManager() {
	}
	public PageManager(int pn, int totalCount, int limit) {
		if (limit != 0) 
			this.limit = limit;
		this.totalCount = totalCount;
		//计算总页数
		this.pages = this.totalCount / this.limit;
		if (this.totalCount % this.limit != 0) {
			this.pages++;
		}
		//判断当前页是不是首页和尾页
		if (pn != 0) {
			if (pn < 1) 
				this.pn = 1;
			else if (pn > this.pages) 
				this.pn = this.pages;
			else 
				this.pn = pn;
		}
	}
	public int getPn() {
		return pn;
	}
	public void setPn(int pn) {
		this.pn = pn;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
}
