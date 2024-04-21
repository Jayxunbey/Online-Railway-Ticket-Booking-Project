package uz.pdp.online.jayxun.onlinerailwayticket.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.request.auth.SignUpReqDto;
import uz.pdp.online.jayxun.onlinerailwayticket.entity.ConfirmSentCode;
import uz.pdp.online.jayxun.onlinerailwayticket.jwt.JwtProvider;
import uz.pdp.online.jayxun.onlinerailwayticket.repo.ConfirmSentCodeRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class GenerateService {

    private final JwtProvider jwtProvider;
    private final ConfirmSentCodeRepository codeRepository;
    private final PasswordEncoder passwordEncoder;
    private final Random random;

    @Value("${spring.security.code.sign.up.length}")
    private int randomNumberLength;

    public String generateCodeWebPage(ConfirmSentCode confirmSentCode, HttpServletRequest request) {
        return confirmSentCode.getCode();
    }

    public ConfirmSentCode generateCodeAndSaveAndReturnDto(SignUpReqDto signUpReqDto, int codeExpireMinute) {

        String token = jwtProvider.generateTokenForOtherWork(signUpReqDto.getEmail(), codeExpireMinute);

        String confirmationCode = generateRandomCode(randomNumberLength);
        String encode = passwordEncoder.encode(signUpReqDto.getCurrent_password());

        ConfirmSentCode confirmSentCode = new ConfirmSentCode();
        confirmSentCode.setCode(confirmationCode);
        confirmSentCode.setToken(token);
        confirmSentCode.setExpire(new Date(System.currentTimeMillis() + (long) codeExpireMinute * 60 * 1000));
        confirmSentCode.setPassword(encode);
        confirmSentCode.setRole("ROLE_USER");
        codeRepository.save(confirmSentCode);
        return confirmSentCode;
    }

    private String generateRandomCode(int numberLength) {
        int randomNumber = random.nextInt((int) Math.pow(10, numberLength-1), (int) Math.pow(10, numberLength ));
        System.out.println("randomNumber = " + randomNumber);
        return String.valueOf(randomNumber);
    }
}
