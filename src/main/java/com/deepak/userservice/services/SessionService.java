package com.deepak.userservice.services;
import com.deepak.userservice.models.Session;
import com.deepak.userservice.models.SessionStatus;
import com.deepak.userservice.models.User;
import com.deepak.userservice.repositories.SessionRepository;
import com.deepak.userservice.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Service
public class SessionService implements ISessionService {
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    SessionService(UserRepository userRepository,
                   SessionRepository sessionRepository,
                   AuthService authService) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.authService = authService;
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
        User user = userRepository.findUserById(userId);
        if (Objects.isNull(session))
            return SessionStatus.NOT_FOUND;
        if (!authService.isTokenValid(token, user))
            return SessionStatus.ENDED;
        return SessionStatus.ACTIVE;
    }
}
