package ua.mike.sso.config.security;

import lombok.Builder;

@Builder
public record AuthUser(String name, String email) { }