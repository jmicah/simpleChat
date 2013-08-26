package models;

import java.util.*;

import play.api.Logger;
import play.libs.F.Callback;
import play.mvc.WebSocket;
import play.mvc.WebSocket.Out;


public class ChatRoom {
	
	final static HashMap<String, WebSocket.Out<String>> sockets = new HashMap<>();

	public static void join(final String userString,
							WebSocket.In<String> in,
							final WebSocket.Out<String> out) {		
		
		sockets.put(userString, out);
				
		in.onMessage(new Callback<String>() {
			public void invoke(String event) {
				for(Out<String> socket : sockets.values()) {
					socket.write(" <p class='message'>"+userString+": "+event+"</p>");
				}
			}
		});
		
	}

}
