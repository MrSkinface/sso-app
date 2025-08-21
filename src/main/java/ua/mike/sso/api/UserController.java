package ua.mike.sso.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.mike.sso.config.security.AuthUser;

@Slf4j
@RestController
public class UserController {

    @GetMapping("/")
    public String home() {
        return "<a href='/user'>Login</a>";
    }

    @GetMapping("/user")
    public AuthUser user(@AuthenticationPrincipal AuthUser principal) {
        log.debug("AuthUser: {}", principal);
        return principal;
    }
}