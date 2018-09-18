package com.example.itemService;

import com.example.itemService.web.RestServer;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.itemService.web, com.example.itemService.models,com.example.itemService.dao"})
public class ItemServiceApplication {

	@Autowired
	private RestServer restServer;

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
		//starting non-blocking verticle rest server
		Vertx.vertx().deployVerticle(new RestServer(),new DeploymentOptions().setWorker(true));
	}

}
