package uz.pdp.online.jayxun.onlinerailwayticket.jwt;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.pdp.online.jayxun.onlinerailwayticket.entity.User;
import uz.pdp.online.jayxun.onlinerailwayticket.service.UserService;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtService {
    private final UserService userService;

    public boolean isExpired(Claims parse) {
        return new Date().after(parse.getExpiration());
    }

    public String addBearerToToken(String token) {
        return "Bearer " + token;
    }

    public User getUserIfAvailable(Claims parse) throws AccountNotFoundException {
        String email = parse.getSubject();

        User userByEmail = userService.getUserByEmail(email);

        return userByEmail;


    }
}
