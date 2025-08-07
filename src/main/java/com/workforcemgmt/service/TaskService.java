package com.workforcemgmt.service;

import com.workforcemgmt.model.*;
import com.workforcemgmt.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskService {
    
    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();
    private final Map<Long, Staff> staff = new ConcurrentHashMap<>();
    private final AtomicLong taskIdCounter = new AtomicLong(1);
    private final AtomicLong staffIdCounter = new AtomicLong(1);
    private final AtomicLong commentIdCounter = new AtomicLong(1);
    private final AtomicLong activityIdCounter = new AtomicLong(1);

    public TaskService() {
        initializeData();
    }

    private void initializeData() {
        // Initialize some sample staff
        Staff staff1 = new Staff(staffIdCounter.getAndIncrement(), "John Doe", "john@company.com", "Sales");
        Staff staff2 = new Staff(staffIdCounter.getAndIncrement(), "Jane Smith", "jane@company.com", "Operations");
        Staff staff3 = new Staff(staffIdCounter.getAndIncrement(), "Bob Wilson", "bob@company.com", "Sales");
        
        staff.put(staff1.getId(), staff1);
        staff.put(staff2.getId(), staff2);
        staff.put(staff3.getId(), staff3);
    }

    public Task createTask(CreateTaskRequest request) {
        Task task = new Task();
        task.setId(taskIdCounter.getAndIncrement());
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.ACTIVE);
        task.setPriority(request.getPriority() != null ? request.getPriority() : Priority.MEDIUM);
        task.setStartDate(request.getStartDate());
        task.setDueDate(request.getDueDate());
        task.setAssignedStaffId(request.getAssignedStaffId());
        task.setCustomerReference(request.getCustomerReference());
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        task.setComments(new ArrayList<>());
        task.setActivityLogs(new ArrayList<>());

        tasks.put(task.getId(), task);
        
        // Log task creation
        addActivityLog(task.getId(), "Task created", "System", "Task created with ID: " + task.getId());
        
        return task;
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public Task getTaskById(Long id) {
        return tasks.get(id);
    }

    public List<Task> getTasksByDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        
        return tasks.values().stream()
            .filter(task -> task.getStatus() != TaskStatus.CANCELLED) // Bug fix: exclude cancelled tasks
            .filter(task -> {
                LocalDateTime taskStartDate = task.getStartDate();
                if (taskStartDate == null) return false;
                
                // Smart daily view: Include tasks that started in range OR started before but are still active
                boolean startedInRange = !taskStartDate.isBefore(startDateTime) && !taskStartDate.isAfter(endDateTime);
                boolean startedBeforeButActive = taskStartDate.isBefore(startDateTime) && task.getStatus() == TaskStatus.ACTIVE;
                
                return startedInRange || startedBeforeButActive;
            })
            .collect(Collectors.toList());
    }

    public List<Task> getTasksByPriority(Priority priority) {
        return tasks.values().stream()
            .filter(task -> task.getPriority() == priority)
            .collect(Collectors.toList());
    }

    public Task assignTaskByReference(String customerReference, Long newStaffId) {
        // Find existing task by customer reference
        Optional<Task> existingTaskOpt = tasks.values().stream()
            .filter(task -> customerReference.equals(task.getCustomerReference()))
            .filter(task -> task.getStatus() == TaskStatus.ACTIVE)
            .findFirst();
        
        if (existingTaskOpt.isPresent()) {
            Task existingTask = existingTaskOpt.get();
            // Bug fix: Cancel the old task instead of leaving it active
            existingTask.setStatus(TaskStatus.CANCELLED);
            existingTask.setUpdatedAt(LocalDateTime.now());
            addActivityLog(existingTask.getId(), "Task cancelled due to reassignment", "System", 
                "Task reassigned to staff ID: " + newStaffId);
            
            // Create new task for the new staff member
            CreateTaskRequest newTaskRequest = new CreateTaskRequest();
            newTaskRequest.setTitle(existingTask.getTitle());
            newTaskRequest.setDescription(existingTask.getDescription());
            newTaskRequest.setPriority(existingTask.getPriority());
            newTaskRequest.setStartDate(existingTask.getStartDate());
            newTaskRequest.setDueDate(existingTask.getDueDate());
            newTaskRequest.setAssignedStaffId(newStaffId);
            newTaskRequest.setCustomerReference(customerReference);
            
            Task newTask = createTask(newTaskRequest);
            addActivityLog(newTask.getId(), "Task reassigned", "System", 
                "Task reassigned from staff ID: " + existingTask.getAssignedStaffId() + " to staff ID: " + newStaffId);
            
            return newTask;
        }
        
        throw new RuntimeException("No active task found for customer reference: " + customerReference);
    }

    public Task updateTaskPriority(Long taskId, Priority priority) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new RuntimeException("Task not found with ID: " + taskId);
        }
        
        Priority oldPriority = task.getPriority();
        task.setPriority(priority);
        task.setUpdatedAt(LocalDateTime.now());
        
        addActivityLog(taskId, "Priority changed", "System", 
            "Priority changed from " + oldPriority + " to " + priority);
        
        return task;
    }

    public Task addComment(Long taskId, AddCommentRequest request) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new RuntimeException("Task not found with ID: " + taskId);
        }
        
        TaskComment comment = new TaskComment();
        comment.setId(commentIdCounter.getAndIncrement());
        comment.setComment(request.getComment());
        comment.setAuthor(request.getAuthor());
        comment.setCreatedAt(LocalDateTime.now());
        
        task.getComments().add(comment);
        task.setUpdatedAt(LocalDateTime.now());
        
        addActivityLog(taskId, "Comment added", request.getAuthor(), 
            "Comment added: " + request.getComment().substring(0, Math.min(50, request.getComment().length())) + 
            (request.getComment().length() > 50 ? "..." : ""));
        
        return task;
    }

    private void addActivityLog(Long taskId, String action, String performedBy, String details) {
        Task task = tasks.get(taskId);
        if (task != null) {
            ActivityLog log = new ActivityLog();
            log.setId(activityIdCounter.getAndIncrement());
            log.setAction(action);
            log.setPerformedBy(performedBy);
            log.setTimestamp(LocalDateTime.now());
            log.setDetails(details);
            
            task.getActivityLogs().add(log);
        }
    }

    public List<Staff> getAllStaff() {
        return new ArrayList<>(staff.values());
    }

    public Staff getStaffById(Long id) {
        return staff.get(id);
    }
}