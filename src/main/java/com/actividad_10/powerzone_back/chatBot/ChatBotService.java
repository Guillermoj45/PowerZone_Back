package com.actividad_10.powerzone_back.chatBot;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;
import java.io.IOException;

@Service
public class ChatBotService {
    private static final String API_URL = "http://localhost:1234/v1/chat/completions";
    private final OkHttpClient client;

    public ChatBotService() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30000, TimeUnit.SECONDS) // Tiempo de espera de conexión
                .readTimeout(30000, TimeUnit.SECONDS)    // Tiempo de espera de lectura
                .writeTimeout(30000, TimeUnit.SECONDS)   // Tiempo de espera de escritura
                .build();
    }

    public String holaBot(String mensaje) {
        // Construcción del JSON
        JsonObject json = new JsonObject();
        json.addProperty("model", "llama-3.2-1b-instruct");

        JsonArray messages = new JsonArray();

        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", "Eres una IA de ejercicios fitness y dietas");
        messages.add(systemMessage);

        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", mensaje);
        messages.add(userMessage);

        json.add("messages", messages);
        json.addProperty("temperature", 0.7);
        json.addProperty("max_tokens", -1);
        json.addProperty("stream", false);

        // Crear el cuerpo de la petición
        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json"));

        // Crear la solicitud HTTP
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        // Ejecutar la petición y manejar la respuesta
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "Error: " + response.code() + " - " + response.message();
            }
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al comunicarse con la API: " + e.getMessage();
        }
    }
}
