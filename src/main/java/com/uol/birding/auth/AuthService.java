//referred https://www.youtube.com/watch?v=lA18U8dGKF8&t=1033s
package com.uol.birding.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class AuthService {

    private static final String KEY = "10adcb64450f39b9cf40bbb0cbbefca6a93742d4c10c38f4313f536431a57d98";
    public <T> T extractClaim(String token, Function<Claims, T> resolveClaims) {
        final Claims claims = extractClaims(token);
        return resolveClaims.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        Date current = new Date(System.currentTimeMillis());
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(current)
                .setExpiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24 * 7))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String extractEmailFromToken(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSigningKey() {
        byte[] key = Decoders.BASE64.decode(KEY);
        return Keys.hmacShaKeyFor(key);
    }
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmailFromToken(token);
        return (email.equals(userDetails.getUsername())
                && !(extractClaim(token, Claims::getExpiration).before(new Date())));
    }
}
