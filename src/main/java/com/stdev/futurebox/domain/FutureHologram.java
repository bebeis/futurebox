package com.stdev.futurebox.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FutureHologram {
    private Long id;
    private Long boxId;
    private String imageUrl;
}
