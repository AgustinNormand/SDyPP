package com.TrabajoPractico_1.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Hello world!
 *
 */
public class Server
{
	
	public Server() throws UnknownHostException{
		this(InetAddress.getByName("127.0.0.1"), 9090);
	}
	
    public Server(InetAddress ip, int port){
        this.StartServer(ip, port);
    }
    
	private void StartServer(InetAddress ip, int port) {
		/*
        Capa de Trasnporte: TCP / UDP
         */
        try{
        	//backlog - requested maximum length of the queue of incoming connections.
            ServerSocket ss = new ServerSocket(port, 1, ip);
            System.out.println("Server has started on socket "+ip.getHostAddress()+":"+port);


            while (true){
                Socket client = ss.accept();
                /*
                Particular de java como maneja la E/S del socket ->
                                                    Canal de Entrada -> server (lee)
                                                    Canal de Salida -> server  (escribe)
                --> Canal
                    * String <-- JSON <-- TEXT
                    * Buffer
                    * Object Serializable (JAVA) --> Public class Auto implements Serializable {}
                 */
                System.out.println("Atendiendo al cliente: "+client.getPort());

                // 3 pasos
                // 1er paso
                ServerHilo sh = new ServerHilo(client);
                // 2do paso
                Thread serverThread = new Thread(sh);
                // 3er paso
                serverThread.start();



            }
        }catch (Exception e){
            e.printStackTrace();
        }
	}
	
	public static void main(String args[]) {
		Server server;
    	if(args.length >= 2) {
    		InetAddress ip = null;
			try {
				ip = InetAddress.getByName(args[0]);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
    		 server = new Server(ip, Integer.valueOf(args[1]));
    	} else
			try {
				server = new Server();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
	}
}
