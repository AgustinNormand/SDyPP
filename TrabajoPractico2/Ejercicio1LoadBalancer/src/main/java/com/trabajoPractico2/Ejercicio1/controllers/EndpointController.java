package com.trabajoPractico2.Ejercicio1.controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.apache.catalina.startup.AddPortOffsetRule;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.parser.MediaType;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.cdimascio.dotenv.Dotenv;

@RestController
public class EndpointController {
    private String ip;
    private Integer port;
	private String mastersIpsFilePath;
	private String directoryWithFilesToShare;

    ArrayList<String> mastersSockets = new ArrayList<>();

    Gson gson = new Gson();

    public EndpointController(){
        readEnvFile();
        readMasterSockets();
        connectToMasters();
    }

    private void readEnvFile() {
        Dotenv dotenv = Dotenv.load();
        this.ip = dotenv.get("ip");
        this.port = Integer.valueOf(dotenv.get("port"));
        this.mastersIpsFilePath = System.getProperty("user.dir") + "/" +dotenv.get("mastersIpsFileName");
        this.directoryWithFilesToShare = System.getProperty("user.dir") + "/shared/";
    }

    @GetMapping("/masters")
	public String getMasters() {
		return mastersSockets.toString();
	}

    @GetMapping("/files")
	public String getFiles() {
        ArrayList<String> filesToShare = new ArrayList<>();
        File f = new File(directoryWithFilesToShare);	
		File[] files = f.listFiles();
		for (File file : files) {
			if (!file.isDirectory()) 
                filesToShare.add(file.getName());
		}
		return gson.toJson(filesToShare);
	}

    @GetMapping("/file/{fileName}")
    public void getFile(
        @PathVariable("fileName") String fileName, 
        HttpServletResponse response) {
    try {
      InputStream is = new FileInputStream(new File(directoryWithFilesToShare+fileName));
      IOUtils.copy(is, response.getOutputStream());
      response.flushBuffer();
    } catch (Exception e) {
        e.printStackTrace();}
    }

    @GetMapping("/fileRequest/{fileName}")
    public void requestFile(
        @PathVariable("fileName") String fileName) {
    try {
        for (String masterSocket : mastersSockets) {
            String resource = "/endpoint/"+fileName;
            String peerSocket = sendGetRequest(masterSocket, resource);
            File f = new File(directoryWithFilesToShare+"/"+fileName);
            BufferedWriter out = new BufferedWriter(new FileWriter(f));
            resource = "/file/"+fileName;
            out.write(sendGetRequest(peerSocket, resource));
            out.close();
        }
        
        
    } catch (Exception e) {
        e.printStackTrace();}
    }

    private void readMasterSockets() {
        readFileAndAppendTo(mastersIpsFilePath, mastersSockets);
    }

    private void readFileAndAppendTo(String fileName, ArrayList<String> list) {
		try {
			File file = new File(fileName);
			Scanner myReader = new Scanner(file);
			while (myReader.hasNextLine()) {
				String item = myReader.nextLine();
				list.add(item);
			}
			myReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    private void connectToMasters() {
        for (String masterSocket : mastersSockets) {
            String header = "/endpoint/"+this.ip+"/"+this.port;
            sendPostRequest(masterSocket, header);
        }
    }

    private void sendPostRequest(String masterSocket, String headers) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://"+
                                masterSocket+
                                headers))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        try {
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();}
    }

    private String sendGetRequest(String endpointSocket, String resource) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://"+
                                endpointSocket+
                                resource))
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (Exception e) {
            e.printStackTrace();}
        return "";
    }
}
