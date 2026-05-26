package com.example.todo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.todo.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User>findByUsername(String username);
	

}

//userテーブル　DB操作を行うクラス