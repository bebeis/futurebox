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

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // 전체 통계
        long totalBoxCount = statisticsService.getTotalBoxCount();
        long openedBoxCount = statisticsService.getOpenedBoxCount();
        long unopenedBoxCount = totalBoxCount - openedBoxCount;

        model.addAttribute("totalBoxCount", totalBoxCount);
        model.addAttribute("openedBoxCount", openedBoxCount);
        model.addAttribute("unopenedBoxCount", unopenedBoxCount);

        // 기프티콘 타입 통계
        List<TypeStatistics> gifticonStats = statisticsService.getGifticonTypeStatistics();
        model.addAttribute("gifticonStats", gifticonStats);

        // 추가 기능 통계
        model.addAttribute("noteCount", statisticsService.getNoteCount());
        model.addAttribute("tarotCount", statisticsService.getTarotCount());
        model.addAttribute("perfumeCount", statisticsService.getPerfumeCount());
        model.addAttribute("hologramCount", statisticsService.getHologramCount());

        return "statistics/dashboard";
    }

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
        // 수정된 메서드 호출
        List<TypeStatistics> typeStats = statisticsService.getTypeStatistics(startDate, endDate);
        Long createCount = statisticsService.getCreateCount(startDate, endDate);
        // 수정된 메서드 호출
        List<ItemStatistics> itemStats = statisticsService.getItemStatistics(startDate, endDate);

        model.addAttribute("dailyStats", dailyStats);
        model.addAttribute("typeStats", typeStats);
        model.addAttribute("createCount", createCount);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("itemStats", itemStats);

        return "statistics/dashboard";
    }
} 