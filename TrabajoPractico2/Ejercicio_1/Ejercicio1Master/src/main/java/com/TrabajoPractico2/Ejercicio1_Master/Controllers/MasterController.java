package com.TrabajoPractico2.Ejercicio1_Master.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class MasterController {
    Gson gson = new Gson();

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/endpoint/{fileName}")
    public String getEndpoint(@PathVariable String fileName) {
        for (String endpointUri : getEndpointsUris()) {
            ArrayList<String> sharedFiles = getSharedFiles(endpointUri);
            if(sharedFiles.contains(fileName))
                return endpointUri;
        }
        return null;
    }

    private ArrayList<String> getSharedFiles(String endpointUri) {
        String jsonFiles = sendGetRequest(endpointUri, "/files");
        return gson.fromJson(jsonFiles, ArrayList.class);
    }

    private String sendGetRequest(String endpointUri, String resource) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpointUri+
                        resource))
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (Exception e) {
            e.printStackTrace();}
        return null;
    }

    public ArrayList<String> getEndpointsUris(){
        List<ServiceInstance> endpoints = discoveryClient.getInstances("endpoint");
        ArrayList<String> endpointsUris = new ArrayList<>();
        for (ServiceInstance endpoint : endpoints)
            endpointsUris.add(endpoint.getUri().toString());
        return endpointsUris;
    }

    @GetMapping("/endpoints")
    public String getEndpoints() {
        return getEndpointsUris().toString();
    }

    @GetMapping("/actuator/health")
    public String getHealth(){
        return "Ok";
    }
}

