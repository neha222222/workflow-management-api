package com.workforcemgmt.controller;

import com.workforcemgmt.dto.*;
import com.workforcemgmt.model.*;
import com.workforcemgmt.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {
    
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody CreateTaskRequest request) {
        log.info("Creating new task: {}", request.getTitle());
        Task task = taskService.createTask(request);
        return ResponseEntity.ok(task);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        log.info("Fetching all tasks");
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        log.info("Fetching task with ID: {}", id);
        Task task = taskService.getTaskById(id);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Task>> getTasksByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("Fetching tasks for date range: {} to {}", startDate, endDate);
        List<Task> tasks = taskService.getTasksByDateRange(startDate, endDate);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Task>> getTasksByPriority(@PathVariable Priority priority) {
        log.info("Fetching tasks with priority: {}", priority);
        List<Task> tasks = taskService.getTasksByPriority(priority);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/assign-by-ref")
    public ResponseEntity<Task> assignTaskByReference(@RequestBody AssignTaskRequest request) {
        log.info("Assigning task by reference: {} to staff: {}", request.getCustomerReference(), request.getStaffId());
        try {
            Task task = taskService.assignTaskByReference(request.getCustomerReference(), request.getStaffId());
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            log.error("Error assigning task: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/priority")
    public ResponseEntity<Task> updateTaskPriority(@PathVariable Long id, @RequestBody UpdatePriorityRequest request) {
        log.info("Updating priority for task {}: {}", id, request.getPriority());
        try {
            Task task = taskService.updateTaskPriority(id, request.getPriority());
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            log.error("Error updating task priority: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Task> addComment(@PathVariable Long id, @RequestBody AddCommentRequest request) {
        log.info("Adding comment to task {}: {}", id, request.getComment());
        try {
            Task task = taskService.addComment(id, request);
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            log.error("Error adding comment: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/staff")
    public ResponseEntity<List<Staff>> getAllStaff() {
        log.info("Fetching all staff");
        List<Staff> staff = taskService.getAllStaff();
        return ResponseEntity.ok(staff);
    }
}