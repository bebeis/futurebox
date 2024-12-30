package com.stdev.futurebox.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FutureGifticon {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String detailImageUrl;
}
