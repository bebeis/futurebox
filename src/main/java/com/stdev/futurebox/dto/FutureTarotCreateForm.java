package com.stdev.futurebox.dto;

import com.stdev.futurebox.domain.FutureTarot;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FutureTarotCreateForm {
    private Long boxId;
    private Integer[] indexes;
    private String description;
    
    public FutureTarot toEntity() {
        return new FutureTarot(
            null,  // id는 DB에서 생성
            boxId,
            indexes,
            description
        );
    }
} 