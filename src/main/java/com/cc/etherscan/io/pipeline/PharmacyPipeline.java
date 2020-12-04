package com.cc.etherscan.io.pipeline;

import com.cc.etherscan.io.common.Constants;
import com.cc.etherscan.io.entity.EtherContract;
import com.cc.etherscan.io.mapper.EtherContractMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.helper.StringUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.Resource;

/**
 * @author carlosxiao
 */
@Component
@Slf4j
public class PharmacyPipeline implements Pipeline {

    @Resource
    private EtherContractMapper etherContractMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void process(ResultItems resultItems, Task task) {
        if (resultItems.getAll().size() == 0) {
            return;
        }
        String address = resultItems.get("address");
        String createAddress = resultItems.get("createAddress");
        String txn = resultItems.get("txn");
        String sourceCode = resultItems.get("sourceCode");

        String rAddress = (String) redisTemplate.opsForHash().get(String.format(Constants.REDIS_ETHER_EUM_KEY, address), "address");
        if (StringUtils.isEmpty(rAddress) || StringUtil.isBlank(address)) {
            log.error("address or rAddress is null: address: {}, aAddress: {}, RequestUrl: {}", address, rAddress, resultItems.getRequest().getUrl());
            return;
        }
        if (!address.equals(rAddress)) {
            return;
        }

        String contractName = (String) redisTemplate.opsForHash().get(String.format(Constants.REDIS_ETHER_EUM_KEY, address), "contractName");
        String dateVerified = (String) redisTemplate.opsForHash().get(String.format(Constants.REDIS_ETHER_EUM_KEY, address), "dateVerified");
        String accessUrl = (String) redisTemplate.opsForHash().get(String.format(Constants.REDIS_ETHER_EUM_KEY, address), "accessUrl");
        String transactions = (String) redisTemplate.opsForHash().get(String.format(Constants.REDIS_ETHER_EUM_KEY, address), "transactions");

        EtherContract contract = new EtherContract();
        contract.setAddress(address);
        Integer tx = null;
        try {
            tx = Integer.parseInt(transactions);
        } catch (NumberFormatException e) {
            log.error("transactions format error : {}", transactions);
        }
        contract.setTransactions(tx);
        contract.setCreateAddress(createAddress);
        contract.setTxn(txn);
        contract.setSourceCode(sourceCode);
        contract.setContractName(contractName);
        contract.setDateVerified(dateVerified);
        contract.setAccessUrl(accessUrl);
        try {
            etherContractMapper.insert(contract);
        } catch (Exception e) {
            log.error("insert failure: {}", e);
        }
    }
}
