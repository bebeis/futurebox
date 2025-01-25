package com.stdev.futurebox.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FutureNote {
    private Long id;
    private Long boxId;
    private String message;
    private String encryptedMessage;

    public FutureNote(Long boxId, String message, String encryptedMessage) {
        this.boxId = boxId;
        this.message = message;
        this.encryptedMessage = encryptedMessage;
    }
}
