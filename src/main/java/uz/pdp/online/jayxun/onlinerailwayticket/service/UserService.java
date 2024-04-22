package uz.pdp.online.jayxun.onlinerailwayticket.service;

import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.custom.SendMailDto;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.entityDtoWithoutId.ConfirmSentCodeDto;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.entityDtoWithoutId.ConfirmSentCodeResDto;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.entityDtoWithoutId.UserDto;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.request.auth.LoginReqDto;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.request.auth.SignUpConfirmDto;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.request.auth.SignUpReqDto;
import uz.pdp.online.jayxun.onlinerailwayticket.entity.ConfirmSentCode;
import uz.pdp.online.jayxun.onlinerailwayticket.entity.User;
import uz.pdp.online.jayxun.onlinerailwayticket.jwt.JwtProvider;
import uz.pdp.online.jayxun.onlinerailwayticket.mapper.ConfirmSentCodeMapper;
import uz.pdp.online.jayxun.onlinerailwayticket.mapper.UserMapper;
import uz.pdp.online.jayxun.onlinerailwayticket.repo.ConfirmSentCodeRepository;
import uz.pdp.online.jayxun.onlinerailwayticket.repo.UserRepository;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountExpiredException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ConfirmSentCodeRepository confirmSentCodeRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final MailingService mailingService;
    private final GenerateService generateService;
    private final JwtProvider jwtProvider;


    @Value("${spring.security.code.sign.up.expire-minute}")
    private int codeExpire;
    private final ConfirmSentCodeMapper confirmSentCodeMapper;
//    private final

    private User getByEmail(String email) throws AccountNotFoundException {

            Optional<User> byEmailIgnoreCase = userRepository.findByEmailIgnoreCase(email);
            if (byEmailIgnoreCase.isPresent()) {
                return byEmailIgnoreCase.get();
            }
            else {
                throw new AccountNotFoundException("User not found");
            }
    }

    public ConfirmSentCodeResDto registerUser(SignUpReqDto signUpReqDto, HttpServletRequest httpServletRequest) throws AccountException {

        if (!signUpReqDto.getCurrent_password().equals(signUpReqDto.getRepeat_password())) {
            throw new AccountException("Passwords do not match");
        }

        User entity = userMapper.toEntity(signUpReqDto);

        if (userRepository.existsByEmailIgnoreCase(signUpReqDto.getEmail())) {
            throw new AccountException("User Already exists");
        }

        ConfirmSentCodeDto confirmSentCodeDto = generateService.generateCodeAndSaveAndReturnDto(signUpReqDto, codeExpire);


        try {
            mailingService.sendMail(SendMailDto.builder()
                    .to(signUpReqDto.getEmail())
                    .subject("Confirm Account (Online Railway Booking)")
                    .content(generateService.generateCodeWebPage(confirmSentCodeDto,httpServletRequest))
                    .build());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return ConfirmSentCodeResDto
                .builder()
                .token(confirmSentCodeDto.getToken())
                .expire(confirmSentCodeDto.getExpire())
                .expiredMinute(confirmSentCodeDto.getExpireMinuteValue())
                .build();

    }

    public UserDto loginUserAndGetDto(LoginReqDto loginReqDto) throws AccountNotFoundException {

        User user = getByEmail(loginReqDto.getEmail());

        boolean matches = passwordEncoder.matches(loginReqDto.getPassword(), user.getPassword());
        if (!matches) {
            throw new AccountNotFoundException("Username or password incorrect");
        }
        if (!user.getEnabled()) {
            throw new AccountNotFoundException("User not found");
        }
        return userMapper.toDto(user);
    }

    public boolean confirmUser(SignUpConfirmDto signUpReqDto) throws AccountException {


        Optional<ConfirmSentCode> byToken = confirmSentCodeRepository.findByToken(signUpReqDto.getToken());
        if (byToken.isEmpty()) {
            throw new AccountException("Try again");
        }

        ConfirmSentCode confirmSentCode = byToken.get();

        if (!passwordEncoder.matches(signUpReqDto.getCode(), confirmSentCode.getCode())) {
            System.out.println("signUpReqDto.getCode() = " + signUpReqDto.getCode());
            System.out.println("confirmSentCode.getPassword() = " + confirmSentCode.getPassword());
            throw new AccountException("Wrong password");
        }

        Claims parse = jwtProvider.parse(signUpReqDto.getToken());
        String email = parse.getSubject();
        System.out.println("email = " + email);

        boolean after = byToken.get().getExpire().after(new Date());

        System.out.println("after = " + after);
        if (after){
            User user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setEmail(email);
            user.setRole(byToken.get().getRole());
            user.setEnabled(true);
            user.setPassword(byToken.get().getPassword());
            User save = userRepository.save(user);
        } else {
            throw new AccountExpiredException("Account has expired");
        }


        return true;
    }
}
