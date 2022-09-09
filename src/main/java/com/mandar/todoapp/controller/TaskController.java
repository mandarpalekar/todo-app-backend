package com.mandar.todoapp.controller;

import com.mandar.todoapp.model.Task;
import com.mandar.todoapp.repository.TaskRepository;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/tasks")
@Slf4j
@AllArgsConstructor
public class TaskController {

    private TaskRepository taskRepository;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Task> getTask(@PathVariable UUID id) {
        return ResponseEntity.ok(taskRepository.getReferenceById(id));
    }

    @PostMapping(value = "/")
    public ResponseEntity<Task> createNewTask(@RequestBody Task newTask) {
        Task createdTask = taskRepository.saveAndFlush(newTask);
        if(Objects.isNull(createdTask)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable UUID id, @RequestBody Task task) throws Exception {
        Task currentTask = taskRepository.getReferenceById(id);
        BeanUtils.copyProperties(task, currentTask, "task_id");
        Task updatedTask = taskRepository.saveAndFlush(currentTask);
        if(Objects.isNull(updatedTask)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedTask);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteTask(@PathVariable UUID id) {
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
