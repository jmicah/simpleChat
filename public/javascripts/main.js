function setWebsockets(rout) {
        	
			var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
			var socket = new WS(rout);
			
			socket.onMessage = function(event) {
				$(".chatWindow").append(event.data);
			}
			
			socket.onmessage = function(event) {
				$(".chatWindow").append(event.data);
			}
			
			 
			$(".inputField").keypress(function(e) {
				if(e.charCode == 13) {				
					var message = $(".inputField").val();
					$(".inputField").val("");
					socket.send(message);
					//$(".chatWindow").append("<br>" + message);
				}
			
			});
			
		}