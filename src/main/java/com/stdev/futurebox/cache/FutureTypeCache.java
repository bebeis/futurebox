package com.stdev.futurebox.cache;

import com.stdev.futurebox.domain.FutureGifticon;
import com.stdev.futurebox.repository.FutureGifticonRepository;
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
    private final Map<Long, FutureGifticon> gifticonCache = new ConcurrentHashMap<>();

    private final FutureGifticonRepository gifticonRepository;

    @PostConstruct
    public void init() {
        try {
            loadAllGifticons();
            log.info("Future type cache initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize future type cache", e);
        }
    }


    private void loadAllGifticons() throws SQLException {
        gifticonRepository.findAll().forEach(gifticon -> 
            gifticonCache.put(gifticon.getId(), gifticon));
    }


    public FutureGifticon findFutureGifticonById(Long id) {
        return gifticonCache.get(id);
    }

    public List<FutureGifticon> findFutureGifticonAll() {
        return gifticonCache.values().stream().toList();
    }


    @Transactional
    public void createFutureGifticon(FutureGifticon gifticon) throws SQLException {
        gifticonRepository.save(gifticon);
        gifticonCache.put(gifticon.getId(), gifticon);
    }


    @Transactional
    public void updateFutureGifticon(FutureGifticon gifticon) throws SQLException {
        gifticonRepository.update(gifticon);
        gifticonCache.put(gifticon.getId(), gifticon);
    }


    @Transactional
    public void deleteFutureGifticonById(Long id) throws SQLException {
        gifticonRepository.delete(id);
        gifticonCache.remove(id);
    }

} 