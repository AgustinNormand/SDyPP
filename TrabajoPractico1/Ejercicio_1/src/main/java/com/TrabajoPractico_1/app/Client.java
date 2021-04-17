package com.TrabajoPractico_1.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	private Socket clientSocket;
	private PrintWriter canalSalida;
	private BufferedReader canalEntrada;
	
	public void startConnection(String ip, int port) throws UnknownHostException, IOException {
		clientSocket = new Socket(ip, port);
		canalSalida = new PrintWriter(clientSocket.getOutputStream(), true);
		canalEntrada = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	}
	
	public void stopConnection() {
        try {
			canalEntrada.close();
			canalSalida.close();
	        clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public String sendMessage(String msg) {
        canalSalida.println(msg);
        String resp = null;
		try {
			resp = canalEntrada.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return resp;
    }
	
	public static void main(String args[]) {
		Client client = new Client();
		if(args.length >= 2) 
		{
    		try {
    			while(true) {
    				client.startConnection(args[0], Integer.valueOf(args[1]));
    				System.out.println(client.sendMessage("el_cliente_pregunta"));
    				client.stopConnection();
    				try {
						Thread.sleep(1700);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
    			}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();} 
    		catch (IOException e) {
				e.printStackTrace();}
    	} else
			try 
			{
				while(true) {
					client.startConnection("127.0.0.1", 9090);
					System.out.println(client.sendMessage("el_cliente_pregunta"));
					client.stopConnection();
					try {
						Thread.sleep(1700);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}catch (UnknownHostException e) 
				{e.printStackTrace();}
			catch (IOException e) 
				{e.printStackTrace();}
	}

}
