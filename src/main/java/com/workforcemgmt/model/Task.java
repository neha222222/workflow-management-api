package com.workforcemgmt.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private Long assignedStaffId;
    private String customerReference;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TaskComment> comments = new ArrayList<>();
    private List<ActivityLog> activityLogs = new ArrayList<>();
}