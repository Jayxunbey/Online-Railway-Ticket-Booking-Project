package uz.pdp.online.jayxun.onlinerailwayticket.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.entityDtoWithoutId.UserDto;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.request.auth.LoginReqDto;
import uz.pdp.online.jayxun.onlinerailwayticket.entity.User;
import uz.pdp.online.jayxun.onlinerailwayticket.mapper.UserMapper;
import uz.pdp.online.jayxun.onlinerailwayticket.service.UserService;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${spring.security.jwt.expiration-time-minute}")
    private long expirationTimeWithoutRemember;
    @Value("${spring.security.jwt.expiration-time-minute-with-remember}")
    private long expirationTimeWithRemember;

    private final UserMapper userMapper;

    @Value("${spring.security.jwt.secret-key}")
    private String SECRET_KEY;

    public String generateToken(UserDto userDto, boolean isRemember) {

        long expirationTime;

        if (isRemember){
            expirationTime = expirationTimeWithRemember;
        }else {
            expirationTime = expirationTimeWithoutRemember;
        }

        return Jwts.builder()
                .subject(userDto.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+expirationTime*60*1000))
                .signWith(key())
                .claim("role", userDto.getRole()).compact();
    }

    public String generateTokenForOtherWork(String username, int expriationMinut) {

        JwtBuilder builder = Jwts.builder();

        return builder
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+expriationMinut*60*1000))
                .signWith(key())
                .compact();


    }


    private SecretKey key() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public Claims parse(String token) {

        JwtParser build = Jwts.parser()
                .verifyWith(key()).build();

        return build.parseSignedClaims(token).getPayload();
    }


}
