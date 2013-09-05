function setWebsockets(rout) {
        	var username = $(".username").val();
        
			var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
			var socket = new WS(rout);
			
			$(".setUsername").click(function() {
				username = $(".username").val();
				if(!$(this).hasClass("connected")) {
					if(username != ""){
						var socketMsg = '{"action": "CONNECT", "user": "'+ username +'"}'
						socket.send(socketMsg);
					}
				} else {
					var socketMsg = '{"action": "DISCONNECT", "user": "'+ username +'"}'
					socket.send(socketMsg);
				}
			});
			
			socket.onmessage = function(event) {
				//
				
				var message = $.parseJSON(event.data);
				console.log(message)
				if(message.action == "RESPONSE_STATUS") {
					if(message.type == "dupeUserError"){
						$(".username").val("");
						$(".setUsername").html("Try Another");
					}
				}
				
				if(message.action == "CONNECT") {
					$(".username").hide();
					$(".displayUsername").html(username);
					$(".mainSection").show(300);
					$(".setUsername").html("Log Out").addClass("connected");
					$(".chatWindow").append("<h5>Welcome "+ username +", you have connected to the chat client!</h5>");
				}
				
				if(message.action == "TALK") {
					$(".chatWindow").append("<p class='message'><b>"+message.user +":</b> "+ message.talk+"</p>");
					$(".chatWindow").scrollTop($(".chatWindow")[0].scrollHeight);
				}
				
				if(message.action == "UPDATE_USERS") {
					$(".userWindow").html("");
					$(message.users).each(function() {
						$(".userWindow").append("<p>"+this+"</p>");
					});
				}
				
				if(message.action == "DISCONNECT") {
					$(".displayUsername").html("");
					$(".mainSection").hide(300);
					$(".username").val("");
					$(".username").show();
					$(".chatWindow").html("");
					$(".userWindow").html("");
					$(".setUsername").html("Log In").removeClass("connected");
				}
				
				
			}
				 
			$(".inputField").keypress(function(event) {
				var keycode = (event.keyCode ? event.keyCode : event.which);
				if(keycode == '13' && !event.shiftKey){
					var message = $(".inputField").val(); 
					var socketMsg = '{"action": "TALK", "user": "'+ username +'", "talk": "' + message + '"}'
					$(".inputField").val("");
					socket.send(socketMsg);
					$(".chatWindow").append("<p class='message'><b>you:</b> " + message + "</p>");
					$(".chatWindow").scrollTop($(".chatWindow")[0].scrollHeight);
					return false;
				}
			});	
		}