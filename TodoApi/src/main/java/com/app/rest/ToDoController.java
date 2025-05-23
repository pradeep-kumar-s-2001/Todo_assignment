package com.app.rest;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.TodoDTO;
import com.app.entity.Todo;
import com.app.entity.Users;
import com.app.exception.TodoException;
import com.app.request.CreateTodoRequest;
import com.app.response.ApiResponse;
import com.app.services.CustomUserDetails;
import com.app.services.ITodoService;
import com.app.services.SummaryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/todo")
public class ToDoController {

    @Autowired
    private ITodoService todoService;
    
    
    @Autowired
    private SummaryService summaryService;

    @PostMapping("/secure/create")
    public ResponseEntity<ApiResponse<?>> createTodo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody CreateTodoRequest request,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new TodoException("Invalid Input data", HttpStatus.BAD_REQUEST);
        }

        Users user = customUserDetails.getUser();

        Todo todo = new Todo();
        todo.setUser(user);
        todo.setTitle(request.getTitle());
        todo.setDescription(null);
        todo.setCompleted(false);

        todo = todoService.create(todo);
        return ResponseEntity.ok(new ApiResponse<>("success", "Todo created successfully", null));
    }

    @PutMapping("/secure/update/{id}")
    public ResponseEntity<ApiResponse<?>> updateTodo(
            @PathVariable Long id,
            @Valid @RequestBody CreateTodoRequest request,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new TodoException("Invalid Input data", HttpStatus.BAD_REQUEST);
        }

        Todo todo = todoService.findById(id);

        if (todo == null) {
            throw new TodoException("Todo not found", HttpStatus.NOT_FOUND);
        }

        todo.setTitle(request.getTitle());
        todo.setDescription(null);
       

        todo = todoService.update(todo);
        return ResponseEntity.ok(new ApiResponse<>("success", "Todo updated successfully", null));
    }

    @DeleteMapping("/secure/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteTodo(@PathVariable Long id) {
        Todo todo = todoService.findById(id);

        if (todo == null) {
            throw new TodoException("Todo not found", HttpStatus.NOT_FOUND);
        }

        todoService.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "Todo deleted successfully", null));
    }

    @GetMapping("/secure/{id}")
    public ResponseEntity<ApiResponse<?>> getTodoById(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long id) {

        Todo todo = todoService.findById(id);

        if (todo == null) {
            throw new TodoException("Todo not found", HttpStatus.NOT_FOUND);
        }

        // Check if the todo belongs to the authenticated user
        if (!todo.getUser().getId().equals(customUserDetails.getUser().getId())) {
            throw new TodoException("Unauthorized access", HttpStatus.FORBIDDEN);
        }

        TodoDTO todoDTO = mapToTodoDTO(todo);
        return ResponseEntity.ok(new ApiResponse<>("success", "Todo retrieved successfully", todoDTO));
    }
    
    
    @GetMapping("/secure/all")
    public ResponseEntity<ApiResponse<?>> getAllTodoByUser(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        List<Todo> todos = todoService.getAll(customUserDetails.getUser());
        
        Map<String, Object> response = new HashMap<>();
        response.put("todoData", todos);


        return ResponseEntity.ok(new ApiResponse<>("success", "Todo retrieved successfully", response ));
    }
    
    
    @GetMapping("/secure/get-summary")
    public ResponseEntity<ApiResponse<?>> getSummary(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        
        String summaryMessage = summaryService.generateSummary(customUserDetails.getUser());


        return ResponseEntity.ok(new ApiResponse<>("success", "Todo retrieved successfully", summaryMessage ));
    }

   

    @PutMapping("/secure/update/status/{id}")
    public ResponseEntity<ApiResponse<?>> toggleTodoComplete(
            @PathVariable Long id) {

        Todo todo = todoService.findById(id);

        if (todo == null) {
            throw new TodoException("Todo not found", HttpStatus.NOT_FOUND);
        }

        todo.setCompleted(true);
        todoService.update(todo);
        return ResponseEntity.ok(new ApiResponse<>("success", "Todo status updated successfully", null));
    }

    private TodoDTO mapToTodoDTO(Todo todo) {
        TodoDTO dto = new TodoDTO();
        dto.setId(todo.getId());
        dto.setTitle(todo.getTitle());
        dto.setDescription(todo.getDescription());
        dto.setCompleted(todo.isCompleted());
        
        
        // Map user without sensitive information
        Users user = todo.getUser();
        user.setPassword(null);
        dto.setUser(user);
        
        return dto;
    }
}
