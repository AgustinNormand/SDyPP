package com.trabajoPractico2.Ejercicio1.controllers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import com.google.gson.Gson;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MasterController {

    Gson gson = new Gson();

    ArrayList<String> endpoints = new ArrayList<>();

    @PostMapping("/endpoint/{ip}/{port}")
    public void addEndpoint(@PathVariable String ip, @PathVariable Integer port){
        endpoints.add(ip+":"+port);
    }

    @GetMapping("/endpoint/{fileName}")
	public String getEndpoint(@PathVariable String fileName) {
        for (String endpoint : endpoints) {
            ArrayList<String> sharedFiles = getSharedFiles(endpoint);
            if(sharedFiles.contains(fileName))
                return endpoint;
        }
		return null;
	}
    
    private ArrayList<String> getSharedFiles(String endpointSocket) {
        String jsonFiles = sendGetRequest(endpointSocket, "/files");
        return gson.fromJson(jsonFiles, ArrayList.class);
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
        return null;
    }

    @GetMapping("/endpoints")
	public String getEndpoints() {
		return endpoints.toString();
	}
}
