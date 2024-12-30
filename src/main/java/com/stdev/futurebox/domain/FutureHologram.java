package com.stdev.futurebox.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FutureHologram CREATE TABLE future_hologram ( id SERIAL PRIMARY KEY, box_id INTEGER REFERENCES future_box(id),
 * message TEXT, image_url TEXT NOT NULL, );
 */
@Data
@NoArgsConstructor
public class FutureHologram {
    private Long id;
    private Long boxId;
    private String message;
    private String imageUrl;

    public FutureHologram(Long boxId, String message, String imageUrl) {
        this.boxId = boxId;
        this.message = message;
        this.imageUrl = imageUrl;
    }
}
