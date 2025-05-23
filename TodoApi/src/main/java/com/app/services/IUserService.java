package com.app.services;


import java.util.Optional;


import com.app.entity.Users;




public interface IUserService 
{
  public Users create(Users user);
  public Users findById(Long id);
  public Optional<Users> findByEmail(String email);
boolean existsByEmail(String email);


}

