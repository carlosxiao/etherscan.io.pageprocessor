package com.cc.etherscan.io;

import com.cc.etherscan.io.mapper.EtherContractMapper;
import com.cc.etherscan.io.pipeline.EthereumPipeline;
import com.cc.etherscan.io.processor.EthereumContractProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import us.codecraft.webmagic.Spider;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PageProcessorTest {

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

    @Test
    public void test() {
        Spider.create(new EthereumContractProcessor(redisTemplate, etherContractMapper))
                .addUrl("https://etherscan.io/contractsVerified/1?ps=" + pageSize)
                .addPipeline(ethereumPipeline)
                .thread(threadCount)
                .run();
    }
}
