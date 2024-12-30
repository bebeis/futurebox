package com.stdev.futurebox.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FutureFaceMirror
 * CREATE TABLE future_face_mirror (
 *   id SERIAL PRIMARY KEY,
 *   box_id INTEGER REFERENCES future_box(id),
 *   year INTEGER,
 *   image_url TEXT NOT NULL,
 * );
 */
@Data
@NoArgsConstructor
public class FutureFaceMirror {
    private Long id;
    private Long boxId;
    private int year;
    private String imageUrl;

    public FutureFaceMirror(Long boxId, int year, String imageUrl) {
        this.boxId = boxId;
        this.year = year;
        this.imageUrl = imageUrl;
    }
}
