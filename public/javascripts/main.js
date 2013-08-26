function setWebsockets(rout) {
        	
			var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
			var socket = new WS(rout);
			
			socket.onMessage = function(event) {
				$(".chatWindow").append(event.data);
			}
			
			socket.onmessage = function(event) {
				$(".chatWindow").append(event.data);
			}
			
			 
			$(".inputField").keypress(function(event) {
				var keycode = (event.keyCode ? event.keyCode : event.which);
				if(keycode == '13' && !event.shiftKey){				
					var message = $(".inputField").val();
					$(".inputField").val("");
					socket.send(message);
					//$(".chatWindow").append("<br>" + message);
				}
			
			});
			
		}