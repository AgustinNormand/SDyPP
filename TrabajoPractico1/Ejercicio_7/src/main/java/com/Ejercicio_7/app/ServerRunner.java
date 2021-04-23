package com.Ejercicio_7.app;

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ServerRunner implements RemoteInt, Remote{

	Gson gson = new Gson();

	@Override
	public String processTask(String jsonTask) throws RemoteException {
		Tarea task = null;
		JSONObject jsonObj = new JSONObject(jsonTask);
		String className = (String) jsonObj.get("class");
		jsonObj.remove("class");
		try {
			task = (Tarea) gson.fromJson(jsonObj.toString(), Class.forName(className));
		} catch (Exception e) {
			e.printStackTrace();}
		return String.valueOf(task.ejecutar());
	}
}