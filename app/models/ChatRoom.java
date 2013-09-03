/*
 * @ Jeremy Huff
 * @ Micah Cooper
 *  
 */

/*
 * Potential inbound actions:
 * 	-CONNECT
 *  -TALK
 *  -DISCONNECT
 *  
 *  
 *  Potential outbound actions:
 *   -ERROR
 *   -CONNECT
 *   -TALK
 *   -UPDATE_USERS
 *   -DISCONNECT
 */
package models;

import java.io.IOException;
import java.util.*;

import play.api.Logger;
import play.api.libs.json.JsValue;
import play.libs.Json;
import play.libs.Json.*;                        
import static play.libs.Json.toJson;
import org.codehaus.jackson.*;
import org.codehaus.jackson.map.ObjectMapper;

import play.libs.F.Callback;
import play.mvc.WebSocket;
import play.mvc.WebSocket.In;
import play.mvc.WebSocket.Out;


public class ChatRoom {
	
	final static Map<String, WebSocket.Out<String>> sockets = new HashMap<String, WebSocket.Out<String>>();

	
	public static void socketRouter(final WebSocket<String> webSocket,
			final play.mvc.WebSocket.In<String> in, final play.mvc.WebSocket.Out<String> out) {
		
		in.onMessage(new Callback<String>() {
			public void invoke(String event) throws JsonProcessingException, IOException {

				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonObj = mapper.readTree(event);									
				String action = jsonObj.path("action").toString();
				String user = jsonObj.path("user").toString();
				String talk = jsonObj.path("talk").toString();
				System.out.println(action);	
				
				if(action.equals("\"CONNECT\"")) {
					connect(user, in, out);
				}
				
				if(action.equals("\"TALK\"")) {
					talk(user, talk, out);					
				}
				
				if(action.equals("\"DISCONNECT\"")) {
					disconnect(user, out);
				}
				
			}
		});
	}


	

	/*
	 * This handles the connection to the chat client
	 */
	public static void connect(final String user,
							In<String> in,
							final Out<String> out) {		
		
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
	
	/*
	 * This handles all chats
	 */
	protected static void talk(String user, String talk, Out<String> out) {
		String message = "{\"action\": \"TALK\", \"user\": " + user + ", \"talk\": "+ talk +"}";
		for(Out<String> socket : sockets.values()) {
			if(!out.equals(socket)) {
				socket.write(message);
			}
		}
	}
	
	/*
	 * This handles disconnection from the chat client
	 */
	protected static void disconnect(String user, Out<String> out) {
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
