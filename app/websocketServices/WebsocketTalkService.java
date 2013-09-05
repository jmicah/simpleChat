package websocketServices;

import java.util.Map;
import java.util.Set;

import models.WebsocketRouter;
import play.mvc.WebSocket;
import play.mvc.WebSocket.In;
import play.mvc.WebSocket.Out;

public class WebsocketTalkService {
	
	/*
	 * This handles the connection to the chat client
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

}
