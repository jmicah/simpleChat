/*
 * @author Jeremy Huff
 * @author Micah Cooper
 *  
 */

package models;

import java.io.IOException;
import java.util.*;

import org.codehaus.jackson.*;
import org.codehaus.jackson.map.ObjectMapper;

import play.libs.F.Callback;
import play.mvc.WebSocket;
import services.WebsocketService;


public class WebsocketRouter {
	
	final static Map<String, WebSocket.Out<String>> sockets = new HashMap<String, WebSocket.Out<String>>();

	public static Map<String, WebSocket.Out<String>> getSockets() {
		return sockets;
	}
	
	public static void rout(final WebSocket<String> webSocket,
			final play.mvc.WebSocket.In<String> in, final play.mvc.WebSocket.Out<String> out) {
				
		in.onMessage(new Callback<String>() {
			public void invoke(String event) throws JsonProcessingException, IOException {

				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonObj = mapper.readTree(event);									
				String action = jsonObj.path("action").toString();
				String user = jsonObj.path("user").toString();
				String talk = jsonObj.path("talk").toString();
				
				if(action.equals("\"CONNECT\"")) {
					WebsocketService.connect(user, in, out);
				}
				
				if(action.equals("\"TALK\"")) {
					WebsocketService.talk(user, talk, out);					
				}
				
				if(action.equals("\"DISCONNECT\"")) {
					WebsocketService.disconnect(user, out);
				}
				
				if(action.equals("\"HEARTBEAT\"")) {
					WebsocketService.heartbeat(user, talk, in, out);					
				}
				
			}
		});
		
	}


	
	
}
