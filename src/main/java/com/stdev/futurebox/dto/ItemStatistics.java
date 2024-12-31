package com.stdev.futurebox.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemStatistics {
    private String itemName;
    private Long count;
    private Double percentage;
} 