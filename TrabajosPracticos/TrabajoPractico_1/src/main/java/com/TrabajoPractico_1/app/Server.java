package com.TrabajoPractico_1.app;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    public Server(int port){
        try{
            ServerSocket ss = new ServerSocket(port);
            System.out.println("Server has started on TCP port "+port);

            while (true){
                Socket client = ss.accept();
                
                System.out.println("Atendiendo al cliente: "+client.getPort());

                ServerHilo sh = new ServerHilo(client);
                Thread serverThread = new Thread(sh);
                serverThread.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main( String[] args )
    {
        Server server = new Server(9090);
    }
}