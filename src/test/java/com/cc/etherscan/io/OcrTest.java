package com.cc.etherscan.io;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.junit.Test;

import java.io.File;

public class OcrTest {

    @Test
    public void should_1 () throws TesseractException {
        ITesseract instance = new Tesseract();
        instance.setDatapath("D:\\tess4j");
        instance.setLanguage("chi_sim+eng");
        String result = instance.doOCR(new File("C:\\Users\\CarlosXiao\\Desktop\\222.jpg"));
        System.out.println(result);
    }
}
