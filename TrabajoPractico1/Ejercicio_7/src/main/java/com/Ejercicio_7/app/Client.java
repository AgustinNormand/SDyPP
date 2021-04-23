package com.Ejercicio_7.app;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.json.JSONObject;

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
            
            Tarea task = new Fibonacci(25);
            
            Tarea task2 = new RandomGenerator(100000);
            
            System.out.println("Resultado Fibonacci: "+sendTask(ri, task));
            
            System.out.println("Resultado Random: "+sendTask(ri, task2));
            
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    
    private String sendTask(RemoteInt ri, Tarea task) {
    	String jsonTask = gson.toJson(task);
        JSONObject obj = new JSONObject(jsonTask);
        String className = task.getClass().toString().split("class ")[1];
        obj.put("class", className);
        
        try {
			return ri.processTask(obj.toString());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return "";
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
