package com.aisino.fpqz.domain;

import java.util.List;

/**
 * xml发票信息对比概况
 * 
 * @author yaoxj
 * @time 2017年4月26日上午11:03:19
 */
public class FPDistributed {
	/**
	 * 对比结果代码
	 */
	private String completeCode;
	/**
	 * 相应结果代码
	 */
	private String reasonCode;
	/**
	 * 相应结果描述
	 */
	private String reasonMessage;
	/**
	 * 发票数据
	 */
	private List<FPData> data;
	/**
	 * 页数
	 */
	private Page page;

	public String getCompleteCode() {
		return completeCode;
	}

	public void setCompleteCode(String completeCode) {
		this.completeCode = completeCode;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getReasonMessage() {
		return reasonMessage;
	}

	public void setReasonMessage(String reasonMessage) {
		this.reasonMessage = reasonMessage;
	}

	public List<FPData> getData() {
		return data;
	}

	public void setData(List<FPData> data) {
		this.data = data;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

}
