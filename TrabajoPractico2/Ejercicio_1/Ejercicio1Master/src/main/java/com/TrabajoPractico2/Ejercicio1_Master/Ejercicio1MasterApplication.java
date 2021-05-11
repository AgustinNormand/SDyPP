package com.TrabajoPractico2.Ejercicio1_Master;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableDiscoveryClient
public class Ejercicio1MasterApplication {
	public static void main(String[] args) {
		SpringApplication.run(Ejercicio1MasterApplication.class, args);
	}

}
