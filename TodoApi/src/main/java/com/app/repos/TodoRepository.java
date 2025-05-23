package com.app.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.Todo;
import com.app.entity.Users;

public interface TodoRepository extends JpaRepository<Todo, Long> {
	
	List<Todo> findByUser(Users user);

}
