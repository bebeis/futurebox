package com.stdev.futurebox.dto;

import com.stdev.futurebox.domain.FutureInvention;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FutureInventionCreateForm {
    private String name;
    private String description;
    private String imageUrl;
    private String detailImageUrl;

    public FutureInvention toEntity() {
        return new FutureInvention(name, description, imageUrl, detailImageUrl);
    }
}
