package com.deepak.userservice.services;

import com.deepak.userservice.models.Session;
import com.deepak.userservice.models.SessionStatus;

import java.util.Date;

public interface ISessionService {
    Session createSession(Session session);
    SessionStatus validate(String token, Long userId);

    boolean isExpired(Date expiredAt);
}
