package com.stdev.futurebox.controller;

import com.stdev.futurebox.service.StatisticsService;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping("/statistics")
    public String dashboard(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                            Model model) {
        try {
            Map<String, Object> stats = statisticsService.getDashboardStats(startDate, endDate);
            
            // 기능 사용률을 리스트로 재구성
            List<Map<String, Object>> features = new ArrayList<>();
            addFeature(features, "Future Note", stats.get("futureNoteUsage"));
            addFeature(features, "Hologram", stats.get("futureHologramUsage"));
            addFeature(features, "Face Mirror", stats.get("futureFaceMirrorUsage"));
            addFeature(features, "Tarot", stats.get("futureTarotUsage"));
            addFeature(features, "Perfume", stats.get("futurePerfumeUsage"));
            
            model.addAttribute("stats", stats);
            model.addAttribute("features", features);
        } catch (SQLException e) {
            log.error("대시보드 쿼리 에러", e);
        }
        return "statistics/dashboard";
    }

    private void addFeature(List<Map<String, Object>> list, String name, Object data) {
        Map<String, Object> feature = new LinkedHashMap<>();
        feature.put("name", name);
        if(data instanceof Map) {
            feature.put("rate", ((Map<?, ?>)data).get("rate"));
        }
        list.add(feature);
    }
} 