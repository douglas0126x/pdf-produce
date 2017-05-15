package com.aisino.fpqz.domain;

/**
 * 分页内容
 * @author yaoxj
 * @time 2017年4月26日上午10:48:22
 */
public class Page {
	
	/**
	 * (当前页数)
	 */
	private String pageNo;
	
	/**
	 * (每页个数)
	 */
	private String pageSize;
	
	
	/**
	 * (页数总数)
	 */
	private String pageCount;
	
	
	/**
	 * (数据总数)
	 */
	private String dataCount;


	public String getPageNo() {
		return pageNo;
	}


	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}


	public String getPageSize() {
		return pageSize;
	}


	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}


	public String getPageCount() {
		return pageCount;
	}


	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}


	public String getDataCount() {
		return dataCount;
	}


	public void setDataCount(String dataCount) {
		this.dataCount = dataCount;
	}
	
	
	
}
