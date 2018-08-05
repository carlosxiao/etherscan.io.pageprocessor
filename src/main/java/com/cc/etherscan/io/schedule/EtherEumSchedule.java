package com.cc.etherscan.io.schedule;

import com.cc.etherscan.io.pipeline.EthereumPipeline;
import com.cc.etherscan.io.processor.EthereumContractProcessor;
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

    @Value("${etherscan.pageSize}")
    private int pageSize = 0;

    @Value("${etherscan.totalPage}")
    private int totalPage = 0;

    @Value("${etherscan.startPage}")
    private int startPage;

    @PostConstruct
    public void start() {
        for (int i = startPage; i<= totalPage; i++) {
            Spider.create(new EthereumContractProcessor(redisTemplate))
                    .addUrl("https://etherscan.io/contractsVerified/" + i + "?ps=" + pageSize)
                    .addPipeline(ethereumPipeline)
                    .thread(8)
                    .run();
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
