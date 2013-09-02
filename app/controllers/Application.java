package controllers;

import org.codehaus.jackson.*;

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

    
    public static WebSocket<String> socketHandler(final String username) {
		
    	return new WebSocket<String>() {
			
			public void onReady(WebSocket.In<String> in,
								final WebSocket.Out<String> out) {
				
				
				//System.out.println("current socket is: " + this.toString());
				
				try {
					ChatRoom.socketRouter(this, in, out);
                    //ChatRoom.join(this.toString(), in, out);
                    //ChatRoom.talk(this, in, out);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
				
			}
			
		};
    	
    }
    
}
