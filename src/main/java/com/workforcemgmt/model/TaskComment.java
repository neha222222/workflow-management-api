package com.workforcemgmt.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskComment {
    private Long id;
    private String comment;
    private String author;
    private LocalDateTime createdAt;
}