package com.stdev.futurebox.service;

import com.stdev.futurebox.dto.DailyStatistics;
import com.stdev.futurebox.dto.TypeStatistics;
import com.stdev.futurebox.dto.ItemStatistics;
import com.stdev.futurebox.repository.StatisticsRepository;
import com.stdev.futurebox.repository.FutureBoxRepository;
import com.stdev.futurebox.repository.FutureNoteRepository;
import com.stdev.futurebox.repository.FutureTarotRepository;
import com.stdev.futurebox.repository.FuturePerfumeRepository;
import com.stdev.futurebox.repository.FutureHologramRepository;
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
    private final FutureBoxRepository futureBoxRepository;
    private final FutureNoteRepository futureNoteRepository;
    private final FutureTarotRepository futureTarotRepository;
    private final FuturePerfumeRepository futurePerfumeRepository;
    private final FutureHologramRepository futureHologramRepository;

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

    @Transactional(readOnly = true)
    public long getTotalBoxCount() {
        try {
            return futureBoxRepository.count();
        } catch (SQLException e) {
            log.error("전체 FutureBox 개수 조회 실패", e);
            return 0L;
        }
    }

    @Transactional(readOnly = true)
    public long getOpenedBoxCount() {
        try {
            return futureBoxRepository.countByOpenTrue();
        } catch (SQLException e) {
            log.error("개봉된 FutureBox 개수 조회 실패", e);
            return 0L;
        }
    }

    @Transactional(readOnly = true)
    public List<TypeStatistics> getGifticonTypeStatistics() {
        try {
            return statisticsRepository.getGifticonTypeStatistics();
        } catch (SQLException e) {
            log.error("기프티콘 타입별 통계 조회 실패", e);
            throw new IllegalStateException("기프티콘 타입별 통계를 조회할 수 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    public long getNoteCount() {
        try {
            return futureNoteRepository.count();
        } catch (SQLException e) {
            log.error("Note 개수 조회 실패", e);
            return 0L;
        }
    }

    @Transactional(readOnly = true)
    public long getTarotCount() {
        try {
            return futureTarotRepository.count();
        } catch (SQLException e) {
            log.error("Tarot 개수 조회 실패", e);
            return 0L;
        }
    }

    @Transactional(readOnly = true)
    public long getPerfumeCount() {
        try {
            return futurePerfumeRepository.count();
        } catch (SQLException e) {
            log.error("Perfume 개수 조회 실패", e);
            return 0L;
        }
    }

    @Transactional(readOnly = true)
    public long getHologramCount() {
        try {
            return futureHologramRepository.count();
        } catch (SQLException e) {
            log.error("Hologram 개수 조회 실패", e);
            return 0L;
        }
    }
} 