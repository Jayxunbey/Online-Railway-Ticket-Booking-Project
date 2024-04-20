package uz.pdp.online.jayxun.onlinerailwayticket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.entityDtoWithoutId.UserDto;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.request.auth.LoginReqDto;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.request.auth.SignUpReqDto;
import uz.pdp.online.jayxun.onlinerailwayticket.entity.User;
import uz.pdp.online.jayxun.onlinerailwayticket.mapper.UserMapper;
import uz.pdp.online.jayxun.onlinerailwayticket.repo.UserRepository;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
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

    public void registerUser(SignUpReqDto signUpReqDto) throws AccountException {

        if (!signUpReqDto.getCurrent_password().equals(signUpReqDto.getRepeat_password())) {
            throw new AccountException("Passwords do not match");
        }

        User entity = userMapper.toEntity(signUpReqDto);

        if (userRepository.existsByEmailIgnoreCase(signUpReqDto.getEmail())) {
            throw new AccountException("User Already exists");
        }

        String encode = passwordEncoder.encode(signUpReqDto.getCurrent_password());

        entity.setId(UUID.randomUUID().toString());
        entity.setEmail(entity.getEmail().toLowerCase());
        entity.setPassword(encode);
        entity.setEnabled(true);
        entity.setRole("ROLE_USER");

        System.out.println("entity.getEmail().toLowerCase() = " + entity.getEmail().toLowerCase());
        System.out.println("encode = " + encode);

        User save = userRepository.save(entity);
        System.out.println(this.getClass().toString()+" //: "+"save = " + save);
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
}
