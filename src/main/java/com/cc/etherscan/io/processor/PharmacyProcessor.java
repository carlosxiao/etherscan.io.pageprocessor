package com.cc.etherscan.io.processor;

import com.cc.etherscan.io.common.Constants;
import com.cc.etherscan.io.entity.YfwProductInfo;
import com.cc.etherscan.io.mapper.YfwProductInfoDao;
import com.cc.etherscan.io.util.Tess4jUtils;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.lang.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.IOException;
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

    public PharmacyProcessor(YfwProductInfoDao yfwProductInfoDao) {
        this.yfwProductInfoDao = yfwProductInfoDao;
    }

    private YfwProductInfoDao yfwProductInfoDao;

    @Override
    public void process(Page page) {
        // subcat
        String requestUrl = page.getUrl().get();
        List<String> catList = page.getHtml().xpath("//div[@class='subcat']").links().all();
        if (page.getUrl().regex(Constants.SUB_CAT).match()) {
            // 获取总页数,得到当前适应症下的所有二级页面
            //
            String catId = requestUrl.substring(requestUrl.indexOf("-") + 1, requestUrl.indexOf(".html"));
            String s = page.getHtml().xpath("////*[@id=\"wrap\"]/div[2]/div[3]/div[3]/span/text()").get();
            String[] split = StringUtils.split(s, "/");
            if (split.length > 1) {
                int totalPage = Integer.parseInt(StringUtils.trim(split[1]));
                log.info("【SUB_CAT】页面： {}, 子页面数：{}", page.getUrl().get(), totalPage);
                for (int i = 1; i <= totalPage; i++) {
                    String catItemUrl = String.format(Constants.SUB_CAT_FG, catId, i);
                    page.addTargetRequest(catItemUrl);
                }
            }
        } else if (page.getUrl().regex(SUB_CAT_SP).match()) {
            List<String> all = page.getHtml().xpath("//*[@id=\"wrap\"]/div[2]/ul/li/div/a[@class=\"photo\"]").links().all();
            log.info("【SUB_CAT_SP】页面： {}, 子页面数：{}", page.getUrl().get(), all.size());
            page.addTargetRequests(all);
        } else if (page.getUrl().regex(SUB_CAT_MED_DETAILS).match()) {
            log.info("【SUB_CAT_MED_DETAILS】页面： {}", page.getUrl().get());
            YfwProductInfo item = new YfwProductInfo();
            String productId = page.getHtml().xpath("//*[@id=\"wrap\"]/div[2]/div[1]/div[2]/div/text()").get();
            productId = productId.substring(productId.indexOf("：") + 1, productId.length() - 1);
            item.setProductId(productId);
            // 商品名
            String productName = page.getHtml().xpath("//*[@id=\"wrap\"]/div[2]/div[2]/h1/strong/span/text()").get();
            item.setProductName(productName);
            // 通用名
            String commodityName = page.getHtml().xpath("//*[@id=\"wrap\"]/div[2]/div[2]/div/dl[1]/dd[1]/strong/text()").get();
            item.setCommodityName(commodityName);
            // 商标
            String trademark = page.getHtml().xpath("//*[@id=\"wrap\"]/div[2]/div[2]/div/dl[1]/dd[2]/text()").get();
            item.setTradeMark(trademark);
            // 挤型
            String jixing = page.getHtml().xpath("//*[@id=\"wrap\"]/div[2]/div[2]/div/dl[1]/dd[4]/text()").get();
            item.setDosage(jixing);
            // 规格
            String spec = page.getHtml().xpath("//*[@id=\"standardOther\"]/div[1]/text()").get();
            item.setSpec(spec);
            // 生产厂家
            String cj = page.getHtml().xpath("//*[@id=\"wrap\"]/div[2]/div[2]/div/dl[1]/dd[5]/text()").get();
            item.setFactory(cj);
            // 有效期
            String ex = page.getHtml().xpath("//*[@id=\"wrap\"]/div[2]/div[2]/div/dl[1]/dd[6]/label/text()").get();
            item.setExpired(ex);

            String pzwh = page.getHtml().xpath("//*[@id=\"wrap\"]/div[2]/div[2]/div/dl[1]/dd[7]/div/img/@src").get();
            String approvalNumber = "";
            try {
                if (StringUtils.isNotEmpty(pzwh) && !StringUtils.equalsIgnoreCase(pzwh, "null")) {
                    approvalNumber = Tess4jUtils.getApprovalNumber("https:" + pzwh);
                    item.setApprovalNumber(approvalNumber);
                }
            } catch (TesseractException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 浏览次数
            String llcs = page.getHtml().xpath("//*[@id=\"wrap\"]/div[2]/div[2]/div/dl[1]/dd[9]/text()").get();
            item.setViewCount(llcs);
            // 适应症
            String syz = page.getHtml().xpath("//*[@id=\"wrap\"]/div[2]/div[2]/div/dl[1]/dd[8]/strong/text()").get();
            item.setIndication(syz);
            // 说明书_标题
            String sms_title = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/h2/text()").get();
            item.setSmsTitle(sms_title);
            String sms_tips = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/div[1]/text()").get();
            item.setSmsTips(sms_tips);
            // 药品名称
            String sms_name_cname = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/div[3]/dl/dd[1]/text()").get();
            item.setSmsCname(sms_name_cname);
            String sms_name_ename = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/div[3]/dl/dd[3]/text()").get();
            item.setSmsEname(sms_name_ename);
            String sms_name_py = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/div[3]/dl/dd[4]/text()").get();
            item.setSmsPy(sms_name_py);
            // 执行标准
            String sms_zxbz = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/dl/dd[1]/p/text()").get();
            item.setSmsZxbz(sms_zxbz);
            // 性状
            String sms_xz = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/dl/dd[2]/p/text()").get();
            item.setSmsXz(sms_xz);
            // 组方/成份
            String sms_cf = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/dl/dd[3]/p/text()").get();
            item.setSmsCf(sms_cf);
            // 功能与主治
            String sms_gnzz = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/dl/dd[4]/p/text()").get();
            item.setSmsGnzz(sms_gnzz);
            // 用法用量
            String sms_yfyl = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/dl/dd[5]/p/text()").get();
            item.setSmsYfyl(sms_yfyl);
            // 不良反应
            String sms_blfy = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/dl/dd[6]/p/text()").get();
            item.setSmsBlfy(sms_blfy);
            // 禁忌症
            String sms_jjz = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/dl/dd[7]/p/text()").get();
            item.setSmsJjz(sms_jjz);
            // 药物相互作用
            String sms_ywzy = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/dl/dd[8]/p/text()").get();
            item.setSmsYwzy(sms_ywzy);
            // 贮藏
            String sms_zc = page.getHtml().xpath("//*[@id=\"guide\"]/div[1]/dl/dd[9]/p/text()").get();
            item.setSmsZc(sms_zc);
            int insert = yfwProductInfoDao.insert(item);
            log.info("【SUB_CAT_MED_DETAILS】页面： {}， 保存结果：{}", page.getUrl().get(), insert);
        } else {
            log.info("【INDEX】页面： {}, 子页面数：{}", page.getUrl().get(), catList.size());
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
