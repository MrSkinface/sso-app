package ua.mike.sso.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.IGoogleAuthenticator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ua.mike.sso.Constants;
import ua.mike.sso.config.security.AuthUser;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import static com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AdminService adminService;
    private final IGoogleAuthenticator authenticator;

    public String loginForm() {
        return Constants.Form.LOGIN;
    }

    public String mfaForm(AuthUser principal, Model model) {
        var admin = adminService.getByEmail(principal.email());
        model.addAttribute("mfaEnabled", admin.isMfaEnabled());
        model.addAttribute("qr", generateQrBase64(admin.getEmail(), admin.getMfaSecret()));
        return Constants.Form.MFA;
    }

    public String mfaVerifyForm(AuthUser principal, int code, Model model) {
        var admin = adminService.getByEmail(principal.email());
        if (authenticator.authorize(admin.getMfaSecret(), code)) {
            admin.setMfaEnabled(true);
            return Constants.Redirect.INFO;
        } else {
            model.addAttribute("error", "Invalid code");
            return mfaForm(principal, model);
        }
    }

    public String infoForm(String email, Model model) {
        model.addAttribute("admin", adminService.getByEmail(email));
        return Constants.Form.INFO;
    }

    public String logout() {
        return Constants.Redirect.LOGOUT;
    }

    @SneakyThrows
    private String generateQrBase64(String email, String secret) {

        var totpUrl = getOtpAuthTotpURL("Mike's SSO App", email, new GoogleAuthenticatorKey.Builder(secret).build());
        var qrCodeWriter = new QRCodeWriter();
        var bitMatrix = qrCodeWriter.encode(totpUrl, BarcodeFormat.QR_CODE, 200, 200, Map.of(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8));

        try (var outputStream = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        }
    }
}