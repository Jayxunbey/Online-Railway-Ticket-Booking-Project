package uz.pdp.online.jayxun.onlinerailwayticket.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.entityDtoWithoutId.ConfirmSentCodeDto;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.request.auth.SignUpReqDto;
import uz.pdp.online.jayxun.onlinerailwayticket.entity.ConfirmSentCode;
import uz.pdp.online.jayxun.onlinerailwayticket.jwt.JwtProvider;
import uz.pdp.online.jayxun.onlinerailwayticket.repo.ConfirmSentCodeRepository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class GenerateService {

    private final JwtProvider jwtProvider;
    private final ConfirmSentCodeRepository codeRepository;
//    private final PasswordEncoder passwordEncoder;
    private final Random random;

    @Value("${spring.security.code.sign.up.length}")
    private int randomNumberLength;

    public String generateCodeWebPage(ConfirmSentCodeDto confirmSentCodeDto, HttpServletRequest request) {

        String platformOs = request.getHeader("sec-ch-ua-platform");
        String remoteAddr = request.getRemoteAddr();
        String browserFullData = request.getHeader("sec-ch-ua");
        String confirmCode = confirmSentCodeDto.getConfirmCode();
        int expireMinuteValue = confirmSentCodeDto.getExpireMinuteValue();

        ///////////////////////////////////////////////////////////////

        if(platformOs==null || platformOs.isEmpty()) platformOs ="unknown";
        if(remoteAddr==null || remoteAddr.isEmpty()) remoteAddr ="unknown";
        if(browserFullData==null || browserFullData.isEmpty()) browserFullData ="unknown";


        String patternDate = "EEE, MMMM d, yyyy";
        String patternTime = "hh:mm:ss a (Z)";

        DateFormat dateFormat = new SimpleDateFormat(patternDate);
        DateFormat timeFormat = new SimpleDateFormat(patternTime);

        Date today = Calendar.getInstance().getTime();
//
        String formatDate = dateFormat.format(today);
        String formatTime = timeFormat.format(today);

        String templateHtml;

        try {
            FileInputStream fileInputStream = new FileInputStream("F:/PdpAcademy/modul9/resourse/html/confirmation_code_template.html");
            byte[] string1 = fileInputStream.readAllBytes();
            fileInputStream.close();

            templateHtml = new String(string1);

            templateHtml = templateHtml.replace("full_information_browser_dinamic", browserFullData)
                    .replace("ip_address_client_dinamic", "")
                    .replace("full_information_browser_dinamic", browserFullData)
                    .replace("os_information_client_dinamic", platformOs)
                    .replace("time_day_month_year_client_dinamic", formatDate)
                    .replace("time_clock_gmt_dinamic", formatTime)
                    .replace("confirmation_code_place_holder_dinamic", confirmCode)
                    .replace("expire_minute_place_holder_dinamic", String.valueOf(expireMinuteValue));

            FileWriter fileWriter = new FileWriter("F:/PdpAcademy/modul9/resourse/html/confirmation_code_template_generated.html");
            fileWriter.write(templateHtml);
            fileWriter.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //////////////////////////////////////////////////////////////


        return templateHtml;
    }

    public ConfirmSentCodeDto generateCodeAndSaveAndReturnDto(SignUpReqDto signUpReqDto, int codeExpireMinute) {

        String token = jwtProvider.generateTokenForOtherWork(signUpReqDto.getEmail(), codeExpireMinute);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String confirmationCode = generateRandomCode(randomNumberLength);
        String encodedConfirmationCode = passwordEncoder.encode(confirmationCode);

        String encode = passwordEncoder.encode(signUpReqDto.getCurrent_password());

        ConfirmSentCode confirmSentCode = new ConfirmSentCode();
        confirmSentCode.setCode(encodedConfirmationCode);
        confirmSentCode.setToken(token);

        Date expiration = new Date(System.currentTimeMillis() + (long) codeExpireMinute * 60 * 1000);
        confirmSentCode.setExpire(expiration);

        confirmSentCode.setPassword(encode);
        confirmSentCode.setRole("ROLE_USER");

        codeRepository.save(confirmSentCode);

        return ConfirmSentCodeDto.builder()
                .token(token)
                .confirmCode(confirmationCode)
                .expire(expiration)
                .expireMinuteValue(codeExpireMinute)
                .build();

//        return confirmSentCode;
    }

    private String generateRandomCode(int numberLength) {
        int randomNumber = random.nextInt((int) Math.pow(10, numberLength-1), (int) Math.pow(10, numberLength ));
        System.out.println("randomNumber = " + randomNumber);
        return String.valueOf(randomNumber);
    }
}
