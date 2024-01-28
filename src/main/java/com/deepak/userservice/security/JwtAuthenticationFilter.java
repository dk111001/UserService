package com.deepak.userservice.security;

import com.deepak.userservice.dtos.ErrorResponseDTO;
import com.deepak.userservice.models.User;
import com.deepak.userservice.services.AuthService;
import com.deepak.userservice.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthService authService;
    private final UserService userService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException, ExpiredJwtException, MalformedJwtException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userName;
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        try {
            jwt = authHeader.substring(7);
            userName = authService.extractUsername(jwt);  //extract user Email from JWT token
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = this.userService.getUserByUsername(userName);
                if (authService.isTokenValid(jwt, user)) {
                    UserDetails userDetails = new CustomUserDetails(user);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                }
            }
        }
        catch (Exception ex) {;
            SecurityContextHolder.clearContext();
            ErrorResponseDTO errorResponse = new ErrorResponseDTO();
            errorResponse.setHttpStatus(401);
            errorResponse.setMessage(ex.getMessage());
            response.setContentType("application/json");
            response.setStatus(errorResponse.getHttpStatus());

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(errorResponse);
            // Jackson ObjectMapper
            PrintWriter out = response.getWriter();
            out.print(jsonString);
            out.flush();
            return;
        }

        filterChain.doFilter(request,response);
    }
}
