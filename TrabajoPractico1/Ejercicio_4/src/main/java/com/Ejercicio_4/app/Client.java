package com.Ejercicio_4.app;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    public Client (int port){
        try{
            Registry clientRMI = LocateRegistry.getRegistry("127.0.0.1", port);
            String[] servicesList = clientRMI.list();

            for (String service : servicesList){
                System.out.println(" Service: "+service);
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
        new Client(port);
    }
}
