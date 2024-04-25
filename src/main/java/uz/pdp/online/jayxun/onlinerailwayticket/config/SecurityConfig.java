package uz.pdp.online.jayxun.onlinerailwayticket.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.context.annotation.SessionScope;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.web.ErrorResponse;
import uz.pdp.online.jayxun.onlinerailwayticket.jwt.JwtFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final ObjectMapper mapper;


    private final String[] WHITE_LIST =
            {
                    "/auth/**"
            };




    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(config ->
                        config.requestMatchers(WHITE_LIST).permitAll()
                                .anyRequest().authenticated())
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    };

//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails admin = new User("admin", "1234", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
//        UserDetails user = new User("user", "1234", List.of(new SimpleGrantedAuthority("ROLE_USER")));
//        return new InMemoryUserDetailsManager(admin, user);
//    }

//    @Bean
//    public AuthenticationEntryPoint authenticationEntryPoint() {
//        return ((request, response, authException) -> {
//            response.setContentType("application/json");
//            ErrorResponse build = ErrorResponse.builder()
//                    .path(request.getRequestURI())
//                    .errorMessage(authException.getMessage())
//                    .errorCode(HttpStatus.UNAUTHORIZED.name())
//                    .status(HttpStatus.UNAUTHORIZED.value())
//                    .build();
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            response.getWriter().write(mapper.writeValueAsString(build));
//        });
//    }


//    @Bean
//    public UserContext userContext() {
//        return new UserContext();
//    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
