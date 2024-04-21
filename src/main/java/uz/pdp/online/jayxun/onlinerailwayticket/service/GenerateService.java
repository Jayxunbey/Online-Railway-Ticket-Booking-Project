package uz.pdp.online.jayxun.onlinerailwayticket.service;

import lombok.RequiredArgsConstructor;
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

    public String generateCodeWebPage(ConfirmSentCode confirmSentCode) {

        return new String(confirmSentCode.getCode());

    }

    public ConfirmSentCode generateCodeAndSaveAndReturnDto(SignUpReqDto signUpReqDto, int codeExpireMinute) {

        String token = jwtProvider.generateTokenForOtherWork(signUpReqDto.getEmail(), codeExpireMinute);

        String confirmationCode = generateRandomCode(6);
        String encode = passwordEncoder.encode(signUpReqDto.getCurrent_password());

        ConfirmSentCode confirmSentCode = new ConfirmSentCode();
        confirmSentCode.setCode(confirmationCode);
        confirmSentCode.setToken(token);
        confirmSentCode.setExpire(new Date(System.currentTimeMillis() + codeExpireMinute * 60 * 1000));
        confirmSentCode.setPassword(encode);
        confirmSentCode.setRole("ROLE_USER");

        codeRepository.save(confirmSentCode);
        return confirmSentCode;

    }

    private String generateRandomCode(int numberLength) {
        int i = (int) (Math.random() * Math.pow(10, numberLength+1));

        String substring = String.valueOf(i).substring(1);

        int repeat_i = numberLength- substring.length();


        for (int i1 = 1; i1 <= repeat_i; i1++) {
            substring = "0"+substring;
        }

        return substring;
    }
}
