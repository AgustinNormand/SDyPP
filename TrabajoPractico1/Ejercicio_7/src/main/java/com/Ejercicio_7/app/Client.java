package com.Ejercicio_7.app;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.google.gson.Gson;

public class Client {
	
	Gson gson = new Gson();
	
    public Client (int port, String ip){
        try{
            Registry clientRMI = LocateRegistry.getRegistry(ip, port);
            System.out.println("Querying "+ip+" on port "+port);
            String[] servicesList = clientRMI.list();

            for (String service : servicesList){
                System.out.println("Service: "+service);
            }
            RemoteInt ri = (RemoteInt) clientRMI.lookup("TaskProcessorServer");
            
            Tarea task = new Fibonacci(2500);
            
            Tarea tasks[] = new Tarea[]{new Fibonacci(5), new RandomGenerator(100000)};
            
            //Tarea task2 = new RandomGenerator();
            
            String jsonTask = gson.toJson(task);
            System.out.println(jsonTask);
            System.out.println(ri.processTask(jsonTask));

            
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void main( String[] args )
    {
        int port = 9090;
        String ip = "127.0.0.1";
        if(args.length == 1)
        	ip = args[0];
        new Client(port, ip);
    }
}
