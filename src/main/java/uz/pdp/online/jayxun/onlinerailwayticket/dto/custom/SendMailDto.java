package uz.pdp.online.jayxun.onlinerailwayticket.dto.custom;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class SendMailDto {
    private String to;
    private String subject;
    private String content;
}
