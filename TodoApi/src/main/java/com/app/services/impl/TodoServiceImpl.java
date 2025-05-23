package com.app.services.impl;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.entity.Todo;
import com.app.entity.Users;
import com.app.exception.TodoException;
import com.app.repos.TodoRepository;
import com.app.services.ITodoService;

@Service
public class TodoServiceImpl implements ITodoService {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public Todo create(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public Todo update(Todo todo) {
        Long id = todo.getId();
        if (id == null || !todoRepository.existsById(id)) {
            throw new TodoException("Todo not found with id: " + id, null);
        }
        return todoRepository.save(todo);
    }


    @Override
    public Todo findById(Long todoId) {
        return todoRepository.findById(todoId)
            .orElseThrow(() -> new TodoException("Todo not found with id: " + todoId, null));
    }

    @Override
    public void deleteById(Long todoId) {
        if (!todoRepository.existsById(todoId)) {
            throw new TodoException("Cannot delete. Todo not found with id: " + todoId, null);
        }
        todoRepository.deleteById(todoId);
    }
    
    @Override
    public List<Todo> getAll(Users user) {
    	return todoRepository.findByUser(user);
    }
}
