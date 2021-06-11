package com.TrabajoPractico2.Ejercicio4.Servidor;

import com.google.cloud.storage.*;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.json.JSONObject;
import org.springframework.amqp.core.AmqpAdmin;
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
import java.sql.Timestamp;
import java.util.ArrayList;

@SpringBootApplication
public class Slicer {

	public static void main(String[] args) {
		SpringApplication.run(Slicer.class, args);
	}

	private RabbitTemplate rabbitTemplate;
	private Storage storage;
	private RedisHandler rh;
	private AmqpAdmin amqpAdmin;

	@Autowired
	public Slicer(RabbitTemplate rabbitTemplate, Storage storage, Environment env, AmqpAdmin amqpAdmin) {
		this.rabbitTemplate = rabbitTemplate;
		this.storage = storage;
		this.rh = new RedisHandler(env);
		this.amqpAdmin = amqpAdmin;
		createQueues();
		loopProcessing();
	}

	public void createQueues() {
		amqpAdmin.declareQueue(new Queue("sliceJobs", true));
		amqpAdmin.declareQueue(new Queue("sobelJobs", true));
		amqpAdmin.declareQueue(new Queue("assemblyJobs", true));
	}

	private void loopProcessing(){
		while(true){
			Message message = rabbitTemplate.receive("sliceJobs");
			if(message != null){
				processMessage(message);
			}
		}
	}

	private void processMessage(Message message){
		String jsonString = new String(message.getBody(), StandardCharsets.UTF_8);
		JSONObject obj = new JSONObject(jsonString);
		String blobName = obj.get("blobName").toString();
		String fileName = obj.get("fileName").toString();
		byte[] multipartBytes = getFromBucket(blobName);
		try {
			InputStream targetStream = new ByteArrayInputStream(multipartBytes);
			BufferedImage source = ImageIO.read(targetStream);
			sliceAndUploadImage(source, fileName, blobName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] getFromBucket(String blobName){
		Bucket bucket = storage.get("sdypp-316414");
		Blob blob = bucket.get(blobName);
		return blob.getContent();
	}

	public void sliceAndUploadImage( BufferedImage source, String originalFilename, String googleStorageName){
		ArrayList<BufferedImage> imageParts = splitImage(source, 2);
		rh.write(googleStorageName, imageParts.size());
		int partNumber = 0;
		for(BufferedImage part : imageParts){
			long datetime = System.currentTimeMillis();
			String timestamp = new Timestamp(datetime).toString().replaceAll(" ", "");
			String partName = timestamp+"Part_"+partNumber+++"_Of_"+originalFilename;
			rh.addPartName(googleStorageName, partName);
			String extension = getExtension(originalFilename);
			byte[] data = toByteArray(part, extension);
			String blobName = storeInBucket(partName, data);
			encolarTrabajo(blobName, googleStorageName);
		}

	}

	public void encolarTrabajo(String partBlobName, String originalBlobName){
		JSONObject obj = new JSONObject();
		obj.put("partBlobName", partBlobName);
		obj.put("originalBlobName", originalBlobName);
		rabbitTemplate.convertAndSend("sobelJobs", obj.toString());
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

	private String getExtension(String fileName){
		String extension = "";

		int index = fileName.lastIndexOf('.');
		if (index > 0) {
			extension = fileName.substring(index + 1);
		}
		return extension;
	}

	private ArrayList<BufferedImage> splitImage(BufferedImage source, int sliceCount) {
		ArrayList<BufferedImage> result = new ArrayList<>();
		ArrayList<BufferedImage> buffer = new ArrayList<>();
		ArrayList<BufferedImage> secondBuffer;
		buffer.add(source);
		for(int i = 0; i < sliceCount; i++) {
			secondBuffer = new ArrayList<>();
			for (BufferedImage part : buffer) {
				secondBuffer.addAll(splitInFour(part));
			}
			buffer = new ArrayList<>();
			buffer.addAll(secondBuffer);

		}
		result = acomodarPartes(buffer, sliceCount);
		return result;
	}

	private ArrayList<BufferedImage> acomodarPartes(ArrayList<BufferedImage> parts, int sliceCount) {
		ArrayList<BufferedImage> result = new ArrayList<>();
		ArrayList<BufferedImage> buffer = new ArrayList<>();
		int elementosFila = (int) Math.pow(2, sliceCount);
		int elementosFilaActuales = 0;
		while(!parts.isEmpty()){
			tomar2(result, parts);
			buferear2(buffer, parts);
			elementosFilaActuales = elementosFilaActuales + 2;
			if(elementosFilaActuales == elementosFila){
				result.addAll(buffer);
				buffer.clear();
			}
		}
		result.addAll(buffer);
		return result;
	}

	private void buferear2(ArrayList<BufferedImage> buffer, ArrayList<BufferedImage> parts) {
		buffer.add(parts.get(0));
		parts.remove(0);
		buffer.add(parts.get(0));
		parts.remove(0);
	}

	private void tomar2(ArrayList<BufferedImage> result, ArrayList<BufferedImage> parts) {
		result.add(parts.get(0));
		parts.remove(0);
		result.add(parts.get(0));
		parts.remove(0);
	}

	public ArrayList<BufferedImage> splitInFour(BufferedImage source){
		ArrayList<BufferedImage> result = new ArrayList<>();
		int height = source.getHeight() / 2;
		int width = source.getWidth() / 2;
		result.add(source.getSubimage(0, 0, width, height));
		result.add(source.getSubimage(width, 0, width, height));
		result.add(source.getSubimage(0, height, width, height));
		result.add(source.getSubimage(width, height, width, height));
		return result;
	}

	public String storeInBucket(String blobName, byte[] data){
		Bucket bucket = storage.get("sdypp-316414");
		Blob blob = bucket.create(blobName, data);
		return blob.getName().toString();
	}
}
