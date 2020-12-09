package com.cc.etherscan.io.util;

import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Tess4jUtils类
 * <p>
 *
 * @author wxd(ttzommed @ foxmail.com)
 */
@UtilityClass
@Slf4j
public class Tess4jUtils {
	//OCR识别
	private static ITesseract instance = new Tesseract();

	static {
		instance.setDatapath("D:\\tess4j");
		instance.setLanguage("chi_sim+eng");
	}

	public String getApprovalNumber(String url) throws TesseractException, IOException {
		log.info("url: {}", url);
		if (StringUtils.isBlank(url)) {
			return null;
		}
		BufferedImage inputImage = ImageIO.read(new URL(url));
		return StrUtil.removeAll(instance.doOCR(resize(inputImage, 3)), " ");
	}

	public BufferedImage resize(BufferedImage img, int SCALE) {
		BufferedImage bi = new BufferedImage(SCALE * img.getWidth(null), SCALE
				* img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D grph = (Graphics2D) bi.getGraphics();
		grph.scale(SCALE, SCALE);
		grph.drawImage(img, 0, 0, null);
		grph.dispose();
		return bi;
	}
}
