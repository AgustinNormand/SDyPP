package com.trabajoPractico2.Ejercicio1.controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import io.github.cdimascio.dotenv.Dotenv;

@RestController

public class EndpointController {
    private String balancerUri;
    private String directoryWithFilesToShare;

    Gson gson = new Gson();

    public EndpointController(){
        Dotenv dotenv = Dotenv.load();
        this.balancerUri = dotenv.get("balancer-uri");
        this.directoryWithFilesToShare = System.getProperty("user.dir") + "/shared/";
    }

    @GetMapping("/actuator/health")
    public String getHealth(){
        return "Ok";
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
            String resource = "/endpoint/"+fileName;
            String endpointUri = sendGetRequest(balancerUri, resource);
            File f = new File(directoryWithFilesToShare+"/"+fileName);
            BufferedWriter out = new BufferedWriter(new FileWriter(f));
            resource = "/file/"+fileName;
            out.write(sendGetRequest(endpointUri, resource));
            out.close();
        } catch (Exception e) {
            e.printStackTrace();}
    }

    private String sendGetRequest(String endpointUri, String resource) {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpointUri+resource))
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (Exception e) {
            e.printStackTrace();}
        return "";
    }
}
