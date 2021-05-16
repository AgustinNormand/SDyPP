package com.trabajoPractico2.Ejercicio_2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Ejercicio2Application {

	public static void main(String[] args) {
		SpringApplication.run(Ejercicio2Application.class, args);
	}

}
