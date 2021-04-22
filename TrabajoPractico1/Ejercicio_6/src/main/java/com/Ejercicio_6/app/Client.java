package com.Ejercicio_6.app;

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
            RemoteInt ri = (RemoteInt) clientRMI.lookup("VectorSumServer");
            
            Vector v1 = new Vector(new int[]{1,2,3,4,5,6,7,8,9,10});
            Vector v2 = new Vector(new int[]{0,1,2,3,4,5,6,7,8,9});
            
            String jsonV1 = gson.toJson(v1, Vector.class);
            String jsonV2 = gson.toJson(v2, Vector.class);
            
            String jsonV3 = ri.sum(jsonV1, jsonV2);
            Vector v3 = gson.fromJson(jsonV3, Vector.class);
            System.out.println(v3);
            
            
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
