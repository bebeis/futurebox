package com.stdev.futurebox.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FutureMovie {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String detailImageUrl;

    public FutureMovie(String name, String description, String imageUrl, String detailImageUrl) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.detailImageUrl = detailImageUrl;
    }
}
