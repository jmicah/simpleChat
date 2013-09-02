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
				String message = jsonObj.path("message").toString();
				
				if(action.equals("\"CONNECT\"")) {
					connect(user, in, out);
				}
				
				if(action.equals("\"TALK\"")) {		
						
					for(Out<String> socket : sockets.values()) {
						if(!out.equals(socket)) {
							socket.write(" <p class='message'>"+user+": "+ message +"</p>");
						}
					}
					
				}
				
				if(action.equals("\"DISCONNECT\"")) {
					System.out.println("we are disconnecting");
				}
				
			}
		});
	}
	
	
	public static void connect(final String user,
							In<String> in,
							final Out<String> out) {		
		
		if(sockets.containsKey(user))	{
			out.write("{\"action\": \"ERROR\", \"type\": \"already a user by that name\"}");
		} else {
			out.write("{\"action\": \"WELCOME\"}");
			sockets.put(user, out);
		}
		
		
	}
	
	
//	public static void talk(final WebSocket<String> webSocket,
//		WebSocket.In<String> in,
//	final WebSocket.Out<String> out) {		
//		in.onMessage(new Callback<String>() {
//			public void invoke(String event) throws JsonProcessingException, IOException {
//
//			ObjectMapper mapper = new ObjectMapper();
//			JsonNode jsonObj = mapper.readTree(event);									
//			   String message = jsonObj.path("message").toString();
//			   String user = jsonObj.path("user").toString();
//				for(Out<String> socket : sockets.values()) {
//					if(!out.equals(socket)) {
//						socket.write(" <p class='message'>"+user+": "+ message +"</p>");
//					}
//				}
//		}
//
//		});
//	
//	}


}
