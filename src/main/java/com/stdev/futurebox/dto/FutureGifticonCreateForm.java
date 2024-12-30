package com.stdev.futurebox.dto;

import com.stdev.futurebox.domain.FutureGifticon;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FutureGifticonCreateForm {
    private String name;
    private String description;
    private String imageUrl;
    private String detailImageUrl;

    public FutureGifticon toEntity() {
        return new FutureGifticon(name, description, imageUrl, detailImageUrl);
    }
}
