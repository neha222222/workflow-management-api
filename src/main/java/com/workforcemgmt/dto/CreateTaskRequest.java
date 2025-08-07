package com.workforcemgmt.dto;

import com.workforcemgmt.model.Priority;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskRequest {
    private String title;
    private String description;
    private Priority priority;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private Long assignedStaffId;
    private String customerReference;
}