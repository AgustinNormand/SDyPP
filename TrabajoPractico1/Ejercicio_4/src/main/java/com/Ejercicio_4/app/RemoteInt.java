package com.Ejercicio_4.app;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInt extends Remote {
    public String getWeather () throws RemoteException;
}
