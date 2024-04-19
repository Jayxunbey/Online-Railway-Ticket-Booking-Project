package uz.pdp.online.jayxun.onlinerailwayticket.jwt;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uz.pdp.online.jayxun.onlinerailwayticket.entity.User;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${spring.security.jwt.expiration-time-sec}")
    private long expirationTime;

    @Value("${spring.security.jwt.secret-key}")
    private String SECRET_KEY;

    public String generateToken(User user) {

        JwtBuilder builder = Jwts.builder();

        return builder
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+expirationTime*1000))
                .signWith(key())
                .claim("role", user.getRole()).compact();


    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

}
