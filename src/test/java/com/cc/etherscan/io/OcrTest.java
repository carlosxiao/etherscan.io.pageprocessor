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
        instance.setDatapath("/Users/carlosxiao/Desktop/");
        instance.setLanguage("chi_sim");
        String result = instance.doOCR(new File("/Users/carlosxiao/Desktop/2222.jpg"));
        System.out.println(result);
    }
}
