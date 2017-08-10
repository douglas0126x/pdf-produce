package com.aisino.fpqz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aisino.certreq.Position;
import com.aisino.certreq.SignPdf;
import com.aisino.fpqz.domain.FPData;
import com.aisino.fpqz.domain.FPDistributed;
import com.aisino.fpqz.domain.FpKjmx;
import com.aisino.fpqz.domain.Fpkj;
import com.aisino.fpqz.domain.JAR_FPQZ_KJ;
import com.aisino.fpqz.domain.JAR_FPQZ_KJMX;
import com.aisino.fpqz.exception.CustomException;
import com.aisino.fpqz.util.InvoiceGenUtil;
import com.aisino.fpqz.util.ValidateUtil;
import com.aisino.fpqz.util.XmlUtil;

/**
 * pdf生成入口类
 * @author yaoxj
 * @time 2017年4月26日上午11:39:27
 * test
 */
public class PDFProducer {
	public static Map<String, Object> validatePdf(JAR_FPQZ_KJ jar_fpqz_kj) {
		Map qzMap = new HashMap();
		try {
			ValidateUtil.validatePdf(jar_fpqz_kj);
		} catch (CustomException e) {
			qzMap.put("code", Integer.valueOf(e.getCode()));
			qzMap.put("msg", e.getMessage());
			qzMap.put("exception", e);
			return qzMap;
		} catch (Throwable e) {
			qzMap.put("code", Integer.valueOf(9990));
			qzMap.put("msg", "校验PDF失败：" + e);
			qzMap.put("exception", e);
			return qzMap;
		}
		qzMap.put("code", Integer.valueOf(1000));
		qzMap.put("msg", "校验通过");
		return qzMap;
	}

	/**
	 * pdf 模板拼装
	 * @param jar_fpqz_kj
	 * @return
	 */
	public static Map<String, Object> buildPdf(JAR_FPQZ_KJ jar_fpqz_kj) {
		Map qzMap = new HashMap();
		byte[] fileByte = null;
		int qdsize = 0;
		try {
			Map pdfMap = PDFBuilder.bulidPdfA5(jar_fpqz_kj);
			if (null == pdfMap) {
				qzMap.put("code", Integer.valueOf(1092));
				qzMap.put("msg", "生成PDF为空");
				qzMap.put("pdfFile", fileByte);
				return qzMap;
			}
			fileByte = (byte[]) pdfMap.get("fileByte");
			qdsize = ((Integer) pdfMap.get("qdsize")).intValue();
			if ((null == fileByte) || (fileByte.length == 0)) {
				qzMap.put("code", Integer.valueOf(1092));
				qzMap.put("msg", "生成PDF为空");
				qzMap.put("pdfFile", fileByte);
				return qzMap;
			}
		} catch (CustomException e) {
			qzMap.put("code", Integer.valueOf(e.getCode()));
			qzMap.put("msg", e.getMessage());
			qzMap.put("exception", e);
			qzMap.put("pdfFile", fileByte);
			return qzMap;
		} catch (Throwable e) {
			qzMap.put("code", Integer.valueOf(9991));
			qzMap.put("msg", "生成PDF失败：" + e);
			qzMap.put("exception", e);
			qzMap.put("pdfFile", fileByte);
			return qzMap;
		}
		qzMap.put("code", Integer.valueOf(1000));
		qzMap.put("msg", "生成PDF成功");
		qzMap.put("qdsize", Integer.valueOf(qdsize));
		qzMap.put("pdfFile", fileByte);
		return qzMap;
	}
	
	/**
	 * pdf签章
	 * @param fileByte
	 * @param sealId
	 * @param qdsize
	 * @return
	 */
	public static Map<String, Object> signPdf(byte[] fileByte, String sealId,
			int qdsize) {
		Map qzMap = new HashMap();
		try {
			fileByte = sign(fileByte, sealId, qdsize);
		} catch (Throwable e) {
			e.printStackTrace();
			qzMap.put("code", Integer.valueOf(9992));
			qzMap.put("msg", "签章失败：" + e);
			qzMap.put("exception", e);
			qzMap.put("pdfFile", fileByte);
			return qzMap;
		}
		qzMap.put("code", Integer.valueOf(1000));
		qzMap.put("msg", "签章成功");
		qzMap.put("pdfFile", fileByte);
		return qzMap;
	}
	/**
	 * 签章
	 * @param pdf
	 * @param sealId
	 * @param qdsize
	 * @return
	 * @throws Throwable
	 */
	public static byte[] sign(byte[] pdf, String sealId, int qdsize)
			throws Throwable {
		return signList(pdf, sealId, InvoiceGenUtil.qzpageindex,
				InvoiceGenUtil.qzleft, InvoiceGenUtil.qztop,
				InvoiceGenUtil.qzright, InvoiceGenUtil.qzbottom, "sign",
				"location", qdsize);
	}
	
	/**
	 * 签章list
	 * @param pdf
	 * @param sealId
	 * @param page
	 * @param left
	 * @param top
	 * @param width
	 * @param height
	 * @param reason
	 * @param location
	 * @param qdsize
	 * @return
	 * @throws Throwable
	 */
	private static byte[] signList(byte[] pdf, String sealId, int page,
			int left, int top, int width, int height, String reason,
			String location, int qdsize) throws Throwable {
		Position[] s = new Position[qdsize + 1];
		s[0] = new Position(1, left, top, width, height);
		for (int i = 1; i <= qdsize; i++) {
			s[i] = new Position(i + 1, left - 392, top + 22, width, height);
		}
		return SignPdf.signEx(pdf, sealId, page, left, top, width, height,
				reason, location, s);
	}
	
	/**
	 * pdf生成
	 * @param jar_fpqz_kj
	 * @return
	 */
	public static Map<String, Object> pdfProduce(JAR_FPQZ_KJ jar_fpqz_kj) {
		Map qzMap = new HashMap();
		byte[] pdfFile = null;
		int qdsize = 0;
		try {
			ValidateUtil.validatePdf(jar_fpqz_kj);
		} catch (CustomException e) {
			qzMap.put("code", Integer.valueOf(e.getCode()));
			qzMap.put("msg", e.getMessage());
			qzMap.put("exception", e);
			return qzMap;
		} catch (Throwable e) {
			qzMap.put("code", Integer.valueOf(9991));
			qzMap.put("msg", "生成PDF失败：" + e);
			qzMap.put("exception", e);
			return qzMap;
		}
		qzMap = buildPdf(jar_fpqz_kj);
		int code = ((Integer) qzMap.get("code")).intValue();
		if (code == 1000) {
			pdfFile = (byte[]) qzMap.get("pdfFile");
			qdsize = ((Integer) qzMap.get("qdsize")).intValue();
			// TODO 真正签章服务存在问题
			// qzMap = signPdf(pdfFile, jar_fpqz_kj.getQZID(), qdsize);
		} else {
			qzMap.put("code", Integer.valueOf(code));
			qzMap.put("msg", (String) qzMap.get("msg"));
			qzMap.put("exception", (Throwable) qzMap.get("exception"));
			return qzMap;
		}
		return qzMap;
	}
	
	/**
	 * 测试入口-main
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		
		/*
		 * 
		String xmlStr = "携带发票信息的xml字符串";
		xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><FPXT><RESPONSE_CODE>0000</RESPONSE_CODE><RESPONSE_TIP>上传成功</RESPONSE_TIP><RESPONSE_CONTENT><FP><PDF_MD5></PDF_MD5><CYJG_CODE>001</CYJG_CODE><CYJG_TIP>查验成功发票一致</CYJG_TIP><FPZL>10</FPZL><FPDM>1100162320</FPDM><FPHM>61355879</FPHM><CYCS>0</CYCS><XFSH>110112599688066</XFSH><XFMC>北京宏源永辉酒店管理有限公司</XFMC><XFDZDH></XFDZDH><XFYHZH>北京银行光机电园区支行01091669200120108001148</XFYHZH><GFSH> </GFSH><GFMC>北京东港嘉华安全信息技术有限公司</GFMC><GFDZDH> </GFDZDH><GFYHZH> </GFYHZH><KPRQ>20170122</KPRQ><JE>168.93</JE><SE>5.07</SE><JSHJ>174.00</JSHJ><BZ></BZ><KPR></KPR><SKR></SKR><FHR></FHR><JYM>16406981958494983504</JYM><LZFPDM></LZFPDM><LZFPHM></LZFPHM><ZFBZ>N</ZFBZ><XM><MXXX><MXXH></MXXH><MC>住宿服务</MC><JE>168.93</JE><SL>3</SL><SE>5.07</SE><HSDJ></HSDJ><HSJE></HSJE><SHUL>1</SHUL><DJ>168.93203883495147</DJ><GGXH> </GGXH><JLDW></JLDW></MXXX></XM></FP></RESPONSE_CONTENT></FPXT>";
		FPDistributed middleModel = XmlUtil.parseXML(xmlStr);
		
		//====================begin to connect model===============================
		
		List<FPData> data = middleModel.getData();
		for(FPData fp:data){
			JAR_FPQZ_KJ jar_fpqz_kj = new JAR_FPQZ_KJ();
			Fpkj kj = fp.getfP_KJ();
			jar_fpqz_kj.setGMF_DZDH(kj.getgMF_DZDH());
			jar_fpqz_kj.setGMF_YHZH(kj.getgMF_YHZH());
			jar_fpqz_kj.setGMF_MC(kj.getgMF_MC());
			jar_fpqz_kj.setGMF_NSRSBH(kj.getgMF_NSRSBH());
			jar_fpqz_kj.setJSHJ(kj.getjSHJ());
			jar_fpqz_kj.setHJJE(kj.gethJJE());
			jar_fpqz_kj.setHJSE(kj.gethJSE());
//			jar_fpqz_kj.setKPLX(kj.getkPLX());
			jar_fpqz_kj.setKPLX("1");
			jar_fpqz_kj.setBZ(kj.getbZ());
			//TODO Date error
//			jar_fpqz_kj.setKPRQ(kj.getkPRQ());
			jar_fpqz_kj.setKPRQ(new Date());
//			jar_fpqz_kj.setKPR(kj.getkPR());
			jar_fpqz_kj.setKPR("姚小军");
			jar_fpqz_kj.setSKR(kj.getsKR());
			jar_fpqz_kj.setFHR(kj.getfHR());
			jar_fpqz_kj.setXSF_YHZH(kj.getxSF_YHZH());
			jar_fpqz_kj.setXSF_DZDH(kj.getxSF_DZDH());
			jar_fpqz_kj.setXSF_MC(kj.getxSF_MC());
			jar_fpqz_kj.setXSF_NSRSBH(kj.getxSF_NSRSBH());
			//TODO   暂无字段
			jar_fpqz_kj
					.setFP_MW("279+9>*6+57</5571196<02*0555+++13*+521/787<714+*153/1*9*+04378/->74+13//92/0-2<3-<155/85+13*+521/787<774*531");
			jar_fpqz_kj.setJYM(kj.getjYM());
			jar_fpqz_kj.setEWM(kj.geteWM());
			jar_fpqz_kj.setFP_DM(kj.getfP_DM());
			jar_fpqz_kj.setFP_HM(kj.getfP_HM());
			jar_fpqz_kj.setJQBH(kj.getjQBH());
			jar_fpqz_kj.setBMB_BBH("1");
//			jar_fpqz_kj.setQD_BZ("1");
			jar_fpqz_kj.setQDXMMC("详见货物清单");
			//签章相关
			jar_fpqz_kj.setQZID("4169455300015209000000030000c927");
			jar_fpqz_kj.setMBDM("0000");
			jar_fpqz_kj.setIAC("");
			jar_fpqz_kj.setSJLY("09");
			jar_fpqz_kj.setDKBZ("0");
			jar_fpqz_kj.setSGBZ("");
			jar_fpqz_kj.setYFP_DM("");
			jar_fpqz_kj.setYFP_HM("");
			//发票明细
			List<FpKjmx> mx = fp.getfP_KJMX();
			List<JAR_FPQZ_KJMX> list = new ArrayList<JAR_FPQZ_KJMX>();
			for(FpKjmx m : mx){
				JAR_FPQZ_KJMX jar_fpqz_kjmx = new JAR_FPQZ_KJMX();
				jar_fpqz_kjmx.setXMMC(m.getxMMC());
				jar_fpqz_kjmx.setDW(m.getDw());
				jar_fpqz_kjmx.setGGXH(m.getgGXH());
				jar_fpqz_kjmx.setXMSL(m.getxMSL());
				jar_fpqz_kjmx.setXMDJ(m.getxMDJ());
				jar_fpqz_kjmx.setXMJE(m.getxMJE());
				jar_fpqz_kjmx.setSL(m.getsL());
				jar_fpqz_kjmx.setSE(m.getsE());
				jar_fpqz_kjmx.setFPHXZ(m.getfPHXZ());
				jar_fpqz_kjmx.setSPBM(m.getsPBM());
				jar_fpqz_kjmx.setZXBM(m.getzXBM());
				jar_fpqz_kjmx.setYHZCBS(m.getyHZCBS());
				jar_fpqz_kjmx.setLSLBS(m.getlSLBS());
				jar_fpqz_kjmx.setZZSTSGL(m.getzZSTSGL());
				jar_fpqz_kjmx.setKCE(m.getkCE());
				if(null != jar_fpqz_kjmx){
					list.add(jar_fpqz_kjmx);
				}
			}
			JAR_FPQZ_KJMX[] array = new JAR_FPQZ_KJMX[list.size()]; 
			list.toArray(array);
//			(JAR_FPQZ_KJMX[]) list.toArray();
			jar_fpqz_kj.setJAR_FPQZ_KJMXS(array);
			
			
			
			//==============================end connect======================================
			
			Map vmap = validatePdf(jar_fpqz_kj);
			if (1000 != ((Integer) vmap.get("code")).intValue()) {
				System.out.println(vmap.get("msg"));
			} else {
				Map map = pdfProduce(jar_fpqz_kj);
				if (1000 == ((Integer) map.get("code")).intValue()) {
					FileOutputStream file = new FileOutputStream(new File(
							"E:\\generateTest.pdf"));
					file.write((byte[]) map.get("pdfFile"));
					file.flush();
					file.close();
					System.out.println("succ");
				} else {
					System.out.println(map.get("msg"));
				}
			}
			
			*/
		
		JAR_FPQZ_KJ jar_fpqz_kj = new JAR_FPQZ_KJ();
		
		jar_fpqz_kj.setGMF_DZDH("北京市海淀区数码大厦A座30层");
		jar_fpqz_kj.setGMF_YHZH("海淀区中关村南大街59号18楼  021-39328876");
		jar_fpqz_kj.setGMF_MC("大象慧云信息技术有限公司");
		jar_fpqz_kj.setGMF_NSRSBH("91110108MA004CPN95");
		jar_fpqz_kj.setJSHJ("4");
		jar_fpqz_kj.setHJJE("4");
		jar_fpqz_kj.setHJSE("0");
		jar_fpqz_kj.setKPLX("0");
		jar_fpqz_kj.setBZ("中国建设银行中国建设银行中国建设银行中国建设银行中国建设银行中国建设银行中国建设银行中国建设银行中中国建设银行中国建设银行中国建设银行中国建设银行中中国建设银行中国建设银行中国建设银行");
		jar_fpqz_kj.setKPRQ(new Date());
		jar_fpqz_kj.setKPR("苏宁易购");
		jar_fpqz_kj.setSKR("管理员");
		jar_fpqz_kj.setFHR("管理员");
		jar_fpqz_kj.setXSF_YHZH("中国建设银行 111133141115211322");
		jar_fpqz_kj.setXSF_DZDH("南京市");
		jar_fpqz_kj.setXSF_MC("苏宁易购电子商务有限公司");
		jar_fpqz_kj.setXSF_NSRSBH("91110112575938948G");
		
		jar_fpqz_kj.setFP_MW("279+9>*6+57</5571196<02*0555+++13*+521/787<714+*153/1*9*+04378/->74+13//92/0-2<3-<155/85+13*+521/787<774*531");
		jar_fpqz_kj.setJYM("56944149623511400854");
		jar_fpqz_kj.setEWM("");
		jar_fpqz_kj.setFP_DM("011001600111");
		jar_fpqz_kj.setFP_HM("11735396");
		jar_fpqz_kj.setJQBH("161565171869");
		jar_fpqz_kj.setBMB_BBH("1");
		jar_fpqz_kj.setQD_BZ("0");
		jar_fpqz_kj.setQDXMMC("详见货物清单");
		jar_fpqz_kj.setQZID("4169455300015209000000030000c927");
		//模板
		jar_fpqz_kj.setMBDM("9990");
//		jar_fpqz_kj.setMBDM("0000");
		jar_fpqz_kj.setIAC("");
		jar_fpqz_kj.setSJLY("09");
		jar_fpqz_kj.setDKBZ("0");
		jar_fpqz_kj.setSGBZ("");
		jar_fpqz_kj.setYFP_DM("");
		jar_fpqz_kj.setYFP_HM("");
		
		JAR_FPQZ_KJMX jar_fpqz_kjmx = new JAR_FPQZ_KJMX();
		jar_fpqz_kjmx.setXMMC("【苏宁易购超市】仲景原味香菇酱210G 调味品 调料 下饭菜");
		jar_fpqz_kjmx.setDW("");
		jar_fpqz_kjmx.setGGXH("");
		jar_fpqz_kjmx.setXMSL("1");
		jar_fpqz_kjmx.setXMDJ("1");
		jar_fpqz_kjmx.setXMJE("1");
		jar_fpqz_kjmx.setSL("0");
		jar_fpqz_kjmx.setSE("0");
		jar_fpqz_kjmx.setFPHXZ("0");
		jar_fpqz_kjmx.setSPBM("1234567890123456789");
		jar_fpqz_kjmx.setZXBM("");
		jar_fpqz_kjmx.setYHZCBS("0");
		jar_fpqz_kjmx.setLSLBS("");
		jar_fpqz_kjmx.setZZSTSGL("");
		jar_fpqz_kjmx.setKCE("");
		
		JAR_FPQZ_KJMX jar_fpqz_kjmx2 = new JAR_FPQZ_KJMX();
		jar_fpqz_kjmx2.setXMMC("【苏宁易购超市】仲景原味香菇酱210G 调味品 调料 下饭菜");
		jar_fpqz_kjmx2.setDW("");
		jar_fpqz_kjmx2.setGGXH("");
		jar_fpqz_kjmx2.setXMSL("");
		jar_fpqz_kjmx2.setXMDJ("");
		jar_fpqz_kjmx2.setXMJE("-2.06");
		jar_fpqz_kjmx2.setSL("0.17");
		jar_fpqz_kjmx2.setSE("-0.35");
		jar_fpqz_kjmx2.setFPHXZ("0");
		jar_fpqz_kjmx2.setSPBM("1234567890123456789");
		jar_fpqz_kjmx2.setZXBM("");
		jar_fpqz_kjmx2.setYHZCBS("0");
		jar_fpqz_kjmx2.setLSLBS("");
		jar_fpqz_kjmx2.setZZSTSGL("");
		jar_fpqz_kjmx2.setKCE("");
		
		JAR_FPQZ_KJMX jar_fpqz_kjmx3 = new JAR_FPQZ_KJMX();
		jar_fpqz_kjmx3.setXMMC("配送费");
		jar_fpqz_kjmx3.setDW("");
		jar_fpqz_kjmx3.setGGXH("");
		jar_fpqz_kjmx3.setXMSL("1");
		jar_fpqz_kjmx3.setXMDJ("1.05128205");
		jar_fpqz_kjmx3.setXMJE("1.05");
		jar_fpqz_kjmx3.setSL("0.17");
		jar_fpqz_kjmx3.setSE("0.18");
		jar_fpqz_kjmx3.setFPHXZ("0");
		jar_fpqz_kjmx3.setSPBM("1234567890123456789");
		jar_fpqz_kjmx3.setZXBM("");
		jar_fpqz_kjmx3.setYHZCBS("0");
		jar_fpqz_kjmx3.setLSLBS("");
		jar_fpqz_kjmx3.setZZSTSGL("");
		jar_fpqz_kjmx3.setKCE("");
		
		JAR_FPQZ_KJMX jar_fpqz_kjmx4 = new JAR_FPQZ_KJMX();
		jar_fpqz_kjmx4.setXMMC("配送费");
		jar_fpqz_kjmx4.setDW("");
		jar_fpqz_kjmx4.setGGXH("");
		jar_fpqz_kjmx4.setXMSL("");
		jar_fpqz_kjmx4.setXMDJ("");
		jar_fpqz_kjmx4.setXMJE("-0.21");
		jar_fpqz_kjmx4.setSL("0.17");
		jar_fpqz_kjmx4.setSE("-0.04");
		jar_fpqz_kjmx4.setFPHXZ("0");
		jar_fpqz_kjmx4.setSPBM("1234567890123456789");
		jar_fpqz_kjmx4.setZXBM("");
		jar_fpqz_kjmx4.setYHZCBS("0");
		jar_fpqz_kjmx4.setLSLBS("");
		jar_fpqz_kjmx4.setZZSTSGL("");
		jar_fpqz_kjmx4.setKCE("");
		JAR_FPQZ_KJMX[] list = new JAR_FPQZ_KJMX[4];

		for (int i = 0; i < 4; i++) {
			list[i] = jar_fpqz_kjmx;
		}
		jar_fpqz_kj.setJAR_FPQZ_KJMXS(list);
		Map vmap = validatePdf(jar_fpqz_kj);
		if (1000 != ((Integer) vmap.get("code")).intValue()) {
			System.out.println(vmap.get("msg"));
		} else {
			Map map = pdfProduce(jar_fpqz_kj);
			if (1000 == ((Integer) map.get("code")).intValue()) {
				FileOutputStream file = new FileOutputStream(new File("F:\\8-line-0.pdf"));
//				FileOutputStream file = new FileOutputStream(new File("F:\\123.pdf"));
				file.write((byte[]) map.get("pdfFile"));
				file.flush();
				file.close();
				System.out.println("succ");
			} else {
				System.out.println(map.get("msg"));
			}
		}
		
	}
}