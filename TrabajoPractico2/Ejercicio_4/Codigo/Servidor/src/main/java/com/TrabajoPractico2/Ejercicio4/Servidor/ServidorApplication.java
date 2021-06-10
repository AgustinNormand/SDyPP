package com.TrabajoPractico2.Ejercicio4.Servidor;

import com.google.cloud.storage.*;
import org.json.JSONObject;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.sql.Timestamp;

@SpringBootApplication
@RestController
public class ServidorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServidorApplication.class, args);
	}

	private RabbitTemplate rabbitTemplate;
	private Storage storage;
	private AmqpAdmin amqpAdmin;

	@Autowired
	public ServidorApplication(RabbitTemplate rabbitTemplate, Storage storage, AmqpAdmin amqpAdmin) {
		this.rabbitTemplate = rabbitTemplate;
		this.storage = storage;
		this.amqpAdmin = amqpAdmin;
		createQueues();
	}

	public void createQueues() {
		amqpAdmin.declareQueue(new Queue("assemblyJobs", true));
		amqpAdmin.declareQueue(new Queue("sobelJobs", true));
		amqpAdmin.declareQueue(new Queue("sliceJobs", true));
	}

	@PostMapping("/uploadImage")
	public void uploadImage( @RequestParam("imageFile") MultipartFile imageFile){
		String timestamp = new Timestamp(System.currentTimeMillis()).toString().replaceAll(" ", "");
		String partName = timestamp+imageFile.getOriginalFilename();
		String blobName = null;
		try {
			blobName = storeInBucket(partName, imageFile.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject obj = new JSONObject();
		obj.put("fileName", imageFile.getOriginalFilename());
		obj.put("blobName", blobName);
		encolarTrabajo(obj.toString());
	}

	public void encolarTrabajo(String blobName){
		rabbitTemplate.convertAndSend("sliceJobs", blobName);
	}

	public String storeInBucket(String blobName, byte[] data){
		Bucket bucket = storage.get("sdypp-316414");
		Blob blob = bucket.create(blobName, data);
		return blob.getName().toString();
	}
}
