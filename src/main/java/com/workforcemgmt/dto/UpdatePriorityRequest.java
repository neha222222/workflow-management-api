package com.workforcemgmt.dto;

import com.workforcemgmt.model.Priority;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePriorityRequest {
    private Priority priority;
}