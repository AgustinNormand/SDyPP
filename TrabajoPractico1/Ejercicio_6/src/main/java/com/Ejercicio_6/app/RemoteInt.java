package com.Ejercicio_6.app;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInt extends Remote {
    public String sum (String jsonV1, String jsonV2) throws RemoteException;
    
    public String subtract (String jsonV1, String jsonV2) throws RemoteException;
    
}
