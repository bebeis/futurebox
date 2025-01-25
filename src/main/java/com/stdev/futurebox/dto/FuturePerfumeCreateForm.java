package com.stdev.futurebox.dto;

import com.stdev.futurebox.domain.FuturePerfume;
import lombok.Data;

@Data
public class FuturePerfumeCreateForm {
    private Long boxId;
    private String name;
    private String description;
    private String[] keywords;
    private Integer shapeType;
    private Integer color;
    private Integer outlineType;
    
    public FuturePerfume toEntity() {
        return new FuturePerfume(
            null,  // id는 DB에서 생성
            boxId,
            name,
            description,
            keywords,
            shapeType,
            color,
            outlineType
        );
    }
} 