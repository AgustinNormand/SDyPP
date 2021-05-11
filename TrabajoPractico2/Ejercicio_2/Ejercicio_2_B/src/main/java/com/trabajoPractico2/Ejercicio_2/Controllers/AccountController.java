package com.trabajoPractico2.Ejercicio_2.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@RestController
public class AccountController {

    String filesDirectory;

    public AccountController(){
        filesDirectory =  System.getProperty("user.dir") + "/accountFiles/";
    }

    @GetMapping("/balance/{clientAccountId}")
    public String receiveDeposit(
            @PathVariable("clientAccountId") int clientAccountId
    ){
        return String.valueOf(getActualBalance(clientAccountId));
    }

    @GetMapping("/deposit/{clientAccountId}/{amount}")
    public String receiveDeposit(
            @PathVariable("clientAccountId") int clientAccountId,
            @PathVariable("amount") int amount
    ){
        return deposit(clientAccountId, amount);
    }

    @PostMapping("/withdrawal/{clientAccountId}/{amount}")
    public String receivewithdrawal(
            @PathVariable("clientAccountId") int clientAccountId,
            @PathVariable("amount") int amount
    ){
        return withdrawal(clientAccountId, amount);
    }

    private String withdrawal(int clientAccountId, int amount) {
        synchronized(this){
            int actualBalance = getActualBalance(clientAccountId);
            try {Thread.sleep(5000);} catch (InterruptedException e) {e.printStackTrace();}
            if(actualBalance >= amount)
                actualBalance = setActualBalance(clientAccountId, actualBalance - amount);
            return String.valueOf(actualBalance);
        }
    }

    private String deposit(int clientAccountId, int amount) {
        synchronized(this) {
            int actualBalance = getActualBalance(clientAccountId);
            try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}
            actualBalance = setActualBalance(clientAccountId, actualBalance + amount);
            return String.valueOf(actualBalance);
        }
    }

    private File getAccountFile(int clientAccountId){
        File accountFile = new File(filesDirectory+String.valueOf(clientAccountId)+".txt");
        if(!accountFile.exists())
            initializeAccountFile(accountFile);
        return accountFile;
    }

    private void initializeAccountFile(File accountFile) {
        try {
            accountFile.createNewFile();
            FileWriter accountFileWriter = new FileWriter(accountFile);
            accountFileWriter.write(String.valueOf(0));
            accountFileWriter.close();
        } catch (IOException e) {e.printStackTrace();}
    }

    private int getActualBalance(int clientAccountId) {
        File accountFile = getAccountFile(clientAccountId);
        int actualBalance = -1;
        try {
            FileReader accountFileReader = new FileReader(accountFile);
            Scanner scanner = new Scanner(accountFileReader);
            actualBalance = scanner.nextInt();
            scanner.close();
            accountFileReader.close();
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
        return actualBalance;
    }

    private int setActualBalance(int clientAccountId, int amount){
        File accountFile = getAccountFile(clientAccountId);
        try {
            FileWriter accountFileWriter = new FileWriter(accountFile, false);
            accountFileWriter.write(String.valueOf(amount));
            accountFileWriter.close();
        }
        catch (IOException e) {e.printStackTrace();}
        return getActualBalance(clientAccountId);
    }


}
