package com.cc.etherscan.io.pipeline;

import com.cc.etherscan.io.common.Constants;
import com.cc.etherscan.io.entity.EtherContract;
import com.cc.etherscan.io.mapper.EtherContractMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.Resource;

/**
 * @author carlosxiao
 */
@Component
public class EthereumPipeline implements Pipeline {

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
        String transactions = resultItems.get("transactions");
        String createAddress = resultItems.get("createAddress");
        String txn = resultItems.get("txn");
        String sourceCode = resultItems.get("sourceCode");

        String rAddress = (String) redisTemplate.opsForHash().get(String.format(Constants.REDIS_ETHER_EUM_KEY, address), "address");
        if (!address.equals(rAddress)) {
            return;
        }

        String contractName = (String) redisTemplate.opsForHash().get(String.format(Constants.REDIS_ETHER_EUM_KEY, address), "contractName");
        String dateVerified = (String) redisTemplate.opsForHash().get(String.format(Constants.REDIS_ETHER_EUM_KEY, address), "dateVerified");
        String accessUrl = (String) redisTemplate.opsForHash().get(String.format(Constants.REDIS_ETHER_EUM_KEY, address), "accessUrl");

        EtherContract contract = new EtherContract();
        contract.setAddress(address);
        contract.setTransactions(transactions);
        contract.setCreateAddress(createAddress);
        contract.setTxn(txn);
        contract.setSourceCode(sourceCode);
        contract.setContractName(contractName);
        contract.setDateVerified(dateVerified);
        contract.setAccessUrl(accessUrl);
        etherContractMapper.insert(contract);
    }
}
