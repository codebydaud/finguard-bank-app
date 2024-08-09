package com.codebydaud.training.banking_app.service;

import com.codebydaud.training.banking_app.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Date;
import java.util.function.Function;

public interface TokenService extends UserDetailsService {


    String generateToken(UserDetails userDetails);

    String generateToken(UserDetails userDetails, Date expiry);

    String getUsernameFromToken(String token) throws InvalidTokenException;

    Date getExpirationDateFromToken(String token) throws InvalidTokenException;

    <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver)
            throws InvalidTokenException;

    void saveToken(String token) throws InvalidTokenException;

    void validateToken(String token) throws InvalidTokenException;

    void invalidateToken(String token);

}