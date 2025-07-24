package com.fraudsystem.fraud.Service;

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
import java.util.function.Function;

@Service
public class JWTService {
    public String generateToken(UserDetails userDetails) {
        String username = userDetails.getUsername();
        Date currentDate = new Date();
        Date expDate = new Date(System.currentTimeMillis()+1000*60*60*24);
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expDate)
                .signWith(getSignKey(), SignatureAlgorithm.HS512)
                .compact();
        return token;
    }
    private Key getSignKey() {
        String secret = "r3DMfYKNiAIzQLMZVnrxTf7U88dwuvO8OwRzXxfZMHYdw0xLfYmRpeuA9iXpTxrG6OTogdfDLn4+DJRaVLQnJQ==";
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    private <T> T extractClaims(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extactAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }
    private Claims extactAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token).getBody();
    }
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }
    public boolean isTokenExpired(String token) {
        return extractClaims(token,Claims::getExpiration).before(new Date());
    }

    public String generateRefreshToken(HashMap<String, Object> extraClaims, UserDetails user) {
        String username = user.getUsername();
        Date currentDate = new Date();
        Date expDate = new Date(System.currentTimeMillis()+604800000);
        String token = Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expDate)
                .signWith(getSignKey(),SignatureAlgorithm.HS512)
                .compact();
        return token;
    }
}