package com.stdev.futurebox.dto;

import com.stdev.futurebox.domain.FutureMovie;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FutureMovieCreateForm {
    private String name;
    private String description;
    private String imageUrl;
    private String detailImageUrl;

    public FutureMovie toEntity() {
        return new FutureMovie(name, description, imageUrl, detailImageUrl);
    }
}
