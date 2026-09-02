package com.ibrahimahmads.github.vbooth.utils.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SlugValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSlug {
    String message() default "Slug ini tidak diizinkan atau menggunakan kata sistem";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
