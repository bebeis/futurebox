package com.stdev.futurebox.cache;

import com.stdev.futurebox.domain.FutureGifticon;
import com.stdev.futurebox.domain.FutureInvention;
import com.stdev.futurebox.domain.FutureMovie;
import com.stdev.futurebox.repository.FutureGifticonRepository;
import com.stdev.futurebox.repository.FutureInventionRepository;
import com.stdev.futurebox.repository.FutureMovieRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Collection;
import java.sql.SQLException;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FutureTypeCache {
    private final Map<Long, FutureMovie> movieCache = new ConcurrentHashMap<>();
    private final Map<Long, FutureGifticon> gifticonCache = new ConcurrentHashMap<>();
    private final Map<Long, FutureInvention> inventionCache = new ConcurrentHashMap<>();
    
    private final FutureMovieRepository movieRepository;
    private final FutureGifticonRepository gifticonRepository;
    private final FutureInventionRepository inventionRepository;

    @PostConstruct
    public void init() {
        try {
            loadAllMovies();
            loadAllGifticons();
            loadAllInventions();
            log.info("Future type cache initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize future type cache", e);
        }
    }

    private void loadAllMovies() throws SQLException {
        movieRepository.findAll().forEach(movie -> 
            movieCache.put(movie.getId(), movie));
    }

    private void loadAllGifticons() throws SQLException {
        gifticonRepository.findAll().forEach(gifticon -> 
            gifticonCache.put(gifticon.getId(), gifticon));
    }

    private void loadAllInventions() throws SQLException {
        inventionRepository.findAll().forEach(invention -> 
            inventionCache.put(invention.getId(), invention));
    }

    public FutureMovie findFutureMovieById(Long id) {
        return movieCache.get(id);
    }

    public FutureGifticon findFutureGifticonById(Long id) {
        return gifticonCache.get(id);
    }

    public FutureInvention findFutureInventionById(Long id) {
        return inventionCache.get(id);
    }

    public List<FutureMovie> findFutureMovieAll() {
        return movieCache.values().stream().toList();
    }

    public List<FutureGifticon> findFutureGifticonAll() {
        return gifticonCache.values().stream().toList();
    }

    public List<FutureInvention> findFutureInventionAll() {
        return inventionCache.values().stream().toList();
    }

    @Transactional
    public void createFutureMovie(FutureMovie movie) throws SQLException {
        movieRepository.save(movie);
        movieCache.put(movie.getId(), movie);
    }

    @Transactional
    public void createFutureGifticon(FutureGifticon gifticon) throws SQLException {
        gifticonRepository.save(gifticon);
        gifticonCache.put(gifticon.getId(), gifticon);
    }

    @Transactional
    public void createFutureInvention(FutureInvention invention) throws SQLException {
        inventionRepository.save(invention);
        inventionCache.put(invention.getId(), invention);
    }

    // 데이터가 변경될 경우를 위한 메서드들
    @Transactional
    public void updateFutureMovie(FutureMovie movie) throws SQLException {
        movieRepository.update(movie);
        movieCache.put(movie.getId(), movie);
    }

    @Transactional
    public void updateFutureGifticon(FutureGifticon gifticon) throws SQLException {
        gifticonRepository.update(gifticon);
        gifticonCache.put(gifticon.getId(), gifticon);
    }

    @Transactional
    public void updateFutureInvention(FutureInvention invention) throws SQLException {
        inventionRepository.update(invention);
        inventionCache.put(invention.getId(), invention);
    }

    @Transactional
    public void deleteFutureMovieById(Long id) throws SQLException {
        movieRepository.delete(id);
        movieCache.remove(id);
    }

    @Transactional
    public void deleteFutureGifticonById(Long id) throws SQLException {
        gifticonRepository.delete(id);
        gifticonCache.remove(id);
    }

    @Transactional
    public void deleteFutureInventionById(Long id) throws SQLException {
        inventionRepository.delete(id);
        inventionCache.remove(id);
    }

} 