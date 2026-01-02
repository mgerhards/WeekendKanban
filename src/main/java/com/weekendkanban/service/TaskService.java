package com.weekendkanban.service;

import com.weekendkanban.domain.Task;
import com.weekendkanban.domain.TaskStatus;
import com.weekendkanban.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> findByStatus(TaskStatus status) {
        return taskRepository.findByStatusOrderByPositionAsc(status);
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow();
    }
}