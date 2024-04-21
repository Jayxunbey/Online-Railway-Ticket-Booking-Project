package uz.pdp.online.jayxun.onlinerailwayticket.dto.entityDtoWithoutId;

import lombok.Value;
import uz.pdp.online.jayxun.onlinerailwayticket.entity.ConfirmSentCode;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link ConfirmSentCode}
 */
@Value
public class ConfirmSentCodeDto implements Serializable {
    String token;
    Date expire;
}