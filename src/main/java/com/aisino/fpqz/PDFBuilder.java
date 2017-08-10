package com.aisino.fpqz;

import com.aisino.fpqz.domain.JAR_FPQZ_KJ;
import com.aisino.fpqz.domain.JAR_FPQZ_KJMX;
import com.aisino.fpqz.domain.JAR_FPQZ_ZHMX;
import com.aisino.fpqz.exception.CustomException;
import com.aisino.fpqz.util.ArithUtil;
import com.aisino.fpqz.util.EWMUtil;
import com.aisino.fpqz.util.FontSize;
import com.aisino.fpqz.util.FontSizeUtil;
import com.aisino.fpqz.util.InvoiceGenUtil;
import com.aisino.fpqz.util.Money2CNUtil;
import com.aisino.fpqz.util.StringUtil;
import com.aisino.fpqz.util.ValidateUtil;
import com.google.common.base.Strings;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;

public class PDFBuilder {
	private static BaseFont fontSimsun;
	private static BaseFont fontCour;
	private static BaseFont fontCourNew;
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static void afterPropertiesSet() throws CustomException {
		try {
			fontSimsun = BaseFont.createFont(InvoiceGenUtil.simsun,	InvoiceGenUtil.encode, false);
			fontCour = BaseFont.createFont("Courier", "", false);
			fontCourNew = BaseFont.createFont(InvoiceGenUtil.courierNew, "",
					false);
		} catch (Exception e) {
			throw new CustomException(1001, new StringBuilder()
					.append("初始化字体错误:").append(e.getMessage()).toString(), e);
		}
	}

	public static Map<String, Object> bulidPdfA5(JAR_FPQZ_KJ kj)
			throws Exception {
		Map pdfMap = new HashMap();
		int qzsize = 0;
		afterPropertiesSet();
		JAR_FPQZ_KJMX[] kjmxs = kj.getJAR_FPQZ_KJMXS();
		JAR_FPQZ_ZHMX[] zhmxs = recombination(kjmxs);
		boolean isQd = (zhmxs.length > 8)|| (InvoiceGenUtil.QDBZ_QZQD.equals(kj.getQD_BZ()));
		int defaultFontSize = InvoiceGenUtil.DEFUALT_FONTSIZE;
		int mmqFontSize = InvoiceGenUtil.MMQ_FONTSIZE;
		int nsrsbhFontSize = InvoiceGenUtil.NSRSBH_FONTSIZE;
		int numberFontSize = InvoiceGenUtil.NUMBER_FONTSIZE;
		PdfReader reader = null;
		ByteArrayOutputStream out = null;
		PdfStamper ps = null;
		try {
			if ((null == kj.getMB_A5()) || (kj.getMB_A5().length == 0))
				reader = new PdfReader(new StringBuilder().append(InvoiceGenUtil.MB_A5_URL)
						
						.append(Strings.isNullOrEmpty(kj.getMBDM()) ? "0000" : kj.getMBDM()).append(".pdf").toString());	
			
			else {
				reader = new PdfReader(kj.getMB_A5());
			}
			out = new ByteArrayOutputStream();
			ps = new PdfStamper(reader, out);
		} catch (Exception e) {
			throw new CustomException(1002, new StringBuilder()
					.append("初始化发票模板错误:").append(e.getMessage()).toString(), e);
		}
		PdfContentByte pcb = ps.getOverContent(1);

		byte[] imageData = null;
		try {
			String ewm = "";
			if ((Strings.isNullOrEmpty(kj.getEWM()))
					|| ("09".equals(kj.getSJLY())))
				ewm = EWMUtil.generateTwoDimCode(kj.getFP_DM(),
						StringUtil.addZero(kj.getFP_HM(), 8), kj.getHJJE(),
						kj.getKPRQ(), kj.getJYM());
			else {
				ewm = kj.getEWM();
			}
			imageData = new Base64().decode(ewm.getBytes());
			Image ewmImage = Image.getInstance(imageData);
			ewmImage.setAbsolutePosition(40.0F, 335.0F);
			ewmImage.scaleAbsolute(50.0F, 50.0F);
			ewmImage.setAlignment(2);
			pcb.addImage(ewmImage);
		} catch (Exception e) {
			throw new CustomException(1003, new StringBuilder()
					.append("生成二维码错误:").append(e.getMessage()).append("(")
					.append(kj.getEWM()).append(")").toString(), e);
		}

		pcb.beginText();

		String tcnr = new StringBuilder()
				.append(InvoiceGenUtil.DKBZ_DKFP.equals(kj.getDKBZ()) ? "代开 "
						: "")
				.append(InvoiceGenUtil.SGBZ_SGFP.equals(kj.getSGBZ()) ? "收购 "
						: "")
				.append(InvoiceGenUtil.KPLX_HZFP.equals(kj.getKPLX()) ? "销项负数"
						: "").toString();
		if (!Strings.isNullOrEmpty(tcnr)) {
			pcb.setFontAndSize(fontSimsun, 15.0F);
			pcb.showTextAligned(3, tcnr.trim(), 105.0F, 335.0F, 0.0F);
		}

		pcb.setFontAndSize(fontSimsun, defaultFontSize);

		pcb.showTextAligned(3, kj.getFP_DM(), 475.0F, 374.0F, 0.0F);

		pcb.showTextAligned(3, StringUtil.addZero(kj.getFP_HM(), 8), 475.0F,
				356.0F, 0.0F);

		String kprq = sdf.format(kj.getKPRQ());
		pcb.setFontAndSize(fontCour, defaultFontSize);
		pcb.showTextAligned(
				3,
				new StringBuilder().append(kprq.substring(0, 4)).append("  ")
						.append(kprq.substring(5, 7)).append("  ")
						.append(kprq.substring(8, 10)).toString(), 477.0F,
				339.0F, 0.0F);

		pcb.setFontAndSize(fontSimsun, defaultFontSize);
		int len = 0;
		String source = kj.getJYM();
		String target = "";
		for (int i = 0; i < source.length(); i++) {
			target = new StringBuilder().append(target)
					.append(source.substring(len, len + 1)).toString();
			len++;
			if (len % 5 == 0) {
				target = new StringBuilder().append(target).append("  ")
						.toString();
			}
		}
		pcb.showTextAligned(3, target, 475.0F, 321.0F, 0.0F);

		pcb.setFontAndSize(fontCourNew, mmqFontSize);
		String mw = kj.getFP_MW();
		pcb.showTextAligned(1, mw.substring(0, mw.length() / 4), 478.0F,
				298.0F, 0.0F);
		pcb.showTextAligned(1,
				mw.substring(mw.length() / 4, mw.length() / 4 * 2), 478.0F,
				283.0F, 0.0F);
		pcb.showTextAligned(1,
				mw.substring(mw.length() / 4 * 2, mw.length() / 4 * 3), 478.0F,
				268.0F, 0.0F);
		pcb.showTextAligned(1, mw.substring(mw.length() / 4 * 3, mw.length()),
				478.0F, 253.0F, 0.0F);

		pcb.setFontAndSize(fontSimsun, defaultFontSize);
		pcb.showTextAligned(3, kj.getJQBH(), 75.0F, 322.0F, 0.0F);

		String gmf_mc = InvoiceGenUtil.SGBZ_SGFP.equals(kj.getSGBZ()) ? kj
				.getXSF_MC() : kj.getGMF_MC();
		pcb.setFontAndSize(fontSimsun, FontSizeUtil.getFontSize(gmf_mc));
		pcb.showTextAligned(3, gmf_mc, 108.0F, 300.0F, 0.0F);

		String gmf_nsrsbh = InvoiceGenUtil.SGBZ_SGFP.equals(kj.getSGBZ()) ? kj
				.getXSF_NSRSBH() : kj.getGMF_NSRSBH();
		if (!Strings.isNullOrEmpty(gmf_nsrsbh)) {
			pcb.setFontAndSize(fontCourNew, nsrsbhFontSize);
			pcb.showTextAligned(3, gmf_nsrsbh, 108.0F, 284.0F, 0.0F);
		}

		String gmf_dzdh = InvoiceGenUtil.SGBZ_SGFP.equals(kj.getSGBZ()) ? kj
				.getXSF_DZDH() : kj.getGMF_DZDH();
		if (!Strings.isNullOrEmpty(gmf_dzdh)) {
			pcb.setFontAndSize(fontSimsun, FontSizeUtil.getFontSize(gmf_dzdh));
			pcb.showTextAligned(3, gmf_dzdh, 108.0F, 270.0F, 0.0F);
		}

		String gmf_yhzh = InvoiceGenUtil.SGBZ_SGFP.equals(kj.getSGBZ()) ? kj
				.getXSF_YHZH() : kj.getGMF_YHZH();
		if (!Strings.isNullOrEmpty(gmf_yhzh)) {
			pcb.setFontAndSize(fontSimsun, FontSizeUtil.getFontSize(gmf_yhzh));
			pcb.showTextAligned(3, gmf_yhzh, 108.0F, 254.0F, 0.0F);
		}

		pcb.setFontAndSize(fontCourNew, numberFontSize);
		pcb.showTextAligned(
				2,
				new StringBuilder().append("¥")
						.append(ArithUtil.fdd(kj.getHJJE(), 2, 2)).toString(),
				478.0F, 126.0F, 0.0F);

		boolean bl = false;

		BigDecimal sl = BigDecimal.ZERO;
		String slStr = "";
		for (int i = 0; i < kjmxs.length; i++) {
			if (i == 0) {
				sl = new BigDecimal(kjmxs[i].getSL());
				if (sl.compareTo(BigDecimal.ZERO) == 0) {
					slStr = ValidateUtil.getSlStr(kj.getBMB_BBH(),
							kjmxs[i].getZZSTSGL());
					bl = true;
				} else {
					slStr = new StringBuilder()
							.append(ArithUtil.fdd(
									sl.multiply(new BigDecimal("100")), 0, 0))
							.append("%").toString();
				}
			} else if (sl.compareTo(new BigDecimal(kjmxs[i].getSL())) != 0) {
				bl = false;
				slStr = "";
				break;
			}

		}

		pcb.setFontAndSize(fontCourNew, numberFontSize);
		if (bl)
			pcb.showTextAligned(2, "***", 590.0F, 126.0F, 0.0F);
		else {
			pcb.showTextAligned(
					2,
					new StringBuilder().append("¥")
							.append(ArithUtil.fdd(kj.getHJSE(), 2, 2))
							.toString(), 590.0F, 126.0F, 0.0F);
		}

		BigDecimal jshj = new BigDecimal(kj.getHJJE()).add(new BigDecimal(kj
				.getHJSE()));
		pcb.setFontAndSize(fontCourNew, numberFontSize);
		pcb.showTextAligned(
				3,
				new StringBuilder().append("¥")
						.append(ArithUtil.fdd(jshj, 2, 2)).toString(), 474.0F,
				110.0F, 0.0F);

		pcb.setFontAndSize(fontSimsun, defaultFontSize);
		pcb.showTextAligned(3, Money2CNUtil.money2CN(jshj), 190.0F, 110.0F,
				0.0F);

		String xsf_mc = InvoiceGenUtil.SGBZ_SGFP.equals(kj.getSGBZ()) ? kj
				.getGMF_MC() : kj.getXSF_MC();
		pcb.setFontAndSize(
				fontSimsun,
				InvoiceGenUtil.DKBZ_DKFP.equals(kj.getDKBZ()) ? FontSizeUtil
						.getFontSizeDK(xsf_mc) : FontSizeUtil
						.getFontSize(xsf_mc));
		pcb.showTextAligned(3, xsf_mc, 108.0F, 91.0F, 0.0F);

		String xsf_nsrsbh = InvoiceGenUtil.SGBZ_SGFP.equals(kj.getSGBZ()) ? kj
				.getGMF_NSRSBH() : kj.getXSF_NSRSBH();
		if (!Strings.isNullOrEmpty(xsf_nsrsbh)) {
			pcb.setFontAndSize(fontCourNew, nsrsbhFontSize);
			pcb.showTextAligned(3, xsf_nsrsbh, 108.0F, 76.0F, 0.0F);
		}

		String xsf_dzdh = InvoiceGenUtil.SGBZ_SGFP.equals(kj.getSGBZ()) ? kj
				.getGMF_DZDH() : kj.getXSF_DZDH();
		if (!Strings.isNullOrEmpty(xsf_dzdh)) {
			pcb.setFontAndSize(fontSimsun, FontSizeUtil.getFontSize(xsf_dzdh));
			pcb.showTextAligned(3, xsf_dzdh, 108.0F, 63.0F, 0.0F);
		}

		String xsf_yhzh = InvoiceGenUtil.SGBZ_SGFP.equals(kj.getSGBZ()) ? kj
				.getGMF_YHZH() : kj.getXSF_YHZH();
		if (!Strings.isNullOrEmpty(xsf_yhzh)) {
			pcb.setFontAndSize(fontSimsun, InvoiceGenUtil.DKBZ_DKFP.equals(kj
					.getDKBZ()) ? FontSizeUtil.getFontSizeDK(xsf_yhzh)
					: FontSizeUtil.getFontSize(xsf_yhzh));
			pcb.showTextAligned(3, xsf_yhzh, 108.0F, 49.0F, 0.0F);
		}

		if (InvoiceGenUtil.DKBZ_DKFP.equals(kj.getDKBZ())) {
			pcb.setFontAndSize(fontSimsun, defaultFontSize);
			pcb.showTextAligned(3, "(代开机关)", 305.0F, 91.0F, 0.0F);
			pcb.showTextAligned(3, "(代开机关)", 305.0F, 77.0F, 0.0F);
			pcb.showTextAligned(3, "(完税凭证号)", 296.0F, 50.0F, 0.0F);
		}

		if (!Strings.isNullOrEmpty(kj.getBZ())) {
			FontSize font = FontSizeUtil.getFontSizeForBZ(StringUtil.substring(
					kj.getBZ(), 230));
			pcb.setFontAndSize(fontSimsun, font.size());
			String[] bzs = StringUtil.substringToArry(
					StringUtil.substring(kj.getBZ(), 230), font.width());
			int bzy = 92;
			int row = 0;
			int spacing = 10;
			for (int i = 0; i < defaultFontSize - font.size(); i++) {
				spacing--;
			}
			for (String bz : bzs) {
				int bzCoordinate = bzy - row * spacing;
				pcb.showTextAligned(3, bz, 370.0F, bzCoordinate, 0.0F);
				row++;
			}
		}
		pcb.setFontAndSize(fontSimsun, defaultFontSize);

		if (!Strings.isNullOrEmpty(kj.getSKR())) {
			pcb.showTextAligned(3, StringUtil.substring(kj.getSKR(), 8), 65.0F,
					30.0F, 0.0F);
		}
		if (!Strings.isNullOrEmpty(kj.getFHR())) {
			pcb.showTextAligned(3, StringUtil.substring(kj.getFHR(), 8),
					215.0F, 30.0F, 0.0F);
		}
		pcb.showTextAligned(3, StringUtil.substring(kj.getKPR(), 8), 347.0F,
				30.0F, 0.0F);

		List qdList = new ArrayList();

		if (!isQd) {
			bulidA5Mx(pcb, kj, zhmxs);
			pcb.endText();
			ps.close();
		} else {
			JAR_FPQZ_KJMX qdxmmc = new JAR_FPQZ_KJMX();
			if (InvoiceGenUtil.QDBZ_QZQD.equals(kj.getQD_BZ())) {
				qdxmmc.setXMMC(kj.getQDXMMC());
			} else if (InvoiceGenUtil.KPLX_LZFP.equals(kj.getKPLX()))
				qdxmmc.setXMMC("(详见销货清单)");
			else if (InvoiceGenUtil.KPLX_HZFP.equals(kj.getKPLX())) {
				qdxmmc.setXMMC("详见对应正数发票及清单");
			}

			qdxmmc.setXMJE(ArithUtil.fdd(kj.getHJJE(), 2, 2));
			if (bl)
				qdxmmc.setSE("***");
			else {
				qdxmmc.setSE(ArithUtil.fdd(kj.getHJSE(), 2, 2));
			}

			qdxmmc.setSL(slStr);
			bulidQdMx(pcb, recombination(new JAR_FPQZ_KJMX[] { qdxmmc }));
			pcb.endText();
			ps.close();
			qdList.add(out.toByteArray());
			int size = 35;
			int mxLength = zhmxs.length;
			List genQd = new ArrayList();
			int start = 0;
			int pagelen = size;
			while (start + pagelen < mxLength) {
				while (zhmxs[(start + pagelen)].getZhs() != 0) {
					pagelen--;
				}
				JAR_FPQZ_ZHMX[] qdmxs = null;
				qdmxs = new JAR_FPQZ_ZHMX[pagelen];
				System.arraycopy(zhmxs, start, qdmxs, 0, pagelen);
				genQd.add(qdmxs);
				start += pagelen;
				pagelen = size;
			}
			pagelen = mxLength - start;
			JAR_FPQZ_ZHMX[] qdmxs = new JAR_FPQZ_ZHMX[mxLength - start];
			System.arraycopy(zhmxs, start, qdmxs, 0, pagelen);
			genQd.add(qdmxs);
			qzsize = genQd.size();
			for (int page = 0; page < genQd.size(); page++) {
				qdList.add(bulidQdMx(kj, (JAR_FPQZ_ZHMX[]) genQd.get(page),
						page + 1, genQd.size()));
			}
		}

		if (isQd) {
			out = new ByteArrayOutputStream();
			mergePdfFiles(qdList, out);
		}
		byte[] data = null;
		data = out.toByteArray();
		if (out != null) {
			out.close();
		}
		pdfMap.put("fileByte", data);
		pdfMap.put("qdsize", Integer.valueOf(qzsize));
		return pdfMap;
	}

	private static void bulidA5Mx(PdfContentByte pcb, JAR_FPQZ_KJ kj,
			JAR_FPQZ_ZHMX[] zhmxs) throws CustomException {
		int fontSize = InvoiceGenUtil.DEFUALT_FONTSIZE;
		for (int i = 0; i < zhmxs.length; i++) {
			JAR_FPQZ_ZHMX mx = zhmxs[i];
			int y = 225 - 12 * i;
			pcb.setFontAndSize(fontSimsun, fontSize);
			pcb.showTextAligned(3, mx.getXMMC(), 27.0F, y, 0.0F);
			if ((!Strings.isNullOrEmpty(mx.getGGXH())) && (!isZkh(kj, mx))) {
				FontSize font = mx.getZhs() > 0 ? FontSizeUtil
						.getFontSizeForGgxh(zhmxs[(i - 1)].getGGXH())
						: FontSizeUtil.getFontSizeForGgxh(mx.getGGXH());
				pcb.setFontAndSize(fontSimsun, font.size());
				pcb.showTextAligned(3, mx.getGGXH(), 178.0F, y, 0.0F);
			}
			if ((!Strings.isNullOrEmpty(mx.getDW())) && (!isZkh(kj, mx))) {
				FontSize font = mx.getZhs() > 0 ? FontSizeUtil
						.getFontSizeForDw(zhmxs[(i - 1)].getDW())
						: FontSizeUtil.getFontSizeForDw(mx.getDW());
				pcb.setFontAndSize(fontSimsun, font.size());
				pcb.showTextAligned(1, mx.getDW(), 238.0F, y, 0.0F);
			}
			pcb.setFontAndSize(fontSimsun, fontSize);
			pcb.showTextAligned(
					2,
					(Strings.isNullOrEmpty(mx.getXMSL())) || (isZkh(kj, mx)) ? ""
							: ArithUtil.fdd(mx.getXMSL(), 8, 0), 317.0F, y,
					0.0F);
			pcb.showTextAligned(
					2,
					(Strings.isNullOrEmpty(mx.getXMDJ())) || (isZkh(kj, mx)) ? ""
							: ArithUtil.fdd(mx.getXMDJ(), 8, 2), 390.0F, y,
					0.0F);
			if (!Strings.isNullOrEmpty(mx.getXMJE()))
				pcb.showTextAligned(2, ArithUtil.fdd(mx.getXMJE(), 2, 2),
						478.0F, y, 0.0F);
			if (!Strings.isNullOrEmpty(mx.getSL())) {
				String slStr = "";
				if (Strings.isNullOrEmpty(zhmxs[0].getKCE())) {
					if (new BigDecimal(mx.getSL()).compareTo(BigDecimal.ZERO) == 0)
						slStr = ValidateUtil.getSlStr(kj.getBMB_BBH(),
								mx.getZZSTSGL());
					else
						slStr = new StringBuilder()
								.append(ArithUtil.fdd(
										new BigDecimal(mx.getSL())
												.multiply(new BigDecimal("100")),
										1, 0)).append("%").toString();
				} else {
					slStr = "***";
				}
				FontSize font = FontSizeUtil.getFontSizeForSl(slStr);
				pcb.setFontAndSize(fontSimsun, font.size());
				pcb.showTextAligned(2, slStr, 502.0F, y, 0.0F);
			}
			if ((!Strings.isNullOrEmpty(mx.getSE()))
					&& (!Strings.isNullOrEmpty(mx.getSL()))) {
				pcb.setFontAndSize(fontSimsun, fontSize);
				pcb.showTextAligned(
						2,
						new BigDecimal(mx.getSL()).compareTo(BigDecimal.ZERO) == 0 ? "***"
								: ArithUtil.fdd(mx.getSE(), 2, 2), 590.0F, y,
						0.0F);
			}
		}
	}

	private static byte[] bulidQdMx(JAR_FPQZ_KJ kj, JAR_FPQZ_ZHMX[] zhmxs,
			int currPage, int totalPage) throws CustomException, Exception {
		int y = 595;
		JAR_FPQZ_KJMX[] kjmxs = kj.getJAR_FPQZ_KJMXS();
		int fontSize = InvoiceGenUtil.DEFUALT_FONTSIZE;
		PdfReader reader = null;
		ByteArrayOutputStream out = null;
		PdfStamper ps = null;
		try {
			if ((null == kj.getMB_LIST()) || (kj.getMB_LIST().length == 0))
				reader = new PdfReader(InvoiceGenUtil.MB_QD_URL);
			else {
				reader = new PdfReader(kj.getMB_LIST());
			}
			out = new ByteArrayOutputStream();
			ps = new PdfStamper(reader, out);
		} catch (Exception e) {
			throw new CustomException(1008, "初始化清单模板错误", e);
		}
		PdfContentByte pcb = ps.getOverContent(1);
		pcb.beginText();

		pcb.setFontAndSize(fontSimsun,
				FontSizeUtil.getFontSizeForQd(kj.getGMF_MC()));
		pcb.showTextAligned(3, kj.getGMF_MC(), 88.0F, 692.0F, 0.0F);

		pcb.setFontAndSize(fontSimsun,
				FontSizeUtil.getFontSizeForQd(kj.getXSF_MC()));
		pcb.showTextAligned(3, kj.getXSF_MC(), 88.0F, 668.0F, 0.0F);

		pcb.setFontAndSize(fontSimsun, fontSize);
		pcb.showTextAligned(3, kj.getFP_DM(), 170.0F, 644.0F, 0.0F);

		pcb.showTextAligned(3, StringUtil.addZero(kj.getFP_HM(), 8), 290.0F,
				644.0F, 0.0F);

		pcb.showTextAligned(1, new StringBuilder().append(totalPage).append("")
				.toString(), 485.0F, 644.0F, 0.0F);

		pcb.showTextAligned(1, new StringBuilder().append(currPage).append("")
				.toString(), 547.0F, 644.0F, 0.0F);

		BigDecimal xjje = BigDecimal.ZERO;
		BigDecimal xjse = BigDecimal.ZERO;
		for (int i = 0; i < zhmxs.length; i++) {
			JAR_FPQZ_ZHMX mx = zhmxs[i];
			pcb.setFontAndSize(fontSimsun, fontSize);
			pcb.showTextAligned(1, mx.getXh(), 40.0F, y, 0.0F);
			pcb.showTextAligned(3, mx.getXMMC(), 55.0F, y, 0.0F);
			if ((!Strings.isNullOrEmpty(mx.getGGXH())) && (!isZkh(kj, mx))) {
				FontSize font = mx.getZhs() > 0 ? FontSizeUtil
						.getFontSizeForGgxh(zhmxs[(i - 1)].getGGXH())
						: FontSizeUtil.getFontSizeForGgxh(mx.getGGXH());
				pcb.setFontAndSize(fontSimsun, font.size());
				pcb.showTextAligned(3, mx.getGGXH(), 220.0F, y, 0.0F);
			}
			if ((!Strings.isNullOrEmpty(mx.getDW())) && (!isZkh(kj, mx))) {
				FontSize font = mx.getZhs() > 0 ? FontSizeUtil
						.getFontSizeForDw(zhmxs[(i - 1)].getDW())
						: FontSizeUtil.getFontSizeForDw(mx.getDW());
				pcb.setFontAndSize(fontSimsun, font.size());
				pcb.showTextAligned(1, mx.getDW(), 286.0F, y, 0.0F);
			}
			pcb.setFontAndSize(fontSimsun, fontSize);
			pcb.showTextAligned(
					2,
					(Strings.isNullOrEmpty(mx.getXMSL())) || (isZkh(kj, mx)) ? ""
							: ArithUtil.fdd(mx.getXMSL(), 8, 0), 360.0F, y,
					0.0F);
			pcb.showTextAligned(
					2,
					(Strings.isNullOrEmpty(mx.getXMDJ())) || (isZkh(kj, mx)) ? ""
							: ArithUtil.fdd(mx.getXMDJ(), 8, 2), 420.0F, y,
					0.0F);
			if (!Strings.isNullOrEmpty(mx.getXMJE())) {
				pcb.showTextAligned(2, ArithUtil.fdd(mx.getXMJE(), 2, 2),
						490.0F, y, 0.0F);
				xjje = xjje.add(new BigDecimal(mx.getXMJE()));
			}
			if (!Strings.isNullOrEmpty(mx.getSL())) {
				String slStr = "";
				if (Strings.isNullOrEmpty(zhmxs[0].getKCE())) {
					if (new BigDecimal(mx.getSL()).compareTo(BigDecimal.ZERO) == 0)
						slStr = ValidateUtil.getSlStr(kj.getBMB_BBH(),
								mx.getZZSTSGL());
					else
						slStr = new StringBuilder()
								.append(ArithUtil.fdd(
										new BigDecimal(mx.getSL())
												.multiply(new BigDecimal("100")),
										1, 0)).append("%").toString();
				} else {
					slStr = "***";
				}
				FontSize font = FontSizeUtil.getFontSizeForSl(slStr);
				pcb.setFontAndSize(fontSimsun, font.size());
				pcb.showTextAligned(2, slStr, 512.0F, y, 0.0F);
			}
			if ((!Strings.isNullOrEmpty(mx.getSE()))
					&& (!Strings.isNullOrEmpty(mx.getSL()))) {
				pcb.setFontAndSize(fontSimsun, fontSize);
				pcb.showTextAligned(
						2,
						new BigDecimal(mx.getSL()).compareTo(BigDecimal.ZERO) == 0 ? "***"
								: ArithUtil.fdd(mx.getSE(), 2, 2), 585.0F, y,
						0.0F);
				xjse = xjse.add(new BigDecimal(mx.getSE()));
			}
			y -= 12;
		}
		pcb.setFontAndSize(fontSimsun, fontSize);
		pcb.showTextAligned(
				2,
				new StringBuilder().append("¥")
						.append(ArithUtil.fdd(xjje, 2, 2)).toString(), 490.0F,
				163.0F, 0.0F);

		boolean bl = false;
		int count = 0;
		for (int i = 0; i < kjmxs.length; i++) {
			if (new BigDecimal(kjmxs[i].getSL()).compareTo(BigDecimal.ZERO) == 0) {
				count++;
			}
		}
		if (count == kjmxs.length) {
			bl = true;
		}

		if (bl)
			pcb.showTextAligned(2, "***", 585.0F, 163.0F, 0.0F);
		else {
			pcb.showTextAligned(
					2,
					new StringBuilder().append("¥")
							.append(ArithUtil.fdd(xjse, 2, 2)).toString(),
					585.0F, 163.0F, 0.0F);
		}

		pcb.showTextAligned(
				2,
				new StringBuilder().append("¥")
						.append(ArithUtil.fdd(kj.getHJJE(), 2, 2)).toString(),
				490.0F, 151.0F, 0.0F);

		if (bl)
			pcb.showTextAligned(2, "***", 585.0F, 151.0F, 0.0F);
		else {
			pcb.showTextAligned(
					2,
					new StringBuilder().append("¥")
							.append(ArithUtil.fdd(kj.getHJSE(), 2, 2))
							.toString(), 585.0F, 151.0F, 0.0F);
		}

		if (!Strings.isNullOrEmpty(kj.getBZ())) {
			FontSize font = FontSizeUtil.getFontSizeForQDBZ(StringUtil
					.substring(kj.getBZ(), 230));
			pcb.setFontAndSize(fontSimsun, font.size());
			String[] bzs = StringUtil.substringToArry(
					StringUtil.substring(kj.getBZ(), 230), font.width());
			int bzY = 12;
			int bzyz = 137;
			if (bzs.length == 1) {
				pcb.showTextAligned(3, StringUtil.substring(kj.getBZ(), 230),
						53.0F, bzyz, 0.0F);
			} else {
				for (int i = 0; i < bzs.length; i++) {
					bzY--;
				}
				for (int k = 0; k < bzs.length; k++) {
					String bz = bzs[k];
					if (k != 0) {
						bzyz -= bzY;
					}
					pcb.showTextAligned(3, bz, 53.0F, bzyz, 0.0F);
				}
			}
		}

		pcb.setFontAndSize(fontSimsun, fontSize);
		String kprqStr = sdf.format(kj.getKPRQ());
		pcb.showTextAligned(
				3,
				new StringBuilder().append(kprqStr.substring(0, 4))
						.append("        ").append(kprqStr.subSequence(5, 7))
						.append("     ").append(kprqStr.subSequence(8, 10))
						.toString(), 460.0F, 88.0F, 0.0F);

		pcb.endText();
		ps.close();
		byte[] pdfBytes = out.toByteArray();
		if (out != null) {
			out.close();
		}
		return pdfBytes;
	}

	private static void bulidQdMx(PdfContentByte pcb, JAR_FPQZ_ZHMX[] zhmxs)
			throws CustomException {
		int defaultFontSize = InvoiceGenUtil.DEFUALT_FONTSIZE;
		for (int i = 0; i < zhmxs.length; i++) {
			JAR_FPQZ_ZHMX mx = zhmxs[i];
			int y = 225 - 12 * i;
			pcb.setFontAndSize(fontSimsun, defaultFontSize);
			pcb.showTextAligned(3, mx.getXMMC(), 27.0F, y, 0.0F);
			pcb.setFontAndSize(fontSimsun, defaultFontSize);
			if (!Strings.isNullOrEmpty(mx.getXMJE()))
				pcb.showTextAligned(2, mx.getXMJE(), 478.0F, y, 0.0F);
			if (!Strings.isNullOrEmpty(mx.getSL())) {
				FontSize font = FontSizeUtil.getFontSizeForSl(mx.getSL());
				pcb.setFontAndSize(fontSimsun, font.size());
				pcb.showTextAligned(2, mx.getSL(), 502.0F, y, 0.0F);
			}
			pcb.setFontAndSize(fontSimsun, defaultFontSize);
			if (!Strings.isNullOrEmpty(mx.getSE()))
				pcb.showTextAligned(2, mx.getSE(), 590.0F, y, 0.0F);
		}
	}

	private static boolean mergePdfFiles(List<byte[]> files,
			ByteArrayOutputStream output) throws CustomException {
		try {
			Document document = new Document();
			PdfCopy copy = new PdfCopy(document, output);
			document.open();
			PdfReader reader;
			int page;
			for (int i = 0; i < files.size(); i++) {
				reader = new PdfReader((byte[]) files.get(i));
				int n = reader.getNumberOfPages();
				for (page = 0; page < n;) {
					document.newPage();
					copy.addPage(copy.getImportedPage(reader, ++page));
				}
			}
			document.close();
		} catch (Exception e) {
			throw new CustomException(1009, "清单合并错误", e);
		}
		return true;
	}

	private static JAR_FPQZ_ZHMX[] recombination(JAR_FPQZ_KJMX[] mxs)
			throws CustomException {
		List mxList = new ArrayList();
		int xh = 0;
		for (JAR_FPQZ_KJMX mx : mxs) {
			/**
			 * 商品名称s
			 */
			String[] spmcs = null;
			/**
			 * 规格型号s
			 */
			String[] ggxhs = null;
			/**
			 * 单位s
			 */
			String[] dws = null;
			int len = 0;

			spmcs = StringUtil.substringToArry(
					StringUtil.substring(mx.getXMMC(), 90),
					InvoiceGenUtil.SPH_SPMC_LENGTH);
			len = spmcs.length;
			if (!Strings.isNullOrEmpty(mx.getGGXH())) {
				ggxhs = StringUtil.substringToArry(
						StringUtil.substring(mx.getGGXH(), 40),
						InvoiceGenUtil.SPH_GGXH_LENGTH);
				len = ggxhs.length > len ? ggxhs.length : len;
			}
			if (!Strings.isNullOrEmpty(mx.getDW())) {
				dws = StringUtil.substringToArry(
						StringUtil.substring(mx.getDW(), 22),
						InvoiceGenUtil.SPH_DW_LENGTH);
				len = dws.length > len ? dws.length : len;
			}
			for (int i = 0; i < len; i++) {
				if (i == 0) {
					JAR_FPQZ_ZHMX zhmx = new JAR_FPQZ_ZHMX();
					zhmx.setXh(String.valueOf(++xh));
					zhmx.setZhs(i);
					zhmx.setXMMC(null == spmcs ? mx.getXMMC() : spmcs[i]);
					zhmx.setDW(null == dws ? mx.getDW() : dws[i]);
					zhmx.setGGXH(null == ggxhs ? mx.getGGXH() : ggxhs[i]);
					String zhsl = "";
					if (Strings.isNullOrEmpty(mx.getXMSL())) {
						if ((!Strings.isNullOrEmpty(mx.getXMDJ()))
								&& (new BigDecimal(mx.getXMDJ())
										.compareTo(BigDecimal.ZERO) != 0))
							zhsl = ArithUtil
									.fdd(new BigDecimal(mx.getXMJE()).divide(
											new BigDecimal(mx.getXMDJ()), 8, 4),
											8, 0);
					} else if (new BigDecimal(mx.getXMSL())
							.compareTo(BigDecimal.ZERO) != 0) {
						zhsl = mx.getXMSL();
					}
					zhmx.setXMSL(zhsl);
					String zhdj = "";
					if (Strings.isNullOrEmpty(mx.getXMDJ())) {
						if ((!Strings.isNullOrEmpty(mx.getXMSL()))
								&& (new BigDecimal(mx.getXMSL())
										.compareTo(BigDecimal.ZERO) != 0))
							zhdj = ArithUtil
									.fdd(new BigDecimal(mx.getXMJE()).divide(
											new BigDecimal(mx.getXMSL()), 8, 4),
											8, 0);
					} else if (new BigDecimal(mx.getXMDJ())
							.compareTo(BigDecimal.ZERO) != 0) {
						zhdj = mx.getXMDJ();
					}
					zhmx.setXMDJ(zhdj);
					zhmx.setXMJE(mx.getXMJE());
					zhmx.setSL(mx.getSL());
					zhmx.setSE(mx.getSE());
					zhmx.setZZSTSGL(mx.getZZSTSGL());
					zhmx.setKCE(mx.getKCE());
					mxList.add(zhmx);
				} else {
					JAR_FPQZ_ZHMX zhmx = new JAR_FPQZ_ZHMX();
					zhmx.setXh("");
					zhmx.setZhs(i);
					zhmx.setXMMC((null != spmcs) && (i < spmcs.length) ? spmcs[i]
							: "");
					zhmx.setGGXH((null != ggxhs) && (i < ggxhs.length) ? ggxhs[i]
							: "");
					zhmx.setDW((null != dws) && (i < dws.length) ? dws[i] : "");
					mxList.add(zhmx);
				}
			}
		}
		JAR_FPQZ_ZHMX[] zhmxs = new JAR_FPQZ_ZHMX[mxList.size()];
		mxList.toArray(zhmxs);
		return zhmxs;
	}

	private static boolean isZkh(JAR_FPQZ_KJ kj, JAR_FPQZ_ZHMX mx) {
		boolean flag = false;
		if (!Strings.isNullOrEmpty(mx.getXMJE())) {
			if ((kj.getKPLX().equals(InvoiceGenUtil.KPLX_LZFP))
					&& (!Strings.isNullOrEmpty(mx.getXMJE()))
					&& (new BigDecimal(mx.getXMJE()).compareTo(BigDecimal.ZERO) == -1))
				flag = true;
			if ((kj.getKPLX().equals(InvoiceGenUtil.KPLX_HZFP))
					&& (new BigDecimal(mx.getXMJE()).compareTo(BigDecimal.ZERO) == 1))
				flag = true;
		}
		return flag;
	}
}