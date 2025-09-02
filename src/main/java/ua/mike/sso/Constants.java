package ua.mike.sso;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static class Redirect {
        public static final String INFO = "redirect:/info";
        public static final String LOGOUT = "redirect:/login?logout";
    }

    public static class Form {
        public static final String LOGIN = "login";
        public static final String MFA = "mfa";
        public static final String INFO = "info";
    }
}
