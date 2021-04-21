package com.Ejercicio_5.app;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInt extends Remote {
    public String getWeather () throws RemoteException;
}
