package com.stdev.futurebox.dto;

import com.stdev.futurebox.domain.FutureNote;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.Base64;

@Data
@RequiredArgsConstructor
public class FutureNoteCreateForm {
    private Long boxId;
    private String content;
    private String encryptedContent;

    public FutureNote toEntity() {
        return new FutureNote(boxId, content, encryptedContent);
    }
}
