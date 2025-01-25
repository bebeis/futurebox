package com.stdev.futurebox.dto;

import com.stdev.futurebox.domain.FutureBox;
import java.sql.Timestamp;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FutureBoxCreateForm {
    private String receiver;
    private String sender;
    private String createdTime;
    private Boolean isOpened;
    private Integer futureGifticonType;
    private Boolean futureValueMeterIncluded;

    public FutureBox toEntity() {
        Timestamp timestamp = Timestamp.valueOf(createdTime.replace("T", " ") + ":00");
        return new FutureBox(receiver, sender, isOpened, 
                            futureGifticonType, futureValueMeterIncluded, timestamp);
    }
}
