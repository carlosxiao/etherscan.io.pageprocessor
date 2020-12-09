package com.cc.etherscan.io;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Tess4jTest类
 * <p>
 *
 * @author wxd(ttzommed @ foxmail.com)
 */
public class Tess4jTest {
	public static void main(String[] args) throws TesseractException, IOException {

		//OCR识别
		ITesseract instance = new Tesseract();
		instance.setDatapath("D:\\tess4j");
		instance.setLanguage("chi_sim+eng");
		for (int i = 1; i <= 7; i++) {
			BufferedImage image = ImageIO.read(new File("D:\\tess4j\\" + i + ".jpg"));
			String result = instance.doOCR(resize(image, 3)).replaceAll(" ", "");
			System.out.println(result);
		}

	}

	public static BufferedImage resize(BufferedImage img, int SCALE) {
		BufferedImage bi = new BufferedImage(SCALE * img.getWidth(null), SCALE
				* img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D grph = (Graphics2D) bi.getGraphics();
		grph.scale(SCALE, SCALE);
		grph.drawImage(img, 0, 0, null);
		grph.dispose();
		return bi;
	}
}
