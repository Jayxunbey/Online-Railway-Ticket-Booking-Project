package uz.pdp.online.jayxun.onlinerailwayticket.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Value;

/**
 * DTO for {@link uz.pdp.online.jayxun.onlinerailwayticket.entity.User}
 */

@Value
public class SignUpConfirmDto {

    @NotNull
    String token;

    @NotNull
    String code;

}
