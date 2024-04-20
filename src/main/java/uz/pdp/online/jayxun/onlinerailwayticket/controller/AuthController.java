package uz.pdp.online.jayxun.onlinerailwayticket.controller;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.entityDtoWithoutId.UserDto;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.request.auth.LoginReqDto;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.request.auth.SignUpReqDto;
import uz.pdp.online.jayxun.onlinerailwayticket.jwt.JwtProvider;
import uz.pdp.online.jayxun.onlinerailwayticket.jwt.JwtService;
import uz.pdp.online.jayxun.onlinerailwayticket.service.UserService;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final JwtService jwtService;

    @PostMapping("/sign-up")
    public ResponseEntity<Object> signUp(@RequestBody @Valid SignUpReqDto signUpReqDto) throws AccountException {

        System.out.println("\nsignUpReqDto = " + signUpReqDto);

        userService.registerUser(signUpReqDto);

        return ResponseEntity.status(HttpStatus.CREATED).body("Created Successfully");

    }

    @PostMapping("/log-in")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginReqDto loginReqDto) throws AccountNotFoundException {

        System.out.println("\nloginReqDto = " + loginReqDto);

        UserDto userDto = userService.loginUserAndGetDto(loginReqDto);

        String token = jwtProvider.generateToken(userDto, loginReqDto.isRemember_me());

        String bearerToken = jwtService.addBearerToToken(token);

        return ResponseEntity.accepted().header(HttpHeaders.AUTHORIZATION, bearerToken).body(userDto);


    }

}
