package com.eidiko.security.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eidiko.security.model.Users;
@Repository
public interface UserRepo extends JpaRepository<Users, Integer>{
public Users findByEmail(String email);
}
