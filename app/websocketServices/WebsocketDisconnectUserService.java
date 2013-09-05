package websocketServices;

import java.util.Map;
import java.util.Set;

import models.WebsocketRouter;
import play.mvc.WebSocket;
import play.mvc.WebSocket.In;
import play.mvc.WebSocket.Out;

public class WebsocketDisconnectUserService {
	
	/*
	 * This handles the connection to the chat client
	 */
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
