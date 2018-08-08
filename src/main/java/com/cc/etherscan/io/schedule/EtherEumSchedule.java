package com.cc.etherscan.io.schedule;

import com.cc.etherscan.io.mapper.EtherContractMapper;
import com.cc.etherscan.io.pipeline.EthereumPipeline;
import com.cc.etherscan.io.processor.EthereumContractProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

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

    @Scheduled(cron="0 0/2 * * * ?")
    public void start() {
        for (int i = startPage; i<= totalPage; i++) {
            Spider.create(new EthereumContractProcessor(redisTemplate, etherContractMapper))
                    .addUrl("https://etherscan.io/contractsVerified/" + i + "?ps=" + pageSize)
                    .addPipeline(ethereumPipeline)
                    .thread(threadCount)
                    .run();
            try {
                Thread.sleep(intervalSeconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
