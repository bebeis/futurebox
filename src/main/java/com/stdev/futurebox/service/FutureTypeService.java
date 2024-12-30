package com.stdev.futurebox.service;

import com.stdev.futurebox.cache.FutureTypeCache;
import com.stdev.futurebox.domain.FutureGifticon;
import com.stdev.futurebox.domain.FutureInvention;
import com.stdev.futurebox.domain.FutureMovie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FutureTypeService {
    private final FutureTypeCache futureTypeCache;

    public void createFutureMovie(FutureMovie futureMovie) {
        try {
            futureTypeCache.createFutureMovie(futureMovie);
        } catch (Exception e) {
            log.error("Failed to create future movie", e);
            throw new IllegalArgumentException("Creating FutureMovie failed.");
        }
    }

    public void createFutureGifticon(FutureGifticon futureGifticon) {
        try {
            futureTypeCache.createFutureGifticon(futureGifticon);
        } catch (Exception e) {
            log.error("Failed to create future gifticon", e);
            throw new IllegalArgumentException("Creating FutureGifticon failed.");
        }
    }

    public void createFutureInvention(FutureInvention futureInvention) {
        try {
            futureTypeCache.createFutureInvention(futureInvention);
        } catch (Exception e) {
            log.error("Failed to create future invention", e);
            throw new IllegalArgumentException("Creating FutureInvention failed.");
        }
    }

    public FutureMovie findMovieById(Long id) {
        return futureTypeCache.findFutureMovieById(id);
    }

    public FutureGifticon findGifticonById(Long id) {
        return futureTypeCache.findFutureGifticonById(id);
    }

    public FutureInvention findInventionById(Long id) {
        return futureTypeCache.findFutureInventionById(id);
    }

    public void updateFutureMovie(FutureMovie futureMovie) {
        try {
            futureTypeCache.updateFutureMovie(futureMovie);
        } catch (Exception e) {
            log.error("Failed to update future movie", e);
            throw new IllegalArgumentException("Updating FutureMovie failed.");
        }
    }

    public void updateFutureGifticon(FutureGifticon futureGifticon) {
        try {
            futureTypeCache.updateFutureGifticon(futureGifticon);
        } catch (Exception e) {
            log.error("Failed to update future gifticon", e);
            throw new IllegalArgumentException("Updating FutureGifticon failed.");
        }
    }

    public void updateFutureInvention(FutureInvention futureInvention) {
        try {
            futureTypeCache.updateFutureInvention(futureInvention);
        } catch (Exception e) {
            log.error("Failed to update future invention", e);
            throw new IllegalArgumentException("Updating FutureInvention failed.");
        }
    }

    public void deleteMovieById(Long id) {
        try {
            futureTypeCache.deleteFutureMovieById(id);
        } catch (Exception e) {
            log.error("Failed to delete future movie", e);
            throw new IllegalArgumentException("Deleting FutureMovie failed.");
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

    public void deleteInventionById(Long id) {
        try {
            futureTypeCache.deleteFutureInventionById(id);
        } catch (Exception e) {
            log.error("Failed to delete future invention", e);
            throw new IllegalArgumentException("Deleting FutureInvention failed.");
        }
    }
}
