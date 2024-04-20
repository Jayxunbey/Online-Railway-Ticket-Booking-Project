package uz.pdp.online.jayxun.onlinerailwayticket.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link uz.pdp.online.jayxun.onlinerailwayticket.entity.User}
 */
@Value
@ToString
public class SignUpReqDto implements Serializable {
    @Email(message = "Email is invalid")
    String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "password must be minimum 8 characters, at least one letter and one number")
    String current_password;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "password must be minimum 8 characters, at least one letter and one number")
    String repeat_password;

}