package com.cc.etherscan.io;

import com.cc.etherscan.io.mapper.YfwProductInfoDao;
import com.cc.etherscan.io.pipeline.PharmacyPipeline;
import com.cc.etherscan.io.processor.PharmacyProcessor;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.selector.Selectable;

import javax.annotation.Resource;

import static com.cc.etherscan.io.common.Constants.SUB_CAT_SP;

public class PharmacyTest {

    @Resource
    private YfwProductInfoDao yfwProductInfoDao;

    @Test
    public void should_1() {
        Spider.create(new PharmacyProcessor(yfwProductInfoDao))
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
        String b = "商品编号：521443";
        String substring = b.substring(b.indexOf("：") + 1, b.length() -1);
        System.out.println(substring);
    }

    @Test
    public void should_4() {
        String r = "https://www.yaofangwang.com/catalog-\\w+\\-p\\w+\\.html";
        boolean matches = "https://www.yaofangwang.com/catalog-10-p77.html".matches(r);
        System.out.println(matches);
    }
}
