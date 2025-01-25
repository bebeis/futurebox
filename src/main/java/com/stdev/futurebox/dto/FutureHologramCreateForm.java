package com.stdev.futurebox.dto;

import com.stdev.futurebox.domain.FutureHologram;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FutureHologramCreateForm {
    private Long boxId;
    private String imageUrl;

    public FutureHologram toEntity() {
        return new FutureHologram(
            null,  // id는 DB에서 생성
            boxId,
            imageUrl
        );
    }
}
