package Sever;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // prefix used for server-to-client messages
        config.enableSimpleBroker("/game/");
        // prefix used for client-to-server messages
        config.setApplicationDestinationPrefixes("/player");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // endpoint path used for client to establish WebSocket connection with server
        // ws://ipaddr:port/demo-websocket
        registry.addEndpoint("/demo-websocket")
                .setAllowedOriginPatterns("*"); // accept cross-origin requests from any origin
    }
}
