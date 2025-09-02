package ua.mike.sso.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.mike.sso.config.security.AuthUser;
import ua.mike.sso.service.AuthService;

@Controller
@RequiredArgsConstructor
@RequestMapping({"/", ""})
public class AuthController {

    private final AuthService authService;

    @GetMapping
    public String login() {
        return authService.loginForm();
    }

    @GetMapping("logout")
    public String logout() {
        return authService.logout();
    }

    @GetMapping("mfa")
    public String mfaForm(@AuthenticationPrincipal AuthUser principal, Model model) {
        return authService.mfaForm(principal, model);
    }

    @PostMapping("mfa")
    @Transactional
    public String mfaVerify(@AuthenticationPrincipal AuthUser principal, @RequestParam int code, Model model) {
        return authService.mfaVerifyForm(principal, code, model);
    }

    @GetMapping("info")
    public String info(@AuthenticationPrincipal AuthUser principal, Model model) {
        return authService.infoForm(principal.email(), model);
    }
}