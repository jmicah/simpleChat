package websocketServices;

import java.util.Map;
import java.util.Set;

import models.WebsocketRouter;
import play.mvc.WebSocket;
import play.mvc.WebSocket.In;
import play.mvc.WebSocket.Out;

public class Websocket {
	
	/*
	 * This handles the connection to the chat client
	 */
	public static void connect(String user,
							In<String> in,
							Out<String> out) {		
		//Map<String, WebSocket.Out<String>> sockets = chatroom.getSockets();
		Map<String, WebSocket.Out<String>> sockets = WebsocketRouter.getSockets();
		
		if(sockets.containsKey(user))	{
			out.write("{\"action\": \"ERROR\", \"type\": \"already a user by that name\"}");
		} else {
			out.write("{\"action\": \"CONNECT\"}");
			sockets.put(user, out);
			Set<String> users = sockets.keySet();
			String message = "{\"action\": \"UPDATE_USERS\", \"users\": " + users + "}";
			for(Out<String> socket : sockets.values()) {
				socket.write(message);
			}
		}		
	}
	
	public static void talk(String user, String talk, Out<String> out) {		
		
		Map<String, WebSocket.Out<String>> sockets = WebsocketRouter.getSockets();
		

		String message = "{\"action\": \"TALK\", \"user\": " + user + ", \"talk\": "+ talk +"}";
		for(Out<String> socket : sockets.values()) {
			if(!out.equals(socket)) {
				socket.write(message);
			}
		}
			
	}
	
	public static void disconnect(String user,
				Out<String> out) {		
		
		Map<String, WebSocket.Out<String>> sockets = WebsocketRouter.getSockets();
		
		
		Out<String> thisSocket = sockets.get(user);
		thisSocket.write("{\"action\": \"DISCONNECT\"}");
		sockets.remove(user);
		Set<String> users = sockets.keySet();
		String message = "{\"action\": \"UPDATE_USERS\", \"users\": " + users + "}";
		for(Out<String> socket : sockets.values()) {
		socket.write(message);
		
		}
	}

}
