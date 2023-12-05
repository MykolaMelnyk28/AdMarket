package com.api.admarket.api.v1.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

public class PasswordConfirmValidator implements ConstraintValidator<PasswordConfirm, Object> {

    private String password;
    private String passwordConfirm;

    @Override
    public void initialize(PasswordConfirm passwordConfirm) {
        this.password = passwordConfirm.password();
        this.passwordConfirm = passwordConfirm.passwordConfirm();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }

        Object passwordValue = new BeanWrapperImpl(value).getPropertyValue(password);
        Object passwordConfirmValue = new BeanWrapperImpl(value).getPropertyValue(passwordConfirm);

        return (passwordValue != null && passwordValue.equals(passwordConfirmValue))
                || (passwordValue == null && passwordConfirmValue == null);
    }
}

