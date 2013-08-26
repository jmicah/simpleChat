package controllers;

import models.ChatRoom;
import play.Logger;
import play.libs.F.Callback;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;

import views.html.index;

public class Application extends Controller {
  
    public static Result index() {    	
        return ok(index.render());
    }

    
    public static WebSocket<String> socketHandler() {
		
    	return new WebSocket<String>() {
			
			public void onReady(WebSocket.In<String> in,
								final WebSocket.Out<String> out) {
				
				String getUserString = this.toString();
				String[] splitUserString = getUserString.split("@");
				final String userString = splitUserString[1];
				
				try { 
                    ChatRoom.join(userString, in, out);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
				
			}
			
		};
    	
    }
    
}
