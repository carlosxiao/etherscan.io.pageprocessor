package com.cc.etherscan.io;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author carlosxiao
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.cc.etherscan.io.mapper")
@EnableScheduling
public class EtherContractProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(EtherContractProcessorApplication.class, args);
	}
}
