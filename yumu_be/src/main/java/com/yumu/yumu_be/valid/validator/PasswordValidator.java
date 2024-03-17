package com.yumu.yumu_be.valid.validator;

import com.yumu.yumu_be.valid.annotation.PasswordCheck;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<PasswordCheck, String> {

    private static final String regexUpper = ".*[A-Z].*";
    private static final String regexLower = ".*[a-z].*";
    private static final String regexNumber = ".*[0-9].*";
    private static final String regexSymbol = ".*[`!@#$%^*/-/=_+~?\\\\\\\";\\\\\\\\|{}'<>?///(/)].*";


    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        //비밀번호 형식이 유효한지 확인하는 로직
        int idx = 0;

        if (Pattern.matches(regexUpper, password)) {
            idx += 1;
        }

        if (Pattern.matches(regexLower, password)) {
            idx += 1;
        }

        if (Pattern.matches(regexNumber, password)) {
            idx += 1;
        }

        if (Pattern.matches(regexSymbol, password)){
            idx += 1;
        }

        return idx >= 2 && password.length() >=6 && password.length() <= 16;

    }
}
