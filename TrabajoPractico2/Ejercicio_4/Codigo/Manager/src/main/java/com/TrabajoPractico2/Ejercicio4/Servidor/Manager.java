package com.TrabajoPractico2.Ejercicio4.Servidor;

import org.json.JSONObject;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import java.util.ArrayList;

@SpringBootApplication
public class Manager {

	public static void main(String[] args) {
		SpringApplication.run(Manager.class, args);
	}

	private RabbitTemplate rabbitTemplate;
	private RedisHandler rh;
	private AmqpAdmin amqpAdmin;

	@Autowired
	public Manager(RabbitTemplate rabbitTemplate, Environment env, AmqpAdmin amqpAdmin) {
		this.rabbitTemplate = rabbitTemplate;
		this.rh = new RedisHandler(env);
		this.amqpAdmin = amqpAdmin;
		loopProcessing();
	}

	public void createQueues() {
		amqpAdmin.declareQueue(new Queue("sliceJobs", true));
		amqpAdmin.declareQueue(new Queue("sobelJobs", true));
		amqpAdmin.declareQueue(new Queue("assemblyJobs", true));
	}

	private void loopProcessing(){
		while(true){
			ArrayList<Task> endedTasks = rh.getEndedTasks();
			encolarTrabajos(endedTasks);
		}
	}

	private void encolarTrabajos(ArrayList<Task> endedTasks) {
		for(Task endedTask : endedTasks){
			JSONObject obj = new JSONObject();
			obj.put("storageImageName", endedTask.getStorageImageName());
			obj.put("partsNames", endedTask.getPartsNamesJsonArray());
			rabbitTemplate.convertAndSend("assemblyJobs", obj.toString());
		}
	}
}
