package com.trabajoPractico3.Receptionist;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@SpringBootApplication
@RestController
public class ReceptionistApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReceptionistApplication.class, args);
	}

	private RabbitTemplate rabbitTemplate;

	@Autowired
	public ReceptionistApplication(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	@GetMapping("/queueMessage")
	public void queueMessage(){
		rabbitTemplate.convertAndSend("myQueue", "Hello, world!");
	}

	@Bean
	public Queue myQueue() {
		return new Queue("myQueue", false);
	}
	
	@GetMapping("/health")
	public String getHealth(){
		return "I'm healty! "+ new Timestamp(System.currentTimeMillis());
	}

}
