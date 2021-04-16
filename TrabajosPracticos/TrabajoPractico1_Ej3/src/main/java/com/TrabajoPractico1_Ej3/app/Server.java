package com.TrabajoPractico1_Ej3.app;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
	
	List<String> users;
	Mailbox mailbox = new Mailbox();
	
	public Server(int port){
        try{
            ServerSocket ss = new ServerSocket(port);
            System.out.println("Server has started on TCP port "+port);
            users = Collections.synchronizedList(new ArrayList<String>());
            while (true){
                Socket client = ss.accept();
                ServerHilo sh = new ServerHilo(client, users, mailbox);
                Thread serverThread = new Thread(sh);
                serverThread.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


