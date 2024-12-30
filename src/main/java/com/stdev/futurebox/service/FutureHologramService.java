package com.stdev.futurebox.service;

import com.stdev.futurebox.domain.FutureHologram;
import com.stdev.futurebox.repository.FutureHologramRepository;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FutureHologramService {

    private final FutureHologramRepository futureHologramRepository;

    public Long create(FutureHologram futureHologram) {
        try {
            futureHologramRepository.save(futureHologram);
            return futureHologram.getId();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Creating FutureHologram failed.");
        }
    }

    @Transactional(readOnly = true)
    public FutureHologram findById(Long id) {
        try {
            return futureHologramRepository.findById(id);
        } catch (SQLException e) {
            throw new IllegalArgumentException("FutureHologram not found.");
        }
    }

    @Transactional(readOnly = true)
    public FutureHologram findByBoxId(Long boxId) {
        try {
            return futureHologramRepository.findByBoxId(boxId);
        } catch (SQLException e) {
            throw new IllegalArgumentException("FutureHologram not found.");
        }
    }

    public void update(FutureHologram futureHologram) {
        try {
            futureHologramRepository.update(futureHologram);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Updating FutureHologram failed.");
        }
    }

    public void delete(Long id) {
        try {
            futureHologramRepository.deleteById(id);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Deleting FutureHologram failed.");
        }
    }

    public void deleteByBoxId(Long boxId) {
        try {
            futureHologramRepository.deleteByBoxId(boxId);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Deleting FutureHologram failed.");
        }
    }
}
