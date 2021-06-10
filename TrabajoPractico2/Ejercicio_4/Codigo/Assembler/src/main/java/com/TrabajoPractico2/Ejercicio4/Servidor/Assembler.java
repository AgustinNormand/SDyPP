package com.TrabajoPractico2.Ejercicio4.Servidor;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

@SpringBootApplication
public class Assembler {

	public static void main(String[] args) {
		SpringApplication.run(Assembler.class, args);
	}

	private RabbitTemplate rabbitTemplate;
	private Storage storage;

	@Autowired
	public Assembler(RabbitTemplate rabbitTemplate, Storage storage) {
		this.rabbitTemplate = rabbitTemplate;
		this.storage = storage;
		loopProcessing();
	}

	private void loopProcessing(){
		while(true){
			Message message = rabbitTemplate.receive("assemblyJobs");
			if(message != null){
				processMessage(message);
			}
		}
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
		System.out.println("Entered assembleParts");
		ArrayList<BufferedImage> images = getImages(partsNames);
		BufferedImage originalImage = getImage(storageImageName);
		System.out.println("All images pulled");
		String extension = getExtension(storageImageName);
		String filePath = "/home/agustin/Desktop/Distribuidos/Repo_Propio/TrabajoPractico2/Ejercicio_4/Codigo/Assembler/";
		int heightTotal = 0;
		for(BufferedImage bi : images){
			heightTotal += bi.getHeight();
		}

		BufferedImage result = new BufferedImage(images.get(5).getWidth(), heightTotal, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = result.createGraphics();
		int heightCurr = 0;
			try {
				for(BufferedImage bi : images){
					g2d.drawImage(bi, 0, heightCurr, null);
					heightCurr += bi.getHeight();
				}
				g2d.dispose();
				ImageIO.write(result, "jpeg", new File(filePath+"test"));
			} catch (Exception e) {
				e.printStackTrace();
			}
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

	public BufferedImage joinBufferedImage(BufferedImage img1,
										   BufferedImage img2) {
		int offset = 2;
		int width = img1.getWidth() + img2.getWidth() + offset;
		int height = Math.max(img1.getHeight(), img2.getHeight()) + offset;
		BufferedImage newImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = newImage.createGraphics();
		Color oldColor = g2.getColor();
		g2.setPaint(Color.BLACK);
		g2.fillRect(0, 0, width, height);
		g2.setColor(oldColor);
		g2.drawImage(img1, null, 0, 0);
		g2.drawImage(img2, null, img1.getWidth() + offset, 0);
		g2.dispose();
		return newImage;
	}


}
