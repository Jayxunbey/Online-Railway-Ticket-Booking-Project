package uz.pdp.online.jayxun.onlinerailwayticket.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.DispatcherServlet;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.entityDtoWithoutId.ConfirmSentCodeDto;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.entityDtoWithoutId.UserDto;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.request.auth.LoginReqDto;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.request.auth.SignUpConfirmDto;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.request.auth.SignUpReqDto;
import uz.pdp.online.jayxun.onlinerailwayticket.jwt.JwtProvider;
import uz.pdp.online.jayxun.onlinerailwayticket.jwt.JwtService;
import uz.pdp.online.jayxun.onlinerailwayticket.service.UserService;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.Enumeration;

@RestController
@RequestMapping(path = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor

public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final JwtService jwtService;
//    private final S


    @PostMapping(path = "/sign-up", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> signUp(SignUpReqDto signUpReqDto, HttpServletRequest servletRequest) throws AccountException {

        System.out.println("\nsignUpReqDto = " + signUpReqDto);

        ConfirmSentCodeDto confirmSentCodeDto = userService.registerUser(signUpReqDto, servletRequest);

        return ResponseEntity.status(HttpStatus.OK).body(confirmSentCodeDto);

    }

    @PostMapping(path = "/sign-up-confirm", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> signUp(SignUpConfirmDto signUpReqDto) throws AccountException {

        System.out.println("\nsignUpReqDto = " + signUpReqDto);

        ConfirmSentCodeDto confirmSentCodeDto = userService.confirmUser(signUpReqDto);

        return ResponseEntity.status(HttpStatus.OK).body(confirmSentCodeDto);

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
