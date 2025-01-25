package com.stdev.futurebox.domain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuturePerfume {
    private Long id;
    private Long boxId;
    private String name;
    private String description;
    private String[] keywords;
    private Integer shapeType;
    private Integer color;
    private Integer outlineType;
} 