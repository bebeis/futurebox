package com.stdev.futurebox.service;

import com.stdev.futurebox.dto.DailyStatistics;
import com.stdev.futurebox.dto.TypeStatistics;
import com.stdev.futurebox.dto.ItemStatistics;
import com.stdev.futurebox.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    @Transactional(readOnly = true)
    public List<DailyStatistics> getDailyStatistics(LocalDate startDate, LocalDate endDate) {
        try {
            return statisticsRepository.getDailyStatistics(startDate, endDate);
        } catch (SQLException e) {
            log.error("통계 데이터 조회 실패", e);
            throw new IllegalStateException("통계 데이터를 조회할 수 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<TypeStatistics> getTypeStatistics(LocalDate startDate, LocalDate endDate) {
        try {
            return statisticsRepository.getTypeStatistics(startDate, endDate);
        } catch (SQLException e) {
            log.error("타입별 통계 조회 실패", e);
            throw new IllegalStateException("타입별 통계를 조회할 수 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    public Long getCreateCount(LocalDate startDate, LocalDate endDate) {
        try {
            return statisticsRepository.getCreateCount(startDate, endDate);
        } catch (SQLException e) {
            log.error("생성 수 통계 조회 실패", e);
            throw new IllegalStateException("생성 수 통계를 조회할 수 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<ItemStatistics> getItemStatistics(LocalDate startDate, LocalDate endDate) {
        try {
            return statisticsRepository.getItemStatistics(startDate, endDate);
        } catch (SQLException e) {
            log.error("아이템 통계 조회 실패", e);
            throw new IllegalStateException("아이템 통계를 조회할 수 없습니다.");
        }
    }
} 