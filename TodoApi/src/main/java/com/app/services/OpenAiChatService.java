package com.app.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class OpenAiChatService {

	private final String API_URL = "https://api.openai.com/v1/chat/completions";

    private String apiKey = "OPENAI_SECRET_KEYy";

    private RestTemplate restTemplate = new RestTemplate();

    public String summarizeText(String inputText) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // Build the messages list
        List<Map<String, String>> messages = List.of(
                Map.of("role", "system", "content", "You are a helpful assistant that summarizes text."),
                Map.of("role", "user", "content", "Summarize the following:\n\n" + inputText)
        );

        // Create request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o-mini");
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // Send POST request and get response as Map
        ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, entity, Map.class);

        // Extract summary from response
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

        return message.get("content").toString().trim();
    }
	
}
