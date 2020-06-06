package com.example.websocketdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * configure the websocket connection
 * 
 * 
 * @author mkarim@ntgclarity.com
 * 
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

	/**
	 * The first line defines that the messages whose destination starts with /app
	 * should be routed to message-handling methods in the controller(/chat.sendMessage)
	 * 
	 * And the second line defines that the messages whose destination starts with
	 * (/topic) should be routed to the message broker. Message broker broadcasts
	 * messages to all the connected clients who are subscribed to a particular
	 * topic.
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app");
		registry.enableSimpleBroker("/topic");
	}

	/**
	 * register a websocket endpoint that the clients will use to connect to our
	 * websocket server.
	 */

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws").withSockJS();// withSockJS enable fallback options for browsers that don’t support
													// websocket.
	}

}
