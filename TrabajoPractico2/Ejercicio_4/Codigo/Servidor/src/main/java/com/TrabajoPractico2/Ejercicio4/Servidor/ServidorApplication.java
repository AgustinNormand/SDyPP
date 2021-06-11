package com.TrabajoPractico2.Ejercicio4.Servidor;

import com.google.cloud.storage.*;
import org.json.JSONObject;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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

	@GetMapping("/getImage")
	public ResponseEntity<byte[]> getImage(@RequestParam("blobName") String blobName){
		try{
			byte[] data = getFromBucket(blobName);
			final HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_PNG);
			return new ResponseEntity<byte[]>(data, headers, HttpStatus.CREATED);
		} catch(Exception e){
		}
		return null;
	}

	@PostMapping("/uploadImage")
	public String uploadImage(@RequestParam("imageFile") MultipartFile imageFile, HttpServletRequest request){
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
		String imageResultUrl = request.getLocalAddr().toString() + ":8080/getImage?blobName=finished"+blobName;
		return "See result in: "+imageResultUrl;
	}

	private String replaceExtension(String imageName, String extension) {
		String fileExtension = "";
		int index = imageName.lastIndexOf('.');
		if (index > 0) {
			fileExtension = imageName.substring(index + 1);
		}
		imageName.replace(fileExtension, extension);
		return imageName.replace(fileExtension, extension);
	}

	public void encolarTrabajo(String blobName){
		rabbitTemplate.convertAndSend("sliceJobs", blobName);
	}

	public String storeInBucket(String blobName, byte[] data){
		Bucket bucket = storage.get("sdypp-316414");
		Blob blob = bucket.create(blobName, data);
		return blob.getName().toString();
	}

	public byte[] getFromBucket(String blobName){
		byte[] result = null;
		Bucket bucket = storage.get("sdypp-316414");
		Blob blob = bucket.get(blobName);
		result = blob.getContent();
		return result;
	}
}
