package com.app.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.app.entity.Todo;
import com.app.entity.Users;
import com.app.repos.TodoRepository;

@Service
public class SummaryService 
{
	private String openaiApiKey = "OPEN_API_KEY";

	private String slackWebhookUrl ="SLACKWEBHOOB_URL";
	
	@Autowired
	private ITodoService todoService;
	
	@Autowired
	private OpenAiChatService openAiChatService;
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	 public String generateSummary(Users user) {
	        List<Todo> all = todoService.getAll(user);
	        List<Todo> pending = all.stream().filter(t -> !t.isCompleted()).toList();
	        String prompt = 
	            "I have "+all.size()+" tasks, "+(all.size() - pending.size())+" are completed, and "+pending.size()+" are pending. " +
	            "Here are the pending tasks: %s. Please summarize them briefly."+
	            pending.stream().map(Todo::getTitle).collect(Collectors.joining(", "));


	        String summary = openAiChatService.summarizeText(prompt);

	        
	        
	        try {
	            Map<String, String> slackPayload = new HashMap<>();
	            slackPayload.put("text", summary);

	            HttpHeaders slackHeaders = new HttpHeaders();
	            slackHeaders.setContentType(MediaType.APPLICATION_JSON);

	            HttpEntity<Map<String, String>> slackEntity = new HttpEntity<>(slackPayload, slackHeaders);

	            restTemplate.postForEntity(slackWebhookUrl, slackEntity, String.class);
	        } catch (Exception e) {
	            return "Summary generated, but failed to send to Slack: " + e.getMessage();
	        }

	        return summary;
	    }
	
}
