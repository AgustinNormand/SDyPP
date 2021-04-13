package src.main.java.com.TrabajoPractico_1.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class Server
{
    public Server(InetAddress ip, int port){
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
}
