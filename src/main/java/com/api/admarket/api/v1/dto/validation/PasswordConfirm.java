package com.api.admarket.api.v1.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = PasswordConfirmValidator.class)
@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConfirm {
    String password() default "password";
    String passwordConfirm() default "passwordConfirm";

    String message() default "Passwords not matches";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
