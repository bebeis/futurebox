package com.stdev.futurebox.domain;

import java.sql.Timestamp;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FutureBox {
    private Long id;
    private UUID uuid;
    private String receiver;
    private String sender;
    private Boolean open;
    private int futureMovieType;
    private int futureGifticonType;
    private int futureInventionType;
    private Timestamp createdTime;

    public FutureBox(UUID uuid, String receiver, String sender, Boolean open, int futureMovieType,
                     int futureGifticonType, int futureInventionType, Timestamp createdTime) {
        this.uuid = uuid;
        this.receiver = receiver;
        this.sender = sender;
        this.open = open;
        this.futureMovieType = futureMovieType;
        this.futureGifticonType = futureGifticonType;
        this.futureInventionType = futureInventionType;
        this.createdTime = createdTime;
    }
}
