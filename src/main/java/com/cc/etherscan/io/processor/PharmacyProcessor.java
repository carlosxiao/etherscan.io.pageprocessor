package com.cc.etherscan.io.processor;

import com.cc.etherscan.io.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

import static com.cc.etherscan.io.common.Constants.SUB_CAT_MED_DETAILS;
import static com.cc.etherscan.io.common.Constants.SUB_CAT_SP;

/**
 * @author CarlosXiao
 * @date 2018/8/4
 */

@Slf4j
public class PharmacyProcessor implements PageProcessor {

    private Site site;

    @Override
    public void process(Page page) {
        // subcat
        String requestUrl = page.getUrl().get();
        List<String> catList = page.getHtml().xpath("//div[@class='subcat']").links().all();
        if (catList.size() > 0) {
            log.info("分类地址数量： {}", catList.size());
        }
        if (page.getUrl().regex(Constants.SUB_CAT).match()) {
            // 获取总页数,得到当前适应症下的所有二级页面
            //
            String catId = requestUrl.substring(requestUrl.indexOf("-") + 1, requestUrl.indexOf(".html"));
            String s = page.getHtml().xpath("//*[@id=\"wrap\"]/div[2]/div[3]/div[3]/span").get();
            String[] split = StringUtils.split(s, "/");
            if (split.length > 1) {
                int totalPage = Integer.parseInt(StringUtils.trim(split[1]));
                for (int i = 1; i <= totalPage; i++) {
                    String catItemUrl = String.format(Constants.SUB_CAT_FG, catId, i);
                    page.addTargetRequest(catItemUrl);
                }
            }
        } else if (page.getUrl().regex(SUB_CAT_SP).match()) {
            List<String> all = page.getHtml().xpath("//div[@class=\"goodlist\"]/a[@class=\"photo\"]").links().all();
            page.addTargetRequests(all);
        } else if (page.getUrl().regex(SUB_CAT_MED_DETAILS).match()) {
            // 商品名
            String productName = page.getHtml().xpath("//*[@id=\"wrap\"]/div[2]/div[2]/h1/strong/span/text()").get();
            // 通用名
            String commodityName = page.getHtml().xpath("//*[@id=\"wrap\"]/div[2]/div[2]/div/dl[1]/dd[1]/strong/text()").get();
            // 商标
            String trademark = page.getHtml().xpath("//*[@id=\"wrap\"]/div[2]/div[2]/div/dl[1]/dd[2]/text()").get();
            // 挤型
            String jixing = page.getHtml().xpath("//*[@id=\"wrap\"]/div[2]/div[2]/div/dl[1]/dd[4]/text()").get();
            // 规格
            String spec = page.getHtml().xpath("//*[@id=\"standardOther\"]/div[1]/text()").get();
            // 生产厂家
            String cj = page.getHtml().xpath("//*[@id=\"wrap\"]/div[2]/div[2]/div/dl[1]/dd[5]/text()").get();
            // 有效期
            String ex = page.getHtml().xpath("//*[@id=\"wrap\"]/div[2]/div[2]/div/dl[1]/dd[6]/label/text()").get();
            // 适应症
            String syz = page.getHtml().xpath("//*[@id=\"wrap\"]/div[2]/div[2]/div/dl[1]/dd[8]/strong/text()").get();
            // 说明书_标题
            String sms_title = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/h2/text()").get();
            String sms_tips = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/div[1]/text()").get();

            // 药品名称
            String sms_name_cname = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/div[3]/dl/dd[1]/text()").get();
            String sms_name_ename = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/div[3]/dl/dd[3]/text()").get();
            String sms_name_py = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/div[3]/dl/dd[4]/text()").get();
            // 执行标准
            String sms_zxbz = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/dl/dd[1]/p/text()").get();
            // 性状
            String sms_xz = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/dl/dd[2]/p/text()").get();
            // 组方/成份
            String sms_cf = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/dl/dd[3]/p/text()").get();
            // 功能与主治
            String sms_gnzz = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/dl/dd[4]/p/text()").get();
            // 用法用量
            String sms_yfyl = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/dl/dd[5]/p/text()").get();
            // 不良反应
            String sms_blfy = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/dl/dd[6]/p/text()").get();
            // 禁忌症
            String sms_jjz = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/dl/dd[7]/p/text()").get();
            // 药物相互作用
            String sms_ywzy = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/dl/dd[8]/p/text()").get();
            // 贮藏
            String sms_zc = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/dl/dd[9]/p/text()").get();
            String productImgText = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/dl/dd[10]/textarea/text()").get();
            Html html = new Html(productImgText);
            Page page1 = new Page();
            page1.setHtml(html);
            List<String> all = page1.getHtml().xpath("//*a/@href").all();
            String sms_img = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/dl/dd[11]/textarea/text()").get();
            Html sms_img_html = new Html(sms_img);
            Elements a = sms_img_html.getDocument().getElementsByTag("a");
            String href = a.get(0).attr("href");
            System.out.println(sms_blfy);
        } else {
            page.addTargetRequests(catList);
        }
    }

    @Override
    public Site getSite() {
        if (null == site) {
            site = Site
                    .me()
                    .setSleepTime(2000)
                    .setTimeOut(20000)
                    .setRetrySleepTime(20000)
                    .setRetryTimes(3)
                    .setCharset("utf-8")
                    .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31")
                    .setDomain("www.yaofangwang.com");
        }
        return site;
    }

    /*public static void main(String [] args) {
        Spider.create(new EthereumContractProcessor())
                .addUrl("https://etherscan.io/contractsVerified/1")
                .addPipeline(new JsonFilePipeline("D://EtherScan"))
                .thread(5)
                .run();
    }*/
}
