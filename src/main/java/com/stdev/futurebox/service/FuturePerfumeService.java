package com.stdev.futurebox.service;

import com.stdev.futurebox.domain.FuturePerfume;
import com.stdev.futurebox.repository.FuturePerfumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FuturePerfumeService {
    
    private final FuturePerfumeRepository futurePerfumeRepository;

    @Transactional
    public void save(FuturePerfume perfume) {
        try {
            futurePerfumeRepository.save(perfume);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Creating FuturePerfume failed.");
        }
    }

    @Transactional(readOnly = true)
    public FuturePerfume findByBoxId(Long boxId) {
        try {
            return futurePerfumeRepository.findByBoxId(boxId);
        } catch (Exception e) {
            log.warn("FuturePerfume를 찾을 수 없습니다: boxId={}", boxId, e);
            return null;
        }
    }

    @Transactional
    public void deleteByBoxId(Long boxId) {
        try {
            futurePerfumeRepository.deleteByBoxId(boxId);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Deleting FuturePerfume failed.");
        }
    }

    @Transactional(readOnly = true)
    public FuturePerfume findById(Long id) {
        try {
            return futurePerfumeRepository.findById(id);
        } catch (Exception e) {
            log.warn("FuturePerfume를 찾을 수 없습니다: id={}", id, e);
            return null;
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            futurePerfumeRepository.deleteById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Deleting FuturePerfume failed.");
        }
    }

    @Transactional
    public void update(FuturePerfume perfume) {
        try {
            futurePerfumeRepository.update(perfume);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Updating FuturePerfume failed.");
        }
    }
} 