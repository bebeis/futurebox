package com.stdev.futurebox.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FutureNote {
    private Long id;
    private Long boxId;
    private String message;

    public FutureNote(Long boxId, String message) {
        this.boxId = boxId;
        this.message = message;
    }
}
