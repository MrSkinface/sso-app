package ua.mike.sso.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import ua.mike.sso.Constants;
import ua.mike.sso.service.AdminService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AdminService adminService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/" + Constants.Form.INFO).authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/" + Constants.Form.LOGIN)
                        .successHandler(ssoSuccessHandler()))
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll());
        http.csrf(AbstractHttpConfigurer::disable);
        http.headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
        );

        return http.build();
    }

    private AuthenticationSuccessHandler ssoSuccessHandler() {
        return (request, response, authentication) -> {
            OidcUser user = (OidcUser) authentication.getPrincipal();
            adminService.getOrCreate(user);
            response.sendRedirect("/" + Constants.Form.MFA);
        };
    }
}