package com.example.demo;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // 自分のタスク一覧（ページング付き）
    Page<Task> findByUsername(String username, Pageable pageable);

    // 自分のタスクを1件取得（IDとユーザー名で絞り込み）
    Optional<Task> findByIdAndUsername(Long id, String username);

}

//JpaRepositoryを継承して、DBの全件取得等のメソッドを使えるようにする