package com.devsuperior.dscatalog.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dscatalog.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);

    
}
