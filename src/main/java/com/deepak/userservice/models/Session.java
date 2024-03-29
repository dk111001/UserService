package com.deepak.userservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Entity
@Getter
@Setter
public class Session extends BaseModel{
    private String token;
    private Date expiringAt;
    @ManyToOne
    private User user;
    @Enumerated(EnumType.ORDINAL)
    private SessionStatus sessionStatus;

    public void setExpiringAt(int seconds) {
        long expiryTime = new Date().getTime() + (seconds * 1000L);
        expiringAt = new Date(expiryTime);
    }
}
