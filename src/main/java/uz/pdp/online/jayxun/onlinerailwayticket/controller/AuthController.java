package uz.pdp.online.jayxun.onlinerailwayticket.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.request.SignUpReqDto;
import uz.pdp.online.jayxun.onlinerailwayticket.entity.User;
import uz.pdp.online.jayxun.onlinerailwayticket.jwt.JwtProvider;
import uz.pdp.online.jayxun.onlinerailwayticket.mapper.UserMapper;
import uz.pdp.online.jayxun.onlinerailwayticket.repo.UserRepository;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody @Valid SignUpReqDto signUpReqDto) {


        System.out.println("signUpReqDto = " + signUpReqDto);

        User entity = userMapper.toEntity(signUpReqDto);
        System.out.println("entity = " + entity);

        entity.setId(UUID.randomUUID().toString());
        entity.setEnabled(true);
        entity.setRole("ROLE_USER");

        String token = jwtProvider.generateToken(entity);
        System.out.println("token = " + token);

        User save = userRepository.save(entity);
        System.out.println("save = " + save);

        return ResponseEntity.status(HttpStatus.CREATED).body("Succesfully");

    }

}
