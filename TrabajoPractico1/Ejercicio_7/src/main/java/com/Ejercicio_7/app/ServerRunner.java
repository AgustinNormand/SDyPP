package com.Ejercicio_7.app;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.google.gson.Gson;

public class ServerRunner implements RemoteInt, Remote{

	Gson gson = new Gson();

	@Override
	public String processTask(String jsonTask) throws RemoteException {
		Tarea task = gson.fromJson(jsonTask, Fibonacci.class);
		return String.valueOf(task.ejecutar());
	}
}