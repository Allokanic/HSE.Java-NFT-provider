package com.allokanic.nft.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Slf4j
@Configuration
public class Web3jConfig {
    @Bean
    public Web3j getWeb3j(@Value("${alchemy.api-key}") String key) {
        return Web3j.build(new HttpService("https://eth-sepolia.g.alchemy.com/v2/" + key));
    }
}
