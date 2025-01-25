package com.stdev.futurebox.service;

import com.stdev.futurebox.domain.FutureTarot;
import com.stdev.futurebox.repository.FutureTarotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FutureTarotService {
    
    private final FutureTarotRepository futureTarotRepository;

    @Transactional
    public void save(FutureTarot tarot) {
        try {
            futureTarotRepository.save(tarot);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Creating FutureTarot failed.");
        }
    }

    @Transactional(readOnly = true)
    public FutureTarot findByBoxId(Long boxId) {
        try {
            return futureTarotRepository.findByBoxId(boxId);
        } catch (Exception e) {
            log.warn("FutureTarot를 찾을 수 없습니다: boxId={}", boxId, e);
            return null;
        }
    }

    @Transactional
    public void deleteByBoxId(Long boxId) {
        try {
            futureTarotRepository.deleteByBoxId(boxId);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Deleting FutureTarot failed.");
        }
    }

    @Transactional(readOnly = true)
    public FutureTarot findById(Long id) {
        try {
            return futureTarotRepository.findById(id);
        } catch (Exception e) {
            log.warn("FutureTarot를 찾을 수 없습니다: id={}", id, e);
            return null;
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            futureTarotRepository.deleteById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Deleting FutureTarot failed.");
        }
    }
} 