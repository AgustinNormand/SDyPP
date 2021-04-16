package com.TrabajoPractico_UDP.app;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server {
	
	public Server(int port) {
		try {
			DatagramSocket datagramSocket = new DatagramSocket(port);
			System.out.println("Server has started on UDP port "+port);
			 while (true){
				 /* Recibir peticion */
				 byte[] bufer = new byte[1000];
				 DatagramPacket peticion = new DatagramPacket(bufer, bufer.length);
				 datagramSocket.receive(peticion);
				 System.out.println("Datagrama recibido del host " + peticion.getAddress() +
						 " desde el puerto " + peticion.getPort() +
						 " con el payload " + peticion.getData().toString());
				 /* */
				 
				 /* Enviar respuesta */
				 DatagramPacket respuesta = new DatagramPacket(peticion.getData(),
						 									   peticion.getLength(),
						 									   peticion.getAddress(),
						 									   peticion.getPort());
				 
				 datagramSocket.send(respuesta);
				 /* */
			 }
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
