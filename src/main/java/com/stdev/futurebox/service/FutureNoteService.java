package com.stdev.futurebox.service;

import com.stdev.futurebox.domain.FutureNote;
import com.stdev.futurebox.repository.FutureNoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FutureNoteService {

    private final FutureNoteRepository futureNoteRepository;

    public Long create(FutureNote futureNote) {
        try {
            futureNoteRepository.save(futureNote);
            return futureNote.getId();
        } catch (Exception e) {
            throw new IllegalArgumentException("Creating FutureNote failed.");
        }
    }

    @Transactional(readOnly = true)
    public FutureNote findById(Long id) {
        try {
            return futureNoteRepository.findById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("FutureNote not found.");
        }
    }

    @Transactional(readOnly = true)
    public FutureNote findByBoxId(Long boxId) {
        try {
            return futureNoteRepository.findByBoxId(boxId);
        } catch (Exception e) {
            return null;
        }
    }

    public void update(FutureNote futureNote) {
        try {
            futureNoteRepository.update(futureNote);
        } catch (Exception e) {
            throw new IllegalArgumentException("Updating FutureNote failed.");
        }
    }

    public void delete(Long id) {
        try {
            futureNoteRepository.deleteById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Deleting FutureNote failed.");
        }
    }

    public void deleteByBoxId(Long boxId) {
        try {
            futureNoteRepository.deleteByBoxId(boxId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Deleting FutureNote failed.");
        }
    }
}
