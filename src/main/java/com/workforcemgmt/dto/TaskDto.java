package com.workforcemgmt.dto;

import com.workforcemgmt.model.TaskStatus;
import com.workforcemgmt.model.Priority;
import com.workforcemgmt.model.TaskComment;
import com.workforcemgmt.model.ActivityLog;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
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
    private List<TaskComment> comments;
    private List<ActivityLog> activityLogs;
}