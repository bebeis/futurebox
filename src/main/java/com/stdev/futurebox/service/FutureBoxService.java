package com.stdev.futurebox.service;

import com.stdev.futurebox.domain.FutureBox;
import com.stdev.futurebox.dto.FutureBoxCreateForm;
import com.stdev.futurebox.repository.FutureBoxRepository;
import com.stdev.futurebox.util.UuidGenerator;
import java.sql.SQLException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FutureBoxService {

    private final FutureBoxRepository futureBoxRepository;
    public Long create(FutureBox futureBox) {
        futureBox.setOpen(false);
        futureBox.setUuid(UuidGenerator.generateUuid());
        try {
            futureBoxRepository.save(futureBox);
            return futureBox.getId();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Creating FutureBox failed.");
        }
    }

    public FutureBox findById(Long id) {
        try {
            return futureBoxRepository.findById(id);
        } catch (SQLException e) {
            throw new IllegalArgumentException("FutureBox not found.");
        }
    }

    public FutureBox findByUuid(UUID uuid) {
        try {
            return futureBoxRepository.findByUuid(uuid);
        } catch (SQLException e) {
            throw new IllegalArgumentException("FutureBox not found.");
        }
    }

    public FutureBox findByReceiver(String receiver) {
        try {
            return futureBoxRepository.findByReceiver(receiver);
        } catch (SQLException e) {
            throw new IllegalArgumentException("FutureBox not found.");
        }
    }

    public FutureBox findBySender(String sender) {
        try {
            return futureBoxRepository.findBySender(sender);
        } catch (SQLException e) {
            throw new IllegalArgumentException("FutureBox not found.");
        }
    }

    public void delete(Long id) {
        try {
            futureBoxRepository.deleteById(id);
        } catch (SQLException e) {
            throw new IllegalArgumentException("FutureBox not found.");
        }
    }

    public void update(FutureBox futureBox) {
        try {
            futureBoxRepository.update(futureBox);
        } catch (SQLException e) {
            throw new IllegalArgumentException("FutureBox not found.");
        }
    }

}
