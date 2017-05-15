package com.aisino.fpqz.domain;

import java.util.Date;

/**
 * Pdf发票Model（所有信息）
 * @author yaoxj
 * @time 2017年4月17日下午1:45:18
 */
public class JAR_FPQZ_KJ {
	/**
	 * 购买方：地址、电话
	 */
	private String GMF_DZDH;
	/**
	 * 购买方：银行账号
	 */
	private String GMF_YHZH;
	/**
	 * 购买方：名称
	 */
	private String GMF_MC;
	/**
	 * 购买方：纳税人识别号
	 */
	private String GMF_NSRSBH;
	/**
	 * 价税合计
	 */
	private String JSHJ;
	/**
	 * 合计金额
	 */
	private String HJJE;
	/**
	 * 合计税额
	 */
	private String HJSE;
	/**
	 * 开票类型
	 */
	private String KPLX;
	/**
	 * 备注
	 */
	private String BZ;
	/**
	 * 开票日期
	 */
	private Date KPRQ;
	/**
	 * 开票人
	 */
	private String KPR;
	/**
	 * 收款人
	 */
	private String SKR;
	/**
	 * 复核人
	 */
	private String FHR;
	/**
	 * 销售方：银行账户
	 */
	private String XSF_YHZH;
	/**
	 * 销售方：地址、电话
	 */
	private String XSF_DZDH;
	/**
	 * 销售方：名称
	 */
	private String XSF_MC;
	/**
	 * 销售方：纳税人识别号
	 */
	private String XSF_NSRSBH;
	/**
	 * 发票密文
	 */
	private String FP_MW;
	/**
	 * 校验码
	 */
	private String JYM;
	/**
	 * 二维码
	 */
	private String EWM;
	/**
	 * 发票代码
	 */
	private String FP_DM;
	/**
	 * 发票号码
	 */
	private String FP_HM;
	/**
	 * 机器编号
	 */
	private String JQBH;
	/**
	 * 编码表版本号
	 */
	private String BMB_BBH;
	/**
	 * 清单备注(多页PDF：1，否则：0)
	 */
	private String QD_BZ = "0";
	/**
	 * 清单发票项目名称
	 */
	private String QDXMMC;
	/**
	 * 签章ID
	 */
	private String QZID;
	/**
	 * 模版代码
	 */
	private String MBDM;
	/**
	 * 模板——A5
	 */
	private byte[] MB_A5;
	/**
	 * 模板列表
	 */
	private byte[] MB_LIST;
	private String IAC;
	/**
	 * 数据来源？
	 */
	private String SJLY;
	/**
	 * 代开标志:1
	 */
	private String DKBZ;
	/**
	 * 收购标志
	 */
	private String SGBZ;
	/**
	 * 对应正数发票代码
	 */
	private String YFP_DM;
	/**
	 * 对应正数发票号码
	 */
	private String YFP_HM;
	/**
	 * 发票明细
	 */
	private JAR_FPQZ_KJMX[] JAR_FPQZ_KJMXS;

	public String getGMF_DZDH() {
		return this.GMF_DZDH;
	}

	public void setGMF_DZDH(String gmf_dzdh) {
		this.GMF_DZDH = gmf_dzdh;
	}

	public String getGMF_YHZH() {
		return this.GMF_YHZH;
	}

	public void setGMF_YHZH(String gmf_yhzh) {
		this.GMF_YHZH = gmf_yhzh;
	}

	public String getGMF_MC() {
		return this.GMF_MC;
	}

	public void setGMF_MC(String gmf_mc) {
		this.GMF_MC = gmf_mc;
	}

	public String getGMF_NSRSBH() {
		return this.GMF_NSRSBH;
	}

	public void setGMF_NSRSBH(String gmf_nsrsbh) {
		this.GMF_NSRSBH = gmf_nsrsbh;
	}

	public String getJSHJ() {
		return this.JSHJ;
	}

	public void setJSHJ(String jshj) {
		this.JSHJ = jshj;
	}

	public String getHJJE() {
		return this.HJJE;
	}

	public void setHJJE(String hjje) {
		this.HJJE = hjje;
	}

	public String getHJSE() {
		return this.HJSE;
	}

	public void setHJSE(String hjse) {
		this.HJSE = hjse;
	}

	public String getKPLX() {
		return this.KPLX;
	}

	public void setKPLX(String kplx) {
		this.KPLX = kplx;
	}

	public String getBZ() {
		return this.BZ;
	}

	public void setBZ(String bz) {
		this.BZ = bz;
	}

	public Date getKPRQ() {
		return this.KPRQ;
	}

	public void setKPRQ(Date kprq) {
		this.KPRQ = kprq;
	}

	public String getKPR() {
		return this.KPR;
	}

	public void setKPR(String kpr) {
		this.KPR = kpr;
	}

	public String getSKR() {
		return this.SKR;
	}

	public void setSKR(String skr) {
		this.SKR = skr;
	}

	public String getFHR() {
		return this.FHR;
	}

	public void setFHR(String fhr) {
		this.FHR = fhr;
	}

	public String getXSF_YHZH() {
		return this.XSF_YHZH;
	}

	public void setXSF_YHZH(String xsf_yhzh) {
		this.XSF_YHZH = xsf_yhzh;
	}

	public String getXSF_DZDH() {
		return this.XSF_DZDH;
	}

	public void setXSF_DZDH(String xsf_dzdh) {
		this.XSF_DZDH = xsf_dzdh;
	}

	public String getXSF_MC() {
		return this.XSF_MC;
	}

	public void setXSF_MC(String xsf_mc) {
		this.XSF_MC = xsf_mc;
	}

	public String getXSF_NSRSBH() {
		return this.XSF_NSRSBH;
	}

	public void setXSF_NSRSBH(String xsf_nsrsbh) {
		this.XSF_NSRSBH = xsf_nsrsbh;
	}

	public String getFP_MW() {
		return this.FP_MW;
	}

	public void setFP_MW(String fp_mw) {
		this.FP_MW = fp_mw;
	}

	public String getJYM() {
		return this.JYM;
	}

	public void setJYM(String jym) {
		this.JYM = jym;
	}

	public String getEWM() {
		return this.EWM;
	}

	public void setEWM(String ewm) {
		this.EWM = ewm;
	}

	public String getFP_DM() {
		return this.FP_DM;
	}

	public void setFP_DM(String fp_dm) {
		this.FP_DM = fp_dm;
	}

	public String getFP_HM() {
		return this.FP_HM;
	}

	public void setFP_HM(String fp_hm) {
		this.FP_HM = fp_hm;
	}

	public String getJQBH() {
		return this.JQBH;
	}

	public void setJQBH(String jqbh) {
		this.JQBH = jqbh;
	}

	public String getBMB_BBH() {
		return this.BMB_BBH;
	}

	public void setBMB_BBH(String bmb_bbh) {
		this.BMB_BBH = bmb_bbh;
	}

	public String getQD_BZ() {
		return this.QD_BZ;
	}

	public void setQD_BZ(String qd_bz) {
		this.QD_BZ = qd_bz;
	}

	public String getQDXMMC() {
		return this.QDXMMC;
	}

	public void setQDXMMC(String qdxmmc) {
		this.QDXMMC = qdxmmc;
	}

	public String getQZID() {
		return this.QZID;
	}

	public void setQZID(String qzid) {
		this.QZID = qzid;
	}

	public String getMBDM() {
		return this.MBDM;
	}

	public void setMBDM(String mbdm) {
		this.MBDM = mbdm;
	}

	public byte[] getMB_A5() {
		return this.MB_A5;
	}

	public void setMB_A5(byte[] mb_a5) {
		this.MB_A5 = mb_a5;
	}

	public byte[] getMB_LIST() {
		return this.MB_LIST;
	}

	public void setMB_LIST(byte[] mb_list) {
		this.MB_LIST = mb_list;
	}

	public String getIAC() {
		return this.IAC;
	}

	public void setIAC(String iac) {
		this.IAC = iac;
	}

	public String getSJLY() {
		return this.SJLY;
	}

	public void setSJLY(String sjly) {
		this.SJLY = sjly;
	}

	public String getDKBZ() {
		return this.DKBZ;
	}

	public void setDKBZ(String dkbz) {
		this.DKBZ = dkbz;
	}

	public String getSGBZ() {
		return this.SGBZ;
	}

	public void setSGBZ(String sgbz) {
		this.SGBZ = sgbz;
	}

	public JAR_FPQZ_KJMX[] getJAR_FPQZ_KJMXS() {
		return this.JAR_FPQZ_KJMXS;
	}

	public void setJAR_FPQZ_KJMXS(JAR_FPQZ_KJMX[] jar_fpqz_kjmxs) {
		this.JAR_FPQZ_KJMXS = jar_fpqz_kjmxs;
	}

	public String getYFP_DM() {
		return this.YFP_DM;
	}

	public void setYFP_DM(String yfp_dm) {
		this.YFP_DM = yfp_dm;
	}

	public String getYFP_HM() {
		return this.YFP_HM;
	}

	public void setYFP_HM(String yfp_hm) {
		this.YFP_HM = yfp_hm;
	}
}