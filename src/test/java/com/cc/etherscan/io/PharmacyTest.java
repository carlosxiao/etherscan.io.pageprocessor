package com.cc.etherscan.io;

import com.cc.etherscan.io.pipeline.PharmacyPipeline;
import com.cc.etherscan.io.processor.PharmacyProcessor;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import us.codecraft.webmagic.Spider;

public class PharmacyTest {


    @Test
    public void should_1() {
        Spider.create(new PharmacyProcessor())
                .addUrl("https://www.yaofangwang.com/medicine-521443.html")
                .addPipeline(new PharmacyPipeline())
                .thread(1)
                .run();
    }

    @Test
    public void should_2 (){
        String s = "1 ";
        String[] split = StringUtils.split(s, "/");
        for (String s1 : split) {
            System.out.println(s1);
        }
    }

    @Test
    public void should_3 (){
        String s = "https://www.yaofangwang.com/catalog-7020.html";
        System.out.println(s.substring(s.indexOf("-") + 1, s.indexOf(".html")));
    }
}
