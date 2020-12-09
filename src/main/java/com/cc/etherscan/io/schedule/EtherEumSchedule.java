package com.cc.etherscan.io.schedule;

import com.cc.etherscan.io.mapper.EtherContractMapper;
import com.cc.etherscan.io.mapper.YfwProductInfoDao;
import com.cc.etherscan.io.pipeline.EthereumPipeline;
import com.cc.etherscan.io.processor.PharmacyProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author carlosxiao
 */
@Component
public class EtherEumSchedule {


    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private EthereumPipeline ethereumPipeline;

    @Resource
    private EtherContractMapper etherContractMapper;

    @Resource
    private YfwProductInfoDao yfwProductInfoDao;

    @Value("${etherscan.pageSize}")
    private int pageSize = 0;

    @Value("${etherscan.totalPage}")
    private int totalPage = 0;

    @Value("${etherscan.startPage}")
    private int startPage;

    @Value("${etherscan.threadCount}")
    private int threadCount = 2;

    @Value("${etherscan.intervalSeconds}")
    private int intervalSeconds = 4;

    //@Scheduled(cron="${etherscan.scanIntervalCron}")
    @PostConstruct
    public void start() {
         Spider.create(new PharmacyProcessor(yfwProductInfoDao))
//                .addUrl("https://www.yaofangwang.com/medicine-261518.html")
//                .addUrl("https://www.yaofangwang.com/catalog-10-p8.html")
                .addUrl("https://www.yaofangwang.com")
                .thread(1)
                .run();
    }
}
