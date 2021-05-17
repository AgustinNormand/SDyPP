package com.trabajoPractico2.Ejercicio_3;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Ejercicio3Application {

	private RabbitTemplate rabbitTemplate;

	public static void main(String[] args) {
		SpringApplication.run(Ejercicio3Application.class, args);
	}

	private void processMessage(){
		try {
			int processingTime = 1000*5;
			System.out.println("Processing message (Simulated Time: "+String.valueOf(processingTime)+")");
			Thread.sleep(processingTime);
			System.out.println("Finish processing message");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void loopProcessing(){
		while(true){
			Message message = rabbitTemplate.receive("myQueue");
			if(message != null)
				processMessage();
		}
	}

	@Autowired
	public Ejercicio3Application(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
		loopProcessing();
	}
}
