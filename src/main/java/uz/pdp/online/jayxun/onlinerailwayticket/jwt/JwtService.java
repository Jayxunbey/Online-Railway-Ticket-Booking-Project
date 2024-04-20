package uz.pdp.online.jayxun.onlinerailwayticket.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class JwtService {

    public boolean isExpire(Claims parse) {
        return parse.getIssuedAt().before(parse.getExpiration());
    }

    public String addBearerToToken(String token) {
        return "Bearer " + token;
    }
}
