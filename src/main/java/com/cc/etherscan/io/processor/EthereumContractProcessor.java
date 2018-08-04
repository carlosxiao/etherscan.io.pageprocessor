package com.cc.etherscan.io.processor;

import com.cc.etherscan.io.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cc.etherscan.io.common.Constants.REDIS_ETHER_EUM_KEY;

/**
 * @author CarlosXiao
 * @date 2018/8/4
 */

@Slf4j
public class EthereumContractProcessor implements PageProcessor {

    private Site site;

    private RedisTemplate redisTemplate;

    public EthereumContractProcessor(RedisTemplate template) {
        this.redisTemplate = template;
    }


    @Override
    public void process(Page page) {

        if (page.getUrl().regex(Constants.URL_LIST).match()) {
            //TR
            List<String> trs = page.getHtml().xpath("/html/body/div[1]/div[5]/div[3]/div/div/div/table/tbody/tr").all();
            for (int i = 1; i <= trs.size(); i++) {
                String address = page.getHtml().xpath("/html/body/div[1]/div[5]/div[3]/div/div/div/table/tbody/tr[" + i + "]/td[1]/a/text()").get();
                String contractName = page.getHtml().xpath("/html/body/div[1]/div[5]/div[3]/div/div/div/table/tbody/tr[" + i + "]/td[2]/text()").get();
                String dateVerified = page.getHtml().xpath("/html/body/div[1]/div[5]/div[3]/div/div/div/table/tbody/tr[" + i + "]/td[7]/text()").get();
                String url = "https://etherscan.io/address/" + address + "#code";
                if (redisTemplate.hasKey(String.format(REDIS_ETHER_EUM_KEY, address))) {
                    return;
                }
                page.addTargetRequest(url);
                Map<String, String> map = new HashMap<>();
                map.put("address", address);
                map.put("contractName", contractName);
                map.put("dateVerified", dateVerified);
                map.put("accessUrl", url);
                redisTemplate.boundHashOps(String.format(REDIS_ETHER_EUM_KEY, address)).putAll(map);
            }
        } else {
            //
            String address = page.getHtml().xpath("//*[@id=\"mainaddress\"]/text()").get();
            String transactions = page.getHtml().xpath("//*[@id=\"ContentPlaceHolder1_divSummary\"]/div[1]/table/tbody/tr[3]/td[2]/span/text()").get();
            String createAddress = page.getHtml().xpath("//*[@id=\"ContentPlaceHolder1_trContract\"]/td[2]/a/text()").get();
            String txn = page.getHtml().xpath("//*[@id=\"ContentPlaceHolder1_trContract\"]/td[2]/span/a/text()").get();
            String sourceCode = page.getHtml().xpath("//*[@id=\"editor\"]/text()").get();
            page.putField("address", address);
            page.putField("transactions", transactions);
            page.putField("createAddress", createAddress);
            page.putField("txn", txn);
            page.putField("sourceCode", sourceCode);
        }
    }

    @Override
    public Site getSite() {
        if (null == site) {
            site = Site
                    .me()
                    .setCharset("utf-8")
                    .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31")
                    .setDomain("etherscan.io");
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
