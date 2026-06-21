package com.fernandoruiz.app.management.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class LegacyCompatiblePasswordEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
    private final PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        return delegatingPasswordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String storedPassword) {
        if (storedPassword == null || storedPassword.isBlank()) {
            return false;
        }

        if (hasEncodingPrefix(storedPassword)) {
            return delegatingPasswordEncoder.matches(rawPassword, storedPassword);
        }

        if (isBcryptHash(storedPassword)) {
            return bcryptPasswordEncoder.matches(rawPassword, storedPassword);
        }

        return storedPassword.contentEquals(rawPassword);
    }

    private boolean hasEncodingPrefix(String value) {
        return value.startsWith("{") && value.contains("}");
    }

    private boolean isBcryptHash(String value) {
        return value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$");
    }
}
