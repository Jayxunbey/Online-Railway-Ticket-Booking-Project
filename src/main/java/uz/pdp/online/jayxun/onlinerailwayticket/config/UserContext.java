package uz.pdp.online.jayxun.onlinerailwayticket.config;

import io.jsonwebtoken.Claims;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static io.jsonwebtoken.Jwts.claims;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserContext implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private String roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(roles));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPropertiesByClaims(final Claims claims) {
        this.id = claims.get("id", Long.class);
        this.username = claims.getSubject();
        this.roles = claims.get("role", String.class);


    }
}
