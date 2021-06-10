package com.TrabajoPractico2.Ejercicio4.WorkerSobel;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import org.json.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class WorkerSobelApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkerSobelApplication.class, args);
	}

	private RabbitTemplate rabbitTemplate;
 	private Storage storage;
 	private RedisHandler rh;

	@Bean
	public Queue myQueue() {
		return new Queue("sobelJobs", false);
	}

	@Autowired
	public WorkerSobelApplication(RabbitTemplate rabbitTemplate, Storage storage, Environment env) {
		this.rabbitTemplate = rabbitTemplate;
		this.storage = storage;
		this.rh = new RedisHandler(env);
		loopProcessing();
	}

	private void loopProcessing(){
		while(true){
				Message message = rabbitTemplate.receive("sobelJobs");
				if(message != null){
					processMessage(message);
				}
		}
	}

	public static BufferedImage toBufferedImage(byte[] bytes){
		InputStream is = new ByteArrayInputStream(bytes);
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(is);
		} catch (IOException e) {e.printStackTrace();}
		return bi;
	}

	private void processMessage(Message message){
		String jsonString = new String(message.getBody(), StandardCharsets.UTF_8);
		JSONObject obj = new JSONObject(jsonString);
		String partBlobName = obj.get("partBlobName").toString();
		String originalBlobName = obj.get("originalBlobName").toString();
		BufferedImage image = toBufferedImage(getFromBucket(partBlobName));
		String extension = getExtension(partBlobName);
		storeInBucket("finished"+partBlobName, toByteArray(image, extension));
		rh.increment(originalBlobName);
	}

	public byte[] getFromBucket(String blobName){
		Bucket bucket = storage.get("sdypp-316414");
		Blob blob = bucket.get(blobName);
		byte[] result = blob.getContent();
		blob.delete();
		return result;
	}

	private String getExtension(String fileName){
		String extension = "";

		int index = fileName.lastIndexOf('.');
		if (index > 0) {
			extension = fileName.substring(index + 1);
		}
		return extension;
	}

	public String storeInBucket(String blobName, byte[] data){
		Bucket bucket = storage.get("sdypp-316414");
		Blob blob = bucket.create(blobName, data);
		return blob.getName().toString();
	}

	public static byte[] toByteArray(BufferedImage bi, String format){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bi, format, baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] bytes = baos.toByteArray();
		return bytes;
	}

}
