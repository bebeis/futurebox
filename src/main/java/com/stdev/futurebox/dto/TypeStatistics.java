package com.stdev.futurebox.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TypeStatistics {
    private String typeName;
    private Long count;
} 