package com.Ejercicio_6.app;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.google.gson.Gson;

public class ServerRunner implements RemoteInt, Remote{

	Gson gson = new Gson();
	
	@Override
	public String sum(String jsonV1, String jsonV2) throws RemoteException {
		Vector v1 = gson.fromJson(jsonV1, Vector.class);
		Vector v2 = gson.fromJson(jsonV2, Vector.class);
		Vector v3 = v1.sum(v2);
		String jsonV3 = gson.toJson(v3, Vector.class);
		return jsonV3;
	}

	@Override
	public String subtract(String jsonV1, String jsonV2) throws RemoteException {
		Vector v1 = gson.fromJson(jsonV1, Vector.class);
		Vector v2 = gson.fromJson(jsonV2, Vector.class);
		Vector v3 = v1.subtract(v2);
		String jsonV3 = gson.toJson(v3, Vector.class);
		return jsonV3;
	}
}