package com.stdev.futurebox.dto;

import com.stdev.futurebox.domain.FutureNote;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FutureNoteCreateForm {
    private Long boxId;
    private String content;

    public FutureNote toEntity() {
        return new FutureNote(boxId, content);
    }
}
