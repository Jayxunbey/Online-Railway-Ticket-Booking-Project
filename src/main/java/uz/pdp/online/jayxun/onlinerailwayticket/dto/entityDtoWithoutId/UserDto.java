package uz.pdp.online.jayxun.onlinerailwayticket.dto.entityDtoWithoutId;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link uz.pdp.online.jayxun.onlinerailwayticket.entity.User}
 */
@Value
public class UserDto implements Serializable {
    @NotNull
    @Size(max = 255)
    String email;

    @NotNull
    @Size(max = 255)
    String role;
    @NotNull
    Boolean enabled;
}