/*
 * @ Jeremy Huff
 * @ Micah Cooper
 *  
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
import websocketServices.Websocket;


public class WebsocketRouter {
	
	final static Map<String, WebSocket.Out<String>> sockets = new HashMap<String, WebSocket.Out<String>>();

	
	public static void socketRouter(final WebSocket<String> webSocket,
			final play.mvc.WebSocket.In<String> in, final play.mvc.WebSocket.Out<String> out) {
		
		final WebsocketRouter thisChatroom = new WebsocketRouter();
		
		in.onMessage(new Callback<String>() {
			public void invoke(String event) throws JsonProcessingException, IOException {

				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonObj = mapper.readTree(event);									
				String action = jsonObj.path("action").toString();
				String user = jsonObj.path("user").toString();
				String talk = jsonObj.path("talk").toString();
				
				if(action.equals("\"CONNECT\"")) {
					Websocket.connect(user, in, out);
				}
				
				if(action.equals("\"TALK\"")) {
					Websocket.talk(user, talk, out);					
				}
				
				if(action.equals("\"DISCONNECT\"")) {
					Websocket.disconnect(user, out);
				}
				
			}
		});
		
	}


	public static Map<String, WebSocket.Out<String>> getSockets() {
		return sockets;
	}
	
}
