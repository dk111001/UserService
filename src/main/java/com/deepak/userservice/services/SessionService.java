package com.deepak.userservice.services;
import com.deepak.userservice.models.Session;
import com.deepak.userservice.models.SessionStatus;
import com.deepak.userservice.repositories.SessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Service
public class SessionService implements ISessionService {
    private final SessionRepository sessionRepository;
    SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }
    public Session createSession(Session session) {
        return sessionRepository.save(session);
    }

    public boolean isExpired(Date expiryDate) {
        Date currentDateTime = new Date();
        return expiryDate.before(currentDateTime);
    }

    public SessionStatus validate(String token, Long userId) {
        Session session = sessionRepository.findByTokenAndUser_Id(token, userId);
        if (Objects.isNull(session))
            return SessionStatus.NOT_FOUND;

        if (isExpired(session.getExpiringAt()))
            return SessionStatus.ENDED;
        return SessionStatus.ACTIVE;
    }
}
