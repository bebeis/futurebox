package com.stdev.futurebox.service;

import com.stdev.futurebox.domain.FutureBox;
import com.stdev.futurebox.dto.FutureBoxCreateForm;
import com.stdev.futurebox.repository.FutureBoxRepository;
import com.stdev.futurebox.repository.FutureFaceMirrorRepository;
import com.stdev.futurebox.repository.FutureHologramRepository;
import com.stdev.futurebox.repository.FutureLottoRepository;
import com.stdev.futurebox.repository.FutureNoteRepository;
import com.stdev.futurebox.util.UuidGenerator;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FutureBoxService {

    private final FutureBoxRepository futureBoxRepository;

    private final FutureFaceMirrorRepository futureFaceMirrorRepository;

    private final FutureHologramRepository futureHologramRepository;

    private final FutureLottoRepository futureLottoRepository;

    private final FutureNoteRepository futureNoteRepository;

    @Transactional
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

    public List<FutureBox> findByReceiver(String receiver) {
        try {
            return futureBoxRepository.findByReceiver(receiver);
        } catch (SQLException e) {
            throw new IllegalArgumentException("FutureBox not found.");
        }
    }

    public List<FutureBox> findBySender(String sender) {
        try {
            return futureBoxRepository.findBySender(sender);
        } catch (SQLException e) {
            throw new IllegalArgumentException("FutureBox not found.");
        }
    }

    public List<FutureBox> findAll() {
        try {
            return futureBoxRepository.findAll();
        } catch (SQLException e) {
            throw new IllegalArgumentException("FutureBox not found.");
        }
    }

    @Transactional
    public void deleteById(Long id) {
        try {
            try {
                futureFaceMirrorRepository.deleteByBoxId(id);
            } catch (Exception e) {
                log.warn("FutureFaceMirror 삭제 실패", e);
            }

            try {
                futureHologramRepository.deleteByBoxId(id);
            } catch (Exception e) {
                log.warn("FutureHologram 삭제 실패", e);
            }

            try {
                futureLottoRepository.deleteByBoxId(id);
            } catch (Exception e) {
                log.warn("FutureLotto 삭제 실패", e);
            }

            try {
                futureNoteRepository.deleteByBoxId(id);
            } catch (Exception e) {
                log.warn("FutureNote 삭제 실패", e);
            }

            futureBoxRepository.deleteById(id);
        } catch (SQLException e) {
            throw new IllegalStateException("FutureBox 삭제 실패", e);
        }
    }

    @Transactional
    public void update(FutureBox futureBox) {
        try {
            futureBoxRepository.update(futureBox);
        } catch (SQLException e) {
            throw new IllegalArgumentException("FutureBox not found.");
        }
    }

}
