package com.stdev.futurebox.dto;

import lombok.Data;

@Data
public class FutureFaceMirrorCreateForm {
    private Long boxId;
    private int year;
    private String imageUrl;

    public FutureFaceMirrorCreateForm(Long boxId, int year, String imageUrl) {
        this.boxId = boxId;
        this.year = year;
        this.imageUrl = imageUrl;
    }
}
