package com.actividad_10.powerzone_back.webSocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class webSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // Prefijo para las respuestas
        registry.setApplicationDestinationPrefixes("/app"); // Prefijo para los mensajes entrantes
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-native") // Endpoint nativo
                .setAllowedOrigins("*"); // Permite todas las conexiones para pruebas
        registry.addEndpoint("/ws") // Endpoint con SockJS
                .setAllowedOrigins("*")
                .withSockJS();
    }
}

