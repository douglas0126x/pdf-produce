package com.esa2000.test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.esa2000.NXServerSignShell;
import com.esa2000.pdfsign.util.NXServerSignShellUtils;

public class TestSign implements Runnable {
	
//	★★★★★★★★★★★★★★★★★ prepare  begin  ★★★★★★★★★★★★★★★★★ 
	
	
	// PDF文件路径
	String tmp1 = "tmp\\原文\\竖版.pdf";
	String tmp2 = "tmp\\原文\\横版.pdf";

	String dxhy = "D:\\work\\eclipse_workspace\\eclipse2_workspace\\pdf-produce\\tmp\\dxhy\\dxhy.pdf";
	// 签章后文件的路径
	String resultPath = "tmp/generatePdf/";
	// 页码
	int page = 1;
	// 注释文本
	String text1 = "同意";
	String text2 = "不同意";
	String text3 = "有异议";
	String text4 = "最后面的注释";
	// 是否添加时间戳
	boolean TSA_type = false;
	// 字体大小
	int fontSize = 11;

	public static int index = 1;

	private String username = "";

	public TestSign(String username) {
		this.username = username;
	}
	
	
//	★★★★★★★★★★★★★★★★★ prepare  end  ★★★★★★★★★★★★★★★★★
	
	
	

	public static void main(String args[]) {
		// 纳税人识别号字符串数组，调用签章时，默认取数组第一个值
		String[] seals = { "11100000000", "11200000000", "11300000000" };

		for (int i = 1; i < 2; i++) {
			Thread tmpThread = new Thread(new TestSign(seals[i]));
			tmpThread.start();
		}
	}

	// 线程执行方法
	public void run() {
		try {
			// 山东签章测试
			ShanDongSign();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * pdf首页签章
	 */
	private void ShanDongSign() {
		long startTime = System.currentTimeMillis();
		System.out.println("★★★★★★★★★  application starting★★★★★★★★★  \n");
		
		String orgcpde = "11010866052765X";
		String sealcpde = "AF6C11FBAF62162A0DF8B5CC54E72E58";
		
		String orgcpde_anno ="13709000000";
		String sealcpde_anno ="D071AB23726724BA0942C9CE77137E14";
		
		
		File file = new File(resultPath);  
		/**
		 * 创建目录
		 */
	    if(!file.exists()){  
	            file.mkdirs();  
	        } 
		
		
		
//		System.out.println(getFormateNow());
		/*
		①、根据坐标签章：测试成功！  注意：机构印章分类只能为空，否则失败；
		*/
		signByPosition(dxhy, resultPath+"signByPosition_"+getFormateNow()+".pdf", orgcpde, sealcpde, 504, 60, 1, TSA_type,"", 0.7f);
	    //TODO  HOW to use the method?
//		doUpdateSealImgdemo(orgcpde, sealcpde, sealcpde, new File("D:\\work\\eclipse_workspace\\eclipse2_workspace\\pdf-produce\\tmp\\dxhy\\dxhy.pdf"));
		/*
		②、根据关键字签章:测试失败-返回状态码：7
		*/
//		signByKeyword(dxhy, resultPath+"signByKeyword_00.pdf", orgcpde, sealcpde, "关键字测试", 1, TSA_type, 0.7f);
		/*
		③验章
		*/
//		verifySign(resultPath + "signByPosition_00.pdf");
//		④Pdf添加文字注释
//		signByPosition(resultPath+"anno\\old\\横版.pdf", resultPath+"anno\\new\\横版_0.pdf",orgcpde_anno,sealcpde_anno, 50, 50, 1, TSA_type,"0", 0.8f);
//		addAnnotation(out_Path, out_Path_zs, text, 30, 30, 90, 90, fontSize,page);
//		⑤⑥⑦⑧⑨
		
		long endTime = System.currentTimeMillis();
		System.out.println("\n★★★★★★★★★  application end ★★★★★★★★★  \n\nlasted time  =  "+(endTime-startTime)/1000.0f+" s ");
		
	}

	/**
	 * 根据关键字签章
	 * @param suroce_Path  待签文件
	 * @param temp_path  签章文件保存路径
	 * @param username  纳税人识别号/容器号
	 * @param pwd  纳税人校验码
	 * @param Keyword    关键字
	 * @param leftOffset  左右偏移量：大于0往右偏移，小于0往左偏移
	 * @param flag   是否添加时间戳
	 * @param opacity     印章透明度(取值范围为0~1之间)
	 */
	private void signByKeyword(String suroce_Path, String temp_path,String username, String pwd, String Keyword, int leftOffset,
			boolean flag, float opacity) {
		String code = NXServerSignShellUtils.pdfSgin(suroce_Path, temp_path,username, pwd, Keyword, leftOffset, flag, opacity);
		System.out.println("根据关键字签章返回代码==========" + code);
	}
	
	
	/**
	 * 修改签章图片
	 * @param sbh 印章标识符
	 * @param pwd 印章授权码
	 * @param sealType 印章类型编码
	 * @param file 印模图片文件
	 * @return
	 */
	public static void doUpdateSealImgdemo(String sbh, String pwd, String sealType, File file) {
		String code = NXServerSignShellUtils.doUpdateSealImg(sbh, pwd, sealType, file);//更新印模接口
		System.out.println("调用接口修改印章图片返回代码==========" + code);
	}

	/**
	 * 根据坐标签章
	 * @param suroce_path   待签文件路径
	 * @param temp_path   签章后文件保存路径
	 * @param username   纳税人识别号/容器号
	 * @param pwd  纳税人校验码
	 * @param xPosition  X轴坐标
	 * @param yPosition    Y轴坐标
	 * @param pageNum   PDF的页码
	 * @param flag   是否添加时间戳
	 * @param opacity  印章透明度(取值范围为0~1之间)
	 */
	private void signByPosition(String suroce_path, String temp_path,String username, String pwd, float xPosition, float yPosition,
			int pageNum, boolean flag, float opacity) {
		String code2 = NXServerSignShellUtils.pdfSgin(suroce_path, temp_path,username, pwd, xPosition, yPosition, pageNum, flag, opacity);
		System.out.println("根据坐标签章返回代码==========" + code2);
	}

	/**
	 * 给pdf添加文字注释
	 * @param sources_path  待签文件路径
	 * @param target_path  文件保存路径
	 * @param text   注释内容
	 * @param x0   开始坐标X 毫米
	 * @param y0   开始坐标Y 毫米
	 * @param x1     结束坐标X 毫米
	 * @param y1  结束坐标Y 毫米
	 * @param fontSize  字体大小
	 * @param pageNum  页码
	 * @return
	 */
	private void addAnnotation(String sources_path, String target_path,	String text, float x0, float y0, float x1, float y1, int fontSize,
			int pageNum) {
		
		boolean flag = NXServerSignShellUtils.addAnnotation(sources_path,target_path, text, x0, y0, x1, y1, fontSize, pageNum);
		
		System.out.println("给pdf添加文字注释==========" + flag);
	}

	/**
	 * 根据关键字签章
	 * @param suroce_Path   待签文件
	 * @param temp_path   签章文件保存路径
	 * @param username   纳税人识别号/容器号
	 * @param pwd   纳税人校验码
	 * @param Keyword  关键字
	 * @param leftOffset  左右偏移量：大于0往右偏移，小于0往左偏移
	 * @param flag  是否添加时间戳
	 * @param sealType  机构印章分类(纳税人印章为-1，机构公章为0，机构业务专用章为1)
	 * @param opacity  印章透明度(取值范围为0~1之间)
	 */
	private void signByKeyword(String suroce_Path, String temp_path,String username, String pwd, String Keyword, int leftOffset,
			boolean flag, String sealType, float opacity) {
		
		String code = NXServerSignShellUtils.pdfSgin(suroce_Path, temp_path,username, pwd, Keyword, leftOffset, flag, sealType, opacity);
		
		System.out.println("根据关键字签章返回代码==========" + code);
	}

	/**
	 * 根据坐标签章
	 * @param suroce_path  待签文件路径
	 * @param temp_path  签章后文件保存路径
	 * @param username   纳税人识别号/容器号
	 * @param pwd   纳税人校验码
	 * @param xPosition     X轴坐标 毫米
	 * @param yPosition  Y轴坐标 毫米
	 * @param pageNum  PDF的页码
	 * @param flag   是否添加时间戳
	 * @param sealType  机构印章分类(纳税人印章为-1，机构公章为0，机构业务专用章为1) 
	 * @param opacity  	印章透明度(取值范围为0~1之间)
	 */
	private void signByPosition(String suroce_path, String temp_path,String username, String pwd, float xPosition, float yPosition,
			int pageNum, boolean flag, String sealType, float opacity) {
		
		String code2 = "";
		if (sealType == "") {
			code2 = NXServerSignShellUtils.pdfSgin(suroce_path, temp_path, username, pwd, xPosition,yPosition, pageNum, flag, opacity);
		} else {
			code2 = NXServerSignShellUtils.pdfSgin(suroce_path, temp_path,username, pwd, xPosition, yPosition, pageNum, flag,sealType, opacity);
		}
		
		System.out.println("根据坐标签章返回代码==========" + code2);
	}

	/**
	 * 验章
	 * @param tempPdfPath
	 */
	private void verifySign(String tempPdfPath) {
		/*
		 根据传入的已签章pdf路径验章 
		 已签章pdf文件保存目录
		 秘钥容器，可以为空
		*/
		NXServerSignShell signShell = new NXServerSignShell();
		/*
		String tempPdfPath =
		"E:\\pdfsigntest\\11200000000_Test_position_2.pdf";
		*/
		/*
		 * 不含时间戳的验证verifySign 第一个参数是待验证PDF文件路径 第二个参数是容器号 必须填入
		 */
		String result = signShell.verifySign(tempPdfPath, "#rsa1");
		String resultInfo = signShell.getSignInfo(tempPdfPath, "#rsa1");
		System.out.println("根据验证返回代码==========" + result);
		System.out.println("根据验证返回印章信息==========" + resultInfo);
		// 包含时间戳的验证
		String resultCode =signShell.verifySign(tempPdfPath, "",true);
	 	System.out.println("根据验证返回代码==========" + resultCode);
	}
	
	/**
	 * get current time as formation 'yyyy-MM-dd HH,mm,ss'
	 * @return dateString
	 */
	private String getFormateNow(){
		
		   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH,mm,ss");
		   String dateString = formatter.format(new Date());
		   return dateString;
		
	}
	
	
	
	

}