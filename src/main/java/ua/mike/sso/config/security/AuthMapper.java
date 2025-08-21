package ua.mike.sso.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public AuthUser map(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof OidcUser user) {
            return AuthUser.builder()
                    .name(user.getGivenName() + " " + user.getFamilyName())
                    .email(user.getEmail())
                    .provider(Provider.GOOGLE)
                    .build();
        }

        if (principal instanceof User user) {
            return AuthUser.builder()
                    .name(user.getUsername())
                    .provider(Provider.RAW)
                    .build();
        }

        return null;
    }
}