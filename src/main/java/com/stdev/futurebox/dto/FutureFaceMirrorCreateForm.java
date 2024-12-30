package com.stdev.futurebox.dto;

import com.stdev.futurebox.domain.FutureFaceMirror;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FutureFaceMirrorCreateForm {
    private Long boxId;
    private int year;
    private String imageUrl;

    public FutureFaceMirror toEntity() {
        return new FutureFaceMirror(boxId, year, imageUrl);
    }
}
