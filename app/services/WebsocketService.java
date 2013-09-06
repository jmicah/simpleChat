/*
 * @author Jeremy Huff
 * @author Micah Cooper
 *  
 */

package services;

import java.util.Map;
import java.util.Set;

import models.WebsocketRouter;
import play.mvc.WebSocket;
import play.mvc.WebSocket.In;
import play.mvc.WebSocket.Out;

public class WebsocketService {
	
	/*
	 * This handles the connection to the chat client
	 */
	public static void connect(String user,
							In<String> in,
							Out<String> out) {		
		
		Map<String, WebSocket.Out<String>> sockets = WebsocketRouter.getSockets();
		
		if(sockets.containsKey(user))	{
			out.write("{\"action\": \"RESPONSE_STATUS\", \"status\": \"ERROR\", \"type\": \"dupeUserError\"}");
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
	
	/*
	 * This handles chat message broadcasting
	 */
	public static void talk(String user, String talk, Out<String> out) {		
		
		Map<String, WebSocket.Out<String>> sockets = WebsocketRouter.getSockets();
		
		String message = "{\"action\": \"TALK\", \"user\": " + user + ", \"talk\": "+ talk +"}";
		
		for(Out<String> socket : sockets.values()) {
			if(!out.equals(socket)) {
				socket.write(message);
			}
		}
			
	}
	
	/*
	 * This handles the disconnection from the chat client
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

	/*
	 * This handles the disconnection from the chat client
	 */
	public static void heartbeat(String user,
				String talk, In<String> in, Out<String> out) {		
		
		String response ="dub";
		
		System.out.println(user+" "+talk+" "+response);	
		
		out.write("{\"action\": \"HEARTBEAT\", \"message\": " + response + "}");		
			
	}
	
	
}
