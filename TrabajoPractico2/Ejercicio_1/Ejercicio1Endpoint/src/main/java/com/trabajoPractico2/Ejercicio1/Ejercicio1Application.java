package com.trabajoPractico2.Ejercicio1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class Ejercicio1Application {
	public static void main(String[] args) {
		SpringApplication.run(Ejercicio1Application.class, args);
	}

}
