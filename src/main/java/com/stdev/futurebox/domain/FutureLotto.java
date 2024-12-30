package com.stdev.futurebox.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FutureLotto CREATE TABLE future_lotto ( id SERIAL PRIMARY KEY, box_id INTEGER REFERENCES future_box(id), numbers
 * INTEGER[] NOT NULL, );
 */
@Data
@NoArgsConstructor
public class FutureLotto {
    private Long id;
    private Long boxId;
    private int[] numbers;

    public FutureLotto(Long boxId, int[] numbers) {
        this.boxId = boxId;
        this.numbers = numbers;
    }
}
