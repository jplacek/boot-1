package com.jared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
public class SimpleClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleClientApplication.class, args);
	}
}

@RestController
class SimpleController {
	@Autowired
	LoadBalancerClient loadBalancer;
	
	private static final Logger logger = LoggerFactory.getLogger(SimpleController.class);

	
	@RequestMapping("/")
	public @ResponseBody String index() {
		logger.debug("this is a test2");
		
		return "hello from simple-client";
	}
	
	@RequestMapping("get-hello/{appName}")
	public @ResponseBody String getHello(@PathVariable String appName) {
		ServiceInstance instance = loadBalancer.choose(appName);
		
		RestTemplate restTemplate = new RestTemplate();
		
		String response = restTemplate.getForEntity(instance.getUri(), String.class).getBody();
		
		logger.debug("this is a test");
		
		return "response from: "+appName+", on: "+instance.getUri()+", response: "+response;
	}
}