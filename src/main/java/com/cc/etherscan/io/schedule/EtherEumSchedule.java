package com.cc.etherscan.io.schedule;

import com.cc.etherscan.io.pipeline.EthereumPipeline;
import com.cc.etherscan.io.processor.EthereumContractProcessor;
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

    int pageSize = 100;

    int totalPage = 390;

    @PostConstruct
    public void start() {
        for (int i = 1; i<= totalPage; i++) {
            Spider.create(new EthereumContractProcessor(redisTemplate))
                    .addUrl("https://etherscan.io/contractsVerified/" + i + "?ps=" + pageSize)
                    .addPipeline(ethereumPipeline)
                    .thread(5)
                    .run();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
