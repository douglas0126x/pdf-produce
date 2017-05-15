package com.aisino.fpqz.domain;

import java.util.List;


public class FPData {
	
	/**
	 * 发票基本信息
	 */
	private Fpkj fP_KJ;
	/**
	 * 货物明细
	 */
	private List<FpKjmx> fP_KJMX;

	public Fpkj getfP_KJ() {
		return fP_KJ;
	}

	public void setfP_KJ(Fpkj fP_KJ) {
		this.fP_KJ = fP_KJ;
	}

	public List<FpKjmx> getfP_KJMX() {
		return fP_KJMX;
	}

	public void setfP_KJMX(List<FpKjmx> fP_KJMX) {
		this.fP_KJMX = fP_KJMX;
	}
	
	
	
}
