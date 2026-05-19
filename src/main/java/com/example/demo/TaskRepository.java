package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

}

//JpaRepositoryを継承して、DBの全件取得等のメソッドを使えるようにする