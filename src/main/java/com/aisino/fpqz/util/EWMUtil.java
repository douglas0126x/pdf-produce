package com.aisino.fpqz.util;

import com.aisino.pojo.QrcodeBean;
import com.aisino.qrcode.util.QrcodeUtil;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 二维码工具类
 * 
 * @author yaoxj
 * @time 2017年4月15日上午10:45:48
 */
public class EWMUtil {
	/**
	 * 生成二维码
	 * @param fpdm 发票代码
	 * @param fphm 发票号码
	 * @param hjbhsje 合计不含税金额
	 * @param kprq 开票日期
	 * @param jym 校验码
	 * @return
	 * @throws Exception
	 */
	public static String generateTwoDimCode(String fpdm, String fphm,
			String hjbhsje, Date kprq, String jym) throws Exception {
		
		QrcodeBean QrcodeBean = new QrcodeBean();
		QrcodeBean.setHEAD_version("01");
		QrcodeBean.setEXPRESS_eInvoiceType("10");
		QrcodeBean.setEXPRESS_eInvoiceCode(fpdm);
		QrcodeBean.setEXPRESS_EInvoiceNo(fphm);

		hjbhsje = ArithUtil.fdd(hjbhsje, 2, 2);
		QrcodeBean.setEXPRESS_billingAmount(hjbhsje);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(kprq);
		QrcodeBean.setEXPRESS_billingDate(dateString);
		QrcodeBean.setEXPRESS_checkCode(jym);

		return QrcodeUtil.generationQrcode(QrcodeBean);
	}
}