package com.Ejercicio_7.app;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInt extends Remote {
    public String processTask (String jsonTask) throws RemoteException;
}
