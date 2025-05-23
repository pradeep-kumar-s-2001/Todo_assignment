package com.app.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
  
	Optional<Users> findByEmail(String email);
	boolean existsByEmail(String email);
}
