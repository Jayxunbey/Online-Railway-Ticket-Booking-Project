package uz.pdp.online.jayxun.onlinerailwayticket.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "confirm_sent_code")
public class ConfirmSentCode {
    @Id
    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "code")
    private String code;

    @Column(name = "expire")
    private Date expire;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "role", nullable = false)
    private String role;

}