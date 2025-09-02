package ua.mike.sso.service;

import com.warrenstrange.googleauth.IGoogleAuthenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.mike.sso.data.entities.Admin;
import ua.mike.sso.data.repository.AdminRepository;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final IGoogleAuthenticator authenticator;

    public Admin getByEmail(String email) {
        return adminRepository.findByEmail(email).orElseThrow();
    }

    @Transactional
    public Admin getOrCreate(OidcUser user) {
        return adminRepository.findByEmail(user.getEmail())
                .orElseGet(() -> adminRepository.save(Admin.builder()
                        .name(user.getGivenName() + " " + user.getFamilyName())
                        .email(user.getEmail())
                        .mfaSecret(authenticator.createCredentials().getKey())
                        .build()));
    }
}