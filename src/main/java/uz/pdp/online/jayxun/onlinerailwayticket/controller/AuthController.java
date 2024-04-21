package uz.pdp.online.jayxun.onlinerailwayticket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.entityDtoWithoutId.ConfirmSentCodeResDto;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.entityDtoWithoutId.UserDto;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.request.auth.LoginReqDto;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.request.auth.SignUpConfirmDto;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.request.auth.SignUpReqDto;
import uz.pdp.online.jayxun.onlinerailwayticket.jwt.JwtProvider;
import uz.pdp.online.jayxun.onlinerailwayticket.jwt.JwtService;
import uz.pdp.online.jayxun.onlinerailwayticket.service.UserService;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping(path = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor

public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final JwtService jwtService;
//    private final S


    @PostMapping(path = "/sign-up", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> signUp(@Valid SignUpReqDto signUpReqDto, HttpServletRequest servletRequest) throws AccountException {

        System.out.println("\nsignUpReqDto = " + signUpReqDto);

        ConfirmSentCodeResDto confirmSentCodeResDto = userService.registerUser(signUpReqDto, servletRequest);

        return ResponseEntity.status(HttpStatus.OK).body(confirmSentCodeResDto);

    }

    @PostMapping(path = "/sign-up-confirm", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> signUp(@Valid SignUpConfirmDto signUpReqDto) throws AccountException {

        System.out.println("\nsignUpReqDto = " + signUpReqDto);

        if (userService.confirmUser(signUpReqDto)) {

            return ResponseEntity.status(HttpStatus.CREATED).body("Created Successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Not Acceptable");

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
