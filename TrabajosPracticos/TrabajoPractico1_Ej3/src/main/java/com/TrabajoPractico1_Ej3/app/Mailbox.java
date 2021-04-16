package com.TrabajoPractico1_Ej3.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mailbox {
	
	//TODO, ADD SYNCRONIZED TO THIS

	List<Message> messages;
	
	public Mailbox() {
		messages = Collections.synchronizedList(new ArrayList<Message>());
	}
	
	public List<Message> getMessagesTo(int idTo){
		ArrayList<Message> result = new ArrayList<Message>();
		for (Message message : messages) {
			if(message.getIdTo() == idTo)
				result.add(message);
		}
		return result;
	}
	
	public void sendMessage(String nameFrom, int idFrom, int idTo, String payload) {
		messages.add(new Message(nameFrom, idFrom, idTo, payload));
	}

	public void removeMessages(int loggedUserId) {
		List<Message> messagesToRemove = new ArrayList<>();
		for (Message message : messages) {
			if(message.getIdTo() ==loggedUserId)
				messagesToRemove.add(message);
		}
		messages.removeAll(messagesToRemove);
	}
}
