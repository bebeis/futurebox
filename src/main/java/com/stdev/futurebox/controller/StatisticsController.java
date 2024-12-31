package com.stdev.futurebox.controller;

import com.stdev.futurebox.dto.DailyStatistics;
import com.stdev.futurebox.dto.TypeStatistics;
import com.stdev.futurebox.dto.ItemStatistics;
import com.stdev.futurebox.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    public String statistics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {
            
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        List<DailyStatistics> dailyStats = statisticsService.getDailyStatistics(startDate, endDate);
        List<TypeStatistics> typeStats = statisticsService.getTypeStatistics();
        Long createCount = statisticsService.getCreateCount(startDate, endDate);
        List<ItemStatistics> itemStats = statisticsService.getItemStatistics();

        model.addAttribute("dailyStats", dailyStats);
        model.addAttribute("typeStats", typeStats);
        model.addAttribute("createCount", createCount);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("itemStats", itemStats);

        return "statistics/dashboard";
    }
} 