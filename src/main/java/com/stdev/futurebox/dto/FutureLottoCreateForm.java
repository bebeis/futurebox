package com.stdev.futurebox.dto;

import com.stdev.futurebox.domain.FutureLotto;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FutureLottoCreateForm {
    private Long boxId;
    private int[] numbers;

    public FutureLotto toEntity() {
        return new FutureLotto(boxId, numbers);
    }
}
