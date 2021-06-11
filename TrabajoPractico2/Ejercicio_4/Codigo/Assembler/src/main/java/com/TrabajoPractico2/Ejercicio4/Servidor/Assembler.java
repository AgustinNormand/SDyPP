package com.TrabajoPractico2.Ejercicio4.Servidor;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

@SpringBootApplication
public class Assembler {

	public static void main(String[] args) {
		SpringApplication.run(Assembler.class, args);
	}

	private RabbitTemplate rabbitTemplate;
	private Storage storage;
	private AmqpAdmin amqpAdmin;

	@Autowired
	public Assembler(RabbitTemplate rabbitTemplate, Storage storage, AmqpAdmin amqpAdmin) {
		this.rabbitTemplate = rabbitTemplate;
		this.storage = storage;
		this.amqpAdmin = amqpAdmin;
	}

	@PostConstruct
	private void loopProcessing(){
		while(true){
			Message message = rabbitTemplate.receive("assemblyJobs");
			if(message != null){
				processMessage(message);
			}
		}
	}

	public void createQueues() {
		amqpAdmin.declareQueue(new Queue("sliceJobs", true));
		amqpAdmin.declareQueue(new Queue("sobelJobs", true));
		amqpAdmin.declareQueue(new Queue("assemblyJobs", true));
	}

	private void processMessage(Message message) {
		String jsonString = new String(message.getBody(), StandardCharsets.UTF_8);
		JSONObject obj = new JSONObject(jsonString);
		String storageImageName = obj.get("storageImageName").toString();
		JSONArray partsNamesJson = new JSONArray(obj.get("partsNames").toString());
		List partsNames = partsNamesJson.toList();
		assembleParts(partsNames, storageImageName);
	}

	public ArrayList<BufferedImage> getImages(List partsNames){
		ArrayList<BufferedImage> result = new ArrayList<BufferedImage>();
		for(Object partName : partsNames)
			result.add(getImage("finished"+partName.toString()));
		return result;
	}

	private void assembleParts(List partsNames, String storageImageName) {
		ArrayList<BufferedImage> images = getImages(partsNames);
		BufferedImage originalImage = getImage(storageImageName);
		//String extension = getExtension(storageImageName);
		String filePath = "/home/agustin/Desktop/Distribuidos/Repo_Propio/TrabajoPractico2/Ejercicio_4/Codigo/Assembler/";
		BufferedImage result = joinParts(images, originalImage.getWidth(), originalImage.getHeight());
		try {
			byte[] data = toByteArray(result, "png");
			storeInBucket("finished"+storageImageName, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private BufferedImage joinParts(ArrayList<BufferedImage> images, int width, int height) {
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = result.createGraphics();
		int x = 0;
		int cantidadFotosFilaTotales = width / images.get(0).getWidth();
		int cantidadFotosFilaActuales = 0;
		int currentHeight = 0;
		int currentWidth = 0;
		for(BufferedImage part : images){
			if(cantidadFotosFilaActuales == cantidadFotosFilaTotales){
				currentHeight = currentHeight+part.getHeight();
				currentWidth = 0;
				cantidadFotosFilaActuales = 0;
			}
			g2d.drawImage(part, currentWidth, currentHeight, null);
			cantidadFotosFilaActuales++;
			currentWidth += part.getWidth();
		}
		g2d.dispose();
		return result;
	}

	public String storeInBucket(String blobName, byte[] data){
		Bucket bucket = storage.get("sdypp-316414");
		Blob blob = bucket.create(blobName, data);
		return blob.getName().toString();
	}

	private BufferedImage getImage(String storageImageName) {
		BufferedImage result = null;
		byte[] partBytes = getFromBucket(storageImageName);
		InputStream targetStream = new ByteArrayInputStream(partBytes);
		try {
			result = ImageIO.read(targetStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public byte[] getFromBucket(String blobName){
		byte[] result = null;
		Bucket bucket = storage.get("sdypp-316414");
		Blob blob = bucket.get(blobName);
		result = blob.getContent();
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

	public static byte[] toByteArray(BufferedImage bi, String format)
			throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bi, format, baos);
		byte[] bytes = baos.toByteArray();
		return bytes;

	}
}
