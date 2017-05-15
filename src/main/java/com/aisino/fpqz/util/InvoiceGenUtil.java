package com.aisino.fpqz.util;

/**
 * 发票常量定义
 * 
 * @author yaoxj
 * @time 2017年4月15日上午11:05:35
 */
public class InvoiceGenUtil {
	/**
	 * GBK编码
	 */
	public static String CHARSET = "GBK";
	/**
	 * 商品行 ——商品名称长度
	 */
	public static int SPH_SPMC_LENGTH = 32;
	/**
	 * 商品行——规格型号长度
	 */
	public static int SPH_GGXH_LENGTH = 20;
	/**
	 * 商品行——单位长度
	 */
	public static int SPH_DW_LENGTH = 14;
	/**
	 * 默认字体大小
	 */
	public static int DEFUALT_FONTSIZE = 9;
	/**
	 * 数字默认大小
	 */
	public static int NUMBER_FONTSIZE = 11;
	/**
	 * 纳税人识别号-字体
	 */
	public static int NSRSBH_FONTSIZE = 12;
	/**
	 * 密码区-字体
	 */
	public static int MMQ_FONTSIZE = 12;
	/**
	 * 发票首页模板路径
	 */
	public static String MB_A5_URL = "template/";
	/**
	 * 发票附录清单页模板路径
	 */
	public static String MB_QD_URL = "template/list.pdf";
	/**
	 * simsun：字体
	 */
	public static String simsun = "STSong-Light";
	public static String courierNew = "fonts/cour.ttf";
//	public static String courierNew = "cour.ttf";
	public static String encode = "UniGB-UCS2-H";
	/**
	 * 差额征税蓝字发票
	 */
	public static String KPLX_LZFP = "0";
	/**
	 * 开票类型：红字发票
	 */
	public static String KPLX_HZFP = "1";
	/**
	 * 清单标志：自动清单
	 */
	public static String QDBZ_ZDQD = "0";
	/**
	 * 清单标志：强制清单</br>
	 */
	public static String QDBZ_QZQD = "1";
	/**
	 * 代开标志——直开发票
	 */
	public static String DKBZ_ZKFP = "0";
	/**
	 * 代开标志——代开发票：1
	 */
	public static String DKBZ_DKFP = "1";
	/**
	 * 收购标志：1
	 */
	public static String SGBZ_SGFP = "Y";
	
	public static String YHZCBS_FALSE = "0";
	public static String YHZCBS_TRUE = "1";
	
	public static String BB_FSPBMBB = "0";
	public static String BB_SPBMBB = "1";
	/**
	 * 签章位置
	 */
	public static int qzleft = 473;
	public static int qztop = 10;
	public static int qzright = 121;
	public static int qzbottom = 83;
	public static int qzpageindex = 1;
}