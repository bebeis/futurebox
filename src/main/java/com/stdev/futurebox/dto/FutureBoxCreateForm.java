package com.stdev.futurebox.dto;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class FutureBoxCreateForm {
    private String receiver;
    private String sender;
    private Timestamp createdTime;

    public FutureBoxCreateForm(String receiver, String sender, Timestamp createdTime) {
        this.receiver = receiver;
        this.sender = sender;
        this.createdTime = createdTime;
    }
}
