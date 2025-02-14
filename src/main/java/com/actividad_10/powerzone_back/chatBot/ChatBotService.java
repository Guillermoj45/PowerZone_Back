package com.actividad_10.powerzone_back.chatBot;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class ChatBotService {

    @Value("${GOOGLE_API_KEY}")
    private String googleApiKey;

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";
    private final OkHttpClient client;

    public ChatBotService() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // Tiempo de espera de conexión
                .readTimeout(30, TimeUnit.SECONDS)    // Tiempo de espera de lectura
                .writeTimeout(30, TimeUnit.SECONDS)   // Tiempo de espera de escritura
                .build();
    }

    public String holaBot(String mensaje) {
        JsonObject json = new JsonObject();
        JsonArray contents = new JsonArray();
        JsonObject content = new JsonObject();
        JsonArray parts = new JsonArray();
        JsonObject part = new JsonObject();

        part.addProperty("text", mensaje);
        parts.add(part);
        content.add("parts", parts);
        contents.add(content);
        json.add("contents", contents);

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(API_URL + "?key=" + googleApiKey)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "Error: " + response.code() + " - " + response.message();
            }
            String responseBody = response.body().string();
            JsonObject responseJson = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonArray candidates = responseJson.getAsJsonArray("candidates");
            if (candidates != null && candidates.size() > 0) {
                JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
                JsonObject contentObject = firstCandidate.getAsJsonObject("content");
                JsonArray partsArray = contentObject.getAsJsonArray("parts");
                if (partsArray != null && partsArray.size() > 0) {
                    return partsArray.get(0).getAsJsonObject().get("text").getAsString();
                }
            }
            return "No content found in the response.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al comunicarse con la API: " + e.getMessage();
        }
    }
}