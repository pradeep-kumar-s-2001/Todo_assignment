package com.app.services;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.entity.Todo;
import com.app.entity.Users;




public interface ITodoService {

    public Todo create(Todo todo);

    public Todo update(Todo todo);

    public Todo findById(Long todoId);
    public void deleteById(Long todoId);
    
    public List<Todo> getAll(Users user);
}

