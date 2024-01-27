package com.deepak.userservice.services;
import com.deepak.userservice.models.Session;
import com.deepak.userservice.repositories.SessionRepository;
import org.springframework.stereotype.Service;

@Service
public class SessionService implements ISessionService {
    private final SessionRepository sessionRepository;
    SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }
    public Session createSession(Session session) {
        return sessionRepository.save(session);
    }
}
