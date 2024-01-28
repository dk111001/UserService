package com.deepak.userservice.services;

import com.deepak.userservice.dtos.UserResponseDTO;
import com.deepak.userservice.models.User;
import com.deepak.userservice.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.EcPrivateJwk;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class AuthService implements IAuthService {
    private static final String SECRET_KEY="33743677397A24432646294A404D635166546A576E5A7234753778214125442A"; //WnZr4u7x
    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    // BCryptEncoder :- using external library so need to include using @Bean in SpringSecurity
    AuthService(UserRepository userRepository,
            PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    public String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }
    public User signup(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodePassword(password));
        return userRepository.save(user);
    }
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public boolean passwordMatches(String password, String encodedPassword) {
        return bCryptPasswordEncoder.matches(password, encodedPassword);
    }

    public String generateJwtToken(User user) {
        Key key = getSignInKey();

        return Jwts
                .builder()
                .setSubject(user.getUsername())
                .setId(String.valueOf(user.getId()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*5)) //how long valid
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey(){
        byte[] keyBytes= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractUserId(String token) { return Long.valueOf(extractClaim(token, Claims::getId)); }

    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());

    }

    public boolean isTokenValid(String token, User user){
        try {
            final String username = extractUsername(token);
            return (username.equals(user.getUsername())) && !isTokenExpired(token);
        }
        catch (Exception e) {
            return false;
        }


    }
}
