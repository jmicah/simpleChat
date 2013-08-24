package controllers;

import play.*;
import play.mvc.*;
import play.libs.F.Callback;

import views.html.*;

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
				
				in.onMessage(new Callback<String>() {
					public void invoke(String event) {
					
					    Logger.info(userString + ": " + event);
						out.write(" <p class='message'>"+userString+": "+event+"</p>");
					
					
					}
				});
				
				
				
			}
			
		};
    	
    }
    
}
