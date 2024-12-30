package com.stdev.futurebox.service;

import com.stdev.futurebox.domain.FutureFaceMirror;
import com.stdev.futurebox.repository.FutureFaceMirrorRepository;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FutureFaceMirrorService {

    private final FutureFaceMirrorRepository futureFaceMirrorRepository;

    public Long create(FutureFaceMirror futureFaceMirror) {
        try {
            futureFaceMirrorRepository.save(futureFaceMirror);
            return futureFaceMirror.getId();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Creating FutureFaceMirror failed.");
        }
    }

    @Transactional(readOnly = true)
    public FutureFaceMirror findById(Long id) {
        try {
            return futureFaceMirrorRepository.findById(id);
        } catch (SQLException e) {
            throw new IllegalArgumentException("FutureFaceMirror not found.");
        }
    }

    @Transactional(readOnly = true)
    public FutureFaceMirror findByBoxId(Long boxId) {
        try {
            return futureFaceMirrorRepository.findByBoxId(boxId);
        } catch (Exception e) {
            return null;
        }
    }

    public void update(FutureFaceMirror futureFaceMirror) {
        try {
            futureFaceMirrorRepository.update(futureFaceMirror);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Updating FutureFaceMirror failed.");
        }
    }

    public void delete(Long id) {
        try {
            futureFaceMirrorRepository.deleteById(id);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Deleting FutureFaceMirror failed.");
        }
    }

    public void deleteByBoxId(Long boxId) {
        try {
            futureFaceMirrorRepository.deleteByBoxId(boxId);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Deleting FutureFaceMirror failed.");
        }
    }
}
