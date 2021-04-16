package com.TrabajoPractico1_Ej3.app;

public class Message {
	
	int idFrom;
	String nameFrom;
	int idTo;
	String payload;
	
	public Message(String nameFrom, int idFrom, int idTo, String payload) {
		this.idFrom = idFrom;
		this.nameFrom = nameFrom;
		this.idTo = idTo;
		this.payload = payload;
		
	}
	
	public int getIdFrom() {
		return idFrom;
	}
	
	public String getNameFrom() {
		return nameFrom;
	}
	
	public int getIdTo() {
		return idTo;
	}
	
	public String getPayload() {
		return payload;
	}
	
	public String toString(){
	    return "Message from " + nameFrom + ": " + payload;
	  }

}
