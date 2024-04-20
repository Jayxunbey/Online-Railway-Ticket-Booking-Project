package uz.pdp.online.jayxun.onlinerailwayticket.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link uz.pdp.online.jayxun.onlinerailwayticket.entity.User}
 */

@Value
@ToString
public class LoginReqDto implements Serializable {
    @Email(message = "Email is invalid")
    String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "password must be minimum 8 characters, at least one letter and one number")
    String password;

    @NotNull(message = "remember me not be null")
    boolean remember_me;

}