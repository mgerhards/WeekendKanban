package com.weekendkanban.repository;

import com.weekendkanban.domain.Task;
import com.weekendkanban.domain.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findByStatusOrderByPositionAsc(TaskStatus status);
}