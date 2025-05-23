package com.app.services;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.app.entity.Users;




public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	
	private Users user;
	
	public CustomUserDetails(Users user)
	{
		this.user = user;
	}
	
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public Users getUser()
	{
		return user;
	}

}
