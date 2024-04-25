package uz.pdp.online.jayxun.onlinerailwayticket.jwt;


import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.pdp.online.jayxun.onlinerailwayticket.entity.User;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;
    private final JwtService jwtService;
    RequestAttributeSecurityContextRepository repository = new RequestAttributeSecurityContextRepository();


    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        filterChain.doFilter(request, response);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        try {

            HttpServletRequest request = (HttpServletRequest) servletRequest;

            String header = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (header == null || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
            }

            String token = header.replace("Bearer ", "");

            Claims parse = jwtProvider.parse(token);

            if (jwtService.isExpired(parse)) {
                throw new AccountExpiredException("Expired token");
            }

            String email = parse.getSubject();

            User user;
            try {
                user = jwtService.getUserIfAvailable(parse);
            } catch (AccountNotFoundException e) {
                throw new RuntimeException(e);
            }

            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            email, null,
                            List.of(new SimpleGrantedAuthority(user.getRole()))));

//            this.repository.saveContext(context, request, response);

        }catch( Exception e )
        {
            logger.error(e.getMessage());
        }

    }
}
