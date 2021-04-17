package com.TrabajoPractico1_Ej3.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ServerHilo implements Runnable{
	BufferedReader canalEntrada;
	PrintWriter canalSalida;
	Socket client;
	
	List<String> users;
	
	Mailbox mailbox;
	
	int loggedUserId = -1;

	public ServerHilo(Socket client, List<String> users, Mailbox mailbox) {
		this.client = client;
		this.users = users;
		this.mailbox = mailbox;
		try {
			canalSalida = new PrintWriter(this.client.getOutputStream(), true);
			canalEntrada = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	private void send(String message) {
		canalSalida.println(message);
	}
	
	private String receive() {
		try {
			return canalEntrada.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void createUser() {
		send("Ingrese el nombre de usuario");
		String userName = receive();
		if(users.contains(userName)) {
			send("El nombre de usuario "+ userName + " ya se encuentra en nuestra base de datos.");
			menuInicial();
		}
		else {
			users.add(userName);
			loggedUserId = users.indexOf(userName);
			send("Usuario registrado exitosamente.");
			menuLogged();
		}
	}

	
	private void login() {
		send("Ingrese el nombre de usuario");
		String userName = receive();
		if(users.contains(userName)) {
			send("Logeado exitosamente");
			loggedUserId = users.indexOf(userName);
			menuLogged();
		}
		else {
			send("El nombre de usuario "+ userName + " no se encuentra en nuestra base de datos.");
			menuInicial();
		}
	}
	
	private void menuInicial() {
		try {
			send("Bienvenido al servidor de chat, presione la opción que desea.");
			send("1-> Crear usuario");
			send("2-> Logearse");
			send("0-> Salir");
			int option = readOption(0, 2);
			switch (option) {
			case 1:
				createUser();
				break;
			case 2:
				login();
				break;
			case 0:
				exit();
				break;
			}
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private void listUsersExceptLogged() {
		for(int i = 0; i < users.size(); i++) 
		    if(i != loggedUserId)
		    	send(i + "-> " + users.get(i));
	}
	
	private boolean areUsersLogged() {
		return users.size() > 1;
	}
	
	private int readUserId() {
		int userId = -1;
		while(userId < 0 || userId > users.size() - 1 || userId == loggedUserId) {
			try {
				userId = Integer.valueOf(receive());
			} catch (Exception e) {
				userId = -1;
			}
		}
		return userId;
	}
	
	private void loopSendingMessages(int idFrom, int idTo) {
		String userMessage = receive();
		while(!userMessage.equals("0")) {
			mailbox.sendMessage(users.get(loggedUserId), idFrom, idTo, userMessage);
			userMessage = receive();
		}
		send("Mensajes enviados");
		menuLogged();
	}
	
	private void sendMessage() {
		if(areUsersLogged()) {
			send("Ingrese ID de el destinatario");
			listUsersExceptLogged();
			int userIdTo = readUserId();
			send("Escriba sus mensajes, envíe 0 para finalizar");
			loopSendingMessages(loggedUserId, userIdTo);
		}
		else {
			send("Eres el unico usuario en el servidor, debes esperar a que se registre alguien.");
			menuLogged();
		}
		
	}
	
	private void confirmMessages() {
		mailbox.removeMessages(loggedUserId);
		send("Mensajes eliminados");
		menuLogged();
	}
	
	private void menuMessages() {
		send("1-> Actualizar mensajes");
		send("2-> Confirmar mensajes");
		send("3-> Volver al menu anterior");
		int option = readOption(1, 3);
		switch (option) {
		case 1:
			listMessages();
			break;
		case 2:
			confirmMessages();
			break;
		case 3:
			menuLogged();
			break;
		}
	}
	
	private int readOption(int fromValue, int toValue) {
		int option = fromValue - 1;
		while(option < fromValue || option > toValue) {
			try {
				option = Integer.valueOf(receive());
			} catch (Exception e) {
				option = fromValue - 1;
			}
		}
		return option;
	}

	private void listMessages() {
		List<Message> messages = mailbox.getMessagesTo(loggedUserId);
		if(messages.size() == 0)
			send("No tienes ningún mensaje sin leer");
		else 
			listFormatedMessages(messages);
		menuMessages();
	}
	
	private void listFormatedMessages(List<Message> messages) {
		ArrayList<Integer> idListedMessages = new ArrayList<>();
		for(Message message : messages) {
			if(!idListedMessages.contains(message.getIdFrom())) {
				listMessagesFrom(message.getIdFrom(), message.getNameFrom(), messages);
				idListedMessages.add(message.getIdFrom());
			}
		}
	}
	
	private void listMessagesFrom(int idFrom, String nameFrom, List<Message> messages) {
		send("Messages from "+ nameFrom+":");
		for (Message message : messages) {
			if(message.getIdFrom() == idFrom)
				send(message.getPayload());
		}
	}
	
	private void exit() {
		try {
			String loggedUserName = "";
			if(loggedUserId != -1)
				loggedUserName = users.get(loggedUserId);
			send("Hasta la próxima " + loggedUserName);
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void closeSession() {
		loggedUserId = -1;
		menuInicial();
	}
	
	private void menuLogged() {
		send("Bienvenido " + users.get(loggedUserId));
		send("1-> Enviar mensaje");
		send("2-> Listar mensajes recibidos");
		send("3-> Cerrar sesión");
		int option = readOption(1, 3);
		switch (option) {
		case 1:
			sendMessage();
			break;
		case 2:
			listMessages();
			break;
		case 3:
			closeSession();
			break;
		}
	}

	@Override
	public void run() {
		menuInicial();
	}

}
