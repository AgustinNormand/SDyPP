package com.Ejercicio_4.app;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    public Client (int port, String ip){
        try{
            Registry clientRMI = LocateRegistry.getRegistry(ip, port);
            System.out.println("Querying "+ip+" on port "+port);
            String[] servicesList = clientRMI.list();

            for (String service : servicesList){
                System.out.println("Service: "+service);
            }
            RemoteInt ri = (RemoteInt) clientRMI.lookup("WeatherServer");
            String weather = ri.getWeather();
            System.out.println(weather);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void main( String[] args )
    {
        int port = 9090;
        //String ip = "127.0.0.1"; //THIS IS ORIGINAL
        String ip = "192.168.0.3";
        if(args.length == 1)
        	ip = args[0];
        new Client(port, ip);
    }
}
