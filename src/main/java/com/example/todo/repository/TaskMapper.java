package com.example.todo.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.todo.entity.Task;

@Mapper
public interface TaskMapper {

    List<Task> findByUsername(@Param("username") String username,
                             @Param("limit") int limit,
                             @Param("offset") int offset);

    long countByUsername(@Param("username") String username);

    Optional<Task> findByIdAndUsername(@Param("id") Long id,
                                      @Param("username") String username);

    int insert(Task task);

    int updateMyTask(Task task);

    int deleteByIdAndUsername(@Param("id") Long id,
                             @Param("username") String username);
}