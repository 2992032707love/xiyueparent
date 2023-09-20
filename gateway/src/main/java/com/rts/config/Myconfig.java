package com.rts.config;

import com.netflix.loadbalancer.IRule;
import com.rts.core.NacosWeightLoadBalanceRule;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Myconfig {
    @Bean
    IRule getLoadBalancerRule(){
        return new NacosWeightLoadBalanceRule();
    }

}
