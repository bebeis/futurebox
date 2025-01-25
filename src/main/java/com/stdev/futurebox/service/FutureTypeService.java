package com.stdev.futurebox.service;

import com.stdev.futurebox.cache.FutureTypeCache;
import com.stdev.futurebox.domain.FutureGifticon;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FutureTypeService {

    private final FutureTypeCache futureTypeCache;


    public void createFutureGifticon(FutureGifticon futureGifticon) {
        try {
            futureTypeCache.createFutureGifticon(futureGifticon);
        } catch (Exception e) {
            log.error("Failed to create future gifticon", e);
            throw new IllegalArgumentException("Creating FutureGifticon failed.");
        }
    }


    public FutureGifticon findGifticonById(Long id) {
        return futureTypeCache.findFutureGifticonById(id);
    }


    public List<FutureGifticon> findFutureGifticonsAll() {
        return futureTypeCache.findFutureGifticonAll();
    }


    public void updateFutureGifticon(FutureGifticon futureGifticon) {
        try {
            futureTypeCache.updateFutureGifticon(futureGifticon);
        } catch (Exception e) {
            log.error("Failed to update future gifticon", e);
            throw new IllegalArgumentException("Updating FutureGifticon failed.");
        }
    }


    public void deleteGifticonById(Long id) {
        try {
            futureTypeCache.deleteFutureGifticonById(id);
        } catch (Exception e) {
            log.error("Failed to delete future gifticon", e);
            throw new IllegalArgumentException("Deleting FutureGifticon failed.");
        }
    }

}
