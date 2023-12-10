package com.softhomeliving.softhomeliving.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.softhomeliving.softhomeliving.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {
	
	UserDetails findByLogin(String login);

}
