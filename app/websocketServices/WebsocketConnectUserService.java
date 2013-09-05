package websocketServices;

import java.util.Map;
import java.util.Set;

import models.WebsocketRouter;

import play.mvc.WebSocket;
import play.mvc.WebSocket.In;
import play.mvc.WebSocket.Out;

public class WebsocketConnectUserService {
	
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

}
