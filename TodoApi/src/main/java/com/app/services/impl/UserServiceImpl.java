package com.app.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entity.Users;
import com.app.repos.UsersRepository;
import com.app.services.IUserService;



@Service
public class UserServiceImpl implements IUserService {
	@Autowired
    private  UsersRepository userRepository;

    @Override
	public Users create(Users user) {
		return userRepository.save(user);
	}

    @Override
	public Users findById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

 // In UserServiceImpl.java
    @Override
    public Optional<Users> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

	
   
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


	
}
