package com.example.todo.repository;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.todo.entity.User;

@Mapper
public interface UserMapper {

    Optional<User> findByUsername(@Param("username") String username);

    int insert(User user);
}