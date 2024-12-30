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
    private Boolean open;
    private Integer futureMovieType;
    private Integer futureGifticonType;
    private Integer futureInventionType;

    public FutureBox toEntity() {
        Timestamp timestamp = Timestamp.valueOf(createdTime.replace("T", " ") + ":00");
        
        // 0인 경우 null로 변환
        Integer movieType = futureMovieType == 0 ? null : futureMovieType;
        Integer gifticonType = futureGifticonType == 0 ? null : futureGifticonType;
        Integer inventionType = futureInventionType == 0 ? null : futureInventionType;
        
        return new FutureBox(receiver, sender, open, movieType, gifticonType, inventionType, timestamp);
    }
}
