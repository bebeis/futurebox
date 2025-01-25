package com.stdev.futurebox.domain;

import java.sql.Timestamp;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FutureBox {
    private Long id;
    private UUID uuid;
    private String receiver;
    private String sender;
    private Boolean isOpened;
    private Integer futureGifticonType;
    private Boolean futureValueMeterIncluded;
    private Timestamp createdTime;

    public FutureBox(String receiver, String sender, Boolean isOpened, 
                     Integer futureGifticonType, Boolean futureValueMeterIncluded, 
                     Timestamp createdTime) {
        this.uuid = UUID.randomUUID();
        this.receiver = receiver;
        this.sender = sender;
        this.isOpened = isOpened;
        this.futureGifticonType = futureGifticonType;
        this.futureValueMeterIncluded = futureValueMeterIncluded;
        this.createdTime = createdTime;
    }
}
