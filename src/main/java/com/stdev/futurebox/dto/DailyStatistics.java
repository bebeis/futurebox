package com.stdev.futurebox.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailyStatistics {
    private LocalDate date;
    private Long totalCount;
    private Long openedCount;
    private Long movieCount;
    private Long gifticonCount;
    private Long inventionCount;

    public double getOpenRate() {
        return totalCount == 0 ? 0 : (double) openedCount / totalCount * 100;
    }
}