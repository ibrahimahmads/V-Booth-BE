package com.ibrahimahmads.github.vbooth.utils.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class SlugValidator implements ConstraintValidator<ValidSlug, String> {
    private static final Set<String> RESERVED_SLUGS = Set.of(
            "admin", "api", "auth", "login", "register", "logout",
            "dashboard", "settings", "profile", "account",
            "create", "new", "edit", "update", "delete",
            "event", "events", "booth", "vbooth",
            "terms", "privacy", "help", "support", "faq",
            "null", "undefined", "public", "static", "assets"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        return !RESERVED_SLUGS.contains(value.toLowerCase().trim());
    }
}
